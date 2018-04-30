<%@ include file="/common/taglibs.jsp"%>
<div  id="tabArea">
										
	<c:set var="venfsid" value="${sessionScope.vndrFulfillmentService.vndrFulfillmentServId}"/>
		
		<c:choose>
	<c:when test="${sessionScope.param == 'param'}">
		<c:url value="/oma/AddVendor.html?venfsid=${venfsid}&mode=edit&param=param" var="formAction"/>
		<a onclick="return false" id="tab100" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendor Properties</a>
	
	</c:when>
	<c:otherwise>
		<c:url value="/oma/AddVendor.html?venfsid=${venfsid}&mode=edit" var="formAction"/>
		<a onclick="return false" id="tab100" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendor Properties</a>
	
	</c:otherwise>
	</c:choose>
	
		<c:url value="/oma/fulfillmentVendorReturns.html" var="formAction"/>
	<a  onclick="return false"   disabled=true id="tab10" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Return</a>
	<c:url value="/oma/shippingOptions.html" var="formAction"/>
	<a onclick="return false" id="tab6"  disabled=true class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Shipping Options</a>
	
	<c:url value="/oma/vendorShippingFee.html?method=load" var="formAction"/>
	<a onclick="return false" id="tab7"  disabled=true class="tab" href='<c:out value="${formAction}" escapeXml="false"/>' class="tab">Fees</a>	
	<c:url value="/oma/fulfillmentServiceContacts.html?method=load" var="formAction"/>
	<a onclick="return false" id="tab4"  disabled=true class="tab" href='<c:out value="${formAction}" escapeXml="false"/>' class="tab">Contacts</a>
	<c:url value="/oma/fulfillmentServiceNotes.html" var="formAction">
	<c:param name="method" value="loadVendorNote"/>
	<c:param name="mode" value='<%=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"" %>'/>
	</c:url>
	<a onclick="return false"  disabled=true id="tab5" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Notes</a>
	
	<a onclick="return false" id="tab9"  disabled=true class="tab" href="../../oma/styleskus.html" class="tab">Styles & SKU's</a>
	<a onclick="return false" id="tab8"  disabled=true class="tab" href="../../oma/requestHistory.html" class="tab">Request History</a>														
															
</div>
<div style="border-bottom: 1px solid #000000;padding:2px"> </div>		