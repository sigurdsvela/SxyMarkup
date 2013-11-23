package sxyml;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import sxyml.syntaxError.ExpectedGot;
import sxyml.syntaxError.SyntaxError;
import sxyml.syntaxError.UnexpectedToken;

public class SXYML {
	
	private enum STATE {
		InsideTag, //When Textnodes may appear
		DefiningTagType, //After an @
		DefiningAttributes,
		DefiningAttributeValue,
		PreAttributeColonSpace
	}
	
//	private static void println(String msg) {
//		System.out.println(msg);
//	}
	
	public static boolean isWhiteSpace(String string) {
		return (string.matches("\\s*"));
	}
	
	public static Element parseFile(BufferedReader file) throws IOException {
		TokenReader tokens = new TokenReader(file);

		
		STATE state = STATE.InsideTag;
		
		Element rootNode    = null;
		Element currentNode = null;
		TextNode currentTextNode = null;

		/**
		 * The current attribute key being defined. Null if none
		 */
		String definingAttrKey = null;
		
		/**
		 * The current attribute value being defined. Null if none
		 */
		String definingAttrValue = null;
		
		/**
		 * Are we currently inside a quote?
		 */
		boolean insideQuote = false;
		
		/**
		 * Is the current token escaped?
		 */
		boolean escape = false;
		
		TokenReader.Token token;
		
		while ((token = tokens.nextToken()) != null) {
			if (token.value().compareTo("\\") == 0 && !escape) {
				escape = true;
				token = tokens.nextToken(); //Skip this token, it's just an escape character
			} else {
				escape = false;
			}
			
			if (token.value().compareTo("\"") == 0 && !escape) {
				insideQuote = !insideQuote;
				continue;
			}
			
			
			//System.out.println(state);
			
			switch (state) {
			
				case InsideTag:
					if (token.value().compareTo("}")==0 && !escape) {
						currentNode = currentNode.getParrent();
						currentTextNode = null;
					} else if (token.value().compareTo("@") == 0 && !escape) {
						state = STATE.DefiningTagType;
						currentTextNode = null;
					} else if (token.value().matches("\\s*") && currentTextNode == null) {
						//Ignore Whitespaces when not a textnode
					} else {
						if (currentNode == null) {
							throw new SyntaxError("Text is not allowed outside the root node.", token);
						}
						
						if (currentTextNode == null) {
							currentTextNode = currentNode.addTextNode(token.value());
						} else {
							currentTextNode.pushContent(token.value());
						}
					}
					break;
			
					
					
				case DefiningTagType:
					if (token.value().matches("\\w+") || escape) {
						Element temp = new Element(token.value());
						if (rootNode == null) {
							rootNode = temp;
							currentNode = rootNode;
						} else {
							currentNode.addElement(temp);
							currentNode = temp;
						}
						state = STATE.DefiningAttributes;
					}
					break;
					
					
					
				case DefiningAttributes:
					if (token.value().matches("\\w+") || escape) {
						if (definingAttrKey == null)
							definingAttrKey = "";
						definingAttrKey += token.value(); //In case one key get slit up into several tokens. Can't happen now as its must match \\w+, but you know, for the future
						
						
					} else if (token.value().compareTo(":") == 0) {
						if (definingAttrKey == null) {
							throw new ExpectedGot("attribute key", token);
						} else {
							definingAttrValue = null;
							state = STATE.DefiningAttributeValue;
						}
					
					
					} else if (token.value().matches("\\s+")) {
						if (definingAttrKey != null) {
							state = STATE.PreAttributeColonSpace;
						} //Else ignore
						
					
					} else if (token.value().compareTo("{") == 0 || token.value().compareTo(";") == 0) {
						//TODO If the current defining attribute is not set, it is a simple syntax attribute
						state = STATE.InsideTag;
						if (definingAttrKey != null) {
							currentNode.addToAttribute(definingAttrKey, definingAttrKey);
						}
						definingAttrValue = "";
						definingAttrKey = null;
						if (token.value().compareTo(";") == 0) {
							currentNode.setVoid();
							currentNode = currentNode.getParrent();
						}
					
					
					} else {
						throw new UnexpectedToken(token);
					}
					break;
					
					
				case PreAttributeColonSpace: // from the pipes:  @div attr | :| value
					if (token.value().matches("\\w+") || escape) {
						currentNode.addToAttribute(definingAttrKey, definingAttrKey); //Simple syntax for the previous one
						definingAttrKey = token.value();
						state = STATE.DefiningAttributes;
					} else if (token.value().matches("\\s+")) {
						//Ignore Spaces
					} else if (token.value().compareTo(";") == 0 || token.value().compareTo("{") == 0) {
						state = STATE.InsideTag;
						currentNode.addToAttribute(definingAttrKey, definingAttrKey); //Simple syntax attribute
						definingAttrKey = null;
						if (token.value().compareTo(";") == 0) {
							currentNode.setVoid();
							currentNode = currentNode.getParrent();
						}
					} else if (token.value().compareTo(":") == 0) {
						definingAttrValue = null;
						state = STATE.DefiningAttributeValue;
					} else {
						throw new UnexpectedToken(token);
					}
					
					break;
				
					
					
				case DefiningAttributeValue:
					if (!insideQuote && definingAttrValue == null && token.value().matches("\\s*")) //IF we havent started to declare the value yet, ignore spaces
						continue;
					
					if (insideQuote) {
						if (definingAttrValue == null)
							definingAttrValue = "";
						definingAttrValue += token.value();
					
					
					} else if (token.value().matches("\\s*") || token.value().compareTo("{") == 0 || token.value().compareTo(";") == 0) {
						//This is a very hackish solution duplicating code like this
						//find a better way to be able to pick up on a ; without the " "
						//Delimiter. Might need some major reconstruction
						currentNode.addToAttribute(definingAttrKey, definingAttrValue);
						state = STATE.DefiningAttributes;
						definingAttrKey  = null;
						
						if (token.value().compareTo("{") == 0) {
							state = STATE.InsideTag;
						} else if (token.value().compareTo(";") == 0) {
							state = STATE.InsideTag;
							currentNode.setVoid();
							currentNode = currentNode.getParrent();
						} 
					
					
					} else {
						throw new UnexpectedToken(token);
					}
					break;
			}
		}
		
		return rootNode;
	}
}
