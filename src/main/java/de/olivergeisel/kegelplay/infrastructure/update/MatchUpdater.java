package de.olivergeisel.kegelplay.infrastructure.update;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.game.Game120;
import de.olivergeisel.kegelplay.core.game.GameKind;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.MatchStatusInfo;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import de.olivergeisel.kegelplay.infrastructure.csv.CSVFileReader;
import de.olivergeisel.kegelplay.infrastructure.csv.GameCSVFileReader;
import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
		// TODO read the players again. This is necessary because the players could have changed in the meantime
		//  (e.g. a player was substituted)
		var dir = match.getBaseDir().resolve(STR."\{team.getName()}.ini");
		try {
			var teamIniFile = new IniFile(dir);
			var players = teamIniFile.getRegions().stream().filter(r -> r.getName().startsWith("Spieler")).toList();
			for (var region : players) {
				var name = region.getValue("Name");
				var vorname = region.getValue("Vorname");
				var number = Integer.parseInt(region.getName().split(" ")[1]);
				var player = team.getPlayer(number);
				if (!(player.getNachname().equals(name) && player.getVorname().equals(vorname))) {
					var club = region.getValue("Verein");
					LocalDate birthdate;
					try {
						birthdate = LocalDate.parse(region.getValue("Geburtstag"));
					} catch (DateTimeParseException e) {
						birthdate = null;
					}
					var teamName = team.getName();
					var newPlayer = new Player<G>(vorname, name, club, teamName, birthdate);
					var game = player.getGame();
					game.setPlayer(newPlayer);
					team.setPlayer(number - 1, newPlayer);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		// second read from csv for substitutions
		var teamCsv = new CSVFileReader(match.getBaseDir().resolve(STR."\{team.getName()}.csv"));
		var lines = teamCsv.getData();
		var playerNumber = 0;
		for (var playerLine : lines) {
			var sub1Name = playerLine[7];
			var sub1Vorname = playerLine[8];
			var sub2Name = playerLine[12];
			var sub2Vorname = playerLine[13];
			var originalPlayer = team.getPlayer(playerNumber);
			var game = originalPlayer.getGame();
			if (sub1Name.length() < 3 || sub1Vorname.length() < 3) {
				var sub = new Player<G>(sub1Vorname, sub1Name);
				game.setSubstitution1(sub);
			}
			if (sub2Name.length() < 3 || sub2Vorname.length() < 3) {
				var sub = new Player<G>(sub2Vorname, sub2Name);
				game.setSubstitution1(sub);
			}
			playerNumber++;
		}
		loadTeamGames(team);
	}

	private void loadTeamGames(Team<G> team) {
		for (var playerFolder : Objects.requireNonNull(
				match.getBaseDir().resolve(team.getName()).toFile().listFiles())) {
			var path = playerFolder.toPath().resolve("werte.csv");
			var playerName = playerFolder.getName(); // Todo reuse old Reader if not changed
			var game = new GameCSVFileReader<Game120>(path, GameKind.GAME_120).readGame(); // Todo allow other games
			var player = Arrays.stream(team.getPlayers())
							   .filter(p -> p.getCompleteNameWithCommata().equals(playerName))
							   .findFirst().orElseThrow();
			game.setPlayer(player);
		}
	}

}
