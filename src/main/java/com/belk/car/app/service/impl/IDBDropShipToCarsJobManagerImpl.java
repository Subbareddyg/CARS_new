package com.belk.car.app.service.impl;

import org.apache.commons.lang.StringUtils;
import com.belk.car.app.Constants;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.belk.car.app.exceptions.CarJobDetailException;

import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowType;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.CatalogImportManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.IDBDropShipToCarsJobManager;

import com.belk.car.app.service.ManualCarManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.service.ReportManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.to.IdbCarSkuTO;
import com.belk.car.app.to.IdbDropshipDataTO;

import com.belk.car.app.util.PropertyLoader;
import com.belk.car.app.util.ReadIDBFileForDropship;
import com.belk.car.util.DateUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.appfuse.model.User;
import org.appfuse.service.UserManager;


/**
 * Purpose: Batch job to import IDB Feed Vendor Drop Ship data and create CARS
 * 
 * Initial Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I
 * 
 * Initial Requirements: BR.012.002 (see also BR.007)
 *
 * Description: In order to feed CARS without having a PO generated,
 *              a data feed from IDB provides the appropriate information.
 *              This feed is almost the same as the existing IDB feed into CARS.
 *              
 * @author afudxm2
 *
 */
public class IDBDropShipToCarsJobManagerImpl implements IDBDropShipToCarsJobManager {

	private ReadIDBFileForDropship readIDBFileForDropship;

	private static final Log LOG = LogFactory.getLog(IDBDropShipToCarsJobManagerImpl.class);
        private ManualCarManager manualCarManager;
	private CarLookupManager lookupManager;
	private UserManager userManager;
	private CarManager carManager;
	private WorkflowManager workflowManager;
	private AttributeManager attributeManager;
	private ProductManager productManager;
	private EmailManager sendEmailManager;
	private CatalogImportManager catalogImportManager;
	private ReportManager reportManager;

    public AttributeManager getAttributeManager() {
        return attributeManager;
    }

    public void setAttributeManager(AttributeManager attributeManager) {
        this.attributeManager = attributeManager;
    }
        

    public CarManager getCarManager() {
        return carManager;
    }

    public void setCarManager(CarManager carManager) {
        this.carManager = carManager;
    }

    public CatalogImportManager getCatalogImportManager() {
        return catalogImportManager;
    }

    public void setCatalogImportManager(CatalogImportManager catalogImportManager) {
        this.catalogImportManager = catalogImportManager;
    }

    public CarLookupManager getLookupManager() {
        return lookupManager;
    }

    public void setLookupManager(CarLookupManager lookupManager) {
        this.lookupManager = lookupManager;
    }

    public ManualCarManager getManualCarManager() {
        return manualCarManager;
    }

    public void setManualCarManager(ManualCarManager manualCarManager) {
        this.manualCarManager = manualCarManager;
    }

