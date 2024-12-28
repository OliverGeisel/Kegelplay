package core.game;

/**
 * The state of a {@link GameSet}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see GameSet
 * @since 1.0.0
 */
public enum SetState {
	/**
	 * The game set is not started yet.
	 */
	NOT_STARTED,
	/**
	 * The game set is in progress.
	 */
	IN_PROGRESS,
	/**
	 * The game set is finished.
	 */
	FINISHED
}
