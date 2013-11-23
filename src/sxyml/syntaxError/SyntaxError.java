package sxyml.syntaxError;

import sxyml.TokenReader.Token;

public class SyntaxError extends RuntimeException{
	private static final long serialVersionUID = -5372148445920906901L;

	public SyntaxError(String message, Token token) {
		super(message + " on line:" + token.line() + " column:" + token.column());
	}
}
