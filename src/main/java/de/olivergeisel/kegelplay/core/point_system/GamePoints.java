package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.team_and_player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class to store the points of a {@link Player} in a {@link Match}. The points depend on the {@link Game} that is
 * played in the match.
 * @param <G> the type of the {@link Game} that is played in the match
 *
 * @see Game
 * @see Match
 * @see Player
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public abstract class GamePoints<G extends Game> {

	private Player<G> player;
	private List<GameSetPoints> gameSetPoints;

	protected GamePoints(Player<G> player) {
		this.player = player;
		this.gameSetPoints = new ArrayList<>();
	}

	protected GamePoints(Player<G> player1, List<GameSetPoints> gameSetPoints) {
		this.player = player1;
		this.gameSetPoints = gameSetPoints;
	}

	public void addGameSetPoints(Player<G> player, GameSetPoints setPoints) {
		if (this.player.equals(player)) {
			gameSetPoints.add(setPoints);
		}
	}

	/**
	 * Returns the points in the selected {@link de.olivergeisel.kegelplay.core.game.GameSet} by number.
	 *
	 * @return the points for the selected {@link de.olivergeisel.kegelplay.core.game.GameSet}
	 * @throws IllegalArgumentException if the {@link de.olivergeisel.kegelplay.core.game.GameSet} number is not valid
	 */
	public double getGameSetPointsFor(int gameSetNumber) throws IllegalArgumentException {
		if (gameSetNumber < 0 || player.getGame().getNumberOfDurchgaenge() + 1 < gameSetNumber) {
			throw new IllegalArgumentException("GameSet number is not valid");
		}
		return gameSetPoints.stream()
							.filter(it -> it.getGameSet().getGameSetNumber() == gameSetNumber)
							.mapToDouble(GameSetPoints::getPoints).sum();
	}

	//region setter/getter

	/**
	 * Returns the points of the player. It's the sum of all {@link GameSetPoints} of the player.
	 * @return the points of the player
	 */
	public abstract double getPoints();

	public abstract boolean isWinner();

	public abstract boolean isLoser();

	public abstract boolean isDraw();

	public Player<G> getPlayer() {
		return player;
	}

	public List<GameSetPoints> getGameSetPoints() {
		return Collections.unmodifiableList(gameSetPoints);
	}

	public double getSumGameSetPoints() {
		return gameSetPoints.stream().mapToDouble(GameSetPoints::getPoints).sum();
	}

//endregion
}
