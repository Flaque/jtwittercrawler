package twittercrawler.utils;

public class GarbageCollector {

	private static final Runtime s_runtime = Runtime.getRuntime();

	// this is our way of requesting garbage collection to be run:
	// [how aggressive it is depends on the JVM to a large degree, but
	// it is almost always better than a single Runtime.gc() call]
	public static void runGC() throws Exception {
		// for whatever reason it helps to call Runtime.gc()
		// using several method calls:
		for (int r = 0; r < 4; ++r)
			_runGC();
	}

	@SuppressWarnings("static-access")
	private static void _runGC() throws Exception {
		for (int i = 0; i < 10; ++i) {
			s_runtime.runFinalization();
			s_runtime.gc();
			Thread.currentThread().yield();
		}
	}

}
