package core.point_system;

import core.game.Game;
import core.game.GameSet;
import core.match.Match;
import core.team_and_player.Player;

import java.util.*;

/**
 * A PointSystem is a claculation system for a match.
 * This system decides who will win or lose a match.
 * A Draw is possible too.
 * A match can be decided by different factors. So the Total Teamscore, the points per Player or the points per Set
 * can be selected. The concrete implementation of the PointSystem decides the winner.
 * The important method is the .getMatchPoints() method. This method calculates the points for a match.
 * For subparts of a match, like a {@link Game} or a {@link GameSet}, there are other methods to calculate the points.
 *
 * @param <G> The type of the {@link Game} that is played in the match.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @see Game
 * @see GameSet
 * @since 1.0.0
 */
public abstract class PointSystem<G extends Game> {

	private static final PointType pointType          = PointType.PER_DURCHGANG;
	private static final int[]     pointsPerDurchgang = {4, 3, 2, 1};

	protected String description;

	protected PointSystem() {
	}

	public abstract MatchPoints getMatchPoints(Match<G> match);

	//region setter/getter
	public String getDescription() {
		return description;
	}
//endregion

	public static Map<Player, Integer> getPlayerPoints(int durchgang, Player... players) {
		if (players.length != 4) {
			throw new IllegalArgumentException("Es müssen genau 4 Spieler übergeben werden");
		}
		var playerPoints = new ArrayList<PlayerScore>(4);
		var scores = Arrays.stream(players).mapToInt(player -> player.getGame().getDurchgang(durchgang)
																	 .getScore()).toArray();
		for (int i = 0; i < players.length; i++) {
			playerPoints.add(new PlayerScore(players[i], scores[i]));
		}
		return evalScores(playerPoints);
	}

	private static Map<Player, Integer> evalScores(List<PlayerScore> scores) {
		int pointPos = 0;
		var grouping = new TreeMap<Integer, List<PlayerAndPoint>>(Comparator.reverseOrder());
		for (var player : scores) {
			var list = grouping.getOrDefault(player.score(), new ArrayList<>());
			list.add(new PlayerAndPoint(player.player(), pointsPerDurchgang[pointPos]));
			grouping.put(player.score(), list);
		}
		var back = new HashMap<Player, Integer>();
		for (var entry : grouping.entrySet()) {
			var list = entry.getValue();
			var sum = list.stream().mapToInt(PlayerAndPoint::getPoints).sum();
			var perPlayer = sum / list.size();
			for (var player : list) {
				back.put(player.getPlayer(), perPlayer);
			}
		}
		return back;
	}

	private static class PlayerAndPoint {
		private Player player;
		private int    points;

		public PlayerAndPoint(Player player) {
			this.player = player;
		}

		public PlayerAndPoint(Player player, int points) {
			this.player = player;
			this.points = points;
		}

//region setter/getter
		public Player getPlayer() {
			return player;
		}

		public void setPlayer(Player player) {
			this.player = player;
		}

		public int getPoints() {
			return points;
		}

		public void setPoints(int points) {
			this.points = points;
		}
//endregion


	}

	private record PlayerScore(Player player, int score) {
	}
}
