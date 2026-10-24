package de.kegelplay;

import core.game.Game120;
import core.match.Match;
import de.kegelplay.generator.Generator;
import de.kegelplay.generator.MatchGenerator;

/**
 * Generator that can be used over the Command Line Interface
 */
public class GeneratorCli {

	public static void main(String[] args) {
		var scanner = new java.util.Scanner(System.in);
		System.out.print("Enter the number of teams to generate: ");
		int numberOfTeams = scanner.nextInt();
		System.out.print("Enter the number of players to generate: ");
		int numberOfPlayers = scanner.nextInt();
		var generator = new MatchGenerator<Game120>(numberOfTeams, numberOfPlayers);
		var match = generator.generate();
		var writer = new de.kegelplay.writer.MatchWriter(match);
		writer.write();
	}
}
