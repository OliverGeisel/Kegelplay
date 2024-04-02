package de.olivergeisel.kegelplay.core.game;

public record Wurf(int value, Wurfbild bild, boolean foul, boolean redCard) {

	//region setter/getter
	public int getScore() {
		return value;
	}
//endregion

}
