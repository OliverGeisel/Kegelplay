package de.olivergeisel.kegelplay.core.game;

import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.infrastructure.csv.CSVFileReader;
import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.util.Subscription;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A Game120 is a game with 120 throws.
 * The game is played in 4 Durchgänge with 30 throws each.

 */
public class Game120 extends Game {

	private static final int ANZAHL_DURCHGAENGE = 4;

	private final GameSet[] sets = new GameSet[ANZAHL_DURCHGAENGE];

	public Game120(Player player) {
		this(player, LocalDateTime.now());
	}

	public Game120(Player player, List<Player> substitutions, LocalDateTime date) {
		super(player, substitutions, date);
	}

	protected Game120(Player player, LocalDateTime date) {
		super(player, date);
		for (int i = 0; i < ANZAHL_DURCHGAENGE; i++) {
			sets[i] = new GameSet(30, 15, 15, i);
		}
	}

	public static Game120 createGameFromCSV(Player player, LocalDateTime date, CSVFileReader csvFileReader) {
		return new Game120(player, date);
	}

	@Override
	public void start() {

	}

	/**
	 * Adds an {@link InvalidationListener} which will be notified whenever the
	 * {@code Observable} becomes invalid. If the same
	 * listener is added more than once, then it will be notified more than
	 * once. That is, no check is made to ensure uniqueness.
	 * <p>
	 * Note that the same actual {@code InvalidationListener} instance may be
	 * safely registered for different {@code Observables}.
	 * <p>
	 * The {@code Observable} stores a strong reference to the listener
	 * which will prevent the listener from being garbage collected and may
	 * result in a memory leak. It is recommended to either unregister a
	 * listener by calling {@link #removeListener(InvalidationListener)
	 * removeListener} after use or to use an instance of
	 * {@link WeakInvalidationListener} avoid this situation.
	 *
	 * @param listener The listener to register
	 * @throws NullPointerException if the listener is null
	 * @see #removeListener(InvalidationListener)
	 */
	@Override
	public void addListener(InvalidationListener listener) {

	}

	/**
	 * Removes the given listener from the list of listeners, that are notified
	 * whenever the value of the {@code Observable} becomes invalid.
	 * <p>
	 * If the given listener has not been previously registered (i.e. it was
	 * never added) then this method call is a no-op. If it had been previously
	 * added then it will be removed. If it had been added more than once, then
	 * only the first occurrence will be removed.
	 *
	 * @param listener The listener to remove
	 * @throws NullPointerException if the listener is null
	 * @see #addListener(InvalidationListener)
	 */
	@Override
	public void removeListener(InvalidationListener listener) {

	}

	/**
	 * Creates a {@code Subscription} on this {@code Observable} which calls
	 * {@code invalidationSubscriber} whenever it becomes invalid. The provided
	 * subscriber is akin to an {@code InvalidationListener} without the
	 * {@code Observable} parameter. If the same subscriber is subscribed more
	 * than once, then it will be notified more than once. That is, no check is
	 * made to ensure uniqueness.
	 * <p>
	 * Note that the same subscriber instance may be safely subscribed for
	 * different {@code Observables}.
	 * <p>
	 * Also note that when subscribing on an {@code Observable} with a longer
	 * lifecycle than the subscriber, the subscriber must be unsubscribed
	 * when no longer needed as the subscription will otherwise keep the subscriber
	 * from being garbage collected.
	 *
	 * @param invalidationSubscriber a {@code Runnable} to call whenever this
	 *                               value becomes invalid, cannot be {@code null}
	 * @return a {@code Subscription} which can be used to cancel this
	 * subscription, never {@code null}
	 * @throws NullPointerException if the subscriber is {@code null}
	 * @see #addListener(InvalidationListener)
	 * @since 21
	 */
	@Override
	public Subscription subscribe(Runnable invalidationSubscriber) {
		return super.subscribe(invalidationSubscriber);
	}

	@Override
	public GameSet getDurchgang(int durchgang) {
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
		var back = 0;
		for (GameSet set : sets) {
			if (set.getState() == SetState.FINISHED) {
				back += set.getAnzahlWuerfe();
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
			back += set.getAnzahlVolle();
		}
		return back;
	}

	@Override
	public int getTotalAbraeumen() {
		var back = 0;
		for (GameSet set : sets) {
			back += set.getAnzahlAbraeumen();
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
			throw new IllegalArgumentException("Anzahl der Durchgänge stimmt nicht");
		}
		for (int i = 0; i < ANZAHL_DURCHGAENGE; i++) {
			sets[i] = durgaenge.get(i);
		}
		checkState();
	}
//endregion
}
