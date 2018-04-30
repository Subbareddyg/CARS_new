package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class CarsPIMAttributeMappingId implements java.io.Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -4559322809486031139L;
    private long pimAttrId;
    private long deptId;
    private long classId;
    private long prodTypeId;
    private long attrId;
    
    public CarsPIMAttributeMappingId(){}
    
    public CarsPIMAttributeMappingId(long pimAttrId,long deptId,long classId,long prodTypeId,long attrId){
        this.pimAttrId = pimAttrId;
        this.classId = classId;
        this.deptId = deptId;
        this.prodTypeId = prodTypeId;
        this.attrId = attrId;
    }
    
    @Column (name="PIM_ATTR_ID", nullable = false, precision = 12, scale = 0)
    public long getPimAttrId() {
        return pimAttrId;
    }
    public void setPimAttrId(long pimAttrId) {
        this.pimAttrId = pimAttrId;
    }
    
    @Column (name="DEPT_ID", nullable = false, precision = 12, scale = 0)
    public long getDeptId() {
        return deptId;
    }
    public void setDeptId(long deptId) {
        this.deptId = deptId;
    }
    
    @Column(name="CLASS_ID", nullable = false, precision = 12, scale = 0)
    public long getClassId() {
        return classId;
    }
    public void setClassId(long classId) {
        this.classId = classId;
    }
    
    @Column(name="PRODUCT_TYPE_ID", nullable = false, precision = 12, scale = 0)
    public long getProdTypeId() {
        return prodTypeId;
    }
    public void setProdTypeId(long prodTypeId) {
        this.prodTypeId = prodTypeId;
    }
    
    @Column(name="ATTR_ID", nullable = false, precision = 12, scale = 0)
    public long getAttrId() {
        return attrId;
    }
    public void setAttrId(long attrId) {
        this.attrId = attrId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (attrId ^ (attrId >>> 32));
        result = prime * result + (int) (classId ^ (classId >>> 32));
        result = prime * result + (int) (deptId ^ (deptId >>> 32));
        result = prime * result + (int) (pimAttrId ^ (pimAttrId >>> 32));
        result = prime * result + (int) (prodTypeId ^ (prodTypeId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CarsPIMAttributeMappingId other = (CarsPIMAttributeMappingId) obj;
        if (attrId != other.attrId)
            return false;
        if (classId != other.classId)
            return false;
        if (deptId != other.deptId)
            return false;
        if (pimAttrId != other.pimAttrId)
            return false;
        if (prodTypeId != other.prodTypeId)
            return false;
        return true;
    }


    

}
