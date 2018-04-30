package com.belk.car.integrations.pim.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "carsGroupDtl", propOrder = {
		"grouping",
		"component"
})
public class CarsGroupingDtl {
    
    
    
    protected GroupingParent grouping;
    protected List<GroupingComponent> component;

    public GroupingParent getGrouping() {
        return grouping;
    }

    public List<GroupingComponent> getComponent() {
        return component;
    }

        
    
}
