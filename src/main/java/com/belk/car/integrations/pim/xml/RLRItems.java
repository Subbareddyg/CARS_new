package com.belk.car.integrations.pim.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "classNumber",
    "className",
    "deptNumber",
    "deptName",
    "dmmName",
    "dmmEmail",
    "gmmName",
    "gmmEmail",
    "demandCenter",
    "demandCenterDesc"
})

@XmlRootElement(name = "RLRItems")
public class RLRItems {

    @XmlElement(name = "class_number", required = true)
    protected String classNumber;
    @XmlElement(name = "class_name", required = true)
    protected String className;
    @XmlElement(name = "dept_number", required = true)
    protected String deptNumber;
    @XmlElement(name = "dept_name", required = true)
    protected String deptName;
    @XmlElement(name = "dmm_name", required = true)
    protected String dmmName;
    @XmlElement(name = "dmm_email", required = true)
    protected String dmmEmail;
    @XmlElement(name = "gmm_name", required = true)
    protected String gmmName;
    @XmlElement(name = "gmm_email", required = true)
    protected String gmmEmail;
    @XmlElement(name = "demand_center", required = true)
    protected String demandCenter;
    @XmlElement(name = "demand_center_desc", required = true)
    protected String demandCenterDesc;

  
    public String getClassNumber() {
        return classNumber;
    }

   
    public void setClassNumber(String value) {
        this.classNumber = value;
    }

   
    public String getClassName() {
        return className;
    }

    
    public void setClassName(String value) {
        this.className = value;
    }

    public String getDeptNumber() {
        return deptNumber;
    }

    
    public void setDeptNumber(String value) {
        this.deptNumber = value;
    }

    public String getDeptName() {
        return deptName;
    }

   
    public void setDeptName(String value) {
        this.deptName = value;
    }

    
    public String getDmmName() {
        return dmmName;
    }

    
    public void setDmmName(String value) {
        this.dmmName = value;
    }

  
    public String getDmmEmail() {
        return dmmEmail;
    }

    
    public void setDmmEmail(String value) {
        this.dmmEmail = value;
    }

   
    public String getGmmName() {
        return gmmName;
    }

   
    public void setGmmName(String value) {
        this.gmmName = value;
    }

   
    public String getGmmEmail() {
        return gmmEmail;
    }

   
    public void setGmmEmail(String value) {
        this.gmmEmail = value;
    }

   
    public String getDemandCenter() {
        return demandCenter;
    }

    public void setDemandCenter(String value) {
        this.demandCenter = value;
    }

   
    public String getDemandCenterDesc() {
        return demandCenterDesc;
    }

   
    public void setDemandCenterDesc(String value) {
        this.demandCenterDesc = value;
    }

}
