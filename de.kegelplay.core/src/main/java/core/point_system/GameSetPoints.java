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

	public GameSetPoints(Player player, GameSet gameSet, double points) {
		this.points = points;
		this.player = player;
		this.gameSet = gameSet;
	}

	//region setter/getter
	public Player getPlayer() {
		return player;
	}

	public GameSet getGameSet() {
		return gameSet;
	}

	public double getPoints() {
		return points;
	}
//endregion
}
