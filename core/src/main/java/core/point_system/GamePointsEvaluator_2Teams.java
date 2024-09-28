package core.point_system;


import core.game.Game;
import core.team_and_player.Player;
import core.util.Pair;

/**
 * The GamePointsEvaluator evaluates 2 players and decides who wins the game.
 */
public class GamePointsEvaluator_2Teams {

	/**
	 * Evaluates the two games and returns the points for each game.
	 *
	 * @param player1 The first player
	 * @param player2 The second player
	 * @param <G>     The type of the game
	 * @return A pair of the points for each game
	 */
	public static <G extends Game> Pair<GamePointsPlayer<G>, GamePointsPlayer<G>> evaluate(Player<G> player1,
			Player<G> player2) {
		{
			if (player1.getClass() != player2.getClass()) {
				throw new IllegalArgumentException("Both games must be of the same type");
			}
			var points1 = new GamePointsPlayer<>(player1);
			var points2 = new GamePointsPlayer<>(player2);
			for (int i = 0; i < player1.getGame().getNumberOfDurchgaenge(); i++) {
				var collection = getSetPoints(i, player1, player2);
				points1.addGameSetPoints(player1, collection.getGamePoints(player1));
				points2.addGameSetPoints(player2, collection.getGamePoints(player2));
			}
			setFinalPoints(points1, points2, player1, player2);
			return new Pair<>(points1, points2);
		}
	}

	private static <G extends Game> void setFinalPoints(
			GamePointsPlayer<G> player1Game, GamePointsPlayer<G> player2Game,
			Player<G> player1, Player<G> player2) {
		if (player1Game.getSumGameSetPoints() > player2Game.getSumGameSetPoints()) {
			player1Game.setTeamPoints(1.0);
		} else if (player1Game.getSumGameSetPoints() < player2Game.getSumGameSetPoints()) {
			player2Game.setTeamPoints(1.0);
		} else {
			if (player1.getGame().getTotalScore() > player2.getGame().getTotalScore()) {
				player1Game.setTeamPoints(1.0);
			} else if (player1.getGame().getTotalScore() < player2.getGame().getTotalScore()) {
				player2Game.setTeamPoints(1.0);
			} else {
				// draw (both get 0.5 points
				player1Game.setTeamPoints(0.5);
				player2Game.setTeamPoints(0.5);
			}
		}
	}

	private static <G extends Game> GameSetPointsCollection getSetPoints(int setNumber, Player<G> player1,
			Player<G> player2) {
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
