package de.olivergeisel.kegelplay.infrastructure.data_reader;

public class UnsupportedMatchSchema extends Throwable {
	public UnsupportedMatchSchema() {
		super();
	}

	public UnsupportedMatchSchema(String message) {
		super(message);
	}

	public UnsupportedMatchSchema(Throwable cause) {
		super(cause);
	}

	public UnsupportedMatchSchema(String message, Throwable cause) {
		super(message, cause);
	}
}
