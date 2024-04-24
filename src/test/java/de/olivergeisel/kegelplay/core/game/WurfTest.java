package de.olivergeisel.kegelplay.core.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class WurfTest {

	private Wurf     wurf;
	@Mock
	private Wurfbild wurfbild;

	@BeforeEach
	void setUp() {
		wurf = new Wurf(0, wurfbild, false, false);
	}

	@Test
	void constructor_test_0() {
		assertEquals(0, wurf.getScore());
		assertFalse(wurf.foul());
		assertFalse(wurf.redCard());
	}

	@Test
	void getScore() {

	}

	@Test
	void value() {
	}

	@Test
	void bild() {
	}

	@Test
	void foul() {
	}

	@Test
	void redCard() {
	}
}