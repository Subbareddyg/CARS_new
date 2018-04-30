/**
 * 
 */
package com.belk.car.app.model.oma;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * @author afusy01
 *
 */

@Entity
@Table(name = "vifr_style_sku")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VFIRStyleSku extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4666215410183037110L;
	
	public static final String ACTIVE = "ACTIVE";
	public static final String INACTIVE = "INACTIVE";
	
	private CompositeKeyVFIRStylesku compositeKeyVFIRStylesku;
	private String skuexceptionInd;
	private Double unitCost;
	private Double unitHandlingfee;
	private Double overrideCost;
	private Double overrideFee;
	private String statusCode;
	private String color;
	private String sizeDescription;
	
	/**
	 * 
	 */
	public VFIRStyleSku() {
		super();
	}

	@EmbeddedId
	public CompositeKeyVFIRStylesku getCompositeKeyVFIRStylesku() {
		return compositeKeyVFIRStylesku;
	}

	public void setCompositeKeyVFIRStylesku(
			CompositeKeyVFIRStylesku compositeKeyVFIRStylesku) {
		this.compositeKeyVFIRStylesku = compositeKeyVFIRStylesku;
	}

	@Column(name = "sku_exc_ind", nullable = false, length=1)
	public String getSkuexceptionInd() {
		return skuexceptionInd;
	}

	public void setSkuexceptionInd(String skuexceptionInd) {
		this.skuexceptionInd = skuexceptionInd;
	}

	@Column(name = "unit_cost", nullable = true, scale=12, precision=2)
	public Double getUnitCost() {
		return unitCost;
	}

	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	@Column(name = "unit_handling_fee", nullable = true, scale=12, precision=2)
	public Double getUnitHandlingfee() {
		return unitHandlingfee;
	}

	public void setUnitHandlingfee(Double unitHandlingfee) {
		this.unitHandlingfee = unitHandlingfee;
	}

	@Column(name = "override_cost", nullable = true, scale=12, precision=2)
	public Double getOverrideCost() {
		return overrideCost;
	}

	public void setOverrideCost(Double overrideCost) {
		this.overrideCost = overrideCost;
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
	@Column(name = "status_cd", nullable = false, length=20)
	public String getStatusCode() {
		return statusCode;
	}
	
	@Column(name = "color", nullable = true, length=50)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Column(name = "size_descr", nullable = true, length=50)
	public String getSizeDescription() {
		return sizeDescription;
	}

	public void setSizeDescription(String sizeDescription) {
		this.sizeDescription = sizeDescription;
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
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n---------------------------------------\n");
		stringBuilder.append("\nVendor style #:");
		stringBuilder.append(this.getCompositeKeyVFIRStylesku().getVendorStyleId());
		stringBuilder.append("\nColor:");
		stringBuilder.append(this.getColor());
		stringBuilder.append("\nSize:");
		stringBuilder.append(this.getSizeDescription());
		stringBuilder.append("\nSku exception indicator:");
		stringBuilder.append(this.getSkuexceptionInd());
		stringBuilder.append("\nOverride cost:");
		stringBuilder.append(this.getOverrideCost());
		stringBuilder.append("\nOverride fee:");
		stringBuilder.append(this.getOverrideFee());
		return stringBuilder.toString();
	}
}
