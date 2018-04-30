package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.catalog.CatalogImport;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.catalog.CatalogTemplate;

public interface CatalogImportDao extends UniversalDao {

	List<CatalogTemplate> getAllCatalogTemplates();

	CatalogTemplate getCatalogTemplate(long catalogTemplateId);

	CatalogImport save(CatalogImport catalogImport);
	
	CatalogProduct getProduct(String vendorNumber, String styleNumber) ;
	
	CatalogSku getSku(String upc) ;
	
	CatalogProduct save(CatalogProduct catalogProduct) ;
	
	CatalogSku save(CatalogSku catalogSku) ;

}
