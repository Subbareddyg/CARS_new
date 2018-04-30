<div  id="tabArea">
										
	<c:url value="/vendorCatalog/catalogVendors.html?method=viewAll" var="formAction"/>
	<a id="tab1" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendor Catalogs</a>
	
	<c:url value="/vendorCatalog/catalogVendors.html?method=viewAllOpenCatalogs" var="formAction"/>
	<a id="tab2" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Open Request</a>
										
</div>

	<div style="border-bottom: 1px solid #000000;padding:2px"> </div>	
		
