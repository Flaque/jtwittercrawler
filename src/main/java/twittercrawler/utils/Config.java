package twittercrawler.utils;

public class Config {

	@Deprecated
	public final static String DB_NAME = "twitter_1";

	private static String root_path = "data/";

	public Config() { // defaults
	}

	/** @param root_path relative. including trailing slash */
	public Config(String root_path) {
		this.root_path = root_path;
	}

	public String getEdgesPath(int serie) {
		return root_path + "edges" + serie + ".dat";
	}

	public String getEVCentralityPath(int serie) {
		return root_path + "_ev_centrality/" + serie + ".dat";
	}

	/** latest */
	public String getEVCentralityPath() {
		return root_path + "_ev_centrality/17.dat";// TODO
	}

	@Deprecated
	public static String getPath() {
		return root_path;
	}
}
