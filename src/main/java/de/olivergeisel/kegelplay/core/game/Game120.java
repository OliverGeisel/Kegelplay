package de.olivergeisel.kegelplay.core.game;

import de.olivergeisel.kegelplay.core.team_and_player.Player;
import de.olivergeisel.kegelplay.infrastructure.data_reader.CSVFile;
import javafx.beans.InvalidationListener;
import javafx.util.Subscription;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Game120 extends Game{

	private static final int ANZAHL_DURCHGAENGE = 4;

	private final Durchgang[] durchgaenge = new Durchgang[ANZAHL_DURCHGAENGE];

	protected Game120(Player player, LocalDateTime date) {
		super(player, date);
		for (int i = 0; i < ANZAHL_DURCHGAENGE; i++) {
			durchgaenge[i] = new Durchgang(30, 15, 15);
		}
	}

	public static Game120 createGameFromCSV(Player player, LocalDateTime date, CSVFile csvFile) {
		var back =  new Game120(player, date);
		return back;
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
	public int getNumberOfDurchgaenge() {
		return ANZAHL_DURCHGAENGE;
	}

	@Override
	public int getNumberOfWurf() {
		return 0;
	}

	@Override
	public Durchgang[] getDurchgaenge() {
		return new Durchgang[0];
	}

	@Override
	public Durchgang getDurchgang(int durchgang) {
		return durchgaenge[durchgang];
	}
}
