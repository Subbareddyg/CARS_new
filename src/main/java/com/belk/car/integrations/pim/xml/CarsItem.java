package com.belk.car.integrations.pim.xml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "carsItem", propOrder = {
		"sku",
		"idbId",
		"vendorUpc",
		"retailPrice",
		"qtyOrdered",
		"packId",
		"idbPack"
})
public class CarsItem {

	@XmlElement(required = true)
	protected String sku;
	@XmlElement(name = "idb_id", required = true)
	protected String idbId;
	@XmlElement(name = "vendor_upc", required = true)
	protected String vendorUpc;
	@XmlElement(name = "retail_price", required = true)
	protected BigDecimal retailPrice;
	@XmlElement(name = "qty_ordered", required = false)
	protected BigDecimal qtyOrdered;
	@XmlElement(name = "pack_id", required = true)
	protected String packId;
	@XmlElement(name = "idb_pack", required = true)
	protected String idbPack;
	
	public String getSku() {
		return sku;
	}


	public void setSku(String value) {
		this.sku = value;
	}


	public String getIdbId() {
		return idbId;
	}


	public void setIdbId(String value) {
		this.idbId = value;
	}


	public String getVendorUpc() {
		return vendorUpc;
	}

	public void setVendorUpc(String value) {
		this.vendorUpc = value;
	}


	public BigDecimal getRetailPrice() {
		return retailPrice;
	}


	public void setRetailPrice(BigDecimal value) {
		this.retailPrice = value;
	}


	public BigDecimal getQtyOrdered() {
		return qtyOrdered;
	}


	public void setQtyOrdered(BigDecimal value) {
		this.qtyOrdered = value;
	}


	public String getPackId() {
		return packId;
	}


	public void setPackId(String value) {
		this.packId = value;
	}


	public String getIdbPack() {
		return idbPack;
	}

	public void setIdbPack(String value) {
		this.idbPack = value;
	}

}
