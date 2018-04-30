package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 
 * @author Yogesh.Vedak
 *
 */
@Entity
@Table(name = "COLOR_MAPPING_MASTER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class ColorMappingMaster extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -3982928293839292839L;
	
	private long cmmId;
	private String colorCodeBegin;
	private String colorCodeEnd;
	private String superColorCode;
	private String superColorName;
	private String statusCode;
	private String ruleChanged="" ;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CMM_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CMM_SEQ_GEN", sequenceName = "CMM_SEQ", allocationSize = 1)
	@Column(name = "CMM_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCmmId() {
		return cmmId;
	}

	public void setCmmId(long cmmId) {
		this.cmmId = cmmId;
	}

	@Column(name = "CMM_COLOR_CODE_BEGIN", nullable = true, length = 3)
	public String getColorCodeBegin() {
		return colorCodeBegin;
	}

	public void setColorCodeBegin(String colorCodeBegin) {
		this.colorCodeBegin = colorCodeBegin;
	}

	@Column(name = "CMM_COLOR_CODE_END", nullable = true, length = 3)
	public String getColorCodeEnd() {
		return colorCodeEnd;
	}

	public void setColorCodeEnd(String colorCodeEnd) {
		this.colorCodeEnd = colorCodeEnd;
	}

	@Column(name = "CMM_SUPER_COLOR_CODE", nullable = true, length = 3)
	public String getSuperColorCode() {
		return superColorCode;
	}

	public void setSuperColorCode(String superColorCode) {
		this.superColorCode = superColorCode;
	}

	@Column(name = "CMM_SUPER_COLOR_NAME", nullable = true, length = 100)
	public String getSuperColorName() {
		return superColorName;
	}

	public void setSuperColorName(String superColorName) {
		this.superColorName = superColorName;
	}

	@Column(name = "STATUS_CD", nullable = true, length = 100)
	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "CMM_RULE_CHANGED", nullable = true, length = 1)
	public String getRuleChanged() {
		return ruleChanged;
	}

	public void setRuleChanged(String ruleChanged) {
		this.ruleChanged = ruleChanged;
	}
	
		
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
}


