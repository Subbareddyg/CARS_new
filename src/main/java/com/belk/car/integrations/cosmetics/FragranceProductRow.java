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
public class FragranceProductRow implements ExcelDataRow{

	public enum Field {
		Brand_Name,
		//Frangrance_Designer_Name,
		Style_Num,
		UPC,
		Fragrance_Gender,
		Product_Name,
		Copy_Text,
		Size,
		Is_Set,
		//Image_Location,
		//Image_Num,
		//Women_Scent_Category,
		//Women_Scent_SubCategory,
		//Men_Scent_Category,
		Cross_Sell_1,
		Cross_Sell_2,
		Hazardous_Material
		//,
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
		for (FragranceProductRow.Field field : FragranceProductRow.Field.values()) {
			map.put(field.name(),this.getAsString(hssfRow, field));
		}
	}
	
	public Map<String, String> getData() {
		return map ;
	}
	
	public String getValue(FragranceProductRow.Field field) {
		return this.getValue(field.name());
	}
	
	public String getValue(String fieldName) {
		return map.get(fieldName) ;
	}
	public String getStyleNumber() {
		return map.get(FragranceProductRow.Field.Style_Num.name());
	}

	public String getName() {
		return map.get(FragranceProductRow.Field.Product_Name.name());
	}

	public String getSecondaryName() {
		//return map.get(FragranceProductRow.Field.Product_Name.name());
		return null;
	}

	public String getCopy() {
		return map.get(FragranceProductRow.Field.Copy_Text.name());
	}
	
	public String getSku() {
		return map.get(FragranceProductRow.Field.UPC.name());
	}

	public String[] getExcelProductAttributes() {
		return new String[]
		        {
				FragranceProductRow.Field.Brand_Name.name(),
				//FragranceProductRow.Field.Frangrance_Designer_Name.name(),
				FragranceProductRow.Field.Fragrance_Gender.name(),
				FragranceProductRow.Field.Is_Set.name(),
				//FragranceProductRow.Field.Women_Scent_Category.name(),
				//FragranceProductRow.Field.Women_Scent_SubCategory.name(),
				//FragranceProductRow.Field.Men_Scent_Category.name(),
				FragranceProductRow.Field.Cross_Sell_1.name(),
				FragranceProductRow.Field.Cross_Sell_2.name(),
				FragranceProductRow.Field.Hazardous_Material.name()
				};
	}

	public String[] getProductAttributes() {
		return new String[]
		        {
				"GBL_BRAND",
				//"CSF_FRAGRANCE_DESIGNER_NAME",
				"CSF_FRAGRANCE_GENDER",
				"CSF_FRAGRANCE_SET",
				//"CSF_SCENT_CATEGORY:_WOMENS",
				//"CSF_SCENT_SUB-CATEGORY:_WOMENS",
				//"CSF_SCENT_CATEGORY:_MENS",
				"CSF_CROSS_SELL_#1",
				"CSF_CROSS_SELL_#2",
				"CSF_HAZARDOUS_MATERIALS?"
				};
	}

	public String[] getExcelSkuAttributes() {
		return new String[]
		        {
				FragranceProductRow.Field.Size.name()
				};
	}

	public String[] getSkuAttributes() {
		return new String[]
		        {
				"SKU_SIZE"
				};
	}
}
