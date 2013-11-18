package sxyml;

import java.io.BufferedReader;
import java.io.IOException;

public class TokenReader {
	private BufferedReader inputFile;
	private String currentLine;
	private int currentLineNumber;
	private String[] tokens;
	private int currentTokenPosition;
	private boolean eol;
	
	private int currentColumnPosition;
	private Token currentToken;
	
	private String delimiterRegex(String[] delims) {
		String pattern = "";
		pattern = "(?<=\\\\)(?<=\\.)|";
		for (String delim: delims) {
			pattern += "(?<=" + delim + ")|(?=" + delim + ")|";
		}
		return pattern.substring(0, pattern.length() - 1); //Remove the last |(pipe)
	}
	
	public TokenReader(BufferedReader inputFile) {
		this.inputFile = inputFile;
		eol = true;
		currentTokenPosition = 0;
		currentLineNumber = 0;
		tokens = new String[0];
	}
	
	public Token nextToken() throws IOException {
		if (currentTokenPosition == tokens.length) {
			currentLine = inputFile.readLine();
			if (currentLine == null)
				return null;
			tokens = currentLine.split(delimiterRegex(new String[]{
				"\\@",
				"\\{",
				"\\s",
				"\\\"",
				"\\}",
				"\\:",
				"\\\\"
			}));
			currentLineNumber++;
			currentColumnPosition = 0;
			currentTokenPosition = 0;
		}
		
		
		currentTokenPosition++;
		currentColumnPosition+=tokens[currentTokenPosition-1].length();
		currentToken = new Token(tokens[currentTokenPosition-1], currentLineNumber, currentColumnPosition);
		return currentToken;
	}
	
	public Token currentToken() {
		return currentToken;
	}
	
	public class Token {
		private int column;
		private int line;
		private String value;
		
		public Token(String tokenValue, int tokenLine, int tokenColumn) {
			column = tokenColumn;
			line = tokenLine;
			
			value = tokenValue;
		}
		
		/**
		 * Get the value of this token
		 * @return The value of this token
		 * */
		public String value() {
			return value;
		}
		
		/**
		 * Get the line position of this token
		 * @return
		 */
		public int line() {
			return line;
		}
		
		/**
		 * Get the line column of this token
		 * @return
		 */
		public int column() {
			return column;
		}
	}
}
