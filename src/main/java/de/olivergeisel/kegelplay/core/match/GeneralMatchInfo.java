package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.infrastructure.ini.IniRegion;

public record GeneralMatchInfo(String name, String location, String facility, String matchLeader1,
							   String matchLeader2, String kind, String schemaName, int matchDay, String season,
							   String matchNumber, String matchLevel, String leage, String referee,
							   String refereeNumber,
							   int testPitchCount, int testPitchTime) {

	public GeneralMatchInfo(IniRegion region) {
		this(region.getValue("Name"),
				region.getValue("Ort"),
				region.getValue("Bahnanlage"),
				region.getValue("Spielleitung 1"),
				region.getValue("Spielleitung 2"),
				region.getValue("Bezeichnung"),
				region.getValue("WKS"),
				region.getValue("Spieltag").isBlank() ? 0 : Integer.parseInt(region.getValue("Spieltag")),
				region.getValue("Spieljahr"),
				region.getValue("Spielnummer"),
				region.getValue("Spielklasse"),
				region.getValue("Liga"),
				region.getValue("SRNR"),
				region.getValue("SRName"),
				region.getValue("Probewürfe Anzahl").isBlank() ? 0 :
						Integer.parseInt(region.getValue("Probewürfe Anzahl")),
				region.getValue("Probewürfe Zeit").isBlank() ? 0 :
						Integer.parseInt(region.getValue("Probewürfe Zeit")));
	}

	public GeneralMatchInfo() {
		this("", "", "", "", "", "", "", 0, "", "", "", "", "", "", 0, 0);
	}

}
