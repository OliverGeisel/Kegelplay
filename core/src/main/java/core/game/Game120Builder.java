package core.game;


import java.util.LinkedList;

/**
 * Builder for a {@link Game120}. Build the game from a {@link GameSource}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see GameBuilder
 * @see Game120
 * @since 1.0.0
 */
public class Game120Builder extends GameBuilder<Game120> {


	@Override
	public Game120 buildGame(GameSource source) {
		var gameThrows = source.get();
		final var throwsPerGameSet = 30;
		GameSet gameSet = null;
		int i = 0;
		var sets = new LinkedList<GameSet>();
		var setNumber = 0;
		for (var wurf : gameThrows) {
			var throwNumber = i++;
			var throwInGameSet = throwNumber % throwsPerGameSet;
			if (throwInGameSet == 0) {
				gameSet = new GameSet(30, 15, 15, setNumber++);
				sets.add(gameSet);
			}
			gameSet.set(throwInGameSet, wurf, 12);
		}
		if (sets.size() < 4) { // fill up with empty sets
			for (int j = sets.size(); j < 4; j++) {
				sets.add(new GameSet(30, 15, 15, j));
			}
		}
		var game = new Game120(null);
		game.setGameSets(sets);
		return game;
	}

	@Override
	public Game120 buildEmptyGame() {
		return new Game120(null);
	}

	//region setter/getter
	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_120;
	}
//endregion
}
