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

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "VENDOR_STYLE", uniqueConstraints = @UniqueConstraint(columnNames = {
		"VENDOR_NUMBER", "VENDOR_STYLE_NUMBER" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorStyle extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7695121996561045293L;
	private long vendorStyleId;
	private Vendor vendor;
	private Classification classification;
	private String descr;
	private String vendorNumber;
	private String vendorStyleNumber;
	private String vendorStyleName;
	private String statusCd;
	private String alternateCategory ;
	private VendorStyleType vendorStyleType ;
	private VendorStyle parentVendorStyle ;

	private ProductType productType ;
	private Set<VendorSku> vendorSkus;
	private String globalStartDate;
	private Set<VendorStylePIMAttribute> vendorStylePIMAttribute = new HashSet<VendorStylePIMAttribute>(0);
	private Long orinNumber;
	private Long groupId;
	private String isDefault;
	
	public VendorStyle() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDOR_STYLE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "VENDOR_STYLE_SEQ_GEN", sequenceName = "VENDOR_STYLE_SEQ", allocationSize = 1)
	@Column(name = "VENDOR_STYLE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getVendorStyleId() {
		return this.vendorStyleId;
	}

	public void setVendorStyleId(long vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_ID", nullable = false)
	public Vendor getVendor() {
		return this.vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CLASS_ID", nullable = false)
	public Classification getClassification() {
		return this.classification;
	}

	public void setClassification(Classification classification) {
		this.classification = classification;
	}

	@Column(name = "DESCR", length = 2000)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		if (descr != null && descr.length()>2000)
			descr = StringUtils.substring(descr, 0, 2000);
		this.descr = descr;
	}

	@Column(name = "VENDOR_NUMBER", nullable = false, length = 50)
	public String getVendorNumber() {
		return this.vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = StringUtils.trim(vendorNumber);
	}

	@Column(name = "VENDOR_STYLE_NUMBER", nullable = false, length = 100)
	public String getVendorStyleNumber() {
		return this.vendorStyleNumber;
	}

	public void setVendorStyleNumber(String vendorStyleNumber) {
		this.vendorStyleNumber = StringUtils.trim(vendorStyleNumber);
	}

	@Column(name = "VENDOR_STYLE_NAME", nullable = false, length = 500)
	public String getVendorStyleName() {
		return this.vendorStyleName;
	}

	public void setVendorStyleName(String vendorStyleName) {
		if (StringUtils.isEmpty(vendorStyleName)) {
			vendorStyleName = " ";
		} else if(vendorStyleName.length()>150) {
			vendorStyleName = StringUtils.substring(vendorStyleName, 0, 150);
		}
		this.vendorStyleName = vendorStyleName;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
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

	/*
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "vendorStyle")
	public Set<Car> getCars() {
		return this.cars;
	}

	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}
	*/

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_TYPE_ID", nullable = true)
	public ProductType getProductType() {
		return productType;
	}

	public void setProductType(ProductType productType) {
		this.productType = productType;
	}

	@Column(name = "ALT_CATEGORY", nullable = true, length = 100)
	public String getAlternateCategory() {
		return alternateCategory;
	}

	public void setAlternateCategory(String alternateCategory) {
		this.alternateCategory = alternateCategory;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_STYLE_TYPE_CD", nullable = false)
	public VendorStyleType getVendorStyleType() {
		return vendorStyleType;
	}

	public void setVendorStyleType(VendorStyleType vendorStyleType) {
		this.vendorStyleType = vendorStyleType;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "PARENT_VENDOR_STYLE_ID", nullable = true)
	public VendorStyle getParentVendorStyle() {
		return parentVendorStyle;
	}

	public void setParentVendorStyle(VendorStyle parentVendorStyle) {
		this.parentVendorStyle = parentVendorStyle;
	}
	
	public void setVendorSkus(Set<VendorSku> vendorSkus) {
		this.vendorSkus = vendorSkus;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="vendorStyle")
	public Set<VendorSku> getVendorSkus() {
		return vendorSkus;
	}

	@Transient
	public boolean isPattern() {
		return (vendorStyleType != null && !VendorStyleType.PRODUCT.equals(vendorStyleType.getCode()) && !VendorStyleType.OUTFIT.equals(vendorStyleType.getCode()) && !VendorStyleType.PYG.equals(vendorStyleType.getCode()));
	}
	
    @Transient
    public boolean isConsolidatedProduct() {
        return (vendorStyleType != null && VendorStyleType.PATTERN_CONS_VS.equals(vendorStyleType.getCode()));
    }

	/**
	 * This method used to get global start date of skus.
	 * @return  It return date string.
	 */
	@Transient
	public String getGlobalStartDate() {
		return globalStartDate;
	}

	/**
	 * This method is used to set the Global start date of skus.
	 * 
	 * @param globalStartDate passing date string.
	 */
	@Transient
	public void setGlobalStartDate(String globalStartDate) {
		this.globalStartDate = globalStartDate;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="vendorStyle")
	public Set<VendorStylePIMAttribute> getVendorStylePIMAttribute() {
		return vendorStylePIMAttribute;
	}

	public void setVendorStylePIMAttribute(
			Set<VendorStylePIMAttribute> vendorStylePIMAttribute) {
		this.vendorStylePIMAttribute = vendorStylePIMAttribute;
	}

	@Column(name = "ORIN_NUM", nullable = true, precision = 12, scale = 0)
	public Long getOrinNumber() {
		return orinNumber;
	}

	public void setOrinNumber(Long orinNumber) {
		this.orinNumber = orinNumber;
	}
	
	@Column(name = "GROUP_ID", nullable = true, precision = 12, scale = 0)
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	@Column(name = "IS_DEFAULT", nullable = true, length = 10)
	public String getIsDefault() {
		return this.isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
}
