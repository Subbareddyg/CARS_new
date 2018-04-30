package com.belk.car.app.dto;

import java.io.Serializable;

public class AttributeBMMappingDTO implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -1825521463464329221L;

    String attributeName;
    String type;
    String bmAttributeName;
    String displayName;
    
    public String getAttributeName() {
        return attributeName;
    }
    
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getBmAttributeName() {
        return bmAttributeName;
    }
    
    public void setBmAttributeName(String bmAttributeName) {
        this.bmAttributeName = bmAttributeName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    
}
