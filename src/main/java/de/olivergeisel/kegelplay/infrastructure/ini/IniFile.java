package de.olivergeisel.kegelplay.infrastructure.ini;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class IniFile {

	private static final System.Logger LOGGER = System.getLogger(IniFile.class.getName());

	private String          name;
	private List<IniRegion> regions = new java.util.ArrayList<>();

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

	public IniFile(Path path) throws IOException, IniFileException, IllegalArgumentException {
		this(path, "ISO-8859-1");
	}

	public IniFile(String path) throws IOException, IniFileException, IllegalArgumentException {
		this(Path.of(path));
	}

	/**
	 * Create IniFile with multiple regions
	 *
	 * @param firstLine first line of the ini file
	 * @param lines all lines of the ini file
	 *  @throws IniFileException if the first line is not a region
	 */
	private void createWithMultipleRegions(String firstLine, String[] lines)
			throws IniFileException {
		// iterate threw lines
		String regionName;
		List<String> regionLines = new LinkedList<>();
		if (!firstLine.startsWith("[") || !firstLine.endsWith("]")) {
			throw new IniFileException("First line is not a region in ini file");
		}
		IniRegion currentRegion = new IniRegion("", new String[0]);
		for (var line : lines) {
			var trimmedLine = line.trim();
			if (trimmedLine.isBlank()) {
				continue;
			}
			if (trimmedLine.startsWith("[") && trimmedLine.endsWith("]")) { // new region
				currentRegion.setLines(regionLines.toArray(String[]::new)); // finish old region
				// add new region to regions
				regionName = trimmedLine.substring(1, trimmedLine.length() - 1);
				regionLines = new java.util.ArrayList<>();
				currentRegion = new IniRegion(regionName, regionLines.toArray(String[]::new));
				this.regions.add(currentRegion);
			} else {
				if (!trimmedLine.contains("=")) {
					LOGGER.log(System.Logger.Level.INFO, STR."invalid pair '\{line}' in ini file");
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

	private void creatWithOneRegion(String[] lines) {
		var map = new java.util.HashMap<String, String>();
		for (var line : lines) {
			var keyValue = line.split("=");
			if (keyValue.length != 2) {
				throw new IniFileException("invalid ini file");
			}
			map.put(keyValue[0].trim(), keyValue[1].trim());
		}
		this.regions.add(new IniRegion("", lines, map));
	}

	public IniRegion getRegion(String name) {
		return regions.stream().filter(r -> r.getName().equals(name)).findFirst().orElse(null);
	}

	public IniRegion getRegion(int index) {
		return regions.get(index);
	}

	//region setter/getter
	public String getName() {
		return name;
	}

	public List<IniRegion> getRegions() {
		return Collections.unmodifiableList(regions);
	}

	public int getRegionCount() {
		return regions.size();
	}
//endregion

}



