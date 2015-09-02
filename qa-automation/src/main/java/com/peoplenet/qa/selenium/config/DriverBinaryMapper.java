package com.peoplenet.qa.selenium.config;

import static com.peoplenet.qa.selenium.config.DriverBinaryContext.binaryFor;
import static com.peoplenet.qa.selenium.config.DriverBinaryContext.binaryPath;
import static com.peoplenet.qa.selenium.config.DriverType.Chrome;
import static com.peoplenet.qa.selenium.config.DriverType.IE;
import static com.peoplenet.qa.selenium.config.DriverType.useRemoteWebDriver;
import static com.peoplenet.qa.selenium.config.OperatingSystem.LINUX;
import static com.peoplenet.qa.selenium.config.OperatingSystem.MAC;
import static com.peoplenet.qa.selenium.config.OperatingSystem.WINDOWS;
import static com.peoplenet.qa.selenium.config.SystemArchitecture.ARCHITECTURE_32_BIT;
import static com.peoplenet.qa.selenium.config.SystemArchitecture.ARCHITECTURE_64_BIT;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * @author Anil.Vunnava
 * 
 *         This Class gets the Binary executable file for IE,Chrome,Firefox and
 *         Safari browsers
 *
 */
public class DriverBinaryMapper {
	private static final Logger log = Logger.getLogger("DriverBinaryMapper");
	private static final Map<DriverBinaryContext, String> binaryLocation = new HashMap<DriverBinaryContext, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -7659559973028198457L;

		{
			put(binaryFor(Chrome, WINDOWS, ARCHITECTURE_64_BIT),
					binaryPath("/windows/googlechrome/64bit/2.9/chromedriver.exe"));
			put(binaryFor(Chrome, WINDOWS, ARCHITECTURE_32_BIT),
					binaryPath("/windows/googlechrome/32bit/2.9/chromedriver.exe"));

			put(binaryFor(Chrome, MAC, ARCHITECTURE_64_BIT),
					binaryPath("/osx/googlechrome/64bit/2.9/chromedriver"));
			put(binaryFor(Chrome, MAC, ARCHITECTURE_32_BIT),
					binaryPath("/osx/googlechrome/32bit/2.9/chromedriver"));

			put(binaryFor(Chrome, LINUX, ARCHITECTURE_64_BIT),
					binaryPath("/linux/googlechrome/64bit/2.9/chromedriver"));
			put(binaryFor(Chrome, LINUX, ARCHITECTURE_32_BIT),
					binaryPath("/linux/googlechrome/32bit/2.9/chromedriver"));

			put(binaryFor(DriverType.PHANTOMJS, WINDOWS, ARCHITECTURE_64_BIT),
					binaryPath("/windows/phantomjs/64bit/2.0.0/phantomjs.exe"));
			put(binaryFor(DriverType.PHANTOMJS, WINDOWS, ARCHITECTURE_32_BIT),
					binaryPath("/windows/phantomjs/32bit/2.0.0/phantomjs.exe"));

			put(binaryFor(DriverType.PHANTOMJS, LINUX, ARCHITECTURE_64_BIT),
					binaryPath("/linux/phantomjs/64bit/1.9.8/phantomjs"));
			put(binaryFor(DriverType.PHANTOMJS, LINUX, ARCHITECTURE_32_BIT),
					binaryPath("/linux/phantomjs/32bit/1.9.8/phantomjs"));

			put(binaryFor(DriverType.PHANTOMJS, MAC, ARCHITECTURE_64_BIT),
					binaryPath("/osx/phantomjs/64bit/2.0.0/phantomjs"));
			put(binaryFor(DriverType.PHANTOMJS, MAC, ARCHITECTURE_32_BIT),
					binaryPath("/osx/phantomjs/32bit/2.0.0/phantomjs"));

			put(binaryFor(IE, WINDOWS, ARCHITECTURE_64_BIT),
					binaryPath("/windows/internetexplorer/64bit/2.45.0/IEDriverServer.exe"));
			put(binaryFor(IE, WINDOWS, ARCHITECTURE_32_BIT),
					binaryPath("/windows/internetexplorer/32bit/2.45.0/IEDriverServer.exe"));

		}
	};

	static void configureBinary(DriverType driverType,
			OperatingSystem operatingSystem,
			SystemArchitecture systemArchitecture) {
		if (!useRemoteWebDriver) {
			final String binarySystemProperty = driverType
					.getWebDriverSystemPropertyKey();

			if (null != binarySystemProperty
					&& binarySystemProperty.length() != 0) {
				final String binaryConfiguration = binaryLocation
						.get(binaryFor(driverType, operatingSystem,
								systemArchitecture));
				log.info("Setting System Property '" + binarySystemProperty
						+ "'='" + binaryConfiguration + "'");
				System.setProperty(binarySystemProperty, binaryConfiguration);
			}
		}
	}

	static String getBinaryPath(DriverType browserType, OperatingSystem osName,
			SystemArchitecture systemArch) {
		return binaryLocation.get(binaryFor(browserType, osName, systemArch));
	}
}