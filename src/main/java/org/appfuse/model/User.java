package org.appfuse.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.CarUserNote;
import com.belk.car.app.model.Department;
import com.belk.car.app.model.Status;
import com.belk.car.app.model.UserType;
import com.belk.car.app.model.Vendor;

@Entity
@Table(name = "CAR_USER",uniqueConstraints = @UniqueConstraint(columnNames = "LOGIN_NAME"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class User extends BaseAuditableModel implements java.io.Serializable,
		UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7534433312478853161L;
	private Long id;
	private UserType userType;
	private String userTypeCd;
	private String username;
	private String firstName;
	private String password;
	private String confirmPassword;
	private String lastName;
	private String altPhone;
	private String altPhoneAreaCode;
	private String altPhoneNumber1;
	private String altPhoneNumber2;
	private String locale;
	private String isLocked;
	private String phone;
	private String phoneAreaCode;
	private String phoneNumber1;
	private String phoneNumber2;
	private String addr1;
	private String addr2;
	private String city;
	private String stateCd;
	private String zipcode;
	private String administrator;
	private String statusCd;
	private Date lastLoginDate;
	private String createdBy;
	private String updatedBy;
	private Date createdDate;
	private Date updatedDate;
	private Integer version = new Integer(0);
	private boolean accountExpired;
	private boolean accountLocked;
	private boolean credentialsExpired;
	private String[] selectedDepartments;
	private Set<Vendor> vendors = new HashSet<Vendor>(0);
	//private Set<CarHistory> carHistoriesForLoggedBy = new HashSet<CarHistory>(0);
	//private Set<CarHistory> carHistoriesForAssignedTo = new HashSet<CarHistory>(
	//		0);
	//private Set<Car> carsForLoggedByUserId = new HashSet<Car>(0);
	//private Set<CarUserClass> carUserClasses = new HashSet<CarUserClass>(0);
	//private Set<Car> carsForAssignedToUserId = new HashSet<Car>(0);
	private Set<Department> departments = new HashSet<Department>(0);
	private Set<CarUserNote> carUserNotes = new HashSet<CarUserNote>(0);
	private Set<Role> roles = new HashSet<Role>(0);
	private String emailAddress;
	private String primary="N";

	public User() {
	}

	public User(final String username) {
		this.username = username;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CAR_USER_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "CAR_USER_SEQ_GEN", sequenceName = "CAR_USER_SEQ", allocationSize = 1)
	@Column(name = "CAR_USER_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_TYPE_CD", nullable = false)
	public UserType getUserType() {
		return this.userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
		if (userType != null) {
			this.userTypeCd = userType.getUserTypeCd();
		}
	}

	@Column(name = "LOGIN_NAME", unique = true, nullable = false, length = 50)
	public String getUsername() {
		return username;
	}

	public void setUsername(String loginName) {
		this.username = StringUtils.trim(loginName);
	}

	@Column(name = "FIRST_NAME", nullable = false, length = 50)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "PASSWORD", nullable = false, updatable = true)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "LAST_NAME", length = 100)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Transient
	public String getFullName() {

		return (firstName != null ? firstName : "") + ' '
				+ (lastName != null ? lastName : "");
	}

	@Column(name = "ALT_PHONE", length = 20)
	public String getAltPhone() {
		return this.altPhone;
	}

	public void setAltPhone(String altPhone) {
		this.altPhone = altPhone;
		breakDownPhone(altPhone, true);
	}

	@Transient
	public String getConfirmPassword() {
		return confirmPassword;
	}

	@Column(name = "LOCALE", length = 20)
	public String getLocale() {
		return this.locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Column(name = "IS_LOCKED", nullable = false, length = 1)
	public String getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(String isLocked) {
		this.isLocked = isLocked;
	}

	@Column(name = "PHONE", length = 20)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
		breakDownPhone(phone, false);
	}

	@Column(name = "ADDR1", length = 100)
	public String getAddr1() {
		return this.addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	@Column(name = "ADDR2", length = 100)
	public String getAddr2() {
		return this.addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Column(name = "CITY", length = 100)
	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "STATE_CD", length = 20)
	public String getStateCd() {
		return this.stateCd;
	}

	public void setStateCd(String stateCd) {
		this.stateCd = stateCd;
	}

	@Column(name = "ZIPCODE", length = 20)
	public String getZipcode() {
		return this.zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	@Column(name = "IS_ADMINISTRATOR", nullable = false, length = 1)
	public String getAdministrator() {
		return administrator;
	}

	public void setAdministrator(String administrator) {
		this.administrator = administrator;
	}
	
	@Column(name = "IS_PRIMARY", nullable = false, length = 1)
	public String getPrimary() {
		return primary;
	}
	public void setPrimary(String primary) {
		this.primary = primary;
	}
	
	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_LOGIN_DATE", nullable = false)
	public Date getLastLoginDate() {
		return this.lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "CAR_USER_VENDOR", joinColumns = { @JoinColumn(name = "CAR_USER_ID") }, inverseJoinColumns = @JoinColumn(name = "VENDOR_ID"))
	public Set<Vendor> getVendors() {
		return this.vendors;
	}

	public void setVendors(Set<Vendor> vendors) {
		this.vendors = vendors;
	}

//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carUserByLoggedBy")
//	public Set<CarHistory> getCarHistoriesForLoggedBy() {
//		return this.carHistoriesForLoggedBy;
//	}
//
//	public void setCarHistoriesForLoggedBy(
//			Set<CarHistory> carHistoriesForLoggedBy) {
//		this.carHistoriesForLoggedBy = carHistoriesForLoggedBy;
//	}
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carUserByAssignedTo")
//	public Set<CarHistory> getCarHistoriesForAssignedTo() {
//		return this.carHistoriesForAssignedTo;
//	}
//
//	public void setCarHistoriesForAssignedTo(
//			Set<CarHistory> carHistoriesForAssignedTo) {
//		this.carHistoriesForAssignedTo = carHistoriesForAssignedTo;
//	}
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "carUserByLoggedByUserId")
//	public Set<Car> getCarsForLoggedByUserId() {
//		return this.carsForLoggedByUserId;
//	}
//
//	public void setCarsForLoggedByUserId(Set<Car> carsForLoggedByUserId) {
//		this.carsForLoggedByUserId = carsForLoggedByUserId;
//	}
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carUser")
//	public Set<CarUserClass> getCarUserClasses() {
//		return this.carUserClasses;
//	}
//
//	public void setCarUserClasses(Set<CarUserClass> carUserClasses) {
//		this.carUserClasses = carUserClasses;
//	}
//
//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carUserByAssignedToUserId")
//	public Set<Car> getCarsForAssignedToUserId() {
//		return this.carsForAssignedToUserId;
//	}
//
//	public void setCarsForAssignedToUserId(Set<Car> carsForAssignedToUserId) {
//		this.carsForAssignedToUserId = carsForAssignedToUserId;
//	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "carUser")
	public Set<CarUserNote> getCarUserNotes() {
		return this.carUserNotes;
	}

	public void setCarUserNotes(Set<CarUserNote> carUserNotes) {
		this.carUserNotes = carUserNotes;
	}

	// @Column(name="account_enabled")
	@Transient
	public boolean isEnabled() {
		return Status.ACTIVE.equals(this.getStatusCd());
	}

	// @Column(name="account_enabled")
	@Transient
	public void setEnabled(boolean value) {
		if (value) {
			this.setStatusCd(Status.ACTIVE);
		} else {
			this.setStatusCd(Status.INACTIVE);
		}
	}

	/**
	 * Adds a role for the user
	 * 
	 * @param role
	 *            the fully instantiated role
	 */
	public void addRole(Role role) {
		this.roles.add(role);
	}

	/**
	 * Adds a department for the user
	 * 
	 * @param role
	 *            the fully instantiated role
	 */
	public void addDepartment(Department dept) {
		this.departments.add(dept);
	}

	/**
	 * Associates a vendor with the user
	 * 
	 * @param vendor
	 *            the fully instantiated vendor
	 */
	public void addVendor(Vendor vendor) {
		this.vendors.add(vendor);
	}

	/**
	 * Adds a note to the user
	 * 
	 * @param note
	 *            the fully instantiated note
	 */
	public void addNote(CarUserNote note) {
		this.carUserNotes.add(note);
	}

	/**
	 * @see org.acegisecurity.userdetails.UserDetails#getAuthorities()
	 * @return GrantedAuthority[] an array of roles.
	 */
	@Transient
	public GrantedAuthority[] getAuthorities() {
		return roles.toArray(new GrantedAuthority[0]);
	}

	@Transient
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@Transient
	public boolean isAccountNonExpired() {
		return true;
	}

	@Transient
	public boolean isAccountNonLocked() {
		return true;
	}

	@Transient
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Transient
	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}

	@Transient
	public boolean isAccountExpired() {
		return accountExpired;
	}

	@Transient
	public boolean isAccountLocked() {
		return accountLocked;
	}

	public void setAccountExpired(boolean accountExpired) {
		this.accountExpired = accountExpired;
	}

	public void setAccountLocked(boolean accountLocked) {
		this.accountLocked = accountLocked;
	}

	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}

	@Transient
	public String getUserTypeCd() {
		return userTypeCd;
	}

	public void setUserTypeCd(String userTypeCd) {
		this.userTypeCd = userTypeCd;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "CAR_USER_ROLE", joinColumns = { @JoinColumn(name = "CAR_USER_ID") }, inverseJoinColumns = @JoinColumn(name = "ROLE_CD"))
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
	@JoinTable(name = "CAR_USER_DEPARTMENT", joinColumns = { @JoinColumn(name = "CAR_USER_ID") }, inverseJoinColumns = @JoinColumn(name = "DEPT_ID"))
	public Set<Department> getDepartments() {
		return this.departments;
	}

	public void setDepartments(Set<Department> carUserDepartments) {
		this.departments = carUserDepartments;
	}

	/**
	 * Convert user roles to LabelValue objects for convenience.
	 * 
	 * @return a list of LabelValue objects with role information
	 */
	@Transient
	public List<LabelValue> getRoleList() {
		List<LabelValue> userRoles = new ArrayList<LabelValue>();
		if (this.roles != null) {
			for (Role role : roles) {
				// convert the user's roles to LabelValue Objects
				userRoles.add(new LabelValue(role.getName(), role.getName()));
			}
		}
		return userRoles;
	}

	@Transient
	private List<String> getRoleKeys() {
		List<String> roleKeys = new ArrayList<String>();
		if (roles != null && roles.size() > 0) {
			for (Role role : roles) {
				if (role != null) {
					roleKeys.add(role.getId());
				}
			}
		}
		return roleKeys;
	}

	@Transient
	public boolean getAdmin() {
		if (getRoleKeys() != null && !getRoleKeys().isEmpty()) {
			if (getRoleKeys().contains(Role.ROLE_ADMIN) || getRoleKeys().contains(Role.ROLE_SUPER_ADMIN)) {
				return true;
			}
		}
		return false;
	}
	
	@Transient
	public boolean isSuperAdmin() {
		if (getRoleKeys() != null && !getRoleKeys().isEmpty()) {
			if (getRoleKeys().contains(Role.ROLE_SUPER_ADMIN)) {
				return true;
			}
		}
		return false;
	}

	@Transient
	public boolean getVendorRights() {
		return getRoleKeys().contains(Role.ROLE_VENDOR);
	}
	
	@Transient
	public boolean getBuyerRights() {
		return getRoleKeys().contains(Role.ROLE_BUYER);
	}
	
	
	@Transient
	public String[] getSelectedDepartments() {
		return selectedDepartments;
	}

	public void setSelectedDepartments(String[] selectedDepartments) {
		this.selectedDepartments = selectedDepartments;
	}

	@Transient
	public String getPhoneAreaCode() {
		return phoneAreaCode;
	}

	public void setPhoneAreaCode(String phoneAreaCode) {
		this.phoneAreaCode = phoneAreaCode;
	}

	@Transient
	public String getPhoneNumber1() {
		return phoneNumber1;
	}

	public void setPhoneNumber1(String phoneNumber1) {
		this.phoneNumber1 = phoneNumber1;
	}

	@Transient
	public String getPhoneNumber2() {
		return phoneNumber2;
	}

	public void setPhoneNumber2(String phoneNumber2) {
		this.phoneNumber2 = phoneNumber2;
	}

	@Transient
	public String getAltPhoneAreaCode() {
		return altPhoneAreaCode;
	}

	public void setAltPhoneAreaCode(String altPhoneAreaCode) {
		this.altPhoneAreaCode = altPhoneAreaCode;
	}

	@Transient
	public String getAltPhoneNumber1() {
		return altPhoneNumber1;
	}

	public void setAltPhoneNumber1(String altPhoneNumber1) {
		this.altPhoneNumber1 = altPhoneNumber1;
	}

	@Transient
	public String getAltPhoneNumber2() {
		return altPhoneNumber2;
	}

	public void setAltPhoneNumber2(String altPhoneNumber2) {
		this.altPhoneNumber2 = altPhoneNumber2;
	}
	
	@Transient
	public String getFormattedPhoneNumber() {
		if (StringUtils.isNotBlank(this.phone)) {
			return (new StringBuffer().append("(").append(phoneAreaCode).append(") ").append(phoneNumber1).append("-").append(phoneNumber2)).toString();
		}
		return "" ;
	}

	@Transient
	public String getFormattedAltPhoneNumber() {
		if (StringUtils.isNotBlank(this.altPhone)) {
			return (new StringBuffer().append("(").append(altPhoneAreaCode).append(") ").append(altPhoneNumber1).append("-").append(altPhoneNumber2)).toString();
		}
		return "" ;
	}
	/**
	 * Convenient method to break down the phone into several fields
	 * 
	 * @param phone2
	 * @param isAltPhone
	 *            determines which phone number to break
	 */
	private void breakDownPhone(String phone2, boolean isAltPhone) {
		if (StringUtils.isNotBlank(phone2)) {
			if (phone2.length() >= 10 && !isAltPhone) {
				setPhoneAreaCode(phone2.substring(0, 3));
				setPhoneNumber1(phone2.substring(3, 6));
				setPhoneNumber2(phone2.substring(6, 10));
			} else if (phone2.length() >= 10) {
				setAltPhoneAreaCode(phone2.substring(0, 3));
				setAltPhoneNumber1(phone2.substring(3, 6));
				setAltPhoneNumber2(phone2.substring(6, 10));
			}
		}
	}

	@Column(name = "EMAIL_ADDR", unique = true, nullable = false, length = 50)
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = StringUtils.trim(emailAddress);
	}

	@Transient
	public boolean isBuyer() {
		return UserType.BUYER.equals(this.getUserType().getUserTypeCd()) ;
	}

	@Transient
	public boolean isSampleCoordinator() {
		return UserType.SAMPLE_COORDINATOR.equals(this.getUserType().getUserTypeCd()) ;
	}

	@Transient
	public boolean isContentManager() {
		return UserType.CONTENT_MANAGER.equals(this.getUserType().getUserTypeCd()) ;
	}

	@Transient
	public boolean isContentWriter() {
		return UserType.CONTENT_WRITER.equals(this.getUserType().getUserTypeCd()) ;
	}

	@Transient
	public boolean isVendor() {
		return UserType.VENDOR.equals(this.getUserType().getUserTypeCd()) ;
	}

	@Transient
	public boolean isArtDirector() {
		return UserType.ART_DIRECTOR.equals(this.getUserType().getUserTypeCd()) ;
	}
	
	//==================== New Method Added by Syntel For Dropship ===============//
	/**
	 * 
	 * @return boolean
	 * This method will return true if User has been assigned to 
	 * Order Management Role
	 */
	@Transient
	public boolean isOrderMgmtUser() {
		return getRoleKeys().contains(Role.ROLE_ORDERMGMT);
	}
	/**
	 * 
	 * @return boolean
	 * This method will return true if User has been assigned to 
	 * Order Management Administrator Role
	 */
	@Transient
	public boolean isOrderMgmtAdmin() {
		return getRoleKeys().contains(Role.ROLE_ORDERMGMT_ADMIN);
	}
	
	/**
	 * 
	 * @return boolean
	 * This method will return true if User has been assigned to 
	 * Order Management Administrator Role
	 */
	@Transient
	public boolean isDataGovernanceAdmin() {
		return getRoleKeys().contains(Role.ROLE_SIZE_COLOR);
	}
	
	
}
