/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.rrd;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.transform.Transformers;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;

import com.belk.car.app.dao.ProductDao;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.integrations.rrd.dao.ProductInfoDAO;
import com.belk.car.integrations.rrd.dao.ReturnInstructionsDAO;
import com.belk.car.integrations.rrd.dao.SampleInfoDAO;
import com.belk.car.integrations.xml.CarXML;
import com.belk.car.integrations.xml.CarsMessageXML;
import com.belk.car.integrations.xml.ColorXML;
import com.belk.car.integrations.xml.DepartmentXML;
import com.belk.car.integrations.xml.ProductXML;
import com.belk.car.integrations.xml.SampleXML;
import com.belk.car.integrations.xml.ShippingAccountXML;
import com.belk.car.integrations.xml.VendorXML;
import com.belk.car.integrations.xml.photoRequest.PhotoRequestMessageXML;
import com.belk.car.integrations.xml.photoRequest.PhotoXML;
import com.belk.car.integrations.xml.photoRequest.ProductPhotoRequestXML;
import com.belk.car.integrations.xml.photoRequest.SamplePhotoXML;
import com.belk.car.integrations.xml.xstream.CarsEnumSingleValueConverter;
import com.belk.car.util.DateUtils;
import com.thoughtworks.xstream.XStream;
/**
 * This class creates an XML message for RRD of type=photoRequest.
 * It 
 * 
 * @author amuaxg1
 */
public class ProductPhotoRequestMessageWriter extends UniversalDaoHibernate {

	/** CARS-193/SUP-776 **/
	private CarLookupManager lookupManager;
	private UserManager userManager;
	private EmailManager sendEmailManager;

	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/** CARS-193/SUP-776 **/

	private boolean isUnitTest = false;
	public static void main(String[] args)
	{
		// ########################### CURRENTLY WORKS PERFECTLY #################################
		try {
		ProductPhotoRequestMessageWriter writer = new ProductPhotoRequestMessageWriter();
		writer.isUnitTest = true;
		File f = new File("c:/temp", "photoRequests_" + DateFormatUtils.format(new Date(), "yyyy-MM-dd_HHmmss") + ".xml");
		if (f.exists()) {f.delete();}
		f.createNewFile();
		//writer.isUnitTest = true;
		OutputStream out = new BufferedOutputStream(new FileOutputStream(f));
		writer.write(out);
		out.close();
		System.exit(0);
		}
		catch (Exception ex) { ex.printStackTrace(); }
	}

	
	public void write(OutputStream out) 
	{
		getSampleInfoFromDB();
		
		if (isUnitTest) debugSamples();
		
		storeCarIDs();
		getProductInfoFromDB();

		// No product to retrieve creates a 0-byte file
		if (this.productDAOs.isEmpty()) {

			/** START CARS-193/SUP-776 if there is one/ all eligible CAR(s)for Export RRD having Null Product Type**/
			if(sampleDAOs !=null && !sampleDAOs.isEmpty()){
			sendNotificationToCARSUserList("EXPORT CAR SAMPLE DETAILS TO RRD",sampleDAOs);
			}
			/** END CARS-193/SUP-776 **/
			return;
		}

		correspondProductsWithCarIDs();
		correspondSamplesWithProducts();
		if (isUnitTest) debugProducts();

		getReturnInstructionsInfoFromDB();
		correspondReturnInstructionsWithProducts();
		if (isUnitTest) debugReturnInstructions();	

		createCarsMessage();
		/** START CARS-193/SUP-776 Send mail notification to CARS business user having Null Product Type **/
		if (UnprocessedSamples !=null && UnprocessedSamples.size() > 0) {
			sendNotificationToCARSUserList("EXPORT CAR SAMPLE DETAILS TO RRD",UnprocessedSamples);
		}
		/** END CARS-193/SUP-776 **/
		sendMessage(out);
	}


	
	
	CarsMessageXML carsMessage;
	List<SampleInfoDAO> UnprocessedSamples = new ArrayList<SampleInfoDAO>();/** CARS-193/SUP-776 **/
	
	

