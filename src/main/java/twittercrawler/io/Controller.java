package twittercrawler.io;

import twitter4j.IDs;
import twittercrawler.utils.Config;
import twittercrawler.utils.TwitterClient;

/**
 * Main controller class for crawler
 * @author renaud@apache.org
 */
public class Controller {

	static int nrThreads = 15;

	public static void main(String[] args) throws Exception {
		Thread.sleep(1000);

		UserCrawlerStatuses ioUserDAO = new UserCrawlerStatuses();
		// uses an older crawl if recovering from a crash
		// UserCrawlerStatuses ioUserDAO = new UserCrawlerStatuses(serieNr);

		// seed
		IDs friends = new TwitterClient().getFriendsIDs(50043);
		for (int userId : friends.getIDs()) {
			ioUserDAO.seed(userId);
		}

		ioUserDAO.logHeaders();

		UserWriter userWriter = new UserWriter(ioUserDAO);
		EdgeWriter edgeWriter = new EdgeWriter(new Config(), ioUserDAO, 1);

		CrawlerThread[] crawlerThreads = new CrawlerThread[nrThreads];
		for (int i = 0; i < nrThreads; i++) {
			crawlerThreads[i] = new CrawlerThread(userWriter, edgeWriter);
			crawlerThreads[i].start();
		}

		while (true) {
			Thread.sleep(171717);
		}
	}
}
