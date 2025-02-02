package core.game;

/**
 * Internal subset of a {@link GameSet}. Is either a {@link AbschnittType#VOLLE} or a {@link AbschnittType#RAEUMEN}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see GameSet
 * @since 1.0.0
 */
public abstract class Abschnitt {

	/**
	 * The {@link Wurfbild}s of the {@link Abschnitt}.
	 */
	public Wurfbild[] wurfbilder;

	/**
	 * The type of the {@link Abschnitt}.
	 */
	public AbschnittType type;


}
