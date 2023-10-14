package de.olivergeisel.kegelplay.infrastructure.data_reader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class IniFile {

	private String          name;
	private List<IniRegion> regions = new java.util.ArrayList<>();

	public IniFile(Path path) throws IOException, IniFileException, IllegalArgumentException {
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
		try (var reader = new java.io.BufferedReader(new java.io.FileReader(file))) {
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

	private void createWithMultipleRegions(String firstLine, String[] lines) {
		// iterate threw lines
		var regionName = firstLine.substring(1, firstLine.length() - 1);
		var map = new java.util.HashMap<String, String>();
		var regionLines = new java.util.ArrayList<String>();

		for (var line : lines) {
			var trimmedLine = line.trim();
			if (trimmedLine.startsWith("[") && trimmedLine.endsWith("]")) {
				this.regions.add(new IniRegion(regionName, regionLines.toArray(String[]::new), map));
				regionName = trimmedLine.substring(1, trimmedLine.length() - 1);
				map = new java.util.HashMap<>();
				regionLines = new java.util.ArrayList<>();
			} else {
				var keyValue = trimmedLine.split("=");
				if (keyValue.length != 2) {
					throw new IniFileException("invalid ini file");
				}
				map.put(keyValue[0].trim(), keyValue[1].trim());
				regionLines.add(line);
			}
		}
		this.regions.add(new IniRegion(regionName, regionLines.toArray(String[]::new), map));
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



