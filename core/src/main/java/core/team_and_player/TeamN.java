package core.team_and_player;

import core.game.Game;

/**
 * A Team with a free number of players. This is used for training matches or cups.
 *
 * @param <G> The type of the game that is played in the match
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Team
 * @since 1.0.0
 */
public class TeamN<G extends Game> extends Team<G> {

	/**
	 * Creates a new team with the given values.
	 *
	 * @param name          the name of the team
	 * @param info          the general information of the team
	 * @param numberPlayers the number of players in the team
	 */
	public TeamN(String name, GeneralTeamInfo info, int numberPlayers) {
		super(name, info, new Player[numberPlayers], new Player[4]);
	}

	/**
	 * Creates a new team with the given values.
	 *
	 * @param name          the name of the team
	 * @param info          the general information of the team
	 * @param players       the players of the team
	 * @param substitutions the substitutions of the team
	 */
	public TeamN(String name, GeneralTeamInfo info, Player<G>[] players, Player<G>[] substitutions) {
		super(name, info, players, substitutions);
	}
}
