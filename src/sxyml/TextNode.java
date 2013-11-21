package sxyml;

import sxyml.output.Output;

public class TextNode extends Node{
	private String content;
	
	public TextNode(String content) {
		this.content = content;
	}
	
	public TextNode() {
		this("");
	}
	
	/**
	 * Adds content to this textnode
	 * 
	 * @param content The content to add
	 */
	public void pushContent(String content) {
		this.content += content;
	}
	
	public String getContent() {
		return content;
	}

	@Override
	/**
	 * Prints this textnode to $output and ends with newline.
	 */
	public void print(Output output) {
		output.println(content);
	}
	
	public void print(Output output, int indentSize) {
		String ind = "";
		for (int i = 0; i < indentSize; i++) {
			ind += " ";
		}
		output.println(ind + content);
	}
	
}
