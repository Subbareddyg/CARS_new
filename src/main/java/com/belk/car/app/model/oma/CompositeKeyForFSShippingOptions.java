/**
 * @author afusy07-Priyanka Gadia
 * Composite key class for Shipping options
 */
package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class CompositeKeyForFSShippingOptions implements java.io.Serializable {
	
	private static final long serialVersionUID = 4277375812481542685L;
	private Long shippingOptionId;
	private Long fulfillmentServId;

	public CompositeKeyForFSShippingOptions(Long shippingOption,
			Long fulfillmentServId) {
		this.shippingOptionId = shippingOption;
		this.fulfillmentServId = fulfillmentServId;
	}

	public CompositeKeyForFSShippingOptions() {
		super();
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((fulfillmentServId == null) ? 0 : fulfillmentServId
						.hashCode());
		result = prime
				* result
				+ ((shippingOptionId == null) ? 0 : shippingOptionId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		CompositeKeyForFSShippingOptions other = (CompositeKeyForFSShippingOptions) obj;
		if (fulfillmentServId == null) {
			if (other.fulfillmentServId != null){
				return false;
			}
		}
		else if (!fulfillmentServId.equals(other.fulfillmentServId)){
			return false;
		}
		if (shippingOptionId == null) {
			if (other.shippingOptionId != null){
				return false;
			}
		}
		else if (!shippingOptionId.equals(other.shippingOptionId)){
			return false;
		}
		return true;
	}

	
	

}
