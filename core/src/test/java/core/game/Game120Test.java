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
import static org.junit.jupiter.api.Assertions.fail;
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
	void getGameSet() {
	}

	@Test
	void getGameInfo() {
	}

	@Test
	void getGameKind() {
	}

	@Test
	void getNumberOfGameSets() {
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
		var gameSet1 = mock(GameSet.class);
		var gameSet2 = mock(GameSet.class);
		var gameSet3 = mock(GameSet.class);
		var gameSet4 = mock(GameSet.class);
		when(gameSet1.getVolleScore()).thenReturn(50);
		when(gameSet2.getVolleScore()).thenReturn(50);
		when(gameSet3.getVolleScore()).thenReturn(50);
		when(gameSet4.getVolleScore()).thenReturn(50);
		game.setGameSets(List.of(gameSet1, gameSet2, gameSet3, gameSet4));

		assertEquals(200, game.getTotalVolle());
	}

	@Test
	void getTotalAbraeumen() {
	}

	@Test
	void getGameSets() {
		fail();
	}

	@Test
	void setGameSets() {
	}
}