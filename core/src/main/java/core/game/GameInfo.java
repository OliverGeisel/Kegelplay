package core.game;


/**
 * All information about a {@link Game}. This includes the number of {@link GameSet}s, the number of volle and
 * abraeumen per {@link GameSet}, the time per {@link GameSet} and if the game is symmetric.
 * <br>
 * A game is symmetric if all {@link GameSet}s have the same parameters.
 *
 * @param gameSets            Number of {@link GameSet}s
 * @param vollePerGameSet     Number of volle per game set
 * @param abrauemenPerGameSet Number of abrauemen per game set
 * @param minutesPerGameSet   Minutes per game set to play
 * @param symmetric           True if the game is symmetric. All sets have same parameters.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @see GameSet
 * @since 1.0.0
 */
public record GameInfo(int gameSets, int vollePerGameSet, int abrauemenPerGameSet, double minutesPerGameSet,
					   boolean symmetric) {


//region setter/getter

	/**
	 * Get the total number of throws for the {@link Game}.
	 * Number of throws per game set * number of GameSets
	 *
	 * @return The total number of throws in the {@link Game}.
	 */
	public int getTotalThrows() {
		return (vollePerGameSet + abrauemenPerGameSet) * gameSets;
	}

	/**
	 * Get the total number of throws per game set.
	 *
	 * @return Total number of throws per {@link GameSet}.
	 */
	public int getThrowsPerGameSet() {
		return vollePerGameSet + abrauemenPerGameSet;
	}
//endregion

}
