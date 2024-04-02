package de.olivergeisel.kegelplay.core.game;

import de.olivergeisel.kegelplay.core.match.GameKind;
import de.olivergeisel.kegelplay.core.match.Match;
import de.olivergeisel.kegelplay.core.team_and_player.Player;
import javafx.beans.Observable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A game.
 * A game is played by one {@link Player}.
 * Represents the current state of a game in a {@link Match}
 */
public abstract class Game implements Observable {


	private final List<Player>  substitutions      = new ArrayList<>();
	private final List<Player>  substitutedPlayers = new ArrayList<>();
	private       Player        player; // current player
	private       LocalDateTime date;
	private       GameState     state;

	protected Game(Player player, LocalDateTime date) {
		this.player = player;
		if (player != null) {
			player.setGame(this);
		}
		this.date = date;
		state = new NotStartedState();
	}

	protected Game(Player player) {
		this(player, LocalDateTime.now());
	}

	protected Game(Player player, List<Player> substitutions, LocalDateTime date) {
		this(player, date);
		this.substitutions.addAll(substitutions);
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

	public void substitute(Player player) {
		if (substitutions.contains(player)) {
			substitutions.remove(player);
			substitutedPlayers.add(this.player);
			this.player = player;
		}
		throw new IllegalArgumentException("Spieler nicht in der Liste der Ersatzspieler");
	}

	public abstract void start();

	public abstract GameSet getDurchgang(int durchgang);

	//region setter/getter

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

	public abstract void setDurchgaenge(List<GameSet> durgaenge);
//endregion


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
