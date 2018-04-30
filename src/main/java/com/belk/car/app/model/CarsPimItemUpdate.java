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

@Entity
@Table(name = "CARS_PIM_ITEM_UPDATE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CarsPimItemUpdate extends BaseAuditableModel implements Serializable {

    private static final long serialVersionUID = -551831293436979246L;

    public static final String TYPE_STYLE = "Style";
    public static final String TYPE_SKU = "Sku";
    public static final String TYPE_PACK = "Complex Pack";
    public static final String TYPE_STYLE_COLOR = "StyleColor";
    public static final String TYPE_PACK_COLOR = "PackColor";
    public static final String FLAG_FAILED = "F" ;

    private long id;
    private String vendorNumber;
    private String vendorStyleNumber;
    private String orin;
    private String itemType;
    private String processedByCar;

    @Temporal(TemporalType.DATE)
    private Date updatedTimestamp;
    private String dropshipUpdate;
    private String belkUpc;
    private String MerchHierarchyFlag;
    private Date petUpdatedTimestamp;

    public CarsPimItemUpdate() {
    }

    @Id
    @Column(name = "ITEM_ID", unique = true, nullable = false)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "VENDOR_NUMBER", unique = false, nullable = true)
    public String getVendorNumber() {
        return vendorNumber;
    }

    public void setVendorNumber(String vendorNumber) {
        this.vendorNumber = vendorNumber;
    }

    @Column(name = "VENDOR_STYLE_NUMBER", unique = false, nullable = true)
    public String getVendorStyleNumber() {
        return this.vendorStyleNumber;
    }

    public void setVendorStyleNumber(String vendorStyleNumber) {
        this.vendorStyleNumber = vendorStyleNumber;
    }

    @Column(name = "ORIN", unique = false, nullable = true)
    public String getOrin() {
        return this.orin;
    }

    public void setOrin(String orin) {
        this.orin = orin;
    }

    @Column(name = "TYPE", unique = false, nullable = true)
    public String getItemType() {
        return this.itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    @Column(name = "PROCESSED_BY_CARS", unique = false, nullable = true)
    public String getProcessedByCar() {
        return this.processedByCar;
    }

    public void setProcessedByCar(String processedByCar) {
        this.processedByCar = processedByCar;
    }

    @Column(name = "UPDATED_TIMESTAMP", unique = false, nullable = true)
    public Date getUpdatedTimestamp() {
        return this.updatedTimestamp;
    }

    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    @Column(name = "DROP_SHIP_UPDATE", unique = false, nullable = true)
    public String getDropshipUpdate() {
        return this.dropshipUpdate;
    }

    public void setDropshipUpdate(String dropshipUpdate) {
        this.dropshipUpdate = dropshipUpdate;
    }

    @Column(name = "BELK_UPC", unique = false, nullable = true)
    public String getBelkUpc() {
        return this.belkUpc;
    }

    public void setBelkUpc(String belkUpc) {
        this.belkUpc = belkUpc;
    }

    @Column(name = "MERCH_HIERARCHY_CHANGED", unique = false, nullable = true)
    public String getMerchHierarchyFlag() {
        return MerchHierarchyFlag;
    }

    public void setMerchHierarchyFlag(String merchHierarchyFlag) {
        MerchHierarchyFlag = merchHierarchyFlag;
    }
    
    @Column(name = "PET_UPDATED_TIMESTAMP", unique = false, nullable = true)
    public Date getPetUpdatedTimestamp() {
        return petUpdatedTimestamp;
    }

    public void setPetUpdatedTimestamp(Date petUpdatedTimestamp) {
        this.petUpdatedTimestamp = petUpdatedTimestamp;
    }
}
