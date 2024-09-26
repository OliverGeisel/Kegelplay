package core.game;

import core.match.Match;
import core.team_and_player.Player;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * A Game120 is a game with 120 throws. This is the international standard for a game in classic 9pin bowling.
 * The game is played in 4 {@link GameSet Durchgänge} with 30 throws each.
 * The player has to play 15 Volle and 15 Abraeumen in each {@link GameSet}.
 *
 * @see Game
 * @see GameSet
 * @see Player
 * @see Match
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class Game120 extends Game {

	private static final int ANZAHL_DURCHGAENGE = 4;

	private final GameSet[] sets = new GameSet[ANZAHL_DURCHGAENGE];

	public Game120(Player player) {
		this(player, LocalDateTime.now());
	}

	public Game120(Player player, Player substitution1, Player substitution2, LocalDateTime date) {
		super(player, substitution1, substitution2, date);
	}

	protected Game120(Player player, LocalDateTime date) {
		super(player, date);
		for (int i = 0; i < ANZAHL_DURCHGAENGE; i++) {
			sets[i] = new GameSet(30, 15, 15, i);
		}
	}

	/* Todo move
	public static Game120 createGameFromCSV(Player player, LocalDateTime date, CSVFileReader csvFileReader) {
		return new Game120(player, date);
	}*/

	@Override
	public void start() {

	}

	/**
	 * {@inheritDoc}
	 * @param durchgang the number of the {@link GameSet} to get (0 &lt;= durchgang &lt; 4)
	 * @return the {@link GameSet} at the given number
	 * @throws IllegalArgumentException if the number is out of bounds
	 */
	@Override
	public GameSet getDurchgang(int durchgang) throws IllegalArgumentException {
		if (durchgang < 0 || durchgang >= ANZAHL_DURCHGAENGE) {
			throw new IllegalArgumentException(STR."Index out of bounds: \{durchgang}. Game120 has only 4 Durchgänge.");
		}
		return sets[durchgang];
	}

	//region setter/getter
	@Override
	public GameInfo getGameInfo() {
		return new GameInfo(4, 15, 15, 12, true);
	}

	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_120;
	}

	@Override
	public int getNumberOfDurchgaenge() {
		return ANZAHL_DURCHGAENGE;
	}

	@Override
	public int getNumberOfWurf() {
		return Arrays.stream(sets).mapToInt(GameSet::getAnzahlGespielteWuerfe).sum();
	}

	@Override
	public int getTotalFehlwurf() {
		var back = 0;
		for (GameSet set : sets) {
			back += set.getAnzahlFehler();
		}
		return back;
	}

	@Override
	public int getTotalScore() {
		var back = 0;
		for (GameSet set : sets) {
			back += set.getScore();
		}
		return back;
	}

	@Override
	public int getTotalVolle() {
		var back = 0;
		for (GameSet set : sets) {
			back += set.getVolleScore();
		}
		return back;
	}

	@Override
	public int getTotalAbraeumen() {
		var back = 0;
		for (GameSet set : sets) {
			back += set.getAbraeumenScore();
		}
		return back;
	}

	@Override
	public GameSet[] getSets() {
		return sets;
	}

	@Override
	public void setDurchgaenge(List<GameSet> durgaenge) throws IllegalArgumentException {
		if (durgaenge.size() != ANZAHL_DURCHGAENGE) {
			throw new IllegalArgumentException(
					STR."Anzahl der Durchgänge stimmt nicht. Erwarte \{ANZAHL_DURCHGAENGE}; waren aber \{durgaenge.size()}");
		}
		for (int i = 0; i < ANZAHL_DURCHGAENGE; i++) {
			sets[i] = durgaenge.get(i);
		}
		checkState();
	}
//endregion
}
