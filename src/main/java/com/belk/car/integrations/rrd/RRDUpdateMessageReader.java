/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.rrd;



import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarImageId;
import com.belk.car.app.model.CarSample;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageLocationType;
import com.belk.car.app.model.ImageProvider;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.Sample;
import com.belk.car.app.model.SampleShippingInformation;
import com.belk.car.app.model.SampleShippingInformationId;
import com.belk.car.app.model.SampleTrackingStatus;
import com.belk.car.app.model.ShippingInformation;
import com.belk.car.app.model.ShippingType;
import com.belk.car.app.model.Status;
import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML.HistoryXML.EventXML;
import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML;
import com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML.HistoryXML.ShippedEventXML;
import static com.belk.car.integrations.rrd.xml.update.RRDUpdateMessageXML.*;
import com.belk.car.integrations.xml.CarsMessageXML;
import com.belk.car.integrations.xml.SampleXML;
import com.belk.car.integrations.xml.photoRequest.PhotoXML;
import com.belk.car.integrations.xml.xstream.CarsEnumSingleValueConverter;
import com.belk.car.integrations.xml.xstream.SubclassResolutionByAttributeConverter;
import com.thoughtworks.xstream.XStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
/**
 *
 * @author amuaxg1
 */

public class RRDUpdateMessageReader extends UniversalDaoHibernate {

	Date now = new Date();
	protected boolean isUnitTest = false;
	protected boolean persistDuringUnitTest = false;
	private static final String RRD_FEED = Config.SYSTEM_USER;
	
	public void readAndProcessXML( String xml ) {
		RRDUpdateMessageXML rrdUpdateMessageXML = read(xml);
		processRRDUpdateMessageXML(rrdUpdateMessageXML);
	}

	protected static void readAndProcessXML( RRDUpdateMessageReader reader, String xml ) {
		reader.readAndProcessXML(xml);	
	}
	
	public RRDUpdateMessageReader() { initialize(); initSession();}

	/**
	 * This should be set to false if running from inside webapp.
	 */
	private boolean isStandalone = false;
	public void setStandalone(boolean isStandalone) { this.isStandalone = isStandalone; }
	public boolean isStandalone() { return isStandalone; }
	
	protected void initialize() {} // just a hook


	
	
