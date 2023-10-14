package de.olivergeisel.kegelplay.infrastructure.data_reader;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.match.MatchConfig;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class KeglerheimGeneralReader extends GeneralReader {

	public static final String TODAY_FORMAT = "dd.MM.yyyy";
	private             Path   baseDir;

	public KeglerheimGeneralReader(Path baseDir, boolean ignoreDate) {
		if (baseDir == null) {
			throw new IllegalArgumentException("baseDir must not be null");
		}
		if (ignoreDate) {
			var today = java.time.LocalDate.now();
			var todayString = today.format(DateTimeFormatter.ofPattern(TODAY_FORMAT));
			if (!baseDir.getParent().endsWith(todayString)) {
				throw new IllegalArgumentException("baseDir must end with today's date");
			}
		}
		this.baseDir = baseDir;
	}


	@Override
	public Match initNewMatch() {
		int countTeams;
		List<String> teamNames;
		String backupDir = "backup";
		if (!baseDir.resolve(backupDir).toFile().exists()) {
			throw new RuntimeException("backup dir does not exist");
		}
		var teamFolderCount = Objects.requireNonNull(baseDir.toFile().listFiles(File::isDirectory)).length - 1;

		try {
			var status = new KeglerheimSatusReader(baseDir.resolve("status.ini"));
			countTeams = status.getTeamCount();
			teamNames = status.getTeamNames();
			var teamPlayerMap = collectTeams(teamNames);
			if (teamPlayerMap.size() != countTeams ||
				teamPlayerMap.values().stream().mapToInt(List::size).sum() != countTeams * 4) {
				throw new RuntimeException("team count in status.ini and team folders do not match");
			}
			var playerCount = teamPlayerMap.values().stream().findFirst()
										   .orElseThrow(() -> new RuntimeException("no teams found"))
										   .size();

		} catch (Exception e) {
			throw new RuntimeException("could not init new match", e);
		}
		// config
		try {
			var schema = new IniFile(baseDir.resolve("wks.ini"));
			var allgemein = schema.getRegion("Allgemein");
			var bahnCount = Integer.parseInt(allgemein.getValue("Anzahl Bahnen"));

			var teamsWks = Integer.parseInt(allgemein.getValue("Art")) + 1;
			if (teamsWks != countTeams || teamFolderCount != countTeams) {
				throw new RuntimeException("team count in status.ini and wks.ini do not match");
			}
			var playersPerTeam = Integer.parseInt(allgemein.getValue("Anzahl"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// teams
		return null;
	}

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

	}


}
