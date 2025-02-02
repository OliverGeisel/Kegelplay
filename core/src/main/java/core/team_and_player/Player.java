package core.team_and_player;

import core.game.Game;
import core.match.Match;

import java.time.LocalDate;
import java.util.Objects;


/**
 * Represents a player. Each player has a first name, a last name, a state, a game, a club, a team, a birthday and a
 * {@link PlayerID}.
 * A player play exactly one {@link Game} per {@link Match}. A player can be substituted by another player.
 *
 * @param <G> The type of the game the player is playing.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @see PlayerID
 * @see Match
 * @since 1.0.0
 */
public class Player<G extends Game> {

	private String    vorname;
	private String      nachname;
	private PlayerState state;
	private G         game;
	private String    club;
	private String    team;
	private LocalDate birthday;
	private PlayerID  playerID = new PlayerID("", 0);

	/**
	 * Creates a new player.
	 *
	 * @param vorname  The first name of the player.
	 * @param nachname The last name of the player.
	 */
	public Player(String vorname, String nachname) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.state = new NotPlayedYet();
	}

	/**
	 * Creates a new player.
	 *
	 * @param vorname  The first name of the player.
	 * @param nachname The last name of the player.
	 * @param club     The club of the player.
	 * @param team     The team(name) of the player.
	 * @param birthday The birthday of the player.
	 */
	public Player(String vorname, String nachname, String club, String team, LocalDate birthday) {
		this(vorname, nachname);
		this.club = club;
		this.team = team;
		this.birthday = birthday;
	}

	/**
	 * Get the state of the player.
	 *
	 * @param state The state of the player.
	 * @return The state of the player.
	 */
	private PlayerState getStateFrom(PlayerStateEnum state) {
		return switch (state) {
			case NOT_PLAYED_YET -> new NotPlayedYet();
			case PROBE -> new Probe();
			case RUNNING -> new Running();
			case SATZ_COMPLETE -> new SatzComplete();
			case FINISHED -> new Finished();
		};
	}

	//region setter/getter

	/**
	 * Returns the first name of the player.
	 *
	 * @return The first name of the player.
	 */
	public String getVorname() {
		return vorname;
	}

	/**
	 * Returns the last name of the player.
	 *
	 * @return The last name of the player.
	 */
	public String getNachname() {
		return nachname;
	}

	/**
	 * Returns the state of the player.
	 *
	 * @return The state of the player.
	 */
	public String getClub() {
		return club;
	}

	/**
	 * Returns the team(name) of the player.
	 *
	 * @return The team(name) of the player.
	 */
	public String getTeam() {
		return team;
	}

	/**
	 * Returns the birthday of the player.
	 *
	 * @return The birthday of the player.
	 */
	public LocalDate getBirthday() {
		return birthday;
	}

	/**
	 * Returns the playerID of the player.
	 *
	 * @return The playerID of the player.
	 */
	public PlayerID getPlayerID() {
		return playerID;
	}

	/**
	 * Get the Game of the player.
	 *
	 * @return The Game of the player.
	 */
	public G getGame() {
		return game;
	}

	/**
	 * Set the Game of the player.
	 *
	 * @param game The Game of the player.
	 */
	public void setGame(G game) {
		this.game = game;
		if (game != null) {
			// Set the player of the game to this player if the player of the game is not this player.
			if (game.getPlayer() != this) {
				game.setPlayer(this);
			}
		}
	}

	/**
	 * Returns the complete name of the player. This is the first name followed by the last name.
	 * The string is trimmed.
	 *
	 * @return the complete name of the player
	 */
	public String getCompleteName() {
		return STR."\{vorname} \{nachname}".trim();
	}

	/**
	 * Returns the complete name of the player. Each blank and ',' is replaced by an underscore.
	 *
	 * @return the complete name of the player
	 *
	 * @see #getCompleteName()
	 */
	public String getCompleteNameWithUnderscore() {
		return STR."\{vorname}_\{nachname}".replace(" ", "_").replace(",", "_").trim();
	}

	/**
	 * Returns the complete name of the player. the for and surname are separated by a ','. The string is trimmed.
	 *
	 * @return the complete name of the player
	 *
	 * @see #getCompleteName()
	 */
	public String getCompleteNameWithCommata() {
		return STR."\{nachname},\{vorname}".trim();
	}

	/**
	 * Set the state of the player.
	 *
	 * @param state The state of the player.
	 */
	public void setStateTo(PlayerStateEnum state) {
		this.state = getStateFrom(state);
	}
//endregion

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Player player)) return false;

		if (!vorname.equals(player.vorname)) return false;
		if (!nachname.equals(player.nachname)) return false;
		if (!Objects.equals(club, player.club)) return false;
		if (!Objects.equals(team, player.team)) return false;
		if (!Objects.equals(birthday, player.birthday)) return false;
		return Objects.equals(playerID, player.playerID);
	}

	@Override
	public int hashCode() {
		int result = vorname.hashCode();
		result = 31 * result + nachname.hashCode();
		result = 31 * result + (club != null ? club.hashCode() : 0);
		result = 31 * result + (team != null ? team.hashCode() : 0);
		result = 31 * result + (birthday != null ? birthday.hashCode() : 0);
		result = 31 * result + (playerID != null ? playerID.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return STR."Player{vorname='\{vorname}', nachname='\{nachname}', club='\{club}', birthday=\{birthday}, playerID=\{playerID}}";
	}


	/**
	 * Enum of the states of a player.
	 *
	 * @author Oliver Geisel
	 * @version 1.0.0
	 * @see Player
	 * @since 1.0.0
	 */
	public enum PlayerStateEnum {
		NOT_PLAYED_YET,
		PROBE,
		RUNNING,
		SATZ_COMPLETE,
		FINISHED
	}

	private class NotPlayedYet extends PlayerState {

	}

	private class Probe extends PlayerState {

	}

	private class Running extends PlayerState {

	}

	private class SatzComplete extends PlayerState {

	}

	private class Finished extends PlayerState {

	}

	/**
	 * Represents the state of a player.
	 * Normally a player starts in the state NOT_PLAYED_YET.
	 * If a player comes to the game, the state changes to PROBE.
	 * If the player starts playing, the state changes to RUNNING.
	 * If the player has finished a set, the state changes to SATZ_COMPLETE.
	 * As long as not all players have finished the set, there.
	 * If all players have finished the set, back to RUNNING.
	 * If the player has finished the game, the state changes to FINISHED.
	 * If a player is substituted, the state changes to SUBSTITUTE.
	 */
	private abstract class PlayerState {

	}

	private class Substitute extends PlayerState {

	}


}
