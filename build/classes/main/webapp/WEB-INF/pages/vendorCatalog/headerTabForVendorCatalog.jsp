<div  id="tabArea">
										
	<c:set var="vendorId" value="${sessionScope.vendorCatalogInfo.vendorId}"/>
	<c:set var="numStyles" value="${sessionScope.vendorCatalogInfo.numStyle}"/>
	<c:set var="numSKUs" value="${sessionScope.vendorCatalogInfo.numSKUs}"/>
	<c:url value="/vendorCatalog/catalogVendors.html?vendorId=${vendorId}&numStyles=${numStyles}&numSKUs=${numSKUs}&method=viewAllVendorCatalogs" var="formAction"/>
	<a id="tab1" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendor Spreadsheets</a>
	
	<c:url value="/vendorCatalog/catalogVendors.html?method=viewAllVendorStyles" var="formAction"/>
	<a id="tab2" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Catalog Styles</a>
										
	 <c:url value="/vendorCatalog/catalogVendors.html?method=initVendorProperties" var="formAction"/>
	<a id="tab3" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Properties</a>
										
</div>

	<div style="border-bottom: 1px solid #000000;padding:2px"> </div>	
		
