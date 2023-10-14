package de.olivergeisel.kegelplay.infrastructure.data_reader;

import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.MatchConfig;

public abstract class GeneralReader {

	public abstract Match initNewMatch();

	public abstract MatchConfig readConfig();
}
