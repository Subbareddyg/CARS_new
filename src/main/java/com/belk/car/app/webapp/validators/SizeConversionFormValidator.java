package com.belk.car.app.webapp.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Classification;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.SizeConversionManager;
import com.belk.car.app.webapp.forms.SizeConversionForm;

/**
 * The Validator Class for SIZE Conversion ADD/EDIT Form.
 * The class as of now controls for the following(List May Change):
 * 1.Unique Size Name
 * 2.Valid Department Code/Vendor Number/Class Number
 * 3.Unique value combination of Department Code,Vendor Number and class Number for entered Size Name.
 * @author Yogesh.Vedak
 *
 */
public class SizeConversionFormValidator implements Validator{

	
	private SizeConversionManager sizeConversionManager;
	private CarManager carManager;
	boolean escapeUniqueSizeNameCheck= false;
	int sizeCount=0;
	
	public SizeConversionManager getSizeConversionManager() {
		return sizeConversionManager;
	}
	public void setSizeConversionManager(SizeConversionManager sizeConversionManager) {
		this.sizeConversionManager = sizeConversionManager;
	}
	
	
	public CarManager getCarManager() {
		return carManager;
	}
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
	@Override
	public boolean supports(Class clazz) {
		return SizeConversionForm.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
		SizeConversionForm sizeConversionForm = (SizeConversionForm) obj;
		long scmId = sizeConversionForm.getScmId();
		String sizeName = sizeConversionForm.getSizeName();
		String coversionSizeName = sizeConversionForm.getCoversionSizeName();
		Boolean isEditForm =  sizeConversionForm.getScmId() != 	0L?Boolean.TRUE:Boolean.FALSE;
		String vendorNumberArray = sizeConversionForm.getVendorNumber();
		String deptCodeArray = sizeConversionForm.getDeptCode();
		String strClassNumArray = sizeConversionForm.getClassNumber();
		escapeUniqueSizeNameCheck = false;
		
		//FNY14 chances of multiple values for dept,vendor,class
		//System.out.println("vendorNumberArray==="+vendorNumberArray+"  deptCodeArray==="+deptCodeArray+"    strClassNumArray==="+ strClassNumArray);
		String vendorNumber;
		String deptCode;
		String strClassNum;
		int vendorSize =0;
		int deptSize = 0;
		int classSize = 0;
		String[] vendorArray=null;
		String[] deptArray=null;
		String[] classArray=null;
		String[] fieldArray=null;
		//EC:1602 CARS Size Name Issue -->Starts
		String strVendorId = "";
		String strClassId = "";
		String strDeptId = "";
		//String classNum = "";
		String classId = "";
		String deptId = "";
		String vendorId = "";
		//FNY14 chances of multiple values for dept,vendor,class ONLY for CREATE
		boolean isMultipleVendor = false;
		boolean isMultipleClass = false;
		boolean isMultipleDept = false;
		

		
		if(isEditForm) {//edit normal process
				validateSizeConversion(scmId,sizeName,coversionSizeName,vendorNumberArray,deptCodeArray,strClassNumArray,isEditForm,errors);
		}else{ //create
			//System.out.println("Inside Create------>");
				//vendor array
				if(StringUtils.isNotBlank(vendorNumberArray))
						vendorArray=vendorNumberArray.split(",");
						
				if(StringUtils.isNotBlank(deptCodeArray))
						deptArray=deptCodeArray.split(",");
				
				if(StringUtils.isNotBlank(strClassNumArray))
						classArray=strClassNumArray.split(",");
						
						
				vendorSize = vendorArray ==null ? 0 : vendorArray.length;
				deptSize = deptArray ==null ? 0 : deptArray.length;
				classSize = classArray ==null ? 0 : classArray.length;
				
				vendorNumber = vendorSize == 0 ? "" : vendorArray[0];
				deptCode = deptSize == 0 ? "" : deptArray[0];
				strClassNum = classSize == 0 ? "" : classArray[0];
				
				//find the muliple values field
				if(vendorSize > 1){
					isMultipleVendor=true; 
					fieldArray = vendorArray;
					
				}else if(deptSize > 1){
					isMultipleDept = true; 
					fieldArray = deptArray;
					
				}else if(classSize > 1){
					isMultipleClass =true;
					fieldArray = classArray;
					
				}
				//loop for multipiple values in ONE field
				if(isMultipleVendor) {
					for(int i=0;i<fieldArray.length;i++){
							validateSizeConversion(scmId,sizeName,coversionSizeName,fieldArray[i],deptCode,strClassNum,isEditForm,errors);
					}		
				}else if(isMultipleDept) {
					for(int i=0;i<fieldArray.length;i++){
							validateSizeConversion(scmId,sizeName,coversionSizeName,vendorNumber,fieldArray[i],strClassNum,isEditForm,errors);
					}
				}else if(isMultipleClass) {
					for(int i=0;i<fieldArray.length;i++){
							validateSizeConversion(scmId,sizeName,coversionSizeName,vendorNumber,deptCode,fieldArray[i],isEditForm,errors);
					}
				}else {//No Mutiple values so only one mapping rule			
					//System.out.println("Validator -------> ONE mapping rule");			
					validateSizeConversion(scmId,sizeName,coversionSizeName,vendorNumber,deptCode,strClassNum,isEditForm,errors);
				}
				
			}
		
	}



