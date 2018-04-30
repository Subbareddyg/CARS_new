/**
 * This is the model class for table name VENDOR_CATALOG_HEADER
 * 
 * @version 1.0 14 Jan  2010
 * @author afusy07	 : Priyanka Gadia
 */

package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "VENDOR_CATALOG_HEADER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalogHeader extends BaseAuditableModel
		implements
			java.io.Serializable {

	private static final long serialVersionUID = -7355720595406811828L;
	private long vendorCatalogFieldNum;
	private VendorCatalog vendorCatalogID;
	private String vendorCatalogHeaderFieldName;
	private Long vendorCatalogHeaderId;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDOR_CATALOG_FIELD_GEN")
	@javax.persistence.SequenceGenerator(name = "VENDOR_CATALOG_FIELD_GEN", sequenceName = " SEQ_VENDOR_CATALOG_FIELD_ID", allocationSize = 1)
	@Column(name = "VENDOR_CATALOG_HEADER_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getVendorCatalogHeaderId() {
		return vendorCatalogHeaderId;
	}

	public void setVendorCatalogHeaderId(Long vendorCatalogHeaderId) {
		this.vendorCatalogHeaderId = vendorCatalogHeaderId;
	}

	@Column(name = "VNDR_CATL_HDR_FLD_NUM", unique = true, nullable = false, precision = 12, scale = 0)
	public long getVendorCatalogFieldNum() {
		return vendorCatalogFieldNum;
	}

	public void setVendorCatalogFieldNum(long vendorCatalogFieldNum) {
		this.vendorCatalogFieldNum = vendorCatalogFieldNum;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "VENDOR_CATALOG_ID", nullable = false)
	public VendorCatalog getVendorCatalogID() {
		return vendorCatalogID;
	}

	public void setVendorCatalogID(VendorCatalog vendorCatalogID) {
		this.vendorCatalogID = vendorCatalogID;
	}

	@Column(name = "VNDR_CATL_HDR_FLD_NAME", nullable = false)
	public String getVendorCatalogHeaderFieldName() {
		return vendorCatalogHeaderFieldName;
	}

	public void setVendorCatalogHeaderFieldName(
			String vendorCatalogHeaderFieldName) {
		this.vendorCatalogHeaderFieldName = vendorCatalogHeaderFieldName;
	}

}
