package core.game;

import java.util.LinkedList;

/**
 * Builder for a {@link Game200}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see GameBuilder
 * @see Game200
 * @since 1.0.0
 */
public class Game200Builder extends GameBuilder<Game200> {


	@Override
	public Game200 buildGame(GameSource source) {
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
		if (sets.size() < 4) { // fill up with empty sets
			for (int j = sets.size(); j < 4; j++) {
				sets.add(new GameSet(50, 25, 25, j));
			}
		}
		var game = new Game200(null);
		game.setGameSets(sets);
		if (getPlayer() != null) {
			game.setPlayer(getPlayer());
		}
		return game;
	}

	@Override
	public Game200 buildEmptyGame() {
		return new Game200(getPlayer());
	}

	//region setter/getter
	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_200;
	}
//endregion
}
