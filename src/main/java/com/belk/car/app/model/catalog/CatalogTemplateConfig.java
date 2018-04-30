package com.belk.car.app.model.catalog;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "CATALOG_TEMPLATE_CONFIG", uniqueConstraints = @UniqueConstraint(columnNames = "CATALOG_TEMPLATE_CONFIG_ID"))
public class CatalogTemplateConfig extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8437398351097416862L;

	private long configId;
	private CatalogTemplate template;
	private String excelColumnLocation;
	private String attributeName ;

	public CatalogTemplateConfig() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CATALOG_TEMPLATE_CONFIG_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CATALOG_TEMPLATE_CONFIG_SEQ_GEN", sequenceName = "CATALOG_TEMPLATE_CONFIG_SEQ", allocationSize = 1)
	@Column(name = "CATALOG_TEMPLATE_CONFIG_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getConfigId() {
		return this.configId;
	}

	public void setConfigId(long configId) {
		this.configId = configId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "TEMPLATE_ID", nullable = false)
	public CatalogTemplate getTemplate() {
		return template;
	}

	public void setTemplate(CatalogTemplate template) {
		this.template = template;
	}

	@Column(name = "EXCEL_COLUMN_LOCATION", nullable = false, length = 50)
	public String getExcelColumnLocation() {
		return excelColumnLocation;
	}

	public void setExcelColumnLocation(String excelColumnLocation) {
		this.excelColumnLocation = excelColumnLocation;
	}

	@Column(name = "ATTR_NAME", unique = true, nullable = false, length = 50)
	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
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
