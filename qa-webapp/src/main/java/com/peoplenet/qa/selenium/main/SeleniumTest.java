package com.peoplenet.qa.selenium.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.TestNG;

import com.peoplenet.qa.selenium.data.ExcelUtils;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.factory.SeleniumDriverFactory;
import com.peoplenet.qa.selenium.listeners.ScreenVideoListener;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;
import com.peoplenet.qa.selenium.reports.MailerService;

/**
 * @author Anil.Vunnava
 *
 *         <pre>
 * {@code
 *          Base Main Class from with the Selenium Automation Script starts
 *          executing with the below params as example and were set as
 *          environment variables.
 *          Test Properties File [C:/test.properties]
 *          Test ExcelSpreadsheet File [C:/SuiteConfig.xlsx]
 *          Test Spreadsheet name [Data]
 *          Test SuiteConfig XML File [C:/suites.xml]
 * }
 * </pre>
 */
public class SeleniumTest {

	private static final Logger log = Logger.getLogger("SeleniumTest");
	public static ScreenVideoListener video = new ScreenVideoListener();

	// Base path for your Test Data
	private static final String baseTestPath = "C:/tmc-tests/";
	private static final String accessSheet = "Access";
	private static int count = 0;

	/**
	 * @param args
	 * 
	 *            Main method from the the Execution starts
	 */
	public static void main(String[] args) {

		String file = System.getenv("TEST_PROPS");
		String excelFile = System.getenv("TEST_SHEET");
		String dataSheet = System.getenv("SHEET_NAME");
		String suiteConfig = System.getenv("TEST_SUITE");

		// Enable below and comment above for Testing in local IDE.

		// String file = baseTestPath + "tmc-config.properties";
		// String excelFile = baseTestPath + "TMCConfig.xlsx";
		// String dataSheet = "Data";
		// String suiteConfig = baseTestPath + "suites.xml";

		log.info("Loaded System Properties......... ");
		log.info(file);
		log.info(excelFile);
		log.info(dataSheet);
		log.info(suiteConfig);
		try {
			if (file != null && !file.isEmpty()) {
				PropertiesLoader.loadProperties(file);
				if (excelFile != null && !excelFile.isEmpty()) {
					if (excelFile.endsWith(".xls")
							|| excelFile.endsWith(".xlsx")) {
						if (dataSheet == null || dataSheet.isEmpty()) {
							dataSheet = "none";
						}
						ExcelUtils.setExcelFile(excelFile, dataSheet,
								accessSheet);
						log.info("Loading Input Data....");
						SeleniumDriverFactory.setSuiteConfig(ExcelUtils
								.loadSuiteData());
						for (SuiteConfig config : SeleniumDriverFactory
								.getSuiteConfig()) {
							if (config.getStatus().equals("Yes")) {
								count++;
							}
						}
						SeleniumDriverFactory.setAccessConfig(ExcelUtils
								.loadSuiteAccessData());
						log.info("Loaded Input Data Successfully.... "
								+ SeleniumDriverFactory.getSuiteConfig().size());
						List<String> suites = new ArrayList<String>();
						String suiteFile = "";
						if (suiteConfig.startsWith("suites")) {
							suiteFile = new File("") + "suites.xml";
							log.info("SuiteConfig XML Path : " + new File("")
									+ "suites.xml");

						} else
							suiteFile = suiteConfig;
						suites.add(suiteFile);
						run(suites);
					} else {
						System.err
								.println("Invalid Format excel test data.. Should be '.xls' (or) '.xlsx'");
						System.exit(0);
					}
				} else {
					System.err
							.println("Please specify the excel test data File as argument");
					System.exit(0);
				}
			} else {
				System.err
						.println("Please specify the selenium test properties file as argument");
				System.exit(0);
			}
		} catch (Exception e) {
			log.error("Error Due to : " + e.getMessage());
			System.err.println("Exception Occured Due To : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void run(List<String> suites) {
		long start = System.currentTimeMillis();
		String outdir = PropertiesLoader.getProperty("testResultsDir");
		try {
			TestNG testng = new TestNG();
			testng.setOutputDirectory(outdir != null ? outdir + "/test-output/"
					: "/" + ExcelUtils.sheet + "/");
			testng.setTestSuites(suites);
			startRecording(Boolean.valueOf(PropertiesLoader
					.getProperty("enableVideoRecording")),
					PropertiesLoader.getProperty("videoFile"));
			testng.run();
			long end = System.currentTimeMillis();
			 stopRecording(
					Boolean.valueOf(PropertiesLoader
							.getProperty("enableVideoRecording")),
					HtmlReportGenerator.getTime(end - start));
			/*List<SuiteConfig> failedTests = stopRecording(
					Boolean.valueOf(PropertiesLoader
							.getProperty("enableVideoRecording")),
					HtmlReportGenerator.getTime(end - start));*/
			// Code below is Re-Running failed tests and a separate mail is sent
			// for this re-running
			 /*if (failedTests.size() > 0) {
			 long start1 = System.currentTimeMillis();
			 PropertiesLoader.prop.put("testResultsDir", outdir + "-failed");
			 PropertiesLoader.prop.put("mailSubject",
			 "[Failed Re-Run] Automated Selenium SmokeTest Reports");
			 SeleniumDriverFactory.setSuiteConfig(failedTests);
			 testng.setOutputDirectory(outdir != null ? outdir
			 + "-failed/test-output/" : "/" + ExcelUtils.sheet + "/");
			 testng.setTestSuites(suites);
			 startRecording(Boolean.valueOf(PropertiesLoader
			 .getProperty("enableVideoRecording")),
			 PropertiesLoader.getProperty("videoFile"));
			 testng.run();
			 long end1 = System.currentTimeMillis();
			 stopRecording(Boolean.valueOf(PropertiesLoader
			 .getProperty("enableVideoRecording")),
			 HtmlReportGenerator.getTime(end1 - start1));
			 }*/
		} catch (Exception e) {
			stopRecording(Boolean.valueOf(PropertiesLoader
					.getProperty("enableVideoRecording")),
					HtmlReportGenerator.getTime(System.currentTimeMillis()
							- start));
			log.error("Error Due to : " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void startRecording(boolean start, String project) {
		if (start) {
			if (video == null)
				video = new ScreenVideoListener();
			try {
				video.startRecording(project);
			} catch (Exception e1) {
				log.info("Unable to Start Video Recording : " + e1.getMessage());
			}
		}
	}

	public static List<SuiteConfig> stopRecording(boolean stop, String totalTime) {
		List<SuiteConfig> failedTests = new ArrayList<SuiteConfig>();
		if (stop) {
			try {
				video.stopRecording();
			} catch (Exception e) {
				log.info("Unable to Stop Video Recording : " + e.getMessage());
			}
		}
		String finalContent = new String();
		for (String report : HtmlReportGenerator.getHtmlContent()) {
			finalContent += report;
		}
		if (count > 0) {
			failedTests = HtmlReportGenerator.witeToHtml(finalContent,
					PropertiesLoader.getProperty("testResultsDir"), totalTime);
			if (Boolean.valueOf(PropertiesLoader.getProperty("emailReports")))
				MailerService.sendReportMail(
						PropertiesLoader.getProperty("mailFrom"),
						PropertiesLoader.getProperty("mailPassword"),
						PropertiesLoader.getProperty("mailTo"));
			else
				log.info("Enable emailReports property to 'true' for mailing the reports");
		}
		// HtmlReportGenerator.getHtmlContent().clear();
		return failedTests;
	}
}