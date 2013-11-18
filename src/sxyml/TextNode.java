package sxyml;

public class TextNode extends Node{
	private String content;
	
	public TextNode(String content) {
		this.content = content;
	}
	
	public void pushContent(String content) {
		this.content += content;
	}
	
	public String getContent() {
		return content;
	}
	
}
