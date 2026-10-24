package de.kegelplay.generator;

import core.game.Game;
import core.match.Match;
import lombok.Getter;
import lombok.Setter;

/**
 * Generates a match with a given number of teams and players
 *
 * @author Oliver Geisel
 * @version 1.0
 * @since 1.0
 */
@Setter
@Getter
public class MatchGenerator<G extends Game> implements Generator<Match<G>> {

	private int numberOfTeams;
	private int numberOfPlayers;

	public MatchGenerator(int numberOfTeams, int numberOfPlayers) {
		this.numberOfTeams = numberOfTeams;
		this.numberOfPlayers = numberOfPlayers;
	}

	@Override
	public Match<G> generate() {
		return null;
	}
}
