package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "STYLE_POPULATION_METH")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class StylePopulationMethod extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3522192284449202407L;
	
	private long stylePopulationMethodId;
	private String description;
	
	/**
	 * 
	 */
	public StylePopulationMethod() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "STYLE_POPULATION_METH_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getStylePopulationMethodId() {
		return stylePopulationMethodId;
	}
	public void setStylePopulationMethodId(long stylePopulationMethodId) {
		this.stylePopulationMethodId = stylePopulationMethodId;
	}
	
	@Column(name = "METH_DESC", nullable = false, length = 200)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
