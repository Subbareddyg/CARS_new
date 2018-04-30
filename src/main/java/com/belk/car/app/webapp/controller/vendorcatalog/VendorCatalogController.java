package com.belk.car.app.webapp.controller.vendorcatalog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.LabelValue;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dto.CatalogVendorDTO;
import com.belk.car.app.dto.NameValue;
import com.belk.car.app.dto.VendorCatalogDTO;
import com.belk.car.app.dto.VendorCatalogStyleDTO;
import com.belk.car.app.dto.VendorStyleInfo;
import com.belk.car.app.dto.VendorStylePropertiesDTO;
import com.belk.car.app.dto.VendorUpcDTO;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.app.webapp.controller.MultiActionFormController;
import com.belk.car.app.webapp.forms.CatalogVendorsForm;
import com.belk.car.app.webapp.forms.EditVendorStylePropertiesForm;
import com.belk.car.app.webapp.forms.SearchCatalogForm;
import com.belk.car.app.webapp.forms.SearchStyleForm;
import com.belk.car.app.webapp.forms.VendorPropertiesForm;

//public class VendorCatalogController extends BaseFormController {
public class VendorCatalogController extends MultiActionFormController implements DropShipConstants {
	private static final String STYLE_NUMBER = "styleNumber";
	private static final String IS_BUYER = "isBuyer";
	private transient final Log log = LogFactory.getLog(VendorCatalogController.class);
	private BindingResult errors;
        private VendorCatalogManager catalogManager;
        /**
         * Get Errors.
         * @return BindingResult
         */
	public BindingResult getErrors() {
		return errors;
	}
        /**
         * Set Errors.
         * @param errors BindingResult
         */
	public void setErrors(BindingResult errors) {
		this.errors = errors;
	}

	/**
	 * @param catalogManager VendorCatalogManager
	 */
	public void setCatalogManager(VendorCatalogManager catalogManager) {
		this.catalogManager = catalogManager;
	}

	/**
	 * Method to view only Active Vendors
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView
	 * @throws Exception
	 */
	
	public ModelAndView viewAllActive(HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'viewAll ' method...");
		}
		int isDisplayable = 1; 
		
		HttpSession session = request.getSession(false);
		// set ENABLE to false for New Catalog
		if (null != session.getAttribute(DropShipConstants.ENABLE)) {
			session.setAttribute(DropShipConstants.ENABLE, DropShipConstants.FALSE);
		}
		// remove DEPARTMENTS from session
		if (null != session.getAttribute(DropShipConstants.DEPARTMENTS)) {
			session.removeAttribute(DropShipConstants.DEPARTMENTS);
		}
		// remove NEW_DEPT_LIST from session
		if (null != session.getAttribute(DropShipConstants.NEW_DEPT_LIST)) {
			session.removeAttribute(DropShipConstants.NEW_DEPT_LIST);
		}
		
		// remove IMAGE_DIRECTORY from session- added drop ship deployment
		if (null != session.getAttribute("IMAGE_DIRECTORY")) {
			session.removeAttribute("IMAGE_DIRECTORY");
		}
		
