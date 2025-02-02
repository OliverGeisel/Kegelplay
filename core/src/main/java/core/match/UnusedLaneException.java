package core.match;

import core.game.Game;

/**
 * This exception is thrown when a lane is not used in a game.
 *
 * @version 1.0.0
 * @see Match
 * @see Game
 * @since 1.0.0
 */
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
