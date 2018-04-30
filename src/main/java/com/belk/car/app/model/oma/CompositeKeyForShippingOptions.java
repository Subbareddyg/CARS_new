/**
 * @author afusy07-Priyanka Gadia
 * Composite Key For Vendor Shipping Options
 *
 */
package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class CompositeKeyForShippingOptions implements java.io.Serializable {
	
	private static final long serialVersionUID = 8334634086581680863L;

	private Long shippingOptionId;
	private Long fulfillmentServId;
	private Long vendorId;

	public CompositeKeyForShippingOptions() {
		super();
	}

	public CompositeKeyForShippingOptions(Long shippingOption,
			Long fulfillmentServId) {
		this.shippingOptionId = shippingOption;
		this.fulfillmentServId = fulfillmentServId;
	}

	@Column(name = "SHIPPING_CARRIER_OPTION_ID", nullable = false, precision = 12, scale = 0)
	public Long getShippingOptionId() {
		return this.shippingOptionId;
	}

	public void setShippingOptionId(Long shippingOption) {
		this.shippingOptionId = shippingOption;
	}

	@Column(name = "FULFMNT_SERVICE_ID", nullable = false, precision = 12, scale = 0)
	public Long getFulfillmentServId() {
		return this.fulfillmentServId;
	}

	public void setFulfillmentServId(Long fulfillmentServId) {
		this.fulfillmentServId = fulfillmentServId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	@Column(name = "VENDOR_ID", nullable = false, precision = 12, scale = 0)
	public Long getVendorId() {
		return vendorId;
	}

	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof CompositeKeyForShippingOptions)) {
			return false;
		}
		CompositeKeyForShippingOptions castOther = (CompositeKeyForShippingOptions) other;

		return (this.getShippingOptionId() == castOther.getShippingOptionId())
				&& (this.getFulfillmentServId() == castOther
						.getFulfillmentServId() && this.getVendorId() == castOther
						.getVendorId());
	}

	public int hashCode() {
		int result = 17;

		result = (int) (37 * result + this.getShippingOptionId());
		result = (int) (37 * result + this.getFulfillmentServId());
		result = (int) (37 * result + this.getVendorId());
		return result;
	}

}
