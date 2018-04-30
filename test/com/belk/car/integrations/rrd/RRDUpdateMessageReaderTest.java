/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.rrd;

import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML;
import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML.HistoriesXML;
import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML.HistoryXML.EventXML;
import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML.SampleHistoriesXML;
import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML.SampleHistoryXML;
import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML.SamplePhotoHistoriesXML;
import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML.SamplePhotoHistoryXML;
import com.belk.car.integrations.xml.SampleXML;
import com.belk.car.integrations.xml.photoRequest.PhotoXML;
import com.belk.car.integrations.xml.photoRequest.PhotoXML.FileXML;
import com.belk.car.integrations.xml.xstream.CarsEnumSingleValueConverter;
import com.thoughtworks.xstream.XStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author amuaxg1
 */
public class RRDUpdateMessageReaderTest {

    public RRDUpdateMessageReaderTest() {
    }


	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}


	@AfterClass
	public static void tearDownClass() throws Exception
	{
	}


	
//	/**
//	 * Test of main method, of class RRDUpdateMessageReader.
//	 */
//	@Test
//	public void main()
//	{
//		System.out.println("main");
//		String[] args = null;
//		RRDUpdateMessageReader.main(args);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}


	/**
	 * Test of readAndProcessXML method, of class RRDUpdateMessageReader.
	 */
	@Test
	public void readAndProcessXML()
	{
		System.out.println("readAndProcessXML");
		RRDUpdateMessageReader instance = new RRDUpdateMessageReader();
		
		instance.readAndProcessXML(testXML);
	}
	
	@Test
	public void testWrite() {
		RRDUpdateMessageXML xml = createTestMessageXML();
		RRDUpdateMessageReader instance = new RRDUpdateMessageReader() {
			{this.isUnitTest = true;}
			//public XStream xStream() {return super.createXStream();} 
		};	
		XStream xStream = instance.createXStream();//xStream();
		xStream.registerConverter(new CarsEnumSingleValueConverter(), XStream.PRIORITY_NORMAL+10);
		xStream.omitField(SampleXML.class, "returnRequested");
		xStream.toXML(xml, System.out);		
	}
	
	
	
	RRDUpdateMessageXML createTestMessageXML()
	{
		RRDUpdateMessageXML xml = new RRDUpdateMessageXML();

		SampleHistoriesXML sampleHistories = new SampleHistoriesXML();
		List<SampleHistoryXML> sampleHistoryList = new ArrayList<SampleHistoryXML>();

		SampleHistoryXML sampleHistoryXML = new SampleHistoryXML();
		{
			SampleXML sampleXML = new SampleXML();
			sampleXML.setSampleID(56789);
			sampleHistoryXML.setSample(sampleXML);

			EventXML eventXML = new EventXML();
			eventXML.setQualifier("toStudio");
			eventXML.setType("shipped");
			eventXML.setTime(new Date());

			EventXML eventXML2 = new EventXML();
			eventXML2.setQualifier("toBelk");
			eventXML2.setType("shipped");
			eventXML2.setTime(new Date());

			sampleHistoryXML.setEvents(Arrays.asList(eventXML, eventXML2));
		}
		sampleHistoryList.add(sampleHistoryXML);
		sampleHistories.setHistories(sampleHistoryList);

		SamplePhotoHistoriesXML samplePhotoHistoriesXML = new SamplePhotoHistoriesXML();
		List<SamplePhotoHistoryXML> samplePhotoHistoryXMLList = new ArrayList<SamplePhotoHistoryXML>();
		SamplePhotoHistoryXML samplePhotoHistoryXML = new SamplePhotoHistoryXML();
		{
			SampleXML sampleXML = new SampleXML(56789);
			SampleXML sampleXML2 = new SampleXML(23456);
			samplePhotoHistoryXML.setSamples(Arrays.asList(sampleXML, sampleXML2));

			PhotoXML photoXML = new PhotoXML();
			photoXML.setFileXML(new FileXML("Scene 7", "/xxx/yyy/zzz", "4567_345678_X_456", "A", ".tif"));
			samplePhotoHistoryXML.setPhoto(photoXML);

			EventXML eventXML = new EventXML();
			eventXML.setType("taken");
			eventXML.setTime(new Date());

			EventXML eventXML2 = new EventXML();
			eventXML2.setQualifier("toScene7");
			eventXML2.setType("uploaded");
			eventXML2.setTime(new Date());

			samplePhotoHistoryXML.setEvents(Arrays.asList(eventXML, eventXML2));
		}
		samplePhotoHistoryXMLList.add(samplePhotoHistoryXML);
		samplePhotoHistoriesXML.setHistories(samplePhotoHistoryXMLList);


		List<HistoriesXML> histories = new ArrayList<HistoriesXML>();
		histories.add(sampleHistories);
		histories.add(samplePhotoHistoriesXML);
		xml.setHistories(histories);

		return xml;
		
		
	}
	static final String testXML = 
		"<carsMessage from=\"RRD\" to=\"CARS\" type=\"update\">" +
			"<histories>" +
				"<sampleHistories>" +
					"<history>" +
						"<sample id=\"53\"/>" +
						"<event type=\"shipped\" qualifier=\"toBelk\" time=\"2008-03-07 11:50:41.323 EST\">" +
							"<shipment carrier=\"FedEx\" trackingNumber=\"76087-F23-1142X\"/>" +
						"</event>" +
						"<event type=\"shipped\" qualifier=\"toStudio\" time=\"2008-03-07 11:50:41.323 EST\"/>" +
					"</history>" +
				"</sampleHistories>" +
				"<samplePhotoHistories>" +
					"<history>" +
						"<sample id=\"6789\"/>  <!-- may be >1:  implied collection -->" +
						"<photo>" +
							"<file>" +
								"<location>Scene7</location>" +
								"<path>/xxx/yyy/zzz</path>" +
								"<name>" +
									"<prefix>1234567_ABC123_123_S</prefix>" +
									"<suffix>F01_0001</suffix>" +
									"<extension>.tif</extension>" +
								"</name>" +
							"</file>" +
						"</photo>" +
						"<event type=\"taken\" time=\"2008-03-07 11:50:41.323 EST\"/>" +
						"<event type=\"rejected\" time=\"2008-03-07 11:50:41.323 EST\"/>" +
						"<event type=\"reshot\" time=\"2008-03-07 11:50:41.323 EST\"/>" +
						"<event type=\"approved\" time=\"2008-03-07 11:50:41.323 EST\"/>" +
						"<event type=\"uploaded\" qualifier=\"toScene7\" time=\"2008-03-07 11:50:41.323 EST\"/>" +
					"</history>" +
				"</samplePhotoHistories>" +
			"</histories>" +
		"</carsMessage>";

	private static final int nSampleHistories = 1;
	private static final long sample0ID = 53;
	
	@Test
	public void testRead() {
		
		
		RRDUpdateMessageReader instance = new RRDUpdateMessageReader();
		RRDUpdateMessageXML rrdXML = instance.read(testXML);
		List<HistoriesXML> historiesXMLs = rrdXML.getHistories();
		assertTrue("there are two <histories>", historiesXMLs.size() == 2);
		HistoriesXML historiesXML0 = historiesXMLs.get(0);
		HistoriesXML historiesXML1 = historiesXMLs.get(1);
		SampleHistoriesXML sampleHistoriesXML = (SampleHistoriesXML) historiesXML0;
		SamplePhotoHistoriesXML samplePhotoHistoriesXML = (SamplePhotoHistoriesXML) historiesXML1;
		assertTrue("there is 1 <history> in the <sampleHistories>", sampleHistoriesXML.getHistories().size() == nSampleHistories);
		List<SampleHistoryXML> sampleHistoryXMLs = sampleHistoriesXML.getHistories();
		SampleHistoryXML sampleHistoryXML0 = sampleHistoryXMLs.get(0);
		SampleXML sampleXML0 = sampleHistoryXML0.getSample();
		assertTrue("sampleHistories/history/sample/@id = 53", sample0ID == sampleXML0.getSampleID());
		List<EventXML> eventXMLs = sampleHistoryXML0.getEvents();
		for (EventXML eventXML : eventXMLs) { System.out.println(eventXML.toString()); }

		
	}
	
}