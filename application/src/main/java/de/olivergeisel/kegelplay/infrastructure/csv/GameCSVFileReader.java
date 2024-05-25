package de.olivergeisel.kegelplay.infrastructure.csv;


import core.game.*;

import java.io.Closeable;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Reader for CSV files with game data.
 * This represents the data of a game. A game is played by one player in a team. A team is a parts in a match.
 */
public class GameCSVFileReader<G extends Game> extends CSVFileReader implements GameSource {

	private static final System.Logger LOGGER = System.getLogger(GameCSVFileReader.class.getName());

	private final GameKind gameKind;

	public GameCSVFileReader(Path file, GameKind gameKind) {
		super(file);
		this.gameKind = gameKind;
	}

	/**
	 * Read the game data from the file.
	 * The player will be <b>null</b>
	 * @return Game object with the data from the file.
	 * @throws IllegalStateException if gameKind is <b>null</b>
	 */
	public G readGame() throws IllegalStateException {
		if (gameKind == null) {
			throw new IllegalStateException("gameKind must not be null");
		}
		var durgaenge = gameKind.getNumberOfDurchgaenge();
		var volleCount = gameKind.getNumberOfVolle();
		var abraeumenCount = gameKind.getNumberOfAbraeumen();
		var timePerDurchgang = gameKind.getTimePerDurchgang();
		var throwsPerDurchgang = volleCount + abraeumenCount;
		var header = getHeader();
		var lines = getData();
		int i = 0;
		var sets = new LinkedList<GameSet>();
		GameSet gameSet = null;
		var setsNumber = 0;
		var prevCode = 0;
		var game = switch (gameKind) {
			case GAME_100, GAME_200, GAME_40_2 -> null;
			case GAME_40 -> (G) new Game40(null);
			case GAME_120 -> (G) new Game120(null);
		};
		if (lines.isEmpty()) {
			fillWithEmpty(game);
			return game;
		}
		for (String[] line : lines) {
			var throwNumber = i++;
			var throwInGameSet = throwNumber % throwsPerDurchgang;
			if (throwInGameSet == 0) {
				gameSet = new GameSet(throwsPerDurchgang, volleCount, abraeumenCount, setsNumber++);
				sets.add(gameSet);
			}
			if (line.length != 14) {
				LOGGER.log(System.Logger.Level.ERROR, STR."Line has not 14 columns: \{line}");
				continue;
			}
			var picRaw = line[3];
			if (picRaw == null || picRaw.isBlank() || picRaw.equals("-1")) {
				prevCode = 0;
				continue;
			}
			var value = Integer.parseInt(line[2]);
			if (value == -1) {
				prevCode = 0;
				continue;
			}
			var picEncoded = Integer.parseInt(picRaw);
			var bild = new Wurfbild(picEncoded);
			var fehlwurf = line[4].equals("1");
			var time = Double.parseDouble(line[13]);
			var isVolle = throwInGameSet < volleCount;
			var isAnspiel = isVolle || prevCode == 511;
			gameSet.set(throwInGameSet, new Wurf(value, bild, fehlwurf, false, isVolle, isAnspiel), time);
			prevCode = picEncoded;
		}

		game.setDurchgaenge(sets);
		return game;
	}

	private void fillWithEmpty(G game) {
		var sets = new LinkedList<GameSet>();
		var throwsPerDurchgang = gameKind.getNumberOfVolle() + gameKind.getNumberOfAbraeumen();
		for (int i = 0; i < gameKind.getNumberOfDurchgaenge(); i++) {
			sets.add(new GameSet(throwsPerDurchgang, gameKind.getNumberOfVolle(), gameKind.getNumberOfAbraeumen(), i));
		}
		game.setDurchgaenge(sets);
	}


	@Override
	public Iterable<Wurf> get() {
		var back = new LinkedList<Wurf>();
		var i = 0;
		var data = getData();
		var volleCount = gameKind.getNumberOfVolle();
		var abraeumenCount = gameKind.getNumberOfAbraeumen();
		var timePerDurchgang = gameKind.getTimePerDurchgang();
		var throwsPerDurchgang = volleCount + abraeumenCount;
		var prevCode = 0;
		for (String[] line : data) {
			++i;
			if (line.length != 14) {
				LOGGER.log(System.Logger.Level.ERROR, STR."Line has not 14 columns: \{line}");
				continue;
			}
			var picRaw = line[3];
			if (picRaw == null || picRaw.isBlank() || picRaw.equals("-1")) {
				prevCode = 0;
				continue;
			}
			var value = Integer.parseInt(line[2]);
			if (value == -1) {
				prevCode = 0;
				continue;
			}
			var code = Integer.parseInt(picRaw);
			var bild = new Wurfbild(code);
			var fehlwurf = line[4].equals("1");
			var isVolle = ((i - 1) % throwsPerDurchgang) < volleCount;
			var isAbraeumen = !isVolle || prevCode == 511;
			back.add(new Wurf(value, bild, fehlwurf, false, isVolle, isAbraeumen));
			prevCode = code;
		}
		return back;
	}

	/**
	 * Closes this resource, relinquishing any underlying resources.
	 * This method is invoked automatically on objects managed by the
	 * {@code try}-with-resources statement.
	 *
	 * @throws Exception if this resource cannot be closed
	 * @apiNote While this interface method is declared to throw {@code
	 * Exception}, implementers are <em>strongly</em> encouraged to
	 * declare concrete implementations of the {@code close} method to
	 * throw more specific exceptions, or to throw no exception at all
	 * if the close operation cannot fail.
	 *
	 * <p> Cases where the close operation may fail require careful
	 * attention by implementers. It is strongly advised to relinquish
	 * the underlying resources and to internally <em>mark</em> the
	 * resource as closed, prior to throwing the exception. The {@code
	 * close} method is unlikely to be invoked more than once and so
	 * this ensures that the resources are released in a timely manner.
	 * Furthermore it reduces problems that could arise when the resource
	 * wraps, or is wrapped, by another resource.
	 *
	 * <p><em>Implementers of this interface are also strongly advised
	 * to not have the {@code close} method throw {@link
	 * InterruptedException}.</em>
	 * <p>
	 * This exception interacts with a thread's interrupted status,
	 * and runtime misbehavior is likely to occur if an {@code
	 * InterruptedException} is {@linkplain Throwable#addSuppressed
	 * suppressed}.
	 * <p>
	 * More generally, if it would cause problems for an
	 * exception to be suppressed, the {@code AutoCloseable.close}
	 * method should not throw it.
	 *
	 * <p>Note that unlike the {@link Closeable#close close}
	 * method of {@link Closeable}, this {@code close} method
	 * is <em>not</em> required to be idempotent.  In other words,
	 * calling this {@code close} method more than once may have some
	 * visible side effect, unlike {@code Closeable.close} which is
	 * required to have no effect if called more than once.
	 * <p>
	 * However, implementers of this interface are strongly encouraged
	 * to make their {@code close} methods idempotent.
	 */
	@Override
	public void close() throws Exception {
		// not needed
	}

	/**
	 * Returns an iterator over elements of type {@code T}.
	 *
	 * @return an Iterator.
	 */
	@Override
	public Iterator<Wurf> iterator() {
		return get().iterator();
	}
}
