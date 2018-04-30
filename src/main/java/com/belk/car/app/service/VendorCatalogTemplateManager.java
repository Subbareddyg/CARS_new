package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;

public interface VendorCatalogTemplateManager extends UniversalManager{
	List<VendorCatalogTemplate> getVendorCatalogTemplates();
	VendorCatalogTemplate getVendorCatalogTemplate(Long vendorCatalogTemplateId);
	VendorCatalogTemplate save(VendorCatalogTemplate vendorCatalogTemplate);
	VendorCatalog saveCatalog(VendorCatalog vendorCatalog);

}
