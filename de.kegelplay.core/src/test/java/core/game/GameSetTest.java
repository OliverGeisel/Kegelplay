package core.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Tag("unit")
class GameSetTest {

	private GameSet gameSet;

	@BeforeEach
	void setUp() {
		gameSet = new GameSet(2, 1, 1, 1);
	}

	@Test
	void constructor_normal_okay() {
		assertEquals(2, gameSet.getThrowCount());
		assertEquals(0, gameSet.getVolleScore());
		assertEquals(0, gameSet.getAbraeumenScore());
		assertEquals(1, gameSet.getGameSetNumber());
		assertEquals(0, gameSet.getTime());
		assertEquals(0, gameSet.getScore());
		assertEquals(SetState.NOT_STARTED, gameSet.getState());
		assertEquals(0, gameSet.getAnzahlGespielteWuerfe());
		assertEquals(0, gameSet.getAnzahlFehler());
		assertEquals(1, gameSet.getAnzahlVolle());
	}

	@Test
	void constructor_normal_totalThrow_less_one() {
		assertThrows(IllegalArgumentException.class, () -> new GameSet(0, 0, 0, 1),
				"No GameSet can be created with 0 throws");
	}

	@Test
	void constructor_normal_totalThrow_mismatch() {
		assertThrows(IllegalArgumentException.class, () -> new GameSet(2, 0, 0, 1),
				"Total throws and game set number do not match");
	}


	@Test
	void set_first() {
		var wurf = Mockito.mock(Wurf.class);
		assertNull(gameSet.get(0));
		assertEquals(SetState.NOT_STARTED, gameSet.getState());

		gameSet.set(0, wurf);

		assertEquals(wurf, gameSet.get(0));
		assertEquals(SetState.IN_PROGRESS, gameSet.getState());
	}

	@Test
	void set_all_full() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(9);
		assertNull(gameSet.get(0));
		assertEquals(SetState.NOT_STARTED, gameSet.getState());

