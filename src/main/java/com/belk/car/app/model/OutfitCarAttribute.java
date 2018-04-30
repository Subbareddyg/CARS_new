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
@Table(name= "OUTFIT_CAR_ATTRIBUTE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all") 
public class OutfitCarAttribute extends BaseAuditableModel implements Serializable {
	
	private static final long serialVersionUID = 7621576945379912345L;
	private long outfitCarAttributeId;
	private  Attribute attribute;
	
	private CarAttribute carAttribute;
	
	public OutfitCarAttribute(){
		
	}
	
	 @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OUTFIT_CAR_ATTRIBUTE_SEQ_GEN")
     @javax.persistence.SequenceGenerator(name = "OUTFIT_CAR_ATTRIBUTE_SEQ_GEN", sequenceName = "OUTFIT_CAR_ATTRIBUTE_SEQ")
     @Column(name = "OUTFIT_CAR_ATTR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getOutfitCarAttributeId() {
		return outfitCarAttributeId;
	}

	public void setOutfitCarAttributeId(long outfitCarAttributeId) {
		this.outfitCarAttributeId = outfitCarAttributeId;
	}
	
	 @ManyToOne(fetch = FetchType.LAZY)
     @JoinColumn(name = "ATTR_ID", nullable = false)
	public Attribute getAttribute() {
		return attribute;
	}

	public void setAttribute(Attribute attribute) {
		this.attribute = attribute;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CAR_ATTR_ID", nullable = false)
	public CarAttribute getCarAttribute() {
		return carAttribute;
	}

	public void setCarAttribute(CarAttribute carAttribute) {
		this.carAttribute = carAttribute;
	}

	 
    @Override
    public boolean equals(Object obj) {
        if (obj == null || this.attribute == null || this.carAttribute == null) {
            return false;
        }
        if (! (obj instanceof OutfitCarAttribute)) {
            return false;
        }
        final OutfitCarAttribute other = (OutfitCarAttribute) obj;
        if(other.attribute == null || other.carAttribute == null){
        	return false;
        }
        if (this.outfitCarAttributeId != other.outfitCarAttributeId ||
        		this.attribute.getAttributeId()!= other.attribute.getAttributeId() ||
        		this.carAttribute.getCarAttrId() != other.carAttribute.getCarAttrId()) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (int) (this.outfitCarAttributeId ^ (this.outfitCarAttributeId >>> 32));
        return hash;
    }

    public String toString(){
    	return "outfitCarAttributeId: "+ Long.toString(outfitCarAttributeId);
    }
    	
	
	

}
