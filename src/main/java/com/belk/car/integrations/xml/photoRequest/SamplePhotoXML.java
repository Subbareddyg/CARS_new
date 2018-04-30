/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.xml.photoRequest;


import com.belk.car.integrations.xml.SampleXML;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.util.List;
/**
 *
 * @author amuaxg1
 */
@XStreamAlias("photo")
public class SamplePhotoXML extends PhotoXML 
{	
	{this.type = PhotoXML.Type.Sample;}

	List<SampleXML> samples;
	public List<SampleXML> getSamples() { return this.samples; }
	public void setSamples(List<SampleXML> samples) { this.samples = samples; }
}
