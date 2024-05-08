package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import javafx.util.Pair;

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

	public double totalPointsTeam1() {
		return gamePointsTeam1.stream().mapToDouble(GamePoints::getPoints).sum();
	}

	public double totalPointsTeam2() {
		return gamePointsTeam2.stream().mapToDouble(GamePoints::getPoints).sum();
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

//region setter/getter
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
		return totalPointsTeam1() > totalPointsTeam2() ?
				Map.of(team1, winnerPoints, team2, 0.) : Map.of(team1, 0., team2, winnerPoints);
	}

	/**
	 * When all parties have equal points, the match is a draw.
	 *
	 * @return true if the match is a draw
	 */
	@Override
	public boolean isDraw() {
		return totalPointsTeam1() == totalPointsTeam2();
	}
//endregion
}
