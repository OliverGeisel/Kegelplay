package core.match;

import core.game.GameKind;

import java.util.List;

/**
 * Configuration of a normal {@link Match}
 * <p>
 * This configuration is used for a normal match with only one game kind.
 * It contains the number of players per team, the number of teams, the kind of game and the lanes.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see MatchConfig
 * @see Match
 * @since 1.0.0
 */
public class NormalMatchConfig extends MatchConfig {

	/**
	 * Creates a new match configuration. This constructor is only used for serialization.
	 */
	public NormalMatchConfig() {
		super();
	}

	/**
	 * Creates a new match configuration.
	 *
	 * @param schema The schema of the match.
	 * @param kind   The kind of game.
	 */
	public NormalMatchConfig(MatchSchema schema, GameKind kind) {
		super(schema, kind);
	}

	/**
	 * Creates a new match configuration.
	 *
	 * @param playersPerTeam The number of players per team.
	 * @param teams          The number of teams.
	 * @param kind           The kind of game.
	 * @param laneCount      The number of lanes.
	 * @param laneNames      The names of the lanes.
	 */
	public NormalMatchConfig(int playersPerTeam, int teams, GameKind kind, int laneCount, List<String> laneNames) {
		super(playersPerTeam, teams, kind, laneCount, laneNames);
	}
}
