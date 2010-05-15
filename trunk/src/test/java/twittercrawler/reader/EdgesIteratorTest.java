package twittercrawler.reader;

import static twittercrawler.utils.Log.info;

import org.junit.Test;

import twittercrawler.reader.EdgeDTO;
import twittercrawler.reader.EdgesIterator;
import twittercrawler.utils.Config;


public class EdgesIteratorTest {
	@Test
	public void test() throws Exception {

		EdgesIterator edgesIterator = new EdgesIterator(new Config());

		int cnt = 0;
		while (edgesIterator.hasNext()) {
			EdgeDTO edge = edgesIterator.next();
			if (cnt++ % 100000 == 0) {
				info("" + cnt);
			}
		}

	}
}
