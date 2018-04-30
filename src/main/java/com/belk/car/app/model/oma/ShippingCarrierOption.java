
package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "SHIPPING_CARRIER_OPTION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class ShippingCarrierOption extends BaseAuditableModel implements java.io.Serializable {

	
	private static final long serialVersionUID = -3080611906067977288L;

	private Long shippingOptionId;
	private String carrierName;
	private String carrierClass;
	private String account;
	private String isAllowed;

	@Id
	@Column(name = "SHIPPING_CARRIER_OPTION_ID", precision = 12, scale = 0)
	public Long getShippingOptionId() {
		return shippingOptionId;
	}

	public void setShippingOptionId(Long shippingOptionId) {
		this.shippingOptionId = shippingOptionId;
	}

	@Column(name = "SHIPPING_CARRIER_NAME", nullable = false, length = 50)
	public String getCarrierName() {
		return carrierName;
	}

	public void setCarrierName(String carrierName) {
		this.carrierName = carrierName;
	}

	@Column(name = "SHIPPING_CLASS", nullable = false, length = 50)
	public String getCarrierClass() {
		return carrierClass;
	}

	public void setCarrierClass(String carrierClass) {
		this.carrierClass = carrierClass;
	}

	@Transient
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Transient
	public String getIsAllowed() {
		return isAllowed;
	}

	public void setIsAllowed(String isAllowed) {
		this.isAllowed = isAllowed;
	}

}
