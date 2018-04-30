package com.belk.car.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "VENDORSTYLE_PIM_ATTRIBUTE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class VendorStylePIMAttribute extends  BaseAuditableModel implements Serializable{


	private static final long serialVersionUID = 732234764376864L;
	
	VendorStylePIMAttributeId id;
	VendorStyle vendorStyle;
	PIMAttribute pimAttribute;
	String value;
	
	public VendorStylePIMAttribute(){}
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "vendorStyleId", column = @Column(name = "VENDOR_STYLE_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "pimAttrId", column = @Column(name = "PIM_ATTR_ID", nullable = false, precision = 12, scale = 0)) })
	public VendorStylePIMAttributeId getId() {
		return this.id;
	}

	public void setId(VendorStylePIMAttributeId id) {
	this.id = id;
	}
	
	@Column(name = "PIM_ATTR_VALUE", nullable = true, length = 100)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_STYLE_ID", nullable = false, insertable = false, updatable = false)
	public VendorStyle getVendorStyle() {
		return vendorStyle;
	}

	public void setVendorStyle(VendorStyle vendorStyle) {
		this.vendorStyle = vendorStyle;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PIM_ATTR_ID", nullable = false, insertable = false, updatable = false)
	public PIMAttribute getPimAttribute() {
		return pimAttribute;
	}

	public void setPimAttribute(PIMAttribute pimAttribute) {
		this.pimAttribute = pimAttribute;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
	
	
	//used instanceOf instead of comparing class names, as hibernate modifies the class names of object fetched from DB
    @Override
    public boolean equals(Object obj) {
    	if(this== null && obj==null){
    		return true;
    	}
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof VendorStylePIMAttribute)) {
            return false;
        }
        final VendorStylePIMAttribute other = (VendorStylePIMAttribute) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    
	@Override
	public int hashCode() {
		int hash = 3;
        hash = 73 * hash + (int) (this.getId().pimAttrId ^ (this.getId().pimAttrId>>> 43));
        hash = 73 * hash + (int) (this.getId().vendorStyleId ^ (this.getId().vendorStyleId>>> 43));
        return hash;
	}

}
