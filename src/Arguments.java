import java.util.HashMap;

import com.sun.xml.internal.ws.api.model.ExceptionType;

public class Arguments {
	HashMap<String, Option> arguments;
	HashMap<String, String> aliases;
	String[] args;
	
	public Arguments(String[] args) {
		this.args = args;
		arguments = new HashMap<String, Option>();
		aliases = new HashMap<String, String>();
	}
	
	public void parse() throws KeyDoesNotExistException {
		String currentArg = null;
		for (String arg : args) {
			if (arg.startsWith("-")) {
				currentArg = arg;
				set(currentArg, "");
			} else {
				set(currentArg, arg);
				currentArg = null;
			}
		}
	}
	
	/**
	 * Get the value of an option, you may use the main option key
	 * or one of its aliases
	 * 
	 * @param key
	 * @return
	 * @throws KeyDoesNotExistException 
	 */
	public String get(String key) throws KeyDoesNotExistException {
		Option theOption;
		
		if (aliases.containsKey(key)) { //Is this key an alias
			key = aliases.get(key);
		}
		
		if (arguments.containsKey(key)) {
			return arguments.get(key).getValue();
		} else {
			throw new KeyDoesNotExistException("The option " + key + " was never registered.", key);
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
		
		if (arguments.containsKey(key)) {
			arguments.get(key).setValue(value);
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
		return arguments.containsKey(key) || aliases.containsKey(key);
	}
	
	/**
	 * Register an option
	 * 
	 * @param key The possible keys for this option
	 * @param expectedValue bool can not be spesified here
	 * @param requred
	 * 
	 * @throws KeyAllreadyExistsException 
	 */
	public void registerOption(String key, Expect excpect) throws KeyAllreadyExistsException {
		if (isKeyOccupied(key))
			throw new KeyAllreadyExistsException("", key);
		
		arguments.put(key, new Option(null, excpect));
	}
	
	public void registerOption(String key) throws KeyAllreadyExistsException {
		registerOption(key, Expect.Any);
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
	public void registerOptionAlias(String alias, String option) throws KeyAllreadyExistsException, KeyDoesNotExistException {
		if (isKeyOccupied(alias)) {
			throw new KeyAllreadyExistsException("There is allready a key or alias registered to " + alias + ".", alias);
		}
		
		if (!arguments.containsKey(option)) {
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
		private Expect excpect;
		
		
		public Option(String value, Expect excpect) {
			this.value = value;
			this.excpect = excpect;
		}

		public String getValue() {
			return value;
		}
		
		public void setValue(String value) {
			this.value = value;
		}
		
		public Expect getExpext() {
			return excpect;
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
	
	public abstract class ArgumentException extends Exception{
		private static final long serialVersionUID = -5657520492551951138L;

		public ArgumentException(String message) {
			super(message);
		}
	}
	
	public abstract class KeyException extends ArgumentException {
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