	private void initSession() { 
		//if (!this.isStandalone()) return;
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
					Logger.getLogger(RRDUpdateMessageReader.class.getName()).log(Level.SEVERE, "Thread resource not found, don't know what to do now!  Could not load jdbc.properties file.");
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
					Logger.getLogger(RRDUpdateMessageReader.class.getName()).log(Level.SEVERE, "Hibernate configuration (hibernate.cfg.xml) not found, don't know what to do now!");
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
			Logger.getLogger(RRDUpdateMessageReader.class.getName()).log(Level.SEVERE, null, ex);
		}		
	}

	protected XStream createXStream()
	{
		XStream xStream = new XStream();
		xStream.registerConverter(new CarsEnumSingleValueConverter(CarsMessageXML.Type.class), XStream.PRIORITY_NORMAL+10);
		
		SubclassResolutionByAttributeConverter subClassConverter = new SubclassResolutionByAttributeConverter(xStream.getMapper(), xStream.getReflectionProvider());
		Class eventXMLClass = RRDUpdateMessageXML.HistoryXML.EventXML.class;
		subClassConverter.putDifferentiatingAttribute(eventXMLClass, "type");
		subClassConverter.addSubclassResolution(eventXMLClass, "shipped", RRDUpdateMessageXML.HistoryXML.ShippedEventXML.class);
		xStream.registerConverter(subClassConverter, XStream.PRIORITY_NORMAL+10);
		
		xStream.registerLocalConverter(RRDUpdateMessageXML.HistoryXML.class, "events", subClassConverter);
		xStream.registerLocalConverter(CarsMessageXML.class, "type", new CarsEnumSingleValueConverter(CarsMessageXML.Type.class));
		xStream.registerLocalConverter(CarsMessageXML.Type.class, "type", new CarsEnumSingleValueConverter(CarsMessageXML.Type.class));
		xStream.processAnnotations(new Class[] {
			RRDUpdateMessageXML.class, 
			RRDUpdateMessageXML.HistoriesXML.class, 
			RRDUpdateMessageXML.HistoryXML.class,
			RRDUpdateMessageXML.HistoryXML.EventXML.class,
			RRDUpdateMessageXML.HistoryXML.ShippedEventXML.class,
			RRDUpdateMessageXML.SampleHistoriesXML.class,
			RRDUpdateMessageXML.SampleHistoryXML.class,
			RRDUpdateMessageXML.SamplePhotoHistoriesXML.class,
			RRDUpdateMessageXML.SamplePhotoHistoryXML.class,
			RRDUpdateMessageXML.ShipmentXML.class			
		});
		return xStream;
	}
	
	protected RRDUpdateMessageXML read(String xml) {
		XStream xStream = createXStream();
		
		RRDUpdateMessageXML rrdUpdateMessageXML = new RRDUpdateMessageXML();
		return (RRDUpdateMessageXML) xStream.fromXML(xml, rrdUpdateMessageXML);
	}
	
	private void processRRDUpdateMessageXML(RRDUpdateMessageXML rrdUpdateMessageXML) 
	{
		List<HistoriesXML> histories = rrdUpdateMessageXML.getHistories();
		
		SampleHistoriesXML sampleHistoriesXML = null;
		SamplePhotoHistoriesXML samplePhotoHistoriesXML = null;
		for (HistoriesXML currHistories : histories) 
		{
			if (currHistories instanceof SampleHistoriesXML) {
				sampleHistoriesXML = (SampleHistoriesXML) currHistories;
				if (sampleHistoriesXML != null && sampleHistoriesXML.getHistories() != null) {
					processSampleHistoryXMLs( sampleHistoriesXML.getHistories() );
				}
			}
			else if (currHistories instanceof SamplePhotoHistoriesXML) { 
				samplePhotoHistoriesXML = (SamplePhotoHistoriesXML) currHistories; 
				if (samplePhotoHistoriesXML != null && samplePhotoHistoriesXML.getHistories() != null) {
					processSamplePhotoHistoryXMLs( samplePhotoHistoriesXML.getHistories() );
				}
			}
		}
		persistAll();
		getHibernateTemplate().clear();
	}
	private void processSampleHistoryXMLs(List<SampleHistoryXML> sampleHistoryXMLs)
	{
		Map<Long, SampleHistoryXML> sampleID2SampleHistoryXML = new HashMap<Long, SampleHistoryXML>();
		{
			for (SampleHistoryXML sampleHistoryXML : sampleHistoryXMLs) {
				sampleID2SampleHistoryXML.put( sampleHistoryXML.getSample().getSampleID(), sampleHistoryXML );
			}
		}
		List<Sample> samples;
		{			
			String sampleIDsStr = StringUtils.join(sampleID2SampleHistoryXML.keySet(), ","); 

			samples = (List<Sample>) getHibernateTemplate()
				.find("from Sample sample " +
				"left join fetch sample.sampleShippingInformations ssi " +
				"left join fetch ssi.shippingInformation si " + 
				"where sample.sampleId in ("+sampleIDsStr+")");
		}
		if (samples == null) return;

		// for each,
		// 1.  get the ShippingInformations already attached to the sample (in the database) 		
		// 2.  look in the XML for currentSample/event[@type=shipping], and create and upsert any ShippingInformations there 
		// 3.  update the status of the sample from the latest event
		
		for (Sample sample : samples) 
		{
			if (isUnitTest) System.out.println("Sample id "+sample.getSampleId());
			
			SampleHistoryXML sampleHistoryXML = sampleID2SampleHistoryXML.get(sample.getSampleId());
			List<EventXML> eventXMLs = sampleHistoryXML.getEvents();
			
			if (isBlank(eventXMLs)) continue;
			
			Set<ShippedEventXML> shippedEventXMLs = new HashSet<ShippedEventXML>();
			{
				for (EventXML eventXML : eventXMLs) {
					if ("shipped".equals(eventXML.getType()))  { shippedEventXMLs.add((ShippedEventXML) eventXML); }
				}	
			}			
			if (!isBlank(shippedEventXMLs)) upsertSampleShippingInformation(sample, shippedEventXMLs);
			
			SampleTrackingStatus newSampleTrackingStatus;
			{
				EventXML mostRecentEventXML = getMostRecentEventXML(eventXMLs);
				newSampleTrackingStatus = getSampleTrackingStatus(mostRecentEventXML);
			}			
			if (newSampleTrackingStatus != null) sample.setSampleTrackingStatus(newSampleTrackingStatus);
			sample.setUpdatedDate(now);
			
			sample.setImageProvider(nameToImageProvider.get("RR Donnelly Studio"));
		}
		addAsPersistent(samples);
		persistAll();
	}

	
	
	private void processSamplePhotoHistoryXMLs(List<SamplePhotoHistoryXML> samplePhotoHistoryXMLs)
	{
		readSamplesAndStoreByID(samplePhotoHistoryXMLs);

		Map<SamplePhotoHistoryXML, Map<Sample, Image>> samplePhotoHistoryXML2Image = new HashMap<SamplePhotoHistoryXML, Map<Sample, Image>>();
		{
			for (SamplePhotoHistoryXML samplePhotoHistoryXML : samplePhotoHistoryXMLs) 
			{
				// there *won't* be any images (per Vasan) -- we are creating them now.
				// create an image
				List<Sample> samples = getSamplesFor(samplePhotoHistoryXML);
				if (samples == null) continue;

				Map<Sample, Image> images = new HashMap<Sample, Image>() ;
				//Sample mainSample = samples.get(0);
				for(Sample mainSample:samples) {
				//if (mainSample == null) continue;

				// Variables for the image
				String fileName; 
				PhotoXML photoXML = samplePhotoHistoryXML.getPhoto();
				try {fileName = photoXML.getFileXML().getNameXML().getFullName();} catch (NullPointerException nex) {continue;}
				
				ImageLocationType imageLocationType;
				{
					String imageLocationTypeKey;
					try {
						imageLocationTypeKey = photoXML.getFileXML().getLocation();
					}	
					catch (NullPointerException nex) { imageLocationTypeKey = "Belk FTP Site"; }
					imageLocationType = nameToImageLocationType.get(imageLocationTypeKey);
					if (imageLocationType == null && !"Belk FTP Site".equals(imageLocationTypeKey)) {
						imageLocationType = nameToImageLocationType.get("Belk FTP Site");
					}
				}
				
				//String imageTypeCode;
				//boolean isMainPic = false; 
				//for (Sample sample : samples) {if (sample != null && !"Y".equals(sample.getIsSwatch())) {isMainPic = true; break;} }
				boolean isMainPic = !("Y".equals(mainSample.getIsSwatch())) ;
				String imageTypeCode = (isMainPic) ? "MAIN" : "SWATCH";

				// create and load the image
				Image image = new Image();
				image.setApprovalNotesText("");
				image.setCarImages(new HashSet<CarImage>());
				image.setCreatedBy(RRD_FEED);
				image.setCreatedDate(now);
				image.setDescription(" ");
				image.setImageFinalUrl(" ");
				image.setImageLocation(fileName);
				image.setImageLocationType(imageLocationType);
				image.setImageLocationTypeCd(imageLocationType.getImageLocationTypeCd());
				image.setImageSourceType(this.codeToImageSourceType.get("FROM_SAMPLE"));
				image.setImageSourceTypeCd("FROM_SAMPLE");
				image.setImageTrackingStatus(this.codeToImageTrackingStatus.get("RECEIVED"));
				image.setImageTrackingStatusCd("RECEIVED");
				image.setImageType(this.codeToImageType.get(imageTypeCode));
				image.setImageTypeCd(imageTypeCode);
				image.setNotesText(" ");
				image.setReceivedDate(now);
				image.setRequestDate(now);
				image.setSample(mainSample);
				image.setStatusCd("ACTIVE");
				image.setUpdatedBy(RRD_FEED);
				image.setUpdatedDate(now);
				image.setImageProcessingStatusCd(Image.PROCESSING_STATUS_PENDING) ;
				
				images.put(mainSample, image);
				}
				// put the new (unIDed) image in a map keyed to samplePhotoHistoryXML
				samplePhotoHistoryXML2Image.put(samplePhotoHistoryXML, images);	
			}
		}
		
		// save all the new Images so that they are IDed
		List<Image> myImages = new ArrayList<Image>();
		for(Map<Sample, Image> imageMap: samplePhotoHistoryXML2Image.values()){
			myImages.addAll(imageMap.values());
		}
		addAsPersistent(myImages);
		persistAll();
		
		// make CarImages after getting Image ID
		Set<CarImage> newCarImages = new HashSet<CarImage>();
		for (SamplePhotoHistoryXML samplePhotoHistoryXML : samplePhotoHistoryXMLs) 
		{
			List<Sample> samples = getSamplesFor(samplePhotoHistoryXML);
			if (samples == null) continue;
			
			Map<Sample, Image> images = samplePhotoHistoryXML2Image.get(samplePhotoHistoryXML); // remember our Map?
			if (images == null) continue;
			
			// for each sample.carSample, get its car and link the Car to the Image of this SamplePhotoHistoryXML
			for (Sample sample : samples) 
			{ 
				if (sample == null) continue;
				Set<CarSample> carSamples = sample.getCarSamples();
				if (carSamples == null) continue;
				
				for (CarSample carSample : carSamples) 
				{
					// get the Car
					Car car = carSample.getCar();
					Image image = images.get(sample) ;
					if (image != null) {
						// create a CarImage
						CarImage carImage = new CarImage();
						carImage.setCar(car);
						carImage.setCreatedBy(RRD_FEED);
						carImage.setCreatedDate(now);
						carImage.setId(new CarImageId(car.getCarId(), image.getImageId()));
						carImage.setImage(image);
						carImage.setUpdatedBy(RRD_FEED);
						carImage.setUpdatedDate(now);
						
						newCarImages.add(carImage);
					}
				}
			}
		}
		// save the new CarImages (and get IDs for them)
		addAsPersistent(newCarImages);
		persistAll();
		
		// set the new IDed CarImages on car.carImages and image.carImages
		for (CarImage carImage: newCarImages) {
			Car car = carImage.getCar();
			Image image = carImage.getImage();
			car.getCarImages().add(carImage);
			image.getCarImages().add(carImage);
		}
		
		// save the whole thing
		persistAll();
		
	}

	

	
	
	private void upsertSampleShippingInformation( Sample sample, Set<ShippedEventXML> shippedEventXMLs ) 
	{	
		// get the ShippingInformations already attached to the Sample
		Set<ShippingInformation> oldShippingInformations = new HashSet();
		{			
			Set<SampleShippingInformation> ssis = sample.getSampleShippingInformations();
			if (ssis != null) {
				for (SampleShippingInformation ssi : ssis) {
					ShippingInformation si = ssi.getShippingInformation();
					oldShippingInformations.add(si);
				}
			}
		}
		
		// update old ShippingInformations and create new ones
		Set<SampleShippingInformation> newSampleShippingInformations = new HashSet<SampleShippingInformation>();
		Set<ShippingInformation> newShippingInformations = new HashSet();
		{
			creationOfShippingInformations:
			for (ShippedEventXML shippedEventXML : shippedEventXMLs)
			{
				// a trackingNumber and a ShippingType are required for a ShippingInformation to be created
				String trackingNumber;
				ShippingType shippingType;
				try {
					trackingNumber = shippedEventXML.getShipment().getTrackingNumber();
					if (StringUtils.isBlank(trackingNumber)) throw new IllegalArgumentException();
					
					shippingType = carrierNameToShippingType.get(shippedEventXML.getShipment().getCarrierName());
				}
				catch (NullPointerException nex) { continue creationOfShippingInformations; }
				catch (IllegalArgumentException iex) { continue creationOfShippingInformations; }
				
				
				boolean siIsNew = false;
				ShippingInformation si = null;
				{
					if (si == null) si = findShippingInformationInOldShippingInformations(trackingNumber, oldShippingInformations);
					if (si == null) si = findShippingInformationInDB(trackingNumber);
					if (si == null) { 
						si = new ShippingInformation(); 
						siIsNew = true; 
					}
									
					si.setShippingDate(startOfDayForDate(shippedEventXML.getTime()));  //only use year/month/day of the timestamp
					si.setShippingCost(BigDecimal.ZERO);
					si.setIsReturnLabelEnclosed(ShippingInformation.FLAG_NO);
					si.setShippingType(shippingType);
					si.setShipTrackingNumber(trackingNumber);
					if (siIsNew) si.setCreatedBy(RRD_FEED);
					si.setUpdatedBy(RRD_FEED);					
					if (siIsNew) si.setCreatedDate(now);
					si.setUpdatedDate(now);
					si.setStatusCd(Status.ACTIVE);			
				}				
				if (siIsNew) newShippingInformations.add(si);
		
				
				SampleShippingInformation ssi = new SampleShippingInformation();
				ssi.setSample(sample);
				ssi.setShippingInformation(si);
				SampleShippingInformationId ssiID = new SampleShippingInformationId(si.getShippingInfoId(), sample.getSampleId());
				ssi.setId(ssiID);
				ssi.setCreatedBy(RRD_FEED);
				ssi.setUpdatedBy(RRD_FEED);
				ssi.setCreatedDate(now);
				ssi.setUpdatedDate(now);
				
				if (!sample.getSampleShippingInformations().contains(ssi)) newSampleShippingInformations.add(ssi);
			}
		}
		// persist the newShippingInformations
		addAsPersistent(newShippingInformations);
		// persist the old ShippingInformations
		persistAll();
		
		// put the IDs from the newly-persisted ShippingInformations into the SampleShippingInformations...
		for (SampleShippingInformation ssi : newSampleShippingInformations) { 
			long shippingInfoId = ssi.getShippingInformation().getShippingInfoId();
			ssi.getId().setShippingInfoId(shippingInfoId); 
		}
		//...and save them
		sample.getSampleShippingInformations().addAll(newSampleShippingInformations);
		persistAll();
	}
	
	private abstract class EnumHashCache<A, B> 
	{
		private HashMap<A,B> map;
		private String queryStr;
		public EnumHashCache(String queryStr) {this.queryStr = queryStr;}
		public B get(A a) {
			if (a == null) return null;
			if (map == null) {
				List<B> list = (List<B>) getHibernateTemplate().find(queryStr);
				map = new HashMap<A,B>();
				for(B b:list) { map.put(getAFromB(b), b); }
			}
			return map.get(a);
		}
		protected abstract A getAFromB(B b);
	}
	
	EnumHashCache<String, ImageLocationType> nameToImageLocationType = new EnumHashCache<String, ImageLocationType>("from ImageLocationType") {
		protected String getAFromB(ImageLocationType type) { return type.getName(); }
	};
	EnumHashCache<String, ImageSourceType> codeToImageSourceType = new EnumHashCache<String, ImageSourceType>("from ImageSourceType") {
		protected String getAFromB(ImageSourceType type) { return type.getImageSourceTypeCd(); }
	};
	EnumHashCache<String, ImageType> codeToImageType = new EnumHashCache<String, ImageType>("from ImageType") {
		protected String getAFromB(ImageType type) { return type.getImageTypeCd(); }
	};
	EnumHashCache<String, ImageTrackingStatus> codeToImageTrackingStatus = new EnumHashCache<String, ImageTrackingStatus>("from ImageTrackingStatus") {
		protected String getAFromB(ImageTrackingStatus type) { return type.getImageTrackingStatusCd() ; }
	};
	EnumHashCache<String, ShippingType> carrierNameToShippingType = new EnumHashCache<String, ShippingType>("from ShippingType") {
		protected String getAFromB(ShippingType type) { return type.getName(); }
	};
	EnumHashCache<String, SampleTrackingStatus> nameToSampleTrackingStatus = new EnumHashCache<String, SampleTrackingStatus>("from SampleTrackingStatus") {
		protected String getAFromB(SampleTrackingStatus type) { return type.getSampleTrackingStatusCd(); }
	};
	EnumHashCache<String, ImageProvider> nameToImageProvider = new EnumHashCache<String, ImageProvider>("from ImageProvider") {
		protected String getAFromB(ImageProvider type) { return type.getName(); }
	};
	
	private Map<Long, Sample> sampleID2Sample = new HashMap<Long, Sample>();
	
	private void readSamplesAndStoreByID( List<SamplePhotoHistoryXML> samplePhotoHistoryXMLs ) 
	{
		List<Long> allSampleIDs = new ArrayList<Long>();
		for (SamplePhotoHistoryXML samplePhotoHistoryXML : samplePhotoHistoryXMLs) 
		{
			// get the sample xmls
			List<SampleXML> sampleXMLs = samplePhotoHistoryXML.getSamples();
			
			if (!isBlank(sampleXMLs)) {
				for(SampleXML sampleXML : sampleXMLs) { allSampleIDs.add(sampleXML.getSampleID()); }
			}			
		}	
		if (isBlank(allSampleIDs)) return;
		
		List<Sample> allSamples = (List<Sample>) getHibernateTemplate()
			.find("from Sample sample " +
			"left join fetch sample.carSamples carSample " +
			"left join fetch carSample.car " +
			"where sample.sampleId in ("+ StringUtils.join(allSampleIDs, ", ") +")");
		if (isBlank(allSamples)) return;
		
		for (Sample sample: allSamples) { sampleID2Sample.put(sample.getSampleId(), sample); }
	}
	private List<Sample> getSamplesFor(SamplePhotoHistoryXML samplePhotoHistoryXML) 
	{
		List<SampleXML> sampleXMLs = samplePhotoHistoryXML.getSamples();
		if (sampleXMLs == null || sampleXMLs.size() < 1) return null;
			
		List<Sample> samples = new ArrayList<Sample>();
		for (SampleXML sampleXML : sampleXMLs) {
			Sample s = sampleID2Sample.get(sampleXML.getSampleID());
			if (s != null) samples.add(s);
		}
		return samples;
	}
	
	
	private EventXML getMostRecentEventXML(List<EventXML> eventXMLs)
	{
		EventXML mostRecentEventXML = null;
		{
			for (EventXML eventXML : eventXMLs) {
				if (mostRecentEventXML == null) {mostRecentEventXML = eventXML; continue; }
				else {
					Date d = eventXML.getTime();
					if (d != null && d.after(mostRecentEventXML.getTime())) mostRecentEventXML = eventXML;
				}
			}
		}
		return mostRecentEventXML;
	}

	private SampleTrackingStatus getSampleTrackingStatus(EventXML eventXML)
	{
		SampleTrackingStatus sampleTrackingStatus;
		String trackingStatusCode = null;
		String eventType = eventXML.getType();
		String eventQualifier = eventXML.getQualifier();
		if ("shipped".equals(eventType) || "arrived".equals(eventType)) {
			trackingStatusCode =	("toStudio".equals(eventQualifier) || "atStudio".equals(eventQualifier))	?	"SHIPPED" :
						("toBelk".equals(eventQualifier) || "atBelk".equals(eventQualifier))		?	"RECEIVED" :
						("toVendor".equals(eventQualifier) || "atVendor".equals(eventQualifier))	?	"RETURNED" : 
						"";
		}
		if (trackingStatusCode == null) trackingStatusCode = "";
		return nameToSampleTrackingStatus.get(trackingStatusCode);
	}	
	
	private Date startOfDayForDate(Date date)
	{
		Date shippingDate;
		{
			Calendar xmlDate = Calendar.getInstance();
			xmlDate.setTime(date);

			Calendar newDate = Calendar.getInstance();
			newDate.set(Calendar.YEAR, xmlDate.get(Calendar.YEAR));
			newDate.set(Calendar.MONTH, xmlDate.get(Calendar.MONTH));
			newDate.set(Calendar.DATE, xmlDate.get(Calendar.DATE));

			shippingDate = newDate.getTime();
		}
		return shippingDate;
	}	
	
	private ShippingInformation findShippingInformationInOldShippingInformations(String trackingNumber, Set<ShippingInformation> oldShippingInformations)
	{
		for (ShippingInformation oldShippingInformation : oldShippingInformations) {
			if (trackingNumber.equals(oldShippingInformation.getShipTrackingNumber())) {
				return oldShippingInformation;
			}
		}
		return null;
	}

	private ShippingInformation findShippingInformationInDB(String trackingNumber) throws DataAccessException
	{
		ShippingInformation si;
		List<ShippingInformation> l = (List<ShippingInformation>) getHibernateTemplate().find("from ShippingInformation si where shipTrackingNumber = '"+trackingNumber+"'");
		si = (l != null && l.size() > 0) ? l.get(0) : null;
		return si;
	}
	private List<Sample> getSamples(List<SamplePhotoHistoryXML> samplePhotoHistoryXMLs) 
	{
		List<Long> allSampleIDs = null;
		for (SamplePhotoHistoryXML samplePhotoHistoryXML : samplePhotoHistoryXMLs) 
		{
			// get the sample xmls
			List<SampleXML> sampleXMLs = samplePhotoHistoryXML.getSamples();
			allSampleIDs = new ArrayList<Long>(); {
				for(SampleXML sampleXML : sampleXMLs) { allSampleIDs.add(sampleXML.getSampleID()); }
			}
		}	
		if (allSampleIDs == null || allSampleIDs.size() < 1) return null;
		
		List<Sample> allSamples = (List<Sample>) getHibernateTemplate()
			.find("from Sample sample " +
			"left join fetch sample.carSamples carSample " +
			"left join fetch carSample.car " +
			"where sample.sampleId in ("+ StringUtils.join(allSampleIDs, ", ") +")");
		
		return allSamples;
	}
	private boolean isBlank(Collection c) { return c == null || c.size() == 0; }
	
	private void addAsPersistent(Collection c) {
		if (!isUnitTest || (isUnitTest && persistDuringUnitTest)) {
			getHibernateTemplate().saveOrUpdateAll(c);
		}
	}
	private void persistAll() {
		if (!isUnitTest || (isUnitTest && persistDuringUnitTest)) {
			getHibernateTemplate().flush();
		}
	}
	
	
	public static void main(String[] args)
	{
		RRDUpdateMessageReader reader = new RRDUpdateMessageReader();
		reader.isUnitTest = true;
		reader.persistDuringUnitTest = false;
		String xml;
		xml = RRDUpdateMessageReader_TestData.testXML;
//		try {
//			xml = FileUtils.readFileToString(new File("c:/temp/testFromRRD.xml"), "UTF-8");
//		}
//		catch (IOException ex) {
//			Logger.getLogger(RRDUpdateMessageReader.class.getName()).log(Level.SEVERE, null, ex);
//			xml = null;
//		}
		
		//System.out.println(RRDUpdateMessageReader_TestData.testXML);
		if (xml != null) readAndProcessXML(reader, xml);
//		if (args.length < 1) throw new IllegalArgumentException("The RRDUpdateMessageReader main() takes the XML String as its parameter.");
//		new RRDUpdateMessageReader().readAndProcessXML(args[0]);
////		reader.testBS();
////		//reader.testWrite();
		for (int i = 0; i<1; i++) {}
		System.exit(0);
	}
		
	
	
	
	
	
	
	
	
	
	
	
}
