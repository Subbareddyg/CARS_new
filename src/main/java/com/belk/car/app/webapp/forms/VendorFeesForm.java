/**
 * Class Name : FSContactForm.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.webapp.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.belk.car.app.model.oma.Fee;
import com.belk.car.app.model.oma.VendorFee;
import com.belk.car.app.model.oma.VendorFeeRequest;

/**
 * Form Class to map the fields of VendorFees Form
 * @author afusy13
 *
 */
@SuppressWarnings("unchecked")
public class VendorFeesForm implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private VendorFeeRequest vendorFeeRequestModel;
	private List<VendorFee> vendorFees = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(VendorFee.class));
	private List<Fee> fees = LazyList.decorate(new ArrayList(), FactoryUtils.instantiateFactory(Fee.class));
	private String formEffectiveDate;
	public VendorFeesForm(){
		vendorFeeRequestModel = new VendorFeeRequest(); //Required as need to map the blank fields in case of first Vendor Fee.
		fees.add(new Fee());
		vendorFees.add(new VendorFee());
		
	}
	
	/**
	 * @param vendorFeeRequestModel the vendorFeeRequestModel to set
	 */
	public void setVendorFeeRequestModel(VendorFeeRequest vendorFeeRequestModel) {
		this.vendorFeeRequestModel = vendorFeeRequestModel;
	}
	/**
	 * @return the vendorFeeRequestModel
	 */
	public VendorFeeRequest getVendorFeeRequestModel() {
		return vendorFeeRequestModel;
	}
	/**
	 * @param vendorFees the vendorFees to set
	 */
	public void setVendorFees(List<VendorFee> vendorFees) {
		this.vendorFees = vendorFees;
	}
	/**
	 * @return the vendorFees
	 */
	public List<VendorFee> getVendorFees() {
		return vendorFees;
	}

	/**
	 * @param fees the fees to set
	 */
	public void setFees(List<Fee> fees) {
		this.fees = fees;
	}

	/**
	 * @return the fees
	 */
	public List<Fee> getFees() {
		return fees;
	}

	/**
	 * @param formEffectiveDate the formEffectiveDate to set
	 */
	public void setFormEffectiveDate(String formEffectiveDate) {
		this.formEffectiveDate = formEffectiveDate;
	}

	/**
	 * @return the formEffectiveDate
	 */
	public String getFormEffectiveDate() {
		return formEffectiveDate;
	}

}
