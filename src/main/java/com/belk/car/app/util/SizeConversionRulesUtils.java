package com.belk.car.app.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.belk.car.app.Constants;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarSkuAttribute;
import com.belk.car.app.model.SizeConversionMaster;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.OutfitManager;
import com.belk.car.app.service.SizeConversionManager;


/**
 * A utill class containing common methods to deal with Size Conversion rules to be applied on skus and skus' attributes.
 * @author Yogesh.Vedak
 *
 */
public class SizeConversionRulesUtils  {


	private static SizeConversionManager sizeConversionManager;
	private CarManager carManager;
	private OutfitManager outfitManager;
	
	
	private static transient final Log log = LogFactory.getLog(SizeConversionRulesUtils.class);
	
	public OutfitManager getOutfitManager() {
		return outfitManager;
	}
	public void setOutfitManager(OutfitManager outfitManager) {
		this.outfitManager = outfitManager;
	}
	public SizeConversionManager getSizeConversionManager() {
		return sizeConversionManager;
	}
	public void setSizeConversionManager(SizeConversionManager sizeConversionManager) {
		SizeConversionRulesUtils.sizeConversionManager = sizeConversionManager;
	}

	public CarManager getCarManager() {
		return carManager;
	}
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
	
	public static String getSizeRuleSelectionQuery(String deptId,String classId,String vendorId, String sizeName, String vendorUPC){

		StringBuffer query = new StringBuffer("SELECT * FROM size_conversion_master a, vendor_sku b");

		query.append(" WHERE  a.scm_size_name = b.size_name and b.vendor_upc = "+vendorUPC);  

		query.append(" and b.dept_id = nvl(a.scm_dept_id, b.dept_id) and b.class_id = nvl(a.scm_class_id, b.class_id) and b.vendor_id = nvl(a.scm_vendor_id, b.vendor_id) ");
		
		if(log.isDebugEnabled()){
			log.debug("Query for getSizeRuleSelectionQuery:"+query.toString());
		}

		return query.toString();

		}

	/*public static String getSizeRuleSelectionQuery(String deptId,String classId,String vendorId){
		StringBuffer query = new StringBuffer("SELECT * FROM size_conversion_master");
		query.append(" WHERE ");
		query.append("(SCM_DEPT_ID = "+deptId+" and SCM_CLASS_ID = "+classId+" and SCM_VENDOR_ID ="+vendorId+")");
		query.append(" or ");
				query.append("(SCM_DEPT_ID = "+deptId+" and SCM_CLASS_ID = "+classId+" and SCM_VENDOR_ID is null)");
				query.append(" or ");
				query.append("(SCM_DEPT_ID = "+deptId+" and SCM_CLASS_ID is null and SCM_VENDOR_ID = "+vendorId+")");
				query.append(" or ");
				query.append("(SCM_DEPT_ID = "+deptId+" and SCM_CLASS_ID is null and SCM_VENDOR_ID is null)");
				query.append(" or ");
				query.append("(SCM_DEPT_ID is null and SCM_CLASS_ID = "+classId+" and SCM_VENDOR_ID = "+vendorId+")");
				query.append(" or ");
				query.append("(SCM_DEPT_ID is null and SCM_CLASS_ID is null and SCM_VENDOR_ID = "+vendorId+")");
				query.append(" or ");
				query.append("(SCM_DEPT_ID is null and SCM_CLASS_ID = "+classId+" and SCM_VENDOR_ID is null)");
				query.append(" or ");
				query.append("(SCM_DEPT_ID = "+deptId+" and SCM_CLASS_ID is null and SCM_VENDOR_ID is null)");
				query.append(" or ");
				query.append("(SCM_DEPT_ID is null and SCM_CLASS_ID is null and SCM_VENDOR_ID is null)");
				query.append("order by SCM_DEPT_ID, SCM_VENDOR_ID, SCM_CLASS_ID");
				log.debug("________--query formed for RuleSelection is"+query.toString());
		return query.toString(); 
		
	}*/
	
	
	
