package core.point_system;

import core.game.Game;
import core.team_and_player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

	public double getGameSetPointsFor(int gameSetNumber) {
		return gameSetPoints.stream()
							.filter(it -> it.getGameSet().getGameSetNumber() == gameSetNumber)
							.mapToDouble(GameSetPoints::getPoints).sum();
	}

	//region setter/getter
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
