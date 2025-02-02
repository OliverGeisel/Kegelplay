package core.point_system;

import core.game.Game;
import core.match.Match;
import core.team_and_player.Player;
import core.team_and_player.Team;

/**
 * Represents the point that one {@link Player} get for his {@link Team} in the {@link Match}.
 * This point is used to determine the winner of a match.
 *
 * @param <G> The type of the game the player is playing.
 */
public class GamePointsTeam<G extends Game> extends GamePoints<G> {

	/**
	 * The points of the player in the game set. Can be 0, 0.5 or 1. 0.5 means that the player has a draw.
	 */
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

	/**
	 * Set the points of the player in the game set. Can be 0, 0.5 or 1. 0.5 means that the player has a draw.
	 *
	 * @param points
	 */
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
