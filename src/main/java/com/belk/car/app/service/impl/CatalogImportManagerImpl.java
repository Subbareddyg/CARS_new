/**
 * 
 */
package com.belk.car.app.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.appfuse.service.impl.UniversalManagerImpl;

import com.belk.car.app.dao.AttributeDao;
import com.belk.car.app.dao.CatalogImportDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorStyle;
import com.belk.car.app.model.catalog.CatalogImport;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogProductAttribute;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.catalog.CatalogSkuAttribute;
import com.belk.car.app.model.catalog.CatalogTemplate;
import com.belk.car.app.service.CatalogImportManager;

/**
 * @author vasans
 *
 */
public class CatalogImportManagerImpl extends UniversalManagerImpl implements
		CatalogImportManager {

	private CatalogImportDao dao;
	private AttributeDao attributeDao;

	/**
	 * Method that allows setting the DAO to talk to the data store with.
	 * @param dao the dao implementation
	 */
	public void setDao(CatalogImportDao dao) {
		this.dao = dao;
	}

	public void setAttributeDao(AttributeDao attributeDao) {
		this.attributeDao = attributeDao;
	}

	public List<CatalogTemplate> getAllCatalogTemplates() {
		return dao.getAllCatalogTemplates();
	}

	public CatalogTemplate getCatalogTemplate(long catalogTemplateId) {
		return dao.getCatalogTemplate(catalogTemplateId);
	}

	public CatalogImport save(CatalogImport catalogImport) {
		return dao.save(catalogImport);
	}

	public CatalogProduct getProduct(String vendorNumber, String styleNumber) {
		return dao.getProduct(vendorNumber, styleNumber);
	}

	public CatalogSku getSku(String upc) {
		return dao.getSku(upc);
	}

	public CatalogProduct save(CatalogProduct catalogProduct) {
		return dao.save(catalogProduct);
	}

	public CatalogSku save(CatalogSku catalogSku) {
		return dao.save(catalogSku);
	}
	
	public Car copyToCar(Car car) {
		return copyToCar(this.getProduct(car.getVendorStyle().getVendorNumber(), car.getVendorStyle().getVendorStyleNumber()), car);
	}

	public Car copyToCar(CatalogProduct product, Car car) {
		return copyToCar(product, car, false) ;
	}

	public Car copyToCar(CatalogProduct product, Car car, boolean createOnly) {
		//Update Data from Catalog
		if (product != null) {
			VendorStyle style = car.getVendorStyle();
			if (style != null) {
				style.setVendorStyleName(product.getPrimaryName());
				style.setDescr(product.getCopyText());
				Map<String, String> catalogAttrMap = new HashMap<String, String>() ;
				for(CatalogProductAttribute cpa : product.getAttributes()) {
					catalogAttrMap.put(cpa.getAttributeName(), cpa.getAttributeValue()) ;
				}
				
				if (car.getCarAttributes() != null && !car.getCarAttributes().isEmpty()) {
					for(CarAttribute carAttr : car.getCarAttributes()) {
						if (catalogAttrMap.containsKey(carAttr.getAttribute().getName()) && !createOnly) {
							carAttr.setAttrValue(catalogAttrMap.get(carAttr.getAttribute().getName()));
						}
					}
				}
				
			}
			
			//Update VendorSku Information for Catalog
			Set<VendorSku> skus = car.getVendorSkus();
			
			
			if (skus != null && !skus.isEmpty()) {
				for(VendorSku sku: skus) {
					//TODO: Need to verify if what is in the spreadsheet in the Belk UPC
					CatalogSku catSku = this.getSku(sku.getVendorUpc()) ;
					if (catSku != null) {
						Set<CatalogSkuAttribute> catSkuAttrs = catSku.getAttributes();

						if(catSkuAttrs != null) {
							Map<String, CarSkuAttribute> skuMap = new HashMap<String, CarSkuAttribute>() ;
							if (sku.getCarSkuAttributes() != null && !sku.getCarSkuAttributes().isEmpty()) {
								for(CarSkuAttribute skuAttr: sku.getCarSkuAttributes()) {
									skuMap.put(skuAttr.getAttribute().getName(), skuAttr) ;
								}
							}

							for(CatalogSkuAttribute catSkuAttr: catSkuAttrs) {
								if (catSkuAttr.getAttributeName().equals("SKU_SIZE")) {
									sku.setSizeName(catSkuAttr.getAttributeValue());
								} else if (catSkuAttr.getAttributeName().equals("SKU_COLOR_NAME")) {
									sku.setColorName(catSkuAttr.getAttributeValue());
								} else {
									//HEX VALUE OR OTHER ATTRIBUTES
									if (StringUtils.isNotEmpty(catSkuAttr.getAttributeValue())) {
										if (skuMap.containsKey(catSkuAttr.getAttributeName())) {
											//update existing values
											if (!createOnly) {
												CarSkuAttribute carSkuAttr = skuMap.get(catSkuAttr.getAttributeName());
												carSkuAttr.setUpdatedDate(new Date()) ;
												carSkuAttr.setAttrValue(catSkuAttr.getAttributeValue()) ;
											}
										} else {
											//create new CarSkuAttribute
											Attribute attr = attributeDao.getAttribute(catSkuAttr.getAttributeName());
											if (attr != null) {
												CarSkuAttribute carSkuAttr = new CarSkuAttribute();
												carSkuAttr.setAttribute(attr);
												carSkuAttr.setAttrValue(catSkuAttr.getAttributeValue()) ;
												carSkuAttr.setVendorSku(sku);
												carSkuAttr.setCreatedDate(new Date()) ;
												carSkuAttr.setUpdatedDate(new Date()) ;
												carSkuAttr.setCreatedBy(car.getUpdatedBy()) ;
												carSkuAttr.setUpdatedBy(car.getUpdatedBy()) ;
												sku.getCarSkuAttributes().add(carSkuAttr) ;
											}
										}
									}
								}
								
							}
						}
					}
				}
			}
		}
		return car ;
	}
}
