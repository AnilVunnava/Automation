package com.peoplenet.qa.selenium.data;

// Model For storing Login Data
public class AccessConfig {

	private String Script;
	private String URL;
	private String Login;
	private String Pasword;

	/**
	 * @return the script
	 */
	public String getScript() {
		return Script;
	}

	/**
	 * @param script
	 *            the script to set
	 */
	public void setScript(String script) {
		this.Script = script;
	}

	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL
	 *            the uRL to set
	 */
	public void setURL(String uRL) {
		this.URL = uRL;
	}

	/**
	 * @return the login
	 */
	public String getLogin() {
		return Login;
	}

	/**
	 * @param login
	 *            the login to set
	 */
	public void setLogin(String login) {
		this.Login = login;
	}

	/**
	 * @return the pasword
	 */
	public String getPasword() {
		return Pasword;
	}

	/**
	 * @param pasword
	 *            the pasword to set
	 */
	public void setPasword(String pasword) {
		this.Pasword = pasword;
	}

	@Override
	public String toString() {
		return String.format("AccessConfig : {" + "[Script : "
				+ this.getScript() + "],[URL : " + this.getURL()
				+ "],[Login : " + this.getLogin() + "],[Pasword : "
				+ this.getPasword() + "]}");
	}

}
