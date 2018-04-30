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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 *
 * @author amuaxg1
 */
public class RRDUpdateMessageReader_TestData {

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
"  <histories>" + 
"    <sampleHistories>" + 
"      <history>" + 
"        <sample id=\"1858\"/>" + 
"        <event type=\"shipped\" qualifier=\"toStudio\" time=\"2008-03-21 09:03:20 EST\">" + 
"          <shipment carrier=\"FedEx\" trackingNumber=\"12345\"/>" + 
"	</event>" + 
"        <event type=\"arrived\" qualifier=\"toStudio\" time=\"2008-03-21 09:03:37 EST\"/>" + 
"        <event type=\"shipped\" qualifier=\"toBelk\" time=\"2008-03-21 09:03:54 EST\">" + 
"          <shipment carrier=\"UPS\" trackingNumber=\"67890\"/>" + 
"	</event>" + 
"        <event type=\"arrived\" qualifier=\"toBelk\" time=\"2008-03-21 09:03:15 EST\"/>" + 
"      </history>" + 
"    </sampleHistories>" + 
"    <samplePhotoHistories>" + 
"        <history>" + 
"          <sample id=\"1859\"/>" + 
"          <photo>" + 
"            <file>" + 
"              <location>toBelk</location>" + 
"              <path>photos</path>" + 
"              <name>" + 
"                <prefix>0000002_LEVI001_A_103</prefix>" + 
"                <extension>.tif</extension>" + 
"              </name>" + 
"            </file>" + 
"          </photo>" + 
"          <event type=\"sample sent\" time=\"2008-03-10 09:03:19 EST\"/>" + 
"          <event type=\"captured\" time=\"2008-03-15 12:53:05 EST\"/>" + 
"          <event type=\"approved\" time=\"2008-03-15 12:53:05 EST\"/>" + 
"          <event type=\"photo sent\" time=\"2008-03-25 11:00:00 EST\"/>" + 
"        </history>       " + 
"    </samplePhotoHistories>" + 
"  </histories>" + 
"</carsMessage>";

}
