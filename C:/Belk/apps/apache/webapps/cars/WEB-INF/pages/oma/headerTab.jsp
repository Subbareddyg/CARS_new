<div  id="tabArea">
	
	<c:url value="/oma/fsPropertiesList.html" var="formAction">
<c:param name="mode" value='<%=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"" %>'/>
<c:param name="fsID" value="${sessionScope.fulfillmentService.fulfillmentServiceID}"/>
</c:url>
	<a id="tab1" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Service Properties</a>

<c:url value="/oma/shippingOptions.html" var="formAction"/>
<%if(("add").equals((session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"")){ %>
		<a id="tab3"  style="margin-top:0;
	display: inline;
	margin-bottom:10px;
	background: #D9D9D9;
	color: #15428B;
	text-decoration: none;
	padding: 2px 5px;
	border: solid 1px #aaaaaa;
	display:inline;
	font-size: 13px;
	font-weight: bold;
    margin-right: -2px;" id="tab6" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' onclick="return false;">Shipping Options</a>
		<%}else { %>
		<a id="tab6" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>'>Shipping Options</a>
		<%} %>
		 <c:url value="/oma/fulfillmentVendorReturns.html" var="formAction"/>
<%if(("add").equals((session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"")){ %>
      <a id="tab10" style="margin-top:0;
	display: inline;
	margin-bottom:10px;
	background: #D9D9D9;
	color: #15428B;
	text-decoration: none;
	padding: 2px 5px;
	border: solid 1px #aaaaaa;
	display:inline;
	font-size: 13px;
	font-weight: bold;
    margin-right: -2px;"  class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all" onclick="return false;">Returns</a>
      
     <%}else{ %> 
       <a id="tab10"  class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Returns</a>
       <%} %>
       
		 <c:url value="/oma/fulfillmentServiceContacts.html?method=load" var="formAction"/>
		<%if(("add").equals((session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"")){ %> 
	  <a  id="tab4" style="margin-top:0;
	display: inline;
	margin-bottom:10px;
	background: #D9D9D9;
	color: #15428B;
	text-decoration: none;
	padding: 2px 5px;
	border: solid 1px #aaaaaa;
	display:inline;
	font-size: 13px;
	font-weight: bold;
    margin-right: -2px;"  class="tab" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all" onclick="return false;">Contacts</a>
	  <%}else{ %> 
	   <a  id="tab4" class="tab" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Contacts</a>
	   <%} %>
	  	<c:url value="/oma/fulfillmentServiceNotes.html" var="formAction">
<c:param name="method" value="load"/>
<c:param name="mode" value='<%=(session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"" %>'/>
</c:url>
<%if(("add").equals((session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"")){ %>
	<a id="tab5" style="margin-top:0;
	display: inline;
	margin-bottom:10px;
	background: #D9D9D9;
	color: #15428B;
	text-decoration: none;
	padding: 2px 5px;
	border: solid 1px #aaaaaa;
	display:inline;
	font-size: 13px;
	font-weight: bold;
    margin-right: -2px;" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all" onclick="return false;">Notes</a>
	 <%}else{ %> 
	<a id="tab5" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Notes</a>
	  <%} %>
	<c:url value="/oma/fulfillmentServiceVendors.html?method=viewAll" var="formAction"/>
<%if(("add").equals((session.getAttribute("mode") !=null)?session.getAttribute("mode").toString():"")){ %> 
      <a id="tab2" style="margin-top:0;
	display: inline;
	margin-bottom:10px;
	background: #D9D9D9;
	color: #15428B;
	text-decoration: none;
	padding: 2px 5px;
	border: solid 1px #aaaaaa;
	display:inline;
	font-size: 13px;
	font-weight: bold;
    margin-right: -2px;" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all" onclick="return false;">Vendors</a>
      <%}else{ %> 
	<a id="tab2" class="tab" style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">Vendors</a>	
	 <%} %>
		
		
		
	
										
</div>
	<div style="border-bottom: 1px solid #000000;padding:2px"> </div>