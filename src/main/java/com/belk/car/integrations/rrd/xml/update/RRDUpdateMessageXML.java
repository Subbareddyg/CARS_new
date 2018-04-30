/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.rrd.xml.update;

import com.belk.car.integrations.xml.CarsMessageXML;
import com.belk.car.integrations.xml.SampleXML;
import com.belk.car.integrations.xml.photoRequest.PhotoXML;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import java.util.Date;
import java.util.List;
/**
 *
 * @author amuaxg1
 */

@XStreamAlias("carsMessage")
public class RRDUpdateMessageXML extends CarsMessageXML {
	
	{type = CarsMessageXML.Type.Update;}
	
	private List<HistoriesXML> histories;
	public List<HistoriesXML> getHistories() { return this.histories; }
	public void setHistories(List<HistoriesXML> histories) { this.histories = histories; }
	
	
	public static interface HistoriesXML {
		
	}
	@XStreamAlias("sampleHistories")
	public static class SampleHistoriesXML implements HistoriesXML {
		@XStreamImplicit(itemFieldName="history")
		List<SampleHistoryXML> histories;
		public List<SampleHistoryXML> getHistories() { return histories; }
		public void setHistories(List<SampleHistoryXML> histories) { this.histories = histories; }
	}
	@XStreamAlias("samplePhotoHistories")
	public static class SamplePhotoHistoriesXML implements HistoriesXML {
		@XStreamImplicit(itemFieldName="history")
		List<SamplePhotoHistoryXML> histories;
		public List<SamplePhotoHistoryXML> getHistories() { return histories; }
		public void setHistories(List<SamplePhotoHistoryXML> histories) { this.histories = histories; }
	}
	
	@XStreamAlias("history")
	public static abstract class HistoryXML
	{
		//@XStreamImplicit(itemFieldName="event")
		//List<EventXML> events;
		public abstract List<EventXML> getEvents() ;
		public abstract void setEvents(List<EventXML> events) ;
		
		
		@XStreamAlias("event")
		public static class EventXML 
		{
			@XStreamAsAttribute
			String type;
			public String getType() { return this.type; }
			public void setType(String type) { this.type = type; }
			
			@XStreamAsAttribute
			String qualifier;
			public String getQualifier() { return this.qualifier; }
			public void setQualifier(String qualifier) { this.qualifier = qualifier; }
			
			@XStreamAsAttribute
			Date time;
			public Date getTime() { return this.time; }
			public void setTime(Date time) { this.time = time; }
			
			@Override
			public String toString() {
				return new StringBuffer("EventXML[type: ").append(type).append(", qualifier:").append(qualifier).append(", time:").append(time).append(']').toString();
			}
		}
		
		@XStreamAlias("event")
		public static class ShippedEventXML extends EventXML
		{
			{this.type = "shipped";}
			
			ShipmentXML shipment;
			public ShipmentXML getShipment() { return this.shipment; }
			public void setShipment(ShipmentXML shipment) { this.shipment = shipment; }
			
		}
	}
	
	
	@XStreamAlias("shipment")
	public static class ShipmentXML {
		@XStreamAlias("carrier")
		@XStreamAsAttribute
		String carrierName;
		public String getCarrierName() { return this.carrierName; }
		public void setCarrierName(String carrierName) { this.carrierName = carrierName; }
		
		@XStreamAsAttribute
		String trackingNumber;
		public String getTrackingNumber() { return this.trackingNumber; }
		public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
	}
	
	@XStreamAlias("history")
	public static class SampleHistoryXML extends HistoryXML {
		SampleXML sample;
		public SampleXML getSample() { return this.sample; }
		public void setSample(SampleXML sample) { this.sample = sample; }
		
		
		@XStreamImplicit(itemFieldName="event")
		List<EventXML> events;
		public List<EventXML> getEvents() { return this.events; }
		public void setEvents(List<EventXML> events) { this.events = events; }
	}
	@XStreamAlias("history")
	public static class SamplePhotoHistoryXML extends HistoryXML 
	{	
		@XStreamImplicit(itemFieldName="sample")
		List<SampleXML> samples;
		public List<SampleXML> getSamples() { return this.samples; }
		public void setSamples(List<SampleXML> samples) { this.samples = samples; }
				
		PhotoXML photo;
		public PhotoXML getPhoto() { return this.photo; }
		public void setPhoto(PhotoXML photo) { this.photo = photo; }
		
		@XStreamImplicit(itemFieldName="event")
		List<EventXML> events;
		public List<EventXML> getEvents() { return this.events; }
		public void setEvents(List<EventXML> events) { this.events = events; }
	}
}
