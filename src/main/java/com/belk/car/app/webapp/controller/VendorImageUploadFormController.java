package com.belk.car.app.webapp.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.dto.UploadImagesDTO;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.VendorImageManager;
import com.belk.car.app.util.FtpUtil;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;


/**
 * @author AFUSYQ3
 *
 */
public class VendorImageUploadFormController extends BaseFormController implements HandlerExceptionResolver  {
	
	protected VendorImageManager vendorImageManager;
	protected VendorImage vendorImage;
	protected CarManager carManager;

	public VendorImageManager getVendorImageManager() {
		return vendorImageManager;
	}

	public void setVendorImageManager(VendorImageManager vendorImageManager) {
		this.vendorImageManager = vendorImageManager;
	}
	
	public VendorImage getVendorImage() {
		return vendorImage;
	}

	public void setVendorImage(VendorImage vendorImage) {
		this.vendorImage = vendorImage;
	}
	
	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public VendorImageUploadFormController() {
		setCommandName("uploadImagesDTO");
		setCommandClass(UploadImagesDTO.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request)throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("entering ImageRequestFormController 'formBackingObject'");
		}		
		UploadImagesDTO uploadDto=new UploadImagesDTO();
		return uploadDto;
	}
	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws Exception {
		String vendorNumber = request.getParameter("vNo");
		String styleNumber = request.getParameter("vsNo");
		String colorCode = request.getParameter("cCode");
		String sampleType =request.getParameter("sampleType");
		String carId=request.getParameter("carId");
		Map referenceData = new HashMap();
		Map<String, String> imageType = new LinkedHashMap<String, String>();
		List<String> listShotTypes = new ArrayList<String>();
		VendorStyle vendorStyle = carManager.getVendorStyle(
				vendorNumber, styleNumber);
		listShotTypes = vendorImageManager.getNextShotTypeCodeList(
				vendorStyle.getVendorStyleId(), colorCode, new Long(carId));		
		Map<String, String> shotTypeMap = new LinkedHashMap<String, String>();
		boolean uploadMain=false;
		boolean uploadSwatch=false;
		boolean uploadAlt=false;
		if (listShotTypes != null && listShotTypes.contains("A")) {
			uploadMain=true;
			listShotTypes.remove("A");
		} 
		if (listShotTypes != null && listShotTypes.contains("SW")) {
			uploadSwatch=true;
			listShotTypes.remove("SW");
		} 
		int alternateImageCount = listShotTypes.size();
		referenceData.put("alternateImageCount",alternateImageCount);
		if(alternateImageCount > 0){
			uploadAlt=true;
		}
		if (listShotTypes != null) {
			shotTypeMap.put("","");
			for (String shotTypevalue : listShotTypes) {
				if (shotTypevalue != null) {
				   shotTypeMap.put(shotTypevalue, shotTypevalue);
				}
			}
		}
		// If sample type not Neither then do not populate drop down for Main and Swatch  images
		if (!"NITHER".equals(sampleType)) {
			uploadMain = false;
			uploadSwatch = false;
		}
		// Set image upload drop down order
		if (uploadMain) {
			imageType.put("MAIN", "Main Image");
		}
		if (uploadAlt) {
			imageType.put("ALT", "Alternate Image");
		}
		if (uploadSwatch) {
			imageType.put("SWATCH", "Swatch Image");
		}
		// Check the DB for image type availability
		referenceData.put("ImageTypeList", imageType);
		Map<String, String> imageLocationType = new LinkedHashMap<String, String>();
		imageLocationType.put("CD", "Browse my locations");
		imageLocationType.put("FTP", "FTP");
		referenceData.put("imageLocList", imageLocationType);
		referenceData.put("shotTypeMapList", shotTypeMap);
		return referenceData;
	}
	
	public ModelAndView onSubmit(HttpServletRequest request,HttpServletResponse response, Object command,
	       BindException errors)throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering VendorImageUploadFormController 'onSubmit'");
		}
		String imageLocationType="";
		String ftpResult="";
		boolean uploadedSucess=false;
		UploadImagesDTO uploadImagesDTO = (UploadImagesDTO)command;
		Map<String,Object> model = new HashMap<String,Object>();	
		uploadImagesDTO.setVendorNumber(request.getParameter("vNo"));
		uploadImagesDTO.setStyleNumber(request.getParameter("vsNo"));
		uploadImagesDTO.setColorCode(request.getParameter("cCode"));
		uploadImagesDTO.setCarId(new Long(request.getParameter("carId")));
		String colorName=request.getParameter("cName");
		String cName=colorName.replace("_"," ");
		uploadImagesDTO.setColorName(cName);
		imageLocationType=uploadImagesDTO.getImageLocationType().get(0);
		if (imageLocationType.equals("CD")) {
			 model.put("type","CD"); 
			uploadedSucess=UploadCDImages(uploadImagesDTO);
			if (uploadedSucess) {
				model.put("uploaded", "success");
			} else {
			    model.put("uploaded","failed"); 
		    }
		} else {
			model.put("type","FTP");
			ftpResult=UploadFTPImages(uploadImagesDTO);
			if (ftpResult.equals("success")) {
				model.put("uploaded", "success");
			} else {
				model.put("uploaded","failed"); 
				// FTP failed reasons - dirNotFound,imageNotFound,unableToConnectFTP
			    model.put("ftpUploadFailedReason",ftpResult); 
		    }
		}
		model.put("fileName", uploadImagesDTO.getUserUploadedFileName());
		request.setAttribute("jsonObj", new JSONObject(model));
		return new ModelAndView(getAjaxView());
    }
	
	/**
	 * Upload FTP images from upload images screen
	 * @param uploadImagesDTO
	 * @param vendorNumber
	 * @param styleNumber
	 * @param colorCode
	 * @return
	 */
	@SuppressWarnings({ "unused", "static-access" })
	private String UploadFTPImages(UploadImagesDTO uploadImagesDTO) {
		String imageType = "";
		boolean validImage = false;
		String ftpResult = "";
		String ftpUrl = uploadImagesDTO.getFtpUrl();
		String ftpPath = uploadImagesDTO.getFtpPath();
		String ftpUserId = uploadImagesDTO.getFtpUserId();
		String ftpPassword = uploadImagesDTO.getFtpPassword();
		String ftpImageName = uploadImagesDTO.getFtpFileName();
		if (ftpImageName != null && !ftpImageName.isEmpty()) {
			uploadImagesDTO.setUserUploadedFileName(ftpImageName);
			String[] fileExtn = ftpImageName.split("\\.");
			setImageDetails(uploadImagesDTO, fileExtn[(fileExtn.length - 1)]);
			FtpUtil ftp = new FtpUtil();
			// download image from FTP site
			ftpResult = ftp.downloadFTPImage(ftpUrl, ftpUserId, ftpPassword,
					ftpPath, ftpImageName, uploadImagesDTO.getImageName(),
					uploadImagesDTO.getVendorImageUploadDir());
			if (ftpResult.equals("success")) {
				if (vendorImageManager.saveImageData(uploadImagesDTO)) {
					ftpResult = "success";
				} else {
					ftpResult = "unableToSaveData";
				}
			} else {
				log.error("Unable to download the vendor image " + ftpResult);
			}
		}
		return ftpResult;
	}
	
	/**
	 * Upload local images from upload images screen
	 * @param uploadImagesDTO
	 * @param vendorNumber
	 * @param styleNumber
	 * @param colorCode
	 * @return
	 * @throws IOException
	 */
	private boolean UploadCDImages(UploadImagesDTO uploadImagesDTO)
			throws IOException {
		boolean uploadedSucess = false;
		InputStream is = null;
		OutputStream os = null;
		MultipartFile multipartFile = uploadImagesDTO.getCdImageFile();
		if (multipartFile != null) {
			uploadImagesDTO.setUserUploadedFileName(multipartFile
					.getOriginalFilename());
			String[] fileExtn = uploadImagesDTO.getUserUploadedFileName().split("\\.");
			setImageDetails(uploadImagesDTO, fileExtn[(fileExtn.length - 1)]);
			try {
				is = multipartFile.getInputStream();
				os = new FileOutputStream(
						uploadImagesDTO.getVendorImageUploadDir()+ uploadImagesDTO.getImageName());
				byte[] b = new byte[2048];
				int length;
				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}
				
				uploadedSucess = true;
			} catch (Exception e) {
				uploadedSucess = false;
				log.error("Unable to upload the local image" + e);
			}finally{
				try{
					is.close();
					os.close();
				}catch(Exception e){
					log.error("Unable to close OutputStream objects" + e);
				}
			}
			// Save image Details to DB
			if (uploadedSucess){
				uploadedSucess = vendorImageManager.saveImageData(uploadImagesDTO);
			}else{
				log.error("Error occured while saving the vendor image data to database");
			}
		}
		return uploadedSucess;
	}

	/**
	 * Set the imageName and Shot type to UploadImagesDTO
	 * 
	 * @param uploadImagesDTO
	 * @param fileExtn
	 */
	public void setImageDetails(UploadImagesDTO uploadImagesDTO, String fileExtn) {
		String vendorImageUploadDir = "";
		String RRDImageUploadedDir = "";
		try {
			String imageType = uploadImagesDTO.getImageType().get(0);
			// Set Image Type to uploadImagesDTO
			if (imageType.equals("MAIN")) {
				uploadImagesDTO.setShotType("A");
			} else if (imageType.equals("SWATCH")) {
				uploadImagesDTO.setShotType("SW");
			} else {
				VendorStyle vendorStyle = carManager.getVendorStyle(
						uploadImagesDTO.getVendorNumber(),
						uploadImagesDTO.getStyleNumber());
				uploadImagesDTO
						.setVendorStyleId(vendorStyle.getVendorStyleId());
				//Get next alternate shot type code
				String ShotType = uploadImagesDTO.getShotTypeValue().get(0);
				if (StringUtils.isNotBlank(ShotType)) {
					uploadImagesDTO.setShotType(ShotType);
				} 
			}
			// [VENDOR_NUMBER]_[STYLE_NUMBER]_[A-Z SHOT
			// CODE]_[NRF_COLOR_CODE].[EXT]
			String imageName = uploadImagesDTO.getVendorNumber() + "_"
					+ uploadImagesDTO.getStyleNumber() + "_"
					+ uploadImagesDTO.getShotType() + "_"
					+ uploadImagesDTO.getColorCode() + "." + fileExtn;
			uploadImagesDTO.setImageName(imageName.toUpperCase());
		} catch (Exception e) {
			log.error("VendorImageUploadFormController.setImageDetails() error occured while setting the values" +e);
		}
		try {
			// Set Vendor Image upload directory
			// /ecommercedev/images/vendor_images/
			// Set RRD image upload directory - Final directory Belkmacl
			vendorImageUploadDir = vendorImageManager.getVendorImageUploadDir();
			uploadImagesDTO.setVendorImageUploadDir(vendorImageUploadDir);
			RRDImageUploadedDir = vendorImageManager.getRRDImageUploadedDir();
			uploadImagesDTO.setRRDImageUploadedDir(RRDImageUploadedDir);

		} catch (Exception e) {
			log.error("Unable to read the vendorImageUploadDir property from properties file");
		}
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception exception) {
		Map<String, Object> model = new HashMap<String, Object>();
		if (exception instanceof MaxUploadSizeExceededException) {
			model.put("uploaded", "failed");
		} else {
			model.put("uploaded", "failed");
		}
		request.setAttribute("jsonObj", new JSONObject(model));
		return new ModelAndView(getAjaxView());
	}
}