package core.match;

/**
 * The change schema for a game. This schema defines the order of the players in the next round.
 * <p>
 * The change schema is a 2D array. The first dimension is the round and the second dimension is the position of the players.
 * The value in the specified position is the number of the player in the round.
 * </p>
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @since 1.0.0
 */
public class ChangeSchema {

	public static final ChangeSchema ALL_AGAINST = new ChangeSchema();

	private final int numOfBahnen      = 4;
	private final int numOfDurchgaenge = 4;

	private final int[][] changeSchema = {
			{1, 2, 3, 4},
			{2, 1, 4, 3},
			{4, 3, 2, 1},
			{3, 4, 1, 2}
	};


	/**
	 * Get the position of the players in the selected round.
	 *
	 * @param durchgang The round to get the position of the players.
	 * @return The position of the players in the selected round.
	 */
	public int[] getChangeSchema(int durchgang) {
		return changeSchema[durchgang - 1];
	}
}
