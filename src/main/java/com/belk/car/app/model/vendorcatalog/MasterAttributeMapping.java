
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.belk.car.app.model.BaseAuditableModel;

/**
 * @author afusy07-Priyanka Gadia Model class for MASTER_ATTRIBUTE_MAPPING table
 */
@Entity
@Table(name = "MASTER_ATTRIBUTE_MAPPING")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, include = "all")
public class MasterAttributeMapping extends BaseAuditableModel implements java.io.Serializable {

	private static final long serialVersionUID = 4064999399543281977L;
	private Long catalogMasterMappingId;
	private Long catalogTemplateId;
	private Long catalogMasterAttrId;
	private String catalogHeaderFieldName;
	private String status;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_MASTER_ATTRUBUTE_MAPPING")
	@javax.persistence.SequenceGenerator(name = "SEQ_MASTER_ATTRUBUTE_MAPPING", sequenceName = "SEQ_MASTER_ATTRUBUTE_MAPPING", allocationSize = 1)
	@Column(name = "CATALOG_MASTER_MAPPING_ID", precision = 12, scale = 0)
	public Long getCatalogMasterMappingId() {
		return catalogMasterMappingId;
	}

	public void setCatalogMasterMappingId(Long catalogMasterMappingId) {
		this.catalogMasterMappingId = catalogMasterMappingId;
	}

	@Column(name = "CATALOG_TEMPLATE_ID", precision = 12, scale = 0)
	public Long getCatalogTemplateId() {
		return catalogTemplateId;
	}

	public void setCatalogTemplateId(Long catalogTemplateId) {
		this.catalogTemplateId = catalogTemplateId;
	}

	@Column(name = "CATALOG_MASTER_ATTR_ID", precision = 12, scale = 0)
	public Long getCatalogMasterAttrId() {
		return catalogMasterAttrId;
	}

	public void setCatalogMasterAttrId(Long catalogMasterAttrId) {
		this.catalogMasterAttrId = catalogMasterAttrId;
	}

	@Column(name = "STATUS_CD", length = 20)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name = "VNDR_CATL_HDR_FLD_NAME", nullable = true, length = 100)
	public String getCatalogHeaderFieldName() {
		return catalogHeaderFieldName;
	}

	public void setCatalogHeaderFieldName(String catalogHeaderFieldName) {
		this.catalogHeaderFieldName = catalogHeaderFieldName;
	}
	
	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("catalogMasterMappingId", this.catalogMasterMappingId)
                .append("catalogTemplateId", this.catalogTemplateId)
                .append("catalogMasterAttrId", this.catalogMasterAttrId)
                .append("catalogHeaderFieldName", this.catalogHeaderFieldName)
                .append("status", this.status).toString();
    }

}
