package de.olivergeisel.kegelplay.infrastructure.csv;

import de.olivergeisel.kegelplay.core.game.*;
import de.olivergeisel.kegelplay.core.match.GameKind;

import java.nio.file.Path;
import java.util.LinkedList;

/**
 * Reader for CSV files with game data.
 * This represents the data of a game. A game is played by one player in a team. A team is a parts in a match.
 */
public class GameCSVFileReader<G extends Game> extends CSVFileReader {

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
	 */
	public G readGame() {
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
		for (String[] line : lines) {
			var throwNumber = i++;
			var throwInGameSet = throwNumber % throwsPerDurchgang;
			if (throwInGameSet == 0) {
				gameSet = new GameSet(throwsPerDurchgang, volleCount, abraeumenCount);
				sets.add(gameSet);
			}
			if (line.length != 14) {
				LOGGER.log(System.Logger.Level.ERROR, STR."Line has not 14 columns: \{line}");
				continue;
			}
			var picRaw = line[3];
			if (picRaw == null || picRaw.isBlank() || picRaw.equals("-1")) {
				continue; // end last throw was empty
			}
			var value = Integer.parseInt(line[2]);
			if (value == -1) {
				continue;
			}
			var bild = new Wurfbild(Integer.parseInt(picRaw));
			var fehlwurf = line[4].equals("1");
			var time = Double.parseDouble(line[13]);
			gameSet.set(throwInGameSet, new Wurf(value, bild, fehlwurf, false), time);
		}
		var game = switch (gameKind) {
			case GAME_100, GAME_200, GAME_40, GAME_40_2 -> null;
			case GAME_120 -> (G) new Game120(null);
		};
		game.setDurchgaenge(sets);
		return game;
	}


}
