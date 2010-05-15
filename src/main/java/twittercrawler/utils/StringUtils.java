package twittercrawler.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class StringUtils {

	public static String print(Throwable e, boolean shorten) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append(e.toString().replace('\r', ' ').replace('\n', ' ') + " caused by "
					+ e.getCause().toString().replace('\r', ' ').replace('\n', ' ') + " (");
		}
		catch (Exception e2) {
			sb.append(e.toString());
		}
		int i = 0;
		for (StackTraceElement el : e.getStackTrace()) {
			sb.append(el.getClassName() + "." + el.getMethodName() + ":l." + el.getLineNumber() + "; ");
			if (shorten && (i++ > 11)) {
				break;
			}
		}
		return sb.toString();
	}

	public static final String SER_PATH = "serialized/";

	public static Object deserialize(String name) throws IOException, ClassNotFoundException {

		FileInputStream fileIn = new FileInputStream(SER_PATH + name + ".ser");
		ObjectInputStream in = new ObjectInputStream(fileIn);
		Object o = in.readObject();
		in.close();
		fileIn.close();
		return o;
	}



	public static void serialize(Object o, String name) throws IOException {

		FileOutputStream fileOut = new FileOutputStream(SER_PATH + name + ".ser");
		ObjectOutputStream out = new ObjectOutputStream(fileOut);
		out.writeObject(o);
		out.close();
		fileOut.close();

	}
}
