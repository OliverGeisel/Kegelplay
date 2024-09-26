package core.util;


/**
 * A simple pair of two values.
 * @param <K> the type of the first value
 * @param <V> the type of the second value
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class Pair<K, V> {

	private K key;
	private V value;

	/**
	 * Creates a new pair with the given values.
	 * @param key the first value
	 * @param value the second value
	 */
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

//region setter/getter

	/**
	 * Returns the first value of the pair.
	 * @return the first value
	 */
	public K getKey() {
		return key;
	}

	/**
	 * Returns the second value of the pair.
	 * @return the second value
	 */
	public V getValue() {
		return value;
	}

	/**
	 * Gets the first value of the pair. This is the same as {@link #getKey()}.
	 * @return the first value
	 */
	public K getFirst() {
		return key;
	}

	/**
	 * Gets the second value of the pair. This is the same as {@link #getValue()}.
	 * @return the second value
	 */
	public V getSecond() {
		return value;
	}
//endregion


}
