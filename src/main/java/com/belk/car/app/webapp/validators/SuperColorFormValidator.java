package com.belk.car.app.webapp.validators;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.Constants;
import com.belk.car.app.service.SuperColorManager;
import com.belk.car.app.webapp.forms.SuperColorForm;

/**
 * The validator Class for Super Color ADD/EDIT form validations.
 * Validation set are for the following(list may change):
 * 1.Nonempty Fields: Super Color ode/Super Color Name/Begin Color Code/ End Color Code.
 * 2.Unique Super Color Code/SperColor Name.
 * 3.Unique Begin Color Cod-End Color Code Range. ie To prevent the range overlapping.
 * @author Yogesh.Vedak
 *
 */
public class SuperColorFormValidator implements Validator{

	
	private SuperColorManager superColorManager;
	
	public SuperColorManager getSuperColorManager() {
		return superColorManager;
	}

	public void setSuperColorManager(SuperColorManager superColorManager) {
		this.superColorManager = superColorManager;
	}	
	
	
	
	@Override
	public boolean supports(Class clazz) {
		return SuperColorForm.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object obj, Errors errors) {
		SuperColorForm scolorForm = (SuperColorForm) obj;
		long currntObjectCmmId = scolorForm.getCmmId();
		String superColorName = scolorForm.getSuperColorName();
		String superColorCode = StringUtils.leftPad(scolorForm.getSuperColorCode(),Constants.COLORCODE_MAXLEN, Constants.COLORCODE_LEFTPAD);
		String colorCodeBegin = StringUtils.leftPad(scolorForm.getColorCodeBegin(),Constants.COLORCODE_MAXLEN,Constants.COLORCODE_LEFTPAD);
		String colorCodeEnd = StringUtils.leftPad(scolorForm.getColorCodeEnd(),Constants.COLORCODE_MAXLEN,Constants.COLORCODE_LEFTPAD);
		Boolean isEditForm =  scolorForm.getCmmId() != 0L?Boolean.TRUE:Boolean.FALSE;
		
		//for super Color code
		if(StringUtils.isNotBlank(superColorCode)){
			if(isSuperColorCodeUnacceptable(superColorCode,currntObjectCmmId))
			{	
			 Object[] args = {superColorCode};
			 errors.rejectValue("superColorCode", "errors.existing.supercolor.code",args,"Super Color Code Should be unique");
			}
			//if super Color Code must have to be kept within its Begin and End range then just we can uncomment following 'else if' code snippet 
			/*else if(!((Integer.parseInt(superColorCode)>= Integer.parseInt(colorCodeBegin)) &&(Integer.parseInt(superColorCode)<= Integer.parseInt(colorCodeEnd)))){
				Object[] args = {superColorCode,colorCodeBegin,colorCodeEnd};
				 errors.rejectValue("superColorCode", "errors.outofrange.supercolor.code",args,"Super Color Code is out of specified Range");
			}*/
		}else{
			Object[] args = {Constants.SUPERCOLOR_CODE};
			errors.rejectValue( "superColorCode", "errors.required",args,"Super Color Code should not be Empty");
		}
		
		// for super color name
		if(StringUtils.isNotBlank(superColorName)){
			/*if((!isEditForm) && (sizeSuperColorManager.getSuperColorByColorName(superColorName)) != null )
			{*/
			if(isSuperColorNameUnacceptable(superColorName,currntObjectCmmId)){
				Object[] args = {scolorForm.getSuperColorName()};
				errors.rejectValue("superColorName", "errors.existing.supercolor.name",args,"Super Color Should be unique.Enter a diffrent one");
			}
		}else{
			Object[] args = {Constants.SUPERCOLOR_NAME};
			errors.rejectValue("superColorName", "errors.required",args,"Super Color Name should not be Empty" );
		}
		//for color code begin
		if(StringUtils.isNotBlank(colorCodeBegin))
		{
			if((getSuperColorManager().isColorBeginCodeOverlaps(colorCodeBegin,currntObjectCmmId)))
			{
			/*if(isBeginCodeUnacceptable(superColorName,currntObjectCmmId)){*/
			Object[] args = {colorCodeBegin};
			errors.rejectValue("colorCodeBegin", "supercolor.validate.colorCodeBegin.overlaps",args,"Begin color code overlaps with the existing range");	
			}else if(Integer.parseInt(colorCodeBegin) > Integer.parseInt(colorCodeEnd)){
				Object[] args = {Constants.SUPERCOLOR_BEGINCODE};
				errors.rejectValue("colorCodeBegin", "supercolor.validate.colorCodeBegin.greater",args,"Begin Color Code must be lesser than Color end code");
			}
		}else{
			Object[] args = {Constants.SUPERCOLOR_BEGINCODE};
			errors.rejectValue("colorCodeBegin", "errors.required",args,"Begin Color Code is required");	
		}
		
		//for color code end
		if(StringUtils.isNotBlank(colorCodeEnd))
		{
		/*if((!isEditForm) && (sizeSuperColorManager.isColorEndCodeOverlaps(colorCodeEnd)))
		{*/
			if(isEndCodeUnacceptable(colorCodeEnd,currntObjectCmmId)){
				Object[] args = {Constants.SUPERCOLOR_ENDCODE};
			errors.rejectValue("colorCodeEnd", "supercolor.validate.colorCodeEnd.overlaps",args,"End Code  overlaps with the existing range");	
		}
		}else{
			Object[] args = {Constants.SUPERCOLOR_ENDCODE};
			errors.rejectValue("colorCodeEnd", "errors.required",args,"End Color Code is required");	
		}
		if((StringUtils.isNotBlank(colorCodeBegin)) && (StringUtils.isNotBlank(colorCodeEnd)) )
		{
			if(Integer.parseInt(colorCodeEnd) < Integer.parseInt(colorCodeBegin)){
				errors.rejectValue("colorCodeEnd", "supercolor.validate.colorCodeEnd.greater");	
			}
		}
	}
	
