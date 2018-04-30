package com.belk.car.app.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "COLLECTION_SKUS", uniqueConstraints = @UniqueConstraint(columnNames = {
		"PROD_CODE", "SKU_CODE" }))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class CollectionSkus extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2551321316129221093L;
	/**
	 * 
	 */
	private CollectionSkusId id;
	private String prodCode;
	private String skuCode;

	public CollectionSkus() {
	}
	
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "skuCode", column = @Column(name = "SKU_CODE", nullable = false, length = 2000)),
			@AttributeOverride(name = "prodCode", column = @Column(name = "PROD_CODE", nullable = false, length = 2000)) })
	public CollectionSkusId getId() {
		return this.id;
	}

	public void setId(CollectionSkusId id) {
		this.id = id;
	}
	
	@Column(name = "SKU_CODE", length = 2000, insertable = false, updatable = false)
	public String getSkuCode() {
		return this.skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	
	
	@Column(name = "PROD_CODE", length = 2000, insertable = false, updatable = false)
	public String getProdCode() {
		return this.prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
}
