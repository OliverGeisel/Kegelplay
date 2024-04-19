package de.olivergeisel.kegelplay.core.team_and_player;

import de.olivergeisel.kegelplay.core.game.Game;

import java.time.LocalDate;
import java.util.Objects;


/**
 * Represents a player.
 *
 * @param <G> The type of the game the player is playing.
 *
 * @since 1.0
 * @version 1.0
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

	public Player(String vorname, String nachname) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.state = new NotPlayedYet();
	}

	public Player(String vorname, String nachname, String club, String team, LocalDate birthday) {
		this(vorname, nachname);
		this.club = club;
		this.team = team;
		this.birthday = birthday;
	}

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
	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public String getClub() {
		return club;
	}

	public String getTeam() {
		return team;
	}

	public LocalDate getBirthday() {
		return birthday;
	}

	public PlayerID getPlayerID() {
		return playerID;
	}

	public G getGame() {
		return game;
	}

	public void setGame(G game) {
		this.game = game;
	}

	public String getCompleteName() {
		return STR."\{vorname}_\{nachname}".replace(" ", "_").replace(",", "_").trim();
	}

	public String getCompleteNameWithCommata() {
		return STR."\{nachname},\{vorname}".trim();
	}

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
