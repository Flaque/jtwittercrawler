package twittercrawler.utils;

import twitter4j.Twitter;

/**
 * Just a wrapper around Twitter to provide id&pw
 * @author ren
 */
public class TwitterClient extends Twitter {

	// curl -u renaudrichardet:{pw} http://twitter.com/account/rate_limit_status.xml

	public static String myIdString = "renaudrichardet";

	public static String myPw = "{pw}";

	@SuppressWarnings("deprecation")
	public TwitterClient() {
		super(myIdString, myPw);
	}

}
