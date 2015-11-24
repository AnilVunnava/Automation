package com.peoplenet.qa.selenium.tests.hmy;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.seleniumhq.selenium.fluent.FluentWebDriver;
import org.seleniumhq.selenium.fluent.FluentWebElement;
import org.seleniumhq.selenium.fluent.FluentWebElements;
import org.testng.Assert;

import com.peoplenet.qa.selenium.base.tests.AbstractSmokeTests;
import com.peoplenet.qa.selenium.data.AccessConfig;
import com.peoplenet.qa.selenium.data.EmployeeData;
import com.peoplenet.qa.selenium.data.ExcelUtils;
import com.peoplenet.qa.selenium.data.SuiteConfig;
import com.peoplenet.qa.selenium.db.config.DatabaseFactory;
import com.peoplenet.qa.selenium.factory.PropertiesLoader;
import com.peoplenet.qa.selenium.reports.HtmlReportGenerator;

/**
 * @author Anil.Vunnava
 *
 *         HMY_TimeEntryTests [Angular JS Version]
 */
public class HMY_TimeEntryTests extends AbstractSmokeTests {
	private static final Logger log = Logger
			.getLogger("HMY_AJ_TimeEntry_Tests");
	private static final long wait = Long.valueOf(PropertiesLoader
			.getProperty("wait"));

	public WebDriver employeeLogin(SuiteConfig suiteConfig,
			Map<String, String> inputValues, WebDriver webDriver) {
		WebDriver driver = null;
		try {
			AccessConfig data = null;
			for (AccessConfig accessConf : accessConfig) {
				if (suiteConfig.getScript().equals(accessConf.getScript())) {
					data = accessConf;
				} else if (accessConf.getScript().equals(
						"HMY_" + ExcelUtils.sheet)) {
					data = accessConf;
				}
			}
			if (data != null) {
				if (ExcelUtils.sheet.equals("Data")) {
					if (webDriver == null)
						driver = getDriver(suiteConfig.getUniqueScript());
					else
						driver = webDriver;
					driver.manage().window().maximize();
					driver.get(data.getURL());
					FluentWebDriver fluent = new FluentWebDriver(driver);
					Thread.sleep(wait);
					FluentWebElement form = fluent.div(By.id("bodycontent"))
							.div(By.className("container")).div().form();
					form.input().sendKeys(inputValues.get("recordId"));
					Thread.sleep(wait);
					takeScreenShots("employeeLogin", suiteConfig.getGroup(),
							driver);
					form.button().submit();

					Thread.sleep(wait);
					inputValues.put("EmpName", inputValues.get("firstName")
							+ " " + inputValues.get("lastName"));
				} else {
					driver = registerAndLogin(suiteConfig, false, inputValues,
							null);
				}
			}
			return driver;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(suiteConfig.getScript() + " Error Due to : "
					+ e.getMessage());
			return driver;
		}

	}

	public static Map<String, FluentWebElement> getHeaderLinks(WebDriver driver) {
		Map<String, FluentWebElement> headers = new HashMap<String, FluentWebElement>();
		FluentWebDriver fluent = new FluentWebDriver(driver);
		FluentWebElement headerBar = fluent.div(By.id("mainheader"));
		if (headerBar.isEnabled().value() && headerBar.isDisplayed().value()) {
			FluentWebElement headerLinks = headerBar.div(By
					.id("navigationmobile"));
			if (headerLinks.isEnabled().value()
					&& headerLinks.isDisplayed().value()) {
				FluentWebElements headerNavs = headerLinks.lis();
				for (FluentWebElement headerNav : headerNavs) {
					if (headerNav.isEnabled().value()
							&& headerNav.isDisplayed().value()) {

						FluentWebElements links = headerNav.links();
						for (FluentWebElement link : links) {
							String linkText = link.getText().toString();
							if (!linkText.isEmpty() && linkText != null) {
								headers.put(linkText, link);
							}
						}
					}
				}
			}
		}
		return headers;
	}

	private String getTimeString(String InOrOut, double index, String phrase) {
		String resp = new String();
		double val = 0;
		if (phrase.equals("work"))
			val = Integer.valueOf(InOrOut) + (index * 2);
		if (phrase.equals("break"))
			val = Integer.valueOf(InOrOut) + (index * 0.25);
		resp = val + "";
		return resp.trim();
	}

