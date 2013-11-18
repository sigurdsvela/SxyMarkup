package sxyml;

import java.io.BufferedReader;
import java.io.IOException;

import com.sun.tools.internal.ws.wsdl.framework.Defining;

public class SXYML {
	
	private enum STATE {
		InsideTag, //When Textnodes may appear
		DefiningTagType, //After an @
		DefiningAttributes,
		DefiningAttributeValue
	}
	
	private static void println(String msg) {
		System.out.println(msg);
	}
	
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

		String definingAttrKey = "";
		String definingAttrValue = "";
		
		/**
		 * Are we currently inside a quote
		 */
		boolean insideQuote = false;
		boolean escape = false;
		
		TokenReader.Token token;
		
		while ((token = tokens.nextToken()) != null) {
			if (!token.value().matches("\\s+")) {
				String lc = (token.line() + ":" + token.column());
				//println(lc + "\t" + token.value());
			}
				
			if (token.value().compareTo("\\") == 0 && !escape) {
				escape = true;
				tokens.nextToken(); //Skip this token, it's just an escape character
			} else {
				escape = false;
			}
			
			if (token.value().compareTo("\"") == 0 && !escape) {
				insideQuote = !insideQuote;
				continue;
			}
			
			
			switch (state) {
			
				case InsideTag:
					println("Inside tag");
					if (token.value().compareTo("}")==0 && !escape) {
						currentNode = currentNode.getParrent();
						currentTextNode = null;
					} else if (token.value().compareTo("@") == 0 && !escape) {
						state = STATE.DefiningTagType;
						currentTextNode = null;
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
					println("DefiningTagType");
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
					println("DefiningAttributes");
					if (token.value().matches("\\w+")) {
						definingAttrKey = token.value();
					} else if (token.value().compareTo(":") == 0) {
						state = STATE.DefiningAttributeValue;
					} else if (token.value().compareTo("{") == 0) {
						state = STATE.InsideTag;
					} else if (isWhiteSpace(token.value())) {
					} else {
						syntaxError("Unregognized token "+ token.value() + "", token);
					}
					break;
					
				case DefiningAttributeValue:
					println("DefiningAttributeValue");
					if (insideQuote) {
						definingAttrValue += token.value();
					} else if (token.value().matches("\\s+")) {
						currentNode.addToAttribute(definingAttrKey, definingAttrValue);
						state = STATE.DefiningAttributes;
					} else {
						syntaxError("Unregognized token \'" + token.value() + "\'", token);
					}
					break;
			}
		}
		
		return rootNode;
	}
}
