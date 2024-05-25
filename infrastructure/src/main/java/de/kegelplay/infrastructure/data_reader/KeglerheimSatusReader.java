package de.kegelplay.infrastructure.data_reader;


import de.kegelplay.infrastructure.ini.IniFile;
import de.kegelplay.infrastructure.ini.KeglerheimIniReader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeglerheimSatusReader {

	private final List<String> teamNames;
	private       IniFile      iniFile;
	private       int          teamCount;
	private boolean aborted;
	private       boolean      finished;

	public KeglerheimSatusReader(Path iniFilePath) throws IOException {
		if (iniFilePath == null) {
			throw new IllegalArgumentException("IniFilePath must not be null");
		}
		if (!iniFilePath.toFile().exists()) {
			throw new IllegalArgumentException("IniFilePath must exist");
		}
		if (!iniFilePath.toFile().getName().equals("status.ini")) {
			throw new IllegalArgumentException("IniFilePath must be named 'status.ini'");
		}
		this.iniFile = KeglerheimIniReader.read(iniFilePath);
		var allgemeinRegion = iniFile.getRegion("Allgemein");
		this.aborted = allgemeinRegion.getValue("Abgebrochen").equals("1");
		this.finished = allgemeinRegion.getValue("Abgeschlossen").equals("1");

		var teamRegion = iniFile.getRegion("Mannschaften");
		teamCount = Integer.parseInt(teamRegion.getValue("Anzahl"));
		teamNames = new ArrayList<>(teamCount);
		for (int i = 0; i < teamCount; i++) {
			teamNames.add(teamRegion.getValue(STR."Mannschaft \{i}"));
		}
	}

	//region setter/getter
	public int getTeamCount() {
		return teamCount;
	}

	public boolean isAborted() {
		return aborted;
	}

	public boolean isFinished() {
		return finished;
	}

	public List<String> getTeamNames() {
		return Collections.unmodifiableList(teamNames);
	}
//endregion
}
