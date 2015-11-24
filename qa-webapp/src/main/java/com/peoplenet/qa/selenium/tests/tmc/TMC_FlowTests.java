package com.peoplenet.qa.selenium.tests.tmc;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.seleniumhq.selenium.fluent.FluentSelect;
import org.seleniumhq.selenium.fluent.FluentWebDriver;
import org.seleniumhq.selenium.fluent.FluentWebElement;
import org.seleniumhq.selenium.fluent.FluentWebElements;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.CustomElements;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava
 *
 *         TMC_FlowTests
 */
public class TMC_FlowTests extends AbstractSmokeTests {
	private static final Logger log = Logger.getLogger("TMC_FlowTests");
	private static final long wait = Long.valueOf(PropertiesLoader
			.getProperty("wait"));

	public String getPayPeriod(String payPeriod) {
		String[] payDate = payPeriod.split("/");
		String day = payDate[1];
		String month = payDate[0];
		if (Integer.parseInt(payDate[1]) < 10)
			day = "0" + payDate[1] + "/";
		else
			day = payDate[1] + "/";
		if (Integer.parseInt(payDate[0]) < 10)
			month = "0" + payDate[0] + "/";
		else
			month = payDate[0] + "/";

		return payPeriod = month + day + payDate[2];
	}

	public List<CustomElements> getAllElementsInPage(FluentWebDriver fluent,
			boolean admin) {
		List<CustomElements> allElements = new ArrayList<CustomElements>();
		fluent = new FluentWebDriver(driver);
		FluentWebElement webClientTable = fluent.table(By
				.className("admintable"));
		if (webClientTable.isEnabled().value()
				&& webClientTable.isDisplayed().value()) {
			FluentWebElements trs = webClientTable.trs();
			for (FluentWebElement tr : trs) {
				if (tr.isDisplayed().value() && tr.isEnabled().value()) {
					FluentWebElements tds = tr.tds();
					for (FluentWebElement td : tds) {
						if (td.isDisplayed().value() && td.isEnabled().value()) {
							FluentWebElements headers = td.h3s();
							for (FluentWebElement h3 : headers) {
								if (h3.isEnabled().value()
										&& h3.isDisplayed().value()) {
									String header = h3.getText().toString();
									Map<String, FluentWebElement> map = getAllVisibleElements(
											td, admin);
									for (Entry<String, FluentWebElement> innerElements : map
											.entrySet()) {
										allElements.add(new CustomElements(
												header, innerElements.getKey(),
												innerElements.getValue()));
									}
								}
							}
						}
					}
				}
			}
		}
		return allElements;
	}

	public Map<String, FluentWebElement> getAllVisibleElements(
			FluentWebElement baseElement, boolean admin) {
		Map<String, FluentWebElement> allVisibles = new HashMap<String, FluentWebElement>();
		List<FluentWebElement> allElements = new ArrayList<FluentWebElement>();

		allElements.addAll(baseElement.abbrs());
		allElements.addAll(baseElement.acronyms());
		allElements.addAll(baseElement.addresses());
		allElements.addAll(baseElement.areas());
		allElements.addAll(baseElement.bs());
		allElements.addAll(baseElement.blockquotes());
		allElements.addAll(baseElement.buttons());
		allElements.addAll(baseElement.divs());
		allElements.addAll(baseElement.fieldsets());
		allElements.addAll(baseElement.figures());
		allElements.addAll(baseElement.footers());
		allElements.addAll(baseElement.forms());
		allElements.addAll(baseElement.h1s());
		allElements.addAll(baseElement.h2s());
		allElements.addAll(baseElement.h3s());
		allElements.addAll(baseElement.h4s());
		allElements.addAll(baseElement.headers());
		allElements.addAll(baseElement.imgs());
		allElements.addAll(baseElement.inputs());
		allElements.addAll(baseElement.labels());
		allElements.addAll(baseElement.legends());
		allElements.addAll(baseElement.links());
		allElements.addAll(baseElement.lis());
		allElements.addAll(baseElement.maps());
		allElements.addAll(baseElement.navs());
		allElements.addAll(baseElement.ols());
		allElements.addAll(baseElement.options());
		allElements.addAll(baseElement.ps());
		allElements.addAll(baseElement.pres());
		allElements.addAll(baseElement.selects());
		allElements.addAll(baseElement.spans());
		allElements.addAll(baseElement.tables());
		allElements.addAll(baseElement.tbodys());
		allElements.addAll(baseElement.trs());
		allElements.addAll(baseElement.tds());
		allElements.addAll(baseElement.textareas());
		allElements.addAll(baseElement.ths());

		for (FluentWebElement element : allElements) {
			setElementByAttribute(element, "class", null, allVisibles, admin);
		}
		return allVisibles;

	}

