package core.team_and_player;

import core.game.Game;

/**
 * A Team with 6 players. This is used for league matches.
 *
 * @param <G> The type of the game that is played in the match
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Team
 * @since 1.0.0
 */
public class Team6<G extends Game> extends Team<G> {

	/**
	 * Creates a new team with the given values. The team has 6 players. The substitutions are 4.
	 *
	 * @param name the name of the team
	 */
	public Team6(String name) {
		super(name, new GeneralTeamInfo(), new Player[6], new Player[6]);
	}


	/**
	 * Creates a new team with the given values.
	 *
	 * @param name          the name of the team
	 * @param info          the general information of the team
	 * @param players       the players of the team
	 * @param substitutions the substitutions of the team
	 * @throws IllegalArgumentException if the number of players is not 6
	 */
	public Team6(String name, GeneralTeamInfo info, Player<G>[] players, Player<G>[] substitutions)
			throws IllegalArgumentException {
		super(name, info, players, substitutions);
		if (players.length != 6) {
			throw new IllegalArgumentException("A team with 6 players must have 6 players.");
		}
	}

}
