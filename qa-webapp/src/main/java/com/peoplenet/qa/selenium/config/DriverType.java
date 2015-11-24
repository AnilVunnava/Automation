package com.peoplenet.qa.selenium.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.peoplenet.qa.selenium.factory.PropertiesLoader;

import static com.peoplenet.qa.selenium.config.DriverBinaryMapper.configureBinary;
import static com.peoplenet.qa.selenium.config.DriverBinaryMapper.getBinaryPath;
import static com.peoplenet.qa.selenium.config.OperatingSystem.getOperatingSystem;
import static com.peoplenet.qa.selenium.config.SystemArchitecture.getSystemArchitecture;

/**
 * @author Anil.Vunnava
 *
 *         DriverType Class will be used to instantiate the DriverType based on
 *         the parameter 'remoteBrowser=true/false' The basic Driver setup is
 *         done on the below browsers Chrome Firefox IE8 IE9 IE10 IE11 Safari
 */
public enum DriverType implements DriverSetup {

	Firefox {
		public DesiredCapabilities getDesiredCapabilities() {
			return DesiredCapabilities.firefox();
		}

		public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
			String desiredBrowserVersion = PropertiesLoader
					.getProperty("desiredBrowserVersion");
			String desiredPlatform = PropertiesLoader
					.getProperty("desiredPlatform");

			if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
				capabilities.setPlatform(Platform.valueOf(desiredPlatform
						.toUpperCase()));
			}

