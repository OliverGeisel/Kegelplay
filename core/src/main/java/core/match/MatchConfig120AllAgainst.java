package core.match;

import core.game.Game;
import core.game.Game120;
import core.game.GameKind;
import core.point_system.AllAgainstAll120_4PlayerPointSystem;
import core.point_system.PointSystem;

import java.util.LinkedList;
import java.util.List;

/**
 * Special config for a match with 4 players playing against each other.
 * There is only one team with 4 players. The {@link Game} is a {@link Game120}.
 */
public class MatchConfig120AllAgainst extends MatchConfig {

	private static final int          MAX_PLAYERS   = 4;
	private static final int          MAX_TEAMS     = 1;
	private static final ChangeSchema CHANGE_SCHEMA = ChangeSchema.ALL_AGAINST;

	private static final int NUM_WURF    = 120;
	private static final int NUM_SAETZE  = 4;
	private static final int NUM_VOLLE   = 15;
	private static final int NUM_RAEUMER = 15;

	private static final PointSystem POINTSYSTEM = new AllAgainstAll120_4PlayerPointSystem();

	/**
	 * Create a new MatchConfig120AllAgainst with the default values.
	 * The names of the lanes are empty.
	 */
	public MatchConfig120AllAgainst() {
		super(MAX_PLAYERS, MAX_TEAMS, GameKind.GAME_120, 4, new LinkedList<>());
	}

	public MatchConfig120AllAgainst(int playersPerTeam, int teams, GameKind kind, int laneCount,
			List<String> laneNames) {
		super(playersPerTeam, teams, kind, laneCount, laneNames);
	}
}
