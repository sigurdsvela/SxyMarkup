
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import sxyml.SXYML;
import sxyml.Element;

public class Main {
	Element sxymlRootNode;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main(args);
	}
	
	public Main(String[] args) {
		BufferedReader inputFile = null;
		try {
			inputFile = new BufferedReader(new FileReader("//Users/sigurdbergsvela/Documents/index.sxy"));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return;
		}
		try {
			sxymlRootNode = SXYML.parseFile(inputFile);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		
		sxymlRootNode.printTree();
 	}
}
