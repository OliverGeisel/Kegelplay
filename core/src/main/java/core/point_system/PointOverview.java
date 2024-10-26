package core.point_system;

import core.game.Game;
import core.match.Match;
import core.team_and_player.Player;

/**
 * The point overview of a match.
 *
 * @param <G> the type of the game that is played in the match
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class PointOverview<G extends Game> {

	public void evaluate(Match<G> match) {

	}

	public GamePoints<G> getGamePointsFor(Player<G> player) {
		return null;
	}

//region setter/getter
	public TeamPointOverview<G> getTeamPointOverview() {
		return null;
	}
//endregion
}
