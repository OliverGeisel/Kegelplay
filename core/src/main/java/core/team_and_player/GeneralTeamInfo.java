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
		numberOfSubstitutes = Integer.parseInt(region.getValueOrDefault("Anzahl Ersatzspieler", "0"));
	}

	/**
	 * Creates a new GeneralTeamInfo object with all information.
	 *
	 * @param name                The name of the team.
	 * @param gameClass           The game class of the team.
	 * @param league              The league of the team.
	 * @param district            The district of the team.
	 * @param leader              The leader of the team.
	 * @param observer            The observer of the team.
	 * @param clubNumber          The club number of the team.
	 * @param numberOfPlayers     The number of players in the team.
	 * @param numberOfSubstitutes The number of substitutes in the team.
	 */
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

	/**
	 * Creates a new GeneralTeamInfo object with default values.
	 */
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

	/**
	 * Returns the name of the team.
	 *
	 * @return The name of the team.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the game class of the team. The game class the level of the team in the league system.
	 * @return The game class of the team.
	 */
	public String getGameClass() {
		return gameClass;
	}

	/**
	 * Returns the league of the team.
	 * @return The league of the team.
	 */
	public String getLeague() {
		return league;
	}

	/**
	 * Returns the district of the team.
	 * @return The district of the team.
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * Returns the leader of the team.
	 * @return The leader of the team.
	 */
	public String getLeader() {
		return leader;
	}

	/**
	 * Returns the observer of the team.
	 *
	 * @return The observer of the team.
	 */
	public String getObserver() {
		return observer;
	}

	/**
	 * Returns the club number of the team.
	 *
	 * @return The club number of the team.
	 */
	public String getClubNumber() {
		return clubNumber;
	}

	/**
	 * Returns the number of players in the team.
	 *
	 * @return The number of players in the team.
	 */
	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	/**
	 * Returns the number of substitutes in the team.
	 * @return The number of substitutes in the team.
	 */
	public int getNumberOfSubstitutes() {
		return numberOfSubstitutes;
	}
//endregion


}
