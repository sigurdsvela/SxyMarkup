package sxyml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Element extends Node{
		private HashMap<String, ArrayList<String>> attributes;
		private ArrayList<TextNode> textNodes;
		private String tagType;
		private Element parrent;
		private boolean isVoid;
		private ArrayList<Element> children;
		
		public Element(String tagType) {
			attributes = new HashMap<String, ArrayList<String>>();
			children = new ArrayList<Element>();
			textNodes = new ArrayList<TextNode>();
			this.tagType = tagType;
			isVoid = false;
		}
		
		public Element getParrent() {
			return parrent;
		}
		
		public ArrayList<Element> getChildren() {
			return children;
		}
		
		/**
		 * Prints the node tree
		 */
		public void printTree() {
			printTree("");
		}
		
		/**
		 * Prints the node tree from here
		 * 
		 * @param iind The indent
		 */
		public void printTree(String iind) {
			String ind = iind;
			if (isVoid) {
				return;
			}
			for (Element element:children) {
				System.out.print(ind + "<" + element.getTagName());
				for (String key : element.getAttributes().keySet()) {
					System.out.print(" ");
					System.out.print(key + "=\""+element.getAttributes().get(key)+"\"");
					System.out.print(" ");
				}
			    System.out.println((element.isVoid() ? " /" : "") + ">");
			    System.out.println(ind + "  " + element.getTextNodesAsString());
				element.printTree(ind + "  ");
				
				if (!element.isVoid())
					System.out.println(ind + "</" + element.getTagName() + ">" );
			}
		}
		
		public HashMap<String, ArrayList<String>> getAttributes() {
			return attributes;
		}
		
		public ArrayList<String> getAttribute(String key) {
			if (!attributes.containsKey(key)) {
				return new ArrayList<String>();
			} else {
				return attributes.get(key);
			}
		}
		public String getAttributeAsString(String key, String seperator) {
			String rtrn = "";
			if (!attributes.containsKey(key)) {
				return rtrn;
			} else {
				for (String a:attributes.get(key)) {
					rtrn += seperator;
					rtrn += a;
				}
				return rtrn;
			}
		}
		public String getAttributeAsString(String key, char c) { return getAttributeAsString(key, "" + c + ""); }
		public String getAttributeAsString(String key) { return getAttributeAsString(key, " "); }
		
		public void setAttribute(String attribute, String value) {
			if (attributes.containsKey(attribute)) {
				attributes.remove(attribute);
			}
			ArrayList<String> a = new ArrayList<String>();
			a.add(value);
			attributes.put(attribute, a);
		}
		
		public void addToAttribute(String attribute, String value) {
			if (!attributes.containsKey(attribute)) {
				attributes.put(attribute, new ArrayList<String>());
			}
			attributes.get(attribute).add(value);
		}
		public void addToAttribute(String attribute, String[] values) {
			for (String value: values) {
				addToAttribute(attribute, value);
			}
		}
		
		public void addElement(Element element) {
			children.add(element);
			element.setParrent(this);
		}
		
		
		public boolean hasChilderen() {
			return !children.isEmpty();
		}
		
		public boolean hasChild(Element element) {
			for(int i = 0; i < children.size(); i++) {
				if (children.get(i) == element) {
					return true;
				}
			}
			return false;
		}
		
		public ArrayList<Element> getElementsByAttr(String key, String value) {
			ArrayList<Element> rtrn = new ArrayList<Element>();
			for (Element element:children) {
				for (String attrValue:element.getAttribute(key)) {
					if (attrValue.compareToIgnoreCase(value) == 0) {
						rtrn.add(element);
						break;
					}
				}
			}
			return rtrn;
		}
		
		/**
		 * Gets a single child element.
		 * 
		 * @param key	The key to match agains
		 * @param value The value to match
		 * @return XMLElement the element, or null if nothing was found
		 */
		public Element getElementByAttr(String key, String value) {
			for (Element element:children) {
				for (String attrValue:element.getAttribute(key)) {
					if (attrValue.compareToIgnoreCase(value) == 0) {
						return element;
					}
				}
			}
			return null;
		}
		
		public ArrayList<Element> getElementsByTagName(String tagName) {
			ArrayList<Element> rtrn = new ArrayList<Element>();
			for (Element element:children) {
				if (element.getTagName().compareToIgnoreCase(tagName) == 0) {
					rtrn.add(element);
				}
			}
			return rtrn;
		}
		
		
		private void setParrent(Element parrent) {
			if (this.parrent != null) {
				throw new NullPointerException("Adding element to two places in the tree.");
			}
			this.parrent = parrent;
		}
		
		
		/**
		 * Remove an element
		 * 
		 * @param element The element to remove
		 * @return true if it could find the element, false if not
		 */
		public void removeElement(Element element) {
			element.remove();
		}
		
		
		/**
		 * Remove this element from its parent.
		 */
		public void remove() {
			parrent.removeElement(this);
			this.parrent = null;
		}
		
		
		/**
		 * Remove a set of elements from this node
		 * 
		 * @param elements
		 */
		public void removeElements(ArrayList<Element> elements) {
			for (Element element : elements) {
				children.remove(element);
			}
		}
		
		
		/**
		 * Remove a set of elements from this node
		 * 
		 * @param elements
		 */
		public void removeElements(Element[] elements) {
			removeElements(new ArrayList<Element>(Arrays.<Element>asList(elements)));
		}
		
		/**
		 * Adds a text node a returns said text node
		 * 
		 * @param content
		 * @return
		 */
		public TextNode addTextNode(String content) {
			TextNode temp = new TextNode(content);
			textNodes.add(temp);
			return temp;
		}
		
		/**
		 * Adds a text node a returns said text node
		 * 
		 * @return
		 */
		public TextNode addTextNode() {
			return addTextNode("");
		}
		
		public void getTextNode(int id) throws NullPointerException{
			textNodes.get(id);
		}
		
		/**
		 * Get all textnodes as one string
		 * 
		 * @return
		 */
		public String getTextNodesAsString() {
			String rtrn = "";
			for (TextNode textNode : textNodes) {
				rtrn += textNode.getContent();
			}
			return rtrn;
		}
		
		/*
		 * ----------------------------------------
		 * SIMPLE GETTERS & SETTERS
		 * ----------------------------------------
		 * */
		public String getTagName() {
			return tagType;
		}
		
		public void setVoid() {
			isVoid = true;
			children = null; //Keep from having children, this line is like a SxyElement birth controll
		}
		
		public boolean isVoid() {
			return isVoid;
		}
		
		
	}