	public void setElementByAttribute(FluentWebElement element,
			String attributeName, String value,
			Map<String, FluentWebElement> allVisibles, boolean admin) {
		if (element.isDisplayed().value() && element.isEnabled().value()) {
			if (admin) {
				String text = element.getText().toString();
				String name = element.getAttribute("name").toString();
				String id = element.getAttribute("id").toString();

				if (text != null && !text.isEmpty()) {
					allVisibles.put(text.trim(), element);
				} else if (id != null && !id.isEmpty()) {
					allVisibles.put(id.trim(), element);
				} else if (name != null && !name.isEmpty()) {
					allVisibles.put(name.trim(), element);
				} else {
					allVisibles.put(element.getAttribute("class").toString()
							.trim(), element);
				}
			} else {
				if (element.getAttribute(attributeName) != null)
					if (value != null) {
						if (value.equalsIgnoreCase(element.getAttribute(
								attributeName).toString())) {
							allVisibles.put(
									element.getText().toString().trim(),
									element);
						}
					} else {
						allVisibles.put(element.getText().toString().trim(),
								element);
					}
			}
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

	public Map<String, FluentWebElement> getAdminFunctions(WebDriver driver) {
		Map<String, FluentWebElement> adminFunctions = new HashMap<String, FluentWebElement>();
		try {
			driver.switchTo().frame("Table");
			FluentWebDriver fluent = new FluentWebDriver(driver);
			FluentWebElement menuTable = fluent.table(By
					.className("maintenance-table"));
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
								adminFunctions.put(link.getText().toString(),
										link);
							}
						}
						FluentWebElements selects = menuItem.selects();
						for (FluentWebElement select : selects) {
							if (select.isDisplayed().value()
									&& select.isEnabled().value()) {
								adminFunctions.put(select.getAttribute("name")
										.toString(), select);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return adminFunctions;
	}

	public Map<String, FluentWebElement> getTMCRole(WebDriver driver) {
		Map<String, FluentWebElement> roleItems = new HashMap<String, FluentWebElement>();
		try {
			FluentWebDriver fluent = new FluentWebDriver(driver);

			FluentWebElements inputs = fluent.div(By.id("tabs-1")).form()
					.inputs();
			for (FluentWebElement input : inputs) {
				if (input.isDisplayed().value() && input.isEnabled().value()) {
					String newRole = input.getAttribute("type").toString();
					if (newRole.equals("text") || newRole.equals("radio")) {
						String key = "";
						if (newRole.equals("radio")) {
							key = input.getAttribute("value").toString() + "-"
									+ input.getAttribute("name").toString();
						} else {
							key = input.getAttribute("name").toString();
						}
						roleItems.put(key, input);
					}
					if (newRole.equals("submit")) {
						roleItems.put(input.getAttribute("value").toString(),
								input);
					}
				}
			}
			FluentWebElements selects = fluent.div(By.id("tabs-1")).form()
					.selects();
			for (FluentWebElement select : selects) {
				if (select.isDisplayed().value() && select.isEnabled().value()) {
					roleItems.put(select.getAttribute("name").toString(),
							select);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roleItems;
	}

	public Map<String, FluentWebElement> getTMCUserInputs(WebDriver driver) {
		Map<String, FluentWebElement> usrItems = new HashMap<String, FluentWebElement>();
		try {
			FluentWebDriver fluent = new FluentWebDriver(driver);
			FluentWebElements userInputs = fluent.inputs();
			for (FluentWebElement userIn : userInputs) {
				if (userIn.isDisplayed().value() && userIn.isEnabled().value()) {
					String id = userIn.getAttribute("id").toString();
					String name = userIn.getAttribute("name").toString();
					String type = userIn.getAttribute("type").toString();
					if (name != null && !name.isEmpty()) {
						if (type.equals("text"))
							usrItems.put(name, userIn);
						if (type.equals("checkbox"))
							usrItems.put("CheckBox-" + name, userIn);
						if (type.equals("password"))
							usrItems.put(name, userIn);
						if (name.equals("UserChoice")) {
							log.info("Found A Submit : "
									+ userIn.getAttribute("value").toString()
									+ " And its Name : "
									+ userIn.getAttribute("name").toString());
							usrItems.put(userIn.getAttribute("value")
									.toString(), userIn);
						}

					}
					if (id != null && !id.isEmpty()) {
						if (type.equals("text"))
							usrItems.put(id, userIn);
						if (type.equals("checkbox"))
							usrItems.put("CheckBox-" + id, userIn);
						if (type.equals("password"))
							usrItems.put(name, userIn);
					}
				}
			}
			FluentWebElement form = fluent.form(By.name("UserForm"));

			FluentWebElements userIns = form.inputs();
			log.info("" + userIns.size());
			for (FluentWebElement userIn : userIns) {
				String type = userIn.getAttribute("type").toString();
				if (userIn.getAttribute("value").toString()
						.equals("Save Changes")) {
					log.info("Type : " + type);
					if (!type.equalsIgnoreCase("hidden")) {
						log.info("Found A Submit : "
								+ userIn.getAttribute("value").toString()
								+ " And its Name : "
								+ userIn.getAttribute("name").toString());
						usrItems.put(userIn.getAttribute("value").toString(),
								userIn);
					}
				}
			}
			FluentWebElements userSelects = fluent.selects();
			for (FluentWebElement usrSelect : userSelects) {
				if (usrSelect.isDisplayed().value()
						&& usrSelect.isEnabled().value()) {
					String id = usrSelect.getAttribute("id").toString();
					String name = usrSelect.getAttribute("name").toString();
					if (name != null && !name.isEmpty()) {
						usrItems.put("Select-" + name, usrSelect);

					}
					if (id != null && !id.isEmpty()) {
						usrItems.put("Select-" + id, usrSelect);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usrItems;
	}

	public boolean FlowTest(final SuiteConfig data,
			final Map<String, String> inputData, WebDriver webDriver) {
		boolean state = false;
		log.info("******************************************************* START ***********************************************************");
		log.info("\n");

		WebDriver driver = registerAndLogin(data, false, null, webDriver);
		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
			log.info("winHandle: " + winHandle);
		}
		try {
			FluentWebDriver fluent = new FluentWebDriver(driver);
			Thread.sleep(wait);
			driver.switchTo().frame("Table");
			Thread.sleep(wait);
			if (Boolean.valueOf(inputData.get("addWebClient"))) {
				FluentWebElement webClient = fluent.link(By
						.xpath("contains(.,'Web Client')"));
				if (webClient.isDisplayed().value()
						&& webClient.isEnabled().value()) {
					webClient.click();
					Thread.sleep(wait);
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
						log.info("winHandle: " + winHandle);
					}
					List<CustomElements> allElements = getAllElementsInPage(
							fluent, Boolean.valueOf(inputData.get("isAdmin")));
					System.out.println(allElements.size());
					for (CustomElements custElements : allElements) {
						if (custElements.getBase().endsWith("General")) {
							if (custElements.getField().equals("ClientCode")) {
								custElements.getElement().clearField();
								custElements.getElement().sendKeys(
										inputData.get("ClientCode"));
							}
						}
						if (custElements.getBase().endsWith("General")) {
							if (custElements.getField().equals("ClientName")) {
								custElements.getElement().clearField();
								custElements.getElement().sendKeys(
										inputData.get("ClientName"));
							}
						}
					}

					FluentWebElements inputs = fluent.form().inputs();
					for (FluentWebElement input : inputs) {
						if (input.isDisplayed().value()
								&& input.isEnabled().value()) {
							String butn = input.getAttribute("value")
									.toString();
							if (butn.equals("Create Client")) {
								input.click();
								break;
							}
						}
					}
					Thread.sleep(wait);
					Robot robot1 = new Robot();
					robot1.keyPress(KeyEvent.VK_ALT);
					robot1.keyPress(KeyEvent.VK_TAB);
					Thread.sleep(wait);
					robot1.keyRelease(KeyEvent.VK_TAB);
					robot1.keyRelease(KeyEvent.VK_ALT);
					Thread.sleep(wait);

					robot1.keyPress(KeyEvent.VK_F5);
					Thread.sleep(wait);
					robot1.keyRelease(KeyEvent.VK_F5);
					Thread.sleep(wait);

					driver.switchTo().frame("Table");
					Thread.sleep(wait);
				}
			}
			Thread.sleep(wait);
			FluentWebElement client = fluent.link(By.xpath("contains(.,'"
					+ inputData.get("ClientName") + "')"));
			if (client.isDisplayed().value() && client.isEnabled().value()) {
				client.click();
				Thread.sleep(wait);
			}

			driver.switchTo().frame("Table");
			Thread.sleep(wait);
			if (Boolean.valueOf(inputData.get("addBranch"))) {
				FluentWebElement webClient = fluent.link(By
						.xpath("contains(.,'Add New Group')"));
				if (webClient.isDisplayed().value()
						&& webClient.isEnabled().value()) {
					webClient.click();
					Thread.sleep(wait);
					for (String winHandle : driver.getWindowHandles()) {
						driver.switchTo().window(winHandle);
						log.info("winHandle: " + winHandle);
					}
					List<CustomElements> allElements = getAllElementsInPage(
							fluent, Boolean.valueOf(inputData.get("isAdmin")));
					System.out.println(allElements.size());
					for (CustomElements custElements : allElements) {
						if (custElements.getBase().endsWith("Group Info")) {
							if (custElements.getField().equals("GroupCode")) {
								custElements.getElement().clearField();
								custElements.getElement().sendKeys(
										inputData.get("GroupCode"));
							}
						}
						if (custElements.getBase().endsWith("Group Info")) {
							if (custElements.getField().equals("GroupName")) {
								custElements.getElement().clearField();
								custElements.getElement().sendKeys(
										inputData.get("GroupName"));
							}
						}
						if (custElements.getBase().endsWith("Group Info")) {
							if (custElements.getField().equals("WeeklyHQ")) {
								custElements.getElement().clearField();
								custElements.getElement().sendKeys(
										inputData.get("WeeklyHQ"));
							}
						}
					}

					FluentWebElements inputs = fluent.form().inputs();
					for (FluentWebElement input : inputs) {
						if (input.isDisplayed().value()
								&& input.isEnabled().value()) {
							String butn = input.getAttribute("value")
									.toString();
							if (butn.equals("Create Group")) {
								input.click();
								break;
							}
						}
					}
					Thread.sleep(wait);
					Robot robot2 = new Robot();
					robot2.keyPress(KeyEvent.VK_ALT);
					robot2.keyPress(KeyEvent.VK_TAB);
					Thread.sleep(wait);
					robot2.keyRelease(KeyEvent.VK_TAB);
					robot2.keyRelease(KeyEvent.VK_ALT);
					Thread.sleep(wait);

					robot2.keyPress(KeyEvent.VK_F5);
					Thread.sleep(wait);
					robot2.keyRelease(KeyEvent.VK_F5);
					Thread.sleep(wait);
				}
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				driver.switchTo().frame("Table");
			}
			Thread.sleep(wait);
			FluentWebElement group = fluent.link(By.xpath("contains(.,'"
					+ inputData.get("GroupName") + "')"));
			if (group.isDisplayed().value() && group.isEnabled().value()) {
				group.click();
				Thread.sleep(wait);
			}
			if (inputData.containsKey("menuFunction")) {
				FluentSelect sel = (FluentSelect) getTMCMenu(driver).get(
						inputData.get("menuFunction"));
				sel.selectByVisibleText(inputData.get("menuAction"));
				Thread.sleep(wait);
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				fluent.input(By.id("btnDecline")).click();
				Thread.sleep(wait);
				for (String winHandle : driver.getWindowHandles()) {
					driver.switchTo().window(winHandle);
					log.info("winHandle: " + winHandle);
				}
				Thread.sleep(wait);

				if (Boolean.valueOf(inputData.get("addRole"))) {
					Map<String, FluentWebElement> adminFunctions = getAdminFunctions(driver);
					adminFunctions.get("Roles").click();
					Thread.sleep(wait);
					FluentWebElements inputs = fluent.div(By.id("tabs-1"))
							.form().inputs();
					for (FluentWebElement input : inputs) {
						if (input.isDisplayed().value()
								&& input.isEnabled().value()) {
							String newRole = input.getAttribute("value")
									.toString();
							if (newRole.equalsIgnoreCase("New Role")) {
								input.click();
								break;
							}
						}
					}
					Thread.sleep(wait);
					Map<String, FluentWebElement> allRoleAccess = getTMCRole(driver);
					for (Entry<String, FluentWebElement> roleEntry : allRoleAccess
							.entrySet()) {
						if (roleEntry.getKey().startsWith("1-")
								|| roleEntry.getKey().startsWith("3-")) {
							if (!roleEntry.getKey().contains("TimeImporter"))
								roleEntry.getValue().click();
						}
						if (roleEntry.getKey().equals("RoleAccessLevel"))
							((FluentSelect) roleEntry.getValue())
									.selectByVisibleText("1 (highest)");
						if (roleEntry.getKey().equals("ClientAdm_Users"))
							((FluentSelect) roleEntry.getValue())
									.selectByVisibleText("Yes");
						if (roleEntry.getKey().equals("ClientAdm_AccessGroups"))
							((FluentSelect) roleEntry.getValue())
									.selectByVisibleText("Yes");
						if (roleEntry.getKey().equals("OrderEntryAccess"))
							((FluentSelect) roleEntry.getValue())
									.selectByVisibleText("Super User");
						if (roleEntry.getKey().equals("RoleName"))
							roleEntry.getValue().sendKeys(
									inputData.get("roleName"));
					}
					Thread.sleep(wait);
					allRoleAccess.get("Save Role Info").click();
					Thread.sleep(wait);
				}
				if (Boolean.valueOf(inputData.get("addUser"))) {
					Map<String, FluentWebElement> adminFunctions = getAdminFunctions(driver);
					adminFunctions.get("User Setup").click();
					Thread.sleep(wait);
					FluentWebElements inputs = fluent.inputs();
					for (FluentWebElement input : inputs) {
						if (input.isDisplayed().value()
								&& input.isEnabled().value()) {
							String newRole = input.getAttribute("value")
									.toString();
							if (newRole.equalsIgnoreCase("Add New User")) {
								input.click();
								break;
							}
						}
					}
					Thread.sleep(wait);
					Map<String, FluentWebElement> userInputs = getTMCUserInputs(driver);
					log.info("" + userInputs);
					for (Entry<String, FluentWebElement> usrIn : userInputs
							.entrySet()) {
						if (usrIn.getKey().startsWith("Select-")) {
							if (!usrIn.getKey().equals("Select-client")) {
								FluentSelect select = ((FluentSelect) usrIn
										.getValue());
								if (usrIn.getKey().equals("Select-RoleId")) {
									select.selectByVisibleText(inputData
											.get("roleName"));
								} else {
									List<WebElement> options = select
											.getOptions();
									for (WebElement option : options) {
										if (option.getText().equals("Yes")) {
											select.selectByVisibleText("Yes");
										} else if (option.getText().contains(
												"Yes - Edit")) {
											select.selectByVisibleText(option
													.getText());
										} else if (option.getText().equals(
												"Admin")) {
											select.selectByVisibleText("Admin");
										}
									}
								}
							}
						} else if (usrIn.getKey().startsWith("CheckBox-")) {
							if (!usrIn.getKey().equals("CheckBox-SendWelcome"))
								usrIn.getValue().click();
						} else {
							if (inputData.containsKey(usrIn.getKey())) {
								usrIn.getValue().clearField();
								usrIn.getValue().sendKeys(
										inputData.get(usrIn.getKey()));
							}
						}
					}
					Thread.sleep(wait);
					if (userInputs.containsKey("Save Changes"))
						userInputs.get("Save Changes").click();
					else if (userInputs.containsKey("Add User"))
						userInputs.get("Add User").click();
					Thread.sleep(wait);
				}
			}
		} catch (Exception e) {
			driver.quit();
			e.printStackTrace();
			log.error("TimeEntry Error Due To : "
					+ e.getCause().getLocalizedMessage());
		}
		log.info("******************************************************* END ***********************************************************");
		return state;
	}

	@Override
	public void tests() {
		String content = new String();
		String groupTests = "TMC_Tests";
		try {
			for (SuiteConfig data : suiteConfig) {
				boolean status = false;
				if (data.getGroup().equals(groupTests)) {
					if (data.getStatus().equals("No")) {
						log.info(data.getScript() + " Execution on Bowser - "
								+ data.getBrowser()
								+ " is configured not to run and Skipped ....!");
					} else {
						Map<String, String> inputValues = parse(data, "input");
						long startTime = System.currentTimeMillis();
						if (data.getProject().equals("FLOW")) {
							if (Boolean.valueOf(inputValues.get("CBRU"))) {
								log.info("Input Values : " + inputValues);
								status = FlowTest(data, inputValues, null);
								if (status) {
									if (Boolean.valueOf(inputValues
											.get("addEmployee"))) {
										Map<String, Object> state = AddEmployee(
												data, inputValues);
										status = (Boolean) state.get("status");
									}
								}
							}
							data.setStatus(String.valueOf(status));
							long endTime = System.currentTimeMillis();
							String currentTime = new String(""
									+ System.currentTimeMillis());
							content += HtmlReportGenerator.getFormattedHtmlHMY(
									data,
									data.getScript(),
									HtmlReportGenerator.getTime(endTime
											- startTime), inputValues,
									currentTime.trim());
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("Error Due to : " + e.getMessage());
			e.printStackTrace();

		} finally {
			HtmlReportGenerator.setHtmlContent(content);
		}
	}
}