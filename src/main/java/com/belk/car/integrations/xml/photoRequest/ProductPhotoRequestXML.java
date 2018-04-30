/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.xml.photoRequest;

import com.belk.car.integrations.xml.CarXML;
import com.belk.car.integrations.xml.ProductXML;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;
/**
 *
 * @author amuaxg1
 */

@XStreamAlias("productPhotoRequest")
public class ProductPhotoRequestXML 
{	
	@XStreamAlias("car")
	CarXML carXML;
	public CarXML getCarXML() { return this.carXML; }
	public void setCarXML(CarXML carXML) { this.carXML = carXML; }

	@XStreamAlias("product")
	ProductXML productXML;
	public ProductXML getProductXML() { return this.productXML; }
	public void setProductXML(ProductXML product) { this.productXML = product; }

	@XStreamAlias("photos")
	List<PhotoXML> photoXMLs;
	public List<PhotoXML> getPhotoXMLs() { return this.photoXMLs; }
	public void setPhotoXMLs(List<PhotoXML> photos) { this.photoXMLs = photos; }
}