package com.belk.car.app.webapp.controller.admin.latecars;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.belk.car.app.dto.LateCarsAssociationDTO;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;

public class LateCarsAssociationManagerController extends AbstractController {
	private CarManager carManager = null;
	private CarLookupManager carLookupManager;

	private String successView;

	public String getSuccessView() {
		return successView;
	}

	public void setSuccessView(String successView) {
		this.successView = successView;
	}

	/**
	 * Spring Injected manager
	 * 
	 * @param carLookupManager
	 */
	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	/**
	 * Spring Injected manager
	 * 
	 * @param carManager
	 */
	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	@Override
	public ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long lateCarsGroupId = ServletRequestUtils.getLongParameter(request,
				"lateCarsGroupId");
		Long lateCarsDeptId = ServletRequestUtils.getLongParameter(request,
				"lateCarsDeptId");
		String method = request.getParameter("method");

		if ("remove".equals(method)) {

			if (lateCarsDeptId.equals(new Long(0))) {
				lateCarsDeptId = null;
			}
			int lateCarsAssociationList = carLookupManager
					.getLateCarsAssocByDeptId(lateCarsGroupId, lateCarsDeptId);
			List<LateCarsAssociationDTO> lateCarsAssociatonList = carLookupManager
					.getLateCarsAssocById(lateCarsGroupId);

			ModelAndView modelnView = new ModelAndView(getSuccessView());
			modelnView.addObject("lateCarsAssociationDetails",
					lateCarsAssociatonList);

			return modelnView;
		} else {
			String methods = request.getParameter("method");
			return new ModelAndView(
					"redirect:/admin/latecarsgroup/lateCarsGroupDetails.html?method=detail&lateCarsGroupId="
							+ lateCarsGroupId);

		}
	}

}