	Boolean isSuperColorCodeUnacceptable(String superColorCode,long currntObjectId){
		long otherColorCmmId = getSuperColorManager().getSuperColorByColorCode(superColorCode)!=null?getSuperColorManager().getSuperColorByColorCode(superColorCode).getCmmId():currntObjectId ; 
		if(otherColorCmmId != currntObjectId ){
			return Boolean.TRUE;
		}
	return Boolean.FALSE;
	}
	
	Boolean isSuperColorNameUnacceptable(String superColorName,long currntObjectId){
		long otherColorCmmId = getSuperColorManager().getSuperColorByColorName(superColorName)!=null?getSuperColorManager().getSuperColorByColorName(superColorName).getCmmId():currntObjectId ; 
		if(otherColorCmmId != currntObjectId ){
			return Boolean.TRUE;
		}
	return Boolean.FALSE;
	}
	
	Boolean isBeginCodeUnacceptable(String beginCode,long currntObjectId){
		//long otherColorCmmId = sizeSuperColorManager.getSuperColorByBeginCode(beginCode)!=null?sizeSuperColorManager.getSuperColorByBeginCode(beginCode).getCmmId():currntObjectId ; 
		if(getSuperColorManager().isColorBeginCodeOverlaps(beginCode, currntObjectId) ){
			return Boolean.TRUE;
		}
	return Boolean.FALSE;
	}
	
	Boolean isEndCodeUnacceptable(String endCode,long currntObjectId){
		//long otherColorCmmId = sizeSuperColorManager.getSuperColorByBeginCode(endCode)!=null?sizeSuperColorManager.getSuperColorByBeginCode(endCode).getCmmId():currntObjectId ; 
		if(getSuperColorManager().isColorBeginCodeOverlaps(endCode, currntObjectId)){
			return Boolean.TRUE;
		}
	return Boolean.FALSE;
	}
	
	public void setError(String fieldName,String messagekey,Object[] args,String defaultMsg){
		
	}
	
}
