package core.team_and_player;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
class PlayerIDTest {

	@Test
	void testToString() {
		var playerID = new PlayerID("DE", 0);
		assertEquals("DE000000", playerID.toString());
	}

	@Test
	void testToStringWithLeadingZeros() {
		var playerID = new PlayerID("DE", 123);
		assertEquals("DE000123", playerID.toString());
	}

	@Test
	void testNumberTooBig() {
		assertThrows(IllegalArgumentException.class, () -> new PlayerID("DE", 1_000_000));
	}

	@Test
	void testToStringCountryCodeNull() {
		var playerID = new PlayerID(null, 0);
		assertEquals("000000", playerID.toString());
	}
}