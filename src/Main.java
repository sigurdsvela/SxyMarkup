
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import sxyml.Element;
import sxyml.Node.Lang;
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
		options.addOption("--output-lang", false, "Define what language to output as. HTML|XHTML|XML|SGML. Defaults to HTML.");
		options.addOptionAlias("-o", "--output-file");
		options.addOptionAlias("-i", "--input-file");
		
		options.parse(args, true);
		if (options.getOptionValue("--tab-size") == null) {
			options.set("--tab-size", "4");
		}
		
		if (options.getOptionValue("--input-file").startsWith("~")) {
			throw new RuntimeException("You can not use the \"~\" to specify the path. You must use an absolute path, or a path relative to your current directory.");
		}
		
		if (options.getOptionValue("--output-lang") == null)
			options.set("--output-lang", "HTML");
		
		Lang lang = null;
		try {
			lang = Lang.valueOf(options.getOptionValue("--output-lang"));
		} catch(IllegalArgumentException e) {
			System.out.println(options.getOptionValue("--output-lang") + " is not a valid output language!");
			options.printHelp();
			System.exit(0);
		}
		
		
		BufferedReader inputFile = null;
		
		
		try {
			inputFile = new BufferedReader(new FileReader(options.getOptionValue("--input-file")));
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
			if (options.getOptionValue("--output-file") == null) {
				output = new SystemOutput();
			} else {
				output = new FileOutput(options.getOptionValue("--output-file"));
			}

			sxymlRootNode.print(output, 0, Integer.parseInt(options.getOptionValue("--tab-size")), lang);
			
			output.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
 	}
}
