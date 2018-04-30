package com.belk.car.app.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.belk.car.app.model.vendorcatalog.VendorCatalogHeader;


public class CatalogHeaderDTO implements Serializable {

	private static final long serialVersionUID = 3782540643927570365L;
	private Long vendorCatalogHeaderId;
	private Long vendorCatalogID;
	private Long vendorCatalogFieldNum;
	private String vendorCatalogHeaderFieldName;

	public CatalogHeaderDTO(){}
	
	/**
	 * Gets the vendor catalog header and converts to the catalog header DTO
	 * @param vendorCatalogHeader
	 */
	public CatalogHeaderDTO(VendorCatalogHeader vendorCatalogHeader){
		this.vendorCatalogHeaderId = vendorCatalogHeader.getVendorCatalogHeaderId();
		this.vendorCatalogID = vendorCatalogHeader.getVendorCatalogID().getVendorCatalogID();
		this.vendorCatalogFieldNum = vendorCatalogHeader.getVendorCatalogFieldNum();
		this.vendorCatalogHeaderFieldName = vendorCatalogHeader.getVendorCatalogHeaderFieldName();
	}
	
	public Long getVendorCatalogHeaderId() {
		return vendorCatalogHeaderId;
	}

	public void setVendorCatalogHeaderId(Long vendorCatalogHeaderId) {
		this.vendorCatalogHeaderId = vendorCatalogHeaderId;
	}

	public Long getVendorCatalogID() {
		return vendorCatalogID;
	}

	public void setVendorCatalogID(Long vendorCatalogID) {
		this.vendorCatalogID = vendorCatalogID;
	}

	public Long getVendorCatalogFieldNum() {
		return vendorCatalogFieldNum;
	}

	public void setVendorCatalogFieldNum(Long vendorCatalogFieldNum) {
		this.vendorCatalogFieldNum = vendorCatalogFieldNum;
	}

	public String getVendorCatalogHeaderFieldName() {
		return vendorCatalogHeaderFieldName;
	}

	public void setVendorCatalogHeaderFieldName(String vendorCatalogHeaderFieldName) {
		this.vendorCatalogHeaderFieldName = vendorCatalogHeaderFieldName;
	}
	
	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("vendorCatalogHeaderId", this.vendorCatalogHeaderId)
                .append("vendorCatalogID", this.vendorCatalogID)
                .append("vendorCatalogFieldNum", this.vendorCatalogFieldNum)
                .append("vendorCatalogHeaderFieldName", this.vendorCatalogHeaderFieldName).toString();
    }

}
