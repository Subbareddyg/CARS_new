package com.belk.car.app.model.report;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.appfuse.model.User;

import com.belk.car.util.DateUtils;

public class BaseReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5733977592349769987L;

	private String name;
	private Date reportDate;
	private String reportedBy;
	private User currentUser ;
	private Map<String, Object> criteria = new HashMap<String, Object>();
	private Map<String, Object> dataMap;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public String getReportDateFormatted() {
		return DateUtils.formatDate(reportDate, "dd-MMM-yyyy hh:mm:ss");
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Map<String, Object> getCriteria() {
		return criteria;
	}

	public void setCriteria(Map<String, Object> criteria) {
		this.criteria = criteria;
	}

	public Map<String, Object> getDataMap() {
		return dataMap;
	}

	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}

	public String getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(String reportedBy) {
		this.reportedBy = reportedBy;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
