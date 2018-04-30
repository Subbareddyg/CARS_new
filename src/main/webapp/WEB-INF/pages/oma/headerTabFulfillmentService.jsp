<div  id="tabArea">
										
	<c:url value="/oma/orderManagement.html" var="formAction">
		<c:param name="method" value="load"/>
		<c:param name="mode" value='<%=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"" %>'/>
	</c:url>
	
	<a id="tab1" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Fulfillment Services</a>
	<c:url value="../oma/vendorTaxStates.html?method=viewAll" var="formAction"/>
	<a id="tab2" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendor Tax States</a>
										
</div>

	<div style="border-bottom: 1px solid #000000;padding:2px"> </div>	
		
