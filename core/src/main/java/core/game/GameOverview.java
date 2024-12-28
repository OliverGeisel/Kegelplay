package core.game;

import java.time.LocalDateTime;

/**
 * Overview of a game. This contains the date, the name and the teams of the game.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @since 1.0.0
 */
public class GameOverview {
	private LocalDateTime date;
	private String        name;
	private String[]      teams;

	/**
	 * Creates a new game overview.
	 *
	 * @param date  The date of the game.
	 * @param name  The name of the game.
	 * @param teams The teams of the game.
	 */
	public GameOverview(LocalDateTime date, String name, String[] teams) {
		this.date = date;
		this.name = name;
		this.teams = teams;
	}

	//region setter/getter

	/**
	 * Returns the date of the game.
	 *
	 * @return The date of the game.
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Sets the date of the game.
	 *
	 * @param date The date of the game.
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * Returns the name of the game.
	 *
	 * @return The name of the game.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the game.
	 *
	 * @param name The name of the game.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the teams of the game.
	 *
	 * @return The teams of the game.
	 */
	public String[] getTeams() {
		return teams;
	}

	/**
	 * Sets the teams of the game.
	 *
	 * @param teams The teams of the game.
	 */
	public void setTeams(String[] teams) {
		this.teams = teams;
	}
//endregion
}
