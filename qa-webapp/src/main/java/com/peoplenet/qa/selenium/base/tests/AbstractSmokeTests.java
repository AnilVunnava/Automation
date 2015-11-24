package com.peoplenet.qa.selenium.base.tests;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.seleniumhq.selenium.fluent.FluentSelect;
import org.seleniumhq.selenium.fluent.FluentWebDriver;
import org.seleniumhq.selenium.fluent.FluentWebElement;
import org.seleniumhq.selenium.fluent.FluentWebElements;
import org.testng.annotations.Test;

import com.peoplenet.qa.selenium.data.AccessConfig;
import com.peoplenet.qa.selenium.data.ExcelUtils;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.db.config.DatabaseFactory;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.factory.SeleniumDriverFactory;
import com.peoplenet.qa.selenium.listeners.ScreenshotListener;

/**
 * @author Anil.Vunnava
 *
 *         <pre>
 * {@code
 *         Base Abstract Class for TMC Smoke Tests
 *         }
 * </pre>
 */
public abstract class AbstractSmokeTests extends SeleniumDriverFactory {

	public static WebElement userid;
	public static WebElement password;
	public static WebElement submit;
	public static WebDriver driver;
	protected static final Logger log = Logger.getLogger("AbstractSmokeTests");
	protected static final SimpleDateFormat formatData = new SimpleDateFormat(
			"MM/dd/yyyy-HH-mm-ss");
	public static AccessConfig config;

	/**
	 * <pre>
	 * {@code
	 * Abstract TestNG method used to call in sub classes
	 * }
	 * </pre>
	 */
	@Test
	public void tests() {
		System.out.println("Hi How are you :: " + new Date());
	}

	public static AccessConfig getConfig() {
		return AbstractSmokeTests.config;
	}

	public static void setConfig(AccessConfig config) {
		AbstractSmokeTests.config = config;
	}

