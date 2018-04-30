
package com.belk.car.app.webapp.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.belk.car.app.Constants;
import com.belk.car.app.dto.CarsDTO;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.CarSample;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.DBPromotionCollectionSkus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.SkuAttributeDelete;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuDelete;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStylePIMAttribute;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.WorkflowTransitionInfo;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.CatalogImportManager;
import com.belk.car.app.service.DBPromotionManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.util.JSONUtil;
import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.util.DateUtils;

public class DashBoardController extends BaseController {
	private static final String DETAILCAR = "detailCar";
	private static final String VENDORCONTACTS = "vendorContacts";
	
	private transient final Log log = LogFactory
			.getLog(DashBoardController.class);

	private WorkflowManager workflowManager;
	private CatalogImportManager catalogImportManager;
	
	/**
	 * @param workflowManager
	 *            the workflowManager to set
	 */
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public void setCatalogImportManager(CatalogImportManager catalogImportManager) {
		this.catalogImportManager = catalogImportManager;
	}

	private DBPromotionManager dbPromotionManager;
	
	
	public DBPromotionManager getDbPromotionManager() {
		return dbPromotionManager;
	}

	public void setDbPromotionManager(DBPromotionManager dbPromotionManager) {
		this.dbPromotionManager = dbPromotionManager;
	}
	

	private void doEditCarRequestAttributes(Car car,
			HttpServletRequest request, User user, Image image, String editParam) {
		log.debug("Inside doEdit Cars REquest Attributes.");
		long eStartTime = System.currentTimeMillis();
		request.setAttribute(editParam, image);
		request.setAttribute("user", user);
		request.setAttribute("detailCar", car);
		request.setAttribute("vendorContacts", this.carManager.getUsersForVendorAndDept(car.getVendorStyle().getVendor().getVendorId(),
				                                                                 car.getDepartment().getDeptId()));

		if (user.getUserType().getUserTypeCd().equals(UserType.BUYER)) {
		}
		
		if (car !=null) {

			WorkflowStatus currentStatus = car.getCurrentWorkFlowStatus();
			UserType currentUserType = car.getAssignedToUserType();

			WorkflowTransitionInfo wti = workflowManager
					.getNextWorkFlowTransitionInformation(currentUserType.getUserTypeCd(),
							currentStatus.getStatusCd());

			request.setAttribute("workflowTransition", wti);

			String loggedInUserType = getLoggedInUser().getUserTypeCd();
			if (loggedInUserType.equals(UserType.BUYER)) {
				WorkflowTransitionInfo wti2 = workflowManager
						.getNextWorkFlowTransitionInformation(loggedInUserType,
								currentStatus.getStatusCd());
				request.setAttribute("webMerchantWorkflowTransition", wti2);
			}
		}
		if (log.isDebugEnabled())
			log.debug("Car Edit doEditCarRequestAttributes: " + (System.currentTimeMillis() - eStartTime) + " ms");
	}

