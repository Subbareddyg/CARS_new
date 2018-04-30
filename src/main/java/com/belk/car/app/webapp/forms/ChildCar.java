package com.belk.car.app.webapp.forms;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import javax.persistence.Transient;

public class ChildCar implements Serializable {

	private static final long serialVersionUID = 1L;
	private long carId;
	private  Map<String, String> belkUpc;
	private String styleNumber;
	private String productName;
	private String brandName;
	private  Map<String, String> colorSkuMap;
	private int order;
	private String sku;
	private String skuID;
	private String skuColor;
	private String skuSize;
	private String vendorName;
	private String vendorUpc;
	private String vendorStyle;
	private String colorName;
	private String sizeName;
	private long skuCarid;
	private String skuStyleName;
	private String belkSku;
	private String skuSelValues;
	private Long parentCarStyleId;
	private String compDt;
	private String styleTypeCd;
	private String allChildCarIds;	
	
	@Transient
	public Long getParentCarStyleId() {
		return parentCarStyleId;
	}
	@Transient
	public void setParentCarStyleId(Long parentCarStyleId) {
		this.parentCarStyleId = parentCarStyleId;
	}
    public String getSkuSelValues() {
		return skuSelValues;
	}
	public void setSkuSelValues(String skuSelValues) {
		this.skuSelValues = skuSelValues;
	}		
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}		
	public long getSkuCarid() {
		return skuCarid;
	}
	public void setSkuCarid(long skuCarid) {
		this.skuCarid = skuCarid;
	}	
	public String getSkuStyleName() {
		return skuStyleName;
	}
	public void setSkuStyleName(String skuStyleName) {
		this.skuStyleName = skuStyleName;
	}	
	public String getVendorUpc() {
		return vendorUpc;
	}
	public void setVendorUpc(String vendorUpc) {
		this.vendorUpc = vendorUpc;
	}	
	public String getVendorStyle() {
		return vendorStyle;
	}
	public void setVendorStyle(String vendorStyle) {
		this.vendorStyle = vendorStyle;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getSizeName() {
		return sizeName;
	}
	public void setSizeName(String sizeName) {
		this.sizeName = sizeName;
	}
	public String getSkuID() {
		return skuID;
	}
	public void setSkuID(String SkuID) {
		this.skuID = SkuID;
	}
	public String getSkuColor() {
		return skuColor;
	}
	public void setSkuColor(String SkuColor) {
		this.skuColor = SkuColor;
	}
	public String getSkuSize() {
		return skuSize;
	}
	public void setSkuSize(String SkuSize) {
		this.skuSize = SkuSize;
	}	
	public String getBelkSku() {
		return belkSku;
	}
	public void setBelkSku(String belkSku) {
		this.belkSku = belkSku;
	}	
	public void setCompDt(String compDt) {
		this.compDt = compDt;
	}
	public String getCompDt() {
		return compDt;
	}
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public Map<String, String> getColorSkuMap() {
		return colorSkuMap;
	}
	public void setColorSkuMap( Map<String, String> colorSkuMap) {
		this.colorSkuMap = colorSkuMap;
	}
	public long getCarId() {
		return carId;
	}
	public void setCarId(long carId) {
		this.carId = carId;
	}
	public String getStyleNumber() {
		return styleNumber;
	}
	public void setStyleNumber(String styleNumber) {
		this.styleNumber = styleNumber;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public Map<String, String> getBelkUpc() {
		return belkUpc;
	}
	
	public void setStyleTypeCd(String styleTypeCd) {
		this.styleTypeCd = styleTypeCd;
	}
	public String getStyleTypeCd() {
		return styleTypeCd;
	}
	public String getAllChildCarIds() {
		return allChildCarIds;
	}
	public void setAllChildCarIds(String allChildCarIds) {
		this.allChildCarIds = allChildCarIds;
	}	
}
