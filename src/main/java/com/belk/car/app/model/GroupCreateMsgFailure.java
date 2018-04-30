package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table (name="GROUP_CREATE_MSG_FAILURE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class GroupCreateMsgFailure extends BaseAuditableModel implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5437768996774266723L;

    private String styleOrin;
    private String groupCreMsg;
    private String processedFlag;
    private Date updatedTimestamp;
    
    @Id
    @Column(name="STYLE_ORIN",nullable=false)
    public String getStyleOrin() {
        return styleOrin;
    }
    
    public void setStyleOrin(String styleOrin) {
        this.styleOrin = styleOrin;
    }
    
    @Lob
    @Column(name="GROUPCRE_MSG",nullable=false)
    public String getGroupCreMsg() {
        return groupCreMsg;
    }
    
    public void setGroupCreMsg(String groupCreMsg) {
        this.groupCreMsg = groupCreMsg;
    }
    
    @Column(name="PROCESSED_FLAG",nullable=true)
    public String getProcessedFlag() {
        return processedFlag;
    }
    
    public void setProcessedFlag(String processedFlag) {
        this.processedFlag = processedFlag;
    }
    
    @Column(name = "UPDATED_TIMESTAMP", nullable=false)
    public Date getUpdatedTimestamp() {
        return this.updatedTimestamp;
    }

    public void setUpdatedTimestamp(Date updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
}
