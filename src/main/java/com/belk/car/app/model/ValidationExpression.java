package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "VALIDATION_EXPRESSION", uniqueConstraints = @UniqueConstraint(columnNames = "VALIDATION_EXPRESSION_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class ValidationExpression extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3884040767461013937L;

	public static final String NONE = "NONE";
	public static final String EMAIL = "EMAIL";
	public static final String AMOUNT = "AMOUNT";
	public static final String DATE = "DATE";

	private String validationExpressionCd;
	private String name;
	private String validationRule;

	public ValidationExpression() {
	}

	@Id
	@Column(name = "VALIDATION_EXPRESSION_CD", unique = true, nullable = false, length = 20)
	public String getValidationExpressionCd() {
		return this.validationExpressionCd;
	}

	public void setValidationExpressionCd(String validationExpressionCd) {
		this.validationExpressionCd = validationExpressionCd;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "VALIDATION_RULE", nullable = false, length = 200)
	public String getValidationRule() {
		return this.validationRule;
	}

	public void setValidationRule(String validationRule) {
		this.validationRule = validationRule;
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
