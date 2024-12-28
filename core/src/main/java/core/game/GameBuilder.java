package core.game;


import core.team_and_player.Player;

/**
 * Builder for {@link Game}. A GameBuilder can build a game from a source or an empty game.
 *
 * @param <G> The type of the {@link Game}.
 * @see Game
 * @see GameSource
 *
 * @since 1.0.0
 * @version 1.0.0
 * @author Oliver Geisel
 *
 */
public abstract class GameBuilder<G extends Game> {

	private Player<G> player;

	/**
	 * Build a game from a source. But can only build a game with its throws. any other information must be set manually.
	 * If a player is set, the player is set in the game.
	 * @param source The source of the game.
	 * @return The game defined by the {@link GameSource}.
	 */
	public abstract G buildGame(GameSource source);

//region setter/getter

	/**
	 * Get the kind of the game.
	 *
	 * @return The kind of the game.
	 */
	public abstract GameKind getGameKind();

	/**
	 * Build an empty game. There are no throws or other data set. any information must be set manually.
	 *
	 * @return a new empty game.
	 */
	public abstract G buildEmptyGame();

	/**
	 * Get the player of the game.
	 *
	 * @return The player of the game.
	 */
	protected Player<G> getPlayer() {
		return player;
	}

	/**
	 * Set the player of the game.
	 *
	 * @param player The player of the game.
	 */
	public void setPlayer(Player<G> player) {
		this.player = player;
	}
//endregion

}
