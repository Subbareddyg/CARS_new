package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.CarsPimGroupMapping;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.VendorStyle;

public interface PatternAndCollectionDao extends UniversalDao {
    
    List<CarsPimGroupMapping> getValidPIMGroupTypes();
    VendorStyle getVendorStyle(String vendorNumber, String vendorStyleNumber);
    VendorStyle getVendorStyleByGroupId(Long groupId);
    VendorStyle getVendorStyleByProductCode(String productCode);
    Classification getClassificationByClassNumber(short classNumber);
    void removeCollectionSkus(String productCode, List<String> skus);
    void saveGroupingFailureInDB(List<String> styleOrins,String groupingMessage);
    String getGroupingMessageForStyleOrin(String styleOrin);
    void setGroupingFailureAsProcessed(String styleOrin, String flag);
    void merge(Object obj);

    public void updateCarsPoMessageEsbRetry(List<String> orinsToRetry); 
}
