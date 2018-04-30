package com.belk.car.app.webapp.controller.catalog;

public class CatalogFileUpload {
	private String name;
	private byte[] file;
	private String vendorNumber;
	private int templateId;
	private String createOnly = "N";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getVendorNumber() {
		return vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = vendorNumber;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public String getCreateOnly() {
		return createOnly;
	}

	public void setCreateOnly(String createOnly) {
		this.createOnly = createOnly;
	}

}
