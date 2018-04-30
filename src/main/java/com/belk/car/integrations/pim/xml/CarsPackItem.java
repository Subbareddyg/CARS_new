package com.belk.car.integrations.pim.xml;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "carsPackItem", propOrder = {
		"pack",
		"idbPack",
		"packUpc",
		"retailPrice",
		"qtyOrdered"
})
public class CarsPackItem {

	@XmlElement(required = true)
	protected String pack;
	@XmlElement(name = "idb_pack", required = true)
	protected String idbPack;
	@XmlElement(name = "pack_upc", required = true)
	protected String packUpc;
	@XmlElement(name = "retail_price", required = true)
	protected BigDecimal retailPrice;
	@XmlElement(name = "qty_ordered", required = false)
	protected BigDecimal qtyOrdered;


	public String getPack() {
		return pack;
	}


	public void setPack(String value) {
		this.pack = value;
	}


	public String getIdbPack() {
		return idbPack;
	}

	public void setIdbPack(String value) {
		this.idbPack = value;
	}


	public String getPackUpc() {
		return packUpc;
	}


	public void setPackUpc(String value) {
		this.packUpc = value;
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

}
