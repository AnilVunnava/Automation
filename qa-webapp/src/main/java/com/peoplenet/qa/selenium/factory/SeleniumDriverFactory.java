package com.peoplenet.qa.selenium.factory;

import static com.peoplenet.qa.selenium.config.DriverType.determineEffectiveDriverType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.peoplenet.qa.selenium.config.DriverType;
import com.peoplenet.qa.selenium.data.AccessConfig;
import com.peoplenet.qa.selenium.data.ExcelUtils;
import com.peoplenet.qa.selenium.data.SuiteConfig;

/**
 * @author Anil.Vunnava
 *
 *         SeleniumDriverFactory is used to store SuiteConfig model list and
 *         AccessConfig model list and get Driver based on browser.
 */
public class SeleniumDriverFactory {

	public static Map<String, ThreadLocal<WebDriver>> webDriverPool = Collections
			.synchronizedMap(new TreeMap<String, ThreadLocal<WebDriver>>());
	public static List<SuiteConfig> suiteConfig = new ArrayList<SuiteConfig>();
	public static List<AccessConfig> accessConfig = new ArrayList<AccessConfig>();
	private static final Logger log = Logger.getLogger("SeleniumDriverFactory");
	public static final String USERNAME = PropertiesLoader
			.getProperty("remoteUser");
	public static final String AUTOMATE_KEY = PropertiesLoader
			.getProperty("remoteKey");
	public static final String URL = "http://" + USERNAME + ":" + AUTOMATE_KEY
			+ "@hub.browserstack.com/wd/hub";

	public static List<SuiteConfig> getSuiteConfig() {
		return SeleniumDriverFactory.suiteConfig;
	}

	public static void setSuiteConfig(List<SuiteConfig> suiteConfig) {
		SeleniumDriverFactory.suiteConfig = suiteConfig;
	}

	public static List<AccessConfig> getAccessConfig() {
		return SeleniumDriverFactory.accessConfig;
	}

	public static void setAccessConfig(List<AccessConfig> accessConfig) {
		SeleniumDriverFactory.accessConfig = accessConfig;
	}

	/**
	 * Instantiate Driver Object on the browser either to execute Script in
	 * loacal environemnt or on Remote [BrowserStack]
	 */
	@BeforeSuite
	public static void instantiateDriverObject() {
		try {
			if (getSuiteConfig().size() == 0) {
				System.out.println("Loading Suite Config........");
				suiteConfig = ExcelUtils.loadSuiteData();
			}
		} catch (Exception fe) {
			log.info("Unable to load ExcelData : " + fe.getMessage());
		}
		String isRemote = PropertiesLoader.getProperty("remoteDriver");
		log.info("Is Remote Driver Enabled ? " + isRemote);
		if (Boolean.valueOf(isRemote)) {
			for (SuiteConfig config : suiteConfig) {
				if (config.getBrowser().startsWith("IE")) {
					String ie = config.getBrowser();
					String result = ie.substring(ie.indexOf("E") + 1,
							ie.length());
					webDriverPool.put(config.getUniqueScript(),
							getDesiredDriver("IE", result+".0"));
				} else if (config.getBrowser().startsWith("Chrome")) {
					webDriverPool.put(config.getUniqueScript(),
							getDesiredDriver("Chrome", "44.0"));
				} else if (config.getBrowser().startsWith("Firefox")) {
					webDriverPool.put(config.getUniqueScript(),
							getDesiredDriver("Firefox", "40.0"));
				} else if (config.getBrowser().startsWith("Safari")) {
					webDriverPool.put(config.getUniqueScript(),
							getDesiredDriver("Safari", "8.0"));
				}
			}
			webDriverPool.put("T&C", getDesiredDriver("IE", "11"));
		} else {
			for (SuiteConfig config : suiteConfig) {
				if (config.getBrowser().startsWith("IE"))
					webDriverPool
							.put(config.getUniqueScript(),
									getDesiredDriver(determineEffectiveDriverType("IE")));
				else if (config.getBrowser().startsWith("Chrome"))
					webDriverPool
							.put(config.getUniqueScript(),
									getDesiredDriver(determineEffectiveDriverType("Chrome")));
				else if (config.getBrowser().startsWith("Firefox"))
					webDriverPool
							.put(config.getUniqueScript(),
									getDesiredDriver(determineEffectiveDriverType("Firefox")));
				else if (config.getBrowser().startsWith("Safari"))
					webDriverPool
							.put(config.getUniqueScript(),
									getDesiredDriver(determineEffectiveDriverType("Safari")));
			}
			webDriverPool.put("T&C",
					getDesiredDriver(determineEffectiveDriverType("IE")));
		}
	}

