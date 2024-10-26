package core.match;


import core.util.KeyValueRegion;

public record GeneralMatchInfo(String name, String location, String facility, String matchLeader1,
							   String matchLeader2, String kind, String schemaName, int matchDay, String season,
							   String matchNumber, String matchLevel, String leage, String referee,
							   String refereeNumber, int testPitchCount, int testPitchTime) {

	public GeneralMatchInfo(KeyValueRegion<String, String> region) {
		this(region.getValue("Name"),
				region.getValue("Ort"),
				region.getValue("Bahnanlage"),
				region.getValue("Spielleitung 1"),
				region.getValue("Spielleitung 2"),
				region.getValue("Bezeichnung"),
				region.getValue("WKS"),
				// Old versions don't have this
				region.getValueOrDefault("Spieltag", "").isBlank() ? 0 : Integer.parseInt(region.getValue("Spieltag")),
				region.getValueOrDefault("Spieljahr", ""),
				region.getValueOrDefault("Spielnummer", ""),
				region.getValueOrDefault("Spielklasse", ""),
				region.getValueOrDefault("Liga", ""),
				region.getValueOrDefault("SRNR", ""),
				region.getValueOrDefault("SRName", ""),
				region.getValue("Probewürfe Anzahl").isBlank() ? 0 :
						Integer.parseInt(region.getValue("Probewürfe Anzahl")),
				region.getValue("Probewürfe Zeit").isBlank() ? 0 :
						Integer.parseInt(region.getValue("Probewürfe Zeit")));
	}

	public GeneralMatchInfo() {
		this("", "", "", "", "", "", "", 0, "", "", "", "", "", "", 0, 0);
	}

}
