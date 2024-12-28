package core.match;


import core.util.KeyValueRegion;

/**
 * General information about a match. This contains information like the name, location, facility, match leader, kind
 * etc.
 *
 * @param name           The name of the match.
 * @param location       The location of the match.
 * @param facility       The facility of the match.
 * @param matchLeader1   The first match leader.
 * @param matchLeader2   The second match leader.
 * @param kind           The kind of the match.
 * @param schemaName     The schema name of the match. (Will be a {@link MatchSchema})
 * @param matchDay       The day of the match in the season.
 * @param season         The season of the match. (year)
 * @param matchNumber    The number of the match.
 * @param matchLevel     The level of the match. (Kreis, Bezirk, Land, ...)
 * @param leage          The league of the match. (concrete naming of the league)
 * @param referee        The referee of the match.
 * @param refereeNumber  The number of the referee.
 * @param testPitchCount The count of test pitches.
 * @param testPitchTime  The time of the test pitches.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @since 1.0.0
 */
public record GeneralMatchInfo(String name, String location, String facility, String matchLeader1,
							   String matchLeader2, String kind, String schemaName, int matchDay, String season,
							   String matchNumber, String matchLevel, String leage, String referee,
							   String refereeNumber, int testPitchCount, int testPitchTime) {

	/**
	 * Creates a new general match information with default values.
	 */
	public GeneralMatchInfo() {
		this("", "", "", "", "", "", "", 0, "", "", "", "", "", "", 0, 0);
	}

	/**
	 * Creates a new general match information by a {@link KeyValueRegion}.
	 *
	 * @param region The region with the information.
	 */
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

}
