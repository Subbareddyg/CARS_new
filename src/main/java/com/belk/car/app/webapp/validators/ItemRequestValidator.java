/**
 * 
 */
package com.belk.car.app.webapp.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.service.FulfillmentServiceManager;
import com.belk.car.app.webapp.forms.ItemRequestForm;

/**
 * @author afusy01
 *
 */
public class ItemRequestValidator implements Validator,DropShipConstants {

	private FulfillmentServiceManager fulfillmentServiceManager;
	protected final transient Log log = LogFactory.getLog(getClass());
	@SuppressWarnings("unchecked")
	public boolean supports(Class arg0) {
		return ItemRequestForm.class.isAssignableFrom(arg0);
	}

	@SuppressWarnings("deprecation")
	public void validate(Object arg0, Errors arg1) {
		ItemRequestForm form = (ItemRequestForm) arg0;
		
		if(!(null != form.getFile() && form.getFile().length > 0)) {

			if (null == form.getDescription() || form.getDescription().equalsIgnoreCase("")) {
				arg1.rejectValue("description", "invalid",
						"Description is required");
			}
			if (form.getCreateDate().equalsIgnoreCase("")) {
				arg1.rejectValue("createDate", "invalid",
						"Effective date is required");
			} else if(!validateDate(form.getCreateDate())) {
				arg1.rejectValue("createDate", "invalid",
				"Effective date is not in valid format, Required format is : DD/MM/YYYY");
			} else {
				Date todaysDate = this.getCurrentDate();
				Date entryDate = new Date(form.getCreateDate());
				if (entryDate.compareTo(todaysDate) < 0 ) {
					arg1.rejectValue("createDate", "invalid",
					"Effective date must be greater than or equal to today's date!");
				}
			}
			if (null != form.getMerchandise() && !form.getMerchandise().equalsIgnoreCase("N")) {
				if (form.getMinimumMarkup().equals("")) {
					arg1.rejectValue("minimumMarkup", "invalid",
							"Please enter minimum markup %");
				} else  {
					Pattern p = Pattern.compile( "([0-9]*)\\.[0-9]*" );
	                Matcher m = p.matcher(form.getMinimumMarkup());
	                Pattern p1 = Pattern.compile( "([0-9]*)" );
	                Matcher m1 = p1.matcher(form.getMinimumMarkup());
	                
	                if(!(m.matches() || m1.matches())) {
	                	arg1.rejectValue("minimumMarkup", "invalid", "Minimum markup % should be a numeric value");
	                }
				}
			}
			if (null != form.getStylePopMethodId()
					&& form.getStylePopMethodId().equalsIgnoreCase("upload") && (null == form.getFilePath() || form.getFilePath().equals(""))) {
				
					 arg1.rejectValue("file", "invalid",
					 "Please enter a valid filename");
				
			}
			if (null != form.getStylePopMethodId()
					&& form.getStylePopMethodId().equalsIgnoreCase(
							"previous request")) {
				if (form.getLocationName().equals("")) {
					arg1.rejectValue("locationName", "invalid",
							"Please enter previous request id");
				} else {
	                Pattern p1 = Pattern.compile( "([0-9]*)" );
	                Matcher m1 = p1.matcher(form.getLocationName());
	                
	                if(!m1.matches()) {
	                	arg1.rejectValue("locationName", "invalid", "Previous Request ID should be a numeric value");
	                } else {
	                	boolean validRequestId = false;
	        			try {
	        				validRequestId = this.fulfillmentServiceManager.isValidRequestId(new Long(form.getLocationName()),form.getVendorNumber(),new Long(form.getServiceId()) );
	        			} catch(Exception e) {
	        				e.printStackTrace();
	        				arg1.rejectValue(LOCATION_NAME, INVALID, "Exception in validating previous request ID");
	        			}
	        			if(!validRequestId) {
	        				arg1.rejectValue(LOCATION_NAME, INVALID, "Please enter valid previous request ID");
	        			}
	                }
				}
			}
		
		}  
	}

	/**
	 * @param fulfillmentServiceManager the fulfillmentServiceManager to set
	 */
	public void setFulfillmentServiceManager(FulfillmentServiceManager fulfillmentServiceManager) {
		this.fulfillmentServiceManager = fulfillmentServiceManager;
	}

	/**
	 * @return the fulfillmentServiceManager
	 */
	public FulfillmentServiceManager getFulfillmentServiceManager() {
		return fulfillmentServiceManager;
	}

	/**
	 * This method gets the current date and formats it in MM/DD/YYYY format
	 * @return String
	 */
	private Date getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(MM_DD_YYYY);
		String currentDate = dateFormat.format(date);
		return new Date(currentDate);
	}
	
	private boolean validateDate(String strDate) {
		boolean flag=true;
        try
        {
        	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        	dateFormat.parse(strDate);
        }
        catch (ParseException e)
        {
               flag=Boolean.FALSE;
        }
		return flag;
	}
}
