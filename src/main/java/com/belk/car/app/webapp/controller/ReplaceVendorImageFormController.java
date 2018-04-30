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
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.dto.UploadImagesDTO;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.VendorImageManager;
import com.belk.car.app.util.FtpUtil;
import com.belk.car.app.util.PropertyLoader;


/**
 * @author AFUSYQ3
 *THis controller is called when user tries to replace the image from car edit page.
 */
public class ReplaceVendorImageFormController extends BaseFormController implements HandlerExceptionResolver  {
	
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

	public ReplaceVendorImageFormController() {
		setCommandName("uploadImagesDTO");
		setCommandClass(UploadImagesDTO.class);
	}
	
	protected Object formBackingObject(HttpServletRequest request)throws IOException {
		if (log.isDebugEnabled()) {
			log.debug("entering PatternImageUploadFormController 'formBackingObject'");
		}		
		UploadImagesDTO uploadDto=new UploadImagesDTO();
		return uploadDto;
	}
	
	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map referenceData = new HashMap();
		Map<String, String> imageLocationType = new LinkedHashMap<String, String>();
		imageLocationType.put("CD", "Browse my locations");
		imageLocationType.put("FTP", "FTP");
		referenceData.put("imageLocList", imageLocationType);
		return referenceData;
	}
	
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		System.out.println("On Submit");
	
		if (log.isDebugEnabled()) {
			log.debug("entering PatternImageUploadFormController 'onSubmit'");
		}
		String imageLocationType = "";
        String ftpResult="";
		boolean uploadedSucess = false;
		UploadImagesDTO uploadImagesDTO = (UploadImagesDTO) command;
		Map<String, Object> model = new HashMap<String, Object>();
		imageLocationType = uploadImagesDTO.getImageLocationType().get(0);
		if (request.getParameter("imageId")!=null) {
			String imageId = request.getParameter("imageId");
			Image image = (Image) this.getCarLookupManager().getById(Image.class, Long.parseLong(imageId));
			String vendorImageUploadDir = vendorImageManager.getVendorImageUploadDir();
			uploadImagesDTO.setVendorImageUploadDir(vendorImageUploadDir);
			if (imageLocationType.equals("CD")) {
				MultipartFile multipartFile = uploadImagesDTO.getCdImageFile();
				if (multipartFile != null) {
					uploadImagesDTO.setUserUploadedFileName(multipartFile
							.getOriginalFilename());
				}
			} else {
				uploadImagesDTO.setUserUploadedFileName(uploadImagesDTO
						.getFtpFileName());
			}
			// update image extension, if older and current vendor image extensions differ 
			String[] userUploadedImgExtn = uploadImagesDTO.getUserUploadedFileName().split("\\.");
			String[] olderImgExtn = image.getImageLocation().split("\\.");
			if (!userUploadedImgExtn[1].equals(olderImgExtn[1])) {
				String imageName = olderImgExtn[0];
				uploadImagesDTO.setImageName(imageName +"."+ userUploadedImgExtn[1].toUpperCase());
			} else {
				uploadImagesDTO.setImageName(image.getImageLocation());
			}
			if (imageLocationType.equals("CD")) {
				model.put("type", "CD");
				uploadedSucess = UploadCDImages(uploadImagesDTO);
				if(uploadedSucess && vendorImageManager.UpdateImageData(uploadImagesDTO, imageId)){
					model.put("uploaded", "success");
					model.put("fileName", uploadImagesDTO.getUserUploadedFileName());
				}else{
					model.put("uploaded", "failed");
				}
			} else {
				model.put("type", "FTP");
				ftpResult = UploadFTPImages(uploadImagesDTO);
				if (ftpResult.equals("success")) {
					if(vendorImageManager.UpdateImageData(uploadImagesDTO, imageId)){
						model.put("uploaded", "success");
						model.put("fileName", uploadImagesDTO.getUserUploadedFileName());
					}else{
						ftpResult="unableToSaveData";
						model.put("uploaded", "failed");
						model.put("ftpUploadFailedReason",ftpResult); 
					}
				}else{
					model.put("uploaded", "failed");
					model.put("ftpUploadFailedReason",ftpResult); 
					log.error("Error occured while saving the vendor image data to DB");
				}
			}
			
		} 
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
		boolean uploadedSucess = false;
		String ftpResult = "";
		String ftpUrl = uploadImagesDTO.getFtpUrl();
		String ftpPath = uploadImagesDTO.getFtpPath();
		String ftpUserId = uploadImagesDTO.getFtpUserId();
		String ftpPassword = uploadImagesDTO.getFtpPassword();
		String ftpImageName = uploadImagesDTO.getFtpFileName();
		if (ftpImageName != null && !ftpImageName.isEmpty()) {
			uploadImagesDTO.setUserUploadedFileName(ftpImageName);
			// String[] fileExtn = ftpImageName.split("\\.");
			// setImageDetails(uploadImagesDTO, fileExtn[(fileExtn.length - 1)]);
			FtpUtil ftp = new FtpUtil();
			// download image from FTP site
			ftpResult = ftp.downloadFTPImage(ftpUrl, ftpUserId, ftpPassword,
					ftpPath, ftpImageName, uploadImagesDTO.getImageName(),
					uploadImagesDTO.getVendorImageUploadDir());
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
			//setImageDetails(uploadImagesDTO, fileExtn[(fileExtn.length - 1)]);
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
				log.error("Unable to upload the local image" + e);
			}finally{
				try{
					is.close();
					os.close();
				}catch(Exception e){
					log.error("Unable to close OutputStream objects" + e);
				}
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
		List<String> imageType = new ArrayList<String>();
		try {
			if ("A".equals(uploadImagesDTO.getShotType())) {
				imageType.add("MAIN");
			} else if ("SW".equals(uploadImagesDTO.getShotType())) {
				imageType.add("SWATCH");
			} else {
				imageType.add("ALT");
			}
			uploadImagesDTO.setImageType(imageType);
			String imageName = uploadImagesDTO.getVendorNumber() + "_"
					+ uploadImagesDTO.getStyleNumber() + "_"
					+ uploadImagesDTO.getShotType() + "1_"
					+ uploadImagesDTO.getColorCode() + "." + fileExtn;
			uploadImagesDTO.setImageName(imageName.toUpperCase());
		} catch (Exception e) {
			log.error("VendorImageUploadFormController.setImageDetails() error occured while seeting the values");
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