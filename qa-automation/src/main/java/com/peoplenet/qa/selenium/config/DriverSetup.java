package com.peoplenet.qa.selenium.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author Anil.Vunnava
 *
 *         Interface for DriverSetup a DTO Pattern
 */
public interface DriverSetup {

	WebDriver getWebDriverObject(DesiredCapabilities desiredCapabilities);

	DesiredCapabilities getDesiredCapabilities();
}