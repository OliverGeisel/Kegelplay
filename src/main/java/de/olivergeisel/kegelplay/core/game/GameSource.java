package de.olivergeisel.kegelplay.core.game;

public interface GameSource extends AutoCloseable, Iterable<Wurf> {

	Iterable<Wurf> get();
}
