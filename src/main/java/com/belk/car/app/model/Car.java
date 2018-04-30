package com.belk.car.app.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

import org.apache.commons.collections.comparators.ReverseComparator;
import org.appfuse.model.User;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import com.belk.car.app.model.vendorimage.VendorImage;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.util.DateUtils;
import com.belk.car.util.GenericComparator;
import com.belk.car.util.NotesComparator;

@Entity
@Table(name = "CAR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class Car extends BaseAuditableModel implements java.io.Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9122113839144942157L;

	public static final int NUM_DAYS_TO_COMPLETION = 45; 
	
	private long carId;
	private User carUserByAssignedToUserId;
	private Department department;
	private User carUserByLoggedByUserId;
	private WorkFlow WorkFlow;
	private VendorStyle vendorStyle;
	private SourceType sourceType;
	private String sourceId;
	private Date dueDate;
	private Date escalationDate;
	private Date expectedShipDate;
	private WorkflowStatus currentWorkFlowStatus;
	private UserType assignedToUserType;
	private String statusCd;
	private String isProductTypeRequired;
	private String isUrgent;
	private ContentStatus contentStatus ;
	private Date lastWorkflowStatusUpdateDate;
	private Date turninDate;
	private String archive = "N";
	private String lock = "N";
	
	//Added variables for CAR_QUALITY report 
	private String carQualityCode="N";
	private int rejectionCount= -1;
	
	private Set<CarImage> carImages = new HashSet<CarImage>(0);
	private Set<CarSample> carSamples = new HashSet<CarSample>(0);
	private List<CarHistory> carHistories = new ArrayList<CarHistory>(0);
	private Set<VendorSku> vendorSkus = new HashSet<VendorSku>(0);
	private Set<CarNote> carNotes = new HashSet<CarNote>(0);
	private Set<CarAttribute> carAttributes = new HashSet<CarAttribute>(0);
	private Set<CarOutfitChild> carOutfitChild=  new HashSet<CarOutfitChild>(0);
	private Set<CarDBPromotionChild> carDBPromotionChild=  new HashSet<CarDBPromotionChild>(0);
//private MediaCompassImage mediaCompassImage = null;
	private Set<MediaCompassImage> mediaCompassImage=  new HashSet<MediaCompassImage>(0);
	

	private List<VendorStyle> vendorStyles = new ArrayList<VendorStyle>() ;
	private Map<VendorStyle, List<VendorSku>> vendorStyleMap = new HashMap<VendorStyle, List<VendorSku>>() ;

	private Map<String,String> superColors=new HashMap<String,String>();
    private String firstSuperColor;
	
	private String colorName;
	private String templateType;
	private String isSearchable;
	private String effectiveDate;
	
	private String imageMCPendingByUser = "NONE";
	private String buyerApprovalPending = "N";
	private String imagesFailedInCCPending = "";
	private Date shipDateUpdatedDate = new GregorianCalendar(1900,1,1).getTime();
	private String postCutOver;
	//To-DO refine this var if needed.


	public Car() {
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "car")
	public Set<CarImage> getCarImages() {
		return this.carImages;
	}

	public void setCarImages(Set<CarImage> carImages) {
		this.carImages = carImages;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "car")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public Set<CarSample> getCarSamples() {
		return this.carSamples;
	}

	public void setCarSamples(Set<CarSample> carSamples) {
		this.carSamples = carSamples;
	}
	
	@Transient
	public ArrayList<CarSample> getCarSampleList() {
		ArrayList<CarSample> sampleList = null;
		if (carSamples != null && !carSamples.isEmpty()) {
			sampleList = new ArrayList<CarSample>(carSamples);
			Comparator gc = new GenericComparator(
					"sample.swatchColor");
			Collections.sort(sampleList, gc);
		} else {
			sampleList = new ArrayList<CarSample>();
		}
		return sampleList ;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "outfitCar")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<CarOutfitChild> getCarOutfitChild() {
		return carOutfitChild;
	}

	public void setCarOutfitChild(Set<CarOutfitChild> carOutfitChild) {
		this.carOutfitChild = carOutfitChild;
	}
	
	/*public void addCarOutfitChild(CarOutfitChild carOutfitChild){
		carOutfitChild.setOutfitCar(this);
		this.carOutfitChild.add(carOutfitChild);
	}*/
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dbPromotionCar")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public Set<CarDBPromotionChild> getCarDBPromotionChild() {
		return carDBPromotionChild;
	}

	public void setCarDBPromotionChild(Set<CarDBPromotionChild> carDBPromotionChild) {
		this.carDBPromotionChild = carDBPromotionChild;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_SEQ_GEN", sequenceName = "CAR_SEQ", allocationSize = 1)
	@Column(name = "CAR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCarId() {
		return this.carId;
	}

	public void setCarId(long carId) {
		this.carId = carId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSIGNED_TO_USER_ID")
	public User getCarUserByAssignedToUserId() {
		return this.carUserByAssignedToUserId;
	}

	public void setCarUserByAssignedToUserId(User carUserByAssignedToUserId) {
		this.carUserByAssignedToUserId = carUserByAssignedToUserId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DEPT_ID", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Department getDepartment() {
		return this.department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOGGED_BY_USER_ID", nullable = false)
	public User getCarUserByLoggedByUserId() {
		return this.carUserByLoggedByUserId;
	}

	public void setCarUserByLoggedByUserId(User carUserByLoggedByUserId) {
		this.carUserByLoggedByUserId = carUserByLoggedByUserId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "WorkFlow_ID", nullable = false)
	public WorkFlow getWorkFlow() {
		return this.WorkFlow;
	}

	public void setWorkFlow(WorkFlow WorkFlow) {
		this.WorkFlow = WorkFlow;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "VENDOR_STYLE_ID", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public VendorStyle getVendorStyle() {
		return this.vendorStyle;
	}

	public void setVendorStyle(VendorStyle vendorStyle) {
		this.vendorStyle = vendorStyle;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOURCE_TYPE_CD", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public SourceType getSourceType() {
		return this.sourceType;
	}

	
	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	@Column(name = "SOURCE_ID", nullable = false, length = 50)
	public String getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DUE_DATE", length = 7)
	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EXPECTED_SHIP_DATE", nullable = false, length = 7)
	public Date getExpectedShipDate() {
		return this.expectedShipDate;
	}

	public void setExpectedShipDate(Date expectedShipDate) {
		this.expectedShipDate = expectedShipDate;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CURRENT_WORKFLOW_STATUS", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public WorkflowStatus getCurrentWorkFlowStatus() {
		return this.currentWorkFlowStatus;
	}

	public void setCurrentWorkFlowStatus(WorkflowStatus currentWorkFlowStatus) {
		this.currentWorkFlowStatus = currentWorkFlowStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ASSIGNED_TO_USER_TYPE_CD", nullable = false)
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public UserType getAssignedToUserType() {
		return this.assignedToUserType;
	}

	public void setAssignedToUserType(UserType assignedToUserType) {
		this.assignedToUserType = assignedToUserType;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	@Column(name = "IS_PRODUCT_TYPE_REQUIRED", nullable = false, length = 1)
	public String getIsProductTypeRequired() {
		return this.isProductTypeRequired;
	}

	public void setIsProductTypeRequired(String isProductTypeRequired) {
		this.isProductTypeRequired = isProductTypeRequired;
	}

	@Column(name = "IS_URGENT", nullable = false, length = 1)
	public String getIsUrgent() {
		return this.isUrgent;
	}

	public void setIsUrgent(String isUrgent) {
		this.isUrgent = isUrgent;
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
	
	/**
	 * @return the escalationDate
	 */
	@Temporal(TemporalType.DATE)
	@Column(name="ESCALATION_DATE", nullable=false, length=7)
	public Date getEscalationDate() {
		return escalationDate;
	}


	/**
	 * @param escalationDate the escalationDate to set
	 */
	public void setEscalationDate(Date escalationDate) {
		this.escalationDate = escalationDate;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "car")
	public List<CarHistory> getCarHistories() {
		/*if(null != this.carHistories){
	      Collections.sort(carHistories);
		}*/
		return this.carHistories;
	}

	public void setCarHistories(List<CarHistory> carHistories) {
		this.carHistories = carHistories;
	}
	@Transient
	public List<CarHistory> getSortedCarHistories() {
		if(null != this.carHistories){
	      Collections.sort(carHistories);
		}
		return this.carHistories;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "car")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public Set<VendorSku> getVendorSkus() {
		return this.vendorSkus;
	}

	public void setVendorSkus(Set<VendorSku> vendorSkus) {
		this.vendorSkus = vendorSkus;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "car")
	public Set<CarNote> getCarNotes() {
		return this.carNotes;
	}

	@Transient
	public List<CarNote> getCarNotesList() {
		List<CarNote> list = new ArrayList<CarNote>(this.carNotes != null  ? this.carNotes: new ArrayList<CarNote>());
		if(this.carNotes!=null) {			
			Collections.sort(list, new ReverseComparator(new NotesComparator()));
		}
		return list;
	}
	
	public void setCarNotes(Set<CarNote> carNotes) {
		this.carNotes = carNotes;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "car")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<CarAttribute> getCarAttributes() {
		return this.carAttributes;
	}

	public void setCarAttributes(Set<CarAttribute> carAttributes) {
		this.carAttributes = carAttributes;
	}

	//Convenience method
	public void addCarAttribute(CarAttribute carAttribute) {
		carAttributes.add(carAttribute);
	}

	@Transient
	public List<CarAttribute> getAttributes() {
		ArrayList<CarAttribute> attrList = null;
		if (carAttributes != null && !carAttributes.isEmpty()) {
			attrList = new ArrayList<CarAttribute>(carAttributes);
			Comparator gc = new GenericComparator(
					"attribute.attributeConfig.displayName");
			Collections.sort(attrList, gc);
		} else {
			attrList = new ArrayList<CarAttribute>();
		}
		return attrList ;
	}

	@Transient
	public ArrayList<CarImage> getActiveCarImages() {
		ArrayList<CarImage> activeCarImages = new ArrayList<CarImage>(0);
		for (CarImage ci : this.getCarImages()) {
			if (ci.getImage().getStatusCd().equals(Status.ACTIVE)
					&& ci.getImage().getImageSourceType()
							.getImageSourceTypeCd().equals(ImageSourceType.ON_HAND)) {
				activeCarImages.add(ci);
			}
		}
		return activeCarImages;
	}
	/* Method added by AFUSYS3 for VIP 
	 * This method is used to buyer and vendor uploaded images */
	@Transient
	public ArrayList<CarImage> getCarVendorImages() {
        ArrayList<CarImage> uploadedImages = new ArrayList<CarImage>(0);
        for (CarImage ci : this.getCarImages()) {
              Image i = ci.getImage();
              if (i.getStatusCd().equals(Status.ACTIVE)
                          && (i.getImageSourceType()
                                      .getImageSourceTypeCd().equals(ImageSourceType.VENDOR_UPLOADED)|| i.getImageSourceType()
                                      .getImageSourceTypeCd().equals(ImageSourceType.BUYER_UPLOADED) )) {                        
                    uploadedImages.add(ci);
              }
        }
        return uploadedImages;
  }

	
	@Transient
	public ArrayList<CarImage> getAllActiveCarImages() {
		ArrayList<CarImage> activeCarImages = new ArrayList<CarImage>(0);
		for (CarImage ci : this.getCarImages()) {
			if (ci.getImage().getStatusCd().equals(Status.ACTIVE)) {
				activeCarImages.add(ci);
			}
		}
		return activeCarImages;
	}

	@Transient
	public ArrayList<CarImage> getCarImagesFromSample() {
		ArrayList<CarImage> activeCarImages = new ArrayList<CarImage>(0);
		for (CarImage ci : this.getCarImages()) {
			if (ci.getImage().getStatusCd().equals(Status.ACTIVE)
					&& ci.getImage().getImageSourceType()
							.getImageSourceTypeCd().equals(ImageSourceType.FROM_SAMPLE)) {
				activeCarImages.add(ci);
			}
		}
		return activeCarImages;
	}
	
	@Transient
	public ArrayList<CarImage> getActiveCarRequestedImages() {
		ArrayList<CarImage> activeCarImages = new ArrayList<CarImage>(0);
		for (CarImage ci : this.getCarImages()) {
			if (ci.getImage().getStatusCd().equals(Status.ACTIVE)
					&& ci.getImage().getImageSourceType()
							.getImageSourceTypeCd().equals(ImageSourceType.VENDOR)) {
				activeCarImages.add(ci);
			}
		}
		return activeCarImages;
	}
/*
	@Transient
	public Collection<CarSkuAttribute> getDistinctSkuAttributes() {
		HashMap<String, CarSkuAttribute> attrs = new HashMap<String, CarSkuAttribute>(
				0);
		for (VendorSku sku : this.getVendorSkus()) {
			if (sku.getSample() == null) {
				for (CarSkuAttribute attr : sku.getCarSkuAttributes()) {
					if (!attrs.containsKey(attr.getAttrValue())) {
						attrs.put(attr.getAttrValue(), attr);
					}
				}
			}
		}
		return attrs.values();
	}
*/
	@Transient
	public ArrayList<CarImage> getActiveCarApprovableImages() {
		ArrayList<CarImage> activeCarImages = new ArrayList<CarImage>(0);
		for (CarImage ci : this.getCarImages()) {
			if (ci.getImage().getStatusCd().equals("ACTIVE")
					&& (ci.getImage().getImageTrackingStatus()
							.getImageTrackingStatusCd().equals("RECEIVED") || ci
							.getImage().getImageTrackingStatus()
							.getImageTrackingStatusCd().equals("AVAILABLE"))) {
				activeCarImages.add(ci);
			}
		}
		return activeCarImages;
	}

	@Transient
	public int getNumOnHandActiveCarImages() {
		return getActiveCarImages().size();
	}

	@Transient
	public int getNumActiveCarRequestedImages() {
		return getActiveCarRequestedImages().size();
	}

	@Transient
	public ArrayList<CarNote> getSampleNotes() {
		ArrayList<CarNote> sns = new ArrayList<CarNote>();
		for (CarNote cn : this.getCarNotes()) {
			if (cn.getNoteType().getNoteTypeCd().equals(NoteType.SAMPLE_NOTES)) {
				sns.add(cn);
			}
		}
		return sns;
	}

	@Transient
	public ImageProvider getImageProvider() {
		if (getCarSamples() != null && !getCarSamples().isEmpty()) {
			CarSample cs = getCarSamples().iterator().next();
			return cs.getSample().getImageProvider();
		}
		return null;
	}

	@Transient
	public ShippingType getReturnCarrier() {
		for (CarSample cs : this.getCarSamples()) {
			if (cs.getSample().getSampleSourceType().getSampleSourceTypeCd().equals(SampleSourceType.VENDOR))
				return cs.getSample().getShippingType();
		}
		return null;
	}

	@Transient
	public void setReturnCarrier(ShippingType shippingType) {
		for (CarSample cs : this.getCarSamples()) {
			cs.getSample().setShippingType(shippingType);
		}
	}

	@Transient
	public String getCarrierAccount() {
		for (CarSample cs : this.getCarSamples()) {
			if (cs.getSample().getSampleSourceType().getSampleSourceTypeCd().equals(SampleSourceType.VENDOR))
				return cs.getSample().getShipperAccountNumber();
		}
		return null;
	}

	public void setCarrierAccount(String carrierAccount) {
		for (CarSample cs : this.getCarSamples()) {
			cs.getSample().setShipperAccountNumber(carrierAccount);
		}
	}

	@Transient
	public ArrayList<CarNote> getReturnNotes() {
		ArrayList<CarNote> sns = new ArrayList<CarNote>();
		for (CarNote cn : this.getCarNotes()) {
			if (cn.getNoteType().getNoteTypeCd().equals("RETURN_NOTES")) {
				sns.add(cn);
			}
		}
		return sns;
	}

	@Transient
	public boolean isLocked() {
		if (this.getLock().equalsIgnoreCase("N") || (this.getCarUserByAssignedToUserId() == null)){
			return false;
		} else {
			return true;
		}
	}
	
	@Transient
	public Date getCompletionDate() {
			Calendar cal = Calendar.getInstance();
			if (this.createdDate != null) {
				cal.setTime(this.createdDate);
			}
			cal.add(Calendar.DAY_OF_YEAR, Car.NUM_DAYS_TO_COMPLETION) ;
			return cal.getTime();
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CONTENT_STATUS_CD", nullable = false)
	public ContentStatus getContentStatus() {
		return contentStatus;
	}

	public void setContentStatus(ContentStatus contentStatus) {
		this.contentStatus = contentStatus;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="LAST_WF_STATUS_UPDATE_DATE", nullable=false)
	public Date getLastWorkflowStatusUpdateDate() {
		return lastWorkflowStatusUpdateDate;
	}

	public void setLastWorkflowStatusUpdateDate(Date lastWorkflowStatusUpdateDate) {
		this.lastWorkflowStatusUpdateDate = lastWorkflowStatusUpdateDate;
	}
	
	@Transient
	public List<VendorStyle> getVendorStyles() {
		if (vendorStyles.isEmpty()) {
			initVendorStyles();
		}
		return vendorStyles ;
	}

	@Transient
	public void resetVendorStyles() {
		vendorStyles = new ArrayList<VendorStyle>();
	}

	@Transient
	public Map<VendorStyle, List<VendorSku>> getVendorStyleMap() {
		if (vendorStyles.isEmpty()) {
			initVendorStyles();
		}
		return vendorStyleMap ;
	}
	
	@Transient
	private void initVendorStyles() {
		if (vendorStyles.isEmpty()) {
			for (VendorSku sku: getVendorSkus()) {
				VendorStyle tStyle = sku.getVendorStyle();
				if (vendorStyleMap.containsKey(tStyle)) {
					vendorStyleMap.get(sku.getVendorStyle()).add(sku) ;
				} else {
					List<VendorSku> skuList = new ArrayList<VendorSku>();
					skuList.add(sku) ;
					vendorStyleMap.put(tStyle, skuList);
				}
			}
			
			vendorStyles.addAll(vendorStyleMap.keySet()) ;
		}
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "TURNIN_DATE", length = 7)
	public Date getTurninDate() {
		return this.turninDate;
	}

	public void setTurninDate(Date turninDate) {
		this.turninDate = turninDate;
	}

	@Transient	
	public String getTurninDateFormatted(){
    	if (this.getTurninDate()==null){
    		return "";
    	}
        return DateUtils.formatDate(this.getTurninDate());
	}

	@Transient	
	public String getExpectedShipDateFormatted(){
    	if (this.getExpectedShipDate()==null){
    		return "";
    	}
        return DateUtils.formatDate(this.getExpectedShipDate());
	}

	public void setArchive(String archive) {
		this.archive = archive;
	}

	@Column(name = "ARCHIVED", nullable = false, length = 1)
	public String getArchive() {
		return archive;
	}
	
	public void setLock(String lock) {
		this.lock = lock;
	}

	@Column(name = "LOCKED", nullable = false, length = 1)
	public String getLock() {
		return lock;
	}
	
	@Column(name = "CAR_QUALITY_CD", nullable = false, length = 1)
	public String getCarQualityCode() {
		return carQualityCode;
	}

	public void setCarQualityCode(String carQualityCode) {
		this.carQualityCode = carQualityCode;
	}

	@Column(name = "REJECTION_CT", nullable = false, length = 12)
	public int getRejectionCount() {
		return rejectionCount;
	}

	public void setRejectionCount(int rejectionCount) {
		this.rejectionCount = rejectionCount;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "car",cascade = CascadeType.ALL)
	public Set<MediaCompassImage> getMediaCompassImage() {
		return mediaCompassImage;
	}

	public void setMediaCompassImage(Set<MediaCompassImage> mediaCompassImage) {
		this.mediaCompassImage = mediaCompassImage;
	}
	
	//used instanceOf instead of comparing class names, as hibernate modifies the class names of object fetched from DB
    @Override
    public boolean equals(Object obj) {
    	if(this== null && obj==null){
    		return true;
    	}
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof Car)) {
            return false;
        }
        final Car other = (Car) obj;
        if (this.carId != other.getCarId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + (int) (this.carId ^ (this.carId >>> 43));
        return hash;
    }
    
    public String toString(){
    	return " ID: "+ Long.toString(carId);
    }

    @Transient
	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
    	
    @Transient
    public Map<String, String> getSuperColors() {
		return superColors;
	}
    
    @Transient
	public void setSuperColors(Map<String, String> superColors) {
		this.superColors = superColors;
	}
    
    @Transient
    public String getFirstSuperColor() {
		return firstSuperColor;
	}

    @Transient
    public void setFirstSuperColor(String firstSuperColor) {
		this.firstSuperColor = firstSuperColor;
	}
   @Transient
    public String getIsSearchable() {
		return isSearchable;
	}
    @Transient
	public void setIsSearchable(String isSearchable) {
		this.isSearchable = isSearchable;
	}
    @Transient
	public String getEffectiveDate() {
		return effectiveDate;
	}
    @Transient
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}	
	@Transient
	public String getTemplateType() {
		return templateType;
	}
	@Transient
	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
    @Column(name = "BUYER_APPROVAL_PENDING", length = 1, nullable = false)
	public String getBuyerApprovalPending() {
		return buyerApprovalPending;
	}

	public void setBuyerApprovalPending(String buyerApprovalPending) {
		this.buyerApprovalPending = buyerApprovalPending;
	}

	@Column(name = "IMAGES_FAILED_IN_CC_PENDING", length = 1000)
	public String getImagesFailedInCCPending() {
		return imagesFailedInCCPending;
	}

	public void setImagesFailedInCCPending(String imagesFailedInCCPending) {	
		this.imagesFailedInCCPending = imagesFailedInCCPending;
	}
	
	@Column(name = "IMAGE_MC_PENDING_BY_USER", length = 20, nullable = false)
	public String getImageMCPendingByUser() {
		return imageMCPendingByUser;
	}

	public void setImageMCPendingByUser(String imageMCPendingByUser) {
		this.imageMCPendingByUser = imageMCPendingByUser;
	}

	
	@Transient
	public ArrayList<CarImage> getAllNonVendorActiveCarImages() {
		ArrayList<CarImage> activeCarImages = new ArrayList<CarImage>(0);
		for (CarImage ci : this.getCarImages()) {
			Image i = ci.getImage();
			if (i.getStatusCd().equals(Status.ACTIVE) && !(i.getImageSourceType()
                    .getImageSourceTypeCd().equals(ImageSourceType.VENDOR_UPLOADED) || i.getImageSourceType()
                    .getImageSourceTypeCd().equals(ImageSourceType.BUYER_UPLOADED) )) {
				activeCarImages.add(ci);
			}
		}
		return activeCarImages;
	}
	
	@Temporal(TemporalType.DATE)
	@Column(name = "SHIPDATE_UPDATED_DATE", length = 7, nullable = false)
	public Date getShipDateUpdatedDate() {
		return shipDateUpdatedDate;
	}

	public void setShipDateUpdatedDate(Date shipDateUpdatedDate) {
		this.shipDateUpdatedDate = shipDateUpdatedDate;
	}

	@Transient
    public String getPostCutOver() {
        return postCutOver;
    }

	@Transient
    public void setPostCutOver(String isPostCutOver) {
        this.postCutOver = isPostCutOver;
    }

}
