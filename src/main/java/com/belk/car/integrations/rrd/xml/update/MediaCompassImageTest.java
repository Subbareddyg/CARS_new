package com.belk.car.integrations.rrd.xml.update;



import java.io.FileReader;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class MediaCompassImageTest {

	private static final String Test_XML = "c:/tmp/tempphoto.xml";

	public static void main(String[] args) throws JAXBException, IOException {
		
		/*MCTempImageXML MCTempImageXML=new MCTempImageXML();
		MCTempImageXML.setFrom("RRD");
		MCTempImageXML.setTo("CARS");
		
		MCTempImageXML.setType("tempPhoto");
	
		MCTempImageXML.TempSamplePhoto temp=new MCTempImageXML.TempSamplePhoto();
		
		temp.setImageFileName("image1");
		temp.setCARid(101);
		temp.setSampleID(201);
		
		MCTempImageXML.getTempSamplePhoto().add(temp);
		
		// create JAXB context and instantiate marshaller
		JAXBContext context = JAXBContext.newInstance(MCTempImageXML.class);
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(MCTempImageXML, System.out);

		Writer w = null;
		try {
			w = new FileWriter(Test_XML);
			m.marshal(MCTempImageXML, w);
		} finally {
			try {
				w.close();
			} catch (Exception e) {
			}*/
	//	}
		JAXBContext context = JAXBContext.newInstance(MCTempImageXML.class);
		// get variables from our xml file, created before
		System.out.println();
		System.out.println("Output from our XML File: ");
		Unmarshaller um = context.createUnmarshaller();
		MCTempImageXML mcTemp = (MCTempImageXML) um.unmarshal(new FileReader(
				Test_XML));

		for (int i = 0; i < mcTemp.getTempSamplePhoto().toArray().length; i++) {
			
			System.out.println("CAR Id's"+mcTemp.getTempSamplePhoto().get(i).getCARid());
			System.out.println("Sample Id' "+mcTemp.getTempSamplePhoto().get(i).getSampleID());
			System.out.println("Image Name's"+mcTemp.getTempSamplePhoto().get(i).getImageFileName());
			

		}

	}
}

