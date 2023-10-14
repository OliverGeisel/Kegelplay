package de.olivergeisel.kegelplay.core.match;

import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;

public class Match1Team extends Match {

	private Team team;


	public Match1Team() {
		super(new MatchConfig120AllAgainst());
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
