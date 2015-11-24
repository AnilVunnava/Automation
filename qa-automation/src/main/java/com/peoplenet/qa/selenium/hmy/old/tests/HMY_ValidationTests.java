package com.peoplenet.qa.selenium.hmy.old.tests;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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
 *         TMC RunReport Tests
 */
public class HMY_ValidationTests extends AbstractSmokeTests {

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TMC Validate Test based on the input values [Client,Group and
	 *         Site] where it generates report for 'All Days Total Hours-By
	 *         Employee'
	 */
	public boolean ValidationTest(SuiteConfig data,
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
				takeScreenShots("employeeLogin", data.getGroup(), driver);
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
				takeScreenShots("employeeLogin", data.getGroup(), driver);
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
						if (inputValues.get("validate").equals("error"))
							validateError(driver, data, inputValues);
						else if (inputValues.get("validate").equals("noerror"))
							validate(driver, data, inputValues);
						else if (inputValues.get("validate").equals(
								"breakerror"))
							breakTimeError(driver, data, inputValues);
						break;
					}
					if ((third.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[3]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						if (inputValues.get("validate").equals("error"))
							validateError(driver, data, inputValues);
						else if (inputValues.get("validate").equals("noerror"))
							validate(driver, data, inputValues);
						else if (inputValues.get("validate").equals(
								"breakerror"))
							breakTimeError(driver, data, inputValues);
						break;
					}
					if ((fourth.equals(errorMsg))) {

						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[4]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						if (inputValues.get("validate").equals("error"))
							validateError(driver, data, inputValues);
						else if (inputValues.get("validate").equals("noerror"))
							validate(driver, data, inputValues);
						else if (inputValues.get("validate").equals(
								"breakerror"))
							breakTimeError(driver, data, inputValues);
						break;
					}
					if ((fifth.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[5]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						if (inputValues.get("validate").equals("error"))
							validateError(driver, data, inputValues);
						else if (inputValues.get("validate").equals("noerror"))
							validate(driver, data, inputValues);
						else if (inputValues.get("validate").equals(
								"breakerror"))
							breakTimeError(driver, data, inputValues);
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

	public boolean validate(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String output = driver.findElement(
					By.xpath("//input[@data-index_id]")).getAttribute(
					"data-index_id");
			driver.findElement(
					By.xpath("//input[@data-index_id='" + output + "'] "))
					.click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String initialdisplay = driver.findElement(
					By.xpath("//table/tbody/tr[2]/td[2]/div[1]/ul/li"))
					.getText();
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			String expecteddisplay = "REMINDER: Your IN TIME for this day must not be before: ";
			if (initialdisplay != null)
				initialdisplay.contains(expecteddisplay);
			else
				log.info(data.getScript() + "-"
						+ "THE REMINDER DISPLAY IS INCORRECT");
			driver.findElement(By.xpath("//input[@tabindex='2']")).sendKeys(
					inputValues.get("inTime"));
			driver.findElement(By.xpath("//input[@tabindex='4']")).click();
			String textdisplayred = driver.findElement(
					By.className("validation")).getText();
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			if ((textdisplayred).equals("In/Out Times are invalid."))
				driver.findElement(By.className("validation")).equals(
						"In/Out Times are invalid.");
			else
				log.info(data.getScript() + "-"
						+ "In/Out Times IS NOT DISPLAYED CORRECTLY");
			driver.findElement(By.xpath("//input[@tabindex='4']")).sendKeys(
					inputValues.get("outTime"));

			String initialdisplay3 = driver.findElement(
					By.className("validation")).getText();
			String expecteddisplay3 = "REMINDER: Your OUT TIME for this day must be no later than: ";
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			if (initialdisplay3 != null)
				initialdisplay3.contains(expecteddisplay3);
			else
				log.info(data.getScript() + "-"
						+ "THE REMINDER DISPLAY IS INCORRECT");
			driver.findElement(By
					.xpath("//*[@class='timeEntry week timeEntryError']"));

			String textdisplayred2 = driver.findElement(
					By.className("validation")).getText();
			if ((textdisplayred2).equals("In/Out Times are invalid."))
				driver.findElement(
						By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/ul/li"))
						.equals("In/Out Times are invalid.");
			else
				log.info(data.getScript() + "-"
						+ "In/Out Times IS NOT DISPLAYED CORRECTLY");
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			driver.findElement(By.xpath("//input[@tabindex='6']")).click();
			driver.findElement(By.xpath("//input[@tabindex='6']")).sendKeys(
					inputValues.get("start"));
			String initialdisplay4 = driver.findElement(
					By.className("validation")).getText();
			String expecteddisplay4 = "REMINDER: Your IN TIME for this day must not be before: 12:00";
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			if (initialdisplay4 != null)
				initialdisplay4.equals(expecteddisplay4);
			else
				log.info(data.getScript() + "-"
						+ "THE REMINDER DISPLAY IS INCORRECT");
			driver.findElement(By
					.xpath("//*[@class='timeEntry week timeEntryError']"));
			String textdisplayred3 = driver.findElement(
					By.className("validation")).getText();
			if ((textdisplayred3).equals("In/Out Times are invalid."))
				driver.findElement(
						By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/ul/li"))
						.equals("In/Out Times are invalid.");
			else
				log.info(data.getScript() + "-"
						+ "In/Out Times IS NOT DISPLAYED CORRECTLY");
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//input[@tabindex='8']")).click();
			driver.findElement(By.xpath("//input[@tabindex='8']")).sendKeys(
					inputValues.get("end"));

			String initialdisplay5 = driver.findElement(
					By.className("validation")).getText();
			String expecteddisplay5 = "REMINDER: Your IN TIME for this day must not be before: ";
			if (initialdisplay5 != null)
				initialdisplay5.contains(expecteddisplay5);
			else
				log.info(data.getScript() + "-"
						+ "THE REMINDER DISPLAY IS INCORRECT");
			driver.findElement(By
					.xpath("//*[@class='timeEntry week timeEntryError']"));
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			String textdisplayred4 = driver.findElement(
					By.className("validation")).getText();
			if ((textdisplayred4).equals("In/Out Times are invalid."))
				driver.findElement(
						By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/ul/li"))
						.equals("In/Out Times are invalid.");
			else
				log.info(data.getScript() + "-"
						+ "In/Out Times IS NOT DISPLAYED CORRECTLY");
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			WebDriverWait day1wait = new WebDriverWait(driver, 10);
			WebElement pmday1break = day1wait
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[8]/table/tbody/tr/td[4]/button")));
			JavascriptExecutor executorday1break = (JavascriptExecutor) driver;
			executorday1break.executeScript("arguments[0].click();",
					pmday1break);
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			// data-index_id="8"
			// ////////////Start on
			// Day8//////////////////////////////////////////////////////////////////////////////////
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//input[@data-index_id='8']")).click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).sendKeys(
					inputValues.get("inTime"));

			driver.findElement(By.xpath("//input[@tabindex='4']")).click();
			driver.findElement(By.xpath("//input[@tabindex='4']")).sendKeys(
					inputValues.get("outTime"));

			driver.findElement(By.xpath("//input[@tabindex='6']")).click();
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			String initialdisplay8 = driver.findElement(
					By.className("validation")).getText();
			String expecteddisplay8 = "REMINDER: Your IN TIME for this day must not be before: ";

			if (initialdisplay8.contains(expecteddisplay8))
				initialdisplay8.contains(expecteddisplay8);
			driver.findElement(By
					.xpath("//*[@class='timeEntry week timeEntryError']"));
			String textdisplayred8 = driver
					.findElement(
							By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/ul/li"))
					.getText();
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			if ((textdisplayred8).equals("In/Out Times are invalid."))
				driver.findElement(
						By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/ul/li"))
						.equals("In/Out Times are invalid.");
			else
				log.info(data.getScript() + "-"
						+ "In/Out Times IS NOT DISPLAYED CORRECTLY");
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String initialdisplay81 = driver.findElement(
					By.xpath("//table/tbody/tr[2]/td[2]/div[1]/ul/li"))
					.getText();
			String expecteddisplay81 = "REMINDER: Your IN TIME for this day must not be before: ";
			if (initialdisplay81 != null)
				initialdisplay81.contains(expecteddisplay81);
			else
				log.info(data.getScript() + "-"
						+ "THE REMINDER DISPLAY IS INCORRECT");
			driver.findElement(By
					.xpath("//*[@class='timeEntry week timeEntryError']"));
			String textdisplayred82 = driver.findElement(
					By.className("validation")).getText();
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			if ((textdisplayred82).equals("In/Out Times are invalid."))
				driver.findElement(
						By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/ul/li"))
						.equals("In/Out Times are invalid.");
			else
				log.info(data.getScript() + "-"
						+ "In/Out Times IS NOT DISPLAYED CORRECTLY");
			driver.findElement(By.xpath("//input[@tabindex='6']")).sendKeys(
					inputValues.get("start"));
			String initialdisplay83 = driver.findElement(
					By.xpath("//table/tbody/tr[2]/td[2]/div[1]/ul/li"))
					.getText();
			String expecteddisplay83 = "REMINDER: Your IN TIME for this day must not be before: ";
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			if (initialdisplay83 != null)
				initialdisplay83.contains(expecteddisplay83);
			else
				log.info(data.getScript() + "-"
						+ "THE REMINDER DISPLAY IS INCORRECT");
			driver.findElement(By
					.xpath("//*[@class='timeEntry week timeEntryError']"));

			String textdisplayred84 = driver
					.findElement(
							By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/ul/li"))
					.getText();
			if ((textdisplayred84).equals("In/Out Times are invalid."))
				driver.findElement(
						By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/ul/li"))
						.equals("In/Out Times are invalid.");
			else
				log.info(data.getScript() + "-"
						+ "In/Out Times IS NOT DISPLAYED CORRECTLY");
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//input[@tabindex='8']")).click();
			driver.findElement(By.xpath("//input[@tabindex='8']")).sendKeys(
					inputValues.get("end"));
			String initialdisplay85 = driver.findElement(
					By.xpath("//table/tbody/tr[2]/td[2]/div[1]/ul/li"))
					.getText();
			String expecteddisplay85 = "REMINDER: Your OUT TIME for this day must be no later than: ";
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			if (initialdisplay85 != null)
				initialdisplay85.contains(expecteddisplay85);
			else
				log.info(data.getScript() + "-"
						+ "THE REMINDER DISPLAY IS INCORRECT");
			driver.findElement(By
					.xpath("//*[@class='timeEntry week timeEntryError']"));

			String textdisplayred86 = driver.findElement(
					By.className("validation")).getText();
			if ((textdisplayred86).equals("In/Out Times are invalid."))
				driver.findElement(
						By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[2]/td/ul/li"))
						.equals("In/Out Times are invalid.");
			else
				log.info(data.getScript() + "-"
						+ "In/Out Times IS NOT DISPLAYED CORRECTLY");
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			Thread.sleep(2500);
			return true;
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean validateError(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			// ////////////Start on
			// Day1//////////////////////////////////////////////////////////////////////////////////
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String output = driver.findElement(
					By.xpath("//input[@data-index_id]")).getAttribute(
					"data-index_id");

			driver.findElement(
					By.xpath("//input[@data-index_id='" + output + "'] "))
					.click();
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			driver.findElement(By.xpath("//input[@tabindex='2']")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

			String initialdisplay = driver.findElement(
					By.xpath("//table/tbody/tr[2]/td[2]/div[1]/ul/li"))
					.getText();
			String[] parts = initialdisplay
					.split("REMINDER: Your IN TIME for this day must not be before: ");
			String part2 = parts[1];
			String retrieve = (part2.toString());
			driver.findElement(By.xpath("//input[@tabindex='2']")).sendKeys(
					retrieve);
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			driver.findElement(By.xpath("//button[contains(.,'AM')]")).click();
			driver.findElement(By.xpath("//input[@tabindex='4']")).click();
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			String Maxtime = "12.00";
			int compare = retrieve.compareToIgnoreCase(Maxtime);
			if (compare > 0)
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			else if (compare < 0) {
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.findElement(
						By.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[5]/button"))
						.click();
			}
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			WebElement day1enterouttime = driver.findElement(By
					.xpath("//input[@tabindex='4']"));
			day1enterouttime.click();
			day1enterouttime.sendKeys(inputValues.get("outTime"));
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			driver.findElement(By.linkText("[ Close ]")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String TotalDay1 = driver.findElement(
					By.xpath("//table/tbody[3]/tr/td[3]")).getText();
			driver.findElement(By.xpath("//input[@data-index_id='8']")).click();
			driver.findElement(By.xpath("//input[@tabindex='2']")).click();
			WebElement day8entertime = driver.findElement(By
					.xpath("//input[@tabindex='2']"));
			day8entertime.click();
			day8entertime.sendKeys(inputValues.get("inTime"));
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			String initialdisplay1 = driver.findElement(
					By.xpath("//table/tbody/tr[2]/td[2]/div[1]/ul/li"))
					.getText();
			String[] parts1 = initialdisplay1
					.split("REMINDER: Your OUT TIME for this day must be no later than: ");
			String part21 = parts1[1];
			String retrieve1 = (part21.toString());
			WebElement day8enterouttime = driver.findElement(By
					.xpath("//input[@tabindex='4']"));
			day8enterouttime.click();
			day8enterouttime.sendKeys(retrieve1);
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			driver.findElement(By.linkText("[ Close ]")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			String TotalDay8 = driver.findElement(
					By.xpath("//table/tbody[3]/tr/td[10]")).getText();
			driver.findElement(By.xpath("//button[contains(.,'Submit')]"))
					.click();
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			Thread.sleep(10000);

			if (driver.findElements(By.xpath("//button[contains(.,'Modify')]"))
					.size() > 1) {
				driver.findElement(By.xpath("//button[contains(.,'Modify')]"))
						.click();
				takeScreenShots("/validation/validate", data.getGroup(), driver);
				String NextDayStringDay1 = driver.findElement(
						By.xpath("//table/tbody[2]/tr[2]/td[4]/input"))
						.getText();
				String NextDayStringDay8 = driver.findElement(
						By.xpath("//table/tbody[2]/tr[2]/td[10]/input"))
						.getText();

				if ((NextDayStringDay1).equals(TotalDay1))
					driver.findElement(
							By.xpath("//table/tbody[2]/tr[2]/td[4]/input"))
							.getText().equals(TotalDay1);
				else
					log.info(data.getScript() + "-"
							+ "DAY 1 IS NOT DISPLAYED CORRECTLY");
				takeScreenShots("/validation/validate", data.getGroup(), driver);
				if ((NextDayStringDay8).equals(TotalDay8))
					driver.findElement(
							By.xpath("//table/tbody[2]/tr[2]/td[10]/input"))
							.getText().equals(TotalDay8);
				else
					log.info(data.getScript() + "-"
							+ "DAY 8 IS NOT DISPLAYED CORRECTLY");
				takeScreenShots("/validation/validate", data.getGroup(), driver);
			}
			if (driver.findElements(By.xpath("//button[contains(.,'ShowOT')]"))
					.size() > 1) {
				driver.findElement(By.xpath("//button[contains(.,'Show OT')]"))
						.click();
				String NextDayStringDay1 = driver.findElement(
						By.xpath("//table/tbody[3]/tr/td[4]")).getText();
				String NextDayStringDay8 = driver.findElement(
						By.xpath("//table/tbody[3]/tr/td[10]")).getText();
				takeScreenShots("/validation/validate", data.getGroup(), driver);
				if ((NextDayStringDay1).equals(TotalDay1))
					driver.findElement(By.xpath("//table/tbody[3]/tr/td[4]"))
							.getText().equals(TotalDay1);
				else
					log.info(data.getScript() + "-"
							+ "DAY 1 IS NOT DISPLAYED CORRECTLY");
				takeScreenShots("/validation/validate", data.getGroup(), driver);
				if ((NextDayStringDay8).equals(TotalDay8))
					driver.findElement(By.xpath("//table/tbody[3]/tr/td[10]"))
							.getText().equals(TotalDay8);
				else
					log.info(data.getScript() + "-"
							+ "DAY 8 IS NOT DISPLAYED CORRECTLY");
			}
			takeScreenShots("/validation/validate", data.getGroup(), driver);
			Thread.sleep(2500);
			return true;
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean breakTimeError(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//table/tbody[2]/tr[2]/td[3]/input"))
					.click();
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			Thread.sleep(2500);
			WebElement monenterintime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[5]/input"));
			monenterintime.click();
			monenterintime.sendKeys(inputValues.get("inTime"));
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			WebElement monenterouttime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[6]/input"));
			monenterouttime.click();
			monenterouttime.sendKeys(inputValues.get("outTime"));
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);

			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.findElement(By.linkText("Copy to Next Day")).click();
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			// /start on Tuesday
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//table/tbody[2]/tr[2]/td[4]/input"))
					.click();
			driver.findElement(By.linkText("Copy to Next Day")).click();
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			// /start on Wednesday
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//table/tbody[2]/tr[2]/td[5]/input"))
					.click();
			driver.findElement(By.linkText("Copy to Next Day")).click();
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			// /start on Thursday
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//table/tbody[2]/tr[2]/td[6]/input"))
					.click();
			driver.findElement(By.linkText("Copy to Next Day")).click();
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			// /start on Friday
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//table/tbody[2]/tr[2]/td[7]/input"))
					.click();
			driver.findElement(By.linkText("Copy to Next Day")).click();
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			// /start on Saturday
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//table/tbody[2]/tr[2]/td[8]/input"))
					.click();
			driver.findElement(By.linkText("Copy to Next Day")).click();
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
			driver.findElement(By.xpath("//button[contains(.,'Submit All')]"))
					.click();
			
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement notice = driver.findElement(By
					.xpath("/html/body/div[4]/div[1]/div[2]/div/font"));
			String noticevalue = notice.getText();
			String noticealert = "I hereby attest that the time and hours recorded on this time record accurately and fully identify all time that I have worked during the designated pay period. I further acknowledge that, other than as indicated in this time record, I have been provided all meal and rest periods to which I am entitled under the law during the pay period, including one meal period of at least 30 minutes by the fifth hour whenever I worked more than five hours and a second meal break of at least 30 minutes by the tenth hour whenever I worked more than 10 hours.";
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			if ((noticevalue).equals(noticealert))
				driver.findElement(
						By.xpath("/html/body/div[4]/div[1]/div[1]/span"))
						.equals(noticealert);
			else
				log.info(data.getScript() + "-"
						+ "Notice ALert is displayed incorrectly");
			driver.findElement(By.xpath("//button[contains(.,'Accept')]"))
					.click();

			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

			driver.findElement(
					By.xpath("//table/tbody[2]/tr[2]/td[13]/div[2]/div[1]/button"))
					.click();
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			Thread.sleep(3600);

			String expectedtext = driver.findElement(
					By.xpath("//table/tbody[2]/tr[2]/td[11]/span[2]"))
					.getText();

			if ((expectedtext).equals("Saved Not Submitted"))
				driver.findElement(
						By.xpath("//table/tbody[2]/tr[2]/td[11]/span[2]"))
						.equals("Saved Not Submitted");
			else
				log.info(data.getScript() + "-"
						+ "Saved Not Submitted is not displayed correctly");
			takeScreenShots("/breakTimeError/validate", data.getGroup(), driver);
			Thread.sleep(2500);
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
		String groupTests = "ValidationTests";
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
					if (data.getScript().contains("Validation")
							|| data.getScript().contains("Break")) {
						long startTime = System.currentTimeMillis();
						boolean status = ValidationTest(data, inputValues,
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
