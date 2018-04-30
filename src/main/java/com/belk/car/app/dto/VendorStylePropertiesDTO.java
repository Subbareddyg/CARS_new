/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.dto;
 
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author AFUSY85
 */
public class VendorStylePropertiesDTO  implements Serializable{
   private List <NameValue> mappedFields;
   private List<NameValue> unmappedList;
   private List <VendorUpcDTO> upcList;
   private List <VendorStyleImageDTO> styleImages;
   private List <VendorStyleImageDTO> skuImages;
   private VendorUpcDTO upcDetails;
   private String mappedFieldSeq;
   private String unMappedFieldSeq;
   private String mappedFieldsArr[];
   private String unmappedFields[];
   private String vendorCatalogId;
   private String recordNum;
   private String vendorStyleId;
   private String catalogTempalteId;
   private String vendorUpc;
   private String description;
   private VendorStyleInfo styleInfo;
   private String updateDescription;
   private String descriptionFieldNo;
   private String updateColor;
   private String colorFieldNo;
   private String vendorId;
   


    public List<NameValue> getMappedFields() {
        return mappedFields;
    }

    public void setMappedFields(List<NameValue> mappedFields) {
        this.mappedFields = mappedFields;
    }

    public List<VendorStyleImageDTO> getSkuImages() {
        return skuImages;
    }

    public void setSkuImages(List<VendorStyleImageDTO> skuImages) {
        this.skuImages = skuImages;
    }

    public List<VendorStyleImageDTO> getStyleImages() {
        return styleImages;
    }

    public void setStyleImages(List<VendorStyleImageDTO> styleImages) {
        this.styleImages = styleImages;
    }

    public List<NameValue> getUnmappedList() {
        return unmappedList;
    }

    public void setUnmappedList(List<NameValue> unmappedList) {
        this.unmappedList = unmappedList;
    }

    public List<VendorUpcDTO> getUpcList() {
        return upcList;
    }

    public void setUpcList(List<VendorUpcDTO> upcList) {
        this.upcList = upcList;
    }

    public VendorUpcDTO getUpcDetails() {
        return upcDetails;
    }

    public void setUpcDetails(VendorUpcDTO upcDetails) {
        this.upcDetails = upcDetails;
    }

    public String getMappedFieldSeq() {
        return mappedFieldSeq;
    }

    public void setMappedFieldSeq(String mappedFieldSeq) {
        this.mappedFieldSeq = mappedFieldSeq;
    }

    public String getUnMappedFieldSeq() {
        return unMappedFieldSeq;
    }

    public void setUnMappedFieldSeq(String unMappedFieldSeq) {
        this.unMappedFieldSeq = unMappedFieldSeq;
    }

    public String[] getMappedFieldsArr() {
        return mappedFieldsArr;
    }

    public void setMappedFieldsArr(String[] mappedFieldsArr) {
        this.mappedFieldsArr = mappedFieldsArr;
    }

    public String[] getUnmappedFields() {
        return unmappedFields;
    }

    public void setUnmappedFields(String[] unmappedFields) {
        this.unmappedFields = unmappedFields;
    }

    public String getRecordNum() {
        return recordNum;
    }

    public void setRecordNum(String recordNum) {
        this.recordNum = recordNum;
    }

    public String getVendorCatalogId() {
        return vendorCatalogId;
    }

    public void setVendorCatalogId(String vendorCatalogId) {
        this.vendorCatalogId = vendorCatalogId;
    }

    public String getVendorStyleId() {
        return vendorStyleId;
    }

    public void setVendorStyleId(String vendorStyleId) {
        this.vendorStyleId = vendorStyleId;
    }

    public String getCatalogTempalteId() {
        return catalogTempalteId;
    }

    public void setCatalogTempalteId(String catalogTempalteId) {
        this.catalogTempalteId = catalogTempalteId;
    }

    public String getVendorUpc() {
        return vendorUpc;
    }

    public void setVendorUpc(String vendorUpc) {
        this.vendorUpc = vendorUpc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VendorStyleInfo getStyleInfo() {
        return styleInfo;
    }

    public void setStyleInfo(VendorStyleInfo styleInfo) {
        this.styleInfo = styleInfo;
    }

	/**
	 * @return the updateDescription
	 */
	public String getUpdateDescription() {
		return updateDescription;
	}

	/**
	 * @param updateDescription the updateDescription to set
	 */
	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	/**
	 * @return the descriptionFieldNo
	 */
	public String getDescriptionFieldNo() {
		return descriptionFieldNo;
	}

	/**
	 * @param descriptionFieldNo the descriptionFieldNo to set
	 */
	public void setDescriptionFieldNo(String descriptionFieldNo) {
		this.descriptionFieldNo = descriptionFieldNo;
	}

	/**
	 * @return the updateColor
	 */
	public String getUpdateColor() {
		return updateColor;
	}

	/**
	 * @param updateColor the updateColor to set
	 */
	public void setUpdateColor(String updateColor) {
		this.updateColor = updateColor;
	}

	/**
	 * @return the colorFieldNo
	 */
	public String getColorFieldNo() {
		return colorFieldNo;
	}

	/**
	 * @param colorFieldNo the colorFieldNo to set
	 */
	public void setColorFieldNo(String colorFieldNo) {
		this.colorFieldNo = colorFieldNo;
	}

        public String getVendorId() {
            return vendorId;
        }

        public void setVendorId(String vendorId) {
            this.vendorId = vendorId;
        }
     
   
}
