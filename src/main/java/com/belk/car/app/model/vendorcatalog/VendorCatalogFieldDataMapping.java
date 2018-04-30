package com.belk.car.app.model.vendorcatalog;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.BaseAuditableModel;

/**
 * This is the model class for table name VENDOR_CATALOG_FLD_DATA_MAPPING
 * 
 * @version 1.0 14 Jan 2010
 * @author afusy07	 : Priyanka Gadia
 */
@Entity
@Table(name = "VNDR_CATL_FLD_DATA_MAPPING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorCatalogFieldDataMapping extends BaseAuditableModel implements
		java.io.Serializable {

	private static final long serialVersionUID = 3162057662004858738L;
	private CompositeKeyForDataFldMapping compositeKey;
	private AttributeLookupValue blkAttrLookupValue;

	public VendorCatalogFieldDataMapping() {
		super();
	}

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "vendorCatalogtTemplateId", column = @Column(name = "VENDOR_CATALOG_TMPL_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "vendorCatalogHeaderFieldName", column = @Column(name = "VNDR_CATL_HDR_FLD_NAME", nullable = false)),
			@AttributeOverride(name = "vendorCatalogFieldValue", column = @Column(name = "VNDR_CATL_FIELD_VALUE", nullable = false)) })
	public CompositeKeyForDataFldMapping getCompositeKey() {
		return compositeKey;
	}
	
	public void setCompositeKey(CompositeKeyForDataFldMapping compositeKey) {
		this.compositeKey = compositeKey;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "BELK_ATTR_LOOKUP_VALUE_ID", nullable = false)
	public AttributeLookupValue getBlkAttrLookupValue() {
		return blkAttrLookupValue;
	}

	public void setBlkAttrLookupValue(AttributeLookupValue blkAttrLookupValue) {
		this.blkAttrLookupValue = blkAttrLookupValue;
	}

}
