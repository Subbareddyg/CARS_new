package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author afusya2
 *
 */

@Embeddable
public class CompositeKeyForVndrCatlDept implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1219337488655437653L;
	private VendorCatalog vendorCatalog;
	private Long deptId;
	
	
	/**
	 * 
	 */
	public CompositeKeyForVndrCatlDept() {
		super();
	}
	
	@ManyToOne
	@JoinColumn(name="vendor_catalog_id",nullable=false)
	public VendorCatalog getVendorCatalog() {
		return vendorCatalog;
	}

	public void setVendorCatalog(VendorCatalog vendorCatalog) {
		this.vendorCatalog = vendorCatalog;
	}

	@Column(name = "dept_id", nullable = false, length=12)
	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
}
