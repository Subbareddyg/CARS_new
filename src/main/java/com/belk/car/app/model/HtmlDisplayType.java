package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "HTML_DISPLAY_TYPE", uniqueConstraints = @UniqueConstraint(columnNames = "HTML_DISPLAY_TYPE_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class HtmlDisplayType extends BaseAuditableModel implements
		java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7300480381057206460L;

	public static String RADIO_BUTTONS = "RADIO";
	public static String DROP_DOWN = "SELECT" ;
	public static String TEXT_WITH_AUTOCOMPLETE = "FREE_SELECT";
	public static String TEXTBOX = "TEXT";
	public static String HTML_EDITOR = "HTML" ;
	public static String CHECKBOX = "CHECKBOX";

	public static String CHECKBOX_VALUE_DELIMITER = ",";
	
	private String htmlDisplayTypeCd;
	private String name;
	private String descr;

	public HtmlDisplayType() {
	}

	@Id
	@Column(name = "HTML_DISPLAY_TYPE_CD", unique = true, nullable = false, length = 20)
	public String getHtmlDisplayTypeCd() {
		return this.htmlDisplayTypeCd;
	}

	public void setHtmlDisplayTypeCd(String htmlDisplayTypeCd) {
		this.htmlDisplayTypeCd = htmlDisplayTypeCd;
	}

	@Column(name = "NAME", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescription() {
		return this.descr;
	}

	public void setDescription(String descr) {
		this.descr = descr;
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
	
	@Transient
	public boolean isCheckbox() {
		return this.htmlDisplayTypeCd.equals(HtmlDisplayType.CHECKBOX);
	}

	@Transient
	public boolean isRadioButton() {
		return this.htmlDisplayTypeCd.equals(HtmlDisplayType.RADIO_BUTTONS);
	}

	@Transient
	public boolean isDropdown() {
		return this.htmlDisplayTypeCd.equals(HtmlDisplayType.DROP_DOWN);
	}

	@Transient
	public boolean isTextbox() {
		return this.htmlDisplayTypeCd.equals(HtmlDisplayType.TEXTBOX);
	}

	@Transient
	public boolean isAutocomplete() {
		return this.htmlDisplayTypeCd.equals(HtmlDisplayType.TEXT_WITH_AUTOCOMPLETE);
	}

	@Transient
	public boolean isHtmlEditor() {
		return this.htmlDisplayTypeCd.equals(HtmlDisplayType.HTML_EDITOR);
	}
}
