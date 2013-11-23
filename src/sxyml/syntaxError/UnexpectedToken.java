package sxyml.syntaxError;

import sxyml.TokenReader.Token;

public class UnexpectedToken extends SyntaxError{
	private static final long serialVersionUID = -5897968891566599870L;

	public UnexpectedToken(Token token) {
		super("Unexpected token \"" + token.value() + "\"", token);
	}
	
}
