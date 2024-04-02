package de.olivergeisel.kegelplay.infrastructure.ini;

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
