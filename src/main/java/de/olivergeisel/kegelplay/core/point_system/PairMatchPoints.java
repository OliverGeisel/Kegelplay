package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.team_and_player.Player;

import java.util.LinkedList;
import java.util.Map;

public class PairMatchPoints<G extends Game> extends MatchPoints<Player<G>> {

	private final Player<G> player1;
	private final Player<G> player2;
	private final GamePointsPlayer<G> gamePointsPlayer1;
	private final GamePointsPlayer<G> gamePointsPlayer2;

	public PairMatchPoints(Player<G> player1, Player<G> player2) {
		super();
		this.player1 = player1;
		this.player2 = player2;
		var player1GamePoints = new LinkedList<GameSetPoints>();
		var player2GamePoints = new LinkedList<GameSetPoints>();

		for (int i = 0; i < 4; i++) {
			var gameSet1 = player1.getGame().getDurchgang(i);
			var gameSet2 = player2.getGame().getDurchgang(i);
			if (gameSet1.getScore() > gameSet2.getScore()) {
				player1GamePoints.add(new GameSetPoints(player1, gameSet1, 1));
				player2GamePoints.add(new GameSetPoints(player2, gameSet2, 0));
			} else if (gameSet1.getScore() < gameSet2.getScore()) {
				player1GamePoints.add(new GameSetPoints(player1, gameSet1, 0));
				player2GamePoints.add(new GameSetPoints(player2, gameSet2, 1));
			} else {
				player1GamePoints.add(new GameSetPoints(player1, gameSet1, 0.5));
				player2GamePoints.add(new GameSetPoints(player2, gameSet2, 0.5));
			}
		}
		this.gamePointsPlayer1 = new GamePointsPlayer<>(player1, player1GamePoints);
		this.gamePointsPlayer2 = new GamePointsPlayer<>(player2, player2GamePoints);
	}

	/**
	 * Return the points of the winner.
	 *
	 * @return the points of the winner
	 */
	@Override
	public double winnerPoints() {
		if (isDraw()) {
			return 0.5;
		}
		return getMatchPoints().get(getWinner());
	}

	@Override
	public double getGameSetPointsFor(String player, int gameSetNumber) throws IllegalArgumentException {
		if (player1.getCompleteName().equals(player)) {
			return gamePointsPlayer1.getGameSetPointsFor(gameSetNumber);
		} else if (player2.getCompleteName().equals(player)) {
			return gamePointsPlayer2.getGameSetPointsFor(gameSetNumber);
		}
		throw new IllegalArgumentException("Player not found");
	}

	// region setter/getter

	/**
	 * Return the winner of the match.
	 *
	 * @return the winner of the match
	 * @throws IllegalStateException if the match is a draw
	 */
	@Override
	public Player<G> getWinner() throws IllegalStateException {
		if (gamePointsPlayer1.getPoints() > gamePointsPlayer2.getPoints()) {
			return player1;
		} else if (gamePointsPlayer1.getPoints() < gamePointsPlayer2.getPoints()) {
			return player2;
		}
		var score1 = gamePointsPlayer1.getPlayer().getGame().getTotalScore();
		var score2 = gamePointsPlayer2.getPlayer().getGame().getTotalScore();
		if (score1 == score2) {
			throw new IllegalStateException("Match is a draw");
		}
		return score1 > score2 ? player1 : player2;
	}

	/**
	 * Return the concrete points of the match per partie.
	 *
	 * @return a map with the points of the match
	 */
	@Override
	public Map<Player<G>, Double> getMatchPoints() {
		return Map.of(player1, gamePointsPlayer1.getPoints(), player2, gamePointsPlayer2.getPoints());
	}

	/**
	 * When all parties have equal points, the match is a draw.
	 *
	 * @return true if the match is a draw
	 */
	@Override
	public boolean isDraw() {
		var score1 = gamePointsPlayer1.getPlayer().getGame().getTotalScore();
		var score2 = gamePointsPlayer2.getPlayer().getGame().getTotalScore();
		return gamePointsPlayer1.getPoints() == gamePointsPlayer2.getPoints() && score1 == score2;
	}
	// endregion
}
