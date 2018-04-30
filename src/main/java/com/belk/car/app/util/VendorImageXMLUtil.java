package com.belk.car.app.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarNote;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.ImageSourceType;
import com.belk.car.app.model.ImageTrackingStatus;
import com.belk.car.app.model.NoteType;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.vendorimage.RRDImageCheck;
import com.belk.car.app.model.vendorimage.RRDImageCheckComments;
import com.belk.car.app.model.vendorimage.RRDImageCheckFeedback;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.model.vendorimage.VendorImageStatus;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.VendorImageManager;
import com.belk.car.integrations.rrd.xml.vendorImage.RRDImageValidationXML;
import com.belk.car.integrations.rrd.xml.vendorImage.RRDImageValidationXML.RRDImageXML;
import com.belk.car.integrations.rrd.xml.vendorImage.RRDImageValidationXML.RRDImageXML.Reasons;
import com.belk.car.integrations.rrd.xml.vendorImage.RRDImageValidationXML.RRDImageXML.ReviewComments;
import com.belk.car.integrations.rrd.xml.vendorImage.VendorImageHistoryMessageXML;
import com.belk.car.integrations.rrd.xml.vendorImage.VendorImageHistoryMessageXML.VendorImageHistories.VendorImageXML;
import com.belk.car.integrations.rrd.xml.vendorImage.VendorImageUploadMessageXML;

/**
 * @author AFUSY12
 * This is Utility class for Vendor image XML files
 */
public class VendorImageXMLUtil {
	
