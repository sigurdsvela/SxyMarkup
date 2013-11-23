package sxyml.syntaxError;

import sxyml.TokenReader.Token;

public class ExpectedGot extends SyntaxError{
	
	public ExpectedGot(String excpected, Token token) {
		super("Excpected \"" + excpected + "\", got \"" + token.value() + "\"", token);
	}
}