	/**
	 * 
	 */
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("entering dashBoard 'handleRequest'");
		}
		HttpSession session = request.getSession();
		

		String rCar = request.getParameter("review");
		String editCarParam = request.getParameter("car");
		String param = request.getParameter("param");
		String urgentFlag = request.getParameter("urgFlg");
		String productTypeId = request.getParameter("productType");
		String imageId = request.getParameter("image");
		String imageReqId = request.getParameter("imageReq");
		String vu=request.getParameter("vu");
		String oper=request.getParameter("oper");
		Long l = null;
		User user = this.getLoggedInUser();
		
        if (user.isVendor()) {
            // For valid vendor users that logged-in, redirect Vendor to use new portal.
            Map<String,String> model = new HashMap<String,String>();
            model.put("vendor", "true");
            return new ModelAndView(new RedirectView("logout.jsp"), model);
        }

		if (StringUtils.isNotBlank(vu) && StringUtils.isNotBlank(oper)){
		  Long userId=Long.parseLong(vu);
		  User vendorUser=(User)carManager.getFromId(User.class, userId);
		  if (oper.equals("del")){
			  log.debug("operation is delete.");
			  // Vendor Contact Should not be INACTIVATED
			  // Vendor Contact Should be removed for processing the Vendor Information
			 

			  Car car = this.carManager.getCarFromId(new Long(editCarParam));
			  Vendor carVendor= car.getVendorStyle().getVendor() ;
			  Department dept = car.getDepartment() ;
			  if (vendorUser.getVendors() != null && !vendorUser.getVendors().isEmpty()) {
				  if (vendorUser.getVendors().contains(car.getVendorStyle().getVendor())) {
					  vendorUser.getVendors().remove(car.getVendorStyle().getVendor()) ;
					  this.carManager.saveUser(vendorUser);
				  } 
			  }			  
			  request.setAttribute(VENDORCONTACTS, this.carManager.getUsersForVendorAndDept(carVendor.getVendorId(), dept.getDeptId()));
			  request.setAttribute(DETAILCAR, car);
			  return new ModelAndView("vendorManagement"); 

		  } else if (oper.equals("edit")){
			  log.debug("Operation is Edit");
			 
			  request.setAttribute("user", vendorUser);
			  return new ModelAndView("vendorUserForm");
		  }
		}
		if (StringUtils.isNotBlank(editCarParam)) {
			log.debug("Edit Car param is not blank.");
			l = new Long(editCarParam);
			if ("delSku".equals(param)) {
				log.debug("parameter is delsku");
				
				if (l != null) {
					long eStartTime = System.currentTimeMillis();
					Car editCar = (Car) carManager.getCarFromId(l);
					if (log.isDebugEnabled())
						log.debug("Car Retreival Time: " + (System.currentTimeMillis() - eStartTime) + " ms");
	
					eStartTime = System.currentTimeMillis();
					long skuId = Long.parseLong(request.getParameter("skuId"));
					log.info("Deleted SkuId: "+ skuId);
					Set<VendorSku> skus = editCar.getVendorSkus() ;
					if (skus != null) {
						Set<VendorSku> removeSkus = new HashSet<VendorSku>();
					//	Set<VendorSku> deletedSkus = new HashSet<VendorSku>();
						for (VendorSku sku : skus) {
							if (sku.getCarSkuId() ==  skuId) {
								// Track delete color code in CarNote section - start here
								String trackDeleteColorCode = sku.getColorCode();
								VendorStyle tempVendorStyle = sku.getVendorStyle();					
								long vendorStyleId=tempVendorStyle.getVendorStyleId();
								String styleNumber = sku.getVendorStyle().getVendorStyleNumber();
								log.debug("Vendor Style Number: "+styleNumber);
								if(trackDeleteColorCode!= null){
									CarNote carNote = new CarNote();
									String deleteColorCode = "Style "+styleNumber+": NRF "+ trackDeleteColorCode+" deleted";
									log.debug("CarNote for deleted color code: " + deleteColorCode);
									long carID = sku.getCar().getCarId();
									log.debug("Car ID : " + carID);
									//checking same color code existing or not 
									List<VendorSku> vendorSkuList = this.carManager.getColorCodeForStyle(carID, trackDeleteColorCode,vendorStyleId);
									if(!vendorSkuList.isEmpty() && vendorSkuList.size() == 1) {
										NoteType carNoteType = (NoteType)this.getCarLookupManager().getById(NoteType.class, NoteType.CAR_NOTES);
										Car car = this.getCarManager().getCarFromId(carID);
										carNote.setIsExternallyDisplayable(CarNote.FLAG_NO);
										carNote.setCar(car);			
										carNote.setNoteType(carNoteType);
										carNote.setNoteText(deleteColorCode);
										carNote.setStatusCd(Status.ACTIVE);
										carNote.setCreatedBy(user.getUsername());
										carNote.setUpdatedBy(user.getUsername());
										carNote.setCreatedDate(new Date());
										carNote.setUpdatedDate(new Date());
										car.getCarNotes().add(carNote);
							            getCarManager().save(car);
									}
								}
								removeSkus.add(sku);
								//deletedSkus.add(sku);
								break ;
							}
						}
						if (!removeSkus.isEmpty()) {
							// copy the deleted sku data
							copySkuData(removeSkus,request);
							editCar.getVendorSkus().removeAll(removeSkus);
							editCar.resetVendorStyles();
							this.carManager.save(editCar) ;
						}
					}

					if (log.isDebugEnabled())
						log.debug("Car Delete SKU: " + (System.currentTimeMillis() - eStartTime) + " ms");
					return new ModelAndView("redirect:editCarForm.html?carId="
							+ editCar.getCarId()+"&delSku=true#sku_pnl");
				}
			}

			if (StringUtils.isNotBlank(imageId)) {
				if (log.isDebugEnabled())
					log.debug("getting detail Car(imageEdit)=" + l);
				if (l != null) {
					Car car = (Car) carManager.getCarFromId(l);
					Long imageIdNum = new Long(imageId);
					Image image = (Image) carManager
							.getFromId(Image.class, imageIdNum);
					doEditCarRequestAttributes(car, request, user, image,
							"editImage");
					return new ModelAndView("carEdit");
				}
			}
			if (StringUtils.isNotBlank(imageReqId)) {
				if (log.isDebugEnabled())
					log.debug("getting detail Car(imageReqEdit)=" + l);
				if (l != null) {
					Car car = (Car) carManager.getCarFromId(l);
					Long imageIdNum = new Long(imageReqId);
					Image image = (Image) carManager
							.getFromId(Image.class, imageIdNum);
					doEditCarRequestAttributes(car, request, user, image,
							"reqImageNote");
					return new ModelAndView("carEdit");
				}
			}

			if (log.isDebugEnabled())
				log.debug("getting detail Car("+param+")=" + l);
			if ("productType".equals(param)) {
				log.debug("param is product type");
				if (l != null) {
					try {
						Car car = (Car) carManager.getCarFromId(l);
						request.setAttribute("jsonObj", 
								JSONUtil.convertProductTypesToJSON(this.carManager.getPossibleProductTypesForCar(car)));
					} catch (Exception e) {
						log.debug("Failed in get: " + e.getMessage());
						request.setAttribute("jsonObj", "{\"success\":false}");
					}
				} else {
					request.setAttribute("message", "failure:Bad parameter");
				}
				return new ModelAndView("ajaxReturn");
			}
			
			if ("resetProductType".equals(param)) {
				log.debug("param is resetProductType ");
				if (l != null) {
					try {
						Car car = (Car) carManager.getCarFromId(l);
						car.getVendorStyle().setProductType(null);
						VendorStyle vs = car.getVendorStyle();
						List<VendorStylePIMAttribute> vspaList = new ArrayList<VendorStylePIMAttribute>();
						if(vs.isPattern()){
						    List<VendorStyle> vsList = car.getVendorStyles();
						    for(VendorStyle vStyle : vsList){
						        Set<VendorStylePIMAttribute> vspaSet = vStyle.getVendorStylePIMAttribute();
						        vspaList.addAll(vspaSet);
						        vStyle.getVendorStylePIMAttribute().removeAll(vspaSet);
						    }
						}else{
						    Set<VendorStylePIMAttribute> vspaSet = vs.getVendorStylePIMAttribute();
						    vspaList.addAll(vspaSet);
						    vs.getVendorStylePIMAttribute().removeAll(vspaSet);
						}
						
						
						if(vspaList!=null && !vspaList.isEmpty()){
						    this.carManager.deletevspalist(vspaList);
						}
						Set<VendorSku> vSkus = vs.getVendorSkus();
						List<VendorSkuPIMAttribute> vskupaList = new ArrayList<VendorSkuPIMAttribute>();
						for(VendorSku vSku:vSkus){
						    Set<VendorSkuPIMAttribute> vskupaSet = vSku.getSkuPIMAttributes();
						    vskupaList.addAll(vskupaSet);
						    vSku.getSkuPIMAttributes().removeAll(vskupaSet);
						}
						if(vskupaList!=null && !vskupaList.isEmpty()){
						    this.carManager.deletevskupalist(vskupaList);
						}
						carManager.save(car) ;
					} catch (Exception e) {
						log.debug("Failed in get: " + e.getMessage());
						request.setAttribute("jsonObj", "{\"success\":false}");
					}
				}
				return new ModelAndView(this.getSuccessView());
			}
			if ("productTypeApply".equals(param)) {
				log.debug("param is productTypeApply ");
				Long ptId = new Long(productTypeId);
				if (l != null) {
					try {
						Car car = (Car) carManager.getCarFromId(l);
						ProductType productType = (ProductType) carManager.getFromId(
								ProductType.class, ptId);
						car.setIsProductTypeRequired(Constants.FLAG_NO) ;
						if (productType != null) {
							car.getVendorStyle().setProductType(productType);
						}
						
						this.carManager.resyncAttributes(car, this.getLoggedInUser()) ;

						
						//See if We need to copy Attribute Values from Catalog
						CatalogProduct product = null ;
						//Checking for product information in Catalog
						if (car.getVendorStyle().isPattern()) {
							//Find choice based on the first Product with Content within a Pattern
							VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
							criteria.setVendorStyleId(car.getVendorStyle().getVendorStyleId()) ;
							criteria.setChildrenOnly(true);
							if (log.isDebugEnabled())
								log.debug("Pattern - Search for child product information ");
							List<VendorStyle> childProducts = this.carManager.searchVendorStyle(criteria);
							if (childProducts != null) {
								for(VendorStyle childProduct : childProducts) {
									if (log.isDebugEnabled())
										log.debug("Pattern - Searching for product information in catalog " + childProduct.getVendorStyleNumber());
									product = catalogImportManager.getProduct(childProduct.getVendorNumber(), childProduct.getVendorStyleNumber());
									if (product != null) {
										break ;
									}
								}
							}
							
						} else {
							if (log.isDebugEnabled())
								log.debug("Product - Searching for product information in catalog " + car.getVendorStyle().getVendorStyleNumber());
							product = catalogImportManager.getProduct(car.getVendorStyle().getVendorNumber(), car.getVendorStyle().getVendorStyleNumber());
							
						}

						if (product == null) {
							//check based on sku
							CatalogSku sku = null ;
							if (car != null && car.getVendorSkus() != null && !car.getVendorSkus().isEmpty()) {
								catalogImportManager.getSku(car.getVendorSkus().iterator().next().getVendorUpc());
							}

							if (sku != null) {
								product = sku.getCatalogProduct() ;
							}
						}

						if (product != null && product.getCatalogProductId() != 0 && car.getVendorStyle().getProductType() != null) {
							catalogImportManager.copyToCar(product, car) ;
						}
						carManager.save(car) ;
						
						JSONObject object = new JSONObject();
						object.put("contentStatus", car.getContentStatus().getCode());
						object.put("userTypeCd", user.getUserType().getUserTypeCd());
						object.put("success", true);
	
						request.setAttribute("jsonObj", object);
					} catch (Exception e) {
						JSONObject object = new JSONObject();
						object.put("success", false);
						request.setAttribute("jsonObj", object);
					}
				} else {
					JSONObject object = new JSONObject();
					object.put("success", false);
					request.setAttribute("jsonObj", object);
				}
				return new ModelAndView("ajaxReturn");
			}
			if ("setUrgFlg".equals(param)) {
				if (l != null) {
					try {
						Car editCar = (Car) carManager.getCarFromId(l);
						String urgentFlagVal = "N";
						if (urgentFlag != null && !urgentFlag.equals("")) {
							if (urgentFlag.equals("urg")) {
								urgentFlagVal = "Y";
							}
							editCar.setIsUrgent(urgentFlagVal);
							this.carManager.updateCar(editCar, user.getUsername());
							request.setAttribute("jsonObj",
									"{\"success\":true}");
						}
					} catch (Exception e) {
						request.setAttribute("jsonObj", "{\"success\":false}");
					}
				} else {
					request.setAttribute("jsonObj", "{\"success\":false}");
				}
				return new ModelAndView("ajaxReturn");
			}
			
			
			if ("edit".equals(param)) {
				if (l != null) {
					long eStartTime = System.currentTimeMillis();
					Car editCar = (Car) carManager.getCarFromId(l);
					Map<String,String>  backupMap = getCarManager().getBackupAttrVals(editCar.getCarAttributes());
					request.getSession().setAttribute("backUpCarBeforeEdit", backupMap);
					if (log.isDebugEnabled())
						log.debug("Car Retreival Time: " + (System.currentTimeMillis() - eStartTime) + " ms");
	
					eStartTime = System.currentTimeMillis();
					
					if(!editCar.isLocked() || editCar.getCarUserByAssignedToUserId().getUsername().equals(user.getUsername())){
						editCar.setLock("Y");
						editCar.setCarUserByAssignedToUserId(user);
						this.carManager.updateCar(editCar, user.getUsername());	
					}else{
						Map<String, Object> model = new HashMap<String, Object>();
						List<CarsDTO> list = carManager.getCarsForUser(null,user);
						model.put("DashBoard", list);
						model.put("userTypeCd", user.getUserType().getUserTypeCd());
						return new ModelAndView("dashBoard", model);
					}
					if (log.isDebugEnabled())
						log.debug("Car Check User Time: " + (System.currentTimeMillis() - eStartTime) + " ms");
					
					
					eStartTime = System.currentTimeMillis();
					//Commented this code, it is retrieved in the editCarForm anyway..
					//doEditCarRequestAttributes(editCar, request, user, null,
					//		"requestEdit");
					if (log.isDebugEnabled())
						log.debug("Car Edit Request Time: " + (System.currentTimeMillis() - eStartTime) + " ms");
				     return new ModelAndView("redirect:editCarForm.html?carId="
							+ editCar.getCarId());
				}

			}
			if ("resetSample".equals(param)) {
				if (l != null) {
					long eStartTime = System.currentTimeMillis();
					Car editCar = (Car) carManager.getCarFromId(l);
					if (log.isDebugEnabled())
						log.debug("Car Retreival Time: " + (System.currentTimeMillis() - eStartTime) + " ms");
	
					eStartTime = System.currentTimeMillis();

					editCar.getCarSamples().clear() ;
					carManager.save(editCar) ;
					
					if (log.isDebugEnabled())
						log.debug("Car Reset Sample Time: " + (System.currentTimeMillis() - eStartTime) + " ms");
					return new ModelAndView("redirect:editCarForm.html?carId="
							+ editCar.getCarId());
				}
			}
		} else if (rCar != null && !rCar.equals("")) {
			Long carId = new Long(rCar);
			if (log.isDebugEnabled())
				log.debug("getting selected Car=" + carId);
			if (carId != null) {
				Car reviewCar = (Car) carManager.getCarFromId(carId);
				
				//Ec:1553-Added the code for showing the colorName  under preview section ** */
				Map<String, VendorSku> colorNameMap = new HashMap<String, VendorSku>() ;
				for (VendorSku sku: reviewCar.getVendorSkus()) {
					String vendorColor = sku.getVendorStyle().getVendorStyleNumber() + "_" + sku.getColorCode(); 
					if (StringUtils.isNotBlank(sku.getColorCode())) {
						if (!colorNameMap.containsKey(vendorColor)) {
							colorNameMap.put(vendorColor, sku);
						}
					}
				}
				for (CarSample carSample : reviewCar.getCarSamples()) {
					String color = carSample.getSample().getVendorStyle().getVendorStyleNumber() + "_" + carSample.getSample().getSwatchColor();
					if (colorNameMap.containsKey(color)) {
						carSample.getSample().setColorName(colorNameMap.get(color).getColorName()) ;
					} else {
						carSample.getSample().setColorName("Not-Applicable");
					}
				}
				//Ec:1553-End of added code for showing the colorName under preview section **/
				
				//Added By Priyanka Gadia as part of CARS ENHANCEMENT
				//Added the code for showing pattern and their child images under image naming convention header. 
				List<VendorStyle> vendorStyles = new ArrayList<VendorStyle>() ;
				vendorStyles.add(reviewCar.getVendorStyle()) ;
				if (reviewCar.getVendorStyle().isPattern()) {
					VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria() ;
					criteria.setChildrenOnly(true) ;
					criteria.setVendorStyleId(reviewCar.getVendorStyle().getVendorStyleId());
					vendorStyles.addAll(this.carManager.searchVendorStyle(criteria)) ;
				} 
				request.setAttribute("vendorStyles", vendorStyles) ;
				reviewCar.setTemplateType(getTemplateTypeVal(reviewCar)); ///code for Deal Based Management
				
				//request.removeAttribute("viewChildCars");
				Map<String, Object> model = new HashMap<String, Object>();
				List<ChildCar> viewChildCars = null;
				if(reviewCar.getSourceType().getSourceTypeCd().equals(SourceType.PYG)) {
					viewChildCars =  getDBPromotionChildForDisplay(request,reviewCar);
					request.setAttribute("viewChildCars", viewChildCars);
				  }
				model.put("selectedCarOnPage", reviewCar);
				model.put("user", user);
				return new ModelAndView("carDetail", model);
			}
		}
		Map<String, Object> model = new HashMap<String, Object>();
		List<CarsDTO> list = new ArrayList<CarsDTO>();
		model.put("DashBoard", list);
		model.put("userTypeCd", user.getUserType().getUserTypeCd());
		model.put("user",user);
		
		return new ModelAndView("dashBoard", model);
	}
	
	/*
	 * copying the deleted sku data into temp table 
	 * for copy Complete CAR details
	 */
	
	private void copySkuData(Set<VendorSku> Skus,HttpServletRequest request){
	
		    log.debug("inside copy sku data to temp table");
			for(VendorSku vs:Skus){
				VendorSkuDelete vsd=new VendorSkuDelete();
				vsd.setCarSkuId(vs.getCarSkuId());
				vsd.setBelkUpc(vs.getBelkUpc());
				vsd.setCarId(vs.getCar().getCarId());
				vsd.setStatusCd(Status.ACTIVE);
				this.setAuditInfo(request, vsd);
				carManager.save(vsd);
				//Set<SkuAttributeDelete> skds=new HashSet<SkuAttributeDelete>();
				Set<CarSkuAttribute> csa=vs.getCarSkuAttributes();
				if(csa!=null){
					for(CarSkuAttribute cs:csa){
						SkuAttributeDelete skd=null;
						skd=new SkuAttributeDelete();
						skd.setCarSkuId(vsd.getCarSkuId());
						skd.setAttribute(cs.getAttribute());
						skd.setAttrValue(cs.getAttrValue());
						this.setAuditInfo(request, skd);
						carManager.save(skd);
						//skds.add(skd);
					}
				}
			}
		log.debug("copy sku data done to temp table");
	}
	private String getTemplateTypeVal(Car car){
		Attribute templateType = new Attribute(); ;
		String attrVal ="";
		if(car.getSourceType().getSourceTypeCd().equals(SourceType.PYG)){
			templateType = getDbPromotionManager().getAttributeByName(Constants.DBPROMOTION_TEMP_TYPE);
			attrVal = getDbPromotionManager().getAttributeValue(car,templateType);
		}
		System.out.println("attrVal on DashboardController:"+attrVal);
		return attrVal;
	}
	
	public List<ChildCar> getPromotionChildCarSkus(Set<CarDBPromotionChild> cardbPromotionChild)
	 {	
		if (log.isDebugEnabled()) {
			 log.debug("Inside getChildCarSkus method");
		}
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
		List<Car> skuCar=new ArrayList<Car>();	
		try
		{
		for(CarDBPromotionChild dbPromotionChild : cardbPromotionChild){
			if(dbPromotionChild.getStatusCd().equals(Status.ACTIVE)){
				Car childCar=dbPromotionChild.getChildCar();
					VendorStyle vendorStyle = childCar.getVendorStyle();	
			skuCar = getDbPromotionManager().getChildCarSkus(vendorStyle);
				for(Car childCarSku:skuCar){
					Set<VendorSku> carVendorSku=childCarSku.getVendorSkus();
					String styleNumStr = childCarSku.getVendorStyle().getVendorStyleNumber();
					for(VendorSku vSku:carVendorSku){
						ChildCar childCarSkuList= new ChildCar();
						childCarSkuList.setSkuCarid(childCarSku.getCarId());
						childCarSkuList.setCompDt(DateUtils.formatDate(childCarSku.getDueDate()));
						childCarSkuList.setVendorName(vSku.getVendorStyle().getVendor().getName());
						childCarSkuList.setVendorStyle(styleNumStr);
						childCarSkuList.setSkuStyleName(vSku.getVendorStyle().getVendorStyleName());						
						childCarSkuList.setVendorUpc(vSku.getBelkUpc());
						childCarSkuList.setSkuColor(vSku.getColorCode());
						childCarSkuList.setColorName(vSku.getColorName());
						childCarSkuList.setSizeName(vSku.getSizeName());
						childCarSkuList.setSkuID(vSku.getBelkSku());
						childCarSkuList.setBelkSku(vSku.getBelkSku());
						viewChildCarsSkuList.add(childCarSkuList);
						//Collections.sort(viewChildCarsSkuList,new SortingComparator());
					}
			}
		}
		}
		}catch(Exception e){log.error("Exception in getChildCarSkus method ",e);}
		return viewChildCarsSkuList;
	}
	
	
	public  List<ChildCar>  getDBPromotionChildForDisplay(HttpServletRequest request ,Car dbPromotionCar){
		List<ChildCar> viewChildCars=new ArrayList<ChildCar>();
		Set<CarDBPromotionChild> dbPromotionChildCars = dbPromotionCar.getCarDBPromotionChild();
		   for (CarDBPromotionChild carDBPromotionChild: dbPromotionChildCars) {
			   if(carDBPromotionChild.getStatusCd().equals(Status.ACTIVE)){
			        Car childCarTemp = carDBPromotionChild.getChildCar();
			        ChildCar childCar= new ChildCar();
			        childCar.setCarId(childCarTemp.getCarId());
			        childCar.setStyleNumber(childCarTemp.getVendorStyle().getVendorStyleNumber());
			        childCar.setProductName(childCarTemp.getVendorStyle().getVendorStyleName());
			        childCar.setOrder(carDBPromotionChild.getPriority());
			        childCar.setSku(carDBPromotionChild.getDefaultColorSku().getColorName());
			        CarAttribute carAttr= getDbPromotionManager().getCarAttributeByBMName(childCarTemp,"Brand");
			        if(carAttr == null){
			        	childCar.setBrandName("");
			        }else{
			        	childCar.setBrandName(carAttr.getAttrValue());
			        }
			        viewChildCars.add(childCar);
			   }
		   }
		  return viewChildCars;
	}
}
