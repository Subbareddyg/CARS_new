package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name = "PO_UNIT_DETAIL")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class PoUnitDetail  extends BaseAuditableModel implements java.io.Serializable{

	private static final long serialVersionUID = 9122113643944942157L;
	private long poSeqId;
	private String poNumber;
	private String belkSku;
	private double poUnitCost;
	private double poUnitRetail;
	private long orderQty;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PO_UNIT_DETAIL_SEQ")
	@javax.persistence.SequenceGenerator(name = "PO_UNIT_DETAIL_SEQ", sequenceName = "PO_UNIT_DETAIL_SEQ", allocationSize = 1)
	@Column(name = "PO_SEQ_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getPoSeqId() {
		return poSeqId;
	}
	public void setPoSeqId(long poSeqId) {
		this.poSeqId = poSeqId;
	}
	
	@Column(name = "PO_NBR", nullable = false)
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	
	@Column(name = "BELK_SKU", length = 13, nullable = false, unique = true)
	public String getBelkSku() {
		return belkSku;
	}
	public void setBelkSku(String belkSku) {
		this.belkSku = belkSku;
	}
	
	@Column(name = "PO_UNIT_COST", nullable = true, precision = 7, scale = 2)
	public double getPoUnitCost() {
		return poUnitCost;
	}
	public void setPoUnitCost(double poUnitCost) {
		this.poUnitCost = poUnitCost;
	}
	
	@Column(name = "PO_UNIT_RETAIL", nullable = true, precision = 7, scale = 2)
	public double getPoUnitRetail() {
		return poUnitRetail;
	}
	public void setPoUnitRetail(double poUnitRetail) {
		this.poUnitRetail = poUnitRetail;
	}
	
	@Column(name = "ORDER_QTY", nullable = true, precision = 7, scale = 0)
	public long getOrderQty() {
		return orderQty;
	}
	public void setOrderQty(long orderQty) {
		this.orderQty = orderQty;
	}
	
	@Override
    public boolean equals(Object obj) {
    	if(this== null && obj==null){
    		return true;
    	}
        if (obj == null) {
            return false;
        }
        if (! (obj instanceof PoUnitDetail)) {
            return false;
        }
        final PoUnitDetail other = (PoUnitDetail) obj;
        if (this.belkSku != other.getBelkSku()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 4;
        hash = 78 * hash + (int) (this.poSeqId ^ (this.poSeqId >>> 48));
        return hash;
    }
    
    public String toString(){
    	return " Po details of Belk Sku: " + belkSku;
    }
}
