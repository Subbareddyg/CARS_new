<%@ include file="/common/taglibs.jsp"%>
<c:url value="/admin/latecarsgroup/addLateCarsAssociation.html"	var="formAction">
	<c:param name="lateCarsGroupId" value="${lateGroupID}" />
</c:url>
<form:form commandName="lateCarsAssociationForm" method="post"
	action="${formAction}" id="lateCarsAssociationForm">
	<div id="user_vendors_pnl" class="cars_panel x-hidden">
		<div class="x-panel-header">Vendors</div>
		<div class="x-panel-body">
			<input type="hidden" name="method" value="associateVendor"
				id="selectedMethod" /> <input type="hidden" name="firstload"
				value="0" /> <input type="hidden" name="search" value="false" /> <input
				type="hidden" name="displayAll" value="false" /> <input
				type="hidden" name="seltdDeptValues" value="0" id="deptValues" /> <input
				type="hidden" name="seltdVendorValues" value="0" id="VendorValues" />
			<input type="hidden" name="lateGroupID"
				value="<c:out value="${lateGroupID}" />" />

			<div
				style="padding: 5px 0 5px 20px; background: #f0f0f0; clear: both; margin-top: 5px;"
				style="width:97%">
				<div>
					<strong> <fmt:message key="vendor.search.name" /> <input
						type="text" id="vendorName" name="vendorName"
						value="<c:out value="${vendorName}" />" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <fmt:message
							key="vendor.search.number" /> <input type="text"
						id="vendorNumber" name="vendorNumber"
						value="<c:out value="${vendorNumber}" />" />
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <input
						type="submit" class="btn" name="search" id="search"
						style="position: absolute; left: 550px"
						value="<fmt:message key="vendor.button.search"/>" />&nbsp;&nbsp;
						<input type="submit" class="btn" name="displayAll" id="displayAll"
						style="position: absolute; left: 625px"
						value="<fmt:message key="vendor.button.diaplayall"/>" />

					</strong>
				</div>
			</div>

			<br>

			<div id="searchValidateEr" class="error_message"
				style="display: none;">
				<b> Please enter Vendor Name or Vendor MIR # to search </b>
			</div>

			<c:choose>
				<c:when
					test="${ fn:length(vendors) == 0 && (fn:length(vendorName)!=0 || fn:length(vendorNumber)!=0 ) }"><div id="lateCarVendorNoResults"> No results found.</div></c:when>
				<c:otherwise>
					<c:if test="${not empty vendors}">
						<br style="clear: both;" />
						<div id="filterd" class="filter"
							style="padding: 5px 0 5px 20px; background: #f0f0f0; clear: both; margin-top: 5px;">
							<strong>Filter:</strong> <input type="text"
								id="txt_vendor_filter" /> <span id="filter_vendorResults"></span>
						</div>
						<div id="vendorList">
							<ul class="vendors_for_add">
								<app:extendedcheckboxes path="vendors" items="${vendors}"
									useritems="${userVendors}" itemValue="vendorIdAsString"
									itemLabel="displayName" element="li" sortBy="vendorNumber" />
							</ul>
						</div>
					</c:if>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<br>
	<input class="btn" id="saveMyData" style="display: none;" type="submit"
		value="Add Association Hidden" />
</form:form>
