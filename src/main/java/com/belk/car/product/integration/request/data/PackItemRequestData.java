package com.belk.car.product.integration.request.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pack_data")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class PackItemRequestData {
	
	private String vendorNumber;
	
	private String vendorProductNumber;
	
	private String colorCode;
	
	private Integer sizeCode;

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

	@XmlElement(name = "color_code")
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	@XmlElement(name = "size_code")
	public Integer getSizeCode() {
		return sizeCode;
	}

	public void setSizeCode(Integer sizeCode) {
		this.sizeCode = sizeCode;
	}
	
	

}
