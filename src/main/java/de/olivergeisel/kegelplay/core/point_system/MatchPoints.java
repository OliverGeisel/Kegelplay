package de.olivergeisel.kegelplay.core.point_system;

import java.util.Map;

/**
 * Class to store the result of a match.
 * <p>
 * There is one method to get the winner of the match.
 *
 * @param <W> the type of what is returned when the winner is asked.
 *            This can be a team or a player
 */
public abstract class MatchPoints<W> {

	/**
	 * Return the points of the winner.
	 *
	 * @return the points of the winner
	 */
	public abstract double winnerPoints();

	public abstract double getGameSetPointsFor(String player, int gameSetNumber);

	/**
	 * Return the winner of the match.
	 *
	 * @return the winner of the match
	 * @throws IllegalStateException if the match is a draw
	 */
	public abstract W getWinner() throws IllegalStateException;

	/**
	 * Return the concrete points of the match per partie.
	 *
	 * @return a map with the points of the match
	 */
	public abstract Map<W, Double> getMatchPoints();

	/**
	 * When all parties have equal points, the match is a draw.
	 *
	 * @return true if the match is a draw
	 */
	public abstract boolean isDraw();

//region setter/getter
//endregion

}
