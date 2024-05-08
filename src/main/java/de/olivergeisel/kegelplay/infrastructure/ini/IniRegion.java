package de.olivergeisel.kegelplay.infrastructure.ini;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a region in an .ini file ({@link IniFile}). A region is a block of lines that are separated by a name.
 * The region contains key-value pairs that are stored in a map.
 * Each region has a name at the beginning of the block.
 * <p>
 * <b>Example:</b>
 * <pre>
 *     [RegionName]
 *     key1=value1
 *     key2=value
 *     ...
 * </pre>
 * </p>
 * @see IniFile
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class IniRegion {

	private final String              name;
	private final Map<String, String> keyValuePairs = new java.util.HashMap<>();
	private       String[]            lines;

	public IniRegion(String name, String[] lines) {
		this.name = name;
		this.lines = lines;
	}

	public IniRegion(String name, String[] lines, Map<String, String> map) {
		this.name = name;
		this.lines = lines;
		this.keyValuePairs.putAll(map);
	}

	public String getValue(String key) {
		return keyValuePairs.get(key);
	}

	public boolean containsKey(String key) {
		return keyValuePairs.containsKey(key);
	}

	public boolean containsValue(String value) {
		return keyValuePairs.containsValue(value);
	}

	public int size() {
		return keyValuePairs.size();
	}

	public boolean put(String key, String value) {
		return keyValuePairs.put(key, value) != null;
	}

	public boolean remove(String key) {
		return keyValuePairs.remove(key) != null;
	}

	//region setter/getter
	public Map<String, String> getKeyValuePairs() {
		return new java.util.HashMap<>(keyValuePairs);
	}

	public String[] getKeys() {
		return keyValuePairs.keySet().toArray(String[]::new);
	}

	public boolean isEmpty() {
		return keyValuePairs.isEmpty();
	}

	public String getName() {
		return name;
	}

	public String[] getLines() {
		return Arrays.copyOf(lines, lines.length);
	}

	public void setLines(String[] lines) {
		this.lines = Arrays.copyOf(lines, lines.length);
	}


//endregion


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof IniRegion iniRegion)) return false;

		if (!Objects.equals(name, iniRegion.name)) return false;
		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(lines, iniRegion.lines)) return false;
		return keyValuePairs.equals(iniRegion.keyValuePairs);
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + Arrays.hashCode(lines);
		result = 31 * result + keyValuePairs.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return STR."IniRegion{name='\{name}',keyValuePairs=\{keyValuePairs}, lines=\{lines.length}'}";
	}
}
