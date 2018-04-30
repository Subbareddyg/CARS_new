/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.integrations.cosmetics;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * 
 * @author amuaxg1
 */
public class CosmeticProductRow implements ExcelDataRow{

	public enum Field {
		Brand_Name,
		Taxonomy_Path,
		Style_Num,
		UPC,
		Product_Name,
		//Secondary_Name,
		//Brief_Description,
		Copy_Text,
		//How_To_Use,
		Shade_Name,
		Hex_Value,
		Size,
		Is_Part_Of_Set,
		Works_With_StyleNumber_1,
		Works_With_StyleNumber_2
		//,
		//Image_Location,
		//Image_Name,
		//Comments
		;

		private String getAsString(HSSFRow hssfRow) {
			HSSFCell cell = hssfRow.getCell((short) this.ordinal());
			String s = null;
			try {
				s = cell.getRichStringCellValue().getString();
			} catch (Exception ex) {
				try {
					double d = cell.getNumericCellValue();
					DecimalFormat df = new DecimalFormat("##");
					s = df.format(d).toString();
					//BigDecimal bd = (new BigDecimal(d)).stripTrailingZeros() ;
					//if (bd.scale() == 0) {
					//	s= String.valueOf(bd.intValue()) ;
					//} else {
					//	s= String.valueOf(bd.doubleValue());
					//}
				} catch (Exception ex2) {
					s = null;
				}
			}
			return s;
		}
	};

	private String getAsString(HSSFRow hssfRow, Field field) {
		if (field == null || hssfRow == null)
			return null;
		return field.getAsString(hssfRow);
	}
	

	//private HSSFRow hssfRow ;
	private Map<String, String> map ;
	public void setRow(HSSFRow hssfRow) {
		map = new HashMap<String, String>() ;
		for (CosmeticProductRow.Field field : CosmeticProductRow.Field.values()) {
			map.put(field.name(),this.getAsString(hssfRow, field));
		}
	}
	
	public Map<String, String> getData() {
		return map ;
	}
	
	public String getValue(CosmeticProductRow.Field field) {
		return this.getValue(field.name());
	}
	
	public String getValue(String fieldName) {
		return map.get(fieldName) ;
	}
	public String getStyleNumber() {
		return map.get(CosmeticProductRow.Field.Style_Num.name());
	}

	public String getName() {
		return map.get(CosmeticProductRow.Field.Product_Name.name());
	}

	public String getSecondaryName() {
		return "";//map.get(CosmeticProductRow.Field.Secondary_Name.name());
	}

	public String getCopy() {
		return map.get(CosmeticProductRow.Field.Copy_Text.name());
	}
	
	public String getSku() {
		return map.get(CosmeticProductRow.Field.UPC.name());
	}

	public String[] getExcelProductAttributes() {
		return new String[]
        {
			CosmeticProductRow.Field.Brand_Name.name(),
			//CosmeticProductRow.Field.Secondary_Name.name(),
			//CosmeticProductRow.Field.Brief_Description.name(),
			//CosmeticProductRow.Field.How_To_Use.name(),
			CosmeticProductRow.Field.Taxonomy_Path.name(),
			CosmeticProductRow.Field.Works_With_StyleNumber_1.name(),
			CosmeticProductRow.Field.Works_With_StyleNumber_2.name()
		};
	}

	public String[] getProductAttributes() {
		return new String[]
        {
			"GBL_BRAND",
			//"CS_PRODUCT_SECONDARY_NAME",
			//"CS_BRIEF_DESCRIPTION",
			//"CS_HOW_TO_USE",
			"CS_BOUTIQUE_NAVIGATION_PATH",
			"CS_WORKS_WITH_#1_(STYLE_#)",
			"CS_WORKS_WITH_#2_(STYLE_#)"
		};
	}

	public String[] getExcelSkuAttributes() {
		return new String[]
        {
			CosmeticProductRow.Field.Shade_Name.name(),
			CosmeticProductRow.Field.Size.name(),
			CosmeticProductRow.Field.Hex_Value.name()
		};
	}

	public String[] getSkuAttributes() {
		return new String[]
        {
			"SKU_COLOR_NAME",
			"SKU_SIZE",
			"CS_HEX_VALUE"
		};
	}
}
