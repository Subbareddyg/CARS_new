/**
 * 
 */
package com.belk.car.product.integration.request.data;

/**
 * Class to define the current requests supported as part of the 
 * new integration with the PIM System.
 */
public enum GetRequestType {
	
	STYLE("Style"), 
	STYLE_COLOR("StyleColor"), 
	SKU("SKU"), 
	PACK("Complex Pack"), 
	PACK_COLOR("Pack Color");
	private String value;
    GetRequestType(String value) {
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
