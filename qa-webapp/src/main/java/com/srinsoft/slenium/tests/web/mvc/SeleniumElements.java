package com.srinsoft.slenium.tests.web.mvc;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.srinsoft.slenium.tests.web.domain.WebElementPojo;
import com.srinsoft.slenium.tests.web.domain.WebTagConstants;

public class SeleniumElements {

	public static List<WebElementPojo> getWebElements(String url) {
		List<WebElementPojo> displayables = new ArrayList<WebElementPojo>();
		WebDriver driver = new FirefoxDriver();
		try {
			driver.get(url);
			WebTagConstants.setDriver(driver);
			List<WebElement> elements = driver.findElements(By.xpath("//*"));
			System.out.println(elements.size());
			for (WebElement element : elements) {
				if (element.getAttribute("id") != null
						&& element.getAttribute("name") != null) {
					displayables
							.add(new WebElementPojo(element.getAttribute("id"),
									element.getAttribute("name"), element
											.getText(), element
											.getAttribute("value"), element
											.getAttribute("type"), element
											.getTagName()));
				}
			}
			driver.quit();
		} catch (Exception e) {
			driver.quit();
			e.printStackTrace();
		}
		return displayables;
	}
}
