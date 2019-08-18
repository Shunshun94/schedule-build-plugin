package org.jenkinsci.plugins.schedulebuild;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ScheduleBuildSlackNotificationClient {
	private final URL url;
	private final String userName;
	public ScheduleBuildSlackNotificationClient(URL url, String userName) {
		this.url = url;
		this.userName = userName;
	}
	
	public void sendNotify(String message) throws IOException {
		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8));
			writer.write(String.format("payload={\"username\": \"%s\", \"text\": \"%s\", \"icon_emoji\": \":rooster:\"}", userName, message));
			writer.flush();
			if(con.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new IOException(String.format("Failed to access %s :" + con.getResponseCode(), this.url));
			}
		} catch (IOException ioe) {
			throw ioe;
		}
	}
}
