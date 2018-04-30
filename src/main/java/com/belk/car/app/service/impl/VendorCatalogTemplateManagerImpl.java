/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.VendorCatalogTemplateDao;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;
import com.belk.car.app.service.VendorCatalogTemplateManager;

/**
 * Implementation of the product group Manager.
 * 
 * @author AFUSXS7
 * 
 */
public class VendorCatalogTemplateManagerImpl extends UniversalManagerImpl implements
		VendorCatalogTemplateManager {

	private transient final Log log = LogFactory.getLog(VendorCatalogTemplateManagerImpl.class);

	private VendorCatalogTemplateDao vendorCatalogTemplateDao;

	
	public List<VendorCatalogTemplate> getVendorCatalogTemplates(){
		return vendorCatalogTemplateDao.getVendorCatalogTemplates();
	}


	public VendorCatalogTemplate getVendorCatalogTemplate(Long vendorCatalogTemplateId) {
		return vendorCatalogTemplateDao.getVendorCatalogTemplate(vendorCatalogTemplateId);
	}


	public VendorCatalogTemplate save(VendorCatalogTemplate vendorCatalogTemplate) {
		log.debug("Saving vendorCatalogTemplate in - " + vendorCatalogTemplate.getVendorCatalogTmplID());
		return vendorCatalogTemplateDao.save(vendorCatalogTemplate);
	}


	public VendorCatalog saveCatalog(VendorCatalog vendorCatalog) {
		log.debug("Saving vendor catalog in - " + vendorCatalog.getVendorCatalogID());
		return vendorCatalogTemplateDao.saveCatalog(vendorCatalog);
	}
}
