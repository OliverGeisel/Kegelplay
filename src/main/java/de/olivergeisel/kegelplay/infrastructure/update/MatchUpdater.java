package de.olivergeisel.kegelplay.infrastructure.update;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.core.match.GameKind;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.MatchStatusInfo;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import de.olivergeisel.kegelplay.infrastructure.csv.GameCSVFileReader;
import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Updates the match state and the teams.
 * @param <G>
 */
public class MatchUpdater<G extends Game> {

	private static final System.Logger LOGGER = System.getLogger(MatchUpdater.class.getName());

	private final Match<G> match;

	public MatchUpdater(Match<G> match) {
		this.match = match;
	}

	public void updateMatch() throws IllegalStateException {
		if (match == null) {
			throw new IllegalStateException("Match is null");
		}
		// update state
		var stateFile = match.getBaseDir().resolve("status.ini");
		try {
			var stateIniFile = new IniFile(stateFile);
			var backup = match.getBaseDir().resolve("Backup-Daten").resolve("wk_eig_backup.ini");
			var durchgangStr = new IniFile(backup).getRegion("Allgemein").getValue("Durchgang");
			var durchgang = Integer.parseInt(durchgangStr);
			var newState = new MatchStatusInfo(stateIniFile, durchgang);
			match.setStatusInfo(newState);
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.ERROR, STR."Could not read state file for mactch: \{match.getBaseDir()}");
		}
		// update teams
		for (var team : match.getTeams()) {
			updateTeam(team);
		}
		// update state of games
		for (var game : match.getGames()) {
			game.checkState();
		}
	}

	private void updateTeam(Team<G> team) {
		loadTeamGames(team);
	}

	private void loadTeamGames(Team<G> team) {
		for (var playerFolder : Objects.requireNonNull(
				match.getBaseDir().resolve(team.getName()).toFile().listFiles())) {
			var path = playerFolder.toPath().resolve("werte.csv");
			var playerName = playerFolder.getName();
			var game = new GameCSVFileReader<Game120>(path, GameKind.GAME_120).readGame();
			var player =
					Arrays.stream(team.getPlayers()).filter(p -> p.getCompleteNameWithCommata().equals(playerName))
						  .findFirst().orElseThrow();
			game.setPlayer(player);
		}
	}

}
