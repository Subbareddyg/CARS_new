package com.belk.car.product.integration.response.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * POJO to hold the Collection_Spec.
 * Current Path in xml response: /getGroupResponse/Group_Catalog/pim_entry/entry/Collection_Spec.
 */
@XmlRootElement(name = "Collection_Spec")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class GroupCollectionSpecData {
	
	private List<GroupCollectionSpecComponent> component;
	private String outfitNavigation;
	private String omniChannelBrand;
	private String carBrand;

	@XmlElement(name = "Component")
	public List<GroupCollectionSpecComponent> getComponent() {
		return component;
	}

	public void setComponent(List<GroupCollectionSpecComponent> component) {
		this.component = component;
	}
	
	@XmlElement(name = "Outfit_Navigation")
	public String getOutfitNavigation() {
		return outfitNavigation;
	}

	public void setOutfitNavigation(String outfitNavigation) {
		this.outfitNavigation = outfitNavigation;
	}

	@XmlElement(name = "OmniChannelBrand")
    public String getOmniChannelBrand() {
        return omniChannelBrand;
    }

    public void setOmniChannelBrand(String omniChannelBrand) {
        this.omniChannelBrand = omniChannelBrand;
    }

    @XmlElement(name = "CarBrand")
    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }
	
	
}
