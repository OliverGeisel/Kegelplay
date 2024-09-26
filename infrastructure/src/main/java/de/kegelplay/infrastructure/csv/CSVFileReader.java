package de.kegelplay.infrastructure.csv;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * Reads a CSV file and stores the data in a list of String arrays.
 * There is the assumption that the first row of the CSV file is the header.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author Oliver Geisel
 */
public class CSVFileReader {

	private final Path           path;
	private       String[]       header;
	private       List<String[]> data;

	public CSVFileReader(Path file) {
		this.path = file;
		List<String[]> rawData;
		try {
			rawData = read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		header = rawData.getFirst();
		rawData.removeFirst();
		this.data = rawData;
	}

	/**
	 * Read the data from the file.
	 * @return List of String arrays, each array represents a row in the CSV file.
	 * @throws IOException if the file is not readable
	 */
	public List<String[]> read() throws IOException {
		try (var csvReader = new CSVReader(new FileReader(path.toFile()))) {
			return csvReader.readAll();
		} catch (Exception e) {
			throw new IOException("invalid data file");
		}
	}

	public void reread() {
		List<String[]> rawData;
		try {
			rawData = read();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		header = rawData.getFirst();
		rawData.removeFirst();
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
