package com.peoplenet.qa.selenium.hmy.old.tests;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
 * 	EntryRoundingTests per Employee will be executed from the values obtained from Database
 * }
 * </pre>
 */
public class HMY_TimeEntry_HPDUserTests extends AbstractSmokeTests {

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
				Thread.sleep(1500);
				takeScreenShots("employeeLogin", data.getGroup(), driver);
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
						if (inputValues.get("InOut") != null)
							timeEntryInOuts(driver, data, inputValues);
						else if (inputValues.get("exempt") != null)
							exemptTimeEntry(driver, data, inputValues);
						else
							timeEntry(driver, data, inputValues);
						break;
					}
					if ((third.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[3]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						if (inputValues.get("InOut") != null)
							timeEntryInOuts(driver, data, inputValues);
						else if (inputValues.get("exempt") != null)
							exemptTimeEntry(driver, data, inputValues);
						else
							timeEntry(driver, data, inputValues);
						break;
					}
					if ((fourth.equals(errorMsg))) {

						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[4]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						if (inputValues.get("InOut") != null)
							timeEntryInOuts(driver, data, inputValues);
						else if (inputValues.get("exempt") != null)
							exemptTimeEntry(driver, data, inputValues);
						else
							timeEntry(driver, data, inputValues);
						break;
					}
					if ((fifth.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[5]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						if (inputValues.get("InOut") != null)
							timeEntryInOuts(driver, data, inputValues);
						else if (inputValues.get("exempt") != null)
							exemptTimeEntry(driver, data, inputValues);
						else
							timeEntry(driver, data, inputValues);
						break;
					}
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

	public void timeEntry(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		int count = Integer.valueOf(inputValues.get("count"));
		try {
			for (int i = 1; ((i < 8 && i > 0) && i <= count); i++) {
				waitForPageLaod(driver,
						By.xpath("//input[@data-index_id='" + i + "']"), 25);
				driver.findElement(
						By.xpath("//input[@data-index_id='" + i + "']"))
						.click();
				WebElement time = driver.findElement(By
						.xpath("//input[@data-bind='numericInput: hours']"));
				time.click();
				time.clear();
				if (inputValues.get("time" + i) != null)
					time.sendKeys(inputValues.get("time" + i));
				else
					time.sendKeys(inputValues.get("time"));
				Thread.sleep(1000);
				time.sendKeys(Keys.RETURN);
				String errormsg = "Please make sure the time entered is rounded to the nearest .25 hour. For example, 8.00, 8.25, 8.50, 8.75.";

				if (driver.findElement(By
						.xpath("//table/tbody[2]/tr[2]/td[3]/input")) != null) {
					Thread.sleep(2000);
					driver.findElement(
							By.xpath("//table/tbody[2]/tr[2]/td[3]/input"))
							.equals(errormsg);
					takeScreenShots("timeEntry", data.getGroup(), driver);
					Thread.sleep(2000);
					driver.findElement(By.xpath("//a[@title='Close']")).click();
				} else {
					log.info(data.getScript()+"-"+
							"ERROR MESSAGE IS NOT DISPLAYED CORRECTLY");
				}
			}
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[contains(.,'Submit All')]"))
					.click();
			String finalerrormsg = "Errors must be corrected before submitting.";
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			if (waitForPageLaod(driver, By.xpath("//button[contains(.,'Ok')]"),
					20)) {
				driver.findElement(By.xpath("//button[contains(.,'Ok')]"))
						.equals(finalerrormsg);
				driver.findElement(By.xpath("//button[contains(.,'Ok')]"))
						.click();
			} else {
				takeScreenShots("timeEntryError", data.getGroup(), driver);
				log.info(data.getScript()+"-"+"NO ERROR MESSAGE DISPLAYED");
			}
		} catch (Exception e) {
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
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
				log.info(data.getScript()+"-"+"ERROR MESSAGE IS NOT DISPLAYED CORRECTLY");
			}
			takeScreenShots("timeEntryInOut", data.getGroup(), driver);
			driver.findElement(By.xpath("//button[contains(.,'Ok')]")).click();
			driver.findElement(By
					.xpath("//*[@class='timeEntry week timeEntryError']"));

			driver.findElement(By.xpath("//table/tbody[2]/tr[2]/td[3]/input"))
					.click();
			Thread.sleep(1000);
		} catch (Exception e) {
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
		}
	}

	public boolean exemptTimeEntry(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			if (inputValues.get("exempt").equals("standard")) {
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				Thread.sleep(2000);
				driver.findElement(
						By.xpath("//button[contains(.,'Submit All')]")).click();
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

				driver.findElement(By.xpath("//button[contains(.,'Accept')]"))
						.click();
				Thread.sleep(15000);
				String expectedtext = driver.findElement(
						By.xpath("//table/tbody[2]/tr[2]/td[11]/span[2]"))
						.getText();
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				if ((expectedtext).equals("Pending Approval")) {
					driver.findElement(
							By.xpath("//table/tbody[2]/tr[2]/td[11]/span[2]"))
							.equals("Pending Approval");
				} else {
					log.info(data.getScript()+"-"+
							"Pending Approval is not displayed correctly");
				}
				return true;
			} else if (inputValues.get("exempt").equals("saturday")) {
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				Thread.sleep(2000);
				WebElement time = driver.findElement(By
						.xpath("//table/tbody[2]/tr[2]/td[8]/input"));
				time.click();
				time.clear();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				WebElement satentertime = driver
						.findElement(By
								.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[5]/input"));
				satentertime.clear();
				satentertime.sendKeys(inputValues.get("time"));
				Thread.sleep(1000);
				time.sendKeys(Keys.RETURN);
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				Thread.sleep(2000);
				driver.findElement(By.xpath("//a[@title='Close']")).click();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				Thread.sleep(1000);
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				driver.findElement(
						By.xpath("//button[contains(.,'Submit All')]")).click();
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				Thread.sleep(2000);
				WebElement notice = driver.findElement(By
						.xpath("/html/body/div[4]/div[1]/div[2]/div"));
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				String noticevalue = notice.getText();
				String noticealert = "An error occurred. Unable to submit assignment. The total hours submitted for the week may not exceed 40 hours. Please adjust the timesheet accordingly.";

				if ((noticevalue).equals(noticealert)) {
					driver.findElement(
							By.xpath("/html/body/div[4]/div[1]/div[2]/div"))
							.equals(noticealert);
				} else {
					log.info(data.getScript()+"-"+"Notice Alert is displayed incorrectly");
				}
				driver.findElement(By.xpath("//button[contains(.,'Ok')]"))
						.click();
				return true;
			} else if (inputValues.get("exempt").equals("extra")
					|| inputValues.get("exempt").equals("under")) {
				driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
				Thread.sleep(2000);
				driver.findElement(
						By.xpath("//table/tbody[2]/tr[2]/td[13]/div[1]/div/div[3]"))
						.click();
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				driver.findElement(
						By.xpath("/html/body/div[4]/div[1]/div[3]/button[2]"))
						.click();
				Thread.sleep(2000);
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				int count = Integer.valueOf(inputValues.get("count"));
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				WebElement highlightmon = driver.findElement(By
						.xpath("//table/tbody[2]/tr[2]/td[3]/input"));
				highlightmon.click();
				highlightmon.clear();
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				WebElement monentertime = driver
						.findElement(By
								.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[5]/input"));
				monentertime.clear();
				monentertime.sendKeys(inputValues.get("time"));
				monentertime.sendKeys(Keys.RETURN);
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				driver.findElement(By.linkText("Copy to Next Day")).click();
				Thread.sleep(2000);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				for (int i = 4; ((i < 8 && i > 0) && i <= count); i++) {
					driver.findElement(
							By.xpath("//table/tbody[2]/tr[2]/td[" + i
									+ "]/input")).click();
					driver.findElement(By.linkText("Copy to Next Day")).click();
					takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
					Thread.sleep(2000);
				}
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.findElement(
						By.xpath("//button[contains(.,'Submit All')]")).click();
				Thread.sleep(5000);
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				WebElement notice = driver.findElement(By
						.xpath("/html/body/div[4]/div[1]/div[2]/div"));
				String noticevalue = notice.getText();
				String noticealert = "";
				if (inputValues.get("exempt").equals("extra"))
					noticealert = "Errors must be corrected before submitting.";
				if (inputValues.get("exempt").equals("under"))
					noticealert = "An error occurred. Unable to submit assignment. The total hours for each work day must equal 8. Please adjust the hours on the timesheet.";
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				if ((noticevalue).equals(noticealert)) {
					driver.findElement(
							By.xpath("/html/body/div[4]/div[1]/div[2]/div"))
							.equals(noticealert);
				} else {
					log.info(data.getScript()+"-"+"Notice Alert is displayed incorrectly");
				}
				Thread.sleep(2000);
				driver.findElement(By.xpath("//button[contains(.,'Ok')]"))
						.click();
				return true;
			} else if (inputValues.get("exempt").equals("friday")) {
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				WebElement highlightfri = driver.findElement(By
						.xpath("//table/tbody[2]/tr[2]/td[7]/input"));
				highlightfri.click();
				highlightfri.clear();
				highlightfri.sendKeys(Keys.RETURN);
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
				driver.findElement(
						By.xpath("//button[contains(.,'Submit All')]")).click();
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
				Thread.sleep(2000);
				driver.findElement(By.xpath("//button[contains(.,'Accept')]"))
						.click();
				return true;
			} else if (inputValues.get("exempt").equals("latelunch")) {
				return lateLunchDataEntry(driver, data, inputValues);
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
			return false;
		}
	}

	public boolean lateLunchDataEntry(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			String intime = inputValues.get("inTime");// "11.00";
			String outtime = inputValues.get("outTime");// "7.00";
			String start = inputValues.get("start");// "4.05";
			String end = inputValues.get("end");// "4.25";

			// /start on Monday
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement monentertime = driver
					.findElement(By
							.xpath("/html/body/div[3]/div/div/div[2]/div/table/tbody[2]/tr[2]/td[3]/input"));
			monentertime.click();
			takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
			WebElement monenterintime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[5]/input"));
			monenterintime.click();
			takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
			monenterintime.sendKeys(intime);
			monenterintime.sendKeys(Keys.RETURN);
			WebElement monenterouttime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[6]/input"));
			monenterouttime.click();
			monenterouttime.sendKeys(outtime);
			monenterouttime.sendKeys(Keys.RETURN);
			takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
			WebDriverWait wait = new WebDriverWait(driver, 10);
			WebElement pmMon = wait
					.until(ExpectedConditions.visibilityOfElementLocated((By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[6]/button"))));
			JavascriptExecutor executor = (JavascriptExecutor) driver;
			executor.executeScript("arguments[0].click();", pmMon);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement monenterbreakstarttime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[8]/table/tbody/tr/td[3]/input"));
			monenterbreakstarttime.click();
			monenterbreakstarttime.sendKeys(start);
			monenterbreakstarttime.sendKeys(Keys.RETURN);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement monenterbreakendtime = driver
					.findElement(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[8]/table/tbody/tr/td[4]/input"));
			monenterbreakendtime.click();
			monenterbreakendtime.sendKeys(end);
			monenterbreakendtime.sendKeys(Keys.RETURN);
			takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
			WebDriverWait monwait = new WebDriverWait(driver, 10);
			WebElement pmMonbreak = monwait
					.until(ExpectedConditions.visibilityOfElementLocated(By
							.xpath("//table/tbody/tr[2]/td[2]/table/tbody/tr[3]/td[8]/table/tbody/tr/td[4]/button")));
			JavascriptExecutor executorMonbreak = (JavascriptExecutor) driver;
			executorMonbreak.executeScript("arguments[0].click();", pmMonbreak);

			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			driver.findElement(By.linkText("Copy to Next Day")).click();
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement tuesentertime = driver.findElement(By
					.xpath("//table/tbody[2]/tr[2]/td[4]/input"));
			tuesentertime.click();
			driver.findElement(By.linkText("Copy to Next Day")).click();
			takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement approval = driver.findElement(By
					.xpath("//button[contains(.,'Submit All')]"));
			approval.click();
			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			WebElement notice = driver.findElement(By
					.xpath("/html/body/div[4]/div[1]/div[2]/div/font"));
			String noticevalue = notice.getText();
			String noticealert = "I hereby attest that the time and hours recorded on this time record accurately and fully identify all time that I have worked during the designated pay period. I further acknowledge that, other than as indicated in this time record, I have been provided all meal and rest periods to which I am entitled under the law during the pay period, including one meal period of at least 30 minutes by the fifth hour whenever I worked more than five hours and a second meal break of at least 30 minutes by the tenth hour whenever I worked more than 10 hours.";

			if ((noticevalue).equals(noticealert)) {
				driver.findElement(
						By.xpath("/html/body/div[4]/div[1]/div[1]/span"))
						.equals(noticealert);
			} else {
				log.info(data.getScript()+"-"+"Notice ALert is displayed incorrectly");
			}
			driver.findElement(By.xpath("//button[contains(.,'Accept')]"))
					.click();
			Thread.sleep(8000);
			takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
			waitForPageLaod(
					driver,
					By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul/li/span"),
					60);

			String Notice1 = driver
					.findElement(
							By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul/li/span"))
					.getText();
			if ((Notice1)
					.equals("I was not provided a full 30 minute meal break")) {
				driver.findElement(
						By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul/li/span"))
						.equals("I was not provided a full 30 minute meal break");
			} else {
				log.info(data.getScript()+"-"+
						"I was not provided a full 30 minute meal break");
			}

			String Notice2 = driver
					.findElement(
							By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul/li[2]/span"))
					.getText();
			if ((Notice2)
					.equals("I voluntarily returned early from meal break")) {
				driver.findElement(
						By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul/li[2]/span"))
						.equals("I voluntarily returned early from meal break");
			} else {
				log.info(data.getScript()+"-"+
						"I voluntarily returned early from meal break");
			}

			String Notice3 = driver
					.findElement(
							By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul[2]/li/span"))
					.getText();
			if ((Notice3)
					.equals("I was not provided a full 30 minute meal break")) {
				driver.findElement(
						By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul[2]/li/span"))
						.equals("I was not provided a full 30 minute meal break");
			} else {
				log.info(data.getScript()+"-"+
						"I was not provided a full 30 minute meal break");
			}

			String Notice4 = driver
					.findElement(
							By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul[2]/li[2]/span"))
					.getText();
			if ((Notice4)
					.equals("I voluntarily returned early from meal break")) {
				driver.findElement(
						By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul[2]/li[2]/span"))
						.equals("I voluntarily returned early from meal break");
			} else {
				log.info(data.getScript()+"-"+
						"I voluntarily returned early from meal break");
			}

			String Notice5 = driver
					.findElement(
							By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul[3]/li/span"))
					.getText();
			if ((Notice5)
					.equals("I was not provided a full 30 minute meal break")) {
				driver.findElement(
						By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul[3]/li/span"))
						.equals("I was not provided a full 30 minute meal break");
			} else {
				log.info(data.getScript()+"-"+
						"I was not provided a full 30 minute meal break");
			}

			String Notice6 = driver
					.findElement(
							By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul[3]/li[2]/span"))
					.getText();
			if ((Notice6)
					.equals("I voluntarily returned early from meal break")) {
				driver.findElement(
						By.xpath("/html/body/div[4]/div/div[2]/div/div[4]/ul[3]/li[2]/span"))
						.equals("I voluntarily returned early from meal break");
			} else {
				log.info(data.getScript()+"-"+
						"I voluntarily returned early from meal break");
			}

			driver.findElement(
					By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[4]/ul[1]/li[1]/input"))
					.click();
			driver.findElement(
					By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[4]/ul[2]/li[2]/input"))
					.click();
			driver.findElement(
					By.xpath("/html/body/div[4]/div[1]/div[2]/div/div[4]/ul[3]/li[1]/input"))
					.click();
			takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
			Thread.sleep(3600);

			driver.findElement(
					By.xpath("/html/body/div[4]/div[1]/div[3]/button[2]"))
					.click();

			String Modify = driver
					.findElement(
							By.xpath("//table/tbody[2]/tr[2]/td[13]/div[2]/div[1]/button"))
					.getText();
			if ((Modify).equals("Modify")) {
				driver.findElement(
						By.xpath("//table/tbody[2]/tr[2]/td[13]/div[2]/div[1]/button"))
						.equals("Modify");
				takeScreenShots("exemptTimeEntry", data.getGroup(), driver);
			} else {
				log.info(data.getScript()+"-"+"Modify button is not displayed correctly");
			}
			return true;
		} catch (Exception e) {
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
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
		//String groupTests = "Employee-TimeEntry";
		//long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals("HPD_UserTimeEntry")
					|| data.getGroup().equals("ExemptTest")) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "	+ data.getBrowser()	+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().contains("HMY_HPD_UserTimeEntry")) {
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
					} else if (data.getScript().contains("Exempt")) {
						long startTime = System.currentTimeMillis();
						try {
							Map<String, String> credentials = DatabaseFactory
									.getExemptData(inputValues.get("query"),
											inputValues.get("exempt"));
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
		//long end = System.currentTimeMillis();
		// String group = HtmlReportGenerator.getGroupHtml(groupTests + "	["
		// + HtmlReportGenerator.getTime(end - start) + "]");
		HtmlReportGenerator.setHtmlContent(content);
	}
}