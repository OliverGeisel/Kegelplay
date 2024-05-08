package de.olivergeisel.kegelplay.infrastructure.ini;

import core.util.KeyValueRegion;

import java.util.Arrays;
import java.util.Map;

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
public class IniRegion extends KeyValueRegion<String, String> {


	private String[] lines;

	public IniRegion(String name, String[] lines) {
		super(name, new java.util.HashMap<>());
		this.lines = lines;
	}

	public IniRegion(String name, String[] lines, Map<String, String> map) {
		this(name, lines);
		this.getKeyValuePairs().putAll(map);
	}

//region setter/getter
	public String[] getLines() {
		return Arrays.copyOf(lines, lines.length);
	}

	public void setLines(String[] lines) {
		this.lines = Arrays.copyOf(lines, lines.length);
	}
//endregion


//endregion


	@Override
	public String toString() {
		return STR."IniRegion{name='\{getName()}',keyValuePairs=\{getKeyValuePairs()}, lines=\{lines.length}'}";
	}
}
