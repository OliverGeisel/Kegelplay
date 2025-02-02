package core.game;

import core.match.Match;
import core.team_and_player.Player;
import core.team_and_player.Team;

import java.time.LocalDateTime;

/**
 * Represents an overview to a {@link Game}.
 * This is a short overview of a game with the date, name and the teams that are playing.
 * NOT USED YET
 *
 * @author Oliver
 * @version 1.0.0
 * @see Game
 * @see GameSet
 * @see Abschnitt
 * @see AbschnittType
 * @see Wurfbild
 * @see Match
 * @see Player
 * @see Team
 * @since 1.0.0
 */
public class GameOverview {

	private LocalDateTime date;
	private String        name;
	private String[]      teams;

	/**
	 * Creates a new GameOverview with the given date, name and teams.
	 *
	 * @param date  The date of the game
	 * @param name  The name of the game
	 * @param teams The teams that are playing
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
