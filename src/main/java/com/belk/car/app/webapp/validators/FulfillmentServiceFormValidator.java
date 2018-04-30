package com.belk.car.app.webapp.validators;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.model.oma.FulfillmentService;
import com.belk.car.app.webapp.forms.FulfillmentServiceForm;

/*
 * Validator Class to validate all the Address fields which will populate(seen) on the screen only 
 * if fulfillment method is Belk Owned Inventory
 */
public class FulfillmentServiceFormValidator implements Validator,DropShipConstants{

	

	public boolean supports(Class arg0) {
		return FulfillmentServiceForm.class.isAssignableFrom(arg0);
	}

	public void validate(Object arg0, Errors errors) {
		FulfillmentServiceForm fulfillmentServiceForm = (FulfillmentServiceForm) arg0;
		FulfillmentService fulfillmentService=fulfillmentServiceForm.getFulfillmentService();
		Address addr=fulfillmentServiceForm.getAddress();
	
		/*Check if the Integration Type is Direct*/
		if("1".equals(fulfillmentService.getFulfillmentServiceIntTypeID().getIntegrationTypeID())){
		String locName=addr.getLocName();
		if((locName==null)||(locName.length()==0)){
			errors.rejectValue("address.locName", "not_null",  LOCATION_NAME_IS_REQUIRED_MSG);
		}
		String addr1=addr.getAddr1();
		if((addr1==null)||(addr1.length()==0)){
			errors.rejectValue("address.addr1", "not_null",  ADDRESS_LINE1_IS_REQUIRED_MSG);
		}
		String city=addr.getCity();
		if((city==null)||(city.length()==0)){
			errors.rejectValue("address.city", "not_null",  CITY_IS_REQUIRED_MSG);
		}
		String zip=addr.getZip();
		if((zip==null)||(zip.length()==0)){
			errors.rejectValue("address.zip", "not_null",  ZIP_CODE_IS_REQUIRED_MSG);
		}
		String state=addr.getState();
		if((state==null)||(state.length()==0)){
			errors.rejectValue("address.state", "not_null",  STATE_IS_REQUIRED_MSG);
		}
		
		}
		
	}
 
}
