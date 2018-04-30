package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Look table having mapping for cars to pim attributes. 
 * 
 * @author AFUSYS9
 *
 */

@Entity
@Table (name="CARS_PIM_GROUP_MAPPING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class CarsPimGroupMapping extends BaseAuditableModel implements java.io.Serializable{

    private static final long serialVersionUID = 6955395005271930724L;
    
    public static final String TYPE_GROUP_BY_SIZE = "GSG";
    public static final String TYPE_CONSOLIDATED_PRODUCT = "CPG";
    public static final String TYPE_REGULAR_COLLECTION = "RCG";
    public static final String TYPE_BEAUTY_COLLECTION = "BCG";

    private String pimGroupName;
    private String carsGroupName;
    
    public CarsPimGroupMapping(){
        
    }
    
    @Column(name="PIM_GROUP_NAME",nullable=false)
    public String getPimGroupName() {
        return pimGroupName;
    }

    public void setPimGroupName(String pimGroupName) {
        this.pimGroupName = pimGroupName;
    }
    
    @Id
    @Column(name="CAR_GROUP_NAME",nullable=false)
    public String getCarsGroupName() {
        return carsGroupName;
    }

    public void setCarsGroupName(String carsGroupName) {
        this.carsGroupName = carsGroupName;
    }
}
