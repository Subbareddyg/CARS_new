/**
 * 
 */
package com.belk.car.app.webapp.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarImageId;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CarSample;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.model.ContentStatus;
import com.belk.car.app.model.DBPromotionCollectionSkus;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.HtmlDisplayType;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageLocationType;
import com.belk.car.app.model.ImageProvider;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.Sample;
import com.belk.car.app.model.SampleSourceType;
import com.belk.car.app.model.SampleTrackingStatus;
import com.belk.car.app.model.SampleType;
import com.belk.car.app.model.ShippingType;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.VendorStyleType;
import com.belk.car.app.model.WorkflowTransitionInfo;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.model.vendorimage.VendorImageStatus;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.model.workflow.WorkflowTransition;
import com.belk.car.app.service.AttributeManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.DBPromotionManager;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.service.SizeConversionManager;
import com.belk.car.app.service.SuperColorManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.util.SizeConversionRulesUtils;
import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.util.DateUtils;
import com.belk.car.util.GenericComparator;

public class DashBoardFormController extends BaseFormController {
	private transient final Log log = LogFactory.getLog(DashBoardFormController.class);
	private static SampleSourceType onHand;
	private static SampleSourceType vendor;
	private static ImageProvider rrDonnelly;
	private static ImageProvider pineville;
	private static SampleType sampleProduct;
	private static ImageTrackingStatus imageReceived;
	private static SampleType sampleSwatch;

	private static final Long RRDONNELLY = 1L;
	private static final Long PINEVILLE = 2L;
	private static final String OHSAMPLE = "OHsample";
	private static final String OHSWATCH = "OHswatch";
	private static final String FVSAMPLE = "FVsample";
	private static final String FVSWATCH = "FVswatch";

	private static final String PARAMCARID = "carId";
	private static final String CARATTRIBUTE = "carAttribute:";
	private static final String VENDORSTYLEDESC = "vendorStyle.Descr:";
	private static final String VENDORSTYLENAME = "vendorStyle.Name:";
	private static final String IMAGEPROVIDER = "imageProvider";
	private static final String SAMPLE = "sample:";
	private static final String SAMPLESHIPDATE = "sampleShipDate:";
	private static final String HIDESAMPLESHIPDATE = "hidesampleShipDate:";
	private static final String SELECTCRITERIA = "01/01/1900";
	private static final String SELECTDATE = "Select Date";
	private static final String MUSTBERETURNED = "mustBeReturned:";
	private static final String SILHOUETTEREQ="silhouetteReq:";
	private static final String TRACKINGSTATUS = "trackingStatus:";

	private static final String EMPTYSTRING = "";
	private static final String UPDATEDDATE = "updatedDate";
	private static final String UPDATEDBY = "updatedBy";
	private static final String STATUSCD = "statusCd";
	private static final String CREATEDDATE = "createdDate";
	private static final String CREATEDBY = "createdBy";

	private static final String DATEFORMAT = "MM/dd/yyyy";
	private static final String CARRIERACCOUNT = "carrierAccount";
	private Set<Object> command;	
	
	HashMap<String, String> oldColorCodeMap = new HashMap<String, String>();
	HashMap<String, String> newColorCodeMap= new HashMap<String, String>();
	
	private WorkflowManager workflowManager;
	private String currentDate="";
	private AttributeManager attributeManager;
	public AttributeManager getAttributeManager() {
		return attributeManager;
	}

