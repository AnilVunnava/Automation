package com.peoplenet.qa.selenium.tests;

import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava Employee Login and Log-off Tests
 */
public class BaseEmployeeLoginLogOffTests extends AbstractSmokeTests {

	/**
	 * @param data
	 * @return
	 * 
	 *         This method call the base methods from Super Class TMCSmokeTests
	 */
	public boolean EmployeeLoginAndLogoff(SuiteConfig data) {
		WebDriver driver = null;
		try {
			driver = registerAndLogin(data, true);
			driver.quit();
			return true;
		} catch (Exception e) {
			if (driver != null)
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
	@Override
	public void tests() {
		String content = new String();
		String groupTests = "EmployeeTest";
		//long start = System.currentTimeMillis();
		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "	+ data.getBrowser()	+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().contains("EmployeeLogin")) {
						long startTime = System.currentTimeMillis();
						boolean status = EmployeeLoginAndLogoff(data);
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
		//long end = System.currentTimeMillis();
		// String group = HtmlReportGenerator.getGroupHtml(groupTests + "	["
		// + HtmlReportGenerator.getTime(end - start) + "]");
		HtmlReportGenerator.setHtmlContent(content);
	}
}