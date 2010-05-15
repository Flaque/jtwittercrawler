package twittercrawler.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
	final static Logger LOG = LoggerFactory.getLogger(Log.class);

	public static void p(Object p) {
		System.out.println(p.toString());
	}

	public static void error(String msg) {
		LOG.error(msg);
	}

	public static void warn(String msg) {
		LOG.warn(msg);
	}

	public static void debug(String msg) {
		LOG.debug(msg);
	}

	public static void info(String msg) {
		LOG.info(msg);
	}

	public static void errorX(String msg, Throwable t) {
		LOG.error(msg, StringUtils.print(t, false));
	}

	public static void warnX(String msg, Throwable t) {
		LOG.warn(msg, StringUtils.print(t, false));
	}

	public static void debugX(String msg, Throwable t) {
		LOG.debug(msg, StringUtils.print(t, false));
	}

	public static void infoX(String msg, Throwable t) {
		LOG.info(msg, StringUtils.print(t, false));
	}
}
