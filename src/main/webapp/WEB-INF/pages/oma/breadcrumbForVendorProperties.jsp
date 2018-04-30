<%@ include file="/common/taglibs.jsp"%>
<div id="attr_container" style="margin:15px 0px 15px;">
	
	
	<c:url value="/oma/orderManagement.html?method=load" var="formAction"/>
	<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Fulfillment Services</a>>
	
	      <c:url value="/oma/fsPropertiesList.html" var="formAction">
<c:param name="mode" value="mode"/>
<c:param name="fsID" value="${sessionScope.fulfillmentService.fulfillmentServiceID}"/>
</c:url>
<a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out
                  value="${sessionScope.fulfillmentService.fulfillmentServiceName}"/></a>>

	<c:url value="/oma/fulfillmentServiceVendors.html?method=viewAll" var="formAction"/>
	<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendors</a>
	<c:if test="${sessionScope.vndrFulfillmentService.vendorID != null}">
		>
	</c:if>
	<c:set var="venfsid" value="${sessionScope.vndrFulfillmentService.vndrFulfillmentServId}"/>
	<span ><c:out value="${sessionScope.vndrFulfillmentService.venName}"/></span>
	<br>
<br>
	<span style="font-weight:bold">Vendor Properties</span>
</div>
