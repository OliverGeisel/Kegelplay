package core.game;

import core.match.Match;
import core.team_and_player.Player;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A Game120 is a game with 120 throws. This is the international standard for a game in classic 9pin bowling.
 * The game is played in 4 {@link GameSet Durchgänge} with 30 throws each.
 * The player has to play 15 Volle and 15 Abraeumen in each {@link GameSet}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @see GameSet
 * @see Player
 * @see Match
 * @since 1.0.0
 */
public class Game120 extends Game {

	private static final int ANZAHL_DURCHGAENGE = 4;

	private final GameSet[] sets = new GameSet[ANZAHL_DURCHGAENGE];

	/**
	 * Creates a new game with 120 throws.
	 * The start date of the game is the current date.
	 *
	 * @param player The player who plays the game.
	 */
	public Game120(Player player) {
		this(player, LocalDateTime.now());
	}

	/**
	 * Create a new game with the given player and the given date.
	 *
	 * @param player Player that plays the game
	 * @param date   Date when the game was started
	 */
	protected Game120(Player player, LocalDateTime date) {
		super(player, date);
		for (int i = 0; i < ANZAHL_DURCHGAENGE; i++) {
			sets[i] = new GameSet(30, 15, 15, i);
		}
	}

	/**
	 * Creates a new game with 120 throws.
	 *
	 * @param player        The player who plays the game.
	 * @param substitution1 The first substitution player.
	 * @param substitution2 The second substitution player.
	 * @param date          The date of the game. (start)
	 */
	public Game120(Player player, Player substitution1, Player substitution2, LocalDateTime date) {
		super(player, substitution1, substitution2, date);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @param gameSetNumber the number of the {@link GameSet} to get (0 &lt;= durchgang &lt; 4)
	 * @return the {@link GameSet} at the given number
	 *
	 * @throws IllegalArgumentException if the number is out of bounds
	 */
	@Override
	public GameSet getGameSet(int gameSetNumber) throws IllegalArgumentException {
		if (gameSetNumber < 0 || gameSetNumber >= ANZAHL_DURCHGAENGE) {
			throw new IllegalArgumentException(
					STR."Index out of bounds: \{gameSetNumber}. Game120 has only 4 Durchgänge.");
		}
		return getGameSets()[gameSetNumber];
	}

	/* Todo move
	public static Game120 createGameFromCSV(Player player, LocalDateTime date, CSVFileReader csvFileReader) {
		return new Game120(player, date);
	}*/

	@Override
	public void start() {

	}

	//region setter/getter
	@Override
	public GameSet[] getGameSets() {
		return sets;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public GameInfo getGameInfo() {
		return new GameInfo(4, 15, 15, 12, true);
	}

	@Override
	public int getNumberOfGameSets() {
		return ANZAHL_DURCHGAENGE;
	}

	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_120;
	}

	/**
	 * Returns the number of {@link Wurf} that were thrown in the game until now.
	 *
	 * @return Number of {@link Wurf}
	 */
	@Override
	public int getNumberOfWurf() {
		return 0;
	}

	@Override
	public void setGameSets(List<GameSet> gameSets) throws IllegalArgumentException {
		if (gameSets.size() != ANZAHL_DURCHGAENGE) {
			throw new IllegalArgumentException(
					STR."Anzahl der Durchgänge stimmt nicht. Erwarte \{ANZAHL_DURCHGAENGE}; waren aber \{gameSets.size()}");
		}
		for (int i = 0; i < ANZAHL_DURCHGAENGE; i++) {
			sets[i] = gameSets.get(i);
		}
		checkState();
	}
//endregion
}
