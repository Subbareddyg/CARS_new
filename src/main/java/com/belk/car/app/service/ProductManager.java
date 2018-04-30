package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.exceptions.ProductTypeExistsException;
import com.belk.car.app.model.ProductType;

public interface ProductManager extends UniversalManager{
	
	List<ProductType> getAllProductTypes();

	List<ProductType> getProductTypes(long classificationId);

	ProductType getProductType(Long productId);

	ProductType save(ProductType product) throws ProductTypeExistsException;
	
	void remove(ProductType product);
}
