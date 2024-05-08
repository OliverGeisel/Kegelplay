package de.olivergeisel.kegelplay.core.team_and_player;

import de.olivergeisel.kegelplay.core.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@Tag("unit")
class PlayerTest {

	private Player<Game> player;

	@BeforeEach
	void setUp() {
		player = new Player<>("Vorname", "Nachname", "Club", "Team", LocalDate.of(2024, 1, 1));
	}

	@Test
	void testConstructorCompleteNormal() {
		assertEquals("Vorname", player.getVorname());
		assertEquals("Nachname", player.getNachname());
		assertEquals("Club", player.getClub());
		assertEquals("Team", player.getTeam());
		assertEquals(LocalDate.of(2024, 1, 1), player.getBirthday());
		assertEquals(new PlayerID("", 0), player.getPlayerID());
		assertNull(player.getGame());
	}

	@Test
	void testConstructorMinimalOkay() {
		player = new Player<>("Vorname", "Nachname");
		assertEquals("Vorname", player.getVorname());
		assertEquals("Nachname", player.getNachname());

		assertNull(player.getClub());
		assertNull(player.getTeam());
		assertNull(player.getBirthday());
		assertEquals(new PlayerID("", 0), player.getPlayerID());
		assertNull(player.getGame());
	}


	@Test
	void getVorname() {
	}

	@Test
	void getNachname() {
	}

	@Test
	void getClub() {
	}

	@Test
	void getTeam() {
	}

	@Test
	void getBirthday() {
	}

	@Test
	void getPlayerID() {
	}

	@Test
	void getGame() {
	}

	@Test
	void setGame() {
	}

	@Test
	void getCompleteNameWithUnderscore() {
	}

	@Test
	void getCompleteNameWithCommata() {
	}

	@Test
	void setStateTo() {
	}

	@Test
	void testEquals() {
	}

	@Test
	void testHashCode() {
	}

	@Test
	void testToString() {
	}
}