package twittercrawler.io.utils;

import static twittercrawler.utils.Log.debug;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class IntIntStreamWriter {

	private DataOutputStream dos;

	public IntIntStreamWriter(String file) throws FileNotFoundException {
		debug("opening new stream at " + file);
		dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	}

	public void write(int from, int to) throws IOException {
		dos.writeInt(from);
		dos.writeInt(to);
	}

	public void close() throws IOException {
		dos.flush();
		dos.close();
		debug("closing stream ");
	}
}
