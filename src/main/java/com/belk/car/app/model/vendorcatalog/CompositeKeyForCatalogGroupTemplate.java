/**
* This is the composite key class for table name CATALOG_GROUP_TEMPLATE
 * 
 * @version 1.0 14 Jan 2010
 * @author AFUSXS7 :SUBBU
 */
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class CompositeKeyForCatalogGroupTemplate implements java.io.Serializable{

	private static final long serialVersionUID = -1297574531391077302L;
	private Long vendorCatalogTmplID;
	private Long productGroupID;


	public CompositeKeyForCatalogGroupTemplate(Long shippingOption,
			Long fulfillmentServId) {
		this.productGroupID = shippingOption;
		this.vendorCatalogTmplID = fulfillmentServId;
	}

	public CompositeKeyForCatalogGroupTemplate() {
		super();
	}

	@Column(name = "PRODUCT_GROUP_ID", nullable = false, precision = 12, scale = 0)
	public Long getProductGroupID() {
		return this.productGroupID;
	}

	public void setProductGroupID(Long productGroupID) {
		this.productGroupID = productGroupID;
	}

	@Column(name = "VENDOR_CATALOG_TMPL_ID", nullable = false, precision = 12, scale = 0)
	public Long getVendorCatalogTmplID() {
		return this.vendorCatalogTmplID;
	}

	public void setVendorCatalogTmplID(Long vendorCatalogTmplID) {
		this.vendorCatalogTmplID = vendorCatalogTmplID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((vendorCatalogTmplID == null) ? 0 : vendorCatalogTmplID
						.hashCode());
		result = prime
				* result
				+ ((productGroupID == null) ? 0 : productGroupID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		CompositeKeyForCatalogGroupTemplate other = (CompositeKeyForCatalogGroupTemplate) obj;
		if (vendorCatalogTmplID == null) {
			if (other.vendorCatalogTmplID != null){
				return false;
			}
		}
		else if (!vendorCatalogTmplID.equals(other.vendorCatalogTmplID)){
			return false;
		}
		if (productGroupID == null) {
			if (other.productGroupID != null){
				return false;
			}
		}
		else if (!productGroupID.equals(other.productGroupID)){
			return false;
		}
		return true;
	}

	
	

}
