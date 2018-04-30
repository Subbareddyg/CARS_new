package com.belk.car.app.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.appfuse.model.User;
import org.appfuse.service.impl.UniversalManagerImpl;
import org.springframework.dao.DataAccessException;

import antlr.StringUtils;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.VendorImageManagementDao;
import com.belk.car.app.dto.UploadImagesDTO;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarImage;
import com.belk.car.app.model.CarImageId;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageLocationType;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.ImageType;
import com.belk.car.app.model.MediaCompassImage;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.model.vendorimage.VendorImageStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.VendorImageManager;
import com.belk.car.app.util.PropertyLoader;

public class VendorImageManagerImpl extends UniversalManagerImpl implements VendorImageManager {
	
	private CarManager carManager;
	private VendorImageManagementDao vendorImageDao;
	private CarLookupManager carLookupManager;
	/**
	 * @return the carLookupManager
	 */
	public CarLookupManager getCarLookupManager() {
		return carLookupManager;
	}
 
	/**
	 * @param carLookupManager the carLookupManager to set
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public void setVendorImageDao(VendorImageManagementDao vendorImageDao) {
		this.vendorImageDao = vendorImageDao;
	}

	@Override
	public List<Car> getCarsForUser(User usr, boolean disableAssociations)
			throws DataAccessException {
		return vendorImageDao.getCarsForUser(usr);
	}
	
	public List<Image> getImagesByVendorImageStatus(String[] vendorImageStatus) throws Exception{
		return vendorImageDao.getImagesByVendorImageStatus(vendorImageStatus);
	}
	
	public List<Image> getImagesByVendorImageStatus(String[] vendorImageStatus, Date date) throws Exception{
		return vendorImageDao.getImagesByVendorImageStatus(vendorImageStatus,date);
	}
	
	public Object save(Object ob) {
		return vendorImageDao.save(ob);
	}	
	
	public Object saveOrUpdate(Object ob) {
		return vendorImageDao.saveOrUpdate(ob);
	}	
	
	public List<Image> getRemovedVendorImages(String strLastJobRunTime){
		return vendorImageDao.getRemovedVendorImages(strLastJobRunTime );
	}

	/**
	 * This method gets the image id from sequence  
	 * @return long
	 */
	@Override
	public Long getImageIdFromSeq() {
		return vendorImageDao.getImageIdFromSeq();
	}
	
	/**
	 * @return the carManager
	 */
	public CarManager getCarManager() {
		return carManager;
	}

	/**
	 * @param carManager the carManager to set
	 */
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
	
	public User getLoggedInUser() {
    	User user = null;
    	 Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
         if (auth.getPrincipal() instanceof UserDetails) {
             user = (User)  auth.getPrincipal();
         }
         return user;
    }
	
