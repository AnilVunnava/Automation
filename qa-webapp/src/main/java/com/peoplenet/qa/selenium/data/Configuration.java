package com.peoplenet.qa.selenium.data;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;

public class Configuration implements Serializable {

	private static final long serialVersionUID = 1L;
	private String environment;
	private String buildVersion;
	private String testResultsDir;
	@Email
	private String email;
	@NotNull
	private boolean emailReports;
	@NotNull
	private boolean remoteDriver;

	private String browser;
	private String screenshotDirectory;
	private boolean enableVideo;
	private String host;
	private String port;
	private String context;
	private String desiredBrowserVersion;
	private String moduleName;
	private String testCase;
	@NotNull
	private String testUrl;
	private String testInput;
	private String testExpected;

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getTestCase() {
		return testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

	public String getTestUrl() {
		return testUrl;
	}

	public void setTestUrl(String testUrl) {
		this.testUrl = testUrl;
	}

	public String getTestInput() {
		return testInput;
	}

	public void setTestInput(String testInput) {
		this.testInput = testInput;
	}

	public String getTestExpected() {
		return testExpected;
	}

	public void setTestExpected(String testExpected) {
		this.testExpected = testExpected;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getScreenshotDirectory() {
		return screenshotDirectory;
	}

	public void setScreenshotDirectory(String screenshotDirectory) {
		this.screenshotDirectory = screenshotDirectory;
	}

	public boolean isEnableVideo() {
		return enableVideo;
	}

	public void setEnableVideo(boolean enableVideo) {
		this.enableVideo = enableVideo;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getDesiredBrowserVersion() {
		return desiredBrowserVersion;
	}

	public void setDesiredBrowserVersion(String desiredBrowserVersion) {
		this.desiredBrowserVersion = desiredBrowserVersion;
	}

	public String getTestResultsDir() {
		return testResultsDir;
	}

	public void setTestResultsDir(String testResultsDir) {
		this.testResultsDir = testResultsDir;
	}

	public String getEnvironment() {
		return environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public String getBuildVersion() {
		return buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	public boolean isEmailReports() {
		return emailReports;
	}

	public void setEmailReports(boolean emailReports) {
		this.emailReports = emailReports;
	}

	public boolean isRemoteDriver() {
		return remoteDriver;
	}

	public void setRemoteDriver(boolean remoteDriver) {
		this.remoteDriver = remoteDriver;
	}
}
