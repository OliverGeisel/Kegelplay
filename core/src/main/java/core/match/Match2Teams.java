package core.match;

import core.game.Game;
import core.game.Game120;
import core.point_system.GamePointsEvaluator_2Players;
import core.point_system.GameSetPoints;
import core.point_system.PointSystem;
import core.team_and_player.Player;
import core.team_and_player.Team;
import core.util.Pair;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static core.point_system.Evaluate2TeamsPair.evaluateMatchAsPoints;

/**
 * Represents a match between two {@link Team}s.
 *
 * @param <G> The type of the {@link Game} that is played in this match.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @since 1.0.0
 */
public class Match2Teams<G extends Game> extends Match<G> {

	private final Team<G> home;
	private final Team<G> guest;


	/**
	 * Creates a new match between two teams.
	 *
	 * @param config           The configuration of the match.
	 * @param generalMatchInfo The general information about the match.
	 * @param statusInfo       The state about the match.
	 * @param pointSystem      The point system of the match.
	 * @param home             The home team.
	 * @param guest            The guest team.
	 * @param path             The path to the match in the file system.
	 */
	public Match2Teams(MatchConfig config, GeneralMatchInfo generalMatchInfo, MatchStatusInfo statusInfo,
			PointSystem<G> pointSystem, Team<G> home, Team<G> guest, Path path) {
		super(config, generalMatchInfo, statusInfo, pointSystem, path);
		this.home = home;
		this.guest = guest;
	}

	/**
	 * Returns the player of the home team with the given name.
	 * @param name The name of the player.
	 * @return The player with the given name.
	 */
	public Player<G> getHomePlayerByName(String name) {
		return home.getPlayer(name);
	}

	/**
	 * Returns the player of the guest team with the given name.
	 * @param name The name of the player.
	 * @return The player with the given name.
	 */
	public Player<G> getGuestPlayerByName(String name) {
		return guest.getPlayer(name);
	}

	/**
	 * Returns the player of the home team with the given position.
	 * @param position The position of the player.
	 * @return The player with the given position.
	 */
	public Player<G> getHomePlayerByPosition(int position) {
		return home.getPlayer(position);
	}

	/**
	 * Returns the player of the guest team with the given position.
	 * @param position The position of the player.
	 * @return The player with the given position.
	 */
	public Player<G> getGuestPlayerByPosition(int position) {
		return guest.getPlayer(position);
	}

	/**
	 * Returns the pair of players with the given position.
	 * @param position The position of the players.
	 * @return The pair of players with the given position.
	 */
	public Pair<Player<G>, Player<G>> getPairByPosition(int position) {
		return new Pair<>(home.getPlayer(position), guest.getPlayer(position));
	}

	//region setter/getter

	/**
	 * Returns the home team.
	 * @return the home team.
	 */
	public Team<G> getHome() {
		return home;
	}

	/**
	 * Returns the guest team.
	 *
	 * @return the guest team.
	 */
	public Team<G> getGuest() {
		return guest;
	}

	@Override
	public Map<String, Double> getPoints() {
		var back = new HashMap<String, Double>();
		getPointSystem().getMatchPoints(this);
		var map = evaluateMatchAsPoints((Match2Teams<Game120>) this);
		for (var team : getTeams()) {
			for (var player : team.getPlayers()) {
				back.put(player.getCompleteName(), map.get(player.getCompleteName()));
			}
			back.put(team.getName(), map.get(team.getName()));
		}
		return back;
	}

	@Override
	public Map<String, Double> getSetPoints() {
		Map<String, Double> back = new HashMap<>();
		var teams = getTeams();
		var team1Sum = 0.0;
		var team2Sum = 0.0;
		for (int i = 0; i < teams[0].getPlayers().length; i++) {
			var gamePoints = GamePointsEvaluator_2Players.evaluate(teams[0].getPlayer(i), teams[1].getPlayer(i));
			var player1 = gamePoints.getFirst();
			var player2 = gamePoints.getSecond();
			var sum1 = player1.getGameSetPoints().stream().map(GameSetPoints::getPoints).reduce(0.0, Double::sum);
			var sum2 = player2.getGameSetPoints().stream().map(GameSetPoints::getPoints).reduce(0.0, Double::sum);
			back.put(teams[0].getPlayer(i).getCompleteName(),
					sum1);
			back.put(teams[1].getPlayer(i).getCompleteName(),
					sum2);
			team1Sum += sum1;
			team2Sum += sum2;
		}
		back.put(teams[0].getName(), team1Sum);
		back.put(teams[1].getName(), team2Sum);
		return back;
	}

	@Override
	public Team<G>[] getTeams() {
		return new Team[]{home, guest};
	}
//endregion
}
