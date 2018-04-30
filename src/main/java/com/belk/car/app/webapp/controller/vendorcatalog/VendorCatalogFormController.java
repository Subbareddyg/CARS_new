/**
 *
 */

package com.belk.car.app.webapp.controller.vendorcatalog;
 
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.LabelValue;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dto.CatalogVendorDTO;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogImport;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.VendorCatalogForm;

/**
 * Purpose: Form controller for add/edit vendor catalog setup form Initial
 * Project: EC-2009-00092 - FY 10 eCommerce Drop-Ship Phase I Initial
 * Requirements: BR.002 Description: This class is used for adding/updating
 * (new) vendor catalog for particular Vendor. Also it validates vendor catalog
 * fields.
 *
 * @author afusya2
 */
public class VendorCatalogFormController extends BaseFormController implements
		DropShipConstants {

	private static final String TRUE = "true";

	private transient final Log log = LogFactory.getLog(VendorCatalogFormController.class);

	private CarManager carManager;
	
	private VendorCatalogManager vendorCatalogManager;

	/**
	 * @return the carManager
	 */
	public CarManager getCarManager() {
		return carManager;
	}

	/**
	 * @param carManager
	 *            the carManager to set
	 */
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	/**
	 * @return the vendorCatalogManager
	 */
	public VendorCatalogManager getVendorCatalogManager() {
		return vendorCatalogManager;
	}

	/**
	 * @param vendorCatalogManager
	 *            the vendorCatalogManager to set
	 */
	public void setVendorCatalogManager(
			VendorCatalogManager vendorCatalogManager) {
		this.vendorCatalogManager = vendorCatalogManager;
	}

	public VendorCatalogFormController() {
		setCommandName(VENDOR_CATALOG_FORM);
		setCommandClass(VendorCatalogForm.class);
	}

	/**
	 * This method is called when user submits the form The user entered
	 * information is saved/updated in DB and also stored in seesion to be used
	 * in other scenarios(requirements)
	 *
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log
					.debug("entering  VendorCatalogFormController 'onSubmit' method...");
		}

		VendorCatalogForm vendorCatalogForm = (VendorCatalogForm) command;
		String vendorNumber = vendorCatalogForm.getVendorNumber();
		VendorCatalog previousVendorCatalog = new VendorCatalog();
		try {
                        String spreadsheetName="";
                        HttpSession session = request.getSession();
			// checking the file and its name
			if (null != vendorCatalogForm.getFileName()
					&& vendorCatalogForm.getFileName().length != 0) {
				MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
						.getFile(FILE_NAME);
				if (log.isDebugEnabled()) {
					log.debug("catalog file name entered..."+ file.getOriginalFilename());			
				}
				vendorCatalogForm.setFilePath(file.getOriginalFilename());
                                spreadsheetName = vendorCatalogForm.getFilePath().trim();
				// getting extension of the file
				String ext = vendorCatalogForm.getFilePath();
				int ext1 = StringUtils.lastIndexOf(ext, DOT);
				ext = ext.substring(ext1, ext.length());
				if (log.isDebugEnabled()) {
					log.debug("extension of file entered..." + ext);
				}
				// extensions for vendor catalog file
				List<String> extensionOfFile = new ArrayList<String>();
				extensionOfFile.add(XLS);
				extensionOfFile.add(CSV);
				extensionOfFile.add(XML);
				extensionOfFile.add(TXT);
				/**
				 * Checking extension of the file selected. According to
				 * extension validations handled
				 */
				boolean isFileValid = false;
				if (!extensionOfFile.contains(ext.toLowerCase(Locale.US))) {
					vendorCatalogForm.setFilePath(BLANK);
					errors.rejectValue(FILE_NAME, "invalid",
							"File format not supported.");
					return showForm(request, response, errors);
					// isFileValid = true;
				} else if (vendorCatalogForm.getFileFormat().equals(
						TEXT_FILES_TXT_CSV)) {
					if (!ext.equals(TXT) && !ext.equals(CSV)) {
						isFileValid = true;
					}
				} else if ((vendorCatalogForm.getFileFormat()
						.equals(MICROSOFT_OFFICE_EXCEL_FILE))) {
					if (!ext.equals(XLS)) {
						isFileValid = true;
					}
				} else if ((vendorCatalogForm.getFileFormat()
						.equals(XML_FILES_XML))
						&& !ext.equals(XML)) {

					isFileValid = true;

				}
				if (isFileValid) {
					vendorCatalogForm.setFilePath(BLANK);
					errors.rejectValue(FILE_NAME, "invalid",
							"File name and file format do not match.");
					return showForm(request, response, errors);
				}
				try {
					// getting the ftp details which are set in
					// CarStartupListener
					String catalogId= session.getAttribute("vcID").toString();
					Properties properties = (Properties) getServletContext()
							.getAttribute(FTPDETAILS);
					if(StringUtils.isNotBlank(vendorNumber)){
						log.info("catalogId to be used for uploading file"+catalogId);
						getVendorCatalogManager().upLoadFile(properties, file,vendorNumber,catalogId);
					}else{
						getVendorCatalogManager().upLoadFile(properties, file, "0000","0000");
					}
				} catch (IOException io) {
					io.printStackTrace();
					vendorCatalogForm.setFilePath(BLANK);
					errors.rejectValue(FILE_NAME, "invalid",
							"Not able to upload File");
					return showForm(request, response, errors);
				}
			}
			try {
				VendorCatalog vendorCatalog = new VendorCatalog();

				// checking for unique catalog name for vendor in add mode
				if (null == session.getAttribute(EDIT_VENDOR_CATAOLOG)) {
					boolean isCatalogNameExist = false;
					isCatalogNameExist = vendorCatalogManager
							.getCatalogNameForVendor(vendorCatalogForm);
					if (log.isDebugEnabled()) {
						log.debug("isCatalogNameExist:" + isCatalogNameExist);
					}
					// if not unique catalog name then show validation
					if (isCatalogNameExist) {
						log.debug("value of isVendorCatalogNameExist:"
								+ isCatalogNameExist);
						errors.rejectValue("catalogName", "",
								"Spreadsheet Name must be unique for the vendor");
						return showForm(request, response, errors);
					}
                                        spreadsheetName=vendorCatalogForm.getFilePath().trim();
					List previousImageList = new ArrayList();
					/***********8new addition ---siddhi*******************************/
					if (StringUtils.isNotBlank(vendorCatalogForm
							.getPreviousCatalogID())
							&& Long.valueOf(vendorCatalogForm
									.getPreviousCatalogID()) != 0
							&& !vendorCatalogForm.getUploadImage()
									.equalsIgnoreCase("N")) {
						Long previousCatalogId = Long.valueOf(vendorCatalogForm
								.getPreviousCatalogID());
						if (log.isDebugEnabled()) {
							log.debug("previousCatalogId entered..."
								+ previousCatalogId);
						}
						// validate previous catalog id 
						previousVendorCatalog = vendorCatalogManager
								.getVendorCatalog(previousCatalogId);
						if (vendorCatalogForm.getImgLocn() != null
								&& vendorCatalogForm.getImgLocn().equalsIgnoreCase(
										"Upload from Previous Catalog")
								&& null == previousVendorCatalog) {
							Object[] args = new Object[] { getText(
									"Please enter valid catalog id", request
											.getLocale()) };
							errors.rejectValue("previousCatalogID", "", args,
									"Please enter valid catalog ID");
							return showForm(request, response, errors);
						}
						// get the image list of previous catalog id
						previousImageList = vendorCatalogManager
								.getVendorCatalogImageList(previousVendorCatalog
										.getVendorCatalogID());
						
					}
				
					/********8Added code ends*****************/
				}
				// if CD upload from previous catalog selected
				List previousImageList = new ArrayList();
				
				if (StringUtils.isNotBlank(vendorCatalogForm
						.getPreviousCatalogID())
						&& Long.valueOf(vendorCatalogForm
								.getPreviousCatalogID()) != 0
						&& !vendorCatalogForm.getUploadImage()
								.equalsIgnoreCase("N")) {
					Long previousCatalogId = Long.valueOf(vendorCatalogForm
							.getPreviousCatalogID());
					if (log.isDebugEnabled()) {
						log.debug("previousCatalogId entered..."
												+ previousCatalogId);
					}
					// validate previous catalog id
					previousVendorCatalog = vendorCatalogManager
							.getVendorCatalog(previousCatalogId);
					log.debug("vendor catalog ID to be moved"+session.getAttribute("vcID"));
					String vcIDToMove = session.getAttribute("vcID").toString();
					/*new method to move images from previous vendor catalog to new vendor catalog*/
					vendorCatalogManager.methodToMovePreviousCatalogImages(vcIDToMove,vendorNumber ,previousVendorCatalog);
					 /*End new method*/
					if (vendorCatalogForm.getImgLocn() != null
							&& vendorCatalogForm.getImgLocn().equalsIgnoreCase(
									"Upload from Previous Catalog")
							&& null == previousVendorCatalog) {
						Object[] args = new Object[] { getText(
								"Please enter valid catalog id", request
										.getLocale()) };
						errors.rejectValue("previousCatalogID", "", args,
								"Please enter valid catalog ID");
						return showForm(request, response, errors);
					}
					// get the image list of previous catalog id
					previousImageList = vendorCatalogManager
							.getVendorCatalogImageList(previousVendorCatalog
									.getVendorCatalogID());
				}
				/**
				 * If image location is Vendor FTP then copy images to
				 * destination ftp location
				 */
				if (log.isDebugEnabled()) {
					log.debug("Image location: "
									+ vendorCatalogForm.getImgLocn().equals(
								RETRIEVE_FROM_VENDOR_FTP_SITE));
				}
				if (null != vendorCatalogForm.getUploadImage()
						&& vendorCatalogForm.getUploadImage().trim()
								.equalsIgnoreCase(Y)
						&& vendorCatalogForm
								.getImgLocn()
								.trim()
								.equalsIgnoreCase(RETRIEVE_FROM_VENDOR_FTP_SITE)) {
					boolean isConnection = false;
					if (null != vendorCatalogForm.getImgLocn()
							&& vendorCatalogForm.getImgLocn().trim()
									.equalsIgnoreCase(
											RETRIEVE_FROM_VENDOR_FTP_SITE)) {

						// FTP information (address, user name )
						// be defaulted to information enter from the last
						// catalog that
						// was imported with FTP images.
						String ftpUrl = vendorCatalogForm.getFtpUrl();

						log.debug("ftp url entered..." + ftpUrl);

						session.setAttribute(FTP_URL, ftpUrl);

						// if isAnonylous not selected then set ftp user name
						if (!vendorCatalogForm.getIsAnonymousFTP()) {

							String ftpUserName = vendorCatalogForm
									.getFtpUname();
							session.setAttribute(FTP_USER_NAME, ftpUserName);

						} else {

							vendorCatalogForm.setFtpUname(BLANK);
							vendorCatalogForm.setFtpPwd(BLANK);
						}
						// images going to save in this directory
						String imageDir = (String) session
								.getAttribute(IMAGE_DIRECTORY);

						// getting the ftp details which are set in
						// CarStartupListener
						Properties properties = (Properties) getServletContext()
								.getAttribute(FTPDETAILS);
						isConnection = vendorCatalogManager
								.methodInvokingImportFTPImages(imageDir,
										properties, vendorCatalogForm);
					}
					// if FTP connection not established then show validation
					// message
					if (!isConnection) {
						errors.rejectValue("", "",
								"Ftp Connection is not established or invalid");
						return showForm(request, response, errors);
					}
				}
				boolean isNewCatalog = true;

				// department lists to be used inly in edit mode
				List<Department> removedDeptList = new ArrayList<Department>();
				List<Department> addedDeptList = new ArrayList<Department>();
				if (null != session.getAttribute(EDIT_VENDOR_CATAOLOG)) {
					log.debug("vendor catalog form in edit mode...");
					isNewCatalog = false;
					// set the dept list to form for edit mode
					List<Department> DeptListEdit = new ArrayList<Department>(
							(List) session.getAttribute(DEPARTMENTS));
					log.debug("department list size in edit mode:"
							+ DeptListEdit.size());
					vendorCatalogForm.setDepartments(DeptListEdit);

					// added for getting removed dept list in edit mode
					removedDeptList = (List) request.getSession(false)
							.getAttribute(REMOVED_DEPT_LIST);

					// added for getting added dept list in edit mode
					addedDeptList = (List) request.getSession(false)
							.getAttribute(ADDED_DEPT_LIST);
					if (log.isDebugEnabled()) {
						log.debug("department list to be removed before save:"
								+ removedDeptList);
						log.debug("department list to be added before save:"
								+ addedDeptList);

					}
				}
				if (log.isDebugEnabled()) {
					log.debug("session.getAttribute="
						+ session.getAttribute("vcNewID"));
				}
				vendorCatalogForm.setCatalogId((String) (session
						.getAttribute("vcNewID")));
				
				// save operation for New/Edit Catalog
				vendorCatalog = vendorCatalogManager.saveVendorCatalog(
						vendorCatalogForm, isNewCatalog, removedDeptList,
						addedDeptList);

				// save previous catalogs images for this catalog
				if (StringUtils.isNotBlank(vendorCatalogForm
						.getPreviousCatalogID())
						&& previousImageList != null
						&& !previousImageList.isEmpty()) {
					log.debug("upload image from previous catalog selected");
					vendorCatalogManager.saveImage(previousImageList,
							vendorCatalog.getVendorCatalogID());
					log.debug("vendor catalog ID to be moved"+session.getAttribute("vcID"));
					String vcIDToMove = session.getAttribute("vcID").toString();
					/*new method to move images from previous vendor catalog to new vendor catalog*/
					vendorCatalogManager.methodToMovePreviousCatalogImages(vcIDToMove,vendorNumber ,previousVendorCatalog);
				}

				// for vendor catalog info section in notes
				if (log.isDebugEnabled()) {
					log.debug("new/old vendor catalog save successful..."+ errors.hasErrors());
				}
				String saved = "";
				if(!errors.hasErrors()){
					log.debug("*****************");
					saved = "success";
					errors.rejectValue("", "VALID", "Saved Successfully!");
				}
				session.setAttribute("saved", saved);
				if (log.isDebugEnabled()) {
					log.debug("Addded the saved successfully message..");
				}
				// set vendor catalog in session to display it in notes catalog
				// info
				// section(add mode)
				session.setAttribute(VENDOR_CATALOG, vendorCatalog);
				session.setAttribute("mode", EDIT_VENDOR_CATAOLOG);
				String vcID = (String.valueOf(vendorCatalog
						.getVendorCatalogID()));
				session.setAttribute("vcID", vcID);
				 /*End new method*/
				return showForm(request, errors,
						"redirect:../vendorCatalog/vendorCatalogSetupForm.html?mode=edit&saved="+ saved+"&vcID="
								+ vcID);
			} catch (IOException e) {
				e.printStackTrace();
				errors.rejectValue("", "", "Invalid Ftp User name / password.");
				return showForm(request, response, errors);
			} catch (Exception ex) {
				ex.printStackTrace();
				if (log.isErrorEnabled()) {
					log.error("failed save/update vendor catalog..", ex);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ModelAndView(this.getSuccessView(), VENDOR_CATALOG_FORM,
				vendorCatalogForm);
	}

	/**
	 * This method is called after initial call to formBackingObject() when a
	 * user submits the form
	 *
	 * @see com.belk.car.app.webapp.controller.BaseFormController#processFormSubmission(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	public ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log
					.debug("In VendorCatalogFormController processFormSubmission....");
		}
		VendorCatalogForm vendorCatalogForm = (VendorCatalogForm) command;

		// check for cancel button action
		if (request.getParameter("cancel") != null) {
			if (log.isDebugEnabled()) {
				log.debug("cancel button clicked");
			}
			HttpSession session = request.getSession(false);
			CatalogVendorDTO catalogDTO = (CatalogVendorDTO) session
					.getAttribute("vendorCatalogInfo");
			if (null != catalogDTO) {
				if (null != session.getAttribute(EDIT_VENDOR_CATAOLOG)
						|| null != session.getAttribute(VIEW_ONLY)) {
					if (log.isDebugEnabled()) {
						log
								.debug("cancel button clicked and mode is edit/viewOnly");
						log.debug("Cancelling to vendor catalog list page ");
					}
					session.removeAttribute(VENDOR_CATALOG);
					session.removeAttribute("mode");
					session.removeAttribute(REMOVED_DEPT_LIST);
					session.removeAttribute(ADDED_DEPT_LIST);

					return new ModelAndView(getCancelRedirectUrl(catalogDTO));

				} else if ((String) request.getSession(false).getAttribute(
						VENDR_ID) != null) {
					// if user navigates 'New Catalog' from vendor catalog
					// screen
					return new ModelAndView(getCancelRedirectUrl(catalogDTO));
				}
			} else {
				return new ModelAndView(
						"redirect:/vendorCatalog/catalogVendors.html?method=viewAll");
			}
		}
		// for new catalog check verify button action
		else if (!StringUtils.isBlank(ServletRequestUtils.getStringParameter(
				request, VERIFY))
				&& ServletRequestUtils.getStringParameter(request, VERIFY)
						.equals(VERIFY_BUTTON)) {
			
			Vendor vendor = null;
			HttpSession session = request.getSession(false);
			String imageDirectory = null;
			String mode = (String) request.getAttribute(MODE);
			if (log.isDebugEnabled()) {
				log.debug("mode inside processFormSubmission..." + mode);
			}
			// get the vendor catalog id from sequence
			Long catalogSeqId = vendorCatalogManager.getCatalogIdFromSeq();

			String venNum = vendorCatalogForm.getVendorNumber();
			session.setAttribute("vcNewID", catalogSeqId.toString());
			FieldError error = errors.getFieldError("vendorNumber");
			if (null == error && null != venNum) {
				vendor = vendorCatalogManager.getVendorForCatalog(venNum);
				if (vendor != null) {
					// enable catalog screen after validating vendor#
					request.getSession().setAttribute(ENABLE, TRUE);
					request.getSession().setAttribute(VEN_NAME,
							vendor.getName());
					request.getSession().setAttribute(VENDR_ID,
							vendor.getVendorNumber());

					Properties properties = (Properties) getServletContext()
							.getAttribute(FTPDETAILS);
					// create directory to store images from CD
					imageDirectory = vendorCatalogManager
							.createNewImageDirectory(properties, venNum,
									catalogSeqId);

					if (log.isDebugEnabled()) {
						log.debug("images will be stored in this directory..."
								+ imageDirectory);
					}

					// set the image directory to access path in upload from CD
					// applet
					session.setAttribute(IMAGE_DIRECTORY, imageDirectory);

					return new ModelAndView(
							"redirect:/vendorCatalog/vendorCatalogSetupForm.html?name="
							        + URLEncoder.encode(vendor.getName(), "UTF-8")+ "&venNum=" + venNum
									+ "&venID=" + vendor.getVendorId()
									+ "&mode=verify");
				}
				// validations if invalid Vendor number entered
				else {
					request.getSession().setAttribute(ENABLE, FALSE);
					errors.rejectValue("vendorNumber", "invalid",
							"Vendor number does not exists. !");
					return showForm(request, response, errors);
				}
				// log.debug("value of 'enable' after verify button clicked..."+request.getSession().getAttribute(ENABLE));
			} else if (null != error) {
				return showForm(request, response, errors);
			}
			return new ModelAndView(
					"redirect:/vendorCatalog/vendorCatalogSetupForm.html?&mode=add");
		}
		return super.processFormSubmission(request, response,
				vendorCatalogForm, errors);
	}

	/**
	 * This Method saves the error message in to session
	 */
	public void saveError(HttpServletRequest request, String error) {
		List<String> errors = new ArrayList<String>();
		errors.add(error);
		request.setAttribute("errors", errors);
	}

	/**
	 * formBackingObject method is called first whenever this controller is
	 * called. Consists of logic to be implemented before the form is displayed.
	 * Contains condition for add,edit and respective actions Method is called
	 * again when the form is submitted
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("In VendorCatalogFormController formBackingObject....");
		}
		HttpSession session = request.getSession(false);
		VendorCatalogForm vendorCatalogForm = new VendorCatalogForm();
		
		
		String mode = request.getParameter(MODE);
		
		//Get the Catalog ids having images uploaded for this particular vendor.
		// Check whether the user session is timed out.
		if (null == mode && null == session.getAttribute(MODE)) {
			return new ModelAndView("redirect:/mainMenu.html");
		}
		
		/*************new changes -******************/
		String vndrCatlId = "";
		List<VendorCatalogImport> previousVendorCatalogs = new ArrayList<VendorCatalogImport>();
		if(request.getParameter("vcID") !=null){
			vndrCatlId = request.getParameter("vcID");
		}
		
			VendorCatalog vendorCatalog = new VendorCatalog();
			if(!(vndrCatlId.equals("")) && !(vndrCatlId.equals(null)) && !(null == vndrCatlId)){
				log.info("vndrCatlId is not null---------------------"+vndrCatlId);
				
			}
			if((session.getAttribute(VENDOR_CATALOG) !=null)) {
				if (log.isDebugEnabled()) {
					log.debug("get vendor ID from session");
				}
				vendorCatalog =(VendorCatalog) session.getAttribute(VENDOR_CATALOG);
				previousVendorCatalogs = vendorCatalogManager.getPreviousCatalogsList(vendorCatalog.getVendor().getVendorId());
				vendorCatalogForm.setPreviousVendorCatalogs(previousVendorCatalogs);
				log.info("previousVendorCatalogs list...."+previousVendorCatalogs);
			}
			if(ServletRequestUtils.getStringParameter(request,"venID") !=null){
				if (log.isDebugEnabled()) {
					log.debug("get vendID from request");
				}
				String vendorID = ServletRequestUtils.getStringParameter(request,"venID");
				previousVendorCatalogs = vendorCatalogManager.getPreviousCatalogsList(new Long(vendorID));
				vendorCatalogForm.setPreviousVendorCatalogs(previousVendorCatalogs);
				log.info("previousVendorCatalogs list...."+previousVendorCatalogs);
			}else{
				log.debug("new vendor to be added");
			}
			
			
			
	//	}
		
		/*****new changes over**********************/
		if (request.getParameter("fileName") != null) {
			String errString = "File " + request.getParameter("fileName")
					+ " does not exist on the server.";
			saveError(request, errString);
		}   
		if(session.getAttribute("saved") != null ){
			String saved = (String) session.getAttribute("saved");
			if(saved.equalsIgnoreCase("success")){
				String saveString = "Saved Successfully!";
				saveError(request, saveString);
			}
			session.removeAttribute("saved");
			
		}
		if(request.getParameter("saved") != null && request.getParameter("saved").equalsIgnoreCase("true")){
			
		}
		session.setAttribute("vcID", vndrCatlId);
		if (vndrCatlId != null && !vndrCatlId.equals("")) {
			session.setAttribute("vcNewID", vndrCatlId);
			log.debug("vndrCatlId=" + vndrCatlId);
		} else {
			log.debug("vndrCatlIdno=" + vndrCatlId);
		}
               User currentUser = getLoggedInUser();
                LabelValue admin = new LabelValue();
                for(LabelValue role:currentUser.getRoleList()){
                	 if( role.getValue().equals(Role.ROLE_ADMIN) || role.getValue().equals(Role.ROLE_BUYER)){

                		 session.setAttribute("isDataMappingAccess", DropShipConstants.TRUE);
                		 break;
                	 } else {
                         session.setAttribute("isDataMappingAccess", DropShipConstants.FALSE);
                	 }
                 }
		// setting mode to view/add vendor catalog
		if (mode != null
				&& (mode.equalsIgnoreCase(EDIT_VENDOR_CATAOLOG)
						|| mode.equalsIgnoreCase(VIEW_ONLY)
						|| mode.equalsIgnoreCase(ADD_VENDOR_CATALOG) || mode
						.equalsIgnoreCase(VERIFY))) {
			session.setAttribute(MODE, mode);
		} else if (null != session.getAttribute(MODE)) {
			if (null != mode && mode.equalsIgnoreCase(VIEW_ONLY)) {
				session.setAttribute(MODE, VIEW_ONLY);
			} else if (null != mode
					&& mode.equalsIgnoreCase(ADD_VENDOR_CATALOG)) {
				session.setAttribute(MODE, ADD_VENDOR_CATALOG);
			}
			mode = session.getAttribute(MODE).toString();
		}
		if (mode != null && mode.equalsIgnoreCase(ADD_VENDOR_CATALOG)) {
			session.removeAttribute(DropShipConstants.DEPARTMENTS);
			session.removeAttribute(DropShipConstants.NEW_DEPT_LIST);
			session.setAttribute(DropShipConstants.ENABLE,
					DropShipConstants.FALSE);
		}
		if (log.isDebugEnabled()) {
			log.debug("mode in form backing of VendorCatalogFormController:"
					+ mode);
		}
		try {
			// displaying vendor catalog form in viewOnly/edit mode
			if ((mode.equals(VIEW_ONLY) || mode.equals(EDIT_VENDOR_CATAOLOG))
					&& ((String) request.getSession(false).getAttribute("vcID") != null) 
					&& !( request.getSession(false).getAttribute("vcID").equals(""))) {
				if (log.isDebugEnabled()) {
					log.debug("vendorCatalogForm in viewonly/edit mode");
				}
				vndrCatlId = (String) request.getSession(false).getAttribute("vcID");
				log.info("vndrCatlId in edit mode"+vndrCatlId);
				vendorCatalogForm = populateVendorCatalog(vndrCatlId,
						vendorCatalogForm, session, request, mode);
				
				String imageDirectory = null;
				Properties properties = (Properties) getServletContext().getAttribute(FTPDETAILS);
				imageDirectory=properties.getProperty(FTPPATH)+vendorCatalogForm.getVendorNumber() + UNDERSCORE + vndrCatlId+SLASH +UNMAPPED;
				if (log.isDebugEnabled()) {
					log.debug("Image directory location for the exisitng catalog:"+imageDirectory);
				}
				session.setAttribute(IMAGE_DIRECTORY, imageDirectory);
				
				// return vendorCatalogForm;
			} else if (null != session.getAttribute("vcNewID")) {
				// for add department in "Add" mode
				String vcNewID = session.getAttribute("vcNewID").toString();
				session.setAttribute("vcID", vcNewID);
			}
			// set the dropdown values for image source, file format and update
			// action
			if (null == session.getAttribute(IMAGE_LOCATION)) {
				session.setAttribute(IMAGE_LOCATION, this.vendorCatalogManager
						.getVendorCatalogImageLocations());
			}
			if (null == session.getAttribute(UPDATE_ACTION_LIST)) {
				session.setAttribute(UPDATE_ACTION_LIST,
						this.vendorCatalogManager
								.getVendorCatalogUpdateActions());
			}
			if (null == session.getAttribute(FILE_FORMAT_LIST)) {
				session
						.setAttribute(FILE_FORMAT_LIST,
								this.vendorCatalogManager
										.getVendorCatalogFileFormats());
			}
		} catch (DataAccessException ex) {
			ex.printStackTrace();
			if (log.isErrorEnabled()) {
				log
						.error(
								"Failed to get vendor catalog info in save/edit mode..",
								ex);
			}
			// e.printStackTrace();
			// vendorCatalogForm.setError(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			if (log.isErrorEnabled()) {
				log
						.error(
								"Failed to get vendor catalog info in save/edit mode..",
								ex);
			}
			// e.printStackTrace();
			// vendorCatalogForm.setError(true);
		}

		/**
		 * check for logged in user retrieving department list for the logged in
		 * user if mode != viewOnly
		 */
		Long userId = this.getLoggedInUser().getId();
		if (userId == null) {
			if (log.isDebugEnabled()) {
				log.debug("User id was null. Redirecting...");
			}
			return new ModelAndView("redirect:dashBoard.html");
		}
		User user = getUserManager().getUser(String.valueOf(userId));
		if (log.isDebugEnabled()) {
			log.debug("logged in user Id...." + userId);
		}
		session.setAttribute("userId", userId);
		List<Department> deptList = new ArrayList<Department>();
		// get departments for logged in user in add mode
		if (mode != null
				&& (mode.equals(ADD_VENDOR_CATALOG) || mode.equals(VERIFY))) {
			deptList = new ArrayList<Department>(user.getDepartments());
			if (log.isDebugEnabled()) {
				log.debug("remove edit/viewOnly from session");
			}
			session.removeAttribute(EDIT_VENDOR_CATAOLOG);
			session.removeAttribute(VIEW_ONLY);
			session.removeAttribute(REMOVED_DEPT_LIST);
			session.removeAttribute(ADDED_DEPT_LIST);
			session.removeAttribute(VENDOR_CATALOG);

			// FTP information (address, user name )
			// be defaulted to information enter from the last catalog that
			// was imported with FTP images.
			if (null != (String) session.getAttribute(FTP_URL)) {
				vendorCatalogForm.setFtpUrl(session.getAttribute(FTP_URL)
						.toString());
			}
			if (null != (String) session.getAttribute(FTP_USER_NAME)) {
				vendorCatalogForm.setFtpUname(session.getAttribute(
						FTP_USER_NAME).toString());
			}

			String venName = ServletRequestUtils.getStringParameter(request,
					"name");
			String venNum = ServletRequestUtils.getStringParameter(request,
					"venNum");
			// checking for vendor name and vendor number for 'Verify' button
			if (venName != null && venNum != null) {
				vendorCatalogForm.setVendorName(venName);
				vendorCatalogForm.setVendorNumber(venNum);
				request.setAttribute("name", venName);
				// return vendorCatalogForm;
			} else if ((String) request.getSession(false)
					.getAttribute(VENDR_ID) != null
					&& (String) request.getSession(false)
							.getAttribute(VEN_NAME) != null) {
				log.debug("ven_name======================"
						+ request.getSession().getAttribute(VEN_NAME));

				vendorCatalogForm.setVendorNumber(request.getSession()
						.getAttribute(VENDR_ID).toString());
				vendorCatalogForm.setVendorName(request.getSession()
						.getAttribute(VEN_NAME).toString());
			} else if ((CatalogVendorDTO) request.getSession(false)
					.getAttribute(VENDOR_INFO) != null) {
				CatalogVendorDTO catalogDTO = (CatalogVendorDTO) request
						.getSession(false).getAttribute(VENDOR_INFO);
				log.debug("catalogDTO.getName()=" + catalogDTO.getName());
				vendorCatalogForm.setVendorName(catalogDTO.getName());
				String temp = "" + catalogDTO.getVendorNumber();
				vendorCatalogForm.setVendorNumber(temp.trim());
				log.debug("user navigates from vendor catalogs page:");
			}
			
			
		} else {
			// get departments from session in edit/viewonly mode
			if (null != session.getAttribute(DEPARTMENTS)) {
				deptList = (List) request.getSession(false).getAttribute(
						DEPARTMENTS);
				// if (null != deptList && !deptList.isEmpty()) {
				if (log.isDebugEnabled()) {
					log
							.debug("vendor catalog department list not empty in session");
				}
			} 
		}
		
		List<Department> tempList = new ArrayList<Department>();

		// showing updated department list after 'Add Department' functionality
		if (null != session.getAttribute(NEW_DEPT_LIST)) {
			tempList = new ArrayList<Department>();
			tempList = (List) request.getSession(false).getAttribute(
					NEW_DEPT_LIST);
			session.removeAttribute(NEW_DEPT_LIST);
		} else {
			if (null != deptList && !deptList.isEmpty()) {
				tempList = deptList;
			}
		}
		// add selected departments in deptList (Add Department form)
		if (null != session.getAttribute(SELECTED_DEPT)) {
			List<Department> deptList1 = new ArrayList<Department>(
					(List) request.getSession(false)
							.getAttribute(SELECTED_DEPT));
			for (Department dept : deptList1) {
				tempList.add(dept);
			}
			deptList = tempList;
			session.removeAttribute(SELECTED_DEPT);
		}
		// set departments in vendorCatalogForm with the new ones added
		vendorCatalogForm.setDepartments(tempList);
		/**
		 * for getting the updated department list after
		 * VendorCatalogDepartmentForm controller
		 */
		session.setAttribute(NEW_DEPT_LIST, tempList);
		vendorCatalogForm.setUser(user);

		if ((null == session.getAttribute(DEPARTMENTS) && !deptList.isEmpty())
				|| null != session.getAttribute(SELECTED_DEPT)) {
			if (log.isDebugEnabled()) {
				log.debug("gives the departments for logged in user...");
			}
			session.setAttribute("userId", userId);
			session.setAttribute(DEPARTMENTS, deptList);
		}
		if (null != session.getAttribute("EDITED_CATALOG_NAME")) {
			vendorCatalogForm.setCatalogName((String) session
					.getAttribute("EDITED_CATALOG_NAME"));
			session.removeAttribute("EDITED_CATALOG_NAME");
		}
		return vendorCatalogForm;
	}

	private String getCancelRedirectUrl(CatalogVendorDTO catalogDTO) {
		StringBuffer sb = new StringBuffer(
				"redirect:/vendorCatalog/catalogVendors.html?vendorId=");
		String vendorId = catalogDTO.getVendorId();
		long numStyles = catalogDTO.getNumStyle();
		long numSkus = catalogDTO.getNumSKUs();
		sb.append(vendorId);
		sb.append("&numStyles=");
		sb.append(numStyles);
		sb.append("&numSKUs=");
		sb.append(numSkus);
		sb.append("&method=viewAllVendorCatalogs");
		return sb.toString();
	}

	private VendorCatalogForm populateVendorCatalog(String vndrCatlId,
			VendorCatalogForm vendorCatalogForm, HttpSession session,
			HttpServletRequest request, String mode) throws Exception {
		VendorCatalog vendorCatalog = null;
		List<Department> vendorCatalogDepartment = new ArrayList<Department>();
		VendorCatalogImport vendorCatalogImport = null;
		// get vendor catalog for vcID
		vendorCatalog = vendorCatalogManager.getVendorCatalog(Long
				.valueOf(vndrCatlId));

		// set the catalog in session for vendorCatalogInfo section in edit mode
		session.setAttribute(VENDOR_CATALOG, vendorCatalog);
		// set vendor catalog info in session
		CatalogVendorDTO catalogDTO = this.populateCatalogDTO(vendorCatalog
				.getVendor().getVendorNumber());
		session.setAttribute("vendorCatalogInfo", catalogDTO);
		vendorCatalogForm.setVendorName(vendorCatalog.getVendor().getName());
		vendorCatalogForm.setVendorNumber(vendorCatalog.getVendor()
				.getVendorNumber());
		vendorCatalogForm.setCatalogName(vendorCatalog.getVendorCatalogName());
		vendorCatalogForm.setCatalogId(vendorCatalog.getVendorCatalogID()
				.toString());
		// get vendor catalog import info for vcID
		vendorCatalogImport = vendorCatalogManager
				.getVendorCatalogImportDetails(Long.valueOf(vndrCatlId));
		vendorCatalogForm.setFilePath(vendorCatalogImport
				.getVendorCatalogFileName());
		vendorCatalogForm.setFileFormat(vendorCatalogImport.getFileFormatID()
				.getFileFormatName());
		vendorCatalogForm.setImgLocn(vendorCatalogImport.getImageLocationID()
				.getImageLocationDesc());
		if (null != vendorCatalogForm.getImgLocn()
				&& vendorCatalogForm.getImgLocn().equalsIgnoreCase(SELECT_OPT)) {
			vendorCatalogForm.setUploadImage(N);
		} else {
			vendorCatalogForm.setUploadImage(Y);
		}
		vendorCatalogForm.setFtpUrl(vendorCatalogImport.getFtpUrl());
		vendorCatalogForm.setFtpUname(vendorCatalogImport.getFtpUname());
		if (null != vendorCatalogForm.getFtpUname()) {
			vendorCatalogForm.setIsAnonymousFTP(false);
		} else {
			vendorCatalogForm.setIsAnonymousFTP(true);
		}
		vendorCatalogForm.setFtpPwd(vendorCatalogImport.getFtpPassword());
		vendorCatalogForm.setPreviousCatalogID(vendorCatalogImport
				.getPrevCatalogId().toString());
		vendorCatalogForm.setUpdateAction(vendorCatalogImport
				.getUpdateActionID().getUpdateActionDesc());

		// get vendor catalog department(s) for vcID
		vendorCatalogDepartment = vendorCatalogManager
				.getVendorCatalogDeptDetails(vendorCatalog);
		vendorCatalogForm.setDepartments(vendorCatalogDepartment);
		if (log.isDebugEnabled()) {
			log.debug("vendor catalog departments in edit mode");
		}
		if (null == session.getAttribute(DEPARTMENTS)
				&& !vendorCatalogDepartment.isEmpty()) {
			session.setAttribute(DEPARTMENTS, vendorCatalogDepartment);
		}
		// set enable to true to show form fields
		request.getSession().setAttribute(ENABLE, TRUE);
		if (mode.equals(VIEW_ONLY)) {
			session.setAttribute(VIEW_ONLY, true);
			session.setAttribute(EDIT_VENDOR_CATAOLOG, false);
		} else {
			// set enable to true to show form fields
			request.getSession().setAttribute(ENABLE, TRUE);
			session.setAttribute(VIEW_ONLY, false);
			session.setAttribute(EDIT_VENDOR_CATAOLOG, true);
		}
		// lock the catalog
		User currentUser = getLoggedInUser();
		vendorCatalogManager.lockCatalog(vndrCatlId, currentUser);
		
		return vendorCatalogForm;
	}

	private CatalogVendorDTO populateCatalogDTO(String vendorNo)
			throws Exception {
		List<CatalogVendorDTO> list = this.vendorCatalogManager
				.getAllVendors(vendorNo);
		CatalogVendorDTO catalogVendorDTO = list.get(0);
		return catalogVendorDTO;
	}
}
