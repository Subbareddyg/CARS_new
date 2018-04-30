
package com.belk.car.app.webapp.forms;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.FactoryUtils;
import org.apache.commons.collections.list.LazyList;

import com.belk.car.app.model.oma.ShippingCarrierOption;

/**
 * @author afusy07-Priyanka Gadia
 * Form for shipping options.
 */
@SuppressWarnings("unchecked")
public class VendorFSShippingOptionsForm implements Serializable {

	public VendorFSShippingOptionsForm() {

	}

	private List<ShippingCarrierOption> shippingOptionsModel = LazyList.decorate(new ArrayList(),
			FactoryUtils.instantiateFactory(ShippingCarrierOption.class));
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String directBelk;
	private String directVendor;

	public String getDirectBelk() {
		return directBelk;
	}

	public void setDirectBelk(String directBelk) {
		this.directBelk = directBelk;
	}

	public String getDirectVendor() {
		return directVendor;
	}

	public void setDirectVendor(String directVendor) {
		this.directVendor = directVendor;
	}

	public List<ShippingCarrierOption> getShippingOptionsModel() {
		return shippingOptionsModel;
	}

	public void setShippingOptionsModel(List<ShippingCarrierOption> shippingOptionsModel) {
		this.shippingOptionsModel = shippingOptionsModel;
	}

}
