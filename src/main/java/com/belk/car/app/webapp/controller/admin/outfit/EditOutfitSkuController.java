package com.belk.car.app.webapp.controller.admin.outfit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.ChildCar;
import com.belk.car.app.webapp.forms.OutfitForm;


/**
 * 
 * @author afunxy1
 * 
 * MultiAction controller for Listing, Search, Edit and Remove Outfit Cars.
 *
 */

public class EditOutfitSkuController extends MultiActionFormController {

	private transient final Log log = LogFactory.getLog(EditOutfitSkuController.class);

	public OutfitManager outfitManager;
	
	public OutfitManager getOutfitManager() {
		return outfitManager;
	}

	public void setOutfitManager(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	
	
	/**
	 * Edit Outfit Car
	 * 
	 * @param request : CarId(Outfit)
	 * @param response
	 * @return ModelAndView with outfit car details and child car details
	 * @throws Exception
	 */
	
	public ModelAndView getSkusForCar(HttpServletRequest request,HttpServletResponse response) throws Exception {
	log.info("INside EditOutfitSkuController.getSkusForCar");
	
	
	Long outfitCarId = ServletRequestUtils.getLongParameter(request, "CarId");
	Long childCarId = ServletRequestUtils.getLongParameter(request, "childCarId");
	log.info("Editing Outfit Car: outfitCarId11: "+outfitCarId);

	OutfitForm outfitForm=new OutfitForm();
	Map<String, Object> model = new HashMap<String, Object>();
	// List to maintain child cars to save ie. placing in session.
	List<Car> childCars=new ArrayList<Car>();
	// List of child cars to view purpose ie. passing in Model.
	List<ChildCar> viewChildCars=new ArrayList<ChildCar>();
	List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
	List<ChildCar> viewChildCarsSkuListArray[] = (ArrayList<ChildCar>[])new ArrayList[100];
	Attribute attrChildProducts = outfitManager.getAttributeByName(Constants.OUTFIT_CHILD_PRODUCTS);
	Attribute collectionSkus = outfitManager.getAttributeByName(Constants.COLLECTION_PARENT_PRODUCTS);
	
	Attribute templateType = outfitManager.getAttributeByName(Constants.COLLECTION_OUTFIT_TYPE);
	Attribute effectiveDate = outfitManager.getAttributeByName(Constants.COLLECTION_EFFECTIVE_DATE);
	Attribute isSearchable = outfitManager.getAttributeByName(Constants.COLLECTION_IS_PRODUCT_SEARCHABLE);
	
	if(outfitCarId!=null && !"".equals(outfitCarId))
	{
		Car outfitCar=outfitManager.getCarFromId(new Long(outfitCarId));
		Car childCarSku = outfitManager.getCarFromId(new Long(childCarId));
		//Get the attribute values of an outfit Car
		String attrChildProductValue=outfitManager.getAttributeValue(outfitCar,attrChildProducts);
		String[] childProductSKUs = null;
		List<String> lstchildProductSKUs = null;
		// child product sku as a list to send to getViewChildCarDetails, for priority,color sku
		if(attrChildProductValue != null){
			childProductSKUs = attrChildProductValue.split(",");
			lstchildProductSKUs =  Arrays.asList(childProductSKUs);
		}
		
		if(outfitCar!=null ) {
			// set the values of outfit Form to view. 
			outfitForm.setOutfitName(outfitCar.getVendorStyle().getVendorStyleName());
			outfitForm.setDescription(outfitCar.getVendorStyle().getDescr());
			// Get all the outfit child cars 
			Set<CarOutfitChild> carOutfitChild=outfitCar.getCarOutfitChild();
			Set<CarOutfitChild> carChildSku=childCarSku.getCarOutfitChild();
			if(carOutfitChild != null){
				
			String templateTypeVal="collection";
			model.put("templateType", templateTypeVal);
			if(templateTypeVal != null && templateTypeVal.equalsIgnoreCase("collection"))
			{
				String attrChildSkuValue=outfitManager.getAttributeValue(outfitCar,collectionSkus);
				
				String effectiveDateVal=outfitManager.getAttributeValue(outfitCar,effectiveDate);
				log.info("-----effectiveDateVal: "+ effectiveDateVal);
//				if(effectiveDateVal != null)
//				{
//					String[] effectiveDateValArr = effectiveDateVal.split("/");
//					effectiveDateVal =  effectiveDateValArr[1]+"/"+effectiveDateValArr[0]+"/"+effectiveDateValArr[2];
//				}	
				model.put("effectiveDate", effectiveDateVal);
				String[] collectionSKUs = null;
				List<String> lstSelCollectionSKUs = null;
				//  sku as a list to send to getViewChildCarDetails, for priority,color sku
				if(attrChildSkuValue != null){
					collectionSKUs = attrChildSkuValue.split(",");
					lstSelCollectionSKUs =  Arrays.asList(collectionSKUs);
					outfitForm.setSelCollectionSkus(attrChildSkuValue);
					log.info("-----Inside collection if attrChildSkuValue: "+attrChildSkuValue);
					request.setAttribute("attrChildSkuValue", attrChildSkuValue);
				}
					
				log.info("-----Inside collection if childCar: "+carOutfitChild);
				if (request.getSession().getAttribute("CHILD_CAR_SKU_LIST") != null)
				{
					viewChildCarsSkuList = (ArrayList<ChildCar>)request.getSession().getAttribute("CHILD_CAR_SKU_LIST");
				}
				viewChildCarsSkuList = getChildCarSkus(childCarSku);
				request.getSession().setAttribute("CHILD_CAR_SKU_LIST",viewChildCarsSkuList);
				
				long tempSkuCarId = 0;
				int groupCarCounter = -1;
				for(ChildCar vSku: viewChildCarsSkuList){
	
					ChildCar childCarSkuList= new ChildCar();
					long skuCarId = vSku.getSkuCarid();
					if (tempSkuCarId == 0 || tempSkuCarId != skuCarId)
					{
						if (groupCarCounter != -1 && tempSkuCarId != 0 && tempSkuCarId != skuCarId)
						{
							model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);
						}
						tempSkuCarId = skuCarId;
						groupCarCounter++;
						viewChildCarsSkuListArray[groupCarCounter] = new ArrayList<ChildCar>();
					}
	
					childCarSkuList.setSkuCarid(skuCarId);
					childCarSkuList.setVendorStyle(vSku.getVendorStyle());
					childCarSkuList.setSkuStyleName(vSku.getSkuStyleName());
					childCarSkuList.setVendorUpc(vSku.getVendorUpc());
					childCarSkuList.setSkuColor(vSku.getSkuColor());
					childCarSkuList.setColorName(vSku.getColorName());
					childCarSkuList.setSizeName(vSku.getSizeName());
					childCarSkuList.setSkuID(vSku.getSkuID());
					childCarSkuList.setBelkSku(vSku.getBelkSku());
					childCarSkuList.setVendorName(vSku.getVendorName());
					
					if(lstSelCollectionSKUs != null && lstSelCollectionSKUs.contains(vSku.getSkuID()))
						childCarSkuList.setSkuSelValues("1");
					else
						childCarSkuList.setSkuSelValues("0");
	
					log.info("---childCarSkuList.getSkuSelValues() =  "+childCarSkuList.getSkuSelValues());
					viewChildCarsSkuListArray[groupCarCounter].add(childCarSkuList);
				}
				if (groupCarCounter != -1)
				{
					model.put("viewChildCarsSkuList_"+groupCarCounter, viewChildCarsSkuListArray[groupCarCounter]);
				}
				model.put("viewChildCarsSkuListArray", viewChildCarsSkuListArray);
				model.put("viewChildCarsSkuList", viewChildCarsSkuList);
			}
	
				// For each outfit child if it was ACTIVE, get the car details and add to ViewChildCars and childCars.
				for(CarOutfitChild outfitChild : carOutfitChild){
					Car childCar=outfitChild.getChildCar();
					if (log.isDebugEnabled()) {
						log.debug("Outfit Child Car Id :"+ childCar.getCarId() + " Status : "+ outfitChild.getStatusCd());
					}
					if(outfitChild.getStatusCd().equals(Status.ACTIVE)){
						viewChildCars.add(outfitManager.getViewChildCarDetails(childCar,outfitChild,lstchildProductSKUs));
						childCars.add(childCar);
					}
				}
			} else{
				 log.debug("Outfit Child CARs are null");
			}
		}
		else
		{
			if (log.isDebugEnabled()) {
				log.debug("Outfit Car Id :"+outfitCarId+" Not found");
			}
		}
	
	}
	ModelAndView mv=new ModelAndView(getSuccessView(),model);
	// add childCars to session for add/remove child cars in edit and save.
	request.getSession().setAttribute("CHILD_CAR_LIST",childCars);
	// viewChildCars in model to view
	mv.addObject("viewChildCars", viewChildCars);
	return mv;
	}
	
	
	public List<ChildCar> getChildCarSkus(Car childCar)
	throws Exception {
		
		List<ChildCar> viewChildCarsSkuList=new ArrayList<ChildCar>(); // child cars to View
						
		
				Set<VendorSku> carVendorSku=childCar.getVendorStyle().getVendorSkus();
				log.info("----getBelkSku111 size = "+ carVendorSku.size());
				for(VendorSku vSku:carVendorSku){
					log.info("----getBelkSku111 = "+ vSku.getBelkSku()+" | "+vSku.getColorName()+" | "+vSku.getSizeName()+" | "+vSku.getCar());
					ChildCar childCarSkuList= new ChildCar();
		
					childCarSkuList.setSkuCarid(childCar.getCarId());
					childCarSkuList.setVendorName(vSku.getVendorStyle().getVendor().getName());
					long venStyleId = vSku.getVendorStyle().getVendorStyleId();
					String strVenStyleId = (venStyleId != 0)?(new Long(venStyleId)).toString ():"";
					childCarSkuList.setVendorStyle(strVenStyleId);
					childCarSkuList.setSkuStyleName(vSku.getVendorStyle().getVendorStyleName());
					childCarSkuList.setVendorUpc(vSku.getBelkUpc());
					childCarSkuList.setSkuColor(vSku.getColorCode());
					childCarSkuList.setColorName(vSku.getColorName());
					childCarSkuList.setSizeName(vSku.getSizeName());
					
					childCarSkuList.setSkuID(vSku.getBelkSku());
					childCarSkuList.setBelkSku(vSku.getBelkSku());
					viewChildCarsSkuList.add(childCarSkuList);
			}
			if (log.isDebugEnabled()) {
				log.debug("child cars:"+childCar);
				
			}
						
			return viewChildCarsSkuList;
		
	}
	
	/**
	 * Get the login user
	 */
	public User getLoggedInUser() {
		User user = null;
		Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
		if (auth.getPrincipal() instanceof UserDetails) {
			user = (User) auth.getPrincipal();
		}
	 return user;
	}
}
