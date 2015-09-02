package com.peoplenet.qa.selenium.tests;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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
public class HMY_InvoiceAttachTests extends AbstractSmokeTests {

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TMC Validate Test based on the input values [Client,Group and
	 *         Site] where it generates report for 'All Days Total Hours-By
	 *         Employee'
	 */
	public boolean InvoiceTest(SuiteConfig data,
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
						if (Boolean.valueOf(inputValues.get("invoiceAttach")))
							attachInvoice(driver, data, inputValues);
						else
							noAttachInvoice(driver, data, inputValues);
						break;
					}
					if ((third.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[3]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						if (Boolean.valueOf(inputValues.get("invoiceAttach")))
							attachInvoice(driver, data, inputValues);
						else
							noAttachInvoice(driver, data, inputValues);
						break;
					}
					if ((fourth.equals(errorMsg))) {

						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[4]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						if (Boolean.valueOf(inputValues.get("invoiceAttach")))
							attachInvoice(driver, data, inputValues);
						else
							noAttachInvoice(driver, data, inputValues);
						break;
					}
					if ((fifth.equals(errorMsg))) {
						driver.manage().timeouts()
								.implicitlyWait(15, TimeUnit.SECONDS);
						driver.findElement(
								By.xpath("//table/tbody[1]/tr[5]/td[1]/a"))
								.click();
						Thread.sleep(5000);
						if (Boolean.valueOf(inputValues.get("invoiceAttach")))
							attachInvoice(driver, data, inputValues);
						else
							noAttachInvoice(driver, data, inputValues);
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

	public boolean noAttachInvoice(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			takeScreenShots("noInvoice", data.getGroup(), driver);
			if (driver
					.findElements(
							By.xpath("/html/body/div[3]/div/div/div[2]/div/div[7]/div/span[2]"))
					.size() > 1) {
				takeScreenShots("noInvoice", data.getGroup(), driver);
			} else {
				driver.findElement(By.xpath("//input[@data-index_id='1']"))
						.click();
				WebElement time = driver.findElement(By
						.xpath("//input[@data-bind='numericInput: hours']"));
				time.click();
				time.clear();
				time.sendKeys(inputValues.get("time"));
				takeScreenShots("noInvoice", data.getGroup(), driver);
				driver.findElement(By.linkText("Copy to Next Day")).click();
				takeScreenShots("noInvoice", data.getGroup(), driver);
				driver.findElement(By.xpath("//input[@data-index_id='2']"))
						.click();
				driver.findElement(By.linkText("Copy to Next Day")).click();
				takeScreenShots("noInvoice", data.getGroup(), driver);
				driver.findElement(By.xpath("//input[@data-index_id='3']"))
						.click();
				driver.findElement(By.linkText("Copy to Next Day")).click();
				takeScreenShots("noInvoice", data.getGroup(), driver);
				driver.findElement(By.xpath("//input[@data-index_id='4']"))
						.click();
				driver.findElement(By.linkText("Copy to Next Day")).click();
				takeScreenShots("noInvoice", data.getGroup(), driver);
				driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
				driver.findElement(
						By.xpath("//button[contains(.,'Submit All')]")).click();
				driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
				takeScreenShots("noInvoice", data.getGroup(), driver);
				String message = driver.findElement(
						By.xpath("//table/tbody[2]/tr[2]/td[1]/span"))
						.getText();
				String errormsg = "An error occurred. Unable to submit assignment. The following assignments are missing the required invoice attachment:"
						+ message;
				if (driver.findElement(By
						.xpath("/html/body/div[4]/div[1]/div[3]/button")) != null)
					driver.findElement(
							By.xpath("/html/body/div[4]/div[1]/div[3]/button"))
							.getText().contains(errormsg);
				else
					log.info(data.getScript() + "-"
							+ "ERROR MESSAGE IS NOT DISPLAYED CORRECTLY");
				takeScreenShots("noInvoice", data.getGroup(), driver);
				driver.findElement(By.xpath("//button[contains(.,'Ok')]"))
						.click();
				driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
				driver.findElement(
						By.xpath("//table/tbody[2]/tr[2]/td[13]/div[1]/div/button"))
						.click();
				driver.manage().timeouts().implicitlyWait(40, TimeUnit.SECONDS);
				String errormsg2 = "Can't Submit  You are missing the following attachments:"
						+ "Invoice Attachment";
				takeScreenShots("noInvoice", data.getGroup(), driver);
				if (driver.findElement(By
						.xpath("/html/body/div[4]/div[1]/div[3]/button")) != null)
					driver.findElement(
							By.xpath("/html/body/div[4]/div[1]/div[3]/button"))
							.getText().contains(errormsg2);
				else
					log.info(data.getScript() + "-"
							+ "ERROR MESSAGE IS NOT DISPLAYED CORRECTLY");
				driver.findElement(By.xpath("//button[contains(.,'Ok')]"))
						.click();
				takeScreenShots("noInvoice", data.getGroup(), driver);
			}
			Thread.sleep(2500);
			return true;
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean attachInvoice(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			takeScreenShots("attachInvoice", data.getGroup(), driver);
			if (driver
					.findElements(
							By.xpath("/html/body/div[3]/div/div/div[2]/div/div[7]/div/span[2]"))
					.size() > 1) {
				takeScreenShots("attachInvoice", data.getGroup(), driver);
			} else {
				driver.findElement(By.xpath("//input[@data-index_id='1']"))
						.click();
				WebElement time = driver.findElement(By
						.xpath("//input[@data-bind='numericInput: hours']"));
				time.click();
				time.clear();
				time.sendKeys(inputValues.get("time"));
				takeScreenShots("attachInvoice", data.getGroup(), driver);
				driver.findElement(By.linkText("Copy to Next Day")).click();
				takeScreenShots("attachInvoice", data.getGroup(), driver);
				driver.findElement(By.xpath("//input[@data-index_id='2']"))
						.click();
				driver.findElement(By.linkText("Copy to Next Day")).click();
				takeScreenShots("attachInvoice", data.getGroup(), driver);
				driver.findElement(By.xpath("//input[@data-index_id='3']"))
						.click();
				driver.findElement(By.linkText("Copy to Next Day")).click();
				takeScreenShots("attachInvoice", data.getGroup(), driver);
				driver.findElement(By.xpath("//input[@data-index_id='4']"))
						.click();
				driver.findElement(By.linkText("Copy to Next Day")).click();
				takeScreenShots("attachInvoice", data.getGroup(), driver);
				if (driver
						.findElements(By.className("attachIconNoAttachments"))
						.size() > 1) {
					driver.findElement(
							By.xpath("//table/tbody[2]/tr[2]/td[12]/ul/li[1]/span/span"))
							.click();
					driver.findElement(
							By.xpath("//option[contains(.,'Please Select')]"))
							.click();
					driver.findElement(
							By.xpath("//option[contains(.,'Approval Attachment')]"))
							.click();
					takeScreenShots("attachInvoice", data.getGroup(), driver);
					driver.findElement(
							By.xpath("//table[2]/tbody/tr[2]/td[2]/input[1]"))
							.click();

					Thread.sleep(3000);
					takeScreenShots("attachInvoice", data.getGroup(), driver);
					StringSelection ss = new StringSelection(
							"C:/Users/Anil.Vunnava/Test/new/Invoice.png");
					Toolkit.getDefaultToolkit().getSystemClipboard()
							.setContents(ss, null);

					Robot robot = new Robot();
					robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
					robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
					robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
					robot.keyPress(java.awt.event.KeyEvent.VK_V);
					robot.keyRelease(java.awt.event.KeyEvent.VK_V);
					robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
					robot.delay(1000);
					robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
					robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
					takeScreenShots("attachInvoice", data.getGroup(), driver);
					driver.findElement(
							By.xpath("//button[contains(.,'Upload')]")).click();
					takeScreenShots("attachInvoice", data.getGroup(), driver);
					driver.findElement(
							By.xpath("//button[contains(.,'Close')]")).click();
					driver.findElement(
							By.xpath("//button[contains(.,'Submit All')]"))
							.click();
					Thread.sleep(5000);
					String message = driver.findElement(
							By.xpath("//table/tbody[2]/tr[2]/td[1]/span"))
							.getText();
					String errormsg = "An error occurred. Unable to submit assignment. The following assignments are missing the required invoice attachment:"
							+ message;

					if (driver.findElement(By
							.xpath("/html/body/div[4]/div[1]/div[3]/button")) != null)
						driver.findElement(
								By.xpath("/html/body/div[4]/div[1]/div[3]/button"))
								.equals(errormsg);
					else
						log.info(data.getScript()+"-"+
								"ERROR MESSAGE IS NOT DISPLAYED CORRECTLY");

					driver.findElement(By.xpath("//button[contains(.,'Ok')]"))
							.click();
				}
				takeScreenShots("attachInvoice", data.getGroup(), driver);
			}
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
		String groupTests = "InvoiceAttachTests";
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
					if (data.getScript().contains("Invoice")) {
						long startTime = System.currentTimeMillis();
						boolean status = InvoiceTest(data, inputValues,
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
