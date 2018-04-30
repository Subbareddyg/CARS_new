
package com.belk.car.app.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.exceptions.IExceptionProcessor;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeLookupValue;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogFieldDataMapping;
import com.belk.car.app.model.vendorcatalog.VendorCatalogHeader;
import com.belk.car.app.model.vendorcatalog.VendorCatalogRecord;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSku;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuId;
import com.belk.car.app.model.vendorcatalog.VendorCatalogStyleSkuMaster;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.CarPrefillManager;
import com.belk.car.app.service.DropshipManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.service.WorkflowManager;

/**
 * Purpose: Batch job to pre-fill CARS data from vendor catalog data Initial
 * Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I Initial
 * Requirements: BR.012 Description: For Drop-ship, PO's are not issued on
 * vendor drop-ship merchandise. Consequently, the data normally populated into
 * CARS from a PO will not be available. This class provides an application to
 * look at open CARS and populate attribute data based on information supplied
 * through vendor catalogs and mapped to CARS attributes.
 * 
 * @author afudxm2
 */
public class CarPrefillManagerImpl implements CarPrefillManager,IExceptionProcessor,DropShipConstants {

	// Constants

	private final static String CAR_NOTES_TYPE_CD = "CAR_NOTES";

	// Values populated by Quartz Job property configurations (see
	// scheduler.xml)

	private CarLookupManager lookupManager;
	private CarManager carManager;
	private EmailManager sendEmailManager;
	private UserManager userManager;
	private VendorCatalogManager vendorCatalogManager;
	private DropshipManager dropshipManager;
	private WorkflowManager workflowManager;

	// Data values

	private User systemUser;

	private static final Log LOG = LogFactory.getLog(CarPrefillManagerImpl.class);

	public CarPrefillManagerImpl() {
		// Class values are initialized by the Quartz job manager after
		// instantiation
	}

	/**
	 * @param carManager the carManager to set
	 */
	public void setCarManager(final CarManager carManager) {
		this.carManager = carManager;
	}

	private CarManager getCarManager() {
		return this.carManager;
	}

	public void setUserManager(final UserManager userManager) {
		this.userManager = userManager;
	}

	private UserManager getUserManager() {
		return this.userManager;
	}