			if (null != desiredBrowserVersion
					&& !desiredBrowserVersion.isEmpty()) {
				capabilities.setVersion(desiredBrowserVersion);
			}
			return new FirefoxDriver(capabilities);
		}
	},
	Chrome {
		public DesiredCapabilities getDesiredCapabilities() {
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setCapability("chrome.switches",
					Arrays.asList("--no-default-browser-check"));
			HashMap<String, String> chromePreferences = new HashMap<String, String>();
			chromePreferences.put("profile.password_manager_enabled", "false");
			capabilities.setCapability("chrome.prefs", chromePreferences);
			return capabilities;
		}

		public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
			String desiredBrowserVersion = PropertiesLoader
					.getProperty("desiredBrowserVersion");
			String desiredPlatform = PropertiesLoader
					.getProperty("desiredPlatform");

			if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
				capabilities.setPlatform(Platform.valueOf(desiredPlatform
						.toUpperCase()));
			}

			if (null != desiredBrowserVersion
					&& !desiredBrowserVersion.isEmpty()) {
				capabilities.setVersion(desiredBrowserVersion);
			}
			if (PropertiesLoader.getProperty("binary") != null) {
				ChromeOptions options = new ChromeOptions();
				options.setBinary(PropertiesLoader.getProperty("binary"));
				ChromeDriver chrome = new ChromeDriver(options);
				return chrome;
			} else if (Boolean.valueOf(PropertiesLoader.getProperty("mobileView"))) {
				Map<String, String> mobileEmulation = new HashMap<String, String>();
				mobileEmulation.put("deviceName",
						PropertiesLoader.getProperty("deviceName"));
				ChromeOptions options = new ChromeOptions();
				options.setExperimentalOption("mobileEmulation", mobileEmulation);
				
				return new ChromeDriver(options);
			}else{
				return new ChromeDriver(capabilities);
			}
		}

		@Override
		public String getWebDriverSystemPropertyKey() {
			return "webdriver.chrome.driver";
		}
	},
	IE {
		public DesiredCapabilities getDesiredCapabilities() {
			DesiredCapabilities capabilities = DesiredCapabilities
					.internetExplorer();
			capabilities.setCapability(
					CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION,
					true);
			capabilities.setCapability(
					InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, true);
			capabilities.setCapability("requireWindowFocus", true);
			return capabilities;
		}

		public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
			String desiredBrowserVersion = PropertiesLoader
					.getProperty("desiredBrowserVersion");
			String desiredPlatform = PropertiesLoader
					.getProperty("desiredPlatform");

			if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
				capabilities.setPlatform(Platform.valueOf(desiredPlatform
						.toUpperCase()));
			}

			if (null != desiredBrowserVersion
					&& !desiredBrowserVersion.isEmpty()) {
				capabilities.setVersion(desiredBrowserVersion);
			}
			return new InternetExplorerDriver(capabilities);
		}

		@Override
		public String getWebDriverSystemPropertyKey() {
			return "webdriver.ie.driver";
		}
	},
	Safari {
		public DesiredCapabilities getDesiredCapabilities() {
			DesiredCapabilities capabilities = DesiredCapabilities.safari();
			capabilities.setCapability("safari.cleanSession", true);
			return capabilities;
		}

		public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
			String desiredBrowserVersion = PropertiesLoader
					.getProperty("desiredBrowserVersion");
			String desiredPlatform = PropertiesLoader
					.getProperty("desiredPlatform");

			if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
				capabilities.setPlatform(Platform.valueOf(desiredPlatform
						.toUpperCase()));
			} else {
				capabilities.setPlatform(Platform.MAC);
			}

			if (null != desiredBrowserVersion
					&& !desiredBrowserVersion.isEmpty()) {
				capabilities.setVersion(desiredBrowserVersion);
			}
			return new SafariDriver();
		}
	},
	PHANTOMJS {
		public DesiredCapabilities getDesiredCapabilities() {
			DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
			capabilities.setCapability("takesScreenshot", true);
			capabilities.setCapability(
					PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
					getBinaryPath(PHANTOMJS, operatingSystem,
							systemArchitecture));
			return capabilities;
		}

		public WebDriver getWebDriverObject(DesiredCapabilities capabilities) {
			String desiredBrowserVersion = PropertiesLoader
					.getProperty("desiredBrowserVersion");
			String desiredPlatform = PropertiesLoader
					.getProperty("desiredPlatform");

			if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
				capabilities.setPlatform(Platform.valueOf(desiredPlatform
						.toUpperCase()));
			}

			if (null != desiredBrowserVersion
					&& !desiredBrowserVersion.isEmpty()) {
				capabilities.setVersion(desiredBrowserVersion);
			}
			return new PhantomJSDriver(capabilities);
		}
	};

	public static final DriverType defaultDriverType = IE;
	public static final boolean useRemoteWebDriver = Boolean
			.valueOf(PropertiesLoader.getProperty("remoteDriver"));
	private static final OperatingSystem operatingSystem = getOperatingSystem();
	private static final SystemArchitecture systemArchitecture = getSystemArchitecture();
	private static final Logger log = Logger.getLogger("DriverType");

	public String getWebDriverSystemPropertyKey() {
		return null;
	}

	public WebDriver instantiateWebDriver() throws MalformedURLException {
		DesiredCapabilities desiredCapabilities = getDesiredCapabilities();

		if (useRemoteWebDriver) {
			URL seleniumGridURL = new URL(
					PropertiesLoader.getProperty("gridURL"));
			String desiredBrowserVersion = PropertiesLoader
					.getProperty("desiredBrowserVersion");
			String desiredPlatform = PropertiesLoader
					.getProperty("desiredPlatform");

			if (null != desiredPlatform && !desiredPlatform.isEmpty()) {
				desiredCapabilities.setPlatform(Platform
						.valueOf(desiredPlatform.toUpperCase()));
			} else {
				desiredCapabilities.setPlatform(Platform.WIN8);
			}

			if (null != desiredBrowserVersion
					&& !desiredBrowserVersion.isEmpty()) {
				desiredCapabilities.setVersion(desiredBrowserVersion);
			}

			return new RemoteWebDriver(seleniumGridURL, desiredCapabilities);
		}

		return getWebDriverObject(desiredCapabilities);
	}

	public static DriverType determineEffectiveDriverType(String browser) {
		DriverType driverType = defaultDriverType;
		try {
			driverType = valueOf(browser);
		} catch (IllegalArgumentException ignored) {
			log.info("Unknown driver specified, defaulting to '" + driverType
					+ "'...");
		} catch (NullPointerException ignored) {
			log.info("No driver specified, defaulting to '" + driverType
					+ "'...");
		}

		return driverType;
	}

	public static DriverType getEffectiveDriverType(String browser) {
		DriverType driverType = defaultDriverType;
		try {
			driverType = valueOf(browser);
		} catch (IllegalArgumentException ignored) {
			log.info("Unknown driver specified, defaulting to '" + driverType
					+ "'...");
		} catch (NullPointerException ignored) {
			log.info("No driver specified, defaulting to '" + driverType
					+ "'...");
		}

		return driverType;
	}

	public WebDriver configureDriverBinaryAndInstantiateWebDriver() {
		log.info("Current Operating System: "
				+ operatingSystem.getOperatingSystemType());
		log.info("Current Architecture: "
				+ systemArchitecture.getSystemArchitectureType());
		log.info("Current Browser Selection: " + this);

		configureBinary(this, operatingSystem, systemArchitecture);

		try {
			return instantiateWebDriver();
		} catch (MalformedURLException urlIsInvalid) {
			urlIsInvalid.printStackTrace();
			return null;
		}
	}
}