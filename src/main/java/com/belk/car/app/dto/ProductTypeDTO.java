package com.belk.car.app.dto;

/**
 * The DTO class that gets the product type values based of the query in Product
 * group dao hibernate.
 * 
 * @author afusxs7 : Subbu
 * 
 */
public class ProductTypeDTO implements java.io.Serializable {

	private static final long serialVersionUID = 7185104387459530319L;
	private long productTypeId;
	private String productGroupDesc;
	private String name;
	private String descr;
	private String statusCd;
	private String newProdGroup;

	public long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(long productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getProductGroupDesc() {
		return productGroupDesc;
	}

	public void setProductGroupDesc(String productGroupDesc) {
		this.productGroupDesc = productGroupDesc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getStatusCd() {
		return statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	public void setNewProdGroup(String newProdGroup) {
		this.newProdGroup = newProdGroup;
	}

	public String getNewProdGroup() {
		return newProdGroup;
	}

}
