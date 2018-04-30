/**
 * @author afusy07-priyanka_gadia@syntelinc.com
 * @Date 12-dec-09
 *
 */

package com.belk.car.app.model.oma;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.belk.car.app.model.BaseAuditableModel;

@Entity
@Table(name = "FULFMNT_SERV_VNDR_LOC")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class FulfillmentServiceVendorLocation extends BaseAuditableModel
		implements
			java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Address addr;
	private FulfillmentService fullfillServ;
	private CompoundKeyVndrShip compoundId;

	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "addrId", column = @Column(name = "LOCATION_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "fulfillmentServId", column = @Column(name = "FULFMNT_SERVICE_ID", nullable = false, precision = 12, scale = 0)),
			@AttributeOverride(name = "vendorId", column = @Column(name = "VENDOR_ID", nullable = false, precision = 12, scale = 0))})
	public CompoundKeyVndrShip getCompoundId() {
		return compoundId;
	}

	public void setCompoundId(CompoundKeyVndrShip compoundId) {
		this.compoundId = compoundId;
	}

	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "LOCATION_ID", nullable = false, insertable = false, updatable = false)
	public Address getAddr() {
		return addr;
	}

	public void setAddr(Address addr) {
		this.addr = addr;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FULFMNT_SERVICE_ID", nullable = false, insertable = false, updatable = false)
	public FulfillmentService getFullfillServ() {
		return fullfillServ;
	}

	public void setFullfillServ(FulfillmentService fullfillServ) {
		this.fullfillServ = fullfillServ;
	}

}
