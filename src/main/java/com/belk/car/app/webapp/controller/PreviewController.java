/**
 * 
 */
package com.belk.car.app.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.dto.PrintPreviewDTO;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CarSample;
import com.belk.car.app.model.DBPromotionCollectionSkus;
import com.belk.car.app.model.ImageProvider;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.SampleSourceType;
import com.belk.car.app.model.SampleType;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStyleSearchCriteria;
import com.belk.car.app.model.WorkflowTransitionInfo;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.DBPromotionManager;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.service.WorkflowManager;
import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.util.DateUtils;

public class PreviewController extends BaseFormController {
	private transient final Log log = LogFactory
			.getLog(PreviewController.class);
	private static SampleSourceType onHand;
	private static SampleSourceType vendor;
	private static SampleSourceType neither;
	private static ImageProvider rrDonnelly;
	private static ImageProvider pineVille;
	private static SampleType sampleProduct;
	private static ImageTrackingStatus imageRequested;
	private static ImageTrackingStatus imageReceived;
	private static ImageTrackingStatus imageAvailable;
	private static ImageTrackingStatus imageApproved;
	private static ImageTrackingStatus imageRejected;
	private static SampleType sampleSwatch;
	private static final Long PINEVILLE = 2L;
	private static final Long RRDONNELLY = 1L;
	private static final String PARAMCARID = "carId";
	private static final String PARAMPRINTSKUINFO = "printSkuInfo";
	private static final String CARATTRIBUTE = "carAttribute:";
	private static final String DATEFORMAT = "MM/dd/yyyy";
	private static final String DEFAULTLOCALE = "en";
	
