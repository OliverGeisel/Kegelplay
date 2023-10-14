package de.olivergeisel.kegelplay.core.game;

public record Wurf(Wurfbild bild, boolean foul, boolean redCard) {
//region setter/getter
	public int getScore() {
		return bild.getWert();
	}
//endregion

}
