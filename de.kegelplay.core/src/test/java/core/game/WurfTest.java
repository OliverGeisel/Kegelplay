package core.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
class WurfTest {

	private Wurf     wurf;
	@Mock
	private Wurfbild wurfbild;

	@BeforeEach
	void setUp() {
		wurf = new Wurf(0, wurfbild, false, false, false, false);
	}

	@Test
	void constructor_test_0() {
		assertEquals(0, wurf.getScore());
		assertFalse(wurf.foul());
		assertFalse(wurf.redCard());
	}

	@Test
	void constructor_test_10() {
		assertThrows(IllegalArgumentException.class, () -> new Wurf(10, wurfbild, false, false, false, false));
	}

	@Test
	void constructor_test_minus_1() {
		assertThrows(IllegalArgumentException.class, () -> new Wurf(-1, wurfbild, false, false, false, false));
	}

	@Test
	void constructor_test_wurfbild_null() {
		assertThrows(IllegalArgumentException.class, () -> new Wurf(0, null, false, false, false, false));
	}

	@Test
	void getScore() {
		assertEquals(0, wurf.getScore());
	}

	@Test
	void getScore_9() {
		wurf = new Wurf(9, wurfbild, false, false, false, false);
		assertEquals(9, wurf.getScore());
	}

	@Test
	void getScore_9_dontMatch() {
		wurf = new Wurf(9, wurfbild, false, false, false, false);
		fail("Not implemented yet");
		wurfbild = new Wurfbild(2);
		assertThrows(IllegalArgumentException.class, () -> new Wurf(9, wurfbild, false, false, false, false));
	}

	@Test
	void getScore_2_other_wurfbild() {
		var wurfBild = new Wurfbild(501);
		wurf = new Wurf(2, wurfBild, false, false, false, false);
		assertEquals(2, wurf.getScore());
		assertNotEquals(2, wurf.bild().getWert());
	}

	@Test
	void value() {
		assertEquals(0, wurf.value());
	}

	@Test
	void bild() {
		assertEquals(wurfbild, wurf.bild());
	}

	@Test
	void foulFalse() {
		assertFalse(wurf.foul());
	}

	@Test
	void foulTrue() {
		wurf = new Wurf(0, wurfbild, true, false, false, false);
		assertTrue(wurf.foul());
	}

	@Test
	void redCardFalse() {
		assertFalse(wurf.redCard());
	}

	@Test
	void redCardTrue() {
		wurf = new Wurf(0, wurfbild, false, true, false, false);
		assertTrue(wurf.redCard());
	}

	@Test
	void volleFalse() {
		assertFalse(wurf.volle());
	}

	@Test
	void volleTrue() {
		wurf = new Wurf(0, wurfbild, false, false, true, false);
		assertTrue(wurf.volle());
	}

	@Test
	void anschubFalse() {
		assertFalse(wurf.anschub());
	}

	@Test
	void anschubTrue() {
		wurf = new Wurf(0, wurfbild, false, false, false, true);
		assertTrue(wurf.anschub());
	}
}