package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "HELP_CONTENT")
public class HelpContent extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4552478803491084795L;
	
	public static String NO_HELP_CONTENT_KEY = "/nohelp.html" ;
	
	private long contentId;
	private String contentKey;
	private String contentSection;
	private String contentName;
	private String contentText;
	private String statusCd;


	public HelpContent() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "HELP_CONTENT_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "HELP_CONTENT_SEQ_GEN", sequenceName = "HELP_CONTENT_SEQ", allocationSize =1)
	@Column(name = "CONTENT_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getContentId() {
		return this.contentId;
	}

	public void setContentId(long contentId) {
		this.contentId = contentId;
	}

	@Column(name = "CONTENT_TEXT", length = 4000)
	public String getContentText() {
		return this.contentText;
	}

	public void setContentText(String contentText) {
		this.contentText = contentText;
	}

	@Column(name = "CONTENT_KEY", length = 50)
	public String getContentKey() {
		return this.contentKey;
	}

	public void setContentKey(String contentKey) {
		this.contentKey = contentKey;
	}

	@Column(name = "CONTENT_SECTION", length = 50)
	public String getContentSection() {
		return this.contentSection;
	}

	public void setContentSection(String contentSection) {
		this.contentSection = contentSection;
	}

	@Column(name = "CONTENT_NAME", length = 200)
	public String getContentName() {
		return this.contentName;
	}

	public void setContentName(String contentName) {
		this.contentName = contentName;
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

}
