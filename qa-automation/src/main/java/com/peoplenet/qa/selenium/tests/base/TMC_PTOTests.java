package com.peoplenet.qa.selenium.tests.base;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
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
 * 	TimeCard Tests will be executed based on the Group and Script enabling
 * }
 * </pre>
 * 
 */
public class TMC_PTOTests extends AbstractSmokeTests {

	/**
	 * @param data
	 *            - SuiteConfig Data
	 * @param inputValues
	 *            - Input Map Values separated by '|'
	 * @return true/false upon the execution
	 * 
	 *         <pre>
	 * {@code
	 * PTO [Pay Time Off Maintainance]
	 * }
	 * 
	 *         <pre>
	 */
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
				if (getFrameElement(driver, "Menu") != null) {
					driver.switchTo().frame(getFrameElement(driver, "Menu"));
					Select select = new Select(driver.findElement(By
							.name("Tasks")));
					select.selectByVisibleText(inputValues.get("function"));
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
				}
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
				driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getLinkElement(driver, inputValues.get("employee")).click();
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
			}
		} catch (Exception e) {
			takeScreenShots("pickAnEmployee-error", data.getGroup(), driver);
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
		}
	}

	public boolean AddPTO(WebDriver driver, Map<String, String> inputValues,
			SuiteConfig data, boolean missingOutTime) {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			log.info("winHandle: " + winHandle);
		}
		try {
			takeScreenShots("addPTO", data.getGroup(), driver);
			WebElement pto1 = getInputElement(driver, "name",
					"AccrualBalance_1");
			WebElement pto2 = getInputElement(driver, "name",
					"AccrualBalance_2");
			WebElement pto3 = getInputElement(driver, "name",
					"AccrualBalance_3");
			WebElement pto4 = getInputElement(driver, "name",
					"AccrualBalance_4");
			if (pto1.getAttribute("value").equals("0.00")) {
				pto1.click();
				pto1.clear();
				pto1.sendKeys(inputValues.get("time"));
				takeScreenShots("addPTO", data.getGroup(), driver);
			}
			if (pto2.getAttribute("value").equals("0.00")) {
				pto2.click();
				pto2.clear();
				pto2.sendKeys(inputValues.get("time"));
				takeScreenShots("addPTO", data.getGroup(), driver);
			}
			if (pto3.getAttribute("value").equals("0.00")) {
				pto3.click();
				pto3.clear();
				pto3.sendKeys(inputValues.get("time"));
				takeScreenShots("addPTO", data.getGroup(), driver);
			}
			if (pto4.getAttribute("value").equals("0.00")) {
				pto4.click();
				pto4.clear();
				pto4.sendKeys(inputValues.get("time"));
				takeScreenShots("addPTO", data.getGroup(), driver);
			}
			WebElement comment = driver.findElement(By.id("Comment"));
			comment.clear();
			comment.sendKeys("Test PTO Add - " + new Date());
			Thread.sleep(1000);
			takeScreenShots("addPTO", data.getGroup(), driver);
			driver.findElement(By.name("Submit")).submit();
			Thread.sleep(1500);
			Alert alert = driver.switchTo().alert();
			alert.accept();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}
			driver.switchTo().frame(getFrameElement(driver, "Table"));
			WebElement parent = getLinkElement(driver,
					inputValues.get("employee")).findElement(
					By.xpath("../td[3]/a"));
			parent.click();
			return true;
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean ApprovePTO(WebDriver driver,
			Map<String, String> inputValues, SuiteConfig data,
			boolean missingOutTime) {
		try {
			driver.switchTo().frame(getFrameElement(driver, "Table"));
			takeScreenShots("ApprovePTO", data.getGroup(), driver);
			Select select = new Select(driver.findElement(By.id("ViewMode")));
			select.selectByVisibleText("List Only");
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			Thread.sleep(2000);
			takeScreenShots("ApprovePTO", data.getGroup(), driver);
			return true;
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean ApprovePTOForEmployee(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			pickAnEmployee(driver, inputValues, data);
			if (ApprovePTO(driver, inputValues, data, false)) {
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				Thread.sleep(5000);
			}
			driver.quit();
			return true;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean AddPTOForEmployee(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			pickAnEmployee(driver, inputValues, data);
			if (AddPTO(driver, inputValues, data, false)) {
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				Thread.sleep(5000);
			}
			driver.quit();
			return true;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
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
		String groupTests = "PTOTests";
		// long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().startsWith("TMC_AddPTO")) {
						long startTime = System.currentTimeMillis();
						boolean status = AddPTOForEmployee(data, inputValues);
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
					if (data.getScript().startsWith("TMC_ApprovePTO")) {
						long startTime = System.currentTimeMillis();
						boolean status = ApprovePTOForEmployee(data, inputValues);
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
