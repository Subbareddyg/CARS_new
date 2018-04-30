package com.belk.car.app.webapp.forms;

/**
 * 
 * This is a POJO for holding values of a form used to add/edit  a super color rule configuration 
 * @author Yogesh.Vedak
 *
 */
public class SuperColorForm {

	private long cmmId;
	private String colorCodeBegin="";
	private String colorCodeEnd="";
	private String superColorCode="";
	private String superColorName="";
	private String statusCode="ACTIVEA";
	private Boolean isEditForm = Boolean.FALSE;
	
	
	
	public long getCmmId() {
		return cmmId;
	}
	public void setCmmId(long cmmId) {
		this.cmmId = cmmId;
	}
	public String getColorCodeBegin() {
		return colorCodeBegin;
	}
	public void setColorCodeBegin(String colorCodeBegin) {
		this.colorCodeBegin = colorCodeBegin;
	}
	public String getColorCodeEnd() {
		return colorCodeEnd;
	}
	public void setColorCodeEnd(String colorCodeEnd) {
		this.colorCodeEnd = colorCodeEnd;
	}
	public String getSuperColorCode() {
		return superColorCode;
	}
	public void setSuperColorCode(String superColorCode) {
		this.superColorCode = superColorCode;
	}
	public String getSuperColorName() {
		return superColorName;
	}
	public void setSuperColorName(String superColorName) {
		this.superColorName = superColorName;
	}
	public Boolean getIsEditForm() {
		return isEditForm;
	}
	public void setIsEditForm(Boolean isEditForm) {
		this.isEditForm = isEditForm;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
}
