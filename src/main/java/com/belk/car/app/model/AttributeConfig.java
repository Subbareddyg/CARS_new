package com.belk.car.app.model;

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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "ATTRIBUTE_CONFIG")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class AttributeConfig extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1907024969147529364L;
	public static final String DEFAULT_VALIDATION_EXPRESSION = "NONE";

	private long attrConfigId;
	private String displayName;
	private String attributeName;
	private HtmlDisplayType htmlDisplayType = new HtmlDisplayType();
	private ValidationExpression validationExpression;
	private String statusCd;

	public AttributeConfig() {
	}


	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ATTR_CONFIG_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "ATTR_CONFIG_SEQ_GEN", sequenceName = "ATTRIBUTE_CONFIG_SEQ", allocationSize = 1)
	@Column(name = "ATTR_CONFIG_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getAttrConfigId() {
		return this.attrConfigId;
	}

	public void setAttrConfigId(long attrConfigId) {
		this.attrConfigId = attrConfigId;
	}

	@Column(name = "DISPLAY_NAME", nullable = false, length = 200)
	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Column(name = "ATTRIBUTE_NAME", nullable = false, length = 50)
	public String getName() {
		return this.attributeName;
	}

	public void setName(String attributeName) {
		this.attributeName = attributeName;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "HTML_DISPLAY_TYPE_CD", nullable = false)
	public HtmlDisplayType getHtmlDisplayType() {
		return this.htmlDisplayType;
	}

	public void setHtmlDisplayType(HtmlDisplayType htmlDisplayType) {
		this.htmlDisplayType = htmlDisplayType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VALIDATION_EXPRESSION_CD", nullable = false)
	public ValidationExpression getValidationExpression() {
		return this.validationExpression;
	}

	public void setValidationExpression(ValidationExpression validationExpression) {
		this.validationExpression = validationExpression;
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
}
