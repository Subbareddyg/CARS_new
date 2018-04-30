package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the group entry level information.
 * Contains group specific data and attributes information. 
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry.
 */
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupEntryDetailsData {

	private Integer entryId;
	
	private GroupCatalogSpecData catalogSpecData;
	
    private GroupConsolidatedProductSpecData consolidatedProductSpecData;
	
	private GroupCopySecSpecData copySecSpecData;
	
	private GroupSplitColorSpecData splitColorSpecData;
	
	private GroupSplitSKUSpecData splitSKUSpecData;
	private GroupCollectionSpecData collectionSpecData;
	private GroupImageSecSpecData imageSecSpecData;
	
	private List<JAXBElement<String>> otherElements;
	
	@XmlAttribute(name = "id")
	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}
	
	@XmlElement(name = "Group_Ctg_Spec")
	public GroupCatalogSpecData getGroupCatalogSpecData() {
        return catalogSpecData;
    }

    public void setGroupCatalogSpecData(GroupCatalogSpecData catalogSpecData) {
        this.catalogSpecData = catalogSpecData;
    }
    
    @XmlElement(name = "Consolidated_Product_Spec")
	public GroupConsolidatedProductSpecData getGroupConsolidatedProductSpecData() {
        return consolidatedProductSpecData;
    }

    public void setGroupConsolidatedProductSpecData(GroupConsolidatedProductSpecData consolidatedProductSpecData) {
        this.consolidatedProductSpecData = consolidatedProductSpecData;
    }
    
    @XmlElement(name = "Copy_Sec_Spec")
	public GroupCopySecSpecData getGroupCopySecSpecData() {
        return copySecSpecData;
    }

    public void setGroupCopySecSpecData(GroupCopySecSpecData copySecSpecData) {
        this.copySecSpecData = copySecSpecData;
    }
    
    @XmlElement(name = "Split_Color_Spec")
	public GroupSplitColorSpecData getGroupSplitColorSpecData() {
        return splitColorSpecData;
    }

    public void setGroupSplitColorSpecData(GroupSplitColorSpecData splitColorSpecData) {
        this.splitColorSpecData = splitColorSpecData;
    }
    
    @XmlElement(name = "Split_SKU_Spec")
	public GroupSplitSKUSpecData getGroupSplitSKUSpecData() {
        return splitSKUSpecData;
    }

    public void setGroupSplitSKUSpecData(GroupSplitSKUSpecData splitSKUSpecData) {
        this.splitSKUSpecData = splitSKUSpecData;
    }
    
    @XmlElement(name = "Collection_Spec")
	public GroupCollectionSpecData getGroupCollectionSpecData() {
        return collectionSpecData;
    }

    public void setGroupCollectionSpecData(GroupCollectionSpecData collectionSpecData) {
        this.collectionSpecData = collectionSpecData;
    }
    
    @XmlElement(name = "Image_Sec_Spec")
	public GroupImageSecSpecData getGroupImageSecSpecData() {
        return imageSecSpecData;
    }

    public void setGroupImageSecSpecData(GroupImageSecSpecData imageSecSpecData) {
        this.imageSecSpecData = imageSecSpecData;
    }

	@XmlAnyElement(lax=true)
	public List<JAXBElement<String>> getOtherElements() {
		return otherElements;
	}
	
	public void setOtherElements(List<JAXBElement<String>> otherElements) {
		this.otherElements = otherElements;
	}	
}
