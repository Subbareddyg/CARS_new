/**
 * 
 */
package com.belk.car.app.model.oma;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;
import com.belk.car.app.model.Vendor;
import com.belk.car.app.model.workflow.WorkflowStatus;

/**
 * @author 
 *
 */

@Entity
@Table(name = "vndr_item_fulfmnt_rqst")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include="all")
public class ItemRequest extends BaseAuditableModel implements java.io.Serializable   {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6782897281222750885L;
	
	private Long itemRequestID ;
	private Date effectiveDate ;
	private String desc ;
	private ItemSource itemSource ;
	private String action ;
	private Double minMarkupPct ;
	private String isAllowSalesClearancePrice ;
	private String exceptionReasons;
	private StylePopulationMethod stylePopulationMethod ;
	private String fileName ;
	private Long previousReqID ;
	private FulfillmentService fulfillmentService;
	private Vendor vendor;
	private String postedBy;
	private Date postedDate;
	private String isUserDept;
	private VFIRStatus workflowStatus;
	private Set<VFIRStyle> vfirStyles;
	private Set<VFIRStyleSku> vfirSkus;
	private Set<ItemRequestWorkflow> workFlow;
	
	/**
	 * 
	 */
	public ItemRequest() {
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VENDOR_FULF_ITM_RQST_SEQ_GEN")
	@javax.persistence.SequenceGenerator(name = "VENDOR_FULF_ITM_RQST_SEQ_GEN", sequenceName = "SEQ_VNDR_ITEM_FULFMNT_RQST_ID", allocationSize = 1)
	@Column(name = "vndr_item_fulfmnt_rqst_id", unique = true, nullable = false, precision = 12, scale = 0)
	public Long getItemRequestID() {
		return itemRequestID;
	}
	
	public void setItemRequestID(Long itemRequestID) {
		this.itemRequestID = itemRequestID;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "effective_date", nullable = false)
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	/**
	 * @param effectiveDate the effectiveDate to set
	 * 
	 */
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	
	@Column(name = "item_rqst_descr", nullable = true, length = 200)
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_source_cd", nullable = false)
	public ItemSource getItemSource() {
		return itemSource;
	}
	/**
	 * @param itemSourceID the itemSourceID to set
	 */
	public void setItemSource(ItemSource itemSource) {
		this.itemSource = itemSource;
	}
	
	@Column(name = "VIFR_WORKFLOW_ACTION_CD", nullable = false, length = 100)
	public String getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	
	@Column(name = "min_markup_pct", nullable = true, scale=12)
	public Double getMinMarkupPct() {
		return minMarkupPct;
	}
	/**
	 * @param minMarkupPct the minMarkupPct to set
	 */
	public void setMinMarkupPct(Double minMarkupPct) {
		this.minMarkupPct = minMarkupPct;
	}
	
	@Column(name = "allow_sell_on_clrnc_ind", nullable = false, length=200)
	public String getAllowSalesClearancePrice() {
		return isAllowSalesClearancePrice;
	}
	/**
	 * @param isAllowSalesClearancePrice the isAllowSalesClearancePrice to set
	 */
	public void setAllowSalesClearancePrice(String isAllowSalesClearancePrice) {
		this.isAllowSalesClearancePrice = isAllowSalesClearancePrice;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "style_population_method_cd", nullable = false)
	public StylePopulationMethod getStylePopulationMethod() {
		return stylePopulationMethod;
	}
	/**
	 * @param stylePopulationMethodID the stylePopulationMethodID to set
	 */
	public void setStylePopulationMethod(StylePopulationMethod stylePopulationMethod) {
		this.stylePopulationMethod = stylePopulationMethod;
	}
	
	@Column(name = "style_list_filename", nullable = true, length=250)
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	@Column(name = "prev_rqst_id", nullable = true, scale=12)
	public Long getPreviousReqID() {
		return previousReqID;
	}
	/**
	 * @param previousReqID the previousReqID to set
	 */
	public void setPreviousReqID(Long previousReqID) {
		this.previousReqID = previousReqID;
	}

	public void setFulfillmentService(FulfillmentService fulfillmentService) {
		this.fulfillmentService = fulfillmentService;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fulfmnt_service_id", nullable = false)
	public FulfillmentService getFulfillmentService() {
		return fulfillmentService;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false)
	public Vendor getVendor() {
		return vendor;
	}

	public void setExceptionReasons(String exceptionReasons) {
		this.exceptionReasons = exceptionReasons;
	}

	@Column(name = "exception_comment_text", nullable = true, length=2000)
	public String getExceptionReasons() {
		return exceptionReasons;
	}

	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}

	@Column(name = "posted_by", nullable = true, length=50)
	public String getPostedBy() {
		return postedBy;
	}

	
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "posted_date", nullable = true)
	public Date getPostedDate() {
		return postedDate;
	}
	
	public void setIsUserDept(String isUserDept) {
		this.isUserDept = isUserDept;
	}
	@Column(name = "applies_to_user_dept", nullable = true, length = 1)
	public String getIsUserDept() {
		return isUserDept;
	}

	@Column(name = "UPDATED_BY", nullable = false, length = 100)
	public String getUpdatedBy() {
		return this.updatedBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATED_DATE", nullable = false)
	public Date getUpdatedDate() {
		return this.updatedDate;
	}
	
	@Column(name = "CREATED_BY", nullable = false, length = 100)
	public String getCreatedBy() {
		return this.createdBy;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATED_DATE", nullable = false)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setWorkflowStatus(VFIRStatus workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VIFR_WORKFLOW_STATUS_CD", nullable = false)
	public VFIRStatus getWorkflowStatus() {
		return workflowStatus;
	}

	public void setVfirStyles(Set<VFIRStyle> vfirStyles) {
		this.vfirStyles = vfirStyles;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="compositeKeyVIFRStyle.itemRequest")
	public Set<VFIRStyle> getVfirStyles() {
		return vfirStyles;
	}

	public void setVfirSkus(Set<VFIRStyleSku> vfirSkus) {
		this.vfirSkus = vfirSkus;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="compositeKeyVFIRStylesku.itemRequest")
	public Set<VFIRStyleSku> getVfirSkus() {
		return vfirSkus;
	}

	public void setWorkFlow(Set<ItemRequestWorkflow> workFlow) {
		this.workFlow = workFlow;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy="itemRequest")
	public Set<ItemRequestWorkflow> getWorkFlow() {
		return workFlow;
	}
	
}
