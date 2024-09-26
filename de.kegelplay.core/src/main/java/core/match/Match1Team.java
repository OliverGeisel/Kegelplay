package core.match;

import core.game.Game;
import core.point_system.PointSystem;
import core.team_and_player.Player;
import core.team_and_player.Team;

import java.nio.file.Path;

public class Match1Team<G extends Game> extends Match<G> {

	private Team<G> team;

	public Match1Team(MatchConfig config, GeneralMatchInfo matchInfo, MatchStatusInfo info, PointSystem<G> pointSystem,
			Team<G> team, Path path) {
		super(config, matchInfo, info, pointSystem, path);
		this.team = team;
	}

	public Player<G> getTeamPlayerByName(String name) {
		return team.getPlayer(name);
	}

	public Player<G> getTeamPlayerByPosition(int position) {
		return team.getPlayer(position);
	}

	//region setter/getter
	@Override
	public Team<G>[] getTeams() {
		return new Team[]{team};
	}
//endregion
}
