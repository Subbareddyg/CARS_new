package com.belk.car.integrations.pim.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "component", propOrder = {
		"id",
		"vendorNumber",
		"vendorStyle",
		"productCode",
		"status",
		"isDefault",
                "priority",
		"componentType"
})

@XmlRootElement(name = "component")
public class GroupingComponent {
    
    @XmlElement(required = true)
    protected String id;
    @XmlElement(name = "vendor_number", required = true)
    protected String vendorNumber;
    @XmlElement(name = "vendor_style", required = true)
    protected String vendorStyle;
    @XmlElement(name = "product_code", required = true)
    protected String productCode;
    protected String status;
    @XmlElement(name = "default", required = true)
    protected String isDefault;
    @XmlElement(required = true)
    protected String priority;
    @XmlElement(name = "component_type", required = true)
    protected String componentType;

    public String getId() {
        return id;
    }

    public String getVendorNumber() {
        return vendorNumber;
    }

    public String getVendorStyle() {
        return vendorStyle;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getStatus() {
        return status;
    }

    public String getIsDefault() {
        return isDefault;
    }
    
    public String getPriority() {
        return priority;
    }

    public String getComponentType() {
        return componentType;
    }


    
    
    
}
