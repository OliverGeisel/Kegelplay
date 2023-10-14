package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.core.team_and_player.Player;

import java.util.*;

public class Pointsystem {

	private static final PointType pointType          = PointType.PER_DURCHGANG;
	private static final int[]     pointsPerDurchgang = {4, 3, 2, 1};

	public static Map<Player, Integer> getplayerPoints(int durchgang, Player... players) {
		if (players.length != 4) {
			throw new IllegalArgumentException("Es müssen genau 4 Spieler übergeben werden");
		}
		var playerPoints = new ArrayList<PlayerScore>(4);
		var scores = Arrays.stream(players).mapToInt(player -> player.getGame().getDurchgang(durchgang)
																	 .getScore()).toArray();
		for (int i = 0; i < players.length; i++) {
			playerPoints.add(new PlayerScore(players[i], scores[i]));
		}
		return evalScores(playerPoints);
	}

	private static Map<Player, Integer> evalScores(List<PlayerScore> scores) {
		var sortedScores = new ArrayList<>(scores);
		sortedScores.sort(Comparator.comparingInt(PlayerScore::score).reversed());
		var back = new HashMap<Player, Integer>();
		var player = sortedScores.remove(0);
		PlayerScore finalPlayer3 = player;
		var range = (int) sortedScores.stream().filter(score -> score.score() == finalPlayer3.score()).count() + 1;
		PlayerScore finalPlayer2 = player;
		sortedScores.removeIf(score -> score.score() == finalPlayer2.score());
		var pointList = new LinkedList<Integer>();
		for (int i = 0; i < range; i++) {
			pointList.add(pointsPerDurchgang[i]);
		}
		var sum = pointList.stream().limit(range).mapToInt(x -> x).sum();
		var pointsPerPlayer = sum / range;
		for (int i = 0; i < range; i++) {
			pointList.remove(0);
			back.put(sortedScores.remove(0).player, pointsPerPlayer);
		}
		while (!sortedScores.isEmpty()) {
			player = sortedScores.remove(0);
			PlayerScore finalPlayer1 = player;
			range = (int) sortedScores.stream().filter(score -> score.score() == finalPlayer1.score()).count() + 1;
			PlayerScore finalPlayer = player;
			sortedScores.removeIf(score -> score.score() == finalPlayer.score());
			pointList = new LinkedList<Integer>();
			for (int i = 0; i < range; i++) {
				pointList.add(pointsPerDurchgang[i]);
			}
			sum = pointList.stream().limit(range).mapToInt(x -> x).sum();
			pointsPerPlayer = sum / range;
			for (int i = 0; i < range; i++) {
				pointList.remove(0);
				back.put(sortedScores.remove(0).player, pointsPerPlayer);
			}

		}
		return back;

	}

	private record PlayerScore(Player player, int score) {
	}
}
