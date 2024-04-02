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


}
