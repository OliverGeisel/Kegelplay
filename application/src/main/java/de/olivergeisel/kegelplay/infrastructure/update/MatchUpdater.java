package de.olivergeisel.kegelplay.infrastructure.update;


import core.game.Game;
import core.game.Game120;
import core.game.GameKind;
import core.match.Match;
import core.match.MatchStatusInfo;
import core.team_and_player.Player;
import core.team_and_player.Team;
import core.util.KeyValueRegionCollection;
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
					// get subs
					var playerFolder = dir.resolve(player.getCompleteNameWithCommata());
					var iniFile = new IniFile(playerFolder.resolve("spieler.ini"));
					var subRegion = iniFile.getRegion("Auswechslung");
					var sub1Index = Integer.parseInt(subRegion.getValue("durch"));
					var sub2Index = Integer.parseInt(subRegion.getValue("durchalt"));
					if (sub1Index != -1) {
						var sub1Player = team.getSubstitute(sub1Index);
						game.setSubstitution1(sub1Player);
					}
					if (sub2Index != -1) {
						var sub2Player = team.getSubstitute(sub1Index);
						game.setSubstitution2(sub2Player);
					}
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
			var player = Arrays.stream(team.getPlayers())
							   .filter(p -> p.getCompleteNameWithCommata().equals(playerName))
							   .findFirst().orElseThrow();
			game.setPlayer(player);
		}
	}

}