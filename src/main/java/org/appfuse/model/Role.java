package org.appfuse.model;

/**
 * DO NOT TOUCH/MODIFY THIS FILE'S PACKAGE NAME OR CLASS NAME
 */
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.acegisecurity.GrantedAuthority;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * This class is used to represent available roles in the database.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a> Version by
 *         Dan Kibler dan@getrolling.com Extended to implement Acegi
 *         GrantedAuthority interface by David Carter david@carter.net
 */
@Entity
@Table(name = "ROLE", uniqueConstraints = @UniqueConstraint(columnNames = "ROLE_CD"))
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, include="all")
public class Role extends BaseObject implements Serializable, GrantedAuthority {
	private static final long serialVersionUID = 3690197650654049848L;
        public static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN";
	public static final String ROLE_ADMIN = "ROLE_ADMIN";
	public static final String ROLE_USER = "ROLE_USER";
	public static final String ROLE_BUYER = "ROLE_BUYER";
	public static final String ROLE_CONTENT = "ROLE_CONTENT";
	public static final String ROLE_VENDOR = "ROLE_VENDOR";
	public static final String ROLE_SIZE_COLOR = "ROLE_SIZE_COLOR";
	
	//==================== New Role Added by Syntel For Dropship ===============//
	public static final String ROLE_ORDERMGMT_ADMIN = "ROLE_ORDERMGMT_ADMIN";
	public static final String ROLE_ORDERMGMT = "ROLE_ORDERMGMT";
	//==================== End Of Changes =====================================//
	private static Set<String> administerUserRoles = new HashSet<String>();
	static {
		administerUserRoles.addAll(Arrays.asList(new String[] {Role.ROLE_SUPER_ADMIN, Role.ROLE_ADMIN, Role.ROLE_BUYER }));
	}

	private String id;
	private String name;
	private String description;
	private String statusCode;
	private String createdBy;
	private String updatedBy;
	private Date createdDate;
	private Date updatedDate;

	// private Set<User> carUsers = new HashSet<User>(0);
	// private Set<CarRole> carRoles = new HashSet<CarRole>(0);

	/**
	 * Default constructor - creates a new instance with no values set.
	 */
	public Role() {
	}

	/**
	 * Create a new instance and set the name.
	 * 
	 * @param name
	 *            name of the role.
	 */
	public Role(final String id) {
		this.id = id;
	}

	@Id
	@Column(name = "ROLE_CD", unique = true, nullable = false, length = 20)
	public String getId() {
		return id;
	}

	/**
	 * @see org.acegisecurity.GrantedAuthority#getAuthority()
	 * @return the name property (getAuthority required by Acegi's
	 *         GrantedAuthority interface)
	 */
	@Transient
	public String getAuthority() {
		return getId();
	}

	@Column(name = "NAME", unique = false, nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescription() {
		return this.description;
	}

	@Column(name = "STATUS_CD", nullable = false, length = 1)
	public String getStatusCode() {
		return statusCode;
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

	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "role")
	// public Set<User> getCarUsers() {
	// return carUsers;
	// }

	// @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy =
	// "role")
	// public Set<CarRole> getCarRoles() {
	// return carRoles;
	// }
	public static boolean canAdministerUser(String roleCode) {
		return administerUserRoles.contains(roleCode);
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	// public void setCarUsers(Set<User> carUsers) {
	// this.carUsers = carUsers;
	// }

	// public void setCarRoles(Set<CarRole> carRoles) {
	// this.carRoles = carRoles;
	// }

	/**
	 * {@inheritDoc}
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Role)) {
			return false;
		}

		final Role role = (Role) o;

		return !(name != null ? !name.equals(role.name) : role.name != null);

	}

	/**
	 * {@inheritDoc}
	 */
	public int hashCode() {
		return (name != null ? name.hashCode() : 0);
	}

	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE).append(this.name).toString();
	}

}
