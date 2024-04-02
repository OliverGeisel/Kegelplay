package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of the match schema. Detailed information about the order of all players when and where they play.
 */
public class MatchSchema {

	private static final System.Logger LOGGER = System.getLogger(MatchSchema.class.getName());


	private final String           name;
	private final int              teams;
	private final int              playersPerCycle;
	private final int              laneCount;
	private final List<LaneSchema> lanes;
	private final int              cycles;

	public MatchSchema(IniFile wksIniFile, int correctNumberTeams, int correctNumberPlayersPerTeam) {
		var allgemeinRegion = wksIniFile.getRegion("Allgemein");
		name = allgemeinRegion.getValue("Name");
		teams = Integer.parseInt(allgemeinRegion.getValue("Art")) + 1;
		if (teams != correctNumberTeams) {
			throw new IllegalArgumentException("Number of teams is not correct");
		}
		playersPerCycle = Integer.parseInt(allgemeinRegion.getValue("Anzahl"));

		cycles = playersPerCycle != correctNumberPlayersPerTeam ? correctNumberPlayersPerTeam / playersPerCycle : 1;
		laneCount = Integer.parseInt(allgemeinRegion.getValue("Anzahl Bahnen"));
		var bahnRegions = wksIniFile.getRegions().stream().filter(it -> it.getName().startsWith("Bahn")).toList();
		lanes = new ArrayList<>(laneCount);
		int count = 0;
		for (var lane : bahnRegions) {
			if (count >= laneCount) {
				break;
			}
			try {
				lanes.add(new LaneSchema(lane, cycles, playersPerCycle));
				count++;
			} catch (IllegalArgumentException | UnusedLaneException e) {
				LOGGER.log(System.Logger.Level.DEBUG, "Lane is not used");
			}
		}
	}

	//region setter/getter
	public int getCycles() {
		return cycles;
	}

	public List<LaneSchema> getLanes() {
		return lanes;
	}

	public int getTeams() {
		return teams;
	}

	public int getPlayersPerCycle() {
		return playersPerCycle;
	}

	public String getName() {
		return name;
	}

	public int getLaneCount() {
		return laneCount;
	}

//endregion
}