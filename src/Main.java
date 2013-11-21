
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import sxyml.Element;
import sxyml.SXYML;
import sxyml.output.FileOutput;
import sxyml.output.Output;
import sxyml.output.SystemOutput;

public class Main {
	Element sxymlRootNode;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Main(args);
	}

	
	public Main(String[] args) {
		Arguments options = new Arguments(args);
		
		try {
			options.registerOption("--input-file");
			options.registerOption("--output-file");
			options.registerOption("--tab-size");
			options.registerOption("--html-style-void");
			options.registerOptionAlias("-o", "--output-file");
			options.registerOptionAlias("-i", "--input-file");
		} catch (Exception e){
			e.printStackTrace();
		}
		
		try {
			options.parse();
			if (options.get("--tab-size") == null) {
				options.set("--tab-size", "4");
			}
		} catch(Arguments.KeyException e) {
			System.out.println("The option \"" + e.key() + "\" is not a valid option.");
			System.exit(1);
		}
		
		
		BufferedReader inputFile = null;
		
		
		try {
			inputFile = new BufferedReader(new FileReader(options.get("--input-file")));
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return;
		} catch (Arguments.KeyDoesNotExistException e) {
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
			Output output;
			if (options.get("--output-file") == null) {
				output = new SystemOutput();
			} else {
				output = new FileOutput(options.get("--output-file"));
			}
			
			sxymlRootNode.print(output, Integer.parseInt(options.get("--tab-size")));
			
			output.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (Arguments.KeyException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
 	}
}
