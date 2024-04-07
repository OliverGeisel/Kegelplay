package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.GameSet;
import de.olivergeisel.kegelplay.core.team_and_player.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * The points of a {@link GameSet} for each player.
 */
public class GameSetPointsCollection {

	private final Map<Player, Double>  scores   = new HashMap<>();
	private final Map<Player, GameSet> gameSets = new HashMap<>();

	public GameSetPointsCollection() {

	}

	public void setScore(Player player, double score, GameSet gameSet) {
		scores.put(player, score);
		gameSets.put(player, gameSet);
	}

	public double getScore(Player player) {
		return scores.get(player);
	}

	public GameSetPoints getGamePoints(Player player) {
		return new GameSetPoints(player, gameSets.get(player), scores.get(player));
	}

//region setter/getter
	public int getGameSetNumber() throws IllegalStateException {
		var gameSetNumber = -1;
		for (GameSet gameSet : gameSets.values()) {
			if (gameSetNumber == -1) {
				gameSetNumber = gameSet.getGameSetNumber();
			}
			if (gameSet.getGameSetNumber() != gameSetNumber) {
				throw new IllegalStateException("All players must have the same game set number");
			}
		}
		return gameSetNumber;
	}
//endregion
}
