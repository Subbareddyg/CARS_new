package com.belk.car.app.util;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.belk.car.app.Constants;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.ProductType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.util.DateUtils;

public class JSONUtil {
	private static final String LOCK = "Lock";
	private static final String UNLOCK = "Unlock";
	private static transient final Log log = LogFactory.getLog(JSONUtil.class);

	public static JSONObject convertProductTypesToJSON(Collection<ProductType> col) {

		JSONObject json = new JSONObject();
		try {
			json.put("product_type_fields", createProductTypeFields());
			for (ProductType pt : col) {
				json.accumulate("product_type_data", 
						createProductTypeData(pt.getName(), pt.getDescription(), pt.getProductTypeId()));
			}
		} catch (JSONException jex) {
		}
		return json;
	}

	public static JSONObject convertCarsToJSON(List<Car> cars, User user, CarLookupManager lookupMgr) {
		JSONObject json = new JSONObject();
		if (cars == null) {
			return json ;
		}

		if (log.isDebugEnabled()) {
			log.debug("number of cars found: " + cars.size());
		}

		try { 
			long startTime = System.currentTimeMillis();

			json.put("car_user", user.getUsername());
			json.put("car_user_type", user.getUserType().getUserTypeCd());
			json.put("car_fields", createCarFields());
			for (Car car : cars) {
				json.accumulate("car_data", createCarData(car, user));
				//should be able to display all Cars
				/*if (car.getStatusCd().equals(Status.ACTIVE)) {
					json.accumulate("car_data", createCarData(car, user));
				}*/
			}
			if (log.isDebugEnabled())
				log.debug("*********************End Process car time: " + (System.currentTimeMillis() - startTime) + " ms");
		} catch (JSONException jex) {
			if (log.isDebugEnabled()) {
				log.debug("JSONException while processing cars: " + jex);
			}
		}
		return json;
	}

	public static JSONArray createCarFields() throws JSONException {
		JSONArray carFields = new JSONArray();
		carFields.put(createCarType("flag", "string"));
		carFields.put(createCarType("car_id", "int"));
		carFields.put(createCarType("source", "string"));
		carFields.put(createCarType("source_type", "string"));
		carFields.put(createCarType("dept_num", "string"));
		carFields.put(createCarType("vendor", "string"));
		carFields.put(createCarType("vendor_style", "string"));
		carFields.put(createCarType("source_type_cd", "string"));
		carFields.put(createCarType("status", "string"));
		carFields.put(createCarType("due_date", "date"));
		carFields.put(createCarType("completion_date", "date"));
		carFields.put(createCarType("last_updated_by", "string"));
		carFields.put(createCarType("last_updated_date", "date"));
		carFields.put(createCarType("shipping_date", "date"));
		carFields.put(createCarType("link", "string"));
		carFields.put(createCarType("can_do_urgent", "string"));
		carFields.put(createCarType("lock_unlock", "string"));
		carFields.put(createCarType("owned_by", "string"));
		carFields.put(createCarType("assigned_to_user_type", "string"));
		carFields.put(createCarType("vendor_style_type", "string"));
		carFields.put(createCarType("content_status", "string"));
		return carFields;
	}

	public static JSONArray createProductTypeFields() throws JSONException {
		JSONArray carFields = new JSONArray();
		carFields.put(createCarType("name", "string"));
		carFields.put(createCarType("desc", "string"));
		carFields.put(createCarType("product_type_id", "int"));
		return carFields;
	}

	public static JSONObject createCarType(String name, String type)
			throws JSONException {
		JSONObject dataType = new JSONObject();
		dataType.put("name", name);
		dataType.put("type", type);
		return dataType;
	}

	private static JSONArray createCarData(Car car, User user) throws JSONException {
		JSONArray data = new JSONArray();

		//long startTime = System.currentTimeMillis();

		String urgentStr = car.getIsUrgent().equals(Constants.FLAG_YES) || Calendar.getInstance().getTime().after(car.getDueDate()) ? "urg" : "";
		String prodTypeStr = (car.getVendorStyle().getProductType() == null) ? "set" : "edit";
		User userA=car.getCarUserByAssignedToUserId();
		String userId=(userA!=null)?userA.getUsername():"";

		String canDoUrgent = user.getUserType().getUserTypeCd().equals(
				UserType.BUYER) || user.getUserType().getUserTypeCd().equals(
						UserType.BUYER) ? Constants.FLAG_YES
				: Constants.FLAG_NO;

		data.put(urgentStr);
		data.put(String.valueOf(car.getCarId()));
		data.put(car.getSourceId());
		data.put(car.getSourceType().getName());
		data.put(car.getDepartment().getDeptCd());
		data.put(car.getVendorStyle().getVendor().getName());
		data.put(car.getVendorStyle().getVendorStyleName());
		data.put(car.getSourceType().getSourceTypeCd());
		data.put(car.getCurrentWorkFlowStatus().getName());
		data.put(DateUtils.formatDate(car.getDueDate()));
		data.put(DateUtils.formatDate(car.getExpectedShipDate()));
		data.put(car.getUpdatedBy());
		data.put(DateUtils.formatDate(car.getUpdatedDate()));
		data.put(DateUtils.formatDate(car.getExpectedShipDate()));
		data.put(prodTypeStr);
		data.put(canDoUrgent);
		data.put((userA == null) ? LOCK	: UNLOCK);
		data.put(userId);
		data.put(car.getAssignedToUserType().getName());
		data.put(car.getVendorStyle().getVendorStyleType().getCode());
		data.put(car.getContentStatus().getCode());
		return data;

	}

	public static JSONArray createProductTypeData(String name, String desc,
			Long productTypeId) throws JSONException {
		JSONArray data = new JSONArray();
		data.put(name);
		data.put(desc);
		data.put(productTypeId);
		return data;

	}

}
