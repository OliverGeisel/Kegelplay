package core.point_system;

import core.game.Game;
import core.team_and_player.Player;

import java.util.List;

public class GamePointsPlayer<G extends Game> extends GamePoints<G> {


	protected GamePointsPlayer(Player<G> player) {
		super(player);
	}

	protected GamePointsPlayer(Player<G> player1, List<GameSetPoints> gameSetPoints) {
		super(player1, gameSetPoints);
	}

	//region setter/getter
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
