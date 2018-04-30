
package com.belk.car.app.webapp.controller.oma;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.DropShipConstants;
import com.belk.car.app.model.oma.Address;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.AddressForm;

/**
 * @author afusy07-priyanka_gadia@syntelinc.com
 * @Date 12-dec-09
 */
public class AddVendorAddressFormController extends BaseFormController
		implements
			DropShipConstants {

	/**
	 * Setting the command class
	 */
	public AddVendorAddressFormController() {
		setCommandName("addressForm");
		setCommandClass(AddressForm.class);
	}

	private transient final Log log = LogFactory
			.getLog(AddVendorAddressFormController.class);

	/*
	 * (non-Javadoc)
	 * @see
	 * org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.validation.BindException)
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView onSubmit(
			HttpServletRequest request, HttpServletResponse response,
			Object command, BindException errors)
			throws Exception {
		Address addr = new Address();
		HttpSession session = request.getSession();
		Random r = new Random();
		/**
		 * Creating the Address object by taking parameters from request
		 */
		if (!request.getParameterMap().isEmpty()) {
			if (log.isDebugEnabled()){
				log.debug(".......Getting parameters frm Request");
			}
			addr.setLocName(ServletRequestUtils.getStringParameter(request,
					LOCATION));
			addr.setAddr1(ServletRequestUtils.getStringParameter(request,
					ADDRESS_LINE1));
			addr.setAddr2(ServletRequestUtils.getStringParameter(request,
					ADDRESS_LINE2));
			addr.setCity(ServletRequestUtils.getStringParameter(request, CITY));
			addr.setState(ServletRequestUtils
					.getStringParameter(request, STATE));
			addr.setZip(ServletRequestUtils.getStringParameter(request, ZIP));
			setAuditInfo(request, addr);

		}
		if (log.isDebugEnabled()){
			log.debug("..........Address from request=-" + addr.toString());
		}
		ArrayList<Address> addrList = (ArrayList<Address>) session
				.getAttribute(ADDR_LIST_FROM_SESSION);
		if (log.isDebugEnabled()){
			log.debug("..........Checking conditions-");
			log.debug("1- null!=addrList" + (null != addrList));
		}
		/**
		 * If address list already exists- 1-Add New Address ID as a negative
		 * random Number 2-Add New Address to the array of address already
		 * present in session Else 1-Create a New Address List 2-Assign a new
		 * random -negative id for new address 3-Add the address list in session
		 */

		// Generating random number
		long l = r.nextLong();
		if (l >= 0){
			l = -l;
		}
		if (null != addrList && !addrList.isEmpty() ) {
			log.debug("..........Address exists in Session-addrList.size()="
					+ addrList.size());
			log.debug("......Got the new address ID=" + l);
			addr.setAddressID(l);
			log.debug("addr=" + addr.toString());
			addrList.add(addr);
			log.debug("addrList=" + addrList.size());
			session.setAttribute(ADDR_LIST_FROM_SESSION, addrList);
			log.debug("Added in session");

		}
		else {
			log
					.debug("..........ArrayList of ADDRESS  does not exist in session");
			ArrayList<Address> addrList1 = new ArrayList<Address>();
			addr.setAddressID(l);
			addrList1.add(addr);
			log.debug("..........Adding address list in session="
					+ addrList1.toString());
			session.setAttribute(ADDR_LIST_FROM_SESSION, addrList1);
			log.debug("Added in session");

		}
		session.setAttribute("addedAddress", "yes");

		/**
		 * Creating the JSON object to be returned to ajax
		 */
		Map<String, Boolean> model2 = new HashMap<String, Boolean>();
		model2.put(SUCCESS, true);
		JSONObject json = new JSONObject(model2);
		request.setAttribute(JSON_OBJ, json);
		return new ModelAndView(AJAX_RETURN);
	}
}
