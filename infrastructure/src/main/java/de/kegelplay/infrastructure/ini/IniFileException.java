package de.kegelplay.infrastructure.ini;


/**
 * Exception to be thrown when an ini file is invalid.
 */
public class IniFileException extends RuntimeException {
	public IniFileException(String invalidIniFile) {
		super(invalidIniFile);
	}

	public IniFileException(Exception e) {
		super(e);
	}

	public IniFileException() {
		super();
	}

	public IniFileException(String message, Exception e) {
		super(message, e);
	}


}
