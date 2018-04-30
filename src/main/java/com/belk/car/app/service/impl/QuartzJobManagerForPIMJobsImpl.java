package com.belk.car.app.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
import org.springframework.transaction.annotation.Transactional;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.DropshipDao;
import com.belk.car.app.dto.PackDTO;
import com.belk.car.app.dto.SkuDTO;
import com.belk.car.app.dto.StyleDTO;
import com.belk.car.app.exceptions.GroupCreateException;
import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarUserVendorDepartment;
import com.belk.car.app.model.CarsPimGroupMapping;
import com.belk.car.app.model.CarsPimItemUpdate;
import com.belk.car.app.model.ClassAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.ClosedCarAttrSync;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.DepartmentAttribute;
import com.belk.car.app.model.GroupCreateMsgFailure;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.PIMAttribute;
import com.belk.car.app.model.PatternProcessingRule;
import com.belk.car.app.model.PoUnitDetail;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.ProductTypeAttribute;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.UsersRank;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStylePIMAttribute;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.WorkFlow;
import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.CatalogImportManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.PIMAttributeManager;
import com.belk.car.app.service.PIMFtpImageManager;
import com.belk.car.app.service.PatternAndCollectionManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.service.QuartzJobManagerForPIMJobs;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.util.UpdateAttributesFromPIMThread;
import com.belk.car.app.util.BMIFileGenerationUtil;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.integrations.pim.GroupingMessageMQListener;
import com.belk.car.integrations.pim.JmsQueueSender;
import com.belk.car.integrations.pim.xml.CarsDifferentiators;
import com.belk.car.integrations.pim.xml.CarsDropshipItem;
import com.belk.car.integrations.pim.xml.CarsDropshipPackItem;
import com.belk.car.integrations.pim.xml.CarsGroupingDtl;
import com.belk.car.integrations.pim.xml.CarsItem;
import com.belk.car.integrations.pim.xml.CarsPackItem;
import com.belk.car.integrations.pim.xml.CarsPoDtl;
import com.belk.car.integrations.pim.xml.DropShipMessage;
import com.belk.car.integrations.pim.xml.GroupingComponent;
import com.belk.car.integrations.pim.xml.GroupingMessage;
import com.belk.car.integrations.pim.xml.GroupingParent;
import com.belk.car.integrations.pim.xml.PoMessage;
import com.belk.car.integrations.pim.xml.RLRItems;
import com.belk.car.integrations.pim.xml.SkuOrins;
import com.belk.car.util.DateUtils;
import org.hibernate.Hibernate;

