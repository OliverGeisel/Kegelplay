package de.kegelplay.infrastructure.data_reader;


import core.game.Game;
import core.match.Match;
import core.match.MatchConfig;

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
