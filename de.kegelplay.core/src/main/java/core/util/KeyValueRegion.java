package core.util;


import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a collection of key-value pairs. The region has a specific name;
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class KeyValueRegion<K, V> {

	private final String    name;
	private final Map<K, V> keyValuePairs;


	public KeyValueRegion(String name, Map<K, V> keyValues) {
		this.name = name;
		this.keyValuePairs = keyValues;
	}

	public V getValue(K key) {
		return keyValuePairs.get(key);
	}

	public boolean containsKey(K key) {
		return keyValuePairs.containsKey(key);
	}

	public boolean containsValue(V value) {
		return keyValuePairs.containsValue(value);
	}

	public int size() {
		return keyValuePairs.size();
	}

	public boolean put(K key, V value) {
		return keyValuePairs.put(key, value) != null;
	}

	public boolean remove(K key) {
		return keyValuePairs.remove(key) != null;
	}

	//region setter/getter
	public Map<K, V> getKeyValuePairs() {
		return new java.util.HashMap<>(keyValuePairs);
	}

	public List<K> getKeys() {
		return keyValuePairs.keySet().stream().toList();
	}

	public boolean isEmpty() {
		return keyValuePairs.isEmpty();
	}

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
