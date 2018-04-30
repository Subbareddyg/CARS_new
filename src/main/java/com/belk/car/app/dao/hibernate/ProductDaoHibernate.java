package com.belk.car.app.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;

import com.belk.car.app.dao.ProductDao;
import com.belk.car.app.model.ProductType;

public class ProductDaoHibernate extends UniversalDaoHibernate implements
		ProductDao {


	public List<ProductType> getAllProductTypes() {
		return getHibernateTemplate().find("from ProductType");
	}

	public List<ProductType> getProductTypes(long classificationId) {
		StringBuffer sqlB = new StringBuffer("from ProductType pt ");
		sqlB.append(" inner join fetch pt.classifications as c");
		sqlB.append(" left outer join fetch pt.productTypeAttributes as pta");
		sqlB.append(" left outer join fetch pta.attribute as ptatt");
		sqlB.append(" where c.classId = ?");

		return getHibernateTemplate().find(sqlB.toString(), classificationId);
	}

	public ProductType getProductType(Long productId) {		
		return (ProductType) getHibernateTemplate().get(ProductType.class, productId);
	}

	public ProductType save(ProductType product) {		
		getHibernateTemplate().saveOrUpdate(product);
		// necessary to throw a DataIntegrityViolation and catch it in ProductManager
        getHibernateTemplate().flush();
		return product;
	}

	public void remove(ProductType product) {		
		 getHibernateTemplate().saveOrUpdate(product);
         getHibernateTemplate().flush();
	}

}
