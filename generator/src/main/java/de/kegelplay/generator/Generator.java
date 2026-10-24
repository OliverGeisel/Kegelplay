package de.kegelplay.generator;

/**
 * Generic generator interface
 *
 * @author Oliver Geisel
 * @version 1.0
 * @since 1.0
 *
 * @param <O> the type of object to be generated
 */
public interface Generator <O> {
	O generate();
}
