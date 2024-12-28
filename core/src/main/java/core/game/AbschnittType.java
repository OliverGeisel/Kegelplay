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
	 * A {@link Abschnitt} with volle.
	 */
	VOLLE,
	/**
	 * A {@link Abschnitt} with abraeumen.
	 */
	RAEUMEN,
	/**
	 * A {@link Abschnitt} with a probe(not a real throw).
	 */
	PROBE,
	/**
	 * A {@link Abschnitt} with something else.
	 */
	ANDERE
}
