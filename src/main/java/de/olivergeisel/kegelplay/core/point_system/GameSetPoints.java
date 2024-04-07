package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.GameSet;
import de.olivergeisel.kegelplay.core.team_and_player.Player;

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
