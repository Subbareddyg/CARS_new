package com.belk.car.app.webapp.validators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.model.oma.ShippingCarrierOption;
import com.belk.car.app.webapp.forms.VendorFSShippingOptionsForm;

public class VendorFulfillmentShippingOptionsValidator implements Validator {

	public boolean supports(Class arg0) {
		return VendorFSShippingOptionsForm.class.isAssignableFrom(arg0);
	}

	public void validate(Object arg0, Errors arg1) {
		VendorFSShippingOptionsForm form = (VendorFSShippingOptionsForm) arg0;

		if (StringUtils.isNotBlank(form.getDirectBelk())) {
			List<ShippingCarrierOption> shippingOptionsModelList = new ArrayList<ShippingCarrierOption>();
			shippingOptionsModelList = form.getShippingOptionsModel();
			Iterator<ShippingCarrierOption> itr = shippingOptionsModelList
					.iterator();
			ShippingCarrierOption s = null;
			int flag = 0;
			while (itr.hasNext()) {
				s = itr.next();
				if (StringUtils.isNotBlank(s.getIsAllowed())
						&& s.getIsAllowed().equals("Y")) {
					flag = 1;
					if (StringUtils.isBlank(s.getAccount())) {
						arg1.rejectValue("directBelk", "invalid",
								"Please enter the Account Number for carrier "
										+ StringUtils.upperCase(s
												.getCarrierName())
										+ "  "
										+ StringUtils.upperCase(s
												.getCarrierClass()));
					}
				}
			}
			if (flag == 0) {
				arg1
						.rejectValue("shippingOptionsModel", "invalid",
								"Please select atleast one carrier to allow Belk Direct Bill Of Shipping !!");
			}

		}

	}

}
