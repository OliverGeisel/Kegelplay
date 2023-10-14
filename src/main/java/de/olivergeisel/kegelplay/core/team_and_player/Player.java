package de.olivergeisel.kegelplay.core.team_and_player;

import de.olivergeisel.kegelplay.core.game.Game;

import java.time.LocalDate;

public class Player {

	private String vorname;
	private String      nachname;
	private PlayerState state;
	private Game spiel;
	private String club;
	private String team;
	private LocalDate birthday;
	private PlayerID  playerID = new PlayerID("", 0);

	public Player(String vorname, String nachname) {
		this.vorname = vorname;
		this.nachname = nachname;
	}

//region setter/getter
	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public Game getSpiel() {
		return spiel;
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

	public Game getGame() {
		return spiel;
	}

	public void setGame(Game game) {
		this.spiel = game;
	}

	public String getCompleteName() {
		return STR. "\{ vorname }_\{ nachname }" .replace(" ", "_").replace(",", "_").trim();
	}
//endregion

	private abstract class PlayerState {

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

}
