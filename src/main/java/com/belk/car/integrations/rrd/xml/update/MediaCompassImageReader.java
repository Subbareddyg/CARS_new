package com.belk.car.integrations.rrd.xml.update;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.belk.car.app.Constants;
import com.belk.car.app.dto.MediaCompassImageDTO;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.MediaCompassImage;
import com.belk.car.app.model.Sample;
import com.belk.car.app.model.SampleTrackingStatus;
import com.belk.car.integrations.rrd.ProductPhotoRequestMessageWriter;

/**
 * 
 * Implemented to Read the Media compass image feed file 
 * and update the DB tables 
 * 
 * @author afusyq3-Syntel
 */

public class MediaCompassImageReader extends UniversalDaoHibernate {

	//private static final String xml = "c:/tmp/tempPhoto.xml";
	
	
	public void readAndProcessXML( File f ) 
			throws FileNotFoundException, JAXBException, IOException {
		processMCImageFeed(f);
	}
	private transient final Log log = LogFactory.getLog(MediaCompassImageReader.class);
	public MediaCompassImageReader() {initialize(); initSession();}
	
	protected void initialize() {} 
	
	private void initSession() { 
		try {
			if (this.getSessionFactory() != null  && this.getSession() != null) return;
			SessionFactory sessionFactory = null;
			Properties properties = new Properties();
			{
				InputStream inp = ClassLoader.getSystemResourceAsStream("jdbc.properties");
				{
					if (inp == null) {
						Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "System resource not found, finding class resource!");
						inp = this.getClass().getResourceAsStream("jdbc.properties");
					}			
					if (inp == null) {
						Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Class resource not found, finding class Thread resource!");
						inp = Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties");
					}			
				}
				if (inp != null) { properties.load(inp); } 
				else {
					Logger.getLogger(MediaCompassImageReader.class.getName()).log(Level.SEVERE, "Thread resource not found, don't know what to do now!  Could not load jdbc.properties file.");
					return;
				}
			}

			URL hibernateConfigurationURL;
			{
				String resourceName = "hibernate.cfg.xml";
				hibernateConfigurationURL = ClassLoader.getSystemResource(resourceName);
				if (hibernateConfigurationURL == null) { hibernateConfigurationURL = this.getClass().getResource(resourceName); }
				if (hibernateConfigurationURL == null) { hibernateConfigurationURL = Thread.currentThread().getContextClassLoader().getResource(resourceName); }
				if (hibernateConfigurationURL == null) {
					Logger.getLogger(MediaCompassImageReader.class.getName()).log(Level.SEVERE, "Hibernate configuration (hibernate.cfg.xml) not found, don't know what to do now!");
					return;
				}
			}
			
			AnnotationConfiguration cfg = new AnnotationConfiguration();
			cfg.configure(hibernateConfigurationURL);
			cfg.setProperties(properties);
			cfg.setProperty("hibernate.connection.autocommit","true");
			cfg.setProperty("hibernate.cache.use_second_level_cache","false");
			cfg.setProperty("hibernate.cache.use_query_cache","false");
			sessionFactory = cfg.buildSessionFactory();
			this.setSessionFactory(sessionFactory);
			
			Session session = SessionFactoryUtils.getSession(sessionFactory, true);
			if (!TransactionSynchronizationManager.hasResource(sessionFactory)) TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		}
		catch (Exception ex) {
			Logger.getLogger(MediaCompassImageReader.class.getName()).log(Level.SEVERE, null, ex);
		}		
	}


		protected void processMCImageFeed(File f)
					throws FileNotFoundException, JAXBException,IOException {
			if(log.isInfoEnabled()){
				log.info("Processing the temp Image XML file : " + f.getName());
			}
			List<MediaCompassImageDTO> mcImageDTOList= new ArrayList<MediaCompassImageDTO>();
			List<Long> allSampleIDs = new ArrayList<Long>();
			List<Long> allCarIDs=new ArrayList<Long>();
			
			
			try {
				JAXBContext context = JAXBContext.newInstance(MCTempImageXML.class);
				// get variables from xml file and  persist all
				Unmarshaller um = context.createUnmarshaller();
				MCTempImageXML carsMessage = (MCTempImageXML) um.unmarshal(new FileReader(f));
				if(log.isInfoEnabled()){
					log.info("Number of temp image file names found in XML file : " + carsMessage.getTempSamplePhoto().toArray().length);
				}
				//Loop through all carsMessages [media compass image details] and build a DTO list
			if (carsMessage!=null) {
				for (int i = 0; i < carsMessage.getTempSamplePhoto().toArray().length; i++) {
					MediaCompassImageDTO mcImageDTO= new MediaCompassImageDTO();  
					//add sampleIds and carIDs to list for fetching the Car and Sample objects
					allSampleIDs.add((long) carsMessage.getTempSamplePhoto().get(i).getSampleID());
					allCarIDs.add((long) carsMessage.getTempSamplePhoto().get(i).getCARid());
					mcImageDTO.setCarId((long) carsMessage.getTempSamplePhoto().get(i).getCARid());
					mcImageDTO.setSampleId((long) carsMessage.getTempSamplePhoto().get(i).getSampleID());
					mcImageDTO.setImageName(carsMessage.getTempSamplePhoto().get(i).getImageFileName());
					mcImageDTOList.add(mcImageDTO);
				}	
			 }
			
			//get the car, sample and mediaCompass objects for the all xml CarIds and sampleIds
			Map<Long,Car> carObjectsMap=getAllCars(allCarIDs);	
			Map<Long,MediaCompassImage> mediaCompassMap=getAllMediaComapss(allCarIDs);	
			Map<Long,Sample> sampleObjectsMap=getAllSamples(allSampleIDs);
			if(log.isInfoEnabled()){
				log.info("Number of unique CARS found in XML file: " + carObjectsMap.size());
				log.info("Number of unique Samples found in XML file: " + sampleObjectsMap.size());
			}
			int savedImageCounter = 0;
			Car carObj = null;  
			Sample sampleObj = null; 
			if ((mcImageDTOList!=null )&& (carObjectsMap!=null) &&(sampleObjectsMap!=null) ) {
				//loop through DTO list objects, assign car,sample and imageName to MediaCompassImage object
				for(MediaCompassImageDTO mcImageDTO:mcImageDTOList){
					try{
						if(log.isInfoEnabled()){
							log.debug("Trying to read and save Media compass image to the DB : " + mcImageDTO.getImageName());
						}
						
						if(mediaCompassMap.get(mcImageDTO.getCarId()) == null){
						boolean saveObject = true; 
						MediaCompassImage mcImage=new MediaCompassImage();
						// get the car and sample object from the map 
							if((carObj= carObjectsMap.get(mcImageDTO.getCarId())) != null){
								if(carObj.getMediaCompassImage() != null && carObj.getMediaCompassImage().size() ==0){
									mcImage.setCar(carObj);
								}else{
									saveObject= false; 
								}
							}else{
								log.error("not able to find the CAR object for: "+ mcImageDTO.getCarId());
								saveObject= false; 
							}
							if((sampleObj= sampleObjectsMap.get(mcImageDTO.getSampleId())) != null){ 
								mcImage.setSample(sampleObj);
							}else{
								log.error("not able to find the SAMPLE object for: "+ mcImageDTO.getSampleId());
								saveObject= false; 
							}
							
						mcImage.setCreatedDate(new Date());
						mcImage.setUpdatedDate(new Date());
						mcImage.setImageStatus(Constants.MC_IMAGE_RECEIVED); 
						mcImage.setImageName(mcImageDTO.getImageName());
						//saving media compass image to DB
						if(saveObject){ 
							if(log.isInfoEnabled()){
								log.debug("saveing Media compass image to the DB : " + mcImageDTO.getImageName());
							}
							getHibernateTemplate().save(mcImage);
							persistAll();
							savedImageCounter++;
						}
						else { 
							log.error("Could not save the MediaCompassImageobject for the CarID: "+mcImage.getCar().getCarId()); }
					}else{
						log.info("Media compass image already exist for this CAR "+ mcImageDTO.getCarId() + "So cound not save this image" + mcImageDTO.getImageName());
					}
				}catch(Exception e){
				    log.error("Could not save mediaCompassObject object to DB for CAR:"+ mcImageDTO.getCarId() + " proceeding with next CAR");
				}
			}
				
		 }
		 if(log.isInfoEnabled()){
			log.info("Total images saved to DB : " + savedImageCounter);
		 }
			
	  }catch (Exception ex) {
		log.error("Error processing media compass image temp XML file : " + ex);
	  }		

  }
	
	
	/**get the list of all samples objects for given sample id, added condition of 999 since oracle has limit of max 1000 elements in query
	 * @param allSampleIDs
	 * @return Map<Long,Sample>
	 */
	@SuppressWarnings("unchecked")
	private Map<Long,Sample> getAllSamples(List<Long> allSampleIDs){
		
		List<Sample> allSamplesTemp=null;
		List<Sample> allSamples= new ArrayList<Sample>();
		Map<Long,Sample> samplesForIds=new HashMap<Long, Sample>();
		
		 if (allSampleIDs !=null) {
			 while (allSampleIDs.size()>999) {
				List<Long> allSampleIDsTemp = allSampleIDs.subList(0, 998);
				allSampleIDs.remove(allSampleIDsTemp);
				allSamplesTemp = (List<Sample>) getHibernateTemplate()
						.find("from Sample sample " +
						"left join fetch sample.carSamples carSample " +
						"left join fetch carSample.car " +
						"where sample.sampleId in ("+ StringUtils.join(allSampleIDsTemp, ", ") +")");
			  if(allSamplesTemp != null){
				allSamples.addAll(allSamplesTemp);
			  } 
			 }
			//Get the all Sample's for the  allSampleIDs 
			 List<Sample> allSamplesTemp2= (List<Sample>) getHibernateTemplate()
					.find("from Sample sample " +
					"left join fetch sample.carSamples carSample " +
					"left join fetch carSample.car " +
					"where sample.sampleId in ("+ StringUtils.join(allSampleIDs, ", ") +")");
			 if(allSamplesTemp2 != null){
				 allSamples.addAll(allSamplesTemp2); 
			 }
			if (allSamples != null) {
				for (Sample sample: allSamples) { 
					samplesForIds.put(sample.getSampleId(),sample);
				}
			}
		 }
		 return samplesForIds;
	}
	
	
	/**get the list of all MCImages objects for given image names, added condition of 999 since oracle has limit of max 1000 elements in query
	 * @param carId
	 * @return Map<Long,MediaCompassImage>
	 */
	@SuppressWarnings("unchecked")
	private Map<Long, MediaCompassImage> getAllMediaComapss(List<Long> allCarIDs) {
	
	List<Object[]> allMediaCompass=new ArrayList<Object[]>();
	List<Object[]> allMediaCompassTemp=null;
	Map<Long,MediaCompassImage> MediaCompassForIds= new HashMap<Long, MediaCompassImage>();
	
	if (allCarIDs != null) {
		while (allCarIDs.size()>999){
			List<Long> carsIDsTemp = allCarIDs.subList(0, 998);
			allCarIDs.remove(carsIDsTemp);
			allMediaCompassTemp = (List<Object[]>) getHibernateTemplate()
					.find("from MediaCompassImage m ," +
						"Car c " +
						" where m.car.carId=c.carId and " +
						"c.carId in ("+ StringUtils.join(carsIDsTemp, ", ") +")");
			allMediaCompass.addAll(allMediaCompassTemp);
		 } 
		//Get the all Car's for the allCarIDs 
		List<Object[]> allCarsTemp2 = (List<Object[]>) getHibernateTemplate()
				.find("from MediaCompassImage m ," +
						"Car c " +
						" where m.car.carId=c.carId and " +
						 " c.carId in ("+ StringUtils.join(allCarIDs, ", ") +")");
		if (allMediaCompass != null) {
			allMediaCompass.addAll(allCarsTemp2);
		}
		if (allMediaCompass != null) {
			for (Object[] mcobject: allMediaCompass) { 
				MediaCompassForIds.put(((MediaCompassImage)mcobject[0]).getCar().getCarId(),((MediaCompassImage)mcobject[0]));
			}
		}
	} 
	return MediaCompassForIds;
  }
	
	/**
	 * @param carId
	 * @return Map<Long,Car>
	 * get the list of all car objects for given CAR id, added condition of 999 since oracle has limit of max 1000 elements in query
	 */
	@SuppressWarnings("unchecked")
	private Map<Long,Car> getAllCars(List<Long> allCarIDs) {
	
	List<Car> allCars=new ArrayList<Car>();
	List<Car> allCarsTemp=null;
	Map<Long,Car> carForIds= new HashMap<Long, Car>();
	
	if (allCarIDs != null) {
		while (allCarIDs.size()>999){
			List<Long> carsIDsTemp = allCarIDs.subList(0, 998);
			allCarIDs.remove(carsIDsTemp);
			allCarsTemp = (List<Car>) getHibernateTemplate()
					.find("from Car c " +
					 "where c.carId in ("+ StringUtils.join(carsIDsTemp, ", ") +")");
			allCars.addAll(allCarsTemp);
		 } 
		//Get the all Car's for the allCarIDs 
		List<Car> allCarsTemp2 = (List<Car>) getHibernateTemplate()
			 .find("from Car c " +
			 "where c.carId in ("+ StringUtils.join(allCarIDs, ", ") +")");
		if (allCars != null) {
			allCars.addAll(allCarsTemp2);
		}
		if (allCars != null) {
			for (Car car: allCars) { 
				carForIds.put(car.getCarId(),car);
			}
		}
	} 
	return carForIds;
  }
	
	
	
	/*private void addAsPersistent(Collection c) {
			getHibernateTemplate().saveOrUpdateAll(c);
	}*/
	private void persistAll() {
			getHibernateTemplate().flush();
	}
	
	public static void main(String[] args) {
		MediaCompassImageReader reader = new MediaCompassImageReader();
	}
}
