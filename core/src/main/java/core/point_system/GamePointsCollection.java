package core.point_system;

import core.game.Game;
import core.match.Match;
import core.team_and_player.Player;

import java.util.*;

/**
 * The points of a game for each player.
 *
 * @param <G> The type of the game that is played in the match.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @see Match
 * @since 1.0.0
 */
public class GamePointsCollection<G extends Game> {

	private final Map<Player<G>, List<GameSetPointsCollection>> scores;


	/**
	 * Creates a new GamePointsCollection with the given players.
	 *
	 * @param players The players that get a score
	 */
	public GamePointsCollection(Collection<Player<G>> players) {
		scores = new HashMap<>();
		for (var player : players) {
			scores.put(player, new LinkedList<>());
		}
	}

	/**
	 * Returns the points of a player in a specific game set.
	 *
	 * @param playerName    Player name
	 * @param gameSetNumber Game set number
	 * @return The points of the player in the game set. Can be 0 if the was not found.
	 */
	public double getGameSetPointsFor(String playerName, int gameSetNumber) {
		var player =
				scores.keySet().stream().filter(p -> p.getCompleteNameWithUnderscore().equals(playerName)).findFirst()
					  .orElse(null);
		return scores.get(player).stream()
					 .filter(it -> it.getGameSetNumber() == gameSetNumber)
					 .mapToDouble(it -> it.getGamePoints(player).getPoints()).sum();

	}

	/**
	 * Sets the score of a player.
	 *
	 * @param player Player that gets the score
	 * @param score  List of GameSetPointsCollection (all points of the {@link core.game.GameSet}s)
	 */
	public void setScore(Player<G> player, List<GameSetPointsCollection> score) {
		scores.put(player, score);
	}

	/**
	 * Returns the score of a player.
	 *
	 * @param player Player that gets the score
	 * @return The score of the player
	 */
	public double getScore(Player<G> player) {
		var score = 0.;
		for (GameSetPointsCollection set : scores.get(player)) {
			score += set.getScore(player);
		}
		return score;
	}

	/**
	 * Sets/adds GameSetPointsCollection to the player.
	 *
	 * @param player Player that gets the score
	 * @param set    GameSetPointsCollection that gets added
	 */
	public void addGameSetPoints(Player<G> player, GameSetPointsCollection set) {
		scores.computeIfAbsent(player, k -> new LinkedList<>());
		scores.get(player).add(set);
	}

	//region setter/getter

	/**
	 * Returns the players that have a score.
	 *
	 * @return List of players with a score
	 */
	public List<Player<G>> getPlayers() {
		return new ArrayList<>(scores.keySet());
	}


	/**
	 * Returns the score of all players.
	 *
	 * @return Map of players and their score
	 */
	public Map<Player<G>, Double> getMapping() {
		var back = new HashMap<Player<G>, Double>();
		for (var player : scores.keySet()) {
			var score = getScore(player);
			back.put(player, score);
		}
		return back;
	}
//endregion
}
