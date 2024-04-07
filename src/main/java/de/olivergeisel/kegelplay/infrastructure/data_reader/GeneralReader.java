package de.olivergeisel.kegelplay.infrastructure.data_reader;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.MatchConfig;

/**
 * Reads the current state of a match from a file dir.
 */
public abstract class GeneralReader {

	/**
	 * Reads the current complete state of a match from a file dir.
	 *
	 * @return the match
	 */
	public abstract <G extends Game> Match<G> initNewMatch() throws UnsupportedMatchSchema;

	public abstract MatchConfig readConfig();
}
