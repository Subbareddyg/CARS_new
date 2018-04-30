/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.xml;

import com.belk.car.integrations.xml.ColorXML;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.List;
/**
 *
 * @author amuaxg1
 */

@XStreamAlias("sample")
public class SampleXML
{
	public SampleXML() {}
	public SampleXML(long id) { this.sampleID = id; }
	public SampleXML(long id, String type) { this.sampleID = id; this.type = type; }
	
	@XStreamAlias("returnInformation")
	public static class ReturnInformation 
	{
		public ReturnInformation() {}
		public ReturnInformation(ShippingAccountXML shippingAccount, List<String> instructions) { this.shippingAccount = shippingAccount; this.instructions = instructions; }

		ShippingAccountXML shippingAccount;
		public ShippingAccountXML getShippingAccount() { return this.shippingAccount; }
		public void setShippingAccount(ShippingAccountXML shippingAccount) { this.shippingAccount = shippingAccount; }	

		@XStreamImplicit(itemFieldName="instructions")
		List<String> instructions;
		public List<String> getInstructions() { return this.instructions; }
		public void setInstructions(List<String> instructions) { this.instructions = instructions; }			
	}


	@XStreamAlias("id")
	@XStreamAsAttribute
	private long sampleID; 
	public long getSampleID() { return this.sampleID; }
	public void setSampleID(long sampleID) { this.sampleID = sampleID; }
	
	@XStreamAsAttribute
	String	type;
	public String getType() { return this.type; }
	public void setType(String type) { this.type = (type != null) ? type.toLowerCase() : null; }

	ColorXML color;
	public ColorXML getColor() { return this.color; }
	public void setColor(ColorXML color) { this.color = color; }

	char	returnRequested;
	public char getReturnRequested() { return this.returnRequested; }
	public void setReturnRequested(char returnRequested) { this.returnRequested = returnRequested; }

	ReturnInformation returnInformation;
	public ReturnInformation getReturnInformation() { return this.returnInformation; }
	public void setReturnInformation(ReturnInformation returnInformation) { this.returnInformation = returnInformation; }

	char	silhouetteRequired;
	public char getSilhouetteRequired() { return this.silhouetteRequired; }
	public void setSilhouetteRequired(char silhouetteRequired) { this.silhouetteRequired = silhouetteRequired;	}
	
	@Override
	public String toString() {
		return new StringBuffer("SampleInfo [")
			.append(", sampleID:").append(sampleID)
			.append(", type:").append(type)
			.append(", color:").append(color)
			.append(", returnRequested:").append(returnRequested)
			.append(", silhouetteRequired:").append(silhouetteRequired)
			.append(" ]")
			.toString();
	}
}
