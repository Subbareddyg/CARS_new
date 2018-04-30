package com.belk.car.app.webapp.controller.admin.productgroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.exceptions.ProductGroupExistsException;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.service.ProductGroupManager;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.ProductTypeGroupForm;
import com.belk.car.app.webapp.validators.ProductGroupValidator;
import com.belk.car.util.GenericUtils;

public class ProductGroupController extends MultiActionFormController {

	private transient final Log log = LogFactory.getLog(ProductGroupController.class);

	protected ProductGroupManager productGroupManager;
	protected String addProductGroupView;
	protected String detailView;

	/**
	 * @param productGroupManager
	 *            is set to the class.
	 */
	public void setProductGroupManager(ProductGroupManager productGroupManager) {
		this.productGroupManager = productGroupManager;
	}

	public String getAddProductGroupView() {
		return addProductGroupView;
	}

	public void setAddProductGroupView(String addProductGroupView) {
		this.addProductGroupView = addProductGroupView;
	}

	public String getDetailView() {
		return detailView;
	}

	public void setDetailView(String detailView) {
		this.detailView = detailView;
	}

	/**
	 * Gets all the Product Groups as a list for displaying in the page.
	 */
	public ModelAndView getAllProductGroups(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("In Product Group Controller : Getting all the Product Groups");
		User user = getLoggedInUser();
		if (user.getAdmin()) {
			request.getSession().setAttribute("displayRole", "admin");
		}
		
		return new ModelAndView(getSuccessView(), DropShipConstants.PRODUCT_GROUP_LIST,
				productGroupManager.getAllProductGroups());
	}

	/**
	 * Search based on product group name.
	 * 
	 * @param request
	 *            : Has the product group name
	 * @param response
	 *            : Has the search list of the product group name
	 * @return : Has the list of product group names
	 * @throws Exception
	 */
	public ModelAndView Search(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String productGroupName = request.getParameter("productGroupName");
		log.debug("In Product Group Controller : Search:" + productGroupName);
		if (productGroupName == null || productGroupName.trim().equalsIgnoreCase("")) {
			saveError(request, getText("Product Group Name cannot be empty", request
					.getLocale()));
			productGroupName = "#";
			Map<String, Object> model = new HashMap<String, Object>();
	        model.put("productGroupName", "");
	        model.put(DropShipConstants.PRODUCT_GROUP_LIST, getCarLookupManager().getProductGroup(GenericUtils.escapeSpecialCharacters(productGroupName)));
	        
			return new ModelAndView(getSuccessView(), model);
		}
		Map<String, Object> model = new HashMap<String, Object>();
        model.put("productGroupName", productGroupName);
        model.put( DropShipConstants.PRODUCT_GROUP_LIST, getCarLookupManager().getProductGroup(GenericUtils.escapeSpecialCharacters(productGroupName)));
		return new ModelAndView(getSuccessView(), model);

	}

	/**
	 * This method is invoked when the add product group type button is clicked
	 * on the product group type screen.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addProductGroup(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		log.debug("In Product Group Controller : adding new Product Group");
		request.setAttribute("productTypeGroupForm", new ProductTypeGroupForm());
		return new ModelAndView(getAddProductGroupView());

	}

	/**
	 * The save functionality for the Product Add/Edit group If the product
	 * group id is present we get the existings product group and make the
	 * changes to it and update it. If the id is not there then we create new
	 * product group and save it. After saving we set the value back in the
	 * response for display purpose.
	 * 
	 * @param request
	 * @param response
	 * @param productTypeGroupForm
	 * @return
	 * @throws Exception 
	 */
	public ModelAndView Save(HttpServletRequest request, HttpServletResponse response,
			ProductTypeGroupForm productTypeGroupForm) throws Exception {
		log.debug("productTypeGroupForm value save---:::" + productTypeGroupForm);
		validate(productTypeGroupForm);
		if (errors.hasErrors()) {
			log.debug("In Product Group Controller : save Errors Found" + errors);
			saveError(request, errors.getFieldError().getCode());
			return new ModelAndView(getAddProductGroupView(), "productTypeGroupForm",
					productTypeGroupForm);
		}

		String sProductGroupId = productTypeGroupForm.getId();
		Long productGroupId = null;
		if (sProductGroupId != null && sProductGroupId.length() > 0) {
			productGroupId = ServletRequestUtils.getLongParameter(request, "id");
		}

		try {
			ProductGroup productGroup = null;
			if (productGroupId != null) {
				productGroup = productGroupManager.getProductGroup(productGroupId);
				
				if (DropShipConstants.INACTIVE.equals(productTypeGroupForm.getStatusCd())){
					List <ProductType> productTypes = productGroupManager.getActiveProductGroupTypes(productGroup);
					if (productTypes != null && productTypes.size() > 0){
						saveError(request, getText("Product Group '" + productGroup.getName()+"' cannot be set to Inactive until there are no Product Types associated with it", productGroupId + "", request
								.getLocale()));
						return new ModelAndView(getAddProductGroupView(), "productTypeGroupForm",
								productTypeGroupForm);
					}
					
				}

				productTypeGroupForm.setProductGroup(productGroup);
				
			}
			productGroup = productGroupManager.getAsProductGroup(productTypeGroupForm);
			setAuditInfo(request, productGroup);
			productGroup = productGroupManager.save(productGroup);
			log.debug("Save---" + productGroup);

			productTypeGroupForm.setProductGroup(productGroup);
			productTypeGroupForm = productGroupManager.getAsProductGroupForm(productGroup);
			productTypeGroupForm.setProductTypes(productGroupManager
					.getActiveProductGroupTypes(productGroup));

		} catch (ProductGroupExistsException e) {
			errors.rejectValue("name", e.getMessage());
			saveError(request, errors.getFieldError().getCode());
			return new ModelAndView(getAddProductGroupView(), "productTypeGroupForm",
					productTypeGroupForm);
		} catch (Exception e) {
			errors.rejectValue("name", "Product Group Name '" + productTypeGroupForm.getName()
					+ "' already exists!");
			saveError(request, errors.getFieldError().getCode());
			return new ModelAndView(getAddProductGroupView(), "productTypeGroupForm",
					productTypeGroupForm);
		}
		return new ModelAndView(getDetailView(), "productTypeGroupForm", productTypeGroupForm);
		}

