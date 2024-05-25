package ini;

import de.kegelplay.infrastructure.ini.IniFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

@Tag("unit")
class IniFileTest {

	private IniFile iniFile;

	@BeforeEach
	void setUp() throws IOException {
		iniFile = new IniFile("src/test/java/resources/TestFile.ini");
	}

	@Test
	void getRegion_string_normal() {
		iniFile.getRegion("Heading");
	}

	@Test
	void getRegion_int_normal() {
		iniFile.getRegion(0);
	}

	@Test
	void getName() {
		fail();
	}

	@Test
	void getRegions() {
		fail();
	}

	@Test
	void getRegionCount() {
		fail("Not test yet");
	}
}