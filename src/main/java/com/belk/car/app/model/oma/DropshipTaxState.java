
package com.belk.car.app.model.oma;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * @author afusy07 i.e. Priyanka Gadia priyanka_gadia@syntelinc.com
 */
@Entity
@Table(name = "DROP_SHIP_TAX_STATE" , uniqueConstraints = @UniqueConstraint(columnNames = "STATE_ID"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class DropshipTaxState extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private long vendorTaxStateId;
	private String taxIssue;
	private String status;
	private State state;

	@Transient
	public String toString() {
		StringBuffer sfBuffer = new StringBuffer("vendorTaxStateId=");

		sfBuffer.append(getVendorTaxStateId());
		sfBuffer.append("\n taxIssue=");
		sfBuffer.append(getTaxIssue());
		sfBuffer.append("\n status=");
		sfBuffer.append(getStatus());
		return sfBuffer.toString();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DROP_SHIP_TAX_STATE_ID")
	@javax.persistence.SequenceGenerator(name = "SEQ_DROP_SHIP_TAX_STATE_ID", sequenceName = "SEQ_DROP_SHIP_TAX_STATE_ID", allocationSize = 1)
	@Column(name = "DROP_SHIP_TAX_STATE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getVendorTaxStateId() {
		return vendorTaxStateId;
	}

	public void setVendorTaxStateId(long vendorTaxStateId) {
		this.vendorTaxStateId = vendorTaxStateId;
	}

	@Column(name = "TAX_ISSUE_DESCR", nullable = false, length = 200)
	public String getTaxIssue() {
		return taxIssue;
	}

	public void setTaxIssue(String taxIssue) {
		this.taxIssue = taxIssue;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "STATE_ID", nullable = false,unique=true, updatable = true, insertable = true)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
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
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

}
