
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
		Options options = new Options();
		
		options.addOption("--input-file", true, "Specifies the input file.");
		options.addOption("--output-file", false, "Spesify the output file. If not definied, result will be printed on the screen.");
		options.addOption("--tab-size", false, "Set the size of the tabs for the output.");
		options.addOption("--html-style-void", false, "<link ...> instead of <link .../>");
		options.addOptionAlias("-o", "--output-file");
		options.addOptionAlias("-i", "--input-file");
		
		options.parse(args, true);
		if (options.get("--tab-size") == null) {
			options.set("--tab-size", "4");
		}
		
		
		
		BufferedReader inputFile = null;
		
		
		try {
			inputFile = new BufferedReader(new FileReader(options.get("--input-file")));
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
		}
		
 	}
}
