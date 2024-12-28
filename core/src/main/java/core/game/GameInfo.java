package core.game;


/**
 * All information about a {@link Game}.
 *
 * @param gameSets            Number of {@link GameSet}s
 * @param vollePerGameSet     Number of volle per game set
 * @param abrauemenPerGameSet Number of abrauemen per game set
 * @param minutesPerGameSet   Minutes per game set to play
 * @param symmetric           True if the game is symmetric. All sets have same parameters.
 *
 * @see Game
 * @see GameSet
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 *
 */
public record GameInfo(int gameSets, int vollePerGameSet, int abrauemenPerGameSet, double minutesPerGameSet,
					   boolean symmetric) {


//region setter/getter

	/**
	 * Returns the total number of throws in the {@link Game}.
	 *
	 * @return The total number of throws in the {@link Game}.
	 */
	public int getTotalThrows() {
		return (vollePerGameSet + abrauemenPerGameSet) * gameSets;
	}

	/**
	 * Returns the total number of throws per {@link GameSet}.
	 *
	 * @return The total number of throws per {@link GameSet}.
	 */
	public int getThrowsPerGameSet() {
		return vollePerGameSet + abrauemenPerGameSet;
	}
//endregion

}
