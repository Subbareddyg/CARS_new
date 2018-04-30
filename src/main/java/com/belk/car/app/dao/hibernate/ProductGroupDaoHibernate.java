package com.belk.car.app.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.dao.hibernate.UniversalDaoHibernate;
import org.hibernate.Hibernate;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.ProductGroupDao;
import com.belk.car.app.dto.ProductTypeDTO;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;

/**
 * Implementation class for the product group
 * 
 * @author AFUSXS7 : Subbu
 * 
 */
public class ProductGroupDaoHibernate extends UniversalDaoHibernate implements ProductGroupDao {
	
	/**
	 * This method is used to retrieve the all product groups.
	 * @return list.
	 * */
	@SuppressWarnings("unchecked")
	public List<ProductGroup> getAllProductGroups() {
		return getHibernateTemplate().find("from ProductGroup");
	}
	/**
	 * This Method is used for retrieve all Active Product Groups.
	 * @return list.
	 * */
	public List<ProductGroup> getActiveProductGroups() {
		return getHibernateTemplate().find("from ProductGroup where statusCd =? order by upper(name) asc",
				DropShipConstants.ACTIVE);
	}
	/**
	 * This method returns the Product Group Object from the product group ID.
	 * @param Long productGroupId.
	 * @return ProductGroup productGroup.
	 * */
	public ProductGroup getProductGroup(Long productGroupId) {
		return (ProductGroup) getHibernateTemplate().get(ProductGroup.class, productGroupId);
	}
	/**
	 * This method save the product Group.
	 * @param ProductGroup
	 * @return ProductGroup
	 * */
	public ProductGroup save(ProductGroup productGroup) {
		getHibernateTemplate().saveOrUpdate(productGroup);
		getHibernateTemplate().flush();
		return productGroup;
	}
	/**
	 * This method delete the product group record
	 * @param ProductGroup.
	 * */
	public void remove(ProductGroup productGroup) {
		getHibernateTemplate().delete(productGroup);
		getHibernateTemplate().flush();
	}

