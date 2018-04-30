<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerTabFulfillmentService.jsp" %>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<script type="text/javascript">

//dropship phase 2: added code fix for firefox browser onload
var browserName=navigator.appName; // Get the Browser Name
if(browserName=="Microsoft Internet Explorer") // For IE
{
	window.onload=init; // Call init function in IE
}
else
{
	if (document.addEventListener) // For Firefox
	{
		document.addEventListener("DOMContentLoaded", init, false); // Call init function in Firefox
	}
}

function init(){
	document.getElementById("tab1").className="activeTab";
	viewOnlyMode();
	
	 var s = document.getElementById("fulfillmentServiceStatusCode");
	 var v ='${requestScope.fulfillmentServiceStatusCode}';
	 for (var i = 0; i < s.options.length ; i++) {
	  if( s.options[i].value == v){
	    s.options[i].selected = true;
		return;
	  }
	}
	 var element=document.getElementById('fulfillmentServiceName');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled  ){
		 setTimeout(function() { element.focus(); }, 10);
		}
}
function viewOnlyMode(){
	
	var mode1='<%=session.getAttribute("mode") %>';
	
	if(mode1==='viewOnly'){
		document.getElementById("AddBtn").disabled = true;
						
	}
}


</script>
<head>
    <title>Fulfillment Services</title>
    <meta name="heading" content="<fmt:message key='vendorList.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
  
    
</head>

<body class="admin">
<div style="margin-left:5px;">
<br/>
<b>Fulfillment Services </b>
<br/>
</div>
<br/>
<div id="search_for_user" class="cars_panel x-hidden">

<div class="x-panel-header">
		Fulfillment Service Search
	</div>
	
	<div class="x-panel-body">
<c:url value="/oma/orderManagement.html?method=searchFS" var="formAction"/>
	<form method="post"   action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label ><b><fmt:message key="fulfillmentServiceLoad.servicename"/></b></label>
				<input type="text" id="fulfillmentServiceName" name="fulfillmentServiceName" value="<c:out value="${fulfillmentServiceName}" />"/>
			</li>
			<li>
				<label><b><fmt:message key="fulfillmentServiceLoad.status"/></b></label>
				<select id="fulfillmentServiceStatusCode" name="fulfillmentServiceStatusCode" value="<c:out value="${fulfillmentServiceStatusCode}" />">
							  <option value="all">All</option>
                              <option id="ext-gen96" value="active">Active</option>                              
							  <option id="ext-gen106" value="inactive">Inactive</option>
							 <option id="ext-gen106" value="testing">Testing</option>
				</select>
			</li>
			
			<li>
				<input type="submit" class="btn" name="searchFS" value="<fmt:message key="fulfillmentService.button.search"/>" />
				<c:url value="/oma/orderManagement.html?method=viewAll" var="formAction"/>
				&nbsp;<a href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a>				
				
			</li> 
		</ol>
	</form>
</div></div>

<div class="cars_panel x-hidden" style="margin-top:10px;">
	<div class="x-panel-header">
		Service Listing
	</div>
	<div class="x-panel-body">

<div class="userButtons" id="AddBtn1">
				<c:if test="${sessionScope.mode=='viewOnly'}">
					<div class="buttons" style="float:right;">
						<input type="button" onclick="none" disabled="disabled" value="Add Service" style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="AddBtn" class="btn">
					</div>
				</c:if>
				<c:if test="${sessionScope.mode!='viewOnly'}">
						<a class="btn" id="AddBtn" href="<c:url value="../oma/fsPropertiesList.html?mode=add"/>" title="Add Service">
						<fmt:message key="fulfillmentService.button.addservice"/></a>
				</c:if>
</div>

<display:table name="FulfillmentService" cellspacing="0" cellpadding="0" requestURI="/oma/orderManagement.html"  
    defaultsort="1" id="FulfillmentService" pagesize="25" class="dstable" sort="list">
    <display:column comparator="com.belk.car.util.LongComparator" style="width: 8%;text-align:center;" sortProperty="fulfillmentServiceID" title="ID" sortable="true">    		
			 	<secureurl:secureAnchor name="fulfillmentServiceID" cssStyle="text-align:center;" title="${FulfillmentService.fulfillmentServiceID}" elementName="fulfillmentServiceID"  
			 	localized="true"  hideUnaccessibleLinks="true" arguments="${FulfillmentService.fulfillmentServiceID},${mode}"/>		   	
	</display:column>
    <display:column title="Name" sortable="true" sortProperty="fulfillmentServiceName">    		
			 	<secureurl:secureAnchor name="fulfillmentServiceName" cssStyle="removeVendor" title="${FulfillmentService.fulfillmentServiceName}" elementName="fulfillmentServiceName"  
			 	localized="true"  hideUnaccessibleLinks="true" arguments="${FulfillmentService.fulfillmentServiceName},${mode}"/>		   	
	</display:column>
     <display:column property="fulfillmentServiceDesc" sortable="true" title="Description"  style="width: 25%"/>
  <display:column property="fulfillmentMethodID.fulfillmentMethodDesc" sortable="true" title="Fulfillment Method"  style="width: 25%"/>
   <display:column property="fulfillmentServiceIntTypeID.integrationTypeDesc" sortable="true" title="Integration Type"  style="width: 25%"/>
    <display:column property="fulfillmentServiceStatusCode" sortable="true" title="Status"  style="width: 20%"/>
    
    <display:setProperty name="paging.banner.item_name" value="service"/>
    <display:setProperty name="paging.banner.items_name" value="services"/>
    
</display:table>
</div>
</div>
</body>
<script type="text/javascript" src="<c:url value='/pages/oma/orderManagement.js'/>"></script>
</body>
<content tag="jscript">
<![CDATA[

<script type="text/javascript">
$(document).ready(function(){
	
	$('body.admin table.dstable tr').hover(function(){
	  $('td',this).addClass('trHover');
	  $(this).click(function(){
	  $('td',this).removeClass('trHover');
	  $('td',$(this).parent()).removeClass('trSelected');
	  $('td',this).addClass('trSelected');
	  });
	},function(){
	  $('td',this).removeClass('trHover');
	  });
});
</script>
]]>
</content>

