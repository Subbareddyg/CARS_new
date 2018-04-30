package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * This is the model class for table name vndr_catl_update_action
 * @version 1.0 10 December 2009
 * @author afusya2
 */
@Entity
@Table(name = "vndr_catl_update_action")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorCatalogUpdateAction extends BaseAuditableModel implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8225124363101462961L;
	private long updateActionID;
	private String statusCD;	
	private String updateActionDesc;
	
	   
	
	@Id
	@Column(name = "vnd_catl_update_action_id", unique = false, length = 12)	
	public long getUpdateActionID() {
		return updateActionID;
	}
	public void setUpdateActionID(long updateActionID) {
		this.updateActionID = updateActionID;
	}

	@Column(name = "descr", unique = false, length = 200)
	public String getUpdateActionDesc() {
		return updateActionDesc;
	}

	public void setUpdateActionDesc(String updateActionDesc) {
		this.updateActionDesc = updateActionDesc;
	}

	
	@Column(name = "status_cd", unique = false, length = 20)
	public String getStatusCD() {
		return statusCD;
	}
	
	public void setStatusCD(String statusCD) {
		this.statusCD = statusCD;
	}

	
}
