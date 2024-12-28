package core.game;

import core.team_and_player.Player;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A game with 100 throws.
 * <p>
 * A game 100 is a game with 100 throws. The game is played in 2 Durchgänge with 50 throws each.
 * </p>
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @since 1.0.0
 */
public class Game100 extends Game {

	private static final int ANZAHL_DURCHGAENGE = 2;

	private final GameSet[] sets = new GameSet[ANZAHL_DURCHGAENGE];

	/**
	 * Creates a new game with 100 throws.
	 *
	 * @param player The player who plays the game.
	 */
	public Game100(Player player) {
		this(player, LocalDateTime.now());
	}

	/**
	 * Creates a new game with 200 throws.
	 *
	 * @param player        The player who plays the game.
	 * @param substitution1 The first substitution player.
	 * @param substitution2 The second substitution player.
	 * @param date          The date of the game. (start)
	 */
	public Game100(Player player, Player substitution1, Player substitution2, LocalDateTime date) {
		super(player, substitution1, substitution2, date);
	}

	/**
	 * Creates a new game with 200 throws.
	 *
	 * @param player The player who plays the game.
	 * @param date   The date of the game. (start)
	 */
	protected Game100(Player player, LocalDateTime date) {
		super(player, date);
		for (int i = 0; i < ANZAHL_DURCHGAENGE; i++) {
			sets[i] = new GameSet(50, 25, 25, i);
		}
	}


	@Override
	public void start() {

	}

	@Override
	public GameSet getDurchgang(int durchgang) throws IllegalArgumentException {
		if (durchgang < 0 || durchgang >= ANZAHL_DURCHGAENGE) {
			throw new IllegalArgumentException(STR."Index out of bounds: \{durchgang}. Game100 has only 2 Durchgänge.");
		}
		return sets[durchgang];
	}

//region setter/getter
	@Override
	public GameInfo getGameInfo() {
		return new GameInfo(4, 25, 25, 20, true);
	}

	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_200;
	}

	@Override
	public int getNumberOfDurchgaenge() {
		return ANZAHL_DURCHGAENGE;
	}

	@Override
	public GameSet[] getGameSets() {
		return sets;
	}

	@Override
	public void setDurchgaenge(List<GameSet> durgaenge) {
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
