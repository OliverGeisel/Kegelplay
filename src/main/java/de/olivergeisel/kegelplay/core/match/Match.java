package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.point_system.PointSystem;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import javafx.util.Pair;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;


/**
 * Represents a match. Its contains the complete match date.
 * <p>
 * A match is a running event on some lanes. It is played by at least one team.
 * A match has a {@link MatchConfig}, which defines the number of teams, players per team and how game is played.
 * A match can be in one of the following states:
 *  <ul>
 *      <li>Not started</li>
 *      <li>Preparing</li>
 *      <li>Changing</li>
 *      <li>Running</li>
 *      <li>Finished</li>
 *      <li>Aborted</li>
 *  </ul>
 * Normally a match has a {@link PointSystem}. This decides how the points are calculated in the match.
 * A match has a {@link GeneralMatchInfo} which contains the general information about the match.
 */
public abstract class Match<G extends Game> {

	private final MatchConfig      config;
	private final GeneralMatchInfo generalMatchInfo;
	private final Path             path;
	private       MatchState       state;
	private       MatchStatusInfo  statusInfo;
	private PointSystem<G> pointSystem;

	protected Match(MatchConfig config, GeneralMatchInfo generalMatchInfo, MatchStatusInfo statusInfo,
			PointSystem<G> pointSystem, Path path) {
		this.config = config;
		this.statusInfo = statusInfo;
		state = new NotStarted();
		this.generalMatchInfo = generalMatchInfo;
		this.path = path;
		this.pointSystem = pointSystem;
	}

	public static <G extends Game> List<G> getGames(Team<G>... teams) {
		var games = new LinkedList<G>();
		for (var team : teams) {
			for (var player : team.getPlayers()) {
				games.add(player.getGame());
				// Todo better generic handling
			}
		}
		return games;
	}

	//region setter/getter

	public List<Pair<Integer, Integer>> getPlayerForSet(int set) {
		var schema = config.getSchema();
		var back = new LinkedList<Pair<Integer, Integer>>();
		var lanesInSet = new LinkedList<LaneSchema.LaneSatz>();
		for (var lane : schema.getLanes()) {
			lanesInSet.add(lane.getSatze(set));
		}
		for (var lane : lanesInSet) {
			var team = lane.team();
			var player = lane.player();
			back.add(new Pair<>(team, player));
		}
		return back;
	}

	/**
	 * Will load the complete match from the file system.
	 */
	public void rereadAll() {

	}
	/**
	 * Returns the current set number of the match.
	 * <b>Starting by 1</b>
	 *
	 * @return Current set number
	 */
	public int getCurrentSet() {
		return statusInfo.getCurrentSet();
	}
	public MatchStatusInfo getStatusInfo() {
		return statusInfo;
	}

	public void setStatusInfo(MatchStatusInfo statusInfo) {
		this.statusInfo = statusInfo;
	}

	/**
	 * Returns the players that are currently playing. They are in the order of the lanes.
	 *
	 * @return List of players on the lanes
	 */
	public List<Player<G>> getCurrentPlayers() {
		var lanes = getPlayerForSet();
		var back = new LinkedList<Player<G>>();
		for (var lane : lanes) {
			back.add(getTeams()[lane.getKey()].getPlayers()[lane.getValue()]);
		}
		return back;
		/*
		var back = new LinkedList<Player<G>>();
		if (getGames().stream().allMatch(Game::isFinished)) {
			// load last players to display;
			var laneCount = config.getLaneCount();
			var teamCount = config.getTeams();
			var perTeam = laneCount / teamCount;
			// get the last players
			for (var team : getTeams()) {
				var players = team.getPlayers();
				for (var i = perTeam; i > 0; --i) {
					back.add(players[players.length - i]);
				}
			}
			return back;
		}
		for (G game : getGames()) {
			if (game.isOn()) {
				back.add(game.getPlayer());
			}
		}
		return back;*/
	}

	public List<Pair<Integer, Integer>> getPlayerForSet() {
		return getPlayerForSet(getCurrentSet());
	}

	public Path getBaseDir() {
		return path;
	}

	public void setPointSystem(PointSystem<G> pointSystem) {
		this.pointSystem = pointSystem;
	}

	public GeneralMatchInfo getGeneralMatchInfo() {
		return generalMatchInfo;
	}

	public List<G> getGames() {
		var back = new LinkedList<G>();
		for (var team : getTeams()) {
			for (var player : team.getPlayers()) {
				var game = player.getGame();
				if (game != null) {
					back.add(game);
				}
			}
		}
		return back;
	}

	public MatchConfig getConfig() {
		return config;
	}

	public abstract Team<G>[] getTeams();

	private String getState() {
		return state.name;
	}
//endregion


	private class MatchState {

		protected final String name;


		private MatchState(String name) {this.name = name;}
	}

	private class NotStarted extends MatchState {


		private NotStarted() {
			super("Not started");
		}
	}

	private class Preparing extends MatchState {


		private Preparing() {
			super("Preparing");
		}
	}

	private class Changing extends MatchState {

		private Changing() {
			super("Changing");
		}
	}

	private class Running extends MatchState {


		private Running() {
			super("Running");
		}
	}

	private class Finished extends MatchState {
		private Finished() {
			super("Finished");
		}
	}
}
