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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "PRODUCT_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class ProductType extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8400785034137099499L;
	private long productTypeId;
	private String name;
	private String descr;
	private String statusCd;
	private Set<ProductTypeAttribute> productTypeAttributes = new HashSet<ProductTypeAttribute>(0);
	private Set<Classification> classifications = new HashSet<Classification>(0);
	private ProductGroup productGroup;

	public ProductType() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_TYPE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "PRODUCT_TYPE_SEQ_GEN", sequenceName = "PRODUCT_TYPE_SEQ", allocationSize = 1)
	@Column(name = "PRODUCT_TYPE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getProductTypeId() {
		return this.productTypeId;
	}

	public void setProductTypeId(long productTypeId) {
		this.productTypeId = productTypeId;
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
		return this.descr;
	}

	public void setDescription(String descr) {
		this.descr = descr;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "productType")
	public Set<ProductTypeAttribute> getProductTypeAttributes() {
		return this.productTypeAttributes;
	}

	public void setProductTypeAttributes(
			Set<ProductTypeAttribute> productTypeAttributes) {
		this.productTypeAttributes = productTypeAttributes;
	}
	
	@Transient
	public String getProductIdAsString() {
		return String.valueOf(productTypeId);
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "PRODUCT_TYPE_CLASS", joinColumns = { @JoinColumn(name = "PRODUCT_TYPE_ID") }, inverseJoinColumns = @JoinColumn(name = "CLASS_ID"))
	public Set<Classification> getClassifications() {
		return this.classifications;
	}

	/**
	 * @param classifications the classifications to set
	 */
	public void setClassifications(Set<Classification> classifications) {
		this.classifications = classifications;
	}
	
	/**
	 * Adds a classification for the attribute
	 * 
	 * @param cClass
	 *            the fully instantiated Classification Attribute
	 */
	public void associateWithClassification(Classification classification) {
		this.classifications.add(classification);
	}
	@ManyToOne (cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name="PROD_GROUP_PROD_TYPE", joinColumns={ @JoinColumn(name = "PRODUCT_TYPE_ID") }, inverseJoinColumns = @JoinColumn(name = "PRODUCT_GROUP_ID"))
	public ProductGroup getProductGroup() {
		return productGroup;
	}

	public void setProductGroup(ProductGroup productGroup) {
		this.productGroup = productGroup;
	}

	/**
	 * This mehtod checks whether two product types are equal or not. 
	 * */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ProductType && ((ProductType)obj).getProductTypeId() == this.productTypeId) {
			return true;
		}
		return false;
	}

}
