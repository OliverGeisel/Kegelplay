package de.olivergeisel.kegelplay.infrastructure.data_reader;

import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.MatchConfig;

/**
 * Reads the current state of a match from a file dir.
 */
public abstract class GeneralReader {

	/**
	 * Reads the current complete state of a match from a file dir.
	 * @return the match
	 */
	public abstract Match initNewMatch();

	public abstract MatchConfig readConfig();
}