	private void createCarsMessage()
	{
		
		PhotoRequestMessageXML message = new PhotoRequestMessageXML("CARS", "RRD");
		List<ProductPhotoRequestXML> productPhotoRequests = new ArrayList<ProductPhotoRequestXML>();

		for (Map.Entry<Long, ProductInfoDAO> carIDToProductEntry : this.carIDToProductDAO
				.entrySet()) {

			/**START-CARS-193/SUP-776 Null Pointer Exception fix due to product type is NULL**/
			if (carIDToProductEntry.getValue() == null) {
				addUnProcessedSampleInfo(carIDToProductEntry.getKey());
				continue;
			}
			/** END- CARS-193/SUP-776**/

			ProductPhotoRequestXML productPhotoRequestXML = new ProductPhotoRequestXML();
			{
				ProductInfoDAO productDAO = carIDToProductEntry.getValue()==null? (new ProductInfoDAO()) : carIDToProductEntry.getValue();
								
				CarXML carXML = new CarXML(carIDToProductEntry.getKey());
				productPhotoRequestXML.setCarXML(carXML);

				ProductXML product = new ProductXML();
				product.brand = productDAO.getBrand();
				product.department = new DepartmentXML(productDAO.getDepartmentCode(), productDAO.getDepartmentName());  //modified by santosh
				product.productClass = new ProductXML.Class(productDAO.getClassID(), productDAO.getClassName());
				product.productName = productDAO.getProductName();
				product.productType = productDAO.getProductType();
				product.style = new ProductXML.Style(productDAO.getStyleNumber());
				product.vendor = new VendorXML(productDAO.getVendorNumber(), productDAO.getVendorName());
				productPhotoRequestXML.setProductXML(product);
				
				
				List<PhotoXML> photos = createPhotosXML(productPhotoRequestXML);
				productPhotoRequestXML.setPhotoXMLs(photos);
			}
			productPhotoRequests.add(productPhotoRequestXML);
		}
		message.productPhotoRequestXMLs = productPhotoRequests;
		this.carsMessage = message;
	}
	
	private void sendMessage(OutputStream out)
	{
		XStream xStream = new XStream();
		xStream.registerConverter(new CarsEnumSingleValueConverter(), XStream.PRIORITY_NORMAL+10);
		xStream.processAnnotations(new Class[] {
			PhotoRequestMessageXML.class, 
			SamplePhotoXML.class, 
			SampleXML.class, 
			ShippingAccountXML.class
		});
		xStream.toXML(carsMessage, out);
	}
	
	private List<PhotoXML> createPhotosXML(ProductPhotoRequestXML productPhotoRequest)
	{
		ProductInfoDAO productDAO = carIDToProductDAO.get( productPhotoRequest.getCarXML().id );
				
		List<PhotoXML> photos = new ArrayList<PhotoXML>();
		{
			boolean shouldCreateSamplePhotos;
			{
				List<SampleInfoDAO> productSampleDAOs = productDAO.getSampleInfoDAOs();
				String photoInstructions = productDAO.getPhotoInstructions();
				
				
				boolean thereArePhotoInstructions = false;//photoInstructions != null && !photoInstructions.isEmpty();
				boolean thereAreSamples = productSampleDAOs != null && !productSampleDAOs.isEmpty();
				
				shouldCreateSamplePhotos = thereAreSamples || thereArePhotoInstructions;
			}
			
			List<SamplePhotoXML> samplePhotos = (shouldCreateSamplePhotos) ? createSamplePhotoXMLs(productDAO) : null;
			
			if (samplePhotos != null) photos.addAll(samplePhotos);
		}
		return photos;
	}


	// creates all <photo type="sample"> XMLs needed for this product 
	private List<SamplePhotoXML> createSamplePhotoXMLs(ProductInfoDAO productDAO)
	{
		List<SamplePhotoXML> samplePhotoXMLs = null; // this is correct
		{
			samplePhotoXMLs = new ArrayList<SamplePhotoXML>();
			
			List<SampleInfoDAO> sampleDAOs = productDAO.getSampleInfoDAOs();
			

			// right now, there is only one sample per photo
			// should this change, just pass in more samples to each call to createSamplePhotoXML
			for (SampleInfoDAO sampleDAO : sampleDAOs) {				
				SamplePhotoXML samplePhotoXML = createSamplePhotoXML(Arrays.asList(sampleDAO), productDAO);
				samplePhotoXMLs.add(samplePhotoXML);
			}			
		}		
		return samplePhotoXMLs;
	}
	
