package de.olivergeisel.kegelplay.infrastructure.data_reader;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;
import de.olivergeisel.kegelplay.infrastructure.ini.IniRegion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TeamPlayerCreator<G extends Game> {

	private final IniFile teamIni;

	public TeamPlayerCreator(IniFile teamIni) {
		this.teamIni = teamIni;
	}

	public PlayerAndSubstitute createTeam() {
		var playersRaw =
				teamIni.getRegions().stream().filter(region -> region.getName().startsWith("Spieler ")).toList();
		var substitutesRaw =
				teamIni.getRegions().stream().filter(region -> region.getName().startsWith("Ersatzspieler ")).toList();

		var playersCount = playersRaw.size();
		var substitutesCount = substitutesRaw.size();
		var players = new Player[playersCount];
		var substitutes = new Player[substitutesCount];
		for (int i = 0; i < playersCount; i++) {
			players[i] = createPlayer(playersRaw.get(i));
		}
		for (int i = 0; i < substitutesCount; i++) {
			substitutes[i] = createPlayer(substitutesRaw.get(i));
		}
		return new PlayerAndSubstitute(players, substitutes);
	}

	private Player<G> createPlayerRedu(IniRegion region) {
		var name = region.getValue("Name");
		var firstName = region.getValue("Vorname");
		return new Player(firstName, name);
	}

	private Player<G> createPlayer(IniRegion region) {
		var name = region.getValue("Name");
		var firstName = region.getValue("Vorname");
		var passNumber = region.getValue("Pass-Nr.");
		var birthDate = region.getValue("Geb.-Jahr");
		LocalDate birthDateLocal;
		try {
			birthDateLocal = LocalDate.parse(birthDate, DateTimeFormatter.ofPattern("MM/yyyy"));
		} catch (Exception e) {
			birthDateLocal = LocalDate.of(1970, 1, 1);
		}
		var club = region.getValue("Verein");
		return new Player<>(firstName, name, club, "", birthDateLocal);
	}

	public record PlayerAndSubstitute(Player[] player, Player[] substitute) {

	}
}
