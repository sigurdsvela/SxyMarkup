package sxyml;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sxyml.output.*;

public abstract class Node {

	public enum Lang {
		HTML(false, true),
		XML(true, true),
		SGML(true, false),
		XHTML(true, true);
		
		boolean endTagsHasName;
		boolean voidClose;
		Lang(boolean voidClose, boolean endTagsHasName) {
			this.voidClose = voidClose;
			this.endTagsHasName = endTagsHasName;
		}
		public boolean voidMustBeClosed() {
			return voidClose;
		}
		public boolean endTagsHasName() {
			return endTagsHasName;
		}
	}
	
	public abstract void print(Output output);
	
	public void print(Output output, int indentSize) {
		print(output, 0, indentSize);
	}
	
	public final void print(Output output, int startIndent, int indentSize) {
		print(output, startIndent, indentSize, Lang.HTML);
	}
	
	public void print(Output output, int startIndent, int indentSize, Lang lang) {
		throw new NotImplementedException();
	}
	
}
