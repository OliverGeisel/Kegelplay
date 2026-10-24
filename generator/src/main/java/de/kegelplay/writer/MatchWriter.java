package de.kegelplay.writer;

import core.match.Match;
import core.team_and_player.Team;
import lombok.extern.slf4j.Slf4j;

/**
 * Writer for matches
 *
 * @author Oliver Geisel
 * @version 1.0
 * @since 1.0
 */
@Slf4j
public class MatchWriter {

	private Match match;

	public MatchWriter(Match match) {
		this.match = match;
	}

	public void write() {
		log.info("Write general structure of the Match");
		// Todo create folder structure
		// Todo implement writing of general match structure
		log.debug("Write Teams:");
		for (Team team : match.getTeams()) {
			log.info("Write Team: {}", team.getName());
			for (var player : team.getPlayers()) {
				log.info("Write Player: {}", player.getCompleteName());
				// Todo implement writing of player data

				// Write games of the player
				log.debug("Write Games of Player: {}", player.getCompleteName());

				var game = player.getGame();
				log.info("Write Game: {} for Player: {}", game, player.getCompleteName());
			}
		}
	}

}
