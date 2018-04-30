package com.belk.car.app.service;


import java.util.List;
import java.util.Map;

import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;

import com.belk.car.app.dto.PackDTO;
import com.belk.car.app.dto.SkuDTO;
import com.belk.car.app.dto.StyleDTO;
import com.belk.car.app.dto.vendorStylePIMAttributeDTO;
import com.belk.car.app.model.CarsPimItemUpdate;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.PIMAttribute;
import com.belk.car.app.model.PoUnitDetail;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.product.integration.response.data.AttributeData;



public interface PIMAttributeManager extends UniversalManager {
	
Map<String, StyleDTO> getStylesDetailsFromSMART(List<String> vStyleOrinList) throws Exception;

VendorStyle syncPIMAttributes(VendorStyle vendorStyle,List<vendorStylePIMAttributeDTO> vsPimAttrlist,User systemUser,boolean isReterivePIMAttr) throws Exception;

List<PIMAttribute> getAllPIMAttributesFromCars();

Object save(Object ob);	

List<PIMAttribute> getPIMAttributesFromCars(String searchField);

Map<String, SkuDTO> getSKUDetailsFromSMART(List<String> skuList) throws Exception;

List<vendorStylePIMAttributeDTO> getVendorStylePimAttribute(String VendorStyle);

PIMAttribute getPIMAttributeByID(String pimId);

int updatePIMAttributes(String ids,String flagNo,String user);

void refreshCarPIMAttributes(String carId,User systemUser);

void refreshAdditionalCarPIMAttributes(String carId,User systemUser);

public void refreshCarPIMAttributesForPattern(String carId, User systemUser);

public void refreshCarPIMAttributesForPattern(Car car, VendorStyle defaultChild, User Systemuser) throws Exception;

public void refreshCarPIMAttributesForPatternChildren(Car car, User user) throws Exception;

StyleDTO getManualCarStyleFromSMART(String vendornumber,String vendorStyleNumber,String colorCode,String sizecode) throws Exception;

List<StyleDTO> getPackDetailsFromSMART(List<PackDTO> packDTOlist) throws Exception;

public List<PoUnitDetail> getSkuExistInPOUnitDetails(String belkSKU);

public VendorStyle getVendorStyleByOrin(Long orinNumber);

public VendorStyle getLastUpdatedVendorStyleByOrin(Long orinNumber);

public VendorSku getVendorSkuByOrin(Long orinNumber);

public List<CarsPimItemUpdate> getAllUpdatedItemsFromPim(int batchsize);

public void markAllPimItemUpdatesAsProcessedByCar(List<CarsPimItemUpdate> items);

public void markAllPimItemUpdatesAsFailed(List<CarsPimItemUpdate> items);

public void resetFlagsForPimItemUpdate(CarsPimItemUpdate item);

public int getProcessedPimItemUpdateCount();

public Map<Long, List<AttributeData>> getAdditionalStylesDetailsFromSMART(Car car,List<String> vStyleOrinList,String process) throws Exception;

public Map<Long, List<AttributeData>> getAdditionalSkuDetailsFromSMART(Car car,List<String> vSkuOrinList, User systemUser) throws Exception;

public Map<Long, List<AttributeData>> getAdditionalPackDetailsFromSMART(Car car,List<PackDTO> packList,String process) throws Exception;

public Map<Long, List<AttributeData>> getAdditionalGroupDetailsFromSMART(List<String> groupIds, boolean isConvertedGrouping) throws Exception;


public void processAdditionalPimAttributes(List<Car> cars,User user) throws Exception;

public void processAdditionalPimAttributesForPattern(Car patternCar, List<String> styleOrins, List<String> skuUpcs, User user) throws Exception;

public void processAdditionalPackPimAttributes(List<Car> cars,User user) throws Exception;

public void syncAdditionalPIMAttributes(Car car,Map<Long,List<AttributeData>> pimAttrMap,String type,User systemUser) throws Exception;

public void syncAdditionalPIMAttributesForUpdate(Car car,Map<Long, List<AttributeData>> styleAttrMap,String type,User systemUser)throws Exception;

public void syncAdditionalPIMAttributesForGroup(Car car, Map<Long, List<AttributeData>> groupAttrMap, String type, User systemUser) throws Exception;

public boolean updateDropshipAttributeForSku(VendorSku sku, String value, User systemUser);

public void savePetReopenDetails(Long orin,String vendorNumber,String vendorStyleNumber,String petCreateTimestamp,String format,String type) throws Exception;

public VendorStyle getDefaultChildOfPatternCar(Car patternCar);

}
