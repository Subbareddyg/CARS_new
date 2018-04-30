package com.belk.car.app.webapp.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.car.ManualCar;
import com.belk.car.app.model.car.ManualCarProcessStatus;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowType;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.ProductManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.to.IdbCarDataTO;
import com.belk.car.app.to.IdbCarSkuTO;
import com.belk.car.app.util.ReadIDBFile;
import com.belk.car.util.DateUtils;

public class ReadIDBDataController extends BaseController {

	private transient final Log log = LogFactory
			.getLog(ReadIDBDataController.class);
	private String filePath;
	private CarManager mgr;

	private WorkflowManager workflowMgr;
	
	private ProductManager productTypeMgr ;

	private AttributeManager attributeMgr ;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		File f = new File(filePath);
		Collection<IdbCarDataTO> col = ReadIDBFile.process(f);

		//User user = this.getLoggedInUser();
		String vendorContactEmail = "vendor@belk.com";
		Date escalationDate = this.getEscalationDate();
		//Date completionDate = this.getCompletionDate() ;
		int rec=0;
		WorkflowStatus initiated = (WorkflowStatus) this.getCarLookupManager().getById(WorkflowStatus.class, WorkflowStatus.INITIATED);
		UserType buyer = (UserType) this.getCarLookupManager().getById(UserType.class, UserType.BUYER);
		
