package com.belk.car.app.model.catalog;

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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.integrations.cosmetics.CosmeticProductRow;
import com.belk.car.integrations.cosmetics.ExcelDataRow;
import com.belk.car.integrations.cosmetics.FragranceProductRow;
import com.belk.car.integrations.cosmetics.LeCouventProductRow;

@Entity
@Table(name = "CATALOG_TEMPLATE", uniqueConstraints = @UniqueConstraint(columnNames = "TEMPLATE_NAME"))
public class CatalogTemplate extends BaseAuditableModel implements
		java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4030381893898492623L;

	public static String COSMETICS = "COSMETICS" ;
	public static String FRAGRANCE= "FRAGRANCE";
	public static String LE_COUVENT_DES_MINIMES= "LE_COUVENT_DES_MINIMES";
	
	private long templateId;
	private String name;
	private Set<CatalogTemplateConfig> configurations; 

	public CatalogTemplate() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_TEMPLATE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATALOG_TEMPLATE_SEQ_GEN", sequenceName = "CATALOG_TEMPLATE_SEQ", allocationSize = 1)
	@Column(name = "TEMPLATE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	@Column(name = "TEMPLATE_NAME", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "template")
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE,
          org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
	public Set<CatalogTemplateConfig> getConfigurations() {
		return configurations;
	}

	public void setConfigurations(Set<CatalogTemplateConfig> configurations) {
		this.configurations = configurations;
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
	public ExcelDataRow getProcessor() {
		if (COSMETICS.equals(this.getName())) {
			return new CosmeticProductRow() ;
		} else if (FRAGRANCE.equals(this.getName()))
		{
			return new FragranceProductRow() ;
		} else if (LE_COUVENT_DES_MINIMES.equals(this.getName())) {
			return new LeCouventProductRow() ;
		}
		return new CosmeticProductRow() ;
	}
}
