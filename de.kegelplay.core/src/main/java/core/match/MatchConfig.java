package core.match;

import core.game.GameKind;

import java.util.List;

/**
 * Configuration of a match. This will not change during the match.

 */
public abstract class MatchConfig {

	private int          playersPerTeam;
	private int          teams;
	private int          laneCount;
	private List<String> laneNames;
	private GameKind     kind;
	private MatchSchema  schema;

	protected MatchConfig(int playersPerTeam, int teams, GameKind kind, int laneCount, List<String> laneNames) {
		this.playersPerTeam = playersPerTeam;
		this.teams = teams;
		this.kind = kind;
		this.laneCount = laneCount;
		this.laneNames = laneNames;
	}

	protected MatchConfig(MatchSchema schema, GameKind kind) {
		this.playersPerTeam = schema.getPlayersPerCycle();
		this.teams = schema.getTeams();
		this.kind = kind;
		this.laneCount = schema.getLaneCount();
		this.laneNames = schema.getLanes().stream().map(LaneSchema::getName).toList();
		this.schema = schema;
	}

	protected MatchConfig() {
	}

	//region setter/getter
	public MatchSchema getSchema() {
		return schema;
	}

	public int getPlayersPerTeam() {
		return playersPerTeam;
	}

	public int getTeams() {
		return teams;
	}

	public int getLaneCount() {
		return laneCount;
	}

	public List<String> getLaneNames() {
		return laneNames;
	}

	public void setLaneNames(List<String> laneNames) {
		this.laneNames = laneNames;
	}

	public GameKind getKind() {
		return kind;
	}
//endregion


}
