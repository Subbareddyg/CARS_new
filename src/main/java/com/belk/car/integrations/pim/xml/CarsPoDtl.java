package com.belk.car.integrations.pim.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "carsPoDtl", propOrder = {
		"style",
		"idbStyle",
		"vendorNumber",
		"vendorStyle",
		"item",
		"packItem"
})
public class CarsPoDtl {

	protected String style;
	@XmlElement(name = "idb_style")
	protected String idbStyle;
	@XmlElement(name = "vendor_number", required = true)
	protected String vendorNumber;
	@XmlElement(name = "vendor_style", required = true)
	protected String vendorStyle;
	protected List<CarsItem> item;
	protected List<CarsPackItem> packItem;


	public String getStyle() {
		return style;
	}


	public void setStyle(String value) {
		this.style = value;
	}

	public String getIdbStyle() {
		return idbStyle;
	}


	public void setIdbStyle(String value) {
		this.idbStyle = value;
	}


	public String getVendorNumber() {
		return vendorNumber;
	}


	public void setVendorNumber(String value) {
		this.vendorNumber = value;
	}

	public String getVendorStyle() {
		return vendorStyle;
	}

	public void setVendorStyle(String value) {
		this.vendorStyle = value;
	}


	public List<CarsItem> getItem() {
		if (item == null) {
			item = new ArrayList<CarsItem>();
		}
		return this.item;
	}


	public List<CarsPackItem> getPackItem() {
		if (packItem == null) {
			packItem = new ArrayList<CarsPackItem>();
		}
		return this.packItem;
	}

}
