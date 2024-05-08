package de.olivergeisel.kegelplay.infrastructure.ini;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * Represents an .ini file. An ini file is a file that contains regions. Each region is a block of lines that are
 * separated by a name. The region contains key-value pairs that are stored in a map.
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
 * @see IniRegion
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class IniFile {

	private static final System.Logger LOGGER = System.getLogger(IniFile.class.getName());

	private String          name;
	private List<IniRegion> regions = new java.util.ArrayList<>();


	/**
	 * Creates a new IniFile object from a Path.
	 *
	 * @param path The path to the file.
	 * @param encoding The encoding of the file.
	 * @throws IOException If the file could not be read.
	 * @throws IniFileException If the file is not a valid ini file.
	 * @throws IllegalArgumentException If the path is null, the file does not exist or the file does not end with .ini.
	 */
	public IniFile(Path path, String encoding) throws IOException, IniFileException, IllegalArgumentException {
		if (path == null) {
			throw new IllegalArgumentException("path must not be null");
		}
		var file = path.toFile();
		if (!file.getName().endsWith(".ini")) {
			throw new IllegalArgumentException("file must end with .ini");
		}
		if (!file.exists()) {
			throw new IllegalArgumentException("file must exist");
		}
		this.name = path.getFileName().toString();

		String[] lines;
		try (var reader = new BufferedReader(new FileReader(file, Charset.forName(encoding)))) {
			lines = reader.lines().toArray(String[]::new);
		} catch (Exception e) {
			throw new IOException("could not read file", e);
		}
		// iterate threw regions
		if (lines.length == 0) {
			throw new IniFileException("Ini file is empty");
		}
		var firstLine = lines[0].trim();
		if (!firstLine.startsWith("[") || !firstLine.endsWith("]")) {
			creatWithOneRegion(lines);
		} else {
			createWithMultipleRegions(firstLine, lines);
		}
	}

	/**
	 * Creates a new IniFile object from a Path with default encoding ISO-8859-1.
	 *
	 * @param path The path to the file.
	 * @throws IOException If the file could not be read.
	 * @throws IniFileException If the file is not a valid ini file.
	 * @throws IllegalArgumentException If the path is null, the file does not exist or the file does not end with .ini.
	 */
	public IniFile(Path path) throws IOException, IniFileException, IllegalArgumentException {
		this(path, "ISO-8859-1");
	}

	public IniFile(String path) throws IOException, IniFileException, IllegalArgumentException {
		this(Path.of(path));
	}

	/**
	 * Create IniFile with multiple regions
	 *
	 * @param firstLine first line of the ini file. Is the name of the first region
	 * @param lines all lines of the ini file
	 *  @throws IniFileException if the first line is not the name of a region
	 */
	private void createWithMultipleRegions(String firstLine, String[] lines) throws IniFileException {
		String regionName;
		List<String> regionLines = new LinkedList<>();
		if (!firstLine.startsWith("[") || !firstLine.endsWith("]")) {
			throw new IniFileException("First line is not a region in ini file");
		}
		IniRegion currentRegion = new IniRegion("", new String[0]);
		for (var line : lines) {
			var trimmedLine = line.trim();
			if (trimmedLine.startsWith("[") && trimmedLine.endsWith("]")) { // new region
				currentRegion.setLines(regionLines.toArray(String[]::new)); // finish old region
				// add new region to regions
				regionName = trimmedLine.substring(1, trimmedLine.length() - 1);
				regionLines = new java.util.ArrayList<>();
				currentRegion = new IniRegion(regionName, regionLines.toArray(String[]::new));
				regions.add(currentRegion);
			} else {
				if (!trimmedLine.contains("=")) {
					LOGGER.log(System.Logger.Level.INFO, "invalid pair {line} in ini file");
					continue;
				}
				var keyValue = trimmedLine.split("=");
				var key = keyValue[0].trim();
				var value = keyValue.length != 2 ? "" : keyValue[1].trim();
				currentRegion.put(key, value);
				regionLines.add(line);
			}
		}
	}

	/**
	 * Create IniFile with only one region ({@link IniRegion})
	 * The given lines are the lines of the region. The region has no name. If you want the name of the region, you
	 * get the empty string.
	 *
	 * @see IniRegion
	 * @param lines all lines of the ini region
	 * @throws IniFileException if one key value pair is not valid
	 */
	private void creatWithOneRegion(String[] lines) {
		var map = new java.util.HashMap<String, String>();
		for (var line : lines) {
			var keyValue = line.split("=");
			if (keyValue.length != 2) {
				throw new IniFileException(STR."invalid ini file! Missing value or key in line: \{line}");
			}
			map.put(keyValue[0].trim(), keyValue[1].trim());
		}
		this.regions.add(new IniRegion("", lines, map));
	}

	public IniRegion getRegion(String name) {
		return regions.stream().filter(r -> r.getName().equals(name)).findFirst().orElse(null);
	}

	/**
	 * Get a region by its index.
	 *
	 * @param index The index of the region.
	 * @return The region at the index.
	 * @throws IndexOutOfBoundsException If the index is out of bounds.
	 */
	public IniRegion getRegion(int index) throws IndexOutOfBoundsException {
		return regions.get(index);
	}

	//region setter/getter

	/**
	 * Get the name of the ini file.
	 *
	 * @return The name of the ini file.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get all regions of the ini file.
	 *
	 * @return An unmodifiable list of all regions.
	 */
	public List<IniRegion> getRegions() {
		return Collections.unmodifiableList(regions);
	}

	/**
	 * Get the number of regions in the ini file.
	 *
	 * @return The number of regions.
	 */
	public int getRegionCount() {
		return regions.size();
	}
//endregion

}