	public List<SizeConversionMaster> getChangedSizeConversions() {
		
		 //test code
		 /*List<SizeConversionMaster> sizeRulesTest = new ArrayList<SizeConversionMaster>();
		 SizeConversionMaster size =   getSizeConversionManager().getSizeConversionByScmId(841);
		 sizeRulesTest.add(size);
		 return sizeRulesTest;   */  //test code ends
		 //actual code
		 return getSizeConversionManager().getSizeChangedConversions();
	}

	public boolean isSizeConversionRuleChanged(SizeConversionMaster scmaster) {
		if((scmaster!= null)){
			return scmaster.getRuleChanged().equalsIgnoreCase(Constants.RULECHANGED_TRUE)?true:false;
		}
		return false;
	}

	public boolean isSizeConversionRuleApplied(SizeConversionMaster sizeRule, VendorSku sku) {
		if(sku != null && sizeRule != null){
			if(sku.getSizeName().equals(sizeRule.getCoversionSizeName())){
				return true;
			}
		}
		return false;
	}

	//could be used in editCar real time sizeName-updating on EDITCAR. Currently this requirement is eliminated
	public SizeConversionMaster findSizeRuleForVendorSku(Car car,VendorSku vendorSku) {
		String deptId=String.valueOf(car.getDepartment().getDeptId());
		String classId = String.valueOf(car.getVendorStyle().getClassification().getClassificationIdAsString());  //"418";
		String vendorId=  String.valueOf(car.getVendorStyle().getVendor().getVendorIdAsString()); //"464";
		SizeConversionMaster bestMatchedRule = null;
		List<SizeConversionMaster> rulesList = getSizeConversionManager().getSizeConversionsByQuery(getSizeRuleSelectionQuery(deptId,classId,vendorId,vendorSku.getSizeName(),vendorSku.getVendorUpc()));
		for(SizeConversionMaster displaySize : rulesList){
		}
		if((rulesList != null) && (!rulesList.isEmpty())){
			bestMatchedRule = rulesList.get(0);
			if(bestMatchedRule.getCoversionSizeName()!= vendorSku.getSizeName())
			{
				return bestMatchedRule;
			}else if (bestMatchedRule.getFacetSize1()!= vendorSku.getFacetSize1()){
				return bestMatchedRule;
			}else if (bestMatchedRule.getFacetSize2()!= vendorSku.getFacetSize2()){
				return bestMatchedRule;
			}else if (bestMatchedRule.getFacetSize3()!= vendorSku.getFacetSize3()){
				return bestMatchedRule;
			}else if (bestMatchedRule.getFacetSubSize1()!= vendorSku.getFacetSubSize1()){
				return bestMatchedRule;
			}else if (bestMatchedRule.getFacetSubSize2()!= vendorSku.getFacetSubSize2()){
				return bestMatchedRule;
			}
		}
		return bestMatchedRule;
	}
	
	 
	   
