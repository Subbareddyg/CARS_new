package com.belk.car.app.webapp.controller.vendorcatalog;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dto.CatalogVendorDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.service.VendorCatalogManager;
import com.belk.car.app.util.PropertyLoader;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.VendorCatalogImageUploadForm;
import java.io.File;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
/**
 * Controller to select the vendor catalog file selection opertation.
 * @author afusy13
 *
 */

public class VendorCatalogFilePopupFormContoller  extends BaseFormController{
	private transient final Log log = LogFactory
	.getLog(VendorCatalogFilePopupFormContoller.class);
	
	private VendorCatalogManager vendorCatalogManager;
	
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
	
	/**
	 * Constructor to set the Command Object
	 */
	public VendorCatalogFilePopupFormContoller() {
		if (log.isDebugEnabled()) {
			log.debug("Inside VendorCatalogFilePopupFormContoller() method..");
		}
		setCommandName("vendorCatalogImageUploadForm");
		setCommandClass(VendorCatalogImageUploadForm.class);
	}
	
	
	/**
	 * Method to backing up the form to retain values
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		VendorCatalogImageUploadForm vendorCatalogImageUploadForm = new VendorCatalogImageUploadForm();
		log.debug("Inside the form backing method");
		
                String vendorCatalogId= request.getParameter("vendorCatalogId");
                String vendorStyleId= request.getParameter("vendorStyleId");
                String vendorUpc= request.getParameter("vendorUpc");
                
		HttpSession session = request.getSession();
		if(null !=  vendorCatalogId){
		session.setAttribute("addImageVendorCatalogId", vendorCatalogId);
		}
		if(null !=  vendorStyleId){
			session.setAttribute("addImageVendorStyleId", vendorStyleId);
		}
		if(null !=  vendorUpc){
			session.setAttribute("addImageVendorUPC", vendorUpc);
		}
		return vendorCatalogImageUploadForm;
	}

	/**
	 * Method to check the submit type of the form
	 */
	public ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response,Object command, BindException errors)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("Inside processFormSubmission() method..");
		}
		ModelAndView modelView=null;
		if (request.getParameter("cancel") != null) {
			modelView= new ModelAndView(this.getCancelView());
		}
		else{
                    String  vendorUpc= (String)request.getSession().getAttribute("addImageVendorUPC");
                    if(vendorUpc.equals("") || vendorUpc.equals(null)){
                    	return new ModelAndView("redirect:/mainMenu.html");
                    }
			modelView=super.processFormSubmission(request, response, command, errors);
		}
		return modelView;
	}

	/**
	 * After submitting form, this method saves the records in database.
	 * 
	 * @param request HttpServletRequest response HttpServletResponse command
	 *            Command Object errors Error object
	 */
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors)
			throws Exception {
		log.debug("Inside the onSubmit mode.");
		VendorCatalogImageUploadForm vendorCatalogImageUploadForm = (VendorCatalogImageUploadForm) command;
		//Get the data from session and set it to a map object
		Map<String, Object> sessionValues = getDataFromSession(request);
		
		//Check for all the validations and then upload the file if valid.
		//Get the image type first.
		String imageType="MAIN";
		if(null != request.getParameter("imageType")){
			 imageType = request.getParameter("imageType");
			log.debug("Image type :"+imageType);
		}
                String vendorCatalogId= (String)request.getSession().getAttribute("addImageVendorCatalogId");
                String vendorStyleId= (String)request.getSession().getAttribute("addImageVendorStyleId");
                String vendorUpc= (String)request.getSession().getAttribute("addImageVendorUPC");
                if(vendorCatalogId.equals("") || vendorCatalogId.equals(null)){
                	return new ModelAndView("redirect:/mainMenu.html");
                }
                if(vendorStyleId.equals("") || vendorStyleId.equals(null)){
                	return new ModelAndView("redirect:/mainMenu.html");
                }
                if(vendorUpc.equals("") || vendorUpc.equals(null)){
                	return new ModelAndView("redirect:/mainMenu.html");
                }
                		
               
                if(vendorUpc !=null  && !vendorUpc.equals(DropShipConstants.ZERO)) {
                    vendorUpc = StringUtils.leftPad(vendorUpc, 13, DropShipConstants.ZERO);
                }
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("Vendor_Catalog_Id", Integer.parseInt(vendorCatalogId));
		param.put("Vendor_Style_Id",vendorStyleId);
		param.put("Vendor_UPC",vendorUpc);
		param.put("imageType",imageType);
		int count = vendorCatalogManager.getCountForPreviousUploads(param);
                CatalogVendorDTO catalogVendorDTO  = (CatalogVendorDTO) request.getSession().getAttribute(DropShipConstants.VENDOR_INFO);
                String vendorNumber = ""+catalogVendorDTO.getVendorNumber();
                vendorNumber =vendorNumber.trim();
                if(vendorCatalogId == null){
                	return new ModelAndView("redirect:/mainMenu.html");
                }
                char type='M';
		//Check for Image types.
		if(imageType.equals("MAIN")) {
                    type ='M';
                } else if(imageType.equals("SWATCH")) {
                   type ='S';
                } else {
                    if(count==0) {
                        count =1;
                    }
                    String temp =""+count;
                    type =temp.trim().charAt(0);
                }
		String fileUploaded=null;
		if (log.isDebugEnabled()) {
			log.debug("Count is : "+ count+ " for image type:"+imageType);
		}
		//Validation 1: Only one Main image needs to be there
		//Validation 2: Only one Swatch image needs to be there.
		if((imageType.equalsIgnoreCase("MAIN") || imageType.equalsIgnoreCase("SWATCH")) && count == 1 ){
			errors.rejectValue("", "error.already.present", "The image for this image type already present.");
			return showForm(request, response, errors);
		}else if(imageType.equalsIgnoreCase("ALT") && count == 5){
			//Validation 3: Only 5 alternative images can be uploaded.
			errors.rejectValue("", "error.already.present", "Images for Alternate image type already present.");
			return showForm(request, response, errors);
		}else{
			log.debug("Else block");
			log.debug("Checking for File :" + vendorCatalogImageUploadForm.getFile());
			if (null != vendorCatalogImageUploadForm.getFile() && vendorCatalogImageUploadForm.getFile().length > 0) {
				log.debug("File object is not null, so updating the file...");
				fileUploaded=doFileOperations(vendorCatalogImageUploadForm,request,sessionValues);
				log.debug("File uploading status:"+fileUploaded);
			}
			if(null!=fileUploaded){
				//Create the object and insert it into database..
				try{
                                       User currentUser = getLoggedInUser();
                                       currentUser.getFullName();
				       vendorCatalogManager.addImageToStyleSku(vendorStyleId, vendorCatalogId, vendorUpc, type, fileUploaded,vendorNumber,currentUser.getFullName());
				}catch(Exception e){
					log.error(e);
                                        e.printStackTrace();
				}
				
				//Save the record in Database.
				if (log.isDebugEnabled()) {
					log.debug("Saving the record in Database.");
					log.debug("File uploaded successfully.. Status is:"+fileUploaded);
				}
				
				errors.rejectValue("", "error.already.present", "Saved Successfully!");
				return showForm(request, response, errors);
			}
			else{
				if (log.isDebugEnabled()) {
					log.debug("File uploading failed.. Status is:"+fileUploaded);
				}
				errors.rejectValue("", "error.already.present", "Some problem occured while saving the image. Please try later.");
				return showForm(request, response, errors);
			}
		}
	}
	
	private Map<String, Object> getDataFromSession(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>();
		HttpSession session = request.getSession();
			if(null !=  session.getAttribute("addImageVendorCatalogId")){
				params.put("VendorCatalogId", session.getAttribute("VendorCatalogId"));
				session.removeAttribute("VendorCatalogId");
			}
			if(null !=  session.getAttribute("VendorStyleId")){
				params.put("VendorStyleId", session.getAttribute("addImageVendorStyleId"));
				session.removeAttribute("VendorStyleId");
			}
			if(null !=  session.getAttribute("VendorUPC")){
				params.put("VendorUPC", session.getAttribute("addImageVendorUPC"));
				session.removeAttribute("VendorUPC");
			}

		return params;
	}

	private String doFileOperations(VendorCatalogImageUploadForm vendorCatalogImageUploadForm,
			HttpServletRequest request, Map<String, Object> sessionValues) throws Exception {
		List<String> list = null;
                String fileName=null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
				.getFile("file");
		if (log.isDebugEnabled()) {
			log.debug("File is:"+ file);
		}
		/** Upload the file */

                CatalogVendorDTO catalogVendorDTO  = (CatalogVendorDTO) request.getSession().getAttribute(DropShipConstants.VENDOR_INFO);
                String vendorNumber = ""+catalogVendorDTO.getVendorNumber();
                vendorNumber =vendorNumber.trim();
                String vendorCatalogId=(String)request.getSession().getAttribute("addImageVendorCatalogId");
                Properties properties = PropertyLoader.loadProperties("ftp.properties");
		String destinationPath = properties.getProperty("imagePath");
		StringBuffer destDir =new StringBuffer(destinationPath).append(vendorNumber);

		destDir.append("_");
		destDir.append(vendorCatalogId);
		destDir.append(File.separator);
		destDir.append("Unmapped");
		destDir.append(File.separator);
		boolean isFileUploaded = this.vendorCatalogManager
				.uploadCatalogStyleSkuImage(file,destDir.toString());
                fileName = file.getOriginalFilename();
		/** Condition for file not uploaded */
		if (!isFileUploaded) {
			log.error("Vendor Catalog Style Sku Image File is not getting uploaded.");
		}
		return fileName;
	}
}
