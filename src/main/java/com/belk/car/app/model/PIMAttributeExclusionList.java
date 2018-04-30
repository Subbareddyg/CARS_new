/**
 * 
 */
package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Lookup table to find the exclusion attribute List.
 * 
 * @author AFUSYS9
 *
 */
@Entity
@Table (name="PIM_ATTR_EXCLUSION_LIST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class PIMAttributeExclusionList extends BaseAuditableModel implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 498811912093435544L;

	private Long pimAttrId;

	public PIMAttributeExclusionList(){
		
	}
	
	@Id
	@Column(name="PIM_ATTR_ID", unique = true, nullable = false)
	public Long getPimAttrId() {
		return pimAttrId;
	}

	public void setPimAttrId(Long pimAttrId) {
		this.pimAttrId = pimAttrId;
	}
	
}