	/**
	 * @param data
	 * @param logoff
	 * @return WebDriver
	 * 
	 *         <pre>
	 * {@code
	 *         This Method logins into the specified URL from AccessConfig Model
	 *         loaded from Excel Sheet and logs-off if enabled logoff param as
	 *         true and finally returns the Driver object with current URL
	 *         }
	 * </pre>
	 */
	protected WebDriver registerAndLogin(SuiteConfig data, boolean logoff,
			Map<String, String> inputValues, WebDriver driver) {
		try {
			AccessConfig conf = null;
			for (AccessConfig accessConf : accessConfig) {
				if (data.getProject().equals("TMC")
						|| data.getProject().equals("HMY")
						|| data.getProject().equals("FLOW")) {
					if (accessConf.getScript()
							.equals("URL_" + ExcelUtils.sheet))
						conf = accessConf;
				} else {
					if (accessConf.getScript().equals("Default"))
						conf = accessConf;
				}
			}
			setConfig(conf);
			if (conf != null) {
				if (driver == null) {
					driver = getDriver(data.getUniqueScript());
					if (!Boolean.valueOf(PropertiesLoader
							.getProperty("mobileView")))
						driver.manage().window().maximize();
				}
				driver.get(conf.getURL());

				userid = driver.findElement(By.id("UserID"));
				password = driver.findElement(By.id("Password"));
				submit = driver.findElement(By.id("SignIn"));
				if (inputValues != null) {
					if (Boolean.valueOf(inputValues.get("addEmployee"))) {
						userid.clear();
						userid.sendKeys(conf.getLogin());
						password.clear();
						password.sendKeys(conf.getPasword());
						takeScreenShots("registerAndLogin", data.getGroup(),
								driver);
					} else {
						userid.clear();
						userid.sendKeys(inputValues.get("userId"));
						password.clear();
						password.sendKeys(inputValues.get("password"));
						takeScreenShots("registerAndLogin", data.getGroup(),
								driver);
					}
				} else {
					userid.clear();
					userid.sendKeys(conf.getLogin());
					password.clear();
					password.sendKeys(conf.getPasword());
					takeScreenShots("registerAndLogin", data.getGroup(), driver);
				}
				submit.click();
				if (data.getProject().equals("TMC")
						|| data.getProject().equals("FLOW")) {
					String parentHandle = driver.getWindowHandle();
					log.info("parentHandle: " + parentHandle);
					Thread.sleep(2000);
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
						log.info("winHandle: " + winHandle);
					}
					Thread.sleep(1000);
					if (logoff)
						logOffAndClose(driver, parentHandle);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (driver == null) {
			return getDefaultDriver();
		} else {
			return driver;
		}
	}

	protected WebDriver registerAndLogin(SuiteConfig suiteConfig, boolean logoff) {
		return registerAndLogin(suiteConfig, logoff, null, null);
	}

	/**
	 * @param driver
	 * @param parentHandle
	 *            <pre>
	 * {@code
	 *            This Method logs-off if enabled logoff param as true from
	 *            specified URL on AccessConfig Model loaded from Excel Sheet
	 *            }
	 * </pre>
	 */
	protected void logOffAndClose(WebDriver driver, String parentHandle) {
		try {
			if (getFrameElement(driver, "Menu") != null) {
				driver.switchTo().frame(getFrameElement(driver, "Menu"));
				WebElement logOffBtn = driver.findElement(By
						.linkText("Log Off"));
				logOffBtn.click();
				Alert alert = driver.switchTo().alert();
				alert.accept();
				driver.switchTo().window(parentHandle);
				WebElement signOutBtn = driver.findElement(By
						.linkText("Sign Out"));
				signOutBtn.click();
			}
			driver.quit();
		} catch (Exception e) {
			driver.quit();
			e.printStackTrace();
		}
	}

	/**
	 * @param data
	 * @param inputValues
	 * @return true/false
	 * 
	 *         <pre>
	 * {@code
	 *         Creates an TMC Employee based oon the inputValues supplied from
	 *         Excel Data
	 *         }
	 * </pre>
	 */
	protected Map<String, Object> AddEmployee(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		boolean state = false;
		Map<String, Object> values = new HashMap<String, Object>();
		Map<String, FluentWebElement> clientsMap = new HashMap<String, FluentWebElement>();
		try {
			FluentWebDriver fluent = new FluentWebDriver(driver);
			takeScreenShots("/" + data.getScript() + "/TimeEntry/screenshot",
					data.getGroup(), driver);
			log.info("Input Values : " + inputValues);
			Thread.sleep(8000);
			driver.switchTo().frame("Table");
			// Find an Employee
			FluentWebElements clients = fluent.links(By.xpath("contains(.,'"
					+ inputValues.get("client") + "')"));// *[@id="ClientListTable"]/tbody/tr[287]/td[3]/span[1]/a
			for (FluentWebElement client : clients) {
				if (client.isDisplayed().value() && client.isEnabled().value()) {
					clientsMap.put(client.getText().toString(), client);
				}
			}
			FluentWebElement clientClick = clientsMap.get(inputValues
					.get("client"));
			clientClick.click();
			Thread.sleep(3000);
			driver.switchTo().frame("Table");
			FluentWebElement group = fluent.link(By.xpath("contains(.,'"
					+ inputValues.get("group") + "')"));
			if (group.isDisplayed().value() && group.isEnabled().value()) {
				group.click();
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				Thread.sleep(4000);
				if (data.getProject().equals("TMC")) {
					if (getFrameElement(driver, "Menu") != null) {
						driver.switchTo()
								.frame(getFrameElement(driver, "Menu"));
						Thread.sleep(3000);
						Select select = new Select(driver.findElement(By
								.name("Maintenance")));
						select.selectByVisibleText("Employee Setup");
						Robot robot = new Robot();

						robot.keyPress(KeyEvent.VK_ENTER);
						robot.keyRelease(KeyEvent.VK_ENTER);

						Thread.sleep(500);
					}
					if (getFrameElement(driver, "Navigation") != null) {
						// Add an Employee
						driver.switchTo().frame(
								getFrameElement(driver, "Navigation"));
						takeScreenShots("/" + data.getScript()
								+ "/TimeEntry/screenshot", data.getGroup(),
								driver);
						driver.findElement(By.id("AddEmpButton")).click();
					}
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
						log.info("winHandle: " + winHandle);
					}
					Thread.sleep(3000);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);
					Thread.sleep(2000);
					driver.findElement(By.name("EmplSSN")).sendKeys(
							inputValues.get("ssn"));
					getInputElement(driver, "value", "Next >>").click();
					Thread.sleep(500);
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
						log.info("winHandle: " + winHandle);
					}
					Thread.sleep(3000);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);
					if (getFrameElement(driver, "Main") != null) {
						// Add an Employee
						driver.switchTo()
								.frame(getFrameElement(driver, "Main"));
						driver.findElement(By.id("LastName")).sendKeys(
								inputValues.get("lastName"));
						driver.findElement(By.id("FirstName")).sendKeys(
								inputValues.get("firstName"));

						Select select = new Select(driver.findElement(By
								.name("PayType")));
						select.selectByVisibleText(inputValues.get("payType"));
						Robot robot = new Robot();

						robot.keyPress(KeyEvent.VK_ENTER);
						robot.keyRelease(KeyEvent.VK_ENTER);
						Thread.sleep(500);

						Select select1 = new Select(driver.findElement(By
								.name("Status")));
						select1.selectByVisibleText(inputValues.get("status"));
						Robot robot1 = new Robot();

						robot1.keyPress(KeyEvent.VK_ENTER);
						robot1.keyRelease(KeyEvent.VK_ENTER);

						Thread.sleep(1500);
						takeScreenShots("/" + data.getScript()
								+ "/TimeEntry/screenshot", data.getGroup(),
								driver);

						driver.findElement(By.id("sitesDepts")).click();
						Thread.sleep(1500);
						takeScreenShots("/" + data.getScript()
								+ "/TimeEntry/screenshot", data.getGroup(),
								driver);
						Select select2 = new Select(driver.findElement(By
								.name("SiteDeptTemplate")));
						String optionText = "";
						List<WebElement> options = select2.getOptions();
						for (WebElement element : options) {
							if (element.getText() != null
									&& !element.getText().isEmpty()
									&& !element.getText().equals(
											"Select Template")) {
								optionText = element.getText();
								break;
							}
						}
						select2.selectByVisibleText(optionText);
						Robot robot2 = new Robot();

						robot2.keyPress(KeyEvent.VK_ENTER);
						robot2.keyRelease(KeyEvent.VK_ENTER);
						Thread.sleep(5000);

						Select select3 = new Select(driver.findElement(By
								.name("PrimaryDept")));
						String optionText1 = "";
						List<WebElement> options1 = select3.getOptions();
						for (WebElement element : options1) {
							if (element.getText() != null
									&& !element.getText().isEmpty()
									&& !element.getText().equals(
											"Select Template")) {
								optionText1 = element.getText();
								break;
							}
						}
						select3.selectByVisibleText(optionText1);
						Robot robot3 = new Robot();
						robot3.keyPress(KeyEvent.VK_ENTER);
						robot3.keyRelease(KeyEvent.VK_ENTER);
						Thread.sleep(1500);

						Select select4 = new Select(driver.findElement(By
								.name("PrimarySite")));
						String optionText2 = "";
						List<WebElement> options2 = select4.getOptions();
						for (WebElement element : options2) {
							if (element.getText() != null
									&& !element.getText().isEmpty()
									&& !element.getText().equals(
											"Select Template")) {
								optionText2 = element.getText();
								break;
							}
						}
						select4.selectByVisibleText(optionText2);
						Robot robot4 = new Robot();
						robot4.keyPress(KeyEvent.VK_ENTER);
						robot4.keyRelease(KeyEvent.VK_ENTER);
						Thread.sleep(500);
						takeScreenShots("/AddEmployee/addAnEmployee",
								data.getGroup(), driver);
						driver.findElement(By.id("Remove2")).click();
						Thread.sleep(100);
						driver.findElement(By.id("Remove3")).click();
						Thread.sleep(100);
						driver.findElement(By.id("Remove4")).click();
						Thread.sleep(100);
						driver.findElement(By.id("Remove5")).click();
						takeScreenShots("/AddEmployee/addAnEmployee",
								data.getGroup(), driver);
					}
					Thread.sleep(1000);
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
						log.info("winHandle: " + winHandle);
					}
					Thread.sleep(1000);
					takeScreenShots("/AddEmployee/addAnEmployee",
							data.getGroup(), driver);
					if (getFrameElement(driver, "Navigation") != null) {
						driver.switchTo().frame(
								getFrameElement(driver, "Navigation"));
						driver.findElement(By.name("save")).click();
						Thread.sleep(1000);
					}
					takeScreenShots("/AddEmployee/addAnEmployee",
							data.getGroup(), driver);
					Thread.sleep(1000);
					driver.quit();
				} else {
					driver.switchTo().frame(getFrameElement(driver, "Menu"));
					Thread.sleep(3000);
					FluentSelect select = fluent.select(By.name("Maintenance"));
					select.selectByVisibleText("Administration");
					Robot robot = new Robot();

					robot.keyPress(KeyEvent.VK_ENTER);
					robot.keyRelease(KeyEvent.VK_ENTER);

					Thread.sleep(500);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);

					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
						log.info("winHandle: " + winHandle);
					}
					Thread.sleep(3000);
					fluent.input(By.id("btnDecline")).click();

					Thread.sleep(3000);
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
						log.info("winHandle: " + winHandle);
					}
					Thread.sleep(3000);

					driver.switchTo().frame(getFrameElement(driver, "Table"));
					fluent.link(
							By.xpath("contains(.,'" + inputValues.get("group")
									+ " Setup')")).click();
					Thread.sleep(3000);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);

					// driver.switchTo().frame("Table");
					driver.findElement(
							By.xpath("/html/body/form/table[2]/tbody/tr/td/span/a"))
							.click();
					Thread.sleep(3000);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);

					if (inputValues.get("client").equals("MANPOWER GROUP")
							|| inputValues.get("client").equals("CANADA")) {
						fluent.link(
								By.xpath("contains(.,'Non-Agency Employees')"))
								.click();
						Thread.sleep(1500);
					}
					// driver.switchTo().frame("Table");
					fluent.input(By.className("smallbold")).click();
					Thread.sleep(3000);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);

					// driver.switchTo().frame(getFrameElement(driver,
					// "Table"));

					fluent.input(By.name("SSN")).sendKeys(
							inputValues.get("lastSSN"));
					Thread.sleep(1500);
					fluent.input(By.name("FirstName")).sendKeys(
							inputValues.get("firstName"));
					Thread.sleep(1500);
					fluent.input(By.name("LastName")).sendKeys(
							inputValues.get("lastName"));
					Thread.sleep(1500);
					fluent.input(By.name("EmpEmail")).sendKeys(
							inputValues.get("empEmail"));
					Thread.sleep(1500);
					fluent.input(By.name("FileNo")).sendKeys(
							inputValues.get("empId"));
					Thread.sleep(1500);
					FluentSelect timeEntry = fluent.select(By
							.name("WTE_TimeEntry"));
					timeEntry.selectByVisibleText("Spreadsheet w/Projects");
					Robot robot1 = new Robot();
					robot1.keyPress(KeyEvent.VK_ENTER);
					robot1.keyRelease(KeyEvent.VK_ENTER);
					Thread.sleep(500);
					Thread.sleep(1500);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);

					FluentSelect clientSel = fluent.select(By.name("SiteNo"));
					clientSel.selectByVisibleText(inputValues.get("site"));

					Robot robot2 = new Robot();
					robot2.keyPress(KeyEvent.VK_ENTER);
					robot2.keyRelease(KeyEvent.VK_ENTER);
					Thread.sleep(500);
					Thread.sleep(1500);

					fluent.input(By.name("JobDesc")).sendKeys(
							"Harmony Responsive Tester");
					Thread.sleep(1500);
					fluent.input(By.name("AssignmentNo")).sendKeys("98723425");
					Thread.sleep(1500);
					fluent.input(By.name("AssignmentStart")).sendKeys(
							"01/01/2015");
					Thread.sleep(1500);
					fluent.input(By.name("AssignmentEnd")).sendKeys(
							"12/31/2018");
					Thread.sleep(1500);
					fluent.input(By.name("BillRate")).sendKeys("18.00");
					Thread.sleep(1500);
					fluent.input(By.name("PayRate")).sendKeys("16.00");
					Thread.sleep(1500);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);

					FluentSelect stateSel = fluent.select(By.name("workState"));
					stateSel.selectByVisibleText("GEORGIA");

					Robot robot3 = new Robot();
					robot3.keyPress(KeyEvent.VK_ENTER);
					robot3.keyRelease(KeyEvent.VK_ENTER);
					Thread.sleep(500);
					Thread.sleep(1500);

					fluent.input(By.name("ApproverFirstName")).sendKeys(
							inputValues.get("apprFirstName"));
					Thread.sleep(1500);
					fluent.input(By.name("ApproverLastName")).sendKeys(
							inputValues.get("apprLastName"));
					Thread.sleep(1500);
					fluent.input(By.name("ApproverEmail")).sendKeys(
							inputValues.get("apprEmail"));
					Thread.sleep(1500);

					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);
					Thread.sleep(1500);
					getInputElement(driver, "value", "Save Employee").click();
					Thread.sleep(12000);
				}
				state = true;
				String currentUrl = driver.getCurrentUrl();
				if (!currentUrl.contains("qa2-www.mypeoplenet.com")) {
					Map<String, String> employeeData = checkForEmployee(data,
							inputValues);
					if (employeeData != null && !employeeData.isEmpty()) {
						inputValues.put("userId", employeeData.get("userId"));
						inputValues.put("recordId",
								employeeData.get("recordId"));
						inputValues.put("password",
								employeeData.get("password"));
					}
				}
				values.put("status", state);
				values.put("driver", driver);
			} else {
				driver.quit();
			}

		} catch (Exception e) {
			takeScreenShots("/" + data.getScript() + "/ERROR/screenshot",
					data.getGroup(), driver);
			driver.quit();
			e.printStackTrace();
		}
		return values;

	}

	public Map<String, String> checkForEmployee(SuiteConfig data,
			Map<String, String> inputValues) {

		Map<String, String> employeeData = DatabaseFactory
				.checkEmployeeCreation(inputValues.get("empEmail"));

		if (employeeData.isEmpty()) {
			log.info("Employee Not Created = So Skip All and Try Again");
		} else {
			log.info("Employee Saved :: " + employeeData);
			DatabaseFactory.modifyEmployee(inputValues.get("empEmail"),
					inputValues.get("client"));
		}

		return employeeData;
	}

	/**
	 * @param driver
	 * @param condition
	 * @return true/false
	 * 
	 *         <pre>
	 * {@code
	 *         A Wait method which waits on the By condition of an Element for
	 *         about 50 seconds
	 *         }
	 * </pre>
	 */
	protected boolean waitForPageLaod(WebDriver driver, final By condition) {
		WebDriverWait myWait = new WebDriverWait(driver, 50);
		ExpectedCondition<Boolean> conditionToCheck = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver expect) {
				try {
					return (expect.findElement(condition) != null);
				} catch (Exception e) {
					return false;
				}
			}
		};
		try {
			boolean flag = myWait.until(conditionToCheck);
			return flag;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param driver
	 * @param condition
	 * @param timeout
	 * @return true/false
	 * 
	 *         <pre>
	 * {@code
	 *         A Wait method which waits on the By condition of an Element for
	 *         about ${timeout} seconds
	 *         }
	 * </pre>
	 */
	protected boolean waitForPageLaod(WebDriver driver, final By condition,
			long timeout) {
		WebDriverWait myWait = new WebDriverWait(driver, timeout);
		ExpectedCondition<Boolean> conditionToCheck = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver expect) {
				try {
					return (expect.findElement(condition) != null);
				} catch (Exception e) {
					return false;
				}
			}
		};
		try {
			boolean flag = myWait.until(conditionToCheck);
			return flag;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * @param driver
	 * @param frame
	 * @return Frame Element
	 * 
	 *         <pre>
	 * {@code
	 *         Get all <frame> tag elements based on the attribute 'name'from
	 *         the driver and the current URL on driver
	 *         }
	 * </pre>
	 */
	protected WebElement getFrameElement(WebDriver driver, String frame) {
		waitForPageLaod(driver, By.tagName("frame"));
		List<WebElement> elementsList = driver
				.findElements(By.tagName("frame"));
		WebElement frameElement = null;
		for (WebElement webElement : elementsList) {
			if (webElement.getAttribute("name").equals(frame)) {
				frameElement = webElement;
			}
		}
		return frameElement;
	}

	/**
	 * @param driver
	 * @param link
	 * @return Link Element
	 * 
	 *         <pre>
	 * {@code
	 *         Get all <a> tag elements based on the text they were given from
	 *         the driver and the current URL on driver
	 *         }
	 * </pre>
	 */
	protected WebElement getLinkElement(WebDriver driver, String link) {
		List<WebElement> elementsList = driver.findElements(By.tagName("a"));
		WebElement frameElement = null;
		for (WebElement webElement : elementsList) {
			if (webElement.getText().contains(link)) {
				frameElement = webElement;
				break;
			}
		}
		return frameElement;
	}

	/**
	 * @param driver
	 * @param src
	 * @return Image Element
	 * 
	 *         <pre>
	 * {@code
	 *         Get all <img> tag elements from the driver based on the attribute
	 *         'alt' and the current URL on driver
	 *         }
	 * </pre>
	 */
	protected WebElement getImgElement(WebDriver driver, String src) {
		waitForPageLaod(driver, By.tagName("img"));
		List<WebElement> elementsList = driver.findElements(By.tagName("img"));
		WebElement frameElement = null;
		for (WebElement webElement : elementsList) {
			if (webElement.getAttribute("alt").contains(src)) {
				frameElement = webElement;
				break;
			}
		}
		return frameElement;
	}

	/**
	 * @param driver
	 * @param attr
	 * @param input
	 * @return Input Element
	 * 
	 *         <pre>
	 * {@code
	 *         Get all <input> tag elements from the driver based on the
	 *         attribute and the current URL on driver
	 *         }
	 * </pre>
	 */
	protected WebElement getInputElement(WebDriver driver, String attr,
			String input) {
		waitForPageLaod(driver, By.tagName("input"));
		List<WebElement> elementsList = driver
				.findElements(By.tagName("input"));
		WebElement frameElement = null;
		for (WebElement webElement : elementsList) {
			if (webElement.getAttribute(attr).contains(input)) {
				frameElement = webElement;
				break;
			}
		}
		return frameElement;
	}

	/**
	 * @param driver
	 * @param span
	 * @return Span Element
	 * 
	 *         <pre>
	 * {@code
	 *         Get all <span> tag elements from the driver based on the text and
	 *         the current URL on driver
	 *         }
	 * </pre>
	 */
	protected WebElement getSpanElement(WebDriver driver, String span) {
		waitForPageLaod(driver, By.tagName("span"));
		List<WebElement> elementsList = driver.findElements(By.tagName("span"));
		WebElement frameElement = null;
		for (WebElement webElement : elementsList) {
			if (webElement.getText().contains(span)) {
				frameElement = webElement;
				break;
			}
		}
		return frameElement;
	}

	/**
	 * @param driver
	 * @param button
	 * @return Button Element
	 * 
	 *         <pre>
	 * {@code
	 *         Get all <button> tag elements from the driver based on the
	 *         attribute 'data-operation' and the current URL on driver
	 *         }
	 * </pre>
	 */
	protected WebElement getButtonElement(WebDriver driver, String button,
			String attr) {
		waitForPageLaod(driver, By.tagName("button"));
		List<WebElement> elementsList = driver.findElements(By
				.tagName("button"));
		WebElement frameElement = null;
		for (WebElement webElement : elementsList) {
			if (webElement.getAttribute(attr).contains(button)) {
				frameElement = webElement;
				break;
			}
		}
		return frameElement;
	}

	/**
	 * @param fileName
	 * @param group
	 * @param driver
	 *            <pre>
	 * {@code
	 *            This method takes screenshots and stores in PropertiesLoader
	 *            .getProperty
	 *            ("screenshotDirectory")/screenshots/all/${group}/${fileName}.
	 *            }
	 * </pre>
	 */
	protected static void takeScreenShots(String fileName, String group,
			WebDriver driver) {
		String screenshotDirectory = PropertiesLoader
				.getProperty("testResultsDir") != null ? PropertiesLoader
				.getProperty("testResultsDir") + "/screenshots/all/"
				: new File(".") + "/screenshots/all/";
		String screenshotAbsolutePath = screenshotDirectory
				+ "/"
				+ group
				+ "/"
				+ fileName
				+ "_"
				+ new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")
						.format(new Date()) + "_" + System.currentTimeMillis()
				+ ".png";
		File screenshot = new File(screenshotAbsolutePath);
		if (ScreenshotListener.createFile(screenshot)) {
			try {
				ScreenshotListener.writeScreenshotToFile(driver, screenshot);
			} catch (ClassCastException weNeedToAugmentOurDriverObject) {
				ScreenshotListener.writeScreenshotToFile(driver, screenshot);
			}
			log.info("Written screenshot to " + screenshotAbsolutePath);
		} else {
			log.info("Unable to create " + screenshotAbsolutePath);
		}
	}

	/**
	 * @param data
	 * @param key
	 * @return Map of InputValues
	 * 
	 *         <pre>
	 * {@code
	 *         This Method will parse the SuiteConfig data model based on the
	 *         key say'input' used for especially parsing the Input values from
	 *         ExcelData.
	 *         }
	 * </pre>
	 */
	protected static Map<String, String> parse(SuiteConfig data, String key) {
		Map<String, String> map = new TreeMap<String, String>();
		String token = null;
		if (key.equals("input"))
			token = data.getInput();
		if (token != null) {
			String[] values = token.split("\\|");
			for (int i = 0; i < values.length; i++) {
				String string = values[i];
				if (string.startsWith("query")) {
					map.put("query",
							string.substring(string.indexOf("=") + 1,
									string.length()));
				} else {
					String[] split = string.split("=");
					if (split.length > 1)
						map.put(split[0], split[1]);
					else
						map.put(split[0], "");
				}
			}
		}
		return map;
	}
}