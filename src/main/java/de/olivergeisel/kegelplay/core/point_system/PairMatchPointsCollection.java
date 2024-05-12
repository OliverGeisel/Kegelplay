package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.core.team_and_player.Player;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PairMatchPointsCollection extends MatchPoints<Player<Game120>> {

	private List<PairMatchPoints> matchPointsList;

	public PairMatchPointsCollection() {
		matchPointsList = new LinkedList<>();
	}

	/**
	 * Add the points of a match to the collection.
	 *
	 * @param matchPoints the points of the match
	 */
	public void addMatchPoints(PairMatchPoints matchPoints) {
		matchPointsList.add(matchPoints);
	}

	/**
	 * Return the points of the winner.
	 *
	 * @return the points of the winner
	 */
	@Override
	public double winnerPoints() {
		return 0;
	}

	/**
	 * Return the points of a player for a specific game set.
	 *
	 * @param player        the player to get the points for
	 * @param gameSetNumber the game set to get the points for
	 * @return the points of the player for the game set
	 * @throws IllegalArgumentException if the player is not found or the game set number is not valid
	 */
	@Override
	public double getGameSetPointsFor(String player, int gameSetNumber) throws IllegalArgumentException {
		for (var matchPoints : matchPointsList) {
			try {
				return matchPoints.getGameSetPointsFor(player, gameSetNumber);
			} catch (IllegalArgumentException e) {
				// ignore
			}
		}
		throw new IllegalArgumentException("Player not found");
	}

//region setter/getter

	/**
	 * Return the winner of the match.
	 *
	 * @return the winner of the match
	 * @throws IllegalStateException if the match is a draw
	 */
	@Override
	public Player<Game120> getWinner() throws IllegalStateException {
		return null;
	}

	/**
	 * Return the concrete points of the match per partie.
	 *
	 * @return a map with the points of the match
	 */
	@Override
	public Map<Player<Game120>, Double> getMatchPoints() {
		var back = new HashMap<Player<Game120>, Double>();
		for (var matchPoints : matchPointsList) {
			back.putAll(matchPoints.getMatchPoints());
		}
		return back;
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
//endregion
}
