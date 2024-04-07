package de.olivergeisel.kegelplay.gui;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.match.Match;

public class MatchView<T extends Game> {

	private Match<T> match;


	public MatchView(Match<T> match) {
		this.match = match;
	}

}
