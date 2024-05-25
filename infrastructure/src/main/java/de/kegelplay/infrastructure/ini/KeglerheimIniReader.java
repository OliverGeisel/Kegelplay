package de.kegelplay.infrastructure.ini;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Reader for an {@link IniFile} of the program used in the Keglerheim.
 *
 * @author Oliver Geisel
 * @version 1.0.0
 * @see IniFile
 * @since 1.0.0
 */
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
