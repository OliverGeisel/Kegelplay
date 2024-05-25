package core.game;

import java.util.LinkedList;

public class Game40Builder extends GameBuilder<Game40> {


	@Override
	public Game40 buildGame(GameSource source) {
		var gameThrows = source.get();
		final var throwsPerDurchgang = 20;
		GameSet gameSet = null;
		int i = 0;
		var sets = new LinkedList<GameSet>();
		var setNumber = 0;
		for (var wurf : gameThrows) {
			var throwNumber = i++;
			var throwInGameSet = throwNumber % throwsPerDurchgang;
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
		game.setDurchgaenge(sets);
		return game;
	}

	@Override
	public Game40 buildEmptyGame() {
		return new Game40(null);
	}

//region setter/getter
	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_40;
	}
//endregion
}