package de.olivergeisel.kegelplay.core.game;


/**
 * Representation of a single Throw ({@link Wurf}) in a game.
 * @param value the score of the Wurf. Pins hitted by the Wurf.
 * @param bild the {@link Wurfbild} of the Wurf. Number of pins must not be null.
 * @param foul true if the Wurf is a foul.
 * @param redCard true if the Wurf is a red card.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @see Wurfbild
 * @author Oliver Geisel
 */
public record Wurf(int value, Wurfbild bild, boolean foul, boolean redCard) {

	/**
	 * Returns the {@link Wurfbild} of the Wurf.
	 *
	 * @throws IllegalArgumentException if the Wurfbild is null
	 */
	public Wurf {
		if (bild == null) {
			throw new IllegalArgumentException("Wurfbild darf nicht null sein");
		}
		if (value < 0) {
			throw new IllegalArgumentException("Wurf darf nicht negativ sein");
		}
		if (value > 9) {
			throw new IllegalArgumentException("Wurf darf nicht mehr als 9 Pins treffen");
		}
	}

	//region setter/getter

	/**
	 * Returns the score of the Wurf. Is only an alias for {@link #value}.
	 *
	 * @return the score of the Wurf
	 */
	public int getScore() {
		return value;
	}
	//endregion

}
