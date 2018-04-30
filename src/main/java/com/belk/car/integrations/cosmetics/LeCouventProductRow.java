package com.belk.car.integrations.cosmetics;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * 
 * @author amuaxg1
 */
public class LeCouventProductRow implements ExcelDataRow{

	public enum Field {
		Brand_Name,
		Style_Num,
		UPC,
		Product_Name,
		Copy_Text,
		Size,
		Is_Set,
		//Image_Num,
		Scent_Category,
		Bed_And_Bath_Category,
		Cross_Sell_1,
		Cross_Sell_2;//,
		//Comments;

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
			//TODO: Remove System.out...
			/*if (cell != null) {
				System.out.println("Row Num: " + hssfRow.getRowNum() + " Cell Num: " + cell.getCellNum() + " Value: " + s);
			} else {
				System.out.println("Row Num: " + hssfRow.getRowNum() + " Cell Num: " + "NULL" + " Value: " + s);				
			}*/
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
		for (LeCouventProductRow.Field field : LeCouventProductRow.Field.values()) {
			map.put(field.name(),this.getAsString(hssfRow, field));
		}
	}
	
	public Map<String, String> getData() {
		return map ;
	}
	
	public String getValue(LeCouventProductRow.Field field) {
		return this.getValue(field.name());
	}
	
	public String getValue(String fieldName) {
		return map.get(fieldName) ;
	}
	public String getStyleNumber() {
		return map.get(LeCouventProductRow.Field.Style_Num.name());
	}

	public String getName() {
		return map.get(LeCouventProductRow.Field.Product_Name.name());
	}

	public String getSecondaryName() {
		//return map.get(LeCouventProductRow.Field.Product_Name.name());
		return null;
	}

	public String getCopy() {
		return map.get(LeCouventProductRow.Field.Copy_Text.name());
	}
	
	public String getSku() {
		return map.get(LeCouventProductRow.Field.UPC.name());
	}

	public String[] getExcelProductAttributes() {
		return new String[]
		        {
				LeCouventProductRow.Field.Brand_Name.name(),
				LeCouventProductRow.Field.Is_Set.name(),
				LeCouventProductRow.Field.Scent_Category.name(),
				LeCouventProductRow.Field.Bed_And_Bath_Category.name(),
				LeCouventProductRow.Field.Cross_Sell_1.name(),
				LeCouventProductRow.Field.Cross_Sell_2.name()
				};
	}

	public String[] getProductAttributes() {
		return new String[]
		        {
				"GBL_BRAND",
				"CSLCSM_FRAGRANCE_SET",
				"CSLCSM_SCENT_CATEGORY",
				"CSLCSM_BATH_&_BODY_CATEGORY",
				"CSLCSM_CROSS_SELL_#1",
				"CSLCSM_CROSS_SELL_#2"
				};
	}

	public String[] getExcelSkuAttributes() {
		return new String[]
		        {
				LeCouventProductRow.Field.Size.name()
				};
	}

	public String[] getSkuAttributes() {
		return new String[]
		        {
				"SKU_SIZE"
				};
	}
}
