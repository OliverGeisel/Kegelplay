package core.game;


/**
 * Information to the {@link GameSet} and Throws in a {@link Game}. Can be converted and compared with a
 * {@link GameInfo}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see GameInfo
 * @see GameSet
 * @see Game
 * @since 1.0.0
 */
public enum GameKind {

	/**
	 * A game with 2 {@link GameSet}s, 25 Volle, 25 Abraeumen and 20 minutes per Durchgang.
	 * 100 throws in total.
	 */
	GAME_100(2, 25, 25, 20),

	/**
	 * A game with 4 {@link GameSet}s, 25 Volle, 25 Abraeumen and 20 minutes per Durchgang.
	 * 200 throws in total.
	 */
	GAME_200(4, 25, 25, 20),

	/**
	 * A game with 4 {@link GameSet}s, 15 Volle, 15 Abraeumen and 12 minutes per Durchgang.
	 * 120 throws in total.
	 */
	GAME_120(4, 15, 15, 12),

	/**
	 * A game with 2 {@link GameSet}s, 10 Volle, 10 Abraeumen and 8 minutes per Durchgang.
	 * 40 throws in total.
	 */
	GAME_40(2, 10, 10, 8),
	/**
	 * A game with 4 {@link GameSet}s, 10 Volle, 10 Abraeumen and 8 minutes per Durchgang.
	 * 40 throws in total.
	 */
	GAME_40_2(4, 10, 10, 8);

	private final int    numberOfGameSets;
	private final int    numberOfVolle;
	private final int    numberOfAbraeumen;
	private final double timePerGameSet;

	/**
	 * Create a new {@link GameKind} with the given values.
	 *
	 * @param numberOfGameSets  The number of {@link GameSet}s in a {@link Game}
	 * @param numberOfVolle     The number of Volle in a {@link GameSet}
	 * @param numberOfAbraeumen The number of Abraeumen in a {@link GameSet}
	 * @param timePerGameSet  The time per {@link GameSet} in a {@link Game}
	 */
	GameKind(int numberOfGameSets, int numberOfVolle, int numberOfAbraeumen, int timePerGameSet) {
		this.numberOfGameSets = numberOfGameSets;
		this.numberOfVolle = numberOfVolle;
		this.numberOfAbraeumen = numberOfAbraeumen;
		this.timePerGameSet = timePerGameSet;
	}

	/**
	 * Get the  matching {@link GameKind} from a {@link GameInfo}
	 *
	 * @param gameInfo The {@link GameInfo} to compare
	 * @return The matching {@link GameKind} or <b>null</b> if no matching {@link GameKind} was found.
	 */
	public static GameKind fromGameInfo(GameInfo gameInfo) {
		for (GameKind kind : GameKind.values()) {
			if (kind.isCompatible(gameInfo)) {
				return kind;
			}
		}
		return null;
	}

	/**
	 * Check if the {@link GameKind} is compatible with a {@link GameInfo}
	 *
	 * @param gameInfo The {@link GameInfo} to compare
	 * @return <b>true</b> if the {@link GameKind} is compatible with the {@link GameInfo}
	 */
	public boolean isCompatible(GameInfo gameInfo) {
		if (gameInfo == null) return false;
		return gameInfo.vollePerGameSet() == numberOfVolle && gameInfo.abrauemenPerGameSet() == numberOfAbraeumen
			   && gameInfo.gameSets() == numberOfGameSets && gameInfo.minutesPerGameSet() == timePerGameSet;
	}

	/**
	 * Convert the {@link GameKind} to a {@link GameInfo}
	 *
	 * @param symmetric set if a game is symmetric
	 * @return The {@link GameInfo} of the {@link GameKind}
	 */
	public GameInfo toGameInfo(boolean symmetric) {
		return new GameInfo(numberOfGameSets, numberOfVolle, numberOfAbraeumen, timePerGameSet, symmetric);
	}

	//region setter/getter

	/**
	 * Get the number of {@link GameSet}s in a {@link Game}
	 *
	 * @return The number of {@link GameSet}s
	 */
	public int getNumberOfGameSets() {
		return numberOfGameSets;
	}

	/**
	 * Get the number of Volle in a {@link GameSet}
	 *
	 * @return The number of Volle
	 */
	public int getNumberOfVolle() {
		return numberOfVolle;
	}

	/**
	 * Get the number of Abraeumen in a {@link GameSet}
	 *
	 * @return The number of Abraeumen
	 */
	public int getNumberOfAbraeumen() {
		return numberOfAbraeumen;
	}

	/**
	 * Get the time per {@link GameSet} in a {@link Game}
	 *
	 * @return The time per {@link GameSet}
	 */
	public double getTimePerGameSet() {
		return timePerGameSet;
	}
//endregion
}
