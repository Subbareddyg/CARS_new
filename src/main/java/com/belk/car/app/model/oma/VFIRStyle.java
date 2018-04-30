/**
 * 
 */
package com.belk.car.app.model.oma;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Vendor;

/**
 * @author afusy01
 *
 */

@Entity
@Table(name = "vifr_style")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VFIRStyle extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3246981271327690036L;
	
	public static final String ACTIVE = "ACTIVE";
	public static final String INACTIVE = "INACTIVE";
	
	private CompositeKeyVIFRStyle compositeKeyVIFRStyle;
	private String styleDescription;
	private String allSkuIndicator;
	private String allColorIndicator;
	private String allSizeIndicator;
	private Double unitCost;
	private Double unitHandlingFee;
	private Double overrideUnitCost;
	private Double overrideFee;
	private String statusCode;
	private Vendor vendor;
	
	/**
	 * 
	 */
	public VFIRStyle() {
	}
	
	
	public void setCompositeKeyVIFRStyle(CompositeKeyVIFRStyle compositeKeyVIFRStyle) {
		this.compositeKeyVIFRStyle = compositeKeyVIFRStyle;
	}

	@EmbeddedId
	public CompositeKeyVIFRStyle getCompositeKeyVIFRStyle() {
		return compositeKeyVIFRStyle;
	}


	public void setStyleDescription(String styleDescription) {
		this.styleDescription = styleDescription;
	}
	@Column(name = "style_descr", nullable = true, length = 200)
	public String getStyleDescription() {
		return styleDescription;
	}

	@Column(name = "applies_to_all_sku_ind", nullable = false, length = 1)
	public String getAllSkuIndicator() {
		return allSkuIndicator;
	}
	public void setAllSkuIndicator(String allSkuIndicator) {
		this.allSkuIndicator = allSkuIndicator;
	}
	
	@Column(name = "applies_to_all_color_ind", nullable = false, length = 1)
	public String getAllColorIndicator() {
		return allColorIndicator;
	}
	public void setAllColorIndicator(String allColorIndicator) {
		this.allColorIndicator = allColorIndicator;
	}
	
	@Column(name = "applies_to_all_size_ind", nullable = false, length = 1)
	public String getAllSizeIndicator() {
		return allSizeIndicator;
	}
	public void setAllSizeIndicator(String allSizeIndicator) {
		this.allSizeIndicator = allSizeIndicator;
	}
	
	@Column(name = "unit_cost", nullable = true, scale=12, precision=2)
	public Double getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}
	
	@Column(name = "unit_handling_fee", nullable = true, scale=12, precision=2)
	public Double getUnitHandlingFee() {
		return unitHandlingFee;
	}
	public void setUnitHandlingFee(Double unitHandlingFee) {
		this.unitHandlingFee = unitHandlingFee;
	}
	
	@Column(name = "override_cost", nullable = true, scale=12, precision=2)
	public Double getOverrideUnitCost() {
		return overrideUnitCost;
	}

	public void setOverrideUnitCost(Double overrideUnitCost) {
		this.overrideUnitCost = overrideUnitCost;
	}

	@Column(name = "override_handling_fee", nullable = true, scale=12, precision=2)
	public Double getOverrideFee() {
		return overrideFee;
	}

	public void setOverrideFee(Double overrideFee) {
		this.overrideFee = overrideFee;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCode() {
		return statusCode;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vndr_id", nullable = false)
	public Vendor getVendor() {
		return vendor;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("\n------------------------------------------------------");
		stringBuffer.append("\nAll color indicator:" + this.getAllColorIndicator());
		stringBuffer.append("\nAll size indicator:" + this.getAllSizeIndicator());
		stringBuffer.append("\nAll sku indicator:" + this.getAllSkuIndicator());
		stringBuffer.append("\nUnit cost:" + this.getUnitCost());
		stringBuffer.append("\nOverride Unit cost:" + this.getOverrideUnitCost());
		stringBuffer.append("\nUnit handling fee:" + this.getUnitHandlingFee());
		stringBuffer.append("\nOverride Unit handling fee:" + this.getOverrideFee());
		stringBuffer.append("\nVendor style #:" + this.compositeKeyVIFRStyle.getVendorStyleId());
		stringBuffer.append("\nStyle description:" + this.getStyleDescription());
		stringBuffer.append("\nStatus:" + this.getStatusCode());
		return stringBuffer.toString();
	}

	@Column(name = "UPDATED_BY", nullable = true, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = true)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
	
	@Column(name = "CREATED_BY", nullable = true, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = true)
	public Date getCreatedDate() {
		return this.createdDate;
	}
}
