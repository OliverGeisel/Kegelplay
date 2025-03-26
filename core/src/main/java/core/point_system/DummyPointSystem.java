package core.point_system;

import core.game.Game;
import core.match.Match;

import java.util.List;

/**
 * A fake implementation of {@link PointSystem}.
 * This class is used when there is no {@link PointSystem} for an {@link core.match.Match} needed.
 */
public class DummyPointSystem<G extends Game> extends PointSystem<G> {
	@Override
	public List<Winner> getWinner(Match match) {
		return List.of();
	}

	@Override
	public MatchPoints getMatchPoints(Match match) {
		return new DummyMatchPoints();
	}
}
