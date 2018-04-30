/**
 * 
 */
package com.belk.car.product.integration.request.data;

/**
 * Class to define the current requests supported as part of the 
 * new grouping integration with the PIM System.
 */
public enum GetGroupRequestType {
	
	GROUPID("GroupID"),
	VENDORSTYLE("VendorStyle");
	
	private String value;
    
	GetGroupRequestType(String value) {
		this.value = value;
	}
    
	public String getValue() {
    	return this.value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
