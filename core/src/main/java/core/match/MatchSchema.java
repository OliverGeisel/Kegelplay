package core.match;

import core.game.GameInfo;
import core.util.KeyValueRegion;
import core.util.KeyValueRegionCollection;

import java.util.ArrayList;
import java.util.HashSet;
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

	public MatchSchema(KeyValueRegionCollection<String, String, KeyValueRegion<String, String>> wksIniFile,
			int correctNumberTeams, int correctNumberPlayersPerTeam) {
		var allgemeinRegion = wksIniFile.getRegion("Allgemein");
		name = allgemeinRegion.getValue("Name");
		teams = Integer.parseInt(allgemeinRegion.getValue("Art")) + 1;
		var singleTeam = false;
		if (teams == 1){
			LOGGER.log(System.Logger.Level.DEBUG, "Only one Team");
			singleTeam = true;
		}
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
				lanes.add(new LaneSchema(lane, cycles, playersPerCycle,singleTeam));
				count++;
			} catch (IllegalArgumentException | UnusedLaneException e) {
				LOGGER.log(System.Logger.Level.DEBUG, "Lane is not used");
			}
		}
	}

	public GameInfo getGameInfoFor(int team, int player) {
		var setCount = 0;
		var volleSet = new HashSet<Integer>();
		var abrauemerSet = new HashSet<Integer>();
		var timeSet = new HashSet<Integer>();
		for (var lane : lanes) {
			for (var set : lane.getSaetze()) {
				if (set.player() == player && set.team() == team) {
					setCount++;
					volleSet.add(set.volle());
					abrauemerSet.add(set.abraeumen());
					timeSet.add(set.time());
				}
			}
		}
		var symmetric = volleSet.size() == 1 && abrauemerSet.size() == 1 && timeSet.size() == 1;
		if (!symmetric) {
			throw new IllegalArgumentException("Player does not play symmetric");
		}
		var volle = volleSet.iterator().next();
		var abraeumen = abrauemerSet.iterator().next();
		var time = timeSet.iterator().next();
		return new GameInfo(setCount, volle, abraeumen, time, symmetric);
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
