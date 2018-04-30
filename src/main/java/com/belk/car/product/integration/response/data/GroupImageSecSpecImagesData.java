package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group image spec component information.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Image_Sec_Spec/Images.
 */
@XmlRootElement(name = "Images")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupImageSecSpecImagesData {

	private long id;
	
	private String imageType;
	
	private String imageURL;
	
	private String uploadedURL;
	
	private String belkURL;
	
	private String removeImage;
	
	private String uploadedBy;
	
	private String shotType;
	
	private String status;
	
	private String imageSource;
	
	private String fileName;
	
	private String silhouette;
	
	private String imageLocation;
	
	private String description;
	
	private String notes;
	
	private String additionalInfo;
	
	private String artDirectorComment;
	
	private String checkType;
	
	private String checkTypeValue;
	
	private String sampleFlag;
	
	private String viewerURL;
	
	private List<GroupImageSecSpecImagesAuditData> audit;
	
	private List<GroupImageSecSpecImagesSystemData> system;
	
	private String imageURLDescription;
	
	private String artDirectorIndicator;
	
	private List<GroupImageSecSpecImagesRejectedData> rejected;
	
	private String belkLANLink;
	
	@XmlElement(name = "Id")
	public long getPimImageId() {
		return id;
	}

	public void setPimImageId(long id) {
		this.id = id;
	}
	
	@XmlElement(name = "Image_Type")
	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	
	@XmlElement(name = "Image_URL")
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	@XmlElement(name = "Uploaded_URL")
	public String getUploadedURL() {
		return uploadedURL;
	}

	public void setUploadedURL(String uploadedURL) {
		this.uploadedURL = uploadedURL;
	}
	
	@XmlElement(name = "Belk_URL")
	public String getBelkURL() {
		return belkURL;
	}

	public void setBelkURL(String belkURL) {
		this.belkURL = belkURL;
	}
	
	@XmlElement(name = "Remove_Image")
	public String getRemoveImage() {
		return removeImage;
	}

	public void setRemoveImage(String removeImage) {
		this.removeImage = removeImage;
	}
	
	@XmlElement(name = "uploaded_by")
	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	
	@XmlElement(name = "Shot_Type")
	public String getShotType() {
		return shotType;
	}

	public void setShotType(String shotType) {
		this.shotType = shotType;
	}
	
	@XmlElement(name = "Status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@XmlElement(name = "Image_Source")
	public String getImageSource() {
		return imageSource;
	}

	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}
	
	@XmlElement(name = "FileName")
	public String getImgFileName() {
		return fileName;
	}

	public void setImgFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@XmlElement(name = "Silhouette")
	public String getSilhouette() {
		return silhouette;
	}

	public void setSilhouette(String silhouette) {
		this.silhouette = silhouette;
	}
	
	@XmlElement(name = "Image_Location")
	public String getImageLocation() {
		return imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	
	@XmlElement(name = "Description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlElement(name = "notes")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@XmlElement(name = "AdditionalInfo")
	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}
	
	@XmlElement(name = "ArtDirectorComment")
	public String getArtDirectorComment() {
		return artDirectorComment;
	}

	public void setArtDirectorComment(String artDirectorComment) {
		this.artDirectorComment = artDirectorComment;
	}
	
	@XmlElement(name = "CheckType")
	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}
	
	@XmlElement(name = "CheckTypeValue")
	public String getCheckTypeValue() {
		return checkTypeValue;
	}

	public void setCheckTypeValue(String checkTypeValue) {
		this.checkTypeValue = checkTypeValue;
	}
	
	@XmlElement(name = "Sample_Flag")
	public String getSampleFlag() {
		return sampleFlag;
	}

	public void setSampleFlag(String sampleFlag) {
		this.sampleFlag = sampleFlag;
	}
	
	@XmlElement(name = "Viewer_URL")
	public String getViewerURL() {
		return viewerURL;
	}

	public void setViewerURL(String viewerURL) {
		this.viewerURL = viewerURL;
	}	
	
	@XmlElement(name = "Audit")
	public List<GroupImageSecSpecImagesAuditData> getAudit() {
		return audit;
	}

	public void setAudit(List<GroupImageSecSpecImagesAuditData> audit) {
		this.audit = audit;
	}
	
	@XmlElement(name = "System")
	public List<GroupImageSecSpecImagesSystemData> getSystem() {
		return system;
	}

	public void setSystem(List<GroupImageSecSpecImagesSystemData> system) {
		this.system = system;
	}
	
	@XmlElement(name = "image_url_description")
	public String getImageURLDescription() {
		return imageURLDescription;
	}

	public void setImageURLDescription(String imageURLDescription) {
		this.imageURLDescription = imageURLDescription;
	}
	
	@XmlElement(name = "ArtDirectorIndicator")
	public String getArtDirectorIndicator() {
		return artDirectorIndicator;
	}

	public void setArtDirectorIndicator(String artDirectorIndicator) {
		this.artDirectorIndicator = artDirectorIndicator;
	}
	
	@XmlElement(name = "Rejected")
	public List<GroupImageSecSpecImagesRejectedData> getRejected() {
		return rejected;
	}

	public void setRejected(List<GroupImageSecSpecImagesRejectedData> rejected) {
		this.rejected = rejected;
	}
	
	@XmlElement(name = "Belk_LAN_Link")
	public String getBelkLANLink() {
		return belkLANLink;
	}

	public void setBelkLANLink(String belkLANLink) {
		this.belkLANLink = belkLANLink;
	}
}
