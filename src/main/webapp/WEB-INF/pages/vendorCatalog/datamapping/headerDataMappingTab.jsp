<%@ page import="com.belk.car.app.model.vendorcatalog.VendorCatalog" %>
<div  id="tabArea">
	<c:url value="/vendorCatalog/vendorCatalogSetupForm.html" var="formAction">
	<c:param name="mode" value='<%=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"" %>'/>
	<c:param name="vcID" value='<%=(session.getAttribute("vcID") !=null)?session.getAttribute("vcID").toString():"" %>'/>
	</c:url>
	
	<a id="tab1" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Spreadsheet Details</a>

	<%  VendorCatalog catalog = (VendorCatalog) session.getAttribute("vendorCatalog");
    if (null != catalog) { %>
    
    <c:url value="/vendorCatalog/datamapping/vendorCatalogDataMapping.html?method=getVendorCatalog&amp;cid=${vcID}" var="formAction"/>
	<a id="tab2" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Data Mapping</a>
	
	<c:url value="/vendorCatalog/CatalogNotes.html?method=load" var="formAction"/>
	<a id="tab3" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Notes</a>
	<%
        if (catalog.getStatusCD().equals("Complete")) {
    %>
    <c:url value="/vendorCatalog/catalogVendors.html?method=viewAllVendorCatalogStyles" var="formAction1"/>
    <a id="tab4" class="tab" style="margin-top:0;" href='<c:out value="${formAction1}" escapeXml="false"/>' id="a_view_all">Spreadsheet Items</a>
    <% } else {%>
	<a id="tab4" class="tab" style="margin-top:0;" href="#" id="a_view_all" disabled="true">Spreadsheet Items</a>
    <% }%>
    <!-- Added to show disabled tab if new Vendor Catalog -->
	<!-- Added to show disabled tab if new Vendor Catalog -->
	<% }else { %>
    <a id="tab2" class="tab" style="margin-top:0;" href="#" id="a_view_all" disabled="true">Data Mapping</a>
    <a id="tab3" class="tab" style="margin-top:0;" href="#" id="a_view_all" disabled="true" id="a_view_all">Notes</a>
	<a id="tab4" class="tab" style="margin-top:0;" href="#" id="a_view_all" disabled="true">Spreadsheet Items</a>    
	<%} %>

</div>
<div style="border-bottom: 1px solid #000000;padding:2px"> </div>
		
