<%@ include file="/common/taglibs.jsp"%>
<div  id="tabArea">
										
	<c:set var="venfsid" value="${sessionScope.vndrFulfillmentService.vndrFulfillmentServId}"/>
		
		<c:choose>
	<c:when test="${sessionScope.param == 'param'}">
		<c:url value="/oma/AddVendor.html?venfsid=${venfsid}&mode=edit&param=param" var="formAction"/>
		<a id="tab100" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendor Properties</a>
	
	</c:when>
	<c:otherwise>
		<c:url value="/oma/AddVendor.html?venfsid=${venfsid}&mode=edit" var="formAction"/>
		<a id="tab100" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendor Properties</a>
	
	</c:otherwise>
	</c:choose> 
	
		<c:url value="/oma/fulfillmentVendorReturns.html" var="formAction"/>
	<a id="tab10" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Return</a>
	<c:url value="/oma/shippingOptions.html" var="formAction"/>
	<a id="tab6" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Shipping Options</a>
	
	<c:url value="/oma/vendorShippingFee.html?method=load" var="formAction"/>
	<a  id="tab7" class="tab" href='<c:out value="${formAction}" escapeXml="false"/>' class="tab">Fees</a>	
	<c:url value="/oma/fulfillmentServiceContacts.html?method=load" var="formAction"/>
	<a  id="tab4" class="tab" href='<c:out value="${formAction}" escapeXml="false"/>' class="tab">Contacts</a>
	<c:url value="/oma/fulfillmentServiceNotes.html" var="formAction">
	<c:param name="method" value="loadVendorNote"/>
	<c:param name="mode" value='<%=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"" %>'/>
	</c:url>
	<a id="tab5" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Notes</a>
	
	<c:url value="/oma/styleskus.html" var="formAction"/>
	<a  id="tab9" class="tab" href='<c:out value="${formAction}" escapeXml="false"/>' class="tab">Styles & SKU's</a>
	
	<c:url value="/oma/requestHistory.html" var="formAction"/>
	<a  id="tab8" class="tab" href='<c:out value="${formAction}" escapeXml="false"/>' class="tab">Request History</a>
															
</div>
<div style="border-bottom: 1px solid #000000;padding:2px"> </div>		