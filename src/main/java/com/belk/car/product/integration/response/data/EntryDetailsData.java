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
 * POJO to hold the entry level information. Request will fetch only 1 entry level information.
 * Contains item level specific details including category spec data, attributes information. 
 * Current Path in xml response: /getItemRespone/Item_Catalog/pim_entry/entry.
 */
@XmlRootElement(name = "entry")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class EntryDetailsData {

	private Integer entryId;
	
	private ItemCatalogSpecData itemCatalogSpecData;
	
    private ItemStyleSpecData itemStyleSpecData;
	
	private ItemSkuSpecData itemSkuSpecData;
	
	private ItemStyleColorSpecData itemStyleColorSpecData;
	
	private ItemComplexPackSpecData itemComplexPackSpecData;
	private ItemPackColorSpecData itemPackColorSpecData;
	private SecondaryImageSpecsData itemImageSpecData;
	
	private List<JAXBElement<String>> otherElements;
	private LookupAttributeData attributeLookupData;
	
	private ItemPetCatalogData itemPetCatalogData;

	@XmlElement(name = "Item_Pet_Ctg_Spec")
	public ItemPetCatalogData getItemPetCatalogData() {
        return itemPetCatalogData;
    }

    public void setItemPetCatalogData(ItemPetCatalogData itemPetCatalogData) {
        this.itemPetCatalogData = itemPetCatalogData;
    }

    @XmlAttribute(name = "id")
	public Integer getEntryId() {
		return entryId;
	}

	public void setEntryId(Integer entryId) {
		this.entryId = entryId;
	}

	@XmlAnyElement(lax=true)
	public List<JAXBElement<String>> getOtherElements() {
		return otherElements;
	}
	
	public void setOtherElements(List<JAXBElement<String>> otherElements) {
		this.otherElements = otherElements;
	}

	@XmlElement(name = "Item_Style_Spec")
	public ItemStyleSpecData getItemStyleSpecData() {
		return itemStyleSpecData;
	}

	public void setItemStyleSpecData(ItemStyleSpecData itemStyleSpecData) {
		this.itemStyleSpecData = itemStyleSpecData;
	}

	@XmlElement(name = "Image_Sec_Spec")
	public SecondaryImageSpecsData getItemImageSpecData() {
		return itemImageSpecData;
	}

	public void setItemImageSpecData(SecondaryImageSpecsData itemImageSpecData) {
		this.itemImageSpecData = itemImageSpecData;
	}

	
    @XmlElement(name = "Item_Ctg_Spec")
    public ItemCatalogSpecData getItemCatalogSpecData() {
        return itemCatalogSpecData;
    }

    public void setItemCatalogSpecData(ItemCatalogSpecData itemCatalogSpecData) {
        this.itemCatalogSpecData = itemCatalogSpecData;
    }

	@XmlElement(name = "Item_SKU_Spec")
	public ItemSkuSpecData getItemSkuSpecData() {
		return itemSkuSpecData;
	}

	public void setItemSkuSpecData(ItemSkuSpecData itemSkuSpecData) {
		this.itemSkuSpecData = itemSkuSpecData;
	}

	@XmlElement(name = "Item_StyleColor_Spec")
	public ItemStyleColorSpecData getItemStyleColorSpecData() {
		return itemStyleColorSpecData;
	}

	public void setItemStyleColorSpecData(
			ItemStyleColorSpecData itemStyleColorSpecData) {
		this.itemStyleColorSpecData = itemStyleColorSpecData;
	}
	@XmlElement(name = "Item_Complex_Pack_Spec")
	public ItemComplexPackSpecData getItemComplexPackSpecData() {
		return itemComplexPackSpecData;
	}
	public void setItemComplexPackSpecData(
			ItemComplexPackSpecData itemComplexPackSpecData) {
		this.itemComplexPackSpecData = itemComplexPackSpecData;
	}
	@XmlElement(name = "zIPH_Lkp_Red")
	public LookupAttributeData getAttributeLookupData() {
		return attributeLookupData;
	}
	public void setAttributeLookupData(LookupAttributeData attributeLookupData) {
		this.attributeLookupData = attributeLookupData;
	}
	@XmlElement(name = "Item_PackColor_Spec")
	public ItemPackColorSpecData getItemPackColorSpecData() {
		return itemPackColorSpecData;
	}
	public void setItemPackColorSpecData(ItemPackColorSpecData itemPackColorSpecData) {
		this.itemPackColorSpecData = itemPackColorSpecData;
	}
	
}
