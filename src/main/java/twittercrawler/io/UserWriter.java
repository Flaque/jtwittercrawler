package twittercrawler.io;

import java.io.IOException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.RateLimitStatus;
import twitter4j.TwitterException;
import twittercrawler.utils.StringUtils;
import twittercrawler.utils.TwitterClient;

public class UserWriter {
	final static Logger LOG = LoggerFactory.getLogger(UserWriter.class);

	public final static int USERS_PER_FETCHES = 300;

	private UserCrawlerStatuses userDAO;
	TwitterClient twitterClient = new TwitterClient();

	private BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(USERS_PER_FETCHES * 2);
	private Queue<Integer> outThereQueue = new ConcurrentLinkedQueue<Integer>();

	public UserWriter(UserCrawlerStatuses userDAO) throws IOException {
		this.userDAO = userDAO;
		MyFetcherThread fetcherThread = new MyFetcherThread();// twitterDAO, queue, outThereQueue);
		fetcherThread.start();
	}

	public synchronized int getToFetch() throws InterruptedException {
		return queue.take();
	}

	public synchronized void setFetched(int id, boolean privte, int[] newFriends) {
		outThereQueue.remove(id);// welcome home
		userDAO.setFetched(id, privte, newFriends);
	}

	public synchronized void setFailedFetched(int id) {
		outThereQueue.remove(id);// try again!
	}

	class MyFetcherThread extends Thread {

		public void run() {

			while (true) {
				if (queue.isEmpty()) {
					try {

						String info = userDAO.logInfo();

						try {
							while (true) {
								RateLimitStatus rate = twitterClient.getRateLimitStatus();
								int remaining = rate.getRemainingHits();
								System.out.println(info + "\t" + remaining);

								if (remaining < (USERS_PER_FETCHES + 100)) {
									LOG.info("TOO low on API, waiting ");
									Thread.sleep(2 * 60 * 1000);
								} else {
									break;// free!
								}
							}
						}
						catch (TwitterException e) {

							if (e.getMessage().indexOf("401:Authentication credentials were missing") != -1) {

							} else if (e.getMessage().indexOf("502:Twitter is down or being upgraded") != -1) {

							} else {
								LOG.warn("picker1: " + StringUtils.print(e, false));
							}
						}
						catch (Exception e) {
							LOG.warn("picker2: " + StringUtils.print(e, false));
						}

						// get next users to crawl and skip them if outThere beeing fetched
						List<Integer> nextTinyUserCrawls = userDAO.getNextTinyUserCrawls();
						for (Integer userId : nextTinyUserCrawls) {

							if (!outThereQueue.contains(userId)) {
								queue.add(userId);
								outThereQueue.add(userId);
							}
						}
					}
					catch (Exception e) {
						LOG.debug("ooouuups! " + StringUtils.print(e, false));
					}
				}
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.gc();
			}
		}
	}
}