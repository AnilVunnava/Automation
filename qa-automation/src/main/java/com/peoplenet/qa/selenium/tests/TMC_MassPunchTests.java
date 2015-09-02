package com.peoplenet.qa.selenium.tests;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
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
 * 	MassPunch Tests will be executed based on the Group and Script enabling
 * }
 * </pre>
 * 
 */
public class TMC_MassPunchTests extends AbstractSmokeTests {

	/**
	 * @param data
	 *            - SuiteConfig Data
	 * @param inputValues
	 *            - Input Map Values separated by '|'
	 * @return true/false upon the execution
	 * 
	 *         <pre>
	 * {@code
	 * MassPunch for an Employee
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
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getLinkElement(driver, inputValues.get("site")).click();
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
			}
			Thread.sleep(2000);
		} catch (Exception e) {
			takeScreenShots("pickAnEmployee-error", data.getGroup(), driver);
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
		}
	}

	public boolean massPunch(WebDriver driver, Map<String, String> inputValues,
			SuiteConfig data) {
		try {
			takeScreenShots("massPunch", data.getGroup(), driver);
			if (getFrameElement(driver, "Menu") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Menu"));
				Select select = new Select(driver.findElement(By.name("Tasks")));
				select.selectByVisibleText(inputValues.get("function"));
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
			}
			Thread.sleep(2000);
			takeScreenShots("massPunch", data.getGroup(), driver);
			
			driver.switchTo().frame(getFrameElement(driver, "Navigation"));
			Select select = new Select(driver.findElement(By.name("PunchDate")));
			select.selectByVisibleText(inputValues.get("punchDate"));
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			takeScreenShots("massPunch", data.getGroup(), driver);
			Thread.sleep(2000);
			driver.switchTo().frame(getFrameElement(driver, "Navigation"));
			Select punchType = new Select(driver.findElement(By
					.name("PunchType")));
			if (inputValues.get("punchType").equals("In"))
				punchType.selectByVisibleText("In");
			if (inputValues.get("punchType").equals("Out"))
				punchType.selectByVisibleText("Out");
			takeScreenShots("massPunch", data.getGroup(), driver);
			Thread.sleep(2000);
			driver.switchTo().frame(getFrameElement(driver, "Navigation"));
			WebElement punchTime = driver.findElement(By.id("Punch Time"));
			punchTime.clear();
			if (inputValues.get("punchType").equals("In"))
				punchTime.sendKeys(inputValues.get("inTime"));
			if (inputValues.get("punchType").equals("Out"))
				punchTime.sendKeys(inputValues.get("outTime"));
			Thread.sleep(2000);
			takeScreenShots("massPunch", data.getGroup(), driver);
			driver.switchTo().frame(getFrameElement(driver, "Table"));
			List<WebElement> tableTr = driver.findElements(By.tagName("tr"));
			for (WebElement element : tableTr) {
				String employee = element.findElement(
						By.name("PreviewTimeCard")).getText();
				if (inputValues.get("employee").equals(employee)) {
					element.findElement(By.tagName("input")).click();
					break;
				}
			}
			takeScreenShots("massPunch", data.getGroup(), driver);
			Thread.sleep(2000);
			driver.findElement(By.name("SubmitButton")).submit();
			Thread.sleep(5000);
			Alert alert = driver.switchTo().alert();
			alert.accept();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			takeScreenShots("massPunch", data.getGroup(), driver);
			return true;
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean MassPunchForEmployee(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			pickAnEmployee(driver, inputValues, data);
			if (massPunch(driver, inputValues, data)) {
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
	
	public boolean massEntry(WebDriver driver, Map<String, String> inputValues,
			SuiteConfig data) {
		try {
			takeScreenShots("massEntry", data.getGroup(), driver);
			if (getFrameElement(driver, "Menu") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Menu"));
				Select select = new Select(driver.findElement(By.name("Tasks")));
				select.selectByVisibleText(inputValues.get("function"));
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
			}
			Thread.sleep(2000);
			takeScreenShots("massEntry", data.getGroup(), driver);
			
			driver.switchTo().frame(getFrameElement(driver, "Navigation"));
			Select select = new Select(driver.findElement(By.name("PayDay")));
			select.selectByVisibleText(inputValues.get("payDay"));
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
			takeScreenShots("massEntry", data.getGroup(), driver);
			
			Select punchType = new Select(driver.findElement(By
					.name("Adj")));
			punchType.selectByVisibleText(inputValues.get("adjType"));
			Thread.sleep(2000);
			takeScreenShots("massEntry", data.getGroup(), driver);
			
			driver.switchTo().frame(getFrameElement(driver, "Table"));
			Thread.sleep(2000);
			List<WebElement> tableTr = driver.findElements(By.tagName("tr"));
			for (WebElement element : tableTr) {
				String employee = element.findElement(
						By.xpath("/td[4]/a")).getText();
				if (inputValues.get("employee").equals(employee)) {
					element.findElement(By.xpath("/td[6]/input")).click();
					break;
				}
			}
			takeScreenShots("massEntry", data.getGroup(), driver);
			Thread.sleep(2000);
			driver.findElement(By.name("SubmitButton")).submit();
			Thread.sleep(5000);
			Alert alert = driver.switchTo().alert();
			alert.accept();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			takeScreenShots("massEntry", data.getGroup(), driver);
			return true;
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean MassEntryForEmployee(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			pickAnEmployee(driver, inputValues, data);
			if (massPunch(driver, inputValues, data)) {
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
		String groupTests = "TimePunch";
		// long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().startsWith("TMC_MassPunch")) {
						long startTime = System.currentTimeMillis();
						boolean status = MassPunchForEmployee(data, inputValues);
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
					if (data.getScript().startsWith("TMC_MassEntry")) {
						long startTime = System.currentTimeMillis();
						boolean status = MassEntryForEmployee(data, inputValues);
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
