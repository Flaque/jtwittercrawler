package twittercrawler.io;

import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import twittercrawler.io.UserCrawlerStatuses;
import twittercrawler.utils.Config;


public class UserDAOTest {

	@Test
	public void dumpNLoad() throws Exception {

		UserCrawlerStatuses userDAO = new UserCrawlerStatuses();
		userDAO.seed(9);
		userDAO.seed(8);
		userDAO.seed(7);

		userDAO.dump(0);
		assertTrue(new File(Config.getPath() + "0/fetched.dat").exists());

		UserCrawlerStatuses userDAO2 = new UserCrawlerStatuses();
		userDAO2.load(0);
		String logInfo = userDAO2.logInfo();
		assertTrue(logInfo.indexOf(3 + "") != -1);
	}

}
