package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.core.game.GameKind;

import java.util.List;

public class NormalMatchConfig extends MatchConfig {

	public NormalMatchConfig() {
		super();
	}

	public NormalMatchConfig(MatchSchema schema, GameKind kind) {
		super(schema, kind);
	}

	public NormalMatchConfig(int playersPerTeam, int teams, GameKind kind, int laneCount, List<String> laneNames) {
		super(playersPerTeam, teams, kind, laneCount, laneNames);
	}
}
