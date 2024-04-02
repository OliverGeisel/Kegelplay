package de.olivergeisel.kegelplay.infrastructure.data_reader;

import de.olivergeisel.kegelplay.infrastructure.csv.CSVFileReader;
import de.olivergeisel.kegelplay.infrastructure.ini.IniFile;
import de.olivergeisel.kegelplay.infrastructure.ini.IniFileException;
import de.olivergeisel.kegelplay.infrastructure.ini.KeglerheimIniReader;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public class KeglerheimPlayerReader extends PlayerReader {

	private static final String PLAYER_GAME_FILE_NAME = "werte.csv";
	private static final String PLAYER_INFO_FILE_NAME = "spieler.ini";

	private final String  name;
	private final String  vorname;
	private final Path          teamDir;
	private       IniFile       infoFile;
	private       CSVFileReader dataFile;

	public KeglerheimPlayerReader(String name, String vorname, Path teamDir) {
		this.name = name;
		this.vorname = vorname;
		this.teamDir = teamDir;
		try {
			this.infoFile = KeglerheimIniReader.read(teamDir.resolve(PLAYER_INFO_FILE_NAME));
		} catch (Exception e) {
			throw new IniFileException("invalid ini file");
		}
	}

	public KeglerheimPlayerReader(Path playerDir) throws FileNotFoundException {
		var nameString = playerDir.toFile().getName();
		var nameParts = nameString.split(",");
		this.vorname = nameParts[1].trim();
		this.name = nameParts[0].trim();
		this.teamDir = playerDir.getParent();
		try {
			this.infoFile = KeglerheimIniReader.read(playerDir.resolve(PLAYER_INFO_FILE_NAME));
		} catch (Exception e) {
			throw new IniFileException("invalid ini file");
		}
		var dataPath = playerDir.resolve(PLAYER_GAME_FILE_NAME);
		try {
			this.dataFile = new CSVFileReader(dataPath);
		} catch (Exception e) {
			throw new RuntimeException("invalid data file");
		}
	}


}
