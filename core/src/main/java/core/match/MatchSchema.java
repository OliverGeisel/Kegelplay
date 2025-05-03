package core.match;

import core.game.GameInfo;
import core.util.KeyValueRegion;
import core.util.KeyValueRegionCollection;
import core.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Representation of the match schema. Detailed information about the order of all players when and where they play.
 *
 * <p>
 * Each {@link MatchSchema} has the following properties:
 *   <ul>
 *       <li>name: Name of the match</li>
 *       <li>teams: Number of teams</li>
 *       <li>playersPerCycle: Number of players per cycle -  A cycle is the complete wks </li>
 *       <li>laneCount: Number of lanes</li>
 *       <li>lanes: List of {@link LaneSchema}</li>
 *       <li>cycles: Number of cycles - Number to repeat the cycles to do the full match</li>
 * </ul>
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @since 1.0.0
 */
public class MatchSchema {

	private static final System.Logger LOGGER = System.getLogger(MatchSchema.class.getName());

	/**
	 * The name of the match.
	 */
	private final String           name;
	/**
	 * The number of teams in the match.
	 */
	private final int              teams;
	private final int              playersPerCycle;
	/**
	 * The number of lanes in the match.
	 */
	private final int              laneCount;
	/**
	 * The lanes in the match. Each lane has a {@link LaneSchema}. This is the configuration and order of the players
	 * that play on this lane.
	 */
	private final List<LaneSchema> lanes;
	/**
	 * The number of cycles in the match. A cycle is one round in the {@link MatchSchema}.
	 * But in some cases it can make sense to have more than one cycle.
	 */
	private final int              cycles;

	/**
	 * Creates a new match schema.
	 * The number of teams and players per team must be correct. so they must be checked before.
	 *
	 * @param wksIniFile                  The ini file of the match
	 * @param correctNumberTeams          The correct number of teams
	 * @param correctNumberPlayersPerTeam The correct number of players per team
	 */
	public MatchSchema(KeyValueRegionCollection<String, String, KeyValueRegion<String, String>> wksIniFile,
			int correctNumberTeams, int correctNumberPlayersPerTeam) {
		var allgemeinRegion = wksIniFile.getRegion("Allgemein");
		name = allgemeinRegion.getValue("Name");
		teams = Integer.parseInt(allgemeinRegion.getValue("Art")) + 1;
		var singleTeam = false;
		if (teams == 1) {
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
				lanes.add(new LaneSchema(lane, cycles, playersPerCycle, singleTeam, true));
				count++;
			} catch (IllegalArgumentException | UnusedLaneException e) {
				LOGGER.log(System.Logger.Level.DEBUG, "Lane is not used");
			}
		}
	}

	/**
	 * Get the {@link GameInfo} for a specific player in a team.
	 *
	 * @param team   team number
	 * @param player player number in the team
	 * @return The {@link GameInfo} for the player
	 */
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

		var volle = volleSet.iterator().next();
		var abraeumen = abrauemerSet.iterator().next();
		var time = timeSet.iterator().next();
		return new GameInfo(setCount, volle, abraeumen, time, symmetric);
	}

	//region setter/getter

	/**
	 * Get the number of cycles.
	 *
	 * @return the number of cycles
	 */
	public int getCycles() {
		return cycles;
	}

	/**
	 * Get the lanes.
	 *
	 * @return the lanes
	 */
	public List<LaneSchema> getLanes() {
		return lanes;
	}

	/**
	 * Get the number of teams.
	 *
	 * @return the number of teams
	 */
	public int getTeams() {
		return teams;
	}

	/**
	 * Get the number of players per cycle.
	 * A cycle is one round in the {@link MatchSchema}.
	 * But in some cases, it can make sense to have more than one cycle.
	 *
	 * @return The number of players per cycle
	 */
	public int getPlayersPerCycle() {
		return playersPerCycle;
	}

	/**
	 * Get the List of {@link Pair}s with the encoded team and player (int) how they will start.
	 * The first entry is the Player who starts first on the first lane. The second entry is the Player who starts
	 * on the second lane and so on.
	 *
	 * @return List of {@link Pair}s with the encoded team and player.
	 */
	public List<Pair<Integer, Integer>> getStartPositions() {
		var startPositions = new ArrayList<Pair<Integer, Integer>>();
		var numOfSets = lanes.getFirst().getSaetze().size();
		for (int i = 0; i < numOfSets; i += 4) {
			for (LaneSchema lane : lanes) {
				var set = lane.getSaetze().get(i);
				if (set != null) {
					startPositions.add(new Pair<>(set.team(), set.player()));
				}
			}
		}
		return startPositions;
	}

	/**
	 * Get the name of the WKS.
	 *
	 * @return the name of the wks
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the number of lanes that are used.
	 *
	 * @return the number of lanes
	 */
	public int getLaneCount() {
		return laneCount;
	}

//endregion
}
