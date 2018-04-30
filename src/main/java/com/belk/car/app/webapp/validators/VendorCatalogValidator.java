package com.belk.car.app.webapp.validators;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.UrlValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.webapp.forms.VendorCatalogForm;

/**
 * This class contains validations for vendor catalog set up form
 * @version 1.0 10 December 2009
 * @author afusya2
 */
public class VendorCatalogValidator implements Validator, DropShipConstants {


	/**
	 * This method supports the VendorCatalogForm
	 * 
	 * @param Class  aClass
	 * @return boolean
	 */
	@SuppressWarnings("unchecked")
	public boolean supports(Class aClass) {
		return VendorCatalogForm.class.equals(aClass);
	}

	/**
	 * This method validates the vendor catalog setup form
	 * @param Object obj
	 * @param Errors errors
	 * @return void
	 */
	public void validate(Object obj, Errors errors) {
		VendorCatalogForm vendorCatalogForm = (VendorCatalogForm) obj;

		/** Validate FTP URL */
		String[] schemes = { FTP, HTTP };
		UrlValidator urlValidator = new UrlValidator(schemes);

		if (null != vendorCatalogForm.getUploadImage()
				&& vendorCatalogForm.getUploadImage().trim()
						.equalsIgnoreCase(Y)) {
			/** validation for selecting one of option from image location drop down */
			if (null != vendorCatalogForm.getImgLocn()
					&& vendorCatalogForm.getImgLocn().trim().equalsIgnoreCase(
							SELECT_OPT)
					&& !vendorCatalogForm.getButtonClicked().equalsIgnoreCase(
							VERIFY)) {
				errors.rejectValue("imgLocn", "field.required",
						"Image Location is a required field");

			}
			/** validations for FTP url if nothing is entered */
			if (null != vendorCatalogForm.getImgLocn()
					&& vendorCatalogForm.getImgLocn().trim().equalsIgnoreCase(
							RETRIEVE_FROM_VENDOR_FTP_SITE)
					&& (null == vendorCatalogForm.getFtpUrl() || vendorCatalogForm
							.getFtpUrl().equals(""))) {
				errors.rejectValue("ftpUrl", "field.required",
						"In order to retrieve images from a vendor's FTP site, a valid FTP URL must be provided");
			}
			/** checks whether valid FTP url is entered or not */
			else if (null != vendorCatalogForm.getImgLocn()
					&& vendorCatalogForm.getImgLocn().trim().equalsIgnoreCase(
							RETRIEVE_FROM_VENDOR_FTP_SITE)
					&& !urlValidator.isValid(vendorCatalogForm.getFtpUrl())) {
				errors.rejectValue("ftpUrl", "field.required",
						"Please enter valid FTP URL");
			}
			if(!vendorCatalogForm.getIsAnonymousFTP()){
				if(StringUtils.isBlank(vendorCatalogForm.getFtpUname())
					&& vendorCatalogForm.getImgLocn().trim().equalsIgnoreCase(
								RETRIEVE_FROM_VENDOR_FTP_SITE)){
				errors.rejectValue("ftpUname", "field.required",
				"FTP User Name is a required field");
				}
				if(StringUtils.isBlank(vendorCatalogForm.getFtpPwd())
					&& vendorCatalogForm.getImgLocn().trim().equalsIgnoreCase(
								RETRIEVE_FROM_VENDOR_FTP_SITE)){
				errors.rejectValue("ftpPwd", "field.required",
				"FTP Password is a required field");	
				}
			}

			/** validations for previous catalog id if nothing is entered */
			if (null != vendorCatalogForm.getImgLocn()
					&& !vendorCatalogForm.getImgLocn().trim().equalsIgnoreCase(
							RETRIEVE_FROM_VENDOR_FTP_SITE)
					&& !vendorCatalogForm.getImgLocn().trim().equalsIgnoreCase(SELECT_OPT)
					&& !vendorCatalogForm.getImgLocn().trim().equalsIgnoreCase(UPLOAD_FROM_CD)) {
				if (StringUtils.isBlank(vendorCatalogForm.getPreviousCatalogID())) {
					errors.rejectValue("previousCatalogID", "field.required",
							"Previous Catalog ID is a required field");
				}
				 if (!StringUtils.isNumeric(vendorCatalogForm.getPreviousCatalogID())) {
					errors.rejectValue("previousCatalogID", "field.required",
							"Previous Catalog ID: must be a number.");
				}
			}
		}
		if (!vendorCatalogForm.getButtonClicked().equalsIgnoreCase(VERIFY)) {
			/** validations for file if nothing is entered */
			if (((null == vendorCatalogForm.getFileName()
					|| vendorCatalogForm.getFileName().length == 0))
					&& (null == vendorCatalogForm.getFilePath() || 
							vendorCatalogForm.getFilePath().equals(""))) {
				errors.rejectValue("fileName", "field.required",
						"The path and filename for a valid data file must be provided.");
			}
			/** validations for vendor catalog name if nothing is entered */
			if ((null == vendorCatalogForm.getCatalogName()
					|| vendorCatalogForm.getCatalogName().equals(""))
					) {
				errors.rejectValue("catalogName", "field.required",
						"Catalog Name is a required field");
			}
		}
	}
}
