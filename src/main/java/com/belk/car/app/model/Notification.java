package com.belk.car.app.model;

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

@Entity
@Table(name = "NOTIFICATION")
public class Notification extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7179298333708429862L;
	private long notificationId;
	private NotificationType notificationType;
	private String fromEmailAddr;
	private String toEmailAddr;
	private String subject;
	private String content;
	private Date sentDate;
	private String errorMessage;

	public Notification() {
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "NOTIFICATION_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "NOTIFICATION_SEQ_GEN", sequenceName = "NOTIFICATION_SEQ", allocationSize = 1)
	@Column(name = "NOTIFICATION_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getNotificationId() {
		return this.notificationId;
	}

	public void setNotificationId(long notificationId) {
		this.notificationId = notificationId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "NOTIFICATION_TYPE_CD", nullable = false)
	public NotificationType getNotificationType() {
		return this.notificationType;
	}

	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}

	@Column(name = "FROM_EMAIL_ADDR", nullable = false)
	public String getFromEmailAddress() {
		return this.fromEmailAddr;
	}

	public void setFromEmailAddress(String fromEmailAddr) {
		this.fromEmailAddr = fromEmailAddr;
	}

	@Column(name = "TO_EMAIL_ADDR", nullable = false)
	public String getToEmailAddress() {
		return this.toEmailAddr;
	}

	public void setToEmailAddress(String toEmailAddr) {
		this.toEmailAddr = toEmailAddr;
	}

	@Column(name = "SUBJECT", nullable = false, length = 500)
	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "CONTENT", nullable = false, length = 4000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SENT_DATE", nullable = false, length = 7)
	public Date getSentDate() {
		return this.sentDate;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
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

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	/**
	 * @return the errorMessage
	 */
	@Column(name = "SEND_ERROR_MSG", nullable = true, length = 4000)
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