	public void setLookupManager(final CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	private CarLookupManager getLookupManager() {
		return this.lookupManager;
	}

	public void setVendorCatalogManager(final VendorCatalogManager vendorCatalogManager) {
		this.vendorCatalogManager = vendorCatalogManager;
	}

	private VendorCatalogManager getVendorCatalogManager() {
		return this.vendorCatalogManager;
	}

	/**
	 * @return the dropshipManager
	 */
	public DropshipManager getDropshipManager() {
		return this.dropshipManager;
	}

	/**
	 * @param dropshipManager the dropshipManager to set
	 */
	public void setDropshipManager(final DropshipManager dropshipManager) {
		this.dropshipManager = dropshipManager;
	}

	public void setWorkflowManager(final WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public WorkflowManager getWorkflowManager() {
		return this.workflowManager;
	}

	/**
	 * @param sendEmailManager the sendEmailManager to set
	 */
	public void setSendEmailManager(final EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}

	private WorkflowStatus getNextWorkflowStatus() {
		WorkflowStatus nextStatus = (WorkflowStatus) this.lookupManager.getById(
				WorkflowStatus.class, WorkflowStatus.READY_FOR_REVIEW);
		return nextStatus;
	}

	/**
	 * Retrieve the system user. Use lazy instantiation, so the value is only
	 * retrieved once.
	 * 
	 * @return system user
	 */
	private User getSystemUser() {

		if (this.systemUser == null) {

			final Config userName = (Config) getLookupManager().getById(Config.class,
					Config.CAR_IMPORT_USER);

			this.systemUser = getUserManager().getUserByUsername(userName.getValue());
		}

		return this.systemUser;
	}

	/**
	 * Pre-fill eligible CARs. For each CAR: 1. Check whether a record found in
	 * Style UPC master table for a style or UPC for which a CAR is created. If
	 * record found the proceed to next step. 2. Get the last catalog using
	 * which the style or UPC is uploaded to the database. 3. Get the catalog
	 * template of the cataLOG. 4. Get the data mapping rules and field mapping
	 * rules for the catalog template. 5. Get the catalog data and apply the
	 * data mapping rules to create information for pre-filling a CAR. 6.
	 * Pre-fill CAR using the information generated in step 5.
	 * 
	 * @see com.belk.car.app.service.CarsPrefillManager#prefillCars()
	 */
	public void prefillCars() {

		if (LOG.isInfoEnabled()) {
			LOG.info("---------------->  Begin prefilling CARs Data <-----------------");
		}

		prefillBatch();

		if (LOG.isInfoEnabled()) {
			LOG.info("---------------->  End prefilling CARs Data <-----------------");
		}

	} // end of prefillCars()

	/**
	 * Prefill a batch of Car objects. Return true if any were updated. If no
	 * Cars were updated, then there are no additional batches of new cars to
	 * process
	 * 
	 * @return true if any cars were updated
	 */
	private void prefillBatch() {

		LOG.debug(" prefill batch called");
		List<Car> cars=null;
		// Get a list of eligible CARs
		List<String> vendorStyles=null;
		Long catalogId=null;
		Map<Long,List<String>> latestCatalogs=getLatestVendorCatalogStyles();
		if(latestCatalogs!=null && !latestCatalogs.isEmpty()){
			LOG.info("Number of latest catalogs: "+latestCatalogs.size());
			for(Map.Entry<Long,List<String>> catalog:latestCatalogs.entrySet()){
				vendorStyles=catalog.getValue();
				catalogId=catalog.getKey();
				for(String style:vendorStyles){
					cars=getDropshipManager().getCarsByVendorStyle(style);
					if (cars == null || cars.isEmpty()) {
						LOG.info("No CARS in INITIATED status and assigned to BUYER for style: "+style);
					} else {
						LOG.info("Number of cars to process:" + cars.size() +" for the Style: "+style + " and catalog id: "+catalogId);
						
						for(Car c:cars){
							VendorStyle catalogVendorStyle=null;
							try{
								catalogVendorStyle= getDropshipManager().getVendorStyleFromNumber(style);
							}catch (Exception e){
								LOG.info("Exception :"+e.getMessage());
							}
							LOG.info("Catalog Vendor Style Number:"+catalogVendorStyle.getVendorStyleNumber());
							prefill(c,catalogVendorStyle,catalogId);
						}
					}
				}
			}
		}else{
			LOG.debug("There were no new Styles avaialbe in catalog");
		}
	}

	

	
	/**
	 * Retrieve the mapped record value for the given record
	 * 
	 * @param aTemplate
	 * @param aRecord
	 * @return record value
	 */
	private String getRecordValue(
			final VendorCatalogTemplate aTemplate, final VendorCatalogRecord aRecord) {

		String recordValue;

		final VendorCatalogFieldDataMapping fieldDataMapping = getDropshipManager()
				.getVendorCatalogFieldDataMapping(aTemplate.getVendorCatalogTmplID(), aRecord);

		if (fieldDataMapping == null) {
			recordValue = aRecord.getVendorCatalogFieldValue();
		}
		else {
		//	LOG.debug("fieldDataMapping==============="+fieldDataMapping.getCompositeKey().getVendorCatalogFieldValue()+"   fieldDataMapping========"+fieldDataMapping.getBlkAttrLookupValue().getValue());
			recordValue = fieldDataMapping.getBlkAttrLookupValue().getValue();
			//LOG.debug("recordValue======================"+recordValue);
		}

		return recordValue;
	}

	/**
	 * Retrieve the most recent Vendor Catalog for this vendor-style. The most
	 * recent will be the one with the highest VendorCatalog ID value
	 * 
	 * @param vendorStyle
	 * @return Long vendor catalog ID
	 */
	private Long getLatestVendorCatalogID(final VendorStyle vendorStyle,DropshipManager dropshipManager) {

	
		
		Long latestID = null;

		VendorCatalogStyleSkuMaster latestMaster = null;

		// vendorCatalogStyleSkuMaster from vendor ID and vendorStyle with
		// max(UpdatedDate)
		
		
		List<VendorCatalogStyleSkuMaster> masterList = dropshipManager
				.getMasterRecordsForStyle(vendorStyle.getVendorStyleNumber());
		for (VendorCatalogStyleSkuMaster master : masterList) {

			LOG.info("getLatestVendorCatalogID:: VendorCatalogStyleSkuMaster: "	+ master.getVendorCatalogId() + ", updated: " + master.getUpdatedDate());

			// First time through: If the latest hasn't been set yet, then set
			// it to the current value
			if (latestMaster == null) {
				latestMaster = master;
			}
			else {
				Date updatedDate = latestMaster.getUpdatedDate();
				if (updatedDate != null && updatedDate.before(master.getUpdatedDate())) {
					latestMaster = master;
				}
			}
		}

		if (latestMaster != null) {
			latestID = latestMaster.getVendorCatalogId();
		}
		return latestID;
	}



	/**
	 * Pre-fill one CAR
	 * 
	 * @param car
	 * @param style the VendorStyle of the given CAR
	 * @param latestCatalogID most recent catalog corresponding to the style
	 * @return true if the CAR has been updated successfully
	 */

	public boolean prefill(final Car car, final VendorStyle style, final Long catalogID) {

		LOG.info("prefill method for carId:"+car.getCarId());
				
		boolean updated = false;

		VendorCatalog latestCatalog = getVendorCatalogManager().getVendorCatalog(catalogID);
		final VendorCatalogTemplate template = latestCatalog.getVendorCatalogTemplate();
		// please do not print the log of the template alone
		if(template==null){
			LOG.info(" template is null, returning ");
			return false;
		} else{
			//LOG.info(" template from catalog:"+ template.getVendorCatalogTmplID());
			LOG.info(" template is not null ");
		}
		// Get the data mapping rules and field mapping rules for the catalog
		// template
		// Get a list of attribute IDs for this CAR
		List<Attribute> attributes = getDropshipManager().getAttributesforCar(car.getCarId());
		// For each VendorSku in the CAR...
		final Set<VendorSku> skuList = car.getVendorSkus();
		final Iterator<VendorSku> skuItr = skuList.iterator();
		Iterator<Attribute> attrIter;
		while (skuItr.hasNext()) {
			final VendorSku vendorSku = skuItr.next();
			                        //Use Verdor Style No instead of Vendor Style Id
                     final VendorCatalogStyleSkuId compositeKey = new VendorCatalogStyleSkuId(latestCatalog.getVendorCatalogID(),
                    		 style.getVendorStyleNumber(), vendorSku.getVendorUpc());

			final VendorCatalogStyleSku sku = getDropshipManager().getStyleSkuForMaster(
					compositeKey);
			if (sku == null) {
				LOG.info("No SKU found for: " + vendorSku.getBelkSku() + " compositeKey: "
						+ compositeKey.getVendorCatalogId() + " " + compositeKey.getVendorStyleId()
						+ " " + compositeKey.getVendorUPC());
			}
			else {

				LOG.info(vendorSku.getBelkSku() + " recordNum: " + sku.getRecordNum());

				// Go through all vendor catalog records from the latest catalog
				// for this SKU

				final List<VendorCatalogRecord> catalogRecords = getDropshipManager()
						.getAllRecordsWithRecordNum(sku.getRecordNum(),
								sku.getCompositeKey().getVendorCatalogId());

				for (VendorCatalogRecord recordModel : catalogRecords) {

					if (LOG.isDebugEnabled()) {
						LOG.debug("Got the record model="
								+ recordModel.getCompositeKey().getRecordNumber() + " "
								+ template.getVendorCatalogTmplID());
					}

					// Retrieve Blue Martini attribute name
					final String bmsAttrName = getDropshipManager().getAttributeForRecord(
							recordModel, template.getVendorCatalogTmplID());

					Attribute mappedAttribute = null;
					attrIter = attributes.iterator();
					while (attrIter.hasNext()) {
						Attribute attr = attrIter.next();
						if (attr.getBlueMartiniAttribute().equals(bmsAttrName)) {
							mappedAttribute = attr;
							break;
						}
					}

					if (LOG.isDebugEnabled()) {
						LOG.debug("attributeIdFromMapping="
								+ (mappedAttribute == null ? "NULL" : mappedAttribute
										.getAttributeId()));
					}

					if (mappedAttribute != null) {

						final String recordValue = getRecordValue(template, recordModel);

						// Get the corresponding CAR SKU attribute

						CarSkuAttribute carSkuAttribute = new CarSkuAttribute();
					//	LOG.debug("mappedAttribute.getAttributeId()============================>"+mappedAttribute.getAttributeId());
						final CarSkuAttribute existingAttribute = getDropshipManager()
								.getCarSkuAttribute(new Long(vendorSku.getCarSkuId()),
										Long.toString(mappedAttribute.getAttributeId()));

						if (existingAttribute != null) {
							LOG.debug("Car SKU attribute already exists.");
							carSkuAttribute.setCarSkuAttrId(existingAttribute.getCarSkuAttrId());
						}

						// Prefill CAR SKU attribute only if it doesn't already
						// have a value

						if (existingAttribute != null && existingAttribute.getAttrValue() == null) {

							carSkuAttribute.setVendorSku(vendorSku);
							carSkuAttribute.setAttribute(mappedAttribute);
							carSkuAttribute.setAttrValue(recordValue);

							// Save the CAR attribute
							getDropshipManager().saveOrUpdateCarSkuAttribute(carSkuAttribute);
						}

					}
				}
			}
		} // end of while

		// Get records for pre-filling cars_attribute table
		// Get the records which has latest UPC from latest catalog for the
		// style

		List<VendorCatalogRecord> latestRecordList = getDropshipManager().getRecordsForLatestStyleUPC(
				latestCatalog.getVendorCatalogID(), style.getVendorStyleNumber());

		for (VendorCatalogRecord recordModel : latestRecordList) {

			// Retrieve Blue Martini attribute name
			final String bmsAttrName = getDropshipManager().getAttributeForRecord(recordModel,
					template.getVendorCatalogTmplID());
			attrIter = attributes.iterator();
			while (attrIter.hasNext()) {
				Attribute attr = attrIter.next();

				// If Blue Martini attribute name matches with that of record
				// attribute
				//
				if (attr.getBlueMartiniAttribute().equals(bmsAttrName)) {
					LOG.debug("Attribute in cars attribute=" + bmsAttrName);
					final String recordValue = getRecordValue(template, recordModel);

					// Get the existing attribute in car_attribute table for car

					CarAttribute carAttribute = getDropshipManager().getCarAttribute(
							car.getCarId(), attr.getAttributeId());

					// If car Attribute found then update it else
					// insert into car attribute table
					LOG.debug("recordValue="+recordValue);
					if (carAttribute != null) {
						LOG.debug("Attribute not null=" + carAttribute.getCarAttrId() + " "	+ bmsAttrName);
						carAttribute.setAttrValue(recordValue);
						carAttribute.setHasChanged("Y");
						carAttribute.setStatusCd(Status.ACTIVE);
						this.setAuditInfoCustom(carAttribute);
					}
					else {
						LOG.debug("Attribute not present=" + bmsAttrName);
						carAttribute = new CarAttribute();
						carAttribute.setAttribute(attr);
						carAttribute.setCar(car);
						carAttribute.setAttrValue(recordValue);
						carAttribute.setHasChanged("N");
						carAttribute.setIsChangeRequired("N");
						carAttribute.setStatusCd(Status.ACTIVE);
						carAttribute.setDisplaySeq((short) 0);
						carAttribute
								.setAttributeValueProcessStatus((AttributeValueProcessStatus) dropshipManager
										.getById(AttributeValueProcessStatus.class,
												AttributeValueProcessStatus.CHECK_REQUIRED));
						this.setAuditInfoCustom(carAttribute);
					}
					getDropshipManager().saveOrUpdateCarAttribute(carAttribute);
				}
			}

		} // end of for
		//Any unmapped fields that have a value in it should be added to
		// a new CAR Note

		List<VendorCatalogHeader> unmappedHeaders = this.getUnmappedAttribute(latestCatalog
				.getVendorCatalogID(), latestCatalog.getVendorCatalogTemplate()
				.getVendorCatalogTmplID());
		Map<String,String> headerAndValue = new HashMap<String, String>();
		for (VendorCatalogHeader unmappedHeader : unmappedHeaders) {
			for (VendorCatalogRecord recordModel : latestRecordList) {
				if (unmappedHeader.getVendorCatalogFieldNum() == recordModel.getCompositeKey()
						.getHeaderNum().longValue()) {
					headerAndValue.put(unmappedHeader.getVendorCatalogHeaderFieldName(), recordModel.getVendorCatalogFieldValue());
					LOG.debug("Unmapped Headers="+unmappedHeader.getVendorCatalogHeaderFieldName());
					LOG.debug("Value-"+recordModel.getVendorCatalogFieldValue());
					break;
				}
			}
		}
		boolean isPrivateFlag = false;
		StringBuffer msg = new StringBuffer(100);
		// Move the msg message inside for loop 
		Set<String> unmappedHeadersKetSet=headerAndValue.keySet();
		for(String header :unmappedHeadersKetSet){
			msg.append("The following information was available from vendor catalog: ").append(
			"\n vendor-field-name").append("\t").append("catalog-value");
			msg.append("\n")
			.append(header)
			.append("\t")
			.append(headerAndValue.get(header));
		}

		Car updatedCar = getCarManager().addCarNote(car, isPrivateFlag, msg.toString(),
				CAR_NOTES_TYPE_CD, getSystemUser().getUsername());
         
		// update the CAR status
		updatedCar.setCurrentWorkFlowStatus(getNextWorkflowStatus());

		getCarManager().save(updatedCar);

		updated = true; 

		return updated;
	}

	/**
	 * @param vendorCatalogID
	 * @param templateId
	 * @return Set<Long>
	 * @return Set<Long>
	 * @Enclosing_Method getUnmappedAttribute
	 */
	private List<VendorCatalogHeader> getUnmappedAttribute(Long vendorCatalogID, long templateId) {
		return getDropshipManager().getUnmappedAttribute(vendorCatalogID, templateId);

	}

	/**
	 * Send an exception
	 * 
	 * @param processName the name of this process
	 * @param ex the Exception
	 */
	public void processException(final String processName, final Exception ex) {
		LOG.error(processName + " failed", ex);
	}

	private void setAuditInfoCustom(BaseAuditableModel model) {
		String user = CARS_ADMIN_USER;
		if (user == null) {
			user = "Unknown";
		}
		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(user);
			model.setCreatedDate(new Date());
			model.setUpdatedBy(user);
			model.setUpdatedDate(new Date());
		}
		else {
			model.setUpdatedBy(user);
			model.setUpdatedDate(new Date());
		}
	}
	
	/*
	 * Method to get the latest catalogs styles  
	 */
	public Map<Long,List<String>> getLatestVendorCatalogStyles(){
		if(LOG.isDebugEnabled()){
			LOG.debug("getLatestVendorCatalogStyles ");
		}
		List<VendorCatalogStyleSkuMaster>  	latestStyles=new ArrayList<VendorCatalogStyleSkuMaster>();
		Map<Long,List<String>> catalogStyles=new HashMap<Long,List<String>>();
		latestStyles = dropshipManager.getLatestCatalogStyles();
		java.util.Iterator<VendorCatalogStyleSkuMaster> it = latestStyles.iterator();
		while(it.hasNext()){
			VendorCatalogStyleSkuMaster vs = it.next();
			List<String> styleList;
			if((styleList=catalogStyles.get(vs.getVendorCatalogId()))== null)
				styleList = new ArrayList<String>();
			// make unique styles for the catalog
			if(!styleList.contains(vs.getCompositeKey().getVendorStyleId())){
				styleList.add(vs.getCompositeKey().getVendorStyleId());
			}
			catalogStyles.put(vs.getVendorCatalogId(), styleList);
		}
		LOG.info("Latest catalog styles:"+catalogStyles);
		return catalogStyles;
	}

}
