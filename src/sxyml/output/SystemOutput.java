package sxyml.output;

import java.io.FileWriter;

public class SystemOutput implements Output{

	public void print(String content) {
		System.out.print(content);
	}

	public void println(String content) {
		System.out.println(content);
	}
	
	public void close(){}

}
