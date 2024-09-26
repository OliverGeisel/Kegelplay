package core.point_system;


import core.game.Game120;
import core.match.Match;
import core.team_and_player.Player;
import core.team_and_player.Team;
import core.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@link PointSystem} where two {@link Player}s play against each other.
 * This PointSystem can decide between multiple pairs of players. The only requirement is that they are in the same
 * team and the order of the players is that the n-th player of the team plays against the n+1-th player of the team.
 * <br>
 * They play 120 throws in 4 sets with 30 throws each. Each set has 1 point. The player with the most points wins the game.
 * Is it a draw in points, the score decides. If the score is equal, "Sudden Victory" decides.
 * <b>WARNING</b> This PointSystem can only decide a winner if the score is different. If the score is equal, both
 * players will be the winner.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Player
 * @see Match
 * @see PointSystem
 * @since 1.0.0
 */
public class PairPlayerAgainstPointSystem extends PointSystem<Game120> {


	@Override
	public MatchPoints<Player<Game120>> getMatchPoints(Match<Game120> match) {
		var back = new PairMatchPointsCollection();
		for (var team : match.getTeams()) {
			var pairs = getPairings(team);
			for (var pair : pairs) {
				var points = new PairMatchPoints(pair.getKey(), pair.getValue());
				back.addMatchPoints(points);
			}
		}
		return back;
	}

	/**
	 * Creates the pairs for a single team.
	 * Players are paired in the order they are in the team.
	 * The first player plays against the second, the third against the fourth and so on.
	 *
	 * @param team The team to create the pairs for.
	 * @return A list of pairs of players.
	 */
	private List<Pair<Player<Game120>, Player<Game120>>> getPairings(Team<Game120> team) {
		var back = new ArrayList<Pair<Player<Game120>, Player<Game120>>>();
		for (int i = 0; i < team.getNumberOfPlayers() - 1; i += 2) {
			back.add(new Pair<>(team.getPlayer(i), team.getPlayer(i + 1)));
		}
		return back;
	}

	/**
	 * Get the winner of a match.
	 * This can have multiple winners, if the match is a draw or the Pointssystem allows multiple winners (like
	 * Qualifiers for a next round).
	 *
	 * @param match The match to get the winner from.
	 * @return A list of Winners.
	 */
	@Override
	public List<Winner> getWinner(Match<Game120> match) {
		var team = match.getTeams()[0];
		var pairs = getPairings(team);
		var back = new LinkedList<Winner>() {};
		for (var pair : pairs) {
			var points = new PairMatchPoints(pair.getKey(), pair.getValue());
			try {
				var winner = points.getWinner();
				back.add(new Winner(winner.getCompleteName(), winner.getGame().getTotalScore(),
						Winner.WinnerType.PLAYER));
			} catch (IllegalStateException e) {
				// ignore
			}
		}
		return back;
	}
}
