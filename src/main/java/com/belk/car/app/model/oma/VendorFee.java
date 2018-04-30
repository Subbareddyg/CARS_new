/**
 * Class Name : VendorFeeModel.java 
 * Version Information : v1.0 
 * Date : 12/01/2009 
 * Copyright Notice : All rights are reserved to Syntel.
 */
package com.belk.car.app.model.oma;

import java.io.Serializable;
import java.text.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
/**
 * Bean class to map the Vendor Fee, which contains the fee amounts.
 * @author afusy13
 *
 */
@Entity
@Table(name = "VNDR_FEE", uniqueConstraints = @UniqueConstraint(columnNames = "VNDR_FEE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorFee extends BaseAuditableModel implements Serializable{
	/**
	 * Default Serial Version ID
	 */
	private static final long serialVersionUID = 1L;
	private long vendorFeeId;
	private Fee  fee;  
	
	
	private char allowable; 
	private String allow;
	private long feeId;
	private  double perOrderAmount;
	private double perItemAmount;
	
	private String perOrderAmt;
	private String perItemAmt;

	/**
	 * @param fee
	 */
	public VendorFee() {
		super();
		this.fee = new Fee();
	}
	
	public VendorFee(Fee fee) {
		super();
		this.fee = fee;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VNDR_FEE_ID_GEN")
	@javax.persistence.SequenceGenerator(name = "SEQ_VNDR_FEE_ID_GEN", sequenceName = "SEQ_VNDR_FEE_ID", allocationSize = 1)
	@Column(name = "VNDR_FEE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getVendorFeeId() {
		return vendorFeeId;
	}
	public void setVendorFeeId(long vendorFeeId) {
		this.vendorFeeId = vendorFeeId;
	}
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="FEE_CD", nullable = false)
	public Fee getFee() {
		return fee;
	}
	public void setFee(Fee fee) {
		//this.feeId=fee.getFeeId();
		this.fee = fee;
	}
	
	@Column(name="ALLOW_OPTION", nullable = false)
	public char getAllowable() {
		//this.allow = ""+allowable;
		return allowable;
	}
	public void setAllowable(char allowable) {
		this.allowable = allowable;
	}
	@Column(name="PER_ORDER_AMOUNT")
	public double getPerOrderAmount() {
		return perOrderAmount;
	}
	public void setPerOrderAmount(double perOrderAmount) {
		this.perOrderAmount = perOrderAmount;
	}
	@Column(name = "PER_ITEM_AMOUNT")
	public double getPerItemAmount() {
		return perItemAmount;
	}
	public void setPerItemAmount(double perItemAmount) {
		this.perItemAmount = perItemAmount;
	}
	
	@Transient
	public String getAllow() {
		allow = ""+(this.allowable);
		return allow;
		//return allow;
	}
	public void setAllow(String  allow){
		this.allow = allow;
		if(allow.compareTo("Y") == 0 ){
			this.allowable = 'Y';
		}else{
			this.allowable = 'N';
		}
	}
		
	public void setFeeId(long feeId) {
		this.feeId = feeId;
	}
	@Transient
	public long getFeeId() {
		return feeId;
	}
	 
	@Transient
	public String getPerOrderAmt() {
		perOrderAmt = checkValueAndAddZero(String.valueOf(perOrderAmount));
		return perOrderAmt;
	}

	public void setPerOrderAmt(String perOrderAmt) {
		this.perOrderAmt = perOrderAmt;
		this.perOrderAmount = Double.parseDouble(removeUnwantedCharacters(perOrderAmt));
	}
	@Transient
	public String getPerItemAmt() {
		perItemAmt = checkValueAndAddZero(String.valueOf(perItemAmount));
		return perItemAmt;
	}

	public void setPerItemAmt(String perItemAmt) {
		this.perItemAmt = perItemAmt;
		this.perItemAmount = Double.parseDouble(removeUnwantedCharacters(perItemAmt));
	} 
	/**
	 * Appends the zeros if number is not in form 00.00
	 * @param value	String Value to be formatted.
	 * @return value String Formatted value.
	 */
	private String checkValueAndAddZero(String value){
		String zero ="0";
		if(value.contains("E")){
			value = getAmountInProperForm(Double.parseDouble(value));
		}else{
			
			if(value!= null){
				if(value.substring(0,value.indexOf('.')).length() < 2 ){
					value = zero.concat(value);
				}

			}else {
				value="00.00";
			}
		}
		if((value.substring(value.indexOf('.')+1,value.length())).length() < 2){
			value =  value.concat(zero);
		}
		//The case when no decimal point is entered.
		if(value.indexOf('.') == -1){
			value =  value.concat(".").concat(zero).concat(zero);
		}
		return value;
	}
	/**
	 * Removes the unwanted characters like comma, from String
	 * @param amount	String to be formatted
	 * @return	String without any Commas.
	 */
	private String removeUnwantedCharacters(String amount){
		return amount.replace(",", "");
	}
	/**
	 * Formats the double into user friendly format.
	 * @param amount {@link Double} Value to be formatted
	 * @return	String with proper format.
	 */
	private String getAmountInProperForm(Double amount) {
		NumberFormat num = NumberFormat.getNumberInstance();
		String sNumber = num.format(amount);
		return removeUnwantedCharacters(sNumber);
	}
}