		for (IdbCarDataTO idbCarTO : col) {
			if (log.isDebugEnabled())
				log.debug("Processing Record: " + (rec++) + "PO: " + idbCarTO.getPoNumber());
			if ("N".equals(idbCarTO.getValidItemFlag())) {
				if (log.isErrorEnabled())
					log.error("Error in retrieving Item Information from IDB") ;
				//Invalid Data
				if (StringUtils.isNotBlank(idbCarTO.getManualCarId())) {
					try {
						ManualCar mCar = (ManualCar) mgr.getFromId(ManualCar.class, Long.parseLong(idbCarTO.getManualCarId()));
						if (mCar != null) {
							mCar.setProcessStatus((ManualCarProcessStatus)mgr.getFromId(ManualCarProcessStatus.class, ManualCarProcessStatus.COMPLETED_WITH_ERROR));
							mgr.save(mCar);
						}
					} catch (Exception ex) {
						if (log.isErrorEnabled())
							log.error("Error in processing manual car: " + ex);
					}
				}
			} else {
				Car car = null ;
				ManualCar mCar = null;

				//Check if Department Exists
				Department dept = mgr.getDepartment(idbCarTO.getDepartmentNumber());
				List<String> errorList = new ArrayList<String>()  ;
				if (dept == null) {
					// Missing Department Do Not Process Data
					errorList.add("Department: " + idbCarTO.getDepartmentNumber() + " not setup in the database") ;
				}
				
				Classification classification = null;
				if (StringUtils.isBlank(idbCarTO.getClassNumber())) {
					errorList.add("Classification Is Null For: " + idbCarTO.getVendorStyle() + ", invalid data!!") ;
				} else {
					classification= mgr.getClassification(Short.parseShort(idbCarTO.getClassNumber()));
				}
				if (classification == null) {
					// Missing Classification Do Not Process Data
					errorList.add("Classification: " + idbCarTO.getClassNumber() + " not setup in the database") ;
				}
				
				//There are errors
				if (errorList.isEmpty()) {
					//Check if Vendor Exists
					Vendor vendor = mgr.getVendor(idbCarTO.getVendorNumber());
					
					if (vendor == null) {
						//Create a Vendor
						vendor = new Vendor();
						vendor.setName(idbCarTO.getVendorName());
						vendor.setVendorNumber(idbCarTO.getVendorNumber());
						vendor.setDescr(idbCarTO.getVendorName());
						vendor.setStatusCd(Status.ACTIVE);
						vendor.setContactEmailAddr(vendorContactEmail);				
						this.setAuditInfo(request, vendor);
						vendor.setIsDisplayable("Y");
						vendor = mgr.createVendor(vendor) ;
						
					}
					
					
					//Check if Vendor Style Exists
					VendorStyle vendorStyle = null;
					//No Need to check if VENDOR did not exist as there can be no VENDOR STYLE without VENDOR
					if (vendor.getVendorId() > 0) { 
						vendorStyle = mgr.getVendorStyle(idbCarTO.getVendorNumber(), idbCarTO.getVendorStyle());
					}
	
					if (vendorStyle == null) {
						//Create Vendor Style
						vendorStyle = new VendorStyle() ;
						vendorStyle.setVendorNumber(idbCarTO.getVendorNumber());
						vendorStyle.setVendorStyleName(idbCarTO.getVendorStyleDescription());
						vendorStyle.setVendorStyleNumber(idbCarTO.getVendorStyle());
						vendorStyle.setDescr(""); //change to <blank> based on the bug idbCarTO.getVendorStyleDescription()
						vendorStyle.setStatusCd(Status.ACTIVE);
						
						this.setAuditInfo(request, vendorStyle);
		
						vendorStyle.setVendor(vendor);
						vendorStyle.setClassification(classification);
						
						vendorStyle = mgr.createVendorStyle(vendorStyle);
					} else {
						//Check if there is an open CAR for the VENDOR_STYLE
						car = mgr.getCarByVendorStyle(vendorStyle.getVendorStyleId());
					}
					
					//Create CAR only if there are no CAR for the VENDOR_STYLE
					if (car == null) {
						car = new Car() ;
						car.setDepartment(dept) ;
						SourceType sourceType = null ;
						if (StringUtils.isNotBlank(idbCarTO.getManualCarId())) {
							try {
								car.setSourceType(mgr.getSourceTypeForCode(SourceType.MANUAL));
								mCar = (ManualCar) mgr.getFromId(ManualCar.class, Long.parseLong(idbCarTO.getManualCarId()));
								car.setSourceId(mCar.getCreatedBy());
							} catch(Exception ex) {
								car.setSourceId("Manual Car: " + idbCarTO.getManualCarId());
							}
						} else if (StringUtils.isNotBlank(idbCarTO.getPoNumber())){
							car.setSourceType(mgr.getSourceTypeForCode(SourceType.PO));
							car.setSourceId(idbCarTO.getPoNumber());
						} else {
							car.setSourceType(mgr.getSourceTypeForCode(SourceType.FINE_JEWELRY));
							//car.setSourceId(idbCarTO.getPoNumber()); //Currently PO Number is Blank for Fine Jewelry
							car.setSourceId(idbCarTO.getVendorNumber()+"-"+idbCarTO.getVendorStyle());
						}
						car.setVendorStyle(vendorStyle) ;
						car.setWorkFlow(workflowMgr.getWorkFlow(workflowMgr.getWorkflowType(WorkflowType.CAR)));
						car.setCarUserByLoggedByUserId(this.getLoggedInUser());
						//car.setCarUserByAssignedToUserId(carUserByAssignedToUserId)
						car.setAssignedToUserType(buyer);
						car.setCurrentWorkFlowStatus(initiated);
						
						//Set the Initial Escalation Date to 7 Days...
						//Need to figure out where we should default the 7 Days from i.e Transition OR Config
						car.setEscalationDate(escalationDate);
	
						car.setIsUrgent(Constants.FLAG_NO);
						
						if (StringUtils.isNotBlank(idbCarTO.getExpectedShipDate())) {
							car.setExpectedShipDate(DateUtils.parseDate(idbCarTO.getExpectedShipDate(), "yyyy-MM-dd"));
							car.setDueDate(DateUtils.parseDate(idbCarTO.getDueDate(), "yyyy-MM-dd"));
						} else {
							Date currentDate = new Date();
							car.setExpectedShipDate(DateUtils.parseDate(idbCarTO.getExpectedShipDate(), "yyyy-MM-dd"));
							car.setDueDate(DateUtils.parseDate(idbCarTO.getDueDate(), "yyyy-MM-dd"));
						}
						String isProductTypeReq = Constants.FLAG_NO ;
						List<ProductType> l = productTypeMgr.getProductTypes(classification.getClassId());
						if (l != null && !l.isEmpty()) {
							isProductTypeReq = Constants.FLAG_YES;
						}
						car.setIsProductTypeRequired(isProductTypeReq);
						car.setStatusCd(Status.ACTIVE);
						this.setAuditInfo(request, car);
						
						car = mgr.createCar(car) ;
	
						//Create CAR/CAR SKU Attributes
						String arr[] = { "SKU_COLOR", "SKU_COLOR_NAME", "SKU_SIZE_CODE", "SKU_SIZE" };
						for (IdbCarSkuTO idbSku : idbCarTO.getSkuInfo()) {
							VendorSku sku = mgr.getSku(idbSku.getBelkUPC()) ;
							
							if(sku == null) { 
								sku = new VendorSku();
								sku.setCar(car);
								sku.setBelkSku(idbSku.getBelkUPC());
								sku.setLongSku(idbSku.getLongSku());
								sku.setBelkUpc(idbSku.getBelkUPC());
								sku.setStatusCd(Status.ACTIVE);
								this.setAuditInfo(request, sku) ;
		
								sku = mgr.createVendorSku(sku) ;
								
								String attrValue[] = {idbSku.getVendorColor(), idbSku.getVendorColorName(), idbSku.getVendorSizeCode(), idbSku.getVendorSizeDesc()};
								for (int i = 0; i < arr.length; i++) {
									Attribute attr = attributeMgr.getAttribute(arr[i]);
									CarSkuAttribute csa = new CarSkuAttribute();
									csa.setAttribute(attr);
									csa.setVendorSku(sku);
									csa.setAttrValue(attrValue[i]);
									this.setAuditInfo(request, csa);
									
									mgr.createCarSkuAttribute(csa) ;
									
									//addCarSkuAttribute(mgr.getAttributeForName(arr[i]), vendorSku, user
									//		.getUsername());
								}
							}
						}
						
						if (car != null) {
							mgr.doCarAttributes(
									new Long(car.getCarId()),
									new Long(car.getVendorStyle().getClassification()
											.getClassId()),
									new Long(car.getDepartment().getDeptId()), new Long(0));
						}

						if (mCar != null) {
							try {
								mCar.setProcessStatus((ManualCarProcessStatus)mgr.getFromId(ManualCarProcessStatus.class, ManualCarProcessStatus.COMPLETED));
								mCar.setPostProcessInfo("Car Created With ID: " + car.getCarId());
								mgr.save(mCar);
							} catch (Exception ex) {
								log.error("Error in processing manual car: " + ex);
							}
						}

					} else {
						if (mCar != null
								&& mCar
										.getProcessStatus()
										.getStatusCd()
										.equals(ManualCarProcessStatus.EXPORTED)) {
							try {
								mCar.setProcessStatus((ManualCarProcessStatus)mgr.getFromId(ManualCarProcessStatus.class, ManualCarProcessStatus.COMPLETED_WITH_ERROR));
								mCar.setPostProcessInfo("Duplicate Car would have been created!!!");
								mgr.save(mCar);
							} catch (Exception ex) {
								log.error("Error in processing manual car: " + ex);
							}

						}
					}
					
					//if (runAttributes) {
					//	doCarAttributes(car.getCarId(), classification.getClassId(),
					//			classification.getDepartment().getDeptId(), null);
					//}
				}
			}
			
			log.debug("!!!Completed Import!!!");
			//return car;

			//Car car = this.getMgr().createCar(to, sourceType, user);
		}
		
		return new ModelAndView(this.getSuccessView());
	}
	
	public Date getEscalationDate() {
		int initNumOfEscalationDays = 7;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, initNumOfEscalationDays) ;
		return cal.getTime();
	}

	public Date getCompletionDate() {
		int initNumOfDaysAfterStart = 70;
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, initNumOfDaysAfterStart) ;
		return cal.getTime();
	}

	public void setMgr(CarManager mgr) {
		this.mgr = mgr;
	}

	public void setWorkflowMgr(WorkflowManager workflowMgr) {
		this.workflowMgr = workflowMgr;
	}

	public void setAttributeMgr(AttributeManager attributeMgr) {
		this.attributeMgr = attributeMgr;
	}

	public void setProductTypeMgr(ProductManager productTypeMgr) {
		this.productTypeMgr = productTypeMgr;
	}
	

}
