package com.belk.car.app.service;
import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarDBPromotionChild;
import com.belk.car.app.model.DBPromotionCollectionSkus;
import com.belk.car.app.model.PromoType;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.webapp.forms.ChildCar;
public interface DBPromotionManager extends UniversalManager{
	
	public Car createDBPromotion(String name, String desc, List<Car> childCars,
			List<String> orderCars, List<String> skus,String templTypVal);
	
	public void createDBPromotionCollectionSkus(String prodCode, String skuCode);
	
	public void removeDBPromotionCollectionSkus(String productCode);

	public void createProductAttributes(Long dbPromotionCarId, String attrValue,
			String attrParentProducts);

	public void updateDBPromotionAttributes(Long skuCarid, String attrValue,
			String valueToBeUpdated);

	public List<String> getAttributeValueById();

	public void saveDBPromotion(Car dbPromotionalCar, String dbPromotionName, String dbPromotionDesc,
			List<Car> childCars, List<String> orderCars, List<String> sku);
	
	public List<Car> getAllDBPromotions();
	
	public List<Car> serachDBPromotionCars(String dbPromotionName,String styleNumber);
	
	//public Car getCarById(Long carId);
	
	public Car getChildCarById(Long carId);
	
	public void saveorUpdate(Object o);
	
	public void removeCarDBPromotionChild(CarDBPromotionChild dbPromotionChild);
  
	public Car getCarFromId(Long carId);
	
	public Attribute getAttributeByName(String attrName);
	
	public CarAttribute findAttributeInCar(Car c, Attribute attr);
	
	public boolean getBMProductValue(Car c);

	public void assignDBPromotionToVendor(Car DBPromotion, String childCarId);

	public void resyncDBPromotionAttributes(Car car, User user);

	public CarAttribute getCarAttributeByAttributeName(Car c,
			String attributeName);

	public List<CarAttribute> getAllDBPromotionCarAttributes(Car car);
	
	public CarAttribute getCarAttributeByBMName(Car c, String strBMName);

	public String getAttributeValue(Car dbPromotionCar, Attribute attrChildProducts);
	
	public long getCarAttributeId(Car car,Attribute isSearchableData);

	public ChildCar getViewChildCarDetails(Car car, CarDBPromotionChild child,
			List<String> lstchildProductSKUs);

	public Car getChildCarForRemove(Long carId);

	public List<Car> getChildCarSkus(VendorStyle venStyleId);
	
	public List<DBPromotionCollectionSkus> getDBPromotionCollectionSkus(String productCode);	
	public List<String> getPromoTemplateTypes();
	public PromoType findPromoType(long carId);
	public void savePromoType(PromoType promoType);
	
	
}
