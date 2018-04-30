
<div  id="tabArea">
	<c:if test="${mode == 'add'}">
		<c:url value="/oma/addeditItemRequest.html?mode=add" var="formAction"/>
		<a id="tab1" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Request Properties</a>
	</c:if>
	<c:if test="${mode == 'edit'}">
		<c:url value="/oma/addeditItemRequest.html?mode=edit" var="formAction"/>
		<a id="tab1" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Request Properties</a>
	</c:if>
	<c:choose>
	<c:when test="${not empty itemRequestForm.requestId}">
	<c:url value="/oma/itemrequeststyles.html" var="formAction"/>
	<a id="tab2" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Request Styles</a>
	
	<c:if test="${itemRequestForm.enableDisableSkuException == false}">
	<c:url value="/oma/requestskuexception.html" var="formAction"/>
	<a  id="tab3" class="tab" href='<c:out value="${formAction}" escapeXml="false"/>'>SKU Exceptions</a>
	</c:if>
	<c:if test="${itemRequestForm.enableDisableSkuException == null || itemRequestForm.enableDisableSkuException == true}">
	<a  id="tab3" class="tab" href="#" disabled="true">SKU Exceptions</a>
	</c:if>
	
	<c:url value="/oma/requestUpdateHistory.html" var="formAction"/>
	<a  id="tab4" class="tab" href="<c:out value="${formAction}" escapeXml="false"/>" class="tab">Update History</a>
	</c:when>
	<c:otherwise>
	<a id="tab2" class="tab" style="margin-top:0;" href="#" disabled="true">Request Styles</a>
	<a  id="tab3" class="tab" href="#" disabled="true">SKU Exceptions</a>
	<a  id="tab4" class="tab" href="#" class="tab" disabled="true">Update History</a>
	</c:otherwise>
	</c:choose>
</div>
<div style="border-bottom: 1px solid #000000;padding:2px"> </div>