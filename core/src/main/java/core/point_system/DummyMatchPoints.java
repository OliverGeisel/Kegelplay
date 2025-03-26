package core.point_system;

import java.util.Map;

/**
 * A fake implementation of {@link MatchPoints}.
 * This class is used when there is no {@link PointSystem} for an {@link core.match.Match} needed.
 */
public class DummyMatchPoints<W extends Winner> extends MatchPoints<W> {
	@Override
	public double winnerPoints() {
		return 0;
	}

	@Override
	public double getGameSetPointsFor(String player, int gameSetNumber) throws IllegalArgumentException {
		return 0;
	}

//region setter/getter
	@Override
	public W getWinner() throws IllegalStateException {
		return null;
	}

	@Override
	public Map getMatchPoints() {
		return Map.of();
	}

	@Override
	public boolean isDraw() {
		return false;
	}
//endregion
}
