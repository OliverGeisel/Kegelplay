package core.game;


/**
 * This interface represents a source of all {@link Wurf} of a game.
 * The source can be a file, a database-Entity, ...
 * NOT USED YET
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Wurf
 * @see AutoCloseable
 * @see Iterable
 * @see Game
 * @since 1.0.0
 */
public interface GameSource extends AutoCloseable, Iterable<Wurf> {

	/**
	 * Get all throws of the game as an {@link Iterable}.
	 *
	 * @return All throws of the game as an {@link Iterable}.
	 */
	Iterable<Wurf> get();
}
