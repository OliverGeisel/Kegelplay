package core.match;

import core.game.GameKind;
import core.team_and_player.Player;
import core.team_and_player.Team;

import java.util.List;

/**
 * Configuration of a match. This will not change during the match.
 * It is set by the creation of the {@link Match}.
 * The most important values are the {@link MatchSchema}, the {@link GameKind} and the number of
 * {@link Team}s and {@link Player}s per team.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @since 1.0.0
 */
public abstract class MatchConfig {

	private int          playersPerTeam;
	private int          teams;
	private int          laneCount;
	private List<String> laneNames;
	private GameKind     kind;
	private MatchSchema  schema;

	/**
	 * Create a new MatchConfig with the given values.
	 *
	 * @param playersPerTeam The number of players per team
	 * @param teams          The number of teams
	 * @param kind           The kind of the game
	 * @param laneCount      The number of lanes
	 * @param laneNames      The names of the lanes
	 */
	protected MatchConfig(int playersPerTeam, int teams, GameKind kind, int laneCount, List<String> laneNames) {
		this.playersPerTeam = playersPerTeam;
		this.teams = teams;
		this.kind = kind;
		this.laneCount = laneCount;
		this.laneNames = laneNames;
	}

	/**
	 * Creates a new MatchConfig with the given {@link MatchSchema} and {@link GameKind}.
	 * Gets the other values from the {@link MatchSchema}.
	 *
	 * @param schema The schema of the match.
	 * @param kind   The kind of the game.
	 */
	protected MatchConfig(MatchSchema schema, GameKind kind) {
		this.playersPerTeam = schema.getPlayersPerCycle();
		this.teams = schema.getTeams();
		this.kind = kind;
		this.laneCount = schema.getLaneCount();
		this.laneNames = schema.getLanes().stream().map(LaneSchema::getName).toList();
		this.schema = schema;
	}

	/**
	 * Creates a new match configuration.
	 * Empty constructor for serialization.
	 */
	protected MatchConfig() {
	}

	//region setter/getter

	/**
	 * Returns the schema of the match.
	 *
	 * @return The schema of the match.
	 */
	public MatchSchema getSchema() {
		return schema;
	}

	/**
	 * Returns the number of players per team.
	 *
	 * @return The number of players per team.
	 */
	public int getPlayersPerTeam() {
		return playersPerTeam;
	}

	/**
	 * Returns the number of teams.
	 *
	 * @return The number of teams.
	 */
	public int getTeams() {
		return teams;
	}

	/**
	 * Returns the number of lanes.
	 *
	 * @return The number of lanes.
	 */
	public int getLaneCount() {
		return laneCount;
	}

	/**
	 * Returns the names of the lanes.
	 *
	 * @return The names of the lanes.
	 */
	public List<String> getLaneNames() {
		return laneNames;
	}

	/**
	 * Sets the names of the lanes.
	 *
	 * @param laneNames The names of the lanes.
	 */
	public void setLaneNames(List<String> laneNames) {
		this.laneNames = laneNames;
	}

	/**
	 * Returns the kind of game.
	 *
	 * @return The kind of game.
	 */
	public GameKind getKind() {
		return kind;
	}
//endregion


}
