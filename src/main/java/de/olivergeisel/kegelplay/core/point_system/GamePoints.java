package de.olivergeisel.kegelplay.core.point_system;

import de.olivergeisel.kegelplay.core.team_and_player.Player;

import java.util.ArrayList;
import java.util.List;

public class GamePoints {

	private Player              player;
	private List<GameSetPoints> gameSetPoints;

	public GamePoints(Player player) {
		this.player = player;
		this.gameSetPoints = new ArrayList<>();
	}

	public GamePoints(Player player1, List<GameSetPoints> gameSetPoints) {
		this.player = player1;
		this.gameSetPoints = gameSetPoints;
	}

	public void addGameSetPoints(Player player, GameSetPoints setPoints) {
		if (this.player.equals(player)) {
			gameSetPoints.add(setPoints);
		}
	}

	//region setter/getter
	public double getScore() {
		return gameSetPoints.stream().mapToDouble(GameSetPoints::getScore).sum();
	}
//endregion
}
