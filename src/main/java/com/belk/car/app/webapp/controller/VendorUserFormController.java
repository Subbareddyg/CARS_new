/**
 * 
 */
package com.belk.car.app.webapp.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.AccessDeniedException;
import org.appfuse.model.Role;
import org.appfuse.model.User;
import org.appfuse.service.UserExistsException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

import com.belk.car.app.exceptions.SendEmailException;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.NotificationType;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.service.CarManager;
import com.belk.car.app.webapp.controller.admin.user.UserFormController;
import com.belk.car.util.PasswordGenerator;

public class VendorUserFormController extends UserFormController {

	private static final String DETAILCAR = "detailCar";
	private static final String VENDORCONTACTS = "vendorContacts";
	
	private CarManager carManager;

	public User user = new User();

	public ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		String carIdString = request.getParameter("car");

		if (log.isDebugEnabled())
			log.debug("entering userForm 'onSubmit' method...");
		
		if (request.getParameter("cancel") != null) {
			return new ModelAndView("redirect:editCarForm.html?carId="+carIdString);
		}
		
		//User logggedInuser = this.getLoggedInUser();

		User user = (User) command;
		if (user.getId() != null) {
			User cu = (User) carManager.getFromId(User.class, user.getId());
			if (cu != null) {
				
				String phoneNumber = user.getPhoneAreaCode() + user.getPhoneNumber1()
						+ user.getPhoneNumber2();
				String altPhoneNumber = user.getAltPhoneAreaCode()
						+ user.getAltPhoneNumber1() + user.getAltPhoneNumber2();
				cu.setPhone(phoneNumber);
				cu.setAltPhone(altPhoneNumber);

				cu.setAddr1(user.getAddr1());
				cu.setAddr2(user.getAddr2());
				cu.setCity(user.getCity());

				//Ensure that we trim any trailing of leading space in the Email and resulting Username
				//user.setEmailAddress(user.getEmailAddress());

				//update username
				if (!cu.getEmailAddress().equals(user.getUsername())) {
					cu.setUsername(user.getEmailAddress());
				}
				
				cu.setEmailAddress(user.getEmailAddress());
				cu.setFirstName(user.getFirstName());
				cu.setLastName(user.getLastName());
				cu.setStateCd(user.getStateCd());
				cu.setZipcode(user.getZipcode());
				this.setAuditInfo(request, cu) ;

				carManager.saveUser(cu);
				
				// put required attributes for vendorManagement.jsp in the request
				Car car = this.getCarManager().getCarFromId(new Long(carIdString));
				request.setAttribute(VENDORCONTACTS, 
						this.getCarManager().getUsersForVendorAndDept(car.getVendorStyle().getVendor().getVendorId(),
                        car.getDepartment().getDeptId()));
				request.setAttribute(DETAILCAR, car);
				
				return new ModelAndView(getSuccessView());
			}
		}

		String phoneNumber = user.getPhoneAreaCode() + user.getPhoneNumber1() + user.getPhoneNumber2();
		String altPhoneNumber = user.getAltPhoneAreaCode() + user.getAltPhoneNumber1() + user.getAltPhoneNumber2();

		user.setAccountExpired(false);		
		user.setAccountLocked(false);
		user.setUserType(getCarLookupManager().getUserType(UserType.VENDOR));
		user.setLastLoginDate(new Date());
		user.setAdministrator(User.FLAG_NO);
		user.setIsLocked(User.FLAG_NO);
		user.setEnabled(true);
		user.setPhone(phoneNumber);
		user.setAltPhone(altPhoneNumber);
		user.setUsername(user.getEmailAddress());
		
		this.setAuditInfo(request, user);
		
		Integer originalVersion = user.getVersion();
		if (user.getId() != null) {
			this.getCarManager().saveUser(user);
			return showForm(request, response, errors);
		}
		
		Department dept = null;		
		Vendor vendor = null;
		try {
			//Long deptId = new Long(deptString);
			//dept = (Department) carManager.getFromId(Department.class, deptId);
			Car car = (Car) carManager.getFromId(Car.class, Long.valueOf(carIdString));
			if (car != null) {
				dept = car.getDepartment();
				vendor = car.getVendorStyle().getVendor();
			}
		} catch (Exception e) {
			log.info("couldn't get the department");
		}

		
		//Add user to Department
		if (dept != null) {
			//Set<Department> depts = new HashSet<Department>();
			//depts.add(dept);
			//user.setDepartments(depts);
			user.addDepartment(dept);
		}
		
		//Add Vendor Contact To Vendor
		if (vendor != null) {
			user.addVendor(vendor);
		}

		//Add User To Role
		user.addRole(this.getCarLookupManager().getRole(Role.ROLE_USER));

		//Set the Password
		String generatedPassword = PasswordGenerator.getPassword();
		user.setPassword(generatedPassword);

		Map model = new HashMap();
		model.put("newPassword", generatedPassword);

		try {

			getUserManager().saveUser(user);
			//user.getVendors().add(vendor);
			//getUserManager().saveUser(user);

		} catch (AccessDeniedException ade) {
			// thrown by UserSecurityAdvice configured in aop:advisor
			// userManagerSecurity
			log.warn(ade.getMessage());
			response.sendError(HttpServletResponse.SC_FORBIDDEN);
			return null;
		} catch (UserExistsException e) {
			errors.rejectValue("username", "errors.existing.user",
					new Object[] { user.getUsername() }, "duplicate user");

			// redisplay the unencrypted passwords
			user.setPassword(user.getConfirmPassword());
			// reset the version # to what was passed in
			user.setVersion(originalVersion);
			//Reset the Id so that the user Id doesn't get re-populated
			user.setId(null);
			return showForm(request, response, errors);
			// return showForm(request, response, errors);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Send Email Notification to USER
		NotificationType type = getCarLookupManager().getNotificationType(
				NotificationType.NEW_REGISTRATION);
		try {
			this.getEmailManager().sendEmail(type, user, model);
		} catch (SendEmailException see) {
			// add failed email warning message
			saveError(request, see.getMessage());
		}
		
		// put required attributes for vendorManagement.jsp in the request
		Car car = this.getCarManager().getCarFromId(new Long(carIdString));
		request.setAttribute(VENDORCONTACTS, 
				this.getCarManager().getUsersForVendorAndDept(car.getVendorStyle().getVendor().getVendorId(),
                car.getDepartment().getDeptId()));
		request.setAttribute(DETAILCAR, car);
		
		return new ModelAndView(getSuccessView());
	}

	public CarManager getCarManager() {
		return carManager;
	}

	public void setCarManager(CarManager carManager) {
		this.carManager = carManager;
	}
}
