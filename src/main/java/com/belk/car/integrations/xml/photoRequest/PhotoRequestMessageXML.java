/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.xml.photoRequest;

import com.belk.car.integrations.xml.CarsMessageXML;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;
/**
 *
 * @author amuaxg1
 */

@XStreamAlias("carsMessage")
public class PhotoRequestMessageXML extends CarsMessageXML 
{
		
	public PhotoRequestMessageXML(String from, String to) { 
		this.from = from;
		this.to = to;
		this.type = CarsMessageXML.Type.PhotoRequests;
	}

	@XStreamAlias("productPhotoRequests")
	public List<ProductPhotoRequestXML> productPhotoRequestXMLs;
	public List<ProductPhotoRequestXML> getProductPhotoRequestXMLs() { return this.productPhotoRequestXMLs; }
	public void setProductPhotoRequestXMLs(List<ProductPhotoRequestXML> productPhotoRequests) { this.productPhotoRequestXMLs = productPhotoRequests; }	

}
