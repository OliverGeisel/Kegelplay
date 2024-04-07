package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * The normal point system for a match with two teams and 120 throws.
 * Each player of a team plays against one player of the other team.
 * The winner of this partie get 1 point, the loser 0 points.
 * Both players play 120 in 4 sets with 30 throws each. The player with the higher score in a set gets 1 set point.
 * If both players have the same score, both get 0,5 set points.
 * The player with the most set points wins the game. If both players have the same set points, the score decides.
 * If the score is equal, both players get 0,5 game points.
 * In total, you can get one game point per player in a team and 2 extra points for the team score.
 * If the team score is equal, both teams get 1 point.
 * The match is a draw when both teams have the same game points.
 * The winner is the team with the most game points.
 * The winner team get 2 points, the loser 0 points.
 * If the game is a draw, both teams get 1 point.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see PointSystemCriteria#TEAM
 * @see PointSystem
 * @see MatchPoints
 * @since 1.0.0
 */
public class _2Teams120PointSystem extends PointSystem<Game120> {


	public static List<Pair<Player<Game120>, Player<Game120>>> getPairings(Team<Game120> team1, Team<Game120> team2) {
		var back = new ArrayList<Pair<Player<Game120>, Player<Game120>>>();
		for (int i = 0; i < team1.getNumberOfPlayers(); i++) {
			back.add(new Pair<>(team1.getPlayer(i), team2.getPlayer(i)));
		}
		return back;
	}

	private static void setFinalPoints(GamePointsTeam<Game120> player1Points, GamePointsTeam<Game120> player2Points,
			Player<Game120> player1, Player<Game120> player2) {
		if (player1Points.getSumGameSetPoints() > player2Points.getSumGameSetPoints()) {
			player1Points.setPoints(1.0);
		} else if (player1Points.getSumGameSetPoints() < player2Points.getSumGameSetPoints()) {
			player2Points.setPoints(1.0);
		} else {
			if (player1.getGame().getTotalScore() > player2.getGame().getTotalScore()) {
				player1Points.setPoints(1.0);
			} else if (player1.getGame().getTotalScore() < player2.getGame().getTotalScore()) {
				player2Points.setPoints(1.0);
			} else {
				// draw (both get 0.5 points
				player1Points.setPoints(0.5);
				player2Points.setPoints(0.5);
			}
		}
	}

	@Override
	public MatchPoints<Team<Game120>> getMatchPoints(Match<Game120> match) throws IllegalArgumentException {
		if (match == null) {
			throw new IllegalArgumentException("Match must not be null");
		}
		if (match.getTeams().length != 2) {
			throw new IllegalArgumentException("Match must have exactly two teams");
		}
		var team1 = match.getTeams()[0];
		var team2 = match.getTeams()[1];
		var pairs = getPairings(team1, team2);
		var team1Result = new ArrayList<GamePointsTeam<Game120>>();
		var team2Result = new ArrayList<GamePointsTeam<Game120>>();
		for (var pair : pairs) {
			var player1 = pair.getKey();
			var player2 = pair.getValue();
			var player1Points = new GamePointsTeam<>(player1, 0.0);
			var player2Points = new GamePointsTeam<>(player2, 0.0);
			for (int i = 0; i < 4; i++) {
				var setPoints = getSetPoints(i, player1, player2);
				player1Points.addGameSetPoints(player1, setPoints.getGamePoints(player1));
				player2Points.addGameSetPoints(player2, setPoints.getGamePoints(player2));
			}
			team1Result.add(player1Points);
			team2Result.add(player2Points);
		}
		var results = evalTeamScores(team1, team2);


		return new _2TeamsMatchPoints<>(pairs, team1, team2, team1Result, team2Result, results.getKey(),
				results.getValue(), 2.0);
	}

	private Pair<Double, Double> evalTeamScores(Team<Game120> team1, Team<Game120> team2) {
		var team1Score = team1.getTeamScore();
		var team2Score = team2.getTeamScore();
		if (team1Score > team2Score) {
			return new Pair<>(2.0, 0.0);
		} else if (team1Score < team2Score) {
			return new Pair<>(0.0, 2.0);
		} else {
			return new Pair<>(1.0, 1.0);
		}
	}

	private GameSetPointsCollection getSetPoints(int setNumber, Player<Game120> player1, Player<Game120> player2) {
		var player1Set = player1.getGame().getDurchgang(setNumber);
		var player2Set = player2.getGame().getDurchgang(setNumber);
		var back = new GameSetPointsCollection();
		if (player1Set.getScore() > player2Set.getScore()) {
			back.setScore(player1, 1, player1Set);
			back.setScore(player2, 0, player2Set);
		} else if (player1Set.getScore() < player2Set.getScore()) {
			back.setScore(player1, 0, player1Set);
			back.setScore(player2, 1, player2Set);
		} else {
			back.setScore(player1, 0.5, player1Set);
			back.setScore(player2, 0.5, player2Set);
		}
		return back;
	}


}