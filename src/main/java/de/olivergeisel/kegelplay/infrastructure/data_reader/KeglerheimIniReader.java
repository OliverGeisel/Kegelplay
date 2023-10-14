package de.olivergeisel.kegelplay.infrastructure.data_reader;

import java.io.IOException;
import java.nio.file.Path;

public class KeglerheimIniReader {

	private Path iniFile;

	public KeglerheimIniReader(Path iniFile) {
		this.iniFile = iniFile;
	}

	public static IniFile read(Path iniFile) throws IOException {
		return new IniFile(iniFile);
	}

	public IniFile readIniFile() throws IOException {
		return new IniFile(iniFile);
	}

	public void updatePath(Path path) {
		this.iniFile = path;
	}

	public IniFile readAgain() throws IOException {
		return new IniFile(iniFile);
	}


}
