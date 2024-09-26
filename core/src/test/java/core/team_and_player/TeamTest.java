package core.team_and_player;

import core.game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("unit")
class TeamTest {

	private Team<Game> team;

	@BeforeEach
	void setUp() {
		team = new Team<Game>("Team", new GeneralTeamInfo(), new Player[4], new Player[4]) {};
	}

	@Test
	void getPlayerIndexOutOfBoundsNegative() {
		assertThrows(IllegalArgumentException.class, () -> team.getPlayer(-1));
	}

	@Test
	void getPlayerIndexInBoundsNegativeText() {
		try {
			team.getPlayer(-1);
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("Index out of bounds: -1", e.getMessage());
		}
	}

	@Test
	void getPlayerIndexOutOfBoundsPositive() {
		assertThrows(IllegalArgumentException.class, () -> team.getPlayer(4));
	}

	@Test
	void getPlayerIndexInBounds() {
		assertNull(team.getPlayer(0));
	}

	@Test
	void getPlayerByNameNotFoundWithoutPlayers() {
		assertThrows(NoPlayerFoundException.class, () -> team.getPlayer("NotExisting"));
	}

	@Test
	void getPlayerByNameNotFoundWithPlayers() {
		Player player = Mockito.mock(Player.class);
		when(player.getVorname()).thenReturn("Max");
		when(player.getNachname()).thenReturn("Mustermann");
		when(player.getCompleteNameWithCommata()).thenReturn("Max,Mustermann");
		team.setPlayer(0, player);
		assertThrows(NoPlayerFoundException.class, () -> team.getPlayer("NotExisting"));
	}

	@Test
	void getPlayerByNameFound() {
		Player player = Mockito.mock(Player.class);
		when(player.getVorname()).thenReturn("Max");
		when(player.getNachname()).thenReturn("Mustermann");
		when(player.getCompleteNameWithCommata()).thenReturn("Max,Mustermann");
		team.setPlayer(0, player);
		assertEquals(player, team.getPlayer("Max,Mustermann"));
	}

	@Test
	void getSubstitute() {
		fail("Not yet implemented concept");
	}

	@Test
	void setPlayer() {
	}

	@Test
	void setSubstitute() {
	}

	@Test
	void substitute() {
	}

	@Test
	void getGeneralTeamInfo() {
	}

	@Test
	void getTeamScore() {
	}

	@Test
	void getTeamTotalMissThrow() {
	}

	@Test
	void getTeamTotalVolle() {
	}

	@Test
	void getTeamTotalAbraeumen() {
	}

	@Test
	void getTeamTotalThrows() {
	}

	@Test
	void getName() {
	}

	@Test
	void setName() {
	}

	@Test
	void getPlayers() {
	}

	@Test
	void setPlayers() {
	}

	@Test
	void getSubstitutes() {
	}

	@Test
	void setSubstitutes() {
	}

	@Test
	void getSubstitutesMap() {
	}

	@Test
	void getNumberOfPlayers() {
	}

	@Test
	void getNumberOfSubstitutes() {
		fail();
	}
}