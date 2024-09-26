package core.point_system;

import core.game.Game120;
import core.match.Match;
import core.team_and_player.Player;
import core.team_and_player.Team;
import core.util.Pair;

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

	/**
	 * Sets the final points for the players of a match.
	 * The player with the most set points gets 1 point.
	 * If both players have the same set points, the player with the most score gets 1 point.
	 * If both players have the same score, both get 0,5 points.
	 *
	 * @param player1Game the points of team 1
	 * @param player2Game the points of team 2
	 * @param player1       the player 1
	 * @param player2       the player 2
	 */
	private static void setFinalPoints(GamePointsTeam<Game120> player1Game, GamePointsTeam<Game120> player2Game,
			Player<Game120> player1, Player<Game120> player2) {
		if (player1Game.getSumGameSetPoints() > player2Game.getSumGameSetPoints()) {
			player1Game.setPoints(1.0);
		} else if (player1Game.getSumGameSetPoints() < player2Game.getSumGameSetPoints()) {
			player2Game.setPoints(1.0);
		} else {
			if (player1.getGame().getTotalScore() > player2.getGame().getTotalScore()) {
				player1Game.setPoints(1.0);
			} else if (player1.getGame().getTotalScore() < player2.getGame().getTotalScore()) {
				player2Game.setPoints(1.0);
			} else {
				// draw (both get 0.5 points
				player1Game.setPoints(0.5);
				player2Game.setPoints(0.5);
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
			setFinalPoints(player1Points, player2Points, player1, player2);
			team1Result.add(player1Points);
			team2Result.add(player2Points);
		}
		var results = evalTeamScores(team1, team2);


		return new _2TeamsMatchPoints<>(pairs, team1, team2, team1Result, team2Result, results.getKey(),
				results.getValue(), 2.0);
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
		// Todo implement
		var matchPoints = getMatchPoints(match);
		var winner = matchPoints.getWinner();
		if (winner == null) {
			return List.of();
		}
		final var type = Winner.WinnerType.TEAM;
		var team1 = match.getTeams()[0];
		var team2 = match.getTeams()[1];
		if (winner.equals(team1)) {
			return List.of(new Winner(team1.getName(), 2, type));
		} else if (winner.equals(team2)) {
			return List.of(new Winner(team2.getName(), 2, type));
		}
		return List.of(new Winner(team1.getName(), 1, type),
				new Winner(team2.getName(), 1, type));
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
		var winner = evalTeamScores(match.getTeams()[0], match.getTeams()[1]);
		var team1Score = match.getTeams()[0].getTeamScore();
		var team2Score = match.getTeams()[1].getTeamScore();
		var team1Name = match.getTeams()[0].getName();
		var team2Name = match.getTeams()[1].getName();
		if (winner.getKey() > winner.getValue()) {
			return List.of(new Winner(team1Name, team1Score, Winner.WinnerType.TEAM));
		} else if (winner.getKey() < winner.getValue()) {
			return List.of(new Winner(team2Name, team2Score, Winner.WinnerType.TEAM));
		} else {
			return List.of(new Winner(team1Name, team1Score, Winner.WinnerType.TEAM),
					new Winner(team2Name, team2Score, Winner.WinnerType.TEAM));
		}
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
