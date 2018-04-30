package com.belk.car.integrations.cosmetics;

import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;

public interface ExcelDataRow {
	void setRow(HSSFRow row);
	Map<String, String> getData() ;
	
	String getStyleNumber() ;
	String getName() ;
	String getSecondaryName() ;
	String getCopy() ;
	String getSku() ;
	String[] getExcelProductAttributes() ;
	String[] getExcelSkuAttributes() ;
	String[] getProductAttributes() ;
	String[] getSkuAttributes() ;
	
	String getValue(String fieldName) ;
}
