/**
 * @author afusy07-priyanka_gadia@syntelinc.com
 * @Date 12-dec-09
 *
 */

package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompoundKeyVndrShip implements java.io.Serializable {
	private static final long serialVersionUID = 166230878679123530L;
	private long addrId;
	private long fulfillmentServId;
	private long vendorId;

	public CompoundKeyVndrShip() {
		super();
	}

	public CompoundKeyVndrShip(long addrId, long fulfillmentServId) {
		this.addrId = addrId;
		this.fulfillmentServId = fulfillmentServId;
	}

	@Column(name = "LOCATION_ID", nullable = false, precision = 12, scale = 0)
	public long getAddrId() {
		return this.addrId;
	}

	public void setAddrId(long addrId) {
		this.addrId = addrId;
	}

	@Column(name = "FULFMNT_SERVICE_ID", nullable = false, precision = 12, scale = 0)
	public long getFulfillmentServId() {
		return this.fulfillmentServId;
	}

	public void setFulfillmentServId(long fulfillmentServId) {
		this.fulfillmentServId = fulfillmentServId;
	}

	@Column(name = "VENDOR_ID", nullable = false, precision = 12, scale = 0)
	public long getVendorId() {
		return vendorId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (addrId ^ (addrId >>> 32));
		result = prime * result
				+ (int) (fulfillmentServId ^ (fulfillmentServId >>> 32));
		result = prime * result + (int) (vendorId ^ (vendorId >>> 32));
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
		CompoundKeyVndrShip other = (CompoundKeyVndrShip) obj;
		if (addrId != other.addrId){
			return false;
		}
		if (fulfillmentServId != other.fulfillmentServId){
			return false;
		}
		if (vendorId != other.vendorId){
			return false;
		}
		return true;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	

}
