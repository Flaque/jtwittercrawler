package twittercrawler.reader;

import static twittercrawler.utils.Log.info;
import twittercrawler.io.EdgeWriter;
import twittercrawler.utils.Config;
import cern.colt.list.IntArrayList;
import cern.colt.map.AbstractIntIntMap;
import cern.colt.map.OpenIntIntHashMap;

public class InDegree {

	public static void main(String[] args) throws Exception {

		AbstractIntIntMap users = new OpenIntIntHashMap();
		long start = System.currentTimeMillis() / 1000;

		EdgesIterator edgesIterator = new EdgesIterator(new Config());

		int cnt = 0;
		while (edgesIterator.hasNext()) {
			EdgeDTO edge = edgesIterator.next();

			int key = edge.getTo();

			if (users.containsKey(key)) {
				int count = users.get(key);
				users.put(key, count + 1);
			} else {
				users.put(key, 1);
			}

			if (++cnt % EdgeWriter.RECORDS_PER_FILE == 0) {
				info("" + (cnt - 1) + "\t" + ((System.currentTimeMillis() / 1000) - start) + "\t");
			}
		}
		info("finished\t" + ((System.currentTimeMillis() / 1000) - start) + "\tusers:" + users.size());

		// print
		if (true) {
			IntArrayList keys = users.keys();
			for (int i = 0; i < keys.size(); i++) {

				int user = keys.get(i);
				int indegree = users.get(user);
				System.out.println(user + "\t" + indegree);
			}
		}
	}
}
