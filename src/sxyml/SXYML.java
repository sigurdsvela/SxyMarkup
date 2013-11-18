package sxyml;

import java.io.BufferedReader;
import java.io.IOException;

import com.sun.tools.internal.ws.wsdl.framework.Defining;

public class SXYML {
	
	private enum STATE {
		InsideTag, //When Textnodes may appear
		DefiningTagType, //After an @
		DefiningAttributes
	}
	
	public static boolean isWhiteSpace(String string) {
		return (string.matches("\\s*"));
	}
	
	public static void syntaxError(String error, SXYMLTokenReader.Token token) {
		System.out.println("Syntax error: " + error + " on line " + token.line() + ":" + token.column());
	}
	
	public static SXYMLElement parseFile(BufferedReader file) throws IOException {
		SXYMLTokenReader tokens = new SXYMLTokenReader(file);

		STATE state = STATE.InsideTag;
		
		SXYMLElement rootNode    = null;
		SXYMLElement currentNode = null;

		String definingAttr = "";
		String definingAttrValue = "";
		boolean startQuote = false;
		
		SXYMLTokenReader.Token token;
		
		while ((token = tokens.nextToken()) != null) {
			switch (state) {
			
				case InsideTag:
					if (isWhiteSpace(token.value())) {
					} else if (token.value() == "@") {
						state = STATE.DefiningTagType;
					}
					break;
			
				case DefiningTagType:
					if (token.value().matches("\\w+")) {
						SXYMLElement temp = new SXYMLElement(token.value());
						if (rootNode == null) {
							rootNode = temp;
							currentNode = rootNode;
						} else {
							currentNode.addElement(temp);
							currentNode = temp;
						}
						state = STATE.DefiningAttributes;
					}
					
				case DefiningAttributes:
					
					break;
				
			}
		}
		
		//rootnode.printTree();
		
		return rootNode;
	}
}
