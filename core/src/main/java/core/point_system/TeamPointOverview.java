package core.point_system;

import core.game.Game;
import core.team_and_player.Player;
import core.team_and_player.Team;

/**
 * Represents all points of a team.
 * <p>
 * This includes the {@link GamePoints} and {@link GameSetPoints} of each Player and the {@link MatchPoints} for the team.
 * </p>
 *
 * @param <G> the type of the {@link Game} that is played in the {@link core.match.Match}
 */
public class TeamPointOverview<G extends Game> {
	private final Team<G> team;

	public TeamPointOverview(Team<G> team) {
		this.team = team;
	}

	/**
	 * Returns the game points of a player.
	 *
	 * @param player the player
	 * @return the game points of the player
	 */
	public double getGamePointsFor(Player<G> player) {
		return 0;
	}

	/**
	 * Returns the sum of {@link GameSetPoints} of a player.
	 *
	 * @param player the player
	 * @return the game set points of the player
	 */
	public double getGameSetPointsFor(Player<G> player) {
		return 0;
	}

	/**
	 * Get the GameSetPoints of a player for a specific set.
	 *
	 * @param player the player
	 * @param set    the set number
	 * @return the game set points of the player
	 */
	public double getGameSetPointsFor(Player<G> player, int set) {
		return 0;
	}

//region setter/getter

	/**
	 * Returns the points of the team for the match that will be added in the league table.
	 *
	 * @return the points of the team for the match that will be added in the league table
	 */
	public double getMatchPoints() {
		return 0;
	}

	public Team<G> getTeam() {
		return team;
	}

	/**
	 * Sum of all game points of the team.
	 *
	 * @return the sum of all game points of the team
	 */
	public double getGamePoints() {
		return 0;
	}
//endregion

}