		// remove VENDR_ID & VEN_NAME from session for New Catalog
		if (null != session.getAttribute(DropShipConstants.VENDR_ID)
				|| null != session.getAttribute(DropShipConstants.VEN_NAME)) {
			session.removeAttribute(DropShipConstants.VENDR_ID);
			session.removeAttribute(DropShipConstants.VEN_NAME);
		}
		// remove VENDOR_INFO from session for new catalog
		if (null != session.getAttribute(DropShipConstants.VENDOR_INFO)) {
			session.removeAttribute(DropShipConstants.VENDOR_INFO);
		}
		       CatalogVendorsForm catalogVendorsForm= new CatalogVendorsForm();
		       //catalogVendorsForm.setDepartmentOnly("Off");
		request.setAttribute(DropShipConstants.CATALOG_VENDOR_FORM, catalogVendorsForm);
		return new ModelAndView(getSuccessView(), DropShipConstants.CATALOG_VENDOR_LIST,
				catalogManager.getAllVendors(isDisplayable));
		}
	
	/**
	 * View list of Vendors.
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ModelAndView
	 * @throws Exception
	 */
	public ModelAndView viewAll(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'viewAll ' method...");
		}
		HttpSession session = request.getSession(false);
		// set ENABLE to false for New Catalog
		if (null != session.getAttribute(DropShipConstants.ENABLE)) {
			session.setAttribute(DropShipConstants.ENABLE, DropShipConstants.FALSE);
		}
		// remove DEPARTMENTS from session
		if (null != session.getAttribute(DropShipConstants.DEPARTMENTS)) {
			session.removeAttribute(DropShipConstants.DEPARTMENTS);
		}
		// remove NEW_DEPT_LIST from session
		if (null != session.getAttribute(DropShipConstants.NEW_DEPT_LIST)) {
			session.removeAttribute(DropShipConstants.NEW_DEPT_LIST);
		}

		// remove VENDR_ID & VEN_NAME from session for New Catalog
		if (null != session.getAttribute(DropShipConstants.VENDR_ID)
				|| null != session.getAttribute(DropShipConstants.VEN_NAME)) {
			session.removeAttribute(DropShipConstants.VENDR_ID);
			session.removeAttribute(DropShipConstants.VEN_NAME);
		}
		// remove VENDOR_INFO from session for new catalog
		if (null != session.getAttribute(DropShipConstants.VENDOR_INFO)) {
			session.removeAttribute(DropShipConstants.VENDOR_INFO);
		}
                User currentUser = getLoggedInUser();
                if(currentUser.getAdmin()) {
                    session.setAttribute(DropShipConstants.IS_ADMIN, DropShipConstants.TRUE);
                } else {
                    session.setAttribute(DropShipConstants.IS_ADMIN, DropShipConstants.FALSE);

                }
                 if(currentUser.getBuyerRights()) {
                    session.setAttribute(IS_BUYER, DropShipConstants.TRUE);
                } else {
                    session.setAttribute(IS_BUYER, DropShipConstants.FALSE);

                }
                
                 for(LabelValue role:currentUser.getRoleList()){
                	 if(role.getValue().equals(Role.ROLE_BUYER)){
                	
                		 session.setAttribute(IS_BUYER, DropShipConstants.TRUE);
                		 break;
                	 } else {
                         session.setAttribute(IS_BUYER, DropShipConstants.FALSE);
                	 }
                 }
                
                Properties properties = PropertyLoader.loadProperties("ftp.properties");
                String imageAppURL = properties.getProperty("imageAppURL");
                session.setAttribute("imageAppURL", imageAppURL);
               CatalogVendorsForm catalogVendorsForm= new CatalogVendorsForm();
		request.setAttribute(DropShipConstants.CATALOG_VENDOR_FORM, catalogVendorsForm);
		User user = getLoggedInUser();
		request.setAttribute("user",user);
		// unlock catalog1
		catalogManager.unlockCatalogs(currentUser.getUsername());
		
		return new ModelAndView(getSuccessView(), DropShipConstants.CATALOG_VENDOR_LIST,
				catalogManager.getAllVendors());
	}
        /**
         * Serach the vendors who have catalogs in the CARS.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @param catalogVendorsForm CatalogVendorsForm
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView search(
			HttpServletRequest request, HttpServletResponse response,
			CatalogVendorsForm catalogVendorsForm)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'Search' method..." + catalogVendorsForm.getVendorName());
		}
		CatalogVendorDTO catalogVendorDTO = new CatalogVendorDTO();
		List<CatalogVendorDTO> vendorList = new ArrayList();
                //Validate the search criteria entered by the user.
		validate(catalogVendorsForm);
                //set form in request.
		request.setAttribute(DropShipConstants.CATALOG_VENDOR_FORM, catalogVendorsForm);
		User user = getLoggedInUser();
		String userId = DropShipConstants.EMPTY_STRING + user.getId();
		if (errors.hasErrors()) {
                    if (log.isDebugEnabled()) {
			log.debug("Error Found");
                    }
                    request.setAttribute("scrollPos", "0");
			saveErrors(request, errors);
		}
		else {
                        if (log.isDebugEnabled()) {
			log.debug(" Start Set DTO");
                        }
                        //set the search criteria in DTO
                        if(!(catalogVendorsForm.getVendorNumber().equals(""))) {
                           Long venNo = Long.parseLong(catalogVendorsForm.getVendorNumber());
                           catalogVendorDTO.setVendorNumber(venNo);
                        }
                        
			
			catalogVendorDTO.setName(catalogVendorsForm.getVendorName());
			catalogVendorDTO.setIsDisplayable(catalogVendorsForm.getDisplay());
			catalogVendorDTO.setDateLastImportStart(catalogVendorsForm.getDateLastImportStart());
			catalogVendorDTO.setDateLastImportEnd(catalogVendorsForm.getDateLastImportEnd());
			catalogVendorDTO.setUserId(""+user.getId());
			catalogVendorDTO.setUserDeptOnly(catalogVendorsForm.getDepartmentOnly());
                        //Serach Catalog Vendors
			vendorList = catalogManager.searchCatalogVendors(catalogVendorDTO);
		}
                //return catalog vendors list.
                if (log.isDebugEnabled()) {
			log.debug("Search Method End...");
		}
		return new ModelAndView(getSuccessView(), DropShipConstants.CATALOG_VENDOR_LIST, vendorList);
	}
        /**
         * View All the catalogs of a particular Vendor
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView viewAllVendorCatalogs(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Start 'viewAllVendorCatalogs' method...");
		}
		User currentUser = getLoggedInUser();
		HttpSession session = request.getSession(false);

		// remove DEPARTMENTS from session
		if (null != session.getAttribute(DropShipConstants.DEPARTMENTS)) {
			session.removeAttribute(DropShipConstants.DEPARTMENTS);
		}
		// remove NEW_DEPT_LIST from session
		if (null != session.getAttribute(DropShipConstants.NEW_DEPT_LIST)) {
			session.removeAttribute(DropShipConstants.NEW_DEPT_LIST);
		}
		// set ENABLE to false for New Catalog
		if (null != session.getAttribute(DropShipConstants.ENABLE)) {
			session.setAttribute(DropShipConstants.ENABLE, DropShipConstants.FALSE);
		}
		// remove VENDR_ID & VEN_NAME from session for New Catalog
		if (null != session.getAttribute(DropShipConstants.VENDR_ID)
				|| null != session.getAttribute(DropShipConstants.VEN_NAME)) {
			session.removeAttribute(DropShipConstants.VENDR_ID);
			session.removeAttribute(DropShipConstants.VEN_NAME);
		}
		if (currentUser.isBuyer()) {
			if (log.isDebugEnabled()) {
				log.debug("User has role buyer..set mode to edit");
			}
			session.setAttribute(DropShipConstants.MODE, DropShipConstants.EDIT_VENDOR_CATAOLOG);
		}
		else{
			session.setAttribute(DropShipConstants.MODE, DropShipConstants.VIEW_ONLY);
		}
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'viewAllVendorCatalogs' method...");
		}
		CatalogVendorDTO catalogDTO = null;
		String vendorId = request.getParameter(DropShipConstants.VENDOR_Id);
		if (log.isDebugEnabled()) {
			log.debug("vendorId=" + vendorId);
		}
		if (null == vendorId) {
			vendorId = (String) request.getSession().getAttribute(
					DropShipConstants.VENDOR_CATALOG_VENDOR_ID);
			if(null == vendorId){
				System.out.println("if vendor id is null");
				return new ModelAndView("redirect:/mainMenu.html");
			}
			catalogDTO = (CatalogVendorDTO) request.getSession().getAttribute(
					DropShipConstants.VENDOR_INFO);
			if(null == catalogDTO){
			System.out.println("if catalogDTO is null");
				return new ModelAndView("redirect:/mainMenu.html");
			}
		}
		else {
                        if (log.isDebugEnabled()) {
			log.debug("Not in session=" + vendorId);
                        }
                        //Get Vendor Info To display in Header Section.
			catalogDTO = catalogManager.getVendorInfo(vendorId);
			catalogDTO.setVendorId(vendorId);
			catalogDTO.setNumSKUs(Long.parseLong(request.getParameter(DropShipConstants.NUM_OF_SKUS)));
			catalogDTO.setNumStyle(Long.parseLong(request.getParameter(DropShipConstants.NUM_OF_STYLES)));
			request.getSession().setAttribute(DropShipConstants.VENDOR_INFO, catalogDTO);
			request.getSession().setAttribute(DropShipConstants.VENDOR_CATALOG_VENDOR_ID, vendorId);
			session.setAttribute(DropShipConstants.VENDOR_CATALOG_INFO, catalogDTO);

		}

		/**
		 * ---- To access vendor info in bread crumb & tab
		 */
                CatalogVendorDTO catalogVendorDTO = new CatalogVendorDTO();
	        catalogVendorDTO.setVendorId(vendorId);
		SearchCatalogForm searchCatalogForm = new SearchCatalogForm();
		searchCatalogForm.setVendorId(vendorId);
		searchCatalogForm.setTempStatus("All");
		request.setAttribute(DropShipConstants.SEARCH_CATALOG_FORM, searchCatalogForm);
		request.setAttribute(DropShipConstants.USER_ID, this.getLoggedInUser().getUsername());
                if (log.isDebugEnabled()) {
			log.debug("End 'viewAllVendorCatalogs' method...");
		}
        //Setting the display table parameters to retain the user action.
        setDisplayTableParameters(request);
        request.setAttribute("scrollPos", "0");
        // unlock catalog2
		catalogManager.unlockCatalogs(currentUser.getUsername());
		return new ModelAndView(DropShipConstants.VENDOR_CATALOG_LIST_PAGE,
				DropShipConstants.VENDOR_CATALOG_LIST, catalogManager.searchVendorCatalogs(catalogVendorDTO));
	}
        /**
         * Search Vendor Catalogs accroing the search Criteria.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @param searchCatalogForm SearchCatalogForm
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView searchVendorCatalogs(
			HttpServletRequest request, HttpServletResponse response,
			SearchCatalogForm searchCatalogForm)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'searchVendorCatalogs' method..." + searchCatalogForm);
		}
		CatalogVendorDTO catalogDTO = (CatalogVendorDTO) request.getSession().getAttribute(
				DropShipConstants.VENDOR_INFO);
		if(null == catalogDTO){
		log.debug("if catalogDTO is null");
			return new ModelAndView("redirect:/mainMenu.html");
		}
		List<VendorCatalogDTO> vendorCatalogList = new ArrayList();
		validate(searchCatalogForm);
		if (errors.hasErrors()) {
			log.debug("Error Found");
                        request.setAttribute("scrollPos", "0");
			saveErrors(request, errors);
		}
		else {
			CatalogVendorDTO catalogVendorDTO = new CatalogVendorDTO();
			catalogVendorDTO.setVendorId(searchCatalogForm.getVendorId());
			catalogVendorDTO.setCatalogName(searchCatalogForm.getCatalogName());
			catalogVendorDTO.setDateLastUpdateStart(searchCatalogForm.getDateLastUpdatedStart());
			catalogVendorDTO.setDateLastUpdateEnd(searchCatalogForm.getDateLastUpdatedEnd());
			catalogVendorDTO.setStatus(searchCatalogForm.getStatus());
			catalogVendorDTO.setDepartment(searchCatalogForm.getDepartment());
			catalogVendorDTO.setUserDeptOnly(searchCatalogForm.getDepartmentOnly());
			User user = getLoggedInUser();
			catalogVendorDTO.setUserId("" +user.getId());
			request.setAttribute(DropShipConstants.USER_ID, this.getLoggedInUser().getUsername());
			vendorCatalogList = catalogManager.searchVendorCatalogs(catalogVendorDTO);
		}
		searchCatalogForm.setTempStatus(searchCatalogForm.getStatus());
		request.setAttribute(DropShipConstants.SEARCH_CATALOG_FORM, searchCatalogForm);
                request.setAttribute("scrollPos", "0");
        //Setting the display table parameters to retain the user action.
        setDisplayTableParameters(request);
		return new ModelAndView(DropShipConstants.VENDOR_CATALOG_LIST_PAGE,
				DropShipConstants.VENDOR_CATALOG_LIST, vendorCatalogList);
	}
        /**
         * Lock Or Unlock a Catalog
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @param searchCatalogForm SearchCatalogForm
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView lockUnlockCatalog(
			HttpServletRequest request, HttpServletResponse response,
			SearchCatalogForm searchCatalogForm)
			throws Exception {

		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'lockUnlockCatalog' method..." + searchCatalogForm);
		}
		List<VendorCatalogDTO> vendorCatalogList = new ArrayList();
                //Set search criteria into CatalogVendorDTO
		CatalogVendorDTO catalogVendorDTO = new CatalogVendorDTO();
		catalogVendorDTO.setVendorId(searchCatalogForm.getVendorId());
		catalogVendorDTO.setCatalogName(searchCatalogForm.getCatalogName());
		catalogVendorDTO.setDateLastUpdateStart(searchCatalogForm.getDateLastUpdatedStart());
		catalogVendorDTO.setDateLastUpdateEnd(searchCatalogForm.getDateLastUpdatedEnd());
		catalogVendorDTO.setStatus(searchCatalogForm.getStatus());
		catalogVendorDTO.setDepartment(searchCatalogForm.getDepartment());
		catalogVendorDTO.setUserDeptOnly(searchCatalogForm.getDepartmentOnly());
		User user = getLoggedInUser();
		catalogVendorDTO.setUserId("" + user.getId());
		String userId = user.getUsername();
		request.setAttribute(DropShipConstants.USER_ID, userId);
		String vendorCatalogId = request.getParameter(DropShipConstants.VENDOR_CATALOG_ID);
                //get the Mode Lock/Unlock catalog from request.
		String mode = request.getParameter(DropShipConstants.MODE);
                //Lock or Unlock teh Catalog
		//vendorCatalogList = catalogManager.lockUnlockCatalog(catalogVendorDTO, vendorCatalogId,	user, mode);
		searchCatalogForm.setTempStatus(searchCatalogForm.getStatus());
		request.setAttribute(DropShipConstants.SEARCH_CATALOG_FORM, searchCatalogForm);
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'lockUnlockCatalog' method..." + searchCatalogForm);
		}
                request.setAttribute("scrollPos", request.getParameter("scrollPos"));
        //Setting the display table parameters to retain the user action.
        setDisplayTableParameters(request);
		return new ModelAndView(DropShipConstants.VENDOR_CATALOG_LIST_PAGE,
				DropShipConstants.VENDOR_CATALOG_LIST, vendorCatalogList);
	}
	/**
	 * Sets  the Display table parameters like d-<Table_Id>-p, to retain the previous actions of the user.
	 * This is required when user selects any page and clicks on Lock/Unlock button.
	 * This method will retain those page numbers and user could view the same page if clicked on any Lock/Unlock button.
	 * @param request	{@link HttpServletRequest} Request Object 
	 */
    private void setDisplayTableParameters(HttpServletRequest request) {
		if(log.isDebugEnabled()){
			log.debug("Inside setDisplayTableParameters() method.");
		}
		HttpSession session = request.getSession();
		String page=request.getParameter(CATALOG_REQUEST_DISPLAY_TABLE_PAGINATION_ID);
		if (log.isDebugEnabled()) {
			log.debug("Pagination Parameter:"+ page);
		}
		if(StringUtils.isBlank(page)){
			log.debug("Setting pagination parameter to 1.");
			page="1";
		}
		if (log.isDebugEnabled()) {
			log.debug("Setting pagination parameter to :"+ page);
		}
		//Set the parameter in request.
		session.setAttribute("pagination", page);
		request.setAttribute("pagination", page);
	}
	/**
     * View All the Open Catalog (The catalogs which dosen't have Complete status.)
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return ModelAndView
     * @throws Exception
     */
	public ModelAndView viewAllOpenCatalogs(HttpServletRequest request, HttpServletResponse response)
			throws Exception {

                if (log.isDebugEnabled()) {
			log.debug("entering workflow 'viewAllOpenCatalogs' method...");
		}
                User currentUser = getLoggedInUser();
		HttpSession session = request.getSession(false);
		// remove DEPARTMENTS from session
		if (null != session.getAttribute(DropShipConstants.DEPARTMENTS)) {
			session.removeAttribute(DropShipConstants.DEPARTMENTS);
		}
		// remove NEW_DEPT_LIST from session
		if (null != session.getAttribute(DropShipConstants.NEW_DEPT_LIST)) {
			session.removeAttribute(DropShipConstants.NEW_DEPT_LIST);
		}
		// set ENABLE to false for New Catalog
		if (null != session.getAttribute(DropShipConstants.ENABLE)) {
			session.setAttribute(DropShipConstants.ENABLE, DropShipConstants.FALSE);
		}
		// remove VENDR_ID & VEN_NAME from session for New Catalog
		if (null != session.getAttribute(DropShipConstants.VENDR_ID)
				|| null != session.getAttribute(DropShipConstants.VEN_NAME)) {
			session.removeAttribute(DropShipConstants.VENDR_ID);
			session.removeAttribute(DropShipConstants.VEN_NAME);
		}
		// remove VENDOR_INFO from session for new catalog
		if (null != session.getAttribute(DropShipConstants.VENDOR_INFO)) {
			session.removeAttribute(DropShipConstants.VENDOR_INFO);
		}
		if (currentUser.isBuyer()) {
			if (log.isDebugEnabled()) {
				log.debug("User has role Buyer..set mode to edit");
			}
			session.setAttribute(DropShipConstants.MODE, DropShipConstants.EDIT_VENDOR_CATAOLOG);
		}
		else{
			session.setAttribute(DropShipConstants.MODE, "viewOnly");
		}
		SearchCatalogForm searchCatalogForm = new SearchCatalogForm();
		searchCatalogForm.setTempStatus("All");
		request.setAttribute(DropShipConstants.SEARCH_CATALOG_FORM, searchCatalogForm);
		User user = getLoggedInUser();
		String userId = user.getUsername();
		request.setAttribute(DropShipConstants.USER_ID, userId);
                 if (log.isDebugEnabled()) {
			log.debug("End 'viewAllOpenCatalogs' method...");
		}
        request.setAttribute("scrollPos","0");
        //Setting the display table parameters to retain the user action.
        setDisplayTableParameters(request);
       
		return new ModelAndView(DropShipConstants.OPEN_CATALOG_LIST_PAGE,
				DropShipConstants.VENDOR_CATALOG_LIST, catalogManager.getOpenCatalogs());

	}
        /**
         * Search Open Catalogs Catalogs using search criteria endter by User.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @param searchCatalogForm SearchCatalogForm
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView searchOpenCatalogs(
			HttpServletRequest request, HttpServletResponse response,
			SearchCatalogForm searchCatalogForm)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'searchOpenCatalogs' method...");
		}
		List<VendorCatalog> vendorCatalogList = new ArrayList();
		validate(searchCatalogForm);
		if (errors.hasErrors()) {
                    if (log.isDebugEnabled()) {
			log.debug("Error Found");
                    }
                    request.setAttribute("scrollPos", "0");
			saveErrors(request, errors);
		}
		else {
		 	//Set Search Criteria in CatalogVendorDTO
                        CatalogVendorDTO catalogVendorDTO = new CatalogVendorDTO();
			catalogVendorDTO.setVendorId(searchCatalogForm.getVendorId());
			catalogVendorDTO.setName(searchCatalogForm.getVendorName());
			catalogVendorDTO.setStatus(searchCatalogForm.getStatus());
			catalogVendorDTO.setUserDeptOnly(searchCatalogForm.getDepartmentOnly());
			User user = getLoggedInUser();
                        catalogVendorDTO.setUserId(DropShipConstants.EMPTY_STRING+user.getId());
			String userId = user.getUsername();
			searchCatalogForm.setTempStatus(searchCatalogForm.getStatus());
			request.setAttribute(DropShipConstants.USER_ID, userId);
                        //Perform a search Operation.
			vendorCatalogList = catalogManager.searchOpenCatalogs(catalogVendorDTO);
		}
		User user = getLoggedInUser();
		String userId = user.getUsername();
		request.setAttribute(DropShipConstants.USER_ID, userId);
		request.setAttribute(DropShipConstants.SEARCH_CATALOG_FORM, searchCatalogForm);
                 request.setAttribute("scrollPos","0");
        //Setting the display table parameters to retain the user action.
		setDisplayTableParameters(request);
		return new ModelAndView(DropShipConstants.OPEN_CATALOG_LIST_PAGE,
				DropShipConstants.VENDOR_CATALOG_LIST, vendorCatalogList);
	}
        /**
         * Lock/Unlock Open Catalogs Catalogs using search criteria endter by User.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @param searchCatalogForm SearchCatalogForm
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView lockUnlockOpenCatalog(
			HttpServletRequest request, HttpServletResponse response,
			SearchCatalogForm searchCatalogForm)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'lockUnlockOpenCatalog' method..." + searchCatalogForm);
		}
		List<VendorCatalog> vendorCatalogList = new ArrayList();
                // Set teh search Criteria in  CatalogVendorDTO.
		CatalogVendorDTO catalogVendorDTO = new CatalogVendorDTO();
		catalogVendorDTO.setVendorId(searchCatalogForm.getVendorId());
		catalogVendorDTO.setName(searchCatalogForm.getVendorName());
		catalogVendorDTO.setStatus(searchCatalogForm.getStatus());
		catalogVendorDTO.setUserDeptOnly(searchCatalogForm.getDepartmentOnly());
		User user = getLoggedInUser();
		String userId = user.getUsername();
		request.setAttribute(DropShipConstants.USER_ID, userId);
		String vendorCatalogId = request.getParameter(DropShipConstants.VENDOR_CATALOG_ID);
                //Get the mode Lock/Unlock from Catalog
		String mode = request.getParameter(DropShipConstants.MODE);
		searchCatalogForm.setTempStatus(searchCatalogForm.getStatus());
                //Lock or Unlock the catalog.
                catalogVendorDTO.setUserId("" + user.getId());
		//vendorCatalogList = catalogManager.lockUnlockOpenCatalog(catalogVendorDTO, vendorCatalogId,	user, mode);
		request.setAttribute(DropShipConstants.SEARCH_CATALOG_FORM, searchCatalogForm);
                 request.setAttribute("scrollPos", request.getParameter("scrollPos"));
                if (log.isDebugEnabled()) {
			log.debug("End workflow 'lockUnlockOpenCatalog' method..." + vendorCatalogList.size());
		}
        //Setting the display table parameters to retain the user action.
        setDisplayTableParameters(request);
		return new ModelAndView(DropShipConstants.OPEN_CATALOG_LIST_PAGE,
				DropShipConstants.VENDOR_CATALOG_LIST, vendorCatalogList);
	}
        /**
         * Get all the Vendor Catalog Styles.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView viewAllVendorStyles(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'viewAllVendorStyles' method...");
		}
		String vendorId = (String) request.getSession().getAttribute(
				DropShipConstants.VENDOR_CATALOG_VENDOR_ID);
		if(null == vendorId){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		SearchStyleForm searchStyleForm = new SearchStyleForm();
		request.setAttribute(DropShipConstants.SEARCH_STYLE_FORM, searchStyleForm);
		User user = getLoggedInUser();
		String userId = user.getUsername();
		request.setAttribute(DropShipConstants.USER_ID, userId);
                request.setAttribute("scrollPos", "0");
		VendorCatalogStyleDTO catalogStyleDTO = new VendorCatalogStyleDTO();
		catalogStyleDTO.setVendorId(vendorId);
		request.getSession().setAttribute(DropShipConstants.VENDOR_STYLE_SEARCH_CRITERIA,
				catalogStyleDTO);
                //Get the List of all styles from Catalog for a particular Vendor.
		List<VendorCatalogStyleDTO> vendorStyleList = catalogManager
				.getVendorStyles(catalogStyleDTO);
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'viewAllVendorStyles' method...");
		}
      
		return new ModelAndView(DropShipConstants.VENDOR_STYLE_LIST_PAGE,
				DropShipConstants.VENDOR_STYLE_LIST, vendorStyleList);

	}
        /**
         * Get all the Vendor Catalog Styles.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView searchVendorStyles(
			HttpServletRequest request, HttpServletResponse response,
			SearchStyleForm searchStyleForm)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'searchVendorStyles' method...");
		}
		String vendorId = (String) request.getSession().getAttribute(
				DropShipConstants.VENDOR_CATALOG_VENDOR_ID);
		if(null == vendorId){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		List<VendorCatalogStyleDTO> vendorStyleList = new ArrayList();
		User user = getLoggedInUser();
		String userId = user.getUsername();
		request.setAttribute(DropShipConstants.USER_ID, userId);
                searchStyleForm.setTempStatus(searchStyleForm.getStatus());
		request.setAttribute(DropShipConstants.SEARCH_STYLE_FORM, searchStyleForm);
                 request.setAttribute("scrollPos", "0");
		validate(searchStyleForm);
		if (errors.hasErrors()) {
                    if (log.isDebugEnabled()) {
			log.debug("Error Found");
                    }
			saveErrors(request, errors);
                        request.setAttribute("scrollPos", "0");
		}
		else {
		 	//Set search criteria in VendorCatalogStyleDTO
                        VendorCatalogStyleDTO catalogStyleDTO = new VendorCatalogStyleDTO();
                        
                        if(searchStyleForm.getVendorId() == null){
                        	log.debug("search vendor id is null in session");
                        	return new ModelAndView("redirect:/mainMenu.html");
                        }else{
			catalogStyleDTO.setVendorId(searchStyleForm.getVendorId());
			catalogStyleDTO.setVendorStyleId(searchStyleForm.getVendorStyle());
			catalogStyleDTO.setDescription(searchStyleForm.getDescription());
			catalogStyleDTO.setDateUpdateStart(searchStyleForm.getDateLastUpdatedStart());
			catalogStyleDTO.setDateUpdateEnd(searchStyleForm.getDateLastUpdatedEnd());
			catalogStyleDTO.setStatus(searchStyleForm.getStatus());
			catalogStyleDTO.setUserDerartmentOnly(searchStyleForm.getDepartmentOnly());
			catalogStyleDTO.setDepartment(searchStyleForm.getDepartment());
			catalogStyleDTO.setVendorUPC(searchStyleForm.getVendorUPC());
			catalogStyleDTO.setUserId(DropShipConstants.EMPTY_STRING + user.getId());
			request.getSession().setAttribute(DropShipConstants.VENDOR_STYLE_SEARCH_CRITERIA,
					catalogStyleDTO);
                        //Perfor a search for Vendor Style
			vendorStyleList = catalogManager.getVendorStyles(catalogStyleDTO);
                        }
		}
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'searchVendorStyles' method...");
		}
		return new ModelAndView(DropShipConstants.VENDOR_STYLE_LIST_PAGE,
				DropShipConstants.VENDOR_STYLE_LIST, vendorStyleList);

	}
        /**
         * Get all the Vendor Styles  for a particular catalog.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView viewAllVendorCatalogStyles(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'viewAllVendorCatalogStyles' method...");
		}
		String vendorCatalogId = (String) request.getSession().getAttribute("vcID");
		if(vendorCatalogId.equals(null) || vendorCatalogId.equals("")){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		String vendorCatalogTemplateId = catalogManager.getVendorCatalogTemplateId(vendorCatalogId);
		if (null != vendorCatalogTemplateId) {
                        // Create SearchStyleForm form and put it in request.
			SearchStyleForm searchStyleForm = new SearchStyleForm();
			String vendorId = (String) request.getSession().getAttribute(
					DropShipConstants.VENDOR_CATALOG_VENDOR_ID);
			if(vendorId.equals("") || vendorId.equals(null)){
				System.out.println("vendorID is null..........");
				return new ModelAndView("redirect:/mainMenu.html");
			}
			searchStyleForm.setCatalogId(vendorCatalogId);
			searchStyleForm.setVendorId(vendorId);
			searchStyleForm.setVendorCatalogTemplaetId(vendorCatalogTemplateId);
			request.setAttribute(DropShipConstants.SEARCH_STYLE_FORM, searchStyleForm);
			User user = getLoggedInUser();
			String userId = user.getUsername();
			request.setAttribute(DropShipConstants.USER_ID, userId);
			VendorCatalogStyleDTO catalogStyleDTO = new VendorCatalogStyleDTO();
			catalogStyleDTO.setVendorId(vendorId);
			catalogStyleDTO.setVendorCatalogId(vendorCatalogId);
			VendorCatalog catalogInfo = catalogManager.getCatalogInfo(Long
					.parseLong(vendorCatalogId));
			request.getSession().setAttribute(DropShipConstants.CATALOG_INFO, catalogInfo);
			List<VendorCatalogStyleDTO> vendorList = catalogManager
					.getVendorCatalogStyles(catalogStyleDTO);
			request.getSession().setAttribute(
					DropShipConstants.VENDOR_CATALOG_STYLE_SEARCH_CRITERIA, catalogStyleDTO);
            request.setAttribute("scrollPos", "0");
           
			return new ModelAndView(DropShipConstants.VENDOR_CATALOG_STYLE_LIST_PAGE,
					DropShipConstants.VENDOR_STYLE_LIST, vendorList);
		}
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'viewAllVendorCatalogStyles' method...");
		}
        
       
        
		return new ModelAndView(DropShipConstants.VENDOR_CATALOG_STYLE_LIST_PAGE);


	}
         /**
         * Get all the Vendor Styles  for a particular catalog.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView searchVendorCatalogStyles(
			HttpServletRequest request, HttpServletResponse response,
			SearchStyleForm searchStyleForm)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'searchVendorCatalogStyles' method...");
		}
		List<VendorCatalogStyleDTO> vendorStyleList = new ArrayList();
		User user = getLoggedInUser();
		String userId = user.getUsername();
		request.setAttribute(DropShipConstants.USER_ID, userId);
                searchStyleForm.setTempStatus(searchStyleForm.getStatus());
		request.setAttribute(DropShipConstants.SEARCH_STYLE_FORM, searchStyleForm);
		validate(searchStyleForm);
		if (errors.hasErrors()) {
			log.debug("Error Found");
                        request.setAttribute("scrollPos", "0");
			saveErrors(request, errors);
		}
		else {
		       //Set search criteria in VendorCatalogStyleDTO
                        VendorCatalogStyleDTO catalogStyleDTO = new VendorCatalogStyleDTO();
			catalogStyleDTO.setVendorId(searchStyleForm.getVendorId());
			catalogStyleDTO.setVendorStyleId(searchStyleForm.getVendorStyle());
			catalogStyleDTO.setDescription(searchStyleForm.getDescription());
			catalogStyleDTO.setDateUpdateStart(searchStyleForm.getDateLastUpdatedStart());
			catalogStyleDTO.setDateUpdateEnd(searchStyleForm.getDateLastUpdatedEnd());
			catalogStyleDTO.setStatus(searchStyleForm.getStatus());
			catalogStyleDTO.setUserDerartmentOnly(searchStyleForm.getDepartmentOnly());
			catalogStyleDTO.setDepartment(searchStyleForm.getDepartment());
			catalogStyleDTO.setUserId(""+user.getId());
			catalogStyleDTO.setVendorCatalogId(searchStyleForm.getCatalogId());
			catalogStyleDTO.setVendorUPC(searchStyleForm.getVendorUPC());
                        //Perform searh useing search criteria.
			vendorStyleList = catalogManager.getVendorCatalogStyles(catalogStyleDTO);
			request.getSession().setAttribute(
					DropShipConstants.VENDOR_CATALOG_STYLE_SEARCH_CRITERIA, catalogStyleDTO);
                         request.setAttribute("scrollPos", "0");
		}
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'searchVendorCatalogStyles' method...");
		}
		return new ModelAndView(DropShipConstants.VENDOR_CATALOG_STYLE_LIST_PAGE,
				DropShipConstants.VENDOR_STYLE_LIST, vendorStyleList);

	}
         /**
         * Lock or Unlock Vendor Style.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView lockUnlockVendorStyles(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'lockUnlockVendorStyles' method...");
		}
		SearchStyleForm searchStyleForm = new SearchStyleForm();
		List<VendorCatalogStyleDTO> vendorList = new ArrayList();
                searchStyleForm.setTempStatus(searchStyleForm.getStatus());
		request.setAttribute(DropShipConstants.SEARCH_STYLE_FORM, searchStyleForm);
		VendorCatalogStyleDTO catalogStyleDTO = (VendorCatalogStyleDTO) request.getSession()
				.getAttribute(DropShipConstants.VENDOR_STYLE_SEARCH_CRITERIA);
		if(catalogStyleDTO == null){
			System.out.println("catalogStyleDTO is null..............");
			return new ModelAndView("redirect:/mainMenu.html");
		}
		User user = getLoggedInUser();
		String userId = user.getUsername();
		request.setAttribute(DropShipConstants.USER_ID, userId);
                request.setAttribute("scrollPos", request.getParameter("scrollPos"));
		String vendorStyleId = request.getParameter(DropShipConstants.VENDOR_STYLE_ID);
		String vendorCatalogId = request.getParameter(DropShipConstants.VENDOR_CATALOG_ID);
		String mode = request.getParameter(DropShipConstants.MODE);
                //Lock or Unlock Vendor Style
		//vendorList = catalogManager.lockUnlockVendorStyles(catalogStyleDTO, vendorStyleId,vendorCatalogId, user, mode);
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'lockUnlockVendorStyles' method...");
		}
		return new ModelAndView(DropShipConstants.VENDOR_STYLE_LIST_PAGE,
				DropShipConstants.VENDOR_STYLE_LIST, vendorList);

	}
        /**
         * Lock or Unlock Vendor Style form a particular catalog.
         * @param request HttpServletRequest
         * @param response HttpServletResponse
         * @return ModelAndView
         * @throws Exception
         */
	public ModelAndView lockUnlockVendorCatalogStyles(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'lockUnlockVendorCatalogStyles' method...");
		}
		SearchStyleForm searchStyleForm = new SearchStyleForm();
		List<VendorCatalogStyleDTO> vendorList = new ArrayList();
		
		VendorCatalogStyleDTO catalogStyleDTO = (VendorCatalogStyleDTO) request.getSession()
				.getAttribute(DropShipConstants.VENDOR_CATALOG_STYLE_SEARCH_CRITERIA);
		if(catalogStyleDTO == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		User user = getLoggedInUser();
		String userId = user.getUsername();
		request.setAttribute(DropShipConstants.USER_ID, userId);
		String vendorStyleId = request.getParameter(DropShipConstants.VENDOR_STYLE_ID);
		String vendorCatalogId = request.getParameter(DropShipConstants.VENDOR_CATALOG_ID);
		String mode = request.getParameter(DropShipConstants.MODE);
                searchStyleForm.setVendorCatalogTemplaetId(request.getParameter("vendorCatalogTemplaetId"));
                searchStyleForm.setTempStatus(searchStyleForm.getStatus());
                request.setAttribute(DropShipConstants.SEARCH_STYLE_FORM, searchStyleForm);
                 request.setAttribute("scrollPos", request.getParameter("scrollPos"));
                //Lock or Unlock Vendor Style from a particular catalog
		//vendorList = catalogManager.lockUnlockVendorStyles(catalogStyleDTO, vendorStyleId,vendorCatalogId, user, mode);
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'lockUnlockVendorCatalogStyles' method...");
		}
		return new ModelAndView(DropShipConstants.VENDOR_CATALOG_STYLE_LIST_PAGE,
				DropShipConstants.VENDOR_STYLE_LIST, vendorList);

	}
        /**
         * Get Proerties of a Vendor Catalofg Styles.
         * @param request
         * @param response
         * @return
         * @throws Exception
         */
	public ModelAndView getStylesProperties(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'getStylesProperties' method...");
		}
		String vendorId = (String) request.getSession().getAttribute(
				DropShipConstants.VENDOR_CATALOG_VENDOR_ID);
		if(vendorId.equals("") || vendorId.equals(null)){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		String vendorStyleId = request.getParameter(DropShipConstants.VENDOR_STYLE_ID);
		//VendorStyle style=(VendorStyle) catalogManager.getById(VendorStyle.class, new Long(vendorStyleId));
		String vendorCatalogId = request.getParameter(DropShipConstants.VENDOR_CATALOG_ID);
		String recordNum = request.getParameter(DropShipConstants.RECORD_NUMBER);
		String catalogTemplateId = request.getParameter(DropShipConstants.CATALOG_TEMPLATE_ID);
		String vendorUpc = request.getParameter(DropShipConstants.VENDOR_UPC);
		VendorStylePropertiesDTO stylePropertiesDTO = catalogManager.getVendorStyleProperties(
				vendorStyleId, vendorCatalogId, recordNum, catalogTemplateId, vendorId, vendorUpc);
		request.setAttribute(DropShipConstants.MAPPED_ATTRIBUTE_LIST, stylePropertiesDTO
				.getMappedFields());
		request.setAttribute(DropShipConstants.UNMAPPED_ATTRIBUTE_LIST, stylePropertiesDTO
				.getUnmappedList());
		request.setAttribute(DropShipConstants.STYLE_IMAGE_LIST, stylePropertiesDTO
				.getStyleImages());
		request.setAttribute(DropShipConstants.VENDOR_UPC_LIST, stylePropertiesDTO.getUpcList());
		VendorStyleInfo styleInfo = stylePropertiesDTO.getStyleInfo();
		CatalogVendorDTO vendorInfo = (CatalogVendorDTO) request.getSession().getAttribute(
				DropShipConstants.VENDOR_INFO);
		if(vendorInfo == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
                String vendorNumber = ""+vendorInfo.getVendorNumber();
                vendorNumber = vendorNumber.trim();
		styleInfo.setVendorNumber(vendorNumber);
		styleInfo.setVendorName(vendorInfo.getName());
		request.setAttribute(DropShipConstants.VENDOR_STYLE_INFO, styleInfo);
		//request.setAttribute(STYLE_NUMBER, style.getVendorStyleNumber());
		stylePropertiesDTO.setCatalogTempalteId(catalogTemplateId);
		request.getSession().setAttribute(DropShipConstants.STYLE_PROPERTIES, stylePropertiesDTO);
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'getStylesProperties' method...");
		}
        User user = getLoggedInUser();
		return new ModelAndView(DropShipConstants.VENDOR_STYLE_PROPERTIES);

	}
        /**
         * Get The Details of UPC under a particular Vendor Catalog Style.
         * @param request
         * @param response
         * @return
         * @throws Exception
         */
	public ModelAndView getUPCDetails(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'getUPCDetails' method...");
		}
		String vendorStyleId = request.getParameter(DropShipConstants.VENDOR_STYLE_ID);
		String vendorCatalogId = request.getParameter(DropShipConstants.VENDOR_CATALOG_ID);
		String vendorUpc = request.getParameter(DropShipConstants.VENDOR_UPC);
                 if(!vendorUpc.equals(DropShipConstants.ZERO)) {
                   vendorUpc = StringUtils.leftPad(vendorUpc, 13, DropShipConstants.ZERO).trim() ;
                }
                VendorUpcDTO upcDTO= new VendorUpcDTO();
                upcDTO.setVendorUPC(vendorUpc);
                
		VendorStylePropertiesDTO stylePropertiesDTO = (VendorStylePropertiesDTO) request
				.getSession().getAttribute(DropShipConstants.STYLE_PROPERTIES);
		if(stylePropertiesDTO == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		request.setAttribute(DropShipConstants.MAPPED_ATTRIBUTE_LIST, stylePropertiesDTO
				.getMappedFields());
		request.setAttribute(DropShipConstants.UNMAPPED_ATTRIBUTE_LIST, stylePropertiesDTO
				.getUnmappedList());
		request.setAttribute(DropShipConstants.STYLE_IMAGE_LIST, stylePropertiesDTO
				.getStyleImages());
		request.setAttribute(DropShipConstants.VENDOR_UPC_LIST, stylePropertiesDTO.getUpcList());
                int ind = stylePropertiesDTO.getUpcList().indexOf(upcDTO);
		request.setAttribute(DropShipConstants.UPC_DETAILS, stylePropertiesDTO.getUpcList().get(ind));
		request.setAttribute(DropShipConstants.SKU_IMAGE_LIST, catalogManager.getStyleSKUImages(
				vendorCatalogId, vendorStyleId, vendorUpc));
		VendorStyleInfo styleInfo = stylePropertiesDTO.getStyleInfo();
		CatalogVendorDTO vendorInfo = (CatalogVendorDTO) request.getSession().getAttribute(
				DropShipConstants.VENDOR_INFO);
		if(vendorInfo == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
                String vendorNumber = ""+vendorInfo.getVendorNumber();
                vendorNumber = vendorNumber.trim();
		styleInfo.setVendorNumber(vendorNumber);
		styleInfo.setVendorName(vendorInfo.getName());
		request.setAttribute(DropShipConstants.VENDOR_STYLE_INFO, styleInfo);
                 if (log.isDebugEnabled()) {
			log.debug("end workflow 'getUPCDetails' method...");
		}
		return new ModelAndView(DropShipConstants.VENDOR_STYLE_PROPERTIES);

	}
        /**
         * Edit Vendor Catalog style proeprties.
         * @param request
         * @param response
         * @return
         * @throws Exception
         */
	public ModelAndView editStylesProperties(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'editStylesProperties' method...");
		}
		String vendorId = (String) request.getSession().getAttribute(
				DropShipConstants.VENDOR_CATALOG_VENDOR_ID);
		if(vendorId.equals(null) || vendorId.equals("")){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		String vendorStyleId = request.getParameter(DropShipConstants.VENDOR_STYLE_ID);
		//VendorStyle style=(VendorStyle) catalogManager.getById(VendorStyle.class, new Long(vendorStyleId));
		String vendorCatalogId = request.getParameter(DropShipConstants.VENDOR_CATALOG_ID);
		String recordNum = request.getParameter(DropShipConstants.RECORD_NUMBER);
		String catalogTemplateId = request.getParameter(DropShipConstants.CATALOG_TEMPLATE_ID);
		String vendorUpc = request.getParameter(DropShipConstants.VENDOR_UPC);

		EditVendorStylePropertiesForm editVendorStylePropertiesForm = new EditVendorStylePropertiesForm();
		editVendorStylePropertiesForm.setVendorCatalogId(vendorCatalogId);
		editVendorStylePropertiesForm.setVendorStyleId(vendorStyleId);
		editVendorStylePropertiesForm.setCatalogTemplateId(catalogTemplateId);
		editVendorStylePropertiesForm.setRecordNum(recordNum);
		editVendorStylePropertiesForm.setStyleVendorUpc(vendorUpc);
		VendorStylePropertiesDTO stylePropertiesDTO = catalogManager.getVendorStyleProperties(
				vendorStyleId, vendorCatalogId, recordNum, catalogTemplateId, vendorId, vendorUpc);
		request.setAttribute(DropShipConstants.MAPPED_ATTRIBUTE_LIST, stylePropertiesDTO
				.getMappedFields());
		request.setAttribute(DropShipConstants.UNMAPPED_ATTRIBUTE_LIST, stylePropertiesDTO
				.getUnmappedList());
		request.setAttribute(DropShipConstants.STYLE_IMAGE_LIST, stylePropertiesDTO
				.getStyleImages());
		request.setAttribute(DropShipConstants.VENDOR_UPC_LIST, stylePropertiesDTO.getUpcList());
                request.getSession().removeAttribute(DropShipConstants.UPC_DETAILS);
                //request.setAttribute(STYLE_NUMBER, style.getVendorStyleNumber());
		editVendorStylePropertiesForm.setMappedFieldSeq(stylePropertiesDTO.getMappedFieldSeq());
		editVendorStylePropertiesForm.setUnMappedFieldSeq(stylePropertiesDTO.getUnMappedFieldSeq());
                editVendorStylePropertiesForm.setUpdateColor(stylePropertiesDTO.getUpdateColor());
                editVendorStylePropertiesForm.setUpdateDescription(stylePropertiesDTO.getUpdateDescription());
                editVendorStylePropertiesForm.setColorFieldNo(stylePropertiesDTO.getColorFieldNo());
                editVendorStylePropertiesForm.setDescriptionFieldNo(stylePropertiesDTO.getDescriptionFieldNo());
		stylePropertiesDTO.setCatalogTempalteId(catalogTemplateId);
		request.setAttribute(DropShipConstants.EDIT_VENDOR_STYLE_PROPERTIES_FORM,
				editVendorStylePropertiesForm);
		stylePropertiesDTO.setVendorCatalogId(vendorCatalogId);
		stylePropertiesDTO.setRecordNum(recordNum);
		stylePropertiesDTO.setVendorUpc(vendorUpc);
                stylePropertiesDTO.setVendorCatalogId(vendorCatalogId);
		VendorStyleInfo styleInfo = stylePropertiesDTO.getStyleInfo();
		CatalogVendorDTO vendorInfo = (CatalogVendorDTO) request.getSession().getAttribute(
				DropShipConstants.VENDOR_INFO);
		if(vendorInfo == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
                String vendorNumber = ""+vendorInfo.getVendorNumber();
                vendorNumber = vendorNumber.trim();
		styleInfo.setVendorNumber(vendorNumber);
		styleInfo.setVendorName(vendorInfo.getName());
		request.setAttribute(DropShipConstants.VENDOR_STYLE_INFO, styleInfo);
		request.getSession().setAttribute(DropShipConstants.STYLE_PROPERTIES, stylePropertiesDTO);
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'editStylesProperties' method...");
		}
		return new ModelAndView(DropShipConstants.EDIT_VENDOR_STYLE_PROPERTIES);

	}
        /**
         * Edit Vendor UPC details(UPC/Color)
         * @param request
         * @param response
         * @param editVendorStylePropertiesForm
         * @return
         * @throws Exception
         */
	public ModelAndView editUPCDetails(
			HttpServletRequest request, HttpServletResponse response,
			EditVendorStylePropertiesForm editVendorStylePropertiesForm)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'editUPCDetails' method...");
		}
		String vendorStyleId = request.getParameter(DropShipConstants.VENDOR_STYLE_ID);
		String vendorCatalogId = request.getParameter(DropShipConstants.VENDOR_CATALOG_ID);
		String vendorUpc = request.getParameter(DropShipConstants.VENDOR_UPC);
		String recordNum = request.getParameter(DropShipConstants.RECORD_NUMBER);
		String catalogTemplateId = request.getParameter(DropShipConstants.CATALOG_TEMPLATE_ID);
		VendorStylePropertiesDTO stylePropertiesDTO = (VendorStylePropertiesDTO) request
				.getSession().getAttribute(DropShipConstants.STYLE_PROPERTIES);
		if(stylePropertiesDTO == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		request.setAttribute(DropShipConstants.MAPPED_ATTRIBUTE_LIST, stylePropertiesDTO
				.getMappedFields());
		request.setAttribute(DropShipConstants.UNMAPPED_ATTRIBUTE_LIST, stylePropertiesDTO
				.getUnmappedList());
		request.setAttribute(DropShipConstants.STYLE_IMAGE_LIST, stylePropertiesDTO
				.getStyleImages());
		request.setAttribute(DropShipConstants.VENDOR_UPC_LIST, stylePropertiesDTO.getUpcList());
                if(!vendorUpc.equals(DropShipConstants.ZERO)) {
                   vendorUpc = StringUtils.leftPad(vendorUpc, 13, DropShipConstants.ZERO).trim() ;
                }
                VendorUpcDTO upcDTO= new VendorUpcDTO();
                upcDTO.setVendorUPC(vendorUpc);
                int ind = stylePropertiesDTO.getUpcList().indexOf(upcDTO);
		VendorUpcDTO upcDTOfromList = stylePropertiesDTO.getUpcList().get(ind);
		List<NameValue> tempList = catalogManager.getUPCHeaderNumber(catalogTemplateId);
                upcDTO= new VendorUpcDTO(upcDTOfromList);
		if (tempList.size() > 1) {
			upcDTO.setVendorUpcHeaderNbr(tempList.get(0).getHeaderNumber());
			upcDTO.setColorHeaderNbr(tempList.get(1).getHeaderNumber());
		}
		upcDTO.setVendorUPC(StringUtils.leftPad(vendorUpc, 13, DropShipConstants.ZERO));
		editVendorStylePropertiesForm.setVendorUpc(StringUtils.leftPad(vendorUpc, 13, DropShipConstants.ZERO));
		editVendorStylePropertiesForm.setOldVendorUpc(StringUtils.leftPad(vendorUpc, 13, DropShipConstants.ZERO));
                if(log.isDebugEnabled()) {
                    log.debug("Vendor Upc-" + editVendorStylePropertiesForm.getVendorUpc());
                    log.debug("Upc Color-" + editVendorStylePropertiesForm.getColor());
                }
		editVendorStylePropertiesForm.setColor(upcDTO.getColor());
		editVendorStylePropertiesForm.setOldColor(upcDTO.getColor());
		editVendorStylePropertiesForm.setUpcCatalogId(vendorCatalogId);
		editVendorStylePropertiesForm.setUpcRecordNum(recordNum);
		editVendorStylePropertiesForm.setVendorUpcHeadderNbr(upcDTO.getVendorUpcHeaderNbr());
		editVendorStylePropertiesForm.setColorHeaderNbr(upcDTO.getColorHeaderNbr());
		editVendorStylePropertiesForm.setUpcCatalogTemplateId(catalogTemplateId);
		editVendorStylePropertiesForm.setUpcCatalogId(upcDTO.getVendorCatalogId());
                editVendorStylePropertiesForm.setVendorCatalogId(stylePropertiesDTO.getVendorCatalogId());
		editVendorStylePropertiesForm.setStyleVendorUpc(StringUtils.leftPad(stylePropertiesDTO.getVendorUpc(),13,DropShipConstants.ZERO));
                editVendorStylePropertiesForm.setMappedFieldSeq(stylePropertiesDTO.getMappedFieldSeq());
		editVendorStylePropertiesForm.setUnMappedFieldSeq(stylePropertiesDTO.getUnMappedFieldSeq());
                editVendorStylePropertiesForm.setCatalogTemplateId(stylePropertiesDTO.getCatalogTempalteId());
                editVendorStylePropertiesForm.setVendorStyleId(stylePropertiesDTO.getVendorStyleId());
                editVendorStylePropertiesForm.setUpdateColor(stylePropertiesDTO.getUpdateColor());
                editVendorStylePropertiesForm.setUpdateDescription(stylePropertiesDTO.getUpdateDescription());
                editVendorStylePropertiesForm.setColorFieldNo(stylePropertiesDTO.getColorFieldNo());
                editVendorStylePropertiesForm.setDescriptionFieldNo(stylePropertiesDTO.getDescriptionFieldNo());

		request.setAttribute(DropShipConstants.EDIT_VENDOR_STYLE_PROPERTIES_FORM,
				editVendorStylePropertiesForm);
		request.setAttribute(DropShipConstants.UPC_DETAILS, upcDTO);
		request.getSession().setAttribute(DropShipConstants.UPC_DETAILS, upcDTO);
		request
				.setAttribute(DropShipConstants.VENDOR_STYLE_INFO, stylePropertiesDTO
						.getStyleInfo());
		request.setAttribute(DropShipConstants.SKU_IMAGE_LIST, catalogManager.getStyleSKUImages(
				vendorCatalogId, vendorStyleId, vendorUpc));
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'editUPCDetails' method...");
		}
		return new ModelAndView(DropShipConstants.EDIT_VENDOR_STYLE_PROPERTIES);

	}
        /**
         * Save Vendor Style or UPC proeprties.
         * @param request
         * @param response
         * @param editVendorStylePropertiesForm
         * @return
         * @throws Exception
         */
	public ModelAndView saveStyleProperties(
			HttpServletRequest request, HttpServletResponse response,
			EditVendorStylePropertiesForm editVendorStylePropertiesForm)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'saveStyleProperties' method...");
		}
		User user = getLoggedInUser();
		String vendorId = (String) request.getSession().getAttribute(
				DropShipConstants.VENDOR_CATALOG_VENDOR_ID);
		if(vendorId.equals(null) || vendorId.equals("")){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		VendorStylePropertiesDTO stylePropertiesDTO = new VendorStylePropertiesDTO();
		stylePropertiesDTO.setMappedFieldSeq(editVendorStylePropertiesForm.getMappedFieldSeq());
		stylePropertiesDTO.setUnMappedFieldSeq(editVendorStylePropertiesForm.getUnMappedFieldSeq());
		stylePropertiesDTO.setMappedFieldsArr(editVendorStylePropertiesForm.getMappedFields());
		stylePropertiesDTO.setUnmappedFields(editVendorStylePropertiesForm.getUnmappedFields());
		stylePropertiesDTO.setVendorCatalogId(editVendorStylePropertiesForm.getVendorCatalogId());
		stylePropertiesDTO.setRecordNum(editVendorStylePropertiesForm.getRecordNum());
		stylePropertiesDTO.setVendorStyleId(editVendorStylePropertiesForm.getVendorStyleId());
                stylePropertiesDTO.setVendorUpc(editVendorStylePropertiesForm.getStyleVendorUpc());
                stylePropertiesDTO.setVendorId(vendorId);
                //Set the field to update Color & Description in Master Table.
                stylePropertiesDTO.setUpdateColor(editVendorStylePropertiesForm.getUpdateColor());
                stylePropertiesDTO.setUpdateDescription(editVendorStylePropertiesForm.getUpdateDescription());
                stylePropertiesDTO.setColorFieldNo(editVendorStylePropertiesForm.getColorFieldNo());
                stylePropertiesDTO.setDescriptionFieldNo(editVendorStylePropertiesForm.getDescriptionFieldNo());

		VendorUpcDTO upcDTO = new VendorUpcDTO();

		if (editVendorStylePropertiesForm.getVendorUpc() != null
				&& !StringUtils.isBlank(editVendorStylePropertiesForm.getVendorUpc())) {

			upcDTO.setRecordNum(editVendorStylePropertiesForm.getUpcRecordNum());
			upcDTO.setVendorUPC(editVendorStylePropertiesForm.getVendorUpc());
			upcDTO.setVendorUpcHeaderNbr(editVendorStylePropertiesForm.getVendorUpcHeadderNbr());
			upcDTO.setColor(editVendorStylePropertiesForm.getColor());
			upcDTO.setColorHeaderNbr(editVendorStylePropertiesForm.getColorHeaderNbr());
			upcDTO.setOldColor(editVendorStylePropertiesForm.getOldColor());
			upcDTO.setOldVendorUpc(editVendorStylePropertiesForm.getOldVendorUpc());
			upcDTO.setVendorStyleId(editVendorStylePropertiesForm.getVendorStyleId());
			upcDTO.setVendorCatalogId(editVendorStylePropertiesForm.getUpcCatalogId());
                        upcDTO.setCatalogTemplateId(editVendorStylePropertiesForm.getUpcCatalogTemplateId());
			upcDTO.setVendorId(vendorId);// Need to take this vale from session
			stylePropertiesDTO.setUpcDetails(upcDTO);
		}
		boolean flag = catalogManager.saveVendorStyleProperties(stylePropertiesDTO, user);
		VendorStylePropertiesDTO stylePropertiesDTO1 = null;
		String vendorCatalogId = null;
		String recordNum = null;
		String catalogTemplateId = null;
		String vendorStyleId = null;
		String vendorUpc = null;
		if (flag) {
			if (editVendorStylePropertiesForm.getVendorUpc() != null
					&& !StringUtils.isBlank(editVendorStylePropertiesForm.getVendorUpc())) {
				vendorCatalogId = upcDTO.getVendorCatalogId();
				recordNum = upcDTO.getRecordNum();
				catalogTemplateId = upcDTO.getCatalogTemplateId();
				vendorStyleId = upcDTO.getVendorStyleId();
				vendorUpc = upcDTO.getVendorUPC();
				//stylePropertiesDTO1.setCatalogTempalteId(catalogTemplateId);
				stylePropertiesDTO1 = catalogManager.getVendorStyleProperties(vendorStyleId,
						vendorCatalogId, recordNum, catalogTemplateId, vendorId, vendorUpc);
                                stylePropertiesDTO1.setCatalogTempalteId(catalogTemplateId);
                                stylePropertiesDTO1.setVendorUpc(vendorUpc);
				request.getSession().setAttribute(DropShipConstants.STYLE_PROPERTIES,
						stylePropertiesDTO1);

			}
		}
		else {
			stylePropertiesDTO1 = (VendorStylePropertiesDTO) request.getSession().getAttribute(
					DropShipConstants.STYLE_PROPERTIES);
			if(stylePropertiesDTO1 == null){
				return new ModelAndView("redirect:/mainMenu.html");
			}
			vendorCatalogId = stylePropertiesDTO1.getVendorCatalogId();
			recordNum = stylePropertiesDTO1.getRecordNum();
			catalogTemplateId = stylePropertiesDTO1.getCatalogTempalteId();
			vendorStyleId = stylePropertiesDTO1.getVendorStyleId();
			vendorUpc = stylePropertiesDTO1.getVendorUpc();
			stylePropertiesDTO1 = catalogManager.getVendorStyleProperties(vendorStyleId,
					vendorCatalogId, recordNum, catalogTemplateId, vendorId, vendorUpc);
		}
		editVendorStylePropertiesForm.setVendorCatalogId(vendorCatalogId);
		editVendorStylePropertiesForm.setVendorStyleId(vendorStyleId);
		editVendorStylePropertiesForm.setCatalogTemplateId(catalogTemplateId);
		editVendorStylePropertiesForm.setRecordNum(recordNum);
		editVendorStylePropertiesForm.setStyleVendorUpc(vendorUpc);
		request.setAttribute(DropShipConstants.MAPPED_ATTRIBUTE_LIST, stylePropertiesDTO1
				.getMappedFields());
		request.setAttribute(DropShipConstants.UNMAPPED_ATTRIBUTE_LIST, stylePropertiesDTO1
				.getUnmappedList());
		request.setAttribute(DropShipConstants.STYLE_IMAGE_LIST, stylePropertiesDTO1
				.getStyleImages());
		request.setAttribute(DropShipConstants.VENDOR_UPC_LIST, stylePropertiesDTO1.getUpcList());

		editVendorStylePropertiesForm.setMappedFieldSeq(stylePropertiesDTO1.getMappedFieldSeq());
		editVendorStylePropertiesForm
				.setUnMappedFieldSeq(stylePropertiesDTO1.getUnMappedFieldSeq());

		request.setAttribute(DropShipConstants.EDIT_VENDOR_STYLE_PROPERTIES_FORM,
				editVendorStylePropertiesForm);
		VendorStyleInfo styleInfo = stylePropertiesDTO1.getStyleInfo();
		CatalogVendorDTO vendorInfo = (CatalogVendorDTO) request.getSession().getAttribute(
				DropShipConstants.VENDOR_INFO);
		if(vendorInfo == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
                String vendorNumber = ""+vendorInfo.getVendorNumber();
                vendorNumber = vendorNumber.trim();
		styleInfo.setVendorNumber(vendorNumber);
		styleInfo.setVendorName(vendorInfo.getName());
		request.setAttribute(DropShipConstants.VENDOR_STYLE_INFO, styleInfo);



                if (log.isDebugEnabled()) {
			log.debug("end workflow 'saveStyleProperties' method...");
		}
		return new ModelAndView(DropShipConstants.EDIT_VENDOR_STYLE_PROPERTIES);

	}

	// Remove Images
	public ModelAndView removeStyleSkuImages(
			HttpServletRequest request, HttpServletResponse response,
			EditVendorStylePropertiesForm editVendorStylePropertiesForm)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'removeStyleSkuImages' method...");
		}
		User user = getLoggedInUser();
		String imageId = request.getParameter("imageId");
		catalogManager.removeImage(imageId, user);
		String vendorCatalogId = null;
		String recordNum = null;
		String catalogTemplateId = null;
		String vendorStyleId = null;
		String vendorUpc = null;
		String vendorId = (String) request.getSession().getAttribute(
				DropShipConstants.VENDOR_CATALOG_VENDOR_ID);
		VendorStylePropertiesDTO stylePropertiesDTO1 = (VendorStylePropertiesDTO) request
				.getSession().getAttribute(DropShipConstants.STYLE_PROPERTIES);
		if(vendorId.equals("") || vendorId.equals(null) || stylePropertiesDTO1 == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		vendorCatalogId = stylePropertiesDTO1.getVendorCatalogId();
		recordNum = stylePropertiesDTO1.getRecordNum();
		catalogTemplateId = stylePropertiesDTO1.getCatalogTempalteId();
		vendorStyleId = stylePropertiesDTO1.getVendorStyleId();
		vendorUpc = stylePropertiesDTO1.getVendorUpc();
		stylePropertiesDTO1 = catalogManager.getVendorStyleProperties(vendorStyleId,
				vendorCatalogId, recordNum, catalogTemplateId, vendorId, vendorUpc);
		editVendorStylePropertiesForm.setVendorCatalogId(vendorCatalogId);
		editVendorStylePropertiesForm.setVendorStyleId(vendorStyleId);
		editVendorStylePropertiesForm.setCatalogTemplateId(catalogTemplateId);
		editVendorStylePropertiesForm.setRecordNum(recordNum);
		editVendorStylePropertiesForm.setStyleVendorUpc(vendorUpc);
		request.setAttribute(DropShipConstants.MAPPED_ATTRIBUTE_LIST, stylePropertiesDTO1
				.getMappedFields());
		request.setAttribute(DropShipConstants.UNMAPPED_ATTRIBUTE_LIST, stylePropertiesDTO1
				.getUnmappedList());
		request.setAttribute(DropShipConstants.STYLE_IMAGE_LIST, stylePropertiesDTO1
				.getStyleImages());
		request.setAttribute(DropShipConstants.VENDOR_UPC_LIST, stylePropertiesDTO1.getUpcList());

		editVendorStylePropertiesForm.setMappedFieldSeq(stylePropertiesDTO1.getMappedFieldSeq());
		editVendorStylePropertiesForm
				.setUnMappedFieldSeq(stylePropertiesDTO1.getUnMappedFieldSeq());
		VendorStyleInfo styleInfo = stylePropertiesDTO1.getStyleInfo();
		CatalogVendorDTO vendorInfo = (CatalogVendorDTO) request.getSession().getAttribute(
				DropShipConstants.VENDOR_INFO);
		if(vendorInfo == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
                String vendorNumber = ""+vendorInfo.getVendorNumber();
                vendorNumber = vendorNumber.trim();
		styleInfo.setVendorNumber(vendorNumber);
		styleInfo.setVendorName(vendorInfo.getName());
		request.setAttribute(DropShipConstants.VENDOR_STYLE_INFO, styleInfo);
		request.setAttribute(DropShipConstants.EDIT_VENDOR_STYLE_PROPERTIES_FORM,
				editVendorStylePropertiesForm);
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'removeStyleSkuImages' method...");
		}
		return new ModelAndView(DropShipConstants.EDIT_VENDOR_STYLE_PROPERTIES);
	}

	/**
	 * Initilal Load of the Vendor Properties Page
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView initVendorProperties(
			HttpServletRequest request, HttpServletResponse response) {
		if (log.isDebugEnabled()) {
			log.debug("entering workflow 'initVendorProperties' method...");
		}
                CatalogVendorDTO catalogDTO = (CatalogVendorDTO) request.getSession().getAttribute(
				DropShipConstants.VENDOR_INFO);
                if(catalogDTO == null){

                	return new ModelAndView("redirect:/mainMenu.html");
                }
		VendorPropertiesForm vendorPropertiesForm = new VendorPropertiesForm();
		vendorPropertiesForm.setVendorId(catalogDTO.getVendorId());
		vendorPropertiesForm.setIsDisplayable(catalogDTO.getIsDisplayable());
		vendorPropertiesForm.setTemp(catalogDTO.getIsDisplayable());
                User user = getLoggedInUser();
               // user.
                Set userRoles = user.getRoles();
                Role admin = new Role();
                admin.setName(DropShipConstants.ROLE_ADMIN);
                Role buyer = new Role();
                admin.setName(DropShipConstants.ROLE_BUYER);
                if(userRoles.contains(admin)|| userRoles.contains(buyer)) {
                    vendorPropertiesForm.setMode(DropShipConstants.EDIT);
                } else {
                    vendorPropertiesForm.setMode(DropShipConstants.READ_ONLY_MODE);
                }
                //userRoles.
		request.setAttribute(DropShipConstants.VENDOR_PROPERTIES_FORM, vendorPropertiesForm);
                if (log.isDebugEnabled()) {
			log.debug("end workflow 'initVendorProperties' method...");
		}
		return new ModelAndView(DropShipConstants.VENDOR_PROPERTIES);
	}
        /**
         * Save Vendor Properties.
         * @param request
         * @param response
         * @param vendorPropertiesForm
         * @return ModelAndView
         */
	public ModelAndView saveVendorProperties(
			HttpServletRequest request, HttpServletResponse response,
			VendorPropertiesForm vendorPropertiesForm) {
              if (log.isDebugEnabled()) {
			log.debug("entering workflow 'saveVendorProperties' method...");
		}
		CatalogVendorDTO catalogDTO = (CatalogVendorDTO) request.getSession().getAttribute(
				DropShipConstants.VENDOR_INFO);
		if(catalogDTO == null){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		catalogManager.saveVendorProperties(vendorPropertiesForm.getVendorId(),
				vendorPropertiesForm.getIsDisplayable());
		vendorPropertiesForm.setTemp(vendorPropertiesForm.getIsDisplayable());
		catalogDTO.setIsDisplayable(vendorPropertiesForm.getIsDisplayable());
		request.setAttribute(DropShipConstants.VENDOR_PROPERTIES_FORM, vendorPropertiesForm);
                  if (log.isDebugEnabled()) {
			log.debug("end workflow 'saveVendorProperties' method...");
		}
		return new ModelAndView(DropShipConstants.VENDOR_PROPERTIES);
	}

	@Override
	protected void bind(HttpServletRequest request, Object command)
			throws Exception {
		ServletRequestDataBinder binder = createBinder(request, command);
		binder.bind(request);
		errors = binder.getBindingResult();

	}
        /**
         * Validate all the Form Object Data.
         * @param command
         */
	public void validate(Object command) {
		Validator validators[] = getValidators();

		if (validators != null) {
			for (int index = 0; index < validators.length; index++) {
				Validator validator = validators[index];
				if (validator.supports(command.getClass())) {
					ValidationUtils.invokeValidator(validators[index], command, errors);
				}
			}
		}
	}
        /**
         * Save Error In Request Object.
         * @param request
         * @param errors
         */
	public void saveErrors(HttpServletRequest request, Errors errors) {
		List allErrors = (List) request.getAttribute(DropShipConstants.ERRORS);

		if (allErrors == null) {
			allErrors = new ArrayList();
		}
		List errList = errors.getAllErrors();

		Iterator itr = errList.iterator();
		int cnt = 0;
		while (cnt < errList.size()) {
			ObjectError err = (ObjectError) errList.get(cnt);
			allErrors.add(err.getCode());
			cnt++;
		}

		request.setAttribute(DropShipConstants.ERRORS, allErrors);
	}
	
	public ModelAndView removeCatalog(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering removeCatalog method");
		}
		String vendorCatalogId = (String) request.getParameter("vcID");
		if(vendorCatalogId.equals(null) || vendorCatalogId.equals("")){
			return new ModelAndView("redirect:/mainMenu.html");
		}
		
		catalogManager.removeVendorCatalog(vendorCatalogId);
		String vendorId = request.getParameter(DropShipConstants.VENDOR_Id);
		String numSkus=request.getParameter(DropShipConstants.NUM_OF_SKUS);
		String numStyles=request.getParameter(DropShipConstants.NUM_OF_STYLES);
		if (log.isDebugEnabled()) {
			log.debug("vendorId:"+vendorId+" numSkus:"+numSkus+" numStyles:"+numStyles);
		}
		return new ModelAndView("redirect:../vendorCatalog/catalogVendors.html?vendorId="+vendorId+"&numStyles="+numStyles+"&numSKUs="+numSkus+"&method=viewAllVendorCatalogs");
	}

}
