package de.kegelplay.infrastructure.data_reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.game.*;
import core.match.*;
import core.point_system.PointSystem;
import core.team_and_player.*;
import core.util.KeyValueRegionCollection;
import de.kegelplay.infrastructure.csv.GameCSVFileReader;
import de.kegelplay.infrastructure.ini.IniFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class AlternatingGameKindGeneralReader {
	private static final System.Logger LOGGER = System.getLogger(AlternatingGameKindGeneralReader.class.getName());

	private final Path baseDir;


	public AlternatingGameKindGeneralReader(Path baseDir) {
		this.baseDir = baseDir;
	}

	private Match createMatch(MatchConfig config, GeneralMatchInfo general,
			MatchStatusInfo stateInfo, PointSystem pointSystem, List<Team> teams, Path baseDir) {
		var teamNumber = config.getSchema().getTeams();
		// decide by Teams
		switch (teamNumber) {
			case 1 -> {
				return new MatchAlternatingKind(config, general, stateInfo, pointSystem, teams.getFirst(), baseDir);
			}
			default -> {
				throw new UnsupportedOperationException("Not implemented yet");
			}
		}
	}

	public Match initNewMatch() {
		var backupDir = baseDir.resolve("Backup-Daten");
		if (!backupDir.toFile().exists()) {
			LOGGER.log(System.Logger.Level.WARNING, STR."Backup dir does not exist: \{backupDir}");
		}
		AltenateGameKindMatchConfig config;
		GeneralMatchInfo general;
		MatchStatusInfo stateInfo;
		List<String> teamNames;
		PointSystem pointSystem = null;
		try {
			// read state of match
			var status = new KeglerheimSatusReader(baseDir.resolve("status.ini"));
			teamNames = status.getTeamNames();
			KeyValueRegionCollection statusIniFile =
					new IniFile(baseDir.resolve("status.ini"));
			int durchgang;
			if (!backupDir.toFile().exists()) {
				durchgang = Integer.parseInt(statusIniFile.getRegion("Anzahl").getValue("Durchgang").toString());
			} else {
				var backupIniFile = new IniFile(backupDir.resolve("wk_eig_backup.ini"));
				durchgang = Integer.parseInt(backupIniFile.getRegion("Allgemein").getValue("Durchgang"));
			}
			stateInfo = new MatchStatusInfo(statusIniFile, durchgang);
			// read general match info
			var generalMatchInfoFile = new IniFile(baseDir.resolve("wettkampf.ini"));
			general = new GeneralMatchInfo(generalMatchInfoFile.getRegion("Allgemein"));
		} catch (Exception e) {
			throw new RuntimeException("could not init new match", e);
		}
		var teamFolders =
				Arrays.stream(Objects.requireNonNull(baseDir.toFile().listFiles()))
					  .filter(File::isDirectory)
					  .map(File::getName)
					  .filter(it -> !it.equals("Backup-Daten")).toList();
		var teamCount = teamFolders.size(); // get over folder in basedir
		var playerPerTeamCount = Objects.requireNonNull(
				baseDir.resolve(teamFolders.getFirst()).toFile().listFiles()).length; // get over folder in first team
		try {
			KeyValueRegionCollection schemaFile = new IniFile(baseDir.resolve("wks.ini"));
			var schema = new MatchSchema(schemaFile, teamCount, playerPerTeamCount);
			var gamekinds = getGameKindsOrderedListed(schema);
			config = new AltenateGameKindMatchConfig(schema, gamekinds);
			var laneNames = readCorrectLaneNamed(baseDir);
			config.setLaneNames(laneNames);
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.ERROR, STR."Could not read schema file for match: \{baseDir}");
			throw new RuntimeException("could not init new match", e);
		}
		// teams init
		List<Team> teams = new LinkedList<>();
		for (var teamName : teamNames) {
			try {
				var teamFolder = baseDir.resolve(teamName);
				KeyValueRegionCollection teamIni = new IniFile(baseDir.resolve(STR."\{teamName}.ini"));
				var players = new TeamPlayerCreator<>(teamIni).createPlayerOfTeam();
				var teamInfo = new GeneralTeamInfo(teamIni);
				Team team = getTeam(teamName, teamInfo, players, teamFolder);
				teams.add(team);
			} catch (Exception e) {
				throw new RuntimeException("could not init new match", e);
			}
		}
		// init games
		for (var team : teams) {
			loadTeamGames(team, config.getGameKinds());
		}
		// create match
		return createMatch(config, general, stateInfo, pointSystem, teams, baseDir);
	}

	private void loadTeamGames(Team team, List<GameKind> gameKinds) {
		int i = 0;
		for (var playerFolder : Objects.requireNonNull(baseDir.resolve(team.getName()).toFile().listFiles())) {
			var kind = gameKinds.get(i);
			GameBuilder builder = getGameBuilder(kind);
			var path = playerFolder.toPath().resolve("werte.csv");
			var playerName = playerFolder.getName();
			var gameSource = new GameCSVFileReader<>(path, kind);
			switch (gameSource.getData().size()) {
				case 120 -> builder = new Game120Builder();
				case 200 -> builder = new Game200Builder();
				case 100 -> builder = new Game100Builder();
			}
			Game game;
			try {
				game = builder.buildGame(gameSource);
			} catch (IllegalArgumentException ie) {
				throw new IllegalArgumentException("Game not buildable for dir:" + playerFolder);
			}
			if (game == null) {
				continue;
			}
			var player =
					Arrays.stream(team.getPlayers()).filter(p -> p.getCompleteNameWithCommata().equals(playerName))
						  .findFirst().orElseThrow();
			game.setPlayer(player);
		}
	}

	private List<GameKind> getGameKindsOrderedListed(MatchSchema schema) {
		var gameKinds = new LinkedList<GameKind>();
		for (int i = 0; i < schema.getTeams(); ++i) {
			for (int player = 0; player < schema.getPlayersPerCycle(); ++player) {
				var gameKind = GameKind.fromGameInfo(schema.getGameInfoFor(i, player));
				gameKinds.add(gameKind);
			}
		}
		return gameKinds;
	}

	public MatchConfig readConfig() {
		return null;
	}

	private List<String> readCorrectLaneNamed(Path baseDir) {
		var back = new LinkedList<String>();
		if (!baseDir.resolve("Backup-Daten").toFile().exists()) {
			LOGGER.log(System.Logger.Level.INFO, STR."Backup dir does not exist! Lanes have Default Names");
			int numberOfLanes = 4;
			oldVersionLaneNames(back, baseDir);
			return back;
		}
		try {
			var iniFile = new IniFile(baseDir.resolve("Backup-Daten/start.ini"));
			var bahnRegion = iniFile.getRegion("Bahnen");
			var temp = new LinkedList<Integer>();
			for (var bahn : bahnRegion.getKeys()) {
				if (bahnRegion.getValue(bahn).equals("1")) {
					temp.add(Integer.parseInt(bahn.split(" ")[1]));
				}
			}
			temp.sort(Comparator.naturalOrder());
			ObjectMapper mapper = new ObjectMapper();
			var json = mapper.readTree(Path.of("configs/settings.json").toFile());
			var lanes = json.get("lanes");
			for (var index : temp) {
				back.add(lanes.get(index).asText());
			}
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.ERROR, STR."Could not read backup for match: \{baseDir}");
		}
		return back;
	}

	private GameBuilder getGameBuilder(GameKind kind) {
		return switch (kind) {
			case GAME_200 -> new Game200Builder();
			case GAME_100 -> new Game100Builder();
			case GAME_120 -> new Game120Builder();
			case GAME_40 -> new Game40Builder();
			default -> null;
		};
	}

	private <G extends Game> Team<G> getTeam(String teamName, GeneralTeamInfo info,
			TeamPlayerCreator.PlayerAndSubstitute<G> playerAndSubstitute, Path teamFolder) {
		if (playerAndSubstitute == null) {
			throw new RuntimeException("playerAndSubstitute must not be null");
		}
		if (playerAndSubstitute.player().length == 6) {
			new Team6<>(teamName, info, playerAndSubstitute.player(), playerAndSubstitute.substitute());
		}
		if (playerAndSubstitute.player().length == 4) {
			return new Team4<>(teamName, info, playerAndSubstitute.player(), playerAndSubstitute.substitute());
		}
		return new TeamN<>(teamName, info, playerAndSubstitute.player(), playerAndSubstitute.substitute());
	}

	private void oldVersionLaneNames(List<String> laneNames, Path baseDir) {
		try {
			var wks = new IniFile(baseDir.resolve("wks.ini"));
			var region = wks.getRegion("Allgemein");
			var number = Integer.parseInt(region.getValue("Anzahl Bahnen"));
			for (int i = 1; i <= number; i++) {
				laneNames.add(STR."Bahn \{i}");
			}
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.ERROR, STR."Could not read wks.ini for match: \{baseDir}");
		}
	}


}
