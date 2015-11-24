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

import com.peoplenet.qa.selenium.data.EmployeeData;
import com.peoplenet.qa.selenium.data.ExcelUtils;

public class DatabaseFactory {
	private static final Logger log = Logger.getLogger("DatabaseFactory");
	public static Connection conn = null;
	static {
		if (DatabaseFactory.conn == null) {
			String dbName = "dev1sql1";
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				if (ExcelUtils.sheet.equals("DEV"))
					dbName = "dev1sql1";
				if (ExcelUtils.sheet.equals("QA"))
					dbName = "qa1-sql1";
				String connectionUrl = "jdbc:sqlserver://"
						+ dbName
						+ ":15150;databaseName=TimeCurrent;integratedSecurity=true;";
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

	public static Map<String, String> checkEmployeeCreation(String empEmail) {
		Map<String, String> credentials = new TreeMap<String, String>();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("update TimeCurrent..tblEmplNames set Password='test@123' where EmpEmail='"
					+ empEmail + "'");
			ResultSet result = stmt
					.executeQuery("select EmpEmail,Password,RecordID from TimeCurrent..tblEmplNames where EmpEmail='"
							+ empEmail + "'");
			if (result != null) {
				while ((result.next())) {
					if (result.getString("EmpEmail") != null
							&& result.getString("Password") != null) {
						credentials.put("userId", result.getString(1));
						credentials.put("password", result.getString(2));
						credentials.put("recordId", result.getString(3));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return credentials;
	}

	public static EmployeeData getEmployeeForClient(String empEmail,
			String client, Map<String, String> inputValues) {
		EmployeeData employee = new EmployeeData();
		try {
			Statement stmt = conn.createStatement();
			ResultSet result = stmt
					.executeQuery("select en.Client,gc.GroupName,en.SSN,en.FirstName,en.LastName,en.EmpEmail,en.Password,en.RecordID from TimeCurrent..tblEmplNames as en INNER JOIN TimeCurrent..tblGroups as gc  on en.Client = gc.Client AND en.GroupCode = gc.GroupCode where en.EmpEmail='"
							+ empEmail
							+ "' AND en.Client in (select Client from TimeCurrent..tblClients where ClientName='"
							+ client + "')");
			if (result != null) {
				while ((result.next())) {
					if (result.getString("EmpEmail") != null
							&& result.getString("Password") != null) {
						employee.setClient(result.getString(1));
						employee.setGroup(result.getString(2));
						employee.setSsn(result.getString(3));
						employee.setFirstName(result.getString(4));
						employee.setLastName(result.getString(5));
						employee.setEmpEmail(result.getString(6));
						employee.setPassword(result.getString(7));
						employee.setRecordID(result.getString(8));

					}
				}
			}
			if (employee.getEmpEmail() == null
					|| employee.getEmpEmail().isEmpty()) {
				if (client.equalsIgnoreCase("KELLY SERVICES"))
					employee.setPassword("kell@123");
				if (client.equalsIgnoreCase("MANPOWER GROUP"))
					employee.setPassword("mpow@123");
				if (client.equalsIgnoreCase("RS Retail"))
					employee.setPassword("rdrm@123");
				if (client.equalsIgnoreCase("CANADA"))
					employee.setPassword("adsc@123");

				employee.setClient(client);
				employee.setGroup("UnDefined");
				employee.setSsn("UnDefined");
				employee.setFirstName(inputValues.get("firstName"));
				employee.setLastName(inputValues.get("lastName"));
				employee.setEmpEmail(empEmail);
				employee.setRecordID("UnDefined");
			}
		} catch (Exception e) {
			log.error("No Records exist so setting a default one....");

			if (client.equalsIgnoreCase("KELLY SERVICES"))
				employee.setPassword("kell@123");
			if (client.equalsIgnoreCase("MANPOWER GROUP"))
				employee.setPassword("mpow@123");
			if (client.equalsIgnoreCase("RS Retail"))
				employee.setPassword("rdrm@123");
			if (client.equalsIgnoreCase("CANADA"))
				employee.setPassword("adsc@123");

			employee.setClient(client);
			employee.setGroup("UnDefined");
			employee.setSsn("UnDefined");
			employee.setFirstName(inputValues.get("firstName"));
			employee.setLastName(inputValues.get("lastName"));
			employee.setEmpEmail(empEmail);
			employee.setRecordID("UnDefined");
		}
		return employee;
	}

	public static void modifyEmployee(String empEmail, String client) {
		String query = "update TimeCurrent..tblEmplAssignments set OTOverrideRuleID=1 where SSN in (select SSN from TimeCurrent..tblEmplNames where EmpEmail='"
				+ empEmail + "')";

		if (client.equals("CANADA")) {
			query = "update TimeCurrent..tblEmplAssignments set PayRuleID=16664,OTOverrideRuleID=1 where SSN in (select SSN from TimeCurrent..tblEmplNames where EmpEmail='"
					+ empEmail + "')";
		}
		try {
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
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