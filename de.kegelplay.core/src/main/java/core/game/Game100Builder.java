package core.game;

public class Game100Builder extends GameBuilder<Game> {
	/**
	 * Build a game from a source. But can only build a game with its throws. any other information must be set manually.
	 *
	 * @param source The source of the game.
	 * @return The game defined by the {@link GameSource}.
	 */
	@Override
	public Game buildGame(GameSource source) {
		return null;
	}

	/**
	 * Build an empty game. There are no throws or other data set. any information must be set manually.
	 *
	 * @return a new empty game.
	 */
	@Override
	public Game buildEmptyGame() {
		return null;
	}

//region setter/getter
	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_100;
	}
//endregion
}
