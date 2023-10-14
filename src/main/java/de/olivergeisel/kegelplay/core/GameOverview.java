package de.olivergeisel.kegelplay.core;

import java.time.LocalDateTime;

public class GameOverview {
	private LocalDateTime date;
	private String        name;
	private String[]      teams;

	public GameOverview(LocalDateTime date, String name, String[] teams) {
		this.date = date;
		this.name = name;
		this.teams = teams;
	}

//region setter/getter
	//region getter / setter
	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getTeams() {
		return teams;
	}

	public void setTeams(String[] teams) {
		this.teams = teams;
	}
//endregion
//endregion
}