		private void validateSizeConversion(Long scmId, String sizeName,String coversionSizeName, String vendorNumber, String deptCode, String strClassNum, Boolean isEditForm,Errors errors) {
	
			String strVendorId = "";
			String strClassId = "";
			String strDeptId = "";
			//String classNum = "";
			String classId = "";
			String deptId = "";
			String vendorId = "";
			
			if(StringUtils.isNotBlank(vendorNumber) && !vendorNumber.equals("") && !vendorNumber.equalsIgnoreCase("null")){
				Vendor venNumber = carManager.getVendor(vendorNumber);
				if(venNumber !=null && !venNumber.equals("") && !venNumber.equals("null")){
					//strVendorId = venNumber.getVendorIdAsString();
					strVendorId = !vendorNumber.equals("")?carManager.getVendor(vendorNumber).getVendorIdAsString():"";
				}
						//strVendorId = venNumber.getVendorIdAsString();
			}
			
			if(StringUtils.isNotBlank(deptCode) && !deptCode.equals("") && !deptCode.equalsIgnoreCase("null")){
				Department deptNumber = carManager.getDepartment(deptCode);
				if(deptNumber !=null && !deptNumber.equals("") && !deptNumber.equals("null")){ 
					deptId = !deptCode.equals("")?carManager.getDepartment(deptCode).getDeptIdAsString():"";
				}
							//strDeptId = deptNumber.getDeptIdAsString();
			}
			
			if(StringUtils.isNotBlank(strClassNum) && !strClassNum.equals("") && !strClassNum.equalsIgnoreCase("null")){
				//System.out.println("strClassNummm=="+strClassNum);
				Classification classNumber = null;
				try{
				classNumber = carManager.getClassification(new Short(strClassNum).shortValue());//
				//System.out.println("classNumber=="+classNumber);
				}catch (Exception ex){
					ex.printStackTrace();				
				}
				//classNum = classNumber.getBelkClassNumber();
				if(classNumber !=null && !classNumber.equals("") && !classNumber.equals("null")){
						classId = !(strClassNum.equals(""))?carManager.getClassification(new Short(strClassNum).shortValue()).getClassificationIdAsString():"";
				}
							//strClassId = classNumber.getClassificationIdAsString();
			}
					// for conversion name
			if(StringUtils.isNotBlank(coversionSizeName)){
			//if((!isEditForm) &&(getSizeConversionManager().getSizeConversionByConversionSizeName(coversionSizeName)) != null )						
				try {
					classId = !(classId.equals(""))?classId:"";
					deptId = !deptId.equals("")?deptId:"";
					vendorId = !strVendorId.equals("")?strVendorId:"";
					//System.out.println("SizeValidator:ClassId is:" +classId+" deptId is :"+deptId+" VendorId is:"+vendorId);
					} catch (Exception e) {
							// TODO: handle exception
						e.printStackTrace();
							
					}
					if((!isEditForm) &&(getSizeConversionManager().getSizeConversionName(sizeName,coversionSizeName,vendorId,deptId,classId)) != null ) //EC:1602 CARS Size Name Issue -->Ends here
						{	
							System.out.println("coversionSizeName non emoty"); Object[] args = {vendorNumber,deptCode,strClassNum};
							if(sizeCount == 0) //add only one time
								errors.rejectValue("coversionSizeName", "errors.existing.size.conversionname",args,"Size Conversion Name should be unique");

							sizeCount++;
						}
			}else{
					System.out.println("SIZE_CONVERSIONNAME empty");
					Object[] args = {Constants.SIZE_CONVERSIONNAME}; errors.rejectValue("coversionSizeName","errors.required",args,"Size Conversion Name should not be Empty" ); 
			}
			//for valid vendorNumber
				if(StringUtils.isNotBlank(vendorNumber)){
					if(getCarManager().getVendor(vendorNumber) ==null ){
						escapeUniqueSizeNameCheck = true;
						Object[] args = {vendorNumber};
						errors.rejectValue("vendorNumber", "errors.vendor.unavailable",args,"No Vendor exists with this number" );
					}
				}
				//for valid deptCode
				if(StringUtils.isNotBlank(deptCode)){
					if(getCarManager().getDepartment(deptCode) == null ){
						escapeUniqueSizeNameCheck = true;
						Object[] args = {deptCode};
						errors.rejectValue("deptCode", "errors.dept.unavailable",args,"No Department exists with this number" );
						}
				}
					
				//for valid classificationNumber
				if(StringUtils.isNotBlank(strClassNum)){
					System.out.println("ClassNumber recived is likes:"+strClassNum);
					Classification classNumber = null;
					try{
						classNumber = getCarManager().getClassification( new Short(strClassNum).shortValue());//
					}catch (Exception ex){
						ex.printStackTrace();				
					}
	

					if(classNumber == null ){
					escapeUniqueSizeNameCheck = true;
					Object[] args = {strClassNum};
					errors.rejectValue("classNumber", "errors.classification.unavailable",args,"No Belk Class Number exists with this number" );
					}
				}
					//for size Name 
				if(StringUtils.isNotBlank(sizeName)){
					System.out.println("escapeUniqueSizeNameCheck val is "+escapeUniqueSizeNameCheck);
						if(!escapeUniqueSizeNameCheck){
								if(!(isSizeNameUnique(scmId,sizeName,strClassNum,deptCode,vendorNumber))){	
									 System.out.println("sizeName not unique");
									 Object[] args = {deptCode,vendorNumber,strClassNum};
									 errors.rejectValue("sizeName", "errors.existing.size.sizeName",args,"Size Name Should be unique");
									// addError(vendorNumber,deptCode,strClassNum);
									 //errors.rejectValue("sizeName", "	errors.existing.size.sizeName",args,"Size Name Should be unique");
								}
						}
				}else{
						System.out.println("sizeName empty");
						Object[] args = {Constants.SIZE_NAME};
						errors.rejectValue( "sizeName", "errors.required",args,"Size Name should not be Empty"); 
				}
	}	


	private boolean isSizeNameUnique(long scmId,String sizeName,String classNumber,String deptCod,String vendorNumber){
	
		String classId = !(classNumber.equals(""))?carManager.getClassification(new Short(classNumber).shortValue()).getClassificationIdAsString():"";
		String deptId = !deptCod.equals("")?carManager.getDepartment(deptCod).getDeptIdAsString():"";
		String vendorId = !vendorNumber.equals("")?carManager.getVendor(vendorNumber).getVendorIdAsString():"";
		System.out.println("SizeValidator:ClassId is:" +classId+" deptId is :"+deptId+" VendorId is:"+vendorId);
		if((getSizeConversionManager().isSizeConversionExists(scmId,sizeName, classId, deptId, vendorId))){
			return false;
		}
		return true;
	}

	
		
}
