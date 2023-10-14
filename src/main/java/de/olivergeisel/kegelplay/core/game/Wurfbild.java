package de.olivergeisel.kegelplay.core.game;

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


	public Wurfbild(int wert) {
		this.one = (wert & 1) != 0;
		this.two = ((wert >> 1) & 1) != 0;
		this.three = ((wert >> 2) & 1) != 0;
		this.four = ((wert >> 3) & 1) != 0;
		this.five = ((wert >> 4) & 1) != 0;
		this.six = ((wert >> 5) & 1) != 0;
		this.seven = ((wert >> 6) & 1) != 0;
		this.eight = ((wert >> 7) & 1) != 0;
		this.nine = ((wert >> 8) & 1) != 0;
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

	public Wurfbild(boolean[] fields) throws IllegalArgumentException{
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
	public int getWert() {
		int sum = 0;
		for (int i = 1; i <= 9; i++) {
			if (get(i)) {
				sum++;
			}
		}
		return sum;
	}
//endregion
}
