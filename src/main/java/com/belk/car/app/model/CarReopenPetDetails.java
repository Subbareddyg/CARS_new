/**
 * 
 */
package com.belk.car.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.product.integration.response.data.AttributeData;

/**
 * Model class to persist cars reopen PET details to be used in updateItem process.
 * @author afusys9
 *
 */
@Entity
@Table(name = "CARS_REOPEN_PET_DETAILS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class CarReopenPetDetails extends BaseAuditableModel implements Serializable {

    private static final long serialVersionUID = -701718164023103998L;

    private Long orinNumber;
    private String vendorNumber;
    private String vendorStyleNumber;
    private String type;
    
    @Temporal(TemporalType.DATE)
    private Date petCreateTimeStamp;
    
    @Temporal(TemporalType.DATE)
    private Date lastPetUpdateTimeStamp;
    
    public CarReopenPetDetails(){
        
    }
    
    @Id
    @Column(name = "ORIN", precision = 12, scale = 0, nullable = false)
    public Long getOrinNumber() {
        return orinNumber;
    }
    
    public void setOrinNumber(Long orinNumber) {
        this.orinNumber = orinNumber;
    }
    
    @Column(name = "VENDOR_NUMBER", length = 50)
    public String getVendorNumber() {
        return vendorNumber;
    }
    
    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }
    
    @Column(name = "VENDOR_STYLE_NUMBER", length = 100)
    public String getVendorStyleNumber() {
        return vendorStyleNumber;
    }
    
    public void setVendorStyleNumber(String vendorStyleNumber) {
        this.vendorStyleNumber = vendorStyleNumber;
    }
    
    @Column(name = "TYPE", nullable = true)
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Column(name = "PET_CREATE_TIMESTAMP", unique = false, nullable = false)
    public Date getPetCreateTimeStamp() {
        return petCreateTimeStamp;
    }
    
    public void setPetCreateTimeStamp(Date petCreateTimeStamp) {
        this.petCreateTimeStamp = petCreateTimeStamp;
    }
    
    @Column(name = "LAST_PET_UPDATE_TIMESTAMP",unique = false, nullable = false)
    public Date getLastPetUpdateTimeStamp() {
        return lastPetUpdateTimeStamp;
    }
    
    public void setLastPetUpdateTimeStamp(Date lastPetUpdateTimeStamp) {
        this.lastPetUpdateTimeStamp = lastPetUpdateTimeStamp;
    }
    
    @Override
    public boolean equals(Object obj) {
        // TODO Auto-generated method stub
        if(this==null && obj==null){
            return true;
        }
        
        if(obj == null){
            return false;
        }
        if(obj instanceof CarReopenPetDetails)
        {
            CarReopenPetDetails temp = (CarReopenPetDetails) obj;
            if(this.orinNumber.equals(temp.orinNumber) || (this.vendorNumber!=null && this.vendorStyleNumber!=null && this.vendorNumber.equalsIgnoreCase(temp.vendorNumber) && this.vendorStyleNumber.equalsIgnoreCase(temp.vendorStyleNumber))){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {

        return (Long.valueOf(this.orinNumber).hashCode() * this.type.length());
    }
    
}
