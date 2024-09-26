package core.team_and_player;

import core.game.Game;

public class TeamN<G extends Game> extends Team<G> {

	public TeamN(String name, GeneralTeamInfo info, int numberPlayers) {
		super(name, info, new Player[numberPlayers], new Player[4]);
	}

	public TeamN(String name, GeneralTeamInfo info, Player<G>[] players, Player<G>[] substitutions)
			throws IllegalArgumentException {
		super(name, info, players, substitutions);
	}
}
