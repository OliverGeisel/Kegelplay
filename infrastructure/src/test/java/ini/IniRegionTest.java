package ini;

import de.kegelplay.infrastructure.ini.IniRegion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
class IniRegionTest {

	private IniRegion region;

	@BeforeEach
	void setUp() {
		region = new IniRegion("RegionName", new String[]{"key1=value1", "key2=value"});
	}

	@Test
	void getValue_match() {
		var result = region.getValue("key1");
		assertNotNull(result);
		assertEquals("value1", result);
	}

	@Test
	void getValue_noMatch() {
		var result = region.getValue("key3");
		assertNull(result);
	}

	@Test
	void getValue_null() {
		assertThrows(IllegalArgumentException.class, () -> region.getValue(null));
	}

	@Test
	void containsKey_true_normal() {
		assertTrue(region.containsKey("key1"));
	}

	@Test
	void containsKey_false_normal() {
		assertFalse(region.containsKey("key3"));
	}

	@Test
	void containsKey_null() {
		assertThrows(IllegalArgumentException.class, () -> region.containsKey(null));
	}

	@Test
	void containsValue_true_normal() {
		assertTrue(region.containsValue("value1"));
	}

	@Test
	void containsValue_false_normal() {
		assertFalse(region.containsValue("value3"));
	}

	@Test
	void containsValue_null() {
		assertThrows(IllegalArgumentException.class, () -> region.containsValue(null));
	}

	@Test
	void size_not_empty() {
		assertEquals(2, region.size());
	}

	@Test
	void put() {
		fail();
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