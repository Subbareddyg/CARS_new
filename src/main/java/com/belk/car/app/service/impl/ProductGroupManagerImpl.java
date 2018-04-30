/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.List;

import javax.persistence.EntityExistsException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.service.impl.UniversalManagerImpl;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.dao.ProductGroupDao;
import com.belk.car.app.dto.ProductTypeDTO;
import com.belk.car.app.exceptions.ProductGroupExistsException;
import com.belk.car.app.model.ProductGroup;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.service.ProductGroupManager;
import com.belk.car.app.webapp.forms.ProductTypeGroupForm;

/**
 * Implementation of the product group Manager.
 * 
 * @author AFUSXS7
 * 
 */
public class ProductGroupManagerImpl extends UniversalManagerImpl implements ProductGroupManager {

	private transient final Log log = LogFactory.getLog(ProductGroupManagerImpl.class);

	private ProductGroupDao productGroupDao;

	/**
	 * @param productDao
	 *            the productDao to set
	 */
	public void setproductGroupDao(ProductGroupDao productGroupDao) {
		this.productGroupDao = productGroupDao;
	}

	public List<ProductGroup> getAllProductGroups() {
		return productGroupDao.getAllProductGroups();
	}
	/**
	 * This Method will get all ACTIVE Product Groups.
	 * @return list.
	 */
	public List<ProductGroup> getAllActiveProductGroups() {
		return productGroupDao.getActiveProductGroups();
	}
	public ProductGroup getProductGroup(Long productGroupId) {
		return productGroupDao.getProductGroup(productGroupId);
	}

	/**
	 * Saves the product group. If the group already exists the Product group
	 * exists exception is thrown.
	 */
	public ProductGroup save(ProductGroup productGroup) throws ProductGroupExistsException {
		try {
			return productGroupDao.save(productGroup);
		} catch (DataIntegrityViolationException e) {
			log.warn(e.getMessage());
			throw new ProductGroupExistsException("Product Group Name '" + productGroup.getName()
					+ "' already exists!");
		} catch (EntityExistsException e) { // needed for JPA
			log.warn(e.getMessage());
			throw new ProductGroupExistsException("Product Group Name '" + productGroup.getName()
					+ "' already exists!");
		} catch (ConstraintViolationException e) {
			log.warn(e.getMessage());
			throw new ProductGroupExistsException("Product Group Name '" + productGroup.getName()
					+ "' already exists!");
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new ProductGroupExistsException("Cannot Save the product group -"
					+ productGroup.getName());
		}
	}

	public void remove(ProductGroup productGroup) {
		productGroupDao.remove(productGroup);
	}

	/**
	 * Populated the product type group form values into the Product Group
	 * values.
	 * 
	 * @param productTypeGroupForm
	 * @return
	 */
	public ProductGroup getAsProductGroup(ProductTypeGroupForm productTypeGroupForm) {
		ProductGroup productGroup = null;
		String productGroupId = productTypeGroupForm.getId();

		if (productGroupId == null || (productGroupId != null && productGroupId.length() < 1)) {
			// New Product Group
			productGroup = new ProductGroup();
		} else {
			// Update Product Group
			productGroup = productTypeGroupForm.getProductGroup();
			if (productGroup == null) {
				productGroup = new ProductGroup();
			}
			productGroup.setProductGroupId(Long.valueOf(productGroupId));
		}

		productGroup.setName(productTypeGroupForm.getName());
		productGroup.setStatusCd(productTypeGroupForm.getStatusCd());
		productGroup.setDescription(productTypeGroupForm.getDescription());
		log.info("Info ProductGroupValues---" + productGroup);
		return productGroup;

	}

	/**
	 * Gets the product type form value object from the Product group DAO
	 * object.
	 */
	public ProductTypeGroupForm getAsProductGroupForm(ProductGroup productGroup) {
		ProductTypeGroupForm productTypeGroupForm = new ProductTypeGroupForm();
		productTypeGroupForm.setId(productGroup.getProductGroupIdAsString());
		productTypeGroupForm.setProductGroup(productGroup);
		productTypeGroupForm.setName(productGroup.getName());
		productTypeGroupForm.setStatusCd(productGroup.getStatusCd());
		productTypeGroupForm.setDescription(productGroup.getDescription());
		if (productGroup.getProductType() != null) {
			productTypeGroupForm.setProductTypes((List<ProductType>) productGroup.getProductType());
		}
		return productTypeGroupForm;
	}

	/**
	 * Gets the products that are not yet associated with the Group. Based on
	 * the group id passed to the method
	 */
	public List<ProductTypeDTO> getProductTypesForAssociation(Long productGroupId) {
		return productGroupDao.getProductTypesForAssociation(productGroupId);
	}

	/**
	 * Gets the products that are currently selected for associating with the
	 * group.
	 */
	public List<ProductTypeDTO> getSelectedProductTypes(String productTypeIds,
			String newProductGroup) {
		return productGroupDao.getSelectedProductTypes(productTypeIds, newProductGroup);
	}

	/**
	 * Updates the product ids that are active in other groups to inactive.
	 */
	public void updateOldProductTypes(String productIds, ProductGroup productGroup)
			throws Exception {
		productGroupDao.updateOldProductTypes(productIds, productGroup);
	}

	/**
	 * Gets only the product types that are active for the product group.
	 */
	public List<ProductType> getActiveProductGroupTypes(ProductGroup productGroup) {
		return productGroupDao.getActiveProductGroupTypes(productGroup);
	}

	/**
	 * Sets the product group passed as Inactive as we don't delete the group we
	 * update the status to Inactive
	 * 
	 * @throws ProductGroupExistsException
	 * @throws Exception
	 * 
	 * @throws ProductGroupExistsException
	 * 
	 */
	public ProductGroup setGroupInactive(ProductGroup productGroup)
			throws ProductGroupExistsException {
		productGroup.setStatusCd(DropShipConstants.INACTIVE);
		return this.save(productGroup);

	}

	/**
	 * Removes the passed product type from the product group.
	 */
	public void removeTypeFromGroup(String productTypeId, ProductGroup productGroup)
			throws Exception {
		productGroupDao.removeTypeFromGroup(productTypeId, productGroup);
	}
	
	/**
	 * Returns the List value passed based on the product groups selected.
	 */
	public List<ProductGroup> getProductGroups(String productGroups){
		return productGroupDao.getProductGroups(productGroups);
	}
}
