package com.belk.car.app.model.report;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.belk.car.app.dto.DetailNotificationUserDTO;

public class LateCarSummaryData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
	
	//GMM
	HashMap<String, HashMap<String, List<DetailNotificationUserDTO>>> GMMs; 
	
	
	private Date reportDate;
	private String buyerName;
	private long count; // total number of late cars
	private long skusCount;// total number of skus for all cars in the report
	private String dmmName;
	private String gmmName;
	private long buyerOrVendorCount;//total number of late cars for a particular buyer
	private long buyerOrVendorSkusCount;// total number of skus for each car for a particular buyer
	private long dMMCount;
	private long gMMCount;
	private long artDirectorCount;
	private long sampleCoOrdinatorCount;
	private long contentManagerCount;
	private long nonBuyerOrVendorCount;

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public long getSkusCount() {
		return skusCount;
	}

	public void setSkusCount(long skusCount) {
		this.skusCount = skusCount;
	}

	public String getDmmName() {
		return dmmName;
	}

	public void setDmmName(String dmmName) {
		this.dmmName = dmmName;
	}

	public String getGmmName() {
		return gmmName;
	}

	public void setGmmName(String gmmName) {
		this.gmmName = gmmName;
	}

	public long getBuyerOrVendorCount() {
		return buyerOrVendorCount;
	}

	public void setBuyerOrVendorCount(long buyerOrVendorCount) {
		this.buyerOrVendorCount = buyerOrVendorCount;
	}

	public long getBuyerOrVendorSkusCount() {
		return buyerOrVendorSkusCount;
	}

	public void setBuyerOrVendorSkusCount(long buyerOrVendorSkusCount) {
		this.buyerOrVendorSkusCount = buyerOrVendorSkusCount;
	}

	public long getdMMCount() {
		return dMMCount;
	}

	public void setdMMCount(long dMMCount) {
		this.dMMCount = dMMCount;
	}

	public long getgMMCount() {
		return gMMCount;
	}

	public void setgMMCount(long gMMCount) {
		this.gMMCount = gMMCount;
	}

	public long getArtDirectorCount() {
		return artDirectorCount;
	}

	public void setArtDirectorCount(long artDirectorCount) {
		this.artDirectorCount = artDirectorCount;
	}

	public long getSampleCoOrdinatorCount() {
		return sampleCoOrdinatorCount;
	}

	public void setSampleCoOrdinatorCount(long sampleCoOrdinatorCount) {
		this.sampleCoOrdinatorCount = sampleCoOrdinatorCount;
	}

	public long getContentManagerCount() {
		return contentManagerCount;
	}

	public void setContentManagerCount(long contentManagerCount) {
		this.contentManagerCount = contentManagerCount;
	}

	public long getNonBuyerOrVendorCount() {
		return nonBuyerOrVendorCount;
	}

	public void setNonBuyerOrVendorCount(long nonBuyerOrVendorCount) {
		this.nonBuyerOrVendorCount = nonBuyerOrVendorCount;
	}

}
