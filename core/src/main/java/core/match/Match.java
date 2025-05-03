package core.match;

import core.game.Game;
import core.point_system.PointSystem;
import core.team_and_player.Player;
import core.team_and_player.Team;
import core.util.Pair;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


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
 *
 * @param <G> The type of the {@link Game} the match is playing.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @since 1.0.0
 */
public abstract class Match<G extends Game> {

	private final MatchConfig      config;
	private final GeneralMatchInfo generalMatchInfo;
	private final Path             path;
	private       MatchState       state;
	private       MatchStatusInfo  statusInfo;
	private PointSystem<G> pointSystem;

	/**
	 * Creates a new match.
	 *
	 * @param config           The configuration of the match.
	 * @param generalMatchInfo The general information about the match.
	 * @param statusInfo       The state about the match.
	 * @param pointSystem      The point system of the match.
	 * @param path             The path to the match in the file system.
	 */
	protected Match(MatchConfig config, GeneralMatchInfo generalMatchInfo, MatchStatusInfo statusInfo,
			PointSystem<G> pointSystem, Path path) {
		this.config = config;
		this.statusInfo = statusInfo;
		state = new NotStarted();
		this.generalMatchInfo = generalMatchInfo;
		this.path = path;
		this.pointSystem = pointSystem;
	}

	/**
	 * Helper function to get the games for the given teams.
	 *
	 * @param teams The teams to get the games from.
	 * @param <G>   The type of the game.
	 * @return List of games from the teams.
	 */
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

	/**
	 * Return the integer encoding for team and player of the given setNumber.
	 * First value is the team number, second value is the player number.
	 *
	 * @param setNumber The number of the set
	 * @return List of pairs with the team and player number
	 */
	public List<Pair<Integer, Integer>> getPlayerForSet(int setNumber) {
		var schema = config.getSchema();
		var back = new LinkedList<Pair<Integer, Integer>>();
		var lanesInSet = new LinkedList<LaneSchema.LaneSatz>();
		for (var lane : schema.getLanes()) {
			lanesInSet.add(lane.getSatze(setNumber));
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

	//region setter/getter

	/**
	 * Returns the name of the match. The name contains the teams and the date of the match.
	 *
	 * @return The name of the match
	 */
	public String getName() {
		return STR."Match: \{getTeamVs()} am \{getStatusInfo().getStartTime()
															  .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))}";
	}

	/**
	 * Returns the point system of the match.
	 *
	 * @return The point system of the match
	 */
	public PointSystem<G> getPointSystem() {
		return pointSystem;
	}

	/**
	 * Sets the point system of the match.
	 *
	 * @param pointSystem The point system of the match
	 */
	public void setPointSystem(PointSystem<G> pointSystem) {
		this.pointSystem = pointSystem;
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

	/**
	 * Returns the current state of the match.
	 *
	 * @return Current state of the match
	 */
	public MatchStatusInfo getStatusInfo() {
		return statusInfo;
	}

	/**
	 * Sets the current state of the match.
	 *
	 * @param statusInfo The current state of the match
	 */
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
			if (lane.getKey() < 0 || lane.getValue() < 0) { // Ignore empty lanes
				continue;
			}
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

	/**
	 * Returns the mapping of the current players to the lanes.
	 *
	 * @return Mapping of the players to the lanes
	 */
	public Map<Player<G>, String> getCurrentPlayerBahnMapping() {
		var bahnNames = config.getLaneNames();
		var players = getCurrentPlayers();
		var back = new HashMap<Player<G>, String>();
		int i = 0;
		for (var player : players) {
			back.put(player, bahnNames.get(i++));
		}
		return back;
	}

	/**
	 * Returns the integer-encoding for team and player for the current {@link core.game.GameSet}.
	 *
	 * @return List of pairs with the team and player number
	 */
	public List<Pair<Integer, Integer>> getPlayerForSet() {
		return getPlayerForSet(getCurrentSet());
	}

	/**
	 * Returns the path to the match where the data is stored.
	 *
	 * @return Path to the match
	 */
	public Path getBaseDir() {
		return path;
	}

	/**
	 * Get the {@link GeneralMatchInfo} of the match.
	 *
	 * @return The general information of the match
	 */
	public GeneralMatchInfo getGeneralMatchInfo() {
		return generalMatchInfo;
	}

	/**
	 * Returns all games that are in the match.
	 * The Order is the order of the teams and the players in the teams.
	 *
	 * @return List of games in the match
	 */
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

	/**
	 * Get the {@link Game}s ordered by the Position in the Team.
	 * The first game ist the {@link Player} who start at track 1, second on track 2 etc.
	 *
	 * @return
	 */
	public List<G> getGamesByPlayerPos() {
		var back = new LinkedList<G>();
		var numberPlayers = getConfig().getPlayersPerTeam() * getConfig().getSchema().getCycles();
		for (var pos = 0; pos < numberPlayers; ++pos) {
			for (var team : getTeams()) {
				var player = team.getPlayers()[pos];
				var game = player.getGame();
				if (game != null) {
					back.add(game);
				}
			}
		}
		return back;
	}

	/**
	 * Returns the games in the match grouped by the rounds.
	 * A round is a set of games that are played at the same time.
	 *
	 * @return List of rounds with the games in it
	 */
	public List<List<G>> getGameRounds() {
		var back = new LinkedList<List<G>>();
		var laneCount = config.getLaneCount();
		var games = getGamesByPlayerPos();
		for (var i = 0; i < games.size(); i += laneCount) {
			var round = new LinkedList<G>();
			for (var j = 0; j < laneCount; ++j) {
				round.add(games.get(i + j));
			}
			back.add(round);
		}
		return back;
	}

	/**
	 * Returns the current game round.
	 * A round is a set of games that are played at the same time.
	 *
	 * @return List of games in the current round
	 */
	public MatchConfig getConfig() {
		return config;
	}

	/**
	 * Get the TeamPoints per player or team.
	 *
	 * @return Map of player/team with TeamPoints
	 */
	public abstract Map<String, Double> getPoints();

	/**
	 * Get the SetPoints per player or team.
	 *
	 * @return Map of player/team with SetPoints
	 */
	public abstract Map<String, Double> getSetPoints();

	/**
	 * Get the teams that are playing in the match.
	 *
	 * @return The teams that are playing in the match
	 */
	public abstract Team<G>[] getTeams();

	/**
	 * Returns a string with the names of the teams that are playing against each other.
	 *
	 * @return String with the names of the teams
	 */
	protected String getTeamVs() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < getTeams().length; i++) {
			builder.append(getTeams()[i].getName());
			if (i < getTeams().length - 1) {
				builder.append(" vs ");
			}
		}
		return builder.toString();
	}

	/**
	 * Returns the current state of the match as a string.
	 *
	 * @return Current state of the match
	 */
	private String getState() {
		return state.name;
	}
//endregion


	/**
	 * Internal state of the match.
	 */
	private static class MatchState {

		protected final String name;

		/**
		 * Creates a new match state with the given name.
		 *
		 * @param name The name of the state
		 */
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
