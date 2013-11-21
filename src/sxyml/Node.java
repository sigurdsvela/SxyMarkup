package sxyml;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sxyml.output.*;

public abstract class Node {

	public abstract void print(Output output);
	
	public void print(Output output, int indentSize) {
		throw new NotImplementedException();
	}
	
	public void print(Output output, int startIndent, int indentSize) {
		throw new NotImplementedException();
	}
	
}
