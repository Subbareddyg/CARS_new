package com.belk.car.app.service.impl;

import com.belk.car.app.Constants;
import com.belk.car.app.dto.UploadImagesDTO;
import com.belk.car.app.exceptions.PIMStyleColorWebServiceException;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.Config;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.model.vendorimage.VendorImageStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.EmailManager;
import com.belk.car.app.service.PIMFtpImageManager;
import com.belk.car.app.service.VendorImageManager;
import com.belk.car.product.integration.request.data.GetGroupRequestType;
import com.belk.car.product.integration.request.data.GetRequestType;
import com.belk.car.product.integration.request.data.GroupIntegrationRequestData;
import com.belk.car.product.integration.request.data.IntegrationRequestData;
import com.belk.car.product.integration.request.data.PackGroupRequestData;
import com.belk.car.product.integration.request.data.PackItemRequestData;
import com.belk.car.product.integration.response.data.EntryDetailsData;
import com.belk.car.product.integration.response.data.GroupCatalogData;
import com.belk.car.product.integration.response.data.GroupEntryDetailsData;
import com.belk.car.product.integration.response.data.GroupImageSecSpecData;
import com.belk.car.product.integration.response.data.GroupImageSecSpecDeletedImagesData;
import com.belk.car.product.integration.response.data.GroupImageSecSpecImagesData;
import com.belk.car.product.integration.response.data.GroupIntegrationResponseData;
import com.belk.car.product.integration.response.data.IntegrationResponseData;
import com.belk.car.product.integration.response.data.ItemCatalogData;
import com.belk.car.product.integration.response.data.ItemDeletedImagesData;
import com.belk.car.product.integration.response.data.ItemImagesData;
import com.belk.car.product.integration.response.data.SecondaryImageSpecsData;
import com.belk.car.product.integration.service.BelkGroupService;
import com.belk.car.product.integration.service.BelkProductService;
import com.belk.car.util.DateUtils;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.appfuse.service.impl.UniversalManagerImpl;
import org.hibernate.Hibernate;

import java.io.*;
import java.util.*;

/**
 * @author AFUTXD3 This class will be transfer the style color images from pim
 *         ftp location to cars image location and update the necessary image
 *         details into CARS vendor_image tables.
 * 
 */
public class PIMFtpImageManagerImpl extends UniversalManagerImpl implements PIMFtpImageManager {
	private static final String WEB_SERVICE_EEROR = "Unable to retrieve car images from PIM  Reason :";
	private static final String PIM_UPLOAD_FAILED_IMAGE = "Unable to upload pimImages ::";
	private static final String IMAGE_UPLOAD_FROM_PIM_TO_CARS = "Image Upload From PIM To CARS";
	private static final String FAILED_TO_REMOVE_DELETED_IMAGE = "Unable to delete pimImage image ::";
	private static final String SUCCESS = "Image Upload Success";
	private static final String IMAGE_UPLOAD_FAIL = "Image Upload Failed";
	protected VendorImageManager vendorImageManager;
	protected VendorImage vendorImage;
	protected CarManager carManager;
	protected CarLookupManager carLookupManager;
    private EmailManager emailManager;
    private UserManager userManager;

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public VendorImageManager getVendorImageManager() {
		return vendorImageManager;
	}

	public void setVendorImageManager(VendorImageManager vendorImageManager) {
		this.vendorImageManager = vendorImageManager;
	}

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

	/**
	 * @param styleColor
	 * @param isPackColor
	 * @return
	 * @throws PIMStyleColorWebServiceException
	 */
	public IntegrationResponseData retriveImageDetailsFromPIM(List<String> styleColor, boolean isPackColor)
			throws PIMStyleColorWebServiceException {
		if (log.isInfoEnabled())
			log.info("calling webservice to get STYLE-COLOR details from SMART for styleColor" + styleColor);
		IntegrationRequestData requestData = new IntegrationRequestData();
		IntegrationResponseData response = null;
		try {
			// calling new getItem web service from PIM
			BelkProductService integrationService = null;
			if (isPackColor) {
				List<PackItemRequestData> packItemRequestDatas = new ArrayList<PackItemRequestData>();
				for (String packColor : styleColor) {
					PackItemRequestData packItemRequestData = new PackItemRequestData();

					
					String[] splitPack = packColor.split(":");
					if (splitPack.length == 3)
						if (log.isDebugEnabled())
							log.debug("Pack color input param meter does not contain excepted values!!!" + splitPack);
					packItemRequestData.setVendorNumber(splitPack[0]);
					packItemRequestData.setVendorProductNumber(splitPack[1]);
					packItemRequestData.setColorCode(splitPack[2]);
					packItemRequestDatas.add(packItemRequestData);
				}
				requestData.setInputPackData(packItemRequestDatas);
				requestData.setRequestType(GetRequestType.PACK_COLOR.toString());
				integrationService = new BelkProductService(requestData);
			} else {
				requestData.setInputData(styleColor);
				requestData.setRequestType(GetRequestType.STYLE_COLOR.toString());
				integrationService = new BelkProductService(requestData);
			}
			if (integrationService != null)
				response = integrationService.getResponse();
			if (response != null && response.getErrorResponseData() == null) {
				return response;

			} else if (response != null && response.getErrorResponseData() != null) {
				throw new PIMStyleColorWebServiceException(response.getErrorResponseData().getErrorMessage());
			} else {
				
				throw new PIMStyleColorWebServiceException(Constants.IS_STYLE_OR_PACK_SERVICE_HAS_NO_RESPONSE.toString());
			}

		} catch (Exception e) {
			log.error(WEB_SERVICE_EEROR + ":" + e.getMessage());
			throw new PIMStyleColorWebServiceException(WEB_SERVICE_EEROR + ":" + e.getMessage());
	
		}
	}

	/**
	 * down load file from pim ftp location and upload to cars image server
	 * 
	 * @param itemImagesData
	 * @param uploadImagesDTO
	 * @param car 
	 * @return
	 */
	public String uploadFileFromPIMFTPLocationTOCARSIMageServer(ItemImagesData itemImagesData,
			UploadImagesDTO uploadImagesDTO, Car car) throws Exception {
		String note = PIM_UPLOAD_FAILED_IMAGE + itemImagesData.getPimImageId() + "-"
				+ itemImagesData.getImgFileName();
		try {

			boolean isSucess = uploadImageFromPIMLoacation(uploadImagesDTO, itemImagesData,car);
			if (isSucess) {
				moveTransferredFilesToArchive(itemImagesData.getImgFileName());

				return SUCCESS;
			}

		} catch (Exception e) {
			if(log.isErrorEnabled()) {
				if (!isCARSNoteAlreadyExists(car, note))
					createPimImageFailureCarNotes(car, true, note);
				log.error(
						PIM_UPLOAD_FAILED_IMAGE + itemImagesData.getPimImageId() + "-"
								+ itemImagesData.getImgFileName());

			}
		}
		return IMAGE_UPLOAD_FAIL;
	}

