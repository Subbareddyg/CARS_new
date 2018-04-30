/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belk.car.integrations.cosmetics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.appfuse.model.User;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.catalog.CatalogImport;
import com.belk.car.app.model.catalog.CatalogProduct;
import com.belk.car.app.model.catalog.CatalogProductAttribute;
import com.belk.car.app.model.catalog.CatalogSku;
import com.belk.car.app.model.catalog.CatalogSkuAttribute;
import com.belk.car.app.model.workflow.WorkflowStatus;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.CatalogImportManager;


/**
 *
 * @author amuaxg1
 */
public class CosmeticFileImporter
{
	private static String SPACE = " " ;
	File cosmeticExcelFile;
	ExcelDataRow rowReader ;
	CatalogImport catalogImport ;
	User user ;
	Map<String, CatalogProduct> processedProducts = new HashMap<String, CatalogProduct>() ;
	Set<String> carAlreadyProcessed = new HashSet<String>() ;
	
	public static class InvalidFileExtensionException extends IllegalArgumentException {
		public InvalidFileExtensionException() { super ("The CosmeticFileImporter constructor requires an MS Excel spreadsheet (ending in .xls)"); }
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CosmeticFileImporter(CatalogImport catalogImport, File f, ExcelDataRow rowReader)
	{
		this.cosmeticExcelFile = f;		
		this.rowReader= rowReader ;
		this.catalogImport = catalogImport;
	}

	public void doImport(CatalogImportManager manager, CarManager carManager, boolean createOnly)
	{		
		if (cosmeticExcelFile == null)
			throw new IllegalArgumentException("The CosmeticFileImporter constructor requires a file, and has been passed a null value.");

		boolean isExcelSpreadsheet = cosmeticExcelFile.getName().toLowerCase().endsWith(".xls");
		if (!isExcelSpreadsheet)
			throw new InvalidFileExtensionException();
		
		try {
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(cosmeticExcelFile));
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			HSSFSheet sheet = wb.getSheetAt(0);
			Iterator rowIterator = sheet.rowIterator();
			
			//rowIterator.next(); // skip row 0 -- should have titles
			int rowsProcessed = 0 ;
			while (rowIterator.hasNext()) {
				rowReader.setRow((HSSFRow) rowIterator.next());
				if (isValidDataRow()) {
					saveData(manager, carManager, createOnly);
					rowsProcessed ++ ;
				}
				//insertRowInDatabase(cosmeticProductRow);
			}
			catalogImport.setRecordsProcessed(rowsProcessed);
			manager.save(catalogImport);

		}
		catch (IOException ex) {
			Logger.getLogger(CosmeticFileImporter.class.getName()).log(Level.SEVERE, null, ex);
		}
		finally {
		}
	}

