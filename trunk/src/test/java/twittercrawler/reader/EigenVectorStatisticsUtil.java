package twittercrawler.reader;

import java.util.HashMap;
import java.util.Map;

import twittercrawler.io.utils.IntIntStreamReader;
import twittercrawler.io.utils.IntIntStreamReader.IntIntPair;
import twittercrawler.utils.Config;


public class EigenVectorStatisticsUtil {

	public static void main(String[] args) throws Exception {

		Config config = new Config();

		IntIntStreamReader reader = new IntIntStreamReader(config.getEVCentralityPath());

		// key = centrality value
		// value = counts
		Map<Integer, Long> distribution = new HashMap<Integer, Long>();

		while (true) {
			IntIntPair pair = reader.read();
			if (pair == null) {
				break;
			}

			// int1 = userId
			// int2 = (ev) centrality / degree --> keys of the distribution map

			int degree = pair.getInt2();

			if (distribution.containsKey(degree)) {
				Long counts = distribution.get(degree);
				distribution.put(degree, counts + 1);
			} else {
				distribution.put(degree, 1l);
			}
		}

		for (Integer centralityValue : distribution.keySet()) {
			System.out.println(centralityValue + "\t" + distribution.get(centralityValue));
		}
	}
}
