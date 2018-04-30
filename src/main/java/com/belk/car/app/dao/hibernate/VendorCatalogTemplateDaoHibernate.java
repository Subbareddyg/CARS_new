package com.belk.car.app.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;

import com.belk.car.app.dao.VendorCatalogTemplateDao;
import com.belk.car.app.model.vendorcatalog.VendorCatalog;
import com.belk.car.app.model.vendorcatalog.VendorCatalogTemplate;

/**
 * Implementation class for the product group
 * 
 * @author AFUSXS7 : Subbu
 * 
 */
public class VendorCatalogTemplateDaoHibernate extends UniversalDaoHibernate implements
		VendorCatalogTemplateDao {

	/**
	 * Gets all the vendor catalog template.
	 */
	public List<VendorCatalogTemplate> getVendorCatalogTemplates() {
		return getHibernateTemplate().find("from VendorCatalogTemplate");
	}

	/**
	 * Gets the VendorCatalog template object based on the catalog template id
	 * that has been passed.
	 * 
	 * @param vendorCatalogTemplateId
	 * @return
	 */
	public VendorCatalogTemplate getVendorCatalogTemplate(Long vendorCatalogTemplateId) {
		return (VendorCatalogTemplate) getHibernateTemplate().get(VendorCatalogTemplate.class,
				vendorCatalogTemplateId);
	}

	/**
	 * Saves/Updates the vendor catalog template.
	 * 
	 * @param vendorCatalogTemplate
	 * @return
	 */
	public VendorCatalogTemplate save(VendorCatalogTemplate vendorCatalogTemplate) {
		getHibernateTemplate().saveOrUpdate(vendorCatalogTemplate);
		getHibernateTemplate().flush();
		return vendorCatalogTemplate;
	}

	/**
	 * Saves the associated vendor catalog to the template
	 * 
	 * @param vendorCatalog
	 * @return
	 */
	public VendorCatalog saveCatalog(VendorCatalog vendorCatalog) {
		getHibernateTemplate().update(vendorCatalog);
		getHibernateTemplate().flush();
		return vendorCatalog;
	}

}
