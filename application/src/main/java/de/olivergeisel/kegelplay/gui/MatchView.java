package de.olivergeisel.kegelplay.gui;

import core.game.Game;
import core.match.Match;

public class MatchView<T extends Game> {

	private Match<T> match;


	public MatchView(Match<T> match) {
		this.match = match;
	}

}
