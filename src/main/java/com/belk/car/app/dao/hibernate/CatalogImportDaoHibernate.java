package com.belk.car.app.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.Hibernate;

import com.belk.car.app.dao.CatalogImportDao;
import com.belk.car.app.model.catalog.CatalogImport;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.catalog.CatalogTemplate;

public class CatalogImportDaoHibernate extends UniversalDaoHibernate implements
		CatalogImportDao {

	public CatalogTemplate getCatalogTemplate(long catalogTemplateId) {
		return (CatalogTemplate) getHibernateTemplate().get(CatalogTemplate.class, catalogTemplateId);
	}

	public List<CatalogTemplate> getAllCatalogTemplates() {
		return getHibernateTemplate().find("from CatalogTemplate ct");
	}
	
	public CatalogImport save(CatalogImport catalogImport) {
		getHibernateTemplate().saveOrUpdate(catalogImport);
		getHibernateTemplate().flush();
		return catalogImport;
	}

	public CatalogProduct getProduct(String vendorNumber, String styleNumber) {
		String[] args = new String[]{vendorNumber, styleNumber};
		List l = getHibernateTemplate().find("FROM CatalogProduct cp WHERE cp.vendorNumber =? AND cp.styleNumber = ?", args);
		CatalogProduct cp = null ;
		if (l != null && !l.isEmpty()) {
			cp = (CatalogProduct) l.get(0);
		}
		return cp ;
	}

	public CatalogSku getSku(String upc) {
		String[] args = new String[]{upc};
		List l = getHibernateTemplate().find("FROM CatalogSku cs WHERE cs.vendorSku=?", args);
		CatalogSku cs = null ;
		if (l != null && !l.isEmpty()) {
			cs = (CatalogSku) l.get(0);
			Hibernate.initialize(cs.getCatalogProduct());
			Hibernate.initialize(cs.getCatalogProduct().getAttributes());
		}

		return cs ;
	}

	public CatalogProduct save(CatalogProduct catalogProduct) {
		getHibernateTemplate().saveOrUpdate(catalogProduct);
		getHibernateTemplate().flush();
		return catalogProduct;
	}

	public CatalogSku save(CatalogSku catalogSku) {
		getHibernateTemplate().saveOrUpdate(catalogSku);
		getHibernateTemplate().flush();
		return catalogSku;
	}

}
