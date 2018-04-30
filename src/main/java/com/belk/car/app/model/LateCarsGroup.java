package com.belk.car.app.model;

import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "LATE_CARS_GROUP", uniqueConstraints = @UniqueConstraint(columnNames = "DESCRIPTION"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class LateCarsGroup extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = -7540238555995568939L;
	private long lateCarsGroupId;
	private String name;
	
	
	private List<LateCarsParams> lateCarsParams = new ArrayList<LateCarsParams>(0);
	private List<LateCarsAssociation> lateCarsAssociation = new ArrayList<LateCarsAssociation>(0);

	public LateCarsGroup() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_USER_VENDOR_DEPARTMENT_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_USER_VENDOR_DEPARTMENT_SEQ_GEN", sequenceName = "CAR_USER_VENDOR_DEPARTMENT_SEQ", allocationSize = 1)
	@Column(name = "LATE_CARS_GROUP_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getLateCarsGroupId() {
		return this.lateCarsGroupId;
	}

	public void setLateCarsGroupId(long lateCarsGroupId) {
		this.lateCarsGroupId = lateCarsGroupId;
	}

	@Column(name = "DESCRIPTION", unique = true, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "LATE_CARS_PARAMETERS",  joinColumns = { @JoinColumn(name = "LATE_CARS_GROUP_ID", unique = true)}, inverseJoinColumns = @JoinColumn(name = "LATE_CARS_PARAMS_ID"))
	public List<LateCarsParams> getLateCarsParams() {
		return lateCarsParams;
	}
	
	public void setLateCarsParams(List<LateCarsParams> lateCarsParams) {
		this.lateCarsParams = lateCarsParams;
	}
	
	/**
	 * Adds additional late cars params to the existing group.
	 * 
	 * @param lateCarsParams
	 */
	public void addLateCarsParams(List<LateCarsParams> lateCarsParams) {
		this.lateCarsParams.addAll(lateCarsParams);
	}
	
	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "LATE_CARS_ASSOCIATION",  joinColumns = { @JoinColumn(name = "LATE_CARS_GROUP_ID", unique = true)}, inverseJoinColumns = @JoinColumn(name = "LATE_CARS_ASSOCIATION_ID") )
	public List<LateCarsAssociation> getLateCarsAssociation() {
		return lateCarsAssociation;
	}

	
	public void setLateCarsAssociation(List<LateCarsAssociation> lateCarsAssociation) {
		this.lateCarsAssociation = lateCarsAssociation;
	}
	
	public String toString() {
		StringBuffer sbObjectValue = new StringBuffer();
		sbObjectValue.append("\nlateCarsGroupId --" + lateCarsGroupId);
		sbObjectValue.append("\nname --" + name);
	 //	sbObjectValue.append("\ndescription --" + descr);
	//	sbObjectValue.append("\nstatusCd --" + statusCd);
		return sbObjectValue.toString();
	}
}
