package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;

import java.nio.file.Path;

public class Match1Team<T extends Game> extends Match<T> {

	private Team team;


	public Match1Team(Path path) {
		super(new MatchConfig120AllAgainst(), new GeneralMatchInfo(), null, path);
	}

	public Player getTeamPlayerByName(String name) {
		return team.getPlayer(name);
	}

	public Player getTeamPlayerByPosition(int position) {
		return team.getPlayer(position);
	}

	//region setter/getter
	@Override
	public Team[] getTeams() {
		return new Team[]{team};
	}
//endregion
}
