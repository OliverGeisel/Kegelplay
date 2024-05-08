package de.olivergeisel.kegelplay.core.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@Tag("Unit")
class WurfbildTest {

	private Wurfbild wurfbild;


	@BeforeEach
	void setUp() {
		wurfbild = new Wurfbild(0);
	}


	@Test
	void constructor_test_0() {
		assertFalse(wurfbild.get(1));
		assertFalse(wurfbild.get(2));
		assertFalse(wurfbild.get(3));
		assertFalse(wurfbild.get(4));
		assertFalse(wurfbild.get(5));
		assertFalse(wurfbild.get(6));
		assertFalse(wurfbild.get(7));
		assertFalse(wurfbild.get(8));
		assertFalse(wurfbild.get(9));
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 2, 4, 8, 16, 32, 64, 128, 256})
	void constructor_single_pin_test(int value) {
		wurfbild = new Wurfbild(value);
		assertNotEquals(0, wurfbild.getWert());
		int count = 0;
		for (int i = 1; i <= 9; i++) {
			if (wurfbild.get(i)) {
				count++;
			}
		}
		assertEquals(1, count);
	}

	@Test
	void constructor_all_pins_test() {
		wurfbild = new Wurfbild(511);
		for (int i = 1; i <= 9; i++) {
			assertTrue(wurfbild.get(i));
		}
	}

	@ParameterizedTest
	@CsvSource({"false, false, false, false, false, false, false, false, false",
			"true, false, false, false, false, false, false, false, false",
			"false, true, false, false, false, false, false, false, false",
			"false, false, true, false, false, false, false, false, false",
			"false, false, false, true, false, false, false, false, false",
			"false, false, false, false, true, false, false, false, false",
			"false, false, false, false, false, true, false, false, false",
			"false, false, false, false, false, false, true, false, false",
			"false, false, false, false, false, false, false, true, false",
			"false, false, false, false, false, false, false, false, true"})
	void constructor_long_test(boolean one, boolean two, boolean three, boolean four, boolean five, boolean six,
			boolean seven, boolean eight, boolean nine) {
		wurfbild = new Wurfbild(one, two, three, four, five, six, seven, eight, nine);
		for (int i = 1; i <= 9; i++) {
			assertEquals(i == 1 && one
						 || i == 2 && two
						 || i == 3 && three
						 || i == 4 && four
						 || i == 5 && five
						 || i == 6 && six
						 || i == 7 && seven
						 || i == 8 && eight
						 || i == 9 && nine, wurfbild.get(i));
		}
	}

	@Test
	void constructor_array_exception_test() {
		assertThrows(IllegalArgumentException.class, () -> new Wurfbild(new boolean[8]));
	}

	@Test
	void constructor_array_test() {
		boolean[] fields = {true, false, false, false, false, false, false, false, false};
		wurfbild = new Wurfbild(fields);
		for (int i = 1; i <= 9; i++) {
			assertEquals(fields[i - 1], wurfbild.get(i), STR."i = \{i}");
		}
	}

	@Test
	void get_outOfBounds() {
		assertThrows(IllegalArgumentException.class, () -> wurfbild.get(0));
		assertThrows(IllegalArgumentException.class, () -> wurfbild.get(10));
	}

	@ParameterizedTest
	@CsvSource({"1, 1", "2, 2", "3, 4", "4, 8", "5, 16", "6, 32", "7, 64", "8, 128", "9, 256"})
	void get_test(int expected, int value) {
		var wurfbild = new Wurfbild(value);
		for (int i = 1; i <= 9; i++) {
			assertEquals(i == expected, wurfbild.get(i));
		}
	}

	@Test
	void getWert_test_0() {
		assertEquals(0, wurfbild.getWert());
	}

	@ParameterizedTest
	@ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8})
	void getWert_test(int value) {
		wurfbild = new Wurfbild(1 << value);
		assertEquals(1, wurfbild.getWert());
	}

	@Test
	void toString_test_0() {
		assertEquals("0: ---------", wurfbild.toString());
	}

	@ParameterizedTest
	@CsvSource({"1, 1, 1: 1--------", "2, 2, 1: -2-------", "3, 4, 1: --3------", "4, 8, 1: ---4-----",
			"5, 16, 1: ----5----", "6, 32, 1: -----6---", "7, 64, 1: ------7--", "8, 128, 1: -------8-",
			"9, 256, 1: --------9"})
	void toString_test(int index, int imageValue, String expectedString) {
		wurfbild = new Wurfbild(imageValue);
		assertEquals(expectedString, wurfbild.toString());
	}

}