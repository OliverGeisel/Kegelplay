package de.olivergeisel.kegelplay.core.team_and_player;

public class NoPlayerFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NoPlayerFoundException() {
		super();
	}

	public NoPlayerFoundException(String message) {
		super(message);
	}

	public NoPlayerFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoPlayerFoundException(Throwable cause) {
		super(cause);
	}


}
