/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.belk.car.app.model.vendorcatalog;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Vendor;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.Temporal;
import java.util.Date;
import javax.persistence.TemporalType;
/**
 *
 * @author AFUSY85
 */
@Entity
@Table(name = "VNDR_CATL_STYLE_MAST")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")

public class VendorCatalogStyleMaster  extends BaseAuditableModel implements java.io.Serializable{
      
	private static final long serialVersionUID = -1489400533041522623L;
		private Long id;
        private long vendorStyleId;
        private Vendor vendor;
        private VendorCatalog vendorCatalog;

        @Column(name = "VENDOR_STYLE_ID", nullable = false)
        public long getVendorStyleId() {
            return vendorStyleId;
        }

        public void setVendorStyleId(long vendorStyleId) {
            this.vendorStyleId = vendorStyleId;
        }

        @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false)
	public Vendor getVendor() {
		return vendor;
	}
        /**
	 * @param vendor the vendor to set
	 */
	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}
        @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false)
        public VendorCatalog getVendorCatalog() {
            return vendorCatalog;
        }
        public void setVendorCatalog(VendorCatalog vendorCatalog) {
            this.vendorCatalog = vendorCatalog;
        }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
     @Temporal (TemporalType.DATE)
	@Column(name = "updated_date", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}

}