	private String getPayPeriod(String payPeriod) {
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

	public void acceptTermsAndConditions(SuiteConfig data,
			Map<String, String> inputValues, WebDriver driver)
			throws InterruptedException {
		driver = registerAndLogin(data, false, inputValues, driver);
		Thread.sleep(2000);
		try {
			FluentWebDriver fluent = new FluentWebDriver(driver);
			Thread.sleep(wait);
			if (driver.getTitle().equals("SelectClient")) {
				FluentWebElements inputClients = fluent.divs(By
						.className("links_container"));
				log.info("Clients : " + inputClients.getText().toString());
				for (FluentWebElement divs : inputClients) {
					FluentWebElement client = divs.input();
					if (client.getAttribute("value").toString()
							.equalsIgnoreCase(inputValues.get("client"))) {
						client.click();
						break;
					}
				}

				Thread.sleep(wait);
			}
			Thread.sleep(wait);
			FluentWebElement dashboardMenu = fluent.div(By.id("dashboardmenu"));
			FluentWebElement tblTimesummary = dashboardMenu.div(By
					.id("tblTimesummary"));
			FluentWebElements timesheettitle = tblTimesummary.divs(By
					.id("timesheettitle"));

			if (timesheettitle.getText().toString().contains("Months")) {
				log.info("TimeSheet Title : "
						+ timesheettitle.getText().toString());
				Map<String, FluentWebElement> entryLinks = new HashMap<String, FluentWebElement>();
				for (FluentWebElement months : timesheettitle) {
					for (FluentWebElement timeLinks : months.links()) {
						entryLinks.put(timeLinks.getText().toString(),
								timeLinks);
					}
				}
				log.info("All Link Elements in TimeEntry Title : " + entryLinks);
				// entryLinks.get("Months").click();
				Thread.sleep(1500);
			}
			FluentWebElements weekTimesheet = tblTimesummary.div(
					By.id("dashboardleft")).divs(
					By.className("timesheetdatarow ng-scope"));
			for (FluentWebElement time : weekTimesheet) {
				String status = time.div(By.className("statusdashboard"))
						.getText().toString();
				log.info("Status :: " + status);
				String currentMonth = new SimpleDateFormat("MMM")
						.format(Calendar.getInstance().getTime());
				String actualMonth = time.link().getText().toString();
				if (status.equals("No Time Entered")) {
					if (actualMonth.startsWith(currentMonth)) {
						log.info("Can not Submit Monthly Time for the Current Month :: "
								+ actualMonth);
					} else {
						time.link().click();
						Thread.sleep(1500);
						FluentWebElements tcModal = fluent.divs(By
								.className("modal-content"));
						if (tcModal.size() > 0) {
							for (FluentWebElement modal : tcModal) {
								if (modal.isDisplayed().value()
										&& modal.isEnabled().value()) {
									FluentWebElements tAndC = modal.divs(By
											.id("div"));
									takeScreenShots("/" + data.getScript()
											+ "/TimeEntry/screenshot",
											data.getGroup(), driver);
									for (FluentWebElement termsAndC : tAndC) {
										if (termsAndC.isDisplayed().value()
												&& termsAndC.isEnabled()
														.value()) {
											for (int i = 0; i <= 10; i++)
												termsAndC
														.sendKeys(Keys.PAGE_DOWN);
											takeScreenShots(
													"/"
															+ data.getScript()
															+ "/TimeEntry/screenshot",
													data.getGroup(), driver);
											FluentWebElement acceptTerms = fluent
													.button(By
															.xpath("contains(.,'Accept')"));
											if (acceptTerms.isEnabled().value()
													&& acceptTerms
															.isDisplayed()
															.value())
												acceptTerms.click();
											Thread.sleep(1500);
										}
									}
								}
							}
						}
						break;
					}
				}
			}
			driver.quit();
		} catch (Exception e) {
			driver.quit();
			e.printStackTrace();
		}
	}

	/**
	 * @param data
	 * @param inputValues
	 * @return
	 * 
	 *         HMY_TimeEntryTests [Angular JS Version]
	 */
	public boolean TimeEntryTest(final SuiteConfig data,
			final Map<String, String> inputValues, WebDriver webDriver) {
		boolean state = false;
		boolean isMobile = Boolean.valueOf(PropertiesLoader
				.getProperty("mobileView"));
		Map<String, String> map = new HashMap<String, String>();
		Map<String, String> breaksMap = new HashMap<String, String>();
		if (inputValues.get("empType").equals("InOut")
				|| inputValues.get("empType").equals("UDF")) {
			map.put("in0", inputValues.get("in"));
			map.put("out0", inputValues.get("out"));
			map.put("in1", inputValues.get("out"));

			for (int i = 1; i <= Integer.valueOf(inputValues.get("workCount")); i++) {
				if (i != 1)
					map.put("in" + i,
							getTimeString(inputValues.get("out"), i - 1, "work"));
				map.put("out" + i,
						getTimeString(inputValues.get("out"), i, "work"));
			}
			if (inputValues.get("empType").equals("UDF")
					&& inputValues.containsKey("checkEntryRounding")) {
				for (int i = 1; i <= (Integer.valueOf(inputValues
						.get("breaksCount")) + 1); i++) {
					breaksMap
							.put("in" + i,
									getTimeString(inputValues.get("in"), i + 1,
											"break"));
					breaksMap.put(
							"out" + i,
							getTimeString(inputValues.get("in"), (i + 0.15),
									"break"));
				}
			} else {
				if (inputValues.get("empType").equals("UDF")) {
					for (int i = 1; i <= (Integer.valueOf(inputValues
							.get("breaksCount")) + 1); i++) {
						breaksMap.put(
								"in" + i,
								getTimeString(inputValues.get("in"), i + 1,
										"break"));
						breaksMap
								.put("out" + i,
										getTimeString(inputValues.get("in"), i,
												"break"));
					}
				} else {
					for (int i = 1; i <= (Integer.valueOf(inputValues
							.get("breaksCount")) + 1); i++) {
						breaksMap.put(
								"in" + i,
								getTimeString(inputValues.get("in"), i + 1,
										"break"));
						breaksMap.put(
								"out" + i,
								getTimeString(inputValues.get("in"),
										(i + 0.15), "break"));
					}
				}
			}
		}
		log.info("******************************************************* START ***********************************************************");
		log.info("RecordId = " + inputValues.get("recordId"));
		log.info("\n");

		WebDriver driver = employeeLogin(data, inputValues, webDriver);
		boolean isMonthly = false;

		try {
			FluentWebDriver fluent = new FluentWebDriver(driver);
			Thread.sleep(wait);
			if (driver.getTitle().equals("SelectClient")) {
				FluentWebElements inputClients = fluent.divs(By
						.className("links_container"));
				log.info("Clients : " + inputClients.getText().toString());
				for (FluentWebElement divs : inputClients) {
					FluentWebElement client = divs.input();
					if (client.getAttribute("value").toString()
							.equalsIgnoreCase(inputValues.get("client"))) {
						client.click();
						break;
					}
				}

				Thread.sleep(wait);
			}
			Thread.sleep(wait);
			inputValues.put("EmpName", fluent.div(By.className("customername"))
					.getText().toString());
			FluentWebElement dashboardMenu = fluent.div(By.id("dashboardmenu"));
			FluentWebElement tblTimesummary = dashboardMenu.div(By
					.id("tblTimesummary"));
			FluentWebElements timesheettitle = tblTimesummary.divs(By
					.id("timesheettitle"));

			if (timesheettitle.getText().toString().contains("Months")) {
				log.info("TimeSheet Title : "
						+ timesheettitle.getText().toString());
				isMonthly = true;
				Map<String, FluentWebElement> entryLinks = new HashMap<String, FluentWebElement>();
				for (FluentWebElement months : timesheettitle) {
					for (FluentWebElement timeLinks : months.links()) {
						entryLinks.put(timeLinks.getText().toString(),
								timeLinks);
					}
				}
				log.info("All Link Elements in TimeEntry Title : " + entryLinks);
				// entryLinks.get("Months").click();
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				Thread.sleep(1500);
			}
			FluentWebElements weekTimesheet = fluent
					.div(By.id("dashboardleft")).divs(
							By.className("timesheetdatarow ng-scope"));
			for (FluentWebElement time : weekTimesheet) {
				String status = time.div(By.className("statusdashboard"))
						.getText().toString();
				log.info("Status :: " + status);
				Thread.sleep(1500);
				String currentMonth = new SimpleDateFormat("MMM")
						.format(Calendar.getInstance().getTime());
				String actualMonth = time.link().getText().toString();
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				if (status.equals("No Time Entered")) {
					if (actualMonth.startsWith(currentMonth)) {
						log.info("Can not Submit Monthly Time for the Current Month :: "
								+ actualMonth);
					} else {
						FluentWebElement weekEnding = time.link();
						log.info("PayPeriod : "
								+ weekEnding.getText().toString());
						inputValues.put("timePeriod", weekEnding.getText()
								.toString().trim());
						weekEnding.click();
						Thread.sleep(wait);
						FluentWebElements tcModal = fluent.divs(By
								.className("modal-content"));
						if (tcModal.size() > 0) {
							for (FluentWebElement tcView : tcModal) {
								if (tcView.isDisplayed().value()
										&& tcView.isEnabled().value()) {
									fluent.button(
											By.xpath("contains(.,'Decline')"))
											.click();
									Thread.sleep(1500);
									fluent.button(
											By.xpath("contains(.,'Close')"))
											.click();
									Thread.sleep(1500);
									Thread tAndC = new Thread(
											"TermsAndConditions") {
										public void run() {
											try {
												acceptTermsAndConditions(data,
														inputValues,
														getDriver("T&C"));
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										}
									};
									tAndC.run();
									Thread.sleep(wait);
									time.link().click();
								}
							}
						}
						Thread.sleep(wait);
						// FluentWebElement inStatus = fluent.div(
						// By.className("projectstatus")).div(
						// By.className("status"));
						// log.info("Inner status : "
						// + inStatus.getText().toString());
						Map<String, FluentWebElement> baseButtons = new HashMap<String, FluentWebElement>();
						FluentWebElements buttons = fluent.buttons();
						for (FluentWebElement button : buttons) {
							if (button.isDisplayed().value()
									&& button.isEnabled().value()) {
								log.info("Button Text : "
										+ button.getText().toString());
								baseButtons.put(button.getText().toString(),
										button);
							}
						}
						FluentWebElements divs = fluent.divs(By
								.className("btn"));
						for (FluentWebElement div : divs) {
							if (div.isDisplayed().value()
									&& div.isEnabled().value()) {
								log.info("Button Text : "
										+ div.getText().toString());
								baseButtons.put(div.getText().toString(), div);
							}
						}

						if (inputValues.containsKey("addAssignments")) {
							if (Boolean.valueOf(inputValues
									.get("addAssignments"))) {
								FluentWebElement assignmentInput = fluent
										.input(By.id("selectedAssignment"));
								if (assignmentInput.isDisplayed().value()
										&& assignmentInput.isEnabled().value()) {
									log.info("Adding Assignment : "
											+ inputValues.get("assigName"));
									assignmentInput.sendKeys(inputValues
											.get("assigName"));
									Thread.sleep(1500);
									takeScreenShots("/" + data.getScript()
											+ "/TimeEntry/screenshot",
											data.getGroup(), driver);
									baseButtons.get("Add").click();
								}
								Thread.sleep(wait);
								takeScreenShots("/" + data.getScript()
										+ "/TimeEntry/screenshot",
										data.getGroup(), driver);
							}
						}

						// All Global Variables
						FluentWebElements days = null;
						int startTime = 1;
						FluentWebElements divs1 = null;

						if (isMonthly) {
							FluentWebElement siteHeader = fluent.div(By
									.className("dayentrybackgrounddefault"));
							String totalHrs = siteHeader
									.div(By.className("totalhours")).getText()
									.toString();
							log.info("Time Total : " + totalHrs);
							log.info("Inner Status : "
									+ siteHeader.div(By.className("status"))
											.getText().toString());
							takeScreenShots("/" + data.getScript()
									+ "/TimeEntry/screenshot", data.getGroup(),
									driver);
							if (status.contains(siteHeader
									.div(By.className("status")).getText()
									.toString())) {
								Thread.sleep(wait);
								FluentWebElements weekdaysHeader = fluent
										.div(By.id("weekday")).div().divs();
								log.info("Weekdays Header : "
										+ weekdaysHeader.getText().toString());

								FluentWebElements monthlyElements = fluent
										.div(By.id("monthlytimesheet")).div()
										.spans();
								days = monthlyElements;
								divs1 = monthlyElements;
							}
						} else {
							if (isMobile) {
								FluentWebElements dayHeader = fluent
										.divs(By.className("Weeklytimesheetdayslayout"));
								log.info("Day Header Mobile : "
										+ dayHeader.getText().toString());
								if (dayHeader.getText().toString()
										.startsWith("Sun")) {
									startTime = 0;
								}
								days = dayHeader;
							} else {
								FluentWebElement dayHeader = fluent
										.div(By.className("col-sm-4 weekentrytableheader hidden-xs paddingnone text-center weekdays"));
								log.info("Day Header :: "
										+ dayHeader.getText().toString());
								days = dayHeader
										.divs(By.className("pull-left paddingnone text-center col-seven"));
								if (dayHeader.getText().toString()
										.startsWith("Sun")) {
									startTime = 0;
								}
							}
							divs1 = fluent.divs(By
									.className("Weeklytimesheetdayslayout"));
						}

						int endTime = days.size() - 1;

						if (inputValues.get("function").equals(
								"Copy to Next Day")) {
							endTime = days.size() - 2;
						}
						takeScreenShots("/" + data.getScript()
								+ "/TimeEntry/screenshot", data.getGroup(),
								driver);
						boolean copyToTomorrow = false;

						for (FluentWebElement entry : divs1) {
							if ((startTime > 0) && (startTime < endTime)) {
								FluentWebElements dayDivs = null;
								if (isMonthly)
									dayDivs = entry
											.divs(By.className("Monthlytimesheetdayslayout"));
								else
									dayDivs = entry.divs(By.id("txtmon"));
								if (dayDivs.size() > 0) {
									FluentWebElement dayDiv = null;
									if (isMobile) {
										for (FluentWebElement div : dayDivs) {
											if (div.isEnabled().value()
													&& div.isDisplayed()
															.value())
												dayDiv = div;
										}
									} else {
										for (FluentWebElement visibleSpan : dayDivs) {
											if (visibleSpan.isEnabled().value()
													&& visibleSpan
															.isDisplayed()
															.value())
												dayDiv = visibleSpan;
										}
									}

									String actualDayHrs = dayDiv.getText()
											.toString();
									log.info("Is Day Time Visible as '0.00' : "
											+ ("0.00"
													.equalsIgnoreCase(actualDayHrs)));
									if (actualDayHrs.equalsIgnoreCase("0.00")) {
										dayDiv.click();
										Thread.sleep(wait);
										takeScreenShots("/" + data.getScript()
												+ "/TimeEntry/screenshot",
												data.getGroup(), driver);
										FluentWebElements modals = fluent
												.divs(By.className("modal-content"));
										Map<String, FluentWebElement> modelButtons = new HashMap<String, FluentWebElement>();
										for (FluentWebElement modal : modals) {
											if (modal.isDisplayed().value()
													&& modal.isEnabled()
															.value()) {
												String header = modal.getText()
														.toString();
												log.info("======================== Modal Opened For : "
														+ modal.getText()
																.toString()
														+ "==========================");
												if (header
														.contains("You have no active assignments for this period.")) {

												} else {
													if (isMobile) {
														FluentWebElement footer = fluent
																.div(By.id("assignmentfooter"));
														modelButtons
																.put("Ok",
																		footer.div(By
																				.className("ok")));
														FluentWebElements btns = modal
																.divs(By.className("footercontent"));
														for (FluentWebElement button : btns) {
															if (button
																	.isEnabled()
																	.value()
																	&& button
																			.isDisplayed()
																			.value()) {
																String day = button
																		.span(By.className("copythrough"))
																		.getText()
																		.toString();
																if (day.contains("Friday"))
																	modelButtons
																			.put("Copy through Friday",
																					button.span(By
																							.className("paddingnone")));
																else
																	modelButtons
																			.put("Copy to Next Day",
																					button.span(By
																							.className("paddingnone")));
															}
														}
													} else {
														FluentWebElements btns = modal
																.divs(By.className("btn"));

														// Divs of type button
														// in
														// Model
														// Popup
														for (FluentWebElement btn : btns) {
															if (btn.isDisplayed()
																	.value()
																	&& btn.isEnabled()
																			.value()) {
																log.info("Button Text : "
																		+ btn.getText()
																				.toString());
																modelButtons
																		.put(btn.getText()
																				.toString(),
																				btn);
															}
														}

														// Buttons in Model
														// Popup
														FluentWebElements buttons1 = modal
																.buttons();
														for (FluentWebElement button1 : buttons1) {
															if (button1
																	.isDisplayed()
																	.value()
																	&& button1
																			.isEnabled()
																			.value()) {
																log.info("Button Text : "
																		+ button1
																				.getText()
																				.toString());
																modelButtons
																		.put(button1
																				.getText()
																				.toString(),
																				button1);
															}
														}
													}
													if (!copyToTomorrow) {
														// Adding break
														int count1 = 1;
														do {
															if (Integer
																	.valueOf(inputValues
																			.get("breaksCount")) > 0) {
																FluentWebElements breaks = modal
																		.divs(By.className("addbreakbutton"));
																for (FluentWebElement breakValue : breaks) {
																	if (breakValue
																			.isDisplayed()
																			.value()
																			&& breakValue
																					.isEnabled()
																					.value()) {
																		log.info("BreakValue Text : "
																				+ breakValue
																						.getText()
																						.toString());
																		breakValue
																				.click();
																		break;
																	}
																}
																count1++;
															}
														} while ((count1 <= Integer
																.valueOf(inputValues
																		.get("breaksCount")))
																&& (Integer
																		.valueOf(inputValues
																				.get("breaksCount")) > 0));

														// Adding Work
														int count2 = 1;
														do {
															if (Integer
																	.valueOf(inputValues
																			.get("workCount")) > 0) {
																FluentWebElements works = modal
																		.divs(By.className("addworkbutton"));
																FluentWebElements addworks = modal
																		.divs(By.className("addwork"));
																for (FluentWebElement work : works) {
																	if (work.isDisplayed()
																			.value()
																			&& work.isEnabled()
																					.value()) {
																		log.info("WorkValue Text : "
																				+ work.getText()
																						.toString());
																		work.click();
																		break;
																	}
																}
																for (FluentWebElement addwork : addworks) {
																	if (addwork
																			.isDisplayed()
																			.value()
																			&& addwork
																					.isEnabled()
																					.value()) {
																		log.info("WorkValue Text : "
																				+ addwork
																						.getText()
																						.toString());
																		addwork.click();
																		break;
																	}
																}
																count2++;
															}
														} while ((count2 <= Integer
																.valueOf(inputValues
																		.get("workCount")))
																&& (Integer
																		.valueOf(inputValues
																				.get("workCount")) > 0));

														int count3 = 1;
														// Adding Project
														do {
															if (Integer
																	.valueOf(inputValues
																			.get("projectCount")) > 0) {
																FluentWebElements projects = modal
																		.divs(By.className("addprojectbutton"));
																for (FluentWebElement project : projects) {
																	if (project
																			.isDisplayed()
																			.value()
																			&& project
																					.isEnabled()
																					.value()) {
																		log.info("ProjectValue Text : "
																				+ project
																						.getText()
																						.toString());
																		project.click();
																	}
																}
																count3++;
															}
														} while ((count3 <= Integer
																.valueOf(inputValues
																		.get("projectCount")))
																&& (Integer
																		.valueOf(inputValues
																				.get("projectCount")) > 0));

														// Sending Work Hours
														// HPD
														FluentWebElements adjustmenthours = fluent
																.divs(By.id("adjustmenthours"));
														int roll = 0;
														for (FluentWebElement adjHrs : adjustmenthours) {
															FluentWebElement workdivs = adjHrs
																	.div(By.className("input-group"));
															FluentWebElement timeEntry = workdivs
																	.input();
															if (roll > 0) {
																if (timeEntry
																		.isDisplayed()
																		.value()
																		&& timeEntry
																				.isEnabled()
																				.value()) {
																	timeEntry
																			.sendKeys(
																					Keys.chord(
																							Keys.CONTROL,
																							"a"),
																					"2");
																}
															} else {
																if (timeEntry
																		.isDisplayed()
																		.value()
																		&& timeEntry
																				.isEnabled()
																				.value()) {
																	timeEntry
																			.sendKeys(
																					Keys.chord(
																							Keys.CONTROL,
																							"a"),
																					inputValues
																							.get("time"));
																}
															}
															roll++;
														}

														// Sending Work Hours
														// In/Out
														FluentWebElements inouts = fluent
																.divs(By.className("col-sm-12 col-xs-12 paddingnone adjustmentbackgrundinout ng-scope"));
														int workRoll = 0;
														for (FluentWebElement inout : inouts) {
															if (inouts.size() > (workRoll + 1)) {
																FluentWebElements workHrs = inout
																		.div(By.className("col-xs-10 col-sm-11 paddingnone"))
																		.divs(By.className("col-xs-12 input-group"));
																roll = 0;
																String in = map
																		.get("in"
																				+ workRoll);
																String out = map
																		.get("out"
																				+ workRoll);

																for (FluentWebElement hrs : workHrs) {
																	FluentWebElement input = hrs
																			.input(By
																					.className("timeentrybox"));
																	input.click();
																	if (roll > 0) {
																		input.sendKeys(out);
																		input.sendKeys(Keys.TAB);

																	} else {
																		input.sendKeys(in);
																		input.sendKeys(Keys.TAB);
																	}
																	roll++;
																}
																workRoll++;
															} else {
																if (!inputValues
																		.containsKey("addAssignments")) {
																	FluentWebElements workHrs = inout
																			.div(By.className("col-xs-10 col-sm-11 paddingnone"))
																			.divs(By.className("col-xs-12 input-group"));
																	roll = 0;
																	String in = map
																			.get("in"
																					+ workRoll);
																	String out = map
																			.get("out"
																					+ workRoll);

																	for (FluentWebElement hrs : workHrs) {
																		FluentWebElement input = hrs
																				.input(By
																						.className("timeentrybox"));
																		input.click();
																		if (roll > 0) {
																			input.sendKeys(out);
																			input.sendKeys(Keys.TAB);

																		} else {
																			input.sendKeys(in);
																			input.sendKeys(Keys.TAB);
																		}
																		roll++;
																	}
																	workRoll++;
																}
															}
														}

														// Adding Break Time
														// Hours
														// and
														// Project Hours

														FluentWebElements breaks = fluent
																.divs(By.className("breakplaceholder"));
														int breakRoll = 1;
														for (FluentWebElement breakTime : breaks) {
															FluentWebElements innerDivs = breakTime
																	.divs(By.className("inoutinputtoppadding"));
															if (breakRoll <= (Integer
																	.valueOf(inputValues
																			.get("breaksCount")) + 1)) {
																if (Integer
																		.valueOf(inputValues
																				.get("breaksCount")) > 0) {
																	String in = breaksMap
																			.get("in"
																					+ breakRoll);
																	String out = breaksMap
																			.get("out"
																					+ breakRoll);
																	for (FluentWebElement div : innerDivs) {
																		div.click();
																		FluentWebElement timeEntry = div
																				.input(By
																						.className("timeentrybox"));
																		if (div.getText()
																				.toString()
																				.contains(
																						"Out")) {

																			timeEntry
																					.sendKeys(out);
																			timeEntry
																					.sendKeys(Keys.TAB);
																		}
																		if (div.getText()
																				.toString()
																				.contains(
																						"In")) {
																			timeEntry
																					.sendKeys(in);
																			timeEntry
																					.sendKeys(Keys.TAB);
																		}
																	}
																}
																breakRoll++;
															}
														}

														if (Integer
																.valueOf(inputValues
																		.get("projectCount")) > 0) {
															if (Boolean
																	.valueOf(inputValues
																			.get("addProject"))) {
																fluent.span(
																		By.className("selectArrow"))
																		.click();
																Thread.sleep(1500);
																FluentWebElements projects = fluent
																		.div(By.className("AddField form-inline"))
																		.inputs();
																for (FluentWebElement project : projects) {
																	if (project
																			.isDisplayed()
																			.value()
																			&& project
																					.isEnabled()
																					.value())
																		project.sendKeys("Test-Project");
																}
																Thread.sleep(1500);
																FluentWebElements addProjs = fluent
																		.div(By.className("AddField form-inline"))
																		.buttons(
																				By.className("addfield"));
																for (FluentWebElement addProj : addProjs) {
																	if (addProj
																			.isDisplayed()
																			.value()
																			&& addProj
																					.isEnabled()
																					.value())
																		addProj.click();
																}
																Thread.sleep(1500);

																FluentWebElements projs = fluent
																		.spans(By
																				.className("selectOption"));
																for (FluentWebElement proj : projs) {
																	if (proj.isDisplayed()
																			.value()
																			&& proj.isEnabled()
																					.value()) {
																		FluentWebElement finalProj = proj
																				.span();
																		if (finalProj
																				.getText()
																				.equals("Test-Project")) {
																			finalProj
																					.click();
																		}
																	}
																}
															}
															for (FluentWebElement breakTime : breaks) {
																FluentWebElements innerDivs = breakTime
																		.divs(By.className("inoutinputtoppadding"));
																for (FluentWebElement div : innerDivs) {
																	FluentWebElements inoutGroups = div
																			.divs(By.className("input-group"));

																	for (FluentWebElement inout : inoutGroups) {
																		if (inout
																				.input()
																				.getAttribute(
																						"class")
																				.toString()
																				.contains(
																						"timeentrybox")) {
																			FluentWebElement timeEntry = inout
																					.input(By
																							.className("timeentrybox"));
																			if (div.getText()
																					.toString()
																					.contains(
																							"Hours")) {
																				timeEntry
																						.sendKeys(
																								Keys.chord(
																										Keys.CONTROL,
																										"a"),
																								inputValues
																										.get("projectHours"));
																				timeEntry
																						.sendKeys(Keys.TAB);
																			}
																		}
																	}
																}
															}
														}
														if (inputValues
																.containsKey("addAssignments")) {
															FluentWebElement addUdf = fluent
																	.div(By.name("addudf"));
															if (addUdf
																	.isDisplayed()
																	.value()
																	&& addUdf
																			.isEnabled()
																			.value()) {
																FluentWebElement sales = addUdf
																		.input(By
																				.name("udfcontrol_0_974_0"));
																if (sales
																		.isDisplayed()
																		.value()
																		&& sales.isEnabled()
																				.value()) {
																	sales.clearField();
																	sales.sendKeys(inputValues
																			.get("udfValue"));
																}

															}
														}
													}

													takeScreenShots(
															"/"
																	+ data.getScript()
																	+ "/TimeEntry/screenshot",
															data.getGroup(),
															driver);

													if (inputValues
															.get("function")
															.equals("Copy through Friday")) {
														modelButtons
																.get("Copy through Friday")
																.click();
														break;
													}
													if (inputValues.get(
															"function").equals(
															"Ok")) {
														modelButtons.get("Ok")
																.click();
													}
													if (inputValues.get(
															"function").equals(
															"Copy to Next Day")) {
														modelButtons
																.get("Copy to Next Day")
																.click();
														copyToTomorrow = true;
													}

													takeScreenShots(
															"/"
																	+ data.getScript()
																	+ "/TimeEntry/screenshot",
															data.getGroup(),
															driver);
													// deleteadjustment
												}
												log.info("\n ==================================================");
											}
										}
										if (inputValues.get("function").equals(
												"Copy through Friday")) {
											break;
										}
									} else {
										if (inputValues
												.containsKey("validateAssignment")) {
											if (Boolean.valueOf(inputValues
													.get("validateAssignment"))) {
												dayDiv.click();
												Thread.sleep(5000);
												FluentWebElements modals = fluent
														.divs(By.className("modal-content"));
												for (FluentWebElement modal : modals) {
													if (modal.isDisplayed()
															.value()
															&& modal.isEnabled()
																	.value()) {
														String header = modal
																.getText()
																.toString();
														log.info("======================== Modal Opened For : "
																+ header
																+ "==========================");
														if (header
																.contains("You have no active assignments for this period.")) {
															modal.div(
																	By.className("MainContainer"))
																	.click();
															Thread.sleep(5000);
															break;
														}
													}
												}
											}
										} else {
											if (inputValues.get("function")
													.equals("Copy to Next Day")) {
												dayDiv.click();
												Thread.sleep(wait);
												if (isMobile) {
													FluentWebElement footer = fluent
															.div(By.id("assignmentfooter"));
													FluentWebElements btns = footer
															.divs(By.className("footercontent"));
													for (FluentWebElement button : btns) {
														if (button.isEnabled()
																.value()
																&& button
																		.isDisplayed()
																		.value()) {
															String day = button
																	.span(By.className("copythrough"))
																	.getText()
																	.toString();
															if (!day.contains("Friday"))
																button.span(
																		By.className("paddingnone"))
																		.click();

														}
													}
												} else {
													fluent.button(
															By.xpath("contains(.,'Copy to Next Day')"))
															.click();
												}
												copyToTomorrow = true;
											}
										}
									}
								}
							}
							startTime++;
							takeScreenShots("/" + data.getScript()
									+ "/TimeEntry/screenshot", data.getGroup(),
									driver);

							Thread.sleep(wait);
						}
						Thread.sleep(wait);
						if (inputValues.containsKey("checkEntryRounding")
								&& Boolean.valueOf(inputValues
										.get("checkEntryRounding"))) {
							FluentWebElements roundOff = fluent.divs(By
									.className("dayentryrejectedtext"));
							for (FluentWebElement entry : roundOff) {
								if (entry.isDisplayed().value()
										&& entry.isEnabled().value()) {
									String error = entry.getText().toString();
									log.info("Error Entry : " + error);
									if (error
											.contains("Please make sure the time entered is rounded to"))
										state = true;
								}
							}
							break;
						} else {
							AddAttachmentAndComment(data, fluent, driver,
									Boolean.valueOf(inputValues
											.get("addAttachment")),
									Boolean.valueOf(inputValues
											.get("addComment")));
							if (inputValues.get("action").equals("Submit")) {
								FluentWebElements submits = fluent
										.divs(By.className("submitwork ng-binding enableworkstatus"));
								for (FluentWebElement submit : submits) {
									if (submit.isDisplayed().value()
											&& submit.isEnabled().value()) {
										submit.click();
										state = true;
									}
								}
							} else {
								baseButtons.get("Submit All For Approval")
										.click();
								state = true;
							}
							Thread.sleep(wait);
							takeScreenShots("/" + data.getScript()
									+ "/TimeEntry/screenshot", data.getGroup(),
									driver);
							FluentWebElements accepts = fluent.buttons(By
									.xpath("contains(.,'Accept')"));
							for (FluentWebElement accept : accepts) {
								log.info("Accept Values : "
										+ accept.getText().toString());
								if (accept.isDisplayed().value()
										&& accept.isEnabled().value()) {
									accept.click();
									state = true;
								}
							}
							if (isMonthly)
								fluent.button(By.xpath("contains(.,'Yes')"))
										.click();
							Thread.sleep(wait);
							takeScreenShots("/" + data.getScript()
									+ "/TimeEntry/screenshot", data.getGroup(),
									driver);
							break;
						}
					}
				}
			}
			if (!inputValues.containsKey("checkEntryRounding")) {
				Thread.sleep(wait);
				if (Boolean.valueOf(inputValues.get("showOT"))) {
					log.info("Waiting for another " + wait
							+ "-ms to get subbmitted Completely");
					Thread.sleep(wait);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);
					log.info("As a secondary Measure Waiting for another "
							+ wait + "-ms to get subbmitted Completely");
					Thread.sleep(wait);
					FluentWebElement showOT = fluent.span(By
							.className("showotbutton"));
					if (showOT.isDisplayed().value()
							&& showOT.isEnabled().value()) {
						showOT.click();
						Thread.sleep(5000);
						takeScreenShots("/" + data.getScript()
								+ "/TimeEntry/screenshot", data.getGroup(),
								driver);
						FluentWebElement saveOT = fluent.button(By
								.xpath("contains(.,'Ok')"));
						if (saveOT.isDisplayed().value()
								&& saveOT.isEnabled().value()) {
							saveOT.click();
							Thread.sleep(5000);
							takeScreenShots("/" + data.getScript()
									+ "/TimeEntry/screenshot", data.getGroup(),
									driver);
						}
					}
				}
				Thread.sleep(5000);
				if (Boolean.valueOf(inputValues.get("checkTMC"))) {
					checkTimeinTMC(driver, data, inputValues);
				}
				if (!Boolean.valueOf(inputValues.get("deleteFromTMC"))) {
					if (!Boolean.valueOf(inputValues.get("approveFromTMC"))) {
						if (!isMonthly)
							clearTime(data, driver, inputValues,
									Boolean.valueOf(inputValues
											.get("addAttachment")), isMonthly);
					}
				}
			}
			driver.quit();
		} catch (Exception e) {
			driver.quit();
			e.printStackTrace();
			log.error("TimeEntry Error Due To : "
					+ e.getCause().getLocalizedMessage());
		}
		log.info("******************************************************* END ***********************************************************");
		return state;
	}

	public void reCheckHMY(WebDriver webDriver, SuiteConfig data,
			Map<String, String> inputValues) {
		try {
			WebDriver driver = employeeLogin(data, inputValues, webDriver);
			FluentWebDriver fluent = new FluentWebDriver(driver);
			Thread.sleep(wait);
			if (driver.getTitle().equals("SelectClient")) {
				FluentWebElements inputClients = fluent.divs(By
						.className("links_container"));
				log.info("Clients : " + inputClients.getText().toString());
				for (FluentWebElement divs : inputClients) {
					FluentWebElement client = divs.input();
					if (client.getAttribute("value").toString()
							.equalsIgnoreCase(inputValues.get("client"))) {
						client.click();
						break;
					}
				}

				Thread.sleep(wait);
			}
			Thread.sleep(wait);
			inputValues.put("EmpName", fluent.div(By.className("customername"))
					.getText().toString());
			FluentWebElement dashboardMenu = fluent.div(By.id("dashboardmenu"));
			FluentWebElement tblTimesummary = dashboardMenu.div(By
					.id("tblTimesummary"));
			FluentWebElements timesheettitle = tblTimesummary.divs(By
					.id("timesheettitle"));

			if (timesheettitle.getText().toString().contains("Months")) {
				log.info("TimeSheet Title : "
						+ timesheettitle.getText().toString());
				Map<String, FluentWebElement> entryLinks = new HashMap<String, FluentWebElement>();
				for (FluentWebElement months : timesheettitle) {
					for (FluentWebElement timeLinks : months.links()) {
						entryLinks.put(timeLinks.getText().toString(),
								timeLinks);
					}
				}
				log.info("All Link Elements in TimeEntry Title : " + entryLinks);
				// entryLinks.get("Months").click();
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				Thread.sleep(1500);
			}
			FluentWebElements weekTimesheet = fluent
					.div(By.id("dashboardleft")).divs(
							By.className("timesheetdatarow ng-scope"));
			for (FluentWebElement time : weekTimesheet) {
				String status = time.div(By.className("statusdashboard"))
						.getText().toString();
				log.info("Status :: " + status);
				Thread.sleep(1500);
				String currentMonth = new SimpleDateFormat("MMM")
						.format(Calendar.getInstance().getTime());
				String actualMonth = time.link().getText().toString();
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				if (status.equals("Approved")
						| status.equals("SSaved Not Submitted")) {
					if (actualMonth.startsWith(currentMonth)) {
						log.info("Can not Submit Monthly Time for the Current Month :: "
								+ actualMonth);
					} else {
						FluentWebElement weekEnding = time.link();
						log.info("PayPeriod : "
								+ weekEnding.getText().toString());
						inputValues.put("timePeriod", weekEnding.getText()
								.toString().trim());
						weekEnding.click();
						Thread.sleep(wait);
						takeScreenShots("/" + data.getScript()
								+ "/TimeEntry/screenshot", data.getGroup(),
								driver);
					}
					break;
				}
			}
			takeScreenShots("/" + data.getScript() + "/TimeEntry/screenshot",
					data.getGroup(), driver);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("TimeEntry Error Due To : "
					+ e.getCause().getLocalizedMessage());
		}
	}

	public void checkTimeinTMC(WebDriver webDriver, SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = registerAndLogin(data, false, null, webDriver);
		Map<String, FluentWebElement> clientsMap = new HashMap<String, FluentWebElement>();
		try {
			for (String winHandle : driver.getWindowHandles()) {
				driver.switchTo().window(winHandle);
				log.info("winHandle: " + winHandle);
			}
			FluentWebDriver fluent = new FluentWebDriver(driver);
			takeScreenShots("/" + data.getScript() + "/TimeEntry/screenshot",
					data.getGroup(), driver);
			log.info("Input Values : " + inputValues);
			Thread.sleep(wait);
			driver.switchTo().frame("Table");
			// Find an Employee
			FluentWebElements clients = fluent.links(By.xpath("contains(.,'"
					+ inputValues.get("client") + "')"));// *[@id="ClientListTable"]/tbody/tr[287]/td[3]/span[1]/a
			for (FluentWebElement client : clients) {
				if (client.isDisplayed().value() && client.isEnabled().value()) {
					clientsMap.put(client.getText().toString(), client);
				}
			}
			FluentWebElement clientClick = clientsMap.get(inputValues
					.get("client"));
			clientClick.click();
			Thread.sleep(wait);
			driver.switchTo().frame("Table");
			FluentWebElement group = fluent.link(By.xpath("contains(.,'"
					+ inputValues.get("group") + "')"));
			if (group.isDisplayed().value() && group.isEnabled().value()) {
				group.click();
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				Thread.sleep(wait);
				driver.switchTo().frame("Table");
				FluentWebElement site = fluent.link(By.xpath("contains(.,'"
						+ inputValues.get("site") + "')"));
				if (site.isDisplayed().value() && site.isEnabled().value()) {
					site.click();
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);
					Thread.sleep(wait);
					String employeeName = inputValues.get("lastName") + ", "
							+ inputValues.get("firstName");
					String emp = employeeName;

					driver.switchTo().frame("Table");
					FluentWebElement employeeList = fluent.table(By
							.id("EmployeeList"));
					FluentWebElement tableBody = employeeList.tbody();
					FluentWebElements tableRows = tableBody.trs();

					for (FluentWebElement row : tableRows) {
						if (row.link().getText().toString()
								.equalsIgnoreCase(employeeName)) {
							emp = row.link().getText().toString();
							break;
						}
					}

					FluentWebElement employee = fluent.link(By
							.xpath("contains(.,'" + emp + "')"));
					if (employee.isDisplayed().value()
							&& employee.isEnabled().value()) {
						employee.click();
						takeScreenShots("/" + data.getScript()
								+ "/TimeEntry/screenshot", data.getGroup(),
								driver);
						Thread.sleep(wait);
						if (inputValues.containsKey("timePeriod")) {
							if (getFrameElement(driver, "Navigation") != null) {
								// Find an Employee
								driver.switchTo().frame(
										getFrameElement(driver, "Navigation"));
								Thread.sleep(wait);
								Select select = new Select(
										driver.findElement(By.name("PayPeriod")));
								select.selectByVisibleText(getPayPeriod(inputValues
										.get("timePeriod")));
								Robot robot = new Robot();

								robot.keyPress(KeyEvent.VK_ENTER);
								robot.keyRelease(KeyEvent.VK_ENTER);
							}
							takeScreenShots("/" + data.getScript()
									+ "/TimeEntry/screenshot", data.getGroup(),
									driver);
						}

						for (String winHandle : driver.getWindowHandles()) {
							driver.switchTo().window(winHandle);
							log.info("winHandle: " + winHandle);
						}

						Map<String, FluentWebElement> tmcActionButtons = new HashMap<String, FluentWebElement>();

						driver.switchTo().frame(
								getFrameElement(driver, "Total"));
						FluentWebElement table = fluent.table(By
								.className("admintable"));
						FluentWebElements allButtons = table.links();
						for (FluentWebElement button : allButtons) {
							if (button.isDisplayed().value()
									&& button.isEnabled().value()) {
								if (button.getText().toString() != null
										&& !button.getText().toString()
												.isEmpty())
									tmcActionButtons.put(button.getText()
											.toString(), button);
								else
									tmcActionButtons.put("Approve All", button);
							}
						}
						log.info("All TMC Action Buttons : " + tmcActionButtons);

						if (Boolean.valueOf(inputValues.get("approveFromTMC"))) {
							log.info("Approving Time From TMC for Employee : "
									+ emp);
							tmcActionButtons.get("Approve All").click();

							Thread.sleep(wait);
							Alert alert = driver.switchTo().alert();
							alert.accept();
							Thread.sleep(wait);
						}

						for (String winHandle : driver.getWindowHandles()) {
							driver.switchTo().window(winHandle);
							log.info("winHandle: " + winHandle);
						}
						Map<String, FluentWebElement> tmcDataModifier = new HashMap<String, FluentWebElement>();

						driver.switchTo().frame(
								getFrameElement(driver, "Table"));
						FluentWebElement dataTable = fluent.table(By
								.className("data-table"));
						FluentWebElement linksRow = dataTable.td(By
								.className("left no-border"));

						FluentWebElements allLinks = linksRow.links();

						for (FluentWebElement link : allLinks) {
							tmcDataModifier
									.put(link.getText().toString(), link);
						}

						log.info("All TMC Data Modification Buttons : "
								+ tmcDataModifier);

						if (Boolean.valueOf(inputValues.get("deleteFromTMC"))) {
							log.info("Deleting Time From TMC for Employee : "
									+ emp);
							tmcDataModifier.get("Delete/Rollback Time").click();

							((JavascriptExecutor) driver)
									.executeScript("return window.getSelection().toString();");
							Thread.sleep(wait);
							fluent.textarea(By.id("DeleteTimeCardComments"))
									.sendKeys(
											"Tested via automation at system time: "
													+ formatData
															.format(Calendar
																	.getInstance()
																	.getTime()));
							takeScreenShots("/" + data.getScript()
									+ "/TimeEntry/screenshot", data.getGroup(),
									driver);
							fluent.button(By.id("DeleteTimeCardWholeWeekBtn"))
									.click();

							Thread.sleep(wait);
							takeScreenShots("/" + data.getScript()
									+ "/TimeEntry/screenshot", data.getGroup(),
									driver);
							((JavascriptExecutor) driver)
									.executeScript("return window.getSelection().toString();");
							fluent.button(By.xpath("contains(.,'Yes')"))
									.click();
							Thread.sleep(wait);
							takeScreenShots("/" + data.getScript()
									+ "/TimeEntry/screenshot", data.getGroup(),
									driver);
						}
						reCheckHMY(webDriver, data, inputValues);
					}
				}
			}
		} catch (Exception e) {
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
		}
	}

