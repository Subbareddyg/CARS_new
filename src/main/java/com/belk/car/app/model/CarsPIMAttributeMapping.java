package com.belk.car.app.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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
@Table (name="CARS_PIM_ATTRIBUTE_MAPPING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class CarsPIMAttributeMapping extends BaseAuditableModel implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1055632342406143978L;
	
	private CarsPIMAttributeMappingId id;	

    private String carTableNm;
	
	public CarsPIMAttributeMapping(){
		
	}
	
	@EmbeddedId
    @AttributeOverrides( {
        @AttributeOverride(name = "pimAttrId", column = @Column(name = "PIM_ATTR_ID", nullable = false, precision = 12, scale = 0)),
        @AttributeOverride(name = "deptId", column = @Column(name = "DEPT_ID", nullable = false, precision = 12, scale = 0)),
        @AttributeOverride(name = "classId", column = @Column(name = "CLASS_ID", nullable = false, precision = 12, scale = 0)),
        @AttributeOverride(name = "prodTypeId", column = @Column(name = "PRODUCT_TYPE_ID", nullable = false, precision = 12, scale = 0)),
        @AttributeOverride(name = "attrId", column = @Column(name = "ATTR_ID", nullable = false, precision = 12, scale = 0))})
    public CarsPIMAttributeMappingId getId() {
        return id;
    }

    public void setId(CarsPIMAttributeMappingId id) {
        this.id = id;
    }
	
	@Column(name="CAR_TABLE_NAME",nullable=false)
	public String getCarTableNm() {
		return carTableNm;
	}
	public void setCarTableNm(String carTableNm) {
		this.carTableNm = carTableNm;
	}

}
