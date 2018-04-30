
package com.belk.car.app.model.oma;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * @author afusy07 i.e. Priyanka Gadia priyanka_gadia@syntelinc.com
 */
@Entity
@Table(name = "FULFMNT_SERV_VNDR_RTN")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class FulfillmentServiceVendorReturn extends BaseAuditableModel
		implements
			java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long vendorReturnId;
	private Long fulfillmentServId;
	private Long vendorID;
	private ReturnMethod rtrnMethod;
	private String statusCd;
	private String rowReadyUpdt;
	private Long addrId;
	private Long rmaNum;
	private Address addr;

	public String toString() {
		StringBuilder model = new StringBuilder();
		model.append("return Id=");
		model.append(this.getVendorReturnId());
		model.append("\n getFulfillmentServId=");
		model.append(this.getFulfillmentServId());
		model.append("\n vndrId=");
		model.append(this.getVendorID());
		model.append("\n rtrnMethodId=");
		model.append(this.getRtrnMethod().getRtnMethName());
		model.append("\n addrId=");
		model.append(this.getAddrId());
		model.append("\n rma num=");
		model.append(this.getRmaNum());
		return model.toString();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAMPLE_SEQ")
	@javax.persistence.SequenceGenerator(name = "SAMPLE_SEQ", sequenceName = "SAMPLE_SEQ", allocationSize = 1)
	@Column(name = "VNDR_RTN_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getVendorReturnId() {
		return vendorReturnId;
	}

	public void setVendorReturnId(Long vendorReturnId) {
		this.vendorReturnId = vendorReturnId;
	}

	@Column(name = "FULFILLMENT_SERVICE_ID", nullable = false, precision = 12, scale = 0)
	public Long getFulfillmentServId() {
		return fulfillmentServId;
	}

	public void setFulfillmentServId(Long fulfillmentServId) {
		this.fulfillmentServId = fulfillmentServId;
	}

	@Column(name = "VENDOR_ID", nullable = true, precision = 12, scale = 0)
	public Long getVendorID() {
		return vendorID;
	}

	public void setVendorID(Long vndrId) {
		this.vendorID = vndrId;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "DFLT_RETURN_METHOD_CD", nullable = false)
	public ReturnMethod getRtrnMethod() {
		return rtrnMethod;
	}
	
	public void setRtrnMethod(ReturnMethod rtrnMethod) {
		this.rtrnMethod = rtrnMethod;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	
	@Column(name = "ROW_READY_UPDATE", nullable = true, length = 1)
	public String getRowReadyUpdt() {
		return rowReadyUpdt;
	}

	public void setRowReadyUpdt(String rowReadyUpdt) {
		this.rowReadyUpdt = rowReadyUpdt;
	}

	@Column(name = "DFLT_RETURN_LOCATION_ID", nullable = true, precision = 12, scale = 0)
	public Long getAddrId() {
		return addrId;
	}

	public void setAddrId(Long addrId) {
		this.addrId = addrId;
	}

	@Column(name = "DFLT_RMA_NUMBER", nullable = true, precision = 12, scale = 0)
	public Long getRmaNum() {
		return rmaNum;
	}

	public void setRmaNum(Long rmaNum) {
		this.rmaNum = rmaNum;
	}

	public void setAddr(Address addr) {
		this.addr = addr;
	}

	@OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
	@JoinColumn(name = "DFLT_RETURN_LOCATION_ID", nullable = true, updatable = false, insertable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Address getAddr() {
		return addr;
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
