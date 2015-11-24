package com.peoplenet.qa.selenium.tests.base;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
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
 * 	TimeCard Tests will be executed based on the Group and Script enabling
 * }
 * </pre>
 * 
 */
public class TMC_TimeCardTests extends AbstractSmokeTests {

	/**
	 * @param data
	 *            - SuiteConfig Data
	 * @param inputValues
	 *            - Input Map Values separated by '|'
	 * @return true/false upon the execution
	 * 
	 *         <pre>
	 * {@code
	 * TimeCard FixPunch
	 * }
	 * 
	 *         <pre>
	 */
	public boolean FixPunch(SuiteConfig data, Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			pickAnEmployee(driver, inputValues, data);
			if (AddTime(driver, inputValues, data, false)) {
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				employeeFixPunch(driver, inputValues.get("punchTime"), data);
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

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TimeCard SplitPunch
	 */
	public boolean SplitPunch(SuiteConfig data, Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			log.info("start test case 57");
			pickAnEmployee(driver, inputValues, data);
			if (AddTime(driver, inputValues, data, false)) {
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				Thread.sleep(500);
				employeeSplitPunch(driver, inputValues, data);
			}
			Thread.sleep(5000);
			log.info("end test 57");
			driver.quit();
			return true;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TimeCard DeleteTranction for a particular period
	 */
	public boolean DeleteTransaction(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			log.info("start test case 58");
			pickAnEmployee(driver, inputValues, data);
			if (AddTime(driver, inputValues, data, false)) {
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				deleteTimeCard(driver, data);
			}
			Thread.sleep(5000);
			log.info("end test 58");
			driver.quit();
			return true;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TimeCard MoveHours for a particular period
	 */
	public boolean MoveHours(SuiteConfig data, Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			pickAnEmployee(driver, inputValues, data);
			if (AddTime(driver, inputValues, data, false)) {
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				moveHours(driver, data, inputValues);
			}
			Thread.sleep(5000);
			driver.quit();
			return true;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean RollBackTransaction(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			log.info("start test case 58");
			pickAnEmployee(driver, inputValues, data);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}
			rollbackTimeCard(driver, data);
			Thread.sleep(5000);
			log.info("end test 58");
			driver.quit();
			return true;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         TimeCard AddTime for the Input SuiteConfig
	 */
	public boolean AddTimeCard(SuiteConfig data, Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			log.info("start test AddTimeCard");
			pickAnEmployee(driver, inputValues, data);
			Thread.sleep(1000);
			boolean flag = AddTime(driver, inputValues, data, false);
			log.info("end test AddTimeCard");
			driver.quit();
			return flag;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}

	}

	public void employeeFixPunch(WebDriver driver, String punchTime,
			SuiteConfig data) {
		try {
			takeScreenShots("/FixPunch/fixPunch", data.getGroup(), driver);
			if (getFrameElement(driver, "Table") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getLinkElement(driver, punchTime).click();
				takeScreenShots("/FixPunch/fixPunch", data.getGroup(), driver);
			}
			Thread.sleep(2000);
			Alert alert1 = driver.switchTo().alert();
			alert1.accept();
		} catch (Exception e) {
			takeScreenShots("/FixPunch/fixPunch-error", data.getGroup(), driver);
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
		}
	}

	public void employeeSplitPunch(WebDriver driver,
			Map<String, String> inputValues, SuiteConfig data) {
		try {
			takeScreenShots("/SplitPunch/splitPunch", data.getGroup(), driver);
			if (getFrameElement(driver, "Table") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getImgElement(driver, "Split Punches").click();
			}
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}
			takeScreenShots("/SplitPunch/splitPunch", data.getGroup(), driver);
			List<WebElement> outTime = driver.findElements(By.id("OutTime"));
			List<WebElement> inTime = driver.findElements(By.id("InTime"));
			for (WebElement element : outTime) {
				if (element.isEnabled())
					element.sendKeys(inputValues.get("splitInTime"));
			}
			for (WebElement element : inTime) {
				if (element.isEnabled())
					element.sendKeys(inputValues.get("splitOutTime"));
			}
			takeScreenShots("/SplitPunch/splitPunch", data.getGroup(), driver);
			driver.findElement(By.id("SavSplitsButton")).click();
			takeScreenShots("/SplitPunch/splitPunch", data.getGroup(), driver);
		} catch (Exception e) {
			takeScreenShots("/SplitPunch/splitPunch-error", data.getGroup(),
					driver);
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
		}
	}

	/**
	 * @param data
	 *            - SuiteConfig Data
	 * @param inputValues
	 *            - Input Map Values separated by '|'
	 * @return true/false upon the execution
	 * 
	 *         <pre>
	 * {@code
	 * TimeCard AddTimeNextEmployee
	 * }
	 * 
	 *         <pre>
	 */
	public boolean AddTimeNextAndPreviousEmployee(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			pickAnEmployee(driver, inputValues, data);
			Thread.sleep(1000);
			boolean flag = AddTime(driver, inputValues, data, false);
			driver.quit();
			return flag;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

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
			Thread.sleep(20000);
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
				Thread.sleep(2000);
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				Thread.sleep(2000);
				getLinkElement(driver, inputValues.get("site")).click();
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				Thread.sleep(2000);
				getLinkElement(driver, inputValues.get("employee")).click();
				if (inputValues.containsKey("empType")) {
					log.info("Employee Selection " + inputValues.get("empType"));
					driver.switchTo().frame(getFrameElement(driver, "Table"));
					getLinkElement(driver,
							inputValues.get("empType") + " Employee").click();
					Thread.sleep(2000);
					takeScreenShots("pickAnEmployee", data.getGroup(), driver);
				}
				if (getFrameElement(driver, "Navigation") != null) {
					// Find an Employee
					driver.switchTo().frame(
							getFrameElement(driver, "Navigation"));
					Thread.sleep(2000);
					Select select = new Select(driver.findElement(By
							.name("PayPeriod")));
					select.selectByVisibleText(inputValues.get("timePeriod"));
					Robot robot = new Robot();

					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
				}
				takeScreenShots("pickAnEmployee", data.getGroup(), driver);
			}
		} catch (Exception e) {
			takeScreenShots("pickAnEmployee-error", data.getGroup(), driver);
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
		}

	}

	/**
	 * @param driver
	 * @param inputValues
	 * @param group
	 * @param missingOutTime
	 * @return
	 * 
	 *         Base method for Adding new Transaction for an Employee with In
	 *         and Out Times for a specific time period and all the values are
	 *         derived from Excel Spreadsheet.
	 */
	public boolean AddTime(WebDriver driver, Map<String, String> inputValues,
			SuiteConfig data, boolean missingOutTime) {
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			log.info("winHandle: " + winHandle);
		}
		try {
			if (getFrameElement(driver, "Table") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getLinkElement(driver, "New Transaction").click();
				takeScreenShots("/AddTime/AddTimeCard", data.getGroup(), driver);
				Thread.sleep(1500);
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				if (inputValues.get("count") == null) {
					Select select = new Select(driver.findElement(By
							.name("sitedept1")));
					waitForPageLaod(driver, By.name("sitedept1"));
					try {
						select.selectByVisibleText(inputValues.get("siteDept"));
					} catch (Exception e) {
						List<WebElement> options = select.getOptions();
						for (WebElement element : options) {
							if (element.getText().contains(
									inputValues.get("site"))) {
								select.selectByVisibleText(element.getText());
								break;
							}
						}
					}
					Robot robot = new Robot();

					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);
					Select selectDate = new Select(driver.findElement(By
							.name("indate1")));
					selectDate.selectByVisibleText(inputValues.get("inDate"));
					Robot robot1 = new Robot();

					robot1.keyPress(KeyEvent.VK_ENTER);
					robot1.keyRelease(KeyEvent.VK_ENTER);

					driver.findElement(By.id("intime1")).sendKeys(
							inputValues.get("inTime"));
					if (!missingOutTime) {
						driver.findElement(By.id("outtime1")).sendKeys(
								inputValues.get("outTime"));
						driver.findElement(By.id("outtime1"))
								.sendKeys(Keys.TAB);
						driver.findElement(By.id("Comment")).sendKeys(
								"\n\n Tested via automation at system time: "
										+ formatData.format(Calendar
												.getInstance().getTime()));
						driver.findElement(By.id("Submit")).submit();
						Thread.sleep(8000);
						for (String winHandle : driver.getWindowHandles()) {
							driver.switchTo().window(winHandle);
							log.info("winHandle: " + winHandle);
						}
						takeScreenShots("/AddTime/AddTimeCard",
								data.getGroup(), driver);
					} else {
						driver.findElement(By.id("outtime1"))
								.sendKeys(Keys.TAB);
						driver.findElement(By.id("Comment")).sendKeys(
								"\n\n Tested via automation at system time: "
										+ formatData.format(Calendar
												.getInstance().getTime()));
						driver.findElement(By.id("Submit")).submit();
						Alert alert = driver.switchTo().alert();
						log.info("alert text: " + alert.getText());
						alert.accept();
						takeScreenShots("/AddTime/AddTimeCard",
								data.getGroup(), driver);
					}

				} else {
					waitForPageLaod(driver, By.id("indate1"));
					Thread.sleep(10000);
					Select selectDate = new Select(driver.findElement(By
							.id("indate1")));
					selectDate.selectByVisibleText(inputValues.get("inDate"));
					if (Integer.valueOf(inputValues.get("count")) > 0
							&& Integer.valueOf(inputValues.get("count")) < 6) {
						for (int i = 1; i <= Integer.valueOf(inputValues
								.get("count")); i++) {
							driver.findElement(By.name("intime" + i)).sendKeys(
									inputValues.get("inTime"));
							if (!missingOutTime) {
								driver.findElement(By.name("outtime" + i))
										.sendKeys(inputValues.get("outTime"));
								driver.findElement(By.name("outtime" + i))
										.sendKeys(Keys.TAB);
								takeScreenShots("/AddTime/AddTimeCard",
										data.getGroup(), driver);
							} else {
								driver.findElement(By.name("outtime" + i))
										.sendKeys(Keys.TAB);
								takeScreenShots("/AddTime/AddTimeCard",
										data.getGroup(), driver);
							}
							if (i < Integer.valueOf(inputValues.get("count"))) {
								waitForPageLaod(driver,
										By.cssSelector("addBtn"));
								getInputElement(driver, "value",
										"Add Row to End").click();
							}
						}
						driver.findElement(By.id("Comment")).sendKeys(
								"\n\n Tested via automation at system time: "
										+ formatData.format(Calendar
												.getInstance().getTime()));
						driver.findElement(By.id("Submit")).submit();
						Thread.sleep(8000);
						if (!missingOutTime) {
							for (String winHandle : driver.getWindowHandles()) {
								driver.switchTo().window(winHandle);
								log.info("winHandle: " + winHandle);
							}
							takeScreenShots("/AddTime/AddTimeCard",
									data.getGroup(), driver);
						} else {
							Alert alert = driver.switchTo().alert();
							log.info("alert text: " + alert.getText());
							alert.accept();
							takeScreenShots("/AddTime/AddTimeCard",
									data.getGroup(), driver);
						}
					}
				}
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			takeScreenShots("/AddTime/AddTimeCard-error", data.getGroup(),
					driver);
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteTimeCard(WebDriver driver, SuiteConfig data) {
		try {
			takeScreenShots("/DeleteTransaction/deleteTimeCard",
					data.getGroup(), driver);
			// Delete Time Card
			WebElement frame = getFrameElement(driver, "Table");
			if (frame != null) {
				driver.switchTo().frame(frame);
				getLinkElement(driver, "Delete/Rollback Time").click();
				((JavascriptExecutor) driver)
						.executeScript("return window.getSelection().toString();");
				Thread.sleep(2000);
				getInputElement(driver, "type", "checkbox").click();
				driver.findElement(By.id("DeleteTimeCardComments")).sendKeys(
						"Test Delete");
				takeScreenShots("/DeleteTransaction/deleteTimeCard",
						data.getGroup(), driver);
				driver.findElement(By.id("DeleteTimeCardBtn")).click();

				Thread.sleep(500);
				takeScreenShots("/DeleteTransaction/deleteTimeCard",
						data.getGroup(), driver);
				((JavascriptExecutor) driver)
						.executeScript("return window.getSelection().toString();");
				getSpanElement(driver, "Yes").click();
				Thread.sleep(500);
				takeScreenShots("/DeleteTransaction/deleteTimeCard",
						data.getGroup(), driver);
				getSpanElement(driver, "close").click();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			takeScreenShots("/DeleteTransaction/deleteTimeCard-error",
					data.getGroup(), driver);
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public boolean moveHours(WebDriver driver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			// Move-Hours Time Card
			WebElement frame = getFrameElement(driver, "Table");
			if (frame != null) {
				driver.switchTo().frame(frame);
				getLinkElement(driver, "Move Hours").click();
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				Thread.sleep(2000);
				takeScreenShots("/MoveHours/movehours", data.getGroup(), driver);
				Select select = new Select(driver.findElement(By.name("Dept1")));
				String optionText = "";
				List<WebElement> options = select.getOptions();
				for (WebElement option : options) {
					if (!option.getText().equals("Select Dept")) {
						optionText = option.getText();
						break;
					}
				}
				select.selectByVisibleText(optionText);
				Robot robot = new Robot();
				robot.keyPress(KeyEvent.VK_ENTER);
				robot.keyRelease(KeyEvent.VK_ENTER);
				takeScreenShots("/MoveHours/movehours", data.getGroup(), driver);
				WebElement element = driver.findElement(By.name("Hours1"));
				element.clear();
				element.sendKeys(inputValues.get("moveTime"));
				takeScreenShots("/MoveHours/movehours", data.getGroup(), driver);
				driver.findElement(By.name("Submit")).submit();
				Thread.sleep(2000);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			takeScreenShots("/MoveHours/movehours-error", data.getGroup(),
					driver);
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	/**
	 * @param driver
	 * @param group
	 * @return
	 * 
	 *         Rollback the time that was deleted for an employee given as
	 *         input.
	 */
	public boolean rollbackTimeCard(WebDriver driver, SuiteConfig data) {
		try {
			takeScreenShots("rollbackTimeCard", data.getGroup(), driver);
			// Rollback Deleted Time Card
			Thread.sleep(500);
			WebElement frame = getFrameElement(driver, "Table");
			driver.switchTo().frame(frame);
			getLinkElement(driver, "Delete/Rollback Time").click();
			Thread.sleep(1500);
			((JavascriptExecutor) driver)
					.executeScript("return window.getSelection().toString();");
			Thread.sleep(500);
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}
			takeScreenShots("rollbackTimeCard", data.getGroup(), driver);
			WebElement rollBack = driver.findElement(By
					.xpath("//table/tbody/tr[1]/td[4]/button"));
			String script = "window.scrollTo(" + rollBack.getLocation().x + ","
					+ rollBack.getLocation().y + ")";
			takeScreenShots("rollbackTimeCard", data.getGroup(), driver);
			((JavascriptExecutor) driver).executeScript(script);
			rollBack.click();
			takeScreenShots("rollbackTimeCard", data.getGroup(), driver);
			Thread.sleep(500);
			((JavascriptExecutor) driver)
					.executeScript("return window.getSelection().toString();");
			getSpanElement(driver, "Yes").click();
			Thread.sleep(500);
			getSpanElement(driver, "close").click();
			Thread.sleep(500);
			takeScreenShots("rollbackTimeCard", data.getGroup(), driver);
			return true;
		} catch (Exception e) {
			takeScreenShots("rollbackTimeCard-error", data.getGroup(), driver);
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
		String groupTests = "TimeCard";
		
		// long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().startsWith("TMC_AddNewEmployee")) {
						long startTime = System.currentTimeMillis();
						boolean status = (boolean) AddEmployee(data,
								inputValues).get("status");
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
					if (data.getScript().startsWith("TMC_DeleteTransaction")) {
						long startTime = System.currentTimeMillis();
						boolean status = DeleteTransaction(data, inputValues);
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
					if (data.getScript().startsWith("TMC_RollBackTransaction")) {
						long startTime = System.currentTimeMillis();
						boolean status = RollBackTransaction(data, inputValues);
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
					if (data.getScript().startsWith("TMC_SplitPunch")) {
						long startTime = System.currentTimeMillis();
						boolean status = SplitPunch(data, inputValues);
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
					if (data.getScript().startsWith("TMC_FixPunch")) {
						long startTime = System.currentTimeMillis();
						boolean status = FixPunch(data, inputValues);
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
					if (data.getScript().startsWith("TMC_AddTimeCard")) {
						long startTime = System.currentTimeMillis();
						boolean status = AddTimeCard(data, inputValues);
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
					if (data.getScript().startsWith("TMC_MoveHours")) {
						long startTime = System.currentTimeMillis();
						boolean status = MoveHours(data, inputValues);
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
					if (data.getScript().startsWith("TMC_NextEmployee_AddTime")
							|| data.getScript().startsWith(
									"TMC_PreviousEmployee_AddTime")) {
						long startTime = System.currentTimeMillis();
						boolean status = AddTimeNextAndPreviousEmployee(data,
								inputValues);
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