	/**
	 * Invoking the validation for the Add/Edit group form
	 * 
	 * @param command
	 */
	public void validate(Object command) {
		Validator validators[] = getValidators();

		if (validators != null) {
			for (int index = 0; index < validators.length; index++) {
				Validator validator = validators[index];
				if (validator instanceof ProductGroupValidator) {
					if (((ProductGroupValidator) validator).supports(command.getClass())) {

						ValidationUtils.invokeValidator(validators[index], command, errors);
					}
				} else if (validator.supports(command.getClass())) {
					ValidationUtils.invokeValidator(validators[index], command, errors);
				}
			}
		}
	}

	/**  
	 * Gets the detail of the product group based on the id passed
	 * 
	 * @param request
	 *            : id of the group
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView detail(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String sProductGroupId = request.getParameter("id");
		User user = getLoggedInUser();
		if (user.getAdmin()) {
			request.getSession().setAttribute("displayRole", "admin");
		}
		Long productGroupId = null;
		if (sProductGroupId != null && sProductGroupId.length() > 0) {
			productGroupId = ServletRequestUtils.getLongParameter(request, "id");
		}
		log.debug("In Product Group Controller : detail:" + productGroupId);
		ProductTypeGroupForm productTypeGroupForm = new ProductTypeGroupForm();
		if (productGroupId == null) {
			log.debug("Product Group id was null. Redirecting...");
			return getAllProductGroups(request, response);
		}
		ProductGroup productGroup = productGroupManager.getProductGroup(Long
				.valueOf(productGroupId));
		if (productGroup == null) {
			log.debug("Product Group id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		productTypeGroupForm = productGroupManager.getAsProductGroupForm(productGroup);
		// Setting the active product types for display
		productTypeGroupForm.setProductTypes(productGroupManager
				.getActiveProductGroupTypes(productGroup));
		productTypeGroupForm.setProductGroupList(productGroupManager.getAllActiveProductGroups());
		log.debug("productGroup.getProductType().size()--" + productGroup.getProductType().size());

		return new ModelAndView(getDetailView(), "productTypeGroupForm", productTypeGroupForm);
	}

	/**
	 * Checks the request for the id (product group id). If it is null redirects
	 * back to the dashboard.html. If not null then gets the product group from
	 * database if that is not null the product group form is populated with the
	 * retrieved values, set in the session and then redirected to the edit
	 * page.
	 * 
	 * @param request
	 * @param response
	 *            : productGroupForm
	 * @return
	 * @throws Exception
	 */
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		Long productGroupId = ServletRequestUtils.getLongParameter(request, "id");
		log.debug("In Product Group Controller : edit:" + productGroupId);
		if (productGroupId == null) {
			log.debug("Product Group id was null for edit. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		ProductGroup productGroup = productGroupManager.getProductGroup(Long
				.valueOf(productGroupId));
		log.debug("edit -- productGroup.getProductType().size()--"
				+ productGroup.getProductType().size());
		// If product group is not null then populate the product group form and
		// pass it on the session for the editing the values.
		if (productGroup != null) {
			return new ModelAndView(getAddProductGroupView(), "productTypeGroupForm",
					productGroupManager.getAsProductGroupForm(productGroup));
		}
		return new ModelAndView("redirect:dashBoard.html");
	}

	/**
	 * Actually changes the status of the product group status Inactive.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView remove(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long productGroupId = ServletRequestUtils.getLongParameter(request, "id");
		log.debug("Remove product group :" + productGroupId);
		if (productGroupId == null) {
			log.debug("Product Group id was null. Redirecting...");
			return new ModelAndView("redirect:dashBoard.html");
		}
		ProductGroup productGroup = productGroupManager.getProductGroup(Long
				.valueOf(productGroupId));
		if (productGroup != null) {
			
			List<ProductType> prodTypes = productGroupManager.getActiveProductGroupTypes(productGroup);
			if (prodTypes != null && prodTypes.size() < 1){
				productGroup = productGroupManager.setGroupInactive(productGroup);
				saveMessage(request, "Successfully removed the product group - " + productGroup.getName());
				log.debug("Product Group set to Inactive successfully" + productGroup);
			}else {
				saveError(request, getText("Product Group '" + productGroup.getName()+"' cannot be removed until there are no Product Types associated with it", productGroupId + "", request
						.getLocale()));
				log.debug("Not able to delete group as we have product Types active--" + productGroup);
			}
			
		} else {
			log.debug("Product Group Not found for removal" + productGroupId);
			saveError(request, getText("Not able to delete product group", productGroupId + "", request
					.getLocale()));
		}
		return new ModelAndView(getSuccessView(), DropShipConstants.PRODUCT_GROUP_LIST,
				productGroupManager.getAllProductGroups());

	}

}
