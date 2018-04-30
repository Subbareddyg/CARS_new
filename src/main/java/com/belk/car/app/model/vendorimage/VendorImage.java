/**
 * @author afusy12
 */
package com.belk.car.app.model.vendorimage;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Image;
import com.belk.car.app.model.VendorStyle;

/**
 * @author afuszm1
 *
 */
@Entity
@Table(name = "VENDOR_IMAGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class VendorImage extends BaseAuditableModel implements java.io.Serializable {
	
	
	private long vendorImageId;
	private Image image;
	private VendorStyle vendorStyle;
	private String swatchTypeCd;
	private String colorCode;
	private String colorName;
	private Date mcUploadDate;
	private String isImageOnMC;
	private Date approvedDate;
	private String buyerApproved;
	private VendorImageStatus vendorImageStatus;
	private String vendorImagePimId;
	
	


	private static final long serialVersionUID = -5096216252035320182L;
	private Set<RRDImageCheck> imageChecks = new HashSet<RRDImageCheck>(0);
	
	public VendorImage() {
		mcUploadDate= null;
		isImageOnMC = "N";
		approvedDate = null;
		buyerApproved = "NONE";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDOR_IMAGE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "VENDOR_IMAGE_SEQ_GEN", sequenceName = "VENDOR_IMAGE_SEQ", allocationSize = 1)
	@Column(name = "VENDOR_IMAGE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getVendorImageId() {
		return this.vendorImageId;
	}

	public void setVendorImageId(long vendorImageId) {
		this.vendorImageId = vendorImageId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_STYLE_ID", nullable = false)
	public VendorStyle getVendorStyle() {
		return this.vendorStyle;
	}

	public void setVendorStyle(VendorStyle vendorStyle) {
		this.vendorStyle = vendorStyle;
	}
	
	@Column(name = "SWATCH_TYPE_CD", nullable = false, length = 2)
	public String getSwatchTypeCd() {
		return swatchTypeCd;
	}

	public void setSwatchTypeCd(String swatchTypeCd) {
		this.swatchTypeCd = swatchTypeCd;
	}

	@Column(name = "COLOR_CODE", nullable = false, length = 3)
	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

	@Column(name = "COLOR_NAME", length = 100)
	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "MC_UPLOAD_DATE", length = 7)
	public Date getMcUploadDate() {
		return this.mcUploadDate;
	}

	public void setMcUploadDate(Date mcUploadDate) {
		this.mcUploadDate = mcUploadDate;
	}
	
	@Column(name = "IMAGE_ON_MC", nullable = false, length = 1)
	public String getIsImageOnMC() {
		return this.isImageOnMC;
	}

	public void setIsImageOnMC(String isImageOnMC) {
		this.isImageOnMC = isImageOnMC;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "APPROVED_DATE", length = 7)
	public Date getApprovedDate() {
		return this.approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
	@Column(name = "BUYER_APPROVED", nullable = false, length = 20)
	public String getBuyerApproved() {
		return this.buyerApproved;
	}

	public void setBuyerApproved(String buyerApproved) {
		this.buyerApproved = buyerApproved;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_IMAGE_STATUS_CD", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public VendorImageStatus getVendorImageStatus() {
		return this.vendorImageStatus;
	}

	public void setVendorImageStatus(VendorImageStatus vendorImageStatus) {
		this.vendorImageStatus = vendorImageStatus;
	}
	
	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATE_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
	@Column(name = "VENDOR_IMAGE_PIM_ID", precision = 12, scale = 0)
	public String getVendorImagePimId() {
		return vendorImagePimId;
	}

	public void setVendorImagePimId(String vendorImagePimId) {
		this.vendorImagePimId = vendorImagePimId;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "vendorImage")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public Set<RRDImageCheck> getImageChecks() {
		return imageChecks;
	}

	public void setImageChecks(Set<RRDImageCheck> imageChecks) {
		this.imageChecks = imageChecks;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_ID", nullable = false)
	//@PrimaryKeyJoinColumn(name="imagev",referencedColumnName="imagev")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}


    public String toString(){
    	return " Vendor Image ID: "+ Long.toString(vendorImageId) + "\t buyer Approved : " + buyerApproved;
    }
	
}
