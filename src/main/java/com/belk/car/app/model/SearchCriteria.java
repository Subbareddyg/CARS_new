package com.belk.car.app.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SearchCriteria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4561163431577195238L;
	
	private Map<String, String> criteria = new HashMap<String, String>();

	public Map<String, String> getCriteria() {
		return criteria;
	}

	public void setCriteria(Map<String, String> criteria) {
		this.criteria = criteria;
	}
	
	public String getCriteriaAsQueryString() {
		StringBuffer strBuf = new StringBuffer() ;
		if (!this.criteria.isEmpty()) {
			for(String name: criteria.keySet()) {
				strBuf.append("&").append(name).append("=").append(criteria.get(name)) ;
			}
			return strBuf.substring(1);
		}
		return strBuf.toString();
	}
}
