package de.olivergeisel.kegelplay.infrastructure.data_reader;


import core.game.Game;
import core.match.GeneralMatchInfo;
import core.match.Match;
import core.match.MatchConfig;
import core.match.MatchStatusInfo;
import core.point_system.PointSystem;
import core.team_and_player.Team;

import java.nio.file.Path;
import java.util.List;

public class MatchNTeams<G extends Game> extends Match<G> {
	private final List<Team<G>> teams;


	public MatchNTeams(MatchConfig config, GeneralMatchInfo general, MatchStatusInfo stateInfo,
			PointSystem<G> pointSystem,
			List<Team<G>> teams, Path baseDir) {
		super(config, general, stateInfo, pointSystem, baseDir);
		this.teams = teams;
	}

//region setter/getter
	@Override
	public Team<G>[] getTeams() {
		return teams.toArray(new Team[0]);
	}
//endregion
}
