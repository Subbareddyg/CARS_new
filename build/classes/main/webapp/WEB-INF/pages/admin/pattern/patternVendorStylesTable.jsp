<%@ include file="/common/taglibs.jsp"%>

<fmt:message key="pattern.details.vendor.styles" /> &nbsp;   	
<display:table name="patternProducts" cellspacing="0"
	cellpadding="0" requestURI="" defaultsort="1" id="vendorStyle"
	pagesize="25" class="table">
	<display:column property="vendorNumber" title="Vendor Number"
		style="width: 15%" />
	<display:column property="vendorStyleNumber" style="width: 25%"  title="Style Number"/>
	<display:column property="vendorStyleName"  title="Style Name"/>
	<display:column>
		<secureurl:secureAnchor cssStyle="remove"
			name="RemovePatternVendorStyle" elementName="RemovePatternVendorStyle"
			title="Remove" localized="true" hideUnaccessibleLinks="true"
			arguments="${pattern.vendorStyleId},${vendorStyle.vendorStyleId},remove" />
	</display:column>
	<display:setProperty name="paging.banner.item_name"
		value="Vendor Style" />
	<display:setProperty name="paging.banner.items_name"
		value="Vendor Styles" />
</display:table>