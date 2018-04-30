package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "INVOICE_METHOD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class InvoiceMethods extends BaseAuditableModel
implements
java.io.Serializable {

	
	private static final long serialVersionUID = 5941420840617883750L;

	private String invoiceMethodCode;
	private String name;
	private String description;
	private String statusCd;
	
	@Id
	@Column(name = "INVOICE_METHOD_CD", nullable = false, length = 20)
	public String getInvoiceMethodCode() {
		return invoiceMethodCode;
	}
	public void setInvoiceMethodCode(String invoiceMethodCode) {
		this.invoiceMethodCode = invoiceMethodCode;
	}
	@Column(name = "NAME", nullable = true, length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "DESCR", nullable = true, length = 200)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "STATUS_CD", nullable = true, length = 20)
	public String getStatusCd() {
		return statusCd;
	}
	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}
	
	
}
