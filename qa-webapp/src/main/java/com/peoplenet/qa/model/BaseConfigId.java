package com.peoplenet.qa.model;

// Generated Nov 4, 2015 10:37:05 AM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BaseConfigId generated by hbm2java
 */
@Embeddable
public class BaseConfigId implements java.io.Serializable {

	private static final long serialVersionUID = 5523683171444730772L;
	private String project;
	private String environment;
	private String buildVersion;

	public BaseConfigId() {
	}

	public BaseConfigId(String project, String environment, String buildVersion) {
		this.project = project;
		this.environment = environment;
		this.buildVersion = buildVersion;
	}

	@Column(name = "project", nullable = false)
	public String getProject() {
		return this.project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	@Column(name = "environment", nullable = false)
	public String getEnvironment() {
		return this.environment;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	@Column(name = "buildVersion", nullable = false)
	public String getBuildVersion() {
		return this.buildVersion;
	}

	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BaseConfigId))
			return false;
		BaseConfigId castOther = (BaseConfigId) other;

		return ((this.getProject() == castOther.getProject()) || (this
				.getProject() != null && castOther.getProject() != null && this
				.getProject().equals(castOther.getProject())))
				&& ((this.getEnvironment() == castOther.getEnvironment()) || (this
						.getEnvironment() != null
						&& castOther.getEnvironment() != null && this
						.getEnvironment().equals(castOther.getEnvironment())))
				&& ((this.getBuildVersion() == castOther.getBuildVersion()) || (this
						.getBuildVersion() != null
						&& castOther.getBuildVersion() != null && this
						.getBuildVersion().equals(castOther.getBuildVersion())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getProject() == null ? 0 : this.getProject().hashCode());
		result = 37
				* result
				+ (getEnvironment() == null ? 0 : this.getEnvironment()
						.hashCode());
		result = 37
				* result
				+ (getBuildVersion() == null ? 0 : this.getBuildVersion()
						.hashCode());
		return result;
	}

}
