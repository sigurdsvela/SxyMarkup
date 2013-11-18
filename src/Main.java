
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import sxyml.SXYML;
import sxyml.SXYMLElement;

public class Main {
	SXYMLElement xmlRootNode;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main(args);
	}
	
	public Main(String[] args) {
		BufferedReader inputFile = null;
		try {
			inputFile = new BufferedReader(new FileReader("//Users/sigurdbergsvela/Documents/index.xml"));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return;
		}
		try {
			xmlRootNode = SXYML.parseFile(inputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		xmlRootNode.printTree();
 	}
}
