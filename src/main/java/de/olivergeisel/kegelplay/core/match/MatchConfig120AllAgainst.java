package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.core.point_system.AllAgainstAll120_4PlayerPointSystem;
import de.olivergeisel.kegelplay.core.point_system.PointSystem;

import java.util.List;

public class MatchConfig120AllAgainst extends MatchConfig {

	private static final int          MAX_PLAYERS   = 4;
	private static final int          MAX_TEAMS     = 1;
	private static final ChangeSchema CHANGE_SCHEMA = ChangeSchema.ALL_AGAINST;

	private static final int NUM_WURF    = 120;
	private static final int NUM_SAETZE  = 4;
	private static final int NUM_VOLLE   = 15;
	private static final int NUM_RAEUMER = 15;

	private static final PointSystem POINTSYSTEM = new AllAgainstAll120_4PlayerPointSystem();

	public MatchConfig120AllAgainst() {
		super();
	}

	public MatchConfig120AllAgainst(int playersPerTeam, int teams, GameKind kind, int laneCount,
			List<String> laneNames) {
		super(playersPerTeam, teams, kind, laneCount, laneNames);
	}
}
