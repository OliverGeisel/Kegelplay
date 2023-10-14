package de.olivergeisel.kegelplay.infrastructure.data_reader;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CSVFile {

	private final Path           path;
	private       String[]       header;
	private       List<String[]> data;

	public CSVFile(Path file) {
		this.path = file;
		List<String[]> rawData;
		try {
			rawData = read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		header = rawData.get(0);
		data.remove(0);
		this.data = rawData;
	}

	public List<String[]> read() throws IOException {
		try (var csvReader = new CSVReader(new FileReader(path.toFile()))) {
			return csvReader.readAll();
		} catch (Exception e) {
			throw new IOException("invalid data file");
		}
	}

	public void reread() throws IOException {
		List<String[]> rawData;
		try {
			rawData = read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		header = rawData.get(0);
		data.remove(0);
		this.data = rawData;
	}

	//region setter/getter
	public List<String[]> getData() {
		return data;
	}

	public String[] getHeader() {
		return header;
	}
//endregion
}