	/**
	 * Gets the product group id and returns the product types that are not
	 * associated with the product group.
	 * 
	 * This query has product description for product group description.
	 * 
	 */
	public List<ProductTypeDTO> getProductTypesForAssociation(Long productGroupId) {
		List<ProductTypeDTO> productTypeList = new ArrayList<ProductTypeDTO>();
		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery.append("select pt.product_type_id as productTypeId, ").append(
				"pt.descr as productGroupDesc,  pt.name as name, pt.descr as descr, ").append(
				"pt.status_cd as statusCd, 'newProdGroup' as newProdGroup from product_type pt ")
				.append("where product_type_id not in(select product_type_id ").append(
						"from PROD_GROUP_PROD_TYPE where product_group_id = :product_group_id and status_cd='ACTIVE') ")
				.append("and pt.status_cd='ACTIVE' order by pt.name");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		SQLQuery query = session.createSQLQuery(sqlQuery.toString());
		query.setLong("product_group_id", productGroupId);

		try {
			productTypeList = getProductTypeDTO(query);
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getProductTypesForAssociation() "
					+ "group id " + productGroupId + "::Msg" + e.getMessage());
		} 
		return productTypeList;
	}
	/**
	 * This method returns the list of selected product types for product group.
	 * @param String
	 * @param String
	 * @return List
	 * */
	public List<ProductTypeDTO> getSelectedProductTypes(String productTypeIds, String newProduGroup) {
		List<ProductTypeDTO> productTypeList = new ArrayList<ProductTypeDTO>();
		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery
				.append("select pt.product_type_id as productTypeId, ")
				.append("pg.descr as productGroupDesc,  pt.name as name, ")
				.append("pt.descr as descr, pt.status_cd as statusCd, ")
				.append("'" + newProduGroup + "' as newProdGroup ")
				.append("from product_type pt ")
				.append(
						"left outer join PROD_GROUP_PROD_TYPE pgt on pt.product_type_id=pgt.product_type_id and pgt.status_cd='ACTIVE'")
				.append(
						"left outer join product_group pg on pg.product_group_id = pgt.product_group_id ")
				.append(" where pt.status_cd='ACTIVE' and pt.product_type_id ").append(
						"in (" + productTypeIds + ") order by pt.name ");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();

		SQLQuery query = session.createSQLQuery(sqlQuery.toString());
		try {
			productTypeList = getProductTypeDTO(query);
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getSelectedProductTypes() "
					+ "product type ids " + productTypeIds + "::Msg" + e.getMessage());
		} 
		getHibernateTemplate().flush();
		return productTypeList;

	}

	/**
	 * Retrieves the product type dto Object based on the query passed to this
	 * function.
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ProductTypeDTO> getProductTypeDTO(SQLQuery query) throws Exception {
		List<ProductTypeDTO> productTypeList = new ArrayList<ProductTypeDTO>();

		query.addScalar("productTypeId", Hibernate.LONG);
		query.addScalar("productGroupDesc", Hibernate.STRING);
		query.addScalar("name", Hibernate.STRING);
		query.addScalar("descr", Hibernate.STRING);
		query.addScalar("statusCd", Hibernate.STRING);
		query.addScalar("newProdGroup", Hibernate.STRING);
		try {
			query.setResultTransformer(Transformers.aliasToBean(ProductTypeDTO.class));
			productTypeList = query.list();
			log.debug("ProductTypeList---" + productTypeList != null ? productTypeList.size()
					: null);
		} catch (Exception e) {
			productTypeList = new ArrayList<ProductTypeDTO>();
			throw e;
		}

		if (productTypeList == null) {
			log.debug("Prodcut Types are empty");
			productTypeList = new ArrayList<ProductTypeDTO>();
		}
		return productTypeList;

	}

	/**
	 * Sets the status of the passed productids status to inactive except for
	 * that product group that is passed.
	 * 
	 * @param productIds
	 * @param productGroup
	 */
	public void updateOldProductTypes(String productIds, ProductGroup productGroup)
			throws Exception {
		updateProductTypes(productIds, productGroup, "FORALLGROUPS");
	}

	/**
	 * Gets all the active product types for the product group passed.
	 * 
	 * @param productGroup
	 */
	@SuppressWarnings("unchecked")
	public List<ProductType> getActiveProductGroupTypes(ProductGroup productGroup) {
		StringBuffer sqlQuery = new StringBuffer();
		List<ProductType> productType = new ArrayList<ProductType>(0);

		sqlQuery
				.append(" SELECT pt.product_type_id as productTypeId, Name as name, ")
				.append(" descr as descr, pt.created_by as createdBy, pt.updated_by as updatedBy, ")
				.append(" pt.created_date as createdDate, pt.updated_date as upDatedDate, ")
				.append(" pt.status_cd as statusCd from product_type pt, PROD_GROUP_PROD_TYPE pgt ")
				.append(" where pt.status_cd='ACTIVE' and pt.product_type_id= pgt.product_type_id ")
				.append(" and pgt.status_cd='ACTIVE' and pgt.product_group_id= :productGroupId ")
				.append(" order by pt.name");

		SessionFactory sf = getHibernateTemplate().getSessionFactory();
		Session session = sf.getCurrentSession();
		try {
			SQLQuery query = (SQLQuery) session.createSQLQuery(sqlQuery.toString()).addScalar(
					"productTypeId", Hibernate.LONG).addScalar("name").addScalar("descr")
					.addScalar("statusCd").addScalar("createdBy").addScalar("updatedBy").addScalar(
							"createdDate", Hibernate.DATE).addScalar("updatedDate", Hibernate.DATE)
					.setResultTransformer(Transformers.aliasToBean(ProductType.class));
			query.setLong("productGroupId", productGroup.getProductGroupId());

			productType = query.list();
		} catch (Exception e) {
			log.error("Hibernate Exception caught in getActiveProductGroupTypes() productGroup-"
					+ productGroup, e);

		} 

		return productType;
	}

	public void updateProductTypes(String productIds, ProductGroup productGroup, String groupOption)
			throws Exception {
		getHibernateTemplate().update(productGroup);
		getHibernateTemplate().flush();
	}

	/**
	 * This Method remove the product type group.
	 * @param String
	 * @param ProductGroup
	 * */
	public void removeTypeFromGroup(String productTypeId, ProductGroup productGroup)
			throws Exception {
		updateProductTypes(productTypeId, productGroup, DropShipConstants.FORSAMEGROUP);
	}
	/**
	 * This Method returns the list of product group for selected Product Group.
	 * @param String.
	 * @return List.
	 * */
	public List<ProductGroup> getProductGroups(String productGroups) {
		return getHibernateTemplate().find("from ProductGroup where product_Group_Id in (" + productGroups +")");
	}

}
