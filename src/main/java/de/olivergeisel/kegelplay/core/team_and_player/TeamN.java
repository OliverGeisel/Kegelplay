package de.olivergeisel.kegelplay.core.team_and_player;

public class TeamN extends Team {

	public TeamN(String name, int numberPlayers) {
		super(name, new Player[numberPlayers], new Player[4]);
	}

	public TeamN(String name, Player[] players, Player[] substitutions) throws IllegalArgumentException {
		super(name, players, substitutions);
	}
}
