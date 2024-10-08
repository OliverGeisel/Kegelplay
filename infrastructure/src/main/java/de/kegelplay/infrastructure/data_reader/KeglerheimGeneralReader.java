package de.kegelplay.infrastructure.data_reader;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.game.*;
import core.match.*;
import core.point_system.PointSystem;
import core.point_system._2Teams120PointSystem;
import core.team_and_player.*;
import core.util.KeyValueRegionCollection;
import de.kegelplay.infrastructure.csv.GameCSVFileReader;
import de.kegelplay.infrastructure.ini.IniFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Reader for a {@link Match} for the program used in the Keglerheim.
 *
 * @see Match
 * @see GeneralReader
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class KeglerheimGeneralReader extends GeneralReader {

	public static final  String        TODAY_FORMAT = "dd.MM.yyyy";
	private static final System.Logger LOGGER       = System.getLogger(KeglerheimGeneralReader.class.getName());


	private final Path baseDir; // folder of the match

	/**
	 * Start a new match with the given baseDir.
	 *
	 * @param baseDir    Concrete Match folder
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

	/**
	 * Create a new match based on the given information. The {@link Team}s must be initialized with their players.
	 *
	 * @param config    Match configuration
	 * @param general   General match information
	 * @param stateInfo Match status information
	 * @param teams     List of teams
	 * @param baseDir   Base directory of the match
	 * @return A new match
	 */
	private <G extends Game> Match<G> createMatch(MatchConfig config, GeneralMatchInfo general,
			MatchStatusInfo stateInfo, PointSystem<G> pointSystem, List<Team<G>> teams, Path baseDir) {
		var teamNumber = config.getSchema().getTeams();
		// decide by Teams
		switch (teamNumber) {
			case 1 -> {
				return new Match1Team<>(config, general, stateInfo, pointSystem, teams.getFirst(), baseDir);
			}
			case 2 -> {
				return new Match2Teams<>(config, general, stateInfo, pointSystem, teams.getFirst(), teams.get(1),
						baseDir);
			}
			default -> {
				return new MatchNTeams<>(config, general, stateInfo, pointSystem, teams, baseDir);
			}
		}
	}

	private GameBuilder getGameBuilder(GameKind kind) throws UnsupportedMatchSchema {
		return switch (kind) {
			case GAME_100 -> new Game100Builder();
			case GAME_120 -> new Game120Builder();
			case GAME_40 -> new Game40Builder();
			default -> null;
		};
	}

	/**
	 * Create a team and its players
	 *
	 * @param teamName   Name of the team
	 * @param teamFolder Folder of the team
	 * @return A new team
	 */
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

	@Override
	public <G extends Game> Match<G> initNewMatch() throws UnsupportedMatchSchema {
		var backupDir = baseDir.resolve("Backup-Daten");
		if (!backupDir.toFile().exists()) {
			throw new RuntimeException("backup dir does not exist");
		}
		MatchConfig config;
		GeneralMatchInfo general;
		MatchStatusInfo stateInfo;
		List<String> teamNames;
		PointSystem<G> pointSystem = (PointSystem<G>) new _2Teams120PointSystem();
		try {
			// read state of match
			var status = new KeglerheimSatusReader(baseDir.resolve("status.ini"));
			teamNames = status.getTeamNames();
			KeyValueRegionCollection statusIniFile = new IniFile(baseDir.resolve("status.ini"));
			var backupIniFile = new IniFile(backupDir.resolve("wk_eig_backup.ini"));
			var durchgang = Integer.parseInt(backupIniFile.getRegion("Allgemein").getValue("Durchgang"));
			stateInfo = new MatchStatusInfo(statusIniFile, durchgang);
			// read general match info
			var generalMatchInfoFile = new IniFile(baseDir.resolve("wettkampf.ini"));
			general = new GeneralMatchInfo(generalMatchInfoFile.getRegion("Allgemein"));
		} catch (Exception e) {
			throw new RuntimeException("could not init new match", e);
		}
		var folders =
				Arrays.stream(Objects.requireNonNull(baseDir.toFile().listFiles()))
					  .filter(File::isDirectory)
					  .map(File::getName)
					  .filter(it -> !it.equals("Backup-Daten")).toList();
		var teamCount = folders.size(); // get over folder in basedir
		var playerPerTeamCount = Objects.requireNonNull(
				baseDir.resolve(folders.getFirst()).toFile().listFiles()).length; // get over folder in first team
		try {
			KeyValueRegionCollection schemaFile = new IniFile(baseDir.resolve("wks.ini"));
			var schema = new MatchSchema(schemaFile, teamCount, playerPerTeamCount);
			var kind = GameKind.fromGameInfo(schema.getGameInfoFor(0, 0));
			config = new NormalMatchConfig(schema, kind);
			var laneNames = readCorrectLaneNamed(baseDir);
			config.setLaneNames(laneNames);
		} catch (IOException e) {
			LOGGER.log(System.Logger.Level.ERROR, STR."Could not read schema file for mactch: \{baseDir}");
			throw new RuntimeException("could not init new match", e);
		}
		var gameKind = config.getKind();
		GameBuilder<G> builder = getGameBuilder(gameKind);
		// teams init
		List<Team<G>> teams = new LinkedList<>();
		for (var teamName : teamNames) {
			try {
				var teamFolder = baseDir.resolve(teamName);
				KeyValueRegionCollection teamIni = new IniFile(baseDir.resolve(STR."\{teamName}.ini"));
				var players = new TeamPlayerCreator<>(teamIni).createPlayerOfTeam();
				var teamInfo = new GeneralTeamInfo(teamIni);
				Team<G> team = (Team<G>) getTeam(teamName, teamInfo, players, teamFolder);
				teams.add(team);
			} catch (Exception e) {
				throw new RuntimeException("could not init new match", e);
			}
		}
		// init games
		for (var team : teams) {
			loadTeamGames(team, builder);
		}
		// create match
		return createMatch(config, general, stateInfo, pointSystem, teams, baseDir);
	}

	private List<String> readCorrectLaneNamed(Path baseDir) {
		var back = new LinkedList<String>();
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


	/**
	 * Load the games of a team
	 *
	 * @param team    Team to load the games for
	 * @param builder Game builder to build the games
	 */
	private <G extends Game> void loadTeamGames(Team<G> team, GameBuilder<G> builder) {
		for (var playerFolder : Objects.requireNonNull(baseDir.resolve(team.getName()).toFile().listFiles())) {
			var path = playerFolder.toPath().resolve("werte.csv");
			var playerName = playerFolder.getName();
			var kind = builder.getGameKind();
			var gameSource = new GameCSVFileReader<>(path, kind);
			var game = builder.buildGame(gameSource);
			var player =
					Arrays.stream(team.getPlayers()).filter(p -> p.getCompleteNameWithCommata().equals(playerName))
						  .findFirst().orElseThrow();
			game.setPlayer(player);
		}
	}

	/**
	 * Get a Map of team names and their player names
	 *
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

}
