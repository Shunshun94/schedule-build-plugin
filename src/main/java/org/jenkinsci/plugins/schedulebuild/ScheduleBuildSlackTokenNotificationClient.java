package org.jenkinsci.plugins.schedulebuild;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ScheduleBuildSlackTokenNotificationClient {
	private final String token;
	private final String userName;
	public ScheduleBuildSlackTokenNotificationClient(String token, String userName) {
		this.token = token;
		this.userName = userName;
	}
	public void sendNotify(String message, String channelName) throws IOException {
		HttpURLConnection con = null;
		try {
			String baseUrl = String.format("https://slack.com/api/chat.postMessage?token=%s&channel=%s&text=%s&username=%s",
					token,
					channelName,
					URLEncoder.encode(message.replaceAll(" ", "%20"), "UTF-8").replaceAll("%2520", "%20").replaceAll("%7E", "~"),
					URLEncoder.encode(userName.replaceAll(" ", "%20"), "UTF-8").replaceAll("%2520", "%20").replaceAll("%7E", "~")
				);
			URL url = new URL(baseUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.getOutputStream();
			if(con.getResponseCode() != HttpURLConnection.HTTP_OK) {
				StringBuilder sb = new StringBuilder();
				try(InputStream is = con.getInputStream()) {
					String encoding = con.getContentEncoding();
					if( encoding == null ) {
						encoding = "UTF-8";
					}
					try(	InputStreamReader isReader = new InputStreamReader(is, encoding);
							BufferedReader bufReader = new BufferedReader(isReader); ) {
						String line = null;
						while((line = bufReader.readLine()) != null) {
							sb.append(line);
						}
					}
				}
				throw new IOException("Failed to access :" + con.getResponseCode() + " / Result: " + sb.toString());
			}
		} catch (IOException ioe) {
			throw ioe;
		}
		
	}
}
