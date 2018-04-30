package com.belk.car.app.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

@Entity
@Table(name = "CAR_ATTRIBUTE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"CAR_ID", "ATTR_ID" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class CarAttribute extends BaseAuditableModel implements		
         java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4068641848151002886L;
	private long carAttrId;
	private Car car;
	private Attribute attribute;
	private AttributeValueProcessStatus attributeValueProcessStatus;
	private String attrValue;
	private String hasChanged;
	private String isChangeRequired;
	private String statusCd;
	private short displaySeq;
	private Set<OutfitCarAttribute> outfitCarAttributes= new HashSet<OutfitCarAttribute>();
	private Set<DBPromotionCarAttribute> dbPromotionCarAttributes= new HashSet<DBPromotionCarAttribute>();

	public CarAttribute() {
	}

	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_ATTRIBUTE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_ATTRIBUTE_SEQ_GEN", sequenceName = "CAR_ATTRIBUTE_SEQ", allocationSize = 1)
	@Column(name = "CAR_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarAttrId() {
		return this.carAttrId;
	}

	public void setCarAttrId(long carAttrId) {
		this.carAttrId = carAttrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAR_ID", nullable = false)
	public Car getCar() {
		return this.car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTR_ID", nullable = false)
	public Attribute getAttribute() {
		return this.attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	@Column(name = "ATTR_VALUE", length = 2000)
	public String getAttrValue() {
		return this.attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

	@Column(name = "HAS_CHANGED", nullable = false, length = 1)
	public String getHasChanged() {
		return this.hasChanged;
	}

	public void setHasChanged(String hasChanged) {
		this.hasChanged = hasChanged;
	}

	@Column(name = "IS_CHANGE_REQUIRED", nullable = false, length = 1)
	public String getIsChangeRequired() {
		return this.isChangeRequired;
	}

	public void setIsChangeRequired(String isChangeRequired) {
		this.isChangeRequired = isChangeRequired;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	@Column(name = "DISPLAY_SEQ", nullable = false, precision = 4, scale = 0)
	public short getDisplaySeq() {
		return this.displaySeq;
	}

	public void setDisplaySeq(short displaySeq) {
		this.displaySeq = displaySeq;
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
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	/**
	 * @return the attributeValueProcessStatus
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ATTR_VALUE_PROCESS_STATUS_CD")
	public AttributeValueProcessStatus getAttributeValueProcessStatus() {
		return attributeValueProcessStatus;
	}
	
	/**
	 * @param attributeValueProcessStatus the attributeValueProcessStatus to set
	 */
	public void setAttributeValueProcessStatus(AttributeValueProcessStatus attributeValueProcessStatus) {
		this.attributeValueProcessStatus = attributeValueProcessStatus;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carAttribute")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
    public Set<OutfitCarAttribute> getOutfitCarAttributes() {
		return outfitCarAttributes;
	}
	public void setOutfitCarAttributes(Set<OutfitCarAttribute> outfitCarAttributes) {
		this.outfitCarAttributes = outfitCarAttributes;
	}


	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CarAttribute other = (CarAttribute) obj;
        if (this.attribute != other.attribute && (this.attribute == null || !this.attribute.equals(other.attribute))) {
            return false;
        }
        if ((this.attrValue == null) ? (other.attrValue != null) : !this.attrValue.equals(other.attrValue)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + (this.attribute != null ? this.attribute.hashCode() : 0);
        hash = 37 * hash + (this.attrValue != null ? this.attrValue.hashCode() : 0);
        return hash;
    }

    /*For Collection SKU*/
    @Transient
	public boolean isCollectionAttribute() {
		return ("OUTFIT".equals(this.attribute.getAttributeType().getAttrTypeCd())
				&& ("Template_Type".equals(this.attribute.getName())) 
				&& ("COLLECTION".equals(this.getAttrValue())));
	}

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carAttributeForPromo")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public Set<DBPromotionCarAttribute> getDbPromotionCarAttributes() {
		return dbPromotionCarAttributes;
	}


	public void setDbPromotionCarAttributes(
			Set<DBPromotionCarAttribute> dbPromotionCarAttributes) {
		this.dbPromotionCarAttributes = dbPromotionCarAttributes;
	}
   





}
