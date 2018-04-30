package com.belk.car.app.dao;

import java.util.Map;
import com.belk.car.app.dto.RRDCheckEmailNotificationDTO;

public interface  VendorImageEmailNotificationDao {

	public Map<String, RRDCheckEmailNotificationDTO> populateEmailListForAllCARS();
	

}
