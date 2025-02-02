package core.point_system;

import core.game.Game;
import core.game.GameSet;
import core.match.Match;
import core.team_and_player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to store the points of a {@link Player} in a {@link Match}. The points depend on the {@link Game} that is
 * played in the match.
 *
 * @param <G> the type of the {@link Game} that is played in the match
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @see Match
 * @see Player
 * @since 1.0.0
 */
public abstract class GamePoints<G extends Game> {

	private Player<G> player;
	private List<GameSetPoints> gameSetPoints;

	/**
	 * Creates a new GamePoints object with the given player.
	 *
	 * @param player the player
	 */
	protected GamePoints(Player<G> player) {
		this.player = player;
		this.gameSetPoints = new ArrayList<>();
	}

	/**
	 * Creates a new GamePoints object with the given player and the given {@link GameSetPoints}.
	 *
	 * @param player1       the player for the points
	 * @param gameSetPoints all {@link GameSetPoints} of the player
	 */
	protected GamePoints(Player<G> player1, List<GameSetPoints> gameSetPoints) {
		this.player = player1;
		this.gameSetPoints = gameSetPoints;
	}

	/**
	 * Adds the points of a {@link GameSet} to the player.
	 * Will only add the points if the player is the player of the {@link GameSet}.
	 *
	 * @param player    the player
	 * @param setPoints the points of the {@link GameSet}
	 */
	public void addGameSetPoints(Player<G> player, GameSetPoints setPoints) {
		if (this.player.equals(player)) {
			gameSetPoints.add(setPoints);
		}
	}

	/**
	 * Returns the points in the selected {@link GameSet} by number.
	 *
	 * @return the points for the selected {@link GameSet}
	 *
	 * @throws IllegalArgumentException if the {@link GameSet} number is not valid
	 */
	public double getGameSetPointsFor(int gameSetNumber) throws IllegalArgumentException {
		if (gameSetNumber < 0 || player.getGame().getNumberOfGameSets() + 1 < gameSetNumber) {
			throw new IllegalArgumentException("GameSet number is not valid");
		}
		return gameSetPoints.stream()
							.filter(it -> it.getGameSet().getGameSetNumber() == gameSetNumber)
							.mapToDouble(GameSetPoints::getPoints).sum();
	}

	//region setter/getter

	/**
	 * Returns the points of the player. It's the sum of all {@link GameSetPoints} of the player.
	 *
	 * @return the points of the player
	 */
	public abstract double getPoints();

	/**
	 * Returns if the player is the winner in the fight against his opponent.
	 *
	 * @return if the player is the winner.
	 */
	public abstract boolean isWinner();

	/**
	 * Returns if the player is the loser in the fight against his opponent.
	 *
	 * @return if the player is the loser
	 */
	public abstract boolean isLoser();

	/**
	 * Returns if the player has a draw in the fight against his opponent.
	 *
	 * @return if the player has a draw
	 */
	public abstract boolean isDraw();

	/**
	 * Returns the player of the points.
	 *
	 * @return the player of the points
	 */
	public Player<G> getPlayer() {
		return player;
	}

	/**
	 * Returns the {@link GameSetPoints} of the player.
	 *
	 * @return the {@link GameSetPoints} of the player
	 */
	public List<GameSetPoints> getGameSetPoints() {
		return Collections.unmodifiableList(gameSetPoints);
	}

	/**
	 * Returns the sum of all {@link GameSetPoints} of the player.
	 *
	 * @return the sum of all {@link GameSetPoints} of the player
	 */
	public double getSumGameSetPoints() {
		return gameSetPoints.stream().mapToDouble(GameSetPoints::getPoints).sum();
	}

//endregion
}
