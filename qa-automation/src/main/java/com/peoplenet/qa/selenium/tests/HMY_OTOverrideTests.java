package com.peoplenet.qa.selenium.tests;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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
 * 	OTOverRideTests per Employee will be executed from the values obtained from Database
 * }
 * </pre>
 */
public class HMY_OTOverrideTests extends AbstractSmokeTests {

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
			Map<String, String> inputValues, Map<String, String> credentials) {
		try {
			AccessConfig config = null;
			for (AccessConfig accessConf : accessConfig) {
				if (accessConf.getScript().equals("Default")) {
					config = accessConf;
				} else if (data.getScript().equals(accessConf.getScript())) {
					config = accessConf;
				}
			}
			for (Map.Entry<String, String> login : credentials.entrySet()) {
				driver = getDriver(data.getUniqueScript());
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
				takeScreenShots("/otOverride/employeeLogin", data.getGroup(),
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
				takeScreenShots("/otOverride/employeeLogin", data.getGroup(),
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
						otOverrideTimeEntry(driver, data, inputValues);
						otOverrideValidation(driver, data, inputValues);
						break;
					}
					if ((third.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[3]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						otOverrideTimeEntry(driver, data, inputValues);
						otOverrideValidation(driver, data, inputValues);
						break;
					}
					if ((fourth.equals(errorMsg))) {

						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[4]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						otOverrideTimeEntry(driver, data, inputValues);
						otOverrideValidation(driver, data, inputValues);
						break;
					}
					if ((fifth.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[5]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						otOverrideTimeEntry(driver, data, inputValues);
						otOverrideValidation(driver, data, inputValues);
						break;
					}
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

	private boolean otOverrideTimeEntry(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			String intime = inputValues.get("inTime");// "11.00";
			String outtime = inputValues.get("outTime");// "7.00";
			String start = inputValues.get("start");// "4.05";
			String end = inputValues.get("end");// "4.25";

			WebElement monentertime = driver.findElement(By
					.xpath("//table/tbody[2]/tr[2]/td[3]/input"));
			monentertime.click();
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			WebElement monenterintime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[5]/input"));
			monenterintime.click();
			monenterintime.sendKeys(intime);
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			WebElement monenterouttime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[6]/input"));
			monenterouttime.click();
			monenterouttime.sendKeys(outtime);
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement pmMon = wait
					.until(ExpectedConditions.visibilityOfElementLocated((By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[6]/button"))));
			pmMon.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			WebElement monenterbreakstarttime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[8]/table/tbody/tr/td[3]/input"));
			monenterbreakstarttime.click();
			monenterbreakstarttime.sendKeys(start);

			driver.findElement(By.xpath("//button[contains(.,'AM')]")).click();
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			WebElement monenterbreakendtime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[8]/table/tbody/tr/td[4]/input"));
			monenterbreakendtime.click();
			monenterbreakendtime.sendKeys(end);
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			WebDriverWait monwait = new WebDriverWait(driver, 10);
			WebElement pmMonbreak = monwait
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[8]/table/tbody/tr/td[4]/button")));
			pmMonbreak.click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			driver.findElement(By.linkText("Copy to Next Day")).click();
			// /start on Tuesday
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			WebElement tuesentertime = driver.findElement(By
					.xpath("//table/tbody[2]/tr[2]/td[4]/input"));
			tuesentertime.click();
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			driver.findElement(By.linkText("Copy to Next Day")).click();

			// /start on Wednesday
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement wedentertime = driver.findElement(By
					.xpath("//table/tbody[2]/tr[2]/td[5]/input"));
			wedentertime.click();
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			driver.findElement(By.linkText("Copy to Next Day")).click();

			// /start on Thursday
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement thursentertime = driver.findElement(By
					.xpath("//table/tbody[2]/tr[2]/td[6]/input"));
			thursentertime.click();
			driver.findElement(By.linkText("Copy to Next Day")).click();

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement approval = driver.findElement(By
					.xpath("//button[contains(.,'Submit All')]"));
			approval.click();
			takeScreenShots("/otOverride/otOverrideTimeEntry", data.getGroup(),
					driver);
			Thread.sleep(5000);
			return true;
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	private boolean otOverrideValidation(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			Thread.sleep(8000);
			WebElement myDynamicElement = (new WebDriverWait(driver, 60))
					.until(ExpectedConditions.presenceOfElementLocated(By
							.xpath("//button[contains(.,'Show OT')]")));
			takeScreenShots("/otOverride/otOverrideValidation",
					data.getGroup(), driver);
			myDynamicElement.click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[contains(.,'Hide OT')]"))
					.click();
			takeScreenShots("/otOverride/otOverrideValidation",
					data.getGroup(), driver);
			driver.findElement(By.xpath("//button[contains(.,'Show OT')]"))
					.click();
			Thread.sleep(2000);
			takeScreenShots("/otOverride/otOverrideValidation",
					data.getGroup(), driver);
			driver.findElement(By.xpath("//button[contains(.,'Save OT')]"))
					.click();
			takeScreenShots("/otOverride/otOverrideValidation",
					data.getGroup(), driver);
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
		// String groupTests = "OTOverrideTest";
		// long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals("OTOverrideTest")) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().contains("OTOverride")) {
						long startTime = System.currentTimeMillis();
						try {
							Map<String, String> credentials = DatabaseFactory
									.getQueryResults(inputValues.get("query"));
							boolean status = EmployeeLoginAndTimeEntry(data,
									inputValues, credentials);
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
		// long end = System.currentTimeMillis();
		// String group = HtmlReportGenerator.getGroupHtml(groupTests + "	["
		// + HtmlReportGenerator.getTime(end - start) + "]");
		HtmlReportGenerator.setHtmlContent(content);
	}
}