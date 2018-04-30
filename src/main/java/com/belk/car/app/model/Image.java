package com.belk.car.app.model;

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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.vendorimage.VendorImage;

@Entity
@Table(name = "IMAGE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class Image extends BaseAuditableModel implements java.io.Serializable {

	public static String PROCESSING_STATUS_PENDING = "PENDING" ;
	public static String PROCESSING_STATUS_PROCESSED = "PROCESSED" ;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4552478803491084795L;
	private long imageId;
	private Sample sample;
	private String descr;
	private ImageLocationType imageLocationType;
	private String imageLocationTypeCd;
	private ImageSourceType imageSourceType;
	private String imageSourceTypeCd;
	private ImageType imageType;
	private String imageTypeCd;
	private String imageLocation;
	private String imageFinalUrl;
	private Date requestDate;
	private Date receivedDate;
	private String notesText;
	private String statusCd;
	private String approvalNotesText;
	private ImageTrackingStatus imageTrackingStatus;
	private String imageTrackingStatusCd;
	private String imageProcessingStatusCd = Image.PROCESSING_STATUS_PENDING ;

	private Set<CarImage> carImages = new HashSet<CarImage>(0);
	private VendorImage vendorImage = null;
	
	public Image() {
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IMAGE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "IMAGE_SEQ_GEN", sequenceName = "IMAGE_SEQ", allocationSize = 1)
	@Column(name = "IMAGE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getImageId() {
		return this.imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescription() {
		return this.descr;
	}

	public void setDescription(String descr) {
		this.descr = descr;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAMPLE_ID")
	public Sample getSample() {
		return this.sample;
	}

	public void setSample(Sample sample) {
		this.sample = sample;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_LOCATION_TYPE_CD", nullable = true)
	public ImageLocationType getImageLocationType() {
		return this.imageLocationType;
	}

	public void setImageLocationType(ImageLocationType imageLocationType) {
		this.imageLocationType = imageLocationType;
		if (imageLocationType != null) {
			this.imageLocationTypeCd = imageLocationType.getImageLocationTypeCd();
		}
	}
	
	@Transient
	public String getImageLocationTypeCd() {
		return this.imageLocationTypeCd;
	}

	public void setImageLocationTypeCd(String imageLocationTypeCd) {
		this.imageLocationTypeCd = imageLocationTypeCd;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_TYPE_CD", nullable = false)
	public ImageType getImageType() {
		return this.imageType;
	}

	public void setImageType(ImageType imageType) {
		this.imageType = imageType;
		if(imageType!=null){
			this.imageTypeCd=imageType.getImageTypeCd();
		}
	}
	
	@Transient
	public String getImageTypeCd() {
		return this.imageTypeCd;
	}

	public void setImageTypeCd(String imageTypeCd) {
		this.imageTypeCd = imageTypeCd;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_SOURCE_TYPE_CD", nullable = false)
	public ImageSourceType getImageSourceType() {
		return this.imageSourceType;
	}

	public void setImageSourceType(ImageSourceType imageSourceType) {
		this.imageSourceType = imageSourceType;
		if(imageSourceType!=null){
			this.imageSourceTypeCd=imageSourceType.getImageSourceTypeCd();
		}
	}
	
	@Transient
	public String getImageSourceTypeCd() {
		return this.imageSourceTypeCd;
	}

	public void setImageSourceTypeCd(String imageSourceTypeCd) {
		this.imageSourceTypeCd = imageSourceTypeCd;
	}
	
	@Column(name = "IMAGE_LOCATION", length = 100)
	public String getImageLocation() {
		return this.imageLocation;
	}

	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}

	@Column(name = "IMAGE_FINAL_URL", length = 200)
	public String getImageFinalUrl() {
		return this.imageFinalUrl;
	}

	public void setImageFinalUrl(String imageFinalUrl) {
		this.imageFinalUrl = imageFinalUrl;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REQUEST_DATE", nullable = false, length = 7)
	public Date getRequestDate() {
		return this.requestDate;
	}

	public void setRequestDate(Date requestDate) {
		this.requestDate = requestDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "RECEIVED_DATE", nullable = true, length = 7)
	public Date getReceivedDate() {
		return this.receivedDate;
	}

	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	@Column(name = "NOTES_TEXT", length = 4000)
	public String getNotesText() {
		return this.notesText;
	}

	public void setNotesText(String notesText) {
		this.notesText = notesText;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
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
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "image")
	public Set<CarImage> getCarImages() {
		return this.carImages;
	}

	public void setCarImages(Set<CarImage> carImages) {
		this.carImages = carImages;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_TRACKING_STATUS_CD", nullable = true)
	public ImageTrackingStatus getImageTrackingStatus() {
		return imageTrackingStatus;
	}

	public void setImageTrackingStatus(ImageTrackingStatus imageTrackingStatus) {
		this.imageTrackingStatus = imageTrackingStatus;
		if(imageTrackingStatus!=null){
			this.imageTrackingStatusCd=imageTrackingStatus.getImageTrackingStatusCd();
		}
	}
	
	@Transient
	public String getImageTrackingStatusCd() {
		return this.imageTrackingStatusCd;
	}

	public void setImageTrackingStatusCd(String imageTrackingStatusCd) {
		this.imageTrackingStatusCd = imageTrackingStatusCd;
	}

	@Column(name = "APPROVAL_NOTES_TEXT", nullable = true, length = 2000)
	public String getApprovalNotesText() {
		return approvalNotesText;
	}

	public void setApprovalNotesText(String approvalNotesText) {
		this.approvalNotesText = approvalNotesText;
	}

	@Column(name = "IMAGE_PROCESSING_STATUS_CD", nullable = false, length = 20)
	public String getImageProcessingStatusCd() {
		return this.imageProcessingStatusCd;
	}

	public void setImageProcessingStatusCd(String imageProcessingStatusCd) {
		this.imageProcessingStatusCd = imageProcessingStatusCd;
	}

	@OneToOne(fetch = FetchType.LAZY, optional = true,  mappedBy="image")
	//@PrimaryKeyJoinColumn(name="image")
	public VendorImage getVendorImage() {
		return vendorImage;
	}

	public void setVendorImage(VendorImage vendorImage) {
		this.vendorImage = vendorImage;
	}
}
