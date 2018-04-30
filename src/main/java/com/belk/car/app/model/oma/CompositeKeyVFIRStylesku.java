/**
 * 
 */
package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.belk.car.app.model.VendorStyle;

/**
 * @author afusy01
 *
 */

@Embeddable
public class CompositeKeyVFIRStylesku implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1219337488655437653L;
	private ItemRequest itemRequest;
	private String vendorStyleId;
	private String belkUpc;
	
	/**
	 * 
	 */
	public CompositeKeyVFIRStylesku() {
		super();
	}

	@ManyToOne
	@JoinColumn(name="vndr_item_fulfmnt_rqst_id",nullable=false)
	public ItemRequest getItemRequest() {
		return itemRequest;
	}

	public void setItemRequest(ItemRequest itemRequest) {
		this.itemRequest = itemRequest;
	}

	public void setVendorStyleId(String vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
	}
	@Column(name = "vendor_style_id", nullable = false, length=20)
	public String getVendorStyleId() {
		return vendorStyleId;
	}

	public void setBelkUpc(String belkUpc) {
		this.belkUpc = belkUpc;
	}
	@Column(name = "belk_upc", nullable = true, length=50)
	public String getBelkUpc() {
		return belkUpc;
	}

}
