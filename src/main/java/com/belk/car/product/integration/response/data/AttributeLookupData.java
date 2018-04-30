package com.belk.car.product.integration.response.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "lookups")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class AttributeLookupData {
	
	private String attributeLookupIndicator;
	
	private String sourceSpecPath;
	
	private String lookupTableName;
	
	private String code;
	
	private String entryValuesInXmlFormat;

	@XmlAttribute(name = "occ")
	public String getAttributeLookupIndicator() {
		return attributeLookupIndicator;
	}

	public void setAttributeLookupIndicator(String attributeLookupIndicator) {
		this.attributeLookupIndicator = attributeLookupIndicator;
	}

	@XmlElement(name = "source_spec_path")
	public String getSourceSpecPath() {
		return sourceSpecPath;
	}

	public void setSourceSpecPath(String sourceSpecPath) {
		this.sourceSpecPath = sourceSpecPath;
	}

	@XmlElement(name = "lookup_table_name")
	public String getLookupTableName() {
		return lookupTableName;
	}

	public void setLookupTableName(String lookupTableName) {
		this.lookupTableName = lookupTableName;
	}

	@XmlElement(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@XmlElement(name = "values_as_xml")
	public String getEntryValuesInXmlFormat() {
		return entryValuesInXmlFormat;
	}

	public void setEntryValuesInXmlFormat(String entryValuesInXmlFormat) {
		this.entryValuesInXmlFormat = entryValuesInXmlFormat;
	}
	
	

}
