package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "images")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ItemImagesData {
	private long pimImageId;
	private String imageType;
	private String imageUrl;
	private String uploadedUrl;
	private String imageStauts;
	private String shotType;
	private String imageSource;
	private String imgFileName;
	private String uploadedBy;
	private boolean isSilhouette;
	private String imageLoacation;
	private String imageDesc;
	private String artDirectorNote;
	private String checkType;
	private String checkTypeValue;
	@XmlElement(name = "Id")
	public long getPimImageId() {
		return pimImageId;
	}
	public void setPimImageId(long pimImageId) {
		this.pimImageId = pimImageId;
	}
	@XmlElement(name = "Image_Type")
	public String getImageType() {
		return imageType;
	}
	public void setImageType(String imageType) {
		this.imageType = imageType;
	}
	@XmlElement(name = "Image_URL")
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	@XmlElement(name = "Uploaded_URL")
	public String getUploadedUrl() {
		return uploadedUrl;
	}
	public void setUploadedUrl(String uploadedUrl) {
		this.uploadedUrl = uploadedUrl;
	}
	@XmlElement(name = "Status")

	public String getImageStauts() {
		return imageStauts;
	}
	public void setImageStauts(String imageStauts) {
		this.imageStauts = imageStauts;
	}
	@XmlElement(name = "Shot_Type")

	public String getShotType() {
		return shotType;
	}
	public void setShotType(String shotType) {
		this.shotType = shotType;
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
		return imgFileName;
	}
	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}
	@XmlElement(name = "uploaded_by")

	public String getUploadedBy() {
		return uploadedBy;
	}
	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}
	@XmlElement(name = "Silhouette")

	public boolean isSilhouette() {
		return isSilhouette;
	}
	public void setSilhouette(boolean isSilhouette) {
		this.isSilhouette = isSilhouette;
	}
	@XmlElement(name = "Image_Location")

	public String getImageLoacation() {
		return imageLoacation;
	}
	public void setImageLoacation(String imageLoacation) {
		this.imageLoacation = imageLoacation;
	}
	@XmlElement(name = "Description")

	public String getImageDesc() {
		return imageDesc;
	}
	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}
	@XmlElement(name = "ArtDirectorComment")

	public String getArtDirectorNote() {
		return artDirectorNote;
	}
	public void setArtDirectorNote(String artDirectorNote) {
		this.artDirectorNote = artDirectorNote;
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
	
}
