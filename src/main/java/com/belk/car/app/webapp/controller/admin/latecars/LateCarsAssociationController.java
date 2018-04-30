package com.belk.car.app.webapp.controller.admin.latecars;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.appfuse.model.User;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.dto.LateCarsAssociationDTO;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.LateCarsAssociation;
import com.belk.car.app.model.LateCarsGroup;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.service.CarLookupManager;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.webapp.controller.BaseFormController;
import com.belk.car.app.webapp.forms.LateCarsAssociationForm;

/**
 * @author AFUTXK2
 * This class used for assigning departments to late cars
 *
 */
public class LateCarsAssociationController extends BaseFormController {

	private CarLookupManager carLookupManager;
	private CarManager carManager;
	private Boolean addAssociation = true;

	public void setCarLookupManager(CarLookupManager carLookupManager) {
		this.carLookupManager = carLookupManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}

	public LateCarsAssociationController() {
		setCommandName("lateCarsAssociationForm");
		setCommandClass(LateCarsAssociationForm.class);
	}

	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws ServletRequestBindingException {

		if (log.isDebugEnabled()) {
			log.debug("entering LateCarsAssociationController 'referenceData'");
		}
		String lateGroupID = request.getParameter("lateCarsGroupId");
		LateCarsAssociationForm lateCarsAssociationForm = (LateCarsAssociationForm) command;
		Set<Department> filteredDepts = new HashSet<Department>();
		Map<String, Object> model = new HashMap<String, Object>();
		String isSearch = request.getParameter("firstload");
		String search = request.getParameter("search");
		String vendorName1 = request.getParameter("vendorName");
		if (null == vendorName1) {
			vendorName1 = "";
		}
		String vendorNumber1 = request.getParameter("vendorNumber");
		if (null == vendorNumber1) {
			vendorNumber1 = "";
		}
		if ("true".equalsIgnoreCase(search)) {
			lateCarsAssociationForm.setSearch(true);
		} else {
			lateCarsAssociationForm.setSearch(false);
		}
		Set<Vendor> filteredVendors = new HashSet<Vendor>();
		List<Vendor> sampleVendor = new ArrayList<Vendor>();

		if (isSearch != null) {
			sampleVendor = carLookupManager.getAllVendorsByVendorName(
					vendorName1, vendorNumber1);
			lateGroupID = lateCarsAssociationForm.getLateGroupID();
		}

		List<LateCarsAssociationDTO> lateCarsAssociatonList = carLookupManager
				.getLateCarsAssocById(new Long(lateGroupID));

		model.put("userDepartments", filteredDepts);
		model.put("departments", carLookupManager.getAllDepartments());
		model.put("lateGroupID", lateGroupID);
		model.put("userVendors", filteredVendors);
		model.put("lateCarsAssociationDetails", lateCarsAssociatonList);
		model.put("vendors", sampleVendor);
		model.put("vendorName", vendorName1);
		model.put("vendorNumber", vendorNumber1);

		return model;

	}
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering LateCarsAssociationController 'formBackingObject'");
		}

		String lateGroupID = request.getParameter("lateGroupID");
		addAssociation = true;
		LateCarsAssociationForm lateCarsAssocForm = new LateCarsAssociationForm();
		lateCarsAssocForm.setLateGroupID(lateGroupID);
		return lateCarsAssocForm;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) {

		binder.registerCustomEditor(Set.class, "vendors",
				new CustomCollectionEditor(Set.class) {
					protected Object convertElement(Object element) {
						Long vendorId = null;
						if (element instanceof Long)
							vendorId = (Long) element;
						else if (element instanceof String)
							vendorId = Long.valueOf((String) element);
						return carLookupManager.getVendorById(vendorId);
					}
				});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		if (log.isDebugEnabled()) {
			log.debug("entering LateCarsAssociationController 'onSubmit'");
		}
		LateCarsAssociationForm lateCarsAssocForm = (LateCarsAssociationForm) command;

		String deptIds = request.getParameter("seltdDeptValues");
		String vendorIds = request.getParameter("seltdVendorValues");
		String lateCarGroupIds = lateCarsAssocForm.getLateGroupID();
		List<String> selectedDeptIds = new ArrayList<String>();
		List<String> selectedVendorIds = new ArrayList<String>();
		StringTokenizer deptTokenizer = new StringTokenizer(deptIds, ",");
		while (deptTokenizer.hasMoreElements()) {
			String deptId = (String) deptTokenizer.nextElement();
			if (!deptId.isEmpty()) {
				selectedDeptIds.add(deptId);
			}
		}

		StringTokenizer vendorTokenizer = new StringTokenizer(vendorIds, ",");
		while (vendorTokenizer.hasMoreElements()) {
			String vendorId = (String) vendorTokenizer.nextElement();
			if (!vendorId.isEmpty()) {
				selectedVendorIds.add(vendorId);
			}
		}

		List<Vendor> sampleVendor = new ArrayList<Vendor>();
		String searchMethod = request.getParameter("method");
		if (searchMethod.equalsIgnoreCase("displayAll")) {
			String vendorName1 = "";
			String vendorNumber1 = "";
			sampleVendor = carLookupManager.getAllVendorsByVendorName(
					vendorName1, vendorNumber1);
		}

		List<LateCarsAssociationDTO> lateCarsAssociatonList = carLookupManager
				.getLateCarsAssocById(new Long(lateCarGroupIds));

		List lateCarsAssociatinFullList = new ArrayList();

		for (LateCarsAssociationDTO dbObject : lateCarsAssociatonList) {
			HashMap<String, List> sample = new HashMap<String, List>();
			String deptValue = dbObject.getDeptIds();
			String vendorValue = dbObject.getVendorNo();
			List deptVendorValues = new ArrayList();
			deptVendorValues.add(0, deptValue);
			deptVendorValues.add(1, vendorValue);
			sample.put(dbObject.getLateCarsAssociationID().toString(),
					deptVendorValues);
			lateCarsAssociatinFullList.add(sample);
		}
		User user = getLoggedInUser();
		Date currDate = new Date();
		if (selectedDeptIds.isEmpty()) {
			for (String vendorId : selectedVendorIds) {
				LateCarsAssociation lateCarsAssociation = new LateCarsAssociation();
				Vendor dbVendor = carLookupManager.getVendorById(Long
						.valueOf(vendorId));
				LateCarsGroup dbLateCarsGroup = carLookupManager
						.getLateCarGroupById(Long.valueOf(lateCarGroupIds));
				lateCarsAssociation.setLateCarsGroup(dbLateCarsGroup);
				lateCarsAssociation.setVendor(dbVendor);
				lateCarsAssociation.setDept(null);
				lateCarsAssociation.setCreatedDate(currDate);
				lateCarsAssociation.setUpdatedDate(currDate);
				lateCarsAssociation.setStatusCd("ACTIVE");
				lateCarsAssociation.setCreatedBy(user.getUsername());
				lateCarsAssociation.setUpdatedBy(user.getUsername());
				for (Object lateCarsObject : lateCarsAssociatinFullList) {
					HashMap<String, List> test = (HashMap<String, List>) lateCarsObject;
					Iterator it = test.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						String lateCarsid = (String) pairs.getKey();
						List deptVendorValues = (ArrayList) pairs.getValue();
						String dept = (String) deptVendorValues.get(0);
						String vendorValues = (String) deptVendorValues.get(1);
						if (lateCarsid.equalsIgnoreCase(new Long(
								dbLateCarsGroup.getLateCarsGroupId())
								.toString())
								&& dept.equals(null)
								&& vendorValues.contains(dbVendor
										.getVendorNumber())) {
							addAssociation = false;
						}
					}
				}
				try {
					if (addAssociation) {
						carManager.save(lateCarsAssociation);
					}
				} catch (AccessDeniedException ade) {
					// thrown by UserSecurityAdvice configured in aop:advisor
					// userManagerSecurity
					log.warn(ade.getMessage());
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return null;
				} catch (Exception e) {
					log.warn(e.getMessage());
				}
			}
		}
		if (selectedVendorIds.isEmpty()) {
			for (String deptId : selectedDeptIds) {
				LateCarsAssociation lateCarsAssociation = new LateCarsAssociation();
				Department department = carLookupManager.getDepartmentById(Long
						.valueOf(deptId));
				LateCarsGroup dbLateCarsGroup = carLookupManager
						.getLateCarGroupById(Long.valueOf(lateCarGroupIds));
				lateCarsAssociation.setLateCarsGroup(dbLateCarsGroup);
				lateCarsAssociation.setVendor(null);
				lateCarsAssociation.setDept(department);
				lateCarsAssociation.setCreatedDate(currDate);
				lateCarsAssociation.setUpdatedDate(currDate);
				lateCarsAssociation.setStatusCd("ACTIVE");
				lateCarsAssociation.setCreatedBy(user.getUsername());
				lateCarsAssociation.setUpdatedBy(user.getUsername());

				for (Object lateCarsObject : lateCarsAssociatinFullList) {
					HashMap<String, List> test = (HashMap<String, List>) lateCarsObject;
					Iterator it = test.entrySet().iterator();
					while (it.hasNext()) {
						Map.Entry pairs = (Map.Entry) it.next();
						String lateCarsid = (String) pairs.getKey();
						List deptVendorValues = (ArrayList) pairs.getValue();
						String dept = (String) deptVendorValues.get(0);
						String vendorValues = (String) deptVendorValues.get(1);
						if (lateCarsid.equalsIgnoreCase(new Long(
								dbLateCarsGroup.getLateCarsGroupId())
								.toString())
								&& new Long(department.getDeptId()).toString()
										.equalsIgnoreCase(dept)
								&& vendorValues.equals(null)) {
							addAssociation = false;
						}
					}
				}
				try {
					if (addAssociation) {
						carManager.save(lateCarsAssociation);
					}
				} catch (AccessDeniedException ade) {
					// thrown by UserSecurityAdvice configured in aop:advisor
					// userManagerSecurity
					log.error(ade.getMessage());
					response.sendError(HttpServletResponse.SC_FORBIDDEN);
					return null;
				} catch (Exception e) {
					log.error(e.getMessage());
				}
			}
		}
		if ((selectedDeptIds.size() > 0) && (selectedVendorIds.size() > 0)) {
			for (String deptId : selectedDeptIds) {
				for (String vendorId : selectedVendorIds) {
					LateCarsAssociation lateCarsAssociation = new LateCarsAssociation();
					Department department = carLookupManager
							.getDepartmentById(Long.valueOf(deptId));
					Vendor dbVendor = carLookupManager.getVendorById(Long
							.valueOf(vendorId));
					LateCarsGroup dbLateCarsGroup = carLookupManager
							.getLateCarGroupById(Long.valueOf(lateCarGroupIds));

					lateCarsAssociation.setLateCarsGroup(dbLateCarsGroup);
					lateCarsAssociation.setVendor(dbVendor);
					lateCarsAssociation.setDept(department);
					lateCarsAssociation.setCreatedDate(currDate);
					lateCarsAssociation.setUpdatedDate(currDate);
					lateCarsAssociation.setStatusCd("ACTIVE");
					lateCarsAssociation.setCreatedBy(user.getUsername());
					lateCarsAssociation.setUpdatedBy(user.getUsername());

					for (Object lateCarsObject : lateCarsAssociatinFullList) {
						HashMap<String, List> test = (HashMap<String, List>) lateCarsObject;
						Iterator it = test.entrySet().iterator();
						while (it.hasNext()) {
							Map.Entry pairs = (Map.Entry) it.next();
							String lateCarsid = (String) pairs.getKey();
							List deptVendorValues = (ArrayList) pairs
									.getValue();
							String dept = (String) deptVendorValues.get(0);
							String vendorValues = (String) deptVendorValues
									.get(1);
							if (lateCarsid.equalsIgnoreCase(new Long(
									dbLateCarsGroup.getLateCarsGroupId())
									.toString())
									&& new Long(department.getDeptId())
											.toString().equalsIgnoreCase(dept)
									&& vendorValues.contains(dbVendor
											.getVendorNumber())) {
								addAssociation = false;
							}
						}
					}
					try {
						if (addAssociation) {
							carManager.save(lateCarsAssociation);
						}
					} catch (AccessDeniedException ade) {
						// thrown by UserSecurityAdvice configured in
						// aop:advisor
						// userManagerSecurity
						log.error(ade.getMessage());
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
						return null;
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}
		}
		Map model = new HashMap();
		Set<Department> filteredDepts = new HashSet<Department>();
		Set<Vendor> filteredVendors = new HashSet<Vendor>();
		lateCarsAssociatonList = carLookupManager
				.getLateCarsAssocById(new Long(lateCarGroupIds));
		model.put("userDepartments", filteredDepts);
		model.put("departments", carLookupManager.getAllDepartments());
		model.put("lateCarsAssociationDetails", lateCarsAssociatonList);
		model.put("userVendors", filteredVendors);
		model.put("lateGroupID", lateCarGroupIds);
		model.put("vendors", sampleVendor);
		request.setAttribute(super.getCommandName(), lateCarsAssocForm);
		return new ModelAndView("/admin/latecarsgroup/lateCarsAssociation",
				model);
	}
}
