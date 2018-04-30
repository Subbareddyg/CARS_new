package com.belk.car.app.model;
import java.io.Serializable;

import javax.persistence.Embeddable;
public class DBPromotionCarAttributeId {private CarAttribute  carAttribute;
private Car car;

public DBPromotionCarAttributeId(){  
}

public DBPromotionCarAttributeId(Car car, CarAttribute carAttribute){
	  this.car = car;
	  this.carAttribute = carAttribute;
}}
