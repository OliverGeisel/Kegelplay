package de.kegelplay.infrastructure.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvService {

	public void parseCSV(String path) throws IOException, CsvException {
		CSVReader csvReader = new CSVReader(new FileReader(path));
		List<String[]> allData = csvReader.readAll();

	}

	public void update() {
	}
}
