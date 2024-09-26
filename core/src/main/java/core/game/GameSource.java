package core.game;


/**
 * This interface represents a source of all {@link Wurf} of a game.
 *
 * The source can be a file, a database-Entity, ...
 *
 *
 * @see Wurf
 * @see AutoCloseable
 * @see Iterable
 * @see Game
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public interface GameSource extends AutoCloseable, Iterable<Wurf> {

	Iterable<Wurf> get();
}
