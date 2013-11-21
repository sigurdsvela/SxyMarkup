package sxyml.output;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class FileOutput implements Output{
	private PrintWriter writer;
	
	public FileOutput(String filePath) throws FileNotFoundException {
		writer = new PrintWriter(filePath);
	}
	
	public void print(String content) {
		writer.print(content);
	}

	public void println(String content) {
		writer.println(content);
	}
	
	public void close() {
		writer.close();
	}

}