	/**
	 * This method will create one <photo type="sample"> xml element holding all the <sample> elements which
	 * represent the samples passed in.
	 * 
	 * @param sampleDAOs	The samples to include in this photo
	 * @param productDAO	The product holding these samples
	 * @return a <samplePhoto> xml element holding 0-* <sample> elements
	 */
	private SamplePhotoXML createSamplePhotoXML(List<SampleInfoDAO> sampleDAOs, ProductInfoDAO productDAO)
	{
		SamplePhotoXML samplePhotoXML = new SamplePhotoXML();
		
		// build fileXML XML
		PhotoXML.FileXML photoFileXML = new PhotoXML.FileXML();
		PhotoXML.FileXML.NameXML photoFileNameXML = new PhotoXML.FileXML.NameXML();
		photoFileNameXML.prefix = calculateFileName(productDAO, sampleDAOs);
		photoFileXML.nameXML = photoFileNameXML;
		samplePhotoXML.setFileXML(photoFileXML);

		// build sample XMLs
		List<SampleXML> samplesXML = new ArrayList<SampleXML>();
		for (SampleInfoDAO sampleDAO : sampleDAOs) {
			SampleXML sampleXML = new SampleXML();

			ShippingAccountXML accountXML = new ShippingAccountXML(sampleDAO.getShippingAccountNumber(), sampleDAO.getCarrier());
			List<String> returnInstructions = null;
			{
				List<ReturnInstructionsDAO> retInstDAOs = productDAO.getReturnInstructionsDAOs();
				int nRetInstDAOs = (retInstDAOs == null) ? 0 : retInstDAOs.size();
				if (nRetInstDAOs > 0) {
					returnInstructions = new ArrayList<String>(nRetInstDAOs);
					for (ReturnInstructionsDAO r : retInstDAOs) {
						returnInstructions.add(r.getReturnInstructions());
					}
				}
			}

			sampleXML.setColor(new ColorXML(sampleDAO.getColorCode(), sampleDAO.getColorName()));
			sampleXML.setReturnRequested(sampleDAO.getReturnRequested());
			sampleXML.setSilhouetteRequired(sampleDAO.getSilhouetteRequired());
			sampleXML.setSampleID(sampleDAO.getSampleID());
			sampleXML.setType(sampleDAO.getType());
			sampleXML.setReturnInformation(new SampleXML.ReturnInformation(accountXML, returnInstructions));

			samplesXML.add(sampleXML);
		}
		samplePhotoXML.setSamples(samplesXML);

		// set instructions
		samplePhotoXML.setInstructions(productDAO.getPhotoInstructions());
		
		return samplePhotoXML;
	}
		
			
			
	private void debugSamples() {
		for (SampleInfoDAO info : sampleDAOs) {
			System.out.println(info.toString());
		}
	}
	private void debugProducts()
	{
		for (ProductInfoDAO info : carIDToProductDAO.values()) {
			System.out.println(info.toString());
		}
	}
	private void debugReturnInstructions()
	{
		if (returnInstructionsDAOs != null) {
			for (ReturnInstructionsDAO info : returnInstructionsDAOs) {
				System.out.println(info.toString());
			}
		}
	}
	
