package com.belk.car.app.webapp.forms;

public class DBPromotionForm implements java.io.Serializable{
	private String dbPromotionName;
	public String getDbPromotionName() {
		return dbPromotionName;
	}
	public void setDbPromotionName(String dbPromotionName) {
		this.dbPromotionName = dbPromotionName;
	}
	private String description;
	private boolean bmProductName=false;
	private String[] removeFlag;
	private String childCarId;
	private String[] order;
	private String[] sku;
	private String[] skuCarid;
	private String[] chkSkuList;
	private String[] vendorStyle;
	private String[] vendorUpc;
	private String[] skuColor;
	private String[] colorName;
	private String[] sizeName;
	private String[] skuCount;
	
	private String selDBPromotionCollectionSkus;
    private String[] templateType;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isBmProductName() {
		return bmProductName;
	}
	public void setBmProductName(boolean bmProductName) {
		this.bmProductName = bmProductName;
	}
	public String[] getRemoveFlag() {
		return removeFlag;
	}
	public void setRemoveFlag(String[] removeFlag) {
		this.removeFlag = removeFlag;
	}
	public String getChildCarId() {
		return childCarId;
	}
	public void setChildCarId(String childCarId) {
		this.childCarId = childCarId;
	}
	public String[] getOrder() {
		return order;
	}
	public void setOrder(String[] order) {
		this.order = order;
	}
	public String[] getSku() {
		return sku;
	}
	public void setSku(String[] sku) {
		this.sku = sku;
	}
	public String[] getSkuCarid() {
		return skuCarid;
	}
	public void setSkuCarid(String[] skuCarid) {
		this.skuCarid = skuCarid;
	}
	public String[] getChkSkuList() {
		return chkSkuList;
	}
	public void setChkSkuList(String[] chkSkuList) {
		this.chkSkuList = chkSkuList;
	}
	public String[] getVendorStyle() {
		return vendorStyle;
	}
	public void setVendorStyle(String[] vendorStyle) {
		this.vendorStyle = vendorStyle;
	}
	public String[] getVendorUpc() {
		return vendorUpc;
	}
	public void setVendorUpc(String[] vendorUpc) {
		this.vendorUpc = vendorUpc;
	}
	public String[] getSkuColor() {
		return skuColor;
	}
	public void setSkuColor(String[] skuColor) {
		this.skuColor = skuColor;
	}
	public String[] getColorName() {
		return colorName;
	}
	public void setColorName(String[] colorName) {
		this.colorName = colorName;
	}
	public String[] getSizeName() {
		return sizeName;
	}
	public void setSizeName(String[] sizeName) {
		this.sizeName = sizeName;
	}
	public String[] getSkuCount() {
		return skuCount;
	}
	public void setSkuCount(String[] skuCount) {
		this.skuCount = skuCount;
	}
	public String getSelDBPromotionCollectionSkus() {
		return selDBPromotionCollectionSkus;
	}
	public void setSelDBPromotionCollectionSkus(String selDBPromotionCollectionSkus) {
		this.selDBPromotionCollectionSkus = selDBPromotionCollectionSkus;
	}
	public String[] getTemplateType() {
		return templateType;
	}
	public void setTemplateType(String[] templateType) {
		this.templateType = templateType;
	}
}