	public void setAttributeManager(AttributeManager attributeManager) {
		this.attributeManager = attributeManager;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	/**
	 * @return the workflowManager
	 */
	public WorkflowManager getWorkflowManager() {
		return workflowManager;
	}

	/**
	 * @param workflowManager
	 *            the workflowManager to set
	 */
	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	private OutfitManager outfitManager;
	
	public OutfitManager getOutfitManager() {
		return outfitManager;
	}

	public void setOutfitManager(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}
	
	// Added for CARS Faceted Navigation
	private SuperColorManager superColorManager;
	
	
	public SuperColorManager getSuperColorManager() {
		return superColorManager;
	}

	public void setSuperColorManager(SuperColorManager superColorManager) {
		this.superColorManager = superColorManager;
	}

	private SizeConversionManager sizeConversionManager;

	public SizeConversionManager getSizeConversionManager() {
		return sizeConversionManager;
	}

	public void setSizeConversionManager(SizeConversionManager sizeConversionManager) {
		this.sizeConversionManager = sizeConversionManager;
	}

	private SizeConversionRulesUtils sizeConversionRulesUtils;
	
	public SizeConversionRulesUtils getSizeConversionRulesUtils() {
		return sizeConversionRulesUtils;
	}

	public void setSizeConversionRulesUtils(
			SizeConversionRulesUtils sizeConversionRulesUtils) {
		this.sizeConversionRulesUtils = sizeConversionRulesUtils;
	}
	private List<ColorMappingMaster> superColorList;
	

	public List<ColorMappingMaster> getSuperColorList() {
		return superColorList;
	}

	public void setSuperColorList(List<ColorMappingMaster> superColorList) {
		this.superColorList = superColorList;
	}

	private DBPromotionManager dbPromotionManager;
	
	
	public DBPromotionManager getDbPromotionManager() {
		return dbPromotionManager;
	}

	public void setDbPromotionManager(DBPromotionManager dbPromotionManager) {
		this.dbPromotionManager = dbPromotionManager;
	}

	@Override
	public void setCarManager(CarManager carManager) {
		super.setCarManager(carManager);
		onHand = (SampleSourceType) this.getCarManager().getFromId(
				SampleSourceType.class, SampleSourceType.ON_HAND);
		vendor = (SampleSourceType) this.getCarManager().getFromId(
				SampleSourceType.class, SampleSourceType.VENDOR);
		rrDonnelly = (ImageProvider) this.getCarManager().getFromId(
				ImageProvider.class, RRDONNELLY);
		pineville = (ImageProvider) this.getCarManager().getFromId(
				ImageProvider.class, PINEVILLE);
		sampleProduct = (SampleType) this.getCarManager().getFromId(
				SampleType.class, SampleType.PRODUCT);
		sampleSwatch = (SampleType) this.getCarManager().getFromId(SampleType.class,
				SampleType.SWATCH);
		imageReceived = (ImageTrackingStatus) getCarManager().getFromId(
				ImageTrackingStatus.class, ImageTrackingStatus.RECEIVED);
	}

	public DashBoardFormController() {
		log.info("Got into DashBoardFormController");
		setCommandClass(Car.class);
		setCommandName("detailCar");


	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.info("Got into processFormSubmission");
		return super.processFormSubmission(request, response, command, errors);
	}

	private Object getObjectForLongValue(String id2, Class<?> theClass) {
		if (id2 != null && !id2.equals(EMPTYSTRING)) {
			Long val = new Long(id2);
			return this.getCarManager().getFromId(theClass, val);
		} else
			return null;
	}

	private ImageProvider getImageProviderName(Car car) {
		if(car.getImageProvider().getName().equalsIgnoreCase(Constants.BELK_PINEVILLE_STUDIO)){
			return pineville;
		}
		 else
			return rrDonnelly;
	}
	private Object getObjectForStringValue(String id2, Class<?> theClass) {
		if (id2 != null && !id2.equals(EMPTYSTRING)) {
			return this.getCarManager().getFromId(theClass, id2);
		} else
			return null;
	}

	/**
	 * This method validates the color code changes and sample changes required
	 * In case of color code changes if old color code and new color code has vendor images then it will not allow to change the color code 
	 * If sample for the color code is already requested to RRD and we are chaging color code then it will not allow to change sku color code
	 * @param request
	 * @return this method will return false in case of error else true
	 */
	public boolean validateSampleColorChanges(HttpServletRequest request){
		String carId = request.getParameter(PARAMCARID);	
		Car car =  this.getCarManager().getCarFromId(new Long(carId));
		Map<String, String> colorCodeChange = new HashMap<String, String>();
		Set<String> existingStyleColorSkuSet = new  HashSet<String>();
		Set<String> removeStyleColorSkuSet = new  HashSet<String>();//adde for swapping color
		String strStyleColor = "";
		String changedStyleColor = "";
		boolean isSwapping=false;
		int i=0;
		String trackOldColorCode = "";
		String belkSKU = "";
		String oldColorSkuValue="";	
		//Below code modified to handle color code swapping
		for(VendorSku sku: car.getVendorSkus()) {
			strStyleColor = sku.getVendorStyle().getVendorStyleNumber()+"_"+sku.getColorCode();
			belkSKU = sku.getBelkSku();
			trackOldColorCode = sku.getColorCode(); // getting old color code
			oldColorSkuValue = sku.getVendorStyle().getVendorStyleNumber()+"_"+belkSKU;
			oldColorCodeMap.put(oldColorSkuValue, trackOldColorCode);
			log.debug("belkSKU - " + belkSKU);
			log.debug("trackOldColorCode -  " + trackOldColorCode);
			isSwapping=false;
			String colorCodeSwap = request.getParameter("carSkuColorCode:" + sku.getCarSkuId());
			if (StringUtils.isNotEmpty(colorCodeSwap) && !colorCodeSwap.equals(sku.getColorCode())) {
				i++;
				for(VendorSku skuInner: car.getVendorSkus()) {
					if(colorCodeSwap.equals(skuInner.getColorCode())){
						String colorCodeSwapInner = request.getParameter("carSkuColorCode:" + skuInner.getCarSkuId());
						if (StringUtils.isNotEmpty(colorCodeSwapInner) && !colorCodeSwapInner.equals(skuInner.getColorCode())) {
							if(colorCodeSwapInner.equals(sku.getColorCode())){
								removeStyleColorSkuSet.add(strStyleColor);
								break;
							}
						}
					}
				}
			}
				if(!existingStyleColorSkuSet.contains(strStyleColor)){
					existingStyleColorSkuSet.add(strStyleColor);  //new set containing unique Style_Color combination
			     }
		 }
		
		if(i > 0 && i == removeStyleColorSkuSet.size()){
			return true;
		}
		boolean skuHasChanged = false ;
		
		for(VendorSku sku: car.getVendorSkus()) {
			String colorCode = request.getParameter("carSkuColorCode:" + sku.getCarSkuId()); 
			changedStyleColor = sku.getVendorStyle().getVendorStyleNumber()+"_"+colorCode;  //new style_color value
			if (StringUtils.isNotEmpty(colorCode) && !colorCode.equals(sku.getColorCode())) { 
				skuHasChanged = true ;
				colorCodeChange.put(strStyleColor, colorCode) ;
				if(existingStyleColorSkuSet.contains(changedStyleColor) && !removeStyleColorSkuSet.contains(changedStyleColor)){ //if new style+color doesnot exists in CAR
					VendorImage vi = null;
					boolean newColorCodeHasImages = false;
					boolean oldColorCodeHasImages = false;
					for(CarImage ci: car.getCarVendorImages()){
						vi= ci.getImage().getVendorImage();
						//if new color code already has vendor images
						if(colorCode.equals(vi.getColorCode()) && vi.getVendorStyle().getVendorStyleNumber().equals(sku.getVendorStyle().getVendorStyleNumber())){
							newColorCodeHasImages = true;
						}
						if(sku.getColorCode().equals(vi.getColorCode()) && vi.getVendorStyle().getVendorStyleNumber().equals(sku.getVendorStyle().getVendorStyleNumber())){
							oldColorCodeHasImages = true;
						}
						if(oldColorCodeHasImages && newColorCodeHasImages){
							request.setAttribute("errorColorCodeChangeMessage", "errorColorCodeChangeMessage");
							request.setAttribute("colorCode", colorCode);
							return false;
						}
					}
					if(!oldColorCodeHasImages){
                        sku.setColorCode(colorCode);
					}
				}
			}
		}

		if(!skuHasChanged){
			return true;
		}

		List<String> sampleColorList = new ArrayList<String> ();
		Set<String> changedStyleColorSet = new HashSet<String>();
		for(VendorSku sku: car.getVendorSkus()) {
			strStyleColor = sku.getVendorStyle().getVendorStyleNumber()+"_"+sku.getColorCode();
			if(!changedStyleColorSet.contains(strStyleColor)){
				changedStyleColorSet.add(strStyleColor);  //new set containing unique Style_Color combination
			}
		}
		DateFormat df = new SimpleDateFormat(DATEFORMAT);
		String currDate=df.format( new Date());
		for (CarSample cs : car.getCarSamples()) {
			Sample sample = cs.getSample();
			strStyleColor = sample.getVendorStyle().getVendorStyleNumber()+ "_" + sample.getSwatchColor();
			if (sampleColorList.contains(strStyleColor)
					|| (!changedStyleColorSet.contains(strStyleColor) && VendorStyleType.PRODUCT
							.equals(sample.getVendorStyle().getVendorStyleType().getCode()))) {
				// Need to delete this sample check if sample is requested and
				// sampleRequest feed is already sent to RRD then do not allow
				// to change color code. show error message
				String strTackingStatus = sample.getSampleTrackingStatus()
						.getSampleTrackingStatusCd();
				if ((SampleTrackingStatus.RECEIVED.equals(strTackingStatus) && sample
						.getUpdatedDate().before(new Date(currDate))) || (!SampleTrackingStatus.REQUESTED
								.equals(strTackingStatus) && !SampleTrackingStatus.RECEIVED
								.equals(strTackingStatus))) {
					// do not allow to change color code - show error message
					request.setAttribute("sampleRequestedError",sample.getSwatchColor());
					return false;
				}

			}
			sampleColorList.add(strStyleColor);
		}
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		
		Car car = null;
		long saveCarStartTime = System.currentTimeMillis();
		User user = getLoggedInUser();
		String carId = request.getParameter(PARAMCARID);		
		if (StringUtils.isNotBlank(carId)) {
			car = this.getCarManager().getCarFromId(new Long(carId));
			if (car == null) {
				throw new Exception ("Invalid Car: Please provide a valid CAR ID") ;
			}
			
			WorkflowStatus currentStatus = car.getCurrentWorkFlowStatus();
			UserType currentUserType = car.getAssignedToUserType();
			WorkflowTransitionInfo wti = workflowManager.getNextWorkFlowTransitionInformation(currentUserType.getUserTypeCd(),
							currentStatus.getStatusCd(), car);
			request.setAttribute("workflowTransition", wti);

		} else {
			car = new Car();
		}

		// Added for CARS Faceted Navigation
		superColorList = getSuperColorManager().getAllSuperColorMapping();
		if(car.getCreatedDate()!=null && carManager.isPostCutoverCar(car)){
		    car.setPostCutOver(Constants.VALUE_YES);
		}else{
		    car.setPostCutOver(Constants.VALUE_NO);
		}
		Map<String,String>  backupMap = getCarManager().getBackupAttrVals(car.getCarAttributes());
		request.getSession().setAttribute("backUpCarBeforeEdit", backupMap);
		this.putItemsIntoRequest(request, car, user);
		
		//Added for VIP to APPROVE/REJECT/DELETE the image
		String strVendImageStatus = request.getParameter("happrove");
		if (strVendImageStatus != null && strVendImageStatus.length()>1) {
			String imageId = request.getParameter("ImageId");
			if(log.isInfoEnabled()){
				log.info("Trying to " + strVendImageStatus + " the image ID: " +imageId);
			}
		    updateVendorImageStatus(car,imageId,strVendImageStatus, user);
		    this.setAuditInfo(request, car) ;
			this.getCarManager().save(car);
		    
		}
		if (!isFormSubmission(request)) {
			return car;
		}
		
		//added code to validate the color code and sample code changes for vendor images
		if(!validateSampleColorChanges(request)){
			return car;
		}
		
		boolean carHasChanged = false ;
		Attribute isSearchableAttr = outfitManager
		.getAttributeByName(Constants.COLLECTION_IS_PRODUCT_SEARCHABLE);
		long isSearchableCarAttrId = outfitManager.getCarAttributeId(car,isSearchableAttr);
		for (CarAttribute attr: car.getCarAttributes()) {
			String attrVal = null ;
			String paramName = CARATTRIBUTE + attr.getCarAttrId() ;
			if (attr.getAttribute().getAttributeConfig().getHtmlDisplayType().isCheckbox() && isSearchableCarAttrId != attr.getCarAttrId()) {
				StringBuffer strB = new StringBuffer() ;
				if (request.getParameterValues(paramName) != null) {
					boolean addDelimiter = false ;
					for(String val : request.getParameterValues(CARATTRIBUTE + attr.getCarAttrId())) {
						if (addDelimiter) {
							strB.append(HtmlDisplayType.CHECKBOX_VALUE_DELIMITER).append(" ") ;
						}
						strB.append(val);
						addDelimiter = true ;
					}
				}
				attrVal = strB.toString() ;
			} else {
				attrVal = request.getParameter(paramName) ;
			}

			if(attrVal == null && request.getParameterMap().containsKey(paramName)) {
				attrVal = "" ;
			}

			if (attrVal != null) {
				if (!attrVal.equals(attr.getAttrValue()==null?"":attr.getAttrValue())) {
					if (log.isDebugEnabled())
						log.debug("Attribute Value Has Changed!!!");
					attr.setAttrValue(attrVal);
					
					//Added Check for Displayable Flag for Attribute
					//Per Virginia & Todds Request
					if (attr.getAttribute().getAttributeConfig().getHtmlDisplayType().isAutocomplete()
							&& Attribute.FLAG_YES.equals(attr.getAttribute().getIsDisplayable())) {
						attr.setAttributeValueProcessStatus((AttributeValueProcessStatus) getCarLookupManager().getAttributeValueProcessStatus(
								AttributeValueProcessStatus.CHECK_REQUIRED));
					} else {
						attr.setAttributeValueProcessStatus((AttributeValueProcessStatus) getCarLookupManager().getAttributeValueProcessStatus(
								AttributeValueProcessStatus.NO_CHECK_REQUIRED));
					}
					this.setAuditInfo(request, attr) ;
					carHasChanged = true;
				}
			}
		}

		boolean isError = false;
		//boolean isWebmerchant = (user != null && user.isWebmerchant()) ;
		boolean isBuyer = (user != null && user.isBuyer()) ;
		Map<String, String> colorCodeChange = new HashMap<String, String>();
		Set<String> existingStyleColorSkuSet = new  HashSet<String>();
		Set<String> newStyleColorSkuSet = new  HashSet<String>();
		HashMap<String, String> newColorCodeMap= new HashMap<String, String>();
		Map<String, String> colorAndSwatch = new HashMap<String, String>();
		String strStyleColor = "";
		String changedStyleColor = "";
		String newColorSkuValue="";
		boolean isSwapping=false;
		for(VendorSku sku: car.getVendorSkus()) {
			String nonHiddenValue = null;
			strStyleColor = sku.getVendorStyle().getVendorStyleNumber()+"_"+sku.getColorCode();
			String belkSKU = sku.getBelkSku();
			String newColorCode = request.getParameter("carSkuColorCode:" + sku.getCarSkuId());
			newColorSkuValue = sku.getVendorStyle().getVendorStyleNumber()+"_"+belkSKU;			
			newColorCodeMap.put(newColorSkuValue, newColorCode);
			log.debug("belkSKU - " + belkSKU);
			log.debug("newColorCode - "+ newColorCode);
			if(!existingStyleColorSkuSet.contains(strStyleColor)){
				existingStyleColorSkuSet.add(strStyleColor);  //new set containing unique Style_Color combination
			}
			if (StringUtils.isNotEmpty(newColorCode) && !newColorCode.equals(sku.getColorCode())) {
				if(!newStyleColorSkuSet.contains(sku.getVendorStyle().getVendorStyleNumber()+"_"+newColorCode)){	
				    newStyleColorSkuSet.add(sku.getVendorStyle().getVendorStyleNumber()+"_"+newColorCode);
				}
			}else{
				 newStyleColorSkuSet.add(strStyleColor);
			}
			if (request.getParameter("carSkuSwatchDescH:"+ sku.getCarSkuId()) != null) {
				nonHiddenValue = null;
			} 
			if (request.getParameter("carSkuSwatchDescT:"+ sku.getCarSkuId()) != null) {
				nonHiddenValue = request.getParameter("carSkuSwatchDescT:"+ sku.getCarSkuId());
			}
			if (sku != null && nonHiddenValue != null) {
				colorAndSwatch.put(("Style:"+ sku.getVendorStyle().getVendorStyleId()
						+ "-ColorCode:" + sku.getColorCode()), new String(nonHiddenValue));
			}
		}
		StringBuffer globalStartDtSkus= new StringBuffer();
		List<String> uniqueColorCodes = new ArrayList<String>();
		boolean isColorAndSwatchRetrieved = false;
		List<VendorStyle> styles = car.getVendorStyles();
		Comparator gcVendorStyle = new GenericComparator("vendorStyleNumber");
		Collections.sort(styles, gcVendorStyle);
		int styleCount = 0;
		String isGlobalStartDateButtonClicked = request
				.getParameter("isGlobalStartDateClicked");
		String isSubmitted = request.getParameter("hiddenSubmitCar");
		for (VendorStyle vStyle : styles) {
			Set<VendorSku> vendorStyleSkus= new HashSet<VendorSku>();
			String globalStartDateButton = request
					.getParameter("applyAllGlobalDate_" + styleCount);
			String globalStartDate = request.getParameter("globalStartDate_"
					+ styleCount);
			if (globalStartDate != null && !globalStartDate.equals("")) {
				vStyle.setGlobalStartDate(globalStartDate);
				vendorStyleSkus=vStyle.getVendorSkus();
			    for (VendorSku vSku: vendorStyleSkus){
			    	globalStartDtSkus.append(vSku.getBelkUpc().toString()+",");
			    }
			}
			styleCount++;
		}
		updateVendorStyleNames(request,styles);   /* Part of Deal Based Pricing Project*/
		if (isGlobalStartDateButtonClicked != null
				&& isGlobalStartDateButtonClicked.equals("Clicked")) {
			return car;
		}

		Date currentDate = new Date();
		Long tempCarId=null;
		String tempStyleNumber=null;
		// Added as part of performance tuning
		String updatedSkus = request.getParameter("updatedSkus");
		String updatedColorCodeShades = request.getParameter("updatedShades");
		String updatedStartDateSkus = request.getParameter("updatedStartDateSkus");
		String updatedSuperColorSkus = request.getParameter("updatedSuperColorSkus");
		log.info("updated skus -" + updatedSkus
				+ " updated color code shades -" + updatedColorCodeShades
			    + " updated start date skus -" + updatedStartDateSkus
				+ " updated super color skus -" + updatedSuperColorSkus
				+ " updated global start date skus -" + globalStartDtSkus);
		for (VendorSku sku: car.getVendorSkus()) {
			tempCarId=sku.getCar().getCarId();
			tempStyleNumber=sku.getVendorStyle().getVendorStyleNumber();
			log.debug("Car ID: " + tempCarId);
			log.debug("Vendor Style Number: " + tempStyleNumber);
			boolean skuHasChanged = false;
			isSwapping = false;
			uniqueColorCodes.add(sku.getColorCode());
			//Save Shade Details
			if (updatedColorCodeShades!=null && updatedColorCodeShades.contains(sku.getColorCode())){
				saveShadeDetails(request, sku, colorAndSwatch);
			}
			//Save Start Date Details
			if ( (updatedStartDateSkus!=null && updatedStartDateSkus.contains(sku.getBelkUpc())) 
						|| (globalStartDtSkus !=null && globalStartDtSkus.toString().contains(sku.getBelkUpc()))){
				saveSkuStartDateDetails(request, sku);
			}
			if (updatedSuperColorSkus!=null && updatedSuperColorSkus.contains(sku.getBelkUpc())){
			   // Added for CARS Faceted Navigation - Saving Super Colors- Begin
			   saveSKUSuperColors(request, sku);
			}
            String colorName = request.getParameter("carSkuColorName:" + sku.getVendorStyle().getVendorStyleId() + ":" + sku.getColorCode());
            String colorCode = request.getParameter("carSkuColorCode:" + sku.getCarSkuId()); 
            String sizeCode = request.getParameter("carSkuSizeCode:" + sku.getCarSkuId());
            String itemName = request.getParameter("carSkuItemName:" + sku.getCarSkuId());
            String idbSizeName = request.getParameter("carSkuIDBSizeName:"+sku.getCarSkuId());
            if(!((colorName != null && !colorName.equals(sku.getColorName()==null?"":sku.getColorName()))
                        || (itemName != null && !itemName.equals(sku.getName()==null?"":sku.getName()))
                        || (StringUtils.isNotEmpty(colorCode) && !colorCode.equals(sku.getColorCode()))
                        || (StringUtils.isNotEmpty(sizeCode) && !sizeCode.equals(sku.getSizeCode()))
                        || (StringUtils.isNotEmpty(idbSizeName) && !idbSizeName.equals(sku.getIdbSizeName())))){
                  continue;
            }
			strStyleColor = sku.getVendorStyle().getVendorStyleNumber()+"_"+sku.getColorCode();
			// CARS Size Conversion Issue - Size name overwritten by resync size job -->
			if (idbSizeName != null && !idbSizeName.equals(sku.getIdbSizeName()==null?"":sku.getIdbSizeName())) {
				skuHasChanged = true;
				sku.setIdbSizeName(idbSizeName);
				sku.setSizeName(idbSizeName); //Setting size name value same as user changed. So if any rule satisfies this size name it will update the size name value to conversion name.
				// Added for CARS Faceted Navigation
				sku.setSizeRule(null);
			}
			if (colorName != null && !colorName.equals(sku.getColorName()==null?"":sku.getColorName())) {
				skuHasChanged = true ;
				sku.setColorName(colorName) ;
			}
			if (itemName != null && !itemName.equals(sku.getName()==null?"":sku.getName())) {
				skuHasChanged = true ;
				sku.setName(itemName) ;
			}
			// Added for CARS Faceted Navigation - Saving Super Colors - End
			if ( isBuyer) {
		    //  Swap code: start here
				if (StringUtils.isNotEmpty(colorCode) && !colorCode.equals(sku.getColorCode())) {
					for(VendorSku skuInner: car.getVendorSkus()) {
						if(colorCode.equals(skuInner.getColorCode())){
							String colorCodeSwapInner = request.getParameter("carSkuColorCode:" + skuInner.getCarSkuId());
							if (StringUtils.isNotEmpty(colorCodeSwapInner) && !colorCodeSwapInner.equals(skuInner.getColorCode())) {
								if(colorCodeSwapInner.equals(sku.getColorCode())){
									isSwapping=true;
									skuInner.setColorCode(sku.getColorCode()) ;
									sku.setColorCode(colorCode) ;
									break;
								}
							}
						}
					}
				}
				//Swap code end here
				changedStyleColor = sku.getVendorStyle().getVendorStyleNumber()+"_"+colorCode;  //new style_color value
				if (!isSwapping && StringUtils.isNotEmpty(colorCode) && !colorCode.equals(sku.getColorCode())) { 
					skuHasChanged = true ;
					colorCodeChange.put(strStyleColor, colorCode) ;
					this.getCarManager().updateVendorImageColor(car, sku, colorCode,user);
					sku.setColorCode(colorCode) ;
				}
				if (StringUtils.isNotEmpty(sizeCode) && !sizeCode.equals(sku.getSizeCode())) {
					skuHasChanged = true ;
					sku.setSizeCode(sizeCode) ;
				}
			}
	
			if (skuHasChanged) {
				if (log.isDebugEnabled())
					log.debug("SKU Has Changed!!!");
				//this.getCarManager().save(sku) ;
				this.setAuditInfo(request, sku) ;
				carHasChanged = true ;
			}
			
		}
		
		if (tempCarId != null) {
			// Start - Track color code changes
			HashMap<String, String> finalMap = new HashMap<String, String>();
			Iterator it = oldColorCodeMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry) it.next();
				String oldKey = (String) pairs.getKey();
				String oldValue = (String) pairs.getValue();
				Iterator newIt = newColorCodeMap.entrySet().iterator();
				while (newIt.hasNext()) {
					Map.Entry newPairs = (Map.Entry) newIt.next();
					String newKey = (String) newPairs.getKey();
					String newValue = (String) newPairs.getValue();
					if (oldKey.equalsIgnoreCase(newKey)) {
						if (!oldValue.equalsIgnoreCase(newValue)) {
							finalMap.put(newKey, newValue);
						}
					}

				}
			}
			Iterator finalIt = finalMap.entrySet().iterator();
			while (finalIt.hasNext()) {
				Map.Entry newPairs = (Map.Entry) finalIt.next();
				String finalKey = (String) newPairs.getKey();
				String finalValue = (String) newPairs.getValue();
				log.debug("FinalKey1::" + finalKey);
				log.debug("FinalValue1::" + finalValue);
			}
			Set<String> styleColorSkuSet = new HashSet<String>();
			Iterator oldItrs = oldColorCodeMap.entrySet().iterator();
			String changedColorCode = "";
			while (oldItrs.hasNext()) {
				Map.Entry pairss = (Map.Entry) oldItrs.next();
				String oldKey = (String) pairss.getKey();
				String oldValue = (String) pairss.getValue();
				Iterator finalItrs = finalMap.entrySet().iterator();
				while (finalItrs.hasNext()) {
					Map.Entry newPairs = (Map.Entry) finalItrs.next();
					String newKey = (String) newPairs.getKey();
					String newValue = (String) newPairs.getValue();
					if (oldKey.equalsIgnoreCase(newKey)) {
						String finalStyleNumber="";
						if (!oldValue.equalsIgnoreCase(newValue) && newValue != null  && !newValue.isEmpty()) {
							if (newKey!=null) {
								String[] th=newKey.split("_");
								if (th!=null) {
										log.debug("StyleNumber - "+th[0]);
										log.debug("BelkSku - "+th[1]);
										finalStyleNumber=th[0];
								}
							}
							changedColorCode = "Style " +finalStyleNumber+ ": NRF changed from " + oldValue
									+ " to " + newValue;
							styleColorSkuSet.add(changedColorCode);
						}
					}
				}
			}
			Iterator iterSet = styleColorSkuSet.iterator();
			while (iterSet.hasNext()) {
				String finalValue = (String) iterSet.next();
				log.debug("CarNote for Changed color code : " + finalValue);
				if (finalValue != null) {
					CarNote carNote = new CarNote();
					carNote.setNoteText(finalValue);
					long carID = tempCarId;
					NoteType carNoteType = (NoteType) this
							.getCarLookupManager().getById(NoteType.class,NoteType.CAR_NOTES);
					car = this.getCarManager().getCarFromId(carID);
					carNote.setIsExternallyDisplayable(CarNote.FLAG_NO);
					carNote.setCar(car);
					carNote.setNoteType(carNoteType);
					carNote.setStatusCd(Status.ACTIVE);
					carNote.setCreatedBy(user.getUsername());
					carNote.setUpdatedBy(user.getUsername());
					carNote.setCreatedDate(currentDate);
					carNote.setUpdatedDate(currentDate);
					car.getCarNotes().add(carNote);
					getCarManager().save(car);
				}
			}
		}
        // End of track color code changes 
		// Change based on Enhancement to set turnin date
		// Set the Turnin Date by Buyer and/or Webmerchant
		String strTurninDate = request.getParameter("sampleTurninDate") ;
		if (strTurninDate != null) {
			if (StringUtils.isEmpty(strTurninDate)) {
				if(car.getTurninDate()!=null) {
					car.setTurninDate(null);
					carHasChanged=true;
				}
			} else {
				Date turninDate = DateUtils.parseDate(strTurninDate, DATEFORMAT);
				if (car.getTurninDate() == null || !turninDate.equals(car.getTurninDate())) {
					car.setTurninDate(turninDate) ;
					carHasChanged=true;
				}
			}
		}
		
