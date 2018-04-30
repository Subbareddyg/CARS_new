/**
 * 
 */
package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.List;

import com.belk.car.app.model.Car;

/**
 * @author afurxd2
 *
 */
public class FailedImagesDeptDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String deptCd;
	private String deptName;
	private	List<FailedImageDTO> failedImagesDTO ;
		
	/**
	 * @return the deptCd
	 */
	public String getDeptCd() {
		return deptCd;
	}

	/**
	 * @param deptCd the deptCd to set
	 */
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	
	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}

	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return the failedImagesDTO
	 */
	public List<FailedImageDTO> getFailedImagesDTO() {
		return failedImagesDTO;
	}

	/**
	 * @param failedImagesDTO the failedImagesDTO to set
	 */
	public void setFailedImagesDTO(List<FailedImageDTO> failedImagesDTO) {
		this.failedImagesDTO = failedImagesDTO;
	}
	
	

}
