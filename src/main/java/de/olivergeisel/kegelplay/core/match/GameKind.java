package de.olivergeisel.kegelplay.core.match;

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

	private GameKind(int numberOfDurchgaenge, int numberOfVolle, int numberOfAbraeumen, int timePerDurchgang) {
		this.numberOfDurchgaenge = numberOfDurchgaenge;
		this.numberOfVolle = numberOfVolle;
		this.numberOfAbraeumen = numberOfAbraeumen;
		this.timePerDurchgang = timePerDurchgang;
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
