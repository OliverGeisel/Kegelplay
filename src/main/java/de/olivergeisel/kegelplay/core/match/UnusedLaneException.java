package de.olivergeisel.kegelplay.core.match;

public class UnusedLaneException extends Throwable {
	public UnusedLaneException(String message) {
		super(message);
	}

	public UnusedLaneException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnusedLaneException(Throwable cause) {
		super(cause);
	}

	public UnusedLaneException() {
		super();
	}
}
