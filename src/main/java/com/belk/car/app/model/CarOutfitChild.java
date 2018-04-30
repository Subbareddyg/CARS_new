package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType; 
import javax.persistence.Id; 
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne; 
import javax.persistence.Table; 
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "CAR_OUTFIT_CHILD")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all") 
public class CarOutfitChild extends BaseAuditableModel implements java.io.Serializable {

        private static final long serialVersionUID = 9122113839144451257L;
        private long carOufitChildID;
        private Car outfitCar;
        private Car childCar;
        private String statusCd;
        private Vendor vendor; 
        private VendorSku defaultColorSku;
        private int priority;
        
        public CarOutfitChild() {
        }
       
        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OUTFIT_SEQ_GEN")
        @javax.persistence.SequenceGenerator(name = "OUTFIT_SEQ_GEN", sequenceName = "CAR_OUTFIT_CHILD_SEQ")
        @Column(name = "CAR_OUTFIT_CHILD_ID", unique = true, nullable = false, precision = 12, scale = 0)
        public long getCarOufitChildID() {
                return carOufitChildID;
        }
        public void setCarOufitChildID(long carOufitChildID) {
                this.carOufitChildID = carOufitChildID;
        }
       
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "OUTFIT_CAR_ID", nullable = false)
        @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
        public Car getOutfitCar() {
                return outfitCar;
        }
        public void setOutfitCar(Car outfitCar) {
                this.outfitCar = outfitCar;
        }
       
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "CHILD_CAR_ID", nullable = false)
        public Car getChildCar() {
                return childCar;
        }
        public void setChildCar(Car childCar) {
                this.childCar = childCar;
        }
       
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "DEFAULT_COLOR_SKU_ID", nullable = false)
        public VendorSku getDefaultColorSku() {
			return defaultColorSku;
		}
        public void setDefaultColorSku(VendorSku defaultColorSku) {
			this.defaultColorSku = defaultColorSku;
		}
        
        @Column(name = "PRIORITY", nullable = false, length = 3) 
        public int getPriority() {
        	return priority;
        }
        public void setPriority(int priority) {
        	this.priority = priority;
        }
        

        @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
        @JoinColumn(name = "VENDOR_ID")
        public Vendor getVendor() {
        	return vendor;
        }
        public void setVendor(Vendor vendor) {
			this.vendor = vendor;
		}
        
        @Column(name = "STATUS_CD", nullable = false, length = 20)
        public String getStatusCd() {
                return statusCd;
        }
        public void setStatusCd(String statusCd) {
                this.statusCd = statusCd;
        }
       
        @Column(name = "CREATED_BY", nullable = false, length = 100)
        public String getCreatedBy() {
                return this.createdBy;
        }

        @Column(name = "UPDATED_BY", nullable = false, length = 100)
        public String getUpdatedBy() {
                return this.updatedBy;
        }

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "CREATED_DATE", nullable = false)
        public Date getCreatedDate() {
                return this.createdDate;
        }

        @Temporal(TemporalType.TIMESTAMP)
        @Column(name = "UPDATED_DATE", nullable = false)
        public Date getUpdatedDate() {
                return this.updatedDate;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CarOutfitChild other = (CarOutfitChild) obj;
            if (this.carOufitChildID != other.carOufitChildID || this.childCar.getCarId() != other.childCar.getCarId()) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + (int) (this.carOufitChildID ^ (this.carOufitChildID >>> 32));
            return hash;
        }

}
