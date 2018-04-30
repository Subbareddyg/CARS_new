
package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * @author afusy07
 */
@Entity
@Table(name = "RETURN_METHOD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class ReturnMethod extends BaseAuditableModel
		implements
			java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String rtnMethCode;
	private String rtnMethName;
	private String rtnMethDesc;
	private String statusCd;

	@Id
	@Column(name = "RETURN_METHOD_CD",length = 20)
	public String getRtnMethCode() {
		return rtnMethCode;
	}

	
	@Column(name = "NAME", nullable = false, length = 50)
	public String getRtnMethName() {
		return rtnMethName;
	}

	public void setRtnMethName(String rtnMethName) {
		this.rtnMethName = rtnMethName;
	}

	@Column(name = "DESCR", nullable = true, length = 250)
	public String getRtnMethDesc() {
		return rtnMethDesc;
	}

	public void setRtnMethDesc(String rtnMethDesc) {
		this.rtnMethDesc = rtnMethDesc;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public void setRtnMethCode(String rtnMethCode) {
		this.rtnMethCode = rtnMethCode;
	}

	

}
