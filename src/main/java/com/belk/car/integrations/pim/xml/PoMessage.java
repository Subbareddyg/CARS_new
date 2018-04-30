package com.belk.car.integrations.pim.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;



@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
		"type",
		"orderNo",
		"notBeforeDate",
		"notAfterDate",
		"approvedDate",
                "skuOrins",
		"poDtl"
})

@XmlRootElement(name = "poMessage")
public class PoMessage {

	@XmlElement(required = true)
	protected String type;
	@XmlElement(name = "order_no", required = true)
	protected String orderNo;
	@XmlElement(name = "not_before_date", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar notBeforeDate;
	@XmlElement(name = "not_after_date", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar notAfterDate;
	@XmlElement(name = "approved_date", required = true)
	@XmlSchemaType(name = "date")
	protected XMLGregorianCalendar approvedDate;
        @XmlElement(name = "sku_orins", required = false)
        protected SkuOrins skuOrins;
	@XmlElement(required = true)
	protected List<CarsPoDtl> poDtl;


	public String getType() {
		return type;
	}


	public void setType(String value) {
		this.type = value;
	}


	public String getOrderNo() {
		return orderNo;
	}


	public void setOrderNo(String value) {
		this.orderNo = value;
	}


	public XMLGregorianCalendar getNotBeforeDate() {
		return notBeforeDate;
	}


	public void setNotBeforeDate(XMLGregorianCalendar value) {
		this.notBeforeDate = value;
	}


	public XMLGregorianCalendar getNotAfterDate() {
		return notAfterDate;
	}


	public void setNotAfterDate(XMLGregorianCalendar value) {
		this.notAfterDate = value;
	}


	public XMLGregorianCalendar getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(XMLGregorianCalendar value) {
		this.approvedDate = value;
	}

	public List<CarsPoDtl> getPoDtl() {
		if (poDtl == null) {
			poDtl = new ArrayList<CarsPoDtl>();
		}
		return this.poDtl;
	}

        public SkuOrins getSkuOrins() {
            return skuOrins;
        }

        public void setSkuOrins(SkuOrins skuOrins) {
            this.skuOrins = skuOrins;
        }

        
        
}
