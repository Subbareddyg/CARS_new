package com.belk.car.integrations.pim.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "grouping", propOrder = {
		"id",
		"vendorNumber",
		"vendorStyle",
		"productCode",
		"vendorName",
		"name",
		"description",
                "deptCd",
		"classNumber",
                "status",
		"groupingType",
		"CARS_group_type",
		"effective_start_date",
		"effective_end_date",
		"expected_ship_date",
		"is_searchable"
})

@XmlRootElement(name = "grouping")
public class GroupingParent {
    
    
    protected String id;
    @XmlElement(name = "vendor_number", required = true)
    protected String vendorNumber;
    @XmlElement(name = "vendor_style", required = true)
    protected String vendorStyle;
    @XmlElement(name = "product_code", required = false)
    protected String productCode;
    @XmlElement(name = "vendor_name", required = true)
    protected String vendorName; 
    
    @XmlElement(required = true)
    protected String name;
    
    @XmlElement(required = true)
    protected String description;
    
    @XmlElement(name = "dept_cd", required = true)
    protected String deptCd;
    
    @XmlElement(name = "class_number", required = true)
    protected String classNumber;
    
    @XmlElement(required = true)
    protected String status;

    @XmlElement(name = "group_type", required = true)
    protected String groupingType;
    
    @XmlElement(required=false)
    protected String CARS_group_type;
    @XmlElement(required=false)
    protected String effective_start_date;
    @XmlElement(required=false)
    protected String effective_end_date;
    @XmlElement(required=false)
    protected String expected_ship_date;
    @XmlElement(required=false)
    protected String is_searchable;

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

    public String getVendorName() {
        return vendorName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDeptCd() {
        return deptCd;
    }

    public String getClassNumber() {
        return classNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getGroupingType() {
        return groupingType;
    }
    
    public String getCARSGroupType() {
        return CARS_group_type;
    }

    public String getEffectiveStartDate() {
        return effective_start_date;
    }

    public String getEffectiveEndDate() {
        return effective_end_date;
    }

    public String getEffectiveShipDate() {
        return expected_ship_date;
    }

    public String getIsSearchable() {
        return is_searchable;
    }
    
    
    
    
}
