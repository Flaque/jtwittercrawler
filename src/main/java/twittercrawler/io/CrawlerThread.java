package twittercrawler.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.IDs;
import twitter4j.TwitterException;
import twittercrawler.utils.StringUtils;
import twittercrawler.utils.TwitterClient;

public class CrawlerThread extends Thread {
	final static Logger LOG = LoggerFactory.getLogger(CrawlerThread.class);

	private TwitterClient client = new TwitterClient();

	private UserWriter userWriter;
	private EdgeWriter edgeWriter;

	public CrawlerThread(UserWriter userWriter, EdgeWriter edgeWriter) {
		this.userWriter = userWriter;
		this.edgeWriter = edgeWriter;
		
	}

	public void run() {

		while (true) {

			int userId = 0;
			try {

				userId = userWriter.getToFetch();

				IDs friends = client.getFriendsIDs(userId);

				edgeWriter.addEdge(userId, friends.getIDs());
				userWriter.setFetched(userId, false, friends.getIDs());

			}
			catch (TwitterException e) {

				if (e.getMessage().indexOf("401:Authentication credentials were missing") != -1) {
					userWriter.setFetched(userId, true, null);

				} else if (e.getMessage().indexOf("502:Twitter is down or being upgraded") != -1) {
					LOG.debug("cthread2 502:Twitter is down or being upgraded");
					userWriter.setFailedFetched(userId);

				} else {
					LOG.warn("cthread1 " + StringUtils.print(e, false));
					userWriter.setFailedFetched(userId);
				}
			}
			catch (Exception e) {
				LOG.warn("cthread 3" + StringUtils.print(e, false));
			}
		}
	}
}
