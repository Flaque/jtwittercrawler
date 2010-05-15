package twittercrawler.io.utils;

import static twittercrawler.utils.Log.debug;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class IntIntStreamReader {

	private DataInputStream dis;

	public IntIntStreamReader(String file) throws FileNotFoundException {
		debug("reading new stream at " + file);
		dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
	}

	public IntIntPair read() throws IOException {

		try { // exception deals catches EOF
			return new IntIntPair(dis.readInt(), dis.readInt());
		}
		catch (EOFException eof) { // end of Stream, try to load a new one
			try {
				dis.close();
			}
			catch (IOException e) {
				throw new RuntimeException(e);// not normal
			}
			debug("end of stream");
			return null;
		}
		catch (Throwable anything) {
			throw new RuntimeException(anything);// not normal
		}
	}

	public void close() throws IOException {
		dis.close();
		debug("closing stream ");
	}

	public static class IntIntPair {

		private int int1;
		private int int2;

		public IntIntPair(int int1, int int2) {
			this.int1 = int1;
			this.int2 = int2;
		}

		public int getInt1() {
			return int1;
		}

		public int getInt2() {
			return int2;
		}

		@Override
		public String toString() {
			return int1 + ":" + int2;
		}
	}
}
