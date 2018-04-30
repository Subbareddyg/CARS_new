package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "VENDOR_STYLE_TYPE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include = "all")
public class VendorStyleType extends BaseAuditableModel implements
		java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4529575107994146462L;

	public static String PATTERN_CONS_VS = "PATTERN-CONS-VS";                    
	public static String PATTERN_SPLIT_VS = "PATTERN-SPLIT-VS";                  
	public static String PATTERN_SGBS_VS = "PATTERN-SGBS-VS";
	public static String PATTERN_SSKU_VS = "PATTERN-SSKU-VS";
	public static String PRODUCT = "PRODUCT";
	public static String OUTFIT = "OUTFIT";
	public static String PYG = "PYG";// added for Deal based management
	

	private String code;
	private String name;
	private String descr;

	public VendorStyleType() {
	}


	@Id
	@Column(name = "VENDOR_STYLE_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Override
	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Override
	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Override
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

}
