package com.peoplenet.qa.selenium.tests;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava
 *
 *         HMY_AJ_TimeEntry_V1Tests [Angular JS Version]
 */
public class HMY_AJ_TimeEntry_V1Tests extends AbstractSmokeTests {
	private static final Logger log = Logger
			.getLogger("HMY_AJ_TimeEntry_V1Tests");

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         HMY_AJ_TimeEntry_V1 Tests [Angular JS Version]
	 */
	public boolean timeEntry(SuiteConfig data, Map<String, String> inputValues) {
		WebDriver driver = registerAndLoginHMY(data, false);
		try {
			List<WebElement> w = driver.findElements(By
					.xpath("//span[starts-with(text(),'0.00')]"));
			int s = w.size();
			log.info("Elements list with '0.00' : " + s);
			for (int i = 0; i < s; i++) {
				if (i == 0) {
					driver.findElement(
							By.xpath("//div[span[starts-with(text(),'0.00')]]/preceding-sibling::div/a"))
							.click();
					takeScreenShots("timeEntry", data.getGroup(), driver);
					Thread.sleep(3000);
//					List<WebElement> we = driver.findElements(By
//							.xpath("//span[@id='txtmon']"));
//
//					int s1 = we.size();
//					log.info("Elements with TimeEntry for '0.00' : " + s1);
//					Thread.sleep(3000);
					WebElement element = driver.findElement(By.xpath("//*[@id='txtmon']"));
					if(element.isDisplayed())
						element.click();
					else{
						JavascriptExecutor executor = (JavascriptExecutor)driver;
						executor.executeScript("document.getElementById('txtmon').click();");
					}
					Thread.sleep(2000);
					driver.findElement(By.xpath("//input[@maxlength='5']"))
							.clear();
					Thread.sleep(1500);
					takeScreenShots("timeEntry", data.getGroup(), driver);
					driver.findElement(
							By.xpath("//*[@id='addEntry']/div[1]/div[4]/div[3]/div/input[1]"))
							.sendKeys(inputValues.get("inTime"));
					driver.findElement(
							By.xpath("//*[@id='addEntry']/div[1]/div[4]/div[5]/div/input[1]"))
							.sendKeys(inputValues.get("outTime"));
					takeScreenShots("timeEntry", data.getGroup(), driver);
					log.info("Page Title : " + driver.getTitle());
					driver.findElement(By.xpath("//button[text()='Ok']"))
							.click();
					takeScreenShots("timeEntry", data.getGroup(), driver);
				}
			}
			Thread.sleep(2000);
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
		String groupTests = "AJ_TimeCard";
		// long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().startsWith("HMY_AJ_TimeEntry_V1")) {
						long startTime = System.currentTimeMillis();
						boolean status = timeEntry(data, inputValues);
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
