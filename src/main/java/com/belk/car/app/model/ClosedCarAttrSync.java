package com.belk.car.app.model;

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
@Table(name = "CLOSED_CAR_ATTR_SYNC")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClosedCarAttrSync extends BaseAuditableModel {

    private static final long serialVersionUID = -2996370987511979642L;

    private long carId;
    @Temporal(TemporalType.DATE)
    private Date lastUpdatedTimestamp;
    private String jobProcessed;
    
    public ClosedCarAttrSync(){
        
    }
    
    @Id
    @Column(name = "CAR_ID", unique = true, nullable = false)
    public long getCarId() {
        return carId;
    }
    public void setCarId(long carId) {
        this.carId = carId;
    }
    @Column(name = "LAST_UPDATED_TIMESTAMP", unique = false, nullable = true)
    public Date getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }
    public void setLastUpdatedTimestamp(Date lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }
    @Column(name = "JOB_PROCESSED", unique = false, nullable = true)
    public String getJobProcessed() {
        return jobProcessed;
    }
    public void setJobProcessed(String jobProcessed) {
        this.jobProcessed = jobProcessed;
    }
}
