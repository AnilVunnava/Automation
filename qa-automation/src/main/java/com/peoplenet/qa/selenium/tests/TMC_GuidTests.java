package com.peoplenet.qa.selenium.tests;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava
 *
 *         TMC GUID Tests
 */
public class TMC_GuidTests extends AbstractSmokeTests {
	private static final Logger log = Logger.getLogger("TMC_GuidTests");

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TMC GUID Tests
	 */
	public boolean guidTests(SuiteConfig data, Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
			}
			Thread.sleep(2000);
			driver.switchTo().frame("Table");
			takeScreenShots("guidTests", data.getGroup(), driver);
			((JavascriptExecutor) driver)
					.executeScript("return window.getSelection().toString();");
			driver.findElement(
					By.xpath("//a[contains(text(),'KELLY SERVICES')]")).click();
			Thread.sleep(1500);
			driver.switchTo().frame("Table");
			takeScreenShots("guidTests", data.getGroup(), driver);

			driver.findElement(By.xpath("//a[contains(text(),'Branch -3547')]"))
					.click();
			Thread.sleep(1500);
			driver.switchTo().frame("Table");
			driver.findElement(By.id("TimeEntry")).click();
			takeScreenShots("guidTests", data.getGroup(), driver);
			driver.findElement(
					By.xpath("//span[@onclick=\"sendRequest(61488,'',206168,'SendTEReq',0,1)\"]"))
					.click();
			Thread.sleep(2000);
			takeScreenShots("guidTests", data.getGroup(), driver);
			Alert alert = driver.switchTo().alert();
			alert.accept();
			Thread.sleep(2000);
			// Send Reminder
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
	@Override
	public void tests() {
		String content = new String();
		String groupTests = "GUIDTests";
		// long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().startsWith("TMC_Guid")) {
						long startTime = System.currentTimeMillis();
						boolean status = guidTests(data, inputValues);
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
