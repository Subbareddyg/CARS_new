<%@ include file="/common/taglibs.jsp"%>
<div id="attr_container" style="margin:15px 0px 15px;font-size:small;">
	<c:url value="/oma/orderManagement.html?method=load" var="formAction"/>
	<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Fulfillment Services</a> >
	
	<c:url value="/oma/fsPropertiesList.html" var="formAction">
<c:param name="mode" value="mode"/>
<c:param name="fsID" value="${sessionScope.fulfillmentService.fulfillmentServiceID}"/>
</c:url>
<a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out
			value="${sessionScope.fulfillmentService.fulfillmentServiceName}"/></a>>
	
	<c:url value="/oma/fulfillmentServiceVendors.html?method=viewAll" var="formAction"/>
	<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendors</a>>
	
	<c:set var="venfsid" value="${sessionScope.vndrFulfillmentService.vndrFulfillmentServId}"/>
	
	
	<c:choose>
	<c:when test="${sessionScope.param == 'param'}">
		<c:url value="/oma/AddVendor.html?venfsid=${venfsid}&mode=edit&param=param" var="formAction"/>
		<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all"><c:out value="${sessionScope.vndrFulfillmentService.venName}"/></a>>
	
	</c:when>
	<c:otherwise>
		<c:url value="/oma/AddVendor.html?venfsid=${venfsid}&mode=edit" var="formAction"/>
		<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all"><c:out value="${sessionScope.vndrFulfillmentService.venName}"/></a>>
	
	</c:otherwise>
	</c:choose>
	<c:url value="/oma/styleskus.html" var="formAction"/>
	<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all"><c:out value="Styles & SKU's"/></a>>
	
	<c:url value="/oma/addeditItemRequest.html?mode=edit&RequestId=${itemRequestForm.requestId}" var="formAction"/>
	<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all"><c:out value="Request ${itemRequestForm.requestId}"/></a>>
	
	<c:out value="Update History"></c:out>
</div>