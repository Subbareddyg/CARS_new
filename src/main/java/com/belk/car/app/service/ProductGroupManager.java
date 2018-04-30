package com.belk.car.app.service;

import java.util.List;

import org.appfuse.service.UniversalManager;

import com.belk.car.app.dto.ProductTypeDTO;
import com.belk.car.app.exceptions.ProductGroupExistsException;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.webapp.forms.ProductTypeGroupForm;

/**
 * Service interface for product group.
 * @author AFUSXS7
 *
 */
public interface ProductGroupManager extends UniversalManager{
	
	List<ProductGroup> getAllProductGroups();

	ProductGroup getProductGroup(Long productGroupId);

	ProductGroup save(ProductGroup productGroup) throws ProductGroupExistsException;
	
	void remove(ProductGroup productGroup);
	
	ProductTypeGroupForm getAsProductGroupForm(ProductGroup productGroup);
	
	ProductGroup getAsProductGroup(ProductTypeGroupForm productTypeGroupForm);
	
	List<ProductTypeDTO> getProductTypesForAssociation(Long productGroupId);
	
	List<ProductTypeDTO> getSelectedProductTypes(String productTypeIds, String newProdGroup);
	
	void updateOldProductTypes(String productIds, ProductGroup productGroup) throws Exception;
	
	List<ProductType> getActiveProductGroupTypes(ProductGroup productGroup);
	
	ProductGroup setGroupInactive(ProductGroup productGroup) throws ProductGroupExistsException;
	
	void removeTypeFromGroup(String productTypeId, ProductGroup productGroup) throws Exception;
	
	List<ProductGroup> getProductGroups(String productGroups);
	
	List<ProductGroup> getAllActiveProductGroups();
	
}
