package core.point_system;

import core.game.Game;
import core.match.Match;
import core.team_and_player.Player;

import java.util.Map;

/**
 * The match score for a match where all players play against each other.
 * There is only one winner in this match.
 *
 * @see PointSystemCriteria#PLAYER
 * @see MatchPoints
 * @see Match
 *
 * @param <G> the type of the game that is played in the match
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class AllAgainstMatchScore<G extends Game> extends MatchPoints<Player<G>> {

	private final GamePointsCollection gamePointsCollection;

	public AllAgainstMatchScore(GamePointsCollection gamePointsCollection) {
		super();
		this.gamePointsCollection = gamePointsCollection;
	}


	/**
	 * Return the points of the winner.
	 *
	 * @return the points of the winner
	 */
	@Override
	public double winnerPoints() {
		// todo: check if this need to be implemented
		return 0;
	}

	@Override
	public double getGameSetPointsFor(String player, int gameSetNumber) {
		return gamePointsCollection.getGameSetPointsFor(player, gameSetNumber);
	}

//region setter/getter

	/**
	 * Return the concrete points of the match per partie.
	 *
	 * @return a map with the points of the match
	 */
	@Override
	public Map<Player<G>, Double> getMatchPoints() {
		return gamePointsCollection.getMapping();
	}

	/**
	 * When all parties have equal points, the match is a draw.
	 *
	 * @return true if the match is a draw
	 */
	@Override
	public boolean isDraw() {
		return false;
	}

	/**
	 * Return the winner of the match.
	 *
	 * @return the winner of the match
	 */
	@Override
	public Player<G> getWinner() {
		Player<G> winner = null;
		for (Player<G> player : gamePointsCollection.getPlayers()) {
			if (winner == null) {
				winner = player;
			}
			if (gamePointsCollection.getScore(player) > gamePointsCollection.getScore(winner)) {
				winner = player;
			}
		}
		return winner;
	}
//endregion
}
