package com.belk.car.app.dao;

import java.util.List;
import org.appfuse.dao.UniversalDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;

public interface OutfitDao extends UniversalDao{

	public long getNextCarSequence();
	
	public List<Car> getAllOutfits();
	
	public List<Car> serachOutfitCars(String outfitName,String styleNumber);
	
	public Object save(Object ob);
	
    public Car getChildCarById(Long carId);
	
    public void removeObject(Object ob);
	
	public Attribute getAttributeByName(String attrName);
	
	public CarAttribute findAttributeInCar(Car c, Attribute attr);

	public Car getCarFromId(Long carId);
	
	public boolean getBMProductValue(Car c); 
	
	public CarAttribute getCarAttributeByAttributeName(Car c, String attributeName);
	
	public CarAttribute getCarAttributeByBMName(Car c, String strBMName);
	
	public List<CarAttribute>getAllOutfitCarAttributes(Car c);
	
	public VendorSku getVendorSku(long carId, String strBelkSku);

	public String getAttributeValue(Car car, Attribute attrChildProducts);
	
	public long getCarAttributeId(Car car,Attribute isSearchableData);

	public Car getChildCarForRemove(Long carId);
	
	/**
	 * 
	 * This method used to get the vendor sku 
	 * of corresponding car id from the vendorsku table.
	 * 
	 * @param car 	Passing car object
	 */
	public VendorSku getVendorSkus(Car car);
	
	/**
	 * 
	 * This method used to get list of the 
	 * attribute template type
	 */
	public List<String> getAttrTemplateType();
	
	/**
	 * 
	 * This method used to get the child car sku
	 * for  corresponding vendor style id .
	 * 
	 * @param venStyleId 	It contains vendor style object object
	 */
	public List<Car> getChildCarSkus(VendorStyle venStyleId);
	
	/**
	 * 
	 * This method used to get the child car collection sku
	 * for  corresponding product code .
	 * 
	 * @param productCode 	It contains productCode
	 */
	public List<CollectionSkus> getCollectionSkus(String productCode);
	
	/**
	 * 
	 * This method used to remove the child car collection sku
	 * for  corresponding product code .
	 * 
	 * @param productCode 	It contains productCode
	 */
	public void removeCollectionSkus(String productCode);
		
}
