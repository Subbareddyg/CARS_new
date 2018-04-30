package com.belk.car.app.webapp.controller.admin.outfit;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import com.belk.car.app.Constants;
import com.belk.car.app.model.Car;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.OutfitForm;
import edu.emory.mathcs.backport.java.util.Arrays;

@SuppressWarnings("unchecked")
/**
 * 
 * @author AFUSY12
 * Form controller is to handle create/save outfit car requests  
 * Save Sku Info of outfit car request
 * 
 */
public class OutfitFormController extends BaseFormController {
	private transient final Log log = LogFactory
			.getLog(OutfitFormController.class);
	public OutfitManager outfitManager;

	public OutfitManager getOutfitManager() {
		return outfitManager;
	}

	public void setOutfitManager(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	public OutfitFormController() {
		setCommandName("outfitForm");
		setCommandClass(OutfitForm.class);
	}

	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		log.debug("entering outfitForm 'processFormSubmission' method...");

		if (request.getParameter("cancel") != null) {
			return new ModelAndView(getSuccessView());
		}
		return super.processFormSubmission(request, response, command, errors);
	}
	
	/**
     * This onSubmit form controller is used to create/update outfit 
     * car and save it. It is performing following things:
     * 1. Create the outfit car by calling the createOutfit method passing 
     * outfit name , outfit description, list of child cars , order(priority) of car and list of skus
     * 2. Store the attribute value(isSearchable, effective, 
	 * start date , template type) in car attribute table by calling 
	 * addCollectionProductAttributes method 
     * 3. Get the attribute value for each sku.
     * 4. Store the sku attributes in CarAttribute table by calling 
	 * createCollectionSkus method and passing sku's carid and attribute value.
     * 5. For existing outfit car save the modification by calling saveOutfit method
     * by passing outfit name , outfit description, list of child cars , order(priority)
     * of car and list of skus.
     * 6. Get the attribute value for each sku.
     * 7. Update the sku attributes in CarAttribute table by calling 
	 * updateCollectionSkuAttributes method and passing sku's carid and attribute value.
	 * 
     * @param request   HttpServletRequest object
	 * @param response  HttpServletResponse object
	 * @param command	OutfitForm object
	 * @param errors
	 * 	
	 * @return model object containing attribute value redirect to outfits.html
     */

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering outfitForm 'onSubmit' method...");
		}
		try {
			OutfitForm outfitForm = (OutfitForm) command;
			String outfitName = outfitForm.getOutfitName();
			String outfitDesc = outfitForm.getDescription();
			String[] order = outfitForm.getOrder();
			List<String> orderCars = (order == null ? new ArrayList<String>()
					: Arrays.asList(order));
			String[] defaultSku = outfitForm.getSku();
			List<String> sku = defaultSku == null ? new ArrayList<String>()
					: Arrays.asList(defaultSku);
			List<Car> childCars = (ArrayList<Car>) request.getSession()
					.getAttribute("CHILD_CAR_LIST");
			Long carId = ServletRequestUtils.getLongParameter(request, "CarId");
			if (childCars == null) {
				childCars = new ArrayList<Car>();
			}
			if (log.isDebugEnabled()) {
				log.debug("childCars: " + childCars);
				log.debug("outfit name: " + outfitName + "\t description: "
						+ outfitDesc);
				log.debug("Child Car order: " + orderCars);
				log.debug("Child Car Sku:" + sku);
			}
			String isSearchable = "";
			String prodEffectiveDate = "";
			
			String templateType = request.getParameter("templateTypeVal");
			String attrParentProducts = Constants.COLLECTION_OUTFIT_TYPE;
			if (templateType
					.equalsIgnoreCase(Constants.ATTR_COLLECTION)) {
				isSearchable = request
						.getParameter(Constants.ATTR_SEARCHABLE);
				prodEffectiveDate = request
						.getParameter(Constants.ATTR_EFFECTIVE_DATE);
				if(isSearchable == null)
					isSearchable = "N";
			}
	
			if (carId == null) {
				/* create outfit car*/
				Car outfitCar = outfitManager.createOutfit(outfitName,
						outfitDesc, childCars, orderCars, sku,templateType);
				if (outfitCar != null) {
					if (log.isDebugEnabled()) {
						log.debug("Outfit car created : "
								+ outfitCar.getCarId());
					}
					Long outfitCarId = outfitCar.getCarId();
					String productCode = outfitCar.getVendorStyle().getVendorNumber()+outfitCar.getVendorStyle().getVendorStyleNumber();
					
					outfitManager.createProductAttributes(outfitCarId,
							templateType, attrParentProducts);
					
					/* if template type="COLLECTION", save the comma separated sku list, is product searchable, 
					 * product active start date(effective date)*/
					if (templateType
							.equalsIgnoreCase(Constants.ATTR_COLLECTION)) {	
						
						String[] skuCount = outfitForm.getSkuCount();
						for (int i = 0; i < skuCount.length; i++) {
							String chkSkuList = "chkSkuList" + i;
							String chkSkuListVal = request
									.getParameter(chkSkuList);
							if (chkSkuListVal != null) {
								String belkSku = "belkSku" + i;
								String belkSkuVal = request
										.getParameter(belkSku);
								outfitManager.createCollectionSkus(productCode,
										belkSkuVal);
							}
						}
						attrParentProducts = Constants.COLLECTION_IS_PRODUCT_SEARCHABLE;
						outfitManager.createProductAttributes(outfitCarId,
								isSearchable, attrParentProducts);
						attrParentProducts = Constants.COLLECTION_EFFECTIVE_DATE;
						outfitManager.createProductAttributes(outfitCarId,
								prodEffectiveDate, attrParentProducts);
					}
				}
			} else {
				/* save outfit car*/
				Car OutfitCar = outfitManager.getCarFromId(carId);
				outfitManager.saveOutfit(OutfitCar, outfitName, outfitDesc,
						childCars, orderCars, sku,templateType);
				if (log.isDebugEnabled()) {
					log.debug("Outfit car Saved : " + OutfitCar.getCarId());
				}
				/* if template type="COLLECTION", update the comma separated sku list, is product searchable, 
				 * product active start date(effective date)*/
				if (templateType.equalsIgnoreCase(Constants.ATTR_COLLECTION)) {
					Long outfiltCarId = OutfitCar.getCarId();
					String productCode = OutfitCar.getVendorStyle().getVendorNumber()+OutfitCar.getVendorStyle().getVendorStyleNumber();
					outfitManager.removeCollectionSkus(productCode);
					String[] skuCount = outfitForm.getSkuCount();
					for (int i = 0; i < skuCount.length; i++) {
						String chkSkuList = "chkSkuList" + i;
						String chkSkuListVal = request
								.getParameter(chkSkuList);
						if (chkSkuListVal != null) {
							String belkSku = "belkSku" + i;
							String belkSkuVal = request
									.getParameter(belkSku);
							outfitManager.createCollectionSkus(productCode,
									belkSkuVal);
						}
					}
					outfitManager.updateCollectionSkuAttributes(outfiltCarId,
							Constants.COLLECTION_IS_PRODUCT_SEARCHABLE,
							isSearchable);
					outfitManager.updateCollectionSkuAttributes(outfiltCarId,
							Constants.COLLECTION_OUTFIT_TYPE, templateType);
					outfitManager.updateCollectionSkuAttributes(outfiltCarId,
							Constants.COLLECTION_EFFECTIVE_DATE, prodEffectiveDate);
				}
			}
			request.getSession().removeAttribute("CHILD_CAR_LIST");
		} catch (Exception e) {
			e.printStackTrace();
			log.error(" OutfitFormController: onSubmit: exception while creating OF car: "
				+ e.getMessage());
		}
		return new ModelAndView("redirect:outfits.html?method=getAllOutfitCars");
	}
}
