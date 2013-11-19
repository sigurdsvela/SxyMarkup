package sxyml;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SXYML {
	
	private enum STATE {
		InsideTag, //When Textnodes may appear
		DefiningTagType, //After an @
		DefiningAttributes,
		DefiningAttributeValue
	}
	
//	private static void println(String msg) {
//		System.out.println(msg);
//	}
	
	public static boolean isWhiteSpace(String string) {
		return (string.matches("\\s*"));
	}
	
	public static void syntaxError(String error, TokenReader.Token token) {
		System.out.println("Syntax error: " + error + " on line " + token.line() + ":" + token.column());
	}
	
	public static Element parseFile(BufferedReader file) throws IOException {
		TokenReader tokens = new TokenReader(file);

		
		STATE state = STATE.InsideTag;
		
		Element rootNode    = null;
		Element currentNode = null;
		TextNode currentTextNode = null;

		String definingAttrKey = null;
		String definingAttrValue = null;
		
		/**
		 * Are we currently inside a quote
		 */
		boolean insideQuote = false;
		boolean escape = false;
		
		HashMap<String, String> definingAttrKeyWithoutValue = new HashMap<String, String>();
		
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
			
			
			switch (state) {
			
				case InsideTag:
					if (token.value().compareTo("}")==0 && !escape) {
						currentNode = currentNode.getParrent();
						currentTextNode = null;
					} else if (token.value().compareTo("@") == 0 && !escape) {
						state = STATE.DefiningTagType;
						currentTextNode = null;
					} else if (token.value().matches("\\s*") && currentTextNode == null) {
						//Ignore Whitespaces
					} else {
						if (currentNode == null) {
							syntaxError("Text is not allowed outside of any element", token);
							continue;
						}
						
						if (currentTextNode == null) {
							currentTextNode = currentNode.addTextNode(token.value());
						} else {
							currentTextNode.pushContent(token.value());
						}
					}
					break;
			
				case DefiningTagType:
					if (token.value().matches("\\w+")) {
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
					if (token.value().matches("\\w+")) {
						if (definingAttrKey == null)
							definingAttrKey = "";
						definingAttrKey += token.value(); //Incase one key get slit up into several tokens.
						
						
					} else if (token.value().compareTo(":") == 0) {
						if (definingAttrKey == null) {
							syntaxError("Expected attribute key, got ':'", token);
						} else {
							if (definingAttrKeyWithoutValue.containsKey(definingAttrKey))
								definingAttrKeyWithoutValue.remove(definingAttrKey);
							state = STATE.DefiningAttributeValue;
						}
					
					
					} else if (token.value().matches("\\s*")) {
						
						
					
					} else if (token.value().compareTo("{") == 0 || token.value().compareTo(";") == 0) {
						state = STATE.InsideTag;
						definingAttrValue = "";
						if (token.value().compareTo(";") == 0) {
							currentNode.setVoid();
							currentNode = currentNode.getParrent();
						}
					
					
					} else {
						syntaxError("Unregognized token "+ token.value() + "", token);
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
						//Delimiter. Might need so major reconstruction
						currentNode.addToAttribute(definingAttrKey, definingAttrValue);
						state = STATE.DefiningAttributes;
						definingAttrValue = null;
						definingAttrKey  = null;
						
						if (token.value().compareTo("{") == 0) {
							state = STATE.InsideTag;
						} else if (token.value().compareTo(";") == 0) {
							state = STATE.InsideTag;
							currentNode.setVoid();
							currentNode = currentNode.getParrent();
						} 
					
					
					} else {
						syntaxError("Unregognized token \'" + token.value() + "\'", token);
					}
					break;
			}
		}
		
		return rootNode;
	}
}
