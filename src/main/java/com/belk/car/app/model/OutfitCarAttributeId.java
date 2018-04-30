package com.belk.car.app.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class OutfitCarAttributeId implements Serializable{
	
  private CarAttribute  carAttribute;
  private Car car;
  
  public OutfitCarAttributeId(){  
  }
  
  public OutfitCarAttributeId(Car car, CarAttribute carAttribute){
	  this.car = car;
	  this.carAttribute = carAttribute;
  }
}
