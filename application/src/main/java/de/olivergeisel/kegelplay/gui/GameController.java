package de.olivergeisel.kegelplay.gui;


import core.game.Game;

/**
 * Interface for all controllers that are responsible for a game.
 * It has a major difference to the {@link DisplayGameController}. Its purpose is to control the game and not to display it.
 *
 * @param <G> the type of the game
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @since 1.0.0
 */
public interface GameController<G extends Game> {
}
