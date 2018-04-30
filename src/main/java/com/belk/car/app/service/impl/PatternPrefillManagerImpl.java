/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.service.impl;

import com.belk.car.app.dao.CarDao;
import com.belk.car.app.dao.DropshipDao;
import com.belk.car.app.model.Attribute;
import com.belk.car.app.model.AttributeValueProcessStatus;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.service.PatternPrefillManager;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author AFUSY85
 */
public class PatternPrefillManagerImpl implements PatternPrefillManager  {
 private DropshipDao dropshipDao;
 private CarDao carDao;
	private transient final Log log = LogFactory
	.getLog(PatternPrefillManagerImpl.class);
    public DropshipDao getDropshipDao() {
        return dropshipDao;
    }

    public void setDropshipDao(DropshipDao dropshipDao) {
        this.dropshipDao = dropshipDao;
    }
    public CarDao getCarDao() {
        return carDao;
    }

    public void setCarDao(CarDao carDao) {
        this.carDao = carDao;
    }
    public void prefIllPatternStyle() {


     try {
    //Get the List of all the Cars for Vendor Styles which are part of a pattern.
    List <Car> carList = dropshipDao.getPatternCars();
    if(carList != null) {
    	if (log.isDebugEnabled()) {
    		log.debug("Car List Size -" + carList.size());
    	}

         Map parentStyleMap  = new HashMap();

         Iterator itr1 = carList.iterator();
         if (log.isDebugEnabled()) {
        	 log.debug("Start First Step......");
         }
         while(itr1.hasNext()) {
             Car childCar =  (Car) itr1.next();
             Long parentVendorStyleaId = childCar.getVendorStyle().getParentVendorStyle().getVendorStyleId();
             if (log.isDebugEnabled()) {
            	 log.debug("Parent Vendor Style Id -" + parentVendorStyleaId);
             log.debug("parentStyleMap.containsKey(parentVendorStyleaId)->" + parentStyleMap.containsKey(parentVendorStyleaId));
             }
             if(parentStyleMap.containsKey(parentVendorStyleaId)) {
                 List childStyleCarsList = (List) parentStyleMap.get(parentVendorStyleaId);
                 childStyleCarsList.add(childCar);
             } else {
                List <Car>  childStyleCarsList =  new ArrayList ();
                childStyleCarsList.add(childCar);
                parentStyleMap.put(parentVendorStyleaId, childStyleCarsList);
             }

         }
         Set parentStyleIdSet = parentStyleMap.keySet();
         Iterator itr2 = parentStyleIdSet.iterator();
         while(itr2.hasNext()) {
             Long parentVendorStyleaId = (Long) itr2.next();
             Car parentStyleCar = dropshipDao.getParentStyleCar(parentVendorStyleaId);
             if(null !=parentStyleCar){
             List childStyleCarsList = (List) parentStyleMap.get(parentVendorStyleaId);
             Set<CarAttribute> commonAttributes = getCommonAttributes(childStyleCarsList);
             if(commonAttributes !=null &&!commonAttributes.isEmpty()) {
                 Set<CarAttribute> parentStyleAttributes =parentStyleCar.getCarAttributes();
                 if(parentStyleAttributes !=null && !parentStyleAttributes.isEmpty()) {
                     Iterator itr = commonAttributes.iterator();
                     while(itr.hasNext()) {
                         CarAttribute commonAttribute = (CarAttribute) itr.next();
                         Attribute attribute = commonAttribute.getAttribute();
                         Iterator itr3 = parentStyleAttributes.iterator();
                         boolean attribFlag = false;
                         while(itr3.hasNext()) {
                             CarAttribute parentCarAttribute = (CarAttribute) itr3.next();
                             Attribute parentAttribute = parentCarAttribute.getAttribute();
                             if(attribute.getAttributeIdAsString().equals(parentAttribute.getAttributeIdAsString())) {
                                 attribFlag =true;
                                 StringUtils.isNotBlank(null);
                                  if((parentCarAttribute.getAttrValue()==null || StringUtils.isBlank(parentCarAttribute.getAttrValue()))
                                  && (StringUtils.isNotBlank(commonAttribute.getAttrValue())) ) {
                                      parentCarAttribute.setAttrValue(commonAttribute.getAttrValue());
                                  }
                             }
                         }


                         //add the attribute if the common attribute is not as attribute for the CARS of pafrent style.
                         if(!attribFlag) {
                        	 if (log.isDebugEnabled()) {
                        		 log.debug("Attribute id"+attribute.getAttributeIdAsString());
                        	 }
                             CarAttribute newAttribute  = new CarAttribute();
                             newAttribute.setAttribute(attribute);
                             newAttribute.setAttrValue(commonAttribute.getAttrValue());
                             newAttribute.setCar(parentStyleCar);
                             newAttribute.setCreatedBy("CARSDEV");
                             newAttribute.setUpdatedBy("CARSDEV");
                             newAttribute.setCreatedDate(new Date());
                             newAttribute.setUpdatedDate(new Date());
                             newAttribute.setDisplaySeq((short)0);
                             newAttribute.setHasChanged("N");
                             newAttribute.setIsChangeRequired("N");
                             newAttribute.setStatusCd("ACTIVE");
                             AttributeValueProcessStatus attributeValueProcessStatus =  new AttributeValueProcessStatus();
                             attributeValueProcessStatus.setProcessStatusCd(AttributeValueProcessStatus.CHECK_REQUIRED);
                             newAttribute.setAttributeValueProcessStatus(attributeValueProcessStatus);
                             parentStyleAttributes.add(newAttribute);
                         }

                     }
                     
                     
                 } else {
                     //Commented for not to set the attributes
                    // parentStyleCar.setCarAttributes(commonAttributes);
                 }
                 if (log.isDebugEnabled()) {
                	 log.debug("Save CAR");
                 }
                 carDao.updateCar(parentStyleCar, "CARSDEV");
                 
             }
                
             
             }
            

         }
        
         if (log.isDebugEnabled()) {
        	 log.debug("End Second Step......");
         }
    } 
         
    } catch(Exception e) {
        e.printStackTrace();
    }

    

 }


 private Set<CarAttribute> getCommonAttributes(List childStyleCarsList)    {

Set<CarAttribute> commonAttributeSet  =  new  HashSet<CarAttribute>();
     try {

     
     Car firstChildStyleCar  = (Car)childStyleCarsList.get(0);

     Set attributeSet = firstChildStyleCar.getCarAttributes();

     if(null !=attributeSet && !attributeSet.isEmpty()) {
         
         Iterator itr1 = attributeSet.iterator();

         while(itr1.hasNext()) {
             boolean isCommonAttribute =true;
             CarAttribute attribute  =  (CarAttribute) itr1.next();
             Iterator itr2 = childStyleCarsList.iterator();
             while(itr2.hasNext()) {
                 if(isCommonAttribute) {
                     Car childCar = (Car)itr2.next();
                     Set childCarAttributeSet = childCar.getCarAttributes();
                     if(childCarAttributeSet !=null && !childCarAttributeSet.isEmpty()) {
                        
                         if(!childCarAttributeSet.contains(attribute)) {
                             isCommonAttribute = false;
                         } 
                     }
                 }
                 
             }
             if(isCommonAttribute) {
                 commonAttributeSet.add(attribute);
             }
         }

         
     }
     } catch(Exception e) {
         e.printStackTrace();
     }

     return commonAttributeSet;

 }
}
