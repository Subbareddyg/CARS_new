package com.belk.car.app.webapp.controller.admin.outfit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.SourceType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.app.webapp.forms.OutfitForm;
import com.belk.car.util.DateUtils;


/**
 * Form Controller to add Child Cars to an Outfit Car and skus to Collection car.
 * 
 * @author afunxy1
 *	@author Karthik, Anto
 */

public class ChildCarFormController extends BaseFormController {

	private transient final Log log = LogFactory.getLog(ChildCarFormController.class);
	
	public OutfitManager outfitManager;
	
	public OutfitManager getOutfitManager() {
		return outfitManager;
	}

	public void setOutfitManager(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	public ChildCarFormController() {
		setCommandName("outfitForm");
		setCommandClass(OutfitForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
		if (log.isDebugEnabled()){ 
			log.debug("entering 'processFormSubmission' method...");
		}
		return super.processFormSubmission(request, response, command, errors);
	}
	
	
	/**
	 * The onSubmit method performing following things:
	 * 1. It is being called when user clicks on 'Add child car' on create/edit outfit page
	 * 2. It will fetch all child car details corresponding to car id and 
	 * active status from car table and  add it in view and getViewChildCarDetails method is 
	 * called to display the child car details.  
	 * 3. It is also validating that the child car cannot be a outfit car.   
	 * 4. For collection car to view the Child Cars Sku , getChildCarSkus 
	 * method is called inside to get the details of each sku to store in viewChildCarsSkuList list
	 * 5. Skus are split based on CAR Id 
	 * 6. In model viewChildCars list and viewChildCarsSkuList list 
	 * appended and send to carandskudetail.jsp
	 * 
	 * @param request   It contains HttpServletRequest object
	 * @param response  It contains HttpServletResponse object 
	 * @param command   It contains the instance of outfit form 
	 * @param errors
	 * 
	 * @return modelandview object containing child car list and sku list
	 */

	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		OutfitForm outfitForm=(OutfitForm)command;
		
		/*new child car id*/
		String childCarId=outfitForm.getChildCarId();
		if(log.isDebugEnabled()){
			log.debug("trying to add child car : "+childCarId + " to outfit");
		}
		
		if(childCarId!=null){
			childCarId=childCarId.trim();
		}
		
		Car newChildCar=outfitManager.getChildCarById(new Long(childCarId));
		
		/*outfit car id is 'null'(string null, passing from edit.jsp) to skip child cars in Create Outfit.*/
		String outfitCarId = ServletRequestUtils.getStringParameter(request, "outfitCarId");
		
		
		Car outfitCar=null;
		if(outfitCarId!=null && !"".equals(outfitCarId) && !"null".equals(outfitCarId) ) {
			outfitCar=outfitManager.getCarFromId(new Long(outfitCarId));
		}
		if(log.isDebugEnabled()){
			log.debug("adding child car to outfit car :"+ outfitCar);
		}
		
		List<Car> childCars=new ArrayList<Car>(); /* Cars to pass SaveOutfit*/ 
		List<ChildCar> viewChildCars=new ArrayList<ChildCar>(); /* child cars to View*/
		List<Car> newChildCars=null; /* for maintaining newly added child cars*/
		Map<String, Object> model = new HashMap<String, Object>();
		List<Car> removedChildCars=null;/* for maintaining the removed child car list from an existing outfit*/
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); /* child cars to View*/
		List<ChildCar> viewChildCarsSkuListArray[] = (ArrayList<ChildCar>[])new ArrayList[100];
		if(null!=newChildCar && !"".equals(newChildCar))
		{
			/* Child Car can not be an Outfit Car*/
			if(SourceType.OUTFIT.equals(newChildCar.getSourceType().getSourceTypeCd())){
				log.error("child car id: "+childCarId +" source type is OUTFIT, can not be added ");
				model.put("error","OutfitCar");
				request.setAttribute("jsonObj", new JSONObject(model));
				return new ModelAndView(getAjaxView());
			}
			if(SourceType.PYG.equals(newChildCar.getSourceType().getSourceTypeCd())){
				log.error("child car id: "+childCarId +" has source type as PYG,so can not be added ");
				model.put("error","NoPYGInOutfit");
				request.setAttribute("jsonObj", new JSONObject(model));
				return new ModelAndView(getAjaxView());
			}
			if(request.getSession().getAttribute("CHILD_CAR_LIST")!=null)
			{
				childCars=(ArrayList<Car>)request.getSession().getAttribute("CHILD_CAR_LIST");
				if(childCars!=null){
					String returnString= childCarCheck(childCars,newChildCar);
					if(returnString!=null){
						if("Exists".equals(returnString)) {
									log.error("child car id: "+childCarId +" already exists");
								model.put("error","Exists");
							} else if("SameVendorStyle".equals(returnString)) {
									log.error("child car id: "+childCarId +" with same Vendor Style already exists");
									model.put("error","SaveVendorStyle");
							}
						request.setAttribute("jsonObj", new JSONObject(model));
						return new ModelAndView(getAjaxView());
					} else if (returnString==null)
					{
						/* adding new child car to child car list.*/
						childCars.add(newChildCar);
						
						if(log.isInfoEnabled()){
							log.info(" child car id: "+childCarId +" added to outfit car " + outfitCar);
						}
						
						/* newChildCars is a copy of childCars to maintain newly added child cars.*/
						newChildCars=new ArrayList<Car>(childCars);
						
						Collections.copy(newChildCars,childCars);
						
						/* outfitCar is not null i.e., adding new child cars to existing Outfit Car
						 get the child cars and add to the View*/
						if(outfitCar!=null){
							removedChildCars=(ArrayList<Car>)request.getSession().getAttribute("REMOVED_CHILD_CAR_LIST");
							if(removedChildCars==null){
								removedChildCars=new ArrayList<Car>();
							}
							Attribute attrChildProducts = outfitManager.getAttributeByName(Constants.OUTFIT_CHILD_PRODUCTS);
							String attrChildProductValue=outfitManager.getAttributeValue(outfitCar,attrChildProducts);
							String[] childProductSKUs = null;
							List<String> lstchildProductSKUs = null;
							if(attrChildProductValue != null){
								childProductSKUs = attrChildProductValue.split(",");
								lstchildProductSKUs =  Arrays.asList(childProductSKUs);
							}
							Set<CarOutfitChild> carOutfitChild=outfitCar.getCarOutfitChild();
							if(carOutfitChild != null){
								for(CarOutfitChild outfitChild : carOutfitChild){
									Car outfitChildCar=outfitChild.getChildCar();
									if(outfitChild.getStatusCd().equals(Status.ACTIVE) && !removedChildCars.contains(outfitChildCar)){
										/* adding outfit child cars to View and excluding the removed child cars*/
										viewChildCars.add(outfitManager.getViewChildCarDetails(outfitChildCar,outfitChild,lstchildProductSKUs));
										// remove outfit child cars from newChildCars so that we have only newly added child cars
										newChildCars.remove(outfitChildCar);
								
									}
								}
							}
							
							/* after adding Outfit Car child cars to View, adding newly added child cars.*/
							for(Car newCar:newChildCars){
								viewChildCars.add(outfitManager.getViewChildCarDetails(newCar,new CarOutfitChild(),new ArrayList<String>()));
							}
							
						} else{
							log.debug("adding child cars to new outfit car");
							/*if outfit car is null then its a new outfit car. adding child cars for new outfit */
							for(Car childCar:childCars){
								viewChildCars.add(outfitManager.getViewChildCarDetails(childCar,new CarOutfitChild(),new ArrayList<String>()));
							}
						}
					}
				}
			}else{  /*there are no child cars in session */
				log.debug("adding first child car to new outfit");
				
				/* adding child cars to the new outfit car first time*/
				childCars.add(newChildCar);
				if(log.isInfoEnabled()){
					log.info(" child car id:"+childCarId +" added");
				}
				viewChildCars.add(outfitManager.getViewChildCarDetails(newChildCar,new CarOutfitChild(),new ArrayList<String>()));	
			}
			if (log.isDebugEnabled()) {
				log.debug("child cars:"+childCars);
				log.debug("view child cars size:"+viewChildCars.size());
			}
			request.getSession().setAttribute("CHILD_CAR_LIST",childCars);
			model.put("viewChildCars", viewChildCars);
			String templateType = request.getParameter("hiddenTemplateType");
			if (templateType
					.equalsIgnoreCase(Constants.ATTR_COLLECTION)) {
				if (log.isDebugEnabled()) {
					log.debug("Inside Collection section ");
				}
				List<String> lstSelCollectionSKUs = new ArrayList<String>();
				if(outfitCarId!=null && !"".equals(outfitCarId) && !"null".equals(outfitCarId) ) 
				{ 
					String productCode = outfitCar.getVendorStyle().getVendorNumber()+outfitCar.getVendorStyle().getVendorStyleNumber();
					List<CollectionSkus> collSkus = outfitManager.getCollectionSkus(productCode);

					for(CollectionSkus collectSkus: collSkus){
						String strCollSkus = collectSkus.getSkuCode();
						lstSelCollectionSKUs.add(strCollSkus);
					}
				}
				if (request.getSession().getAttribute("CHILD_CAR_SKU_LIST") != null)
				{
					viewChildCarsSkuList = (ArrayList<ChildCar>)request.getSession().getAttribute("CHILD_CAR_SKU_LIST");
				}
				viewChildCarsSkuList = getChildCarSkus(request, response, command, errors);
				request.getSession().setAttribute("CHILD_CAR_SKU_LIST",viewChildCarsSkuList);
				
				long tempSkuCarId = 0;
				int groupCarCounter = -1;
				Long newChildCarId = newChildCar.getCarId();
				long newcarStyleId = newChildCar.getVendorStyle().getVendorStyleId();
				int childCarCounter = 0;
				for(ChildCar vSku: viewChildCarsSkuList){
	
					ChildCar childCarSkuList= new ChildCar();
					long skuCarId = vSku.getSkuCarid();
					Car carObj = outfitManager.getCarFromId(skuCarId);
					long carStyleId = carObj.getVendorStyle().getVendorStyleId();
					if (tempSkuCarId == 0 || tempSkuCarId != carStyleId)
					{
						if (groupCarCounter != -1 && tempSkuCarId != 0 && tempSkuCarId != carStyleId)
						{
							model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);
						}
						tempSkuCarId = carStyleId;
						groupCarCounter++;
						viewChildCarsSkuListArray[groupCarCounter] = new ArrayList<ChildCar>();
					}
					viewChildCarsSkuList.get(childCarCounter).setParentCarStyleId(new Long(carStyleId));
					childCarSkuList.setSkuCarid(skuCarId);
					childCarSkuList.setCompDt(vSku.getCompDt());
					childCarSkuList.setVendorStyle(vSku.getVendorStyle());
					childCarSkuList.setSkuStyleName(vSku.getSkuStyleName());
					childCarSkuList.setVendorUpc(vSku.getVendorUpc());
					childCarSkuList.setSkuColor(vSku.getSkuColor());
					childCarSkuList.setColorName(vSku.getColorName());
					childCarSkuList.setSizeName(vSku.getSizeName());
					childCarSkuList.setSkuID(vSku.getSkuID());
					childCarSkuList.setBelkSku(vSku.getBelkSku());
					childCarSkuList.setVendorName(vSku.getVendorName());
					
					if(newChildCarId == skuCarId || newcarStyleId == carStyleId)
					{
						childCarSkuList.setSkuSelValues(Constants.SELECT_SKUCHECKBOX);
					}
					else
					{
						List<String> lstSelBelkUpcColl = null;
						String selectedUpc = request.getParameter("selectedUpc");
						String selBelkUpcArr[] = selectedUpc.split(",");
						lstSelBelkUpcColl =  Arrays.asList(selBelkUpcArr);
						
						if(outfitCarId!=null && !"".equals(outfitCarId) && !"null".equals(outfitCarId) ) 
						{
							if(lstSelCollectionSKUs.contains(vSku.getSkuID()) && lstSelBelkUpcColl.isEmpty())
								childCarSkuList.setSkuSelValues(Constants.SELECT_SKUCHECKBOX);
							else if(lstSelBelkUpcColl.contains(vSku.getBelkSku()))
								childCarSkuList.setSkuSelValues(Constants.SELECT_SKUCHECKBOX);
							else
								childCarSkuList.setSkuSelValues(Constants.UNSELECT_SKUCHECKBOX);
						}
						else
						{
							
							if(lstSelBelkUpcColl.contains(vSku.getBelkSku()))
								childCarSkuList.setSkuSelValues(Constants.SELECT_SKUCHECKBOX);
						}
						
					}
					viewChildCarsSkuListArray[groupCarCounter].add(childCarSkuList);
					childCarCounter++;
				}
				if (groupCarCounter != -1)
				{
					model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);
				}
				model.put("viewChildCarsSkuListArray", viewChildCarsSkuListArray);
				model.put("viewChildCarsSkuList", viewChildCarsSkuList);
				model.put("isSearchableVal", Constants.ISSEARCHABLE_VALUE);
				if (log.isDebugEnabled()) {
					log.debug("viewChildCarsSkuListArray object created ");
				}
			}
			return new ModelAndView(getSuccessView(),model);
		}else{
			log.error("child car id: "+childCarId +" not found");
			model.put("error","NotFound");
			request.setAttribute("jsonObj", new JSONObject(model));
			return new ModelAndView(getAjaxView());
		}
	}
	
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		OutfitForm outfitForm = new OutfitForm();
		return outfitForm;
	}
	
	/**
	 * This  method is performing following things:
	 * 1. It is called from onSubmit method when user clicks on 'Add child car' on create/edit 
	 * 	  from outfit page when the template type is collection. 
	 * 2. This method will fetch all child car sku details in childCarSkuList 
	 *   
	 * @param request  It contain HttpServletRequest object
	 * @param response It contain HttpServletResponse object
	 * @param command  It contains the instance of outfit form
	 * @param errors
	 * @return modelandview containing list of skus.
	 */
	@SuppressWarnings("unchecked")
	public List<ChildCar> getChildCarSkus(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside getChildCarSkus method ");
		}
		OutfitForm outfitForm=(OutfitForm)command;
		
		/*new child car id*/
		String childCarId=outfitForm.getChildCarId();
		if(log.isDebugEnabled()){
			log.debug("trying to add child car : "+childCarId + " to outfit");
		}
		
		if(childCarId!=null){
			childCarId=childCarId.trim();
		}
		
		Car newChildCar=outfitManager.getChildCarById(new Long(childCarId));
		
		/*outfit car id is 'null'(string null, passing from edit.jsp) to skip child cars in Create Outfit.*/
		String outfitCarId = ServletRequestUtils.getStringParameter(request, "outfitCarId");
		
		
		Car outfitCar=null;
		if(outfitCarId!=null && !"".equals(outfitCarId) && !"null".equals(outfitCarId) ) {
			outfitCar=outfitManager.getCarFromId(new Long(outfitCarId));
		}
		if(log.isDebugEnabled()){
			log.debug("adding child car to outfit car :"+ outfitCar);
		}
		
		List<Car> childCars=new ArrayList<Car>(); // Cars to pass SaveOutfit 
		List<ChildCar> viewChildCars=new ArrayList<ChildCar>(); // child cars to View
		List<Car> newChildCars=null; // for maintaining newly added child cars
		Map<String, Object> model = new HashMap<String, Object>();
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
		List<Car> skuCar=new ArrayList<Car>();
		
		
		if(null!=newChildCar && !"".equals(newChildCar))
		{
			/* Child Car can not be an Outfit Car*/
			if(request.getSession().getAttribute("CHILD_CAR_LIST")!=null)
			{
				childCars=(ArrayList<Car>)request.getSession().getAttribute("CHILD_CAR_LIST");
				if(childCars!=null){	
					
					  if(log.isInfoEnabled()){
							log.info(" child car id: "+childCarId +" added to outfit car " + outfitCar);
						}
						
						/* newChildCars is a copy of childCars to maintain newly added child cars.*/
						newChildCars=new ArrayList<Car>(childCars);
						
						Collections.copy(newChildCars,childCars);
						
						for(Car childCar:childCars){
							VendorStyle vendorStyle = childCar.getVendorStyle();
							skuCar = outfitManager.getChildCarSkus(vendorStyle);
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
								}
							}
						}
			}
			}else{ /* when there are no child cars in session*/ 
				log.info("adding first child car to new outfit");			
				/* adding child cars to the new outfit car first time*/
				childCars.add(newChildCar);
				if(log.isInfoEnabled()){
					log.info(" child car id:"+childCarId +" added");
				}
				Set<VendorSku> carVendorSku=newChildCar.getVendorSkus();
				for(VendorSku vSku:carVendorSku){
					log.info("----getBelkSku222 = "+ vSku.getBelkSku());
				}
				
			}
			if (log.isDebugEnabled()) {
				log.debug("child cars:"+childCars);
			}
						
			return viewChildCarsSkuList;
			
		}else{
			log.error("child car id: "+childCarId +" not found");
			model.put("error","NotFound");
			request.setAttribute("jsonObj", new JSONObject(model));
			return viewChildCarsSkuList;
		}
		
	}
	
	/**
	 * Method to Check child car already exists in list and check for same vendor style id
	 * 
	 * @param List of childCars
	 * @param car which is added 
	 * @return String
	 */
	private String childCarCheck(List<Car> childCars,Car car)
	{
		for(Car c:childCars){
			if (log.isDebugEnabled()) {
				log.debug(" child car id:"+c.getCarId() +" car id:"+car.getCarId());
				log.debug(" child car vendor style id :"+c.getVendorStyle().getVendorStyleId() +" and car vendor style id:"+car.getVendorStyle().getVendorStyleId());
			}
			if(c.getCarId()==car.getCarId()){
				return "Exists";
			} else 	if(c.getVendorStyle().getVendorStyleId() == car.getVendorStyle().getVendorStyleId()){
				return "SameVendorStyle";
			}
		}
		return null;
	}
}
