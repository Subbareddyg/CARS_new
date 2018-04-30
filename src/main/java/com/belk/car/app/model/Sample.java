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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.util.DateUtils;

@Entity
@Table(name = "SAMPLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class Sample extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3329802686391197288L;
	private long sampleId;
	private SampleType sampleType;
	private SampleSourceType sampleSourceType;
	private String swatchColor;
	private String associatedWithSku;
	private String isReturnable;
	private String silhoutteRequired;
	private Date expectedShipDate ;
	private SampleTrackingStatus sampleTrackingStatus;
	private ShippingType shippingType;
	private String shipperAccountNumber;
	private ImageProvider imageProvider ;
	private String colorName ;
	private VendorStyle vendorStyle ;

	private String statusCd;

	private Set<CarSample> carSamples = new HashSet<CarSample>(0);

	private Set<SampleShippingInformation> sampleShippingInformations = new HashSet<SampleShippingInformation>(
			0);

	private Set<Image> images = new HashSet<Image>(0);
	
	private MediaCompassImage mediaCompassImage = null;

	public Sample() {
	}

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="SHIPPING_TYPE_CD", nullable=false)
    public ShippingType getShippingType() {
        return this.shippingType;
    }
    
    public void setShippingType(ShippingType shippingType) {
        this.shippingType = shippingType;
    }
    
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sample")
	public Set<CarSample> getCarSamples() {
		return this.carSamples;
	}

	public void setCarSamples(Set<CarSample> carSamples) {
		this.carSamples = carSamples;
	}

	@Transient
	public String getIsSample() {
		if (this.getSampleType() != null
			&& SampleType.PRODUCT.equals(this.getSampleType().getSampleTypeCd())) {
			return "Y";
		} else {
			return "N";
		}
	}

	@Transient
	public String getIsSwatch() {
		if (this.getSampleType() != null
			&& SampleType.SWATCH.equals(this.getSampleType().getSampleTypeCd())) {
			return "Y";
		} else {
			return "N";
		}
	}

	@Transient
	public String getIsNeither() {
		if (this.getIsSwatch().equals("N") && this.getIsSample().equals("N")) {
			return "Y";
		} else {
			return "N";
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SAMPLE_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "SAMPLE_SEQ_GEN", sequenceName = "SAMPLE_SEQ", allocationSize = 1)
	@Column(name = "SAMPLE_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getSampleId() {
		return this.sampleId;
	}

	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAMPLE_TYPE_CD", nullable = false)
	public SampleType getSampleType() {
		return this.sampleType;
	}

	public void setSampleType(SampleType sampleType) {
		this.sampleType = sampleType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAMPLE_SOURCE_TYPE_CD", nullable = false)
	public SampleSourceType getSampleSourceType() {
		return this.sampleSourceType;
	}

	public void setSampleSourceType(SampleSourceType sampleSourceType) {
		this.sampleSourceType = sampleSourceType;
	}
	@Column(name = "SHIPPER_ACCOUNT_NUMBER", length = 100)
	public String getShipperAccountNumber() {
		return this.shipperAccountNumber;
	}

	public void setShipperAccountNumber(String shipperAccountNumber) {
		this.shipperAccountNumber = shipperAccountNumber;
	}
	@Column(name = "SWATCH_COLOR", length = 100)
	public String getSwatchColor() {
		return this.swatchColor;
	}

	public void setSwatchColor(String swatchColor) {
		this.swatchColor = swatchColor;
	}

	@Column(name = "ASSOCIATED_WITH_SKU", length = 100)
	public String getAssociatedWithSku() {
		return this.associatedWithSku;
	}

	public void setAssociatedWithSku(String associatedWithSku) {
		this.associatedWithSku = associatedWithSku;
	}

	@Column(name = "IS_RETURNABLE", nullable = false, length = 1)
	public String getIsReturnable() {
		return this.isReturnable;
	}

	public void setIsReturnable(String isReturnable) {
		this.isReturnable = isReturnable;
	}

	@Column(name = "SILHOUETTEREQ", nullable = false, length = 1)
	public String getSilhoutteRequired() {
		return silhoutteRequired;
	}
	
	public void setSilhoutteRequired(String silhoutteRequired) {
		this.silhoutteRequired = silhoutteRequired;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sample")
	public Set<SampleShippingInformation> getSampleShippingInformations() {
		return this.sampleShippingInformations;
	}

	public void setSampleShippingInformations(
			Set<SampleShippingInformation> sampleShippingInformations) {
		this.sampleShippingInformations = sampleShippingInformations;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "sample")
	public Set<Image> getImages() {
		return this.images;
	}

	public void setImages(Set<Image> images) {
		this.images = images;
	}

	@Column(name = "EXPECTED_SHIP_DATE", nullable = true, length = 7)
	public Date getExpectedShipDate() {
		return expectedShipDate;
	}

	public void setExpectedShipDate(Date expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_PROVIDER_ID", nullable = false)
	public ImageProvider getImageProvider() {
		return imageProvider;
	}

	public void setImageProvider(ImageProvider imageProvider) {
		this.imageProvider = imageProvider;
	}
	@Transient
	public int getSampleDisplayStatus(){
		if (this.getSampleSourceType().getSampleSourceTypeCd().equals("ON_HAND")&&
			this.getSampleType().getSampleTypeCd().equals("PRODUCT")) return 0;

		if (this.getSampleSourceType().getSampleSourceTypeCd().equals("ON_HAND")&&
			this.getSampleType().getSampleTypeCd().equals("SWATCH")) return 1;

		if (this.getSampleSourceType().getSampleSourceTypeCd().equals("VENDOR")&&
			this.getSampleType().getSampleTypeCd().equals("PRODUCT")) return 2;

		if (this.getSampleSourceType().getSampleSourceTypeCd().equals("VENDOR")&&
			this.getSampleType().getSampleTypeCd().equals("SWATCH")) return 3;

		return 4;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SAMPLE_TRACKING_STATUS_CD", nullable = true)
	public SampleTrackingStatus getSampleTrackingStatus() {
		return sampleTrackingStatus;
	}

	public void setSampleTrackingStatus(SampleTrackingStatus sampleTrackingStatus) {
		this.sampleTrackingStatus = sampleTrackingStatus;
	}

	@Transient	
	public String getExpectedShipDateFormatted(){
    	if (this.getExpectedShipDate()==null){
    		return "";
    	}
        return DateUtils.formatDate(this.getExpectedShipDate());
	}
    
    @Transient
    public String getColorName() {
    	return this.colorName;
    }
    
    public void setColorName(String colorName) {
    	this.colorName = colorName;
    }

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "VENDOR_STYLE_ID", nullable = false)
	public VendorStyle getVendorStyle() {
		return vendorStyle;
	}

	public void setVendorStyle(VendorStyle vendorStyle) {
		this.vendorStyle = vendorStyle;
	}
	
	@OneToOne(fetch = FetchType.LAZY, mappedBy = "sample", optional = true)
	public MediaCompassImage getMediaCompassImage() {
		return mediaCompassImage;
	}

	
	public void setMediaCompassImage(MediaCompassImage mediaCompassImage) {
		this.mediaCompassImage = mediaCompassImage;
	}
	
}
