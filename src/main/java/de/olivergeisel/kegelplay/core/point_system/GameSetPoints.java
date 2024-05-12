package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.game.GameSet;
import de.olivergeisel.kegelplay.core.team_and_player.Player;

/**
 * Represents the points of a {@link Player} in a {@link GameSet}. This point is used to determine the winner of a match.
 *
 * @see Player
 * @see GameSet
 * @see Game
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
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
