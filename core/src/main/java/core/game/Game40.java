package core.game;

import core.team_and_player.Player;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a {@link Game} with 40 throws. A game consists of two {@link GameSet}s.
 * Each {@link GameSet} has 20 throws. 10 throws for the {@link AbschnittType#VOLLE} and 10 throws for the {@link AbschnittType#RAEUMEN}.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see Game
 * @since 1.0.0
 */
public class Game40 extends Game {

	GameSet[] sets = new GameSet[2];

	/**
	 * Create a new game with the given player. The start date of the game is the current date.
	 *
	 * @param player Player that plays the game
	 */
	public Game40(Player player) {
		super(player);
	}

	/**
	 * Create a new game with the given player and the given date.
	 *
	 * @param player Player that plays the game
	 * @param date   Date when the game was started
	 */
	public Game40(Player player, LocalDateTime date) {
		super(player, date);
	}

	/**
	 * Create a new game with the given player and the given substitutions. The start date of the game is the current date.
	 *
	 * @param player        Player that plays the game
	 * @param substitution1 First substitution
	 * @param substitution2 Second substitution
	 * @param date          Date when the game was started
	 */
	public Game40(Player player, Player substitution1, Player substitution2, LocalDateTime date) {
		super(player, substitution1, substitution2, date);
	}

	@Override
	public void start() {

	}


	/**
	 * {@inheritDoc}
	 *
	 * @param gameSetNumber {@inheritDoc}
	 * @return {@inheritDoc}
	 *
	 * @throws IllegalArgumentException if the game set number is not 0 or 1
	 */
	@Override
	public GameSet getGameSet(int gameSetNumber) throws IllegalArgumentException {
		return sets[gameSetNumber];
	}

	//region setter/getter
	@Override
	public GameInfo getGameInfo() {
		return new GameInfo(2, 10, 10, 8, true);
	}

	@Override
	public GameKind getGameKind() {
		return GameKind.GAME_40;
	}

	@Override
	public int getNumberOfGameSets() {
		return 2;
	}

	public int getNumberOfWurf() {
		var back = 0;
		for (GameSet set : sets) {
			if (set.getState() == SetState.FINISHED) {
				back += set.getThrowCount();
			} else {
				back += set.getAnzahlGespielteWuerfe();
			}
		}
		return back;
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
	public GameSet[] getGameSets() {
		return sets;
	}

	@Override
	public void setGameSets(List<GameSet> gameSets) throws IllegalArgumentException {
		if (gameSets.size() != 2) {
			throw new IllegalArgumentException(
					STR."Anzahl der Durchgänge stimmt nicht. Erwarte 2; waren aber \{gameSets.size()}");
		}
		for (int i = 0; i < 2; i++) {
			sets[i] = gameSets.get(i);
		}
		checkState();
	}
//endregion
}
