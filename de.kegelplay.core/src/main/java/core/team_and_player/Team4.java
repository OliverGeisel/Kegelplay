package core.team_and_player;

import core.game.Game;

/**
 * Represents a {@link Team} with exact 4 players.
 *
 * @param <G> the type of the game
 * @see Team
 * @see Player
 * @see GeneralTeamInfo
 * @see Game
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class Team4<G extends Game> extends Team<G> {

	/**
	 * Creates a team with 4 players.
	 * @param name the name of the team
	 * @param players the players of the team
	 * @throws IllegalArgumentException if the number of players is not 4
	 */
	public Team4(String name, GeneralTeamInfo info, Player<G>[] players, Player<G>[] substitutions)
			throws IllegalArgumentException {
		super(name, info, players, substitutions);
		if (players.length != 4) {
			throw new IllegalArgumentException("A team with 4 players must have 4 players.");
		}
	}

	protected Team4(String name) {
		super(name, new GeneralTeamInfo(), new Player[4], new Player[4]);
	}

}
