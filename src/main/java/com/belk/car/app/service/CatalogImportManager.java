package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.catalog.CatalogImport;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.catalog.CatalogTemplate;

/**
 * @author vsundar
 *
 */
public interface CatalogImportManager extends UniversalManager{
	
	List<CatalogTemplate> getAllCatalogTemplates();

	CatalogTemplate getCatalogTemplate(long catalogTemplateId);
	
	CatalogImport save(CatalogImport catalogImport);

	CatalogProduct getProduct(String vendorNumber, String styleNumber) ;
	
	CatalogSku getSku(String upc) ;
	
	CatalogProduct save(CatalogProduct catalogProduct) ;
	
	CatalogSku save(CatalogSku catalogSku) ;
	
	Car copyToCar(Car car) ;
	
	Car copyToCar(CatalogProduct product, Car car) ;

	Car copyToCar(CatalogProduct product, Car car, boolean createOnly) ;

}


