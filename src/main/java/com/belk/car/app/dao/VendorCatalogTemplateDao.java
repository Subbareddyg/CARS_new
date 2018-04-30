package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;

import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;

public interface VendorCatalogTemplateDao extends UniversalDao {

	List<VendorCatalogTemplate> getVendorCatalogTemplates();
	VendorCatalogTemplate getVendorCatalogTemplate(Long vendorCatalogTemplateId);
	VendorCatalogTemplate save(VendorCatalogTemplate vendorCatalogTemplate);
	VendorCatalog saveCatalog(VendorCatalog vendorCatalog);
	
}
