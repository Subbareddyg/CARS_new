package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.belk.car.app.model.CarOutfitChild;

public class StyleAndStyleColorDTO implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -3000116631121929742L;
    private String vendor_number;
    private String vendor_style_number;
    private Set<String> styleColors = new HashSet<String>();
    private boolean isStyleColor = false;
    private Set<CarOutfitChild> carOutfitChildren = new HashSet<CarOutfitChild>();
    
    
    
    public StyleAndStyleColorDTO() {
        super();
        // TODO Auto-generated constructor stub
    }
    public String getVendorNumber() {
        return vendor_number;
    }
    public void setVendorNumber(String vendor_number) {
        this.vendor_number = vendor_number;
    }
    public String getVendorStyleNumber() {
        return vendor_style_number;
    }
    public void setVendorStyleNumber(String vendor_style_number) {
        this.vendor_style_number = vendor_style_number;
    }
    public Set<String> getStyleColors() {
        return styleColors;
    }
    public boolean isStyleColor() {
        return isStyleColor;
    }
    public void setIsStyleColor(boolean isStyleColor) {
        this.isStyleColor = isStyleColor;
    }
    public Set<CarOutfitChild> getCarOutfitChildren() {
        return carOutfitChildren;
    }
}