	private String printSkuInfo;
	private WorkflowManager workflowManager;
	private String currentDate = "";
	User user;

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
		pineVille = (ImageProvider) this.getCarManager().getFromId(
				ImageProvider.class, PINEVILLE);
		sampleProduct = (SampleType) this.getCarManager().getFromId(
				SampleType.class, SampleType.PRODUCT);
		sampleSwatch = (SampleType) this.getCarManager().getFromId(
				SampleType.class, SampleType.SWATCH);
		imageRequested = (ImageTrackingStatus) getCarManager().getFromId(
				ImageTrackingStatus.class, ImageTrackingStatus.REQUESTED);
		imageReceived = (ImageTrackingStatus) getCarManager().getFromId(
				ImageTrackingStatus.class, ImageTrackingStatus.RECEIVED);
		imageAvailable = (ImageTrackingStatus) getCarManager().getFromId(
				ImageTrackingStatus.class, ImageTrackingStatus.AVAILABLE);
		imageApproved = (ImageTrackingStatus) getCarManager().getFromId(
				ImageTrackingStatus.class, ImageTrackingStatus.APPROVED);
		imageRejected = (ImageTrackingStatus) getCarManager().getFromId(
				ImageTrackingStatus.class, ImageTrackingStatus.REJECTED);

	}
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		user = getLoggedInUser();
		List<Car> carObjList= new ArrayList<Car>();
		printSkuInfo = request.getParameter(PARAMPRINTSKUINFO);
		String carIds = request.getParameter(PARAMCARID);
		if (StringUtils.isNotBlank(carIds)) {
			String[] carsArray=carIds.split(",");
			if (carsArray.length != 0){ 
				for(int i=0;i<carsArray.length;i++){
					Car car = null;
					car = this.getCarManager().getCarById(new Long(carsArray[i]));
					if (car == null) {
						throw new Exception(
								"Invalid Car: Please provide a valid CAR ID");
					}
					carObjList.add(car);
				}
			}		
		} 
		
		List<PrintPreviewDTO> printpreviewDTOList=new ArrayList<PrintPreviewDTO>();
		for (Car carTemp:carObjList){
			PrintPreviewDTO previewDTO = new PrintPreviewDTO();
			List<VendorStyle> vendorStyles = new ArrayList<VendorStyle>();
			WorkflowStatus currentStatus = carTemp.getCurrentWorkFlowStatus();
			UserType currentUserType = carTemp.getAssignedToUserType();
			WorkflowTransitionInfo wti = workflowManager
					.getNextWorkFlowTransitionInformation(currentUserType.getUserTypeCd(),currentStatus.getStatusCd());
			previewDTO.setWorkflowTransition(wti);
			vendorStyles.add(carTemp.getVendorStyle());
			if (carTemp.getVendorStyle().isPattern()) {
				VendorStyleSearchCriteria criteria = new VendorStyleSearchCriteria();
				criteria.setChildrenOnly(true);
				criteria.setVendorStyleId(carTemp.getVendorStyle().getVendorStyleId());
				vendorStyles.addAll(this.carManager.searchVendorStyle(criteria));
			}
			previewDTO.setVendorStyle(vendorStyles);
			if (carTemp.getSourceType().getSourceTypeCd().equals("OUTFIT")) {
				List<ChildCar> viewChildCars = new ArrayList<ChildCar>();
				Set<CarOutfitChild> outfitChildCars = carTemp.getCarOutfitChild();
				for (CarOutfitChild carOutfitChild : outfitChildCars) {
					if (carOutfitChild.getStatusCd().equals(Status.ACTIVE)) {
					
						Car childCarTemp = carOutfitChild.getChildCar();
						ChildCar childCar = new ChildCar();
						childCar.setCarId(childCarTemp.getCarId());
						childCar.setStyleNumber(childCarTemp.getVendorStyle()
								.getVendorStyleNumber());
						childCar.setProductName(childCarTemp.getVendorStyle()
								.getVendorStyleName());
						childCar.setOrder(carOutfitChild.getPriority());
						childCar.setSku(carOutfitChild.getDefaultColorSku()
								.getColorName());
						CarAttribute carAttr = outfitManager.
								   				getCarAttributeByBMName(childCarTemp, "Brand");
						if (carAttr == null) {
							childCar.setBrandName("");
						} else {
							childCar.setBrandName(carAttr.getAttrValue());
						}
						viewChildCars.add(childCar);	
					}
				}
				previewDTO.setChildCar(viewChildCars);
			}else
			if (carTemp.getSourceType().getSourceTypeCd().equals(SourceType.PYG)) {
				List<ChildCar> viewChildCars = new ArrayList<ChildCar>();
				Set<CarDBPromotionChild> dbPromotionChildCars = carTemp.getCarDBPromotionChild();
				for (CarDBPromotionChild carDBPromotionChild : dbPromotionChildCars) {
					if (carDBPromotionChild.getStatusCd().equals(Status.ACTIVE)) {
						Car childCarTemp = carDBPromotionChild.getChildCar();
						ChildCar childCar = new ChildCar();
						childCar.setCarId(childCarTemp.getCarId());
						childCar.setStyleNumber(childCarTemp.getVendorStyle()
								.getVendorStyleNumber());
						childCar.setProductName(childCarTemp.getVendorStyle()
								.getVendorStyleName());
						childCar.setOrder(carDBPromotionChild.getPriority());
						childCar.setSku(carDBPromotionChild.getDefaultColorSku()
								.getColorName());
						CarAttribute carAttr = getDbPromotionManager().getCarAttributeByBMName(childCarTemp, "Brand");
						if (carAttr == null) {
							childCar.setBrandName("");
						} else {
							childCar.setBrandName(carAttr.getAttrValue());
						}
						viewChildCars.add(childCar);	
					}
				}
				previewDTO.setChildCar(viewChildCars);
			} 
			Map<String, VendorSku> colorNameMap = new HashMap<String, VendorSku>();
			for (VendorSku sku : carTemp.getVendorSkus()) {
				String vendorColor = sku.getVendorStyle().getVendorStyleNumber()
						+ "_" + sku.getColorCode();
				if (StringUtils.isNotBlank(sku.getColorCode())) {
					if (!colorNameMap.containsKey(vendorColor)) {
						colorNameMap.put(vendorColor, sku);
					}
				}
			}
			for (CarSample carSample : carTemp.getCarSamples()) {
				String color = carSample.getSample().getVendorStyle().getVendorStyleNumber() + "_" + carSample.getSample().getSwatchColor();
				if (colorNameMap.containsKey(color)) {
					carSample.getSample().setColorName(colorNameMap.get(color).getColorName()) ;
				} else {
					//set the color name as not applicatble if color not found [specially for outfit cars]
					carSample.getSample().setColorName("Not-Applicable");
				}
			}
			previewDTO.setCar(carTemp);
			printpreviewDTOList.add(previewDTO);
		}
		ModelAndView modelAndView = new ModelAndView("printPreview");
		modelAndView.addObject("isPrintSku", printSkuInfo);
		modelAndView.addObject("previewList",printpreviewDTOList);
		modelAndView.addObject("user",user);
		
		return modelAndView;
	}
	

	
	
	
}
