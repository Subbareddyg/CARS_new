package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "style_population_method")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class StylePopulationMethod extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3522192284449202407L;
	public static final String DROPSHIP_NOT_IN_ECOMMERCE = "Drop Ship In IDB Not In ECommerce";
	public static final String MANUAL = "Manual";
	public static final String UPLOAD = "Upload";
	public static final String PREVIOUS_REQUEST = "Previous Request";
	
	private String stylePopulationMethodId;
	private String name;
	
	/**
	 * 
	 */
	public StylePopulationMethod() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "style_population_method_cd", unique = true, nullable = false, length=20)
	public String getStylePopulationMethodId() {
		return stylePopulationMethodId;
	}
	public void setStylePopulationMethodId(String stylePopulationMethodId) {
		this.stylePopulationMethodId = stylePopulationMethodId;
	}
	
	@Column(name = "\"NAME\"", nullable = false, length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
