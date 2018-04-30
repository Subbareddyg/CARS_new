package com.belk.car.product.integration.request.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pack_data")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PackGroupRequestData {
	
	private String vendorNumber;
	
	private String vendorProductNumber;
	
	@XmlElement(name = "vendor_number")
	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	@XmlElement(name = "vendor_product_number")
	public String getVendorProductNumber() {
		return vendorProductNumber;
	}

	public void setVendorProductNumber(String vendorProductNumber) {
		this.vendorProductNumber = vendorProductNumber;
	}	
}
