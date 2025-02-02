package core.match;

import core.game.Game;
import core.team_and_player.Player;
import core.team_and_player.Team;

import java.util.List;

/**
 * A game round is a single round of a game. It contains the information about the {@link Player} and the points they scored.
 * All players in a game round play at the same time. The players can be in different {@link Team}.
 *
 * @param games       The games of the round.
 *                    The games must be in the same round and must be played by different players.
 * @param roundNumber The number of the round.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @since 1.0.0
 */
public record GameRound(List<Game> games, int roundNumber) {

	/**
	 * Creates a new game round.
	 *
	 * @param games       The games of the round.
	 *                    The games must be in the same round and must be played by different players.
	 * @param roundNumber The number of the round.
	 */
	public GameRound {
		if (games.isEmpty()) {
			throw new IllegalArgumentException("A game round must contain at least one game.");
		}
		if (roundNumber < 1) {
			throw new IllegalArgumentException("The round number must be at least 1.");
		}
	}

//region setter/getter

	/**
	 * Get the players of the games in an ordered list.
	 *
	 * @return The players of the games.
	 */
	public List<Player> getPlayers() {
		return games.stream().map(Game::getPlayer).toList();
	}

	/**
	 * Get the number of players in the game round.
	 *
	 * @return The number of players in the game round.
	 */
	public int getNumberOfPlayers() {
		return games.size();
	}
//endregion
}
