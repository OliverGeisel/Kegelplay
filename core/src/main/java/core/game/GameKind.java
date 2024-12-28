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
	 * A game with 2 Durchgaenge, 25 Volle and 25 Abraeumen per GameSet and 20 minutes per GameSet
	 */
	GAME_100(2, 25, 25, 20),
	/**
	 * A game with 4 Durchgaenge, 25 Volle and 25 Abraeumen per GameSet and 20 minutes per GameSet
	 */
	GAME_200(4, 25, 25, 20),
	/**
	 * A game with 4 Durchgaenge, 15 Volle and 15 Abraeumen per GameSet and 12 minutes per GameSet
	 */
	GAME_120(4, 15, 15, 12),

	/**
	 * A game with 2 Durchgaenge, 10 Volle and 10 Abraeumen per GameSet and 8 minutes per GameSet
	 */
	GAME_40(2, 10, 10, 8),
	/**
	 * A game with 4 Durchgaenge, 10 Volle and 10 Abraeumen per GameSet and 8 minutes per GameSet
	 */
	GAME_40_2(4, 10, 10, 8);

	private int    numberOfDurchgaenge;
	private int    numberOfVolle;
	private int    numberOfAbraeumen;
	private double timePerDurchgang;

	/**
	 * Create a new {@link GameKind}
	 * @param numberOfDurchgaenge The number of Durchgaenge
	 * @param numberOfVolle The number of Volle
	 * @param numberOfAbraeumen The number of Abraeumen
	 * @param timePerDurchgang The time per Durchgang to play
	 */
	GameKind(int numberOfDurchgaenge, int numberOfVolle, int numberOfAbraeumen, int timePerDurchgang) {
		this.numberOfDurchgaenge = numberOfDurchgaenge;
		this.numberOfVolle = numberOfVolle;
		this.numberOfAbraeumen = numberOfAbraeumen;
		this.timePerDurchgang = timePerDurchgang;
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
	 * @param gameInfo The {@link GameInfo} to compare
	 * @return <b>true</b> if the {@link GameKind} is compatible with the {@link GameInfo}
	 */
	public boolean isCompatible(GameInfo gameInfo) {
		if (gameInfo == null) return false;
		return gameInfo.vollePerGameSet() == numberOfVolle && gameInfo.abrauemenPerGameSet() == numberOfAbraeumen
			   && gameInfo.gameSets() == numberOfDurchgaenge && gameInfo.minutesPerGameSet() == timePerDurchgang;
	}

	/**
	 * Convert the {@link GameKind} to a {@link GameInfo}
	 *
	 * @return The {@link GameInfo} of the {@link GameKind}
	 */
	public GameInfo toGameInfo(boolean symmetric) {
		return new GameInfo(numberOfDurchgaenge, numberOfVolle, numberOfAbraeumen, timePerDurchgang, symmetric);
	}

	//region setter/getter

	/**
	 * Get the number of Durchgaenge
	 * @return The number of Durchgaenge
	 */
	public int getNumberOfDurchgaenge() {
		return numberOfDurchgaenge;
	}

	/**
	 * Get the number of Volle
	 * @return The number of Volle
	 */
	public int getNumberOfVolle() {
		return numberOfVolle;
	}

	/**
	 * Get the number of Abraeumen
	 * @return The number of Abraeumen
	 */
	public int getNumberOfAbraeumen() {
		return numberOfAbraeumen;
	}

	/**
	 * Get the time per Durchgang to play
	 * @return The time per Durchgang to play
	 */
	public double getTimePerDurchgang() {
		return timePerDurchgang;
	}
//endregion
}