	public void clearTime(SuiteConfig data, WebDriver webDriver,
			Map<String, String> inputValues, boolean addAttachment,
			boolean isMonthly) {
		try {
			WebDriver driver = employeeLogin(data, inputValues, webDriver);
			FluentWebDriver fluent = new FluentWebDriver(driver);
			Thread.sleep(wait);
			if (driver.getTitle().equals("SelectClient")) {
				FluentWebElements inputClients = fluent.divs(By
						.className("links_container"));
				log.info("Clients : " + inputClients.getText().toString());
				for (FluentWebElement divs : inputClients) {
					FluentWebElement client = divs.input();
					if (client.getAttribute("value").toString()
							.equalsIgnoreCase(inputValues.get("client"))) {
						client.click();
						break;
					}
				}

				Thread.sleep(wait);
			}
			Thread.sleep(wait);
			inputValues.put("EmpName", fluent.div(By.className("customername"))
					.getText().toString());
			FluentWebElement dashboardMenu = fluent.div(By.id("dashboardmenu"));
			FluentWebElement tblTimesummary = dashboardMenu.div(By
					.id("tblTimesummary"));
			FluentWebElements timesheettitle = tblTimesummary.divs(By
					.id("timesheettitle"));

			if (timesheettitle.getText().toString().contains("Months")) {
				log.info("TimeSheet Title : "
						+ timesheettitle.getText().toString());
				Map<String, FluentWebElement> entryLinks = new HashMap<String, FluentWebElement>();
				for (FluentWebElement months : timesheettitle) {
					for (FluentWebElement timeLinks : months.links()) {
						entryLinks.put(timeLinks.getText().toString(),
								timeLinks);
					}
				}
				log.info("All Link Elements in TimeEntry Title : " + entryLinks);
				// entryLinks.get("Months").click();
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				Thread.sleep(1500);
			}
			FluentWebElements weekTimesheet = fluent
					.div(By.id("dashboardleft")).divs(
							By.className("timesheetdatarow ng-scope"));
			for (FluentWebElement time : weekTimesheet) {
				String status = time.div(By.className("statusdashboard"))
						.getText().toString();
				log.info("Status :: " + status);
				Thread.sleep(1500);
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				Thread.sleep(wait);
				if (status.equals("Pending Approval")) {
					if (time.link().isDisplayed().value()
							&& time.link().isEnabled().value())
						time.link().click();
					else {
						time.link().click();
					}
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);
					Thread.sleep(wait);
					FluentWebElements pApprovals = fluent.divs(By
							.className("modifyassignment ng-binding"));
					for (FluentWebElement modify : pApprovals) {
						if (modify.isDisplayed().value()
								&& modify.isEnabled().value()) {
							modify.click();
							Thread.sleep(8000);
							FluentWebElements yesList = fluent.buttons(By
									.xpath("contains(.,'Yes')"));
							for (FluentWebElement yes : yesList) {
								if (yes.isDisplayed().value()
										&& yes.isEnabled().value())
									yes.click();
							}
						}
					}
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);
					Thread.sleep(wait);
					actualClear(driver, data, fluent, addAttachment, isMonthly);
					break;
				}
				if (status.equals("Saved Not Submitted")) {
					time.link().click();
					Thread.sleep(wait);
					actualClear(driver, data, fluent, addAttachment, isMonthly);
					break;
				}
			}
		} catch (Exception e) {
			log.error("ClearTime Error Due To : "
					+ e.getCause().getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public static void actualClear(WebDriver driver, SuiteConfig data,
			FluentWebDriver fluent, boolean addAttachement, boolean isMonthly) {
		try {
			// Open or Download TimeSheet
			Map<String, FluentWebElement> cAndAMap = new HashMap<String, FluentWebElement>();
			FluentWebElements commentsAndAttachments = fluent.divs(By
					.className("commentsattachment"));
			for (FluentWebElement cAndA : commentsAndAttachments) {
				if (cAndA.isDisplayed().value() && cAndA.isEnabled().value()) {
					FluentWebElements commentSpans = cAndA.spans(By
							.className("fa fa-comment"));
					if (commentSpans.size() > 0) {
						for (FluentWebElement comment : commentSpans) {
							if (comment.isDisplayed().value()
									&& comment.isEnabled().value())
								cAndAMap.put("Comment", comment);
						}
					}
					FluentWebElements attachmentsSpans = cAndA.spans(By
							.className("fa fa-paperclip"));

					if (attachmentsSpans.size() > 0) {
						for (FluentWebElement attachment : attachmentsSpans) {
							if (attachment.isDisplayed().value()
									&& attachment.isEnabled().value())
								cAndAMap.put("Attachment", attachment);
						}
					}
				}
			}
			if (addAttachement) {
				cAndAMap.get("Attachment").click();
				Thread.sleep(wait);
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				FluentWebElement download = fluent.div(
						By.className("col-sm-7 col-xs-6 attachmentdatacolumn"))
						.link();
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				if (download.isDisplayed().value()
						&& download.isEnabled().value()) {
					download.click();
					Thread.sleep(wait);
					Robot robot2 = new Robot();
					robot2.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
					robot2.keyPress(java.awt.event.KeyEvent.VK_TAB);
					Thread.sleep(wait);
					robot2.keyRelease(java.awt.event.KeyEvent.VK_TAB);
					robot2.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
					Thread.sleep(wait);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);
					FluentWebElements deletes = fluent
							.divs(By.className("col-sm-1 col-xs-2 attachmentdatacolumn attachmentcolumnwithoutborder delete"));
					for (FluentWebElement del : deletes) {
						del.click();
						Thread.sleep(wait);
						fluent.button(By.xpath("contains(.,'Yes')")).click();
						Thread.sleep(wait);
					}
					fluent.button(By.xpath("contains(.,'Close')")).click();
					Thread.sleep(wait);
					takeScreenShots("/" + data.getScript()
							+ "/TimeEntry/screenshot", data.getGroup(), driver);
				}
			}
			if (!isMonthly) {
				FluentWebElement workDiv = fluent.div(By
						.className("enableworkstatus"));
				FluentWebElements spans = workDiv.spans(By.name("enabled"));
				for (FluentWebElement span : spans) {
					if (span.isEnabled().value() && span.isDisplayed().value())
						span.click();
				}

				Thread.sleep(wait);
				fluent.button(By.xpath("contains(.,'Ok')")).click();
				Thread.sleep(wait);
				FluentWebElements spans1 = fluent.spans(By.name("enabled"));
				for (FluentWebElement span : spans1) {
					if (span.isEnabled().value() && span.isDisplayed().value())
						span.click();
				}
			} else {
				FluentWebElement entry = fluent
						.div(By.id("monthlytimesheet"))
						.div()
						.span()
						.div(By.className("col-sm-12 col-xs-4 paddingnone text-center NoTimeEntred"));
				FluentWebElements spans = null;
				if (isMonthly)
					spans = entry
							.div(By.className("col-sm-12 col-xs-4 paddingnone text-center NoTimeEntred"))
							.spans();
				else
					spans = entry.spans();
				FluentWebElement span = null;
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				for (FluentWebElement visibleSpan : spans) {
					if (visibleSpan.isEnabled().value()
							&& visibleSpan.isDisplayed().value())
						span = visibleSpan;
				}
				span.click();
				Thread.sleep(wait);
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				FluentWebElements modals = fluent.divs(By
						.className("modal-content"));
				for (FluentWebElement modal : modals) {
					if (modal.isDisplayed().value()
							&& modal.isEnabled().value()) {
						// Clearing Work Hours HPD
						FluentWebElements adjustmenthours = fluent.divs(By
								.id("adjustmenthours"));
						for (FluentWebElement adjHrs : adjustmenthours) {
							FluentWebElement workdivs = adjHrs.div(By
									.className("input-group"));
							FluentWebElement timeEntry = workdivs.input();
							if (timeEntry.isDisplayed().value()
									&& timeEntry.isEnabled().value()) {
								timeEntry.sendKeys(
										Keys.chord(Keys.CONTROL, "a"), "0");
							}
						}
						modal.button(
								By.xpath("//button[contains(.,'Copy through Friday')]"))
								.click();
					}
				}
			}
			Thread.sleep(wait);
		} catch (Exception e) {
			log.error("ActualClear Error Due To : "
					+ e.getCause().getLocalizedMessage());
			e.printStackTrace();
		}
	}

	public boolean DashboardValidate(SuiteConfig data,
			Map<String, String> inputValues) {
		WebDriver driver = employeeLogin(data, inputValues, null);
		boolean exec = false;
		try {
			FluentWebDriver fluent = new FluentWebDriver(driver);
			FluentWebElements weekTimesheet = fluent
					.div(By.id("dashboardleft")).divs(
							By.className("timesheetdatarow ng-scope"));
			takeScreenShots("/" + data.getScript()
					+ "/Dashboard/DashboardValidate", data.getGroup(), driver);
			Assert.assertEquals(weekTimesheet.size(), 5);
			log.info("Total Timesheet table size : " + weekTimesheet.size());
			String messages = fluent.div(By.id("messagehead")).getText()
					.toString();
			log.info("Visible Messages are : " + messages);
			Assert.assertTrue(messages.startsWith("Messages"));
			WebElement profile = driver
					.findElement(By
							.xpath("//*[@id='navigationmobile']/div[2]/div/ul[1]/li/a/span"));
			log.info("Visible Profile link Text : " + profile.getText());
			Assert.assertEquals(profile.getText(), "My Profile");
			Thread.sleep(wait);
			takeScreenShots("/" + data.getScript()
					+ "/Dashboard/DashboardValidate", data.getGroup(), driver);
			String lastLogin = driver
					.findElement(
							By.xpath("//*[@id='customerlogo']/div[1]/div[2]/div/div[2]"))
					.getText().toString();
			log.info(inputValues.get("EmpName") + "-" + lastLogin);
			Assert.assertTrue(lastLogin.contains("Last Sign In"));
			String signoutText = driver
					.findElement(
							By.xpath("//*[@id='navigationmobile']/div[2]/div/ul[2]/li/a/span"))
					.getText();
			log.info("Visible Signout Text : " + signoutText);
			Assert.assertEquals(signoutText, "Sign Out");
			driver.findElement(
					By.xpath("//*[@id='navigationmobile']/div[2]/div/ul[2]/li/a"))
					.click();
			Thread.sleep(wait);
			exec = true;
			driver.quit();
		} catch (Exception e) {
			driver.quit();
			log.error(data.getScript() + " Error Due to : " + e.getMessage());
		}
		return exec;
	}

	public static void AddAttachmentAndComment(SuiteConfig data,
			FluentWebDriver fluent, WebDriver driver, boolean addAttachement,
			boolean addComment) {
		try {
			Map<String, FluentWebElement> cAndAMap = new HashMap<String, FluentWebElement>();
			FluentWebElements commentsAndAttachments = fluent.divs(By
					.className("commentsattachment"));
			for (FluentWebElement cAndA : commentsAndAttachments) {
				if (cAndA.isDisplayed().value() && cAndA.isEnabled().value()) {
					FluentWebElements commentSpans = cAndA.spans(By
							.className("fa fa-comment"));
					if (commentSpans.size() > 0) {
						for (FluentWebElement comment : commentSpans) {
							if (comment.isDisplayed().value()
									&& comment.isEnabled().value())
								cAndAMap.put("Comment", comment);
						}
					}
					FluentWebElements attachmentsSpans = cAndA.spans(By
							.className("fa fa-paperclip"));

					if (attachmentsSpans.size() > 0) {
						for (FluentWebElement attachment : attachmentsSpans) {
							if (attachment.isDisplayed().value()
									&& attachment.isEnabled().value())
								cAndAMap.put("Attachment", attachment);
						}
					}
				}
			}
			if (addAttachement) {
				cAndAMap.get("Attachment").click();
				Thread.sleep(wait);
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				FluentWebElement browse = fluent.button(By
						.xpath("contains(.,'Browse')"));
				if (browse.isDisplayed().value() && browse.isEnabled().value()) {
					browse.click();
					Thread.sleep(wait);
					StringSelection ss = new StringSelection(
							"C:\\Users\\Public\\Pictures\\Sample Pictures\\Penguins.jpg");
					Toolkit.getDefaultToolkit().getSystemClipboard()
							.setContents(ss, null);
					Robot robot = new Robot();
					robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
					robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
					robot.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
					robot.keyPress(java.awt.event.KeyEvent.VK_V);
					robot.keyRelease(java.awt.event.KeyEvent.VK_V);
					robot.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
					robot.keyPress(java.awt.event.KeyEvent.VK_ENTER);
					robot.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
					Thread.sleep(wait);
					fluent.button(By.xpath("contains(.,'Attach')")).click();
					Thread.sleep(wait);
				}
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);

				// Add Multiple Attachments if any possible

				if (browse.isDisplayed().value() && browse.isEnabled().value()) {
					browse.click();
					Thread.sleep(wait);
					StringSelection ss1 = new StringSelection(
							"C:\\Users\\Public\\Pictures\\Sample Pictures\\Koala.jpg");
					Toolkit.getDefaultToolkit().getSystemClipboard()
							.setContents(ss1, null);
					Robot robot1 = new Robot();
					robot1.keyPress(java.awt.event.KeyEvent.VK_ENTER);
					robot1.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
					robot1.keyPress(java.awt.event.KeyEvent.VK_CONTROL);
					robot1.keyPress(java.awt.event.KeyEvent.VK_V);
					robot1.keyRelease(java.awt.event.KeyEvent.VK_V);
					robot1.keyRelease(java.awt.event.KeyEvent.VK_CONTROL);
					robot1.keyPress(java.awt.event.KeyEvent.VK_ENTER);
					robot1.keyRelease(java.awt.event.KeyEvent.VK_ENTER);
					Thread.sleep(wait);
					fluent.button(By.xpath("contains(.,'Attach')")).click();
					Thread.sleep(wait);
				}

				Thread.sleep(wait);
				fluent.button(By.xpath("contains(.,'Close')")).click();
				Thread.sleep(wait);
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
			}

			// Add Comment
			if (addComment) {
				cAndAMap.get("Comment").click();
				Thread.sleep(wait);
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				FluentWebElement text = fluent.textarea(By
						.id("commentstextarea"));
				text.click();
				String commentText = "Tested via automation at system time: "
						+ new SimpleDateFormat("MM/dd/yyyy-HH-mm-ss")
								.format(Calendar.getInstance().getTime());
				text.clearField();
				fluent.textarea(By.id("commentstextarea"))
						.sendKeys(commentText);
				Thread.sleep(wait);
				takeScreenShots("/" + data.getScript()
						+ "/TimeEntry/screenshot", data.getGroup(), driver);
				fluent.button(By.xpath("contains(.,'Comment')")).click();
			}
			Thread.sleep(wait);
			takeScreenShots("/" + data.getScript() + "/TimeEntry/screenshot",
					data.getGroup(), driver);
		} catch (Exception e) {
			log.error("AttachmentAndComment Error Due To : "
					+ e.getCause().getLocalizedMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void tests() {
		String content = new String();
		String groupTests = "HMY_TimeEntry";
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
						if (data.getScript().equals("HMY_Validate_Dashboard")) {
							status = DashboardValidate(data, inputValues);
						} else if (data.getProject().equals("FLOW")) {
							if (Boolean.valueOf(inputValues.get("addEmployee"))) {
								log.info("Input Values : " + inputValues);
								Map<String, Object> state = AddEmployee(data,
										inputValues);
								if (!state.isEmpty()) {
									if ((Boolean) state.get("status"))
										if (Boolean.valueOf(inputValues
												.get("processEmployee"))) {
											status = TimeEntryTest(data,
													inputValues,
													(WebDriver) state
															.get("driver"));
										}
								}
							}
							if (Boolean.valueOf(inputValues
									.get("processEmployee"))) {
								EmployeeData employee = DatabaseFactory
										.getEmployeeForClient(
												inputValues.get("empEmail"),
												inputValues.get("client"),
												inputValues);
								inputValues.put("userId",
										employee.getEmpEmail());
								inputValues.put("password",
										employee.getPassword());
								inputValues.put("recordId",
										employee.getRecordID());
								log.info("Input Values : " + inputValues);
								status = TimeEntryTest(data, inputValues, null);
							} else {
								if (Boolean
										.valueOf(inputValues.get("checkTMC"))) {
									EmployeeData employee = DatabaseFactory
											.getEmployeeForClient(
													inputValues.get("empEmail"),
													inputValues.get("client"),
													inputValues);
									inputValues.put("userId",
											employee.getEmpEmail());
									inputValues.put("password",
											employee.getPassword());
									inputValues.put("recordId",
											employee.getRecordID());
									log.info("Input Values : " + inputValues);
									checkTimeinTMC(null, data, inputValues);
								}
							}
						} else {
							status = TimeEntryTest(data, inputValues, null);
						}
						data.setStatus(String.valueOf(status));
						long endTime = System.currentTimeMillis();
						String currentTime = new String(""
								+ System.currentTimeMillis());
						content += HtmlReportGenerator.getFormattedHtmlHMY(
								data, data.getScript(), HtmlReportGenerator
										.getTime(endTime - startTime),
								inputValues, currentTime.trim());
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