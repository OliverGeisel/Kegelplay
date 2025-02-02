package core.match;

import core.point_system.PointSystem;
import core.team_and_player.Team;

import java.nio.file.Path;
import java.util.Map;

/**
 * An in development class (prototype) for a match between alternating {@link core.game.GameKind}s.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see core.game.GameKind
 * @see Match
 * @since 1.0.0
 */
public class MatchAlternatingKind extends Match {

	private Team team;

	public MatchAlternatingKind(MatchConfig config, GeneralMatchInfo generalMatchInfo, MatchStatusInfo statusInfo,
			PointSystem pointSystem, Team team, Path path) {
		super(config, generalMatchInfo, statusInfo, pointSystem, path);
		this.team = team;
	}

	//region setter/getter
	@Override
	public Map<String, Double> getPoints() {
		return Map.of();
	}

	@Override
	public Map<String, Double> getSetPoints() {
		return Map.of();
	}

	@Override
	public Team[] getTeams() {
		return new Team[]{team};
	}
//endregion
}
