package core.point_system;

import core.game.Game;
import core.game.GameSet;
import core.team_and_player.Player;

/**
 * Represents the points of a {@link Player} in a {@link GameSet}. This point is used to determine the winner of a match.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Player
 * @see GameSet
 * @see Game
 * @since 1.0.0
 */
public class GameSetPoints {
	private double points;
	private Player  player;
	private GameSet gameSet;

	/**
	 * Create a new GameSetPoints with the given values.
	 *
	 * @param player  The player that played the {@link Game} and GameSet.
	 * @param gameSet The related {@link GameSet} that get the points.
	 * @param points  The points of the player in the game set. Can be 0, 0.5 or 1. 0.5 means that the player has a draw.
	 */
	public GameSetPoints(Player player, GameSet gameSet, double points) {
		this.points = points;
		this.player = player;
		this.gameSet = gameSet;
	}

	//region setter/getter

	/**
	 * The player that played the {@link Game} and GameSet.
	 *
	 * @return The player that played the {@link Game} and GameSet.
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Get the related {@link GameSet} that get the points.
	 *
	 * @return The related {@link GameSet} that get the points.
	 */
	public GameSet getGameSet() {
		return gameSet;
	}

	/**
	 * Get the points of the player in the game set. Can be 0, 0.5 or 1. 0.5 means that the player has a draw.
	 *
	 * @return The points of the player in the game set.
	 */
	public double getPoints() {
		return points;
	}
//endregion
}
