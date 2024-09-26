package core.match;

public class ChangeSchema {

	public static final ChangeSchema ALL_AGAINST = new ChangeSchema();

	private final int numOfBahnen      = 4;
	private final int numOfDurchgaenge = 4;

	private final int[][] changeSchema = {
			{1, 2, 3, 4},
			{2, 1, 4, 3},
			{4, 3, 2, 1},
			{3, 4, 1, 2}
	};


	public int[] getChangeSchema(int durchgang) {
		return changeSchema[durchgang - 1];
	}
}
