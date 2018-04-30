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
@Table(name = "SOURCE_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "SOURCE_TYPE_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class SourceType extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7055066193257056922L;

	public static final String PO = "PO";
	public static final String MANUAL = "MANUAL";
	public static final String FINE_JEWELRY = "FINE_JEWELRY";
	public static final String CROSS_REFERENCE = "CROSS REFERENCE";
    public static final String DROPSHIP = "DROPSHIP";
    public static final String OUTFIT = "OUTFIT";
    public static final String PYG = "PYG";
	
	private String sourceTypeCd;
	private String name;
	private String descr;

	//private Set<Car> cars = new HashSet<Car>(0);

	public SourceType() {
	}

	@Id
	@Column(name = "SOURCE_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getSourceTypeCd() {
		return this.sourceTypeCd;
	}

	public void setSourceTypeCd(String sourceTypeCd) {
		this.sourceTypeCd = sourceTypeCd;
	}

	@Column(name = "NAME", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
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
