package com.peoplenet.qa.selenium.config;

import com.peoplenet.qa.selenium.factory.PropertiesLoader;

/**
 * @author Anil.Vunnava
 * 
 *         This class is to load and detect the OS and the System architecture
 *         [32/64 bit PC] with the specific DriverType
 */
public class DriverBinaryContext {

	public static final String rootPath = PropertiesLoader
			.getProperty("binaryRootFolder");
	public static final String ROOT_FOLDER = rootPath != null ? rootPath
			: "selenium_standalone_binaries";
	private final DriverType driverType;
	private final SystemArchitecture systemArchitecture;
	private final OperatingSystem operatingSystem;

	private DriverBinaryContext(DriverType driverType,
			OperatingSystem operatingSystem,
			SystemArchitecture systemArchitecture) {
		this.operatingSystem = operatingSystem;
		this.driverType = driverType;
		this.systemArchitecture = systemArchitecture;
	}

	static DriverBinaryContext binaryFor(DriverType browserType,
			OperatingSystem osName, SystemArchitecture architecture) {
		return new DriverBinaryContext(browserType, osName, architecture);
	}

	static String binaryPath(String relativePath) {
		return ROOT_FOLDER + relativePath;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		DriverBinaryContext that = (DriverBinaryContext) o;

		if (driverType != that.driverType)
			return false;
		if (operatingSystem != that.operatingSystem)
			return false;
		if (systemArchitecture != that.systemArchitecture)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = driverType.hashCode();
		result = 31 * result + systemArchitecture.hashCode();
		result = 31 * result + operatingSystem.hashCode();
		return result;
	}
}