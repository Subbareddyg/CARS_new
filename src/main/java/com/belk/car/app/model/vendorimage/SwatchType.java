/**
 * @author afusy12
 */
package com.belk.car.app.model.vendorimage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.belk.car.app.model.BaseAuditableModel;

/**
 * @author afusyq3
 *
 */
@Entity
@Table(name = "SWATCH_TYPE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class SwatchType extends BaseAuditableModel implements java.io.Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8172983572526654745L;
	private long swatchId;
	private String swatchType;
	private String swatchTypeCd;
	
		
	
	@Column(name = "SWATCH_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getSwatchId() {
		return swatchId;
	}

	public void setSwatchId(long swatchId) {
		this.swatchId = swatchId;
	}

	@Column(name = "SWATCH_TYPE", nullable = false, length = 10)
	public String getSwatchType() {
		return swatchType;
	}

	
	public void setSwatchType(String swatchType) {
		this.swatchType = swatchType;
	}


		
	@Column(name = "SWATCH_TYPE_CD", nullable = false, length = 2)
	public String getSwatchTypeCd() {
		return swatchTypeCd;
	}

	public void setSwatchTypeCd(String swatchTypeCd) {
		this.swatchTypeCd = swatchTypeCd;
	}


	
}
