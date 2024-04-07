package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.match.Match1Team;

public class _4PlayersAllAgainstController<G extends Game> extends DisplayGameController<G> {
	public _4PlayersAllAgainstController(Match1Team<G> match) {
		super(match);
	}
}
