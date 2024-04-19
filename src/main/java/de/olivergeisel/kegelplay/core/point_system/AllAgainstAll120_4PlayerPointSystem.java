package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.core.game.GameSet;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.team_and_player.Player;

import java.util.*;

/**
 * The point system for a match where all players play against each other.
 *
 * @see PointSystemCriteria#PLAYER
 * @see PointSystem
 * @see MatchPoints
 */
public class AllAgainstAll120_4PlayerPointSystem extends PointSystem<Game120> {

	private static final List<Integer>       POINTS_PER_SET = List.of(4, 3, 2, 1);
	private final        PointSystemCriteria criteria       = PointSystemCriteria.PLAYER;
	private final        PointDecide         decide         = PointDecide.SET_POINTS;

	@Override
	public MatchPoints<Player<Game120>> getMatchPoints(Match<Game120> match) throws IllegalArgumentException {
		if (match == null) {
			throw new IllegalArgumentException("Match must not be null");
		}
		if (match.getTeams().length != 1) {
			throw new IllegalArgumentException("Match must have exactly one team");
		}
		var team = match.getTeams()[0];
		var players = team.getPlayers();
		var gamePoints = getGamePoints(players);
		return new AllAgainstMatchScore<>(gamePoints);
	}

	private GamePointsCollection getGamePoints(Player<Game120>... players) {
		if (players.length != 4) {
			throw new IllegalArgumentException("there must be exactly 4 players");
		}
		var points = new GamePointsCollection(players);
		for (int i = 0; i < 4; i++) {
			try {
				var setPoints = getSetPoints(i, players);
				for (var player : players) {
					points.addGameSetPoints(player, setPoints);
				}
			} catch (IllegalStateException e) {
				break;
			}
		}
		return points;
	}


	/**
	 * Calculate the points for a set over all 4 players.
	 * The player with the most points gets 4 points, the second 3 points, the third 2 points and the last 1 point.
	 *
	 * @param setNumber the number of the set
	 * @param players   the players
	 * @return the points for the set per player
	 * @throws IllegalArgumentException if the number of players is not 4 or the setNumber is not between 0 and 3
	 * @throws IllegalStateException    if the set is not started yet
	 */
	private GameSetPointsCollection getSetPoints(int setNumber, Player<Game120>... players)
			throws IllegalArgumentException, IllegalStateException {
		if (players.length != 4) {
			throw new IllegalArgumentException("there must be exactly 4 players");
		}
		if (setNumber < 0 || setNumber > 3) {
			throw new IllegalArgumentException("setNumber must be between 0 and 3");
		}
	/*	if (Arrays.stream(players).map(player -> player.getGame().getDurchgang(setNumber))
				  .anyMatch(GameSet::isNotStarted)) {
			throw new IllegalStateException("set not started");
		}*/
		var player1 = players[0];
		var player2 = players[1];
		var player3 = players[2];
		var player4 = players[3];
		var player1Set = player1.getGame().getDurchgang(setNumber);
		var player2Set = player2.getGame().getDurchgang(setNumber);
		var player3Set = player3.getGame().getDurchgang(setNumber);
		var player4Set = player4.getGame().getDurchgang(setNumber);
		var pair1 = new GameSetPlayer(player1, player1Set);
		var pair2 = new GameSetPlayer(player2, player2Set);
		var pair3 = new GameSetPlayer(player3, player3Set);
		var pair4 = new GameSetPlayer(player4, player4Set);
		return evalSetScores(pair1, pair2, pair3, pair4);
	}

	/**
	 * Calculate the points for a set over all 4 players.
	 * The player with the most points gets 4 points, the second 3 points, the third 2 points and the last 1 point.
	 * If two or more players have the same score, they get will be ordered and get zhe sum of the points divided by the number of players.
	 * <br>
	 * Example:
	 * Player 1: 100
	 * Player 2: 90
	 * Player 3: 90
	 * Player 4: 80
	 * <br>
	 * Player 1: 4 points
	 * Player 2: 2 points
	 * Player 3: 2 points
	 * Player 4: 1 point
	 *
	 * @param scores the scores of the players in the set
	 * @return the points for the set per player
	 */
	private GameSetPointsCollection evalSetScores(GameSetPlayer... scores) {
		var scorePlayerMapping = new HashMap<Integer, List<Player<Game120>>>();
		for (var score : scores) {
			var points = score.set().getScore();
			scorePlayerMapping.computeIfAbsent(points, k -> new ArrayList<>());
			scorePlayerMapping.get(points).add(score.player());
		}
		List<Integer> sortedScores = new ArrayList<>(scorePlayerMapping.keySet());
		sortedScores.sort(Integer::compareTo);
		sortedScores = sortedScores.reversed(); // highest score first
		var pointMapping = new HashMap<Integer, Double>();
		var points = new LinkedList<>(POINTS_PER_SET);
		for (Integer score : sortedScores) {
			var players = scorePlayerMapping.get(score);
			for (Player<Game120> player : players) {
				pointMapping.computeIfAbsent(score, k -> 0.);
				pointMapping.put(score, pointMapping.get(score) + points.removeFirst());
			}
			var normalized = pointMapping.get(score) / players.size();
			pointMapping.put(score, normalized);
		}
		var back = new GameSetPointsCollection();
		for (var score : pointMapping.entrySet()) {
			for (var player : scorePlayerMapping.get(score.getKey())) {
				var gameSet =
						Arrays.stream(scores).filter(s -> s.player().equals(player)).findFirst().orElseThrow().set();
				back.setScore(player, score.getValue(), gameSet);
			}
		}
		return back;
	}

	private record GameSetPlayer(Player<Game120> player, GameSet set) {}

}
