package de.olivergeisel.kegelplay.infrastructure.data_reader;

import de.olivergeisel.kegelplay.core.game.*;
import de.olivergeisel.kegelplay.core.match.*;
import de.olivergeisel.kegelplay.core.team_and_player.GeneralTeamInfo;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import de.olivergeisel.kegelplay.core.team_and_player.Team6;
import de.olivergeisel.kegelplay.infrastructure.csv.GameCSVFileReader;
import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class KeglerheimGeneralReader extends GeneralReader {

	public static final String TODAY_FORMAT = "dd.MM.yyyy";
	private static final System.Logger LOGGER = System.getLogger(KeglerheimGeneralReader.class.getName());


	private final Path baseDir; // folder of the match

	/**
	 * Start a new match with the given baseDir.
	 * @param baseDir Concrete Match folder
	 * @param ignoreDate if true, the date check is ignored
	 * @throws IllegalArgumentException if baseDir is null or does not end with today's date
	 */
	public KeglerheimGeneralReader(Path baseDir, boolean ignoreDate) throws IllegalArgumentException {
		if (baseDir == null) {
			throw new IllegalArgumentException("baseDir must not be null");
		}
		if (!ignoreDate) {
			var today = LocalDate.now();
			var todayString = today.format(DateTimeFormatter.ofPattern(TODAY_FORMAT));
			if (!baseDir.getParent().endsWith(todayString)) {
				throw new IllegalArgumentException("baseDir must end with today's date");
			}
		}
		this.baseDir = baseDir;
	}


	@Override
	public Match<Game120> initNewMatch() {
		List<String> teamNames;
		var backupDir = baseDir.resolve("Backup-Daten");
		if (!backupDir.toFile().exists()) {
			throw new RuntimeException("backup dir does not exist");
		}
		MatchConfig config = null;
		GeneralMatchInfo general;
		MatchStatusInfo stateInfo;
		try {
			// read state of match
			var status = new KeglerheimSatusReader(baseDir.resolve("status.ini"));
			teamNames = status.getTeamNames();
			var statusIniFile = new IniFile(baseDir.resolve("status.ini"));
			var backupIniFile = new IniFile(backupDir.resolve("wk_eig_backup.ini"));
			var durchgang = Integer.parseInt(backupIniFile.getRegion("Allgemein").getValue("Durchgang"));
			stateInfo = new MatchStatusInfo(statusIniFile, durchgang);
			// read general match info
			var generalMatchInfoFile = new IniFile(baseDir.resolve("wettkampf.ini"));
			general = new GeneralMatchInfo(generalMatchInfoFile.getRegion("Allgemein"));
		} catch (Exception e) {
			throw new RuntimeException("could not init new match", e);
		}
		// teams
		Team<Game120> home;
		Team<Game120> guest;
		try {
			var homeTeamIni = new IniFile(baseDir.resolve(STR."\{teamNames.get(0)}.ini"));
			var guestTeamIni = new IniFile(baseDir.resolve(STR."\{teamNames.get(1)}.ini"));
			var homePlayers = new TeamPlayerCreator<Game120>(homeTeamIni).createTeam();
			var guestPlayers = new TeamPlayerCreator<Game120>(guestTeamIni).createTeam();
			var homeInfo = new GeneralTeamInfo(homeTeamIni); // todo add to team
			home = new Team6<Game120>(teamNames.get(0), homePlayers.player(), homePlayers.substitute());
			guest = new Team6<Game120>(teamNames.get(1), guestPlayers.player(), guestPlayers.substitute());
			// Todo add checking for wrong team loads

		} catch (Exception e) {
			throw new RuntimeException("could not read team ini files", e);
		}
		// games
		loadTeamGames(home);
		loadTeamGames(guest);
		// read match schema
		try {
			var schemaFile = new IniFile(baseDir.resolve("wks.ini"));
			var schema = new MatchSchema(schemaFile, 2, home.getNumberOfPlayers());
			config = new MatchConfig(schema, GameKind.GAME_120);
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.ERROR, STR."Could not read schema file for mactch: \{baseDir}");
		}
		return new Match2Teams<>(config, general, stateInfo, home, guest, baseDir);
	}

	private void loadTeamGames(Team team) {
		for (var playerFolder : Objects.requireNonNull(baseDir.resolve(team.getName()).toFile().listFiles())) {
			var path = playerFolder.toPath().resolve("werte.csv");
			var playerName = playerFolder.getName();
			var game = new GameCSVFileReader<Game120>(path, GameKind.GAME_120).readGame();
			var player =
					Arrays.stream(team.getPlayers()).filter(p -> p.getCompleteNameWithCommata().equals(playerName))
						  .findFirst().orElseThrow();
			game.setPlayer(player);
		}
	}

	/**
	 * Get a Map of team names and their players
	 * @param teamNames List of team names
	 * @return Map of team names and their players
	 */
	private HashMap<String, List<String>> collectTeams(List<String> teamNames) {
		var teamPlayerMap = new HashMap<String, List<String>>();
		for (String teamName : teamNames) {
			var teamFolder = baseDir.resolve(teamName);
			if (!teamFolder.toFile().exists()) {
				throw new RuntimeException("team folder does not exist");
			}
			var players = new LinkedList<String>();
			for (var playerFolder : Objects.requireNonNull(teamFolder.toFile().listFiles())) {
				players.add(playerFolder.getName());
			}
			teamPlayerMap.put(teamName, players);
		}
		return teamPlayerMap;
	}

	@Override
	public MatchConfig readConfig() {
		return null;
	}


	public void updateMatch(Match match) {
		for (var team : match.getTeams()) {
			updateTeams(team);
		}

	}

	private void updateTeams(Team team) {
		for (var player : team.getPlayers()) {
			updatePlayers(player);
		}
	}

	private void updatePlayers(Player player) {
		updateGames(player.getGame());
	}

	private void updateGames(Game match) {
		for (var durchgang : match.getSets()) {
			updateDurchgang(durchgang);
		}

	}

	private void updateDurchgang(GameSet gameSet) {
		var i = 1;
		var wurf = new Wurf(2, new Wurfbild(12), false, false);
		gameSet.set(i, wurf);
	}

}
