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
@Table(name = "RRD_IMAGE_CHECK_COMMENTS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RRDImageCheckComments extends BaseAuditableModel implements java.io.Serializable {
	
	private static final long serialVersionUID = -5096216252035320188L;
	
	private long checkCommentId;
	private RRDImageCheck rrdImageCheck;
	private String comments;

	public RRDImageCheckComments(){
		
	}
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RRD_IMAGE_CHECK_COMMENTS_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "RRD_IMAGE_CHECK_COMMENTS_SEQ_GEN", sequenceName = "RRD_IMAGE_CHECK_COMMENTS_SEQ", allocationSize = 1)
	@Column(name = "COMMENT_ID", unique = true, nullable = false, precision = 12, scale = 0)	
	public long getCheckCommentId() {
		return checkCommentId;
	}
	public void setCheckCommentId(long checkCommentId) {
		this.checkCommentId = checkCommentId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHECK_ID", nullable = false)
	public RRDImageCheck getRrdImageCheck() {
		return this.rrdImageCheck;
	}

	public void setRrdImageCheck(RRDImageCheck rrdImageCheck) {
		this.rrdImageCheck = rrdImageCheck;
	}
	
	@Column(name = "COMMENTS",  nullable = false, length = 500)
	public String getComments() {
		return this.comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
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