	SessionFactory testSessionFactory = null;
	private SessionFactory getTestSessionFactory() {
		try {
			if (testSessionFactory != null)
				return testSessionFactory;
			
			Properties properties = new Properties();
			InputStream inp = ClassLoader.getSystemResourceAsStream("jdbc.properties");
			if (inp == null) {
				Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "System resource not found, finding class resource!");
				inp = this.getClass().getResourceAsStream("jdbc.properties");
			}
			
			if (inp == null) {
				Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Class resource not found, finding class Thread resource!");
				inp = Thread.currentThread().getContextClassLoader().getResourceAsStream("jdbc.properties");
			}
			
			if (inp != null) {
				properties.load(inp);
			} else 
				Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Thread resource not found, don't know what to do now!");

			Configuration cfg = new Configuration();
			cfg.setProperties(properties);
			//Configuration cfg = new Configuration().setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect").setProperty("hibernate.connection.username", "CARS").setProperty("hibernate.connection.password", "CARS").setProperty("hibernate.connection.url", "jdbc:oracle:thin:@10.1.61.4:1521:BLKBMDEV").setProperty("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver");
			this.testSessionFactory = cfg.buildSessionFactory();			
		}
		catch (IOException ex) {
			Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.SEVERE, null, ex);
		}
		return this.testSessionFactory;
	}
	
	private Session session() {
		SessionFactory sf;
		try { sf = getHibernateTemplate().getSessionFactory(); } catch (Exception ex) {sf = null;}
		if (sf == null) sf = getTestSessionFactory();
		//SessionFactory sf = (isUnitTest) ? getTestSessionFactory() : getHibernateTemplate().getSessionFactory();
		
		Session session = null;
		try { session = sf.getCurrentSession(); } 
		catch (HibernateException ex) { session = sf.openSession(); }
		return session;
	}
	
	List<SampleInfoDAO> sampleDAOs;
	private void getSampleInfoFromDB() {
		System.out.println("Inside getSampleInfoFromDB():");
		Session session = session();
		//String sqlQuery = (isUnitTest) ? sampleInfoForCarIDs_SQL + " where s.car_id <= 300" : sampleInfoForCarIDs_SQL;
		String sqlQuery = sampleInfoForCarIDs_SQL;
		Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Query: " + sqlQuery);
		SQLQuery q = session.createSQLQuery(sqlQuery);
		q.addScalar("carID", Hibernate.LONG);
		q.addScalar("sampleID", Hibernate.LONG);
		q.addScalar("vendorStyleNumber", Hibernate.STRING); //Added for ExportRRD feed file image name
		q.addScalar("type", Hibernate.STRING);
		q.addScalar("colorCode", Hibernate.STRING);
		q.addScalar("colorName", Hibernate.STRING);
		q.addScalar("colorCode", Hibernate.STRING);
		q.addScalar("returnRequested", Hibernate.CHARACTER);
		q.addScalar("silhouetteRequired", Hibernate.CHARACTER);
		q.addScalar("carrier", Hibernate.STRING);
		q.addScalar("shippingAccountNumber", Hibernate.STRING);
		q.setResultTransformer(Transformers.aliasToBean(SampleInfoDAO.class));
		this.sampleDAOs = q.list();	
		Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Result set size: " + this.sampleDAOs.size());
	}
	
	SortedMap<Long, ProductInfoDAO> carIDToProductDAO;
	
	private void storeCarIDs() {
		carIDToProductDAO = new TreeMap<Long, ProductInfoDAO>();
				
		for (SampleInfoDAO sample : sampleDAOs) {			
			if (!carIDToProductDAO.containsKey(sample.getCarID())){
				carIDToProductDAO.put(sample.getCarID(), null);
				
			}
		}
				
		Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Number of Car ID: " + carIDToProductDAO.size());
		if (isUnitTest) Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "There are "+ carIDToProductDAO.keySet().size() + " distinct car IDs");
	}
	
	List <ProductInfoDAO> productDAOs;
	private void getProductInfoFromDB() {
 		System.out.println("Inside getProductInfoFromDB():");
		Session session = session();		
		if (!carIDToProductDAO.isEmpty()) {
			String carIDsStr = StringUtils.join(carIDToProductDAO.keySet(),",");
			if (isUnitTest) Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Car IDs: "+ carIDsStr);
			String queryStr = productInfoForCarID_SQL.replace(":carIDs", carIDsStr);
						
			Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Product Query: " + queryStr);
			SQLQuery q = session.createSQLQuery(queryStr);
			q.addScalar("carID", Hibernate.LONG);
			q.addScalar("vendorName", Hibernate.STRING);
			q.addScalar("vendorNumber", Hibernate.STRING);
			q.addScalar("styleNumber", Hibernate.STRING);
			q.addScalar("productName", Hibernate.STRING);
			q.addScalar("productType", Hibernate.STRING);
			q.addScalar("brand", Hibernate.STRING);
			q.addScalar("departmentCode", Hibernate.STRING);  //modified by santosh
			q.addScalar("departmentName", Hibernate.STRING);
			q.addScalar("classID", Hibernate.LONG);
			q.addScalar("className", Hibernate.STRING);
			q.addScalar("photoInstructions", Hibernate.STRING);
			q.setResultTransformer(Transformers.aliasToBean(ProductInfoDAO.class));
			this.productDAOs = q.list();
			Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Result set size: " + this.productDAOs.size());
		} else {
			Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "carIDToProductDAO is empty");
			this.productDAOs = new ArrayList<ProductInfoDAO>();
			}
	}
	
	
	List <ReturnInstructionsDAO> returnInstructionsDAOs;
	private void getReturnInstructionsInfoFromDB() {
		Session session = session();	
		if (!carIDToProductDAO.isEmpty()) {
			String carIDsStr = StringUtils.join(carIDToProductDAO.keySet(),",");
			String queryStr = returnInstructions_SQL.replace(":carIDs", carIDsStr);
			Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Return Instructions Query: " + queryStr);
			SQLQuery q = session.createSQLQuery(queryStr);
			q.addScalar("carID", Hibernate.LONG);
			q.addScalar("returnInstructions", Hibernate.STRING);
			q.setResultTransformer(Transformers.aliasToBean(ReturnInstructionsDAO.class));
			this.returnInstructionsDAOs = q.list();	
			Logger.getLogger(ProductPhotoRequestMessageWriter.class.getName()).log(Level.INFO, "Result set size: " + this.returnInstructionsDAOs.size());
		} else {
			this.returnInstructionsDAOs = new ArrayList<ReturnInstructionsDAO>();
		}
	}
	
	
	
	private void correspondProductsWithCarIDs() {
		
		for (ProductInfoDAO productDAO : this.productDAOs) {
			carIDToProductDAO.put(productDAO.getCarID(), productDAO);
			}
		
	}
	
	
	private void correspondSamplesWithProducts() {
		
		for (SampleInfoDAO sample : this.sampleDAOs) { 
			ProductInfoDAO product = this.carIDToProductDAO.get(sample.getCarID());
			if (product != null) {
				product.getSampleInfoDAOs().add(sample);
			}
			
		}
		
	}
	
	private void correspondReturnInstructionsWithProducts() {
		for (ReturnInstructionsDAO returnInstructionsDAO : this.returnInstructionsDAOs) { 
			ProductInfoDAO product = this.carIDToProductDAO.get(returnInstructionsDAO.getCarID());
			if (product != null) {
				product.getReturnInstructionsDAOs().add(returnInstructionsDAO);
			}
		}
	}
	
	String calculateFileName(ProductInfoDAO productDAO, List<SampleInfoDAO> sampleDAOs)
	{
		
		if ((productDAO != null)  && (sampleDAOs != null && sampleDAOs.size() >0)){ //Added for ExportRRD feed file image name 

		SampleInfoDAO mainSampleDAO = (sampleDAOs.size() > 0) ? sampleDAOs.get(0) : null;
		StringBuffer nameBuf = new StringBuffer();
		nameBuf
			.append(productDAO.getVendorNumber())
			.append('_')
			.append(mainSampleDAO.getVendorStyleNumber()) //Added for ExportRRD feed file image name
			.append( "swatch".equalsIgnoreCase(productDAO.getProductType()) ? "_SW_" : "_X_" )
			.append(mainSampleDAO.getColorCode());
		return nameBuf.toString();
		}else{
			return "";
		}
		
	}
	
	
	///
	/// SQL Statements
	///
	
	/*	
	private static final String returnInstructions_SQL = "select car_id as \"carID\", note_text as \"returnInstructions\" from car_note where note_type_cd = 'RETURN_NOTES' and car_note.car_id in ( :carIDs )";
	private static final String productInfoForCarID_SQL = 
		"select car.car_id as \"carID\", " +
			"vendor.name as \"vendorName\", " + 
			"vendor.vendor_number as \"vendorNumber\", " + 
			"vendor_style.vendor_style_number as \"styleNumber\", " + 
			"vendor_style.vendor_style_name as \"productName\", " + 
			"product_type.name as \"productType\", " + 
			"brand as \"brand\", " +
			"department.dept_id as \"departmentID\", " + 
			"department.name as \"departmentName\",  " + 
			"classification.class_id as \"classID\", " + 
			"classification.name as \"className\", " + 
			"sample_notes.photo_instructions as \"photoInstructions\" " + 
		"from " + 
			"car " + 
				"left outer join " +
					"(select car_id, attr_value as brand from car_attribute where car_attribute.attr_id = 2232 ) ca on ca.car_id = car.car_id " +
				"left outer join " +
					"(select car_id, note_text as photo_instructions from car_note where note_type_cd = 'SAMPLE_NOTES') sample_notes on sample_notes.car_id = car.car_id " +
			",vendor_style " + 
				"left outer join product_type on vendor_style.product_type_id = product_type.product_type_id " + 
			",vendor " + 
			",classification " + 
			",department " + 
		"where " + 
			"car.car_id in ( :carIDs ) " + 
			"and " + 
			"car.vendor_style_id = vendor_style.vendor_style_id " + 
			"and " + 
			"vendor_style.vendor_id = vendor.vendor_id " + 
			"and " + 
			"vendor_style.class_id = classification.class_id " + 
			"and " + 
			"classification.dept_id = department.dept_id";
*/
	
	private static final String returnInstructions_SQL = 
		"select cn.car_id as carID, cn.note_text as returnInstructions " +
		"from car_note cn " +
	     "   inner join ( " + 
	     "     select distinct c2.car_id as car_id " + 
	     "           from car_sample cs  " + 
	     "             inner join car c2 on c2.car_id = cs.car_id " + 
	     "             inner join sample s on s.sample_id = cs.sample_id " + 
	     "           where s.sample_tracking_status_cd = 'RECEIVED' and  " + 
	     "                 s.status_cd = 'ACTIVE' and  " + 
	     "                 c2.status_cd = 'ACTIVE' and  " + 
	     "                 s.updated_date > to_date((select value from config where param='RRD_LAST_TRANSFER_TIME'), 'MM/DD/YYYY HH24:MI:SS') ) s2 on s2.car_id = cn.car_id " +
		 " where note_type_cd = 'RETURN_NOTES'";
	
			

	private static final String productInfoForCarID_SQL = 
	"select c.car_id as carID,  " +
	"v.name as vendorName,   " + 
	"v.vendor_number as vendorNumber,   " + 
	"vs.vendor_style_number as styleNumber,  " +
	"vs.vendor_style_name as productName,   " + 
	"pt.name as productType,   " + 
	"(select max(ca.attr_value) from car_attribute ca inner join attribute attr on attr.attr_id = ca.attr_id where attr.blue_martini_attribute = 'Brand' and ca.car_id = c.car_id) as brand, " +  
	"dept.dept_cd as departmentCode,   " +  //modified by santosh
	"dept.name as departmentName,    " + 
	"cls.class_id as classID,   " + 
	"cls.name as className,   " + 
	"cn2.note_text as photoInstructions   " + 
	"from   " + 
	"car c inner join vendor_style vs on vs.vendor_style_id = c.vendor_style_id " +
	 "   inner join product_type pt on pt.product_type_id = vs.product_type_id   " + 
     "   inner join vendor v on v.vendor_id = vs.vendor_id   " + 
     "   inner join classification cls on cls.class_id = vs.class_id   " + 
     "   inner join department dept on dept.dept_id = c.dept_id   " + 
     "   left outer join (select cn.car_id, cn.note_text from car_note cn where cn.note_type_cd = 'SAMPLE_NOTES') cn2 on cn2.car_id = c.car_id " + 
     "   inner join ( " + 
     "     select distinct c2.car_id as car_id " + 
     "           from car_sample cs  " + 
     "             inner join car c2 on c2.car_id = cs.car_id " + 
     "             inner join sample s on s.sample_id = cs.sample_id " + 
     "           where s.sample_tracking_status_cd = 'RECEIVED' and  " + 
     "                 s.status_cd = 'ACTIVE' and  " + 
     "                 c2.status_cd = 'ACTIVE' and  " + 
     "                 s.updated_date > to_date((select value from config where param='RRD_LAST_TRANSFER_TIME'), 'MM/DD/YYYY HH24:MI:SS') ) s2 on s2.car_id = c.car_id " ;

	private static final String sampleInfoForCarIDs_SQL = 
		"select " +
			"s.car_id as \"carID\", " +
			"s.sample_id as \"sampleID\", " + 
			"s.vendor_style_number as \"vendorStyleNumber\", " + //Added for ExportRRD feed file image name
			//"( SELECT VENDOR_STYLE_NUMBER FROM vendor_style WHERE VENDOR_STYLE_ID=s.vendor_style_id and rownum=1) as \"vendorStyleNumber\", " + //Added for ExportRRD feed file image name
			"s.sample_type_cd as \"type\", " +
			"s.swatch_color as \"colorCode\", " +
			"( SELECT color_name FROM vendor_sku WHERE ((color_code = s.swatch_color)) AND ((car_id = s.car_id)) and rownum=1) as \"colorName\", " +
			"s.is_returnable as \"returnRequested\", " +
			"s.silhouettereq as \"silhouetteRequired\", " +
			"shipping_type.name as \"carrier\", " +
			"s.shipper_account_number as \"shippingAccountNumber\" " +
		"from " +
			"( " +
				"select " +
					"car_sample.car_id, " +
					"sample.sample_id, " +
					"vendor_style.vendor_style_number, " + //Added for ExportRRD feed file image name
					"sample.sample_type_cd, " +
					"sample.swatch_color, " +
					"sample.is_returnable, " +
					"sample.silhouettereq, " +
					"sample.shipping_type_cd, " +
					"sample.shipper_account_number " +
				"from " +
					"sample, car_sample, car, vendor_style " + //Added for ExportRRD feed file image name
				"where " +
					"sample.sample_id = car_sample.sample_id " +
					"and " +
					"car_sample.car_id = car.car_id " +
					"and " +
					"sample.SAMPLE_TRACKING_STATUS_CD = 'RECEIVED' " +
					"and " +
					"sample.STATUS_CD = 'ACTIVE' " +
					"and " +
					"car.STATUS_CD = 'ACTIVE' " +
					"and " +
					"sample.UPDATED_DATE > to_date((select value from config where param='RRD_LAST_TRANSFER_TIME'), 'MM/DD/YYYY HH24:MI:SS') " +
					"and vendor_style.vendor_style_id=sample.vendor_style_id "+  //Added for ExportRRD feed file image name
