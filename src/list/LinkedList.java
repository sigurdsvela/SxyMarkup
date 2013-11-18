package list;

public class LinkedList<T> {
	private Link<T> firstLink;
	private int length;
	private Link<T> pointer;
	
	public LinkedList() {
		firstLink = null;
	}
	
	
	public T next() {
		try {
			pointer = pointer.nextLink();
		} catch(NullPointerException e) {
			pointer = firstLink;
		}
		return pointer.getValue();
	}
	
	public void reset() {
		pointer = firstLink;
	}
	
	
	public void insert(T value) {
		Link<T> link = new Link<T>(value);
		link.setNextLink(firstLink);
		firstLink = link;
		length++;
	}
	
	public void insert(T value, int position) {
		Link<T> newLink = new Link<T>(value);
		Link<T> prevLink = firstLink;
		Link<T> newLinksNextLink = null;
		try {
			for (int i = 0; i < position - 1; i++) {
				prevLink = prevLink.nextLink();
			}
			newLinksNextLink = prevLink.nextLink();
		} catch (NullPointerException e) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		prevLink.setNextLink(newLink);
		newLink.setNextLink(newLinksNextLink);
		length++;
	}
	
	public T get(int position) {
		Link<T> prevLink = firstLink;
		try {
			for (int i = 0; i < position; i++) {
				prevLink = prevLink.nextLink();
			}
		} catch (NullPointerException e) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return prevLink.getValue();
	}
	
	public void printList() {
		Link<T> currentLink = firstLink;
		while (currentLink != null) {
			currentLink.print();
			currentLink = currentLink.nextLink();
		}
	}
	
	private class Link<T> {
		private T value;
		private Link<T> nextLink;
		
		public Link(T value) {
			this.value = value;
			nextLink = null;
		}
		
		public void setValue(T value) {
			this.value = value;
		}
		
		public T getValue() {
			return value;
		}
		
		public void setNextLink(Link<T> link) {
			nextLink = link;
		}
		
		public Link<T> nextLink() {
			return nextLink;
		}
		
		public void print() {
			System.out.println("{" + value + "}");
		}
	}
}
