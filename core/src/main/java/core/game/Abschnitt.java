package core.game;

/**
 * Internal subset of a {@link core.game.GameSet}. Is either a {@link core.game.AbschnittType#VOLLE} or a {@link core.game.AbschnittType#RAEUMEN}.
 */
public abstract class Abschnitt {

	public core.game.Wurfbild[] wurfbilder;

	public core.game.AbschnittType type;


}
