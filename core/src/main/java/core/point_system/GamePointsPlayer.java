package core.point_system;

import core.game.Game;
import core.team_and_player.Player;

import java.util.List;

/**
 * The GamePointsPlayer represents the points of a player in a game.
 * It contains the points per {@link core.game.GameSet} in the {@link Game}.
 * Also, it contains the points for the team (1, 0.5 or 0).
 *
 * @param <G> The type of the game
 */
public class GamePointsPlayer<G extends Game> extends GamePoints<G> {

	/**
	 * The points for the team. Can be 1, 0.5 or 0.
	 */
	private double teamPoints;


	protected GamePointsPlayer(Player<G> player) {
		super(player);
	}

	protected GamePointsPlayer(Player<G> player1, List<GameSetPoints> gameSetPoints) {
		super(player1, gameSetPoints);
	}

	//region setter/getter
	public double getTeamPoints() {
		return teamPoints;
	}

	public void setTeamPoints(double teamPoints) {
		this.teamPoints = teamPoints;
	}

	@Override
	public double getPoints() {
		return getSumGameSetPoints();
	}

	@Override
	public boolean isWinner() {
		throw new UnsupportedOperationException("Cannot determine if a player is a winner in a single player game");
	}

	@Override
	public boolean isLoser() {
		throw new UnsupportedOperationException("Cannot determine if a player is a loser in a single player game");
	}

	@Override
	public boolean isDraw() {
		throw new UnsupportedOperationException("Cannot determine if a player is a draw in a single player game");
	}
//endregion
}
