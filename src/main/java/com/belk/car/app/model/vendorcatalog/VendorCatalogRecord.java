
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "VENDOR_CATALOG_RECORD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalogRecord extends BaseAuditableModel
		implements
			java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -329936689819914449L;

	private CompositeKeyForVndrCatRecord compositeKey;
	private String vendorCatalogFieldValue;

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "vendorCatalogId", column = @Column(name = "VENDOR_CATALOG_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "catalogHeaderNum", column = @Column(name = "VNDR_CATL_HDR_FLD_NUM", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "recordNumber", column = @Column(name = "VNDR_CATL_RECORD_NUM", nullable = false, precision = 12, scale = 0)) })
	public CompositeKeyForVndrCatRecord getCompositeKey() {
		return compositeKey;
	}

	public void setCompositeKey(CompositeKeyForVndrCatRecord compositeKey) {
		this.compositeKey = compositeKey;
	}

	@Column(name = "VNDR_CATL_FIELD_VALUE", nullable = true, length = 50)
	public String getVendorCatalogFieldValue() {
		return vendorCatalogFieldValue;
	}

	public void setVendorCatalogFieldValue(String vendorCatalogFieldValue) {
		this.vendorCatalogFieldValue = vendorCatalogFieldValue;
	}

}