    public ProductManager getProductManager() {
        return productManager;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public void setReportManager(ReportManager reportManager) {
        this.reportManager = reportManager;
    }

    public EmailManager getSendEmailManager() {
        return sendEmailManager;
    }

    public void setSendEmailManager(EmailManager sendEmailManager) {
        this.sendEmailManager = sendEmailManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public WorkflowManager getWorkflowManager() {
        return workflowManager;
    }

    public void setWorkflowManager(WorkflowManager workflowManager) {
        this.workflowManager = workflowManager;
    }

        

	/**
	 * @return the readIDBFileForDropship
	 */
	public ReadIDBFileForDropship getReadIDBFileForDropship() {
		return this.readIDBFileForDropship;
	}

	/**
	 * @param readIDBFileForDropship the readIDBFileForDropship to set
	 */
	public void setReadIDBFileForDropship(ReadIDBFileForDropship readIDBFileForDropship) {
		this.readIDBFileForDropship = readIDBFileForDropship;
	}

	/**
	 * Method to read file from IDB into CARS and process it
	 * 
	 * @see com.belk.car.app.service.QuartzJobManagerForDropship#importCarsForDropship()
	 */
	public void importIDBDropshipForCars() throws CarJobDetailException {

		LOG.info("---------------->  Begin importing IDB Feed Data For Dropship <-----------------");
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		String filePath =properties.getProperty("idbFeedFilePath");
        String fileName = properties.getProperty("idbFeedFileName");
        

		// We need all of the config values
		if (filePath == null) {
			throw new CarJobDetailException(
					"Cannot process import job. A configuration value is missing");
		}

		
		File[] files = null;
		
		File dir = new File(filePath);
		files = dir.listFiles();

		

		// Directory doesn't exist or there aren't any files to process
		if (files == null) {
			if (LOG.isInfoEnabled()) {
				LOG.info("There are no files to process in directory: "
						+ filePath + " Exiting job...");
			}
		}
		else {
			LOG.debug("inside else file length" + files.length);
			for (int i = 0; i < files.length; i++) {
				
				if (files[i].getName().equals(fileName)) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("if file name is " + fileName + "...");
					}

					List<IdbDropshipDataTO> col = null;
					
					// Process Files
					try {
						col = getReadIDBFileForDropship().process(files[i]);
					}
					catch (ParseException e) {
						LOG.warn("Exception processing file", e);
					}
					catch (Exception e) {
						LOG.fatal("Exception reading IDB File ", e);
					}

					if (col == null || col.isEmpty()) {
						LOG.warn("There was an error processing. "
								+ files[0].getName()
								+ " Skipping to next file...");
					}
					else {
						processDropships(col);
					}

				}// End of if filename is dropship.txt loop
			}// End of no of files for loop

		}

		if (LOG.isInfoEnabled()) {
			LOG.info("---------------->  End importing IDB Feed For Dropship <-----------------");
		}
	}

	
	/**
	 * Process drop ships.
	 * Convert the Drop-ship data into IDB CAR data objects, 
	 * then use the base processCars() method to process the IDB CAR objects 
	 * 
	 * @param dropships
	 */
	private void processDropships(final Collection<IdbDropshipDataTO> dropships) {

		final Collection<IdbCarDataTO> carsData = new ArrayList<IdbCarDataTO>();
		for (IdbDropshipDataTO dropshipData : dropships) {
			final IdbCarDataTO carData = getCarData(dropshipData);
			carsData.add(carData);
		}

		processCars(carsData);
	}

	/**
	 * Map one IDB Drop-ship data object to an IDB Cars data object 
	 * 
	 * @param dropshipData
	 * @return IDB CAR data
	 */
	private IdbCarDataTO getCarData(final IdbDropshipDataTO dropshipData) {

		if (LOG.isDebugEnabled()) {
			LOG.debug("getCarData for: " + dropshipData.getVendorStyleID());
		}
		
		final IdbCarDataTO carData = new IdbCarDataTO();
		
		carData.setClassName(dropshipData.getClassName());
		carData.setClassNumber(dropshipData.getClassNum().toString());
		carData.setDepartmentName(dropshipData.getDeptName());
		carData.setDepartmentNumber(dropshipData.getDeptNum().toString());
		carData.setVendorName(dropshipData.getVendorName());
		carData.setVendorNumber(dropshipData.getVendorNumber());
		carData.setVendorStyle(dropshipData.getVendorStyleID());
		carData.setVendorStyleDescription(dropshipData.getStyleDesc());

		// Add the SKU
		
		IdbCarSkuTO idbSku = new IdbCarSkuTO();

		idbSku.setVendorColor(dropshipData.getVendorColorCode());
		idbSku.setVendorColorName(dropshipData.getColorDesc());
		idbSku.setVendorSizeCode(dropshipData.getVndrSizeCode());
		idbSku.setVendorSizeDesc(dropshipData.getVndrSize());
		idbSku.setLongSku(dropshipData.getBelkUPC());
		idbSku.setSetFlag("Y");
		
		carData.addSku(idbSku);
		
		return carData;
	}

        /**
	 * processCars
	 * @param col
	 */
	protected void processCars(Collection<IdbCarDataTO> col) {

		if (col != null) {
			if (LOG.isDebugEnabled())
				LOG.debug("Processing Cars: There are " + col.size() + " vendor styles in the file.");
		}
		Config userName = (Config) lookupManager.getById(Config.class, Config.CAR_IMPORT_USER);

		WorkflowStatus initiated = (WorkflowStatus) this.lookupManager.getById(WorkflowStatus.class, WorkflowStatus.INITIATED);
		UserType buyer = (UserType) this.lookupManager.getById(UserType.class, UserType.BUYER);

		ManualCarProcessStatus completeWithError = (ManualCarProcessStatus) carManager.getFromId(ManualCarProcessStatus.class,
				ManualCarProcessStatus.COMPLETED_WITH_ERROR);
		ManualCarProcessStatus completed = (ManualCarProcessStatus) carManager.getFromId(ManualCarProcessStatus.class, ManualCarProcessStatus.COMPLETED);

		SourceType sourceManualCar = carManager.getSourceTypeForCode(SourceType.MANUAL);
		SourceType sourcePOCar = carManager.getSourceTypeForCode(SourceType.PO);
		SourceType sourceFJCar = carManager.getSourceTypeForCode(SourceType.FINE_JEWELRY);
		AttributeValueProcessStatus checkRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.CHECK_REQUIRED);
		AttributeValueProcessStatus noCheckRequired = lookupManager.getAttributeValueProcessStatus(AttributeValueProcessStatus.NO_CHECK_REQUIRED);

