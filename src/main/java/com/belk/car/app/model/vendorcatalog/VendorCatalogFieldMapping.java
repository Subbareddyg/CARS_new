/**
 * This is the model class for table name VENDOR_CATALOG_FLD_MAPPING
 * 
 * @version 1.0 14 Jan 2010
 * @author afusxs7 : Priyanka Gadia
 */
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;


@Entity
@Table(name = "VENDOR_CATALOG_FLD_MAPPING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalogFieldMapping extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -1483109279323910868L;
	private CompositeKeyForDataMapping compositeKey;
	private String blueMartiniAttrName;
	private VendorCatalogHeader vendorCatalogHeader;

	public VendorCatalogFieldMapping() {
		super();
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "vendorCatalogtTemplateId", column = @Column(name = "VENDOR_CATALOG_TMPL_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "vendorCatalogHeaderFieldName", column = @Column(name = "VNDR_CATL_HDR_FLD_NAME", nullable = false)) })
	public CompositeKeyForDataMapping getCompositeKey() {
		return compositeKey;
	}
	
	public void setCompositeKey(CompositeKeyForDataMapping compositeKey) {
		this.compositeKey = compositeKey;
	}

	@Column(name = "BLUE_MARTINI_ATTRIBUTE", nullable = false)
	public String getBlueMartiniAttrName() {
		return blueMartiniAttrName;
	}
	
	
	public void setBlueMartiniAttrName(String blueMartiniAttrName) {
		this.blueMartiniAttrName = blueMartiniAttrName;
	}

	public void setVendorCatalogHeader(VendorCatalogHeader vendorCatalogHeader) {
		this.vendorCatalogHeader = vendorCatalogHeader;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_CATALOG_HEADER_ID", nullable = false)
	public VendorCatalogHeader getVendorCatalogHeader() {
		return vendorCatalogHeader;
	}

}
