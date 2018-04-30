package com.belk.car.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

import com.belk.car.util.GenericComparator;

@Entity
@Table(name = "ATTRIBUTE", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class Attribute  extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2667713137037878558L;
	public static String DEFAULT_VALUE  = " ";
	
	private long attrId;
	private AttributeDatatype attributeDatatype;
	private AttributeConfig attributeConfig = new AttributeConfig();
	private AttributeType attributeType = new AttributeType();
	private String name;
	private String description;
	private String statusCd;
	private String blueMartiniAttribute;
	private String isSearchable;
	private String isDisplayable;
	// Variable added for Outfit Management 
	private String isOutfit;
	// Variable added forDeal Based  Management 
		private String isPYG;
	
	// Added for required attribute validation
	private String isRequired;
	
	private Set<ClassAttribute> classAttributes = new HashSet<ClassAttribute>(0);

	//private Set<CarSkuAttribute> carSkuAttributes = new HashSet<CarSkuAttribute>(
	//		0);
	private Set<AttributeLookupValue> attributeLookupValues = new HashSet<AttributeLookupValue>(
			0);
	private Set<ProductTypeAttribute> productTypeAttributes = new HashSet<ProductTypeAttribute>(
			0);
	//private Set<CarAttribute> carAttributes = new HashSet<CarAttribute>(0);
	private Set<DepartmentAttribute> departmentAttributes = new HashSet<DepartmentAttribute>(
			0);

	public Attribute() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTR_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "ATTR_SEQ_GEN", sequenceName = "ATTRIBUTE_SEQ", allocationSize = 1)
	@Column(name = "ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getAttributeId() {
		return this.attrId;
	}

	public void setAttributeId(long attrId) {
		this.attrId = attrId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTR_DATATYPE_CD", nullable = false)
	public AttributeDatatype getDatatype() {
		return this.attributeDatatype;
	}

	public void setDatatype(AttributeDatatype attributeDatatype) {
		this.attributeDatatype = attributeDatatype;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ATTR_CONFIG_ID")
	public AttributeConfig getAttributeConfig() {
		return this.attributeConfig;
	}

	public void setAttributeConfig(AttributeConfig attributeConfig) {
		this.attributeConfig = attributeConfig;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTR_TYPE_CD", nullable = false)
	public AttributeType getAttributeType() {
		return this.attributeType;
	}

	public void setAttributeType(AttributeType attributeType) {
		this.attributeType = attributeType;
	}

	@Column(name = "NAME", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	@Column(name = "BLUE_MARTINI_ATTRIBUTE", nullable = false, length = 200)
	public String getBlueMartiniAttribute() {
		return this.blueMartiniAttribute;
	}

	public void setBlueMartiniAttribute(String blueMartiniAttribute) {
		this.blueMartiniAttribute = blueMartiniAttribute;
	}


	@Column(name = "IS_SEARCHABLE", nullable = false, length = 1)
	public String getIsSearchable() {
		return this.isSearchable;
	}

	public void setIsSearchable(String isSearchable) {
		this.isSearchable = isSearchable;
	}



	@Column(name = "IS_DISPLAYABLE", nullable = false, length = 1)
	public String getIsDisplayable() {
		return this.isDisplayable;
	}

	public void setIsDisplayable(String isDisplayable) {
		this.isDisplayable = isDisplayable;
	}
	
	/* -- Start of code added for Outfit Management -- */
	
	@Column(name = "IS_OUTFIT", nullable = false, length = 1)
	public String getIsOutfit() {
		return isOutfit;
	}

	public void setIsOutfit(String isOutfit) {
		this.isOutfit = isOutfit;
	}
	
	/* -- End of code added for Outfit Management -- */
	
	/* -- Start of code added for Required Field Attribute --*/
	@Column(name = "IS_REQUIRED", nullable = false, length = 1)
	public String getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	
	/* -- End of code added for Required Field Attribute --*/
	
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "attribute")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
          org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<ClassAttribute> getClassAttributes() {
		return this.classAttributes;
	}

	public void setClassAttributes(Set<ClassAttribute> classAttributes) {
		this.classAttributes = classAttributes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "attribute")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
          org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<AttributeLookupValue> getAttributeLookupValues() {
		return this.attributeLookupValues;
	}

	public void setAttributeLookupValues(
			Set<AttributeLookupValue> attributeLookupValues) {
		this.attributeLookupValues = attributeLookupValues;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "attribute")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
          org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<ProductTypeAttribute> getProductTypeAttributes() {
		return this.productTypeAttributes;
	}

	public void setProductTypeAttributes(
			Set<ProductTypeAttribute> productTypeAttributes) {
		this.productTypeAttributes = productTypeAttributes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "attribute")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
          org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<DepartmentAttribute> getDepartmentAttributes() {
		return this.departmentAttributes;
	}

	public void setDepartmentAttributes(
			Set<DepartmentAttribute> departmentAttributes) {
		this.departmentAttributes = departmentAttributes;
	}
	
	@Transient
	public List<DepartmentAttribute> getDepartments() {
		Comparator lookupComparator = new GenericComparator("department.deptCd");
		ArrayList<DepartmentAttribute> lookupList = new ArrayList<DepartmentAttribute>();
		if (this.getDepartmentAttributes() != null && !this.getDepartmentAttributes().isEmpty()){
			lookupList.addAll(this.getDepartmentAttributes()) ;
			Collections.sort(lookupList, lookupComparator);
		}
		return lookupList ;
	}
	
	@Transient
	public List<AttributeLookupValue> getValues() {
		Comparator lookupComparator = new GenericComparator("value");
		ArrayList<AttributeLookupValue> lookupList = new ArrayList<AttributeLookupValue>();
		if (this.getAttributeLookupValues() != null && !this.getAttributeLookupValues().isEmpty()) {
			lookupList.addAll(this.getAttributeLookupValues()) ;
			Collections.sort(lookupList, lookupComparator);
		}
		return lookupList ;
	}
	
	/**
	 * Adds a productType for the attribute
	 * 
	 * @param pAttr
	 *            the fully instantiated ProductType Attribute
	 */
	public void associateWithProductType(ProductTypeAttribute pAttr) {
		this.productTypeAttributes.add(pAttr);
	}
	
	/**
	 * Adds a classification for the attribute
	 * 
	 * @param cClass
	 *            the fully instantiated Classification Attribute
	 */
	public void associateWithClassification(ClassAttribute cAttr) {
		this.classAttributes.add(cAttr);
	}
	
	/**
	 * Adds a departmentAttribute for the attribute
	 * 
	 * @param dept
	 *            the fully instantiated departmentAttribute
	 */
	public void associateWithDepartment(DepartmentAttribute dept) {
		this.departmentAttributes.add(dept);
	}
	
	@Transient
	public String getAttributeIdAsString() {
		return String.valueOf(attrId);
	}

	@Transient
	public boolean isActive() {
		return Status.ACTIVE.equals(statusCd) ;
	}
	
	@Transient
	public boolean isContentAttribute() {
		return "CONTENT".equals(this.getAttributeType().getAttrTypeCd());
	}

	@Transient
	public boolean isSkuAttribute() {
		return "SKU".equals(this.getAttributeType().getAttrTypeCd());
	}
	
	@Transient
	public boolean isFacetAttribute() {
		return "FACET".equals(this.getAttributeType().getAttrTypeCd());
	}

    @Override
    public boolean equals(Object obj) {
    	
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof Attribute)) {
            return false;
        }
        final Attribute other = (Attribute) obj;
        
        if (this.attrId != other.attrId) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (int) (this.attrId ^ (this.attrId >>> 32));
        return hash;
    }

    public String toString(){
    	return name + Long.toString(attrId);
    }

    @Column(name = "IS_PYG", nullable = false, length = 1)
	public String getIsPYG() {
		return isPYG;
	}

	public void setIsPYG(String isPYG) {
		this.isPYG = isPYG;
	}

}
