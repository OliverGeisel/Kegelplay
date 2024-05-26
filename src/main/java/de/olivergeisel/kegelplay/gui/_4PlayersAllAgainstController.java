package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.match.Match1Team;
import de.olivergeisel.kegelplay.core.match.*;

public class _4PlayersAllAgainstController<G extends Game> extends DisplayGameController<G> {
	public _4PlayersAllAgainstController(Match<G> match) {
		super(match);
	}
}
