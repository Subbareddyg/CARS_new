/**
 * 
 */
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

/**
 * @author afusy01
 *
 */
@Entity
@Table(name = "VIFR_WORKFLOW_STATUS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VFIRStatus extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	public static final String APPROVED = "APPROVED";
	public static final String OPEN = "OPEN";
	public static final String REJECT = "REJECT";
	public static final String AWAITING_APPROVAL = "AWAITING_APPROVAL";
	
	private static final long serialVersionUID = 247611426023074297L;
	
	private String vfirStatusCode;
	private String name;
	private String description;
	/**
	 * 
	 */
	public VFIRStatus() {
		// TODO Auto-generated constructor stub
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "VIFR_WORKFLOW_STATUS_CD", unique = true, nullable = false, length = 20)
	public String getVfirStatusCode() {
		return vfirStatusCode;
	}
	public void setVfirStatusCode(String vfirStatusCode) {
		this.vfirStatusCode = vfirStatusCode;
	}
	
	@Column(name = "\"NAME\"", unique = true, nullable = false, length=50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "descr", unique = true, nullable = false, length=200)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	

}
