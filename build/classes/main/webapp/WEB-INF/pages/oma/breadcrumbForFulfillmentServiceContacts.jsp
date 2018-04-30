<%@ include file="/common/taglibs.jsp"%>
<div>
	<c:url value="/oma/orderManagement.html?method=load" var="formAction"/>
	<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Fulfillment Services</a>>
	
	<c:url value="/oma/fsPropertiesList.html" var="formAction">
<c:param name="mode" value="edit"/>
<c:param name="fsID" value="${sessionScope.fulfillmentService.fulfillmentServiceID}"/>
</c:url>
<a href='<c:out value="${formAction}" escapeXml="false"/>'><c:out
			value="${sessionScope.fulfillmentService.fulfillmentServiceName}"/></a>>
	
	<c:url value="/oma/fulfillmentServiceContacts.html?method=load" var="formAction"/>
	<c:choose>
		<c:when test="${requestScope.readOnly != null }">
			<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Fulfillment Service Contacts</a>>Contact
		</c:when> 
		<c:otherwise>
			<c:out value="Fulfillment Service Contacts"></c:out>
		</c:otherwise>
	</c:choose>
	
</div>