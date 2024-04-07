package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.team_and_player.Player;

public class GamePointsTeam<G extends Game> extends GamePoints<G> {

	private double points;

	public GamePointsTeam(Player<G> player, double points) {
		super(player);
		this.points = points;
	}

	//region setter/getter
	@Override
	public double getPoints() {
		return points;
	}

	public void setPoints(double points) {
		this.points = points;
	}

	@Override
	public boolean isWinner() {
		return points == 1;
	}

	@Override
	public boolean isLoser() {
		return points == 0;
	}

	@Override
	public boolean isDraw() {
		return points == 0.5;
	}
//endregion
}
