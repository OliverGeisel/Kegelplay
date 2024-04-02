package de.olivergeisel.kegelplay.core.game;

/**
 * Internal subset of a {@link GameSet}. Is either a {@link AbschnittType#VOLLE} or a {@link AbschnittType#RAEUMEN}.
 */
public abstract class Abschnitt {

	public Wurfbild[] wurfbilder;

	public AbschnittType type;


}
