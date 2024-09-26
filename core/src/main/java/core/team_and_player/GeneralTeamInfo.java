package core.team_and_player;


import core.util.KeyValueRegion;
import core.util.KeyValueRegionCollection;

/**
 * Contains all information about a team.
 *
 * @see Team
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class GeneralTeamInfo {

	private final String name;
	private final String gameClass;
	private final String league;
	private final String district;
	private final String leader;
	private final String observer;
	private final String clubNumber;
	private final int    numberOfPlayers;
	private final int    numberOfSubstitutes;


	/**
	 * Creates a new GeneralTeamInfo object from an IniFile.
	 *
	 * @param iniFile The IniFile that contains the information.
	 * @see KeyValueRegion
	 */
	public GeneralTeamInfo(KeyValueRegionCollection<String, String, KeyValueRegion<String, String>> iniFile) {
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

	public GeneralTeamInfo(String name, String gameClass, String league, String district, String leader,
			String observer, String clubNumber, int numberOfPlayers, int numberOfSubstitutes) {
		this.name = name;
		this.gameClass = gameClass;
		this.league = league;
		this.district = district;
		this.leader = leader;
		this.observer = observer;
		this.clubNumber = clubNumber;
		this.numberOfPlayers = numberOfPlayers;
		this.numberOfSubstitutes = numberOfSubstitutes;
	}

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
