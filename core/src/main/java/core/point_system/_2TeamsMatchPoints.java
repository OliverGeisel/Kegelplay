package core.point_system;

import core.game.Game;
import core.match.Match;
import core.team_and_player.Player;
import core.team_and_player.Team;
import core.util.Pair;

import java.util.List;
import java.util.Map;

/**
 * The MatchPoints for a {@link Match} with two {@link Team}s. The winner of the match is the team with the most
 * pins (score/Holz).
 *
 * @param <G> The type of the {@link Game} that is played in the match.
 *
 * @see Game
 * @see Match
 * @see Player
 * @see Team
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class _2TeamsMatchPoints<G extends Game> extends MatchPoints<Team<G>> {

	private final List<Pair<Player<G>, Player<G>>> pairs;
	private final Team<G>                          team1;
	private final Team<G>                          team2;
	private final double                           scorePointsTeam1;
	private final double                  scorePointsTeam2;
	private final List<GamePointsTeam<G>> gamePointsTeam1;
	private final List<GamePointsTeam<G>> gamePointsTeam2;
	private final double                  winnerPoints;

	/**
	 * @param pairs            The pairs of players that played against each other
	 * @param team1            The first team
	 * @param team2            The second team
	 * @param gamePointsTeam1  The points of the first team
	 * @param gamePointsTeam2  The points of the second team
	 * @param scorePointsTeam1 The score of the first team
	 * @param scorePointsTeam2 The score of the second team
	 * @param winnerPoints     The points of the winner (2 points for the winner for example)
	 */
	public _2TeamsMatchPoints(List<Pair<Player<G>, Player<G>>> pairs,
			Team<G> team1, Team<G> team2,
			List<GamePointsTeam<G>> gamePointsTeam1, List<GamePointsTeam<G>> gamePointsTeam2,
			double scorePointsTeam1, double scorePointsTeam2, double winnerPoints) {
		super();
		this.pairs = pairs;
		this.team1 = team1;
		this.team2 = team2;
		this.scorePointsTeam1 = scorePointsTeam1;
		this.scorePointsTeam2 = scorePointsTeam2;
		this.gamePointsTeam1 = gamePointsTeam1;
		this.gamePointsTeam2 = gamePointsTeam2;
		this.winnerPoints = winnerPoints;
	}

	/**
	 * Return the points of the winner.
	 *
	 * @return the points of the winner
	 */
	@Override
	public double winnerPoints() {
		return winnerPoints;
	}

	/**
	 * Return the points of the first team. Is the sum of the points of the players of the first team.
	 *
	 * @return the points of the first team
	 */
	public double totalPointsTeam1Players() {
		return gamePointsTeam1.stream().mapToDouble(GamePoints::getPoints).sum();
	}

	/**
	 * Return the points of the first team. Is the sum of the points of the players of the first team and the score points.
	 *
	 * @return the points of the first team
	 */
	public double totalPointsTeam1() {
		return scorePointsTeam1 + totalPointsTeam1Players();
	}

	/**
	 * Return the points of the second team. Is the sum of the points of the players of the second team and the score points.
	 *
	 * @return the points of the second team
	 */
	public double totalPointsTeam2() {
		return scorePointsTeam2 + totalPointsTeam2Players();
	}

	/**
	 * Return the points of the second team. Is the sum of the points of the players of the second team.
	 * @return the points of the second team
	 */
	public double totalPointsTeam2Players() {
		return gamePointsTeam2.stream().mapToDouble(GamePoints::getPoints).sum();
	}

//region setter/getter
	public List<Pair<Player<G>, Player<G>>> getPairs() {
		return pairs;
	}

	public Team<G> getTeam1() {
		return team1;
	}

	public Team<G> getTeam2() {
		return team2;
	}

	public double getScorePointsTeam1() {
		return scorePointsTeam1;
	}

	public double getScorePointsTeam2() {
		return scorePointsTeam2;
	}

	public List<GamePointsTeam<G>> getGamePointsTeam1() {
		return gamePointsTeam1;
	}

	public List<GamePointsTeam<G>> getGamePointsTeam2() {
		return gamePointsTeam2;
	}

	@Override
	public double getGameSetPointsFor(String player, int gameSetNumber) throws IllegalArgumentException {
		for (var gamePoints : gamePointsTeam1) {
			if (gamePoints.getPlayer().getCompleteNameWithUnderscore().equals(player)) {
				return gamePoints.getGameSetPointsFor(gameSetNumber);
			}
		}
		for (var gamePoints : gamePointsTeam2) {
			if (gamePoints.getPlayer().getCompleteNameWithUnderscore().equals(player)) {
				return gamePoints.getGameSetPointsFor(gameSetNumber);
			}
		}
		throw new IllegalArgumentException("Player not found");
	}

	public GamePoints getGamePointsForPlayer(String player) {
		for (var gamePoints : gamePointsTeam1) {
			if (gamePoints.getPlayer().getCompleteNameWithUnderscore().equals(player)) {
				return gamePoints;
			}
		}
		for (var gamePoints : gamePointsTeam2) {
			if (gamePoints.getPlayer().getCompleteNameWithUnderscore().equals(player)) {
				return gamePoints;
			}
		}
		throw new IllegalArgumentException("Player not found");
	}

	public double getWinnerPoints() {
		return winnerPoints;
	}
	/**
	 * Return the winner of the match.
	 *
	 * @return the winner of the match
	 * @throws IllegalStateException if the match is a draw
	 */
	@Override
	public Team<G> getWinner() throws IllegalStateException {
		if (isDraw()) {
			throw new IllegalStateException("Match is a draw");
		}
		if (scorePointsTeam1 > scorePointsTeam2) {
			return team1;
		}
		return team2;

	}

	/**
	 * Return the concrete points of the match per partie.
	 *
	 * @return a map with the points of the match
	 */
	@Override
	public Map<Team<G>, Double> getMatchPoints() {
		if (isDraw()) {
			return Map.of(team1, 1.0, team2, 1.0);
		}
		return totalPointsTeam1Players() > totalPointsTeam2Players() ?
				Map.of(team1, winnerPoints, team2, 0.) : Map.of(team1, 0., team2, winnerPoints);
	}

	/**
	 * When all parties have equal points, the match is a draw.
	 *
	 * @return true if the match is a draw
	 */
	@Override
	public boolean isDraw() {
		return totalPointsTeam1Players() == totalPointsTeam2Players();
	}
//endregion
}
