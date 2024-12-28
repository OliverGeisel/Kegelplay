package de.kegelplay.infrastructure.update;

import core.game.Game;
import core.game.GameKind;
import core.match.AltenateGameKindMatchConfig;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class SpecialMatchUpdater extends MatchUpdater {

	private static final System.Logger LOGGER = System.getLogger(MatchUpdater.class.getName());

	public SpecialMatchUpdater(Match match) {
		super(match);
	}

	@Override
	public void updateMatch() {
		var match = getMatch();
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
		for (var obj : match.getGames()) {
			var game = (Game) obj;
			game.checkState();
		}
	}

	private void updateTeam(Team team) {
		var dir = getMatch().getBaseDir().resolve(STR."\{team.getName()}.ini");
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
					var newPlayer = new Player(vorname, name, club, teamName, birthdate);
					var game = player.getGame();
					game.setPlayer(newPlayer);
					team.setPlayer(number, newPlayer);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		var gameKinds = ((AltenateGameKindMatchConfig) getMatch().getConfig()).getGameKinds();
		loadTeamGames(team, gameKinds);

	}

	private void loadTeamGames(Team team, List<GameKind> gameKinds) {
		var files = Objects.requireNonNull(
				getMatch().getBaseDir().resolve(team.getName()).toFile().listFiles());
		for (var playerFolder : files) {
			var path = playerFolder.toPath().resolve("werte.csv");
			var playerName = playerFolder.getName(); // Todo reuse old Reader if not changed
			var playerPos = 0;
			var i = 0;
			for (var p : team.getPlayers()) {
				if (p.getCompleteNameWithCommata().equals(playerName)) {
					playerPos = i;
					break;
				}
				i++;
			}
			var gameKind = gameKinds.get(playerPos % gameKinds.size());
			var game = new GameCSVFileReader(path, gameKind).readGame();
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
}
