package com.srinsoft.slenium.tests.web.domain;

public class WebElementPojo {

	public WebElementPojo(String id, String name, String text, String value,
			String type, String tag) {
		super();
		this.id = id;
		this.name = name;
		this.text = text;
		this.value = value;
		this.type = type;
		this.tag = tag;
	}

	private String id;
	private String name;
	private String text;
	private String value;
	private String type;
	private String tag;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

}
