package core.point_system;

import core.game.Game120;
import core.match.Match2Teams;
import core.team_and_player.Team;
import core.util.Pair;

public class ScoreEvaluator {
	public static Pair<Double, Double> evaluate(Match2Teams<Game120> match) {
		var home = match.getHome();
		var guest = match.getGuest();
		return evaluate(home, guest);
	}

	public static Pair<Double, Double> evaluate(Team<Game120> home, Team<Game120> guest) {
		return evaluate(home.getTeamScore(), guest.getTeamScore());
	}

	public static Pair<Double, Double> evaluate(int homeScore, int guestScore) {
		if (homeScore > guestScore) {
			return new Pair<>(2.0, 0.0);
		} else if (homeScore < guestScore) {
			return new Pair<>(0.0, 2.0);
		} else {
			return new Pair<>(1., 1.);
		}
	}
}
