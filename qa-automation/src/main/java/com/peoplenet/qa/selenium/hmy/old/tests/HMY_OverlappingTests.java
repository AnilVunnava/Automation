package com.peoplenet.qa.selenium.hmy.old.tests;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.AccessConfig;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.db.config.DatabaseFactory;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava
 *
 *         TMC RunReport Tests
 */
public class HMY_OverlappingTests extends AbstractSmokeTests {

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TMC Overlapping Test based on the input values [Client,Group and
	 *         Site] where it generates report for 'All Days Total Hours-By
	 *         Employee'
	 */
	public boolean OverlappingTest(SuiteConfig data,
			Map<String, String> inputValues, Map<String, String> dbValues) {
		try {
			AccessConfig config = null;
			for (AccessConfig accessConf : accessConfig) {
				if (data.getScript().equals(accessConf.getScript())) {
					config = accessConf;
				} else if (accessConf.getScript().equals("Default")) {
					config = accessConf;
				}
			}
			for (Map.Entry<String, String> login : dbValues.entrySet()) {
				driver = getDriver(data.getUniqueScript());
				driver.manage().window().maximize();
				driver.get(config.getURL());
				userid = driver.findElement(By.id("UserID"));
				password = driver.findElement(By.id("Password"));
				submit = driver.findElement(By.id("SignIn"));
				log.info("Employee User : " + login.getKey()
						+ " Employee Password : " + login.getValue());
				userid.clear();
				userid.sendKeys(login.getKey());
				password.clear();
				password.sendKeys(login.getValue());
				submit.click();
				takeScreenShots("/overlapping/employeeLogin", data.getGroup(),
						driver);
				Thread.sleep(1500);
				if (driver.findElements(By.id("oldPassword")).size() > 0) {
					driver.findElement(By.id("oldPassword")).clear();
					driver.findElement(By.id("oldPassword")).sendKeys(
							(login.getValue()));

					driver.findElement(By.id("newPassword")).clear();
					driver.findElement(By.id("newPassword")).sendKeys(
							(login.getValue() + 1));

					driver.findElement(By.id("confirmedPassword")).clear();
					driver.findElement(By.id("confirmedPassword")).sendKeys(
							(login.getValue() + 1));

					driver.findElement(By.id("resetPasswordSubmit")).click();

					Thread.sleep(3000);
					driver.findElement(By.id("login_redirect")).click();

					driver.findElement(By.id("UserID")).sendKeys(
							(login.getKey()));
					driver.findElement(By.id("Password")).sendKeys(
							(login.getValue() + 1));

					driver.findElement(By.id("SignIn")).click();

				}
				takeScreenShots("/overlapping/employeeLogin", data.getGroup(),
						driver);
				if (waitForPageLaod(driver,
						By.xpath(("//table/tbody[1]/tr[2]/td[3]/div")), 20)) {
					String errorMsg = "No Time Entered";
					waitForPageLaod(driver,
							By.xpath(("//table/tbody[1]/tr[2]/td[3]/div")), 20);
					String second = driver.findElement(
							By.xpath("//table/tbody[1]/tr[2]/td[3]/div"))
							.getText();

					waitForPageLaod(driver,
							By.xpath(("//table/tbody[1]/tr[3]/td[3]/div")), 20);
					String third = driver.findElement(
							By.xpath("//table/tbody[1]/tr[3]/td[3]/div"))
							.getText();

					waitForPageLaod(driver,
							By.xpath(("//table/tbody[1]/tr[4]/td[3]/div")), 20);
					String fourth = driver.findElement(
							By.xpath("//table/tbody[1]/tr[4]/td[3]/div"))
							.getText();

					waitForPageLaod(driver,
							By.xpath(("//table/tbody[1]/tr[5]/td[3]/div")), 20);
					String fifth = driver.findElement(
							By.xpath("//table/tbody[1]/tr[5]/td[3]/div"))
							.getText();

					if ((second.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[2]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						overlapNormal(driver, data, inputValues);
						break;
					}
					if ((third.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[3]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						overlapNormal(driver, data, inputValues);
						break;
					}
					if ((fourth.equals(errorMsg))) {

						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[4]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						overlapNormal(driver, data, inputValues);
						break;
					}
					if ((fifth.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[5]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						overlapNormal(driver, data, inputValues);
						break;
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

	public boolean overlapNormal(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//input[@data-index_id='1'] "))
					.click();
			Thread.sleep(1000);
			driver.findElement(By.xpath("//input[@tabindex='2']")).click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).sendKeys(
					inputValues.get("inTime"));
			driver.findElement(By.xpath("//input[@tabindex='4']")).click();
			driver.findElement(By.xpath("//input[@tabindex='4']")).sendKeys(
					inputValues.get("outTime"));
			takeScreenShots("/overlapping/overlapping", data.getGroup(), driver);
			Thread.sleep(1000);
			driver.findElement(By.linkText("[ Close ]")).click();
			driver.findElement(By.xpath("//input[@data-index_id='8'] "))
					.click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).sendKeys(
					inputValues.get("inTime"));
			driver.findElement(By.xpath("//input[@tabindex='4']")).click();
			driver.findElement(By.xpath("//input[@tabindex='4']")).sendKeys(
					inputValues.get("outTime"));
			takeScreenShots("/overlapping/overlapping", data.getGroup(), driver);
			Thread.sleep(1000);
			driver.findElement(By.linkText("[ Close ]")).click();
			Thread.sleep(1500);
			driver.findElement(
					By.xpath("//button[contains(.,'"
							+ inputValues.get("submit") + "')]")).click();
			Thread.sleep(5000);
			takeScreenShots("/overlapping/overlappingExpected",
					data.getGroup(), driver);
			String submitAllErrormsg = "An error occurred. Unable to submit assignment. Please correct the overlapping entries on your timesheet that have been highlighted in Red and submit your timesheet again.";
			String aubmitAssigErrormsg = "Please correct the overlapping entries on your timesheet.";
			String display = driver.findElement(
					By.xpath("/html/body/div[4]/div[1]/div[2]/div")).getText();

			if (driver.findElement(
					By.xpath("/html/body/div[4]/div[1]/div[2]/div"))
					.isDisplayed()) {
				if (inputValues.get("submit").equals("Submit All"))
					display.equals(submitAllErrormsg);
				else
					display.equals(aubmitAssigErrormsg);
			} else {
				log.info(data.getScript()
						+ " ERROR MESSAGE IS NOT DISPLAYED CORRECTLY");
			}
			return true;
		} catch (Exception e) {
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
		String groupTests = "OverlappingTests";
		// long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					Map<String, String> credentials = DatabaseFactory
							.getQueryResults(inputValues.get("query"));
					if (data.getScript().contains("Overlapping")) {
						long startTime = System.currentTimeMillis();
						boolean status = OverlappingTest(data, inputValues,
								credentials);
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
