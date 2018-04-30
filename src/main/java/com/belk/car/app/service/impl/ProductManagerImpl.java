/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.appfuse.service.impl.UniversalManagerImpl;
import org.springframework.dao.DataIntegrityViolationException;

import com.belk.car.app.dao.ProductDao;
import com.belk.car.app.exceptions.ProductTypeExistsException;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.service.ProductManager;

/**
 * @author antoniog
 *
 */
public class ProductManagerImpl extends UniversalManagerImpl implements
		ProductManager {

	private ProductDao productDao;

	/**
	 * @param productDao the productDao to set
	 */
	public void setProductDao(ProductDao productDao) {
		this.productDao = productDao;
	}

	public List<ProductType> getAllProductTypes() {		
		return productDao.getAllProductTypes();
	}

	public List<ProductType> getProductTypes(long classificationId) {		
		return productDao.getProductTypes(classificationId);
	}

	public ProductType getProductType(Long productId) {		
		return productDao.getProductType(productId);
	}

	public ProductType save(ProductType product) throws ProductTypeExistsException {	
		
		try {
			return productDao.save(product);
		}catch (DataIntegrityViolationException e) {          
            log.warn(e.getMessage());
            throw new ProductTypeExistsException("Product Type Name '" + product.getName() + "' already exists!");
        } catch (EntityExistsException e) { // needed for JPA         
            log.warn(e.getMessage());
            throw new ProductTypeExistsException("Product Type Name '" + product.getName() + "' already exists!");
        } 
	}

	public void remove(ProductType product) {
		 productDao.remove(product);
	}

}
