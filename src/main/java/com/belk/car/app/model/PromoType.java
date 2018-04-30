package com.belk.car.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity  
@Table(name = "PROMO_TYPE")
public class PromoType {
	
	public static String YES="Yes";
	public static String NO="No";
	
	private long carId;  
	private String isGWP;  
	private String isPYG;
	


	public PromoType(){
		
	}
	
	
	@Id
	@Column(name = "CAR_ID", unique = true, nullable = false)
	public long getCarId() {
		return carId;
	}
	public void setCarId(long carId) {
		this.carId = carId;
	}
	
	
	@Column(name = "IS_GWP")
	public String getIsGWP() {
		return isGWP;
	}


	public void setIsGWP(String isGWP) {
		this.isGWP = isGWP;
	}
	
	
	
	@Column(name = "IS_PYG")
	public String getIsPYG() {
		return isPYG;
	}


	public void setIsPYG(String isPYG) {
		this.isPYG = isPYG;
	}
	
}


