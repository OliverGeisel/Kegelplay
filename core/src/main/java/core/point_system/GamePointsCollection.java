package core.point_system;

import core.game.Game;
import core.team_and_player.Player;

import java.util.*;

/**
 * The points of a game for each player.
 */
public class GamePointsCollection<G extends Game> {

	private final Map<Player<G>, List<GameSetPointsCollection>> scores;


	public GamePointsCollection(Collection<Player<G>> players) {
		scores = new HashMap<>();
		for (Player player : players) {
			scores.put(player, new LinkedList<>());
		}
	}

	public double getGameSetPointsFor(String playerName, int gameSetNumber) {
		Player player =
				scores.keySet().stream().filter(p -> p.getCompleteNameWithUnderscore().equals(playerName)).findFirst()
					  .orElse(null);
		return scores.get(player).stream()
					 .filter(it -> it.getGameSetNumber() == gameSetNumber)
					 .mapToDouble(it -> it.getGamePoints(player).getPoints()).sum();

	}

	public void setScore(Player player, List<GameSetPointsCollection> score) {
		scores.put(player, score);
	}

	public double getScore(Player player) {
		var score = 0.;
		for (GameSetPointsCollection set : scores.get(player)) {
			score += set.getScore(player);
		}
		return score;
	}

	public void addGameSetPoints(Player player, GameSetPointsCollection set) {
		scores.computeIfAbsent(player, k -> new LinkedList<>());
		scores.get(player).add(set);
	}

	//region setter/getter
	public List<Player<G>> getPlayers() {
		return new ArrayList<>(scores.keySet());
	}


	public <G extends Game> Map<Player<G>, Double> getMapping() {
		var back = new HashMap<Player<G>, Double>();
		for (var entry : scores.keySet()) {
			var player = (Player<G>) entry;
			var score = getScore(player);
			back.put(player, score);
		}
		return back;
	}
//endregion
}
