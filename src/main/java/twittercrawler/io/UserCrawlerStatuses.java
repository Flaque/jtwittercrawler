package twittercrawler.io;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twittercrawler.utils.Config;

/**
 * Holding in-memory {@link Set}s of users (fetched, unfetched, private)
 * @author ren
 */
public class UserCrawlerStatuses {
	final static Logger LOG = LoggerFactory.getLogger(UserCrawlerStatuses.class);

	TIntHashSet fetchedSet = new TIntHashSet();
	TIntHashSet unfetchedSet = new TIntHashSet();
	TIntHashSet privteSet = new TIntHashSet();

	/**
	 * use this to start from scratch
	 */
	public UserCrawlerStatuses() throws IOException {
	}

	/**
	 * use this to recover a server crash
	 * @param serie
	 * @throws IOException
	 */
	public UserCrawlerStatuses(int serie) throws IOException {
		load(serie);
	}

	public List<Integer> getNextTinyUserCrawls() {
		synchronized (this) {
			List<Integer> retrn = new ArrayList<Integer>();

			// add
			int i = 0;
			TIntIterator iterator = unfetchedSet.iterator();
			while (iterator.hasNext() && i++ < UserWriter.USERS_PER_FETCHES) {
				retrn.add(iterator.next());
			}
			// remove
			for (int toRemove : retrn) {
				unfetchedSet.remove(toRemove);
			}
			return retrn;
		}
	}

	public void setFetched(int id, boolean privte, int[] newFriends) {
		synchronized (this) {
			try {
				unfetchedSet.remove(id);
				fetchedSet.add(id);
				if (privte) {
					privteSet.add(id);
				}
				if (newFriends != null) {

					for (int newFriend : newFriends) {

						if (!fetchedSet.contains(newFriend)) { // if not fetched yet
							unfetchedSet.add(newFriend); // try to add to unfetched
						}
					}
				}
			}
			catch (Exception e) {
				LOG.error("unexpected 12", e);
			}
		}
	}

	/** NOT SYNCHRONIZED!!!!!!!!!!!!!!!!!!!!! */
	public void seed(int... seeds) {
		for (int seed : seeds) {
			unfetchedSet.add(seed);
		}
	}

	// log code //////////////////////////////////////////////////////

	static final String D = "\t";
	static long start = System.currentTimeMillis();
	static long runningTime = System.currentTimeMillis();
	static int runningFetched = 0;

	public String logInfo() {
		synchronized (this) {

			try {

				int total = fetchedSet.size() + unfetchedSet.size();
				int totalFetched = fetchedSet.size();

				int fetchedInInterval = totalFetched - runningFetched;
				long now = System.currentTimeMillis();
				long startedSince = (now - start) / 1000;
				long intervalInSeconds = (now - runningTime) / 1000;
				int rate = (int) (fetchedInInterval / (intervalInSeconds + 0d));
				runningFetched = totalFetched;
				runningTime = now;
				return "---" + D + startedSince + D + fetchedInInterval + D + D + rate + D + totalFetched + D + D
						+ total;

			}
			catch (Exception e) {
				LOG.error("unexpected 14", e);
			}
			return "";
		}
	}

	public void logHeaders() {
		System.out.println("---" + D + "since" + D + "ftchdIntvl" + D + "rate" + D + "totFtchd" + D + "totTot" + D
				+ "APIremain");
	}

	// dump / load code //////////////////////////////////////////////////////

	public void dump(int serie) {
		synchronized (this) {
			try {
				String dir = Config.getPath() + serie;
				new File(dir).mkdirs();
				dump(fetchedSet, dir + "/fetched.dat");
				dump(unfetchedSet, dir + "/unfetched.dat");
				dump(privteSet, dir + "/privte.dat");
			}
			catch (Exception e) {
				LOG.error("unexpected 14", e);
			}
		}
	}

	private void dump(TIntHashSet set, String file) throws IOException {
		DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
		TIntIterator it = set.iterator();
		while (it.hasNext()) {
			dos.writeInt(it.next());
		}
		dos.close();
	}

	void load(int serie) throws IOException {
		synchronized (this) {
			LOG.info("loading serie " + serie + "...");
			String dir = Config.getPath() + serie;
			load(fetchedSet, dir + "/fetched.dat");
			load(unfetchedSet, dir + "/unfetched.dat");
			load(privteSet, dir + "/privte.dat");
			LOG.info("loaded serie " + serie);
		}
	}

	private void load(TIntHashSet set, String file) throws IOException {

		DataInputStream dis = null;
		try {
			dis = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			while (true) { // exception deals catches EOF
				set.add(dis.readInt());
			}
		}
		catch (EOFException eof) {
			// Normal program termination
		}
		catch (Throwable anything) {
			throw new IOException(anything);// not normal
		}
		finally {
			if (dis != null) {
				try {
					dis.close();
				}
				catch (IOException anything) {
					throw new IOException(anything);// not normal
				}
			}
		}
	}
}
