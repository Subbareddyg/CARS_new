/**
 * 
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * @author afuszm1
 *
 */
@Entity
@Table(name = "RRD_IMAGE_CHECK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RRDImageCheck extends BaseAuditableModel implements java.io.Serializable {

	
	public static final long serialVersionUID = -5096216252035320186L;
	public static final String CONDITIONAL_APPROVAL ="CONDITIONAL APPROVAL";
	public static final String APPROVE ="APPROVE";
	public static final String RETOUCH = "RETOUCH";
	public static final String CONDITIONAL_APPROVAL_RETOUCH = "CONDITIONAL APPROVAL RETOUCH";
	public static final String MECHANICAL = "MECHANICAL";
	public static final String CREATIVE = "CREATIVE";
	public static final String PASS ="PASS";
	public static final String FAIL ="FAIL";
	public static final String REJECT ="REJECT";
	
	
	private long checkId;
	private VendorImage vendorImage;
	private String checkType;
	private String checkStatus;
	private Set<RRDImageCheckFeedback> imageCheckFeedbackSet = new HashSet<RRDImageCheckFeedback>(0);
	private Set<RRDImageCheckComments> imageCheckCommentSet = new HashSet<RRDImageCheckComments>(0);
	

	public RRDImageCheck() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RRD_IMAGE_CHECK_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "RRD_IMAGE_CHECK_SEQ_GEN", sequenceName = "RRD_IMAGE_CHECK_SEQ", allocationSize = 1)
	@Column(name = "CHECK_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getCheckId() {
		return checkId;
	}

	public void setCheckId(long checkId) {
		this.checkId = checkId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VENDOR_IMAGE_ID", nullable = false)
	public VendorImage getVendorImage() {
		return this.vendorImage;
	}

	public void setVendorImage(VendorImage vendorImage) {
		this.vendorImage = vendorImage;
	}
	
	@Column(name = "CHECK_TYPE", nullable = false, length = 50)
	public String getCheckType() {
		return this.checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	@Column(name = "CHECK_STATUS", nullable = false, length = 50)
	public String getCheckStatus() {
		return this.checkStatus;
	}

	public void setCheckStatus(String checkStatus) {
		this.checkStatus = checkStatus;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rrdImageCheck")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public Set<RRDImageCheckFeedback> getImageCheckFeedbackSet() {
		return imageCheckFeedbackSet;
	}

	public void setImageCheckFeedbackSet(Set<RRDImageCheckFeedback> imageCheckFeedbackSet) {
		this.imageCheckFeedbackSet = imageCheckFeedbackSet;
	}
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "rrdImageCheck")
	@Cascade( { org.hibernate.annotations.CascadeType.SAVE_UPDATE, org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	public Set<RRDImageCheckComments> getImageCheckCommentSet() {
		return imageCheckCommentSet;
	}

	public void setImageCheckCommentSet(
			Set<RRDImageCheckComments> imageCheckCommentSet) {
		this.imageCheckCommentSet = imageCheckCommentSet;
	}
	
	/*@Transient
	public ArrayList<RRDImageCheckFeedback> getMechanicalCheckFeedback() {
		ArrayList<RRDImageCheckFeedback> imageCheckFeedbackList = null;
		if (imageCheckFeedbackSet != null && !imageCheckFeedbackSet.isEmpty()) {
			for(RRDImageCheckFeedback feedback : imageCheckFeedbackSet){
				if(feedback.get)
			}
			imageCheckFeedbackList = new ArrayList<RRDImageCheckFeedback>(imageCheckFeedbackSet);
			
		
		} else {
			imageCheckFeedbackList = new ArrayList<RRDImageCheckFeedback>();
		}
		return imageCheckFeedbackList ;
	}*/
}
