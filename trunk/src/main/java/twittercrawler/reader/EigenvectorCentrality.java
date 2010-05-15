package twittercrawler.reader;

import static twittercrawler.utils.Log.info;
import twittercrawler.io.utils.IntIntStreamWriter;
import twittercrawler.utils.Config;
import twittercrawler.utils.GarbageCollector;
import cern.colt.list.IntArrayList;
import cern.colt.map.AbstractIntIntMap;
import cern.colt.map.OpenIntIntHashMap;

public class EigenvectorCentrality {

	public static void main(String[] args) throws Exception {

		Config config = new Config();

		AbstractIntIntMap users = new OpenIntIntHashMap();
		long start = System.currentTimeMillis() / 1000;

		EdgesIterator edgesIterator = new EdgesIterator(config);

		while (edgesIterator.hasNext()) {
			EdgeDTO edge = edgesIterator.next();

			int key = edge.getTo();

			if (users.containsKey(key)) {
				int count = users.get(key);
				users.put(key, count + 1);
			} else {
				users.put(key, 1);
			}
		}
		info("finished indegree\t" + ((System.currentTimeMillis() / 1000) - start));

		GarbageCollector.runGC();

		// now iterate
		AbstractIntIntMap in = users;
		AbstractIntIntMap out = new OpenIntIntHashMap(in.size());
		for (int i = 0; i < 100000; i++) {// forever...

			edgesIterator = new EdgesIterator(config);

			while (edgesIterator.hasNext()) {
				EdgeDTO edge = edgesIterator.next();

				int key = edge.getTo();

				if (in.containsKey(key)) {
					if (out.containsKey(key)) {
						int count = out.get(key);
						out.put(key, count + in.get(key));
					} else {
						out.put(key, in.get(key));
					}

				} else {
					if (out.containsKey(key)) {
						int count = out.get(key);
						out.put(key, count + 1);
					} else {
						out.put(key, 1);
					}
				}
			}

			// dump
			IntIntStreamWriter dump = new IntIntStreamWriter(config.getEVCentralityPath(i));
			IntArrayList keys = out.keys();
			for (int j = 0; j < keys.size(); j++) {
				int user = keys.get(j);
				int degree = users.get(user);
				dump.write(user, degree);
			}
			dump.close();

			info("finished round\t" + i + "\t" + ((System.currentTimeMillis() / 1000) - start));
		}
	}
}
