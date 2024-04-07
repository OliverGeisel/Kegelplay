package de.olivergeisel.kegelplay.infrastructure.data_reader;

import de.olivergeisel.kegelplay.core.game.Game;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.core.team_and_player.Team;
import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;
import de.olivergeisel.kegelplay.infrastructure.ini.IniRegion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * Create all players and substitutes of a {@link Team} from an IniFile.
 *
 * @param <G> Game type
 */
public class TeamPlayerCreator<G extends Game> {

	private final IniFile teamIni;

	public TeamPlayerCreator(IniFile teamIni) {
		this.teamIni = teamIni;
	}

	public PlayerAndSubstitute<G> createPlayerOfTeam() {
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
		return new PlayerAndSubstitute<G>(players, substitutes);
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

	public record PlayerAndSubstitute<G extends Game>(Player<G>[] player, Player<G>[] substitute) {


		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (!(o instanceof PlayerAndSubstitute<?> that)) return false;

			return Arrays.equals(player, that.player) && Arrays.equals(substitute, that.substitute);
		}

		@Override
		public int hashCode() {
			int result = Arrays.hashCode(player);
			result = 31 * result + Arrays.hashCode(substitute);
			return result;
		}

		@Override
		public String toString() {
			return STR."PlayerAndSubstitute{player=\{Arrays.toString(player)}, substitute=\{Arrays.toString(
					substitute)}\{'}'}";
		}
	}
}
