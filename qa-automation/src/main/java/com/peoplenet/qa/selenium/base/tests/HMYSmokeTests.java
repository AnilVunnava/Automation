package com.peoplenet.qa.selenium.base.tests;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.testng.annotations.Test;

import com.peoplenet.qa.selenium.data.AccessConfig;
import com.peoplenet.qa.selenium.data.SuiteConfig;
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
public abstract class HMYSmokeTests extends SeleniumDriverFactory {

	public static WebElement userid;
	public static WebElement password;
	public static WebElement submit;
	public static WebDriver driver;
	protected static final Logger log = Logger.getLogger("HMYSmokeTests");
	protected static final SimpleDateFormat formatData = new SimpleDateFormat(
			"MM/dd/yyyy-HHmmss");

	/**
	 * <pre>
	 * {@code
	 * Abstract TestNG method used to call in sub classes
	 * }
	 * </pre>
	 */
	@Test
	public abstract void tests();

	/**
	 * @param suiteConfig
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
	protected WebDriver registerAndLogin(SuiteConfig suiteConfig, boolean logoff) {
		WebDriver driver = null;
		try {
			AccessConfig data = null;
			for (AccessConfig accessConf : accessConfig) {
				if (accessConf.getScript().equals("Default")) {
					data = accessConf;
				} else if (suiteConfig.getScript().equals(
						accessConf.getScript())) {
					data = accessConf;
				}
			}
			if (data != null) {
				driver = getDriver(suiteConfig.getUniqueScript());
				driver.get(data.getURL());
				userid = driver.findElement(By.id("UserID"));
				password = driver.findElement(By.id("Password"));
				submit = driver.findElement(By.id("SignIn"));

				userid.clear();
				userid.sendKeys(data.getLogin());
				password.clear();
				password.sendKeys(data.getPasword());
				takeScreenShots("registerAndLogin", suiteConfig.getGroup(),
						driver);
				submit.click();

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
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (driver == null) {
			return getDefaultDriver();
		} else {
			return driver;
		}
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
	protected boolean AddEmployee(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			takeScreenShots("/AddEmployee/addAnEmployee", data.getGroup(),
					driver);
			log.info("Input Values : " + inputValues);
			if (getFrameElement(driver, "Table") != null) {
				// Find an Employee
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				driver.findElement(
						By.xpath("//a[contains(text(),'"
								+ inputValues.get("client") + "')]")).click();
				Thread.sleep(1000);
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getLinkElement(driver, inputValues.get("group")).click();
				takeScreenShots("/AddEmployee/addAnEmployee", data.getGroup(),
						driver);
				driver.switchTo().frame(getFrameElement(driver, "Table"));
				getLinkElement(driver, inputValues.get("site")).click();
				takeScreenShots("/AddEmployee/addAnEmployee", data.getGroup(),
						driver);
				if (getFrameElement(driver, "Menu") != null) {
					driver.switchTo().frame(getFrameElement(driver, "Menu"));

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
					takeScreenShots("/AddEmployee/addAnEmployee",
							data.getGroup(), driver);
					driver.findElement(By.id("AddEmpButton")).click();
				}
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				takeScreenShots("/AddEmployee/addAnEmployee", data.getGroup(),
						driver);
				driver.findElement(By.name("EmplSSN")).sendKeys(
						inputValues.get("ssn"));
				getInputElement(driver, "value", "Next >>").click();
				Thread.sleep(500);
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				takeScreenShots("/AddEmployee/addAnEmployee", data.getGroup(),
						driver);
				if (getFrameElement(driver, "Main") != null) {
					// Add an Employee
					driver.switchTo().frame(getFrameElement(driver, "Main"));
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

					Thread.sleep(500);
					takeScreenShots("/AddEmployee/addAnEmployee",
							data.getGroup(), driver);

					driver.findElement(By.id("sitesDepts")).click();
					Thread.sleep(500);
					takeScreenShots("/AddEmployee/addAnEmployee",
							data.getGroup(), driver);
					Select select2 = new Select(driver.findElement(By
							.name("SiteDeptTemplate")));
					select2.selectByValue(inputValues.get("siteTemplate"));
					Robot robot2 = new Robot();

					robot2.keyPress(KeyEvent.VK_ENTER);
					robot2.keyRelease(KeyEvent.VK_ENTER);
					Thread.sleep(500);

					Select select3 = new Select(driver.findElement(By
							.name("PrimaryDept")));
					select3.selectByVisibleText(inputValues.get("primaryDept"));
					Robot robot3 = new Robot();
					robot3.keyPress(KeyEvent.VK_ENTER);
					robot3.keyRelease(KeyEvent.VK_ENTER);
					Thread.sleep(500);

					Select select4 = new Select(driver.findElement(By
							.name("PrimarySite")));
					select4.selectByVisibleText(inputValues.get("primarySite"));
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
				takeScreenShots("/AddEmployee/addAnEmployee", data.getGroup(),
						driver);
				if (getFrameElement(driver, "Navigation") != null) {
					driver.switchTo().frame(
							getFrameElement(driver, "Navigation"));
					driver.findElement(By.name("save")).click();
					Thread.sleep(1000);
				}
				takeScreenShots("/AddEmployee/addAnEmployee", data.getGroup(),
						driver);
				Thread.sleep(1000);
			}
			driver.quit();
			return true;
		} catch (Exception e) {
			takeScreenShots("/AddEmployee/addAnEmployee-error",
					data.getGroup(), driver);
			driver.quit();
			e.printStackTrace();
			return false;
		}

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