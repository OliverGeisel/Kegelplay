package core.game;

import core.team_and_player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class Game120Test {

	@Mock
	Player<Game120> player;
	private Game120 game;

	@BeforeEach
	void setUp() {
		game = new Game120(player);
	}

	@Test
	void getDurchgang() {
	}

	@Test
	void getGameInfo() {
	}

	@Test
	void getGameKind() {
	}

	@Test
	void getNumberOfDurchgaenge() {
	}

	@Test
	void getNumberOfWurf() {
	}

	@Test
	void getTotalFehlwurf() {
	}

	@Test
	void getTotalScore() {
	}

	@Test
	void getTotalVolle() {
		var durchgang1 = mock(GameSet.class);
		var durchgang2 = mock(GameSet.class);
		var durchgang3 = mock(GameSet.class);
		var durchgang4 = mock(GameSet.class);
		when(durchgang1.getVolleScore()).thenReturn(50);
		when(durchgang2.getVolleScore()).thenReturn(50);
		when(durchgang3.getVolleScore()).thenReturn(50);
		when(durchgang4.getVolleScore()).thenReturn(50);
		game.setDurchgaenge(List.of(durchgang1, durchgang2, durchgang3, durchgang4));

		assertEquals(200, game.getTotalVolle());
	}

	@Test
	void getTotalAbraeumen() {
	}

	@Test
	void getSets() {
	}

	@Test
	void setDurchgaenge() {
	}
}