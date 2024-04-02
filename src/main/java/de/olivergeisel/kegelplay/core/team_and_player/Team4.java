package de.olivergeisel.kegelplay.core.team_and_player;

public class Team4 extends Team {

	/**
	 * Creates a team with 4 players.
	 * @param name the name of the team
	 * @param players the players of the team
	 * @throws IllegalArgumentException if the number of players is not 4
	 */
	public Team4(String name, Player[] players, Player[] substitutions) throws IllegalArgumentException {
		super(name, players, substitutions);
		if (players.length != 4) {
			throw new IllegalArgumentException("A team with 4 players must have 4 players.");
		}
	}

	protected Team4(String name) {
		super(name, new Player[4], new Player[4]);
	}

}
