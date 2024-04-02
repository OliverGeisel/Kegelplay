package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.GameSet;
import de.olivergeisel.kegelplay.core.team_and_player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * The points of a game for each player.
 */
public class GameSetPointsCollection {
	private Map<Player, Double>  scores   = new HashMap<>();
	private Map<Player, GameSet> gameSets = new HashMap<>();

	public GameSetPointsCollection() {
	}

	public void setScore(Player player, double score, GameSet gameSet) {
		scores.put(player, scores.get(player) + score);
		gameSets.put(player, gameSet);
	}

	public double getScore(Player player) {
		return scores.get(player);
	}

	public GameSetPoints getGamePoints(Player player) {
		return new GameSetPoints(player, gameSets.get(player), scores.get(player));
	}
}
