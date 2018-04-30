package com.belk.car.app.dto;

import java.io.Serializable;

/**
 * @author AFUTXD3
 *
 */
public class PimImagesDTO  implements Serializable { 
	private long id;
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

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getUploadedUrl() {
		return uploadedUrl;
	}

	public void setUploadedUrl(String uploadedUrl) {
		this.uploadedUrl = uploadedUrl;
	}

	public String getImageStauts() {
		return imageStauts;
	}

	public void setImageStauts(String imageStauts) {
		this.imageStauts = imageStauts;
	}

	public String getShotType() {
		return shotType;
	}

	public void setShotType(String shotType) {
		this.shotType = shotType;
	}

	public String getImageSource() {
		return imageSource;
	}

	public void setImageSource(String imageSource) {
		this.imageSource = imageSource;
	}

	public String getImgFileName() {
		return imgFileName;
	}

	public void setImgFileName(String imgFileName) {
		this.imgFileName = imgFileName;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	public boolean isSilhouette() {
		return isSilhouette;
	}

	public void setSilhouette(boolean isSilhouette) {
		this.isSilhouette = isSilhouette;
	}

	public String getImageLoacation() {
		return imageLoacation;
	}

	public void setImageLoacation(String imageLoacation) {
		this.imageLoacation = imageLoacation;
	}

	public String getImageDesc() {
		return imageDesc;
	}

	public void setImageDesc(String imageDesc) {
		this.imageDesc = imageDesc;
	}

	public String getArtDirectorNote() {
		return artDirectorNote;
	}

	public void setArtDirectorNote(String artDirectorNote) {
		this.artDirectorNote = artDirectorNote;
	}

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public String getCheckTypeValue() {
		return checkTypeValue;
	}

	public void setCheckTypeValue(String checkTypeValue) {
		this.checkTypeValue = checkTypeValue;
	}

}
