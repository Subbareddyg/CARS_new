/**
 * 
 */
package com.belk.car.app.model.oma;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author afusyd9
 *
 */
@Embeddable
public class CompositeKeyVIFRStyle implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7816927050775819635L;
	private String vendorStyleId;
	private ItemRequest itemRequest;
	
	/**
	 * 
	 */
	public CompositeKeyVIFRStyle() {
		super();
	}

	public void setVendorStyleId(String vendorStyleId) {
		this.vendorStyleId = vendorStyleId;
	}
	@Column(name = "vendor_style_id", nullable = false, length=20)
	public String getVendorStyleId() {
		return vendorStyleId;
	}
	


	public void setItemRequest(ItemRequest itemRequest) {
		this.itemRequest = itemRequest;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vndr_item_fulfmnt_rqst_id", nullable = false)
	public ItemRequest getItemRequest() {
		return itemRequest;
	}


}
