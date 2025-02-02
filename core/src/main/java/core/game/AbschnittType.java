package core.game;

/**
 * The type of an {@link Abschnitt}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Abschnitt
 * @since 1.0.0
 */
public enum AbschnittType {
	/**
	 * Only {@link Wurfbild} where all 9 can be thrown each time.
	 */
	VOLLE,
	/**
	 * Only {@link Wurfbild} where only the pins that are left can be thrown.
	 */
	RAEUMEN,
	/**
	 * Preparation for a {@link Game} will not be counted.
	 */
	PROBE,
	/**
	 * A {@link Abschnitt} with something else.
	 */
	ANDERE
}
