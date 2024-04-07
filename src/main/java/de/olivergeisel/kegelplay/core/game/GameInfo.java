package de.olivergeisel.kegelplay.core.game;


/**
 * All information about a game.
 *
 * @param gameSets            Number of game sets
 * @param vollePerGameSet     Number of volle per game set
 * @param abrauemenPerGameSet Number of abrauemen per game set
 * @param minutesPerGameSet   Minutes per game set
 * @param symmetric           True if the game is symmetric. All sets are equal.
 */
public record GameInfo(int gameSets, int vollePerGameSet, int abrauemenPerGameSet, double minutesPerGameSet,
					   boolean symmetric) {


//region setter/getter
	public int getTotalThrows() {
		return (vollePerGameSet + abrauemenPerGameSet) * gameSets;
	}

	public int getThrowsPerGameSet() {
		return vollePerGameSet + abrauemenPerGameSet;
	}
//endregion

}
