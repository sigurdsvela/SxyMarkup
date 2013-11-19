
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import sxyml.Element;
import sxyml.SXYML;

public class Main {
	Element sxymlRootNode;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main(args);
	}
	
	public Main(String[] args) {
		String currentDir = System.getProperty("user.dir");
		
		HashMap<String, String>arguments = new HashMap<String, String>();
		arguments.put("-i", "");
		arguments.put("-o", "");
		
		String defining = null;
		for (String arg : args) {
			if (defining == null) {
				if (!arguments.containsKey(arg)) {
					System.out.println("Invalig argument " + arg);
					System.exit(1);
				} else {
					defining = arg;
				}
			} else {
				arguments.put(defining, arg);
				defining = null;
			}
		}
		
		
		BufferedReader inputFile = null;
		try {
			inputFile = new BufferedReader(new FileReader(arguments.get("-i")));
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
		
		try {
			PrintWriter writer = new PrintWriter(arguments.get("-o"), "UTF-8");
			
			sxymlRootNode.printTree(writer, true);
			
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
 	}
}
