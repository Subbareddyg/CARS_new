package com.belk.car.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name = "VENDOR", uniqueConstraints = @UniqueConstraint(columnNames = "VENDOR_NUMBER"))
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class Vendor extends BaseAuditableModel implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5783847104399809532L;
	private long vendorId;
	private String vendorNumber;
	private String name;
	private String descr;
	private String contactName;
	private String phone;
	private String contactPhone;
	private String contactEmailAddr;
	private String addr1;
	private String addr2;
	private String city;
	private String stateCd;
	private String zipcode;
	private String statusCd;
        private String isDisplayable;

    //private Set <User> users = new HashSet<User>(0);
	//private Set<VendorStyle> vendorStyles = new HashSet<VendorStyle>(0);

	public Vendor() {
	}

	/*
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	@JoinTable(name = "CAR_USER_VENDOR", joinColumns = { @JoinColumn(name = "VENDOR_ID") }, inverseJoinColumns = @JoinColumn(name = "CAR_USER_ID"))
	public Set<User> getUsers() {
		return this.users;
	}
	
	public void setUsers(Set <User> users){
		this.users=users;
	}
	*/
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDOR_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "VENDOR_SEQ_GEN", sequenceName = "VENDOR_SEQ", allocationSize = 1)
	@Column(name = "VENDOR_ID", unique = true, nullable = false, precision = 12, scale = 0)
	public long getVendorId() {
		return this.vendorId;
	}

	public void setVendorId(long vendorId) {
		this.vendorId = vendorId;
	}

	@Column(name = "VENDOR_NUMBER", unique = true, length = 20)
	public String getVendorNumber() {
		return this.vendorNumber;
	}

	public void setVendorNumber(String vendorNumber) {
		this.vendorNumber = StringUtils.trim(vendorNumber);
	}

	@Column(name = "NAME", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCR", nullable = false, length = 200)
	public String getDescr() {
		return this.descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	@Column(name = "CONTACT_NAME", length = 100)
	public String getContactName() {
		return this.contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	@Column(name = "PHONE", length = 20)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name = "CONTACT_PHONE", length = 20)
	public String getContactPhone() {
		return this.contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	@Column(name = "CONTACT_EMAIL_ADDR", nullable = false)
	public String getContactEmailAddr() {
		return this.contactEmailAddr;
	}

	public void setContactEmailAddr(String contactEmailAddr) {
		this.contactEmailAddr = contactEmailAddr;
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

	@Column(name = "STATUS_CD", nullable = false, length = 20)
	public String getStatusCd() {
		return this.statusCd;
	}

	public void setStatusCd(String statusCd) {
		this.statusCd = statusCd;
	}

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
        @Column(name = "IS_DISPLAYABLE", nullable = false, length = 20)
        public String getIsDisplayable() {
            return isDisplayable;
        }

        public void setIsDisplayable(String isDisplayable) {
            if(null==isDisplayable) {
                isDisplayable="Y";
            }
            this.isDisplayable = isDisplayable;
        }



//	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "vendor")
//	public Set<VendorStyle> getVendorStyles() {
//		return this.vendorStyles;
//	}
//
//	public void setVendorStyles(Set<VendorStyle> vendorStyles) {
//		this.vendorStyles = vendorStyles;
//	}

	@Transient
	public String getVendorIdAsString() {
		return String.valueOf(vendorId);
	}

	@Transient
	public String getDisplayName() {
		return vendorNumber + " - " + name ;
	}

	
	/*@Transient
	public int getVendorSkuSize(){
		Set<VendorStyle> vendorStyles=this.vendorStyles;
		Iterator<VendorStyle> itr = vendorStyles.iterator();
		VendorStyle vndrStyle=null;
		int skus=0;
		while(itr.hasNext()){
			vndrStyle = itr.next();
			skus+=vndrStyle.getVendorSkus().size();
		}
		return skus;
	}*/
	


}
