package com.belk.car.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.belk.car.app.model.vendorcatalog.CatalogGroupTemplate;

@Entity
@Table(name = "Product_Group", uniqueConstraints = @UniqueConstraint(columnNames = "NAME"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class ProductGroup extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -7540238555995568939L;
	private long productGroupId;
	private String name;
	private String descr;
	private String statusCd;
	private List<ProductType> productType = new ArrayList<ProductType>(0);

	public ProductGroup() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCT_GROUP_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "PRODUCT_GROUP_SEQ_GEN", sequenceName = "SEQ_PRODUCT_GROUP_ID", allocationSize = 1)
	@Column(name = "PRODUCT_GROUP_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getProductGroupId() {
		return this.productGroupId;
	}

	public void setProductGroupId(long productGroupId) {
		this.productGroupId = productGroupId;
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

	@Transient
	public String getProductGroupIdAsString() {
		return String.valueOf(productGroupId);
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "PROD_GROUP_PROD_TYPE",  joinColumns = { @JoinColumn(name = "PRODUCT_GROUP_ID", unique = true)}, inverseJoinColumns = @JoinColumn(name = "PRODUCT_TYPE_ID") )
	public List<ProductType> getProductType() {
		return this.productType;
	}

	/**
	 * @param product
	 *            type
	 */
	public void setProductType(List<ProductType> productType) {
		this.productType = productType;
	}

	/**
	 * Adds additional product types to the existing groups.
	 * 
	 * @param productType
	 */
	public void addProductTypes(List<ProductType> productType) {
		this.productType.addAll(productType);
	}

	public String toString() {
		StringBuffer sbObjectValue = new StringBuffer();
		sbObjectValue.append("\nproductGroupId --" + productGroupId);
		sbObjectValue.append("\nname --" + name);
		sbObjectValue.append("\ndescription --" + descr);
		sbObjectValue.append("\nstatusCd --" + statusCd);
		return sbObjectValue.toString();
	}
}
