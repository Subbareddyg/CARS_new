package com.belk.car.app.model;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name= "PROMO_CAR_ATTRIBUTE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all") 
public class DBPromotionCarAttribute extends BaseAuditableModel implements Serializable {
	
	//private static final long serialVersionUID = 7621576945379912345L;
	private long dbPromotionCarAttributeId;
	private  Attribute attribute;
	
	private CarAttribute carAttributeForPromo;
	
	public DBPromotionCarAttribute(){
		
	}
	
	 @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROMO_CAR_ATTRIBUTE_SEQ_GEN")
     @javax.persistence.SequenceGenerator(name = "PROMO_CAR_ATTRIBUTE_SEQ_GEN", sequenceName = "PROMO_CAR_ATTRIBUTE_SEQ")
     @Column(name = "PROMO_CAR_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getDBPromotionCarAttributeId() {
		return dbPromotionCarAttributeId;
	}

	public void setDBPromotionCarAttributeId(long dbPromotionCarAttributeId) {
		this.dbPromotionCarAttributeId = dbPromotionCarAttributeId;
	}
	
	 @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "ATTR_ID", nullable = false)
	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	

	 
    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.attribute == null || this.carAttributeForPromo == null) {
            return false;
        }
        if (! (obj instanceof DBPromotionCarAttribute)) {
            return false;
        }
        final DBPromotionCarAttribute other = (DBPromotionCarAttribute) obj;
        if(other.attribute == null || other.carAttributeForPromo == null){
        	return false;
        }
        if (this.dbPromotionCarAttributeId != other.dbPromotionCarAttributeId ||
        		this.attribute.getAttributeId()!= other.attribute.getAttributeId() ||
        		this.carAttributeForPromo.getCarAttrId() != other.carAttributeForPromo.getCarAttrId()) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.dbPromotionCarAttributeId ^ (this.dbPromotionCarAttributeId >>> 32));
        return hash;
    }

    public String toString(){
    	return "dbPromotionCarAttributeId: "+ Long.toString(dbPromotionCarAttributeId);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_ATTR_ID", nullable = false)
	public CarAttribute getCarAttributeForPromo() {
		return carAttributeForPromo;
	}

	public void setCarAttributeForPromo(CarAttribute carAttributeForPromo) {
		this.carAttributeForPromo = carAttributeForPromo;
	}
    	
	
	

}
