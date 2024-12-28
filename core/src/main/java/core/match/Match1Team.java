package core.match;

import core.game.Game;
import core.point_system.PointSystem;
import core.team_and_player.Player;
import core.team_and_player.Team;

import java.nio.file.Path;
import java.util.Map;

/**
 * A match with only one team.
 *
 * @param <G> The type of the {@link Game} the match is playing.
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @see Match
 * @since 1.0.0
 */
public class Match1Team<G extends Game> extends Match<G> {

	private Team<G> team;

	/**
	 * Creates a new match with only one team.
	 *
	 * @param config      The configuration of the match.
	 * @param matchInfo   The general information about the match.
	 * @param info        The state about the match.
	 * @param pointSystem The point system of the match.
	 * @param team        The team of the match.
	 * @param path        The path to the match in the file system.
	 */
	public Match1Team(MatchConfig config, GeneralMatchInfo matchInfo, MatchStatusInfo info, PointSystem<G> pointSystem,
			Team<G> team, Path path) {
		super(config, matchInfo, info, pointSystem, path);
		this.team = team;
	}

	/**
	 * Get the player of the team by name.
	 * @param name The name of the player.
	 * @return The player with the name.
	 */
	public Player<G> getTeamPlayerByName(String name) {
		return team.getPlayer(name);
	}

	/**
	 * Get the player of the team by position.
	 * @param position The position of the player.
	 * @return The player with the position.
	 */
	public Player<G> getTeamPlayerByPosition(int position) {
		return team.getPlayer(position);
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
		return new Team[]{team};
	}
//endregion
}
