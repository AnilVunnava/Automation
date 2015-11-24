package com.peoplenet.qa.selenium.hmy.old.tests;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.AccessConfig;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava
 * 
 *         <pre>
 * {@code
 * 	EntryRoundingTests per Employee will be executed from the values obtained from Database
 * }
 * </pre>
 */
public class HMY_NewTimeEntryHPDUserTests extends AbstractSmokeTests {

	/**
	 * @param data
	 * @return true/false
	 *
	 *         <pre>
	 * {@code
	 *        This method call the base methods from Super Class TMCSmokeTests
	 * }
	 * </pre>
	 */
	public boolean EmployeeLoginAndTimeEntry(SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			AccessConfig config = null;
			for (AccessConfig accessConf : accessConfig) {
				if (data.getScript().equals(accessConf.getScript())) {
					config = accessConf;
				} else if (accessConf.getScript().equals("Default")) {
					config = accessConf;
				}
			}
			driver = getDriver(data.getUniqueScript());
			driver.get(config.getURL());
			takeScreenShots("employeeLogin", data.getGroup(), driver);
			userid = driver.findElement(By.id("id"));
			log.info("Employee RecordID : " + inputValues.get("id"));
			userid.clear();
			userid.sendKeys(inputValues.get("id"));
			Thread.sleep(1500);
			takeScreenShots("employeeLogin", data.getGroup(), driver);
			userid.sendKeys(Keys.RETURN);
			Thread.sleep(3000);
			takeScreenShots("employeeLogin", data.getGroup(), driver);

			List<WebElement> w = driver.findElements(By
					.xpath("//span[starts-with(text(),'0.00')]"));
			int s = w.size();
			System.out.println(s);
			for (int i = 0; i < s; i++) {
				if (i == 0) {
					driver.findElement(
							By.xpath("//div[span[starts-with(text(),'0.00')]]/preceding-sibling::div/a"))
							.click();
					Thread.sleep(3000);
					List<WebElement> we = driver.findElements(By
							.xpath("//span[@id='txtmon']"));
					int s1 = we.size();
					System.out.println(s1);
					for (int j = 1; j < s1; j++) {
						if (j == 1) {
							Thread.sleep(3000);
							driver.findElement(By.xpath("//span[@id='txtmon']"))
									.click();
							Thread.sleep(2000);
							driver.findElement(
									By.xpath("//input[@maxlength='5']"))
									.clear();
							Thread.sleep(5000);
							driver.findElement(
									By.xpath("//input[@maxlength='5']"))
									.sendKeys("5");
							System.out.println(driver.getTitle());
							Thread.sleep(8000);
							driver.findElement(
									By.xpath("//button[text()='Ok']")).click();
						}
					}
				}
			}
			if (waitForPageLaod(driver,
					By.xpath(("//div[@id='dashboardleft']")), 20)) {
				String errorMsg = "Pending Approval";
				waitForPageLaod(driver,
						By.xpath(("//div[@id='dashboardleft']/div[2]")), 20);
				String first = driver.findElement(
						By.xpath("//div[@id='dashboardleft']/div[2]/div[3]"))
						.getText();

				waitForPageLaod(driver,
						By.xpath(("//div[@id='dashboardleft']/div[3]")), 20);
				String second = driver.findElement(
						By.xpath("//div[@id='dashboardleft']/div[3]/div[3]"))
						.getText();

				waitForPageLaod(driver,
						By.xpath(("//div[@id='dashboardleft']/div[4]")), 20);
				String third = driver.findElement(
						By.xpath("//div[@id='dashboardleft']/div[4]/div[3]"))
						.getText();

				waitForPageLaod(driver,
						By.xpath(("//div[@id='dashboardleft']/div[5]")), 20);
				String fourth = driver.findElement(
						By.xpath("//div[@id='dashboardleft']/div[5]/div[3]"))
						.getText();

				waitForPageLaod(driver,
						By.xpath(("//div[@id='dashboardleft']/div[6]")), 20);
				String fifth = driver.findElement(
						By.xpath("//div[@id='dashboardleft']/div[6]/div[3]"))
						.getText();

				if ((first.equals(errorMsg))) {
					driver.manage().timeouts()
							.implicitlyWait(15, TimeUnit.SECONDS);
					driver.findElement(
							By.xpath("//div[@id='dashboardleft']/div[2]/div[1]/a"))
							.click();
					takeScreenShots("timeCard", data.getGroup(), driver);
					// Thread.sleep(5000);
					// if (inputValues.get("InOut") != null)
					// timeEntryInOuts(driver, data, inputValues);
				}
				if ((second.equals(errorMsg))) {
					driver.manage().timeouts()
							.implicitlyWait(15, TimeUnit.SECONDS);
					driver.findElement(
							By.xpath("//div[@id='dashboardleft']/div[3]/div[1]/a"))
							.click();
					takeScreenShots("timeCard", data.getGroup(), driver);
					// Thread.sleep(5000);
					// if (inputValues.get("InOut") != null)
					// timeEntryInOuts(driver, data, inputValues);
				}
				if ((third.equals(errorMsg))) {
					driver.manage().timeouts()
							.implicitlyWait(15, TimeUnit.SECONDS);
					driver.findElement(
							By.xpath("//div[@id='dashboardleft']/div[4]/div[1]/a"))
							.click();
					takeScreenShots("timeCard", data.getGroup(), driver);
					// Thread.sleep(5000);
					// if (inputValues.get("InOut") != null)
					// timeEntryInOuts(driver, data, inputValues);
				}
				if ((fourth.equals(errorMsg))) {
					driver.manage().timeouts()
							.implicitlyWait(15, TimeUnit.SECONDS);
					driver.findElement(
							By.xpath("//div[@id='dashboardleft']/div[5]/div[1]/a"))
							.click();
					takeScreenShots("timeCard", data.getGroup(), driver);
					// Thread.sleep(5000);
					// if (inputValues.get("InOut") != null)
					// timeEntryInOuts(driver, data, inputValues);
				}
				if ((fifth.equals(errorMsg))) {
					driver.manage().timeouts()
							.implicitlyWait(15, TimeUnit.SECONDS);
					driver.findElement(
							By.xpath("//div[@id='dashboardleft']/div[6]/div[1]/a"))
							.click();
					takeScreenShots("timeCard", data.getGroup(), driver);
					// Thread.sleep(5000);
					// if (inputValues.get("InOut") != null)
					// timeEntryInOuts(driver, data, inputValues);
				}
			}
			if (driver != null)
				driver.quit();
			return true;
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			if (driver != null)
				driver.quit();
			return false;
		}
	}

	public void timeEntryInOuts(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			Thread.sleep(5000);

			driver.findElement(By.xpath("//input[@data-index_id='1']")).click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).sendKeys(
					inputValues.get("intime"));
			driver.findElement(By.xpath("//input[@tabindex='4']")).click();
			driver.findElement(By.xpath("//input[@tabindex='4']")).sendKeys(
					inputValues.get("outtime"));
			driver.findElement(By.xpath("//input[@tabindex='6']")).click();
			driver.findElement(By.xpath("//input[@tabindex='6']")).sendKeys(
					inputValues.get("start"));
			driver.findElement(By.xpath("//input[@tabindex='8']")).click();
			driver.findElement(By.xpath("//input[@tabindex='8']")).sendKeys(
					inputValues.get("end"));
			Thread.sleep(3000);
			takeScreenShots("timeEntryInOut", data.getGroup(), driver);
			driver.findElement(By.linkText("Copy to Next Day")).click();

			driver.findElement(By.xpath("//input[@data-index_id='2']")).click();
			driver.findElement(By.linkText("Copy to Next Day")).click();

			driver.findElement(By.xpath("//input[@data-index_id='3']")).click();
			driver.findElement(By.linkText("Copy to Next Day")).click();

			driver.findElement(By.xpath("//input[@data-index_id='4']")).click();
			driver.findElement(By.linkText("Copy to Next Day")).click();

			Thread.sleep(2000);
			takeScreenShots("timeEntryInOut", data.getGroup(), driver);

			driver.findElement(By.xpath("//input[@data-index_id='8']")).click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).sendKeys(
					inputValues.get("intime"));
			driver.findElement(By.xpath("//input[@tabindex='4']")).click();
			driver.findElement(By.xpath("//input[@tabindex='4']")).sendKeys(
					inputValues.get("outtime"));
			driver.findElement(By.xpath("//input[@tabindex='6']")).click();
			driver.findElement(By.xpath("//input[@tabindex='6']")).sendKeys(
					inputValues.get("start"));
			driver.findElement(By.xpath("//input[@tabindex='8']")).click();
			driver.findElement(By.xpath("//input[@tabindex='8']")).sendKeys(
					inputValues.get("end"));
			driver.findElement(By.linkText("Copy to Next Day")).click();
			takeScreenShots("timeEntryInOut", data.getGroup(), driver);
			driver.findElement(By.xpath("//input[@data-index_id='9']")).click();
			driver.findElement(By.linkText("Copy to Next Day")).click();

			driver.findElement(By.xpath("//input[@data-index_id='10']"))
					.click();
			driver.findElement(By.linkText("Copy to Next Day")).click();

			driver.findElement(By.xpath("//input[@data-index_id='11']"))
					.click();
			driver.findElement(By.linkText("Copy to Next Day")).click();
			takeScreenShots("timeEntryInOut", data.getGroup(), driver);

			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[contains(.,'Submit All')]"))
					.click();

			String errormsg = "An error occurred. Unable to submit assignment. Please correct the overlapping entries on your timesheet that have been highlighted in Red and submit your timesheet again.";
			String display = driver.findElement(
					By.xpath("/html/body/div[4]/div[1]/div[2]/div")).getText();

			if (driver.findElement(
					By.xpath("/html/body/div[4]/div[1]/div[2]/div"))
					.isDisplayed()) {
				display.equals(errormsg);
			}

			else {
				log.info(data.getScript() + "-"
						+ "ERROR MESSAGE IS NOT DISPLAYED CORRECTLY");
			}
			takeScreenShots("timeEntryInOut", data.getGroup(), driver);
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			driver.findElement(By
					.xpath("//*[@class='timeEntry week timeEntryError']"));

			driver.findElement(By.xpath("//table/tbody[2]/tr[2]/td[3]/input"))
					.click();
			Thread.sleep(1000);
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
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
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals("HPD_New_UserTimeEntry")
					|| data.getGroup().equals("ExemptTest")) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().startsWith("HMY_NewTimeEntry")) {
						long startTime = System.currentTimeMillis();
						try {
							boolean status = EmployeeLoginAndTimeEntry(data,
									inputValues);
							data.setStatus(String.valueOf(status));
						} catch (Exception e) {
							log.error("Exception : " + e.getMessage());
							data.setStatus("false");
						}
						long endTime = System.currentTimeMillis();
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
		HtmlReportGenerator.setHtmlContent(content);
	}
}