		Set<String> changedStyleColorSet = new HashSet<String>();
		for(VendorSku sku: car.getVendorSkus()) {
			strStyleColor = sku.getVendorStyle().getVendorStyleNumber()+"_"+sku.getColorCode();
			if(!changedStyleColorSet.contains(strStyleColor)){
				changedStyleColorSet.add(strStyleColor);  //new set containing unique Style_Color combination
			}
		}
		List<String> sampleColorList = new ArrayList<String> ();
		Enumeration rObjs = request.getParameterNames();
		while (rObjs.hasMoreElements()) {
			String param = (String) rObjs.nextElement();
			if (param.startsWith(VENDORSTYLEDESC)) {
				String attrId = param
						.replaceFirst(VENDORSTYLEDESC, EMPTYSTRING);
				String attrValue = request.getParameter(param);
				VendorStyle vs = (VendorStyle) this.getObjectForLongValue(
						attrId, VendorStyle.class);
				if (attrValue != null && !attrValue.equals(vs.getDescr() == null ? "" : vs.getDescr())) {
					if (log.isDebugEnabled())
						log.debug("Description Has Changed!!!");
					vs.setDescr(attrValue);
					this.setAuditInfo(request, vs) ;
					carHasChanged = true ;
				}
			} else if (param.startsWith(VENDORSTYLENAME)) {
				String attrId = param
						.replaceFirst(VENDORSTYLENAME, EMPTYSTRING);
				String attrValue = request.getParameter(param);
				VendorStyle vs = (VendorStyle) getObjectForLongValue(attrId,
						VendorStyle.class);
				if (attrValue != null && !attrValue.equals(vs.getVendorStyleName() == null ? "" : vs.getVendorStyleName())) {
					if (log.isDebugEnabled())
						log.debug("Name Has Changed!!!");
					vs.setVendorStyleName(attrValue);
					this.setAuditInfo(request, vs) ;
					carHasChanged = true ;
				}
			} else if (param.startsWith(SAMPLE)) {
				boolean sampleHasChanged = false ;
				Date ssd = null;
				String id = param.replaceFirst(SAMPLE, EMPTYSTRING);
				String sampleShipDt = request.getParameter(SAMPLESHIPDATE + id);				
				String hidesampleShipDt = request.getParameter(HIDESAMPLESHIPDATE + id);;			
				String carrierAccount=request.getParameter(CARRIERACCOUNT);
				String carrier=request.getParameter("retCarrier");
				String mustBeReturned = request.getParameter(MUSTBERETURNED + id);
				String silhouetteRequired=request.getParameter(SILHOUETTEREQ + id);
				String trackingStatus = request.getParameter(TRACKINGSTATUS + id);
				String sampleValue = request.getParameter(param);			
				//Added by AFUSYS3															
				if (sampleShipDt != null && !SELECTCRITERIA.equalsIgnoreCase(sampleShipDt)){
					ssd = DateUtils.parseSampleDate(sampleShipDt, DATEFORMAT);
				} else if (hidesampleShipDt != null && !hidesampleShipDt.equals("") && !SELECTDATE.equals(hidesampleShipDt)){
 					ssd = DateUtils.parseSampleDate(hidesampleShipDt, DATEFORMAT);	
				} else {
					ssd = DateUtils.parseSampleDate(SELECTCRITERIA, DATEFORMAT);
				}				 
				//Ended by AFUSYS3
				Long sampleId = new Long(id);
				ShippingType shipType=(ShippingType)this.getCarLookupManager().getById(ShippingType.class, carrier);

				Sample sample = (Sample) this.getCarManager().getFromId(Sample.class, sampleId);

				String imageProviderId = request.getParameter(IMAGEPROVIDER);
				ImageProvider ip = sample.getImageProvider();
				if (StringUtils.isNotBlank(imageProviderId)) {
					ip = (ImageProvider) this.getCarLookupManager().getById(ImageProvider.class, new Long(imageProviderId));
				}				

				if (ip.getImageProviderId() != sample.getImageProvider().getImageProviderId()) {
					sampleHasChanged = true ;
					sample.setImageProvider(ip);
				}	
				 DateFormat df = new SimpleDateFormat(DATEFORMAT);
				 Date presentDate = new Date();
				 String currDate=df.format(presentDate);
				 if(ssd.after(new Date(currDate)) || ssd.equals(new Date(currDate))){
						sample.setExpectedShipDate(ssd);	
				 }
				if (shipType.equals(sample.getShippingType())) {
					sampleHasChanged = true ;
					sample.setShippingType(shipType);
				}

				if (carrierAccount.equals(sample.getShipperAccountNumber()==null?"":sample.getShipperAccountNumber())) {
					sampleHasChanged = true ;
					sample.setShipperAccountNumber(carrierAccount);
				}
				SampleType st = null;
				SampleSourceType sst = null;
				if (sampleValue.equals(OHSAMPLE)) {
					st = sampleProduct;
					sst = onHand;
				} else if (sampleValue.equals(OHSWATCH)) {
					st = sampleSwatch;
					sst = onHand;
				} else if (sampleValue.equals(FVSAMPLE)) {
					st = sampleProduct;
					sst = vendor;
				} else if (sampleValue.equals(FVSWATCH)) {
					st = sampleSwatch;
					sst = vendor;
				} else {
					st = sampleSwatch;
					sst = (SampleSourceType) this.getCarManager().getFromId(
							SampleSourceType.class, SampleSourceType.NEITHER);
				} 
				
				if (!sst.equals(sample.getSampleSourceType())) {
					sampleHasChanged = true ;
					sample.setSampleSourceType(sst);
				}
				
				if (!st.equals(sample.getSampleType())) {
					sampleHasChanged = true ;
					sample.setSampleType(st);
				}
				
				if (mustBeReturned == null) {
					mustBeReturned = Sample.FLAG_NO;
				}
				if (!mustBeReturned.equals(sample.getIsReturnable())) {
					sample.setIsReturnable(mustBeReturned);
					sampleHasChanged = true ;
				}
				if (silhouetteRequired == null) {
					silhouetteRequired = Sample.FLAG_NO;
				}

				if (!silhouetteRequired.equals(sample.getSilhoutteRequired())) {
					sample.setSilhoutteRequired(silhouetteRequired);
					sampleHasChanged = true ;
				}
				
				
				if (trackingStatus != null) {
					SampleTrackingStatus sts = (SampleTrackingStatus) this
							.getCarManager().getFromId(
									SampleTrackingStatus.class, trackingStatus);
					if (!sts.equals(sample.getSampleTrackingStatus())) {
						sampleHasChanged = true;
						sample.setSampleTrackingStatus(sts);
					}
				}
				if (colorCodeChange != null && !colorCodeChange.isEmpty()) {
					
					strStyleColor = sample.getVendorStyle().getVendorStyleNumber()+"_"+sample.getSwatchColor();
					String swatchcolor= (String)colorCodeChange.get(strStyleColor);
					// if current sample color code is changed to new color code 
					// and there are no sku for current sample code then only modify sample color
					if (swatchcolor != null && !changedStyleColorSet.contains(strStyleColor)) {
						sample.setSwatchColor(swatchcolor);
						sampleHasChanged = true;
						log.info("Updating the color code: "+swatchcolor);
					}
				}
				
				if (sampleHasChanged) {
					this.setAuditInfo(request, sample) ;
					carHasChanged = true ;
				}
				
			}
		}//end while
		
