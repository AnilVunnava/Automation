package com.peoplenet.qa.selenium.data;

import org.seleniumhq.selenium.fluent.FluentWebElement;

public class CustomElements {

	public CustomElements(String base, String field, FluentWebElement element) {
		super();
		this.base = base;
		this.field = field;
		this.element = element;
	}

	private String base;
	private String field;
	private FluentWebElement element;

	/**
	 * @return the base
	 */
	public String getBase() {
		return base;
	}

	/**
	 * @param base
	 *            the base to set
	 */
	public void setBase(String base) {
		this.base = base;
	}

	/**
	 * @return the field
	 */
	public String getField() {
		return field;
	}

	/**
	 * @param field
	 *            the field to set
	 */
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * @return the element
	 */
	public FluentWebElement getElement() {
		return element;
	}

	/**
	 * @param element
	 *            the element to set
	 */
	public void setElement(FluentWebElement element) {
		this.element = element;
	}

}
