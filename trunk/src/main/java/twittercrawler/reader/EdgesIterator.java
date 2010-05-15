package twittercrawler.reader;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twittercrawler.utils.Config;


/**
 * Utility {@link Iterator} to load edges from the text files
 * @author ren
 */
public class EdgesIterator implements Iterator<EdgeDTO> {
	final static Logger LOG = LoggerFactory.getLogger(EdgesIterator.class);

	private Config config;

	private int currentSerie = 1;
	private long currentEdge = 0;
	private DataInputStream dis;

	public EdgesIterator(Config config) throws IOException {
		this(config, 1);
	}

	public EdgesIterator(Config config, int startSerie) throws IOException {
		currentSerie = startSerie;
		this.config = config;
		if (!load(startSerie)) {
			throw new IOException("no edges found at " + config.getEdgesPath(startSerie));
		}
	}

	private EdgeDTO buffer = null;

	public boolean hasNext() {

		try { // exception deals catches EOF
			buffer = new EdgeDTO(dis.readInt(), dis.readInt());
			return true;
		}
		catch (EOFException eof) { // end of Stream, try to load a new one
			try {
				dis.close();
			}
			catch (IOException e) {
				throw new RuntimeException(e);// not normal
			}
			if (load(++currentSerie)) {
				return true;
			}
			LOG.debug("no hasNext() --> all edges have been read: " + currentEdge);
			return false;
		}
		catch (Throwable anything) {
			throw new RuntimeException(anything);// not normal
		}
	}

	public EdgeDTO next() {
		currentEdge++;
		return buffer;
	}

	public void remove() {
		throw new RuntimeException("unimplemented");
	}

	private boolean load(int serie) {
		try {
			String file = config.getEdgesPath(serie);
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			LOG.debug("loaded serie " + serie + " from " + file + ", edgesCount: " + currentEdge);
			return true;
		}
		catch (FileNotFoundException e) {
			return false;
		}
	}
}
