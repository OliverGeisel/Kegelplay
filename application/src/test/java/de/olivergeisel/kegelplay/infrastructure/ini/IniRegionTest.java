package de.olivergeisel.kegelplay.infrastructure.ini;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class IniRegionTest {

	private IniRegion region;

	@BeforeEach
	void setUp() {
		region = new IniRegion("RegionName", new String[]{"key1=value1", "key2=value"});
	}

	@Test
	void getValue() {
	}

	@Test
	void containsKey() {
	}

	@Test
	void containsValue() {
	}

	@Test
	void size() {
	}

	@Test
	void put() {
	}

	@Test
	void remove() {
	}

	@Test
	void getKeyValuePairs() {
	}

	@Test
	void getKeys() {
	}

	@Test
	void isEmpty() {
	}

	@Test
	void getName() {
	}

	@Test
	void getLines() {
	}

	@Test
	void setLines() {
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