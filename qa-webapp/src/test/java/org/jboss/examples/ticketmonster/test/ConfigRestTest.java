package org.jboss.examples.ticketmonster.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConfigRestTest {

	public static void main(String[] args) {
		try {
			URL url = new URL(
					"http://localhost:8080/ticket-monster/rest/forge/config/");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept",
					"application/json, text/javascript, */*;");

			String payload = "{\"id\":{\"project\":\"HMY\",\"environment\":\"QA\",\"buildVersion\":\"15.4.1571\"},\"testResultsDir\":\"tests\",\"emailReports\":false,\"remoteDriver\":true,\"remoteUser\":\"sajslsj\",\"remoteKey\":\"lsekfasl;dm\",\"timeWait\":\"15000\"}";

			OutputStream os = conn.getOutputStream();
			os.write(payload.getBytes());
			os.flush();

			System.out.println("Response Code : " + conn.getResponseCode());

			// if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED) {
			// throw new RuntimeException("Failed : HTTP error code : "
			// + conn.getResponseCode());
			// }

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}