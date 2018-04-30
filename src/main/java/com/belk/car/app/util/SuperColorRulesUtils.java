package com.belk.car.app.util;

import java.util.ArrayList;
import java.util.Date;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.service.SizeConversionManager;
import com.belk.car.app.service.SuperColorManager;

/**
 * A util class containing Super Color Mapping methods.
 * @author Yogesh.Vedak
 *
 */
public class SuperColorRulesUtils {
	
	private CarManager carManager;
	private OutfitManager outfitManager;
	private SuperColorManager superColorManager;
	
	
	public SuperColorManager getSuperColorManager() {
		return superColorManager;
	}
	public void setSuperColorManager(SuperColorManager superColorManager) {
		this.superColorManager = superColorManager;
	}
	
	
	public OutfitManager getOutfitManager() {
		return outfitManager;
	}
	public void setOutfitManager(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}

	public CarManager getCarManager() {
		return carManager;
	}
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
	
	  public VendorSku setSuperColorAttribute(VendorSku vendorSku ,final String ATR_NAME,String attrVal){
		   CarSkuAttribute carSkuAttr=null;
		   String attrComponentVal = attrVal;
		   Attribute attrSizeFacet=outfitManager.getAttributeByName(ATR_NAME);
		   if (vendorSku.getCarSkuAttributes() != null && !vendorSku.getCarSkuAttributes().isEmpty()) {
				ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(vendorSku.getCarSkuAttributes());
				for(CarSkuAttribute skuAttr: skuAttrList) {
					if (ATR_NAME.equals(skuAttr.getAttribute().getName())){
						carSkuAttr=skuAttr;
				}
			}
		   }
		   if(carSkuAttr==null){
			   carSkuAttr=new CarSkuAttribute();
			   carSkuAttr.setAttribute(attrSizeFacet);
			   carSkuAttr.setAttrValue(attrComponentVal);
			   carSkuAttr.setVendorSku(vendorSku);
				vendorSku.getCarSkuAttributes().add(carSkuAttr);
			} else {
				carSkuAttr.setAttrValue(attrComponentVal);
				vendorSku.getCarSkuAttributes().add(carSkuAttr);
			}
		   
		   return vendorSku;
	   }
	   
	  public VendorSku updateSuperColorAttributeOnSku(VendorSku vendorSku ,final String ATR_NAME,String attrVal,final String CREATED_BY,final String UPDATED_BY){
			CarSkuAttribute carSkuAttr1=null;
				String superColor1Name = attrVal;
				if (vendorSku.getCarSkuAttributes() != null && !vendorSku.getCarSkuAttributes().isEmpty()) {
					ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(vendorSku.getCarSkuAttributes());
					for(CarSkuAttribute skuAttr: skuAttrList) {
						if (ATR_NAME.equals(skuAttr.getAttribute().getName())){
							carSkuAttr1=skuAttr;
						} 
					}
				}
				Attribute superColorAttr1 = outfitManager.getAttributeByName(Constants.ATTR_SUPERCOLOR1);
				if(carSkuAttr1==null){
					carSkuAttr1=new CarSkuAttribute();
					carSkuAttr1.setAttribute(superColorAttr1);
					carSkuAttr1.setAttrValue(superColor1Name);
					carSkuAttr1.setCreatedBy(CREATED_BY);
					carSkuAttr1.setCreatedDate(new Date());
					carSkuAttr1.setUpdatedBy(UPDATED_BY);
					carSkuAttr1.setUpdatedDate((new Date()));
					carSkuAttr1.setVendorSku(vendorSku);
					vendorSku.getCarSkuAttributes().add(carSkuAttr1);
				} else {
					carSkuAttr1.setAttrValue(superColor1Name);
					carSkuAttr1.setUpdatedBy(UPDATED_BY);
					carSkuAttr1.setUpdatedDate((new Date()));
					vendorSku.getCarSkuAttributes().add(carSkuAttr1);
				}
		   return vendorSku;
	   }
	  
	  public Integer getIterationCount(int rowCount,int batchSize){
			 int itrSize = rowCount % batchSize;
			  if(rowCount == 0){
				  return 0;
			  }else if(rowCount < batchSize){
				  return 1;
			  }else  if(itrSize == 0){
		    	 return (rowCount / batchSize);
		      }else{
		    	 return (rowCount / batchSize) + 1;
		      }
		    		
		  }
}
