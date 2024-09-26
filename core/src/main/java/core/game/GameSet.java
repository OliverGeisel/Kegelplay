package core.game;

import java.util.Arrays;
import java.util.Objects;

import static java.lang.Math.max;

/**
 * Represents a set of a game.
 * A set consists of a number of throws. The number of throws is divided into full throws and throws to clear the pins.
 * A set is completed when all throws have been made or the time has expired.
 *
 * @see Wurf
 * @see Game
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class GameSet {

	private final int      throwCount;
	private final int      anzahlVolle;
	private final int      anzahlAbraeumen;
	private final Wurf[]   wuerfe;
	private       double   time;
	private       SetState state;
	private int gameSetNumber;

	public GameSet(int anzahlWuerfe, int anzahlVolle, int anzahlAbraeumen, int gameSetNumber) {
		if (anzahlWuerfe < 1) throw new IllegalArgumentException("Number of throws must be greater than 0");
		if (anzahlVolle + anzahlAbraeumen != anzahlWuerfe) {
			throw new IllegalArgumentException(
					"Number of volle and abraeumen pins must be equal to the number of throws");
		}
		this.throwCount = anzahlWuerfe;
		this.wuerfe = new Wurf[anzahlWuerfe];
		this.anzahlVolle = anzahlVolle;
		this.anzahlAbraeumen = anzahlAbraeumen;
		this.state = SetState.NOT_STARTED;
		this.gameSetNumber = gameSetNumber;
	}

	/**
	 * Set the throw at the given number.
	 *
	 * @param number Number of the throw
	 * @param w      Wurf to set
	 * @throws IndexOutOfBoundsException if the number is out of bounds
	 */
	public void set(int number, Wurf w) throws IndexOutOfBoundsException {
		wuerfe[number] = w;
		state = allThrowsAreSet() ? SetState.FINISHED : SetState.IN_PROGRESS;
	}

	/**
	 * Set the throw at the given number and set the remaining time for the set.
	 *
	 * @param number Number of the throw
	 * @param w      Wurf to set
	 * @param time   Remaining time in minutes
	 * @throws IndexOutOfBoundsException if the number is out of bounds
	 */
	public void set(int number, Wurf w, double time) throws IndexOutOfBoundsException {
		set(number, w);
		setTime(time);
	}

	public void reset(int index) throws IllegalStateException, IllegalArgumentException {
		if (index < 0 || index >= throwCount)
			throw new IllegalArgumentException(STR."Wurf \{index} don't exist");
		if (wuerfe[index] == null)
			throw new IllegalStateException(STR."Wurf \{index} is already null");
		wuerfe[index] = null;
		if (getAnzahlGespielteWuerfe() == 0) {
			state = SetState.NOT_STARTED;
		} else {
			state = time == 0 ? SetState.FINISHED : SetState.IN_PROGRESS;
		}
	}

	/**
	 * Get the throw at the given number. Can be null if throw was set yet.
	 *
	 * @param wurf Number of the throw
	 * @return Wurf at the given number. Is null if the throw was set yet.
	 * @throws IllegalArgumentException if the number is out of bounds
	 */
	public Wurf get(int wurf) throws IllegalArgumentException {
		if (wurf < 0 || wurf >= throwCount) throw new IllegalArgumentException(STR."Wurf \{wurf} existiert nicht");
		return wuerfe[wurf];
	}

	/**
	 * Check if all throws are set.
	 *
	 * @return true if all throws are set, false otherwise (at least one throw is missing)
	 */
	private boolean allThrowsAreSet() {
		return Arrays.stream(wuerfe).noneMatch(Objects::isNull);
	}

	//region setter/getter

	/**
	 * Number of maximum volle throws in this set.
	 * @return Number of maximum volle throws
	 */
	public int getAnzahlVolle() {
		return anzahlVolle;
	}

	/**
	 * Number of the set in the {@link Game}.
	 * @return Number of the set
	 */
	public int getGameSetNumber() {
		return gameSetNumber;
	}

	/**
	 * Get the remaining time for the set.
	 * If the time is 0, the set is {@link SetState#FINISHED}.
	 * @return Remaining time in minutes
	 */
	public double getTime() {
		return time;
	}

	/**
	 * Set the remaining time for the set. If the time is 0, the set is {@link SetState#FINISHED}.
	 * If the time is not 0 and all throws are set, the set is {@link SetState#FINISHED}.
	 * If the time is not 0 and no throw is set, the set is {@link SetState#NOT_STARTED}.
	 * Otherwise the set is {@link SetState#IN_PROGRESS}.
	 *
	 * @param time Remaining time in minutes
	 */
	public void setTime(double time) {
		this.time = max(0, time);
		if (this.time <= 0 || allThrowsAreSet()) {
			state = SetState.FINISHED;
		} else if (getAnzahlGespielteWuerfe() == 0) {
			state = SetState.NOT_STARTED;
		} else {
			state = SetState.IN_PROGRESS;
		}
	}

	/**
	 * Get the sum of all throws values.
	 *
	 * @return Sum of all throws values
	 */
	public int getScore() {
		return Arrays.stream(wuerfe).filter(Objects::nonNull).mapToInt(Wurf::getScore).sum();
	}

	public SetState getState() {
		return state;
	}

	/**
	 * Get the last {@link Wurf} of this set. This is either the last throw that was set, null if no throw was set
	 * yet or the last throw that was set but not the possible last throw of the set, because the time has expired.
	 * @return Last {@link Wurf} or null, if no throw was set yet
	 */
	public Wurf getLastWurf() {
		for (int i = throwCount - 1; i >= 0; i--) {
			if (wuerfe[i] != null) return wuerfe[i];
		}
		return null;
	}

	public boolean isCompleted() {
		return state == SetState.FINISHED;
	}

	public boolean isNotStarted() {
		return state == SetState.NOT_STARTED;
	}

	public boolean isRunning() {
		return state == SetState.IN_PROGRESS;
	}

	/**
	 * Get the sum of volle throws values.
	 *
	 * @return Sum of volle throws values
	 */
	public int getVolleScore() {
		var back = 0;
		for (int i = 0; i < anzahlVolle; ++i) {
			var wurf = wuerfe[i];
			if (wurf != null) {
				back += wurf.getScore();
			}
		}
		return back;
	}

	/**
	 * Get the number of throws that have been played.
	 *
	 * @return Number of throws that have been played
	 */
	public int getAnzahlGespielteWuerfe() {
		return (int) java.util.Arrays.stream(wuerfe).filter(Objects::nonNull).count();
	}

	/**
	 * Get the sum of abraeumen throws values.
	 *
	 * @return Sum of abraeumen throws values
	 */
	public int getAbraeumenScore() {
		var back = 0;
		for (int i = anzahlVolle; i < throwCount; ++i) {
			var wurf = wuerfe[i];
			if (wurf != null) {
				back += wurf.getScore();
			}
		}
		return back;
	}

	/**
	 * Get the number of throws with a score of 0.
	 *
	 * @return Number of missed throws
	 */
	public int getAnzahlFehler() {
		var back = 0;
		for (int i = 0; i < throwCount; ++i) {
			var wurf = wuerfe[i];
			if (wurf != null && wurf.getScore() == 0) {
				back++;
			}
		}
		return back;
	}

	/**
	 * Get the number of total throws in the set.
	 * If you want the number of throws that have been played, use {@link #getAnzahlGespielteWuerfe()}.
	 *
	 * @return Number of throws
	 */
	public int getThrowCount() {
		return throwCount;
	}
//endregion

	@Override
	public final boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof GameSet gameSet)) return false;

		return throwCount == gameSet.throwCount && anzahlVolle == gameSet.anzahlVolle
			   && Double.compare(time, gameSet.time) == 0 && gameSetNumber == gameSet.gameSetNumber
			   && Arrays.equals(wuerfe, gameSet.wuerfe) && state == gameSet.state;
	}

	@Override
	public int hashCode() {
		int result = throwCount;
		result = 31 * result + anzahlVolle;
		result = 31 * result + Arrays.hashCode(wuerfe);
		result = 31 * result + Double.hashCode(time);
		result = 31 * result + state.hashCode();
		result = 31 * result + gameSetNumber;
		return result;
	}

	@Override
	public String toString() {
		return STR."GameSet{state=\{state}, gameSetNumber=\\{gameSetNumber}, wuerfe=\{Arrays.toString(wuerfe)}'}";
	}
}
