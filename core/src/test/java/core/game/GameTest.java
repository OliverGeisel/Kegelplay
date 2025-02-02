package core.game;

import core.team_and_player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class GameTest {

	@Mock
	Player<Game> player;
	private Game game;

	@BeforeEach
	void setUp() {
		game = new Game(player, LocalDateTime.of(2024, 1, 1, 12, 0)) {

			private final GameSet[] sets = new GameSet[4];



			@Override
			public void start() {}

			@Override
			public GameSet getGameSet(int gameSetNumber) {
				return sets[gameSetNumber];
			}

			//region setter/getter
			@Override
			public GameInfo getGameInfo() {
				return null;
			}

			@Override
			public GameKind getGameKind() {
				return null;
			}

			@Override
			public int getNumberOfGameSets() {
				return 0;
			}

			@Override
			public int getNumberOfWurf() {
				return 0;
			}

			@Override
			public int getTotalFehlwurf() {
				return 0;
			}

			@Override
			public int getTotalScore() {
				return 0;
			}

			@Override
			public int getTotalVolle() {
				return 0;
			}

			@Override
			public int getTotalAbraeumen() {
				return 0;
			}

			@Override
			public GameSet[] getGameSets() {
				return sets;
			}

			@Override
			public void setGameSets(List<GameSet> gameSets) {
				if (gameSets.size() != 4) {
					throw new IllegalArgumentException("4 Durchgaenge erwartet");
				}
				for (int i = 0; i < gameSets.size(); i++) {
					sets[i] = gameSets.get(i);
				}
			}
//endregion
		};
	}

	@Test
	void subscribe() {
	}

	@Test
	void checkState() {
	}

	@Test
	void start() {
	}

	@Test
	void getGameSet() {
	}

	@Test
	void getCurrentPlayer() {
	}

	@Test
	void getGameInfo() {
	}

	@Test
	void isOn() {
	}

	@Test
	void isFinished() {
	}

	@Test
	void isNotStarted() {
	}

	@Test
	void isPaused() {
	}

	@Test
	void isRunning() {
	}

	@Test
	void getGameKind() {
	}

	@Test
	void getDate() {
	}

	@Test
	void setDate() {
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
	}

	@Test
	void getTotalAbraeumen() {
	}

	@Test
	void getGameSets() {
		fail();
	}

	@Test
	void getPlayer() {
		assertEquals(player, game.getPlayer());
	}

	@Test
	void setPlayer() {
	}

	@Test
	void getLastWurf() {
	}

	@Test
	void getCurrentSet_not_started() {
		var set1 = mock(GameSet.class);
		var set2 = mock(GameSet.class);
		var set3 = mock(GameSet.class);
		var set4 = mock(GameSet.class);
		when(set1.isNotStarted()).thenReturn(true);
		when(set2.isNotStarted()).thenReturn(true);
		when(set3.isNotStarted()).thenReturn(true);
		when(set4.isNotStarted()).thenReturn(true);

		game.setGameSets(List.of(set1, set2, set3, set4));
		assertEquals(set1, game.getCurrentSet());
	}

	@Test
	void getCurrentSet_first_started() {
		var set1 = mock(GameSet.class);
		var set2 = mock(GameSet.class);
		var set3 = mock(GameSet.class);
		var set4 = mock(GameSet.class);
		when(set1.isRunning()).thenReturn(true);

		game.setGameSets(List.of(set1, set2, set3, set4));
		assertEquals(set1, game.getCurrentSet());
	}

	@Test
	void getCurrentSet_last_started() {
		var set1 = mock(GameSet.class);
		var set2 = mock(GameSet.class);
		var set3 = mock(GameSet.class);
		var set4 = mock(GameSet.class);
		when(set1.isCompleted()).thenReturn(true);
		when(set2.isCompleted()).thenReturn(true);
		when(set3.isCompleted()).thenReturn(true);
		when(set4.isRunning()).thenReturn(true);

		game.setGameSets(List.of(set1, set2, set3, set4));
		assertEquals(set4, game.getCurrentSet());
	}

	@Test
	void getCurrentSet_last_completed() {
		var set1 = mock(GameSet.class);
		var set2 = mock(GameSet.class);
		var set3 = mock(GameSet.class);
		var set4 = mock(GameSet.class);
		when(set1.isCompleted()).thenReturn(true);
		when(set2.isCompleted()).thenReturn(true);
		when(set3.isCompleted()).thenReturn(true);
		when(set4.isCompleted()).thenReturn(true);

		game.setGameSets(List.of(set1, set2, set3, set4));
		assertEquals(set4, game.getCurrentSet());
	}

	@Test
	void getCurrentSet_second_while_First_still_running() {
		var set1 = mock(GameSet.class);
		var set2 = mock(GameSet.class);
		var set3 = mock(GameSet.class);
		var set4 = mock(GameSet.class);
		when(set2.isRunning()).thenReturn(true);


		game.setGameSets(List.of(set1, set2, set3, set4));
		assertEquals(set2, game.getCurrentSet());
	}


	@Test
	void setSubstitution1() {
	}

	@Test
	void setSubstitution2() {
	}

	@Test
	void setGameSets() {
	}

	@Test
	void testEquals() {
	}

	@Test
	void testHashCode() {
		fail();
	}
}