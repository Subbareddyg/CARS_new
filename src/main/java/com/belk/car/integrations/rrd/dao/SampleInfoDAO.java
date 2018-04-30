/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.rrd.dao;
/**
 *
 * @author amuaxg1
 */

public class SampleInfoDAO
{
	long sampleID; 
	public long getSampleID() { return this.sampleID; }
	public void setSampleID(long sampleID) { this.sampleID = sampleID; }
	
	long carID;
	public long getCarID() { return this.carID; }
	public void setCarID(long carID) { this.carID = carID; }
	
	//Added for ExportRRD feed file image name
	String vendorStyleNumber;
	public String getVendorStyleNumber() {	return vendorStyleNumber;	}
	public void setVendorStyleNumber(String vendorStyleNumber) {	this.vendorStyleNumber = vendorStyleNumber;	}
	//<--

	String	type;
	public String getType() { return this.type; }
	public void setType(String type) { this.type = type; }
	
	String colorCode;
	public String getColorCode() { return this.colorCode; }
	public void setColorCode(String colorCode) { this.colorCode = colorCode; }
	
	String colorName;
	public String getColorName() { return this.colorName; }
	public void setColorName(String colorName) { this.colorName = colorName; }
	
	char returnRequested;
	public char getReturnRequested() { return this.returnRequested; }
	public void setReturnRequested(char returnRequested) { this.returnRequested = returnRequested; }
	
	char silhouetteRequired;
	public char getSilhouetteRequired() { return this.silhouetteRequired;}
	public void setSilhouetteRequired(char silhouetteRequired) { this.silhouetteRequired = silhouetteRequired;}

	String	carrier;
	public String getCarrier() { return this.carrier; }
	public void setCarrier(String carrier) { this.carrier = carrier; }
	
	String	shippingAccountNumber;
	public String getShippingAccountNumber() { return this.shippingAccountNumber; }
	public void setShippingAccountNumber(String shippingAccountNumber) { this.shippingAccountNumber = shippingAccountNumber; }

	@Override
	public String toString() {
		return new StringBuffer("SampleInfo [")
			.append(" carID:").append(carID)
			.append(", sampleID:").append(sampleID)
			.append(", vendorStyleNumber:").append(vendorStyleNumber) //Added for ExportRRD feed file image name
			.append(", type:").append(type)
			.append(", colorCode:").append(getColorCode())
			.append(", colorName:").append(getColorName())
			.append(", returnRequested:").append(returnRequested)
			.append(", silhouetteRequired:").append(silhouetteRequired)
			.append(", carrier:").append(carrier)
			.append(", shippingAccountNumber:").append(shippingAccountNumber)
			.append(" ]")
			.toString();
	}
	@Override
	public boolean equals( Object o ) {
		if (this == o) return true;
		if (o == null || (!(o instanceof SampleInfoDAO))) return false;
		SampleInfoDAO si = (SampleInfoDAO)o;
		return (this.sampleID == si.sampleID);
	}


	@Override
	public int hashCode()
	{
		int hash = 7;
		hash = 29 * hash + (int) (this.sampleID ^ (this.sampleID >>> 32));
		return hash;
	}	
}

