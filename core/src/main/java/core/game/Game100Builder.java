package core.game;

import java.util.LinkedList;

public class Game100Builder extends GameBuilder<Game> {
	/**
	 * Build a game from a source. But can only build a game with its throws. any other information must be set manually.
	 *
	 * @param source The source of the game.
	 * @return The game defined by the {@link GameSource}.
	 */
	@Override
	public Game buildGame(GameSource source) {
		var gameThrows = source.get();
		final var throwsPerGameSet = 50;
		GameSet gameSet = null;
		int i = 0;
		var sets = new LinkedList<GameSet>();
		var setNumber = 0;
		for (var wurf : gameThrows) {
			var throwNumber = i++;
			var throwInGameSet = throwNumber % throwsPerGameSet;
			if (throwInGameSet == 0) {
				gameSet = new GameSet(50, 25, 25, setNumber++);
				sets.add(gameSet);
			}
			gameSet.set(throwInGameSet, wurf, 20);
		}
		if (sets.size() < 2) { // fill up with empty sets
			for (int j = sets.size(); j < 2; j++) {
				sets.add(new GameSet(50, 25, 25, j));
			}
		}
		var game = new Game100(null);
		game.setGameSets(sets);
		return game;
	}

	/**
	 * Build an empty game. There are no throws or other data set. any information must be set manually.
	 *
	 * @return a new empty game.
	 */
	@Override
	public Game buildEmptyGame() {
		return new Game100(null);
	}

	//region setter/getter
	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_100;
	}
//endregion
}
