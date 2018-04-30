/**
 * 
 */
package com.belk.car.app.model.vendorimage;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * @author afuszm1
 *
 */
@Entity
@Table(name = "RRD_IMAGE_CHECK_FEEDBACK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RRDImageCheckFeedback extends BaseAuditableModel implements java.io.Serializable {

	
	private static final long serialVersionUID = -5096216252035320187L;
	
	private long checkFeedbackId;
	private RRDImageCheck rrdImageCheck;
	private String feedback;
	
    public RRDImageCheckFeedback(){
    	
    }
    
    @Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RRD_IMAGE_CHECK_FEEDBACK_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "RRD_IMAGE_CHECK_FEEDBACK_SEQ_GEN", sequenceName = "RRD_IMAGE_CHECK_FEEDBACK_SEQ", allocationSize = 1)
	@Column(name = "FEEDBACK_ID", unique = true, nullable = false, precision = 12, scale = 0)
    public long getCheckFeedbackId() {
		return checkFeedbackId;
	}

	public void setCheckFeedbackId(long checkFeedbackId) {
		this.checkFeedbackId = checkFeedbackId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHECK_ID", nullable = false)
	public RRDImageCheck getRrdImageCheck() {
		return rrdImageCheck;
	}

	public void setRrdImageCheck(RRDImageCheck rrdImageCheck) {
		this.rrdImageCheck = rrdImageCheck;
	}
	
	@Column(name = "FEEDBACK",  nullable = false, length = 100)
	public String getFeedback() {
		return this.feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
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
}
