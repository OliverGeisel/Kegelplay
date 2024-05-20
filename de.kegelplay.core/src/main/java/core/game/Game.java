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
 *     A game is in the state {@link NotStartedState} if it has not yet started.
 *     A game is in the state {@link RunningState} if it is currently being played.
 *     A game is in the state {@link PausedState} if it is currently paused.
 *     A game is in the state {@link FinishedState} if it is finished.
 * </p>
 * <p>
 * 		A game is played by one {@link Player} at a time. Each game can substitute the original player with maximum
 * 	two other players. The {@link #getCurrentPlayer()} method returns the current player. This can be the original
 * 	player or one of the two substitutions.
 * </p>
 *
 * @see GameSet
 * @see Player
 * @see Match
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public abstract class Game {


	private Player        substitution1;
	private Player        substitution2; // todo better substitution system.
	private Player        player; // original player
	private LocalDateTime date;
	private GameState     state;


	protected Game(Player player) {
		this(player, LocalDateTime.now());
	}

	protected Game(Player player, LocalDateTime date) {
		this.player = player;
		if (player != null) {
			player.setGame(this);
		}
		this.date = date;
		state = new NotStartedState();
	}

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
		var list = Arrays.stream(getSets()).toList();
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

	public abstract void start();

	/**
	 * Returns the {@link GameSet} of the game at the given number.
	 *
	 * @param durchgang Number of the {@link GameSet}
	 * @return {@link GameSet} at the given number
	 * @throws IllegalArgumentException if the number is out of bounds
	 */
	public abstract GameSet getDurchgang(int durchgang) throws IllegalArgumentException;

	//region setter/getter

	/**
	 * Returns the player that is currently play the game. This can be the original player or one of the two
	 * substitutions. If sub1 is not null then he will be returned except sub2 isn`t.
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
	 * Returns the {@link GameInfo} of the game.
	 * @return {@link GameInfo} of the game
	 */
	public abstract GameInfo getGameInfo();

	/**
	 * Game is currently played.
	 * A game is currently played if it is in the state {@link RunningState} or {@link PausedState}.
	 * @return true if game is currently played
	 */
	public boolean isOn() {
		return state instanceof RunningState || state instanceof PausedState;
	}

	public boolean isFinished() {
		return state instanceof FinishedState;
	}

	public boolean isNotStarted() {
		return state instanceof NotStartedState;
	}

	public boolean isPaused() {
		return state instanceof PausedState;
	}

	public boolean isRunning() {
		return state instanceof RunningState;
	}

	public abstract GameKind getGameKind();

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public abstract int getNumberOfDurchgaenge();

	public abstract int getNumberOfWurf();

	public abstract int getTotalFehlwurf();

	public abstract int getTotalScore();

	public abstract int getTotalVolle();

	public abstract int getTotalAbraeumen();

	/**
	 * Returns the {@link GameSet}s of the game.
	 * @return Array of {@link GameSet}s
	 */
	public abstract GameSet[] getSets();

	/**
	 * Returns the current {@link Player} for the game.
	 * Change if the player is substituted.
	 * @return Current player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Set the current player for the game.
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
	 * @return Last {@link Wurf}
	 */
	public Wurf getLastWurf() {
		var back = getCurrentSet().getLastWurf();
		if (back == null) {
			back = new Wurf(0, new Wurfbild(0), false, false);
		}
		return back;
	}

	/**
	 * Returns the current {@link GameSet} of the game.
	 * @return Current {@link GameSet}
	 */
	public GameSet getCurrentSet() {
		for (var set : getSets()) {
			if (set.isRunning()) {
				return set;
			}
		}
		return getDurchgang(getSets().length - 1);
	}

	public void setSubstitution1(Player player) {
		substitution1 = player;
	}

	public void setSubstitution2(Player player) {
		substitution2 = player;
	}

	public abstract void setDurchgaenge(List<GameSet> durgaenge);
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