//					"sample.UPDATED_DATE between to_date('04/08/2008 00:00:01', 'MM/DD/YYYY HH24:MI:SS') and to_date('04/09/2008 00:00:01', 'MM/DD/YYYY HH24:MI:SS') " +
			") " +		
			"s " +
			"left outer join " +
				"shipping_type on shipping_type.SHIPPING_TYPE_CD = s.SHIPPING_TYPE_CD";
	
	/**
	 * @param carID
	 * @param isAllCARsFailed
	 * SUP-776 method gets the details for the CARs not processed due to Null Product type 
	 */
	private void addUnProcessedSampleInfo(Long carID) {
		try {
			for (SampleInfoDAO sampleDAO : sampleDAOs) {
					if (sampleDAO.getCarID() == carID) {
						UnprocessedSamples.add(sampleDAO);
						break;
					}
				}		
		} catch (Exception ex) {
			log.error("Exception occured. Cause: " + ex.getMessage());
		}
	}

	
	/**
	 * @param processName
	 * @param UnprocessedSamples
	 * SUP-776 method sends Notification mail to recipients configured in CONFIG DB 
	 * for the CARs having Null Product Type. 
	 */
	private void sendNotificationToCARSUserList(String processName,
			List<SampleInfoDAO> UnprocessedSamples) {
		try {

			NotificationType type = lookupManager
					.getNotificationType(NotificationType.CAR_FAILED_EXPORTRRD);
			Config userName = (Config) lookupManager.getById(Config.class,
					Config.SYSTEM_USER);
			Config sendNotifications = (Config) lookupManager.getById(
					Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
			Config emailNotificationList = (Config) lookupManager.getById(
					Config.class,
					Config.SCHEDULED_PROCESS_CAR_DETAILS_EXPORT_RRD_FAILED);

			User systemUser = this.userManager.getUserByUsername(userName
					.getValue());

			Map<String, Object> model = new HashMap<String, Object>();
			String[] emails = StringUtils.split(
					emailNotificationList.getValue(), ",;");

			for (String email : emails) {
				model.put("userEmail", email);
				model.put("processName", processName);
				model.put("UnprocessedSamples", UnprocessedSamples);
				model.put("executionDate",
						DateUtils.formatDate(new Date(), "MM/dd/yyyy HH:mm:ss"));

				try {
					if ("true".equalsIgnoreCase(sendNotifications.getValue())) {

						sendEmailManager.sendNotificationEmail(type,
								systemUser, model);

					}
				} catch (SendEmailException se) {
					log.error("Error when sending email to: " + se.getMessage());
				} catch (Exception exp) {
					log.error("General Exception occured. Cause: " + exp.getMessage());
				}
			}
		} catch (Exception ex) {
			log.error("*** Notification Exception occured." );
			ex.printStackTrace();
		}

	}

}
