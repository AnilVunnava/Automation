package com.peoplenet.qa.selenium.listeners;

import static com.peoplenet.qa.selenium.factory.SeleniumDriverFactory.getDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.factory.SeleniumDriverFactory;

/**
 * @author Anil.Vunnava
 *
 *         Screenshot Listener for capturing test results onSuccess and
 *         onFailure
 */
public class ScreenshotListener extends TestListenerAdapter {

	private static final String DATE_FORMAT_NOW = "yyyy-MM-dd-HH-mm-ss";
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			DATE_FORMAT_NOW);
	private static final Logger log = Logger.getLogger("ScreenshotListener");

	/**
	 * @param screenshot
	 * @return
	 */
	public static boolean createFile(File screenshot) {
		boolean fileCreated = false;

		if (screenshot.exists()) {
			fileCreated = true;
		} else {
			File parentDirectory = new File(screenshot.getParent());
			if (parentDirectory.exists() || parentDirectory.mkdirs()) {
				try {
					fileCreated = screenshot.createNewFile();
				} catch (IOException errorCreatingScreenshot) {
					errorCreatingScreenshot.printStackTrace();
				}
			}
		}

		return fileCreated;
	}

	/**
	 * @param driver
	 * @param screenshot
	 */
	public static void writeScreenshotToFile(WebDriver driver, File screenshot) {
		try {
			FileOutputStream screenshotStream = new FileOutputStream(screenshot);
			screenshotStream.write(((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.BYTES));
			screenshotStream.close();
		} catch (IOException unableToWriteScreenshot) {
			log.info("Unable to write " + screenshot.getAbsolutePath());
			unableToWriteScreenshot.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.testng.TestListenerAdapter#onTestFailure(org.testng.ITestResult)
	 */
	@Override
	public void onTestFailure(ITestResult failingTest) {
		for (SuiteConfig data : SeleniumDriverFactory.suiteConfig) {
			if (data.getStatus().equals("No")) {
				log.info(data.getScript()
						+ " is configured not take Screenshots....!");
			} else {
				WebDriver driver = getDriver(data.getUniqueScript());
				String screenshotDirectory = PropertiesLoader
						.getProperty("screenshotDirectory") != null ? PropertiesLoader
						.getProperty("screenshotDirectory")
						+ "/screenshots/failed/" : new File(".")
						+ "/screenshots/failed/";
				String screenshotAbsolutePath = screenshotDirectory
						+ failingTest.getName() + "_" + sdf.format(new Date())
						+ "_" + System.currentTimeMillis() + ".png";
				File screenshot = new File(screenshotAbsolutePath);
				if (createFile(screenshot)) {
					try {
						writeScreenshotToFile(driver, screenshot);
					} catch (ClassCastException weNeedToAugmentOurDriverObject) {
						writeScreenshotToFile(driver, screenshot);
					}
					log.info("Written screenshot to " + screenshotAbsolutePath);
				} else {
					log.info("Unable to create " + screenshotAbsolutePath);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.testng.TestListenerAdapter#onTestSuccess(org.testng.ITestResult)
	 */
	@Override
	public void onTestSuccess(ITestResult successTest) {
		for (SuiteConfig data : SeleniumDriverFactory.suiteConfig) {
			if (data.getStatus().equals("No")) {
				log.info(data.getScript()
						+ " is configured not take Screenshots....!");
			} else {
				WebDriver driver = getDriver(data.getUniqueScript());
				String screenshotDirectory = PropertiesLoader
						.getProperty("screenshotDirectory") != null ? PropertiesLoader
						.getProperty("screenshotDirectory")
						+ "/screenshots/success/" : new File(".")
						+ "/screenshots/success/";
				String screenshotAbsolutePath = screenshotDirectory
						+ successTest.getName() + "_" + sdf.format(new Date())
						+ "_" + System.currentTimeMillis() + ".png";
				File screenshot = new File(screenshotAbsolutePath);
				if (createFile(screenshot)) {
					try {
						writeScreenshotToFile(driver, screenshot);
					} catch (ClassCastException weNeedToAugmentOurDriverObject) {
						writeScreenshotToFile(driver, screenshot);
					}
					log.info("Written screenshot to " + screenshotAbsolutePath);
				} else {
					log.info("Unable to create " + screenshotAbsolutePath);
				}
			}
		}
	}

}