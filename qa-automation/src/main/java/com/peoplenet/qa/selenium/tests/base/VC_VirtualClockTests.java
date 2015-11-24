package com.peoplenet.qa.selenium.tests.base;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.AccessConfig;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.db.config.DatabaseFactory;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava
 * 
 *         <pre>
 * {@code
 * Virtual Clock Tests
 * }
 * </pre>
 */
public class VC_VirtualClockTests extends AbstractSmokeTests {

	/**
	 * @param data
	 * @return true/false
	 * 
	 *         <pre>
	 *  {@code
	 *  VirtualClock-In Tests
	 * }
	 * </pre>
	 */
	public boolean clockIn(SuiteConfig data) {
		try {
			takeScreenShots("/clockIn/virtualClock", data.getGroup(), driver);
			Thread.sleep(5000);
			waitForPageLaod(driver, By.id("ClockIn"), 20);
			driver.findElement(By.id("ClockIn")).click();
			takeScreenShots("/clockIn/clockIn", data.getGroup(), driver);
			driver.findElement(
					By.xpath("//table/tbody/tr[2]/td/table/tbody/tr/td[2]/div[4]/button"))
					.click();
			String output = driver.findElement(By.id("divPleaseWaitMsg"))
					.getText();
			takeScreenShots("/clockIn/clockIn", data.getGroup(), driver);
			if (output.contains("^ERROR^Duplicate punch")) {
				driver.findElement(By.xpath("//button[contains(.,'Sign Out')]"))
						.click();
			} else {
				driver.findElement(By.xpath("//button[contains(.,'Sign Out')]"))
						.click();
			}
			return true;
		} catch (Exception e) {
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
			return false;
		}
	}

	/**
	 * @param data
	 * @return true/false
	 * 
	 *         <pre>
	 *  {@code
	 *  VirtualClock-Out Tests
	 * }
	 * </pre>
	 */
	public boolean clockOut(SuiteConfig data) {
		try {
			takeScreenShots("/clockOut/virtualClock", data.getGroup(), driver);
			Thread.sleep(5000);
			waitForPageLaod(driver, By.id("ClockOut"), 20);
			driver.findElement(By.id("ClockOut")).click();
			takeScreenShots("/clockOut/clockOut", data.getGroup(), driver);
			driver.findElement(
					By.xpath("//table/tbody/tr[2]/td/table/tbody/tr/td[2]/div[4]/button"))
					.click();
			String output = driver.findElement(By.id("divPleaseWaitMsg"))
					.getText();
			takeScreenShots("/clockOut/clockOut", data.getGroup(), driver);
			if (output.contains("^ERROR^Duplicate punch")) {
				driver.findElement(By.xpath("//button[contains(.,'Sign Out')]"))
						.click();
			} else {
				driver.findElement(By.xpath("//button[contains(.,'Sign Out')]"))
						.click();
			}
			return true;
		} catch (Exception e) {
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
			return false;
		}
	}

	/**
	 * @param data
	 * @param entry
	 * @param credentials
	 * @return true/false
	 * 
	 *         <pre>
	 *  {@code
	 *  VirtualClock Base Tests calls either clockIn/clockOut based on parameter ${entry (In/Out)}
	 * }
	 * </pre>
	 */
	public boolean virtualClock(SuiteConfig data, String entry,
			List<String> credentials) {
		try {
			AccessConfig config = null;
			for (AccessConfig accessConf : accessConfig) {
				if (data.getGroup().equals(accessConf.getScript())) {
					config = accessConf;
				} else if (accessConf.getScript().equals("Default")) {
					config = accessConf;
				}
			}
			for (String login : credentials) {
				driver = getDriver(data.getUniqueScript());
				driver.manage().window().maximize();
				driver.get(config.getURL());
				userid = driver.findElement(By.id("PIN"));
				submit = driver.findElement(By.id("SignIn"));
				log.info("Employee Pin : " + login);
				userid.clear();
				userid.sendKeys(login);
				submit.click();
				if (entry.equals("in")) {
					clockIn(data);
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
				} else if (entry.equals("out")) {
					clockOut(data);
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
				}
			}
			if (driver != null)
				driver.quit();
			return true;
		} catch (Exception e) {
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
			if (driver != null)
				driver.quit();
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.peoplenet.qa.selenium.base.tests.TMCSmokeTests#tests()
	 */
	@Override
	public void tests() {
		String content = new String();
		String groupTests = "VirtualClockTest";
		//long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "	+ data.getBrowser()	+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					try {
						List<String> credentials = DatabaseFactory
								.getClockData(inputValues.get("query"));
						if (credentials.size() > 0) {
							long startTime = System.currentTimeMillis();
							if (data.getScript().contains("VirtualClockIn")) {
								boolean status = virtualClock(data, "in",
										credentials);
								data.setStatus(String.valueOf(status));
							} else if (data.getScript().contains(
									"VirtualClockOut")) {
								boolean status = virtualClock(data, "out",
										credentials);
								data.setStatus(String.valueOf(status));
							}
							long endTime = System.currentTimeMillis();
							String currentTime = new String(""
									+ System.currentTimeMillis());
							content += HtmlReportGenerator.getFormattedHtml(
									data,
									data.getScript(),
									HtmlReportGenerator.getTime(endTime
											- startTime), inputValues,
									currentTime.trim());
							HtmlReportGenerator.generateInputHtml(inputValues,
									data.getScript(), PropertiesLoader
											.getProperty("testResultsDir"),
									currentTime.trim());
						}
					} catch (Exception e) {
						log.error("Exception : " + e.getMessage());
					}
				}
			}
		}
		// long end = System.currentTimeMillis();
		// String group = HtmlReportGenerator.getGroupHtml(groupTests + "	["
		// + HtmlReportGenerator.getTime(end - start) + "]");
		HtmlReportGenerator.setHtmlContent(content);
	}
}