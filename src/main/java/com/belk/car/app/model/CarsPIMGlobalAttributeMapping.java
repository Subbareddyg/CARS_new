package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Look table having global attributes.
 * 
 * @author AFUSYS9
 *
 */
@Entity
@Table (name="CARS_PIM_GLOBAL_ATTR_MAPPING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class CarsPIMGlobalAttributeMapping extends BaseAuditableModel implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7317680237520815888L;
	
	private long pimAttrId;
	
	private String carTableNm;
	private String columnNm;
	private Long carAttrId;
	
    public CarsPIMGlobalAttributeMapping(){
		
	}
	
	@Id
	@Column(name="PIM_ATTR_ID", unique = true, nullable = false)
	public long getPimAttrId() {
		return pimAttrId;
	}
	
	public void setPimAttrId(long pimAttrId) {
		this.pimAttrId = pimAttrId;
	}
	
	@Column(name="CAR_TABLE_NAME",nullable=false)
	public String getCarTableNm() {
		return carTableNm;
	}
	
	public void setCarTableNm(String carTableNm) {
		this.carTableNm = carTableNm;
	}
	
	@Column(name="COLUMN_NAME")
	public String getColumnNm() {
		return columnNm;
	}
	
	public void setColumnNm(String columnNm) {
		this.columnNm = columnNm;
	}

	@Column(name="CAR_ATTR_ID")
    public Long getCarAttrId() {
        return carAttrId;
    }

    public void setCarAttrId(Long carAttrId) {
        this.carAttrId = carAttrId;
    }

}
