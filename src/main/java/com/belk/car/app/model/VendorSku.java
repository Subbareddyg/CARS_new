package com.belk.car.app.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.belk.car.util.GenericComparator;

@Entity
@Table(name = "VENDOR_SKU")
//, uniqueConstraints = @UniqueConstraint(columnNames = "CAR_ID, BELK_UPC"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorSku extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8223048631909445792L;
	private long carSkuId;
	private Car car;
	private String descr;
	private String name;
	private String longSku;
	private String belkUpc;
	private String vendorUpc;
	private String belkSku;
	private String statusCd;
	private String colorCode;
	private String sizeCode;
	private String colorName;
	private String sizeName;
	private String parentUpc;
	private String setFlag="N";
	private double retailPrice=0;
	private String idbSizeName;	//CARS Size Conversion Issue - Size name overwritten by resync size job-->	
	private VendorStyle vendorStyle;
	private String firstSuperColor;
	private String facetSize1="";
	private String facetSize2="";
	private String facetSize3="";
	private String facetSubSize1="";
	private String facetSubSize2="";
	private ColorMappingMaster colorRule;
	private SizeConversionMaster sizeRule;
	private Long orinNumber;
	private Set<CarSkuAttribute> carSkuAttributes = new HashSet<CarSkuAttribute>(0);
        
    private Set<VendorSkuPIMAttribute> skuPIMAttributes = new HashSet<VendorSkuPIMAttribute>(0);

    

	public VendorSku() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDOR_SKU_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "VENDOR_SKU_SEQ_GEN", sequenceName = "VENDOR_SKU_SEQ", allocationSize = 1)
	@Column(name = "CAR_SKU_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarSkuId() {
		return this.carSkuId;
	}

	public void setCarSkuId(long carSkuId) {
		this.carSkuId = carSkuId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CAR_ID", nullable = false)
	public Car getCar() {
		return this.car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	@Column(name = "ITEM_NAME", length = 200, nullable = true)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", length = 2000)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Column(name = "LONG_SKU", length = 20)
	public String getLongSku() {
		return this.longSku;
	}

	public void setLongSku(String longSku) {
		this.longSku = longSku;
	}

	@Column(name = "BELK_UPC", length = 20)
	public String getBelkUpc() {
		return this.belkUpc;
	}

	public void setBelkUpc(String belkUpc) {
		this.belkUpc = belkUpc;
	}

	@Column(name = "VENDOR_UPC", length = 20)
	public String getVendorUpc() {
		return this.vendorUpc;
	}

	public void setVendorUpc(String vendorUpc) {
		this.vendorUpc = vendorUpc;
	}

	@Column(name = "BELK_SKU", length = 20)
	public String getBelkSku() {
		return this.belkSku;
	}

	public void setBelkSku(String belkSku) {
		this.belkSku = belkSku;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "vendorSku")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public Set<CarSkuAttribute> getCarSkuAttributes() {
		return this.carSkuAttributes;
	}

	public void setCarSkuAttributes(Set<CarSkuAttribute> carSkuAttributes) {
		this.carSkuAttributes = carSkuAttributes;
	}

	@Transient
	public Map<Long, CarSkuAttribute> getAttributeMap() {
		Set<CarSkuAttribute> skuAttrList = this.getCarSkuAttributes();
		Map<Long, CarSkuAttribute> skuAttrMap = new HashMap<Long, CarSkuAttribute>();
		if (skuAttrList != null && !skuAttrList.isEmpty()) {
			for (CarSkuAttribute skuAttr : skuAttrList) {
				skuAttrMap.put(new Long(skuAttr.getAttribute().getAttributeId()), skuAttr);
			}
		}
		return skuAttrMap;
	}

	@Transient
	public List<CarSkuAttribute> getAttributes() {
		ArrayList<CarSkuAttribute> attrList = null;
		if (getCarSkuAttributes() != null && !getCarSkuAttributes().isEmpty()) {
			attrList = new ArrayList<CarSkuAttribute>(carSkuAttributes);
			Comparator gc = new GenericComparator("attribute.attributeConfig.displayName");
			Collections.sort(attrList, gc);
		} else {
			attrList = new ArrayList<CarSkuAttribute>();
		}
		return attrList;
	}

	@Transient
	public CarSample getSample() {
		CarSample retVal = null;
		if (this.getCar().getCarSamples() == null) {
			return null;
		}
		for (CarSample sample : this.getCar().getCarSamples()) {
			if (sample.getSample() != null && sample.getSample().getAssociatedWithSku() != null
					&& sample.getSample().getAssociatedWithSku().equals(this.getBelkSku())) {
				return sample;
			}
		}
		return retVal;
	}

	@Column(name = "COLOR_CODE", nullable = true, length = 3)
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	@Column(name = "SIZE_CODE", nullable = true, length = 5)
	public String getSizeCode() {
		return sizeCode;
	}

	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}

	@Column(name = "COLOR_NAME", nullable = true, length = 100)
	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	@Column(name = "SIZE_NAME", nullable = true, length = 100)
	public String getSizeName() {
		return sizeName;
	}

	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	
	//CARS Size Conversion Issue - Size name overwritten by resync size job-->
	@Column(name = "IDB_SIZE_NAME", nullable = true, length = 100)	
	public String getIdbSizeName() {
		return idbSizeName;
	}

	public void setIdbSizeName(String idbSizeName) {
		this.idbSizeName = idbSizeName;
	}

	@Column(name = "PARENT_BELK_UPC", nullable = true, length = 20)
	public String getParentUpc() {
		return parentUpc;
	}

	public void setParentUpc(String parentUpc) {
		this.parentUpc = parentUpc;
	}

	@Column(name = "IS_SET_FLAG", nullable = false, length = 1)
	public String getSetFlag() {
		return setFlag;
	}

	public void setSetFlag(String setFlag) {
		this.setFlag = setFlag;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_STYLE_ID", nullable = false)
	public VendorStyle getVendorStyle() {
		return vendorStyle;
	}

	public void setVendorStyle(VendorStyle vendorStyle) {
		this.vendorStyle = vendorStyle;
	}
	
	@Column(name = "RETAIL_PRICE", nullable = true, precision = 6, scale = 2)
	public double getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(double retailPrice) {
		this.retailPrice = retailPrice;
	}
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COLOR_RULE_ID", nullable = true)
	public ColorMappingMaster getColorRule() {
		return colorRule;
	}

	public void setColorRule(ColorMappingMaster colorRule) {
		this.colorRule = colorRule;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SIZE_RULE_ID", nullable = true)
	public SizeConversionMaster getSizeRule() {
		return sizeRule;
	}

	public void setSizeRule(SizeConversionMaster sizeRule) {
		this.sizeRule = sizeRule;
	}
	
	 @Transient
	    public String getFirstSuperColor() {
			return firstSuperColor;
		}

	    @Transient
	    public void setFirstSuperColor(String firstSuperColor) {
			this.firstSuperColor = firstSuperColor;
		}

	    @Transient
		public String getFacetSize1() {
			return facetSize1;
		}
	    @Transient
		public void setFacetSize1(String facetSize1) {
			this.facetSize1 = facetSize1;
		}
	    @Transient
		public String getFacetSize2() {
			return facetSize2;
		}
	    @Transient
		public void setFacetSize2(String facetSize2) {
			this.facetSize2 = facetSize2;
		}
	    @Transient
		public String getFacetSize3() {
			return facetSize3;
		}
	    @Transient
		public void setFacetSize3(String facetSize3) {
			this.facetSize3 = facetSize3;
		}
	    @Transient
		public String getFacetSubSize1() {
			return facetSubSize1;
		}
	    @Transient
		public void setFacetSubSize1(String facetSubSize1) {
			this.facetSubSize1 = facetSubSize1;
		}
	    @Transient
		public String getFacetSubSize2() {
			return facetSubSize2;
		}
	    @Transient
		public void setFacetSubSize2(String facetSubSize2) {
			this.facetSubSize2 = facetSubSize2;
		}
	    @Transient
		public static long getSerialversionuid() {
			return serialVersionUID;
		}
	    
	    @Column(name = "ORIN_NUM", nullable = true, precision = 12, scale = 0)
		public Long getOrinNumber() {
			return orinNumber;
		}
		public void setOrinNumber(Long orinNumber) {
			this.orinNumber = orinNumber;
		}
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name="VENDOR_SKU_ID")
    public Set<VendorSkuPIMAttribute> getSkuPIMAttributes() {
        return skuPIMAttributes;
    }

    public void setSkuPIMAttributes(Set<VendorSkuPIMAttribute> skuPIMAttributes) {
        this.skuPIMAttributes = skuPIMAttributes;
    }
}
