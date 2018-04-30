/**
 * @author afusy07-Priyanka Gadia
 * Composite Key For Vendor Catalog Records
 *
 */
package com.belk.car.app.model.vendorcatalog;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class CompositeKeyForVndrCatRecord implements java.io.Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6208229754268968285L;
	private Long catalogId;
	private Long headerNum;
	private Long recordNumber;
	
	
	public CompositeKeyForVndrCatRecord(Long catalogId, Long headerNum,
			Long recordNumber) {
		super();
		this.catalogId = catalogId;
		this.headerNum = headerNum;
		this.recordNumber = recordNumber;
	}
	public CompositeKeyForVndrCatRecord(){
		super();
	}
	@Column(name = "VENDOR_CATALOG_ID", nullable = false, precision = 12, scale = 0)
	public Long getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}
	
	@Column(name = "VNDR_CATL_HDR_FLD_NUM", nullable = false, precision = 12, scale = 0)
	public Long getHeaderNum() {
		return headerNum;
	}
	public void setHeaderNum(Long headerNum) {
		this.headerNum = headerNum;
	}
	
	@Column(name = "VNDR_CATL_RECORD_NUM", nullable = false, precision = 12, scale = 0)
	public Long getRecordNumber() {
		return recordNumber;
	}
	public void setRecordNumber(Long recordNumber) {
		this.recordNumber = recordNumber;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((catalogId == null) ? 0 : catalogId.hashCode());
		result = prime * result
				+ ((headerNum == null) ? 0 : headerNum.hashCode());
		result = prime * result
				+ ((recordNumber == null) ? 0 : recordNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		CompositeKeyForVndrCatRecord other = (CompositeKeyForVndrCatRecord) obj;
		if (catalogId == null) {
			if (other.catalogId != null){
				return false;
			}
		}
		else if (!catalogId.equals(other.catalogId)){
			return false;
		}
		if (headerNum == null) {
			if (other.headerNum != null){
				return false;
			}
		}
		else if (!headerNum.equals(other.headerNum)){
			return false;
		}
		if (recordNumber == null) {
			if (other.recordNumber != null){
				return false;
			}
		}
		else if (!recordNumber.equals(other.recordNumber)){
			return false;
		}
		return true;
	}
	

	

}