		if (carHasChanged && !isError) {
			this.setAuditInfo(request, car) ;
			this.getCarManager().save(car);
		}
		//update attribute value process statuses
		getCarManager().updateAttributeValueProcessStatuses(car.getCarId());
		log.info("Save Car Total Time - "+(System.currentTimeMillis()-saveCarStartTime));
		return car;
	}
	
	
	private void setDefault(Object ob, String user) {
		setDefault(ob, user, true, true);
	}

	private void setDefault(Object ob, String user, boolean isNew,
			boolean isActive) {
		try {
			Date dt = new Date();
			Map methods = PropertyUtils.describe(ob);
			if (methods.containsKey(UPDATEDDATE)) {
				PropertyUtils.setProperty(ob, UPDATEDDATE, dt);
			}
			if (methods.containsKey(UPDATEDBY)) {
				PropertyUtils.setProperty(ob, UPDATEDBY, user);
			}
			if (isNew) {
				if (methods.containsKey(CREATEDDATE)) {
					PropertyUtils.setProperty(ob, CREATEDDATE, dt);
				}
				if (methods.containsKey(CREATEDBY)) {
					PropertyUtils.setProperty(ob, CREATEDBY, user);
				}
			}
			if (isActive) {
				if (methods.containsKey(STATUSCD)) {
					PropertyUtils.setProperty(ob, STATUSCD, Status.ACTIVE);
				}
			} else {
				if (methods.containsKey(STATUSCD)) {
					PropertyUtils.setProperty(ob, STATUSCD, Status.INACTIVE);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Image saveImage(Car car, Image img, User user,
			String imageDescription, String imageFinalUrl, String imageType,
			String imageLocationType, String imageText, String notesText,
			String imageSourceTypeCd, String imageTrackingStatusCd, String approvalNotesText) {
		setDefault(img, user.getUsername());
		Date dt = new Date();
		img.setRequestDate(dt);
		img.setReceivedDate(dt);
		img.setDescription(imageDescription);
		img.setNotesText(notesText);
		img.setImageFinalUrl(imageFinalUrl);
		img.setImageType((ImageType) this.getObjectForStringValue(imageType,
				ImageType.class));
		img.setImageLocationType((ImageLocationType) this
				.getObjectForStringValue(imageLocationType,
						ImageLocationType.class));
		img.setImageSourceType((ImageSourceType) this.getObjectForStringValue(
				imageSourceTypeCd, ImageSourceType.class));
		img.setImageType((ImageType) this.getObjectForStringValue(imageType,
				ImageType.class));
		img.setImageTrackingStatus(((ImageTrackingStatus) this
				.getObjectForStringValue(imageTrackingStatusCd,
						ImageTrackingStatus.class)));
		img.setReceivedDate(dt);
		img.setApprovalNotesText(approvalNotesText);
		this.getCarManager().save(img);

		CarImage ci = new CarImage();
		CarImageId cId = new CarImageId();
		cId.setCarId(car.getCarId());
		cId.setImageId(img.getImageId());
		ci.setId(cId);
		ci.setCar(car);
		setDefault(ci, user.getUsername());
		ci.setImage(img);

		this.getCarManager().save(ci);
		return ci.getImage();
	}
	
	
	private void saveVendorImage(HttpServletRequest request, Car car, User user) {
		Image img = new Image();
		img.setImageProcessingStatusCd(Image.PROCESSING_STATUS_PENDING) ;

		String reqImageNote = request.getParameter("reqImageNote");
		if (reqImageNote==null || reqImageNote.equals("")){
			log.info("Can not save empty vendor image");
			return;
		}
		String reqImageTypeCd = request.getParameter("reqImageType");
		String reqImageId = request.getParameter("reqImageId");
		String reqTrackingStatusCd = request
				.getParameter("image.imageTrackingStatusCd");
		if (reqTrackingStatusCd==null || reqTrackingStatusCd.equals("")){
			reqTrackingStatusCd="REQUESTED";
		}
		if (reqImageId != null && !reqImageId.equals("")
				&& !reqImageId.equals("0")) {
			try {
				img.setImageId(new Long(reqImageId));
			} catch (Exception e) {
			}
		}

		img = this.saveImage(car, img, user, reqImageNote, null,
				reqImageTypeCd, null, null, null, ImageSourceType.VENDOR, reqTrackingStatusCd, null);
	}
	

	public void saveOnHandImage(HttpServletRequest request, String imageId,
			Car car, User user) {
		Image img = new Image();
		String imageType = request.getParameter("imageType");
		String imageLocationType = request.getParameter("imageLocationType");
		String imageFinalUrl = request.getParameter("image.imageFinalUrl");
		String imageDescription = request.getParameter("imageDescription");
		String notesText = request.getParameter("image.notesText");
		String imageSourceTypeCd = request.getParameter("image.sourceTypeCd");
		String approvalNotes = request.getParameter("imageApprovalRejectComment");
		
		if (StringUtils.isBlank(imageSourceTypeCd)) {
			imageSourceTypeCd = ImageSourceType.ON_HAND ;
		}
		if (imageDescription==null || imageDescription.equals("")){
			log.info("Can not save empty Image record");
			return;
		}
		String reqTrackingStatusCd = request
				.getParameter("image.imageTrackingStatusCd");
		if (reqTrackingStatusCd==null || reqTrackingStatusCd.equals("")){
			reqTrackingStatusCd=ImageTrackingStatus.AVAILABLE;
		}
		
		//this.getCarManager().getImage();
		if (imageId != null && !imageId.equals("") && !imageId.equals("0")) {
			try {
				//img.setImageId(new Long(imageId));
				img = (Image) this.getCarLookupManager().getById(Image.class, new Long(imageId));
				if (img == null) {
					img = new Image() ;
					img.setImageId(new Long(imageId));
				}
			} catch (Exception e) {
			}
		}
		this.saveImage(car, img, user, imageDescription, imageFinalUrl,
				imageType, imageLocationType, imageDescription, notesText,
				imageSourceTypeCd, reqTrackingStatusCd, approvalNotes);
	}
	
	private String getTemplateTypeVal(Car car){
		Attribute templateType = new Attribute(); ;
		String attrVal ="";
		if(car.getSourceType().getSourceTypeCd().equals(SourceType.OUTFIT)){
			templateType = getOutfitManager().getAttributeByName(Constants.COLLECTION_OUTFIT_TYPE);
			attrVal = getOutfitManager().getAttributeValue(car,templateType);
		}else if(car.getSourceType().getSourceTypeCd().equals(SourceType.PYG)){
			templateType = getDbPromotionManager().getAttributeByName(Constants.DBPROMOTION_TEMP_TYPE);
			attrVal = getDbPromotionManager().getAttributeValue(car,templateType);
		}
		return attrVal;
	}
	
	
	private void putItemsIntoRequest(HttpServletRequest request, Car car,
			User user) {
		long eStartTime = System.currentTimeMillis();
		// Added for CARS Faceted Navigation
		// #################  super color management begins  #################
		Map<String,String> superColors=new HashMap<String,String>();
		for(ColorMappingMaster map:superColorList){
			superColors.put(map.getSuperColorCode(), map.getSuperColorName());
		}
		car.setSuperColors(superColors);
		for (VendorSku sku: car.getVendorSkus() ) {
			String superColor1=getColorCodeSuperColor(sku.getColorCode());
			//to set superColor1 synchronization....
			if (log.isDebugEnabled()){
				log.debug("setting superColor1: color code:"+sku.getColorCode() +"  and super color name:"+superColor1);
			}
			sku.setFirstSuperColor(superColor1);
							
		}
		Attribute isSearchable = outfitManager
				.getAttributeByName(Constants.COLLECTION_IS_PRODUCT_SEARCHABLE);
		Attribute effectiveDate = outfitManager
				.getAttributeByName(Constants.COLLECTION_EFFECTIVE_DATE);
		String isSearchableValue = outfitManager.getAttributeValue(car,
				isSearchable);
		String effectiveDateVal = outfitManager.getAttributeValue(car,
				effectiveDate);
		car.setTemplateType(getTemplateTypeVal(car));
		car.setIsSearchable(isSearchableValue);
		car.setEffectiveDate(effectiveDateVal);
		
		request.setAttribute("user", user);
		request.setAttribute("detailCar", car);

		List<VendorStyle> vendorStyles = new ArrayList<VendorStyle>() ;
		vendorStyles.add(car.getVendorStyle()) ;
		if (car.getVendorStyle().isPattern()) {
			VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria() ;
			criteria.setChildrenOnly(true) ;
			criteria.setVendorStyleId(car.getVendorStyle().getVendorStyleId());
			vendorStyles.addAll(this.carManager.searchVendorStyle(criteria)) ;
		} 
		
			request.setAttribute("vendorStyles", vendorStyles) ;
		   request.removeAttribute("viewChildCars");
		
		//childVendorNumbers is used to show the vendor number for 
		// send to vendor of an outfit car
		Map<Long,String> childVendorNumbers=new HashMap<Long,String>();
		if (car.getSourceType().getSourceTypeCd().equals("OUTFIT")) {
			List<ChildCar> viewChildCars=new ArrayList<ChildCar>();
			Set<CarOutfitChild> outfitChildCars = car.getCarOutfitChild();
			   for (CarOutfitChild carOutfitChild: outfitChildCars) {
				   if(carOutfitChild.getStatusCd().equals(Status.ACTIVE)){
				        Car childCarTemp = carOutfitChild.getChildCar();
				        ChildCar childCar= new ChildCar();
				        childCar.setCarId(childCarTemp.getCarId());
				        childCar.setStyleNumber(childCarTemp.getVendorStyle().getVendorStyleNumber());
				        childCar.setProductName(childCarTemp.getVendorStyle().getVendorStyleName());
				        childCar.setOrder(carOutfitChild.getPriority());
				        childCar.setSku(carOutfitChild.getDefaultColorSku().getColorName());
				        CarAttribute carAttr=outfitManager.getCarAttributeByBMName(childCarTemp,"Brand");
				        if(carAttr == null){
				        	childCar.setBrandName("");
				        }else{
				        	childCar.setBrandName(carAttr.getAttrValue());
				        }
				        viewChildCars.add(childCar);
					   // Only the Buyer can see the child car vendor numbers.
					   if(UserType.BUYER.equals(car.getAssignedToUserType().getUserTypeCd())){
						   if(!childVendorNumbers.containsValue(carOutfitChild.getChildCar().getVendorStyle().getVendorNumber())){
							   childVendorNumbers.put(carOutfitChild.getChildCar().getCarId(),carOutfitChild.getChildCar().getVendorStyle().getVendorNumber());
						   }
						}
				   }
			   }
			   request.setAttribute("viewChildCars", viewChildCars);
			   //Placing the childVendorNumbers in request and display on manageCar.jsp
			   request.setAttribute("childVendorNumbers", childVendorNumbers);
			  }else if(car.getSourceType().getSourceTypeCd().equals(SourceType.PYG)) {
				  setDBPromotionChildForDisplay(request,car,childVendorNumbers);
			  }

		if (log.isDebugEnabled())
			log.debug("Car putItemsIntoRequest 1: " + (System.currentTimeMillis() - eStartTime) + " ms");

		eStartTime = System.currentTimeMillis();
		if (user.getUserType().getUserTypeCd().equals(UserType.BUYER)) {
			//request.setAttribute("workflowStatuses", workflowManager.getAllWorkFlowStatuses());
			//request.setAttribute("userTypes", this.getCarLookupManager().getActiveUserTypes());
		}
		if (log.isDebugEnabled())
			log.debug("Car putItemsIntoRequest 2: " + (System.currentTimeMillis() - eStartTime) + " ms");

		eStartTime = System.currentTimeMillis();
		Map<String, VendorSku> colorNameMap = new HashMap<String, VendorSku>() ;
		for (VendorSku sku: car.getVendorSkus()) {
			String vendorColor = sku.getVendorStyle().getVendorStyleNumber() + "_" + sku.getColorCode(); 
			if (StringUtils.isNotBlank(sku.getColorCode())) {
				if (!colorNameMap.containsKey(vendorColor)) {
					colorNameMap.put(vendorColor, sku);
				}
			}
		}
		if (log.isDebugEnabled())
			log.debug("Car putItemsIntoRequest 3: " + (System.currentTimeMillis() - eStartTime) + " ms");

		//Create or delete the new samples -Start
		boolean carSampleChanged=false;
		eStartTime = System.currentTimeMillis();
		SampleSourceType neither = (SampleSourceType) this
				.getCarManager()
				.getFromId(SampleSourceType.class, SampleSourceType.NEITHER);

		ShippingType defShippingType = (ShippingType) this.getCarManager()
				.getFromId(ShippingType.class, ShippingType.UPS);
		Date defualtShipDate = new Date();
		try {
			defualtShipDate = DateUtils.parseSampleDate(SELECTCRITERIA, DATEFORMAT);
		} catch (ParseException e) {
			log.error("can not parse the date", e);
		}
		
		if (car.getCarSamples() == null || car.getCarSamples().isEmpty())
		{
			String vendorStyleTypeCd= car.getVendorStyle().getVendorStyleType().getCode();
			Set<String> colors = colorNameMap.keySet();
			if (colors != null && !colors.isEmpty()) {
				for(String color : colors) {
					//Sample sample = new Sample() ;
					this.getCarManager().processNeedSamples(car, user.getUsername(),
							colorNameMap.get(color).getColorCode(),defualtShipDate, sampleSwatch, rrDonnelly, neither, "", defShippingType, colorNameMap.get(color).getVendorStyle());
				}
				
			}
			if(!VendorStyleType.PRODUCT.equals(vendorStyleTypeCd)){
				//create the  sample with color 000 for the PATTERN/OUTFIT style
				this.getCarManager().processNeedSamples(car, user.getUsername(),
						"000",defualtShipDate, sampleSwatch, rrDonnelly, neither, "", defShippingType, car.getVendorStyle());
			}
			
			carSampleChanged=true;
		}else{  //if car already has some samples
			List<String> sampleColorList = new ArrayList<String> ();
			Sample sample = null;
			String strStyleColor = "";
			Set<CarSample> removeCarSamples = new HashSet<CarSample>();
			for(CarSample cs: car.getCarSamples()){  //loop through all samples of car
				sample= cs.getSample();
				strStyleColor = sample.getVendorStyle().getVendorStyleNumber()+ "_"+ sample.getSwatchColor();
				if(sampleColorList.contains(strStyleColor) || (colorNameMap.get(strStyleColor)==null && VendorStyleType.PRODUCT.equals(sample.getVendorStyle().getVendorStyleType().getCode()))){  //if sku does not exist for this sample
					//delete this sample
					removeCarSamples.add(cs);
					carSampleChanged=true;
				}
				sampleColorList.add(strStyleColor);
			}
			if(!removeCarSamples.isEmpty()){
				car.getCarSamples().removeAll(removeCarSamples);
			}
			for(String styleColor: colorNameMap.keySet()){
				if(!sampleColorList.contains(styleColor)){ //if sample doesnot exists for style+color
					//create new sample
					carSampleChanged=true;
					this.getCarManager().processNeedSamples(car, user.getUsername(),
							colorNameMap.get(styleColor).getColorCode(),defualtShipDate, sampleSwatch, getImageProviderName(car), neither, "", defShippingType, colorNameMap.get(styleColor).getVendorStyle());
				}
			}
			if(!VendorStyleType.PRODUCT.equalsIgnoreCase(car.getVendorStyle().getVendorStyleType().getCode())){
				//need to create the sample for pattern car, 000 is default color code for pattern style as there is no sku associated with it.
				String strPatternStyleColor = car.getVendorStyle().getVendorStyleNumber() + "_" + "000";
				if(!sampleColorList.contains(strPatternStyleColor)){
					this.getCarManager().processNeedSamples(car, user.getUsername(),
							"000",defualtShipDate, sampleSwatch, getImageProviderName(car), neither, "", defShippingType, car.getVendorStyle());
					log.info("Created sample for pattern style "+ strPatternStyleColor);
					carSampleChanged=true;
				}
			}
		}
		if(carSampleChanged){
			this.getCarManager().save(car) ;
		}
		if (log.isDebugEnabled())
			log.debug("Car putItemsIntoRequest 4: " + (System.currentTimeMillis() - eStartTime) + " ms");


		eStartTime = System.currentTimeMillis();
		for (CarSample carSample : car.getCarSamples()) {
			String color = carSample.getSample().getVendorStyle().getVendorStyleNumber() + "_" + carSample.getSample().getSwatchColor();
			if (colorNameMap.containsKey(color)) {
				carSample.getSample().setColorName(colorNameMap.get(color).getColorName()) ;
			} else {
				carSample.getSample().setColorName("Not-Applicable");
				//set the color name as not applicatble if color not found [specially for outfit cars]
			}
		}

		if (log.isDebugEnabled())
			log.debug("Car putItemsIntoRequest 5: " + (System.currentTimeMillis() - eStartTime) + " ms");
		
		//request.setAttribute("requestColors", this.getCarManager().getRequestSampleValues(car));
		
		eStartTime = System.currentTimeMillis();
		request.setAttribute("vendorContacts", this.getCarManager().getUsersForVendorAndDept(car.getVendorStyle().getVendor().getVendorId(),
                car.getDepartment().getDeptId()));
		
		getCollectionSkus(request, car, user);
		
		getDBPromotionCollectionSkus(request, car, user);
		
		if (log.isDebugEnabled())
			log.debug("Car putItemsIntoRequest 6: " + (System.currentTimeMillis() - eStartTime) + " ms");

	}
	
	
	
		
	
	/**
	 * This method perform the following steps: 
	 * 1.Get all the attribute value (isSearchable,
	 * effectiveDate,templateType)from CarAttribute table based on the attribute name and car.
	 * 2. Get all the outfit child cars. 
	 * 3. Get the list of all child car skus by calling getChildCarSkus
	 * method based on the vendor id. 
	 * 4. split the skus based on child car id. 
	 * 
	 * @param request   It contains HttpServletRequest object.
	 * @param car		It contains car object.
	 * @param user	    It contains user object.
	 */
	@SuppressWarnings("unchecked")
	public void getCollectionSkus(HttpServletRequest request, Car car, User user) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<ChildCar> viewChildCarsSkuList = new ArrayList<ChildCar>(); 
		List<ChildCar> viewChildCarsSkuListArray[] = (ArrayList<ChildCar>[]) new ArrayList[100];
		Attribute collectionSkus = outfitManager.getAttributeByName(Constants.COLLECTION_PARENT_PRODUCTS);
		Attribute templateType = outfitManager.getAttributeByName(Constants.COLLECTION_OUTFIT_TYPE);
		Attribute effectiveDate = outfitManager.getAttributeByName(Constants.COLLECTION_EFFECTIVE_DATE);
		if (car != null && !"".equals(car)) {
			Car outfitCar = car;
			if (outfitCar != null) {
				/* Get all the outfit child cars*/
				Set<CarOutfitChild> carOutfitChild = outfitCar
						.getCarOutfitChild();
				if (carOutfitChild != null) {
					String templateTypeVal = outfitManager.getAttributeValue(
							outfitCar, templateType);
					if (templateTypeVal != null
							&& templateTypeVal.equalsIgnoreCase("COLLECTION")) {
						String effectiveDateVal = outfitManager
								.getAttributeValue(outfitCar, effectiveDate);
						model.put("effectiveDate", effectiveDateVal);
						String[] collectionSKUs = null;
						List<String> lstSelCollectionSKUs = new ArrayList<String>();
						/* sku as a list to send to getViewChildCarDetails, for
						 priority,color sku */
						String productCode = outfitCar.getVendorStyle().getVendorNumber()+outfitCar.getVendorStyle().getVendorStyleNumber();
						List<CollectionSkus> collSkus = outfitManager.getCollectionSkus(productCode);
						if (collSkus != null) {
							for(CollectionSkus collectSkus: collSkus){
								String strCollSkus = collectSkus.getSkuCode();
								lstSelCollectionSKUs.add(strCollSkus);
							}
						}
						if (request.getSession().getAttribute(
								"CHILD_CAR_SKU_LIST") != null) {
							viewChildCarsSkuList = (ArrayList<ChildCar>) request
									.getSession().getAttribute(
											"CHILD_CAR_SKU_LIST");
						}
						viewChildCarsSkuList = getChildCarSkus(carOutfitChild);
						request.getSession().setAttribute("CHILD_CAR_SKU_LIST",
								viewChildCarsSkuList);
						/* Added for Sku split based on CAR Id*/
						long tempSkuCarId = 0;
						int groupCarCounter = -1;
						int childCarCounter = 0;
						for (ChildCar vSku : viewChildCarsSkuList) {

							ChildCar childCarSkuList = new ChildCar();
							long skuCarId = vSku.getSkuCarid();
							Car carObj = outfitManager.getCarFromId(skuCarId);
							long carStyleId = carObj.getVendorStyle()
									.getVendorStyleId();
							if (tempSkuCarId == 0 || tempSkuCarId != carStyleId) {
								if (groupCarCounter != -1 && tempSkuCarId != 0
										&& tempSkuCarId != carStyleId) {
									request
											.setAttribute(
													"viewChildCarsSkuList_"
															+ groupCarCounter,
													viewChildCarsSkuListArray[groupCarCounter]);
								}
								tempSkuCarId = carStyleId;
								groupCarCounter++;
								viewChildCarsSkuListArray[groupCarCounter] = new ArrayList<ChildCar>();
							}

							viewChildCarsSkuList.get(childCarCounter)
									.setParentCarStyleId(new Long(carStyleId));
							childCarSkuList.setSkuCarid(skuCarId);
							childCarSkuList.setCompDt(vSku.getCompDt());
							childCarSkuList.setVendorStyle(vSku
									.getVendorStyle());
							childCarSkuList.setSkuStyleName(vSku
									.getSkuStyleName());
							childCarSkuList.setVendorUpc(vSku.getVendorUpc());
							childCarSkuList.setSkuColor(vSku.getSkuColor());
							childCarSkuList.setColorName(vSku.getColorName());
							childCarSkuList.setSizeName(vSku.getSizeName());
							childCarSkuList.setSkuID(vSku.getSkuID());
							childCarSkuList.setBelkSku(vSku.getBelkSku());
							childCarSkuList.setVendorName(vSku.getVendorName());

							if (lstSelCollectionSKUs != null
									&& lstSelCollectionSKUs.contains(vSku
											.getSkuID()))
								childCarSkuList.setSkuSelValues(Constants.SELECT_SKUCHECKBOX);
							else
								childCarSkuList.setSkuSelValues(Constants.UNSELECT_SKUCHECKBOX);

							viewChildCarsSkuListArray[groupCarCounter]
									.add(childCarSkuList);
							childCarCounter++;
						}
						if (groupCarCounter != -1) {
							request.setAttribute("viewChildCarsSkuList_"
									+ groupCarCounter,
									viewChildCarsSkuListArray[groupCarCounter]);
						}
						request.setAttribute("viewChildCarsSkuListArray",
								viewChildCarsSkuListArray);
						request.setAttribute("viewChildCarsSkuList",
								viewChildCarsSkuList);
					}
				} else {
					log.debug("Outfit Child CARs are null");
				}
			} 
		}
	}
	
	/**
	 * This method perform following steps: 
	 * 1. Get the vendor style of all child car.
	 * 2. Get the child car skus based on the vendor id.
	 * 3.Add all details for each child car sku like:CarId,DueDate ,
	 * vendor name,VendorUpc,ColorCode,ColorName,SizeName in list of ChildCar object.
	 * 
	 * @param carOutfitChild It contain set of CarOutfitChild objects.
	 * @return 	It return list having details of childcar sku.	
	 */
	public List<ChildCar> getChildCarSkus(Set<CarOutfitChild> carOutfitChild) {
		List<ChildCar> viewChildCarsSkuList = new ArrayList<ChildCar>(); 
		List<Car> skuCar = new ArrayList<Car>();
		try {
			for (CarOutfitChild outfitChild : carOutfitChild) {

				if (outfitChild.getStatusCd().equals(Status.ACTIVE)) {
					Car childCar = outfitChild.getChildCar();
					VendorStyle vendorStyle = childCar.getVendorStyle();
					skuCar = outfitManager.getChildCarSkus(vendorStyle);
					for (Car childCarSku : skuCar) {
						Set<VendorSku> carVendorSku = childCarSku
								.getVendorSkus();
						String styleNumStr = childCarSku.getVendorStyle().getVendorStyleNumber();
						for (VendorSku vSku : carVendorSku) {
							ChildCar childCarSkuList = new ChildCar();
							childCarSkuList.setSkuCarid(childCarSku.getCarId());
							childCarSkuList.setCompDt(DateUtils.formatDate(childCarSku.getDueDate()));
							childCarSkuList.setVendorName(vSku.getVendorStyle()
									.getVendor().getName());
							childCarSkuList.setVendorStyle(styleNumStr);							
							childCarSkuList.setSkuStyleName(vSku
									.getVendorStyle().getVendorStyleName());
							childCarSkuList.setVendorUpc(vSku.getBelkUpc());
							childCarSkuList.setSkuColor(vSku.getColorCode());
							childCarSkuList.setColorName(vSku.getColorName());
							childCarSkuList.setSizeName(vSku.getSizeName());

							childCarSkuList.setSkuID(vSku.getBelkSku());
							childCarSkuList.setBelkSku(vSku.getBelkSku());
							viewChildCarsSkuList.add(childCarSkuList);
						}
					}
				}
			}
		} catch (Exception e) {
		}
		return viewChildCarsSkuList;
	}
	
	private void setReturnData(HttpServletRequest request, Car car){
		String carrierAccount=request.getParameter(CARRIERACCOUNT);
		String carrier=request.getParameter("retCarrier");
		if (StringUtils.isNotBlank(carrier)) {
			ShippingType st=(ShippingType)getObjectForStringValue(carrier,ShippingType.class);
			car.setReturnCarrier(st);
			car.setCarrierAccount(carrierAccount);
		}
	}
	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		User user = getLoggedInUser();
		
		boolean isError = false;
		String strError = "";
		Map model = new HashMap();		
		model.put("success", new Boolean(true));
		if(request.getAttribute("errorColorCodeChangeMessage")!=null){
			isError= true;
			model.put("colorCodeError",true);
			model.put("colorCode",request.getAttribute("colorCode"));
			strError="colorCodeError";
		}else if(request.getAttribute("sampleRequestedError")!=null){
			isError= true;
			model.put("sampleRequestedError",true);
			strError="sampleRequestedError";
		}
		if(isError){
			if (this.isAjax(request)) {
				request.setAttribute("jsonObj", new JSONObject(model));
				return new ModelAndView(this.getAjaxView());
			}else{
				request.setAttribute("colorCodeErrorMsg", strError);
				return new ModelAndView("carEdit");
			}
		}
		
		String imageSubmit = request.getParameter("imageSubmit");
		String imageSave = request.getParameter("imageSave");
		String deleteImage = request.getParameter("deleteImage");
		String deleteRequestImage = request.getParameter("deleteRequestImage");
		String imageRequestSubmit = request.getParameter("imageRequestSubmit");
		String imageRequestSave = request.getParameter("imageRequestSave");
		String mgCarSaveBut = request.getParameter("Save Car");
		String resyncAttr = request.getParameter("resyncAttr");
		String resetClass = request.getParameter("resetClass");
        String resetDept = request.getParameter("resetDepartment");
		String submitCarBut = request.getParameter("submitCar");
		String imageId = request.getParameter("image.imageId");
		String reqImageId=request.getParameter("reqImageId");
		String transitionId = request.getParameter("transtionId");
		String deleteCar = request.getParameter("deleteCar");
		String undeleteCar = request.getParameter("undeleteCar");
		String activateCar = request.getParameter("activateCar");
		String submitWebmerchantCar = request.getParameter("submitWebmerchantCar");
		String updateDueDate = request.getParameter("updateDueDate");
		String updateShipDate = request.getParameter("updateShipDate");
		String imagesRecievedFromPineville = request.getParameter("ImagesRecievedFromPineville");
		String childCarId=request.getParameter("child_car_id");
		
		boolean carSubmittedByWebmerchant = "Submit".equals(submitWebmerchantCar);

		//String webMerchantTransitionId = request.getParameter("webMerchantTransitionId");

		if (log.isInfoEnabled())
			log.info("Got into DashBoardFormController");
		Car car = (Car) command;
		
		
		/*Scenario START: to ignore user modification for those attributes that are to be processed by resynch job*/ 
		Set<CarAttribute> oldCarAttributes = new HashSet<CarAttribute>();
		Set<CarAttribute> bakupAttrValuesFromForm = car.getCarAttributes();
		if(bakupAttrValuesFromForm!=null){
			log.info("Size Of bakupAttrValuesFromForm variable:"+bakupAttrValuesFromForm.size());
		}
		
		HttpSession session = request.getSession();
		try{
			Map<String,String> backupCarAttrsObject = (Map<String,String>)session.getAttribute("backUpCarBeforeEdit");//different name to be given
			List<Long> unprocessedAttrIds = attributeManager.getUnprocessedAttrIDs();
			for(CarAttribute ca: car.getCarAttributes()){
			 if(unprocessedAttrIds.contains(ca.getAttribute().getAttributeId())){
					
					if(backupCarAttrsObject	!=null){
						String oldVal = backupCarAttrsObject.get(String.valueOf(ca.getCarAttrId()));
						System.out.println(ca.getAttrValue()+" is a form value different than db value i.e variable attrBackupValue?:"+oldVal);
						//ca.setAttrValue(ca.getAttrValue()+"_SAI");
						ca.setAttrValue(oldVal);
					}
				
			 }
			oldCarAttributes.add(ca);
			}//forloop ends
			car.getCarAttributes().clear();
			car.getCarAttributes().addAll(oldCarAttributes);
			
		}catch(Exception ex){
			//car.getCarAttributes().clear();
			car.getCarAttributes().addAll(bakupAttrValuesFromForm);
			ex.printStackTrace();
		}
		
		
		/*Scenario END: to ignore user modification for those attributes that are to be processed by resynch job*/
		
		
		car.setUpdatedBy(user.getUsername());
		car.setUpdatedDate(new Date());       
		//Added for car quality		
		String carQuality= request.getParameter("cquality");				
		if(carQuality != null){
		if(carQuality.equals("G")){
			car.setCarQualityCode("G");			
		}
		else {
			car.setCarQualityCode("B");			
		}
		}	
		//End of car quality
		if ("Request Image".equals(imageRequestSubmit)) {
			Image img = new Image();
			request.setAttribute("reqImageNote", img);
		}else if ("Save".equals(imageRequestSave)) {
			this.saveVendorImage(request, car, user);
		}else if ("Activate CAR".equals(activateCar)) {
			car.setStatusCd(Status.ACTIVE);
			this.setAuditInfo(request, car);
			this.getCarManager().save(car) ;
			return new ModelAndView(this.getSuccessView());			
		}else if ("Delete CAR".equals(deleteCar)) {  /* -- Start of code modifications for Outfit Management -- */
			long carId=car.getCarId();
			if (car.getSourceType().getSourceTypeCd().equals("OUTFIT")){
			    carManager.removeOutfit(carId);
			}else if (car.getSourceType().getSourceTypeCd().equals(SourceType.PYG)){
			    carManager.removeDBPromotion(carId);
			}else {
				car.setStatusCd(Status.DELETED);
				this.setAuditInfo(request, car);
				this.getCarManager().save(car) ;
		 }
			return new ModelAndView(this.getSuccessView()); /* -- End of code modifications for Outfit Management -- */
	   }else if ("ReActivate CAR".equals(undeleteCar)) {
			car.setStatusCd(Status.ACTIVE);
			this.setAuditInfo(request, car);
			this.getCarManager().save(car);
			return new ModelAndView(this.getSuccessView());			
		}else if ("Delete Image".equals(deleteImage) && !imageId.equals("0")) {
			Long id=new Long(imageId);
			Image image = (Image) this.getCarManager().getFromId(
					Image.class, id);
			image.setStatusCd("INACTIVE");
			image.setUpdatedBy(user.getUsername());
			image.setUpdatedDate(new Date());
			this.getCarManager().save(image);
		}else if ("Delete Image".equals(deleteRequestImage)&& !reqImageId.equals("0")) {
			Long id=new Long(reqImageId);
			Image image = (Image) this.getCarManager().getFromId(
					Image.class, id);
			image.setStatusCd("INACTIVE");
			image.setUpdatedBy(user.getUsername());
			image.setUpdatedDate(new Date());
			this.getCarManager().save(image);
		}else if ("Pickup Image".equals(imageSubmit)) {
			Image img = new Image();
			request.setAttribute("editImage", img);
		}else if ("Update".equals(updateDueDate)) {
			String changeDate = request.getParameter("changeDate");
			if (StringUtils.isNotBlank(changeDate)) {
				Date dueDate = DateUtils.parseDate(changeDate, DATEFORMAT);
				if (!dueDate.equals(car.getDueDate())) {
					car.setDueDate(dueDate);
				}
			}
		}
		else if ("Update Ship Date".equals(updateShipDate)) {
			String shipDate = request.getParameter("changeShipDate");
			if (StringUtils.isNotBlank(shipDate)) {
				Date expectedShipDate = DateUtils.parseDate(shipDate, DATEFORMAT) ;
				if (!expectedShipDate.equals(car.getExpectedShipDate())) {
					car.setExpectedShipDate(expectedShipDate);
				}
			}
		}
		else if ("Resync Attributes".equals(resyncAttr)) {
			if(car.getSourceType().getSourceTypeCd().equals(SourceType.PYG)){
				this.dbPromotionManager.resyncDBPromotionAttributes(car, this.getLoggedInUser());
			}else if(car.getSourceType().getSourceTypeCd().equals(SourceType.OUTFIT)){
				this.outfitManager.resyncOutfitAttributes(car, this.getLoggedInUser());
			}else{
				this.carManager.resyncAttributes(car, this.getLoggedInUser()) ;
			}
			this.getCarManager().save(car) ;
			return new ModelAndView("redirect:editCarForm.html?carId="+ car.getCarId());
		}
		else if ("Reset Classification".equals(resetClass)) {
			//this.carManager.resyncAttributes(car, this.getLoggedInUser()) ;
			String newClassNumber = request.getParameter("classNumber") ;
			if (!StringUtils.isEmpty(newClassNumber) && !String.valueOf(car.getVendorStyle().getClassification().getBelkClassNumber()).equals(newClassNumber)) {
				Classification newClass = this.getCarManager().getClassification(Short.parseShort(newClassNumber));
				if (newClass != null) {
        			car.getVendorStyle().setClassification(newClass);
        			
        			/** CARS-35: Check if multiple productTypes exist for new class. **/
        			List<ProductType> productTypeList = this.carManager.getProductTypesByClass(newClass.getBelkClassNumber());
        			ProductType productType = null;
        			if (productTypeList != null && productTypeList.size() == 1) {
        				productType = productTypeList.get(0);
        			}
        			/** end of CARS-35 **/
        			
					car.getVendorStyle().setProductType(productType) ;
					this.getCarManager().save(car) ;
				}
			}
			return new ModelAndView(this.getSuccessView());			
		} else if ("Reset Department".equals(resetDept)) {
			String dept = request.getParameter("departmentNo");

			if (!StringUtils.isEmpty(dept)
					&& StringUtils.isNumeric(dept)
					&& !String.valueOf(car.getDepartment().getDeptCd()).equals(dept)) {
				Department newDept = this.getCarManager().getDepartment(dept);

				if (newDept != null) {
					car.setDepartment(newDept);
					String newClassNumber = request
							.getParameter("deptClassNumber");
					if (!StringUtils.isEmpty(newClassNumber)
							&& StringUtils.isNumeric(newClassNumber)
							&& !String.valueOf(
									car.getVendorStyle().getClassification()
											.getBelkClassNumber()).equals(
									newClassNumber)) {
						Classification newClass = this.getCarManager()
								.getClassification(
										Short.parseShort(newClassNumber));

						if (newClass != null) {
							car.getVendorStyle().setClassification(newClass);
							
							/** CARS-35: Check if multiple productTypes exist for new class. **/
		        			List<ProductType> productTypeList = this.carManager.getProductTypesByClass(newClass.getBelkClassNumber());
		        			ProductType productType = null;
		        			if (productTypeList != null && productTypeList.size() == 1) {
		        				productType = productTypeList.get(0);
		        			}
		        			/** end of CARS-35 **/
		        			
							car.getVendorStyle().setProductType(productType);
						}
					}

					this.getCarManager().save(car);
				}
			}

			return new ModelAndView(this.getSuccessView());
		}
		else if ("Save".equals(imageSave)) {
            setReturnData(request,car);
			this.saveOnHandImage(request, imageId, car, user);
			this.saveVendorImage(request, car, user);
			request.setAttribute("detailCar", car);
		}
		
		else if ("Recieved Images From Pineville".equals(imagesRecievedFromPineville)) {
			this.createSampleImages(request, car);
		}
		
		// user hits save button
		else if ("Save Car".equals(mgCarSaveBut)) {
			if (log.isDebugEnabled())
				log.debug("in saving the page");
			this.saveOnHandImage(request, imageId, car, user);
			this.saveVendorImage(request, car, user);			
		}
		// user hits submit button
		else if ("Submit".equals(submitCarBut) || carSubmittedByWebmerchant) {
		//	WorkflowStatus currentStatus = car.getCurrentWorkFlowStatus();
		//	UserType currentUserType = car.getAssignedToUserType();
			String wmUserType = request.getParameter("wmWorkflowUserType");
			String wmWorkflowStatus = request.getParameter("wmWorkflowStatus"); ;
			WorkflowTransition wt = null;
			WorkflowTransitionInfo tInfo = null ;
			boolean workflStatusChange=false;
			if (carSubmittedByWebmerchant
					&& user.getUserTypeCd().equals(UserType.BUYER)
					&& StringUtils.isNotBlank(wmUserType) && StringUtils.isNotBlank(wmWorkflowStatus)) {
				tInfo = workflowManager.getNextWorkFlowTransitionInformation(wmUserType, wmWorkflowStatus);
				if (tInfo != null && tInfo.getWorkflowTransitionList() != null
						&& !tInfo.getWorkflowTransitionList().isEmpty()) {
					wt = tInfo.getWorkflowTransitionList().get(0);
				}
			} else if (StringUtils.isNotBlank(transitionId)) {
				wt = workflowManager.getWorkflowTransition(Long.parseLong(transitionId));
			}
			
			// Below code for buyer workflow as part of VIP project
			// if BUYER is sending the CAR to WEB_MERCHANT then check whether all images for car are available or requested
			//Since Removed WEB MERHCANT , IF BUYER Submited cars to Art director samapale coordinator and Content Manager , Image vliadation requried 
			if(wt != null && (wt.getTransitionToUserType().getUserTypeCd().equalsIgnoreCase(UserType.SAMPLE_COORDINATOR)
					||wt.getTransitionToUserType().getUserTypeCd().equalsIgnoreCase(UserType.ART_DIRECTOR)
					|| wt.getTransitionToUserType().getUserTypeCd().equalsIgnoreCase(UserType.CONTENT_MANAGER))){
				if(car.getAssignedToUserType().getUserTypeCd().equalsIgnoreCase(UserType.BUYER) && checkImageValidationOnSubmit(car,request)){
					request.setAttribute("detailCar", car);
					return new ModelAndView("carEdit");
				}
			}
			if (wt != null) {
				WorkflowStatus currentWorkFlowStatus=null;
				UserType assignedToUserTypeCd=null;
				Date today = new Date();
				Calendar cal2 = Calendar.getInstance();
				// changed the code to update escalation date according to new due date rules
				// number of days to complete the task is number of days from due date 
				int numberOfDaysToCompleteTask = (int) ((car.getDueDate().getTime() - today.getTime())/(1000*60*60*24));
				
				//Based on rule by Sara, 7 Days from creation for VENDORs
				//Transitioning to a VENDOR by a Webmerchant
				if (carSubmittedByWebmerchant && UserType.VENDOR.equals(wmUserType)) {
					//numberOfDaysToCompleteTask = 7 ;
					cal2.add(Calendar.DAY_OF_YEAR, 7) ;
				}else if (wt.getCurrentUserType().getUserTypeCd().equals(UserType.VENDOR)) {
					//Notify VENDOR 7 Days after TASK creation OR 1 Day Before Due Date
						if (numberOfDaysToCompleteTask > 7) {
							//Notify the VENDOR 7 Days after Task CREATION Date
							cal2.add(Calendar.DAY_OF_YEAR, 7) ;
						} else {
							//Notify the VENDOR 1 Day before Due Date
							cal2.add(Calendar.DAY_OF_YEAR, numberOfDaysToCompleteTask-1) ;
						}
					} else {
					  //For all other User Type, Remind 1 Day Before Due Date
					  cal2.add(Calendar.DAY_OF_YEAR,numberOfDaysToCompleteTask-1);
					}
				if(log.isInfoEnabled()){
					log.info("Send to Vendor to complete childCarId:"+ childCarId);
				}
				if (StringUtils.isNotBlank(childCarId)){
					if(car.getSourceType().getSourceTypeCd().equals(SourceType.OUTFIT)){	
						outfitManager.assignOutfitToVendor(car,childCarId);
					}else if(car.getSourceType().getSourceTypeCd().equals(SourceType.PYG)){
						getDbPromotionManager().assignDBPromotionToVendor(car,childCarId);
					}
				}
				if(log.isInfoEnabled()){
					log.info("Setting escalation date of car ID "+ car.getCarId()  + " to : "+ cal2.getTime());
				}
				car.setEscalationDate(cal2.getTime());
				
				if (carSubmittedByWebmerchant && tInfo != null) {
					//Update WorkflowStatus Updated Date when Status Changes
					if (!car.getCurrentWorkFlowStatus().getStatusCd().equals(wmWorkflowStatus)) {
						car.setLastWorkflowStatusUpdateDate(Calendar.getInstance().getTime()) ;
						workflStatusChange=true;
					}
					//car.setCurrentWorkFlowStatus((WorkflowStatus)this.getCarLookupManager().getById(WorkflowStatus.class, wmWorkflowStatus));
					currentWorkFlowStatus = (WorkflowStatus)this.getCarLookupManager().getById(WorkflowStatus.class, wmWorkflowStatus);
					assignedToUserTypeCd = (UserType)this.getCarLookupManager().getById(UserType.class, wmUserType);
					//car.setAssignedToUserType((UserType)this.getCarLookupManager().getById(UserType.class, wmUserType));
				} else {
					//Update WorkflowStatus Updated Date when Status Changes
					if (!car.getCurrentWorkFlowStatus().getStatusCd().equals(wt.getTransitionStatus().getStatusCd())) {
						car.setLastWorkflowStatusUpdateDate(Calendar.getInstance().getTime()) ;
						workflStatusChange=true;
					}
					car.setCurrentWorkFlowStatus(wt.getTransitionStatus());
					car.setAssignedToUserType(wt.getTransitionToUserType());
				}
				
				// Code added as part of VIP project to chnage the Content Status of CAR
				// if CAR is being sent to Vendor provided image, pending status then change the content status of CAR to sent to CMP so that Copy task can go to CMP. 
				String currentWorkflowStatus=car.getCurrentWorkFlowStatus().getStatusCd();
				String currentAssignedUser=car.getAssignedToUserType().getUserTypeCd();
				String contentStatus=car.getContentStatus().getCode();

				if(workflStatusChange 
						&& currentWorkflowStatus.equalsIgnoreCase(WorkflowStatus.PENDING)
							&& currentAssignedUser.equalsIgnoreCase(UserType.VENDOR_PROVIDED_IMAGE)){
					if(contentStatus.equalsIgnoreCase(ContentStatus.APPROVAL_REQUESTED) 
							|| contentStatus.equalsIgnoreCase(ContentStatus.APPROVED)
								|| contentStatus.equalsIgnoreCase(ContentStatus.IN_PROGRESS)
									|| contentStatus.equalsIgnoreCase(ContentStatus.REJECTED)){
						car.setContentStatus((ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, ContentStatus.PUBLISHED));
					}else if(contentStatus.equalsIgnoreCase(ContentStatus.SENT_TO_CMP)
							   || contentStatus.equalsIgnoreCase(ContentStatus.RESEND_DATA_TO_CMP)){
						car.setContentStatus((ContentStatus) this.getCarLookupManager().getById(ContentStatus.class, ContentStatus.RESEND_TO_CMP));
					}
				}
				if (currentWorkFlowStatus != null) {
					car.setCurrentWorkFlowStatus(currentWorkFlowStatus);
				}
				if (assignedToUserTypeCd != null) {
					car.setAssignedToUserType(assignedToUserTypeCd);
				}
				if (UserType.VENDOR.equals(car.getAssignedToUserType()
						.getUserTypeCd())) {
					if (log.isDebugEnabled()) {
						log.debug("updating the rejection count for car ");
					}
					int rejectionCt = car.getRejectionCount();
					car.setRejectionCount(rejectionCt+1);
				}
				//UNLOCK THE CAR...
				car.setLock("N");
				this.getCarManager().updateCar(car, user.getUsername(),true);
				return new ModelAndView(this.getSuccessView());
			} else {
				request.setAttribute("workflowTransitionError", "Invalid workflow transition!");
			}
			
			car.setCarUserByAssignedToUserId(null);
			this.carManager.updateCar(car, user.getUsername(),true);
			return new ModelAndView(this.getSuccessView());
		}

		setReturnData(request,car);
		this.getCarManager().save(car);
		this.putItemsIntoRequest(request, car, user);
		
		if (this.isAjax(request)) {
			Map model2 = new HashMap();		
			model2.put("success", new Boolean(true));
			model2.put("colorCodeError",false);
			model2.put("sampleRequestedError",false);
			request.setAttribute("jsonObj", new JSONObject(model2));
			return new ModelAndView(this.getAjaxView());
		} else {
			return new ModelAndView("carEdit");
		}
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {
	}

	public Set getCommand() {
		return command;
	}

	public void setCommand(Set command) {
		this.command = command;
	}

	private void createSampleImages(HttpServletRequest request, Car car)  {

		Set<Sample> requestedSamples = new HashSet<Sample>() ;
		if (car.getCarSamples() != null && !car.getCarSamples().isEmpty()) {
			for (CarSample cs : car.getCarSamples()) {
				Sample s = cs.getSample() ;
				if (!s.getSampleSourceType().getSampleSourceTypeCd().equals(SampleSourceType.NEITHER)) {
					requestedSamples.add(s) ;
				}
			}
		}
		
		if (log.isDebugEnabled()) {
			log.debug("There are " + requestedSamples.size() + " sample requested for Car: " + car.getCarId()) ;
		}

		if (!requestedSamples.isEmpty()) {
			Set<Sample> sampleImages = new HashSet<Sample>() ;
			for (CarImage ci : car.getCarImages()) {
				if (ci.getImage().getSample() != null) {
					if(!sampleImages.contains(ci.getImage().getSample()))
						sampleImages.add(ci.getImage().getSample()) ;
				}
			}
			
			for(Sample s : requestedSamples) {
				if (!sampleImages.contains(s)) {
					if (log.isDebugEnabled()) {
						log.debug("Image not found for Sample ID: " + s.getSampleId()) ;
					}

					String imageTypeCode = "Y".equals(s.getIsSample()) ? ImageType.MAIN : ImageType.SWATCH;
					//[VENDOR_NUMBER]_[STYLE_NUMBER]_[A-Z SHOT CODE]_[NRF_COLOR_CODE].[EXT]
					String location = s.getVendorStyle().getVendorNumber() + "_" + s.getVendorStyle().getVendorStyleNumber() + "_A_" +  s.getSwatchColor() + ".psd" ;
					if ("Y".equals(s.getIsSwatch()))
						location = s.getVendorStyle().getVendorNumber() + "_" + s.getVendorStyle().getVendorStyleNumber() + "_SW_" +  s.getSwatchColor() + ".psd" ;
					// create and load the image
					Image image = new Image();
					image.setApprovalNotesText("Created Image for photos taken at pineville location!");
					image.setCarImages(new HashSet<CarImage>());
					image.setDescription(" ");
					image.setImageFinalUrl(location);
					image.setImageLocation(location);
					image.setImageLocationType((ImageLocationType)this.getCarManager().getFromId(ImageLocationType.class, ImageLocationType.BELK_FTP_LOCATION));
					image.setImageLocationTypeCd(ImageLocationType.BELK_FTP_LOCATION);
					image.setImageSourceType((ImageSourceType)this.getCarManager().getFromId(ImageSourceType.class, ImageSourceType.FROM_SAMPLE));
					image.setImageSourceTypeCd(ImageSourceType.FROM_SAMPLE);
					image.setImageTrackingStatus(imageReceived);
					image.setImageTrackingStatusCd(ImageTrackingStatus.RECEIVED);
					image.setImageType((ImageType)this.getCarManager().getFromId(ImageType.class, imageTypeCode));
					image.setImageTypeCd(imageTypeCode);
					image.setNotesText("Created Image for photos taken at pineville location!");
					image.setReceivedDate(Calendar.getInstance().getTime());
					image.setRequestDate(Calendar.getInstance().getTime());
					image.setSample(s);
					image.setStatusCd(Status.ACTIVE);
					image.setImageProcessingStatusCd(Image.PROCESSING_STATUS_PENDING) ;
					this.setAuditInfo(request, image) ;

					this.getCarManager().save(image);

					CarImageId cId = new CarImageId();
					cId.setCarId(car.getCarId());
					cId.setImageId(image.getImageId());
					CarImage ci = new CarImage();
					ci.setId(cId);
					ci.setCar(car);
					ci.setImage(image);
					this.setAuditInfo(request, ci) ;
					
					this.getCarManager().save(ci) ;
					
					if (car.getCarImages()!= null)
						car.getCarImages().add(ci);

				}
			}
		}
	}

	/**
	 * Added for CARS Faceted Navigation
	 * Save Facet Super Color Attributes
	 * @param request
	 * @param vendorSku
	 */
	private void saveSKUSuperColors(HttpServletRequest request,VendorSku vendorSku){
		if (log.isDebugEnabled()) {
			log.debug("Inside saveSKUSuperColors method for sku: " +vendorSku.getBelkSku()) ;
		}
		String superColor1=request.getParameter("SuperColor1:" + vendorSku.getCarSkuId());
		String superColor2=request.getParameter("SuperColor2:" + vendorSku.getCarSkuId());
		String superColor3=request.getParameter("SuperColor3:" + vendorSku.getCarSkuId());
		String superColor4=request.getParameter("SuperColor4:" + vendorSku.getCarSkuId());
		
		String colorCode = request.getParameter("carSkuColorCode:" + vendorSku.getCarSkuId());
		if(colorCode==null || "".equals(colorCode)){
			colorCode=vendorSku.getColorCode();
		}
		String drviedSuperColor1=getColorCodeSuperColor(colorCode);
		boolean superColor1Save=false;
		// only save super color1 if super color is different from the derived color from mapping
		if(superColor1!=null && !superColor1.equals(drviedSuperColor1)){
			superColor1Save=true;
		}
		if(log.isDebugEnabled()){
			log.debug("colorCode:"+colorCode);
			log.debug("superColor1:"+ superColor1 +" superColor2:"+ superColor2 + " superColor3:"+ superColor3 + " superColor4:"+ superColor4+" drviedSuperColor1"+drviedSuperColor1);
			log.debug("superColor1Save(true/false):"+ superColor1Save);
		}
		
		CarSkuAttribute carSkuAttr1=null;
		CarSkuAttribute carSkuAttr2=null;
		CarSkuAttribute carSkuAttr3=null;
		CarSkuAttribute carSkuAttr4=null;
		
		Attribute superColorAttr1=outfitManager.getAttributeByName(Constants.ATTR_SUPERCOLOR1);
		Attribute superColorAttr2=outfitManager.getAttributeByName(Constants.ATTR_SUPERCOLOR2);
		Attribute superColorAttr3=outfitManager.getAttributeByName(Constants.ATTR_SUPERCOLOR3);
		Attribute superColorAttr4=outfitManager.getAttributeByName(Constants.ATTR_SUPERCOLOR4);
		
		if (vendorSku.getCarSkuAttributes() != null && !vendorSku.getCarSkuAttributes().isEmpty()) {
			ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(vendorSku.getCarSkuAttributes());
			for(CarSkuAttribute skuAttr: skuAttrList) {
				if (superColorAttr1.getName().equals(skuAttr.getAttribute().getName())){
					carSkuAttr1=skuAttr;
				} else if (superColorAttr2.getName().equals(skuAttr.getAttribute().getName())){
					carSkuAttr2=skuAttr;
				} else if (superColorAttr3.getName().equals(skuAttr.getAttribute().getName())){
					carSkuAttr3=skuAttr;
				} else if (superColorAttr4.getName().equals(skuAttr.getAttribute().getName())){
					carSkuAttr4=skuAttr;
				}
			}
		}

		if(carSkuAttr1==null){
			if(!superColor1.equals(Constants.INDEX_NOTSELECTED) && superColor1Save){
				carSkuAttr1=new CarSkuAttribute();
				carSkuAttr1.setAttribute(superColorAttr1);
				//carSkuAttr1.setAttrValue(superColor1);
				carSkuAttr1.setAttrValue(drviedSuperColor1);
				carSkuAttr1.setVendorSku(vendorSku);
				this.setAuditInfo(request, carSkuAttr1);
				vendorSku.getCarSkuAttributes().add(carSkuAttr1);
			}
		} else {
			if(!superColor1.equals(Constants.INDEX_NOTSELECTED)){
				//carSkuAttr1.setAttrValue(superColor1);
				carSkuAttr1.setAttrValue(drviedSuperColor1);
				this.setAuditInfo(request, carSkuAttr1);
				vendorSku.getCarSkuAttributes().add(carSkuAttr1);
			}else{
				if(log.isDebugEnabled()){
					log.debug("Removed the Super Color1");
				}
				vendorSku.getCarSkuAttributes().remove(carSkuAttr1);
			}
		}
		if(carSkuAttr2==null){
			if(!superColor2.equals(Constants.INDEX_NOTSELECTED)){
				carSkuAttr2=new CarSkuAttribute();
				carSkuAttr2.setAttribute(superColorAttr2);
				carSkuAttr2.setAttrValue(superColor2);
				carSkuAttr2.setVendorSku(vendorSku);
				this.setAuditInfo(request, carSkuAttr2);
				vendorSku.getCarSkuAttributes().add(carSkuAttr2);
			}
		}else {
			if(!superColor2.equals(Constants.INDEX_NOTSELECTED)){
				carSkuAttr2.setAttrValue(superColor2);
				this.setAuditInfo(request, carSkuAttr2);
				vendorSku.getCarSkuAttributes().add(carSkuAttr2);
			}else{
				if(log.isDebugEnabled()){
					log.debug("Removed the Super Color2");
				}
				vendorSku.getCarSkuAttributes().remove(carSkuAttr2);
			}
		}
		if(carSkuAttr3==null){
			
			if(!superColor3.equals(Constants.INDEX_NOTSELECTED)){
				carSkuAttr3=new CarSkuAttribute();
				carSkuAttr3.setAttribute(superColorAttr3);
				carSkuAttr3.setAttrValue(superColor3);
				carSkuAttr3.setVendorSku(vendorSku);
				this.setAuditInfo(request, carSkuAttr3);
				vendorSku.getCarSkuAttributes().add(carSkuAttr3);
			}
		}else{
			if(!superColor3.equals(Constants.INDEX_NOTSELECTED)){
				carSkuAttr3.setAttrValue(superColor3);
				this.setAuditInfo(request, carSkuAttr3);
				vendorSku.getCarSkuAttributes().add(carSkuAttr3);
			}else{
				if(log.isDebugEnabled()){
					log.debug("Removed the Super Color3");
				}
				vendorSku.getCarSkuAttributes().remove(carSkuAttr3);
			}
		}
		
		if(carSkuAttr4==null){
			if(!superColor4.equals(Constants.INDEX_NOTSELECTED)){
				carSkuAttr4=new CarSkuAttribute();
				carSkuAttr4.setAttribute(superColorAttr4);
				carSkuAttr4.setAttrValue(superColor4);
				this.setAuditInfo(request, carSkuAttr4);
				carSkuAttr4.setVendorSku(vendorSku);
				vendorSku.getCarSkuAttributes().add(carSkuAttr4);
			}
		}else {
			if(!superColor4.equals(Constants.INDEX_NOTSELECTED)){
				carSkuAttr4.setAttrValue(superColor4);
				this.setAuditInfo(request, carSkuAttr4);
				vendorSku.getCarSkuAttributes().add(carSkuAttr4);
			}else{
				if(log.isDebugEnabled()){
					log.debug("Removed the Super Color4");
				}
				vendorSku.getCarSkuAttributes().remove(carSkuAttr4);
			}
		}
		
		if(log.isDebugEnabled()){
			log.debug("Super Color Attributes are saved");
		}
		this.carManager.save(vendorSku);
		
    }
	
	
	/**
	 * This method perform following steps: 1.Get the swatch description from attribute table based on 
	 * active status and attribute name.  2. get arraylist of car sku attribute
	 * 3. save the details of new swatch for each skus  and for 
	 * existing swatch update the details ,in vendor sku table.
	 *  
	 * 
	 * @param request It contain HttpServletRequest object.
	 * @param vendorSku It contain  sku object
	 * @param colorAndSwatch It contain map of vendorstyle id and color code.
	 */
	private void saveShadeDetails(HttpServletRequest request,
			VendorSku vendorSku, Map<String, String> colorAndShade) {
		if (log.isDebugEnabled()) {
			log.debug("Inside saveShadeDetails method for sku: "+ vendorSku.getBelkSku());
		}
		String keyValue = "Style:"
				+ vendorSku.getVendorStyle().getVendorStyleId() + "-ColorCode:"
				+ vendorSku.getColorCode();
		String skuShadeDesc = (String) colorAndShade.get(keyValue);
		CarSkuAttribute carSkuAttr1 = null;
		boolean superColor1Save = false;
		// only save super color1 if super color is different from the derived
		// color from mapping
		if (skuShadeDesc != null) {
			superColor1Save = true;
		}
		Attribute shadeDesc = outfitManager
				.getAttributeByName(Constants.ATTR_SHADEDESC);

		ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(
				vendorSku.getCarSkuAttributes());
		if (vendorSku.getCarSkuAttributes() != null
				&& !vendorSku.getCarSkuAttributes().isEmpty()) {
			for (CarSkuAttribute skuAttr : skuAttrList) {

				if (shadeDesc.getName().equals(
						skuAttr.getAttribute().getName())) {
					carSkuAttr1 = skuAttr;
				}
			}
		}
		if (carSkuAttr1 == null) {
			if (skuShadeDesc != null
					&& !skuShadeDesc.equals(Constants.INDEX_NOTSELECTED)
					&& superColor1Save) {
				carSkuAttr1 = new CarSkuAttribute();
				carSkuAttr1.setAttribute(shadeDesc);
				carSkuAttr1.setAttrValue(skuShadeDesc);
				carSkuAttr1.setVendorSku(vendorSku);
				this.setAuditInfo(request, carSkuAttr1);
				vendorSku.getCarSkuAttributes().add(carSkuAttr1);
			}
		} else {
			if (skuShadeDesc != null
					&& !skuShadeDesc.equals(Constants.INDEX_NOTSELECTED)) {
				carSkuAttr1.setAttribute(shadeDesc);
				carSkuAttr1.setAttrValue(skuShadeDesc);
				carSkuAttr1.setVendorSku(vendorSku);
				this.setAuditInfo(request, carSkuAttr1);
				vendorSku.getCarSkuAttributes().add(carSkuAttr1);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Swatch Description Attributes are saved");
		}
		this.carManager.save(vendorSku);
	}	

	/**
	 * This method performs following steps: 1.Get the sku start date  description from attribute table based on 
	 * active status and attribute name.  2. get the  arraylist of  car sku attribute
	 * 3. save the details of new start date , for each skus  and for 
	 * existing  update the details ,in vendor sku table.
	 *  
	 * @param request It contain HttpServletRequest object.
	 * @param vendorSku It contain vendor sku object
	 * @param colorAndSwatch It contain map of vendorstyle id and color code.
	 */
	private void saveSkuStartDateDetails(HttpServletRequest request,
			VendorSku vendorSku) {
		if (log.isDebugEnabled()) {
			log.debug("Inside saveSkuStartDateDetails method for sku: "+ vendorSku.getBelkSku());
		}
		String skuStartDateVal = request.getParameter("carSkuStartDate:"
				+ vendorSku.getCarSkuId());
		CarSkuAttribute skuStartDateAttr = null;
		boolean superColor1Save = false;
		// only save super color1 if super color is different from the derived
		// color from mapping
		if (skuStartDateVal != null) {
			superColor1Save = true;
		}
		Attribute skuStartDate = outfitManager
				.getAttributeByName(Constants.ATTR_SKU_START_DATE);

		ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(
				vendorSku.getCarSkuAttributes());
		if (vendorSku.getCarSkuAttributes() != null
				&& !vendorSku.getCarSkuAttributes().isEmpty()) {
			for (CarSkuAttribute skuAttr : skuAttrList) {
				if (skuStartDate.getName().equals(
						skuAttr.getAttribute().getName())) {
					skuStartDateAttr = skuAttr;
				}
			}
		}
		if (skuStartDateAttr == null) {
			if (skuStartDateVal != null
					&& !skuStartDateVal.equals(Constants.INDEX_NOTSELECTED)
					&& superColor1Save) {
				skuStartDateAttr = new CarSkuAttribute();
				skuStartDateAttr.setAttribute(skuStartDate);
				skuStartDateAttr.setAttrValue(skuStartDateVal);
				skuStartDateAttr.setVendorSku(vendorSku);
				this.setAuditInfo(request, skuStartDateAttr);
				vendorSku.getCarSkuAttributes().add(skuStartDateAttr);
			}
		} else {
			if (skuStartDateVal != null
					&& !skuStartDateVal.equals(Constants.INDEX_NOTSELECTED)) {
				skuStartDateAttr.setAttribute(skuStartDate);
				skuStartDateAttr.setAttrValue(skuStartDateVal);
				skuStartDateAttr.setVendorSku(vendorSku);
				this.setAuditInfo(request, skuStartDateAttr);
				vendorSku.getCarSkuAttributes().add(skuStartDateAttr);
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Sku Start Date Attributes are saved");
		}
		this.carManager.save(vendorSku);
	}	
	
	/**
	 * Method to get the super color name for the given color code
	 * @param colorCode
	 * @return
	 */
	private String getColorCodeSuperColor(String colorCode){
		int color=Integer.parseInt(colorCode);
		int beginColor;
		int endColor;
		String superColor=null;
		for(ColorMappingMaster mapping:superColorList){
			beginColor=Integer.parseInt(mapping.getColorCodeBegin());
			endColor=Integer.parseInt(mapping.getColorCodeEnd());
			if(color >= beginColor && color <= endColor){
				superColor=mapping.getSuperColorName();
			}
		}
		return superColor;
	}
	
	/**
	 * Added by AFUSYS3 for VIP 
	 * Code used for updating table on approval/rejection and removal of vendor provided images 
	 * @param car: car object to be updated
	 * @param imageId: Image.Image_id which needs to be updated/rejected
	 * @param venImageOper : operation to be performed on ven image  eg: approve/reject/delete
	 * @param user: user email id
	 * 
	 * */	
	public void updateVendorImageStatus(Car car, String imageId, String venImageOper, User user) {
			if (!StringUtils.isNotBlank(imageId)) {
				return;
			}
			Image img = (Image) this.getCarLookupManager().getById(Image.class, new Long(imageId));
			VendorImage vimg = (img == null) ? null :img.getVendorImage(); 
			if(null != vimg){
				
				if ("APPROVED".equals(venImageOper) || "REJECTED".equals(venImageOper)) {
					vimg.setBuyerApproved(venImageOper);
					if("APPROVED".equals(venImageOper)){
						vimg.setApprovedDate(new Date());
					}else{
						//Add note in CAR note section 
						StringBuilder sb = new StringBuilder();
						sb.append("Type of failure: Buyer rejected ");
						sb.append("\t Image Name: "+ img.getImageLocation());
						NoteType carNoteType = (NoteType) this.getCarLookupManager().getById(NoteType.class, NoteType.CAR_NOTES);
						CarNote note = new CarNote();
						note.setNoteType(carNoteType);
						note.setIsExternallyDisplayable(CarNote.FLAG_NO);
						note.setStatusCd(Status.ACTIVE);
						note.setCar(car);
						note.setNoteText(sb.toString());
						note.setAuditInfo(user);
						car.getCarNotes().add(note);
					}
					if ("REJECTED".equals(venImageOper)) {
						vimg.setIsImageOnMC("N");						
					}
					vimg.setAuditInfo(user);
					this.getCarManager().save(vimg);
					if(log.isInfoEnabled()){
						log.info("Approved/Rejected the vendor image" + vimg.getVendorImageId());
					}

				}else if ("REMOVED".equals(venImageOper)) {
					if(log.isInfoEnabled()){
						log.info("DELETED the vendor image" + vimg.getVendorImageId());
					}
					//If image deleted is pending for Mechanical check, then update the CAR.IMAGE_MC_PENDING_BY_USER flag 
					String strPrevVenImageStatus = vimg.getVendorImageStatus().getVendorImageStatusCd();
					VendorImageStatus stat_deleted =  (VendorImageStatus) this.getCarLookupManager().getById(VendorImageStatus.class, VendorImageStatus.DELETED);
					img.setStatusCd(Status.DELETED);
					img.setAuditInfo(user);
					vimg.setVendorImageStatus(stat_deleted);
					vimg.setAuditInfo(user);
					
					this.getCarManager().save(vimg);
					// Delete Removed image from BelkMacl Server
					this.getCarManager().deleteBelkMaclVendorImage(img);
					// If current vendor image which we are deleting is the only image which was 
					// pending for mechanical check, then change the car.IMAGE_MC_PENDING_BY_USER To NONE
					if ( VendorImageStatus.UPLOADED.equals(strPrevVenImageStatus)
						    || VendorImageStatus.REUPLOADED.equals(strPrevVenImageStatus)
							|| VendorImageStatus.SENT_TO_MQ.equals(strPrevVenImageStatus)) {
						
						boolean isImagePendingForMc = this.getCarManager().checkImageMCPendingByUserFlag(car);
						if(!isImagePendingForMc){
							car.setImageMCPendingByUser("NONE");
						}
					}
					
					//Remove current vendor image id from CAR.IMAGE_FAILED_IN_CC column
					 String strImageFailedInCC=car.getImagesFailedInCCPending();  
					 String strVenImageId = String.valueOf(vimg.getVendorImageId());
					 StringBuffer sb=new StringBuffer();
					 boolean isCurrVenImageFailedInCC = strImageFailedInCC == null? false:strImageFailedInCC.contains(strVenImageId);
					 if(isCurrVenImageFailedInCC){
                         ArrayList<String> items = new ArrayList<String>(Arrays.asList(strImageFailedInCC.split(",")));                                          
                         for(String str:items){
                        	 if(!str.equals(strVenImageId)){
                        		 sb.append(str+",");
                        	 }
                         }
                         if(sb.toString().endsWith(",")) {
          	              	sb.setLength(sb.length() - 1);  
          	             }
                         car.setImagesFailedInCCPending(sb.toString());
                         //ImagesFailedInCCPending value holds the VENDOR_IMAGE_ID's separated 
                         //by Comma which are failed in Creative Check are Not on MC.
				}
				//END - of Image removed condition
			}
			// If current image which is Approved/Rejected/Deleted is the last
			// image waiting for buyer approval
			// then switch the CAR.BUYER_APPROVAL_PENDING flag
			if ("APPROVED".equals(venImageOper) || "REMOVED".equals(venImageOper)
				                            	|| "REJECTED".equals(venImageOper)) {
				String strBuyerApprovalPending = this.getCarManager()
						.checkBuyerApprovalPendingFlag(car);
				// making sure method returns only Y or N
				strBuyerApprovalPending = "Y".equals(strBuyerApprovalPending)? "Y"
						: "N";
				car.setBuyerApprovalPending(strBuyerApprovalPending);
			}
		    }
		}

	
	/**
	 * Method to do validations of Images on Submit
	 * This method returns false if validation passes.
	 * 
	 *  Validation cariteria:
	 *  Buyer should be able to submit CAR to web-merchant only if at least one image for each style color combination is requested in CAR 
	 *  and vendor image should be waiting for buyer approval (means images should be in approved/rejected status).
	 *  also, all vendor provided image should be on media compass 
	 */
	
	private boolean checkImageValidationOnSubmit(Car car,
			HttpServletRequest request) {
		if (log.isDebugEnabled()) {
			log.debug("Start of checkImageValidationOnSubmit method "
					+ car.getCarId());
		}

		ArrayList<CarImage> pickUpImages = car.getAllActiveCarImages();
		ArrayList<CarImage> uploadedImages = car.getCarVendorImages();
		ArrayList<CarSample> carSample = car.getCarSampleList();
		Set<String> styleIdColor = new HashSet<String>();
		// newStyleColor is map for all requested images and Approved vendor images
		Set<String> newStyleColor = new HashSet<String>(); 
		boolean pickImageRequestFlag = false;
		boolean allImageRequestedFlag = true;
		boolean vendorImageErrFlag = false;
		boolean vendorImageApproveErrFlag = false;

		try {
			for (CarImage ci : pickUpImages) {
				Image i = ci.getImage();
				if (i.getSample() == null
						&& !(ImageSourceType.VENDOR_UPLOADED.equalsIgnoreCase(i
								.getImageSourceTypeCd()) || ImageSourceType.BUYER_UPLOADED
								.equalsIgnoreCase(i.getImageSourceTypeCd()))) {
					pickImageRequestFlag = true;
					break;
				}
			}

			for (CarSample cs : carSample) {
				Sample s = cs.getSample();
				styleIdColor.add(s.getVendorStyle().getVendorStyleId()
						+ s.getSwatchColor());
				if ((VendorStyleType.PRODUCT.equals(car.getVendorStyle().getVendorStyleType().getCode())
						&& VendorStyleType.PRODUCT.equals(s.getVendorStyle().getVendorStyleType().getCode()))
						&& SampleSourceType.NEITHER.equals(s.getSampleSourceType()
						.getSampleSourceTypeCd())) {
					allImageRequestedFlag = false;
				} else {
					newStyleColor.add(s.getVendorStyle().getVendorStyleId()
							+ s.getSwatchColor());
					//adding style+color in map for requested sample
				}
			}

			for (CarImage ci : uploadedImages) {
				Image i = ci.getImage();
				if(VendorStyleType.PRODUCT.equals(car.getVendorStyle().getVendorStyleType().getCode())
						&& VendorStyleType.PRODUCT.equals(i.getVendorImage().getVendorStyle().getVendorStyleType().getCode())) {
						if (ImageSourceType.VENDOR_UPLOADED.equalsIgnoreCase(i
								.getImageSourceTypeCd())) {
							if ("N".equalsIgnoreCase(i.getVendorImage()
									.getIsImageOnMC()) && !("REJECTED".equalsIgnoreCase(i.getVendorImage()
											.getBuyerApproved()))) {
								vendorImageErrFlag = true;
								break;
							}
						}
						if (!("APPROVED".equalsIgnoreCase(i.getVendorImage()
								.getBuyerApproved()) || "REJECTED".equalsIgnoreCase(i
										.getVendorImage().getBuyerApproved()))) {
							vendorImageApproveErrFlag = true;
							break;
						}	
						if("APPROVED".equalsIgnoreCase(i.getVendorImage()
								.getBuyerApproved())){
							newStyleColor.add(i.getVendorImage().getVendorStyle().getVendorStyleId()
									+ i.getVendorImage().getColorCode());
							//Adding style+color to the map for all approved vendor images
						}
				}else{
					newStyleColor.add(i.getVendorImage().getVendorStyle()
							.getVendorStyleId()
							+ i.getVendorImage().getColorCode());
				}
			}
		} catch (Exception e) {
			log.error("Exception in validation   " + e);
		}
		
		

		if (vendorImageApproveErrFlag) {
			request.setAttribute("errorApproveMessage", "errorApproveMessage");
			log.info("Buyer cannot send CAR " + car.getCarId()
					+ "to web-merchant, All car images not approved");
			return true;
		}
		if (vendorImageErrFlag || !(pickImageRequestFlag || styleIdColor.size() == newStyleColor.size() || allImageRequestedFlag)) {
			request.setAttribute("errorMessage", "errorMessage");
			log.info("Buyer cannot send CAR "+ car.getCarId()
							+ "to web-merchant, images not available for all style-color");
			return true;
		}

		return false;
	}
	
	
	/**
	 * @param request
	 * @param styles
	 */
	public void updateVendorStyleNames(HttpServletRequest request,List<VendorStyle> styles){
		Iterator<VendorStyle> iterVStyles = styles.iterator();
		int iterCount = 0;
		try{
		while(iterVStyles.hasNext()){
			VendorStyle editVStyle= (VendorStyle)iterVStyles.next();
			
			String inputStyleName =request.getParameter("EditableStyleName_"+ iterCount);
			if (inputStyleName != null && !inputStyleName.equals("") &&  !editVStyle.getVendorStyleName().equals(inputStyleName)) {
				editVStyle.setVendorStyleName(inputStyleName);
				getCarManager().save(editVStyle);
			}
			
			iterCount++;
		}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	

	/**
	 * Added for CARS DBP
	 * @param request
	 * @param car
	 * @param user
	 */
	public void getDBPromotionCollectionSkus(HttpServletRequest request, Car car, User user) {
		List<ChildCar> viewChildCarsSkuList = new ArrayList<ChildCar>(); 
		List<ChildCar> viewChildCarsSkuListArray[] = (ArrayList<ChildCar>[]) new ArrayList[100];
		//Attribute collectionSkus = outfitManager.getAttributeByName(Constants.COLLECTION_PARENT_PRODUCTS);
		Attribute templateType = dbPromotionManager.getAttributeByName(Constants.DBPROMOTION_TEMP_TYPE);
		if (car != null && !"".equals(car)) {
			Car dbPromotionCar = car;
			if (dbPromotionCar != null) {
				/* Get all the promo child cars*/
				Set<CarDBPromotionChild> carDBPromotionChild = dbPromotionCar.getCarDBPromotionChild();
				if (carDBPromotionChild != null) {
					String templateTypeVal = dbPromotionManager.getAttributeValue(dbPromotionCar, templateType);
					if (templateTypeVal != null
							&& templateTypeVal.equalsIgnoreCase("PYG")) {
						List<String> lstSelCollectionSKUs = new ArrayList<String>();
						// sku as a list to send to getViewChildCarDetails, for priority,color sku 
						String productCode = dbPromotionCar.getVendorStyle().getVendorNumber()+dbPromotionCar.getVendorStyle().getVendorStyleNumber();
						List<DBPromotionCollectionSkus> collSkus = dbPromotionManager.getDBPromotionCollectionSkus(productCode);
						if (collSkus != null) {
							for(DBPromotionCollectionSkus collectSkus: collSkus){
								String strCollSkus = collectSkus.getSkuCode();
								lstSelCollectionSKUs.add(strCollSkus);
							}
						}
						if (request.getSession().getAttribute("CHILD_CAR_SKU_LIST") != null) {
							viewChildCarsSkuList = (ArrayList<ChildCar>) request.getSession().getAttribute("CHILD_CAR_SKU_LIST");
						}
						viewChildCarsSkuList = getPromotionChildCarSkus(carDBPromotionChild);
						
						request.getSession().setAttribute("CHILD_CAR_SKU_LIST",viewChildCarsSkuList);
						/* Added for Sku split based on CAR Id*/
						long tempSkuCarId = 0;
						int groupCarCounter = -1;
						int childCarCounter = 0;
						for (ChildCar vSku : viewChildCarsSkuList) {

							ChildCar childCarSkuList = new ChildCar();
							long skuCarId = vSku.getSkuCarid();
							Car carObj = dbPromotionManager.getCarFromId(skuCarId);
							long carStyleId = carObj.getVendorStyle().getVendorStyleId();
							if (tempSkuCarId == 0 || tempSkuCarId != carStyleId) {
								if (groupCarCounter != -1 && tempSkuCarId != 0 && tempSkuCarId != carStyleId) {
									request.setAttribute("viewChildCarsSkuList_"+ groupCarCounter,viewChildCarsSkuListArray[groupCarCounter]);
								}
								tempSkuCarId = carStyleId;
								groupCarCounter++;
								viewChildCarsSkuListArray[groupCarCounter] = new ArrayList<ChildCar>();
							}

							viewChildCarsSkuList.get(childCarCounter).setParentCarStyleId(new Long(carStyleId));
							childCarSkuList.setSkuCarid(skuCarId);
							childCarSkuList.setCompDt(vSku.getCompDt());
							childCarSkuList.setVendorStyle(vSku
									.getVendorStyle());
							childCarSkuList.setSkuStyleName(vSku
									.getSkuStyleName());
							childCarSkuList.setVendorUpc(vSku.getVendorUpc());
							childCarSkuList.setSkuColor(vSku.getSkuColor());
							childCarSkuList.setColorName(vSku.getColorName());
							childCarSkuList.setSizeName(vSku.getSizeName());
							childCarSkuList.setSkuID(vSku.getSkuID());
							childCarSkuList.setBelkSku(vSku.getBelkSku());
							childCarSkuList.setVendorName(vSku.getVendorName());

							if (lstSelCollectionSKUs != null
									&& lstSelCollectionSKUs.contains(vSku
											.getSkuID()))
								childCarSkuList.setSkuSelValues(Constants.SELECT_SKUCHECKBOX);
							else
								childCarSkuList.setSkuSelValues(Constants.UNSELECT_SKUCHECKBOX);

							viewChildCarsSkuListArray[groupCarCounter]
									.add(childCarSkuList);
							childCarCounter++;
						}
						if (groupCarCounter != -1) {
							request.setAttribute("viewChildCarsSkuList_"
									+ groupCarCounter,
									viewChildCarsSkuListArray[groupCarCounter]);
						}
						request.setAttribute("viewChildCarsSkuListArray",
								viewChildCarsSkuListArray);
						request.setAttribute("viewChildCarsSkuList",
								viewChildCarsSkuList);
					}
				} else {
					log.debug("Outfit Child CARs are null");
				}
			} 
		}
	}
	
	
	
	/**
	 * Added for CARS DBP
	 * @param request
	 * @param dbPromotionCar
	 * @param childVendorNumbers
	 */
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
	
	
	/**
	 * Added for CARS DBP
	 * @param request
	 * @param dbPromotionCar
	 * @param childVendorNumbers
	 */
	public  void setDBPromotionChildForDisplay(HttpServletRequest request ,Car dbPromotionCar,Map<Long,String> childVendorNumbers){

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
				   // Only the Buyer can see the child car vendor numbers.
				   if(UserType.BUYER.equals(dbPromotionCar.getAssignedToUserType().getUserTypeCd())){
					   if(!childVendorNumbers.containsValue(carDBPromotionChild.getChildCar().getVendorStyle().getVendorNumber())){
						   childVendorNumbers.put(carDBPromotionChild.getChildCar().getCarId(),carDBPromotionChild.getChildCar().getVendorStyle().getVendorNumber());
					   }
					}
			   }
		   }
		   request.setAttribute("viewChildCars", viewChildCars);
		   //Placing the childVendorNumbers in request and display on manageCar.jsp
		   request.setAttribute("childVendorNumbers", childVendorNumbers);
	}
	
	
}
