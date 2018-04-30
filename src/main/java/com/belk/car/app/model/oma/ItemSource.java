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
@Table(name = "ITEM_SOURCE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class ItemSource extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8445525651014445769L;
	public static final String MANUAL_ENTRY = "Manual Entry";
	public static final String IDB_FEED = "IDB Feed";
	public static final String IDB_COST_CHANGE = "IDB Cost Change";
	public static final String CROSS_REFERENCE_FEED = "Cross Reference Feed";
	public static final String FULFILLMENT_SERVICE_FEED = "Fulfillment Service Feed";
	public static final String IDB_PURGE_FEED = "IDB Purge Feed";
	
	private String itemSourceId;
	private String description;
	private String status;
	private String name;
	
	/**
	 * 
	 */
	public ItemSource() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "item_source_cd", unique = true, nullable = false, length=20)
	public String getItemSourceId() {
		return itemSourceId;
	}
	public void setItemSourceId(String itemSourceId) {
		this.itemSourceId = itemSourceId;
	}
	
	@Column(name = "descr", nullable = false, length = 200)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "status_cd", nullable = false, length = 20)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "\"NAME\"", nullable = false, length = 20)
	public String getName() {
		return name;
	}
	

}
