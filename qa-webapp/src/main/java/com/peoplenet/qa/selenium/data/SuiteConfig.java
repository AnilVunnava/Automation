package com.peoplenet.qa.selenium.data;

// Model for storing ExcelData
public class SuiteConfig {

	private String script;
	private String group;
	private String project;
	private String browser;
	private String status;
	private String input;
	private String description;

	/**
	 * @return the script
	 */
	public String getScript() {
		return script;
	}

	/**
	 * @param script
	 *            the script to set
	 */
	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * @return the project
	 */
	public String getProject() {
		return project;
	}

	/**
	 * @param project
	 *            the project to set
	 */
	public void setProject(String project) {
		this.project = project;
	}

	/**
	 * @return the run_in_Browser
	 */
	public String getBrowser() {
		return browser;
	}

	/**
	 * @param run_in_Browser
	 *            the run_in_Browser to set
	 */
	public void setBrowser(String run_in_Browser) {
		if (run_in_Browser != null) {
			if (run_in_Browser.startsWith("IE")
					|| run_in_Browser.startsWith("Chrome")
					|| run_in_Browser.startsWith("Firefox")
					|| run_in_Browser.startsWith("Safari"))
				this.browser = run_in_Browser;
		} else
			this.browser = "IE11";
	}

	/**
	 * @return the run_Status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param run_Status
	 *            the run_Status to set
	 */
	public void setStatus(String run_Status) {
		if (run_Status != null) {
			if (run_Status.equalsIgnoreCase("Yes")
					|| run_Status.equalsIgnoreCase("No"))
				this.status = run_Status;
			else if (run_Status.equalsIgnoreCase("true"))
				this.status = "Passed";
			else if (run_Status.equalsIgnoreCase("false"))
				this.status = "Failed";
			else
				this.status = "No";
		} else
			this.status = "No";
	}

	/**
	 * @return the input
	 */
	public String getInput() {
		return input;
	}

	/**
	 * @param input
	 *            the input to set
	 */
	public void setInput(String input) {
		this.input = input;
	}

	@Override
	public String toString() {
		return String.format("SuiteConfig : {" + "[Script : "
				+ this.getScript() + "],[Group : " + this.getGroup()
				+ "],[Project : " + this.getProject() + "],[Browser : "
				+ this.getBrowser() + "],[Status : " + this.getStatus()
				+ "],[Input : " + this.getInput() + "],[Summary : "
				+ this.getDescription() + "]}");
	}

	/**
	 * @return the unique script string
	 */

	public String getUniqueScript() {
		return "[" + this.script + "-" + this.browser + "-" + this.input + "-"
				+ this.status + "]";
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