	/**
	 * @param type
	 * @return
	 * 
	 *         Get Desired Diver for to execute Script in local Environment
	 */
	private static ThreadLocal<WebDriver> getDesiredDriver(final DriverType type) {

		final ThreadLocal<WebDriver> driverThread = new ThreadLocal<WebDriver>() {
			@Override
			protected WebDriver initialValue() {
				final WebDriver webDriver = type
						.configureDriverBinaryAndInstantiateWebDriver();
				return webDriver;
			}
		};
		return driverThread;
	}

	/**
	 * @param browser
	 * @param version
	 * @return
	 * 
	 *         Get Desired Diver for the RemoteDriver to execute Script on
	 *         BrowserStack
	 */
	private static ThreadLocal<WebDriver> getDesiredDriver(
			final String browser, final String version) {

		final ThreadLocal<WebDriver> driverThread = new ThreadLocal<WebDriver>() {
			@Override
			protected WebDriver initialValue() {
				String desiredPlatform = PropertiesLoader
						.getProperty("desiredPlatform");
				String desiredBrowserVersion = PropertiesLoader
						.getProperty("desiredBrowserVersion");

				DesiredCapabilities caps = new DesiredCapabilities();

				if (desiredPlatform.equals("android")) {
					log.info("Setting Android Browser");
					caps.setCapability("browserName", "android");
					caps.setPlatform(Platform.ANDROID);
					caps.setCapability("device",
							PropertiesLoader.getProperty("deviceName"));
				}

				else if (desiredPlatform.equals("ios")) {
					log.info("Setting IOS Browser");
					caps.setCapability("browserName", "iPhone");
					caps.setPlatform(Platform.MAC);
					caps.setCapability("device",
							PropertiesLoader.getProperty("deviceName"));
				}

				else if (desiredPlatform.equals("ipad")) {
					log.info("Setting IPAD Browser");
					caps.setCapability("browserName", "iPad");
					caps.setPlatform(Platform.MAC);
					caps.setCapability("device",
							PropertiesLoader.getProperty("deviceName"));
				}

				else {
					if ((desiredPlatform == null || desiredPlatform.isEmpty())
							&& !browser.equals("Safari")) {
						caps.setCapability("os", "windows");
					} else {
						caps.setCapability("os", "OS X");
						caps.setPlatform(Platform.MAC);
					}
					if ((desiredBrowserVersion == null || desiredBrowserVersion
							.isEmpty()) && !browser.equals("Safari")) {
						caps.setCapability("os_version", "7");
					} else {
						caps.setCapability("os_version", "Yosemite");
						caps.setCapability("browser_version", version);
					}
				}
				caps.setCapability("browser", browser);
				caps.setCapability("resolution", "1024x768");
				caps.setCapability("build",
						PropertiesLoader.getProperty("buildVersion"));
				caps.setCapability("project",
						PropertiesLoader.getProperty("project"));
				caps.setJavascriptEnabled(true);
				caps.setCapability("handlesAlerts", true);
				caps.setCapability("browserstack.debug", "true");
				caps.setCapability("browserstack.ie.enablePopups", "true");
				caps.setCapability("browserstack.safari.enablePopups", "true");
				caps.setCapability("browserstack.android.enablePopups", "true");
				log.info(caps.asMap());

				WebDriver webDriver = null;
				try {
					webDriver = new RemoteWebDriver(new URL(URL), caps);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				return webDriver;
			}
		};
		return driverThread;
	}

	/**
	 * @param script
	 * @return
	 * 
	 *         Get Driver for the Script
	 */
	public static WebDriver getDriver(String script) {
		return webDriverPool.get(script).get();
	}

	/**
	 * @return
	 * 
	 *         Returns default Chrome
	 */
	public static WebDriver getDefaultDriver() {
		return getDesiredDriver("Chrome", "45").get();
	}

	/**
	 * Closing Driver Object
	 */
	@AfterSuite
	public static void closeDriverObject() {
		for (SuiteConfig data : suiteConfig) {
			if (!data.getStatus().equals("No")) {
				webDriverPool.get(data.getScript()).get().quit();
			}
		}
	}
}