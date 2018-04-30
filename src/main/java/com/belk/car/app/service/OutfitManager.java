package com.belk.car.app.service;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarOutfitChild;
import com.belk.car.app.model.CollectionSkus;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.webapp.forms.ChildCar;

public interface OutfitManager extends UniversalManager {

	public Car createOutfit(String name, String desc, List<Car> childCars,
			List<String> orderCars, List<String> skus,String templateType);
	
	public void createCollectionSkus(String prodCode, String skuCode);
	
	public void removeCollectionSkus(String productCode);

	public void createProductAttributes(Long outfiltCarId, String attrValue,
			String attrParentProducts);

	public void updateCollectionSkuAttributes(Long skuCarid, String attrValue,
			String valueToBeUpdated);

	public List<String> getAttributeValueById();

	public void saveOutfit(Car outfitCar, String outfitName, String outfitDesc,
			List<Car> childCars, List<String> orderCars, List<String> sku,String templateType);
	
	public List<Car> getAllOutfits();
	
	public List<Car> serachOutfitCars(String outfitName,String styleNumber);
	
	//public Car getCarById(Long carId);
	
	public Car getChildCarById(Long carId);
	
	public void saveorUpdate(Object o);
	
	public void removeCarOufitChild(CarOutfitChild outfitChild);
  
	public Car getCarFromId(Long carId);
	
	public Attribute getAttributeByName(String attrName);
	
	public CarAttribute findAttributeInCar(Car c, Attribute attr);
	
	public boolean getBMProductValue(Car c);

	public void assignOutfitToVendor(Car OutfitCar, String childCarId);

	public void resyncOutfitAttributes(Car car, User user);

	public CarAttribute getCarAttributeByAttributeName(Car c,
			String attributeName);

	public List<CarAttribute> getAllOutfitCarAttributes(Car car);
	
	public CarAttribute getCarAttributeByBMName(Car c, String strBMName);

	public String getAttributeValue(Car outfitCar, Attribute attrChildProducts);
	
	public long getCarAttributeId(Car car,Attribute isSearchableData);

	public ChildCar getViewChildCarDetails(Car car, CarOutfitChild child,
			List<String> lstchildProductSKUs);

	public Car getChildCarForRemove(Long carId);

	public List<Car> getChildCarSkus(VendorStyle venStyleId);
	
	public List<CollectionSkus> getCollectionSkus(String productCode);

}