	   /**
	    * This method adds an attribute value entry for any vendor sku if doesnt exists else update the existing.Also it deletes the existing entry if incoming value is blank/Null for that particular attribute. 
	    * @param vendorSku
	    * @param ATR_NAME
	    * @param attrVal
	    * @return
	    */
	   public VendorSku setFacetSizeAttribute(VendorSku vendorSku ,final String ATR_NAME,String attrVal){
		   CarSkuAttribute carSkuAttr=null;
		   String attrComponentVal = attrVal;
		   Attribute attrSizeFacet=outfitManager.getAttributeByName(ATR_NAME);
		   if (vendorSku.getCarSkuAttributes() != null && !vendorSku.getCarSkuAttributes().isEmpty()) {
				ArrayList<CarSkuAttribute> skuAttrList = new ArrayList<CarSkuAttribute>(vendorSku.getCarSkuAttributes());
				for(CarSkuAttribute skuAttr: skuAttrList) {
					if (ATR_NAME.equals(skuAttr.getAttribute().getName())){
						carSkuAttr=skuAttr;
						if(log.isDebugEnabled()){
							log.debug("Sku "+ATR_NAME+" is"+carSkuAttr);
							log.debug("and its value is"+carSkuAttr.getAttrValue());
						}
				}
			}
		   }
		   if(carSkuAttr==null){
			   carSkuAttr=new CarSkuAttribute();
			   carSkuAttr.setAttribute(attrSizeFacet);
			   carSkuAttr.setAttrValue(attrComponentVal);
			   carSkuAttr.setCreatedBy(Constants.SCHEDULER_CREATEDBY);
			   carSkuAttr.setCreatedDate(new Date());
			   carSkuAttr.setUpdatedBy(Constants.SCHEDULER_UPDATEDBY);
			   carSkuAttr.setUpdatedDate((new Date()));
			   carSkuAttr.setVendorSku(vendorSku);
			   vendorSku.getCarSkuAttributes().add(carSkuAttr);
			} else {
				carSkuAttr.setAttrValue(attrComponentVal);
				carSkuAttr.setUpdatedBy(Constants.SCHEDULER_UPDATEDBY);
				carSkuAttr.setUpdatedDate((new Date()));
				vendorSku.getCarSkuAttributes().add(carSkuAttr);
			}
		   
		   return vendorSku;
	   }
	   
	   
	  
	  public  List<VendorSku> getVendorSkusByColorRange(String colorCodeBegin,String colorCodeEnd){
		  return getCarManager().getVendorSkusByColorCode(colorCodeBegin, colorCodeEnd);
	  }
	  
	  public List<VendorSku>getVendorSkuWithNoSizeRuleApplied(){
		  List<VendorSku> skus = getCarManager().getVendorSkusHavingNoSizeRule();  
		  return skus;
	  }
	  
	  public List<String> bulkInsertNewSizeRules(){
		  List<VendorSku> skus = getCarManager().getVendorSkusHavingNoSizeRule();
		  if(log.isDebugEnabled()){
			  log.debug("vendor skus"+skus.size());
		  }
		  List<String> newSizeNameList = new ArrayList<String>();
		  for(VendorSku sku : skus ){
			  String sizeName= sku.getSizeName();
			  String deptId = sku.getCar().getDepartment().getDeptIdAsString();
			  String classId = sku.getVendorStyle().getClassification().getClassificationIdAsString();
			  String vendorId = sku.getVendorStyle().getVendor().getVendorIdAsString();
			  if ((getSizeConversionManager().getSizeConversion(sizeName,classId,deptId,vendorId) == null) || StringUtils.isNotBlank(sizeName)){
				  SizeConversionMaster sizeRule = new SizeConversionMaster();
				  sizeRule.setSizeName(sizeName);
				  sizeRule.setCoversionSizeName("null");
				  sizeRule.setStatusCode(Constants.SIZECONV_STATUS_PENDING); //setting status as P ie Pending. User would manually go and change conv-value after which status would be 'A'
				  sizeRule.setCreatedBy(Constants.SCHEDULER_CREATEDBY);
				  sizeRule.setCreatedDate(new Date());
				  sizeRule.setUpdatedBy(Constants.SCHEDULER_UPDATEDBY);
				  sizeRule.setUpdatedDate((new Date()));
				  getSizeConversionManager().saveOrUpdateSizeConversion(sizeRule);
				  newSizeNameList.add(sizeName);
			  }
		  }
		  if(log.isDebugEnabled()){
			  log.debug("newSizeNameList :"+newSizeNameList.size());
		  }
		  return newSizeNameList;
	  }
	  
	  public Integer getIterationCount(int rowCount,int batchSize){
		  if(log.isDebugEnabled()){
			  	log.debug("ROWCOUNT:"+rowCount+" batchSize: "+batchSize);
		  }
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
	    

