package twittercrawler.io;

import static twittercrawler.utils.Log.info;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twittercrawler.io.utils.IntIntStreamWriter;
import twittercrawler.utils.Config;


public class EdgeWriter {
	final static Logger LOG = LoggerFactory.getLogger(EdgeWriter.class);

	public final static int RECORDS_PER_FILE = 10000000; // 10 mio

	private int currentSerie = 1;

	//
	// write to files

	private IntIntStreamWriter edgeStream;
	private UserCrawlerStatuses ioUserDAO;
	private Config config;

	public EdgeWriter(Config config, UserCrawlerStatuses ioUserDAO, int currentSerie) throws FileNotFoundException {
		this.ioUserDAO = ioUserDAO;
		this.currentSerie = currentSerie;
		edgeStream = new IntIntStreamWriter(config.getEdgesPath(currentSerie));
		this.config = config;
	}

	private long cnt = 0;

	public synchronized void addEdge(int from, int[] followees) {
		try {

			for (int to : followees) {
				edgeStream.write(from, to);

				if (++cnt % RECORDS_PER_FILE == 0) {
					createNewFile();
				}
			}
		}
		catch (Exception e) {
			LOG.error("wtf " + e);
		}
	}

	//
	// manage files
	long start = System.currentTimeMillis() / 1000;

	private void createNewFile() throws IOException {

		// dump user files every 5 run
		if (currentSerie % 5 == 0) {
			ioUserDAO.dump(currentSerie);
		}

		edgeStream.close();

		currentSerie++;
		String path = config.getEdgesPath(currentSerie);
		edgeStream = new IntIntStreamWriter(path);
		info("new file created at " + path + "\t" + cnt + "\t" + ((System.currentTimeMillis() / 1000) - start) + "\t");
	}
}
