package core.match;

import core.game.Game;

/**
 * This class represents the change schema for a game.
 * This is how the players change the lanes in a game after each GameSet.
 * It's a 2D array with the number of lanes and the number of GameSets.
 * First value is the GameSet, second value is the lane or Player.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @see Game
 * @since 1.0.0
 */
public class ChangeSchema {

	public static final ChangeSchema ALL_AGAINST = new ChangeSchema();

	private final int numOfLanes    = 4;
	private final int numOfGameSets = 4;

	private final int[][] changeSchema = {
			{1, 2, 3, 4},
			{2, 1, 4, 3},
			{4, 3, 2, 1},
			{3, 4, 1, 2}
	};


	/**
	 * Get the position of the players in the selected round.
	 *
	 * @param gameSetNumber The round to get the position of the players.
	 * @return The position of the players in the selected round.
	 */
	public int[] getChangeSchema(int gameSetNumber) {
		return changeSchema[gameSetNumber - 1];
	}
}
