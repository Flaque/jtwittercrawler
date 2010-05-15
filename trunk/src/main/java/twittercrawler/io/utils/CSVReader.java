package twittercrawler.io.utils;

import static twittercrawler.utils.Log.debug;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;

public class CSVReader {

	private CsvReader csvReader;

	public CSVReader(String file) throws FileNotFoundException {
		debug("reading new csv at " + file);

		csvReader = new CsvReader(file);
		csvReader.setEscapeMode(CsvWriter.ESCAPE_MODE_BACKSLASH);
	}

	public String[] read() throws IOException {
		return csvReader.getValues();
	}

	public void close() throws IOException {
		debug("closing stream ");
	}
}