	private void saveData(CatalogImportManager manager, CarManager carManager, boolean createOnly)
	{
		Map<String, String> map = rowReader.getData();
		String[] colNames = null;
		String[] attrNames = null;
		if (map != null && !map.isEmpty()) {
			String upc = rowReader.getSku();
			//Pad UPC to 13 Character, the IDB Data is left padded with 0
			if (upc != null) {
				upc = StringUtils.leftPad(upc, 13, '0'); 
			}
			String styleNumber = rowReader.getStyleNumber();
			String copyText = rowReader.getCopy() ;
			String primaryName = rowReader.getName() ;
			String secondaryName = rowReader.getSecondaryName() ;
			if (StringUtils.isBlank(secondaryName))
				secondaryName = CosmeticFileImporter.SPACE ;
			
			CatalogProduct product = processedProducts.get(catalogImport.getVendorNumber()+"-"+styleNumber);
			if (product == null) {
				product = manager.getProduct(catalogImport.getVendorNumber(), styleNumber);
				if (product == null) {
					product = new CatalogProduct();
				}

				//Remove All Existing Association.... 
				if (product.getCatalogProductId() > 0) {
					product.getAttributes().clear();
					manager.save(product);
				}

				product.setSecondaryName(secondaryName);
				product.setCopyText(copyText);
				product.setPrimaryName(primaryName);
				product.setStyleNumber(styleNumber);
				product.setVendorNumber(this.catalogImport.getVendorNumber());
				this.setAuditInfo(product);
				
				
				//Add New Attribute Values
				colNames = rowReader.getExcelProductAttributes();
				attrNames = rowReader.getProductAttributes();
				for (int i=0;i<colNames.length;i++ ) {
					String fieldName = colNames[i];
					String attrName = attrNames[i];
					String value = rowReader.getValue(fieldName) ;
					if (StringUtils.isBlank(value)) {
						value = CosmeticFileImporter.SPACE ;
					}
					CatalogProductAttribute productAttr = new CatalogProductAttribute();
					productAttr.setAttributeName(attrName) ;
					productAttr.setAttributeValue(value);
					productAttr.setCatalogProduct(product);
					this.setAuditInfo(productAttr);
					product.getAttributes().add(productAttr) ;
				}				
				processedProducts.put(catalogImport.getVendorNumber()+"-"+styleNumber, product);
			}
			
			CatalogSku sku = manager.getSku(upc);
			if (sku == null) {
				sku = new CatalogSku() ;
				sku.setVendorSku(upc) ;
				sku.setCatalogProduct(product) ;
			} else {
				product.getSkus().remove(sku) ;
			}
			
			this.setAuditInfo(sku) ;
			product.getSkus().add(sku);
			
			//SKU ATtributes...
			if (sku.getCatalogSkuId() > 0) {
				sku.getAttributes().clear();
				manager.save(sku);
			}
			
			colNames = rowReader.getExcelSkuAttributes();
			attrNames = rowReader.getSkuAttributes();
			for (int i=0;i<colNames.length;i++ ) {
				String fieldName = colNames[i];
				String attrName = attrNames[i];
				String value = rowReader.getValue(fieldName) ;
				if (StringUtils.isBlank(value)) {
					value = CosmeticFileImporter.SPACE ;
				}
				CatalogSkuAttribute skuAttr = new CatalogSkuAttribute();
				skuAttr.setAttributeName(attrName) ;
				skuAttr.setAttributeValue(value);
				skuAttr.setCatalogSku(sku);
				this.setAuditInfo(skuAttr);
				sku.getAttributes().add(skuAttr) ;
			}
			
			manager.save(product) ;

			//Search for Open CARS for the product
			CarSearchCriteria criteria = new CarSearchCriteria() ;
			criteria.setIgnoreUser(true) ;
			criteria.setStatusCd(Status.ACTIVE);
			criteria.setVendorNumber(catalogImport.getVendorNumber());
			criteria.setVendorStyleNumber(product.getStyleNumber()) ;
			criteria.setSearchChildVendorStyle(true) ;
			
			//Update OPEN CARS
			List<Car> cars = carManager.searchCars(criteria) ;
			if ((cars == null || cars.isEmpty()) && product.getSkus() != null && !product.getSkus().isEmpty()) {
				criteria = new CarSearchCriteria() ;
				StringBuffer vendorUpcs = new StringBuffer();
				/*boolean appendComma = false ;
				for(CatalogSku tsku: product.getSkus()) {
					if (appendComma)
						vendorUpcs.append(",") ;
					vendorUpcs.append("'").append(tsku.getVendorSku()).append("'") ;
					appendComma = true ;
				}*/
				vendorUpcs.append(upc) ;	
				criteria.setIgnoreUser(true) ;
				criteria.setStatusCd(Status.ACTIVE);
				criteria.setVendorUpc(vendorUpcs.toString());
				cars = carManager.searchCars(criteria) ;
			}
			
			if (cars != null && !cars.isEmpty()) {
				for(Car car : cars) {
					if (car.getVendorStyle().getProductType() != null) {
						if (!carAlreadyProcessed.contains(String.valueOf(car.getCarId()))) {
							carAlreadyProcessed.add(String.valueOf(car.getCarId()));
							if (!car.getCurrentWorkFlowStatus().getStatusCd().equals(WorkflowStatus.CLOSED)) {
								car = manager.copyToCar(product, car, createOnly);
								carManager.save(car);
							}
						}
					}
				}
			}
			
			//manager.getCatalogTemplate(catalogTemplateId);
		}
	}
	
	private void setAuditInfo(BaseAuditableModel model) {
		String currentUser = "Unknown"; 
		if(user != null) {
			currentUser = user.getUsername() ;
		} else if (catalogImport != null) {
			currentUser = catalogImport.getCreatedBy() ;
		}

		if (StringUtils.isBlank(model.getCreatedBy())) {
    		model.setCreatedBy(currentUser);
        	model.setCreatedDate(new Date());
    	}
		model.setUpdatedBy(currentUser);
    	model.setUpdatedDate(new Date());
	}
	
	
	private boolean isValidDataRow() {
		String sku = rowReader.getSku();
		return StringUtils.isNotBlank(sku) && StringUtils.isNumeric(sku);
	}	
		
	
//	private class CosmeticProductRowPool
//	{
//		List<CosmeticProductRow> lockedRows = new ArrayList<CosmeticProductRow>();
//		List<CosmeticProductRow> unlockedRows = new ArrayList<CosmeticProductRow>();
//
//
//		synchronized CosmeticProductRow getCosmeticProductRow(HSSFRow hssfRow)
//		{
//			CosmeticProductRow r = (unlockedRows.isEmpty()) ? new CosmeticProductRow()
//				: unlockedRows.remove(0);
//			lockedRows.add(r);
//			r.setHSSFRow(hssfRow);
//			return r;
//		}
//
//
//		public synchronized void release(CosmeticProductRow cosmeticProductRow)
//		{
//			lockedRows.remove(cosmeticProductRow);
//			unlockedRows.add(cosmeticProductRow);
//		}
//	}
}
