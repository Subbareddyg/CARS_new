package com.belk.car.app.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "PIM_ATTRIBUTE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class PIMAttribute  extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 9122113845142242864L;
	long pimAttrId;
	String name;
	String descr;
	String statusCd="ACTIVE";
	String isPimAttrDisplayble="N";
	Date displaybleDate = new Date();
	String bmAttrName;
	
	

    private Set<VendorStylePIMAttribute> vendorStylePIMAttribute;

	public PIMAttribute(){}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PIM_ATTRIBUTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "PIM_ATTRIBUTE_SEQ_GEN", sequenceName = "PIM_ATTRIBUTE_SEQ", allocationSize = 1)
	@Column(name = "PIM_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getPimAttrId() {
		return pimAttrId;
	}
	public void setPimAttrId(long pimAttrId) {
		this.pimAttrId = pimAttrId;
	}
	
	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "DESCR", nullable = false, length = 100)
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
	@Column(name = "STATUS_CD", nullable = false, length = 100)
	public String getStatusCd() {
		return statusCd;
	}
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
	
	@Column(name = "IS_PIM_ATTR_DISPLAYABLE", nullable = false, length = 1)
	public String getIsPimAttrDisplayble() {
		return isPimAttrDisplayble;
	}
	public void setIsPimAttrDisplayble(String isPimAttrDisplayble) {
		this.isPimAttrDisplayble = isPimAttrDisplayble;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATE_DISPLAYABLE", length = 7)
	public Date getDisplaybleDate() {
		return displaybleDate;
	}
	public void setDisplaybleDate(Date displaybleDate) {
		this.displaybleDate = displaybleDate;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="pimAttribute")
	public Set<VendorStylePIMAttribute> getVendorStylePIMAttribute() {
		return vendorStylePIMAttribute;
	}
	@Column(name = "BM_ATTR_NAME", nullable = true, length = 50)
	public String getBmAttrName() {
        return bmAttrName;
    }

    public void setBmAttrName(String bmAttrName) {
        this.bmAttrName = bmAttrName;
    }
    
	public void setVendorStylePIMAttribute(
			Set<VendorStylePIMAttribute> vendorStylePIMAttribute) {
		this.vendorStylePIMAttribute = vendorStylePIMAttribute;
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
	
    @Override
    public boolean equals(Object obj) {
    	if(this== null && obj==null){
    		return true;
    	}
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof PIMAttribute)) {
            return false;
        }
        final PIMAttribute other = (PIMAttribute) obj;
        if (this.pimAttrId != other.getPimAttrId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (int) (this.pimAttrId ^ (this.pimAttrId >>> 43));
        return hash;
    }
	
}
