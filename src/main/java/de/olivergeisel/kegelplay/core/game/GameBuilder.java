package de.olivergeisel.kegelplay.core.game;


/**
 * Builder for games. A game builder can build a game from a source or an empty game.
 *
 * @param <G> The type of the game.
 * @version 1.0.0
 * @see Game
 * @see GameSource
 * @since 1.0.0
 */
public abstract class GameBuilder<G extends Game> {

	/**
	 * Build a game from a source. But can only build a game with its throws. any other information must be set manually.
	 *
	 * @param source The source of the game.
	 * @return The game defined by the {@link GameSource}.
	 */
	public abstract G buildGame(GameSource source);

	/**
	 * Build an empty game. There are no throws or other data set. any information must be set manually.
	 *
	 * @return a new empty game.
	 */
	public abstract G buildEmptyGame();


}
