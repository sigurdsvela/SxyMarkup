package sxyml;

/**
 * Holds a buch of XML nodes and gives you some utility functions that allowes you to narrow the list down. This class
 * differes from XMLElement in that it is only a list of XMLElements,a nd not an element in itself.
 * 
 * */
public class NodeList {
	Element[] elements;
	
	public NodeList(Element[] elements) {
		this.elements = elements;
	}
	
}
