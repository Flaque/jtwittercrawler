package twittercrawler.utils;

import static twittercrawler.utils.Log.warnX;
import twitter4j.Status;
import twitter4j.User;

public class TwitterUtils {

	public static final String D = "\t";

	public static boolean isRetweet(String text, String user) {

		if (text.startsWith("@" + user) || text.startsWith(".@" + user) || text.startsWith("? @" + user)) {
			return false;
		}

		if (text.indexOf("RT @" + user) != -1) {
			return true;

		} else if (text.indexOf("RT: @" + user) != -1) {
			return true;

		} else if (text.indexOf("etweeting @" + user) != -1) {
			return true;

		} else if (text.indexOf("etweet @" + user) != -1) {
			return true;
		} else if (text.indexOf("via @" + user) != -1) {
			return true;
		} else if (text.indexOf("thx @" + user) != -1) {
			return true;
		} else if (text.indexOf("THX @" + user) != -1) {
			return true;
		} else if (text.indexOf("ht @" + user) != -1) {
			return true;

		} else if (text.startsWith("r @" + user)) {
			return true;
		}

		return false;
	}

	public static boolean isRetweetFromMe(String text) {

		if (text.startsWith("@") || text.startsWith(".@") || text.startsWith("? @")) {
			return false;
		}

		if (text.indexOf("RT @") != -1) {
			return true;
		} else if (text.indexOf("RT: @") != -1) {
			return true;

		} else if (text.indexOf("etweeting @") != -1) {
			return true;
		} else if (text.indexOf("etweet @") != -1) {
			return true;

		} else if (text.indexOf("via @") != -1) {
			return true;
		} else if (text.indexOf("thx @") != -1) {
			return true;
		} else if (text.indexOf("THX @") != -1) {
			return true;
		} else if (text.indexOf("ht @") != -1) {
			return true;

		} else if (text.startsWith("r @")) {
			return true;
		}

		return false;
	}

	public static String extractUrl(String text) {

		int start = text.indexOf("http://");
		if (start == -1) {
			return null;
		}

		int end = text.indexOf(' ', start + 5);
		if (end == -1) {
			return text.substring(start, text.length());
		}
		return text.substring(start, end);
	}

	public static String cleanupText(String text) {
		return text.replace('\r', ' ').replace('\n', ' ').replace('\t', ' ');
	}

	public static String userAsCsv(User u) {
		String statusCreatedSinceMinutes = "";
		if (u.getStatusCreatedAt() != null) {
			statusCreatedSinceMinutes =
					(System.currentTimeMillis() - u.getStatusCreatedAt().getTime()) / MILLSECS_PER_HOUR + "";
		}

		String createdSinceDays = (System.currentTimeMillis() - u.getCreatedAt().getTime()) / MILLSECS_PER_DAY + "";

		return (u.getId() + TwitterUtils.D + u.getScreenName() + TwitterUtils.D + u.getFollowersCount()
				+ TwitterUtils.D + u.getFriendsCount() + TwitterUtils.D + u.getStatusesCount() + TwitterUtils.D
				+ createdSinceDays + TwitterUtils.D + statusCreatedSinceMinutes);
	}

	public static String statusAsCsv(Status status) {

		try {

			String text = cleanupText(status.getText());
			String url = extractUrl(text);

			if (status.isRetweet()) {
				System.out.println("retweet");
			}

			String retweetId = status.isRetweet() ? status.getRetweetedStatus().getId() + "" : "";
			String retweetUserName = status.isRetweet() ? status.getRetweetedStatus().getUser().getScreenName() : "";

			return (status.getId() + D + text + D + url + D + status.getUser().getId() + D
					+ status.getUser().getScreenName() + D + status.getInReplyToScreenName() + D
					+ status.getInReplyToUserId() + D + status.isRetweet() + D + retweetId + D + retweetUserName + D //
			);
		}
		catch (Exception e) {
			warnX("ouuuuuuups " + status, e);
			return "";
		}
	}

	public final static long MILLSECS_PER_DAY = 24 * 60 * 60 * 1000;
	public final static long MILLSECS_PER_HOUR = 60 * 60 * 1000;

}
