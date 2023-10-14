package de.olivergeisel.kegelplay.infrastructure.data_reader;

import java.util.Arrays;
import java.util.Map;

public class IniRegion {
	private final String              name;
	private final String[]            lines;
	private final Map<String, String> keyValuePairs = new java.util.HashMap<>();

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

	//region setter/getter
	public String getName() {
		return name;
	}

	public String[] getLines() {
		return Arrays.copyOf(lines, lines.length);
	}


//endregion
}
