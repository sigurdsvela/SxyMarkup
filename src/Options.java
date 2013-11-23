import java.util.HashMap;

import com.sun.xml.internal.ws.api.model.ExceptionType;

public class Options {
	private HashMap<String, Option> options;
	private HashMap<String, String> aliases;
	
	public Options() {
		options = new HashMap<String, Option>();
		aliases = new HashMap<String, String>();
		addOption("-h", false, "Prints this message");
		addOptionAlias("--help", "-h");
	}
	
	/**
	 * 
	 * @param args The arguments
	 * @param exitOnMissingRequiredArgument whether or not to System.exit(1) on missing required argument
	 */
	public void parse(String[] args, boolean exitOnMissingRequiredArgument) {
		String currentArg = null;
		try {
			for (String arg : args) {
				if (arg.startsWith("-")) {
					currentArg = arg;
					set(currentArg, "");
				} else {
					set(currentArg, arg);
					currentArg = null;
				}
			}
		} catch (KeyDoesNotExistException e) {
			printHelp();
		}
		
		if (exitOnMissingRequiredArgument) {
			for (String option: options.keySet()) { //TODO Yeah, not all that clever, i'm tired, okay?
				Option theOption = options.get(option);
				if (theOption.getValue() == null && theOption.isRequired()) {
					System.out.println("Missing " + option + " argument.");
					printHelp();
					System.exit(1);
				}
			}
		}
		
		if (getOptionValue("-h") != null) {
			printHelp();
		}
	}
	
	/**
	 * 
	 * @param string
	 * @param padding
	 * @param length
	 * @param left TRUE left, FALSE right
	 * @return
	 */
	private String stringPad(String string, String padding, int length, boolean left) {
		int neededPadding = length - string.length();
		
		if (string.length() >= length) {
			return string;
		}
		for (int i = 0; i < neededPadding; i++) {
			if (left) {
				string = padding + string;
			} else {
				string += padding;
			}
		}
		return string;
	}
	
	public void printHelp() {
		int sizeOfBigges = 0;
		for (String option: options.keySet()) { //TODO This is some horrible brute force right here
			if (option.length() > sizeOfBigges) {
				sizeOfBigges = option.length();
			}
		}
		for (String option: options.keySet()) {
			System.out.print(stringPad(stringPad(option, " ", sizeOfBigges, true), " ", sizeOfBigges + 5, false)); //TODO This is some horrible code right here
			System.out.println(options.get(option).getDescription());
		}
	}
	
	/**
	 * Get the value of an option, you may use the main option key
	 * or one of its aliases
	 * 
	 * @param key
	 * @return
	 */
	public String getOptionValue(String key) {
		if (aliases.containsKey(key)) { //Is this key an alias
			key = aliases.get(key);
		}
		
		if (options.containsKey(key)) {
			return options.get(key).getValue();
		} else {
			return null;
		}
	}
	
	/**
	 * Overide the value of an option. Remember that this does affect the
	 * value of all of its aliases.
	 * @param key
	 * @param value
	 * 
	 * @throws KeyDoesNotExistException 
	 */
	public void set(String key, String value) throws KeyDoesNotExistException {
		if (aliases.containsKey(key)) { //Is this key an alias
			key = aliases.get(key);
		}
		
		if (options.containsKey(key)) {
			options.get(key).setValue(value);
		} else {
			throw new KeyDoesNotExistException("The option " + key + " was never registered.", key);
		}
	}
	
	
	/**
	 * Checks if a key is "busy"
	 * 
	 * @param key The key
	 * @return
	 */
	private boolean isKeyOccupied(String key) {
		return options.containsKey(key) || aliases.containsKey(key);
	}
	
	/**
	 * Register an option
	 * 
	 * @param key The possible keys for this option
	 * @param expectedValue bool can not be spesified here
	 * @param requred
	 * 
	 */
	public void addOption(String key, boolean required, String description) {
		options.put(key, new Option(null, required, description));
	}
	
	
	
	
	/**
	 * Register an alias to an option.
	 * 
	 * @param alias The alias
	 * @param option The option
	 * 
	 * @throws KeyAllreadyExistsException 
	 * @throws KeyDoesNotExistException 
	 */
	public void addOptionAlias(String alias, String option) throws KeyAllreadyExistsException, KeyDoesNotExistException {
		if (isKeyOccupied(alias)) {
			throw new KeyAllreadyExistsException("There is allready a key or alias registered to " + alias + ".", alias);
		}
		
		if (!options.containsKey(option)) {
			throw new KeyDoesNotExistException("The option " + option + " does not exist.", option);
		}
		
		aliases.put(alias, option);
	}
	
	
	/* --------------------
	 * Datastructors
	 * --------------------
	 * */
	
	private class Option {
		private String value;
		private String description;
		private boolean required;
		
		
		public Option(String value, boolean required, String description) {
			this.value = value;
			this.required = required;
			this.description = description;
		}

		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}

		public String getDescription() {
			return description;
		}
		
		public boolean isRequired() {
			return required;
		}
		
	}
	
	
	public enum Expect {
		Any(".*?"),
		Int("\\d+"),
		YorN("(Y|N|y|n)"),
		bool(".*?"),
		String("(\\w|\\s|\\d)+");
		
		private final String reg;
		Expect(String reg) {
			this.reg = reg;
		}
		private String regex() { return reg; }
		private boolean matches(String toWhat) { return toWhat.matches(reg); }
	}
	
	/*
	 * ----------------
	 * Exception
	 * ----------------
	 * */
	
	public abstract class ArgumentException extends RuntimeException{
		private static final long serialVersionUID = -5657520492551951138L;

		public ArgumentException(String message) {
			super(message);
		}
	}
	
	public abstract class KeyException extends ArgumentException {
		private static final long serialVersionUID = -8815057304493745811L;
		private final String key;
		public KeyException(String message, String key) {
			super(message);
			this.key = key;
		}
		
		public final String key() {
			return key;
		}
	}
	
	public final class KeyAllreadyExistsException extends KeyException {
		private static final long serialVersionUID = -503057191298343902L;

		public KeyAllreadyExistsException(String message, String key) {
			super(message, key);
		}
	}
	
	public final class KeyDoesNotExistException extends KeyException {
		private static final long serialVersionUID = 2038891855921282264L;

		public KeyDoesNotExistException(String message, String key) {
			super(message, key);
		}
	}
	
}