	/**
	 * This method saves the CD and FTP image data in DB
	 * 
	 * @param uploadImagesDTO
	 * @return
	 */
	@Override
	public boolean saveImageData(UploadImagesDTO uploadImagesDTO) {
		boolean saveSucess=false;
		try {
			Date currentDate = new Date();
			Image image = new Image();
			VendorStyle vendorStyle = null;
			CarImage ci = new CarImage();
			CarImageId cId = new CarImageId();
			VendorImage vendorImage = new VendorImage();
			// Save imageType code MAIN || SAWTCH || ALT
			ImageLocationType imageLocationType = (ImageLocationType) getCarManager()
				       .getFromId(ImageLocationType.class,uploadImagesDTO.getImageLocationType().get(0));
			image.setImageTypeCd(uploadImagesDTO.getImageType().get(0));
			image.setImageLocation(uploadImagesDTO.getImageName().toUpperCase());
			image.setImageLocationType(imageLocationType);
			// Get the original file name and Save
			image.setDescription(uploadImagesDTO.getUserUploadedFileName());
			// Set the RRD uploaded image directory
			image.setImageFinalUrl(uploadImagesDTO.getRRDImageUploadedDir()+uploadImagesDTO.getImageName());
			StringBuffer sb=new StringBuffer();
			
			if(log.isInfoEnabled()){
				if ("CD".equals(image.getImageLocationTypeCd())) {
					sb.append(uploadImagesDTO.getImageName());
				} else {
					// ftp::uname::password::ftp.host.name::Directory_name::image_name.ext
					sb.append(uploadImagesDTO.getFtpUserId()+ "::"
							+ uploadImagesDTO.getFtpPassword()+ "::"
							+ uploadImagesDTO.getFtpUrl() + "::"
							+ uploadImagesDTO.getFtpPath() + "::"
							+ uploadImagesDTO.getFtpFileName());
				}
				log.info("uploading image : " +sb.toString());
			}
			
			ImageSourceType imageSourceTypeBuyer = ((ImageSourceType) this
					        .getCarLookupManager().getById(ImageSourceType.class,ImageSourceType.BUYER_UPLOADED));
			ImageSourceType imageSourceTypeVendor = ((ImageSourceType) this
					        .getCarLookupManager().getById(ImageSourceType.class,ImageSourceType.VENDOR_UPLOADED));
			// Get the current user object
		
				User user=carManager.getUserForUsername("carsadmin@belk.com");

            boolean isEmtyPimId = org.apache.commons.lang.StringUtils.isEmpty(String
                    .valueOf(uploadImagesDTO.getPimImageId()))
                    || uploadImagesDTO.getPimImageId() == 0;

			if (!isEmtyPimId || UserType.BUYER.equalsIgnoreCase(user.getUserType().getUserTypeCd())) {
				image.setImageSourceType(imageSourceTypeBuyer);
				image.setImageSourceTypeCd(ImageSourceType.BUYER_UPLOADED);
			} else  {
				image.setImageSourceType(imageSourceTypeVendor);
				image.setImageSourceTypeCd(ImageSourceType.VENDOR_UPLOADED);
			}
			ImageTrackingStatus imageReceived = (ImageTrackingStatus) getCarManager()
					       .getFromId(ImageTrackingStatus.class,ImageTrackingStatus.REQUESTED);
			image.setImageTrackingStatus(imageReceived);
			image.setImageTrackingStatusCd(Constants.REQUESTED);
			image.setRequestDate(currentDate);
			image.setStatusCd("ACTIVE");
			image.setAuditInfo(user);
			image.setImageProcessingStatusCd(Constants.PENDING);
			image.setApprovalNotesText(Constants.VENDORIMAGE);
			ImageType imageType = ((ImageType) this.getCarLookupManager()
					  .getById(ImageType.class,uploadImagesDTO.getImageType().get(0)));
			image.setImageType(imageType);
			// Persist image object
			vendorImageDao.save(image);
			// get VendorStyle object from DB
			vendorStyle = carManager.getVendorStyle(
				       uploadImagesDTO.getVendorNumber(),uploadImagesDTO.getStyleNumber());
			if(!isEmtyPimId)
			vendorImage.setVendorImagePimId(String.valueOf(uploadImagesDTO.getPimImageId()));
			vendorImage.setVendorStyle(vendorStyle);
			vendorImage.setSwatchTypeCd(uploadImagesDTO.getShotType());
			vendorImage.setColorCode(uploadImagesDTO.getColorCode());
			// Get the color based on color code
			vendorImage.setColorName(uploadImagesDTO.getColorName());
			// Get the car object
			Car car = carManager.getCarFromId(uploadImagesDTO.getCarId());
			// Auto approve images,if uploaded by BUYER
			if ("BUYER".equals(user.getUserType().getUserTypeCd())) {
				vendorImage.setApprovedDate(currentDate);
				vendorImage.setBuyerApproved(Constants.APPROVED);
				//Maintain value for Dash board changes
				car.setImageMCPendingByUser("BUYER");
			} else {
				//Maintain value for Dash board changes
				car.setImageMCPendingByUser("VENDOR");
				//Maintain value for images pending for buyer approval
				car.setBuyerApprovalPending("Y");
			}
			
			
			VendorImageStatus vendorImageStatusUploaded = ((VendorImageStatus) this
				   .getCarLookupManager().getById(VendorImageStatus.class,VendorImageStatus.UPLOADED));
			// Mark image status as UPLOADED
			vendorImage.setVendorImageStatus(vendorImageStatusUploaded);
			vendorImage.setAuditInfo(user);
			vendorImage.setMcUploadDate(null);
			vendorImage.setImage(image);
			// Persist vendorImage object
			vendorImageDao.save(vendorImage);
			cId.setCarId(car.getCarId());
			cId.setImageId(image.getImageId());
			ci.setId(cId);
			ci.setCar(car);
			ci.setAuditInfo(user);
			ci.setImage(image);
			car.getCarImages().add(ci);
			// Persist car object
			vendorImageDao.save(car);
			saveSucess=true;
			log.info("Image Data Successfully saved for  "+uploadImagesDTO.getImageName());
		} catch (Exception e) {
			log.error("Error occured while saving the image for "+uploadImagesDTO.getImageName()+","+e);
			saveSucess=false;
		}
		return saveSucess;
	}