	private transient final Log log = LogFactory.getLog(VendorImageXMLUtil.class);
	private VendorImageManager vendorImageManager;
	private CarLookupManager lookupManager;
	private CarManager carManager;
	private UserManager userManager;

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	public void setVendorImageManager(VendorImageManager vendorImageManager) {
		this.vendorImageManager = vendorImageManager;
	}
	public void setLookupManager(CarLookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public void createVendorImageUploadXML(String file) throws Exception{
		if(log.isDebugEnabled()){
			log.debug("generating the XML for RRD");
		}
		
		VendorImageUploadMessageXML uploadMsgXML=new VendorImageUploadMessageXML();
		uploadMsgXML.setFrom("CARS");
		uploadMsgXML.setTo("RRD");
		uploadMsgXML.setType("vendorImagesUpload");
		int iVendorImageCount = 0;
		List<Image> imageList = vendorImageManager.getImagesByVendorImageStatus(new String[]{VendorImageStatus.UPLOADED,VendorImageStatus.REUPLOADED});
		
		if(imageList==null || imageList.isEmpty()){
			log.error("No images found to send to RRD");
			imageList = new ArrayList<Image>(0);
		}else{
			if(log.isInfoEnabled()){
				log.info("Number of todays uploaded/reuploaded images found to send to RRD: "+ imageList.size());
			}

			//Retrieve list of image files available in the ecommerce vendor image upload directory
			String imageUploadDir = vendorImageManager.getVendorImageUploadDir();
			List<String> fileNamesList = getFileListFromDirectory(imageUploadDir);

			for(Image img : imageList){
					String imageName = img.getImageLocation();
					VendorImage viObj = img.getVendorImage();
					boolean imageTransferred = false;
					if (StringUtils.isNotEmpty(imageName) && fileNamesList != null && !fileNamesList.isEmpty()) {
						if (fileNamesList.contains(imageName+Constants.VENDORIMAGE_TRANSFERRED_EXTN)) {
							imageTransferred = true;
						}
						else if (fileNamesList.contains(imageName)) {
							if (log.isInfoEnabled()) {
								log.info("Image "+imageName+" is not yet transferred " +
										"to RRD FTP location, will be processed during the next run of the job.");
							}
							continue;
						}
						else {
							if (log.isInfoEnabled()) {
								log.info("Image "+imageName+" is not available in "+imageUploadDir+
										", will not be processed. Deleting...");
							}
							deleteVendorImage(img);
							//Add a Note to the corresponding CAR ID
							StringBuffer strBufCarNote = new StringBuffer();
							strBufCarNote.append("Unable to send image for Mechanical Check, therefore deleted.");
							strBufCarNote.append("\t Reason: Image unavailable for transfer to RRD FTP location.");
							strBufCarNote.append("\t Image Name: ").append(imageName);
							Car car = vendorImageManager.getCarByVendorImageId(viObj.getVendorImageId());
							carManager.addCarNote(car, true, strBufCarNote.
									toString(), NoteType.CAR_NOTES, "caradmin@belk.com");
							continue;
						}
					}
				try{
					VendorImageUploadMessageXML.VendorImageSummeryXML viSummeryXML =
							new VendorImageUploadMessageXML.VendorImageSummeryXML();
					if(viObj!= null && imageTransferred){
						viSummeryXML.setImageId(viObj.getVendorImageId());
						if(log.isDebugEnabled()){
					    	log.debug("Adding details to XML:"+ viObj);
					    }
						if(VendorImageStatus.UPLOADED.equals(viObj.getVendorImageStatus().getVendorImageStatusCd())){
							viSummeryXML.setStatus("UPLOAD");
						}else if(VendorImageStatus.REUPLOADED.equals(viObj.
								getVendorImageStatus().getVendorImageStatusCd())){
							viSummeryXML.setStatus("REUPLOAD");
						}
						if(ImageSourceType.BUYER_UPLOADED.equals(img.getImageSourceTypeCd())){
							viSummeryXML.setUploadedByUser(UserType.BUYER);
						}else{
							viSummeryXML.setUploadedByUser(UserType.VENDOR);
						}
					    viSummeryXML.setName(img.getImageLocation());
					    uploadMsgXML.getVendorImage().add(viSummeryXML);
					    //viObj.getVendorImageStatus().setName((VendorImageStatus.SENT_TO_MQ));
					    viObj.setVendorImageStatus((VendorImageStatus)lookupManager.
							getById(VendorImageStatus.class, VendorImageStatus.SENT_TO_MQ));
					    vendorImageManager.save(viObj);
					    iVendorImageCount++;
					}else{
						log.error("Vendor image details are missing for image Id: " + img.getImageId());
					}
				 }catch(Exception e){
					 log.error("Exception while adding the vendor image detail to XML ", e);
				 }
			}
		}

		convertVendorImageObjtoXML(file, uploadMsgXML);
		log.info("total number of image details send in XML file are: "+ iVendorImageCount );
	}

	public void createVendorImageUpdateXML(String file, String strLastJobRunDate) throws Exception{
		if(log.isDebugEnabled()){
			log.debug("generating the Vendor image update XML for RRD");
		}
		
		VendorImageUploadMessageXML uploadMsgXML=new VendorImageUploadMessageXML();
		uploadMsgXML.setFrom("CARS");
		uploadMsgXML.setTo("RRD");
		uploadMsgXML.setType("vendorImagesUpdate");
		int iVendorImageCount = 0;
		//List<Image> imageList = null; 
		List<Image> imageList = vendorImageManager.getRemovedVendorImages(strLastJobRunDate);
		
		if(imageList==null || imageList.isEmpty()){
			log.error("No images found to send to RRD");
			imageList = new ArrayList<Image>(0);
		}else{
			if(log.isInfoEnabled()){
				log.info("Number of todays removed images found : "+ imageList.size());
			}
			for(Image img : imageList){
				try{
					VendorImageUploadMessageXML.VendorImageSummeryXML viSummeryXML =new VendorImageUploadMessageXML.VendorImageSummeryXML();
					VendorImage viObj = img.getVendorImage();
					if(viObj!= null && img.getImageLocation()!= null){
						viSummeryXML.setImageId(viObj.getVendorImageId());
						if(log.isDebugEnabled()){
					    	log.debug("Adding details to XML:"+ viObj);
					    }
						if(VendorImageStatus.DELETED.equals(viObj.getVendorImageStatus().getVendorImageStatusCd())){
							viSummeryXML.setStatus("DELETED");
						}else if("APPROVED".equals(viObj.getBuyerApproved())){
							viSummeryXML.setStatus("APPROVED");
						}else if("REJECTED".equals(viObj.getBuyerApproved())){
							viSummeryXML.setStatus("REJECTED");
						}
						if( ImageSourceType.VENDOR_UPLOADED.equals(img.getImageSourceTypeCd())){
							viSummeryXML.setUploadedByUser(UserType.VENDOR);
						}else if( ImageSourceType.BUYER_UPLOADED.equals(img.getImageSourceTypeCd())){
							viSummeryXML.setUploadedByUser(UserType.BUYER);
						}
					    viSummeryXML.setName(img.getImageLocation());
					    uploadMsgXML.getVendorImage().add(viSummeryXML);
					    iVendorImageCount++;
					}else{
						log.error("Vendor image details are missing for image Id: " + img.getImageId());
					}
				}catch(Exception e){
					log.error("Exception while adding the image info to XML", e);
				}
			}
		}
		
		convertVendorImageObjtoXML(file, uploadMsgXML);
		log.info("total number of REMOVED image details send in XML file are: "+ iVendorImageCount );
	}
	
	private void convertVendorImageObjtoXML(String file,
			VendorImageUploadMessageXML uploadMsgXML) throws JAXBException,
			PropertyException, IOException, Exception {
		Writer w = null;
		try{
			JAXBContext context = JAXBContext.newInstance(VendorImageUploadMessageXML.class);
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			//m.marshal(uploadMsgXML, System.out);
			w = new FileWriter(file);
			m.marshal(uploadMsgXML, w);
		}catch(Exception ex){
			log.error("Exception while Marshalling the Object to XML file", ex);
			throw ex;
		}
		finally{
			w.close();
		}
	}
	
	public void readAndProcessVendorImageHistoryFeed(File f) throws JAXBException,
	IOException, PropertyException, Exception {
		
		try{
			log.info("Processing the XML file "+  f.getName());
			
			JAXBContext context = JAXBContext.newInstance(VendorImageHistoryMessageXML.class);
			// get variables from xml file and  persist all
			Unmarshaller um = context.createUnmarshaller();
			VendorImageHistoryMessageXML historyMessage = (VendorImageHistoryMessageXML) um.unmarshal(new FileReader(f));
			
			//find the image and update the status to received
			VendorImage vimg = null;
			Image img = null;
			int iprocessedImages = 0;
			Date currDate = new Date();
			VendorImageStatus vendorImageReceived = (VendorImageStatus)lookupManager.getById(VendorImageStatus.class, VendorImageStatus.RECEIVED);
			ImageTrackingStatus imageReceived = (ImageTrackingStatus)lookupManager.getById(ImageTrackingStatus.class, ImageTrackingStatus.RECEIVED);
			
			
			if(historyMessage.getVendorImageHistories() != null ){
				List<VendorImageXML> vImageXmlList =  historyMessage.getVendorImageHistories().getVendorImageXML();
				if(log.isInfoEnabled()){
					log.info("Number of  image names found in XML file : "+vImageXmlList.size());
				}
				for(VendorImageXML vImageXml: vImageXmlList){
					try{
						long id = vImageXml.getId();
						if(log.isInfoEnabled()){
							log.info("Processing vendor image id : "+id);
						}
						//vimg = (VendorImage)lookupManager.getById(VendorImage.class, id);
						vimg = vendorImageManager.getVendorImageDetailsById(id);
						if(vimg != null){
							
							//todo update the image status to received
							vimg.setVendorImageStatus(vendorImageReceived);
							vimg.setUpdatedDate(currDate);
							
							img = vimg.getImage();
							img.setImageTrackingStatus(imageReceived);
							img.setReceivedDate(currDate);
							img.setUpdatedDate(currDate);
							
							vendorImageManager.save(vimg);
							vendorImageManager.save(img);
							iprocessedImages++;
							
							if(log.isDebugEnabled()){
								log.debug("received vendor image  :"+vImageXml.getName());
							}
						}else{
							log.error("Specified image doesnot exists vendor_Image_id:"+ id);
						}
					}catch(Exception e){
						log.error("error while processing the VendorImage ",e);
					}
				}
			}else{
				log.error("XML file is empty, nothing to process");
			}
			
			log.info("total vendor images processed : "+ iprocessedImages);
			
		 }catch(IOException ioe){
			log.error("error while reading the image history feed: File not accesible", ioe);
			throw ioe;
		}
		catch(JAXBException jae){
			log.error("error while reading the image history feed: XML file format incorrect", jae);
			throw jae;
		}
		catch(Exception ex){
			log.error("error while processing image history feed in readAndProcessVendorImageHistoryFeed", ex);
			throw ex;
		}
		
	}
	
	public void readAndProcessVendorImageCheckFeedbackDetails(File f) throws JAXBException,
	IOException, PropertyException, Exception {
		
		try{
			log.info("Processing the XML file "+  f.getName());
			
			JAXBContext context = JAXBContext.newInstance(RRDImageValidationXML.class);
			// get variables from xml file and  persist all
			Unmarshaller um = context.createUnmarshaller();
			RRDImageValidationXML imageValidationXML = (RRDImageValidationXML) um.unmarshal(new FileReader(f));
			User systemUser = this.userManager.getUserByUsername("carsadmin@belk.com");
			NoteType carNoteType = (NoteType) lookupManager.getById(NoteType.class, NoteType.CAR_NOTES);
			RRDImageCheck imageCheckRRD=null;
			RRDImageCheckComments imaageCheckComments=null;
			RRDImageCheckFeedback imageCheckFeedback=null;
			Set<RRDImageCheckFeedback> imageCheckFeedbackSet = null;
			Set<RRDImageCheckComments> imageCheckCommentsSet = null;
			
			
			VendorImage vImg = null;
			//List<Car> car=null;
			Car carDetail=null;
			boolean isCarUpdated = false;
			int iprocessedImages = 0;
			Date currDate = new Date();
			StringBuffer strBufRejectionReasons = new StringBuffer();
				
			if(null != imageValidationXML.getImage()){
				List<RRDImageXML> imageList =  imageValidationXML.getImage();
				if(log.isInfoEnabled()){
					log.info("Number of  image found in XML file : "+imageList.size());
				}
				for(RRDImageXML imageXML: imageList){
					try{
						long id = imageXML.getImageId();
						if(log.isInfoEnabled()){
							log.info("Processing vendor image id : "+id);
						}
						
						vImg = vendorImageManager.getVendorImageDetailsById(id);
						//vImg = (VendorImage)lookupManager.getById(VendorImage.class, id);
						carDetail =vendorImageManager.getCarByVendorImageId(id);
						if (null !=vImg  && null != carDetail) {
							imageCheckRRD = new RRDImageCheck();
							imageCheckFeedbackSet = new HashSet<RRDImageCheckFeedback>();
							imageCheckCommentsSet = new HashSet<RRDImageCheckComments>();
							imageCheckRRD.setVendorImage(vImg);
							imageCheckRRD.setCheckType(imageXML.getCheck()
									.getType().toUpperCase());
							imageCheckRRD.setCheckStatus(imageXML.getCheck()
									.getValue().toUpperCase());
							
							imageCheckRRD.setCreatedDate(currDate);
							imageCheckRRD.setUpdatedDate(currDate);
							// set feed back object to image check
							if (null != imageXML.getReasons()) {
								strBufRejectionReasons = new StringBuffer();
								Reasons failuerReasonXML=imageXML.getReasons();
								List<String> reasons = failuerReasonXML.getReason();
								if(reasons!=null){
									for(String reasonStr: reasons){
										if(null !=reasonStr && reasonStr.length() > 0){
											imageCheckFeedback = new RRDImageCheckFeedback();
											imageCheckFeedback.setFeedback(reasonStr);
											imageCheckFeedback.setRrdImageCheck(imageCheckRRD);
											imageCheckFeedback.setCreatedDate(currDate);
											imageCheckFeedback.setUpdatedDate(currDate);
											imageCheckFeedbackSet.add(imageCheckFeedback);
											strBufRejectionReasons.append(reasonStr +", ");
										}
									}
								}
								if(!imageCheckFeedbackSet.isEmpty())
									imageCheckRRD.setImageCheckFeedbackSet(imageCheckFeedbackSet);
							}
							// set comment object to image check

							if (null != imageXML.getReviewComments()) {
								ReviewComments reviewCommentsXML =imageXML.getReviewComments();
								List<String> comments =null;
								if((comments=reviewCommentsXML.getComment()) != null){
									for(String commentStr :comments){
										if(null != commentStr && commentStr.length() > 0){
											imaageCheckComments = new RRDImageCheckComments();
											imaageCheckComments.setComments(commentStr);
											imaageCheckComments.setRrdImageCheck(imageCheckRRD);
											imaageCheckComments.setCreatedDate(currDate);
											imaageCheckComments.setUpdatedDate(currDate);
											imageCheckCommentsSet.add(imaageCheckComments);
										}
									}
								}
								if(!imageCheckCommentsSet.isEmpty())
									imageCheckRRD.setImageCheckCommentSet(imageCheckCommentsSet);
							}
							// Save imageCheckRRD object
							String checkType=imageCheckRRD.getCheckType();
							String checkStatus=imageCheckRRD.getCheckStatus();
							String userImageUpload=null;
							WorkflowStatus currentWorkFlowStatus=null;
							UserType assignedToUserType=null;
							if(ImageSourceType.BUYER_UPLOADED.equalsIgnoreCase(vImg.getImage().getImageSourceType().getImageSourceTypeCd())){
								userImageUpload=UserType.BUYER;
							}else if(ImageSourceType.VENDOR_UPLOADED.equalsIgnoreCase(vImg.getImage().getImageSourceType().getImageSourceTypeCd())){
								userImageUpload=UserType.VENDOR;
							}
							if(checkType != null && checkStatus != null){
								if((RRDImageCheck.CONDITIONAL_APPROVAL .equalsIgnoreCase(checkStatus) ||
										RRDImageCheck.APPROVE.equalsIgnoreCase(checkStatus) ||RRDImageCheck.RETOUCH.equalsIgnoreCase(checkStatus)
										||RRDImageCheck.CONDITIONAL_APPROVAL_RETOUCH.equalsIgnoreCase(checkStatus))&& RRDImageCheck.CREATIVE.equalsIgnoreCase(checkType)){
										vImg.setVendorImageStatus((VendorImageStatus) this.lookupManager.getById(VendorImageStatus.class, VendorImageStatus.CREATIVE_PASS));
								}else if(RRDImageCheck.MECHANICAL.equalsIgnoreCase(checkType) && RRDImageCheck.PASS.equalsIgnoreCase(checkStatus)){
										vImg.setVendorImageStatus((VendorImageStatus) this.lookupManager.getById(VendorImageStatus.class, VendorImageStatus.MQ_PASSED));
										vImg.setIsImageOnMC("Y");
										//Set MC Uploaded Date
										vImg.setMcUploadDate(new Date());
										// Remove the vendor image id from CC image failed list
										if(null != carDetail.getImagesFailedInCCPending() && carDetail.getImagesFailedInCCPending().length() > 0){
											 String strImageFailedInCC=carDetail.getImagesFailedInCCPending();  
											 String strVenImageId = String.valueOf(vImg.getVendorImageId());
											 StringBuffer sb=new StringBuffer();
											 boolean isCurrVenImageFailedInCC = strImageFailedInCC == null? false:strImageFailedInCC.contains(strVenImageId);
											 if(isCurrVenImageFailedInCC){
						                         ArrayList<String> items = new ArrayList<String>(Arrays.asList(strImageFailedInCC.split(",")));                                          
						                         for(String str:items){
						                        	 if(!str.equals(strVenImageId)){
						                        		 sb.append(str+",");
						                        	 }
						                         }
						                         if(sb.toString().endsWith(",")) {
						          	              	sb.setLength(sb.length() - 1);  
						          	             }
						                         carDetail.setImagesFailedInCCPending(sb.toString());
						                         //ImagesFailedInCCPending value holds the VENDOR_IMAGE_ID's separated 
						                         //by Comma which are failed in Creative Check are Not on MC.
						                         isCarUpdated=true;
										}
									}
								}else if(RRDImageCheck.REJECT.equalsIgnoreCase(checkStatus) && RRDImageCheck.CREATIVE.equalsIgnoreCase(checkType)){
										vImg.setVendorImageStatus((VendorImageStatus) this.lookupManager.getById(VendorImageStatus.class, VendorImageStatus.CREATIVE_FAILED));
										currentWorkFlowStatus =(WorkflowStatus) this.lookupManager.getById(WorkflowStatus.class, WorkflowStatus.IMAGE_FAILED_IN_CC);
										if(null != carDetail.getImagesFailedInCCPending() && carDetail.getImagesFailedInCCPending().length() > 0){
											carDetail.setImagesFailedInCCPending(carDetail.getImagesFailedInCCPending()+","+vImg.getVendorImageId());
										}else{
											carDetail.setImagesFailedInCCPending(vImg.getVendorImageId()+"");
										}
										isCarUpdated=true;
								}else if(RRDImageCheck.MECHANICAL.equalsIgnoreCase(checkType) &&  RRDImageCheck.FAIL.equalsIgnoreCase(checkStatus)){
										vImg.setVendorImageStatus((VendorImageStatus) this.lookupManager.getById(VendorImageStatus.class, VendorImageStatus.MQ_FAILED));
										currentWorkFlowStatus = (WorkflowStatus) this.lookupManager.getById(WorkflowStatus.class, WorkflowStatus.IMAGE_FAILED_IN_MC);
										isCarUpdated=true;
								}
							}	
							vImg.setUpdatedDate(currDate);
							vendorImageManager.save(imageCheckRRD);
							vendorImageManager.save(vImg);
							if(RRDImageCheck.REJECT.equalsIgnoreCase(checkStatus) || RRDImageCheck.FAIL.equalsIgnoreCase(checkStatus)){
								//add a not in CAR
								StringBuffer strBufCarNote = new StringBuffer();
								strBufCarNote.append("Type of failure: ").append(checkType).append(" CHECK");
								strBufCarNote.append(",");
								strBufCarNote.append("\t Reason: ").append(strBufRejectionReasons.toString());
								strBufCarNote.append("\t Image Name: ").append(vImg.getImage().getImageLocation());
								
								CarNote note = new CarNote();
								note.setNoteType(carNoteType);
								note.setIsExternallyDisplayable(CarNote.FLAG_NO);
								note.setStatusCd(Status.ACTIVE);
								note.setCar(carDetail);
								note.setNoteText(strBufCarNote.toString());
								note.setAuditInfo(systemUser);
								carDetail.getCarNotes().add(note);
								
								if (userImageUpload != null){
									assignedToUserType = (UserType) this.lookupManager.getById(UserType.class,userImageUpload);
								}
								isCarUpdated=true;
							}
							Car car = (Car)vendorImageManager.getCarByVendorImageId(id);
							if(RRDImageCheck.MECHANICAL.equalsIgnoreCase(checkType)  && !carManager.checkImageMCPendingByUserFlag(car)){
								log.info(" Setting the CAR.IMAGE_MC_PENDING_BY_USER To NONE: " +carDetail.getCarId());
								carDetail.setImageMCPendingByUser("NONE");
								isCarUpdated = true;
							}
							if(RRDImageCheck.MECHANICAL.equalsIgnoreCase(checkType) &&  RRDImageCheck.FAIL.equalsIgnoreCase(checkStatus)){
								String strBuyerApprovalPending = carManager.checkBuyerApprovalPendingFlag(car);
								// 	making sure method returns only Y or N
								strBuyerApprovalPending = "Y".equals(strBuyerApprovalPending)? "Y" : "N";
								log.info(" Setting the CAR buyer approval pending To : " +strBuyerApprovalPending+ "for CAR : "+carDetail.getCarId());
								carDetail.setBuyerApprovalPending(strBuyerApprovalPending);
								isCarUpdated = true;
							}
							if(isCarUpdated){
								carDetail.setAuditInfo(systemUser);
							}
							if (currentWorkFlowStatus !=null) {
								carDetail.setCurrentWorkFlowStatus(currentWorkFlowStatus);
							}
							if (assignedToUserType!=null) {
								carDetail.setAssignedToUserType(assignedToUserType);
							}
							vendorImageManager.save(carDetail);
							iprocessedImages++;
						}
						if(log.isDebugEnabled()){
							log.debug("received vendor image Id  :"+ id);
						}
					}catch(Exception e){
						log.error("error while processing the VendorImage ",e);
					}
				}
			}else{
				log.error("XML file is empty, nothing to process");
			}
			
			log.info("total vendor images processed : "+ iprocessedImages);
			
		 }catch(IOException ioe){
			log.error("error while reading the RRD Creative Check Feedback details: File not accesible", ioe);
			throw ioe;
		}
		catch(JAXBException jae){
			log.error("error while reading the RRD Creative Check Feedback details: XML file format incorrect", jae);
			throw jae;
		}
		catch(Exception ex){
			log.error("error while processing RRD Creative Check Feedback details in readAndProcessCreativeCheckFeedbackDetails", ex);
			throw ex;
		}
		
	}

	/**
	 * Changes the status of the vendor image to DELETED and
	 * takes care of other dependencies on the deleted vendor image.
	 * @param image
	 */
	public void deleteVendorImage(Image image) {
		VendorImage vi = image.getVendorImage();
		Car car = vendorImageManager.getCarByVendorImageId(vi.getVendorImageId());
		User user=carManager.getUserForUsername("carsadmin@belk.com");
		String strPrevVenImageStatus = vi.getVendorImageStatus().getVendorImageStatusCd();
		VendorImageStatus stat_deleted = (VendorImageStatus) lookupManager.getById(
				VendorImageStatus.class,VendorImageStatus.DELETED);
		image.setStatusCd(Status.DELETED);
		image.setAuditInfo(user);
		vi.setVendorImageStatus(stat_deleted);
		vi.setAuditInfo(user);

		vendorImageManager.save(vi);

		if (log.isInfoEnabled()) {
			log.info("Image "+image.getImageLocation()+" has been deleted.");
		}

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
		String strVenImageId = String.valueOf(vi.getVendorImageId());
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

		String strBuyerApprovalPending = carManager.checkBuyerApprovalPendingFlag(car);
		// making sure method returns only Y or N
		strBuyerApprovalPending = "Y".equals(strBuyerApprovalPending) ? "Y" : "N";
		car.setBuyerApprovalPending(strBuyerApprovalPending);
		carManager.save(car);
	}

	/**
	 * Returns the list of file names available in a directory.
	 * @param imageUploadDir
	 */
	public List<String> getFileListFromDirectory(String imageUploadDir){
		File dir = new File(imageUploadDir);
		String[] fileNames = null;
		fileNames = dir.list();
		List<String> fileNamesList = Arrays.asList(fileNames);
		return fileNamesList;
	}

}
