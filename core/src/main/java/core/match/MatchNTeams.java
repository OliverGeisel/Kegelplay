package core.match;


import core.game.Game;
import core.point_system.PointSystem;
import core.team_and_player.Team;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * A match with n teams.
 *
 * @param <G> The type of the game the match is about.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @see Team
 * @since 1.0.0
 */
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
	public Map<String, Double> getPoints() {
		return Map.of();
	}

	@Override
	public Map<String, Double> getSetPoints() {
		throw new UnsupportedOperationException("Not implemented yet");
	}

	@Override
	public Team<G>[] getTeams() {
		return teams.toArray(new Team[0]);
	}
//endregion
}
