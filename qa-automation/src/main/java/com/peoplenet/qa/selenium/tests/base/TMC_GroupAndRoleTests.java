package com.peoplenet.qa.selenium.tests.base;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.seleniumhq.selenium.fluent.FluentWebDriver;
import org.testng.annotations.Test;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava
 *
 *         Class for Creating New Group,Client and Role with the input values
 *         from ExcelSpreadsheet
 */
public class TMC_GroupAndRoleTests extends AbstractSmokeTests {

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         Creates new Group with the inputValues
	 */
	public boolean CreateGroup(SuiteConfig data, Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			log.info("start test case 56");
			String parentHandle = driver.getWindowHandle();
			log.info("parentHandle: " + parentHandle);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}
			
			FluentWebDriver fluent = new FluentWebDriver(driver);
			
			

			if (getFrameElement(driver, "Menu") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Menu"));

				Select select = new Select(driver.findElement(By
						.name("Maintenance")));
				select.selectByVisibleText(inputValues.get("menu"));
				Robot robot = new Robot();

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				takeScreenShots("/NewGroup/addNewGroup", data.getGroup(),
						driver);
			}
			if (getFrameElement(driver, "Table") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Table"));

				driver.findElement(
						By.xpath("//a[contains(text(),'"
								+ inputValues.get("function") + "')]")).click();
				takeScreenShots("/NewGroup/addNewGroup", data.getGroup(),
						driver);
				driver.findElement(
						By.xpath("//input[@value='New Access Group']")).click();
				waitForPageLaod(driver, By.id("AGName"));
				driver.findElement(By.id("AGName")).click();

				WebElement name = driver.findElement((By.id("AGName")));

				for (int i = 0; i < 1; i++) {
					int randomInt = (int) (10000.0 * Math.random());
					name.sendKeys((inputValues.get("groupName") + randomInt));
					log.info(inputValues.get("groupName") + randomInt);

				}
				takeScreenShots("/NewGroup/addNewGroup", data.getGroup(),
						driver);
				driver.findElement(By.xpath("//input[@value='NEXT>>']"))
						.click();
				Thread.sleep(1000);
				takeScreenShots("/NewGroup/addNewGroup", data.getGroup(),
						driver);
				if (getFrameElement(driver, "AGTable") != null) {
					driver.switchTo().frame(getFrameElement(driver, "AGTable"));
					driver.findElement(By.xpath("//input[@id='FormButton']"))
							.click();
				}
				takeScreenShots("/NewGroup/addNewGroup", data.getGroup(),
						driver);
			}
			log.info("end test 56");
			driver.quit();
			return true;
		} catch (Exception e) {
			takeScreenShots("/NewGroup/addNewGroup-error", data.getGroup(),
					driver);
			driver.quit();
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
			return false;
		}
	}

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         Creates new Role with the inputValues
	 */
	public boolean CreateRole(SuiteConfig data, Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			log.info("start test case 57");
			String parentHandle = driver.getWindowHandle();
			log.info("parentHandle: " + parentHandle);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}

			if (getFrameElement(driver, "Menu") != null) {

				driver.switchTo().frame(getFrameElement(driver, "Menu"));
				Select select = new Select(driver.findElement(By
						.name("Maintenance")));
				select.selectByVisibleText(inputValues.get("menu"));
				Robot robot = new Robot();

				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				driver.findElements(By.tagName("frame"));
				takeScreenShots("/NewRole/addNewRole", data.getGroup(), driver);
			}
			Thread.sleep(2000);
			if (getFrameElement(driver, "Table") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Table"));

				driver.findElement(
						By.xpath("//a[contains(text(),'"
								+ inputValues.get("function") + "')]")).click();
				takeScreenShots("/NewRole/addNewRole", data.getGroup(), driver);
				driver.findElement(By.xpath(" //input[@value='New Role']"))
						.click();
				Thread.sleep(1000);
				WebElement name = driver.findElement((By.id("RoleName")));

				for (int i = 0; i < 1; i++) {
					int randomInt = (int) (10000.0 * Math.random());
					name.sendKeys((inputValues.get("roleName") + randomInt));
				}
				takeScreenShots("/NewRole/addNewRole", data.getGroup(), driver);

				// let page finish loading
				driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

				if (inputValues.get("roleName").equalsIgnoreCase("AdminClient")) {
					driver.findElement(By.name("ClientAdm_Users")).click();
					driver.findElement(By.name("ClientAdm_AccessGroups"))
							.click();
					driver.manage().timeouts()
							.implicitlyWait(30, TimeUnit.SECONDS);
					driver.findElements(By.name("ClientAdm_AdminRights"))
							.get(1).click();
					driver.findElements(By.name("ClientAdm_Divisions")).get(1)
							.click();
					driver.findElements(By.name("ClientAdm_Agencies")).get(1)
							.click();
					driver.findElements(By.name("ClientAdm_SitesDepts")).get(1)
							.click();
					driver.findElements(By.name("ClientAdm_Depts")).get(1)
							.click();
					driver.findElements(By.name("ClientAdm_DeptShifts")).get(1)
							.click();
					driver.findElements(By.name("ClientAdm_ShiftDiffs")).get(1)
							.click();
					driver.findElements(By.name("ClientAdm_Adjustments"))
							.get(1).click();
					driver.findElements(By.name("ClientAdm_EmplShifts")).get(1)
							.click();
					driver.findElements(By.name("ClientAdm_PeriodStatus"))
							.get(1).click();
					driver.findElements(By.name("ClientAdm_OTDays")).get(1)
							.click();
					driver.findElements(By.name("AllowCookieSetup")).get(1)
							.click();
					driver.findElements(By.name("AllowSecondLevelApproval"))
							.get(1).click();
					driver.findElements(By.name("AllowAprvlAdj")).get(1)
							.click();
					driver.findElements(By.name("AllowAprvl")).get(1).click();
					driver.findElements(By.name("autoListReports")).get(1)
							.click();
					driver.findElements(By.name("TimeImporter")).get(1).click();
					driver.findElements(By.name("AllowMassEntry")).get(1)
							.click();
					driver.findElements(By.name("AllowApprovePTO")).get(1)
							.click();
					driver.findElements(By.name("AllowPTOMaint")).get(1)
							.click();
					driver.findElements(By.name("AllowViewAlerts")).get(1)
							.click();
					driver.findElements(By.name("ViewBySite")).get(1).click();
					driver.findElements(By.name("ViewRates")).get(1).click();
					driver.findElements(By.name("AllowViewUploadHist")).get(1)
							.click();
					driver.findElements(By.name("AllowCheckAttendance")).get(1)
							.click();
					driver.findElements(By.name("AllowFileReceive")).get(1)
							.click();
					driver.findElements(By.name("AllowFileSend")).get(1)
							.click();
					driver.findElements(By.name("AllowBatchEntry")).get(1)
							.click();
					driver.findElements(By.name("HideSSNOnReports")).get(1)
							.click();
					driver.findElements(By.name("AllowReportRecipientChange"))
							.get(1).click();
					driver.findElements(By.name("AllowReports")).get(1).click();
					Select select = new Select(driver.findElement(By
							.name("ViewSchedReports")));
					select.selectByVisibleText("Yes");
					Robot robot = new Robot();
					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
					driver.findElements(By.name("ViewPayDollars")).get(1)
							.click();
					driver.findElements(By.name("ViewBillDollars")).get(1)
							.click();
					driver.findElements(By.name("AllowSalAlloc")).get(1)
							.click();
					driver.findElements(By.name("AllowFixPunches")).get(1)
							.click();
					driver.findElements(By.name("AllowShiftDiffChgs")).get(1)
							.click();
					driver.findElements(By.name("AllowDeptChgs")).get(1)
							.click();
					driver.findElements(By.name("AllowVoidPunch")).get(1)
							.click();
					driver.findElements(By.name("AllowAdjEntry")).get(1)
							.click();
					driver.findElements(By.name("AllowOTAlloc")).get(1).click();
					driver.findElements(By.name("AllowMoveHours")).get(1)
							.click();
					driver.findElements(By.name("AllowTxnAfterClose")).get(1)
							.click();
					driver.findElement(By.id("ManualCheckRequestor_1")).click();
					driver.findElement(By.id("ManualCheckApprover_1")).click();
					driver.findElement(By.id("ManualCheckProcessor_1")).click();
					driver.findElements(By.name("AllowCloseViaWeb")).get(1)
							.click();
					driver.findElements(By.name("AllowSiteCloseViaWeb")).get(1)
							.click();
				}

				driver.findElement(By.xpath("//input[@value='Save Role Info']"))
						.click();
				takeScreenShots("/NewRole/addNewRole", data.getGroup(), driver);
			}
			driver.switchTo().window(parentHandle);
			log.info("end test case 57");
			driver.quit();
			return true;
		} catch (Exception e) {
			takeScreenShots("/NewRole/addNewRole-error", data.getGroup(),
					driver);
			driver.quit();
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
			return false;
		}

	}

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         Creates new Client with the inputValues
	 */
	public boolean CreateClient(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			String parentHandle = driver.getWindowHandle();
			log.info("parentHandle: " + parentHandle);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}
			if (getFrameElement(driver, "Table") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				waitForPageLaod(
						driver,
						By.xpath("//a[contains(text(),'"
								+ inputValues.get("link") + "')]"));
				driver.findElement(
						By.xpath("//a[contains(text(),'"
								+ inputValues.get("link") + "')]")).click();
				takeScreenShots("/NewClient/addNewClient", data.getGroup(),
						driver);
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
				}

				driver.findElement(By.id("ClientCode")).clear();
				Thread.sleep(2000);

				WebElement namecode = driver.findElement(By.id("ClientCode"));
				WebElement name = driver.findElement(By.id("ClientName"));

				for (int i = 0; i < 1; i++) {
					int randomInt = (int) (10000.0 * Math.random());
					namecode.sendKeys((inputValues.get("clientCode") + randomInt));
					name.sendKeys((inputValues.get("clientName") + randomInt));
				}
				takeScreenShots("/NewClient/addNewClient", data.getGroup(),
						driver);
				driver.findElement(By.id("UseEmpLevelRates")).click();
				driver.findElement(By.id("WebShiftDiffMaint")).click();
				driver.findElement(By.id("ShiftDiffChange")).click();
				driver.findElement(By.id("TrackPayRates")).click();
				takeScreenShots("/NewClient/addNewClient", data.getGroup(),
						driver);
				driver.findElement(By.id("TrackBillRates")).click();
				driver.findElement(By.id("EnableVTCCookie")).click();
				driver.findElement(By.id("EnableExemptEmplVTS")).click();
				driver.findElement(By.id("EnableSecQuestion")).click();
				takeScreenShots("/NewClient/addNewClient", data.getGroup(),
						driver);
				driver.findElement(By.xpath("//input[@value='Create Client']"))
						.click();
				takeScreenShots("/NewClient/addNewClient", data.getGroup(),
						driver);
				Thread.sleep(3000);
			}
			driver.switchTo().window(parentHandle);
			driver.quit();
			log.info("end test case 58");
			return true;
		} catch (Exception e) {
			takeScreenShots("/NewClient/addNewClient-error", data.getGroup(),
					driver);
			driver.quit();
			log.error(data.getScript()+" Error Due to : "+e.getMessage());
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
		String groupTests = "GroupAndRole";
		//long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "	+ data.getBrowser()	+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().startsWith("TMC_CreateGroup")) {
						long startTime = System.currentTimeMillis();
						boolean status = CreateGroup(data, inputValues);
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
					if (data.getScript().startsWith("TMC_CreateRole")) {
						long startTime = System.currentTimeMillis();
						boolean status = CreateRole(data, inputValues);
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
					if (data.getScript().startsWith("TMC_CreateClient")) {
						long startTime = System.currentTimeMillis();
						boolean status = CreateClient(data, inputValues);
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