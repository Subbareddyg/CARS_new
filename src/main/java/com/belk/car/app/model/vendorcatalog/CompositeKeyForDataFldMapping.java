/**
 * This is the composite key class for table name VENDOR_CATALOG_FLD_DATA_MAPPING
 * 
 * @version 1.0 14 Jan 2010
 * @author afusy07	 : Priyanka Gadia
 */
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CompositeKeyForDataFldMapping implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -566921051806915224L;
	private long vendorCatalogtTemplateId;
	private String vendorCatalogHeaderFieldName;
	private String vendorCatalogFieldValue;
	
	@Column(name = "VENDOR_CATALOG_TMPL_ID", nullable = false)
	public long getVendorCatalogtTemplateId() {
		return vendorCatalogtTemplateId;
	}
	public void setVendorCatalogtTemplateId(long vendorCatalogtTemplateId) {
		this.vendorCatalogtTemplateId = vendorCatalogtTemplateId;
	}
	
	@Column(name = "VNDR_CATL_HDR_FLD_NAME", nullable = false)
	public String getVendorCatalogHeaderFieldName() {
		return vendorCatalogHeaderFieldName;
	}
	public void setVendorCatalogHeaderFieldName(String vendorCatalogHeaderFieldName) {
		this.vendorCatalogHeaderFieldName = vendorCatalogHeaderFieldName;
	}
	
	@Column(name = "VNDR_CATL_FLD_VALUE", nullable = false)
	public String getVendorCatalogFieldValue() {
		return vendorCatalogFieldValue;
	}
	public void setVendorCatalogFieldValue(String vendorCatalogFieldValue) {
		this.vendorCatalogFieldValue = vendorCatalogFieldValue;
	}
	
	public CompositeKeyForDataFldMapping(long vendorCatalogtTemplateId,
			String vendorCatalogHeaderFieldName, String vendorCatalogFieldValue) {
		super();
		this.vendorCatalogtTemplateId = vendorCatalogtTemplateId;
		this.vendorCatalogHeaderFieldName = vendorCatalogHeaderFieldName;
		this.vendorCatalogFieldValue = vendorCatalogFieldValue;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((vendorCatalogFieldValue == null) ? 0
						: vendorCatalogFieldValue.hashCode());
		result = prime
				* result
				+ ((vendorCatalogHeaderFieldName == null) ? 0
						: vendorCatalogHeaderFieldName.hashCode());
		result = prime
				* result
				+ (int) (vendorCatalogtTemplateId ^ (vendorCatalogtTemplateId >>> 32));
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
		CompositeKeyForDataFldMapping other = (CompositeKeyForDataFldMapping) obj;
		if (vendorCatalogFieldValue == null) {
			if (other.vendorCatalogFieldValue != null){
				return false;
			}
		}
		else if (!vendorCatalogFieldValue.equals(other.vendorCatalogFieldValue)){
			return false;
		}
		if (vendorCatalogHeaderFieldName == null) {
			if (other.vendorCatalogHeaderFieldName != null){
				return false;
			}
		}
		else if (!vendorCatalogHeaderFieldName
				.equals(other.vendorCatalogHeaderFieldName)){
			return false;
		}
		if (vendorCatalogtTemplateId != other.vendorCatalogtTemplateId){
			return false;
		}
		return true;
	}
	public CompositeKeyForDataFldMapping() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
