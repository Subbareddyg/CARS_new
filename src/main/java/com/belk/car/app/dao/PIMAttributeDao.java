package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.model.User;

import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.CarsPimItemUpdate;
import com.belk.car.app.model.PIMAttribute;
import com.belk.car.app.model.PoUnitDetail;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.VendorStylePIMAttribute;


public interface PIMAttributeDao extends CachedQueryDao {

    public enum Tables {CAR_ATTRIBUTE,CAR_SKU_ATTRIBUTE,VENDOR,VENDOR_STYLE,VENDOR_SKU};
	List<PIMAttribute> getAllPIMAttributes();

	Object save(Object ob);
		
	List<PIMAttribute >searchPIMAttributes(String attributeName);

	PIMAttribute getPIMAttributeByID(String pimId);
	
	PIMAttribute getPIMAttributeByName(String attributeName);

	int updatePIMAttributes(String pimids);

	PIMAttribute savePimAttribute(PIMAttribute patt);

	void delete(Object ob);

	VendorStyle getvendorStyle(long orinNumber);
	
	VendorStyle getLastUpdatedVendorStyle(long orinNumber);

	void saveVSPimAttributes(List<VendorStylePIMAttribute> vendorStylePIMAttributelist);

	void saveVendoorStyle(VendorStyle vendorStyle);

	public void deletevspalist(List<VendorStylePIMAttribute> vsPimAttList);
	
	public void deletevskupalist(List<VendorSkuPIMAttribute> vskuPimAttList);

	public List<PoUnitDetail> getSkuExistInPOUnitDetailsDao(String belkSKU);
	
	public List<CarsPimItemUpdate> getAllUpdatedItems(final int batchSize);
	
	public void setProcessedByCarFlagForAllItems(List<CarsPimItemUpdate> items, String newValue);
	
	public void resetFlagsForPimItemUpdate(CarsPimItemUpdate item);
	
	public int getProcessedPimItemUpdateCount();
	
    public List<CarAttribute> getCarAttributesByAttrId(Car car,long attrId)throws Exception;
    
    public List<CarSkuAttribute> getCarSkuAttributesByAttrId(VendorSku vSku,long attrId)throws Exception;
    
    void saveVendorSku(VendorSku vendorSku);
    
    public Attribute getAttributeById(Long attrId);

    public VendorStyle getVendorStyleByGroupId(Long groupId);
}
