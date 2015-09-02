package com.peoplenet.qa.selenium.db.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class DatabaseFactory {
	private static final Logger log = Logger.getLogger("DatabaseFactory");
	public static Connection conn = null;
	static {
		if (DatabaseFactory.conn == null) {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String connectionUrl = "jdbc:sqlserver://qa2-sql1:15150;databaseName=TimeCurrent;integratedSecurity=true;";
				DatabaseFactory.conn = DriverManager
						.getConnection(connectionUrl);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static Map<String, String> getListFromDatabase(String query) {
		Map<String, String> credentials = new TreeMap<String, String>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			while ((result.next() && result.getString("empEmail") != null)) {
				if (result.getString("empPass") != null) {
					credentials.put(result.getString(1), result.getString(2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return credentials;
	}

	private static Map<String, String> getDataFromDatabase(String query) {
		Map<String, String> credentials = new TreeMap<String, String>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			while ((result.next() && result.getString("EmpEmail") != null)) {
				if (result.getString("Password") != null) {
					credentials.put(result.getString(1), result.getString(2));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return credentials;
	}

	public static Map<String, String> getQueryResults(String query) {
		Map<String, String> credentials = new TreeMap<String, String>();
		log.info("Query to be Executed :: " + query);
		if (query.startsWith("exec"))
			credentials = getListFromDatabase(query);
		else if (query.toUpperCase().startsWith("SELECT"))
			credentials = getDataFromDatabase(query);
		return credentials;
	}

	public static List<String> getClockData(String query) {
		List<String> clockData = new ArrayList<String>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			while ((result.next() && result.getString("EmpEmail") != null)) {
				if (result.getString("emplpin") != null) {
					clockData.add(result.getString(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clockData;
	}

	public static Map<String, String> getExemptData(String query, String exempt) {
		Map<String, String> credentials = new TreeMap<String, String>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt.executeQuery(query);
			if (exempt.contains("lunch")) {
				while ((result.next())) {
					if (result.getString("EmpEmail") != null
							&& result.getString("Password") != null) {
						credentials.put(result.getString(1),
								result.getString(2));
					}
				}
			} else {
				while ((result.next())) {
					if (result.getString("EmpEmail") != null
							&& result.getString("Password") != null
							&& result.getString("GroupCode") != null) {
						credentials.put(result.getString(1),
								result.getString(2));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return credentials;
	}

	protected static String parse(String src, String key) {
		String token = src;
		String value = "";
		if (token != null) {
			String[] values = token.split("\\|");
			if (key.equals("ssn") || key.equals("code"))
				value = values[1];
			else if (key.equals("password"))
				value = values[0];
		}
		return value;
	}

	public static void main(String[] args) {
		System.out
				.println(getQueryResults("SELECT en.EmpEmail, en.Password FROM  TimeCurrent..tblEmplAssignments AS ea INNER JOIN TimeCurrent..tblEmplNames AS en ON  en.Client = ea.Client AND en.GroupCode = ea.GroupCode AND en.SSN = ea.SSN WHERE  ea.Client = 'kell' AND en.WTE_TimeEntry = 6 AND en.EmpEmail is not null and ea.GroupCode not in (104,132) AND en.Password is not null AND en.RecordStatus = 1 AND ea.EndDate > GETDATE()"));
	}
}