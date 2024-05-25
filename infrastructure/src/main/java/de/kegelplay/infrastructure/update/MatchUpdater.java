package de.kegelplay.infrastructure.update;


import core.game.Game;
import core.game.Game120;
import core.game.GameKind;
import core.match.Match;
import core.match.MatchStatusInfo;
import core.team_and_player.Player;
import core.team_and_player.Team;
import core.util.KeyValueRegionCollection;
import de.kegelplay.infrastructure.csv.GameCSVFileReader;
import de.kegelplay.infrastructure.ini.IniFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Updates the {@link Match} and the {@link Team}s with all {@link Game}s.
 * <br>
 * Is a Service to update a match in an application to do live updates.
 * The application must have the possibility to read the data from the match.
 * Currently only IniFiles and CSVFiles are supported.
 * <p>
 * A MatchUpdater is responsible for exactly one match.
 * </p>
 *
 * @param <G> the type of the Game that is played in the match
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Match
 * @since 1.0.0
 */
public class MatchUpdater<G extends Game> {

	private static final System.Logger LOGGER = System.getLogger(MatchUpdater.class.getName());
	private final        Match<G>      match;

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
			KeyValueRegionCollection stateIniFile = new IniFile(stateFile);
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
		// TODO read the players again. This is necessary because the players could have
		// changed in the meantime
		// (e.g. a player was substituted)
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
						birthdate = LocalDate.parse(region.getValue("Geb.-Jahr"));
					} catch (DateTimeParseException e) {
						birthdate = null;
					}
					var teamName = team.getName();
					var newPlayer = new Player<G>(vorname, name, club, teamName, birthdate);
					var game = player.getGame();
					game.setPlayer(newPlayer);
					team.setPlayer(number, newPlayer);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		loadTeamGames(team);
	}

	private void loadTeamGames(Team<G> team) {
		for (var playerFolder : Objects.requireNonNull(
				match.getBaseDir().resolve(team.getName()).toFile().listFiles())) {
			var path = playerFolder.toPath().resolve("werte.csv");
			var playerName = playerFolder.getName(); // Todo reuse old Reader if not changed
			var game = new GameCSVFileReader<Game120>(path, GameKind.GAME_120).readGame(); // Todo allow other games
			try {
				var player = Arrays.stream(team.getPlayers())
								   .filter(p -> p.getCompleteNameWithCommata().equals(playerName))
								   .findFirst().orElseThrow();
				game.setPlayer(player);
			} catch (NoSuchElementException ne) {
				// ignore
				LOGGER.log(System.Logger.Level.DEBUG, STR."player (folder) \{playerName} not found in team");
			}
		}
	}

	//region setter/getter
	public Match<G> getMatch() {
		return match;
	}
//endregion

}
