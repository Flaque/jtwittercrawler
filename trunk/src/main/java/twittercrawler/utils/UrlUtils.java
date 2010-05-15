package twittercrawler.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class UrlUtils {


	@SuppressWarnings("deprecation")
	public static String getUrl(String url) throws IOException {

		URL u;
		InputStream is = null;
		DataInputStream dis;
		String s;
		StringBuffer sb = new StringBuffer();
		try {
			u = new URL(url);
			is = u.openStream(); // throws an IOException
			dis = new DataInputStream(new BufferedInputStream(is));
			while ((s = dis.readLine()) != null) {
				sb.append(s + " ");
			}
		}
		catch (Exception mue) {
			throw new IOException(mue);
		}
		finally {
			try {
				is.close();
			}
			catch (Exception ioe) {
			}
		}
		return sb.toString();
	}
}
