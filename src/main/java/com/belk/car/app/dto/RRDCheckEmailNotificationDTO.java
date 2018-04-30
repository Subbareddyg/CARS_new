/**
 * 
 */
package com.belk.car.app.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.appfuse.model.User;

/**
 * @author afurxd2
 *
 */
public class RRDCheckEmailNotificationDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	User user;

	Map<String, FailedImagesDeptDTO> mapFailedImagesDeptDTO = new HashMap<String, FailedImagesDeptDTO>();


	/**
	 * @return the userDetailObject
	 */
	public User getUserDetailObject() {
		return user;
	}
	/**
	 * @param userDetailObject the userDetailObject to set
	 */
	public void setUserDetailObject(User userDetailObject) {
		this.user = userDetailObject;
	}
	
	/**
	 * @return the mapFailedImagesDeptDTO
	 */
	public Map<String, FailedImagesDeptDTO> getMapFailedImagesDeptDTO() {
		return mapFailedImagesDeptDTO;
	}
	/**
	 * @param mapFailedImagesDeptDTO the mapFailedImagesDeptDTO to set
	 */
	public void setMapFailedImagesDeptDTO(
			Map<String, FailedImagesDeptDTO> mapFailedImagesDeptDTO) {
		this.mapFailedImagesDeptDTO = mapFailedImagesDeptDTO;
	}
	
}
