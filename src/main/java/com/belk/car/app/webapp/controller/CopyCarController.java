/**
 * 
 */
package com.belk.car.app.webapp.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.appfuse.model.User;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.belk.car.app.model.workflow.WorkflowStatus;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.CarAttribute;
import com.belk.car.app.model.CarSearchCriteria;
import com.belk.car.app.model.ColorMappingMaster;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.WorkflowTransitionInfo;
import com.belk.car.app.model.workflow.WorkflowTransition;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.service.SuperColorManager;
import com.belk.car.app.service.WorkflowManager;

/**
 * @author antoniog
 * 
 */
public class CopyCarController extends BaseController {

	private final Log log = LogFactory.getLog(CopyCarController.class);
	private CarManager carManager;
	private MessageSource messageSource = null;
	private WorkflowManager workflowManager;
	private static final String PARAMCARID = "carId";
// EC - 1550
	public WorkflowManager getWorkflowManager() {
		return workflowManager;
	}

	public void setWorkflowManager(WorkflowManager workflowManager) {
		this.workflowManager = workflowManager;
	}

	public CarManager getCarManager() {
		return carManager;
	}

	//  EC - 1550
	/**
	 * @param carManager
	 *            the carManager to set
	 */
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	private List<ColorMappingMaster> superColorList;

	// Added for CARS Faceted Navigation Begin
	public List<ColorMappingMaster> getSuperColorList() {
		return superColorList;
	}

	public void setSuperColorList(List<ColorMappingMaster> superColorList) {
		this.superColorList = superColorList;
	}

	private SuperColorManager superColorManager;

	public SuperColorManager getSuperColorManager() {
		return superColorManager;
	}

	public void setSuperColorManager(SuperColorManager superColorManager) {
		this.superColorManager = superColorManager;
	}

//	public User user1 = new User();
	// EC - 1550(copy car Functionality)
	private static final String DETAILCAR = "detailCar";
	private static final String VENDORCONTACTS = "vendorContacts";
  //  EC - 1550
	
	// Added for CARS Faceted Navigation End
	@SuppressWarnings("unchecked")
	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map model = new HashMap();
		MessageSourceAccessor text = new MessageSourceAccessor(messageSource,
				request.getLocale());
		Long copyFromCarId = ServletRequestUtils.getLongParameter(request,
				"copyFromCarId");
		Long copyToCarId = ServletRequestUtils.getLongParameter(request,
				"copyToCarId");
		User user = this.getLoggedInUser();
		request.setAttribute("user", user);
		if (copyFromCarId == null || copyToCarId == null) {
			model.put("carId", request.getAttribute("carId"));
			log.warn("---> copyCar() failed.");
			request.setAttribute("error", "Error obtained while copying car.");
			return new ModelAndView(new RedirectView(getErrorView()), model);
		}
		// Get original car source
		Car carFrom = this.carManager.getCarFromId(new Long(copyFromCarId));
		// Get copyTo source
		Car carTo = this.carManager.getCarFromId(new Long(copyToCarId));
		// Getting data from session
		HttpSession session = request.getSession();
		String scrollPos = (String) session.getAttribute("scrollPos");
		log.debug("Scroll Position from session :" + scrollPos);
		// Added for CARS Faceted Navigation Begin
		Map<String, String> superColors = new HashMap<String, String>();
		superColorList = getSuperColorManager().getAllSuperColorMapping();
		for (ColorMappingMaster map : superColorList) {
			superColors.put(map.getSuperColorCode(), map.getSuperColorName());
		}
		carTo.setSuperColors(superColors);
		for (VendorSku sku : carTo.getVendorSkus()) {
			String superColor1 = getColorCodeSuperColor(sku.getColorCode());
			// to set superColor1 synchronization....
			if (log.isDebugEnabled()) {
				log.debug("setting superColor1: color code:"
						+ sku.getColorCode() + "  and super color name:"
						+ superColor1);
			}
			sku.setFirstSuperColor(superColor1);
		}

		// Added for CARS Faceted Navigation End
		if (carFrom != null && carTo != null) {

			// EC - 1550(copy car Functionality)

			
			WorkflowStatus currentStatus = carTo.getCurrentWorkFlowStatus();

			UserType currentUserType = carTo.getAssignedToUserType();
			
			WorkflowTransitionInfo wti = workflowManager.getNextWorkFlowTransitionInformation(currentUserType.getUserTypeCd(),currentStatus.getStatusCd());
			request.setAttribute("workflowTransition", wti);

			Car car = this.getCarManager().getCarFromId(copyToCarId);
			request.setAttribute(
					VENDORCONTACTS,
					this.getCarManager().getUsersForVendorAndDept(
							car.getVendorStyle().getVendor().getVendorId(),
							car.getDepartment().getDeptId()));
 
			if (CopyCarsIsSuccessful(request, carTo, carFrom)) {
				model.put("detailCar", carTo);
				model.put("Copied", true);
				model.put("scrollPos", scrollPos);
			} else {
				model.put("carId", carTo.getCarId());
				log.warn("---> copyCar() failed.");
				model.put("Copied", false);
				request.setAttribute("error",
						"Error obtained while copying car.");
				return new ModelAndView(new RedirectView(getErrorView()), model);
			}

		}

		return new ModelAndView(this.getSuccessView(), model);
	}

	private boolean CopyCarsIsSuccessful(HttpServletRequest request, Car carTo,
			Car carFrom) throws Exception {
		String transitionId = request.getParameter("transtionId");
		WorkflowTransition wt = null;
		boolean successCopy = false;
		// Check that the classification number is the same for both
		// if (carTo.getVendorStyle().getClassification().getClassId() ==
		// carFrom.getVendorStyle().getClassification().getClassId()) {
		if (carFrom != null) {
			Set<CarAttribute> carFromAttributes = carFrom.getCarAttributes();
			// Copy to new Car
			for (CarAttribute carAttrFrom : carFrom.getCarAttributes()) {
				// if the car target contains the attribute then iterate and
				// update the value
				for (CarAttribute carAttrTo : carTo.getCarAttributes()) {
					// Check that the attributes are the same for target and
					// source that way we can update the value
					if (carAttrTo.getAttribute().getAttributeId() == carAttrFrom
							.getAttribute().getAttributeId()) {
						carAttrTo.setAttrValue(carAttrFrom.getAttrValue());
					}

				}
				/**
				 * Removed the check of classification match to copy the vendor
				 * style name and description.
				 */
				if (carFrom.getVendorStyle() != null
						&& carTo.getVendorStyle() != null) {
					if (StringUtils.isNotBlank(carFrom.getVendorStyle()
							.getVendorStyleName())) {
						carTo.getVendorStyle().setVendorStyleName(
								carFrom.getVendorStyle().getVendorStyleName());
					}

					if (StringUtils.isNotBlank(carFrom.getVendorStyle()
							.getDescr())) {
						carTo.getVendorStyle().setDescr(
								carFrom.getVendorStyle().getDescr());
					}

				}

			}
			// Persist the new copied values
			carManager.save(carTo);
			return true;
		}
		return false;
	}

	private User setLoggedInUser() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getColorCodeSuperColor(String colorCode) {
		int color = Integer.parseInt(colorCode);
		int beginColor;
		int endColor;
		String superColor = null;
		for (ColorMappingMaster mapping : superColorList) {
			beginColor = Integer.parseInt(mapping.getColorCodeBegin());
			endColor = Integer.parseInt(mapping.getColorCodeEnd());
			if (color >= beginColor && color <= endColor) {
				superColor = mapping.getSuperColorName();
			}
		}
		return superColor;
	}

}
