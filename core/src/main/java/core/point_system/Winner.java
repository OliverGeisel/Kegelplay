package core.point_system;


import core.match.Match;
import core.team_and_player.Player;
import core.team_and_player.Team;

/**
 * A Winner is a class that represents the winner of a match.
 * The winner can be a {@link Team} or a {@link Player}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @see Team
 * @see Player
 * @since 1.0.0
 */
public class Winner {
	private final String     name;
	private final int        score;
	private final WinnerType winnerType;

	/**
	 * Create a new Winner.
	 *
	 * @param name  The name of the winner.
	 * @param score The score of the winner.
	 */
	public Winner(String name, int score, WinnerType winnerType) {
		this.name = name;
		this.score = score;
		this.winnerType = winnerType;
	}

	//region setter/getter

	/**
	 * Get the type of the winner.
	 *
	 * @return The type of the winner.
	 */
	public WinnerType getWinnerType() {
		return winnerType;
	}

	/**
	 * Get the name of the winner.
	 *
	 * @return The name of the winner.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the score of the winner.
	 *
	 * @return The score of the winner.
	 */
	public int getScore() {
		return score;
	}
//endregion

	@Override
	public String toString() {
		return STR."Winner{name='\{name}', score=\{score}, winnerType=\{winnerType}}";
	}

	/**
	 * The type of the winner.
	 */
	public enum WinnerType {
		/**
		 * The winner is a team.
		 */
		TEAM,
		/**
		 * The winner is a player (single person).
		 */
		PLAYER
	}
}
