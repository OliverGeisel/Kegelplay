package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.infrastructure.ini.IniRegion;

import java.util.Arrays;
import java.util.List;


/**
 * The MatchSchema for a single lane
 */
public class LaneSchema {

	private final String         name; // name of lane
	private final List<LaneSatz> saetze; // list of sets

	/**
	 * Constructor for LaneSchema
	 * @param region the region of the lane
	 * @throws IllegalArgumentException if the lane schema is invalid
	 */
	public LaneSchema(IniRegion region, int cycles, int playerPerCycle) throws IllegalArgumentException,
			UnusedLaneException {
		name = region.getName();
		if (region.size() % 5 != 0) {
			throw new IllegalArgumentException("Invalid lane schema");
		}
		var setCount = region.size() / 5;
		saetze = new java.util.ArrayList<>(setCount);
		// check if is used
		if (!isUsed(region)) {
			throw new UnusedLaneException("Lane is not used");
		}
		for (int c = 0; c < cycles; c++) {
			for (int i = 1; i <= setCount; i++) {
				final var number = i - 1 + c * playerPerCycle;
				var player = Integer.parseInt(region.getValue(STR."Spieler \{i}"));
				var team = Integer.parseInt(region.getValue(STR."Mannschaft \{i}"));
				var volle = Integer.parseInt(region.getValue(STR."Volle \{i}"));
				var abraeumen = Integer.parseInt(region.getValue(STR."Abraeumer \{i}"));
				var time = Integer.parseInt(region.getValue(STR."Zeit \{i}"));
				saetze.add(new LaneSatz(player + c * playerPerCycle, team, number, volle, abraeumen, time));
			}
		}
	}

	private boolean isUsed(IniRegion region) {
		var playerF = Arrays.stream(region.getKeys()).filter(it -> it.startsWith("Spieler")).toList();
		for (var player : playerF) {
			var value = region.getValue(player);
			if (value.isEmpty() || value.equals("-1")) { // -1 is not used
				return false;
			}
		}
		var teamF = Arrays.stream(region.getKeys()).filter(it -> it.startsWith("Mannschaft")).toList();
		for (var team : teamF) {
			var value = region.getValue(team);
			if (value.isEmpty() || value.equals("-1")) { // -1 is not used
				return false;
			}
		}
		return true;
	}

	public LaneSatz getSatze(int index) {
		return saetze.get(index);
	}

	//region setter/getter
	public String getName() {
		return name;
	}

	public List<LaneSatz> getSaetze() {
		return saetze;
	}
//endregion

	/**
	 * A single set/round in a lane
	 *
	 * @param player number in team
	 * @param team number of team
	 * @param number number of set
	 * @param volle number of volle
	 * @param abraeumen number of abraeumen
	 * @param time time of set
	 */
	public record LaneSatz(int player, int team, int number, int volle, int abraeumen, int time) {
	}

}
