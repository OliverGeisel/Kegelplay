package de.kegelplay.infrastructure.data_reader;


import core.game.Game;
import core.match.Match;
import core.match.MatchConfig;

import java.nio.file.Path;

/**
 * Reads the current state of a match from a file dir.
 */
public abstract class GeneralReader {

	protected final Path baseDir;

	protected GeneralReader(Path baseDir) {
		this.baseDir = baseDir;
	}

	/**
	 * Reads the current complete state of a match from a file dir.
	 *
	 * @return the match
	 */
	public abstract <G extends Game> Match<G> initNewMatch() throws UnsupportedMatchSchema;

	public abstract MatchConfig readConfig();
}
