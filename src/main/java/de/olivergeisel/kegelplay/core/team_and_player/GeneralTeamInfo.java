package de.olivergeisel.kegelplay.core.team_and_player;

import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;

public class GeneralTeamInfo {
	private String name;
	private String gameClass;
	private String league;
	private String district;
	private String leader;
	private String observer;
	private String clubNumber;
	private int    numberOfPlayers;
	private int    numberOfSubstitutes;

	protected GeneralTeamInfo() {
		name = "";
		gameClass = "";
		league = "";
		district = "";
		leader = "";
		observer = "";
		clubNumber = "";
		numberOfPlayers = 0;
		numberOfSubstitutes = 0;
	}
	public GeneralTeamInfo(IniFile iniFile) {
		var region = iniFile.getRegion("Allgemein");
		name = region.getValue("Name");
		gameClass = region.getValue("Spielklasse");
		league = region.getValue("Liga");
		district = region.getValue("Bezirk");
		leader = region.getValue("Spielführer");
		observer = region.getValue("Betreuer 1");
		clubNumber = region.getValue("Vereins-Nr");
		numberOfPlayers = Integer.parseInt(region.getValue("Anzahl Spieler"));
		numberOfSubstitutes = Integer.parseInt(region.getValue("Anzahl Ersatzspieler"));
	}

	//region setter/getter
	public String getName() {
		return name;
	}

	public String getGameClass() {
		return gameClass;
	}

	public String getLeague() {
		return league;
	}

	public String getDistrict() {
		return district;
	}

	public String getLeader() {
		return leader;
	}

	public String getObserver() {
		return observer;
	}

	public String getClubNumber() {
		return clubNumber;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public int getNumberOfSubstitutes() {
		return numberOfSubstitutes;
	}
//endregion


}
