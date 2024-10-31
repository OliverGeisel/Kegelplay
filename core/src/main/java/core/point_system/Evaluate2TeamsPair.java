package core.point_system;

import core.game.Game120;
import core.match.Match2Teams;
import core.util.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Evaluate2TeamsPair {

	public static Pair<List<GamePointsPlayer<Game120>>, List<GamePointsPlayer<Game120>>> evaluateMatch(
			Match2Teams<Game120> match) {
		var home = match.getHome();
		var guest = match.getGuest();
		var homePoints = new LinkedList<GamePointsPlayer<Game120>>();
		var guestPoints = new LinkedList<GamePointsPlayer<Game120>>();
		for (int i = 0; i < home.getPlayers().length; i++) {
			var pair = GamePointsEvaluator_2Players.evaluate(home.getPlayers()[i], guest.getPlayers()[i]);
			homePoints.add(pair.getFirst());
			guestPoints.add(pair.getSecond());
		}
		return new Pair<>(homePoints, guestPoints);
	}

	/**
	 * Get a Map of the TeamPoints by complete name or the name of the team
	 *
	 * @param match Match to evaluate
	 * @return Map with player/team and TeamPoints
	 */
	public static Map<String, Double> evaluateMatchAsPoints(Match2Teams<Game120> match) {
		Map<String, Double> back = new HashMap<>();
		var home = match.getHome();
		var guest = match.getGuest();
		var pairs = evaluateMatch(match);
		var sum1 = pairs.getFirst().stream().mapToDouble(GamePointsPlayer::getTeamPoints).sum();
		var sum2 = pairs.getSecond().stream().mapToDouble(GamePointsPlayer::getTeamPoints).sum();
		for (int i = 0; i < home.getPlayers().length; i++) {
			var playerHome = home.getPlayers()[i];
			var playerGuest = guest.getPlayers()[i];
			var pair = GamePointsEvaluator_2Players.evaluate(playerHome, playerGuest);
			back.put(playerHome.getCompleteName(), pair.getFirst().getTeamPoints());
			back.put(playerGuest.getCompleteName(), pair.getSecond().getTeamPoints());
		}
		var scorePoints = ScoreEvaluator.evaluate(home, guest);
		back.put(home.getName(), sum1 + scorePoints.getFirst());
		back.put(guest.getName(), sum2 + scorePoints.getSecond());
		return back;
	}
}
