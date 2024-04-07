package de.olivergeisel.kegelplay.core.game;


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
	GAME_100(2, 25, 25, 20),
	GAME_200(4, 25, 25, 20),
	GAME_120(4, 15, 15, 12),

	GAME_40(2, 10, 10, 8),
	GAME_40_2(4, 10, 10, 8);

	private int    numberOfDurchgaenge;
	private int    numberOfVolle;
	private int    numberOfAbraeumen;
	private double timePerDurchgang;

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

	public boolean isCompatible(GameInfo gameInfo) {
		if (gameInfo == null) return false;
		return gameInfo.vollePerGameSet() == numberOfVolle && gameInfo.abrauemenPerGameSet() == numberOfAbraeumen
			   && gameInfo.gameSets() == numberOfDurchgaenge && gameInfo.minutesPerGameSet() == timePerDurchgang;
	}

	public GameInfo toGameInfo() {
		return new GameInfo(numberOfDurchgaenge, numberOfVolle, numberOfAbraeumen, timePerDurchgang, true);
	}

	//region setter/getter
	public int getNumberOfDurchgaenge() {
		return numberOfDurchgaenge;
	}

	public int getNumberOfVolle() {
		return numberOfVolle;
	}

	public int getNumberOfAbraeumen() {
		return numberOfAbraeumen;
	}

	public double getTimePerDurchgang() {
		return timePerDurchgang;
	}
//endregion
}
