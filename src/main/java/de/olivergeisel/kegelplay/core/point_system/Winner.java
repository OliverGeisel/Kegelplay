package de.olivergeisel.kegelplay.core.point_system;


/**
 * A Winner is a class that represents the winner of a match.
 * The winner can be a {@link de.olivergeisel.kegelplay.core.team_and_player.Team} or a {@link de.olivergeisel.kegelplay.core.team_and_player.Player}.
 *
 * @see de.olivergeisel.kegelplay.core.match.Match
 * @see de.olivergeisel.kegelplay.core.team_and_player.Team
 * @see de.olivergeisel.kegelplay.core.team_and_player.Player
 *
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
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

	public enum WinnerType {
		TEAM,
		PLAYER
	}
}