		gameSet.set(0, wurf);
		assertEquals(SetState.IN_PROGRESS, gameSet.getState());
		gameSet.set(1, wurf);
		assertEquals(SetState.FINISHED, gameSet.getState());
		assertEquals(18, gameSet.getScore());
	}

	@Test
	void set_with_time() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(9);
		assertNull(gameSet.get(0));
		assertEquals(SetState.NOT_STARTED, gameSet.getState());

		gameSet.set(0, wurf, 10);
		assertEquals(SetState.IN_PROGRESS, gameSet.getState());
		assertEquals(10, gameSet.getTime());
	}

	@Test
	void set_with_time_zero() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(9);
		assertNull(gameSet.get(0));
		assertEquals(SetState.NOT_STARTED, gameSet.getState());

		gameSet.set(0, wurf, 0);
		assertEquals(SetState.FINISHED, gameSet.getState());
		assertEquals(0, gameSet.getTime());
	}

	@Test
	void reset_out_of_bounds_negative() {
		assertThrows(IllegalArgumentException.class, () -> gameSet.reset(-1));
	}

	@Test
	void reset_out_of_bounds_positive() {
		assertThrows(IllegalArgumentException.class, () -> gameSet.reset(2));
	}

	@Test
	void reset_no_throws() {
		assertThrows(IllegalStateException.class, () -> gameSet.reset(0));
	}

	@Test
	void reset_okay_no_time_left() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf, 0);
		gameSet.reset(0);
		assertNull(gameSet.get(0));
		assertEquals(SetState.NOT_STARTED, gameSet.getState());
	}

	@Test
	void reset_okay_no_time_left_and_throws() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf, 0);
		gameSet.set(1, wurf);
		gameSet.reset(0);
		assertNull(gameSet.get(0));
		assertEquals(SetState.FINISHED, gameSet.getState());
	}

	@Test
	void reset_okay_with_time_left_and_no_throws() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf, 10);
		gameSet.reset(0);
		assertNull(gameSet.get(0));
		assertEquals(SetState.NOT_STARTED, gameSet.getState());
	}

	@Test
	void reset_okay_with_time_left_and_throws() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf, 10);
		gameSet.set(1, wurf);
		gameSet.reset(0);
		assertNull(gameSet.get(0));
		assertEquals(SetState.IN_PROGRESS, gameSet.getState());
	}

	@Test
	void get_okay() {
		assertNull(gameSet.get(0));
	}

	@Test
	void get_okay_after_set() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf);
		assertNotNull(gameSet.get(0));
	}

	@Test
	void get_out_of_bounds_negative() {
		assertThrows(IllegalArgumentException.class, () -> gameSet.get(-1));
	}

	@Test
	void get_out_of_bounds_positive() {
		assertThrows(IllegalArgumentException.class, () -> gameSet.get(2));
	}

	@Test
	void getGameSetNumber() {
		assertEquals(1, gameSet.getGameSetNumber());
	}

	@Test
	void getTime() {
		assertEquals(0, gameSet.getTime());
	}

	@Test
	void setTime_no_throws_yet() {
		assertEquals(0, gameSet.getTime());
		assertEquals(SetState.NOT_STARTED, gameSet.getState());

		gameSet.setTime(10);

		assertEquals(10, gameSet.getTime());
		assertEquals(SetState.NOT_STARTED, gameSet.getState());
	}

	@Test
	void setTime_with_throws() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf);
		assertEquals(0, gameSet.getTime());
		assertEquals(SetState.IN_PROGRESS, gameSet.getState());

		gameSet.setTime(10);

		assertEquals(10, gameSet.getTime());
		assertEquals(SetState.IN_PROGRESS, gameSet.getState());
	}

	@Test
	void setTime_with_all_throws() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf);
		assertEquals(0, gameSet.getTime());
		gameSet.set(1, wurf);
		assertEquals(SetState.FINISHED, gameSet.getState());

		gameSet.setTime(10);

		assertEquals(10, gameSet.getTime());
		assertEquals(SetState.FINISHED, gameSet.getState());
	}


	@Test
	void getScore_at_start() {
		assertEquals(0, gameSet.getScore());
	}

	@Test
	void getScore_after_first_throw() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(9);
		gameSet.set(0, wurf);
		assertEquals(9, gameSet.getScore());
	}

	@Test
	void getScore_after_remove() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(9);
		gameSet.set(0, wurf);
		assertEquals(9, gameSet.getScore());
		gameSet.set(0, null);
		assertEquals(0, gameSet.getScore());
	}

	@Test
	void getState_at_start() {
		assertEquals(SetState.NOT_STARTED, gameSet.getState());
	}

	@Test
	void getState_after_first_throw() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf);
		assertEquals(SetState.IN_PROGRESS, gameSet.getState());
	}

	@Test
	void getState_after_all_throws() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf);
		gameSet.set(1, wurf);
		assertEquals(SetState.FINISHED, gameSet.getState());
	}

	@Test
	void getLastWurf_at_start() {
		assertNull(gameSet.getLastWurf());
	}

	@Test
	void getLastWurf_after_first_throw() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf);
		assertEquals(wurf, gameSet.getLastWurf());
	}

	@Test
	void getLastWurf_after_all_throws() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf);
		gameSet.set(1, wurf);
		assertEquals(wurf, gameSet.getLastWurf());
	}

	@Test
	void getLastWurf_after_all_throws_and_reset() {
		var wurf1 = Mockito.mock(Wurf.class);
		var wurf2 = Mockito.mock(Wurf.class);
		when(wurf1.getScore()).thenReturn(9);
		when(wurf2.getScore()).thenReturn(8);
		gameSet.set(0, wurf1);
		gameSet.set(1, wurf2);
		gameSet.reset(1);
		assertEquals(9, gameSet.getLastWurf().getScore());
	}

	@Test
	void isCompleted_true() {
		gameSet.set(0, Mockito.mock(Wurf.class));
		gameSet.set(1, Mockito.mock(Wurf.class));
		assertTrue(gameSet.isCompleted());
	}

	@Test
	void isCompleted_false_at_start() {
		assertFalse(gameSet.isCompleted());
	}

	@Test
	void isCompleted_false_after_one_throw() {
		assertFalse(gameSet.isCompleted());

		gameSet.set(0, Mockito.mock(Wurf.class));
		assertFalse(gameSet.isCompleted());
	}

	@Test
	void isNotStarted_true() {
		assertTrue(gameSet.isNotStarted());
	}

	@Test
	void isNotStarted_false_after_one_throw() {
		assertTrue(gameSet.isNotStarted());

		gameSet.set(0, Mockito.mock(Wurf.class));
		assertFalse(gameSet.isNotStarted());
	}

	@Test
	void isRunning_false_at_start() {
		assertFalse(gameSet.isRunning());
	}

	@Test
	void isRunning_true_after_one_throw() {
		assertFalse(gameSet.isRunning());

		gameSet.set(0, Mockito.mock(Wurf.class));
		assertTrue(gameSet.isRunning());
	}

	@Test
	void getVolleScore_at_start() {
		assertEquals(0, gameSet.getVolleScore());
	}

	@Test
	void getVolleScore_after_first_throw() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(9);
		gameSet.set(0, wurf);
		assertEquals(9, gameSet.getVolleScore());
	}

	@Test
	void getVolleScore_after_overriding_first_throw() {
		var wurf1 = Mockito.mock(Wurf.class);
		when(wurf1.getScore()).thenReturn(9);
		gameSet.set(0, wurf1);
		assertEquals(9, gameSet.getVolleScore());
		var wurf2 = Mockito.mock(Wurf.class);
		when(wurf2.getScore()).thenReturn(8);
		when(wurf2.getScore()).thenReturn(8);

		gameSet.set(0, wurf2);

		assertEquals(8, gameSet.getVolleScore());
	}

	@Test
	void getAnzahlGespielteWuerfe_at_start() {
		assertEquals(0, gameSet.getAnzahlGespielteWuerfe());
	}

	@Test
	void getAnzahlGespielteWuerfe_after_first_throw() {
		var wurf = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf);

		assertEquals(1, gameSet.getAnzahlGespielteWuerfe());
	}

	@Test
	void getAnzahlGespielteWuerfe_after_reset_first_throw() {
		var wurf1 = Mockito.mock(Wurf.class);
		gameSet.set(0, wurf1);

		assertEquals(1, gameSet.getAnzahlGespielteWuerfe());
		gameSet.reset(0);
		assertEquals(0, gameSet.getAnzahlGespielteWuerfe());
	}

	@Test
	void getAbraeumenScore_at_start() {
		assertEquals(0, gameSet.getAbraeumenScore());
	}

	@Test
	void getAbraeumenScore_after_first_throw_in_volle() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(9);

		gameSet.set(0, wurf);

		assertEquals(0, gameSet.getAbraeumenScore());
	}

	@Test
	void getAbraeumenScore_after_first_throw_in_abraeumen() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(8);

		gameSet.set(1, wurf);

		assertEquals(8, gameSet.getAbraeumenScore());
		assertEquals(0, gameSet.getVolleScore());
	}

	@Test
	void getAnzahlFehler_at_start() {
		assertEquals(0, gameSet.getAnzahlFehler());
	}

	@Test
	void getAnzahlFehler_after_first_throw() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(0);

		gameSet.set(0, wurf);

		assertEquals(1, gameSet.getAnzahlFehler());
	}

	@Test
	void getAnzahlFehler_after_first_throw_replaced() {
		var wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(0);
		gameSet.set(0, wurf);
		assertEquals(1, gameSet.getAnzahlFehler());

		wurf = Mockito.mock(Wurf.class);
		when(wurf.getScore()).thenReturn(9);
		gameSet.set(0, wurf);

		assertEquals(0, gameSet.getAnzahlFehler());
	}

	@Test
	void getThrowCount() {
		assertEquals(2, gameSet.getThrowCount());
	}

	@Test
	void equals_test() {
		fail();
	}

	@Test
	void hashCode_test() {
		fail();
	}


}