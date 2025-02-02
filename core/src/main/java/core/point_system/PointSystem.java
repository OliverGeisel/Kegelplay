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

	/**
	 * Only for inheritance.
	 */
	protected PointSystem() {
	}

	/**
	 * Get the Points for a {@link GameSet}.
	 * So these points are added at the end of the set.
	 *
	 * @param gameSetNumber number of the gameSet (starting by 0)
	 * @param players       the array of players to set the points for
	 * @return The points for the set.
	 */
	public static Map<Player, Integer> getPlayerPoints(int gameSetNumber, Player... players) {
		if (players.length != 4) {
			throw new IllegalArgumentException("Es müssen genau 4 Spieler übergeben werden");
		}
		var playerPoints = new ArrayList<PlayerScore>(4);
		var scores = Arrays.stream(players).mapToInt(player -> player.getGame().getGameSet(gameSetNumber)
																	 .getScore()).toArray();
		for (int i = 0; i < players.length; i++) {
			playerPoints.add(new PlayerScore(players[i], scores[i]));
		}
		return evalScores(playerPoints);
	}

	/**
	 * eval the scores of the players.
	 *
	 * @param scores The scores of the players.
	 * @return The points for the players as a map.
	 */
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

	/**
	 * Get the winner of a match.
	 * This can have multiple winners, if the match is a draw or the Pointssystem allows multiple winners (like
	 * Qualifiers for a next round).
	 *
	 * @param match The match to get the winner from.
	 * @return A list of Winners.
	 */
	public abstract List<Winner> getWinner(Match<G> match);

	/**
	 * Get the Points for the complete {@link Match}.
	 * So these points are added at the end in the league table.
	 */
	public abstract MatchPoints getMatchPoints(Match<G> match);

	//region setter/getter
	public String getDescription() {
		return description;
	}
//endregion

	/**
	 * Helper class to store a Player and his Points.
	 *
	 * @author Oliver Geisel
	 * @version 1.0.0
	 * @see Player
	 * @see PointSystem
	 * @since 1.0.0
	 */
	private static class PlayerAndPoint {
		private Player player;
		private int    points;

		/**
		 * Creates a new PlayerAndPoint with the given player.
		 *
		 * @param player The player
		 */
		public PlayerAndPoint(Player player) {
			this.player = player;
		}

		/**
		 * Creates a new PlayerAndPoint with the given player and points.
		 *
		 * @param player The player
		 * @param points The points of the player
		 */
		public PlayerAndPoint(Player player, int points) {
			this.player = player;
			this.points = points;
		}

		//region setter/getter

		/**
		 * Returns the player.
		 *
		 * @return The player
		 */
		public Player getPlayer() {
			return player;
		}

		/**
		 * Sets the player.
		 *
		 * @param player The player
		 */
		public void setPlayer(Player player) {
			this.player = player;
		}

		/**
		 * Returns the points of the player.
		 *
		 * @return The points of the player
		 */
		public int getPoints() {
			return points;
		}

		/**
		 * Sets the points of the player.
		 *
		 * @param points The points of the player
		 */
		public void setPoints(int points) {
			this.points = points;
		}
//endregion


	}

	/**
	 * Helper class to store a Player and his Score.
	 *
	 * @author Oliver Geisel
	 * @version 1.0.0
	 * @see Player
	 * @see PointSystem
	 * @since 1.0.0
	 */
	private record PlayerScore(Player player, int score) {
	}
}
