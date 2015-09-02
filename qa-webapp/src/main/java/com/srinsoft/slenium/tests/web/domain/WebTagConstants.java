package com.srinsoft.slenium.tests.web.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class WebTagConstants {

	private static final Map<String, List<WebElement>> allElementsMap = new TreeMap<String, List<WebElement>>();
	public static final List<String> allElementsList = new ArrayList<String>();
	private static WebDriver driver;

	public static WebDriver getDriver() {
		return driver;
	}

	public static void setDriver(WebDriver driver) {
		WebTagConstants.driver = driver;
		setAllElementsMap(driver);
	}

	private static void setAllElementsMap(WebDriver driver) {
		/* Add All Elements in Map */
		allElementsMap.put("a", driver.findElements(By.tagName("a")));// hyperlink
																		// CHANGED
		allElementsMap.put("audio", driver.findElements(By.tagName("audio")));// audio
																				// stream
																				// NEW
		allElementsMap.put("button", driver.findElements(By.tagName("button")));// button
		allElementsMap.put("canvas", driver.findElements(By.tagName("canvas")));
		allElementsMap.put("command",
				driver.findElements(By.tagName("command")));
		allElementsMap.put("datalist", driver.findElements(By.tagName("")));
		allElementsMap.put("div", driver.findElements(By.tagName("div")));
		allElementsMap.put("fieldset",
				driver.findElements(By.tagName("fieldset")));
		allElementsMap.put("form", driver.findElements(By.tagName("form")));
		allElementsMap.put("h1", driver.findElements(By.tagName("h1")));// heading
		allElementsMap.put("h2", driver.findElements(By.tagName("h2")));// heading
		allElementsMap.put("h3", driver.findElements(By.tagName("h3")));// heading
		allElementsMap.put("h4", driver.findElements(By.tagName("h4")));// heading
		allElementsMap.put("h5", driver.findElements(By.tagName("h5")));// heading
		allElementsMap.put("h6", driver.findElements(By.tagName("h6")));// heading
		allElementsMap.put("head", driver.findElements(By.tagName("head")));// document
																			// metadata
																			// container
		allElementsMap.put("header", driver.findElements(By.tagName("header")));// header
																				// NEW
		allElementsMap.put("hgroup", driver.findElements(By.tagName("hgroup")));// heading
																				// group
																				// NEW
		allElementsMap.put("iframe", driver.findElements(By.tagName("iframe")));// nested
																				// browsing
																				// context
																				// (inline
																				// frame)
		allElementsMap.put("frame", driver.findElements(By.tagName("frame")));
		allElementsMap.put("frameset",
				driver.findElements(By.tagName("frameset")));
		allElementsMap.put("img", driver.findElements(By.tagName("img")));// image
		allElementsMap.put("input", driver.findElements(By.tagName("input")));// input
																				// control
																				// CHANGED
		allElementsMap.put("label", driver.findElements(By.tagName("label")));// caption
																				// for
																				// a
																				// form
																				// control
		allElementsMap.put("li", driver.findElements(By.tagName("li")));// list
																		// item
		allElementsMap.put("link", driver.findElements(By.tagName("link")));// inter-document
																			// relationship
																			// metadata
		allElementsMap.put("map", driver.findElements(By.tagName("map")));// image-map
																			// definition
		allElementsMap.put("menu", driver.findElements(By.tagName("menu")));// list
																			// of
																			// commands
																			// CHANGED
		allElementsMap.put("nav", driver.findElements(By.tagName("nav")));// group
																			// of
		allElementsMap.put("ol", driver.findElements(By.tagName("ol")));// ordered
																		// list
		allElementsMap.put("option", driver.findElements(By.tagName("option")));// option
		allElementsMap.put("p", driver.findElements(By.tagName("p")));// paragraph
		allElementsMap.put("param", driver.findElements(By.tagName("param")));// initialization
		allElementsMap.put("section",
				driver.findElements(By.tagName("section")));// section NEW
		allElementsMap.put("select", driver.findElements(By.tagName("select")));// option-selection
		allElementsMap.put("source", driver.findElements(By.tagName("source")));// media
																				// source
		allElementsMap.put("span", driver.findElements(By.tagName("span")));// generic
																			// span
		allElementsMap.put("table", driver.findElements(By.tagName("table")));// table
		allElementsMap.put("tbody", driver.findElements(By.tagName("tbody")));// table
																				// row
		allElementsMap.put("td", driver.findElements(By.tagName("td")));// table
																		// cell
		allElementsMap.put("textarea",
				driver.findElements(By.tagName("textarea")));// text input
		allElementsMap.put("tfoot", driver.findElements(By.tagName("tfoot")));// table
																				// footer
		allElementsMap.put("th", driver.findElements(By.tagName("th")));// table
																		// header
																		// cell
		allElementsMap.put("thead", driver.findElements(By.tagName("thead")));// table
																				// heading
		allElementsMap.put("title", driver.findElements(By.tagName("title")));// document
																				// title
		allElementsMap.put("tr", driver.findElements(By.tagName("tr")));// table
																		// row
		allElementsMap.put("track", driver.findElements(By.tagName("track")));// supplementary
		allElementsMap.put("ul", driver.findElements(By.tagName("ul")));// unordered
																		// list
		allElementsMap.put("var", driver.findElements(By.tagName("var")));// variable
																			// or
		allElementsMap.put("video", driver.findElements(By.tagName("video")));// video
																				// NEW

		/* Display all Elements in List */

		allElementsList.add("a");// hyperlink CHANGED
		allElementsList.add("audio");// audio stream NEW
		allElementsList.add("button");// button
		allElementsList.add("canvas");
		allElementsList.add("command");
		allElementsList.add("datalist");
		allElementsList.add("div");
		allElementsList.add("fieldset");
		allElementsList.add("form");
		allElementsList.add("h1");// heading
		allElementsList.add("h2");// heading
		allElementsList.add("h3");// heading
		allElementsList.add("h4");// heading
		allElementsList.add("h5");// heading
		allElementsList.add("h6");// heading
		allElementsList.add("head");// document metadata container
		allElementsList.add("header");// header NEW
		allElementsList.add("hgroup");// heading group NEW
		allElementsList.add("iframe");// nested browsing context (inline frame)
		allElementsList.add("frame");
		allElementsList.add("frameset");
		allElementsList.add("img");// image
		allElementsList.add("input");// input control CHANGED
		allElementsList.add("label");// caption for a form control
		allElementsList.add("li");// list item
		allElementsList.add("link");// inter-document relationship metadata
		allElementsList.add("map");// image-map definition
		allElementsList.add("menu");// list of commands CHANGED
		allElementsList.add("nav");// group of navigational links NEW
		allElementsList.add("ol");// ordered list
		allElementsList.add("option");// option
		allElementsList.add("p");// paragraph
		allElementsList.add("param");// initialization parameters for plugins
		allElementsList.add("section");// section NEW
		allElementsList.add("select");// option-selection form control
		allElementsList.add("source");// media source NEW
		allElementsList.add("span");// generic span
		allElementsList.add("table");// table
		allElementsList.add("tbody");// table row group
		allElementsList.add("td");// table cell
		allElementsList.add("textarea");// text input area
		allElementsList.add("tfoot");// table footer row group
		allElementsList.add("th");// table header cell
		allElementsList.add("thead");// table heading group
		allElementsList.add("title");// document title
		allElementsList.add("tr");// table row
		allElementsList.add("track");// supplementary media track NEW
		allElementsList.add("ul");// unordered list
		allElementsList.add("var");// variable or placeholder text
		allElementsList.add("video");// video NEW
	}

	public static Map<String, List<WebElement>> getAllElementsAsMap() {
		return allElementsMap;
	}

	public static WebElement getElementByTag(String tag, String text) {
		List<WebElement> list = allElementsMap.get(tag);
		List<WebElement> displayables = new ArrayList<WebElement>();
		WebElement webElement = null;
		for (WebElement element : list) {
			if (element.isDisplayed()) {
				displayables.add(element);
			}
		}
		String matcher;
		for (WebElement element : list) {
			if (element.getText().contains(text)) {
				webElement = element;
				break;
			}
			matcher = element.getAttribute("id");
			if (matcher != null) {
				if (valuesNotNullAndMatches(matcher, text))
					webElement = element;
				break;
			}
			matcher = element.getAttribute("name");
			if (matcher != null) {
				if (valuesNotNullAndMatches(matcher, text))
					webElement = element;
				break;
			}
			matcher = element.getAttribute("placeholder");
			if (matcher != null) {
				if (valuesNotNullAndMatches(matcher, text))
					webElement = element;
				break;
			}
			matcher = element.getAttribute("value");
			if (matcher != null) {
				if (valuesNotNullAndMatches(matcher, text))
					webElement = element;
				break;
			}
			matcher = element.getAttribute("type");
			if (matcher != null) {
				if (valuesNotNullAndMatches(matcher, text))
					webElement = element;
				break;
			}
		}
		return webElement;
	}

	private static boolean valuesNotNullAndMatches(String matcher, String value) {
		if (matcher.matches(value))
			return true;
		else
			return false;
	}
}
