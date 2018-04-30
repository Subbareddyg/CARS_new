package com.belk.car.app.service;

import java.util.List;

import org.appfuse.model.User;

import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.dto.StyleAndStyleColorDTO;
import com.belk.car.app.exceptions.GroupCreateException;
import com.belk.car.app.model.Car;
import com.belk.car.integrations.pim.xml.GroupingComponent;
import com.belk.car.integrations.pim.xml.GroupingParent;

public interface PatternAndCollectionManager {

    public void setContextParameters();
    public void populateAndSavePattern(GroupingParent parent, List<GroupingComponent> children, String vendorStyleType, VendorStyle existingVS, User user) throws GroupCreateException;
    public void populateAndSaveCollection(GroupingParent parent, List<GroupingComponent> children, String vendorStyleType, User user) throws GroupCreateException;
    public String getCarGroupTypeByPimGroupType(String pimGroupType, String carGroupTypeFromPim);
    public VendorStyle getVendorStyleIfExists(String groupId, String vendorNumber, String vendorStyleNumber, String productCode);
    public void syncCollectionProducts(VendorStyle parentStyle, List<GroupingComponent> components, User user);
    public void syncPatternProducts(VendorStyle parentStyle, List<GroupingComponent> components, String vendorStyleType, User user);
    public String syncGroupingType(VendorStyle parentStyle, GroupingParent parentGrouping, User user);
    public void syncGroupingVendorStyle(VendorStyle parentStyle, GroupingParent parentGrouping);
    public void deleteGrouping(VendorStyle groupStyle, User user);
    public void checkPriorityAndUpdate(StyleAndStyleColorDTO dto, GroupingComponent component);
    public void saveGroupingFailureInDB(List<String> styleOrins,String groupingMessage);
    public String getGroupingMessageForStyleOrin(String styleOrin);
    public void setGroupingFailureAsProcessed(String styleOrin, boolean flag);
    public Car getMergeEilgibleCar(VendorStyle pattern, List<VendorStyle> childrenToAdd);
    public void syncEffectiveStartDate(VendorStyle parent, String date, User user);

    public void updateCarsPoMessageEsbRetry(List<String> orinsToRetry); 
}
