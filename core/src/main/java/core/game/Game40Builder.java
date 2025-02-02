package core.game;

import core.team_and_player.Player;

import java.util.LinkedList;

/**
 * Builder for a {@link Game40}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see GameBuilder
 * @see Game40
 * @since 1.0.0
 */
public class Game40Builder extends GameBuilder<Game40> {


	/**
	 * {@inheritDoc}
	 *
	 * @param source {@inheritDoc}
	 * @return {@inheritDoc}
	 */
	@Override
	public Game40 buildGame(GameSource source) {
		var gameThrows = source.get();
		final var throwsPerGameSet = 20;
		GameSet gameSet = null;
		int i = 0;
		var sets = new LinkedList<GameSet>();
		var setNumber = 0;
		for (var wurf : gameThrows) {
			var throwNumber = i++;
			var throwInGameSet = throwNumber % throwsPerGameSet;
			if (throwInGameSet == 0) {
				gameSet = new GameSet(20, 10, 10, setNumber++);
				sets.add(gameSet);
			}
			gameSet.set(throwInGameSet, wurf, 8);
		}
		if (sets.size() < 2) { // fill up with empty sets
			for (int j = sets.size(); j < 2; j++) {
				sets.add(new GameSet(20, 10, 10, j));
			}
		}
		var game = new Game40(null);
		game.setGameSets(sets);
		return game;
	}

	/**
	 * Build an empty game.
	 * The {@link Player} will be null.
	 *
	 * @return Empty game
	 */
	@Override
	public Game40 buildEmptyGame() {
		return new Game40(null);
	}

//region setter/getter

	/**
	 * {@inheritDoc}
	 *
	 * @return {@inheritDoc}
	 */
	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_40;
	}
//endregion
}