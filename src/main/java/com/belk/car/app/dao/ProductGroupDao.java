package com.belk.car.app.dao;

import java.util.List;

import org.appfuse.dao.UniversalDao;
import org.hibernate.SQLQuery;

import com.belk.car.app.dto.ProductTypeDTO;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;

/**
 * Interface for the Product Group DAO actions
 * 
 * @author AFUSXS7 - Subbu
 * 
 */
public interface ProductGroupDao extends UniversalDao {

	List<ProductGroup> getAllProductGroups();

	ProductGroup getProductGroup(Long productGroupId);

	ProductGroup save(ProductGroup productGroup);

	void remove(ProductGroup productGroup);

	List<ProductTypeDTO> getProductTypesForAssociation(Long productGroupId);

	List<ProductTypeDTO> getSelectedProductTypes(String productTypeIds, String newProdGroup);

	List<ProductTypeDTO> getProductTypeDTO(SQLQuery query) throws Exception;

	void updateOldProductTypes(String productIds, ProductGroup productGroup) throws Exception;

	List<ProductType> getActiveProductGroupTypes(ProductGroup productGroup);

	void removeTypeFromGroup(String productTypeId, ProductGroup productGroup) throws Exception;
	
	List<ProductGroup> getProductGroups(String productGroups);
	
	public List<ProductGroup> getActiveProductGroups();
	
}
