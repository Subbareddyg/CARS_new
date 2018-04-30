package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.ProductType;

public interface ProductDao extends UniversalDao {

	List<ProductType> getAllProductTypes();

	ProductType getProductType(Long productId);

	ProductType save(ProductType product);
	
	List<ProductType> getProductTypes(long classificationId) ;
	
	void remove(ProductType product);
  
}
