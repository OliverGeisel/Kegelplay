package de.olivergeisel.kegelplay.core.game;

import de.olivergeisel.kegelplay.core.team_and_player.Player;
import javafx.beans.InvalidationListener;
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

@Tag("unit")
@ExtendWith(MockitoExtension.class)
class GameTest {

	@Mock
	Player<Game> player;
	private Game game;

	@BeforeEach
	void setUp() {
		game = new Game(player, LocalDateTime.of(2024, 1, 1, 12, 0)) {
			@Override
			public void addListener(InvalidationListener listener) {}

			@Override
			public void removeListener(InvalidationListener listener) {}

			@Override
			public void start() {}

			@Override
			public GameSet getDurchgang(int durchgang) {
				return null;
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
			public int getNumberOfDurchgaenge() {
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
			public GameSet[] getSets() {
				return new GameSet[0];
			}

			@Override
			public void setDurchgaenge(List<GameSet> durgaenge) {

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
	void getDurchgang() {
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
	}

	@Test
	void getTotalAbraeumen() {
	}

	@Test
	void getSets() {
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
	void getCurrentSet() {
	}

	@Test
	void setSubstitution1() {
	}

	@Test
	void setSubstitution2() {
	}

	@Test
	void setDurchgaenge() {
	}

	@Test
	void testEquals() {
	}

	@Test
	void testHashCode() {
		fail();
	}
}