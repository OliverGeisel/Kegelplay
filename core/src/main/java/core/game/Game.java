package core.game;

import core.match.Match;
import core.team_and_player.Player;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Represents the current state of a game in a {@link Match}.
 * A game is played by one {@link Player}. It consists of {@link GameSet}s.
 * This is only the abstract class for a game. The concrete implementation is done by the subclasses.
 * The diffenenct between the subclasses is the number of {@link GameSet}s and the specific configuration of the
 * {@link GameSet}s.
 * <br>
 * A game can be in different states.
 * <p>
 * A game is in the state {@link NotStartedState} if it has not yet started.
 * A game is in the state {@link RunningState} if it is currently being played.
 * A game is in the state {@link PausedState} if it is currently paused.
 * A game is in the state {@link FinishedState} if it is finished.
 * </p>
 * <p>
 * A game is played by one {@link Player} at a time. Each game can substitute the original player with maximum
 * two other players. The {@link #getCurrentPlayer()} method returns the current player. This can be the original
 * player or one of the two substitutions.
 * </p>
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see GameSet
 * @see Player
 * @see Match
 * @since 1.0.0
 */
public abstract class Game {


	private Player        substitution1;
	private Player        substitution2; // todo better substitution system.
	private Player        player; // original player
	private LocalDateTime date;
	private GameState     state;


	/**
	 * Creates a new game with the given player.
	 *
	 * @param player The player who plays the game.
	 */
	protected Game(Player player) {
		this(player, LocalDateTime.now());
	}

	/**
	 * Creates a new game with the given player and date.
	 *
	 * @param player The player who plays the game.
	 * @param date   The date of the game. (start)
	 */
	protected Game(Player player, LocalDateTime date) {
		this.player = player;
		if (player != null) {
			player.setGame(this);
		}
		this.date = date;
		state = new NotStartedState();
	}

	/**
	 * Creates a new game with the given player and substitutions.
	 *
	 * @param player        The player who plays the game.
	 * @param substitution1 The first substitution player.
	 * @param substitution2 The second substitution player.
	 * @param date          The date of the game. (start)
	 */
	protected Game(Player player, Player substitution1, Player substitution2, LocalDateTime date) {
		this(player, date);
		this.substitution1 = substitution1;
		this.substitution2 = substitution2;
	}

	/**
	 * Update the current state of the game.
	 */
	public void checkState() {
		if (isFinished()) {
			return;
		}
		var list = Arrays.stream(getGameSets()).toList();
		if (list.stream().allMatch(GameSet::isCompleted)) {
			state = new FinishedState();
			return;
		}
		if (isRunning() && list.stream().anyMatch(it -> !it.isCompleted())) {
			state = new PausedState();
			return;
		}
		if ((isNotStarted() || isPaused()) &&
			list.stream().anyMatch(GameSet::isRunning)) {
			state = new RunningState();
		}
	}

	/**
	 * Start the game. (State changes to {@link RunningState})
	 */
	public abstract void start();

	/**
	 * Returns the {@link GameSet} of the game at the given number.
	 *
	 * @param gameSetNumber Number of the {@link GameSet}
	 * @return {@link GameSet} at the given number
	 *
	 * @throws IllegalArgumentException if the number is out of bounds
	 */
	public abstract GameSet getGameSet(int gameSetNumber) throws IllegalArgumentException;

	//region setter/getter

	/**
	 * Returns the {@link GameSet}s of the game.
	 * The order of the {@link GameSet}s is important and cannot be changed.
	 *
	 * @return {@link GameSet}s of the game
	 */
	public abstract GameSet[] getGameSets();
	/**
	 * Returns the player that is currently play the game. This can be the original player or one of the two
	 * substitutions. If sub1 is not null, then he will be returned except sub2 isn't.
	 *
	 * @return Player that is currently play
	 */
	public Player getCurrentPlayer() {
		if (substitution2 != null) {
			return substitution2;
		}
		if (substitution1 != null) {
			return substitution1;
		}
		return player;
	}

	/**
	 * The Game is currently played.
	 * A game is currently played if it is in the state {@link RunningState} or {@link PausedState}.
	 *
	 * @return true if game is currently played
	 */
	public boolean isOn() {
		return state instanceof RunningState || state instanceof PausedState;
	}

	/**
	 * Returns the {@link GameInfo} of the game.
	 *
	 * @return {@link GameInfo} of the game
	 */
	public abstract GameInfo getGameInfo();

	/**
	 * The Game is finished.
	 * A game is finished if it is in the state {@link FinishedState}.
	 *
	 * @return true if the game is finished
	 */
	public boolean isFinished() {
		return state instanceof FinishedState;
	}

	/**
	 * The Game is not started.
	 * A game is not started if it is in the state {@link NotStartedState}.
	 *
	 * @return true if the game is not started
	 */
	public boolean isNotStarted() {
		return state instanceof NotStartedState;
	}

	/**
	 * The Game is running.
	 * A game is running if it is in the state {@link RunningState}.
	 *
	 * @return true if the game is running
	 */
	public boolean isRunning() {
		return state instanceof RunningState;
	}

	/**
	 * Game is paused.
	 * A game is paused if it is in the state {@link PausedState}.
	 *
	 * @return true if game is paused
	 */
	public boolean isPaused() {
		return state instanceof PausedState;
	}

	/**
	 * Get the {@link GameKind} of the game. Is the main decision type for all Logic.
	 *
	 * @return {@link GameKind} of the game
	 */
	public abstract GameKind getGameKind();

	/**
	 * Returns the date of the game on which it is played.
	 *
	 * @return Date of the game
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Set the date of the game on which it is played.
	 *
	 * @param date Date of the game
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * Returns the number of {@link GameSet}s of the game.
	 *
	 * @return Number of {@link GameSet}s
	 */
	public abstract int getNumberOfGameSets();

	/**
	 * Returns the number of {@link Wurf} that were thrown in the game until now.
	 *
	 * @return Number of {@link Wurf}
	 */
	public int getNumberOfWurf() {
		return Arrays.stream(getGameSets()).mapToInt(GameSet::getAnzahlGespielteWuerfe).sum();
	}

	/**
	 * Get the total number of Volle for the game.
	 * Sum of all {@link GameSet#getVolleScore()} of the game.
	 *
	 * @return Total number of Volle of the game
	 */
	public int getTotalVolle() {
		var back = 0;
		for (GameSet set : getGameSets()) {
			back += set.getVolleScore();
		}
		return back;
	}

	/**
	 * total number of Fehlwürfe (miss throws) of the game.
	 *
	 * @return Sum of all {@link GameSet#getAnzahlFehler()} of the game
	 */
	public int getTotalFehlwurf() {
		var back = 0;
		for (GameSet set : getGameSets()) {
			back += set.getAnzahlFehler();
		}
		return back;
	}

	/**
	 * Get the total score of the game.
	 * Sum of all {@link GameSet#getScore()} of the game.
	 *
	 * @return Total score of the game
	 */
	public int getTotalScore() {
		var back = 0;
		for (GameSet set : getGameSets()) {
			back += set.getScore();
		}
		return back;

	}

	/**
	 * Get the total number of Abraeumen for the game.
	 * Sum of all {@link GameSet#getAbraeumenScore()} of the game.
	 *
	 * @return Total number of Abraeumen of the game
	 */
	public int getTotalAbraeumen() {
		var back = 0;
		for (GameSet set : getGameSets()) {
			back += set.getAbraeumenScore();
		}
		return back;
	}

	/**
	 * Returns the current {@link GameSet} of the game.
	 *
	 * @return Current {@link GameSet}
	 */
	public GameSet getCurrentSet() {
		var sets = this.getGameSets();
		if (Arrays.stream(sets).allMatch(GameSet::isNotStarted))
			return sets[0];
		if (Arrays.stream(sets).allMatch(GameSet::isCompleted))
			return sets[sets.length - 1];
		for (int i = sets.length - 1; i >= 0; i--) {
			var set = sets[i];
			if (set.isRunning() || set.isCompleted()) {
				return set;
			}
		}
		return getGameSet(sets.length - 1);
	}

	/**
	 * Returns the current {@link Player} for the game.
	 * Change if the player is substituted.
	 *
	 * @return Current player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Set the current player for the game.
	 *
	 * @param player Current player
	 * @throws IllegalArgumentException if player is not null
	 */
	public void setPlayer(Player player) throws IllegalArgumentException {
		if (player == null) {
			throw new IllegalArgumentException("Player is null");
		}
		this.player = player;
		player.setGame(this);
	}

	/**
	 * Returns the last {@link Wurf} of the game that was thrown.
	 *
	 * @return Last {@link Wurf}
	 */
	public Wurf getLastWurf() {
		var back = getCurrentSet().getLastWurf();
		if (back == null) {
			back = new Wurf(0, new Wurfbild(0), false, false, false, false);
		}
		return back;
	}

	/**
	 * Overrides all {@link GameSet}s of the game.
	 *
	 * @param gameSets List of {@link GameSet}s to override with
	 */
	public abstract void setGameSets(List<GameSet> gameSets);

	/**
	 * Set the first substitution player.
	 *
	 * @param player First substitution player
	 */
	public void setSubstitution1(Player player) {
		substitution1 = player;
	}

	/**
	 * Set the second substitution for the game.
	 *
	 * @param player Player that substitutes the first substitution
	 */
	public void setSubstitution2(Player player) {
		substitution2 = player;
	}
//endregion

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Game game)) return false;

		return substitution1.equals(game.substitution1) && substitution2.equals(game.substitution2)
			   && Objects.equals(player, game.player) && Objects.equals(date, game.date)
			   && Objects.equals(state, game.state);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(substitution1);
		result = 31 * result + Objects.hashCode(substitution2);
		result = 31 * result + Objects.hashCode(player);
		result = 31 * result + Objects.hashCode(date);
		result = 31 * result + Objects.hashCode(state);
		return result;
	}


	/**
	 * Representation of the state for a game.
	 *
	 * @author Oliver Geisel
	 * @version 1.0.0
	 * @see Game
	 * @since 1.0.0
	 */
	private class GameState {

	}


	private class NotStartedState extends GameState {

	}

	private class RunningState extends GameState {

	}

	private class FinishedState extends GameState {

	}

	private class PausedState extends GameState {

	}

}
