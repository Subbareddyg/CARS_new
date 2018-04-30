package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.belk.car.app.model.CarAttribute;

public class MissingAttributeValueList implements Serializable {
	
	private List<MissingAttributeValue> missingAttributeValues = new ArrayList<MissingAttributeValue>();
	
	private Map<String, MissingAttributeValue> attributeMap = new HashMap<String, MissingAttributeValue>() ;

	public List<MissingAttributeValue> getMissingAttributeValues() {
		return missingAttributeValues;
	}

	public void setMissingAttributeValues(List<MissingAttributeValue> missingAttributeValues) {
		this.missingAttributeValues = missingAttributeValues;
	}
	
	
	public void add(CarAttribute carAttribute) {
		String key = carAttribute.getAttribute().getName() + "-" + carAttribute.getAttrValue();
		MissingAttributeValue mav = attributeMap.get(key);
		if(mav == null) {
			mav = new MissingAttributeValue();
			mav.setAttribute(carAttribute.getAttribute()) ;
			mav.setCarAttributeId(String.valueOf(carAttribute.getCarAttrId()));
			mav.setValue(carAttribute.getAttrValue()) ;
			mav.setOldValue(carAttribute.getAttrValue());
			mav.getCarAttributes().add(carAttribute) ;
			missingAttributeValues.add(mav);
			attributeMap.put(key, mav) ;
		} else {
			mav.getCarAttributes().add(carAttribute) ;
		}
	}

	public void addAll(Collection from) {
		Iterator iter = from.iterator();
		while (iter.hasNext())
			add((CarAttribute) iter.next());
	}
}
