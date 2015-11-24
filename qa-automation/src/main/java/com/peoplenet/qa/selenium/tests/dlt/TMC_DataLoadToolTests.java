package com.peoplenet.qa.selenium.tests.dlt;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.apache.log4j.Logger;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

import org.seleniumhq.selenium.fluent.FluentWebDriver;
import org.seleniumhq.selenium.fluent.FluentWebElement;
import org.seleniumhq.selenium.fluent.FluentWebElements;

/**
 * @author Anil.Vunnava
 *
 *         TMC_DataLoadToolTests
 */
public class TMC_DataLoadToolTests extends AbstractSmokeTests {

	private static final Logger log = Logger.getLogger("TMC_DataLoadToolTests");
	private static final long wait = Long.valueOf(PropertiesLoader
			.getProperty("wait"));

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 */
	public boolean CreateTemplate(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false);
		try {
			FluentWebDriver fluent = new FluentWebDriver(driver);
			Thread.sleep(wait);
			takeScreenShots("/DLT/CreateTemplate", data.getGroup(), driver);
			Select menu = new Select(getTMCMenu(driver).get("Tasks")
					.getWebElement());
			menu.selectByVisibleText("Data Load Tool");
			Thread.sleep(wait);
			Thread.sleep(wait);
			takeScreenShots("/DLT/CreateTemplate", data.getGroup(), driver);

			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}
			Thread.sleep(wait);

			fluent.div(By.className("selectize-input")).click();
			Thread.sleep(1500);
			fluent.input(By.className("ui-select-search")).sendKeys(
					inputValues.get("client"));
			Thread.sleep(1500);

			FluentWebElements optionList = fluent
					.div(By.className("selectize-dropdown"))
					.div(By.className("selectize-dropdown-content"))
					.divs(By.className("option"));
			log.info("Total Options Count : " + optionList.size());
			for (FluentWebElement option : optionList) {
				if (option.getText().toString()
						.equals(inputValues.get("client"))) {
					log.info("Client Matched : " + option.getText());
					log.info("Option Text : " + option.getText().toString());
					option.click();
					break;
				}
			}

			Thread.sleep(1500);
			takeScreenShots("/DLT/CreateTemplate", data.getGroup(), driver);
			FluentWebElement login = fluent.button(By
					.xpath("contains(.,'Login')"));
			if (login.isEnabled().value() && login.isDisplayed().value())
				login.click();
			Thread.sleep(wait);
			takeScreenShots("/DLT/CreateTemplate", data.getGroup(), driver);

			/*
			 * 
			 * Write your implementation for Testing and a sample test code is
			 * shown below
			 * 
			 * 
			 * FluentWebElement element =
			 * fluent.button(By.xpath("contains(.,'Submit')")); if
			 * (element.isDisplayed().value() && element.isEnabled().value()) {
			 * element.click(); Thread.sleep(wait); }
			 * 
			 * FluentWebElement element = fluent.input(By.name("UserId")); if
			 * (element.isDisplayed().value() && element.isEnabled().value()) {
			 * //element.sendKeys("test@test.com");
			 * element.sendKeys(inputValues.get("userId")); Thread.sleep(wait);
			 * }
			 * 
			 * FluentWebElement element = fluent.input(By.id("Password")); if
			 * (element.isDisplayed().value() && element.isEnabled().value()) {
			 * //element.sendKeys("password");
			 * element.sendKeys(inputValues.get("password"));
			 * Thread.sleep(wait); }
			 * 
			 * FluentWebElement element = fluent.link(By.class("hyperlink")); if
			 * (element.isDisplayed().value() && element.isEnabled().value()) {
			 * //element.sendKeys("hyperlink");
			 * element.sendKeys(inputValues.get("hyperlink"));
			 * Thread.sleep(wait); }
			 */
			driver.quit();
			return true;
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
			return false;
		}
	}

	public Map<String, FluentWebElement> getTMCMenu(WebDriver driver) {
		Map<String, FluentWebElement> menuItems = new HashMap<String, FluentWebElement>();
		try {
			driver.switchTo().frame("Menu");
			FluentWebDriver fluent = new FluentWebDriver(driver);

			FluentWebElement menuTable = fluent.table(By.className("nav"));
			if (menuTable.isDisplayed().value()
					&& menuTable.isEnabled().value()) {
				FluentWebElements menuTRS = menuTable.trs();
				for (FluentWebElement menuTr : menuTRS) {
					FluentWebElements menuTDS = menuTr.tds();
					for (FluentWebElement menuItem : menuTDS) {
						FluentWebElements links = menuItem.links();
						for (FluentWebElement link : links) {
							if (link.isDisplayed().value()
									&& link.isEnabled().value()) {
								menuItems.put(link.getText().toString(), link);
							}
						}
						FluentWebElements selects = menuItem.selects();
						for (FluentWebElement select : selects) {
							if (select.isDisplayed().value()
									&& select.isEnabled().value()) {
								menuItems.put(select.getAttribute("name")
										.toString(), select);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return menuItems;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests#tests()
	 */
	@Override
	public void tests() {
		String content = new String();
		String groupTests = "DLT";

		for (SuiteConfig data : suiteConfig) {
			if (data.getGroup().equals(groupTests)) {
				if (data.getStatus().equals("No")) {
					log.info(data.getScript() + " Execution on Bowser - "
							+ data.getBrowser()
							+ " is configured not to run and Skipped ....!");
				} else {
					Map<String, String> inputValues = parse(data, "input");
					if (data.getScript().startsWith("TMC_DataLoadTool")) {
						long startTime = System.currentTimeMillis();
						boolean status = CreateTemplate(data, inputValues);
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
		HtmlReportGenerator.setHtmlContent(content);
	}
}
