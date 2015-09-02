package com.srinsoft.slenium.tests.web.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class InputData implements Serializable {
	private static final long serialVersionUID = -905511959165722515L;

	@NotNull
	@Size(min = 1, max = 25)
	@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	private String testModule;

	@NotNull
	@Size(min = 1, max = 25)
	@Pattern(regexp = "[A-Za-z ]*", message = "must contain only letters and spaces")
	private String testCase;

	@NotNull
	@Size(min = 1, max = 25)
	private String testInput;

	public String getTestModule() {
		return testModule;
	}

	public void setTestModule(String testModule) {
		this.testModule = testModule;
	}

	public String getTestCase() {
		return testCase;
	}

	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}

	public String getTestInput() {
		return testInput;
	}

	public void setTestInput(String testInput) {
		this.testInput = testInput;
	}

}