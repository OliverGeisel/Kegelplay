package core.util;


import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a collection of key-value pairs. The region has a specific name;
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 * @author Oliver Geisel
 * @version 1.0.0
 * @since 1.0.0
 */
public class KeyValueRegion<K, V> {

	private final String    name;
	private final Map<K, V> keyValuePairs;


	/**
	 * Creates a new KeyValueRegion with the given name and key-value pairs.
	 *
	 * @param name      The name of the region.
	 * @param keyValues The key-value pairs of the region.
	 */
	public KeyValueRegion(String name, Map<K, V> keyValues) {
		this.name = name;
		this.keyValuePairs = keyValues;
	}

	/**
	 * Returns the value for the given key.
	 *
	 * @param key the key
	 * @return the value for the given key or null if the key is not present
	 *
	 * @throws IllegalArgumentException if the key is null
	 */
	public V getValue(K key) throws IllegalArgumentException {
		if (key == null) {
			throw new IllegalArgumentException("key must not be null");
		}
		return keyValuePairs.get(key);
	}

	/**
	 * Returns the value for the given key or the default value if the key is not present.
	 *
	 * @param key          the key
	 * @param defaultValue the default value which is returned if the key is not present
	 * @return value for the given key or the default value if the key is not present
	 */
	public V getValueOrDefault(K key, V defaultValue) {
		return keyValuePairs.getOrDefault(key, defaultValue);
	}

	/**
	 * Checks if the region contains the given key.
	 *
	 * @param key the key
	 * @return true if the region contains the key, false otherwise
	 */
	public boolean containsKey(K key) {
		return keyValuePairs.containsKey(key);
	}

	/**
	 * Checks if the region contains the given value.
	 *
	 * @param value the value
	 * @return true if the region contains the value, false otherwise
	 */
	public boolean containsValue(V value) {
		return keyValuePairs.containsValue(value);
	}

	/**
	 * Returns the number of key-value pairs in the region.
	 *
	 * @return the number of key-value pairs
	 */
	public int size() {
		return keyValuePairs.size();
	}

	/**
	 * Puts a new key-value pair into the region.
	 *
	 * @param key   the key
	 * @param value the value
	 * @return true if the key-value pair was added, false if the key already exists
	 */
	public boolean put(K key, V value) {
		return keyValuePairs.put(key, value) != null;
	}

	/**
	 * Removes the key-value pair with the given key from the region.
	 *
	 * @param key the key to remove
	 * @return true if the key-value pair was removed, false if the key does not exist
	 */
	public boolean remove(K key) {
		return keyValuePairs.remove(key) != null;
	}

	//region setter/getter

	/**
	 * Returns all key-value pairs of the region. (Copy of the original)
	 *
	 * @return all key-value pairs
	 */
	public Map<K, V> getKeyValuePairs() {
		return new java.util.HashMap<>(keyValuePairs);
	}

	/**
	 * Returns a list of all keys in the region.
	 *
	 * @return list of all keys
	 */
	public List<K> getKeys() {
		return keyValuePairs.keySet().stream().toList();
	}

	/**
	 * Checks if the region is empty.
	 *
	 * @return true if the region is empty, false otherwise
	 */
	public boolean isEmpty() {
		return keyValuePairs.isEmpty();
	}

	/**
	 * Returns the name of the region.
	 *
	 * @return the name of the region
	 */
	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof KeyValueRegion iniRegion)) return false;

		if (!Objects.equals(name, iniRegion.name)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		return keyValuePairs.equals(iniRegion.keyValuePairs);
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + keyValuePairs.hashCode();
		return result;
	}
}
