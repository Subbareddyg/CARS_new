package com.belk.car.app.model;
import javax.persistence.Column;
import javax.persistence.Embeddable;
public class DBPromotionCollectionSkusId implements java.io.Serializable{

	//private static final long serialVersionUID = -4867725046932111115L;
	/**
	 * 
	 */
	
	private String skuCode;
	private String prodCode;

	public DBPromotionCollectionSkusId() {
	}

	public DBPromotionCollectionSkusId(String skuCode, String prodCode) {
		this.skuCode = skuCode;
		this.prodCode = prodCode;
	}

	@Column(name = "SKU_CODE", nullable = false, precision = 12, scale = 0)
	public String getSkuCode() {
		return this.skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	@Column(name = "PROD_CODE", nullable = false, precision = 12, scale = 0)
	public String getProdCode() {
		return this.prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof DBPromotionCollectionSkusId))
			return false;
		DBPromotionCollectionSkusId castOther = (DBPromotionCollectionSkusId) other;

		return (this.getSkuCode() == castOther.getSkuCode())
				&& (this.getProdCode() == castOther.getProdCode());
	}

	/*public int hashCode() {
		int result = 17;

		result = 37 * result + (int) this.getSkuCode();
		result = 37 * result + (int) this.getProdCode();
		return result;
	}*/

}
