package com.peoplenet.qa.selenium.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.SeleniumDriverFactory;

/**
 * @author Anil.Vunnava
 *
 *         Used to Generate HTML Customized Reports
 */
public class HtmlReportGenerator {

	private static List<String> htmlContent = new ArrayList<String>();
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd-HH-mm");

	/**
	 * @return the htmlContent
	 */
	public static List<String> getHtmlContent() {
		return HtmlReportGenerator.htmlContent;
	}

	/**
	 * @param htmlContent
	 *            the htmlContent to set
	 */
	public static void setHtmlContent(String htmlContent) {
		HtmlReportGenerator.htmlContent.add(htmlContent);
	}

	/**
	 * @param content
	 * @param count
	 * @param outputDir
	 * @param totalTime
	 */
	public static List<SuiteConfig> witeToHtml(String content,
			String outputDir, String totalTime) {
		List<SuiteConfig> failedTests = new ArrayList<SuiteConfig>();
		try {
			File file = new File(outputDir + "/report");
			if (!file.exists()) {
				file.mkdirs();
			}
			VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine = new VelocityEngine();
			velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER,
					"classpath");
			velocityEngine.setProperty("classpath.resource.loader.class",
					ClasspathResourceLoader.class.getName());
			velocityEngine.init();
			Template t = velocityEngine.getTemplate("reports/email-report.vm");
			VelocityContext context = new VelocityContext();
			int passCount = 0;
			int failCount = 0;
			int skipCount = 0;
			for (SuiteConfig config : SeleniumDriverFactory.getSuiteConfig()) {
				if (config.getStatus().equals("Passed"))
					passCount = passCount + 1;
				else if (config.getStatus().equals("Failed")) {
					failCount = failCount + 1;
					failedTests.add(config);
				} else if (config.getStatus().equals("No"))
					skipCount = skipCount + 1;
			}
			context.put("date", dateFormat.format(new Date()));
			context.put("passCount", passCount);
			context.put("failCount", failCount);
			context.put("skipCount", skipCount);
			context.put("totalTime", totalTime);
			context.put("reportBody", content);
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			String htmlReport = writer.toString();
			generateHtml(htmlReport, outputDir + "/qa-smoketest.html");
			return failedTests;
		} catch (Exception e) {
			e.printStackTrace();
			return failedTests;
		}
	}

	/**
	 * @param config
	 * @param testCase
	 * @param time
	 * @param inputValues
	 * @param currentTime
	 * @return
	 */
	public static String getFormattedHtml(SuiteConfig config, String testCase,
			String time, Map<String, String> inputValues, String currentTime) {
		String content = "";
		String status = "<td/>";
		if (config.getStatus().equals("Passed"))
			status = "<td style=\"color: green;font-weight: bold;\">"
					+ config.getStatus() + "</td>";
		else if ((config.getStatus().equals("Failed")))
			status = "<td style=\"color: red;font-weight: bold;\">"
					+ config.getStatus() + "</td>";
		content = "<tr><td>" + testCase + "</td><td>"
				+ config.getBrowser().toUpperCase() + "</td><td>"
				+ "<a href=\"report/" + testCase + "-Input-" + currentTime
				+ ".html" + "\" target=\"_blank\">Input Data</a></td>" + status
				+ "<td>" + "<a href=\"screenshots/all/" + config.getGroup()
				+ "\" target=\"_blank\">Screenshots</a></td><td>"
				+ config.getDescription() + "</td><td>" + time + "</td></tr>";
		return content;
	}

	/**
	 * @param config
	 * @param testCase
	 * @param time
	 * @param inputValues
	 * @param currentTime
	 * @return
	 */
	public static String getFormattedHtmlHMY(SuiteConfig config,
			String testCase, String time, Map<String, String> inputValues,
			String currentTime) {
		String content = "";
		String status = "<td/>";
		if (config.getStatus().equals("Passed"))
			status = "<td style=\"color: green;font-weight: bold;\">"
					+ config.getStatus() + "</td>";
		else if ((config.getStatus().equals("Failed")))
			status = "<td style=\"color: red;font-weight: bold;\">"
					+ config.getStatus() + "</td>";
		content = "<tr><td>" + testCase + "</td><td>"
				+ config.getBrowser().toUpperCase() + "</td><td>"
				+ inputValues.get("EmpName") + "</td>" + status + "<td>"
				+ "<a href=\"screenshots/all/" + config.getGroup()
				+ "\" target=\"_blank\">Screenshots</a></td><td>"
				+ config.getDescription() + "</td><td>" + time + "</td></tr>";
		return content;
	}

	// /**
	// * @param group
	// * @return
	// */
	// public static String getGroupHtml(String group) {
	// String content = "<p>"
	// + "<b style=\"color: #00B2E8;font-size: large;\">"
	// + group
	// + "</b>"
	// + "</p>"
	// + "<table "
	// + "class=\"table table-striped table-bordered table-hover table-fixed\""
	// + "style=\"border: 1px solid;\"><thead><tr>"
	// +
	// "<th style=\"text-align: center;\">Test Case</th><th style=\"text-align: center;\">Browser</th><th style=\"text-align: center;\">Input</th>"
	// +
	// "<th style=\"text-align: center;\">Status</th><th style=\"text-align: center;\">Screenshots</th>"
	// +
	// "<th style=\"text-align: center;\">Summary</th><th style=\"text-align: center;\">Total Time</th></tr></thead>";
	// return content;
	// }

	/**
	 * @param inputValues
	 * @param testCase
	 * @param outDir
	 * @param currentTime
	 */
	public static void generateInputHtml(Map<String, String> inputValues,
			String testCase, String outDir, String currentTime) {
		try {
			File file = new File(outDir + "/report");
			if (!file.exists()) {
				file.mkdirs();
			}
			VelocityEngine velocityEngine = new VelocityEngine();
			velocityEngine = new VelocityEngine();
			velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER,
					"classpath");
			velocityEngine.setProperty("classpath.resource.loader.class",
					ClasspathResourceLoader.class.getName());
			velocityEngine.init();
			velocityEngine.init();
			Template t = velocityEngine.getTemplate("reports/input.vm");
			VelocityContext context = new VelocityContext();
			int i = 1;
			String content = new String();
			for (Map.Entry<String, String> entry : inputValues.entrySet()) {
				content += "<tr><td>" + i + "</td><td>" + entry.getKey()
						+ "</td><td>" + entry.getValue() + "</td></tr>";
				i++;
			}
			context.put("input", content);
			context.put("testCase", testCase + "-Input");
			StringWriter writer = new StringWriter();
			t.merge(context, writer);
			String htmlReport = writer.toString();
			generateHtml(htmlReport, file.getAbsolutePath() + "/" + testCase
					+ "-Input-" + currentTime + ".html");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param millis
	 * @return
	 */
	public static String getTime(long millis) {
		return String.format(
				"%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));
	}

	/**
	 * @param htmlContent
	 * @param outputFile
	 */
	private static void generateHtml(String htmlContent, String outputFile) {

		FileOutputStream fop = null;
		File file;
		try {
			file = new File(outputFile);
			fop = new FileOutputStream(file);
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}
			// get the content in bytes
			byte[] contentInBytes = htmlContent.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}