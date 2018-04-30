package com.belk.car.app.dao.hibernate;

import com.belk.car.app.Constants;
import com.belk.car.app.dao.PatternAndCollectionDao;
import com.belk.car.app.model.CarsPimGroupMapping;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.GroupCreateMsgFailure;
import com.belk.car.app.model.VendorStyle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.dao.DataAccessException;

public class PatternAndCollectionDaoHibernate extends UniversalDaoHibernate implements PatternAndCollectionDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<CarsPimGroupMapping> getValidPIMGroupTypes() {
        List<CarsPimGroupMapping> pimGroupMappings = getHibernateTemplate().find(
                "FROM CarsPimGroupMapping");
        if (pimGroupMappings == null) {
            pimGroupMappings = new ArrayList<CarsPimGroupMapping>();
        }
        return pimGroupMappings;
    }

    @Override
    @SuppressWarnings("unchecked")
    public VendorStyle getVendorStyle(String vendorNumber, String vendorStyleNumber) {
        String[] args = {vendorNumber, vendorStyleNumber};
        List<VendorStyle> vendorStyles = getHibernateTemplate().find(
                "FROM VendorStyle vs where vs.vendorNumber = ? and vs.vendorStyleNumber = ?", args);
        
        if (vendorStyles!=null && !vendorStyles.isEmpty()) {
            return vendorStyles.get(0);
        }
        return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public VendorStyle getVendorStyleByGroupId(Long groupId) {
        List<VendorStyle> vendorStyles = getHibernateTemplate().find(
                "FROM VendorStyle vs where vs.groupId = ?", groupId);
        
        if (vendorStyles!=null && !vendorStyles.isEmpty()) {
            return vendorStyles.get(0);
        }
        return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public VendorStyle getVendorStyleByProductCode(String productCode) {
        List<VendorStyle> vendorStyles = getHibernateTemplate().find(
                "FROM VendorStyle vs left outer join fetch vs.vendorStylePIMAttribute as vspa where vs.vendorNumber || vs.vendorStyleNumber = ?", productCode);
        
        if (vendorStyles!=null && !vendorStyles.isEmpty()) {
            return vendorStyles.get(0);
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public Classification getClassificationByClassNumber(short classNumber) {
        List<Classification> classifications = getHibernateTemplate().find(
                "from Classification cls where belkClassNumber = ?",classNumber);
        
        if (classifications!=null && !classifications.isEmpty()) {
            return classifications.get(0);
        }
        return null;
    }
    
    public void removeCollectionSkus(String productCode, List<String> skus) {
            try {
                StringBuffer buffer = new StringBuffer("DELETE FROM COLLECTION_SKUS COLL");
                buffer.append(" WHERE COLL.PROD_CODE=:productCode AND COLL.SKU_CODE in (:SKUS)");
                SessionFactory sf = getHibernateTemplate().getSessionFactory();
                Session session = sf.getCurrentSession();

                try {
                        SQLQuery query = session.createSQLQuery(buffer.toString());
                        query.setString("productCode", productCode);
                        query.setParameterList("SKUS", skus);
                        query.executeUpdate();
                        getHibernateTemplate().flush();
                }
                catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Exception deleting from header table", e);
                    }
                }
            }
            catch (DataAccessException de) {
                if (log.isDebugEnabled()) {
                    log.debug("Problem saving header", de);
                }
            }
    }
    
    public void saveGroupingFailureInDB(List<String> styleOrins,String groupingMessage) {
        GroupCreateMsgFailure gcm = null;
        for (String orin : styleOrins) {
            gcm = new GroupCreateMsgFailure();
            gcm.setStyleOrin(orin);
            gcm.setGroupCreMsg(groupingMessage);
            gcm.setProcessedFlag(Constants.FLAG_N);
            gcm.setUpdatedTimestamp(new Date());
            getHibernateTemplate().merge(gcm);
        }
        getHibernateTemplate().flush();
    }
    
    @SuppressWarnings("unchecked")
    public String getGroupingMessageForStyleOrin(String styleOrin) {
        List<GroupCreateMsgFailure> gcmList = getHibernateTemplate().find(
                "from GroupCreateMsgFailure gcmf where styleOrin = ? and processedFlag <> 'Y'",styleOrin);
        
        if (gcmList!=null && !gcmList.isEmpty()) {
            return gcmList.get(0).getGroupCreMsg();
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
    public void setGroupingFailureAsProcessed(String styleOrin, String flag) {
        List<GroupCreateMsgFailure> gcmList = getHibernateTemplate().find(
                "from GroupCreateMsgFailure gcmf where styleOrin = ?",styleOrin);
        
        if (gcmList!=null) {
            for (GroupCreateMsgFailure gcm : gcmList) {
                gcm.setUpdatedTimestamp(new Date());
                gcm.setProcessedFlag(flag);
                getHibernateTemplate().merge(gcm);
            }
        }
        getHibernateTemplate().flush();
    }
    
    public void merge(Object obj) {
        getHibernateTemplate().merge(obj);
        getHibernateTemplate().flush();
    }

    public void updateCarsPoMessageEsbRetry(List<String> orinsToRetry) {
        
        try {
            if (log.isInfoEnabled()) log.info("Flushing hibernate query state...");
            this.getHibernateTemplate().flush();
        } catch (Exception e) {
            log.warn("Handled errors in hibernate query state: " + e.getMessage());
        }
        
        if (log.isInfoEnabled()) log.info("Updating CARS_PO_MESSAGE_ESB for retrying failed po/drop messages.");
        StringBuffer orinList = new StringBuffer("{");
        for (String s : orinsToRetry) { 
            orinList.append(" ").append(s).append(" ");
        }
        orinList.append("}");
        if (log.isInfoEnabled()) log.info("Orin list: " + orinList.toString());
        
        try {
            for (int i = 2; i >= 0; i--) {
                StringBuffer buffer = new StringBuffer("UPDATE CARS_PO_MESSAGE_ESB SET CARS_RETRY_COUNT = "+(i+1)+", PROCESSED_INDICATOR = 'N' ");
                buffer.append(" WHERE CARS_RETRY_COUNT = "+i+" AND STYLE_ORIN IN (:ORINS)");
                SessionFactory sf = getHibernateTemplate().getSessionFactory();
                Session session = sf.getCurrentSession();
                
                try {
                    SQLQuery query = session.createSQLQuery(buffer.toString());
                    query.setParameterList("ORINS", orinsToRetry);
                    query.executeUpdate();
                    getHibernateTemplate().flush();
                } catch (Exception e) {
                    if (log.isErrorEnabled()) {
                        log.error("Error saving cars_po_message_esb update: ", e);
                    }
                }
            }
        } catch (DataAccessException de) {
            if (log.isDebugEnabled()) {
                log.debug("Error saving cars_po_message_esb update: ", de);
            }
        }
    }
    
}

