package com.peoplenet.qa.selenium.tests.base;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.Test;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava
 *
 *         <pre>
 * {@code
 * 	TimeCard Tests will be executed for Client 'TRC'
 * }
 * </pre>
 * 
 */
public class TMC_TRC_TimeEntryTests extends AbstractSmokeTests {

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TimeCard AddTime for the Input SuiteConfig
	 */
	public boolean SecondLevelApproval(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			log.info("start test AddTimeCard");
			pickAnEmployee(driver, inputValues, data);
			Thread.sleep(1000);
			boolean flag = AddTime(driver, inputValues, data, false);
			log.info("end test AddTimeCard");
			Thread.sleep(1000);
			CheckUnApproved(data, inputValues, driver);
			driver.quit();
			return flag;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}

	}

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TimeCard AddTime for the Input SuiteConfig
	 */
	public boolean CheckUnApproved(SuiteConfig data,
			Map<String, String> inputValues, WebDriver driver) {
		try {
			if (getFrameElement(driver, "Back") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Back"));
				driver.findElement(By.xpath("//table/tbody/tr/td/a")).click();
				Thread.sleep(1000);
				takeScreenShots("/SecondlevelApproval/approved",
						data.getGroup(), driver);
				Thread.sleep(1000);
				if (getFrameElement(driver, "Back") != null) {
					driver.switchTo().frame(getFrameElement(driver, "Back"));
					driver.findElement(By.xpath("//table/tbody/tr/td/a"))
							.click();
					Thread.sleep(1000);
					takeScreenShots("/SecondlevelApproval/approved",
							data.getGroup(), driver);
					if (getFrameElement(driver, "Table") != null) {
						driver.switchTo().frame(
								getFrameElement(driver, "Table"));
						Thread.sleep(1000);
						driver.findElement(By.id("TimeApproval")).click();
						Thread.sleep(1000);
						takeScreenShots("/SecondlevelApproval/approved",
								data.getGroup(), driver);
						getSpanElement(driver, inputValues.get("employee")).click();
						Thread.sleep(1000);
						driver.switchTo().frame(
								getFrameElement(driver, "Total"));
						Thread.sleep(1000);
						driver.findElement(By.xpath("//table/tbody/tr/td/a[3]"))
								.click();
						Thread.sleep(2000);
						Alert alert = driver.switchTo().alert();
						log.info("alert text: " + alert.getText());
						alert.accept();
						Thread.sleep(1000);
						for (String winHandle : driver.getWindowHandles()) {
							driver.switchTo().window(winHandle);
							log.info("winHandle: " + winHandle);
						}
						if (getFrameElement(driver, "Back") != null) {
							driver.switchTo().frame(
									getFrameElement(driver, "Back"));
							driver.findElement(
									By.xpath("//table/tbody/tr/td/a")).click();
							Thread.sleep(1000);
							takeScreenShots("/SecondlevelApproval/approved",
									data.getGroup(), driver);
							driver.switchTo().frame(
									getFrameElement(driver, "Table"));

							Thread.sleep(1000);
							driver.findElement(By.id("TimeApprovalSecondLevel"))
									.click();
							Thread.sleep(1000);
							takeScreenShots("/SecondlevelApproval/approved",
									data.getGroup(), driver);
							getSpanElement(driver, inputValues.get("employee")).click();
							Thread.sleep(1000);
							takeScreenShots("/SecondlevelApproval/approved",
									data.getGroup(), driver);
							driver.switchTo().frame(
									getFrameElement(driver, "Table"));
							Thread.sleep(1000);
							getLinkElement(driver, "Second Level Approval")
									.click();

							Alert alert1 = driver.switchTo().alert();
							Thread.sleep(1000);
							log.info("alert text: " + alert1.getText());
							alert1.accept();
							Thread.sleep(1000);
							for (String winHandle : driver.getWindowHandles()) {
								driver.switchTo().window(winHandle);
								log.info("winHandle: " + winHandle);
							}
							takeScreenShots("/SecondlevelApproval/approved",
									data.getGroup(), driver);
							driver.switchTo().frame(
									getFrameElement(driver, "Back"));
							driver.findElement(
									By.xpath("//table/tbody/tr/td/a")).click();
							Thread.sleep(1000);
							driver.switchTo().frame(
									getFrameElement(driver, "Table"));
							Thread.sleep(1000);
							takeScreenShots("/SecondlevelApproval/approved",
									data.getGroup(), driver);
						}
					}
				}
			}
			driver.quit();
			return true;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}

	}

	/**
	 * @param driver
	 * @param inputValues
	 * @param group
	 * 
	 * 
	 *            Pick an Employee with Client, Group, Site and Employee name
	 *            given as Input from the SuiteConfig Model extracted from Excel
	 *            Spread
	 */
	public void pickAnEmployee(WebDriver driver,
			Map<String, String> inputValues, SuiteConfig data) {
		try {
			takeScreenShots("clients", data.getGroup(), driver);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}
			Thread.sleep(1000);
			if (getFrameElement(driver, "Table") != null) {
				// Find an Employee
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				driver.findElement(
						By.xpath("//a[contains(text(),'"
								+ inputValues.get("client") + "')]")).click();
				Thread.sleep(1000);
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getLinkElement(driver, inputValues.get("group")).click();
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getLinkElement(driver, inputValues.get("site")).click();
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				Thread.sleep(2000);
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
				getLinkElement(driver, inputValues.get("employee")).click();
				if (getFrameElement(driver, "Navigation") != null) {
					// Find an Employee
					driver.switchTo().frame(
							getFrameElement(driver, "Navigation"));

					Select select = new Select(driver.findElement(By
							.name("PayPeriod")));
					select.selectByVisibleText(inputValues.get("timePeriod"));
					Robot robot = new Robot();

					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
				}
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
			}
		} catch (Exception e) {
			takeScreenShots("pickAnEmployee-error", data.getGroup(), driver);
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
		}

	}

	/**
	 * @param driver
	 * @param inputValues
	 * @param group
	 * @param missingOutTime
	 * @return
	 * 
	 *         Base method for Adding new Transaction for an Employee with In
	 *         and Out Times for a specific time period and all the values are
	 *         derived from Excel Spreadsheet.
	 */
	public boolean AddTime(WebDriver driver, Map<String, String> inputValues,
			SuiteConfig data, boolean missingOutTime) {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			log.info("winHandle: " + winHandle);
		}
		try {
			if (getFrameElement(driver, "Table") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getLinkElement(driver, "New Transaction").click();
				takeScreenShots("/AddTime/AddTimeCard", data.getGroup(), driver);
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}

				Select select = new Select(driver.findElement(By
						.name("sitedept1")));
				waitForPageLaod(driver, By.name("sitedept1"));
				try {
					select.selectByVisibleText(inputValues.get("siteDept"));
				} catch (Exception e) {
					List<WebElement> options = select.getOptions();
					for (WebElement element : options) {
						if (element.getText().contains(inputValues.get("site"))) {
							select.selectByVisibleText(element.getText());
							break;
						}
					}
				}
				Robot robot = new Robot();

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				Select selectDate = new Select(driver.findElement(By
						.name("indate1")));
				selectDate.selectByVisibleText(inputValues.get("inDate"));
				Robot robot1 = new Robot();

				robot1.keyPress(KeyEvent.VK_ENTER);
				robot1.keyRelease(KeyEvent.VK_ENTER);

				driver.findElement(By.id("intime1")).sendKeys(
						inputValues.get("inTime"));
				if (!missingOutTime) {
					driver.findElement(By.id("outtime1")).sendKeys(
							inputValues.get("outTime"));
					Select selectReason = new Select(driver.findElement(By
							.name("reasoncodeid1")));
					selectReason.selectByVisibleText(inputValues.get("reason"));
					Robot robot2 = new Robot();

					robot2.keyPress(KeyEvent.VK_ENTER);
					robot2.keyRelease(KeyEvent.VK_ENTER);

					driver.findElement(By.name("reasoncodeid1")).sendKeys(
							Keys.TAB);
					driver.findElement(By.id("Comment")).sendKeys(
							"\n\n Tested via automation at system time: "
									+ formatData.format(Calendar.getInstance()
											.getTime()));
					driver.findElement(By.id("Submit")).submit();
					Thread.sleep(8000);
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
						log.info("winHandle: " + winHandle);
					}
					takeScreenShots("/AddTime/AddTimeCard", data.getGroup(),
							driver);
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			takeScreenShots("/AddTime/AddTimeCard-error", data.getGroup(),
					driver);
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.peoplenet.qa.selenium.base.tests.TMCSmokeTests#tests()
	 */
	@Test
	public void tests() {
		String content = new String();
		String groupTests = "TRC_TimeEntry";
		// long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().startsWith(
							"TMC_TRC_SecondLevelApproval")) {
						long startTime = System.currentTimeMillis();
						boolean status = SecondLevelApproval(data, inputValues);
						long endTime = System.currentTimeMillis();
						data.setStatus(String.valueOf(status));
						String currentTime = new String(""
								+ System.currentTimeMillis());
						content += HtmlReportGenerator.getFormattedHtml(data,
								data.getScript(), HtmlReportGenerator
										.getTime(endTime - startTime),
								inputValues, currentTime.trim());
						HtmlReportGenerator.generateInputHtml(inputValues,
								data.getScript(),
								PropertiesLoader.getProperty("testResultsDir"),
								currentTime.trim());
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