	@Override
	public Car getCarByVendorImageId(long vendorImageId){
		return vendorImageDao.getCarByVendorImageId(vendorImageId);
	}
	
	/**
	 * This methods gets the directory to upload the image file.
	 * 
	 * @return String
	 */
	public String getVendorImageUploadDir() throws Exception {
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		return properties.getProperty("vendorImageUploadDir");
	}
	
	public String getPimImageUploadDir() throws Exception {
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		return properties.getProperty("PIMImageUploadedDir");
	}
	
	public String getPimImageArchiveDir() throws Exception {
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		return properties.getProperty("PIMImageUploadedArchiveDir");
	}
	
	/**
	 * This methods gets the directory for RRD uploaded image file.
	 * 
	 * @return String
	 */
	public String getRRDImageUploadedDir() throws Exception {
		Properties properties = PropertyLoader.loadProperties("ftp.properties");
		return properties.getProperty("RRDImageUploadedDir");
	}

	@Override
	public boolean UpdateImageData(UploadImagesDTO uploadImagesDTO,
			String imageId) {
		boolean isSaveSucess= false;
		try {
			VendorImage vendorImage = null;
			ImageSourceType imageSourceType=null;
			User user = getLoggedInUser();
			Image image = (Image) this.getCarLookupManager().getById(Image.class, Long.parseLong(imageId));
			vendorImage = image.getVendorImage();
			// set image location pointing to belkmacl
			// update image extension, if older and current vendor image extensions differ 
			String[] userUploadedImgExtn = uploadImagesDTO.getUserUploadedFileName().split("\\.");
			String[] olderImgExtn = image.getImageLocation().split("\\.");
			if (!userUploadedImgExtn[1].equals(olderImgExtn[1])) {
				String imageName = olderImgExtn[0];
				image.setImageLocation(imageName +"."+ userUploadedImgExtn[1].toUpperCase());
				String RRDImageUploadedDir = getRRDImageUploadedDir();
				// Set the RRD uploaded image directory
				image.setImageFinalUrl(RRDImageUploadedDir+image.getImageLocation());
			}
			ImageLocationType imageLocationType = (ImageLocationType) getCarManager()
				       .getFromId(ImageLocationType.class,uploadImagesDTO.getImageLocationType().get(0));
			image.setImageLocationType(imageLocationType);
			// Get the original file name and Save
			image.setDescription(uploadImagesDTO.getUserUploadedFileName());
			if (UserType.BUYER.equalsIgnoreCase(user.getUserType()
					.getUserTypeCd())) {
				imageSourceType = ((ImageSourceType) this
						.getCarLookupManager().getById(ImageSourceType.class,ImageSourceType.BUYER_UPLOADED));
				image.setImageSourceType(imageSourceType);
				image.setImageSourceTypeCd(ImageSourceType.BUYER_UPLOADED);
			} else {
				imageSourceType = ((ImageSourceType) this
						.getCarLookupManager().getById(ImageSourceType.class,ImageSourceType.VENDOR_UPLOADED));
				image.setImageSourceType(imageSourceType);
				image.setImageSourceTypeCd(ImageSourceType.VENDOR_UPLOADED);
			}
			
			image.setImageProcessingStatusCd(Constants.PENDING);
			image.setAuditInfo(user);
			
			if(log.isInfoEnabled()){
				StringBuffer sb = new StringBuffer();
				if ("CD".equals(image.getImageLocationTypeCd())) {
					sb.append(uploadImagesDTO.getImageName());
				} else {
					// ftp::uname::password::ftp.host.name::Directory_name::image_name.ext
					sb.append(uploadImagesDTO.getFtpUserId()+ "::"
							+ uploadImagesDTO.getFtpPassword()+ "::"
							+ uploadImagesDTO.getFtpUrl() + "::"
							+ uploadImagesDTO.getFtpPath() + "::"
							+ uploadImagesDTO.getFtpFileName());
				}
				log.info("RE-uploading image : " +sb.toString());
			}
			
			this.save(image);
			
			VendorImageStatus vendorImageStatusReUploaded = ((VendorImageStatus) this
					   .getCarLookupManager().getById(VendorImageStatus.class,VendorImageStatus.REUPLOADED));
				// Mark image status as REUPLOADED
				vendorImage.setVendorImageStatus(vendorImageStatusReUploaded);
				vendorImage.setIsImageOnMC("N");
				if(UserType.BUYER.equalsIgnoreCase(user.getUserType().getUserTypeCd())){
					vendorImage.setBuyerApproved(Constants.APPROVED);
				}else{
					vendorImage.setBuyerApproved("NONE");
				}
			
			vendorImage.setAuditInfo(user);	
			//when image is reuploaded change the created by email ID to track who re-uploadd the image.
			vendorImage.setCreatedBy(user.getEmailAddress());
			this.save(vendorImage);
			
			//if image reuploaded then set the IMAGE_MC_PENDING_BU_USER flag to that user.
			Car car = vendorImageDao.getCarByVendorImageId(vendorImage.getVendorImageId());
		    car.setImageMCPendingByUser(user.getUserType().getUserTypeCd());
		    // Check for image pending for buyer approval
			if (UserType.BUYER.equalsIgnoreCase(user.getUserType().getUserTypeCd())) {
				String strBuyerApprovalPending = carManager.checkBuyerApprovalPendingFlag(car);
				// making sure method returns only Y or N
				strBuyerApprovalPending = "Y".equals(strBuyerApprovalPending) ? "Y" : "N";
				car.setBuyerApprovalPending(strBuyerApprovalPending);
			} else {
				car.setBuyerApprovalPending("Y");
			}
			this.save(car);
		    
			isSaveSucess=true;
		} catch (Exception e) {
			log.error("Exception while saving the Vendor Image to DB: ", e);
			isSaveSucess=false;
			e.printStackTrace();
		}

		return isSaveSucess;
	}

		
	public VendorImage getVendorImageDetailsById(long vendorImageId){
		return vendorImageDao.getVendorImageDetailsById(vendorImageId);
	}
	
	@Override
	public List<String> getNextShotTypeCodeList(long vendorStyleId,
			String colorCode, Long carId) {
		return vendorImageDao.getNextShotTypeCodeList(vendorStyleId, colorCode, carId);
	}
	//Below Method added for production issue
	public MediaCompassImage getMediaComapssByCarId(long carId){
		return vendorImageDao.getMediaComapssByCarId(carId);
	}

	@Override
	public VendorImage getVendorImageDetailsByPimImageId(String vendorPimImageId) {
		return vendorImageDao.getVendorImageDetailsByPimImageId(vendorPimImageId);
	}
	
}