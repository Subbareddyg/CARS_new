package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group image spec component information.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Image_Sec_Spec/Scene7_Images.
 */
@XmlRootElement(name = "Scene7_Images")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupImageSecSpecScene7ImagesData {
	
	private long id;
	
	private String imageURL;
	
	private String swatchURL;
	
	private String viewerURL;
	
	private String shotType;
	
	private List<GroupImageSecSpecImagesAuditData> audit;
	
	@XmlElement(name = "Id")
	public long getPimImageId() {
		return id;
	}

	public void setPimImageId(long id) {
		this.id = id;
	}
	
	@XmlElement(name = "ImageURL")
	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}
	
	@XmlElement(name = "SwatchURL")
	public String getSwatchURL() {
		return swatchURL;
	}

	public void setSwatchURL(String swatchURL) {
		this.swatchURL = swatchURL;
	}
	
	@XmlElement(name = "ViewerURL")
	public String getViewerURL() {
		return viewerURL;
	}

	public void setViewerURL(String viewerURL) {
		this.viewerURL = viewerURL;
	}
	
	@XmlElement(name = "Shot_Type")
	public String getShotType() {
		return shotType;
	}

	public void setShotType(String shotType) {
		this.shotType = shotType;
	}
	
	@XmlElement(name = "Audit")
	public List<GroupImageSecSpecImagesAuditData> getAudit() {
		return audit;
	}

	public void setAudit(List<GroupImageSecSpecImagesAuditData> audit) {
		this.audit = audit;
	}
}
