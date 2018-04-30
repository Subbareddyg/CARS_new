package com.belk.car.app.model;


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;



@Entity
@Table(name = "VENDOR_SKU_DELETE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class VendorSkuDelete extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8223048692009445792L;
	private long skuDelId;
	private long carSkuId;
	private long carId;
	private String belkUpc;
	private String statusCd;
	
	private Set<SkuAttributeDelete> skuAttributeDelete = new HashSet<SkuAttributeDelete>(0);

	public VendorSkuDelete() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDOR_SKU_DELETE_GEN")
	@javax.persistence.SequenceGenerator(name = "VENDOR_SKU_DELETE_GEN", sequenceName = "VENDOR_SKU_DELETE_SEQ", allocationSize = 1)
	@Column(name = "SKU_DELETE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getSkuDelId() {
		return skuDelId;
	}

	public void setSkuDelId(long skuDelId) {
		this.skuDelId = skuDelId;
	}

	@Column(name = "CAR_SKU_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarSkuId() {
		return this.carSkuId;
	}

	public void setCarSkuId(long carSkuId) {
		this.carSkuId = carSkuId;
	}
	
	@Column(name = "CAR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarId() {
		return this.carId;
	}

	public void setCarId(long carId) {
		this.carId = carId;
	}

	

	@Column(name = "BELK_UPC", length = 20)
	public String getBelkUpc() {
		return this.belkUpc;
	}

	public void setBelkUpc(String belkUpc) {
		this.belkUpc = belkUpc;
	}

	

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name="CAR_SKU_ID")
	public Set<SkuAttributeDelete> getSkuAttributeDelete() {
		return skuAttributeDelete;
	}


	public void setSkuAttributeDelete(Set<SkuAttributeDelete> skuAttributeDelete) {
		this.skuAttributeDelete = skuAttributeDelete;
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

	
}
