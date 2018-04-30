package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class holds the value of the field mapping in Vendor Catalog data
 * mappging.
 * 
 * @author afusxs7 : Subbu.
 */
public class CatalogMappedFieldDTO implements Serializable {

	private static final long serialVersionUID = 3397222652570351089L;
	private int rowNumber;
	private Long vendorSuppliedFieldID;
	private String vendorSuppliedField;
	private boolean isMasterRecord;
	private Long masterMappingId;
	private Long masterAttributeID;
	private String mappingAttribute; // Blue martini attribute
	private boolean fromDatabase=true;
	private Long vendorSuppliedFieldNum;

	// To store the mapped data field for current values.
	private Map<String, FieldMappingDataDTO> fieldMappingDTO = new HashMap<String, FieldMappingDataDTO>(
			0);
	private List<FieldMappingDataDTO> lstFldDataDTOs = new ArrayList<FieldMappingDataDTO>(0);

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public Long getVendorSuppliedFieldID() {
		return vendorSuppliedFieldID;
	}

	public void setVendorSuppliedFieldID(Long vendorSuppliedFieldID) {
		this.vendorSuppliedFieldID = vendorSuppliedFieldID;
	}

	public String getVendorSuppliedField() {
		return vendorSuppliedField;
	}

	public void setVendorSuppliedField(String vendorSuppliedField) {
		this.vendorSuppliedField = vendorSuppliedField;
	}

	public boolean isMasterRecord() {
		return isMasterRecord;
	}

	public void setMasterRecord(boolean isMasterRecord) {
		this.isMasterRecord = isMasterRecord;
	}

	public Long getMasterMappingId() {
		return masterMappingId;
	}

	public void setMasterMappingId(Long masterMappingId) {
		this.masterMappingId = masterMappingId;
	}

	public void setMappingAttribute(String mappingAttribute) {
		this.mappingAttribute = mappingAttribute;
	}

	public String getMappingAttribute() {
		return mappingAttribute;
	}

	public Map<String, FieldMappingDataDTO> getFieldMappingDTO() {
		return fieldMappingDTO;
	}

	public void setFieldMappingDTO(Map<String, FieldMappingDataDTO> fieldMappingDTO) {
		this.fieldMappingDTO = fieldMappingDTO;
	}

	public List<FieldMappingDataDTO> getLstFldDataDTOs() {
		return lstFldDataDTOs;
	}

	public void setLstFldDataDTOs(List<FieldMappingDataDTO> lstFldDataDTOs) {
		this.lstFldDataDTOs = lstFldDataDTOs;
	}

	public Long getMasterAttributeID() {
		return masterAttributeID;
	}

	public void setMasterAttributeID(Long masterAttributeID) {
		this.masterAttributeID = masterAttributeID;
	}

	public boolean isFromDatabase() {
		return fromDatabase;
	}

	public void setFromDatabase(boolean fromDatabase) {
		this.fromDatabase = fromDatabase;
	}

	public Long getVendorSuppliedFieldNum() {
		return vendorSuppliedFieldNum;
	}

	public void setVendorSuppliedFieldNum(Long vendorSuppliedFieldNum) {
		this.vendorSuppliedFieldNum = vendorSuppliedFieldNum;
	}
	
	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("rowNumber", this.rowNumber)
                .append("vendorSuppliedFieldID", this.vendorSuppliedFieldID)
                .append("vendorSuppliedField", this.vendorSuppliedField)
                .append("isMasterRecord", this.isMasterRecord)
                .append("masterMappingId", this.masterMappingId)
                .append("masterAttributeID", this.masterAttributeID)
                .append("mappingAttribute", this.mappingAttribute)
                .append("fromDatabase", this.fromDatabase)
                .append("vendorSuppliedFieldNum", this.vendorSuppliedFieldNum)
                .toString();
    }

}