	/**
	 * Move all the successfully processed file into pim archive folder.
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	void moveTransferredFilesToArchive(String fileName) throws Exception {
		if (log.isDebugEnabled())
			log.info("Move Transffered filese to pim archive location");
		String sourceFile = "";
		File dest=null;
		try {
			String pimImageUploadDir = vendorImageManager.getPimImageUploadDir();
			String pimImageArchiveDir = vendorImageManager.getPimImageArchiveDir();
			sourceFile = pimImageUploadDir + "//" + fileName;
           if(fileName !=null) {
			File source = new File(sourceFile);
			dest = new File(pimImageArchiveDir + fileName);
			FileUtils.copyFile(source, dest, true);
			FileUtils.forceDelete(source);
           }
		} catch (Exception e) {
			if (log.isErrorEnabled())
				log.error("Pim Image is not avalable in PIM SAN location:: " + sourceFile+" ,If the image is already processed  " +
						"and available in CARS, then please ignore this error ");
		}

	}

	public String deleteFileFromPIMFTPLocation(Car car, ItemDeletedImagesData itemImagesData, UploadImagesDTO dto) {
		if (log.isDebugEnabled())
			log.info("Delete " + itemImagesData.getPimImageId() + ":" + itemImagesData.getImgFileName()
					+ "From CARS System");
		User user=getLoggedInUser();
		if(user == null)
		user=carManager.getUserForUsername("carsadmin@belk.com");
		return deletePimImage(car, String.valueOf(itemImagesData.getPimImageId()),user);

	}

	/**
	 * uploadImageFromPIMLoacation /pimimages/current
	 * 
	 * @param uploadImagesDTO
	 * @param itemImagesData
	 * @param car 
	 * @return
	 * @throws Exception
	 */
	private boolean uploadImageFromPIMLoacation(UploadImagesDTO uploadImagesDTO, ItemImagesData itemImagesData, Car car)
			throws Exception { 
		if (log.isDebugEnabled())
			log.debug("uploadImageFromPIMLoacation method called");
		boolean uploadedSucess = false;
        boolean isImgSizeNotValidInPimLocation = false;
		InputStream is = null;
		OutputStream os = null;
		String pimImageUploadDir = vendorImageManager.getPimImageUploadDir();
		// uploadImagesDTO.setVendorImageUploadDir(pimImageUploadDir);
		String sourceFile = pimImageUploadDir + "//" + itemImagesData.getImgFileName();
		File pimImageFile = new File(sourceFile);
		uploadImagesDTO.setPimImageId(itemImagesData.getPimImageId());

		if (pimImageFile != null) {
			String note = PIM_UPLOAD_FAILED_IMAGE + itemImagesData.getPimImageId() + "-"
					+ itemImagesData.getImgFileName();
			uploadImagesDTO.setUserUploadedFileName(itemImagesData.getImgFileName());
			String[] fileExtn = uploadImagesDTO.getUserUploadedFileName().split("\\.");
			setImageDetails(uploadImagesDTO, fileExtn[(fileExtn.length - 1)], itemImagesData);
            try {
                is = new FileInputStream(pimImageFile);
                os = new FileOutputStream(uploadImagesDTO.getVendorImageUploadDir() + uploadImagesDTO.getImageName());
                int count = 0;
                byte[] b = new byte[2048];
                int length;
                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                    count ++ ;
                }
                is.close();
                os.close();
                if (count > 0) {
                    uploadedSucess = verifyIfImagesExistsinCARSSanLocation(uploadImagesDTO);
                }else {
                    isImgSizeNotValidInPimLocation = true;
                    log.info("ImageFile Size is not valid in PIM location for: " +sourceFile + "Pim Image Id: "
                            + itemImagesData.getPimImageId());
                }
			} catch (FileNotFoundException fne) {
				String filenotFound ="Pim Image ::" +itemImagesData.getImgFileName()+ " is not available in PIM FTP location, Please reset image from PEP";
				if(vendorImage !=null &&  vendorImage.getVendorImagePimId().equalsIgnoreCase(String.valueOf(itemImagesData.getPimImageId()))
						&& !(vendorImage.getVendorImageStatus().getName().equalsIgnoreCase("DELETED"))) { 
					if(log.isDebugEnabled()) {
						log.debug("Pim Image" +  itemImagesData.getPimImageId()+"::" +itemImagesData.getImgFileName()  +"is already  uploaded in CARS");
					} 
				}else if(verifyIfImagesExistsinCARSSanLocation(uploadImagesDTO)) {
					uploadedSucess = true;
				} else {
							 
					if (!isCARSNoteAlreadyExists(car, filenotFound))
						createPimImageFailureCarNotes(car, true, filenotFound);
					log.error(filenotFound);
				}
			} catch (Exception e) {
				if(log.isErrorEnabled()) {
					if (!isCARSNoteAlreadyExists(car, note))
						createPimImageFailureCarNotes(car, true, note);
					log.error(
							PIM_UPLOAD_FAILED_IMAGE + itemImagesData.getPimImageId() + "-"
									+ itemImagesData.getImgFileName());

				}
				
			} finally {
                try {
                    if(is !=null) {
                        is.close();
                    }
                    if(os !=null) {
                        os.close();
                    }
                } catch (Exception e) {
					
					if(log.isErrorEnabled()) {
						log.error("Error occured while processing the image"+itemImagesData.getPimImageId() + "-"
								+ itemImagesData.getImgFileName());
					}
				}
			}

            if (isImgSizeNotValidInPimLocation) {
                log.info("ImageFile Size is not valid in PIMLocation: " +sourceFile);
                sendImageUploadFailureNotification("Pim ImageFile : " +sourceFile +" is empty for PIM Image Id: "
                        +itemImagesData.getPimImageId());
                return false;
            } else if (!uploadedSucess){
                log.info("Entering into retry logic for Pim ImageFile : " +sourceFile +" is not available in VI Location: "
                        +uploadImagesDTO.getImageName());
                int retryCount =0;
                while (!uploadedSucess && retryCount < 3) {
                    uploadedSucess =  retryImgFromPIMToVendorImageLocation(pimImageFile, uploadImagesDTO);
                    retryCount++ ;
                }
            }
            // Save image Details to DB
            if (uploadedSucess) {
                log.info("ImageFile Uploaded Successfully from PIM uploaded location: " + sourceFile  +
                        " to VendorImage upload location: " +uploadImagesDTO.getVendorImageUploadDir() +
                        uploadImagesDTO.getImageName() +" and PIM Image Id: " + itemImagesData.getPimImageId());
                uploadedSucess = vendorImageManager.saveImageData(uploadImagesDTO);
            } else {
                sendImageUploadFailureNotification("ImageFile " +sourceFile +
                        " from PIM Location is not uploaded into VendorImageUpload location after retry "
                        +uploadImagesDTO.getVendorImageUploadDir() + uploadImagesDTO.getImageName());
            }
        }
        return uploadedSucess;
    }

	private boolean verifyIfImagesExistsinCARSSanLocation(UploadImagesDTO uploadImagesDTO) {  
		if(log.isDebugEnabled()) {
			log.debug("Verify Image in Belk San Location::"+uploadImagesDTO.getVendorImageUploadDir() +"//"+uploadImagesDTO.getImageName());
		}
		try {
			File file=new File(uploadImagesDTO.getVendorImageUploadDir()+uploadImagesDTO.getImageName());
			if(file !=null)
				log.debug("Image is available in Belk San Location::"+uploadImagesDTO.getVendorImageUploadDir() +"//"+uploadImagesDTO.getImageName());
				return true;
		} catch (Exception e) {
			if(log.isErrorEnabled()) {
			 log.error("Image is not available in Belk SAN location");
			}
		}
		return false;
	}

	/**
	 * Set the imageName and Shot type to UploadImagesDTO
	 * 
	 * @param uploadImagesDTO
	 * @param fileExtn
	 * @param pimImagesDTO
	 */
	public void setImageDetails(UploadImagesDTO uploadImagesDTO, String fileExtn, ItemImagesData itemImagesData)
			throws Exception {
		if (log.isDebugEnabled())
			log.debug("setImageDetails method called");
		String vendorImageUploadDir = "";
		String RRDImageUploadedDir = "";
		List<String> imgType = new ArrayList<String>();
		try {
			String imageShotType = itemImagesData.getShotType();
			// Set Image Type to uploadImagesDTO
			if (StringUtils.isNotBlank(imageShotType) && imageShotType.equals("A")) {
				uploadImagesDTO.setShotType(imageShotType);
				imgType.add("MAIN");
				uploadImagesDTO.setImageType(imgType);
			} else if (StringUtils.isNotBlank(imageShotType) && imageShotType.equals("SW")) {
				uploadImagesDTO.setShotType(imageShotType);
				imgType.add("SWATCH");
				uploadImagesDTO.setImageType(imgType);
			} else {
				uploadImagesDTO.setShotType(imageShotType);
				imgType.add("ALT");
				uploadImagesDTO.setImageType(imgType);
			}

			// [VENDOR_NUMBER]_[STYLE_NUMBER]_[A-Z SHOT
			// CODE]_[NRF_COLOR_CODE].[EXT]
			String imageName = uploadImagesDTO.getVendorNumber() + "_" + uploadImagesDTO.getStyleNumber() + "_"
					+ uploadImagesDTO.getShotType() + "_" + uploadImagesDTO.getColorCode() + "." + fileExtn;
			uploadImagesDTO.setImageName(imageName.toUpperCase());
			// Set Vendor Image upload directory
			// /ecommercedev/images/vendor_images/
			// Set RRD image upload directory - Final directory Belkmacl
			vendorImageUploadDir = vendorImageManager.getVendorImageUploadDir();
			uploadImagesDTO.setVendorImageUploadDir(vendorImageUploadDir);
			RRDImageUploadedDir = vendorImageManager.getRRDImageUploadedDir();
			uploadImagesDTO.setRRDImageUploadedDir(RRDImageUploadedDir);

		} catch (Exception e) {
			
			log.error("Unable to read the vendorImageUploadDir property from properties file");
			throw e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.belk.car.app.service.PIMFtpImageManager#uploadOrDeletePimImagesByCar
	 * (com.belk.car.app.model.Car)
	 */
	@Override
	public Map<String, IntegrationResponseData> uploadOrDeletePimImagesByCar(Car car) {
		String uploadStatus = "SUCCESS";
		IntegrationResponseData pimImagesMapResponse = null;
        String isImageChanged = "false";
		try {
			if (car != null && carManager.isPostCutoverCar(car)) {
				log.info("Update or delete image for post cutover car ====>"+car.getCarId());
				car=carManager.getCarById(car.getCarId());

				List<String> locationType = new ArrayList<String>();
				locationType.add("CD");
				Set<VendorSku> skus =  new HashSet<VendorSku>();
				List<String> inputList = new ArrayList<String>();
				boolean ispackColor = isPack(car.getVendorStyles());
				skus=car.getVendorSkus();
				String styleColor = "";
				Map<String, UploadImagesDTO> styleColorImageDto = new HashMap<String, UploadImagesDTO>();
				Iterator<VendorSku> iterator = skus.iterator();

				while (iterator.hasNext()) {
					try {
				         
						VendorSku vendorSku = (VendorSku) iterator.next();
						if (ispackColor) {
							styleColor = vendorSku.getVendorStyle().getVendorNumber() + ":"
									+ vendorSku.getVendorStyle().getVendorStyleNumber() + ":"
									+ vendorSku.getColorCode();
						} else {
							styleColor = String.valueOf(vendorSku.getVendorStyle().getOrinNumber())
									+ vendorSku.getColorCode();
						}
						Vendor vendor=null;
						try {
							vendor = vendorSku.getVendorStyle().getVendor();

						} catch (Exception e) {
							if(log.isDebugEnabled())
							log.debug("initializing the child vendor object"+e);
							Hibernate.initialize(vendorSku.getVendorStyle().getVendor());
						} 
						String colorName = vendorSku.getColorName();
						UploadImagesDTO uploadImagesDTO = new UploadImagesDTO();
						uploadImagesDTO.setCarId(new Long(car.getCarId()));
						uploadImagesDTO.setVendorNumber(vendor.getVendorNumber());
						uploadImagesDTO.setStyleNumber(vendorSku.getVendorStyle().getVendorStyleNumber());
						uploadImagesDTO.setColorCode(vendorSku.getColorCode());
						if (colorName == null)
							colorName = Constants.NO_COLOR;
						String cName = StringUtils.replace(colorName, " ", "_");
						uploadImagesDTO.setColorName(cName);
						uploadImagesDTO.setStyleColor(styleColor);
						styleColorImageDto.put(styleColor, uploadImagesDTO);
						uploadImagesDTO.setImageLocationType(locationType);
						if (!inputList.contains(styleColor))
							inputList.add(styleColor);
						styleColorImageDto.put(styleColor, uploadImagesDTO);
					
					} catch (Exception e) {
						if (log.isErrorEnabled())
						log.error("Message failed to add" + styleColor+":"+e.getMessage());
					}
				}
				if (log.isDebugEnabled())
					log.debug("Is Pack color ::" + ispackColor + "Input::" + inputList);

				Map<String, Map<String, List>> imageTypeamp = null;
				try {
					if (!styleColor.isEmpty()) {
						removeWebServiceErrorMessage(car);
						pimImagesMapResponse = retriveImageDetailsFromPIM(inputList, ispackColor);
						imageTypeamp = parseImageSpecDetails(pimImagesMapResponse);
					}
				} catch (Exception e) {
					if (!isCARSNoteAlreadyExists(car, e.getMessage())) {
						createPimImageFailureCarNotes(car, true, e.getMessage());
					}
					// for web serivce error no need to tell to business
					if (log.isErrorEnabled())
						log.error(WEB_SERVICE_EEROR + ":" + e.getMessage());
				}
				// Process all the added images
				if (imageTypeamp != null) {
					for (Map.Entry<String, Map<String, List>> entry : imageTypeamp.entrySet()) {
						UploadImagesDTO uploadImagesDTO = styleColorImageDto.get(entry.getKey());
						Map<String, List> pimImagesDetails = entry.getValue();

						@SuppressWarnings("unchecked")
						List<ItemImagesData> imagesDatas = pimImagesDetails.get(Constants.PIMIMAGESTAG);
                      
						if (imagesDatas != null && !imagesDatas.isEmpty() && uploadImagesDTO != null
								&& uploadImagesDTO.getStyleColor().equals(entry.getKey())) {
							for (ItemImagesData data : imagesDatas) {
								String note = PIM_UPLOAD_FAILED_IMAGE + data.getPimImageId() + "-"
										+ data.getImgFileName();
								if(data.getPimImageId()>0){
    								try {
    									vendorImage=vendorImageManager.getVendorImageDetailsByPimImageId(String.valueOf(data.getPimImageId()));
    									if(vendorImage !=null && vendorImage.getVendorImagePimId() !=null 
    											&& !(vendorImage.getVendorImageStatus().getName().equalsIgnoreCase("DELETED"))){
    										continue ;
    									} else {
    															
    										if (data != null && data.getImgFileName() !=null ) {
    											
    											 uploadFileFromPIMFTPLocationTOCARSIMageServer(data,
    													uploadImagesDTO,car);
    											 if (isCARSNoteAlreadyExists(car, note)) {
    			                                        removePIMImageErrorMessage(car, false,  data.getPimImageId());
    											 }
    											 isImageChanged="true";
    										}
    									}
    								}  catch (Exception e) {
    									if (!isCARSNoteAlreadyExists(car, note))
    										createPimImageFailureCarNotes(car, true, note);
    									log.error(
    											PIM_UPLOAD_FAILED_IMAGE + data.getPimImageId() + "-"
    													+ data.getImgFileName());
    
    								}
								}	
							}
						} // Process all the deleted images
						@SuppressWarnings("unchecked")
						List<ItemDeletedImagesData> itemDeletedImagesDatas = pimImagesDetails
								.get(Constants.PIMDELETEDIMGTAG);
						if (itemDeletedImagesDatas != null && !itemDeletedImagesDatas.isEmpty() && uploadImagesDTO != null
								&& uploadImagesDTO.getStyleColor().equals(entry.getKey())) {
							for (ItemDeletedImagesData data : itemDeletedImagesDatas) {
							    if(data.getPimImageId()>0){
    								try {
    									
    									vendorImage=vendorImageManager.getVendorImageDetailsByPimImageId(String.valueOf(data.getPimImageId()));
    									if(vendorImage !=null && vendorImage.getVendorImagePimId() !=null
    											&& !(vendorImage.getVendorImageStatus().getName().equalsIgnoreCase("DELETED"))){
    
    									if (data != null && data.getImgFileName() !=null) {
    										
    										deleteFileFromPIMFTPLocation(car, data, uploadImagesDTO);
    										
    										removeDeletedImageHasFailureCarNotes(car, data.getPimImageId());
    										isImageChanged="true";
    
    									}
    									} else {
    										continue ;
    									}
    								} catch (Exception e) {
    									String note = FAILED_TO_REMOVE_DELETED_IMAGE + data.getPimImageId() + "-"
    											+ data.getImgFileName();
    									if (!isCARSNoteAlreadyExists(car, note))
    										createPimImageFailureCarNotes(car, true, note);
    									
    									log.error(
    											FAILED_TO_REMOVE_DELETED_IMAGE + data.getPimImageId() + "-"
    													+ data.getImgFileName(),e);
    
    								}
							    }

							}
						}
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.info("Requested CAR is a Pre-cutover CAR ====>" +car.getCarId());
				}
			}

		} catch (Exception e) {
			createPimImageFailureCarNotes(car, false, "OTHER");
			log.error("GET IMAGE FAILED DUE TO: " + e.getMessage(),e);
		}
		Map<String, IntegrationResponseData> map = new HashMap<String, IntegrationResponseData>();
		map.put(uploadStatus, pimImagesMapResponse);
		if(isImageChanged.equalsIgnoreCase("true")){
		IntegrationResponseData pimimageChanged= new IntegrationResponseData();
		pimimageChanged.setImageChanged("true");
		map.put("imageChanged", pimimageChanged);
		}
		return map;

	}


	/**
	 * The moethod will validate the vendor style is pack or not 
	 * @param car
	 * @return
	 */
	private boolean isPack(List<VendorStyle> styles) {
        boolean isPack=false;
		if (!styles.isEmpty() ) {
		 for(VendorStyle vendorStyle: styles){
		 	if(vendorStyle.getVendorStyleType() !=null && vendorStyle.getVendorStyleType().getName().equalsIgnoreCase(Constants.PRODUCT)) {
			Long tmpOrin = vendorStyle.getOrinNumber();
			if (tmpOrin == null) 
				isPack=true;
		    }
		 }
		}
		return isPack;
	}

	public String deletePimImage(Car car, String pimImageId, User user) {
		String message = "Successfully Deleted the Pim Image ::" + pimImageId;
		if (StringUtils.isBlank(pimImageId)|| pimImageId.equalsIgnoreCase("0")) {
			return message;
		}
		VendorImage vendorImg = vendorImageManager.getVendorImageDetailsByPimImageId(pimImageId);
		Image img = vendorImg.getImage();
		if (null != vendorImg) {

			if (log.isInfoEnabled()) {
				log.info("DELETED the vendor image" + vendorImg.getVendorImageId());
			}
			// If image deleted is pending for Mechanical check, then update the
			// CAR.IMAGE_MC_PENDING_BY_USER flag
			String strPrevVenImageStatus = vendorImg.getVendorImageStatus().getVendorImageStatusCd();
			VendorImageStatus stat_deleted = (VendorImageStatus) carLookupManager.getById(VendorImageStatus.class,
					VendorImageStatus.DELETED);
			img.setStatusCd(Status.DELETED);
			img.setAuditInfo(user);
			vendorImg.setVendorImageStatus(stat_deleted);
			vendorImg.setAuditInfo(user);

			vendorImageManager.save(vendorImg);
			// Delete Removed image from CARS Image Server
			carManager.deleteBelkMaclVendorImage(img);
			if (VendorImageStatus.UPLOADED.equals(strPrevVenImageStatus)
					|| VendorImageStatus.REUPLOADED.equals(strPrevVenImageStatus)
					|| VendorImageStatus.SENT_TO_MQ.equals(strPrevVenImageStatus)) {

				boolean isImagePendingForMc = carManager.checkImageMCPendingByUserFlag(car);
				if (!isImagePendingForMc) {
					car.setImageMCPendingByUser("NONE");
				}
			}

			// Remove current vendor image id from CAR.IMAGE_FAILED_IN_CC column
			String strImageFailedInCC = car.getImagesFailedInCCPending();
			String strVenImageId = String.valueOf(vendorImg.getVendorImageId());
			StringBuffer sb = new StringBuffer();
			boolean isCurrVenImageFailedInCC = strImageFailedInCC == null ? false : strImageFailedInCC
					.contains(strVenImageId);
			if (isCurrVenImageFailedInCC) {
				ArrayList<String> items = new ArrayList<String>(Arrays.asList(strImageFailedInCC.split(",")));
				for (String str : items) {
					if (!str.equals(strVenImageId)) {
						sb.append(str + ",");
					}
				}
				if (sb.toString().endsWith(",")) {
					sb.setLength(sb.length() - 1);
				}
				car.setImagesFailedInCCPending(sb.toString());

			}
		}

		String strBuyerApprovalPending = carManager.checkBuyerApprovalPendingFlag(car);
		// making sure method returns only Y or N
		strBuyerApprovalPending = "Y".equals(strBuyerApprovalPending) ? "Y" : "N";
		car.setBuyerApprovalPending(strBuyerApprovalPending);
		carManager.save(car);

		return message;

	}

	public User getLoggedInUser() {
		User user = null;
		try {
			Authentication auth = ((SecurityContext) SecurityContextHolder.getContext()).getAuthentication();
			if (auth.getPrincipal() instanceof UserDetails) {
				user = (User) auth.getPrincipal();
			}
		} catch (Exception e) {
			if (log.isErrorEnabled()) {

			}
		}
		return user;
	}

	// Add CAR note

	/**
	 * If any image failed while getting from pim needs to be add note
	 * 
	 * @param car
	 * @param displayFlag
	 * @param imageNote
	 */
	private void createPimImageFailureCarNotes(Car car, boolean displayFlag, String imageNote) {
			carManager.addCarNote(car, displayFlag, imageNote, NoteType.PIM_IMAGE_FAIL_NOTES, "caradmin@belk.com");

	}

	private boolean isCARSNoteAlreadyExists(Car car, String message) {
		for (CarNote carNote : car.getCarNotes()) {
			if (carNote.getNoteText().equalsIgnoreCase(message)
					&& carNote.getNoteType().getNoteTypeCd().equalsIgnoreCase(NoteType.PIM_IMAGE_FAIL_NOTES.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * If image is deleted in pim, the repious error note needs to be deleted in
	 * CARS not table
	 * 
	 * @param car
	 * @param pimImageId
	 */
	private void removeDeletedImageHasFailureCarNotes(Car car, Long pimImageId) {
		if (car.getCarNotes() != null) {
			for (CarNote carNote : car.getCarNotes()) {
				if (carNote.getNoteType().equals(NoteType.PIM_IMAGE_FAIL_NOTES)
						&& carNote.getNoteText().contains(String.valueOf(pimImageId))) {
					carNote.setIsExternallyDisplayable("N");
					carNote.setStatusCd("INACTIVE");
					carManager.removeCarNote(car, carNote);
				}
			}
		}

	}
	

	private void removeWebServiceErrorMessage(Car car) {
		if (car.getCarNotes() != null) {
			for (CarNote carNote : car.getCarNotes()) {
				if (carNote.getNoteType().getNoteTypeCd().equalsIgnoreCase(NoteType.PIM_IMAGE_FAIL_NOTES)
						&& carNote.getStatusCd().equalsIgnoreCase("ACTIVE")) {
					carNote.setIsExternallyDisplayable("N");
					carNote.setStatusCd("INACTIVE");
					carManager.removeCarNote(car, carNote);
				}
			}
		}
		
	}

	private void removePIMImageErrorMessage(Car car, boolean b, Long pimImageId) {
		if (car.getCarNotes() != null) {
			for (CarNote carNote : car.getCarNotes()) {
				if (carNote.getNoteType().equals(NoteType.PIM_IMAGE_FAIL_NOTES)
						&& carNote.getNoteText().contains(String.valueOf(pimImageId))
						&& carNote.getStatusCd().equalsIgnoreCase("ACTIVE")) {
					carNote.setIsExternallyDisplayable("N");
					carNote.setStatusCd("INACTIVE");
					carManager.removeCarNote(car, carNote);
				}
			}
		}

	}


	/**
	 * Parse to get image specific data from style or pack color webservice
	 * response
	 * 
	 * @param response
	 * @return
	 */
	public Map<String, Map<String, List>> parseImageSpecDetails(IntegrationResponseData response) {
        if(log.isDebugEnabled()) {
        	log.debug("Image Response ==>"+response);
        }
		Map<String, Map<String, List>> imageTypeamp = new HashMap<String, Map<String, List>>();
		List<ItemCatalogData> itemCatalogDataList = response.getResponseList();
		if (itemCatalogDataList != null) {

			for (ItemCatalogData itemCatalogData : itemCatalogDataList) {
				Map<String, List> imageTypeList = new HashMap<String, List>();
				Boolean isPackColor=false;
				Long styleColorNum = itemCatalogData.getPimEntry().getItemHeaderInformation().getPrimaryKey();
				isPackColor= itemCatalogData.getResponseType().equals(Constants.PACK_COLOR) ;
				EntryDetailsData entryDetailsData = itemCatalogData.getPimEntry().getEntries();
				SecondaryImageSpecsData secondaryImageSpecsData = entryDetailsData.getItemImageSpecData();
				 Long vendorId=entryDetailsData.getItemCatalogSpecData().getSupplierData().getId();
				 String vendorStyleNumber=entryDetailsData.getItemCatalogSpecData().getSupplierData().getSupplierVpn();
				if (secondaryImageSpecsData != null) {
					imageTypeList.put(Constants.PIMIMAGESTAG, secondaryImageSpecsData.getImages());
					imageTypeList.put(Constants.PIMDELETEDIMGTAG, secondaryImageSpecsData.getDeletedImages());
					if(isPackColor){
						 String colorcode=String.valueOf(entryDetailsData.getItemPackColorSpecData().getColorCode());
						 if( colorcode.length()==1) {
							 colorcode="00"+colorcode;
						 } else if( colorcode.length()==2) {
							 colorcode="0"+colorcode;
						 }
							 
					     String PackString=String.valueOf(vendorId)+":"+vendorStyleNumber+":"+colorcode;
						imageTypeamp.put(PackString, imageTypeList);

					} else {
						imageTypeamp.put(String.valueOf(styleColorNum), imageTypeList);

					}
				}

			}
		}

		return imageTypeamp;
	}
	
	/**
     * Parse to get image specific data from group webservice
     * response
     * 
     * @param response
     * @return
     */
    public Map<Long, Map<String, List>> parseImageSpecDetailsForGroups(GroupIntegrationResponseData response) {
        if(log.isDebugEnabled()) {
            log.debug("Image Response ==>"+response);
        }
        Map<Long, Map<String, List>> imageTypeamp = new HashMap<Long, Map<String, List>>();
        List<GroupCatalogData> groupCatalogDataList = response.getResponseList();
        if (groupCatalogDataList != null) {
            for (GroupCatalogData groupCatalogData : groupCatalogDataList) {
                Map<String, List> imageTypeList = new HashMap<String, List>();
                Long groupId = groupCatalogData.getPimEntry().getGroupHeaderInformation().getPrimaryKey();
                GroupEntryDetailsData entryDetailsData = groupCatalogData.getPimEntry().getEntries();
                GroupImageSecSpecData imageSpecsData = entryDetailsData.getGroupImageSecSpecData();
                if (imageSpecsData != null) {
                    imageTypeList.put(Constants.PIMIMAGESTAG, imageSpecsData.getImages());
                    imageTypeList.put(Constants.PIMDELETEDIMGTAG, imageSpecsData.getDeletedImages());
                    imageTypeamp.put(groupId, imageTypeList);
                }

            }
        }
        return imageTypeamp;
    }

    @Override
    public Map<String, GroupIntegrationResponseData> uploadOrDeletePimImagesByCarForGroups(Car car) {
        GroupIntegrationResponseData pimImagesMapResponse = null;

        try {
            if (car != null) {
                log.info("Update or delete image for post cutover car ====>"+car.getCarId());
                
                ArrayList<String> groupIds = new ArrayList<String>();
                String carGroupId = car.getVendorStyle().getGroupId()!=null ? car.getVendorStyle().getGroupId().toString() : null;
                
                boolean isConvertedGrouping = false;
                if (carGroupId!=null) {
                    groupIds.add(carGroupId);
                } else {
                    if (log.isInfoEnabled())log.info("Updating PIM Images for 'Converted' (pre cutover) grouping. Getting Group Images from Vendor Number + Style Number instead.");
                    groupIds.add(car.getVendorStyle().getVendorNumber() +","+ car.getVendorStyle().getVendorStyleNumber());   
                    isConvertedGrouping = true;
                }

                Map<Long, Map<String, List>> imageTypeamp = null;
                try {
                    if (!groupIds.isEmpty()) {
                        removeWebServiceErrorMessage(car);
                        pimImagesMapResponse = getGroupImageDetailsFromSMART(groupIds, isConvertedGrouping);
                        imageTypeamp = parseImageSpecDetailsForGroups(pimImagesMapResponse);
                    } 
                } catch (Exception e) {
                    if (!isCARSNoteAlreadyExists(car, e.getMessage())) {
                        createPimImageFailureCarNotes(car, true, e.getMessage());
                    }
                    // for web serivce error no need to tell to business
                    if (log.isErrorEnabled())
                        log.error(WEB_SERVICE_EEROR + ":" + e.getMessage());
                }
                // Process all the added images
                if (imageTypeamp != null) {
                    if (log.isInfoEnabled())log.info("Images returned from PIM: " + imageTypeamp.size());
                    UploadImagesDTO uploadImagesDTO = new UploadImagesDTO();
                    uploadImagesDTO.setCarId(new Long(car.getCarId()));
                    uploadImagesDTO.setVendorNumber(car.getVendorStyle().getVendorNumber());
                    uploadImagesDTO.setStyleNumber(car.getVendorStyle().getVendorStyleNumber());
                    uploadImagesDTO.setColorCode(Constants.DEFAULT_COLOR_CODE);
                    // patterns have no_color
                    uploadImagesDTO.setColorName(StringUtils.replace(Constants.NO_COLOR, " ", "_"));
                    List<String> locationType = new ArrayList<String>();
                    locationType.add("CD");
                    uploadImagesDTO.setImageLocationType(locationType);
                    
                    for (Map.Entry<Long, Map<String, List>> entry : imageTypeamp.entrySet()) {
                        Map<String, List> pimImagesDetails = entry.getValue();

                        @SuppressWarnings("unchecked")
                        List<GroupImageSecSpecImagesData> imagesDatas = pimImagesDetails.get(Constants.PIMIMAGESTAG);
                        
                        if (imagesDatas != null && !imagesDatas.isEmpty()) {
                            for (GroupImageSecSpecImagesData data : imagesDatas) {
                                if (log.isInfoEnabled())log.info("Processing image data: " + data.getImgFileName());
                                String note = PIM_UPLOAD_FAILED_IMAGE + data.getPimImageId() + "-"
                                        + data.getImgFileName();
                                if(data.getPimImageId()>0){
                                    try {
                                        vendorImage=vendorImageManager.getVendorImageDetailsByPimImageId(String.valueOf(data.getPimImageId()));
                                        if(vendorImage !=null && vendorImage.getVendorImagePimId() !=null 
                                                && !(vendorImage.getVendorImageStatus().getName().equalsIgnoreCase("DELETED"))){
                                            continue ;
                                        } else {
                                            if (data != null && data.getImgFileName() != null) {
                                                if (log.isInfoEnabled())log.info("Downloading file from pim FTP location...");
                                                uploadFileFromPIMFTPLocationTOCARSIMageServerForGrouping(data,
                                                        uploadImagesDTO, car);
                                                if (isCARSNoteAlreadyExists(car, note)) {
                                                    removePIMImageErrorMessage(car, false, data.getPimImageId());
                                                }
                                            }
                                        }
                                    }  catch (Exception e) {
                                        if (!isCARSNoteAlreadyExists(car, note))
                                            createPimImageFailureCarNotes(car, true, note);
                                        log.error(
                                                PIM_UPLOAD_FAILED_IMAGE + data.getPimImageId() + "-"
                                                        + data.getImgFileName());
    
                                    }
                                }  
                            }
                        } // Process all the deleted images
                        @SuppressWarnings("unchecked")
                        List<GroupImageSecSpecDeletedImagesData> deletedImagesDatas = pimImagesDetails
                                .get(Constants.PIMDELETEDIMGTAG);
                        if (deletedImagesDatas != null && !deletedImagesDatas.isEmpty()) {
                            for (GroupImageSecSpecDeletedImagesData data : deletedImagesDatas) {
                                if (log.isInfoEnabled())log.info("Deleting image in CARS: " + data.getImgFileName());
                                if(data.getPimImageId()>0){
                                    try {
                                        vendorImage=vendorImageManager.getVendorImageDetailsByPimImageId(String.valueOf(data.getPimImageId()));
                                        if(vendorImage !=null && vendorImage.getVendorImagePimId() !=null
                                                && !(vendorImage.getVendorImageStatus().getName().equalsIgnoreCase("DELETED"))){
                                            if (data != null && data.getImgFileName() != null) {
                                                deleteFileFromPIMFTPLocationForGrouping(car, data, uploadImagesDTO);
                                                removeDeletedImageHasFailureCarNotes(car, data.getPimImageId());
                                            }
                                        } else {
                                            continue;
                                        }
                                    } catch (Exception e) {
                                        String note = FAILED_TO_REMOVE_DELETED_IMAGE + data.getPimImageId() + "-"
                                                + data.getImgFileName();
                                        if (!isCARSNoteAlreadyExists(car, note)) {
                                            createPimImageFailureCarNotes(car, true, note);
                                        }
                                        
                                        log.error(FAILED_TO_REMOVE_DELETED_IMAGE + data.getPimImageId() + 
                                                "-" + data.getImgFileName(),e);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.info("Requested CAR is a Pre-cutover CAR ====>" +car.getCarId());
                }
            }

        } catch (Exception e) {
            createPimImageFailureCarNotes(car, false, "OTHER");
            log.error("GET IMAGE FAILED DUE TO: " + e.getMessage(),e);
        }
        Map<String, GroupIntegrationResponseData> map = new HashMap<String, GroupIntegrationResponseData>();
        map.put("SUCCESS", pimImagesMapResponse);
        return map;
    }
    
    /**
     *  This method is used to get image details from SMART system by passing
     *  Grouping Id using the new service.
     * @param car  
     * @param List of Grouping Ids
     * @return Map of <GroupId, styleDTO>
     * */
    private GroupIntegrationResponseData getGroupImageDetailsFromSMART(List<String> groupIds, boolean isConvertedGrouping) throws Exception {
        GroupIntegrationRequestData requestData = new GroupIntegrationRequestData();
        GroupIntegrationResponseData response = null;

        // calling new getGroup web service from PIM
        
        if (!isConvertedGrouping) {
            requestData.setInputData(groupIds);
            requestData.setRequestType(GetGroupRequestType.GROUPID.toString());
        } else {
            List<PackGroupRequestData> packDataList = new ArrayList<PackGroupRequestData>();
            for (String groupId : groupIds) {
                String[] vendorNumVendorStyleArr = groupId.split(",");
                if (vendorNumVendorStyleArr.length>=2) {
                    PackGroupRequestData packData = new PackGroupRequestData();
                    packData.setVendorNumber(vendorNumVendorStyleArr[0]);
                    packData.setVendorProductNumber(vendorNumVendorStyleArr[1]);
                    packDataList.add(packData);
                }
            }
            requestData.setInputPackData(packDataList);
            requestData.setRequestType(GetGroupRequestType.VENDORSTYLE.toString());
        }
        
        BelkGroupService integrationService = new BelkGroupService(requestData);
        if (integrationService != null) {
            response = integrationService.getResponse();
        }
        if (response != null && response.getErrorResponseData() == null) {
            return response;
        } 
        else if (response != null && response.getErrorResponseData() != null) {
            throw new Exception(response.getErrorResponseData().getErrorMessage());
        } 
        else {
            throw new Exception(Constants.NO_RESPONSE);
        }
    }
    
    /**
     * download file from pim ftp location and upload to cars image server for
     * groupings
     * 
     * @param imagesData
     * @param uploadImagesDTO
     * @param car
     * @return
     */
    public String uploadFileFromPIMFTPLocationTOCARSIMageServerForGrouping(GroupImageSecSpecImagesData imagesData,
            UploadImagesDTO uploadImagesDTO, Car car) throws Exception {
        String note = PIM_UPLOAD_FAILED_IMAGE + imagesData.getPimImageId() + "-" + imagesData.getImgFileName();
        try {

            boolean isSucess = uploadImageFromPIMLocationForGrouping(uploadImagesDTO, imagesData, car);
            if (isSucess) {
                moveTransferredFilesToArchive(imagesData.getImgFileName());

                return SUCCESS;
            }

        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                if (!isCARSNoteAlreadyExists(car, note))
                    createPimImageFailureCarNotes(car, true, note);
                log.error(PIM_UPLOAD_FAILED_IMAGE + imagesData.getPimImageId() + "-" + imagesData.getImgFileName());

            }
        }
        return IMAGE_UPLOAD_FAIL;
    }

    /**
     * delete file from pim ftp location for groupings
     * 
     * @param car
     * @param deletedImagesData
     * @param dto
     * @return
     */
    public String deleteFileFromPIMFTPLocationForGrouping(Car car,
            GroupImageSecSpecDeletedImagesData deletedImagesData, UploadImagesDTO dto) {
        if (log.isDebugEnabled())
            log.info("Delete " + deletedImagesData.getPimImageId() + ":" + deletedImagesData.getImgFileName()
                    + "From CARS System");
        User user = getLoggedInUser();
        if (user == null)
            user = carManager.getUserForUsername("carsadmin@belk.com");
        return deletePimImage(car, String.valueOf(deletedImagesData.getPimImageId()), user);

    }

    /**
     * uploadImageFromPIMLocation /pimimages/current
     * 
     * @param uploadImagesDTO
     * @param imagesData
     * @param car
     * @return
     * @throws Exception
     */
    private boolean uploadImageFromPIMLocationForGrouping(UploadImagesDTO uploadImagesDTO,
            GroupImageSecSpecImagesData imagesData, Car car) throws Exception {
        if (log.isDebugEnabled())
            log.debug("uploadImageFromPIMLoacation method called");
        boolean uploadedSucess = false;
        InputStream is = null;
        OutputStream os = null;
        String pimImageUploadDir = vendorImageManager.getPimImageUploadDir();
        // uploadImagesDTO.setVendorImageUploadDir(pimImageUploadDir);
        String sourceFile = pimImageUploadDir + "//" + imagesData.getImgFileName();
        File pimImageFile = new File(sourceFile);
        uploadImagesDTO.setPimImageId(imagesData.getPimImageId());

        if (pimImageFile != null) {
            String note = PIM_UPLOAD_FAILED_IMAGE + imagesData.getPimImageId() + "-" + imagesData.getImgFileName();
            uploadImagesDTO.setUserUploadedFileName(imagesData.getImgFileName());
            String[] fileExtn = uploadImagesDTO.getUserUploadedFileName().split("\\.");
            setImageDetailsForGroupings(uploadImagesDTO, fileExtn[(fileExtn.length - 1)], imagesData);
            try {
                is = new FileInputStream(pimImageFile);
                os = new FileOutputStream(uploadImagesDTO.getVendorImageUploadDir() + uploadImagesDTO.getImageName());
                byte[] b = new byte[2048];
                int length;
                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                uploadedSucess = true;
            } catch (FileNotFoundException fne) {
                String filenotFound = "Pim Image ::" + imagesData.getImgFileName()
                        + " is not available in PIM FTP location, Please reset image from PEP";
                if (vendorImage != null
                        && vendorImage.getVendorImagePimId().equalsIgnoreCase(
                                String.valueOf(imagesData.getPimImageId()))
                        && !(vendorImage.getVendorImageStatus().getName().equalsIgnoreCase("DELETED"))) {
                    if (log.isDebugEnabled()) {
                        log.debug("Pim Image" + imagesData.getPimImageId() + "::" + imagesData.getImgFileName()
                                + "is already  uploaded in CARS");
                    }
                } else if (verifyIfImagesExistsinCARSSanLocation(uploadImagesDTO)) {
                    uploadedSucess = true;
                } else {

                    if (!isCARSNoteAlreadyExists(car, filenotFound))
                        createPimImageFailureCarNotes(car, true, filenotFound);
                    log.error(filenotFound);
                }
            } catch (Exception e) {
                if (log.isErrorEnabled()) {
                    if (!isCARSNoteAlreadyExists(car, note))
                        createPimImageFailureCarNotes(car, true, note);
                    log.error(PIM_UPLOAD_FAILED_IMAGE + imagesData.getPimImageId() + "-" + imagesData.getImgFileName());

                }

            } finally {
                try {
                    if (is != null)
                        is.close();
                    if (os != null)
                        os.close();
                } catch (Exception e) {

                    if (log.isErrorEnabled()) {
                        log.error("Error occured while processing the image" + imagesData.getPimImageId() + "-"
                                + imagesData.getImgFileName());
                    }
                }
            }
            // Save image Details to DB
            if (uploadedSucess) {
                uploadedSucess = vendorImageManager.saveImageData(uploadImagesDTO);
            }
        }
        return uploadedSucess;
    }

    /**
     * Set the imageName and Shot type to UploadImagesDTO
     * 
     * @param uploadImagesDTO
     * @param fileExtn
     * @param imagesData
     */
    public void setImageDetailsForGroupings(UploadImagesDTO uploadImagesDTO, String fileExtn,
            GroupImageSecSpecImagesData imagesData) throws Exception {
        if (log.isDebugEnabled())
            log.debug("setImageDetails method called");
        String vendorImageUploadDir = "";
        String RRDImageUploadedDir = "";
        List<String> imgType = new ArrayList<String>();
        try {
            String imageShotType = imagesData.getShotType();
            // Set Image Type to uploadImagesDTO
            if (StringUtils.isNotBlank(imageShotType) && imageShotType.equals("A")) {
                uploadImagesDTO.setShotType(imageShotType);
                imgType.add("MAIN");
                uploadImagesDTO.setImageType(imgType);
            } else if (StringUtils.isNotBlank(imageShotType) && imageShotType.equals("SW")) {
                uploadImagesDTO.setShotType(imageShotType);
                imgType.add("SWATCH");
                uploadImagesDTO.setImageType(imgType);
            } else {
                uploadImagesDTO.setShotType(imageShotType);
                imgType.add("ALT");
                uploadImagesDTO.setImageType(imgType);
            }

            // [VENDOR_NUMBER]_[STYLE_NUMBER]_[A-Z SHOT
            // CODE]_[NRF_COLOR_CODE].[EXT]
            String imageName = uploadImagesDTO.getVendorNumber() + "_" + uploadImagesDTO.getStyleNumber() + "_"
                    + uploadImagesDTO.getShotType() + "_" + uploadImagesDTO.getColorCode() + "." + fileExtn;
            uploadImagesDTO.setImageName(imageName.toUpperCase());
            // Set Vendor Image upload directory
            // /ecommercedev/images/vendor_images/
            // Set RRD image upload directory - Final directory Belkmacl
            vendorImageUploadDir = vendorImageManager.getVendorImageUploadDir();
            uploadImagesDTO.setVendorImageUploadDir(vendorImageUploadDir);
            RRDImageUploadedDir = vendorImageManager.getRRDImageUploadedDir();
            uploadImagesDTO.setRRDImageUploadedDir(RRDImageUploadedDir);

        } catch (Exception e) {

            log.error("Unable to read the vendorImageUploadDir property from properties file");
            throw e;
        }
    }

    /**
     * Retry image from PIM to Cars VI location
     *
     * @param pimImageFile
     * @param uploadImagesDTO
     * @return
     */
    private boolean retryImgFromPIMToVendorImageLocation(File pimImageFile, UploadImagesDTO uploadImagesDTO) {
        log.info("Retrying image logic from Pim to Vendor Image Location");
        boolean isFileAvailable = false;
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(pimImageFile);
            os = new FileOutputStream(uploadImagesDTO.getVendorImageUploadDir() + uploadImagesDTO.getImageName());
            byte[] b = new byte[2048];
            int length;
            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
            is.close();
            os.close();
        } catch (Exception ex) {
            if(log.isErrorEnabled()) {
                log.error("Error occured while retying image file from PIM to Vendor Image location ");
            }
            isFileAvailable = false;
        } finally {
        // close all resources
            try {
                if(is !=null){
                    is.close();
                }
                if(os !=null){
                    os.close();
                }
            } catch (Exception e) {
                if(log.isErrorEnabled()) {
                    log.error("Error occured while closing I/O resources for the image ");
                }
            }
        }

        if (verifyIfImagesExistsinCARSSanLocation(uploadImagesDTO)) {
            isFileAvailable = true;
        }
        return isFileAvailable ;
    }

    /**
     * sendImageUploadFailureNotification e-mail
     *
     * @param content
     */
    private void sendImageUploadFailureNotification(String content) {
        NotificationType type = carLookupManager.getNotificationType(NotificationType.PIM_IMG_UPLOAD_FAIL);

        Config userName = (Config) carLookupManager.getById(Config.class, Config.SYSTEM_USER);
        Config sendNotifications = (Config) carLookupManager.getById(Config.class, Config.SEND_EMAIL_NOTIFICATIONS);
        Config emailNotificationList = (Config) carLookupManager.getById(Config.class, Config.SCHEDULED_PROCESS_ERROR_NOTIFICATION_LIST);
        User systemUser = this.userManager.getUserByUsername(userName.getValue());
        Map model = new HashMap();
        String[] emails = StringUtils.split(emailNotificationList.getValue(), ",;");
        for (String email : emails) {
            model.put("userEmail", email);
            model.put("processName", IMAGE_UPLOAD_FROM_PIM_TO_CARS);
            model.put("exceptionContent", StringUtils.abbreviate(content,4000));
            model.put("executionDate", DateUtils.formatDate(new Date(), "MM/dd/yyyy HH:mm:ss"));
            try {
                if ("true".equalsIgnoreCase(sendNotifications.getValue())) {
                    emailManager.sendNotificationEmail(type, systemUser, model);
                }
            } catch (Exception ex) {
                log.error("General Exception occured in sendImageUploadFailureNotification Cause: " + email);
            }
        }
    }

}
