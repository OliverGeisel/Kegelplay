package core.game;

/**
 * Represents a single throw ({@link Wurf}).
 * It can display the concrete fallen pins.
 * if a pin is fallen the value is true.
 * The pins are numbered from 1 to 9.
 *
 * @see Wurf
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class Wurfbild {

	private final boolean one;
	private final boolean two;
	private final boolean three;
	private final boolean four;
	private final boolean five;
	private final boolean six;
	private final boolean seven;
	private final boolean eight;
	private final boolean nine;


	/**
	 * create a new instance by an integer. The value is the encoded version of the pitch.
	 * Each pin is the exponent of the base to in there position -> Pin 5 = 2^5.
	 * @param value integer value of the pitch.
	 */
	public Wurfbild(int value) {
		this.one = (value & 1) != 0;
		this.two = ((value >> 1) & 1) != 0;
		this.three = ((value >> 2) & 1) != 0;
		this.four = ((value >> 3) & 1) != 0;
		this.five = ((value >> 4) & 1) != 0;
		this.six = ((value >> 5) & 1) != 0;
		this.seven = ((value >> 6) & 1) != 0;
		this.eight = ((value >> 7) & 1) != 0;
		this.nine = ((value >> 8) & 1) != 0;
	}

	public Wurfbild(boolean one, boolean two, boolean three, boolean four, boolean five, boolean six, boolean seven,
			boolean eight, boolean nine) {
		super();
		this.one = one;
		this.two = two;
		this.three = three;
		this.four = four;
		this.five = five;
		this.six = six;
		this.seven = seven;
		this.eight = eight;
		this.nine = nine;
	}

	public Wurfbild(boolean[] fields) throws IllegalArgumentException {
		if (fields.length != 9) {
			throw new IllegalArgumentException("Wurfbild hat genau 9 Werte");
		}
		this.one = fields[0];
		this.two = fields[1];
		this.three = fields[2];
		this.four = fields[3];
		this.five = fields[4];
		this.six = fields[5];
		this.seven = fields[6];
		this.eight = fields[7];
		this.nine = fields[8];
	}

	/**
	 * Returns the hit of a specific Pin.
	 * @param i number of the pin
	 * @return true if the pin is fallen
	 * @throws IllegalArgumentException if the position is not in the range of 1-9
	 */
	public boolean get(int i) throws IllegalArgumentException {
		return switch (i) {
			case 1 -> one;
			case 2 -> two;
			case 3 -> three;
			case 4 -> four;
			case 5 -> five;
			case 6 -> six;
			case 7 -> seven;
			case 8 -> eight;
			case 9 -> nine;
			default -> throw new IllegalArgumentException("Wurfbild hat nur 9 Werte");
		};
	}

	//region setter/getter

	/**
	 * Get the number of fallen pins in this Bild. Must not be identical to the score of a {@link Wurf}.
	 * @return number of fallen pins
	 */
	public int getWert() {
		int sum = 0;
		for (int i = 1; i <= 9; i++) {
			if (get(i)) {
				++sum;
			}
		}
		return sum;
	}

	/**
	 * Get the encoded version of the bild.
	 * @return integer value of the bild
	 */
	public int getBildEncoded() {
		int value = 0;
		if (one) value += 1;
		if (two) value += 2;
		if (three) value += 4;
		if (four) value += 8;
		if (five) value += 16;
		if (six) value += 32;
		if (seven) value += 64;
		if (eight) value += 128;
		if (nine) value += 256;
		return value;

	}
//endregion

	public String toString() {
		return STR."\{getWert()}: \{one ? "1" : "-"}\{two ? "2" : "-"}\{three ? "3" : "-"}\{four ? "4" : "-"}\{five ?
				"5" : "-"}\{six ? "6" : "-"}\{seven ? "7" : "-"}\{eight ? "8" : "-"}\{nine ? "9" : "-"}";
	}
}