		NoteType carNoteType = (NoteType) lookupManager.getById(NoteType.class, NoteType.CAR_NOTES);
		WorkFlow defaultWorkflow = workflowManager.getWorkFlow(workflowManager.getWorkflowType(WorkflowType.CAR));

		ContentStatus contentInProgress = (ContentStatus) lookupManager.getById(ContentStatus.class, ContentStatus.IN_PROGRESS);
		VendorStyleType vendorStyleTypeProduct = (VendorStyleType) lookupManager.getById(VendorStyleType.class, VendorStyleType.PRODUCT);

		User systemUser = this.userManager.getUserByUsername(userName.getValue());

		String vendorContactEmail = "vendor@belk.com";
		Date escalationDate = this.getEscalationDate();

		// Benchmark
		long executionTime = System.currentTimeMillis();
		int totalRecordSize = col.size();
		int rec = 0, numSkusInFile = 0, numElimSkus = 0;
		Map<VendorStyle, List<IdbCarDataTO>> productMap = new HashMap<VendorStyle, List<IdbCarDataTO>>();

		nextCarFromIdb:
		for (IdbCarDataTO idbCarTO : col) {
			if (LOG.isDebugEnabled())
				LOG.debug("Processing Vendor Style: " + idbCarTO.getVendorStyle() + " which has " + idbCarTO.getSkuInfo().size() + " skus...");
			numSkusInFile = numSkusInFile + idbCarTO.getSkuInfo().size();
			try {
				if (VendorStyle.FLAG_NO.equals(idbCarTO.getValidItemFlag())) {
					if (LOG.isErrorEnabled())
						LOG.error("Error in retrieving Item Information from IDB");
					// Invalid Data
					if (StringUtils.isNotBlank(idbCarTO.getManualCarId())) {
						try {
							ManualCar mCar = (ManualCar) carManager.getFromId(ManualCar.class, Long.parseLong(idbCarTO.getManualCarId()));
							if (mCar != null) {
								mCar.setProcessStatus(completeWithError);
								this.setAuditInfo(systemUser, mCar);
								carManager.save(mCar);
							}
						} catch (Exception ex) {
							if (LOG.isErrorEnabled())
								LOG.error("Error in processing manual car: " + ex);
						}
					}
					numElimSkus = numElimSkus + idbCarTO.getSkuInfo().size();
					continue nextCarFromIdb;
				}


				// Check if Department Exists
				Department dept = carManager.getDepartment(idbCarTO.getDepartmentNumber());
				List<String> errorList = new ArrayList<String>();
				if (dept == null) {
					// Missing Department Do Not Process Data
					String errorText = "Department: " + idbCarTO.getDepartmentNumber() + " not setup in the database";
					errorList.add(errorText);
					if (LOG.isDebugEnabled()) {
						LOG.debug(errorText);
					}

				}
				idbCarTO.setDepartment(dept);

				Classification classification = null;
				if (StringUtils.isBlank(idbCarTO.getClassNumber())) {
					String errorText = "Classification Is Null For: " + idbCarTO.getVendorStyle() + ", invalid data!!";
					errorList.add(errorText);
					if (LOG.isDebugEnabled()) {
						LOG.debug(errorText);
					}
				} else {
					classification = carManager.getClassification(Short.parseShort(idbCarTO.getClassNumber()));
				}

				if (classification == null) {
					// Missing Classification Do Not Process Data
					String errorText = "Classification: " + idbCarTO.getClassNumber() + " not setup in the database";
					errorList.add(errorText);
					if (LOG.isDebugEnabled()) {
						LOG.debug(errorText);
					}
				}

				idbCarTO.setClassification(classification);

				if (StringUtils.isNotBlank(idbCarTO.getManualCarId())) {
					idbCarTO.setManualCar((ManualCar) carManager.getFromId(ManualCar.class, Long.parseLong(idbCarTO.getManualCarId())));
				}

				//Check if Vendor Style Exists
				//If VendorStyle Exists then Check if SKU Exists in CAR
				//If VendorStyle itself does not Exist, there is no point is check for CARS
				VendorStyle vendorStyle = carManager.getVendorStyle(idbCarTO.getVendorNumber(), idbCarTO.getVendorStyle());
				VendorStyle pattern = null;
				List<IdbCarSkuTO> existingSkus = new ArrayList<IdbCarSkuTO>();
				if (vendorStyle != null) {
					idbCarTO.setVendorStyleObj(vendorStyle);
					idbCarTO.setVendor(vendorStyle.getVendor());


					//Update the VendorStyle Information
					if (StringUtils.isEmpty(StringUtils.trimToNull(vendorStyle.getVendorStyleName()))) {
						vendorStyle.setVendorStyleName(idbCarTO.getVendorStyleDescription());
						vendorStyle.setDescr(idbCarTO.getVendorStyleDescription());

						//create or update vendor style
						carManager.createVendorStyle(vendorStyle);
					}

					//Update the Vendor Information
					Vendor vendor = vendorStyle.getVendor();
					if (!StringUtils.equals(idbCarTO.getVendorName(), vendor.getName())) {
						vendor.setName(idbCarTO.getVendorName());
						vendor.setDescr(idbCarTO.getVendorName());
                                                vendor.setIsDisplayable("Y");
						//create or update vendor
						carManager.createVendor(vendor);
					}
					pattern = vendorStyle.getParentVendorStyle();

					List<VendorSku> skusOfStyle = carManager.getSkusForStyle(vendorStyle);
					Map<String, ArrayList<Car>> skumap = new HashMap<String, ArrayList<Car>>();
					java.util.Iterator<VendorSku> it = skusOfStyle.iterator();
					while(it.hasNext()){
						VendorSku vs = it.next();
						ArrayList<Car> carList;
						if((carList=skumap.get(vs.getBelkUpc()))== null)
							 carList = new ArrayList<Car>();
						carList.add(vs.getCar());
						skumap.put(vs.getBelkUpc(), carList);
					}

				if(!(skumap == null ||skumap.isEmpty())){
					for(IdbCarSkuTO sku : idbCarTO.getSkuInfo()){
						List<Car> skuTocarList= null;
						if((skuTocarList=skumap.get(sku.getBelkUPC()))!= null){
							java.util.Iterator<Car> cariteraor = skuTocarList.iterator();
							while(cariteraor.hasNext()){
							Car skuCar = cariteraor.next();
							if(skuCar.getStatusCd().equals(Status.DELETED)){
								if(skuCar.getSourceId().equals(idbCarTO.getPoNumber())){
									existingSkus.add(sku);
									if (LOG.isDebugEnabled())
										LOG.debug("sku "+ sku.getBelkUPC()+ " found in existing deleted car #"+ skuCar.getCarId() +" with same PO number ");
									break;
								}
							}
							else{
								 existingSkus.add(sku);
								 if (LOG.isDebugEnabled())
									LOG.debug("sku "+ sku.getBelkUPC() +" found in existing Active car #"+ skuCar.getCarId());
								 break;
							 }
						 }
					   }
					}
						//Remove all Existing SKU's
						if (!existingSkus.isEmpty()) {
							numElimSkus = numElimSkus + existingSkus.size();
							idbCarTO.getSkuInfo().removeAll(existingSkus);
						}
						if (idbCarTO.getSkuInfo().isEmpty()) {
							String errorText = "All Skus in found in other ACTIVE/DLETED Cars (Skus):  " + existingSkus;
							errorList.add(errorText);
							if (LOG.isDebugEnabled())
								LOG.debug(errorText);
							}
						}
					}

				// Create CAR only if there are no CAR for the and or SKU does not exist in another CAR...
				// VENDOR_STYLE
				if (!errorList.isEmpty()) {
					if (idbCarTO.getManualCar() != null && idbCarTO.getManualCar().getProcessStatus().getStatusCd().equals(ManualCarProcessStatus.EXPORTED)) {
						try {
							idbCarTO.getManualCar().setProcessStatus(completeWithError);
							idbCarTO.getManualCar().setPostProcessInfo(StringUtils.left(errorList.toString(), 300));
							this.setAuditInfo(systemUser, idbCarTO.getManualCar());
							carManager.save(idbCarTO.getManualCar());
						} catch (Exception ex) {
							if (LOG.isErrorEnabled())
								LOG.error("Error in processing manual car: " + ex);
						}

					}
				//
				    if(existingSkus.isEmpty())
					 numElimSkus = numElimSkus + idbCarTO.getSkuInfo().size();
					continue nextCarFromIdb;
				}

				//Go the next Row if there are no Skus that in the data
				if (idbCarTO != null && idbCarTO.getSkuInfo().isEmpty()) {
					if (LOG.isDebugEnabled())
						LOG.debug("There is not SKU information available for Product: " + idbCarTO.getVendorStyle());
					continue nextCarFromIdb;
				}

				if (vendorStyle == null) {
					Vendor vendor = carManager.getVendor(idbCarTO.getVendorNumber());
					if (vendor == null) {
						// Create a Vendor
						vendor = new Vendor();
						vendor.setName(idbCarTO.getVendorName());
						vendor.setVendorNumber(idbCarTO.getVendorNumber());
						vendor.setDescr(idbCarTO.getVendorName());
						vendor.setStatusCd(Status.ACTIVE);
						vendor.setContactEmailAddr(vendorContactEmail);
						this.setAuditInfo(systemUser, vendor);
                                                vendor.setIsDisplayable("Y");

						vendor = carManager.createVendor(vendor);

						if (LOG.isDebugEnabled())
							LOG.debug("Vendor Created with ID: " + vendor.getVendorId());

					} else if (!StringUtils.equals(idbCarTO.getVendorName(), vendor.getName())) {
						vendor.setName(idbCarTO.getVendorName());
						//vendor.setVendorNumber(idbCarTO.getVendorNumber());
						vendor.setDescr(idbCarTO.getVendorName());
						vendor.setStatusCd(Status.ACTIVE);
						this.setAuditInfo(systemUser, vendor);
                                                vendor.setIsDisplayable("Y");
						vendor = (Vendor) carManager.createVendor(vendor);

					}

					//Removed the code which creates pattern based on pattern processing rule of classifications

					vendorStyle = new VendorStyle();
					vendorStyle.setVendorNumber(idbCarTO.getVendorNumber());
					vendorStyle.setVendorStyleName(idbCarTO.getVendorStyleDescription());
					vendorStyle.setVendorStyleNumber(idbCarTO.getVendorStyle());
					vendorStyle.setDescr(idbCarTO.getVendorStyleDescription()); // change to <blank>
					// based on the bug
					// idbCarTO.getVendorStyleDescription()
					vendorStyle.setStatusCd(Status.ACTIVE);
					this.setAuditInfo(systemUser, vendorStyle);
					vendorStyle.setVendorStyleType(vendorStyleTypeProduct);
					vendorStyle.setVendor(vendor);
					vendorStyle.setClassification(classification);
					vendorStyle.setParentVendorStyle(pattern);

					vendorStyle = carManager.createVendorStyle(vendorStyle);

					idbCarTO.setVendor(vendor);
					idbCarTO.setVendorStyleObj(vendorStyle);

					if (LOG.isDebugEnabled())
						LOG.debug("Vendor Style Created with ID: " + vendorStyle.getVendorStyleId());
				}

				List<IdbCarDataTO> vendorStyleList = null;
				if (pattern != null) {
					if (productMap.containsKey(pattern)) {
						vendorStyleList = productMap.get(pattern);
					} else {
						vendorStyleList = new ArrayList<IdbCarDataTO>();
						productMap.put(pattern, vendorStyleList);
					}
					vendorStyleList.add(idbCarTO);
				} else {
					vendorStyleList = new ArrayList<IdbCarDataTO>();
					vendorStyleList.add(idbCarTO);
					productMap.put(vendorStyle, vendorStyleList);
				}
			} catch (Exception ex) {
				if (LOG.isErrorEnabled()) {
					ex.printStackTrace();
					LOG.error("Transaction Exception. Cause= " + ex.getMessage());
				}
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Going to Process Cars: # of Vendor Styles to process - " + productMap.size());
			LOG.debug("<!------ # SKUs - Num Of Sku In File: " + numSkusInFile + "---------->");
			LOG.debug("<!------ # SKUs - Num Of Skuz Eliminated: " + numElimSkus + "---------->");
		}

		for (VendorStyle vs : productMap.keySet()) {
			if (LOG.isDebugEnabled())
				LOG.debug("Process Cars for Vendor Style: " + vs.getVendorStyleNumber());

			long startime = System.currentTimeMillis();
			List<IdbCarDataTO> lCarData = productMap.get(vs);
			Car car = null;
			CarNote note = null;
			for (IdbCarDataTO idbCarTO : lCarData) {

				LOG.debug("Processing Record: " + (++rec) + " of " + totalRecordSize + " Vendoe: " + idbCarTO.getPoNumber() + " Manual Car Id: "
						+ idbCarTO.getManualCarId());

				if (car == null) {
					car = new Car();
					car.setDepartment(idbCarTO.getDepartment());

					if (idbCarTO.getManualCar() != null) {
						car.setSourceType(sourceManualCar);
						car.setSourceId(idbCarTO.getManualCar().getCreatedBy());
					} else if (StringUtils.isNotBlank(idbCarTO.getManualCarId()) && idbCarTO.getManualCar() == null) {
						car.setSourceType(sourceManualCar);
						car.setSourceId("Manual Car: " + idbCarTO.getManualCarId());
					} else if (StringUtils.isNotBlank(idbCarTO.getPoNumber())) {
						car.setSourceType(sourcePOCar);
						car.setSourceId(idbCarTO.getPoNumber());
					} else {
						car.setSourceType(sourceFJCar);
						
						// //Currently PO Number is Blank for Fine
						// Jewelry
						car.setSourceId(idbCarTO.getVendorNumber() + "-" + idbCarTO.getVendorStyle());
					}
					car.setVendorStyle(vs);
					car.setWorkFlow(defaultWorkflow);
					car.setCarUserByLoggedByUserId(systemUser);
					car.setAssignedToUserType(buyer);
					car.setCurrentWorkFlowStatus(initiated);

					// Set the Initial Escalation Date to 7 Days...
					// Need to figure out where we should
					// default the 7 Days from i.e Transition OR
					// Config
					car.setEscalationDate(escalationDate);

					car.setIsUrgent(Constants.FLAG_NO);

					if (StringUtils.isNotBlank(idbCarTO.getExpectedShipDate())) {
						car.setExpectedShipDate(DateUtils.parseDate(idbCarTO.getExpectedShipDate(), "yyyy-MM-dd"));
						car.setDueDate(this.getDueDate());
					} else {
						car.setExpectedShipDate(this.getCompletionDate());
						car.setDueDate(this.getDueDate());
					}

					String isProductTypeReq = Constants.FLAG_NO;
					List<ProductType> l = productManager.getProductTypes(vs.getClassification().getClassId());
					ProductType productType = null;
					if (l != null && !l.isEmpty()) {
						if (l.size() > 1) {
							isProductTypeReq = Constants.FLAG_YES;
						} else {
							productType = l.get(0);
						}
					}
					car.setIsProductTypeRequired(isProductTypeReq);
					car.setStatusCd(Status.ACTIVE);

					car.setContentStatus(contentInProgress);
					car.setLastWorkflowStatusUpdateDate(Calendar.getInstance().getTime());

					this.setAuditInfo(systemUser, car);

					if (productType != null) {
						List<DepartmentAttribute> departmentAttributes = carManager.getAllDepartmentAttributes(car.getDepartment().getDeptId());
						List<ClassAttribute> classificationAttributes = carManager.getAllClassificationAttributes(car.getVendorStyle().getClassification()
								.getClassId());

						Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();
						Map<String, String> attributeDefaultValueMap = new HashMap<String, String>();

						for (DepartmentAttribute deptAttr : departmentAttributes) {
							if (deptAttr.getAttribute().isActive()) {
								attributeMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), deptAttr.getAttribute());
								attributeDefaultValueMap.put(deptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(deptAttr
										.getDefaultAttrValue()));
							}
						}

						//Setup Product Type Attributes
						if (productType != null) {
							//Set the VendorStyle Product
							vs.setProductType(productType);
							for (ProductTypeAttribute ptAttr : productType.getProductTypeAttributes()) {
								if (ptAttr.getAttribute().isActive()) {
									if (attributeMap.containsKey(ptAttr.getAttribute().getBlueMartiniAttribute())) {
										attributeMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
										attributeDefaultValueMap.remove(ptAttr.getAttribute().getBlueMartiniAttribute());
									}
									attributeMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), ptAttr.getAttribute());
									attributeDefaultValueMap.put(ptAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(ptAttr
											.getDefaultAttrValue()));
								}
							}
						}

						for (ClassAttribute classAttr : classificationAttributes) {
							if (classAttr.getAttribute().isActive()) {
								if (attributeMap.containsKey(classAttr.getAttribute().getBlueMartiniAttribute())) {
									attributeMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
									attributeDefaultValueMap.remove(classAttr.getAttribute().getBlueMartiniAttribute());
								}
								attributeMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), classAttr.getAttribute());
								attributeDefaultValueMap.put(classAttr.getAttribute().getBlueMartiniAttribute(), StringUtils.defaultString(classAttr
										.getDefaultAttrValue()));
							}
						}

						for (Attribute attribute : attributeMap.values()) {
							CarAttribute carAttribute = new CarAttribute();
							carAttribute.setAttribute(attribute);
							carAttribute.setCar(car);

							if (attribute.getAttributeConfig().getHtmlDisplayType().isAutocomplete()) {
								carAttribute.setAttributeValueProcessStatus(checkRequired);
							} else {
								carAttribute.setAttributeValueProcessStatus(noCheckRequired);
							}
							// Setting to blank for now. Need to get it from the association
							carAttribute.setAttrValue(attributeDefaultValueMap.get(attribute.getBlueMartiniAttribute()));
							carAttribute.setDisplaySeq((short) 0);
							carAttribute.setHasChanged(Constants.FLAG_NO);
							carAttribute.setIsChangeRequired(Constants.FLAG_YES);
							carAttribute.setStatusCd(Status.ACTIVE);
							this.setAuditInfo(systemUser, carAttribute);
							car.getCarAttributes().add(carAttribute);
						}
					}

				}

				if (StringUtils.isNotBlank(idbCarTO.getExpectedShipDate()) && StringUtils.isNotBlank(idbCarTO.getPoNumber())) {
					Date expectedShipDate = DateUtils.parseDate(idbCarTO.getExpectedShipDate(), "yyyy-MM-dd");
					if (expectedShipDate.before(car.getExpectedShipDate())) {
						car.setExpectedShipDate(expectedShipDate);
						car.setSourceId(idbCarTO.getPoNumber());
					}
				}

				//Add Note to Existing Car...
				if (idbCarTO.getExistingCar() != null && note == null) {
					note = new CarNote();
					note.setNoteType(carNoteType);
					note.setIsExternallyDisplayable(CarNote.FLAG_NO);
					note.setStatusCd(Status.ACTIVE);
					note.setCar(car);
					note.setNoteText("Car ID: " + idbCarTO.getExistingCar().getCarId()
							+ " exists for the same Vendor Style.  You can use the copy functionality to update the attributes from this Car.");
					this.setAuditInfo(systemUser, note);
					car.getCarNotes().add(note);
				}

				// Create CAR/CAR SKU Attributes
				for (IdbCarSkuTO idbSku : idbCarTO.getSkuInfo()) {

					/*
					//Don't need to do the check for existing sku, becuase we are already doing that in the check above SKU's exisitance
					//in other active cars above
					//if we have reached this point it means that we need to create the Vendor SKU
					//VendorSku sku = carManager.getSku(idbSku.getBelkUPC());
					
					 */
					VendorSku sku = new VendorSku();
					sku.setCar(car);
					sku.setBelkSku(idbSku.getBelkUPC());
					sku.setLongSku(idbSku.getLongSku());
					sku.setBelkUpc(idbSku.getBelkUPC());
					sku.setVendorUpc(idbSku.getLongSku());
					sku.setStatusCd(Status.ACTIVE);
					sku.setColorCode(idbSku.getVendorColor());
					sku.setColorName(idbSku.getVendorColorName());
					sku.setSizeCode(idbSku.getVendorSizeCode());
					sku.setSizeName(idbSku.getVendorSizeDesc());
					//CARS Size Conversion Issue - Size name overwritten by resync size job-->
					sku.setIdbSizeName(idbSku.getVendorSizeDesc());//Adding size name of the SKU in conversion_name column to show the incoming size name in cars edit page
					sku.setParentUpc(idbSku.getParentUPC());
					sku.setSetFlag(idbSku.getSetFlag());
					sku.setVendorStyle(idbCarTO.getVendorStyleObj());
					this.setAuditInfo(systemUser, sku);

					car.getVendorSkus().add(sku);
				}
			}

			CatalogProduct product = null;
			//Checking for product information in Catalog
			if (car.getVendorStyle().isPattern()) {
				//Find choice based on the first Product with Content within a Pattern
				VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
				criteria.setVendorStyleId(car.getVendorStyle().getVendorStyleId());
				criteria.setChildrenOnly(true);
				if (LOG.isDebugEnabled())
					LOG.debug("Pattern - Search for child product information ");
				List<VendorStyle> childProducts = this.carManager.searchVendorStyle(criteria);
				if (childProducts != null) {
					for (VendorStyle childProduct : childProducts) {
						if (LOG.isDebugEnabled())
							LOG.debug("Pattern - Searching for product information in catalog " + childProduct.getVendorStyleNumber());
						product = catalogImportManager.getProduct(childProduct.getVendorNumber(), childProduct.getVendorStyleNumber());
						if (product != null) {
							break;
						}
					}
				}

			} else {
				if (LOG.isDebugEnabled())
					LOG.debug("Product - Searching for product information in catalog " + car.getVendorStyle().getVendorStyleNumber());
				product = catalogImportManager.getProduct(car.getVendorStyle().getVendorNumber(), car.getVendorStyle().getVendorStyleNumber());

			}

			if (product == null) {
				//check based on sku
				CatalogSku sku = null;
				if (car != null && car.getVendorSkus() != null && !car.getVendorSkus().isEmpty()) {
					catalogImportManager.getSku(car.getVendorSkus().iterator().next().getVendorUpc());
				}

				if (sku != null) {
					product = sku.getCatalogProduct();
				}
			}

			if (product != null && product.getCatalogProductId() != 0 && car.getVendorStyle().getProductType() != null) {
				catalogImportManager.copyToCar(product, car);

				//Item found in catalog.. then assign the CAR to buyer
				if (car.getCarId() == 0) {
					car.setAssignedToUserType(buyer);
				}
			}

			try {
				if (LOG.isDebugEnabled())
					LOG.debug("Creating Car for Vendor Style with ID: " + car.getVendorStyle().getVendorStyleId());
				car = carManager.createCar(car);
			} catch (Exception ex) {
				if (LOG.isErrorEnabled())
					LOG.error("Save Exception. Cause: " + ex.getMessage());
			}

			// Update the Manual CAR
			if (lCarData != null) {
				for (IdbCarDataTO idbCarTO : lCarData) {
					if (idbCarTO.getManualCar() != null && car != null) {
						try {
							idbCarTO.getManualCar().setProcessStatus(completed);
							idbCarTO.getManualCar().setPostProcessInfo("Car Created With ID: " + car.getCarId());
							this.setAuditInfo(systemUser, idbCarTO.getManualCar());
							carManager.save(idbCarTO.getManualCar());
						} catch (Exception ex) {
							if (LOG.isErrorEnabled())
								LOG.error("Error in processing manual car: " + ex);
						}
					}
				}
			}// End of My Car Update

			if (LOG.isInfoEnabled())
				LOG.info("End time for current Car = " + (System.currentTimeMillis() - startime));
		}

		if (LOG.isInfoEnabled()) {
			LOG.info("----------------> Overall execution time in minutes  = " + (System.currentTimeMillis() - executionTime) / 60000);
			LOG.info("---------------->  End Importing Car Data   <-----------------");
		}

	}
        private Date getEscalationDate() {
		Config numberOfEscalationDays = (Config) lookupManager.getById(Config.class, Config.INIT_NUMBER_OF_ESCALATION_DAYS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(numberOfEscalationDays.getValue()));
		return cal.getTime();
	}

	private Date getDueDate() {
		Config numberOfDueDays = (Config) lookupManager.getById(Config.class, Config.INIT_NUMBER_OF_DUE_DAYS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(numberOfDueDays.getValue()));
		return cal.getTime();
	}

	private Date getCompletionDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Car.NUM_DAYS_TO_COMPLETION);
		return cal.getTime();
	}
        public void setAuditInfo(User user, BaseAuditableModel model) {

		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(user.getUsername());
			model.setCreatedDate(new Date());
		}
		model.setUpdatedBy(user.getUsername());
		model.setUpdatedDate(new Date());
	}

}
