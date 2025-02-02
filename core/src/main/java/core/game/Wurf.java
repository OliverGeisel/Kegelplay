package core.game;


/**
 * Representation of a single Throw ({@link Wurf}) in a game.
 *
 * @param value   the score of the Wurf. Pins hit by the Wurf.
 * @param bild    the {@link Wurfbild} of the Wurf. Number of pins must not be null.
 * @param foul    true if the Wurf is a foul.
 * @param redCard true if the Wurf is a red card.
 * @param volle   true if the Wurf is a volle (all 9 Pins are there).
 * @param anschub true if the Wurf is an anschub (in abräumen is the first throw after all pin were hit in abräumen).
 * @param volle   true if the Wurf is a volle. If false, the Wurf is raeumer
 * @param anschub true if the Wurf is an Anschub. If false, the Wurf is not an Anschub. Is only important in the
 *                raemer phase
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Wurfbild
 * @since 1.0.0
 */
public record Wurf(int value, Wurfbild bild, boolean foul, boolean redCard, boolean volle, boolean anschub) {

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
