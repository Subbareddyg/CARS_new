package com.belk.car.app.model.view;

import java.util.Date;

public class CarCmpStatusView extends CarPendingImagesView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2462138596436720946L;
	
	private String cmpStatus;
	private String inv;
	private String approvalStatus;
	private Date firstPublishedDate;
	private Date lastPublishedDate;
	private String parentTask;
	private Date parentTaskCreateDate;
	private Date parentTaskLastUpdateDate;
	private String parentTaskStatus;
	private String copyTask;
	private Date copyTaskCreateDate;
	private Date copyTaskLastUpdateDate;
	private String copyTaskStatus;
	private String copyTaskAssignedTo;
	private String imageTask;
	private Date imageTaskCreateDate;
	private Date imageTaskLastUpdateDate;
	private String imageTaskStatus;
	private String imageTaskAssignedTo;

	public String getCmpStatus() {
		return cmpStatus;
	}

	public void setCmpStatus(String cmpStatus) {
		this.cmpStatus = cmpStatus;
	}
	public String getInv() {
		return inv;
	}

	public void setInv(String inv) {
		this.inv = inv;
	}
	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public Date getFirstPublishedDate() {
		return firstPublishedDate;
	}

	public void setFirstPublishedDate(Date firstPublishedDate) {
		this.firstPublishedDate = firstPublishedDate;
	}

	public Date getLastPublishedDate() {
		return lastPublishedDate;
	}

	public void setLastPublishedDate(Date lastPublishedDate) {
		this.lastPublishedDate = lastPublishedDate;
	}

	public String getParentTask() {
		return parentTask;
	}

	public void setParentTask(String parentTask) {
		this.parentTask = parentTask;
	}

	public Date getParentTaskCreateDate() {
		return parentTaskCreateDate;
	}

	public void setParentTaskCreateDate(Date parentTaskCreateDate) {
		this.parentTaskCreateDate = parentTaskCreateDate;
	}

	public String getParentTaskStatus() {
		return parentTaskStatus;
	}

	public void setParentTaskStatus(String parentTaskStatus) {
		this.parentTaskStatus = parentTaskStatus;
	}

	public String getCopyTask() {
		return copyTask;
	}

	public void setCopyTask(String copyTask) {
		this.copyTask = copyTask;
	}

	public Date getCopyTaskCreateDate() {
		return copyTaskCreateDate;
	}

	public void setCopyTaskCreateDate(Date copyTaskCreateDate) {
		this.copyTaskCreateDate = copyTaskCreateDate;
	}

	public String getCopyTaskStatus() {
		return copyTaskStatus;
	}

	public void setCopyTaskStatus(String copyTaskStatus) {
		this.copyTaskStatus = copyTaskStatus;
	}

	public String getCopyTaskAssignedTo() {
		return copyTaskAssignedTo;
	}

	public void setCopyTaskAssignedTo(String copyTaskAssignedTo) {
		this.copyTaskAssignedTo = copyTaskAssignedTo;
	}

	public String getImageTask() {
		return imageTask;
	}

	public void setImageTask(String imageTask) {
		this.imageTask = imageTask;
	}

	public Date getImageTaskCreateDate() {
		return imageTaskCreateDate;
	}

	public void setImageTaskCreateDate(Date imageTaskCreateDate) {
		this.imageTaskCreateDate = imageTaskCreateDate;
	}

	public String getImageTaskStatus() {
		return imageTaskStatus;
	}

	public void setImageTaskStatus(String imageTaskStatus) {
		this.imageTaskStatus = imageTaskStatus;
	}

	public String getImageTaskAssignedTo() {
		return imageTaskAssignedTo;
	}

	public void setImageTaskAssignedTo(String imageTaskAssignedTo) {
		this.imageTaskAssignedTo = imageTaskAssignedTo;
	}

	public Date getParentTaskLastUpdateDate() {
		return parentTaskLastUpdateDate;
	}

	public void setParentTaskLastUpdateDate(Date parentTaskLastUpdateDate) {
		this.parentTaskLastUpdateDate = parentTaskLastUpdateDate;
	}

	public Date getCopyTaskLastUpdateDate() {
		return copyTaskLastUpdateDate;
	}

	public void setCopyTaskLastUpdateDate(Date copyTaskLastUpdateDate) {
		this.copyTaskLastUpdateDate = copyTaskLastUpdateDate;
	}

	public Date getImageTaskLastUpdateDate() {
		return imageTaskLastUpdateDate;
	}

	public void setImageTaskLastUpdateDate(Date imageTaskLastUpdateDate) {
		this.imageTaskLastUpdateDate = imageTaskLastUpdateDate;
	}
}
