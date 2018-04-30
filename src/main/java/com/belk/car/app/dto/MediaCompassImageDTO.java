package com.belk.car.app.dto;

import java.io.Serializable;

public class MediaCompassImageDTO implements Serializable, Comparable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7985957574579187301L;
	
	private Long CarId;
	private Long SampleId;
	private String ImageName;
		
	@Override
	public int compareTo(Object arg0) {
		String name = ((MediaCompassImageDTO) arg0).getImageName();
		return this.ImageName.compareToIgnoreCase(name);
	}
	
	
	
	/**
	 * @return the carId
	 */
	public Long getCarId() {
		return CarId;
	}


	/**
	 * @param carId the carId to set
	 */
	public void setCarId(Long carId) {
		CarId = carId;
	}

	/**
	 * @return the sampleId
	 */
	public Long getSampleId() {
		return SampleId;
	}


	/**
	 * @param sampleId the sampleId to set
	 */
	public void setSampleId(Long sampleId) {
		SampleId = sampleId;
	}


	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return ImageName;
	}


	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		ImageName = imageName;
	}
 @Override
public String toString() {
	// TODO Auto-generated method stub
   return "ToString: carsID "+ this.CarId + "sampleId" + this.getSampleId();	
}

}
