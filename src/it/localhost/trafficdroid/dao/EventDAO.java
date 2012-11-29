package it.localhost.trafficdroid.dao;

import it.localhost.trafficdroid.exception.BadConfException;
import it.localhost.trafficdroid.exception.ConnectionException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.xml.sax.InputSource;

public class EventDAO {
	private static final String url = "http://www.cciss.it/portale/rss?rsstype=traffic";

	public static InputSource getData() throws BadConfException, ConnectionException {
		InputSource inputSource = null;
		int numTries = 3;
		while (true) {
			try {
				inputSource = new InputSource(new URL(url).openStream());
				break;
			} catch (MalformedURLException e) {
				throw new BadConfException(e);
			} catch (IOException e) {
				if (--numTries == 0)
					throw new ConnectionException(e);
			}
		}
		return inputSource;
	}
}