public class QuartzJobManagerForPIMJobsImpl extends BaseFormController implements
QuartzJobManagerForPIMJobs {

	private CarManager carManager;
	private CarLookupManager carLookupManager;
	private PIMAttributeManager pimAttributeManager;
	private WorkflowManager workflowManager;
	private ProductManager productManager;
	private CatalogImportManager catalogImportManager;
	private UserManager userManager;
	private EmailManager sendEmailManager;
	private DropshipDao dropshipDao;
	private PIMFtpImageManager pimFtpImageManager;
	private PatternAndCollectionManager patternAndCollectionManager;
	private JmsQueueSender queueSender;

	public SourceType sourcePOCar = null; 
	public SourceType sourceManualCar=null;
	public WorkFlow defaultWorkflow = null; 
	public WorkflowStatus initiated = null;
	public WorkflowStatus withVendor = null;
	public UserType buyer =null;
	public UserType vendorType =null;
	public AttributeValueProcessStatus checkRequired =null;
	public AttributeValueProcessStatus noCheckRequired = null;
	public NoteType carNoteType = null;
	public ContentStatus contentInProgress =null;
	public Config userName=null;
	public User systemUser=null;
	public VendorStyleType vendorStyleTypeProduct= null;
	public ManualCarProcessStatus completed =null;
	public ManualCarProcessStatus completeWithError =null;
	public NotificationType type =null;
	Config sendNotifications =null;
	public Config emailNotificationList =null;

	private transient final Log log = LogFactory
			.getLog(QuartzJobManagerForPIMJobsImpl.class);
	private SourceType sourceDropshipCar;

	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public void setPimAttributeManager(PIMAttributeManager pimAttributeManager) {
		this.pimAttributeManager = pimAttributeManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}


	public void setProductManager(ProductManager productManager) {
		this.productManager = productManager;
	}


	public void setCatalogImportManager(CatalogImportManager catalogImportManager) {
		this.catalogImportManager = catalogImportManager;
	}


	public void setSendEmailManager(EmailManager sendEmailManager) {
		this.sendEmailManager = sendEmailManager;
	}

	public void setDropshipDao(DropshipDao dropshipDao) {
		this.dropshipDao = dropshipDao;
	}

    public void setPimFtpImageManager(PIMFtpImageManager pimFtpImageManager) {
        this.pimFtpImageManager = pimFtpImageManager;
    }
    
    public void setPatternAndCollectionManager(PatternAndCollectionManager patternAndCollectionManager) {
        this.patternAndCollectionManager = patternAndCollectionManager;
    }
    
    public void setQueueSender(JmsQueueSender queueSender) {
        this.queueSender = queueSender;
    }

    /**
	 * This method decide whether to create new CAR or just modify Header info based of 
	 * PO type.
	 * @throws Exception 
	 */
	public void processPOCAR(PoMessage poMessage) throws Exception  {
		log.info("------------------------> Begin Processing PO Message ------------------------>");
		try {
			this.systemUser = (User) getServletContext().getAttribute(
					Constants.SYSTEM_USER);
			String poType = poMessage.getType();
			if (null != poType && !"".equals(poType)) {
				if (Constants.PO_HDR_MOD.equalsIgnoreCase(poType)) {
					updatePOHeader(poMessage);
				} else if (Constants.PO_CRE.equalsIgnoreCase(poType)
						|| Constants.PO_DTL_CRE.equalsIgnoreCase(poType)) {
					processAndCreateCAR(poMessage);
				}
			}
		} catch (Exception ex) {
			log.error("Error while Processing PO Car: " + ex.getMessage(), ex);			
			throw ex;
		}
		log.info("------------------------> End Processing PO Message ------------------------>");
	}
    
	/**
	 * 
	 * @param poMessage
	 * @throws Exception
	 * This method used to update the expected shipDate and shipDate 
	 */
	public void updatePOHeader(PoMessage poMessage) throws Exception {
		try {
			log.info("---------------->  Start PO Header Update <-----------------");
			String poNumber = poMessage.getOrderNo().toString();

			if (log.isDebugEnabled()) {
				log.debug("PO Number is: " + poNumber);
			}
			Date poShipDate = poMessage.getNotBeforeDate()
					.toGregorianCalendar().getTime();
                        
                        List<Car> existingCar;
                        Config shipDateBySkusKillswitch = (Config)carLookupManager.getById(Config.class, Config.UPDATE_HEADER_BY_SKUS);
                        
                        // "Kill switch" configuration setting for updating ship date by list of skus.
                        if (shipDateBySkusKillswitch != null && shipDateBySkusKillswitch.getValue().toUpperCase().equals("TRUE")) {
                            SkuOrins orins = poMessage.getSkuOrins();
                            if (orins != null) {
                                List<String> skuOrins = orins.getSkuOrin();
                                existingCar = carManager.getActiveCarsBySkuOrins(skuOrins);
                                
                                if (existingCar.isEmpty()) {
                                    log.info("No created cars have been found for poNumber"+poNumber+"... checking CARS_PO_MESSAGE_ESB for uncreated CARS");
                                    // checks for uncreated cars 
                                    carManager.updateUncreatedCarsBySkuOrins(skuOrins, poShipDate);
                                }
                                
                            } else {
                                log.warn("Update CAR ship date by SKUs enabled, yet CARS received an update message with no orin list... updating by PO number"+poNumber+" instead.");
                                existingCar = carManager.getActiveCarForPO(poNumber);
                            }
                            
                        } else {
                            existingCar = carManager.getActiveCarForPO(poNumber);
                        }
                        
			

			if (null != existingCar && existingCar.size() > 0) {
				for (Car car : existingCar) {
					if (!car.getExpectedShipDate().equals(poShipDate)) {
						log	.info("updating shipdate for car: "+ car.getCarId()+"for PO Number"+poNumber);
						car.setExpectedShipDate(poShipDate);
						car.setShipDateUpdatedDate(new Date());
						car.setAuditInfo(systemUser);
						carManager.save(car);
					} else {
						log.info(" The expected ship date trying insert for the CAR: "
										+ car.getCarId()
										+ " is already exists in DB for the PO Number"+poNumber);
					}
				}
			} else {
				log.info("No Existing Cars found for the PO number " + poNumber
						+ " to update the shipdate.");
			}

		} catch (Exception ex) {
			log.error("Error while update PO Header" + ex);
			throw ex;

		}
		log.info("---------------->  End PO Header Update <-----------------");
	}

	/**
	 * This Method will create Manual Car
	 */
	public ManualCar processManualCAR(ManualCar manualcar) throws IOException {

		try {
			log.info("Create manual car for Manual Car ID :"
					+ manualcar.getManualCarId());
			this.systemUser = (User) getServletContext().getAttribute(
					Constants.SYSTEM_USER);
			this.completeWithError = (ManualCarProcessStatus) getServletContext()
					.getAttribute(
							Constants.MANUAL_CAR_STATUS_COMPLETE_WITH_ERROR);
			this.completed = (ManualCarProcessStatus) getServletContext()
					.getAttribute(Constants.MANUAL_CAR_STATUS_COMPLETED);

			// call the web-service by passing
			// vendorNumber,VendorStyleNumber,colorCode,SizeCode
			String vendorNumber = manualcar.getVendorNumber();
			String vendorStyleNumber = manualcar.getVendorStyleNumber();
			String colorCode = manualcar.getColorDescription();
			String sizeCode = manualcar.getSizeDescription();
			String isPack = manualcar.getIsPack();
			List<PackDTO> packDtoList = new ArrayList<PackDTO>();
			PackDTO packDto = new PackDTO();
			packDto.setVendorNumber(vendorNumber);
			packDto.setVendorStyleNumber(manualcar.getVendorStyleNumber());
			if (manualcar.getColorDescription() != null) {
				packDto.setColorDescription(manualcar.getColorDescription());
			}
			if (manualcar.getSizeDescription() != null) {
				packDto.setSizeDescription(manualcar.getSizeDescription());
			}
			packDtoList.add(packDto);

			Map<String, StyleDTO> packDtoMap = new HashMap<String, StyleDTO>();

			log.info("creating manual car for vendor number: " + vendorNumber
					+ " and style number: " + vendorStyleNumber);

			StyleDTO manualCarStyleDTO = null;
			// Get the StyleDTO from the web-service response
			List<StyleDTO> styleList = new ArrayList<StyleDTO>();
			List<StyleDTO> styleListTemp = null;
			List<StyleDTO> validVendorStyleList = null;
			if (Constants.FLAG_NO.equals(isPack)) {
				manualCarStyleDTO = pimAttributeManager
						.getManualCarStyleFromSMART(vendorNumber,
								vendorStyleNumber, colorCode, sizeCode);
				if (manualCarStyleDTO != null) {
					manualCarStyleDTO.setManualCar(manualcar);
					styleList.add(manualCarStyleDTO);
				} else {
					log.error("Could not find manual car details in SMART - setting processing status to completeWithError");
					manualcar.setPostProcessInfo("No items found for the specified search criteria");
					manualcar.setProcessStatus(completeWithError);
					this.setAuditInfo(systemUser, manualcar);
					carManager.saveOrUpdate(manualcar);
					return manualcar;
				}
			} else {
				styleListTemp = pimAttributeManager
						.getPackDetailsFromSMART(packDtoList);

				for (StyleDTO styleDtoObj : styleListTemp) {
					if (styleDtoObj.isSellable()) {
						styleDtoObj.setManualCar(manualcar);
						styleList.add(styleDtoObj);
					}
				}
				if (styleList.isEmpty()) {
					log.error("Could not find manual car details in SMART - setting processing status to completeWithError");
					manualcar.setPostProcessInfo("No Packs found for the specified search criteria");
					manualcar.setProcessStatus(completeWithError);
					this.setAuditInfo(systemUser, manualcar);
					carManager.saveOrUpdate(manualcar);
					return manualcar;
				}
			}
			// TO check the StyleDTO is not empty and set it to the manual car
			if (log.isDebugEnabled()) {
				if (null != manualCarStyleDTO
						&& null != manualCarStyleDTO.getVendorStyle())
					log.debug("received Vendor Style details from PIM :"
							+ manualCarStyleDTO.getVendorStyle());
			}
			// Call processValidVendorStyles method to validate the valid
			// VendorStyle
			validVendorStyleList = processValidVendorStyles(styleList);
			// to check the valid vendor style response
			if (validVendorStyleList.size() > 0) {
				StyleDTO styleDto = validVendorStyleList.get(0);
				if (styleDto.getSkuInfo().size() > 0) {
					if (log.isDebugEnabled()) {
						log.debug("found valid : Vendor style: "
								+ styleDto.getVendorStyle()
								+ "  Vendor Number: "
								+ styleDto.getVendorNumber());
					}
					// Add VendorStyle to map object for processing the SKU
					// information
					Map<VendorStyle, List<StyleDTO>> productMap = new HashMap<VendorStyle, List<StyleDTO>>();

					VendorStyle manalVS = styleDto.getVendorStyleObj();
					VendorStyle pattern = manalVS.getParentVendorStyle();
					if (pattern != null) {
						productMap.put(pattern, validVendorStyleList);
					} else {
						productMap.put(manalVS, validVendorStyleList);
					}
					// call the createCARforValidVendorStyles method to get all
					// the SKU's to create manual car

					if (Constants.FLAG_NO.equals(isPack)) {
						createCARforValidVendorStyles(productMap, packDtoMap);
					} else {
						createCarForPack(styleDto);
					}
				} else {
					log.error("SKU List is Empty: All requested skus for manual car found in existing car");
					manualcar.setPostProcessInfo("No UPCs found for the specified search criteria");
					manualcar.setProcessStatus(completeWithError);
					this.setAuditInfo(systemUser, manualcar);
					// carManager.saveOrUpdate(manualcar);
				}
			} else {
				log.error("ValiVendorSTyle List is Empty: manual car requested for vendor style is not valid");
				manualcar.setPostProcessInfo("No Items found for the specified search criteria");
				manualcar.setProcessStatus(completeWithError);
				this.setAuditInfo(systemUser, manualcar);
				// carManager.saveOrUpdate(manualcar);
			}
			carManager.saveOrUpdate(manualcar);
		} catch (Exception ex) {
			log.error("Error in processing manual car: " + ex);
			manualcar.setPostProcessInfo("Error occured while  processing manual car:");
			manualcar.setProcessStatus(completeWithError);
			this.setAuditInfo(systemUser, manualcar);
			carManager.saveOrUpdate(manualcar);
		}
		return manualcar;

	}
	/**
	 * 
	 * @param packDTO
	 * @return 
	 * @throws Exception
	 * This method used to create Pack CAR 
	 * 
	 */
	public Car createCarForPack(StyleDTO packDTO) throws Exception
	{
		boolean isSKUExists = false;
		//boolean isAssignedToVendor = false;
		Date escalationDate = this.getEscalationDate();
		this.setContextParameters();
		ManualCar manualCar = null;
		Attribute dropShipAtr = null;
		CarNote note = null;
        List<Car> packCarsList = new ArrayList<Car>();
		log.info("in Create CAR for PACK  Vendor style: "
				+ packDTO.getVendorStyle() + "  Vendor Number: "
				+ packDTO.getVendorNumber());

		// Add VendorStyle to map object for processing the SKU information
		VendorStyle packStyle = packDTO.getVendorStyleObj()
				.getParentVendorStyle();
		VendorStyle vs = packDTO.getVendorStyleObj();
		if (packStyle != null) {
			log.info(" pattern pack vendor style :"
					+ packStyle.getVendorStyleId() + " vendor style :"
					+ packDTO.getVendorStyleObj().getVendorStyleId());
			vs = packStyle;
		}

		Car car = new Car();
		car.setDepartment(packDTO.getDepartment());
		if (packDTO.getManualCar() != null) {
			manualCar = packDTO.getManualCar();
			car.setSourceType(sourceManualCar);
			car.setSourceId(packDTO.getManualCar().getCreatedBy());
		} else if (StringUtils.isNotBlank(packDTO.getPoNumber())) {
			car.setSourceType(sourcePOCar);
			car.setSourceId(packDTO.getPoNumber());
		}
		car.setVendorStyle(vs);
		car.setCarUserByLoggedByUserId(systemUser);
		car.setWorkFlow(defaultWorkflow);
		/* CR assign to vendor - REMOVING for PI-103  */
//		isAssignedToVendor = sendDirectToVendor(packDTO.getDepartment()
//				.getDeptId(), packDTO.getVendor().getVendorId());
//		if (isAssignedToVendor) {
//			log.info("send car directly to vendor "
//					+ packDTO.getVendor().getVendorId());
//			car.setAssignedToUserType(vendorType);
//			car.setCurrentWorkFlowStatus(withVendor);
//			car.setRejectionCount(0);
//		} else {
			car.setAssignedToUserType(buyer);
			car.setCurrentWorkFlowStatus(initiated);
//		}
		car.setEscalationDate(escalationDate);
		car.setIsUrgent(Constants.FLAG_NO);

		if (StringUtils.isNotBlank(packDTO.getExpectedShipDate())) {
			car.setExpectedShipDate(DateUtils.parseDate(packDTO
					.getExpectedShipDate(), "yyyy-MM-dd"));
			car.setDueDate(this.getDueDate());
		} else {
			car.setExpectedShipDate(this.getCompletionDate());
			car.setDueDate(this.getDueDate());
		}

		String isProductTypeReq = Constants.FLAG_NO;
		List<ProductType> productTypeList = productManager.getProductTypes(vs
				.getClassification().getClassId());
		List<ProductType> productTypeListNew = new ArrayList<ProductType>();
		for (ProductType pt : productTypeList) {
			if (!productTypeListNew.contains(pt))
				productTypeListNew.add(pt);
		}
		ProductType productType = null;
		if (productTypeListNew != null && !productTypeListNew.isEmpty()) {
			if (productTypeListNew.size() > 1) {
				isProductTypeReq = Constants.FLAG_YES;
			} else {
				productType = productTypeListNew.get(0);
			}
		}
		log.info("Total number of product Type Available :" + productTypeListNew.size());
		car.setIsProductTypeRequired(isProductTypeReq);
		car.setStatusCd(Status.ACTIVE);
		car.setContentStatus(contentInProgress);
		car.setLastWorkflowStatusUpdateDate(Calendar.getInstance().getTime());
		this.setAuditInfo(systemUser, car);
		if (productType != null) {
			// Set the VendorStyle Product
			if(packStyle!=null){
			vs.setProductType(productType);
			this.carManager.saveOrUpdate(vs);
			}
			this.resyncCarsAttributes(car, productType);
		}
		Car existingCar = carManager.getCarForStyle(vs.getVendorStyleId());
		
		if(existingCar==null  && packDTO.getParentVendorStyle()!=null && packDTO.isVendorStyleExixtsInDB() ) {
			existingCar = carManager.getCarForStyle(packDTO.getVendorStyleObj().getVendorStyleId());
			
		}
		
		//Add Note to Existing Car...
		if ( existingCar != null && note == null) {
			note = new CarNote();
			note.setNoteType(carNoteType);
			note.setIsExternallyDisplayable(CarNote.FLAG_NO);
			note.setStatusCd(Status.ACTIVE);
			note.setCar(car);
			note.setNoteText("Car ID: " + existingCar.getCarId()
					+ " exists for the same Vendor Style.  You can use the copy functionality to update the attributes from this Car.");
			this.setAuditInfo(systemUser, note);
			car.getCarNotes().add(note);
		}

		dropShipAtr = carManager.getAttributeByName(Constants.IS_DROPSHIP);

		List<SkuDTO> skuList = packDTO.getSkuInfo();
		for (SkuDTO packSkuDTO : skuList) {
			try {
				VendorSku venSku = carManager.getActiveCarSkus(packSkuDTO
						.getBelkSku());
				if (venSku != null) {
					log.info(" CAR already exists for Belk Sku: "
							+ packSkuDTO.getBelkSku());
					continue;
				}
				log.info("Creating Vendor Skus for Car: Belk Sku:"
						+ packSkuDTO.getBelkSku());
				VendorSku packSku = new VendorSku();
				packSku.setCar(car);
				packSku.setOrinNumber(Long.valueOf(packSkuDTO.getOrin()));
				packSku.setBelkSku(packSkuDTO.getBelkSku());
				packSku.setBelkUpc(packSkuDTO.getBelkSku());
				packSku.setLongSku(packSkuDTO.getVendorUpc());
				packSku.setVendorUpc(packSkuDTO.getVendorUpc());
				packSku.setStatusCd(Status.ACTIVE);
				packSku.setSetFlag(Constants.FLAG_YES);
				packSku.setRetailPrice(packSkuDTO.getRetailPrice());
				packSku.setVendorStyle(packDTO.getVendorStyleObj());
				packSku.setColorCode(packSkuDTO.getColorCode());
				packSku.setColorName(packSkuDTO.getColorName());
				packSku.setSizeCode(packSkuDTO.getSizeCode());
				packSku.setSizeName(packSkuDTO.getSizeName());
				packSku.setIdbSizeName(packSkuDTO.getSizeName());
				this.setAuditInfo(systemUser, packSku);

				if (log.isDebugEnabled()) {
					log.debug("Setting IS_DROPSHIP flag No at sku level");
				}
				CarSkuAttribute dropshipSkuAttr = new CarSkuAttribute();
				dropshipSkuAttr.setAttribute(dropShipAtr);
				dropshipSkuAttr.setAttrValue(Constants.VALUE_NO);
				this.setAuditInfo(systemUser, dropshipSkuAttr);
				dropshipSkuAttr.setVendorSku(packSku);
				packSku.getCarSkuAttributes().add(dropshipSkuAttr);
				car.getVendorSkus().add(packSku);
				isSKUExists = true;
			} catch (Exception e) {
				log.error("ERROR while creating pack sku " + e.getMessage());
			}
		}

		if (!isSKUExists && manualCar != null) {
			manualCar.setProcessStatus(completeWithError);
			manualCar.setPostProcessInfo("This PACK SKU Under this vendorstyle is already exixts.");
			this.setAuditInfo(systemUser, manualCar);
			carManager.saveOrUpdate(manualCar);
			log.error("manual car completed with error ....");
			return new Car();
		} else if (!isSKUExists) {
			log.info("This PACK SKU Under this vendorstyle is already exixts");
			return new Car();
		}

		CatalogProduct product = null;
		// Checking for product information in Catalog
		if (car.getVendorStyle().isPattern()) {
			// Find choice based on the first Product with Content within a
			// Pattern
			VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
			criteria.setVendorStyleId(car.getVendorStyle().getVendorStyleId());
			criteria.setChildrenOnly(true);
			if (log.isDebugEnabled())
				log.debug("Pattern - Search for child product information ");
			List<VendorStyle> childProducts = this.carManager
					.searchVendorStyle(criteria);
			if (childProducts != null) {
				for (VendorStyle childProduct : childProducts) {
					if (log.isDebugEnabled())
						log.debug("Pattern - Searching for product information in catalog "
										+ childProduct.getVendorStyleNumber());
					product = catalogImportManager.getProduct(childProduct
							.getVendorNumber(), childProduct
							.getVendorStyleNumber());
					if (product != null) {
						break;
					}
				}
			}

		} else {
			if (log.isDebugEnabled())
				log.debug("Product - Searching for product information in catalog "
								+ car.getVendorStyle().getVendorStyleNumber());
			log.info("Get Product Info in Catalog based on vendor Number :"+car.getVendorStyle().getVendorNumber()+
					"    vendor Style Number :"+car.getVendorStyle().getVendorStyleNumber());
			product = catalogImportManager.getProduct(car.getVendorStyle()
					.getVendorNumber(), car.getVendorStyle()
					.getVendorStyleNumber());
		}

		if (product == null) {
			// check based on sku
			CatalogSku sku = null;
			if (car != null && car.getVendorSkus() != null
					&& !car.getVendorSkus().isEmpty()) {
				sku = catalogImportManager.getSku(car.getVendorSkus()
						.iterator().next().getVendorUpc());
			}
			if (sku != null) {
				product = sku.getCatalogProduct();
			}
		}

		if (product != null && product.getCatalogProductId() != 0
				&& car.getVendorStyle().getProductType() != null) {
			catalogImportManager.copyToCar(product, car);

			// Item found in catalog.. then assing the CAR to buyer
			if (car.getCarId() == 0) {
				car.setAssignedToUserType(buyer);
			}
		}

		try {
			log.info("Creating Car for Vendor Style with ID: "
					+ car.getVendorStyle().getVendorStyleId());
			car = carManager.createCar(car);
			log.info("new CAR created with with CarID: " + car.getCarId());

			if (manualCar != null) {
				manualCar.setProcessStatus(completed);
				manualCar.setPostProcessInfo("Car Created With ID: "
						+ car.getCarId());
			}
		} catch (Exception ex) {
			if (log.isErrorEnabled())
				log.error("Save Exception. Cause: " + ex.getMessage());
			if (manualCar != null && car != null) {
				manualCar.setProcessStatus(completeWithError);
				manualCar.setPostProcessInfo("ERROR while creating PACK CAR");
			}

		}
		if (manualCar != null) {

			this.setAuditInfo(systemUser, manualCar);
			carManager.saveOrUpdate(manualCar);
			log.info("manual car Completed ....");
		}
		
		return car;
	}			

	/**
	 * 
	 * @param packList
	 * @return
	 * @throws Exception
	 * This method returns the pack details from SMART  
	 */
	public Map<String, StyleDTO> getPackDetails(List<PackDTO> packList) throws Exception
	{
		Map<String, StyleDTO> styleMap = new HashMap<String, StyleDTO>();
		List<StyleDTO> validPackList = new ArrayList<StyleDTO>();
		List<StyleDTO> packDtoList = new ArrayList<StyleDTO>();

		packDtoList = pimAttributeManager.getPackDetailsFromSMART(packList);

		if (log.isDebugEnabled())
			log.debug("Pack list Size from SMART:" + packDtoList.size());

		// if the pack list is not empty from SMART exit the method.
		if (packDtoList.isEmpty()) {
			return null;
		}
		validPackList = processValidVendorStyles(packDtoList);
		if (log.isDebugEnabled())
			log.debug("Pack list Size after processing the vendorstyle for pack:"
							+ validPackList.size());
		for (StyleDTO styleDTO : validPackList) {
			String packUPC = styleDTO.getPackUPC();
			if (null != packUPC && !"".equals(packUPC)) {
				VendorSku venSku = carManager.getActiveCarSkus(packUPC);
				if (venSku != null) {
					log.info(" PACK already exists: " + packUPC);
					continue;
				}
				styleMap.put(packUPC, styleDTO);
			}
			if (log.isDebugEnabled())
				log.debug("Eligible Pack UPC code  for creating pack :"
						+ packUPC);

		}
		return styleMap;
	}

	public void processDropShipCAR(DropShipMessage dropshipMessage) throws Exception
	{
        boolean isSuccess = false;
		boolean isValidFlag = false;
		boolean isSkuAvailable = false;
		String OriinNumber = "";
		CarsDifferentiators differentiators = null;
		List<SkuDTO> skuDtoList = new ArrayList<SkuDTO>();
		List<SkuDTO> packDtoList = new ArrayList<SkuDTO>();
		List<CarsDropshipItem> skuItemList = new ArrayList<CarsDropshipItem>();
		List<CarsDropshipPackItem> packItemList = new ArrayList<CarsDropshipPackItem>();
		List<PackDTO> packDTOlist = new ArrayList<PackDTO>();
		List<Car> dsCarList = new ArrayList<Car>();
		List<String> vStyleOrinList = new ArrayList<String>();
		List<StyleDTO> validVendorStyleList = null;
                log.info("------------------------> Begin Processing DROPSHIP Message ------------------------>");

		try {
			// Get All the style ORIN Number from DROPSHIP Message
			Map<String, StyleDTO> styledtoMap = new HashMap<String, StyleDTO>();
			if (dropshipMessage == null) {
				log.error("dropshipMessage is null.");
				return;
			}
			if (dropshipMessage.getStyleId() != null) {
				OriinNumber = dropshipMessage.getStyleId();
			}

			if (null != dropshipMessage.getItem()) {
				skuItemList = dropshipMessage.getItem();
			}

			if (null != dropshipMessage.getPackItem()) {
				packItemList = dropshipMessage.getPackItem();
				PackDTO packDto = new PackDTO();
				if (dropshipMessage.getVendorNumber() != null
						&& dropshipMessage.getVendorStyle() != null) {
					packDto.setVendorNumber(dropshipMessage.getVendorNumber());
					packDto.setVendorStyleNumber(dropshipMessage
							.getVendorStyle());
					packDTOlist.add(packDto);
				}
			}

			if (log.isDebugEnabled()) {
				log.debug("Vendor Style Orin Number from DROPSHIP Message : "
						+ OriinNumber);
			}
			// Check if the ORIN is valid. If not valid skip to next message.

			List<StyleDTO> styleDtoList = new ArrayList<StyleDTO>();
			if (StringUtils.isNotBlank(OriinNumber)) {
				// set the Vendor Style ORIN to list and call the SMART system.
				vStyleOrinList.add(String.valueOf(OriinNumber));
				// call the webservice by passing List of vendor Style Orin
				// Numbers
				styledtoMap = pimAttributeManager
						.getStylesDetailsFromSMART(vStyleOrinList);
				if (null == styledtoMap || styledtoMap.isEmpty()) {
					log.error("Vendor style dto is null or empty from SMART System.");
				}
				isValidFlag = true;
				for (StyleDTO stDto : styledtoMap.values())
					styleDtoList.add(stDto);

			} else if (!packItemList.isEmpty()) {
				styleDtoList = pimAttributeManager
						.getPackDetailsFromSMART(packDTOlist);
				isValidFlag = true;
			}

			

			if (!isValidFlag || styleDtoList.size() == 0) {
				log.error("Vendor_style_Orin/pack_List is null or empty in the dropship message.");
				return;
			}

			if ((null == skuItemList || skuItemList.isEmpty())
					&& (null == packItemList || packItemList.isEmpty())) {
				log.error("sku Item List and Pack Item List are null or empty from SMART System.");
				return;
			}
			// iterate the sku list from DROPSHIP message and add it to the
			// style dto.
			for (CarsDropshipItem dropShipItem : skuItemList) {
				
				if (null == dropShipItem.getVendorUpc() || dropShipItem.getVendorUpc() .isEmpty()) {
					log.error("Venodor UPC is null for the dropshipSku: "
							+ dropShipItem.getSku());
					continue ;
				}
				if(Constants.FALSE.equals(dropShipItem.getDropshipFlag())){
					log.info("Dropship flag set to false for this SKU "
							+ dropShipItem.getSku());
					continue ;
				}
				SkuDTO skuDTO = new SkuDTO();
				skuDTO.setOrin(dropShipItem.getSku());
				skuDTO.setBelkSku(dropShipItem.getIdbId());
				skuDTO.setVendorUpc(dropShipItem.getVendorUpc());
				skuDTO.setLongSku(dropShipItem.getVendorUpc());
				skuDTO.setRetailPrice(dropShipItem.getRetailPrice()
						.doubleValue());

				if (null == dropShipItem.getDifferentiators()) {
					log.error("no differentiators for the sku"
							+ dropShipItem.getSku());
					continue;
				}
				differentiators = dropShipItem.getDifferentiators();
				skuDTO.setColorCode(differentiators.getColorCode());
				skuDTO.setColorName(differentiators.getColorName());
				skuDTO.setSizeCode(differentiators.getSizeCode());
				skuDTO.setSizeName(differentiators.getSizeName());
				skuDtoList.add(skuDTO);
				if (log.isDebugEnabled()) {
					log.debug("SKU UPC for this vendor style : "
							+ dropShipItem.getIdbId());
				}
				isSkuAvailable = true;
			}

			// Get the CarsDropshipPackItem from dropship message.
			nextPackItem:
			for (CarsDropshipPackItem packitem : packItemList) {
				
				if (null == packitem.getPackUpc() || packitem.getPackUpc() .isEmpty()) {
					log.error("Venodor UPC is null for the dropshipPackSku: "
							+ packitem.getPack());
					continue nextPackItem;
				}
				if(Constants.FALSE.equals(packitem.getDropshipFlag())){
					log.info("Dropship flag set to false for this SKU "
							+ packitem.getPack());
					continue nextPackItem;
				}
				SkuDTO skuDTO = new SkuDTO();
				skuDTO.setOrin(packitem.getPack());
				skuDTO.setBelkSku(packitem.getIdbPack());
				skuDTO.setVendorUpc(packitem.getPackUpc());
				skuDTO.setLongSku(packitem.getPackUpc());
				skuDTO.setRetailPrice(packitem.getRetailPrice().doubleValue());
				if (null == packitem.getDifferentiators()) {
					log.error("no differentiators for the pack"
							+ packitem.getPack());
					continue nextPackItem;
				}
				differentiators = packitem.getDifferentiators();
				skuDTO.setColorCode(differentiators.getColorCode());
				skuDTO.setColorName(differentiators.getColorName());
				skuDTO.setSizeCode(differentiators.getSizeCode());
				skuDTO.setSizeName(differentiators.getSizeName());
				packDtoList.add(skuDTO);
				if (log.isDebugEnabled()) {
					log.debug("Pack UPC for this vendor style : "
							+ packitem.getIdbPack());
				}
			}

			// Call processValidVendorStyles method to validate vendorstyles
			validVendorStyleList = processValidVendorStyles(styleDtoList);
			// get the style dto and process for creating car as the dropship
			// message will be having only one vendorstyle.

			if (validVendorStyleList == null || validVendorStyleList.isEmpty()) {
				log.error("Valid vendor Style list is Empty for this Dropship car:");
				return;
			}

			StyleDTO validStyleDTO = validVendorStyleList.get(0);
			validStyleDTO.setSkuInfo(skuDtoList);

			if (log.isDebugEnabled()) {
				log.debug("Will create the CAR for vendorstyle :"
						+ validStyleDTO.getVendorStyle());
			}
			// call create car method for validating sku and creating CAR.
			dsCarList = createCARforDropshipValidVendorStyles(validStyleDTO, packDtoList);
			
	        if (log.isInfoEnabled()) {
	            log.info("------------------------> End Processing DROPSHIP Message ------------------------>");
	        }
	        
            isSuccess=true;

		} catch (Exception e) {
            if (!isSuccess) {
                log.error("Error while processing the Dropship car:" + e.getMessage());
                
                // update the vendor style orin list table when dropship msg fails to process
                updateCarsPoMessageEsbRetry(vStyleOrinList);
                processException("ERROR in  Create DropShip Car", e);
                throw e;
            }
		}

		if(log.isInfoEnabled()){
            log.info("----------------> Post Processing DROPSHIP Message (Calling new getItem webservice) Begins <-----------------");
        }
        
        if(dsCarList!=null && dsCarList.size()>0){
            if (dsCarList.size()==1 && dsCarList.get(0).getVendorStyle().isPattern()) {
                // call new processAdditionalPimAttributesForPattern() method if dropShipItems were merged to existing pattern CAR
                        List<String> skuUpcs = new ArrayList<String>();
                        for (StyleDTO s : validVendorStyleList) {
                            if (vStyleOrinList.contains(s.getOrin())) {
                                for (SkuDTO sk : s.getSkuInfo()) {
                                    skuUpcs.add(sk.getVendorUpc());
                                }
                            }
                        }
                pimAttributeManager.processAdditionalPimAttributesForPattern(dsCarList.get(0), vStyleOrinList, skuUpcs, this.systemUser);
            }
            else {
                // call existing method to populate attributes for the newly created CAR
                pimAttributeManager.processAdditionalPimAttributes(dsCarList,this.systemUser);
            }
        }
        
        // add logic for checking style_orins against GroupCre failures
        if (log.isInfoEnabled()) {
            log.info("----------------> Post DROPSHIP CAR Creation : Starting check if any grouping message failed earlier <-----------------");
        }
        
        for (String styleOrin : vStyleOrinList) {
            String groupingMessageXML = getGroupingMessageForStyleOrin(styleOrin);
            if (groupingMessageXML!=null) {
                if (log.isInfoEnabled()) {
                    log.info("Resending GroupCre that failed earlier for styleOrin: " + styleOrin);
                }
                // if groupingMessage exists, post into queue.  Then update as processed.
                this.queueSender.sendMessageXML(groupingMessageXML);
                patternAndCollectionManager.setGroupingFailureAsProcessed(styleOrin, true);
            }
        }
        
        if (log.isInfoEnabled()) {
            log.info("----------------> Post DROPSHIP CAR Creation : Finished checking if any grouping message failed earlier <-----------------");
            log.info("----------------> Post Processing DROPSHIP Message END <-----------------");
        }
	}

	/**
	 * This Method will Create Car
	 * @throws Exception 
	 */
	@Transactional
	public void processAndCreateCAR(PoMessage poMessage) throws Exception {
	    boolean isSuccess = false;
            List<String> styleOrinList = new ArrayList<String>();
		try {
		    Calendar startTime = Calendar.getInstance();
		    
			List<StyleDTO> validVendorStyleList = null;
			VendorStyle pattern = null;
			VendorStyle vendorStyle = null;
			Map<VendorStyle, List<StyleDTO>> productMap = new HashMap<VendorStyle, List<StyleDTO>>();
			
			Map<String, List<String>> skuOrinMap = new HashMap<String, List<String>>();

			List<PackDTO> packList = new ArrayList<PackDTO>();

			String poNumber = poMessage.getOrderNo().toString();
			String expectedShipDate = poMessage.getNotBeforeDate().toString();
			List<CarsPoDtl> CarsPoDtlList = poMessage.getPoDtl();
			Map<String, StyleDTO> packDtoMap = new HashMap<String, StyleDTO>();
			Map<String, StyleDTO> packMap = new HashMap<String, StyleDTO>();
			Map<String, StyleDTO> StyleMap = new HashMap<String, StyleDTO>();
			Map<String, SkuDTO> SkudtoMap = new HashMap<String, SkuDTO>();

			log.info("Creating cars for PO in processAndCreateCAR: " + poNumber);

			if (null == CarsPoDtlList || CarsPoDtlList.isEmpty()) {
				log.error("Po Message XML file is empty, nothing to process in processAndCreateCAR Method for PO Number"+poNumber);
				return;
			}

			Map<String, String> skuPackMap = new HashMap<String, String>();
			// Get All the style and sku ORIN Number from PO Message and create
			// the Map
			for (CarsPoDtl carsPoDtl : CarsPoDtlList) {
				if (null != carsPoDtl) {
					if (StringUtils.isNotBlank(carsPoDtl.getStyle())) {
				if (log.isDebugEnabled()) {
					log.debug("Vendor Style Orin Number from PO Message : "
							+ carsPoDtl.getStyle());
				}				
					styleOrinList.add(carsPoDtl.getStyle());
				}else {
						log.debug("Style Details are empty for PO Number Please check Response : "
								+ poNumber);
				}
				List<String> skuOrinlist = new ArrayList<String>();

				// Get All the SkuOrins from POMessage
				if(null != carsPoDtl.getItem()) {
				for (CarsItem carsItem : carsPoDtl.getItem()) {
					// if the vendor number is null or there is no qty ordered
					if (StringUtils.isEmpty(carsItem.getVendorUpc())) {
						log.warn("Skipping CAR creation for the SKU: " + carsItem.getSku() +
								" vendor UPC was null on the PO message for PO Number." + poNumber);
						continue; // skip this element
					}
					if (log.isDebugEnabled()) {
						log.debug("Sku Orin Number from PO message : "
								+ carsItem.getSku() + "For PO Number"+ poNumber);
					}
					skuOrinlist.add(carsItem.getSku());
					skuPackMap.put(carsItem.getSku(), carsItem.getIdbPack());
				}
				}else {
					log.debug("SKU Details are empty for PO Number Please check Response : "
							+ poNumber);
				}
				List<CarsPackItem> packItemList = carsPoDtl.getPackItem();
				if (packItemList != null && !packItemList.isEmpty()) {

					for (CarsPackItem packitem : packItemList) {
						PackDTO packDTO = new PackDTO();

						packDTO.setVendorNumber(carsPoDtl.getVendorNumber());
						packDTO
								.setVendorStyleNumber(carsPoDtl
										.getVendorStyle());
						packList.add(packDTO);
						log.info("vendor name from packlist==>"
								+ carsPoDtl.getVendorNumber() + "VPN"
								+ carsPoDtl.getVendorStyle());

					}

				}
				// Add skuOrinlist in Map obj for the Styleorin
				skuOrinMap.put(carsPoDtl.getStyle(), skuOrinlist);
					}else {
						log.debug("CarsPoDtl in createcarsmethod empty for : " + poNumber);
					}
			}

			// Call the webservices by passing List of Pack Id's
			if (!packList.isEmpty()) {
				packDtoMap = getPackDetails(packList);
			}

			// call the webservice by passing List of vendor Style Orin Numbers
			if (!styleOrinList.isEmpty()) {
				StyleMap = pimAttributeManager
						.getStylesDetailsFromSMART(styleOrinList);
			}
			if (!StyleMap.isEmpty()) {

				List<StyleDTO> styleDtoList = new ArrayList<StyleDTO>();
				for (StyleDTO stDto : StyleMap.values()) {
					styleDtoList.add(stDto);

				}

				// Call processValidVendorStyles method to validate vendorstyles
				validVendorStyleList = processValidVendorStyles(styleDtoList);

				List<String> validSkuOrinList = new ArrayList<String>();

				// Get Valid SkuOrins from Map and add it to SkuskuOrinListFinal
				for (StyleDTO styleDTO : validVendorStyleList) {
					List<String> skuOrinList = skuOrinMap.get(styleDTO
							.getOrin());
					if (skuOrinList != null && !skuOrinList.isEmpty()) {
						if (log.isDebugEnabled()) {
							log.debug("found Skus for valid vendor style : "
									+ styleDTO.getOrin());
						}
						validSkuOrinList.addAll(skuOrinList);
					}
				}
				// Call the webservice by passing all the SkuOrinList of Valid
				// vendor Styles
				if (!validSkuOrinList.isEmpty()) {
					SkudtoMap = pimAttributeManager
							.getSKUDetailsFromSMART(validSkuOrinList);
				}
				if (!SkudtoMap.isEmpty()) {

					List<StyleDTO> stlyeDTOlist = new ArrayList<StyleDTO>();
					// Get Sku details from webservice and Map it to StyleDto

					for (StyleDTO styleDTO : validVendorStyleList) {
						List<SkuDTO> skuDtolist = new ArrayList<SkuDTO>();
						List<String> skuOrinList = skuOrinMap.get(styleDTO
								.getOrin());
						if (skuOrinList != null && !skuOrinList.isEmpty()) {
							for (String skuOrin : skuOrinList) {
								SkuDTO lSkuDTO = SkudtoMap.get(skuOrin);
								lSkuDTO.setParentBelkSKU(skuPackMap.get(lSkuDTO
										.getOrin()));
								skuDtolist.add(lSkuDTO);
							}
						}
						styleDTO.setSkuInfo(skuDtolist);
						styleDTO.setPoNumber(poNumber);
						styleDTO.setExpectedShipDate(expectedShipDate);
						stlyeDTOlist.add(styleDTO);
					}

					List<StyleDTO> processvendorStyleList = null;
					Map<Long, VendorStyle> vsmap = new HashMap<Long, VendorStyle>();
					for (StyleDTO styleDTO : stlyeDTOlist) {

						vendorStyle = styleDTO.getVendorStyleObj();
						pattern = vendorStyle.getParentVendorStyle();

						if (pattern != null) {
							if (vsmap.containsKey(pattern.getVendorStyleId())) {
								processvendorStyleList = productMap.get(vsmap
										.get(pattern.getVendorStyleId()));
							} else {
								processvendorStyleList = new ArrayList<StyleDTO>();
								vsmap.put(pattern.getVendorStyleId(), pattern);
								productMap.put(pattern, processvendorStyleList);

							}
							processvendorStyleList.add(styleDTO);
							// TO-DO : do we need to add List to map atfer we
							// add the styleDTO to list
						} else {
							processvendorStyleList = new ArrayList<StyleDTO>();
							processvendorStyleList.add(styleDTO);
							productMap.put(vendorStyle, processvendorStyleList);
						}

					}
				}
			}

			for (StyleDTO packStyle : packDtoMap.values()) {

				packStyle.setPoNumber(poNumber);
				packStyle.setExpectedShipDate(expectedShipDate);
				packMap.put(packStyle.getPackUPC(), packStyle);
			}

			if (log.isDebugEnabled()) {
				log.debug("Will create the CAR for " + productMap.size()
						+ " products for PO Number"+poNumber);
				log.debug("Will create the CAR for " + packMap.size()
						+ " pack products for PO Number"+poNumber);
			}
			log.info("Will create the CAR for " + productMap.size()
					+ " products for PO Number"+poNumber);
			log.info("Will create the CAR for " + packMap.size()
					+ " pack products for PO Number"+poNumber);

            Map<String, List<Car>> carMap = createCARforValidVendorStyles(
                    productMap, packMap);// returns arraylist
            
            Calendar endTime = Calendar.getInstance();
            if (log.isInfoEnabled()) {
                long duration = endTime.getTimeInMillis() - startTime.getTimeInMillis();
                duration = duration / 1000; // convert to seconds
                log.info("Car creation took "+duration+" seconds! for PO Number"+poNumber);
            }
            log.info("---------------->  End PO CAR Creation <-----------------"); 
            if (log.isInfoEnabled()) {
                log.info("----------------> Post PO CAR Creation (new get Item web service call) Begins <-----------------");
            }
            
            Calendar postCreationStart = Calendar.getInstance();
            for (Map.Entry<String, List<Car>> mapEntry : carMap.entrySet()) {
                if (mapEntry.getKey().equalsIgnoreCase("car")) {
                    if (mapEntry.getValue()!=null && mapEntry.getValue().size()==1 && mapEntry.getValue().get(0).getVendorStyle().isPattern()) {
                        // call new processAdditionalPimAttributesForPattern() method if poItems were merged to existing pattern CAR
                        List<String> skuUpcs = new ArrayList<String>();
                        for (StyleDTO s : validVendorStyleList) {
                            if (styleOrinList.contains(s.getOrin())) {
                                for (SkuDTO sk : s.getSkuInfo()) {
                                    skuUpcs.add(sk.getVendorUpc());
                                }
                            }
                        }
                        pimAttributeManager.processAdditionalPimAttributesForPattern(mapEntry.getValue().get(0), styleOrinList, skuUpcs, this.systemUser);
                    }
                    else {
                        pimAttributeManager.processAdditionalPimAttributes(mapEntry.getValue(), this.systemUser);
                    }
                } else {
                    pimAttributeManager.processAdditionalPackPimAttributes(
                            mapEntry.getValue(), this.systemUser);
                }
            }
            Calendar postCreationEnd = Calendar.getInstance();
            if (log.isInfoEnabled()) {
                long duration = postCreationEnd.getTimeInMillis() - postCreationStart.getTimeInMillis();
                duration = duration / 1000; // convert to seconds
                log.info("POST Car Creation took "+duration+" seconds! for PO Number"+poNumber);
            }
            
            // add logic for checking style_orins against GroupCre failures
            if (log.isInfoEnabled()) {
                log.info("----------------> Post PO CAR Creation : Starting check if any grouping message failed earlier <-----------------");
            }
            
            for (String styleOrin : styleOrinList) {
                String groupingMessageXML = getGroupingMessageForStyleOrin(styleOrin);
                if (groupingMessageXML!=null) {
                    if (log.isInfoEnabled()) {
                        log.info("Resending GroupCre that failed earlier for styleOrin: " + styleOrin+"for PO Number"+poNumber);
                    }
                    // if groupingMessage exists, post into queue.  Then update as processed.
                    this.queueSender.sendMessageXML(groupingMessageXML);
                    patternAndCollectionManager.setGroupingFailureAsProcessed(styleOrin, true);
                }
            }
            
            if (log.isInfoEnabled()) {
                log.info("----------------> Post PO CAR Creation : Finished checking if any grouping message failed earlier <-----------------");
                log.info("----------------> Post PO CAR Creation END <-----------------");
            }
            
            isSuccess = true;
            
		} catch (Exception ex) {
		    if (!isSuccess) {
                        
		        log.error("Exception while creting PO car:" + ex.getMessage());
                        
                        updateCarsPoMessageEsbRetry(styleOrinList);
                        
                        try {
                            processException("ERROR in Create PO Car", ex);
                        } catch (Exception e) { 
                            log.error("Error sending email for issue in car creation: " + e.getMessage());
                        }
                        throw ex;
		    }
		}
	}
	


	/**
	 * This Method will Process Valid vendor Styles from PIM Data
	 */

	public  List<StyleDTO> processValidVendorStyles(Collection<StyleDTO> col)throws Exception
	{

		this.completeWithError = (ManualCarProcessStatus) getServletContext()
				.getAttribute(Constants.MANUAL_CAR_STATUS_COMPLETE_WITH_ERROR);
		this.systemUser = (User) getServletContext().getAttribute(
				Constants.SYSTEM_USER);
		Long orinNumber = null;
		vendorStyleTypeProduct = (VendorStyleType) getServletContext()
				.getAttribute(Constants.VENDOR_STYLE_TYPE_PRODUCT);
		List<String> errorList = new ArrayList<String>();
		List<StyleDTO> vendorStyleList = new ArrayList<StyleDTO>();
		String vendorContactEmail = "vendor@belk.com";

		// Iterating StyleDto for vendor Styles
		for (StyleDTO styleDTO : col) {
			log.info("Processing Vendor Style: " + styleDTO.getVendorStyle());
			try {
				if (styleDTO.getOrin() != null)
					orinNumber = Long.valueOf(styleDTO.getOrin());

				// get Department and classification from DB for the style
				Department dept = null;
				if (StringUtils.isNotBlank(styleDTO.getDepartmentNumber())) {
					dept = carManager.getDepartment(styleDTO
							.getDepartmentNumber());
					if (dept == null) {
						String errorText = "Department: "
								+ styleDTO.getDepartmentNumber()
								+ "not setup in the CARS database";
						log.error(errorText);
						errorList.add(errorText);
					}
				} else {
					String errorText = "Department Is Null For: "
							+ styleDTO.getVendorStyle() + ", invalid data!!";
					errorList.add(errorText);
					log.error(errorText);
				}

				styleDTO.setDepartment(dept);

				Classification classification = null;
				if (StringUtils.isNotBlank(styleDTO.getClassNumber())) {
					classification = carManager.getClassification(Short
							.parseShort(styleDTO.getClassNumber()));
					if (classification == null) {
						String errorText = " Classification"
								+ styleDTO.getClassNumber()
								+ "not setup in the database";
						log.error(errorText);
						errorList.add(errorText);
					}
				} else {
					String errorText = "Classification  Is Null For: "
							+ styleDTO.getVendorStyle() + ", invalid data!!";
					errorList.add(errorText);
					log.error(errorText);
				}

				styleDTO.setClassification(classification);

				// Check if Department and Classification are exist in DB, if
				// not skip the style
				if (!errorList.isEmpty()) {
					if (styleDTO.getManualCar() != null) {
						ManualCar manualcar = styleDTO.getManualCar();
						manualcar.setProcessStatus(completeWithError);
						manualcar.setPostProcessInfo(StringUtils.left(errorList
								.toString(), 300));
						this.setAuditInfo(systemUser, manualcar);
						// carManager.saveOrUpdate(manualcar);
						// log.info("manual car completed with error ....");
						throw new Exception(
								"Department or Classification do not exists in the database");
					} else {
						log.error(errorList.toString());
					}
					continue;

				}

				if (log.isDebugEnabled()) {
					log.debug("creating car for department # "
							+ dept.getDeptCd() + "classification # "
							+ classification.getBelkClassNumber());
				}

				Vendor vendor = carManager
						.getVendor(styleDTO.getVendorNumber());
				if (vendor != null) {
					if (styleDTO.getVendorName() != null
							&& !StringUtils.equals(styleDTO.getVendorName(),
									vendor.getName())) {
						// update vendor Information
						log.info("Vendor  already exist in DB");
						vendor.setName(styleDTO.getVendorName());
						vendor.setDescr(styleDTO.getVendorName());
						vendor.setIsDisplayable("Y");
						// create or update vendor
						carManager.createVendor(vendor);
					}
				} else {
					// create new vendor
					log.info("Vendor  already exist in DB");
					vendor = new Vendor();
					vendor.setName(styleDTO.getVendorName());
					vendor.setVendorNumber(styleDTO.getVendorNumber());
					vendor.setDescr(styleDTO.getVendorName());
					vendor.setStatusCd(Status.ACTIVE);
					vendor.setContactEmailAddr(vendorContactEmail);
					this.setAuditInfo(systemUser, vendor);
					vendor.setIsDisplayable("Y");
					vendor = carManager.createVendor(vendor);
					log.info("Vendor Created with ID: " + vendor.getVendorId());
				}
				VendorStyle vendorStyle = carManager.getVendorStyle(styleDTO
						.getVendorNumber(), styleDTO.getVendorStyle());
				VendorStyle pattern = null;

				if (vendorStyle != null) {
					log.info("Vendor Style already exist in DB");
					Long tmporin = vendorStyle.getOrinNumber();
					if(tmporin == null && orinNumber!=null)
						vendorStyle.setOrinNumber(orinNumber);
					if (styleDTO.getVendorStyleDescription() != null && 
                                            (StringUtils.isEmpty(StringUtils.trimToNull(vendorStyle.getVendorStyleName())) ||
                                             vendorStyle.getParentVendorStyle() != null)) {
						if (log.isInfoEnabled()) log.info("Updating style name + descr: " + styleDTO.getVendorStyleDescription());
                                                // update vendor style
						vendorStyle.setVendorStyleName(styleDTO.getVendorStyleDescription());
						vendorStyle.setDescr(styleDTO.getVendorStyleDescription());
					}
					styleDTO.setVendorStyleExixtsInDB(true);
					pattern = vendorStyle.getParentVendorStyle();
					styleDTO.setPattern(pattern);
				} else {
					// create new vendor Style
					vendorStyle = new VendorStyle();
					vendorStyle.setVendorNumber(styleDTO.getVendorNumber());
					vendorStyle.setVendorStyleName(styleDTO
							.getVendorStyleDescription());
					vendorStyle.setVendorStyleNumber(styleDTO.getVendorStyle());
					if (orinNumber != null)
						vendorStyle.setOrinNumber(orinNumber);
					vendorStyle.setDescr(styleDTO.getVendorStyleDescription());
					vendorStyle.setStatusCd(Status.ACTIVE);
					this.setAuditInfo(systemUser, vendorStyle);
					vendorStyle.setVendorStyleType(vendorStyleTypeProduct);
					vendorStyle.setVendor(vendor);
					vendorStyle.setClassification(classification);
					vendorStyle.setParentVendorStyle(pattern);
					styleDTO.setVendorStyleExixtsInDB(false);

				}
				
			
				List<ProductType> productTypeList = productManager.getProductTypes(
						classification.getClassId());
				List<ProductType> productTypeListNew = new ArrayList<ProductType>();
				for (ProductType pt : productTypeList) {
					if (!productTypeListNew.contains(pt))
						productTypeListNew.add(pt);
				}
				ProductType productType = null;
				if (productTypeListNew != null && !productTypeListNew.isEmpty()) {
					if (productTypeListNew.size() ==1) {
						productType = productTypeListNew.get(0);
					}
				}
				
				log.info("Total number of product Type Available :" + productTypeListNew.size());
				if (null!=productType){
				vendorStyle.setProductType(productType);
				}
				vendorStyle = pimAttributeManager.syncPIMAttributes(
						vendorStyle, styleDTO.getPimAttributeDTOList(),
						systemUser, false);
				styleDTO.setVendorStyleObj(vendorStyle);
				styleDTO.setVendor(vendorStyle.getVendor());

				vendorStyleList.add(styleDTO);

			} catch (Exception ex) {
				log.error("Error while Process Vendor Styles", ex);
				throw ex;
			}

		}

		return vendorStyleList;

	}
	
    /**
     * Process PIM Attributes that are updated AFTER car creation.
     * @throws IOException 
     */
    @Override
    public void processPIMAttributeUpdates() {
        boolean lockFileCreated = false;
        try {
            if (log.isInfoEnabled()) {
                log.info("Starting processPIMAttributeUpdates!");
            }
            
            lockFileCreated = createLockFile(Config.PIM_UPDATE_LOCK_PATH);
            if (!lockFileCreated) {
                if (log.isErrorEnabled()) {
                    log.error("Exiting.  Another process is already running!");
                }
                sendFailureNotification("Process PIM Attribute Updates", "Job was not executed on schedule due to the presence of a lockfile.");
                return;
            }
            
            boolean thresholdReached = isMaxThresholdReachedToday();
            if (thresholdReached) {
                if (log.isInfoEnabled()) {
                    log.info("Max Threshold is reached for update items to process today.");
                }
                throw new Exception("MAX_THRESHOLD_IS_REACHED");
            }
            
            int maxThreads = getMaxThreads();
            int maxRecordsToProcess = getMaxRecordsToProcessPerRun(); //-1 means no max
            int batchSize = getBatchSizePerThread(maxThreads, maxRecordsToProcess);
            int totalRecordsProcessed = 0;
            
            this.systemUser = (User) getServletContext().getAttribute(
                    Constants.SYSTEM_USER);
            
            Calendar startTime = Calendar.getInstance();
            ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
            /** Update attributes & images from PIM for update records, then mark as processed. **/
            List<CarsPimItemUpdate> failedItems = new ArrayList<CarsPimItemUpdate>();
            List<CarsPimItemUpdate> itemsUpdated = pimAttributeManager.getAllUpdatedItemsFromPim(batchSize);
            while (!itemsUpdated.isEmpty()) {
                if (maxRecordsToProcess>0 && totalRecordsProcessed>=maxRecordsToProcess) {
                    break; //exit loop.  Don't process anymore updates.
                }
                totalRecordsProcessed += itemsUpdated.size();
                pimAttributeManager.markAllPimItemUpdatesAsProcessedByCar(itemsUpdated);
                Runnable UpdateAttributesFromPIMThread = new UpdateAttributesFromPIMThread(this.carManager,
                        this.carLookupManager, this.pimAttributeManager, this.pimFtpImageManager, this.systemUser,
                        itemsUpdated, failedItems);
                executor.execute(UpdateAttributesFromPIMThread);
                itemsUpdated = pimAttributeManager.getAllUpdatedItemsFromPim(batchSize);
            }
            
            executor.shutdown();
            while (!executor.isTerminated()) {
                //wait for all threads to complete
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    // do nothing
                }
            }
            //Reset processedByCar flag if item failed to sync from PIM.
            if (failedItems != null && !failedItems.isEmpty()) {
                if (log.isInfoEnabled()) {
                    log.info("Failed to process " + failedItems.size() + " items.  Marking as unprocessed.");
                }
                pimAttributeManager.markAllPimItemUpdatesAsFailed(failedItems);
            }
            
            Calendar endTime = Calendar.getInstance();
            if (log.isInfoEnabled()) {
                long duration = endTime.getTimeInMillis() - startTime.getTimeInMillis();
                duration = duration / 1000; // convert to seconds
                log.info("processPIMAttributeUpdates finished processing "+(totalRecordsProcessed-failedItems.size()) 
                        +" out of "+totalRecordsProcessed+" records successfully in "+duration+" seconds!");
            }
        }
        catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Exception occurred while processing updated PIM attributes",e);
            }
            processException("Error while processing updated PIM Attributes", e);
        }
        finally {
            if (lockFileCreated) {
                deleteLockFile(Config.PIM_UPDATE_LOCK_PATH); // only delete lock file if was created by current run.
            }
        }
    }
    
    /**
     * This method creates the LockFile if it doesn't exist.
     * 
     * @return true if LockFile was successfully created.
     * @throws IOException 
     */
    private boolean createLockFile(String configName) throws IOException {
        if (log.isInfoEnabled()) {
            log.info("Creating LockFile: " + configName);
        }
        Config lockfilePathConfig = (Config) carLookupManager.getById(Config.class, configName);
        if (lockfilePathConfig == null) {
            if (log.isErrorEnabled()) {
                log.error("Unable to create LockFile due to undefined param "+configName+" in CONFIG table!");
            }
            return false; // no path to create lockfile
        }
        String lockfilePath = lockfilePathConfig.getValue();
        File lockfile = new File(lockfilePath);
        return lockfile.createNewFile();
    }
    
    /**
     * This method deletes the LockFile if it exists.  
     * 
     * @return
     */
    private boolean deleteLockFile(String configName) {
        if (log.isInfoEnabled()) {
            log.info("Deleting LockFile: " + configName);
        }
        Config lockfilePathConfig = (Config) carLookupManager.getById(Config.class, configName);
        if (lockfilePathConfig == null) {
            if (log.isErrorEnabled()) {
                log.error("Unable to delete LockFile due to undefined param "+configName+" in CONFIG table!");
            }
            return false; // no path to delete lockfile
        }
        String lockfilePath = lockfilePathConfig.getValue();
        File lockfile = new File(lockfilePath);
        return lockfile.delete();
    }
    
    /**
     * This method get maxThread from config table.
     * 
     * @return
     */
    private int getMaxThreads() {
        Config config = (Config) carLookupManager.getById(Config.class, Config.PIM_UPDATE_MAX_THREADS);
        int maxThreads = 5; //default
        try {
            if (config != null) {
                maxThreads = Integer.parseInt(config.getValue());
            }
        }
        catch (NumberFormatException nfe) {
            if (log.isWarnEnabled()) {
                log.warn("PIM_UPDATE_MAX_THREADS config is invalid.");
            }
        }
        return maxThreads;
    }
    
    /**
     * This method get maxRecords from config table.
     * 
     * @return
     */
    private int getMaxRecordsToProcessPerRun() {
        Config config = (Config) carLookupManager.getById(Config.class, Config.PIM_UPDATE_MAX_RECORDS);
        int maxRecordsToProcess = -1; //default
        try {
            if (config != null) {
                maxRecordsToProcess = Integer.parseInt(config.getValue());
            }
        }
        catch (NumberFormatException nfe) {
            if (log.isWarnEnabled()) {
                log.warn("PIM_UPDATE_MAX_RECORDS config is invalid.");
            }
        }
        return maxRecordsToProcess;
    }
    
    /**
     * This method calculates the batchSize each update item
     * thread should process based on MaxRecords & ThreadCount.
     * 
     * @param threadCount
     * @param maxRecords
     * @return
     */
    private int getBatchSizePerThread(int maxThreads, int maxRecordsToProcess) {
        Config config = (Config) carLookupManager.getById(Config.class, Config.PIM_UPDATE_BATCH_SIZE);
        int defaultBatchSize = 1000; //default
        try {
            if (config != null) {
                defaultBatchSize = Integer.parseInt(config.getValue());
            }
        }
        catch (NumberFormatException nfe) {
            if (log.isWarnEnabled()) {
                log.warn("PIM_UPDATE_BATCH_SIZE config is invalid.");
            }
        }
        if (maxRecordsToProcess > 0) {
            int batchSize = maxRecordsToProcess / maxThreads;
            if (maxRecordsToProcess % maxThreads > 0) {
                batchSize += 1;
            }
            if (batchSize < defaultBatchSize) {
                return batchSize;
            }
        }
        return defaultBatchSize;
    }
    
    private boolean isMaxThresholdReachedToday() {
        int dailyMaxThreshold = getDailyMaxThreshold();
        int count = pimAttributeManager.getProcessedPimItemUpdateCount();
        
        if (count >= dailyMaxThreshold) {
            return true;
        }
        return false;
    }
    
    /**
     * This method get maxThread from config table.
     * 
     * @return
     */
    private int getDailyMaxThreshold() {
        Config config = (Config) carLookupManager.getById(Config.class, Config.PIM_UPDATE_DAILY_MAX_THRESHOLD);
        int dailyMaxThreshold = 10000; //default
        try {
            if (config != null) {
                dailyMaxThreshold = Integer.parseInt(config.getValue());
            }
        }
        catch (NumberFormatException nfe) {
            if (log.isWarnEnabled()) {
                log.warn("PIM_UPDATE_DAILY_MAX_THRESHOLD config is invalid.");
            }
        }
        return dailyMaxThreshold;
    }

	
	/**
	 * Method used to get all context parameter.
	 */
	private void setContextParameters() {
		this.systemUser = (User) getServletContext().getAttribute(
				Constants.SYSTEM_USER);
		this.initiated = (WorkflowStatus) getServletContext().getAttribute(
				Constants.INITIATED);
		this.withVendor = (WorkflowStatus) getServletContext().getAttribute(
				Constants.WITH_VENDOR);
		this.buyer = (UserType) getServletContext().getAttribute(
				Constants.BUYER);
		this.vendorType = (UserType) getServletContext().getAttribute(
				Constants.VENDOR_TYPE);
		
		this.checkRequired = (AttributeValueProcessStatus) getServletContext()
				.getAttribute(Constants.CHECK_REQUIRED);
		this.noCheckRequired = (AttributeValueProcessStatus) getServletContext()
				.getAttribute(Constants.NO_CHECK_REQUIRED);
		this.carNoteType = (NoteType) getServletContext().getAttribute(
				Constants.CAR_NOTE_TYPE);
		this.defaultWorkflow = (WorkFlow) getServletContext().getAttribute(
				Constants.DEFAULT_WORKFLOW);
		this.contentInProgress = (ContentStatus) getServletContext()
				.getAttribute(Constants.CONTENT_IN_PROGRESS);
		this.sourcePOCar = (SourceType) getServletContext().getAttribute(
				Constants.SOURCE_PO_CAR);
		this.sourceManualCar = (SourceType) getServletContext().getAttribute(
				Constants.SOURCE_MANUAL_CAR);
		this.completed = (ManualCarProcessStatus) getServletContext()
				.getAttribute(Constants.MANUAL_CAR_STATUS_COMPLETED);
		this.sourceDropshipCar = (SourceType) getServletContext().getAttribute(
				Constants.SOURCE_DROPSHIP_CAR);
	}	

	/**
	 *  Method to create cars for valid vendor styles. The signature of this method
	 *  is changed to return a map of regular cars and pack cars.
	 * @param productMap
	 * @param packDtoMap
	 * @return 
	 * @throws Exception
	 * This method create CAR based on vendor style ,pack details etc
	 * 
	 */
	
	public Map<String, List<Car>> createCARforValidVendorStyles(
			Map<VendorStyle, List<StyleDTO>> productMap,
			Map<String, StyleDTO> packDtoMap) throws Exception {

		//boolean isAssignedToVendor = false;
		Date escalationDate = this.getEscalationDate();
		Long orinNumber;
		Attribute dropShipAtr = null;

		setContextParameters();

		log.info("Going to Process Cars: # of Vendor Styles to process - "
				+ productMap.size());
		log.info("Going to Process Cars: # of Pack Vendor Styles to process - "
				+ packDtoMap.size());
		ManualCar manualcar = null;
		List<Car> packCarsList = new ArrayList<Car>();
		List<Car> carsList = new ArrayList<Car>();
		Map<String,List<Car>> carMap = new HashMap<String,List<Car>>();
		Set<String> newPackUPCset = new HashSet<String>();
		for (VendorStyle vs : productMap.keySet()) {
			List<PoUnitDetail> poUnitDetailList = new ArrayList<PoUnitDetail>();

			if (log.isDebugEnabled()) {
				log.debug("Process Cars for Vendor Style: "
						+ vs.getVendorStyleNumber());
			}
			List<StyleDTO> validStylePIMDataList = productMap.get(vs);
			Car car = null;
                        boolean createCar = true;
                        if (vs.isPattern() ||  (vs.getParentVendorStyle() != null )) {
                            // if this vendor style is a child of an existing pattern,
                            // merge skus into the pattern parent as opposed to creating a new car.
                            if (log.isInfoEnabled()) log.info("PO for existing style with a pattern came in. Attempting to merge.");
                            
                            VendorStyle parent;
                            if (vs.getParentVendorStyle() != null) {
                                parent = vs.getParentVendorStyle();
                            } else {
                                parent = vs;
                            }
                            car = patternAndCollectionManager.getMergeEilgibleCar(parent, null);
                            if (car != null) {
                                car = carManager.getCarAndAttributes(car.getCarId());
                            }
                            
                            // skus dont exist at this point, they are added below.
                            // if we initialize car to the parent, skus should be just added to that.
                            if (car != null) {
                                if (log.isInfoEnabled()) log.info("Found parent pattern car to merge skus into: " + car.getCarId());
                                car = carManager.getCarAndAttributes(car.getCarId());
                                createCar = false;
                            } else {
                                if (log.isInfoEnabled()) log.info("No parent pattern car found.");
                            }
                        }
			CarNote note = null;
			boolean isSKUExists = false;

			for (StyleDTO pimCarTo : validStylePIMDataList) {
				if (car == null) {
					car = new Car();
					car.setDepartment(pimCarTo.getDepartment());
					if (pimCarTo.getManualCar() != null) {
						manualcar = pimCarTo.getManualCar();
						car.setSourceType(sourceManualCar);
						car.setSourceId(pimCarTo.getManualCar().getCreatedBy());
					} else if (StringUtils.isNotBlank(pimCarTo.getPoNumber())) {
						car.setSourceType(sourcePOCar);
						car.setSourceId(pimCarTo.getPoNumber());

					}
					car.setVendorStyle(vs);
					car.setCarUserByLoggedByUserId(systemUser);
					car.setWorkFlow(defaultWorkflow);
					/* CR assign to vendor - REMOVING FOR PI-103 */
//					isAssignedToVendor = sendDirectToVendor(pimCarTo
//							.getDepartment().getDeptId(), pimCarTo.getVendor()
//							.getVendorId());
//
//					if (isAssignedToVendor) {
//						log.info("send car directly to vendor "
//								+ pimCarTo.getVendor().getVendorId());
//						car.setAssignedToUserType(vendorType);
//						car.setCurrentWorkFlowStatus(withVendor);
//						car.setRejectionCount(0);
//					} else {
						car.setAssignedToUserType(buyer);
						car.setCurrentWorkFlowStatus(initiated);
//					}
					car.setEscalationDate(escalationDate);
					car.setIsUrgent(Constants.FLAG_NO);

					if (StringUtils.isNotBlank(pimCarTo.getExpectedShipDate())) {
						car.setExpectedShipDate(DateUtils.parseDate(pimCarTo
								.getExpectedShipDate(), "yyyy-MM-dd"));
						car.setDueDate(this.getDueDate());
					} else {
						car.setExpectedShipDate(this.getCompletionDate());
						car.setDueDate(this.getDueDate());
					}

					String isProductTypeReq = Constants.FLAG_NO;
					List<ProductType> productTypeList = productManager.getProductTypes(vs
							.getClassification().getClassId());
					List<ProductType> productTypeListNew = new ArrayList<ProductType>();
					for (ProductType pt : productTypeList) {
						if (!productTypeListNew.contains(pt))
							productTypeListNew.add(pt);
					}
					ProductType productType = null;
					if (productTypeListNew != null && !productTypeListNew.isEmpty()) {
						if (productTypeListNew.size() > 1) {
							isProductTypeReq = Constants.FLAG_YES;
						} else {
							productType = productTypeListNew.get(0);
						}
					}
					log.info("Total number of product Type Available :"
							+ productTypeListNew.size());
					car.setIsProductTypeRequired(isProductTypeReq);
					car.setStatusCd(Status.ACTIVE);

					car.setContentStatus(contentInProgress);
					car.setLastWorkflowStatusUpdateDate(Calendar.getInstance()
							.getTime());
					this.setAuditInfo(systemUser, car);

					if (productType != null) {
						// Set the VendorStyle Product
						if(pimCarTo.getParentVendorStyle()!=null){
						vs.setProductType(productType);
						this.carManager.saveOrUpdate(vs);
						}
						this.resyncCarsAttributes(car, productType);
					}
				}
				
				Car existingCar = carManager.getCarForStyle(vs.getVendorStyleId());
				
				if(existingCar==null  && pimCarTo.getParentVendorStyle()!=null && pimCarTo.isVendorStyleExixtsInDB() ) {
					existingCar = carManager.getCarForStyle(pimCarTo.getVendorStyleObj().getVendorStyleId());
					
				}
				
				//Add Note to Existing Car...
				if ( createCar && existingCar != null && note == null) {
					note = new CarNote();
					note.setNoteType(carNoteType);
					note.setIsExternallyDisplayable(CarNote.FLAG_NO);
					note.setStatusCd(Status.ACTIVE);
					note.setCar(car);
					note.setNoteText("Car ID: " + existingCar.getCarId()
							+ " exists for the same Vendor Style.  You can use the copy functionality to update the attributes from this Car.");
					this.setAuditInfo(systemUser, note);
					car.getCarNotes().add(note);
				}
				dropShipAtr = carManager.getAttributeByName(Constants.IS_DROPSHIP);

				for (SkuDTO pimCarSkuTo : pimCarTo.getSkuInfo()) {
					try {
						VendorSku venSku = carManager
								.getActiveCarSkus(pimCarSkuTo.getBelkSku());
						if (venSku != null) {
							log.info(" CAR already exists for Belk Sku: "
									+ pimCarSkuTo.getBelkSku());
							continue;
						}
						log.info("Creating Vendor Skus for Car: Belk Sku:"
								+ pimCarSkuTo.getBelkSku());
						orinNumber = Long.valueOf(pimCarSkuTo.getOrin());
						VendorSku sku = new VendorSku();
						sku.setCar(car);
						sku.setBelkSku(pimCarSkuTo.getBelkSku());
						sku.setBelkUpc(pimCarSkuTo.getBelkSku());
						sku.setLongSku(pimCarSkuTo.getVendorUpc());
						sku.setVendorUpc(pimCarSkuTo.getVendorUpc());
						sku.setStatusCd(Status.ACTIVE);
						sku.setColorCode(pimCarSkuTo.getColorCode());
						sku.setColorName(pimCarSkuTo.getColorName());
						sku.setSizeCode(pimCarSkuTo.getSizeCode());
						sku.setSizeName(pimCarSkuTo.getSizeName());
						sku.setOrinNumber(orinNumber);						
						// sku.setPimColorNameUpdatedDate(new Date());						
						// sku.setPimSizeNameUpdatedDate(new Date());
						// CARS Size Conversion Issue - Size name overwritten by
						// resync size job
						sku.setIdbSizeName(pimCarSkuTo.getSizeName()); 
						sku.setParentUpc(pimCarSkuTo.getParentBelkSKU());

						// Added the retail price to sku
						sku.setRetailPrice(pimCarSkuTo.getRetailPrice());
						sku.setVendorStyle(pimCarTo.getVendorStyleObj());
						this.setAuditInfo(systemUser, sku);
						// Setting is_dropship attribute as N for all the PO
						if (log.isDebugEnabled()) {
							log.debug("Setting IS_DROPSHIP flag No at sku level");
						}
						CarSkuAttribute dropshipSkuAttr = new CarSkuAttribute();
						dropshipSkuAttr.setAttribute(dropShipAtr);
						dropshipSkuAttr.setAttrValue(Constants.VALUE_NO);
						this.setAuditInfo(systemUser, dropshipSkuAttr);
						dropshipSkuAttr.setVendorSku(sku);
						sku.getCarSkuAttributes().add(dropshipSkuAttr);

						car.getVendorSkus().add(sku);
						if (pimCarTo.getPoNumber() != null) {
							    List<PoUnitDetail> podetailList=pimAttributeManager.
							    	getSkuExistInPOUnitDetails(pimCarSkuTo.getBelkSku());
								if(podetailList.size() > 0){
									log.info("SKU  "+pimCarSkuTo.getBelkSku()+" is already exists in the PO Unit Details for PO#"+pimCarTo.getPoNumber());
								}else{
									PoUnitDetail poUnitDetail = new PoUnitDetail();
									poUnitDetail.setBelkSku(pimCarSkuTo.getBelkSku());
									poUnitDetail.setPoNumber(pimCarTo.getPoNumber());
									poUnitDetail.setPoUnitCost(pimCarSkuTo
											.getUnitCost());
									poUnitDetail.setPoUnitRetail(pimCarSkuTo
											.getRetailPrice());
									poUnitDetail.setOrderQty(pimCarSkuTo
											.getOrdQuantity());
									poUnitDetailList.add(poUnitDetail);
								}
						}
						isSKUExists = true;
					} catch (Exception ex) {
						ex.getMessage();
						log.error("Error while Creating Vendor Skus for Car "+ ex);

					}
				}

				for (StyleDTO packStyleDto : packDtoMap.values()) {
					log.info("pack vendor style :"
							+ packStyleDto.getVendorStyle() + " vendor style :"
							+ pimCarTo.getVendorStyle());

					if (!packStyleDto.getVendorStyle().equals(
							pimCarTo.getVendorStyle())) {
						continue;
					}
					for (SkuDTO packDto : packStyleDto.getSkuInfo()) {
						try {
							VendorSku venSku = carManager
									.getActiveCarSkus(packDto.getBelkSku());
							if (venSku != null) {
								log.info(" CAR already exists for pack Belk Sku: "
												+ packDto.getBelkSku());
								continue;
							}
							log.info("Creating Vendor pack Sku for Car: Belk UPC:"
											+ packDto.getBelkSku());
							VendorSku packSku = new VendorSku();
							packSku.setCar(car);
							packSku.setOrinNumber(Long.valueOf(packDto
									.getOrin()));
							packSku.setBelkSku(packDto.getBelkSku());
							packSku.setBelkUpc(packDto.getBelkSku());
							packSku.setLongSku(packDto.getVendorUpc());
							packSku.setVendorUpc(packDto.getVendorUpc());
							packSku.setStatusCd(Status.ACTIVE);
							packSku.setSetFlag(Constants.FLAG_YES);
							packSku.setRetailPrice(packDto.getRetailPrice());
							packSku.setVendorStyle(packStyleDto
									.getVendorStyleObj());
							packSku.setColorCode(packDto.getColorCode());
							packSku.setColorName(packDto.getColorName());
							packSku.setSizeCode(packDto.getSizeCode());
							packSku.setSizeName(packDto.getSizeName());
							packSku.setIdbSizeName(packDto.getSizeName());
							// if online only attribute is not set at product
							// level
							// set at sku level
							this.setAuditInfo(systemUser, packSku);
							// Setting is_dropship attribute as N for all the PO
							// skus
							if (log.isDebugEnabled()) {
								log
										.debug("Setting IS_DROPSHIP flag No at sku level");
							}
							CarSkuAttribute dropshipSkuAttr = new CarSkuAttribute();
							dropshipSkuAttr.setAttribute(dropShipAtr);
							dropshipSkuAttr.setAttrValue("No");
							this.setAuditInfo(systemUser, dropshipSkuAttr);
							dropshipSkuAttr.setVendorSku(packSku);
							packSku.getCarSkuAttributes().add(dropshipSkuAttr);
							car.getVendorSkus().add(packSku);
							newPackUPCset.add(packDto.getVendorUpc());
							isSKUExists = true;
						} catch (Exception ex) {
							log.error("Error while Creating Vendor Pack Sku for Car"
											+ ex.getMessage());
						}
					}

				}
			}

			for (StyleDTO packStyleDto : packDtoMap.values()) {
				// log.info("pack vendor style :"
				// +packStyleDto.getVendorStyle()+" vendor style :"+pimCarTo.getVendorStyle());

				VendorStyle packStyle = packStyleDto.getVendorStyleObj()
						.getParentVendorStyle();

				if (packStyle != null) {
					log.info(" pattern pack vendor style :"
							+ packStyle.getVendorStyleId() + " vendor style :"
							+ vs.getVendorStyleId());
					if (packStyle.getVendorStyleId() != vs.getVendorStyleId()) {
						continue;
					}
					for (SkuDTO packDto : packStyleDto.getSkuInfo()) {
						try {
							VendorSku venSku = carManager
									.getActiveCarSkus(packDto.getBelkSku());
							if (venSku != null) {
								log.error(" CAR already exists for Belk Sku: "
												+ packDto.getBelkSku());
								continue;
							}
							log.info("Creating Vendor pack Sku for Car: Belk UPC:"
											+ packDto.getBelkSku());
							VendorSku packSku = new VendorSku();
							packSku.setCar(car);
							packSku.setOrinNumber(Long.valueOf(packDto
									.getOrin()));
							packSku.setBelkSku(packDto.getBelkSku());
							packSku.setBelkUpc(packDto.getBelkSku());
							packSku.setLongSku(packDto.getVendorUpc());
							packSku.setVendorUpc(packDto.getVendorUpc());
							packSku.setStatusCd(Status.ACTIVE);
							packSku.setSetFlag(Constants.FLAG_YES);
							packSku.setRetailPrice(packDto.getRetailPrice());
							packSku.setVendorStyle(packStyleDto
									.getVendorStyleObj());
							packSku.setColorCode(packDto.getColorCode());
							packSku.setColorName(packDto.getColorName());
							packSku.setSizeCode(packDto.getSizeCode());
							packSku.setSizeName(packDto.getSizeName());
							packSku.setIdbSizeName(packDto.getSizeName());
							// if online only attribute is not set at product
							// level
							// set at sku level
							this.setAuditInfo(systemUser, packSku);
							// Setting is_dropship attribute as N for all the PO
							// skus
							if (log.isDebugEnabled()) {
								log.debug("Setting IS_DROPSHIP flag No at sku level");
							}
							CarSkuAttribute dropshipSkuAttr = new CarSkuAttribute();
							dropshipSkuAttr.setAttribute(dropShipAtr);
							dropshipSkuAttr.setAttrValue(Constants.VALUE_NO);
							this.setAuditInfo(systemUser, dropshipSkuAttr);
							dropshipSkuAttr.setVendorSku(packSku);
							packSku.getCarSkuAttributes().add(dropshipSkuAttr);
							car.getVendorSkus().add(packSku);
							newPackUPCset.add(packDto.getVendorUpc());
							isSKUExists = true;
						} catch (Exception ex) {
							log.error("Error while Creating Vendor Pack Sku for Car"
											+ ex.getMessage());
						}
					}
				}
			}
			if (!isSKUExists && manualcar != null) {
				manualcar.setProcessStatus(completeWithError);
				manualcar.setPostProcessInfo("All the SKU's Under this vendorstyle is already exixts.");
				this.setAuditInfo(systemUser, manualcar);
				carManager.saveOrUpdate(manualcar);
				log.info("manual car completed with error ....");
				log.info("All the SKU's Under this vendorstyle is already exixts");
				continue;
			} else if (!isSKUExists) {
				log.info("All the Skus in the Vendorstyle is already exist");
				continue;
			}

			CatalogProduct product = null;
			// Checking for product information in Catalog
			if (car.getVendorStyle().isPattern()) {
				// Find choice based on the first Product with Content within a
				// Pattern
				VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
				criteria.setVendorStyleId(car.getVendorStyle()
						.getVendorStyleId());
				criteria.setChildrenOnly(true);
				if (log.isDebugEnabled())
					log.debug("Pattern - Search for child product information ");
				
				List<VendorStyle> childProducts = this.carManager
						.searchVendorStyle(criteria);
				if (childProducts != null) {
					for (VendorStyle childProduct : childProducts) {
						if (log.isDebugEnabled())
							log.debug("Pattern - Searching for product information in catalog "
											+ childProduct.getVendorStyleNumber());
						
						product = catalogImportManager.getProduct(childProduct
								.getVendorNumber(), childProduct
								.getVendorStyleNumber());
						if (product != null) {
							break;
						}
					}
				}

			} else {
				if (log.isDebugEnabled())
					log.debug("Product - Searching for product information in catalog "
								+ car.getVendorStyle().getVendorStyleNumber());
				product = catalogImportManager.getProduct(car.getVendorStyle()
						.getVendorNumber(), car.getVendorStyle()
						.getVendorStyleNumber());

			}

			if (product == null) {
				// check based on sku
				CatalogSku sku = null;
				if (car != null && car.getVendorSkus() != null
						&& !car.getVendorSkus().isEmpty()) {
					sku = catalogImportManager.getSku(car.getVendorSkus()
							.iterator().next().getVendorUpc());
				}
				if (sku != null) {
					product = sku.getCatalogProduct();
				}
			}
			if (product != null && product.getCatalogProductId() != 0
					&& car.getVendorStyle().getProductType() != null) {
				catalogImportManager.copyToCar(product, car);

				// Item found in catalog.. then assing the CAR to buyer
				if (car.getCarId() == 0) {
					car.setAssignedToUserType(buyer);
				}
			}
                        if (createCar) {
			try {
				log.info("Creating Car for Vendor Style with ID: "
						+ car.getVendorStyle().getVendorStyleId());
				car = carManager.createCar(car);
				if(car!=null && !(Constants.FLAG_YES.equalsIgnoreCase(car.getIsProductTypeRequired()))){
					carsList.add(car);
				}
				log.info("new CAR created with with CarID: " + car.getCarId());
				if (manualcar != null) {
					manualcar.setProcessStatus(completed);
					manualcar.setPostProcessInfo("Car Created With ID: "
							+ car.getCarId());
				}
			} catch (Exception ex) {
				if (log.isErrorEnabled())
					log.error("Save Exception. Cause: " + ex.getMessage());
				if (manualcar != null && car != null) {
					manualcar.setProcessStatus(completeWithError);
					manualcar.setPostProcessInfo("ERROR while creating CAR");
				}
			}
                        } else {
                            carsList.add(car);
                            carManager.saveOrUpdate(car);
                        }
			try {
				if (poUnitDetailList.size() > 0) {
					log.info("POUnit details saved for CarID: "
							+ car.getCarId() );
					carManager.savePOUnitdetails(poUnitDetailList);
				}
			} catch (Exception ex) {
				log.error("Save PO Exception. Cause: " + ex.getMessage());
			}
			if (manualcar != null && car != null) {
				this.setAuditInfo(systemUser, manualcar);
				carManager.saveOrUpdate(manualcar);
				log.info("manual car Completed ....");
			}
		}
		try {
			Set<String> allPackUPCset = packDtoMap.keySet();
			allPackUPCset.removeAll(newPackUPCset);
			for (String newpackUPC : allPackUPCset) {
				StyleDTO packDTO = packDtoMap.get(newpackUPC);
				Car packCar = createCarForPack(packDTO);
				//populating list of pack cars created which has only one product type associated
				if(packCar!=null && !(Constants.FLAG_YES.equalsIgnoreCase(packCar.getIsProductTypeRequired()))){
					packCarsList.add(packCar);
				}
			}
		} catch (Exception e) {
			log.error("Save  Exception. Cause in PACK creation: "
					+ e.getMessage());
		}
		carMap.put("car", carsList);
		carMap.put("packCars", packCarsList);
		return carMap;
	}

	/*
	 * This method used to resync Car Attributes
	 * 
	 */
	public void resyncCarsAttributes(Car car, ProductType productType) {

		
		List<DepartmentAttribute> departmentAttributes = carManager
				.getAllDepartmentAttributes(car.getDepartment().getDeptId());
		List<ClassAttribute> classificationAttributes = carManager
				.getAllClassificationAttributes(car.getVendorStyle()
						.getClassification().getClassId());
	
		Map<String, Attribute> attributeMap = new HashMap<String, Attribute>();
		Map<String, String> attributeDefaultValueMap = new HashMap<String, String>();
		for (DepartmentAttribute deptAttr : departmentAttributes) {
			if (deptAttr.getAttribute().isActive()) {
				attributeMap.put(deptAttr.getAttribute()
						.getBlueMartiniAttribute(), deptAttr.getAttribute());
				attributeDefaultValueMap.put(deptAttr.getAttribute()
						.getBlueMartiniAttribute(), StringUtils
						.defaultString(deptAttr.getDefaultAttrValue()));
			}
		}

		for (ProductTypeAttribute ptAttr : productType
				.getProductTypeAttributes()) {
			if (ptAttr.getAttribute().isActive()) {
				if (attributeMap.containsKey(ptAttr.getAttribute()
						.getBlueMartiniAttribute())) {
					attributeMap.remove(ptAttr.getAttribute()
							.getBlueMartiniAttribute());
					attributeDefaultValueMap.remove(ptAttr.getAttribute()
							.getBlueMartiniAttribute());
				}
				attributeMap.put(ptAttr.getAttribute()
						.getBlueMartiniAttribute(), ptAttr.getAttribute());
				attributeDefaultValueMap.put(ptAttr.getAttribute()
						.getBlueMartiniAttribute(), StringUtils
						.defaultString(ptAttr.getDefaultAttrValue()));
			}
		}

		for (ClassAttribute classAttr : classificationAttributes) {
			if (classAttr.getAttribute().isActive()) {
				if (attributeMap.containsKey(classAttr.getAttribute()
						.getBlueMartiniAttribute())) {
					attributeMap.remove(classAttr.getAttribute()
							.getBlueMartiniAttribute());
					attributeDefaultValueMap.remove(classAttr.getAttribute()
							.getBlueMartiniAttribute());
				}
				attributeMap.put(classAttr.getAttribute()
						.getBlueMartiniAttribute(), classAttr.getAttribute());
				attributeDefaultValueMap.put(classAttr.getAttribute()
						.getBlueMartiniAttribute(), StringUtils
						.defaultString(classAttr.getDefaultAttrValue()));
			}
		}

		for (Attribute attribute : attributeMap.values()) {
			CarAttribute carAttribute = new CarAttribute();
			carAttribute.setAttribute(attribute);
			carAttribute.setCar(car);

			if (attribute.getAttributeConfig().getHtmlDisplayType()
					.isAutocomplete()) {
				carAttribute.setAttributeValueProcessStatus(checkRequired);
			} else {
				carAttribute.setAttributeValueProcessStatus(noCheckRequired);
			}
			// Setting to blank for now. Need to get it from the association
			carAttribute.setAttrValue(attributeDefaultValueMap.get(attribute
					.getBlueMartiniAttribute()));
			carAttribute.setDisplaySeq((short) 0);
			carAttribute.setHasChanged(Constants.FLAG_NO);
			carAttribute.setIsChangeRequired(Constants.FLAG_YES);
			carAttribute.setStatusCd(Status.ACTIVE);
			this.setAuditInfo(systemUser, carAttribute);
			car.getCarAttributes().add(carAttribute);
			if(log.isDebugEnabled())
			log.debug("New Attribute " + attribute.getAttributeId()
					+ " to CAR :" + car.getCarId());
		}

	}
	
	/**
	 * This method returns a List of Cars created for the drop ship vendor styles.
	 * The method signature of this method has been changed to track the cars
	 * that got created by this process to fetch additional attributes from PIM for these cars.
	 *   
	 * @return List of Cars created.
	 *   
	 * @param pimCarTo
	 * @param packDtoList
	 * @throws Exception
	 */
	public List<Car> createCARforDropshipValidVendorStyles(StyleDTO pimCarTo,
			List<SkuDTO> packDtoList) throws Exception {

		this.setContextParameters();
		Date escalationDate = this.getEscalationDate();
		Long orinNumber;
		CarNote note = null;
		//boolean isAssignedToVendor = false;

		log.info("Going to Process Cars for  Vendor Styles - "
				+ pimCarTo.getVendorStyle());
		log.info("Number of pack details to process: " + packDtoList.size());
		log.info("Number of Sku details to process: "
				+ pimCarTo.getSkuInfo().size());
		VendorStyle packStyle = pimCarTo.getVendorStyleObj()
				.getParentVendorStyle();
		VendorStyle vs = pimCarTo.getVendorStyleObj();
        List<Car> dsCarsList = new ArrayList<Car>();
        
		if (packStyle != null) {
			log.info(" pattern pack vendor style :"
					+ packStyle.getVendorStyleId() + " vendor style :"
					+ pimCarTo.getVendorStyleObj().getVendorStyleId());
			vs = packStyle;
		}

		boolean isSKUExists = false;
        boolean createCar = true;
                Car car = null;
                if (vs.isPattern()  || (vs.getParentVendorStyle() != null ) ) {    
                    // if this vendor style is a child of an existing pattern,
                    // merge skus into the pattern parent as opposed to creating a new car.
                    if (log.isInfoEnabled()) log.info("DROPSHIP for existing style with a pattern came in. Attempting to merge.");
                    
                    VendorStyle parent;
                    if (vs.getParentVendorStyle() != null) {
                        parent = vs.getParentVendorStyle();
                    } else {
                        parent = vs;
                    }
                    car = patternAndCollectionManager.getMergeEilgibleCar(parent, null); 
                    
                    // skus dont exist at this point, they are added below.
                    // if we initialize car to the parent, skus should be just added to that.
                    if (car != null) {
                        if (log.isInfoEnabled()) log.info("Found parent pattern car to merge skus into: " + car.getCarId());
                        car = carManager.getCarAndAttributes(car.getCarId());
                        createCar = false;
                    } else {
                        if (log.isInfoEnabled()) log.info("No parent pattern car found.");
                    }
                }
                
                if (car == null) {
                    car = new Car();
                    car.setDepartment(pimCarTo.getDepartment());
                    car.setSourceType(sourceDropshipCar);
                    car.setSourceId(pimCarTo.getVendorNumber() + "-"
                                    + pimCarTo.getVendorStyle());
                    car.setVendorStyle(vs);
                    car.setWorkFlow(defaultWorkflow);
                    car.setCarUserByLoggedByUserId(systemUser);

                    /* CR assign to vendor -- REMOVING FOR PI-103 */
    //		isAssignedToVendor = sendDirectToVendor(pimCarTo
    //				.getDepartment().getDeptId(), pimCarTo.getVendor()
    //				.getVendorId());

//		if (isAssignedToVendor) {
//			log.info("send car directly to vendor "
//					+ pimCarTo.getVendor().getVendorId());
//			car.setAssignedToUserType(vendorType);
//			car.setCurrentWorkFlowStatus(withVendor);
//			car.setRejectionCount(0);
//		} else {
			car.setAssignedToUserType(buyer);
			car.setCurrentWorkFlowStatus(initiated);
//		}

		car.setEscalationDate(escalationDate);
		car.setIsUrgent(Constants.FLAG_NO);
		if (StringUtils.isNotBlank(pimCarTo.getExpectedShipDate())) {
			car.setExpectedShipDate(DateUtils.parseDate(pimCarTo
					.getExpectedShipDate(), "yyyy-MM-dd"));
			car.setDueDate(this.getDueDate());
		} else {
			car.setExpectedShipDate(this.getCompletionDate());
			car.setDueDate(this.getDueDate());
		}

		String isProductTypeReq = Constants.FLAG_NO;
		List<ProductType> productTypeList = productManager.getProductTypes(vs
				.getClassification().getClassId());
		List<ProductType> productTypeListNew = new ArrayList<ProductType>();
		for (ProductType pt : productTypeList) {
			if (!productTypeListNew.contains(pt))
				productTypeListNew.add(pt);
		}
		ProductType productType = null;
		if (productTypeListNew != null && !productTypeListNew.isEmpty()) {
			if (productTypeListNew.size() > 1) {
				isProductTypeReq = Constants.FLAG_YES;
			} else {
				productType = productTypeListNew.get(0);
			}
		}
		log.info("Total number of product Type Available :" + productTypeListNew.size());
		car.setIsProductTypeRequired(isProductTypeReq);
		car.setStatusCd(Status.ACTIVE);
		car.setContentStatus(contentInProgress);
		car.setLastWorkflowStatusUpdateDate(Calendar.getInstance().getTime());
		this.setAuditInfo(systemUser, car);

                    if (productType != null) {
                            // Set the VendorStyle Product
                            if(pimCarTo.getParentVendorStyle()!=null){
                            vs.setProductType(productType);
                            this.carManager.saveOrUpdate(vs);
                            }
                            this.resyncCarsAttributes(car, productType);
                    }
                }
                // Check whether online only attribute at product level
                String setOnlineOnly = Constants.FLAG_NO;
                Set<CarAttribute> carAttrs = car.getCarAttributes();
                for (CarAttribute ca : carAttrs) {
                        if (ca.getAttribute().getBlueMartiniAttribute().indexOf("On Line") != -1) {
                                if (log.isDebugEnabled()) {
                                        log.debug("Online Only Attribute at Product Level");
                                }
                                setOnlineOnly = Constants.FLAG_YES;
                                break;
                        }
                }
                
		Car existingCar = carManager.getCarForStyle(vs.getVendorStyleId());
		
		if(existingCar==null  && pimCarTo.getParentVendorStyle()!=null && pimCarTo.isVendorStyleExixtsInDB() ) {
			existingCar = carManager.getCarForStyle(pimCarTo.getVendorStyleObj().getVendorStyleId());
			
		}
		
		//Add Note to Existing Car...
		if (createCar && existingCar != null && note == null) {
			note = new CarNote();
			note.setNoteType(carNoteType);
			note.setIsExternallyDisplayable(CarNote.FLAG_NO);
			note.setStatusCd(Status.ACTIVE);
			note.setCar(car);
			note.setNoteText("Car ID: " + existingCar.getCarId()
					+ " exists for the same Vendor Style.  You can use the copy functionality to update the attributes from this Car.");
			this.setAuditInfo(systemUser, note);
			car.getCarNotes().add(note);
		}
		Attribute onlineOnlyAtr = dropshipDao
				.getAttributeByName(Constants.SDF_ONLINE_ONLY);
		Attribute dropShipAtr = dropshipDao.getAttributeByName(Constants.IS_DROPSHIP);

		for (SkuDTO pimCarSkuTo : pimCarTo.getSkuInfo()) {
			try {
				VendorSku venSku = carManager.getActiveCarSkus(pimCarSkuTo
						.getBelkSku());
				if (venSku != null) {
					log.info(" CAR already exists for Belk Sku: "
							+ pimCarSkuTo.getBelkSku());
					continue;
				}
				log.info("Creating Vendor Skus for Car: Belk Sku:"
						+ pimCarSkuTo.getBelkSku());
				orinNumber = Long.valueOf(pimCarSkuTo.getOrin());
				VendorSku sku = new VendorSku();
				sku.setCar(car);
				sku.setBelkSku(pimCarSkuTo.getBelkSku());
				sku.setBelkUpc(pimCarSkuTo.getBelkSku());
				sku.setLongSku(pimCarSkuTo.getVendorUpc());
				sku.setVendorUpc(pimCarSkuTo.getVendorUpc());
				sku.setStatusCd(Status.ACTIVE);
				sku.setColorCode(pimCarSkuTo.getColorCode());
				sku.setColorName(pimCarSkuTo.getColorName());
				sku.setSizeCode(pimCarSkuTo.getSizeCode());
				sku.setSizeName(pimCarSkuTo.getSizeName());
				sku.setOrinNumber(orinNumber);			
				// sku.setSetFlag(Constants.FLAG_NO);
				
				// CARS Size Conversion Issue - Size name overwritten by resync
				// size job
				// Adding size name of the SKU in conversion_name column to show the
				// incoming size name in cars edit page
				sku.setIdbSizeName(pimCarSkuTo.getSizeName()); 
				sku.setParentUpc(pimCarSkuTo.getParentBelkSKU());
				sku.setVendorStyle(pimCarTo.getVendorStyleObj());
				// if online only attribute is not set at product level
				// set at sku level
				if (Constants.FLAG_NO.equals(setOnlineOnly)) {
					if (log.isDebugEnabled()) {
						log.debug("Online Only Attribute at Sku Level");
					}
					CarSkuAttribute onlineSkuAttr = new CarSkuAttribute();
					onlineSkuAttr.setAttribute(onlineOnlyAtr);
					onlineSkuAttr.setAttrValue(Constants.VALUE_YES);
					this.setAuditInfo(systemUser, onlineSkuAttr);
					onlineSkuAttr.setVendorSku(sku);
					sku.getCarSkuAttributes().add(onlineSkuAttr);
				}
				// Added the retail price to sku
				// creating the dropship attribute at sku level
				CarSkuAttribute dropshipSkuAttr = new CarSkuAttribute();
				dropshipSkuAttr.setAttribute(dropShipAtr);
				dropshipSkuAttr.setAttrValue(Constants.VALUE_YES);
				this.setAuditInfo(systemUser, dropshipSkuAttr);
				dropshipSkuAttr.setVendorSku(sku);
				sku.getCarSkuAttributes().add(dropshipSkuAttr);
				this.setAuditInfo(systemUser, sku);
				car.getVendorSkus().add(sku);
				isSKUExists = true;
			} catch (Exception ex) {
				log.error("Error while Creating Vendor Skus for Car"
						+ ex.getMessage());
			}
		}
		for (SkuDTO packDto : packDtoList) {
			try {
				VendorSku venSku = carManager.getActiveCarSkus(packDto
						.getBelkSku());
				if (venSku != null) {
					log.info(" CAR already exists for Belk PackSku: "
							+ packDto.getBelkSku());
					continue;
				}
				log.info("Creating Vendor pack Sku for Car: Belk UPC:"
						+ packDto.getBelkSku());
				VendorSku packSku = new VendorSku();
				packSku.setCar(car);
				packSku.setOrinNumber(Long.valueOf(packDto.getOrin()));
				packSku.setBelkSku(packDto.getBelkSku());
				packSku.setBelkUpc(packDto.getBelkSku());
				packSku.setLongSku(packDto.getVendorUpc());
				packSku.setVendorUpc(packDto.getVendorUpc());
				packSku.setStatusCd(Status.ACTIVE);
				packSku.setSetFlag(Constants.FLAG_YES);
				packSku.setRetailPrice(packDto.getRetailPrice());
				packSku.setVendorStyle(pimCarTo.getVendorStyleObj());
				packSku.setColorCode(packDto.getColorCode());
				packSku.setColorName(packDto.getColorName());
				packSku.setSizeCode(packDto.getSizeCode());
				packSku.setSizeName(packDto.getSizeName());
				packSku.setIdbSizeName(packDto.getSizeName());
				// if online only attribute is not set at product level
				// set at sku level
				if (Constants.FLAG_NO.equals(setOnlineOnly)) {
					if (log.isDebugEnabled()) {
						log.debug("Online Only Attribute at Sku Level");
					}
					CarSkuAttribute onlineSkuAttr = new CarSkuAttribute();
					onlineSkuAttr.setAttribute(onlineOnlyAtr);
					onlineSkuAttr.setAttrValue(Constants.VALUE_YES);
					this.setAuditInfo(systemUser, onlineSkuAttr);
					onlineSkuAttr.setVendorSku(packSku);
					packSku.getCarSkuAttributes().add(onlineSkuAttr);
				}
				// creating the dropship attribute at sku level
				CarSkuAttribute dropshipSkuAttr = new CarSkuAttribute();
				dropshipSkuAttr.setAttribute(dropShipAtr);
				dropshipSkuAttr.setAttrValue(Constants.VALUE_YES);
				this.setAuditInfo(systemUser, dropshipSkuAttr);
				dropshipSkuAttr.setVendorSku(packSku);
				packSku.getCarSkuAttributes().add(dropshipSkuAttr);
				this.setAuditInfo(systemUser, packSku);
				car.getVendorSkus().add(packSku);
				isSKUExists = true;
			} catch (Exception ex) {
				log.error("Error while Creating Vendor Pack Sku for Car:"
						+ ex.getMessage());
			}
		}
		if (!isSKUExists) {
			log.info("All the Skus & Pack Skus in the Vendorstyle is already exist");
			return dsCarsList;
		}

		CatalogProduct product = null;
		// Checking for product information in Catalog
		if (car.getVendorStyle().isPattern()) {
			// Find choice based on the first Product with Content within a
			// Pattern
			VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
			criteria.setVendorStyleId(car.getVendorStyle().getVendorStyleId());
			criteria.setChildrenOnly(true);
			if (log.isDebugEnabled())
				log.debug("Pattern - Search for child product information ");
			List<VendorStyle> childProducts = this.carManager
					.searchVendorStyle(criteria);
			if (childProducts != null) {
				for (VendorStyle childProduct : childProducts) {
					if (log.isDebugEnabled())
						log.debug("Pattern - Searching for product information in catalog "
										+ childProduct.getVendorStyleNumber());
					product = catalogImportManager.getProduct(childProduct
							.getVendorNumber(), childProduct
							.getVendorStyleNumber());
					if (product != null) {
						break;
					}
				}
			}
		} else {
			if (log.isDebugEnabled())
				log.debug("Product - Searching for product information in catalog "
								+ car.getVendorStyle().getVendorStyleNumber());
			log.info("vendor Number :"
							+ car.getVendorStyle().getVendorNumber());
			log.info("VendorStyle Number :"
					+ car.getVendorStyle().getVendorStyleNumber());
			product = catalogImportManager.getProduct(car.getVendorStyle()
					.getVendorNumber(), car.getVendorStyle()
					.getVendorStyleNumber());
		}
		if (product == null) {
			// check based on sku
			CatalogSku sku = null;
			if (car != null && car.getVendorSkus() != null
					&& !car.getVendorSkus().isEmpty()) {
				sku = catalogImportManager.getSku(car.getVendorSkus()
						.iterator().next().getVendorUpc());
			}
			if (sku != null) {
				product = sku.getCatalogProduct();
			}
		}

		if (product != null && product.getCatalogProductId() != 0
				&& car.getVendorStyle().getProductType() != null) {
			catalogImportManager.copyToCar(product, car);

			// Item found in catalog.. then assing the CAR to buyer
			if (car.getCarId() == 0) {
				car.setAssignedToUserType(buyer);
			}
		}

		try {
			log.info("Creating Car for Vendor Style with ID: "
					+ car.getVendorStyle().getVendorStyleId());
			car = carManager.createCar(car);
			if(car!=null && !(Constants.FLAG_YES.equalsIgnoreCase(car.getIsProductTypeRequired()))){
                dsCarsList.add(car);
            }
			log.info("new CAR created with with CarID: " + car.getCarId());
		} catch (Exception ex) {
			if (log.isErrorEnabled())
				log.error("Save Exception. Cause: " + ex.getMessage());
			//throw ex;
		}
		
		return dsCarsList;
	}

	/**
	 * 
	 * @param user
	 * @param model
	 */
	public void setAuditInfo(User user, BaseAuditableModel model) {

		if (StringUtils.isBlank(model.getCreatedBy())) {
			model.setCreatedBy(user.getUsername());
			model.setCreatedDate(new Date());
		}
		model.setUpdatedBy(user.getUsername());
		model.setUpdatedDate(new Date());
	}
	/**
	 * 
	 * @param deptId
	 * @param vendorId
	 * @return
	 */

	private boolean sendDirectToVendor(Long deptId,Long vendorId) {
		List <CarUserVendorDepartment> vendors = carManager.getVendorsByDeptId(deptId);

		for (CarUserVendorDepartment carUserVendorDepartment : vendors) {
			if(carUserVendorDepartment.getVendor().getVendorId() == (vendorId)){
				log.info("returning vendor user");	
				return true;
			}
		}
		return false;
	}

	/**
	 * get Escalation date
	 * @return
	 */
	private Date getEscalationDate() {
		Config numberOfEscalationDays = (Config) carLookupManager.getById(
				Config.class, Config.INIT_NUMBER_OF_ESCALATION_DAYS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(numberOfEscalationDays
				.getValue()));
		return cal.getTime();
	}
	/**
	 * get due date
	 * @return
	 */
	private Date getDueDate() {
		Config numberOfDueDays = (Config) carLookupManager.getById(
				Config.class, Config.INIT_NUMBER_OF_DUE_DAYS);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Integer.parseInt(numberOfDueDays
				.getValue()));
		return cal.getTime();
	}
	/**
	 * Get Completion date
	 * @return
	 */
	private Date getCompletionDate() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, Car.NUM_DAYS_TO_COMPLETION);
		return cal.getTime();
	}

	/**
	 * Process the exception
	 * @param processName
	 * @param ex
	 */
	public void processException(String processName, Exception ex) {
		StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		sendFailureNotification(processName, sw.toString());
	}

	/**
	 * Send email to USer
	 * @param processName
	 * @param content
	 */
	private void sendFailureNotification(String processName, String content) {
		this.systemUser = (User) getServletContext().getAttribute(
				Constants.SYSTEM_USER);
		this.type = (NotificationType) getServletContext().getAttribute(
				Constants.NOTIFICATION_TYPE_SYSTEM_FAILURE);
		this.sendNotifications = (Config) getServletContext().getAttribute(
				Constants.SEND_EMAIL_NOTIFICATIONS);
		this.emailNotificationList = (Config) getServletContext().getAttribute(
				Constants.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST);

		/*
		 * NotificationType type =
		 * lookupManager.getNotificationType(NotificationType.SYSTEM_FAILURE);
		 * 
		 * Config userName = (Config) lookupManager.getById(Config.class,
		 * Config.SYSTEM_USER); Config sendNotifications = (Config)
		 * lookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
		 * Config emailNotificationList = (Config)
		 * lookupManager.getById(Config.class,
		 * Config.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST);
		 */
		Map<String, String> model = new HashMap<String, String>();
		String[] emails = StringUtils.split(emailNotificationList.getValue(),
				",;");
		for (String email : emails) {
			model.put("userEmail", email);
			model.put("processName", processName);
			model
					.put("exceptionContent", StringUtils.abbreviate(content,
							4000));
			model.put("executionDate", DateUtils.formatDate(new Date(),
					"MM/dd/yyyy HH:mm:ss"));

			try {
				if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
					sendEmailManager.sendNotificationEmail(type, systemUser,
							model);
				}
			} catch (SendEmailException se) {
				log.error("Error when sending email to: " + email);
			} catch (Exception ex) {
				log.error("General Exception occured. Cause: " + email);
			}
		}

	}

	@Override
	public void processRLRMessage(RLRItems rlrMessage) throws Exception  {

		try {
			log.info("---------------->  Start Process RLR Message<-----------------");
			this.systemUser = (User) getServletContext().getAttribute(
					Constants.SYSTEM_USER);
			// check whether RLR message is not null for processing.
			if (rlrMessage == null) {
				log.info("RLR message is null after parsing.");
				return;
			}
			Department dept = null;
			log.info("DeptNumber: " + rlrMessage.getDeptNumber()
					+ " DeptName: " + rlrMessage.getDeptName()
					+ " ClassNumber: " + rlrMessage.getClassNumber()
					+ " ClassName: " + rlrMessage.getClassName() + " DmmEmail:"
					+ rlrMessage.getDmmEmail() + " DmmName: "
					+ rlrMessage.getDmmName() + " GmmEmail: "
					+ rlrMessage.getGmmEmail() + " GmmName: "
					+ rlrMessage.getGmmName() + "DemandCenter: "
					+ rlrMessage.getDemandCenter() + " DemandCenterDesc: "
					+ rlrMessage.getDemandCenterDesc());

			if (StringUtils.isNotBlank(rlrMessage.getDeptNumber())) {
				dept = carManager.getDepartment(rlrMessage.getDeptNumber());
			   }
						
			if (StringUtils.isNotBlank(rlrMessage.getDeptNumber())
					&& StringUtils.isNotBlank(rlrMessage.getDeptName())) {
				log.info("Enter into Department insert or update");
				// Check if Department exists and create if does not exists
				if (dept == null) {
					log.info("Department is not exists");
					dept = new Department();
					dept.setDeptCd(rlrMessage.getDeptNumber());
					dept.setStatusCd(Status.ACTIVE);
					dept.setName(rlrMessage.getDeptName());
					dept.setDescription(rlrMessage.getDeptName());
					this.setAuditInfo(systemUser, dept);
					dept = (Department) carManager.save(dept);
					// assign new department to Art Directors and Sample
					// coordinators
					assignNewDepartment(dept);
				} else {
					log.info("Department is already exists");
					if (!StringUtils.equals(rlrMessage.getDeptName(), dept
							.getName())) {
						dept.setName(rlrMessage.getDeptName());
						dept.setDescription(rlrMessage.getDeptName());
						this.setAuditInfo(systemUser, dept);
						dept = (Department) carManager.save(dept);
					}
				}
				log.info("End Department insert or update");
			}
			if (StringUtils.isNotBlank(rlrMessage.getClassNumber())
					&& StringUtils.isNotBlank(rlrMessage.getClassName())) {
				
				log.info("Enter into Classifcation insert or update");
				PatternProcessingRule defPatternProcessingRule = (PatternProcessingRule) carLookupManager
						.getById(PatternProcessingRule.class,
								PatternProcessingRule.NONE);
				Classification cls = carManager.getClassification(Short
						.parseShort(rlrMessage.getClassNumber()));
				// Check if Classification exists and create if does not exists
				if (cls == null && dept != null) {
					log.info("Classification is not exists");

					// create class
					cls = new Classification();
					cls.setBelkClassNumber(Short.parseShort(rlrMessage
							.getClassNumber()));
					cls.setIsProductTypeRequired(Classification.FLAG_NO);
					cls.setStatusCd(Status.ACTIVE);
					cls.setPatternProcessingRule(defPatternProcessingRule);
					cls.setName(rlrMessage.getClassName());
					cls.setDescr(rlrMessage.getClassName());
					cls.setDepartment(dept);
					this.setAuditInfo(systemUser, cls);
					carManager.save(cls);
				} else if (cls != null) {
					log.info("Classification is already exists");
					cls.setName(rlrMessage.getClassName());
					cls.setDescr(rlrMessage.getClassName());
					if (dept != null) {
						cls.setDepartment(dept);
					}
					this.setAuditInfo(systemUser, cls);
					carManager.save(cls);
				} else {
					log.error("department is not available for this classification");
				}
				log.info("End Classifcation insert or update");
			}
			if (dept != null && rlrMessage.getDmmEmail() != null
					&& rlrMessage.getDmmName() != null
					&& rlrMessage.getGmmEmail() != null
					&& rlrMessage.getGmmName() != null
					&& StringUtils.isNotBlank(rlrMessage.getDmmEmail())
					&& StringUtils.isNotBlank(rlrMessage.getDmmName())
					&& StringUtils.isNotBlank(rlrMessage.getGmmEmail())
					&& StringUtils.isNotBlank(rlrMessage.getGmmName())) {
			    log.info("Enter into User Rank insert or update");
				UsersRank usersRank = carManager.getUsersRank(Long
						.parseLong(dept.getDeptCd()));
				if (usersRank == null) {
					log.info("User Rank is empty for the department: "
							+ dept.getDeptCd());
					usersRank = new UsersRank();
					usersRank.setDepartmentCode(Long
							.parseLong(dept.getDeptCd()));
				} else {
					log.info("User Rank is already exists for the department: "
							+ dept.getDeptCd());
				}
				usersRank.setDmmEmail(rlrMessage.getDmmEmail());
				usersRank.setDmmName(rlrMessage.getDmmName());
				usersRank.setGmmEmail(rlrMessage.getGmmEmail());
				usersRank.setGmmName(rlrMessage.getGmmName());
				usersRank.setDemandCenter(rlrMessage.getDemandCenter());
				usersRank.setDcDesc(rlrMessage.getDemandCenterDesc());
				this.setAuditInfo(systemUser, usersRank);
				carManager.save(usersRank);
				log.info("End User Rank insert or update");
			}
			log.info("---------------->  End Process RLR Message<-----------------");

		} catch (Exception ex) {
			log.error("ERROR wile processing RLR message" + ex.getMessage());
			processException("ERROR in Processing RLR Message", ex);
			throw ex;

		}

	}

	private void assignNewDepartment(Department dept) {
		// Get the list of Art director and Sample Coordinator car_userid's from
		// CAR_USER table
		List<User> users = this.carManager.getAllArtAndSampleUsers();
		for (User user : users) {
			user.addDepartment(dept);
			this.carManager.save(user);
		}
	}
	
	/**
	 * Method to create and ftp txt files to CMP containing all updated PIM attributes
	 *  for closed cars.
	 *  
	 */
	public void processPIMAttributeUpdatesForClosedCars()throws Exception{
	    if (log.isInfoEnabled()){
            log.info("---------------->  Begin PIM Attribute Updates For ClosedCars Process <-----------------");
	    }
                
            
        Config exportDirConfig = (Config) carLookupManager.getById(Config.class, Config.PIM_ATTRIBUTE_UPDATE_EXPORT_DIRECTORY);
        Config exportFileConfig = (Config) carLookupManager.getById(Config.class, Config.PIM_ATTRIBUTE_UPDATE_EXPORT_FILENAME);
        Config ftpHost = (Config) carLookupManager.getById(Config.class, Config.PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_HOST);
        Config ftpUserId = (Config) carLookupManager.getById(Config.class, Config.PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_USERNAME);
        Config ftpPassword = (Config) carLookupManager.getById(Config.class, Config.PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_PASSWORD);
        Config ftpRemoteDir = (Config) carLookupManager.getById(Config.class, Config.PIM_ATTRIBUTE_UPDATE_EXPORT_FTP_REMOTE_DIRECTORY);
        Config completedDirectory = (Config) carLookupManager.getById(Config.class, Config.PIM_ATTRIBUTE_UPDATE_DATA_FILE_COMPLETED_DIR_NAME);
        

        File dir = new File(exportDirConfig.getValue());
        if (!dir.exists())
            dir.mkdirs();

        // Create a File object
        String fileName = exportFileConfig.getValue();
        BMIFileGenerationUtil.FileSession fileSession = BMIFileGenerationUtil.getFileSession(exportDirConfig.getValue(), fileName);

        boolean hasErrors = false ;

        // closed car pim update job should share lock file with the update item job - cant let both run at once
        boolean lockFileCreated = createLockFile(Config.PIM_UPDATE_LOCK_PATH); 
        if (!lockFileCreated) {
            if (log.isErrorEnabled()) {
                log.error("Exiting.  Another process is already running!");
                log.info("---------------->  End PIM Attribute Updates For ClosedCars Process <-----------------");
            }
            
            sendFailureNotification("PIM Attribute Update for Closed Cars", "Job was not executed on schedule due to the presence of a lockfile.");
            
            return;
        }
        
        try{

            
            List<ClosedCarAttrSync> closedCarsTobeProcessed = carManager.getUpdatedClosedCars();
            if(closedCarsTobeProcessed!=null && !closedCarsTobeProcessed.isEmpty()){
                if(log.isInfoEnabled()){
                    log.info("Number of closed cars to be processed "+closedCarsTobeProcessed.size());
                }
                for(ClosedCarAttrSync closedCarAttrSync : closedCarsTobeProcessed){
                    long carId = closedCarAttrSync.getCarId();
                    Date lastUpdatedDt = closedCarAttrSync.getLastUpdatedTimestamp();
                    if(log.isInfoEnabled()){
                        log.info("updated timestamp for car_id "+carId+" is:  "+lastUpdatedDt);
                    }
                    Car car = carManager.getCarAndAttributes(carId);
                    VendorStyle vStyle = car.getVendorStyle();
                    String prodCode = vStyle.getVendorNumber().concat(vStyle.getVendorStyleNumber());
                    Set<VendorSku>vSkus = car.getVendorSkus();
                    Set<CarAttribute>carAttrs = car.getCarAttributes();
                    //generating bmi records for car attributes
                    if(carAttrs!=null && !carAttrs.isEmpty()){
                        for(CarAttribute carAttr : carAttrs){
                            if(carAttr.getUpdatedDate().compareTo(lastUpdatedDt) >= 0){
                                Attribute attr = carAttr.getAttribute();
                                String carAttrVal = carAttr.getAttrValue()!=null?carAttr.getAttrValue():"<NULL>";
                                String carAttrRecord = getRecordForUpdatedPIMAttributes(Constants.PRODUCT_TYPE,prodCode,attr.getBlueMartiniAttribute().replaceAll("[^a-zA-Z0-9]", "_"),carAttrVal);
                                if(log.isDebugEnabled()){
                                    log.debug("CAR Attribute record  "+carAttrRecord);
                                }
                                fileSession.write(carAttrRecord);
                            }
                        }
                    }
                    
                    //generate object attribute record for pim attributes at vendor style
                    Set<VendorStylePIMAttribute>vStylePIMAttrs = vStyle.getVendorStylePIMAttribute();
                    if(vStylePIMAttrs!=null && !vStylePIMAttrs.isEmpty()){
                        for(VendorStylePIMAttribute vSPAttr :vStylePIMAttrs){
                            if(vSPAttr.getUpdatedDate().compareTo(lastUpdatedDt) >= 0){
                                PIMAttribute pimAttr = vSPAttr.getPimAttribute();
                                String vSPAttrVal = vSPAttr.getValue()!=null?vSPAttr.getValue():"<NULL>";
                                String prodRecord = getRecordForUpdatedPIMAttributes(Constants.PRODUCT_TYPE,prodCode,pimAttr.getName().replaceAll("[^a-zA-Z0-9]", "_"),vSPAttrVal);
                                if(log.isDebugEnabled()){
                                    log.debug("Vendor style PIM Attribute record  "+prodRecord);
                                }
                                fileSession.write(prodRecord);
                            }
                        }
                    }else{
                        if(log.isInfoEnabled()){
                            log.info("There are no vendor style PIM attributes associated to vendor style id "+vStyle.getVendorStyleId());
                        }
                    }

                    //generate object attribute sku record for color/size name
                    for(VendorSku vSku : vSkus){
                        if(vSku.getUpdatedDate().compareTo(lastUpdatedDt) >= 0){
                            String colorVal = vSku.getColorName()!=null?vSku.getColorName() : "<NULL>";
                            String sizeVal = vSku.getSizeName()!=null ? vSku.getSizeName() : "<NULL>";
                            String colorRecord = getRecordForUpdatedPIMAttributes(Constants.SKU_TYPE,vSku.getBelkUpc(),Constants.COlOR_DESC,colorVal);
                            String sizeRecord = getRecordForUpdatedPIMAttributes(Constants.SKU_TYPE,vSku.getBelkUpc(),Constants.SIZE_DESC,sizeVal);
                            if(log.isDebugEnabled()){
                                log.debug("SKU record  for color  "+colorRecord);
                                log.debug("SKU record  for size  "+sizeRecord);
                            }
                            fileSession.write(colorRecord);
                            fileSession.write(sizeRecord);
                        }
                        Set<VendorSkuPIMAttribute>vSkuPIMAttrs = vSku.getSkuPIMAttributes();
                        if(vSkuPIMAttrs!=null && !vSkuPIMAttrs.isEmpty()){
                            for(VendorSkuPIMAttribute vSKAttr : vSkuPIMAttrs){
                                if(vSKAttr.getUpdatedDate().compareTo(lastUpdatedDt) >= 0){
                                    PIMAttribute skuPIMAttr = vSKAttr.getPimAttributeDetails();
                                    String vSKAttrVal = vSKAttr.getAttributeValue()!=null?vSKAttr.getAttributeValue():"<NULL>";
                                    String skuPARecord = getRecordForUpdatedPIMAttributes(Constants.SKU_TYPE,vSku.getBelkUpc(),skuPIMAttr.getName().replaceAll("[^a-zA-Z0-9]", "_"),vSKAttrVal);
                                    if(log.isDebugEnabled()){
                                        log.debug("SKU PIM Attribute record  "+skuPARecord);
                                    }
                                    fileSession.write(skuPARecord);
                                }                                    
                            }
                        }else{
                            if(log.isInfoEnabled()){
                                log.info("There are no vendor sku PIM attributes associated to vendor sku id "+vSku.getCarSkuId());
                            }
                        }
                        //generating bmi records for car sku attributes
                        List<CarSkuAttribute>carSkuAttrs = vSku.getAttributes();
                        if(carSkuAttrs!=null && !carSkuAttrs.isEmpty()){
                            for(CarSkuAttribute carSkuAttr : carSkuAttrs){
                                if(carSkuAttr.getUpdatedDate().compareTo(lastUpdatedDt) >= 0){
                                    Attribute cAttr = carSkuAttr.getAttribute();
                                    String carSkuAttrVal = carSkuAttr.getAttrValue()!=null?carSkuAttr.getAttrValue():"<NULL>";
                                    String carSkuAttrRecord = getRecordForUpdatedPIMAttributes(Constants.SKU_TYPE,vSku.getBelkUpc(),cAttr.getBlueMartiniAttribute().replaceAll("[^a-zA-Z0-9]", "_"),carSkuAttrVal);
                                    if(log.isDebugEnabled()){
                                        log.debug("CAR SKU Attribute record  "+carSkuAttrRecord);
                                    }
                                    fileSession.write(carSkuAttrRecord);
                                }                                    
                            }
                        }else{
                            if(log.isInfoEnabled()){
                                log.info("There are no car sku attributes associated to vendor sku id "+vSku.getCarSkuId());
                            }
                        }                            
                    }                    
                }
            }else{
                if(log.isInfoEnabled()){
                    log.info("No Closed cars to process");
                }
            }
            fileSession.flush();
            if(fileSession.getFile().exists() && !hasErrors){
                BMIFileGenerationUtil.sFtpFile(fileSession.getFile(), ftpHost, ftpUserId, ftpPassword, ftpRemoteDir);
            }
            BMIFileGenerationUtil.processCompletedFile(exportDirConfig.getValue(), fileName, completedDirectory.getValue(),true,true);
            
            //mark the Porcessed flag as Y after completion 
            updateClosedCarsAsProcessed(closedCarsTobeProcessed);
            
            if (log.isInfoEnabled()){
                log.info("---------------->  End PIM Attribute Updates For ClosedCars Process <-----------------");
            }
            
        }catch(Exception e){
            hasErrors = true;
            if(log.isErrorEnabled()){
                log.error("Error while generating txt File for updated PIM Attributes for closed cars "+e);
            }
            processException("Error while generating txt File for updated PIM Attributes for closed cars ", e);
        }finally{
            fileSession.close();
            deleteLockFile(Config.PIM_UPDATE_LOCK_PATH);
        }     
            
	}
	
	/**
	 * Method to mark the processed flag to Y after the processing is 
	 * successfully complete.
	 * 
	 * @param closedCarsTobeProcessed
	 * @throws Exception
	 */
    private void updateClosedCarsAsProcessed(
            List<ClosedCarAttrSync> closedCarsTobeProcessed) throws Exception {
        if(closedCarsTobeProcessed!=null && !closedCarsTobeProcessed.isEmpty()){
            for(ClosedCarAttrSync closedCar : closedCarsTobeProcessed){
                closedCar.setJobProcessed(Constants.FLAG_Y);
                closedCar.setLastUpdatedTimestamp(new Date());
                if (log.isInfoEnabled()){
                    log.info(" Updating processed flag to Y for the Car Id ----------------- "+closedCar.getCarId());
                }
                carManager.createOrUpdateClosedCarAttrSync(closedCar);
            }
        }
        
    }

    /*
     * construct record in the below format:
     * SKU|{BELK_UPC}|ATTR NAME|{ATTR_VALUE}
     * PRODUCT|{PRODUCT_CODE}|ATTR NAME|{ATTR_VALUE}
     */       
    private String getRecordForUpdatedPIMAttributes(String type, String code, String atrName, String atrVal) {
        StringBuffer sb = new StringBuffer();
        sb.append(type);
        sb.append("|");
        sb.append(code);
        sb.append("|");
        sb.append(atrName);
        sb.append("|");
        sb.append(atrVal);  
        return sb.toString();
    }

        /**
         * Execute a PL/SQL procedure to update Skus with their 
         * pack parent sku id.
         */
        public void executeSkuPackParentResyncProcedure() {
            if (log.isInfoEnabled()) {
                log.info("---------------->  Begin Sku Parent Resync Procedure <-----------------");
            }
            carManager.executeSkuPackParentResyncProcedure();
            
            if (log.isInfoEnabled()) {
                log.info("---------------->  End Sku Parent Resync Procedure <-----------------");
            }
        }
        
        /**
         * Processes a GroupingMessage, determines the action to take for the group based on the type of message.
         * 
         * @param groupingMessage
         * @throws Exception 
         */
        public void processGrouping(GroupingMessage groupingMessage) throws Exception {
            
            if (log.isInfoEnabled()) {
                log.info("Processing Group");
            }
            
            if (systemUser==null) {
                systemUser = (User) getServletContext().getAttribute(
                        Constants.SYSTEM_USER);
            }
            
            String type = groupingMessage.getType();
            
            if (type.equalsIgnoreCase(Constants.GROUP_CREATE)) {
                createGroup(groupingMessage.getCarsGroupDtl());
                
            } else if (type.equalsIgnoreCase(Constants.GROUP_MODIFY)) {
                modifyGroup(groupingMessage.getCarsGroupDtl());
                
            } else if (type.equalsIgnoreCase(Constants.GROUP_DEACTIVATE)) {
                CarsGroupingDtl groupDtl = groupingMessage.getCarsGroupDtl();
                GroupingParent parent = null;
                if (groupDtl != null) {
                    if (groupDtl.getGrouping() != null) {
                        parent = groupDtl.getGrouping();
                    }
                }
                // Check if Parent Component exists.
                if (parent!=null) {
                    VendorStyle vs = patternAndCollectionManager.getVendorStyleIfExists(parent.getId(),
                            parent.getVendorNumber(), parent.getVendorStyle(), parent.getProductCode());
                    if (vs==null) {
                        if (log.isErrorEnabled()) {
                            log.error("Received a GroupDis request group that does not exist in CARS.  Group Id:" 
                                    + parent.getId() + ".  Ignoring.");
                        }
                        return;
                    }
                    deactivateGroup(vs);
                }
                
            } else if (type.equalsIgnoreCase(Constants.GROUP_DELETE)) {
                CarsGroupingDtl groupDtl = groupingMessage.getCarsGroupDtl();
                GroupingParent parent = null;
                if (groupDtl != null) {
                    if (groupDtl.getGrouping() != null) {
                        parent = groupDtl.getGrouping();
                    }
                }
                // Check if Parent Component exists.
                if (parent!=null) {
                    VendorStyle vs = patternAndCollectionManager.getVendorStyleIfExists(parent.getId(),
                            parent.getVendorNumber(), parent.getVendorStyle(), parent.getProductCode());
                    if (vs==null) {
                        if (log.isErrorEnabled()) {
                            log.error("Received a GroupDel request group that does not exist in CARS.  Group Id:" 
                                    + parent.getId() + ".  Ignoring.");
                        }
                        return;
                    }
                    patternAndCollectionManager.deleteGrouping(vs, systemUser);
                }
            }
        }
        
        /**
         * This method "creates" the new grouping.
         */
        public void createGroup(CarsGroupingDtl groupDtl) throws Exception {
            patternAndCollectionManager.setContextParameters();
            
            if (log.isInfoEnabled()) {
                log.info("Creating Group");
            }
            
            GroupingParent parent = null;
            List<GroupingComponent> children = null;
            if (groupDtl != null) {
                if (groupDtl.getGrouping() != null) {
                    parent = groupDtl.getGrouping();
                }
                if (groupDtl.getComponent()!=null) {
                    children = groupDtl.getComponent(); 
                }
            }
            // Check if Parent Component exists.
            if (parent==null) {
                if (log.isErrorEnabled()) {
                    log.error("GroupingMessage is invalid.  Ignoring.");
                }
                return;  //ignore message if invalid data
            }
            
            VendorStyle parentVS = patternAndCollectionManager.getVendorStyleIfExists(parent.getId(), 
                    parent.getVendorNumber(), parent.getVendorStyle(), parent.getProductCode());
            if (parentVS!=null) {
                
                Car parentCar = carManager.getCarByVendorStyle(parentVS.getVendorStyleId());
                
                // check if it is a dummy record, continue to update.
                if (!VendorStyleType.PRODUCT.equals(parentVS.getVendorStyleType().getCode())
                        && Constants.DUMMY_PRODUCT_NAME.equals(parentVS.getVendorStyleName())) {
                    if (log.isInfoEnabled()) {
                        log.info("GroupCre message received for a dummy group.  Will proceed to create group: "
                                + parentVS.getVendorStyleNumber());
                    }
                } else if (Status.ACTIVE.equals(parentVS.getStatusCd()) && parentCar == null) {
                    if (log.isInfoEnabled()) log.info("GroupCre message was sent for an active group VendorStyle, but had no parent Car. Will proceed to create group: " 
                                    + parentVS.getVendorStyleNumber());
                }else {
                    if (Status.ACTIVE.equals(parentVS.getStatusCd())) {
                        if (log.isInfoEnabled()) {
                            log.info("Activation message was sent for an active grouping.  Ignoring Style_Number: " 
                                    + parentVS.getVendorStyleNumber());
                        }
                        return;  //ignore message if status is active.
                    }
                    activateGroup(parentVS); //if vendorStyle exists, this is an activate message.
                    return;
                }
            }
            
            // CODE will only get here if it is a new group or dummy group.
            // Check if Child Component(s) exist.
            if (children==null || children.isEmpty()) {
                if (log.isErrorEnabled()) {
                    log.error("GroupingMessage is invalid.  Ignoring.");
                }
                return;  //ignore message if invalid data
            }
            // Check if the PIM group type maps to one of the existing CARS group type.
            String carGroupType = patternAndCollectionManager.getCarGroupTypeByPimGroupType(parent.getGroupingType(), parent.getCARSGroupType());
            if (carGroupType==null) {
                if (log.isErrorEnabled()) {
                    log.error("PimGroupType did not return any match for carGroupTypes: " + parent.getGroupingType());
                }
                return;  //ignore message if group-type is invalid.
            }
            // Create Grouping
            if (VendorStyleType.OUTFIT.equals(carGroupType)) {
                patternAndCollectionManager.populateAndSaveCollection(parent, children, carGroupType, systemUser);
            }
            else {
                patternAndCollectionManager.populateAndSavePattern(parent, children, carGroupType, parentVS, systemUser);
            }
        }
        
        /**
         * This method "activates" product grouping.
         * @param parentVS
         */
        public void activateGroup(VendorStyle parentVS) {
            try {
                if (log.isInfoEnabled()) {
                    log.info("Activating Group");
                }
                
                parentVS.setStatusCd(Status.ACTIVE);
                carManager.save(parentVS);
                
                CarSearchCriteria criteria = new CarSearchCriteria();
                criteria.setVendorNumber(parentVS.getVendorNumber());
                criteria.setVendorStyleNumber(parentVS.getVendorStyleNumber());
                criteria.setStatusCd(Status.ACTIVE);
                criteria.setIgnoreUser(true);
                
                List<Car> patternCars = carManager.searchCars(criteria);
                for (Car car : patternCars) {
                    if (ContentStatus.SENT_TO_CMP.equals(car.getContentStatus().getCode()) ||
                            ContentStatus.DISABLE_IN_CMP.equals(car.getContentStatus().getCode())) {
                        car.setContentStatus(carLookupManager.getContentStatus(ContentStatus.ENABLE_IN_CMP));
                        car.setAuditInfo(systemUser);
                        carManager.saveOrUpdate(car);
                    }
                }
                if (log.isInfoEnabled()) {
                    log.info("Group Activated Successfully!  Style_Number: " + parentVS.getVendorStyleNumber());
                }
            }
            catch (Exception e) {
                if (log.isErrorEnabled()) {
                    log.error("Failed to activate grouping!!!  Style_Number: " + parentVS.getVendorStyleNumber(),e);
                }
            }
        }
        
    /**
     * This method "deactivates" product grouping.
     * @param parentVS
     */
    public void deactivateGroup(VendorStyle parentVS) {
        try {
            parentVS.setStatusCd(Status.INACTIVE);
            carManager.save(parentVS);
            
            CarSearchCriteria criteria = new CarSearchCriteria();
            criteria.setVendorNumber(parentVS.getVendorNumber());
            criteria.setVendorStyleNumber(parentVS.getVendorStyleNumber());
            criteria.setStatusCd(Status.ACTIVE);
            criteria.setIgnoreUser(true);
            
            List<Car> patternCars = carManager.searchCars(criteria);
            for (Car car : patternCars) {
                if (ContentStatus.SENT_TO_CMP.equals(car.getContentStatus().getCode()) ||
                        ContentStatus.RESEND_TO_CMP.equals(car.getContentStatus().getCode()) ||
                        ContentStatus.RESEND_DATA_TO_CMP.equals(car.getContentStatus().getCode()) ||
                        ContentStatus.ENABLE_IN_CMP.equals(car.getContentStatus().getCode())) {
                    car.setContentStatus(carLookupManager.getContentStatus(ContentStatus.DISABLE_IN_CMP));
                    car.setAuditInfo(systemUser);
                    carManager.saveOrUpdate(car);
                }
            }
        }
        catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("Failed to deactivate grouping!!!  Style_Number: " + parentVS.getVendorStyleNumber(),e);
            }
        }
       
    }

    /**
     * This method "modifies" the product grouping.  It checks for:
     *   1. any child components added/removed
     *   2. any updates to grouping-type
     *   3. any content/image updates
     * @param carsGroupDtl
     */
    private void modifyGroup(CarsGroupingDtl carsGroupDtl) {
        patternAndCollectionManager.setContextParameters();
        
        if (log.isInfoEnabled()) {
            log.info("Modifying Group");
        }
        
        GroupingParent parentGrouping = carsGroupDtl.getGrouping();
        List<GroupingComponent> components = carsGroupDtl.getComponent();
        
        VendorStyle parentStyle = patternAndCollectionManager.getVendorStyleIfExists(parentGrouping.getId(), 
                parentGrouping.getVendorNumber(), parentGrouping.getVendorStyle(), parentGrouping.getProductCode());
        
        if (parentStyle == null) {
            if (log.isErrorEnabled()) {
                log.error("Received a GroupMod request group that does not exist in CARS.  Group Id:" 
                        + parentGrouping.getId() + ".  Ignoring.");
            }
            return;
        }
    
        if (log.isInfoEnabled()) {
            log.info("Vendor Style of parent group: " + parentStyle.getVendorStyleNumber());
        }
        
        // update name/desc if changed
        patternAndCollectionManager.syncGroupingVendorStyle(parentStyle, parentGrouping);
        // update group type if changed
        String carGroupType = patternAndCollectionManager.syncGroupingType(parentStyle, parentGrouping, systemUser);
        
        // outfits/patterns have different data structures, thus require different methods to add/remove.
        if (VendorStyleType.OUTFIT.equals(carGroupType)) {
            // update effective date
            String date = parentGrouping.getEffectiveStartDate();
            patternAndCollectionManager.syncEffectiveStartDate(parentStyle, date, systemUser);
            patternAndCollectionManager.syncCollectionProducts(parentStyle, components, systemUser);
        } else {
            patternAndCollectionManager.syncPatternProducts(parentStyle, components, carGroupType, systemUser);
        }
        
    }
    
    public void saveGroupingFailureInDB(List<String> styleOrins,String groupingMessage) {
        patternAndCollectionManager.saveGroupingFailureInDB(styleOrins, groupingMessage);
    }
    
    private String getGroupingMessageForStyleOrin(String styleOrin) {
        return patternAndCollectionManager.getGroupingMessageForStyleOrin(styleOrin);
    }

    private void updateCarsPoMessageEsbRetry(List<String> orinsToRetry) {
    
        patternAndCollectionManager.updateCarsPoMessageEsbRetry(orinsToRetry);
    
    }
}