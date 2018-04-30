package com.belk.car.app.webapp.controller.admin.productgroup;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.dto.ProductTypeDTO;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.webapp.forms.ProductTypeGroupForm;
import com.belk.car.app.webapp.validators.ProductGroupTypeValidator;

public class ProductAssociateController extends ProductGroupController {

	private transient final Log log = LogFactory.getLog(ProductAssociateController.class);
	private String associateView;
	private String associateConfirm;
	private String removeView;

	public String getAssociateView() {
		return associateView;
	}

	public void setAssociateView(String associateView) {
		this.associateView = associateView;
	}

	public String getAssociateConfirm() {
		return associateConfirm;
	}

	public void setAssociateConfirm(String associateConfirm) {
		this.associateConfirm = associateConfirm;
	}

	public String getRemoveView() {
		return removeView;
	}

	public void setRemoveView(String removeView) {
		this.removeView = removeView;
	}

	public ModelAndView getNonAssociatedProducts(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long productGroupId = ServletRequestUtils.getLongParameter(request, "id");
		if (productGroupId == null) {
			log.debug("Product Group id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		ProductGroup productGroup = productGroupManager.getProductGroup(productGroupId);
		ProductTypeGroupForm productTypeGroupForm = productGroupManager
				.getAsProductGroupForm(productGroup);
		log.debug("After getting product group" + productTypeGroupForm);

		List<ProductTypeDTO> productTypes = productGroupManager
				.getProductTypesForAssociation(productGroupId);
		productTypeGroupForm.setProductTypesList(productTypes);

		log.debug("****************************************" + productTypeGroupForm);

		return new ModelAndView(associateView, "productTypeGroupForm", productTypeGroupForm);

	}

	/**
	 * Validates for the product types selected and gets the selected product
	 * types that are to be associated with the product product group and sets
	 * them in the request.
	 * 
	 * @param request
	 * @param response
	 * @param productTypeGroupForm
	 * @return
	 * @throws Exception
	 */
	public ModelAndView next(HttpServletRequest request, HttpServletResponse response,
			ProductTypeGroupForm productTypeGroupForm) throws Exception {
		String productTypeIds = null;

		validate(productTypeGroupForm);
		if (errors.hasErrors()) {
			log.debug("Error found in Product type association" + errors);
			saveError(request, errors.getFieldError().getCode());
			return getNonAssociatedProducts(request, response);
		}

		Long productGroupId = ServletRequestUtils.getLongParameter(request, "id");
		if (productGroupId == null) {
			log.debug("Product Group id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		//Get the product Group
		ProductGroup productGroup = productGroupManager.getProductGroup(productGroupId);
		//Copy the product type group to new product type group form.
		ProductTypeGroupForm newProductTypeGroupForm = productGroupManager
				.getAsProductGroupForm(productGroup);
		log.debug("productTypeGroupForm--" + productTypeGroupForm);
		if (productTypeGroupForm != null && productTypeGroupForm.getNewProductTypeArr() != null) {
			productTypeIds = getProductIdsAsString(productTypeGroupForm.getNewProductTypeArr());
			/*
			 * setting the values of the product types that are being assigned
			 * to the new group.
			 */
			log.debug("Product type ids (which will be action):"+ productTypeIds);
			newProductTypeGroupForm.setAction(productTypeIds);
			//Sets the newly added product type attributes.
			newProductTypeGroupForm.setNewProductTypeArr(productTypeGroupForm
					.getNewProductTypeArr());
			//Sets all active product groups.
			newProductTypeGroupForm.setProductGroupList(productGroupManager.getAllActiveProductGroups());
			List<ProductTypeDTO> productTypes = productGroupManager.getSelectedProductTypes(
					productTypeIds, productGroup.getName()); //Get the selected product type groups
			newProductTypeGroupForm.setProductTypesList(productTypes); //Set the new group list to form to show.
			log.debug("Setting of new product type group completes. :"+ newProductTypeGroupForm.toString());
		}
		//Saving the product type group
		save(request,response,newProductTypeGroupForm);
		return new ModelAndView(associateConfirm, "productTypeGroupForm", newProductTypeGroupForm);
	}

	/**
	 * @param request
	 * @param response
	 * @param productTypeGroupForm
	 * @return
	 * @throws Exception
	 */
	public ModelAndView save(HttpServletRequest request, HttpServletResponse response,
			ProductTypeGroupForm productTypeGroupForm) throws Exception {
		if(log.isDebugEnabled()){
			log.debug("Inside Save() method..");
		}
		ModelAndView modelAndView;
		String productTypeIds = null;
		Long productGroupId = ServletRequestUtils.getLongParameter(request, "id");
		if (productGroupId == null) {
			log.debug("Product Group id was null. Redirecting...");
			modelAndView = new ModelAndView("redirect:dashBoard.html");
		} else {
			ProductGroup productGroup = productGroupManager.getProductGroup(productGroupId);
			log.debug("productGroup from product group id" + productGroup);
			ProductTypeGroupForm newProductTypeGroupForm = new ProductTypeGroupForm();

			/*
			 * Setting the session product ids in action and getting the value
			 * in the action variable for convenience sake.
			 */
			if (productGroup != null && productTypeGroupForm != null
					&& productTypeGroupForm.getAction() != null) {
				productTypeIds = productTypeGroupForm.getAction();
				log.debug("Action in save :"+ productTypeIds);
				// Remove the Selected product Type from old Product Group
				List<ProductType> productTypes = getCarLookupManager().getProductTypes(
						productTypeIds);
				Iterator<ProductType> iteratorProductType = productTypes.iterator();
				while (iteratorProductType.hasNext()) {
					ProductType productType = iteratorProductType.next();
					if (productType.getProductGroup() == null)
						continue;
					ProductGroup oldProductGroup = productGroupManager.getProductGroup(productType.getProductGroup().getProductGroupId());
					List<ProductType> removeProductType = productGroupManager.getActiveProductGroupTypes(oldProductGroup);
					removeProductType.remove(productType);
					oldProductGroup.setProductType(removeProductType);
					productGroupManager.removeTypeFromGroup("", oldProductGroup);
				}
				productGroup.setProductType(productGroupManager
						.getActiveProductGroupTypes(productGroup));
				productGroup.addProductTypes(productTypes);
				try {
					setAuditInfo(request, productGroup);
					log.debug("Product Group value for save " + productGroup);
					productGroupManager.save(productGroup);
					saveMessage(request, "Saved Successfully.");
				} catch (Exception e) {
					errors.rejectValue("name", e.getMessage());
					saveError(request, errors.getFieldError().getCode());
					return new ModelAndView(getAddProductGroupView());
				}

				newProductTypeGroupForm = productGroupManager.getAsProductGroupForm(productGroup);
				newProductTypeGroupForm.setProductGroupList(productGroupManager.getAllActiveProductGroups());

			}
			modelAndView = new ModelAndView(getDetailView(), "productTypeGroupForm",
					newProductTypeGroupForm);
		}
		return modelAndView;
	}

	/**
	 * Gets the string array and converts it into a String value.
	 * 
	 * @param productTypeIdVals
	 * @return
	 */
	private String getProductIdsAsString(String[] productTypeIdVals) {
		log.debug("productTypeIdVals--" + productTypeIdVals.length);
		String productTypeIds = Arrays.deepToString(productTypeIdVals);
		log.debug("ToString--" + productTypeIds);
		productTypeIds = productTypeIds.replace("[", "");
		productTypeIds = productTypeIds.replace("]", "");

		return productTypeIds;
	}

	/**
	 * Invoking the validation for the product Type association form.
	 * 
	 * @param command
	 */
	public void validate(Object command) {
		Validator validators[] = getValidators();

		if (validators != null) {
			for (int index = 0; index < validators.length; index++) {
				Validator validator = validators[index];
				if (validator instanceof ProductGroupTypeValidator) {
					if (((ProductGroupTypeValidator) validator).supports(command.getClass())) {

						ValidationUtils.invokeValidator(validators[index], command, errors);
					}
				} else if (validator.supports(command.getClass())) {
					ValidationUtils.invokeValidator(validators[index], command, errors);
				}
			}
		}
	}

	/**
	 * Remove the product type from the product group where the type is is
	 * passed as a value.
	 * 
	 * @deprecated use removeProdType
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView removeType(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String sProductGroupId = request.getParameter("id");
		String sProductTypeId = request.getParameter("typeId");

		Long productGroupId = null;
		if (sProductGroupId != null && sProductGroupId.length() > 0) {
			try {
				productGroupId = ServletRequestUtils.getLongParameter(request, "id");
			} catch (ServletRequestBindingException e) {
				log.error("Bind exception for grou id" + sProductGroupId, e);
			}
		}

		if (productGroupId == null || !(sProductTypeId != null && sProductTypeId.length() > 0)) {
			log.debug("Either product group id or Product id was null. Redirecting...");
			log.debug("sProductGroupId--" + sProductGroupId);
			log.debug("sProductTypeId--" + sProductTypeId);
			return new ModelAndView("redirect:dashBoard.html");
		}

		ProductGroup productGroup = productGroupManager.getProductGroup(Long
				.valueOf(productGroupId));

		removeProductType(request, productGroup, sProductTypeId, productGroupId);
		return detail(request, response);

	}

	/**
	 * Gets the product type that should be removed from the group and prepares
	 * a list of groups other than the current group for the product type to be
	 * associated.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView removeProdTypeView(HttpServletRequest request, HttpServletResponse response) {
		String sProductGroupId = request.getParameter("id");
		String sProductTypeId = request.getParameter("typeId");
		Long productGroupId = getGroupIdAndTypeId(request);
		if (productGroupId == null) {
			return new ModelAndView("redirect:dashBoard.html");
		}

		List<ProductGroup> productGroups = productGroupManager.getAllProductGroups();
		ProductGroup oldProductGroup = null;
		// Removing the oldproduct group from the list for association
		for (ProductGroup prodGroup : productGroups) {
			if (productGroupId.equals(prodGroup.getProductGroupId())) {
				oldProductGroup = prodGroup;
				productGroups.remove(prodGroup);
				break;
			}
		}
		ProductTypeGroupForm productTypeGroupForm = new ProductTypeGroupForm();
		productTypeGroupForm.setId(sProductGroupId);
		productTypeGroupForm.setProductGroupList(productGroups);
		ProductType productType = getCarLookupManager().getProductTypeById(
				Long.parseLong(sProductTypeId));
		productTypeGroupForm.setChProductType(productType);
		productTypeGroupForm.setProductGroup(oldProductGroup); // Old product
		// group

		return new ModelAndView(removeView, "productTypeGroupForm", productTypeGroupForm);

	}

	public ModelAndView removeProdType(HttpServletRequest request, HttpServletResponse response,
			ProductTypeGroupForm productTypeGroupForm) {
		ProductGroup productGroup = productGroupManager.getProductGroup(Long.valueOf(productTypeGroupForm.getId()));
		String newProductGroup = productTypeGroupForm.getNewProductGroupID();
		if (productTypeGroupForm.getNewProductGroupID().indexOf(",") != -1) {
			newProductGroup=productTypeGroupForm.getNewProductGroupID().substring(1);	
		}
		removeProductType(request, productGroup, productTypeGroupForm.getProductTypeID(), Long.parseLong(newProductGroup));
		ProductTypeGroupForm newProductTypeGroupForm = new ProductTypeGroupForm();
		productGroup.setProductType(productGroupManager
				.getActiveProductGroupTypes(productGroup));
		newProductTypeGroupForm = productGroupManager.getAsProductGroupForm(productGroup);
		newProductTypeGroupForm.setProductGroupList(productGroupManager.getAllActiveProductGroups());
		return new ModelAndView(getDetailView(), "productTypeGroupForm",
				newProductTypeGroupForm);


	}

	/**
	 * Checks to see if product group id and type id are in the request if not
	 * returns null
	 * 
	 * @param request
	 * @return
	 */
	private Long getGroupIdAndTypeId(HttpServletRequest request) {
		String sProductGroupId = request.getParameter("id");
		String sProductTypeId = request.getParameter("typeId");

		Long productGroupId = null;
		if (sProductGroupId != null && sProductGroupId.length() > 0) {
			try {
				productGroupId = ServletRequestUtils.getLongParameter(request, "id");
			} catch (ServletRequestBindingException e) {
				log.error("Bind exception for grou id" + sProductGroupId, e);
			}
		}

		if (productGroupId == null || !(sProductTypeId != null && sProductTypeId.length() > 0)) {
			log.debug("Either product group id or Product id was null. Redirecting...");
			log.debug("sProductGroupId--" + sProductGroupId);
			log.debug("sProductTypeId--" + sProductTypeId);
			productGroupId = null;
		}
		return productGroupId;
	}

	/**
	 * Removes the product type from the product group.
	 * @param request
	 * @param productGroup
	 * @param sProductTypeId
	 * @param productGroupId
	 */
	private void removeProductType(HttpServletRequest request, ProductGroup productGroup,
			String sProductTypeId, long productGroupId) {
		if (productGroup != null) {
			try {
				List <ProductType> list = productGroupManager.getActiveProductGroupTypes(productGroup);
				List<ProductType> productTypes = getCarLookupManager().getProductTypes(
						sProductTypeId);
				ProductType productTypeRemove = productTypes.get(0);
				list.remove(productTypeRemove);
				productGroup.setProductType(list);
				productGroupManager.removeTypeFromGroup(sProductTypeId, productGroup);
				ProductGroup newProductGroup = productGroupManager.getProductGroup(productGroupId);
				newProductGroup.setProductType(productGroupManager
						.getActiveProductGroupTypes(newProductGroup));
				newProductGroup.addProductTypes(productTypes);
				setAuditInfo(request, newProductGroup);
				productGroupManager.save(newProductGroup);
			} catch (Exception e) {
				log.error("Error in removing the product type from product group");
				log.error("sProductGroupId--" + productGroupId);
				log.error("sProductTypeId--" + sProductTypeId);
			}
			saveMessage(request, getText("Product Type delete from group ", productGroupId + "",
					request.getLocale()));
			log.debug("Product Group set to Inactive successfully" + productGroup);
		} else {
			log.debug("Product Group Not found for type removal" + productGroupId);
			saveMessage(request, getText("Not able to delete product group", productGroupId + "",
					request.getLocale()));
		}

	}

}