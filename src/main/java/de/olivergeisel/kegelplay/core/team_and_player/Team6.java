package de.olivergeisel.kegelplay.core.team_and_player;

import de.olivergeisel.kegelplay.core.game.Game;

public class Team6<G extends Game> extends Team<G> {

	public Team6(String name) {
		super(name, new Player[6], new Player[6]);
	}

	public Team6(String name, Player<G>[] players, Player<G>[] substitutions) throws IllegalArgumentException {
		super(name, players, substitutions);
		if (players.length != 6) {
			throw new IllegalArgumentException("A team with 6 players must have 6 players.");
		}
	}

}
