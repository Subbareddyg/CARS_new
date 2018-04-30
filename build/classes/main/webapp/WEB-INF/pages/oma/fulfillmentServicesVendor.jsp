<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerTab.jsp"%>
<%@ include file="breadcrumbForVendors.jsp"%>
<head>
    <title>Fulfillment Service Vendors</title>
    <c:if test="${pageContext.request.remoteUser != null}">
    	<c:set var="headingUser">
      	 <authz:authentication operation="fullName"/>
       </c:set> 
	</c:if>
    <meta name="heading" content="<fmt:message key='mainMenu.heading'/> <c:out value='${headingUser}'/>"/>
    <meta name="menu" content="MainMenu"/>
       
</head>

<body class="admin">
<script language="javascript">
function unlockVendor(action,vendorfsId,pagination){
	
	document.forms[0].action ="fulfillmentServiceVendors.html?method="+action+"&id="+vendorfsId+"&scrollPos="+document.documentElement.scrollTop+"&d-49788-p="+pagination;
    document.forms[0].submit();
}
function disableRole(){
	var role='<c:out value="${sessionScope.displayRole}" />';
	//alert(role);
	if(role === 'user'){
		//alert('Inside if');
		//alert(document.getElementById("AddBtn"));
		document.getElementById("AddBtn").disabled = true;
	}
}

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

function init() {
	 document.getElementById("tab2").className="activeTab";
	 disableRole();
	 var pos=<c:out value="${requestScope.scrollPos}" />
	
	 if(pos == 0){
		 setTimeout(function() { document.getElementById('vendorID').focus(); }, 10);
	 }
}
</script>
<div class="cars_panel x-hidden"  style="margin:10px 0px 0px;">


	<%@ include file="omaInfoSection.jsp"%>
	
</div>

<div id="search_for_user" class="cars_panel x-hidden" style="margin:10px 0px 0px;">
	<div class="x-panel-header">
		Vendor Search
	</div>
	<div class="x-panel-body">
	<c:if test="${not empty searchFormError}">
	    <div class="error">        
	            <img src="<c:url value="/images/iconWarning.gif"/>"
	                alt="<fmt:message key="icon.warning"/>" class="icon"/>
	            <c:out value="${searchFormError}" escapeXml="false"/><br />        
	    </div>
    </c:if>
<c:url value="/oma/fulfillmentServiceVendors.html?method=search" var="formAction"/>
	<form method="post"   action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label style="font-weight:bold"><fmt:message key="fsvendor.search.first.name"/></label>
				<input type="text" id="vendorID" maxlength="7" name="vendorID" value="<c:out value="${vendorID}" />"/>
				
			</li>
			<li>
				<label style="font-weight:bold"><fmt:message key="fsvendor.search.last.name"/></label>
				<input type="text" id="vendorName" name="vendorName" value="<c:out value="${vendorName}" />"/>
			</li>
			<li>
				<label style="font-weight:bold"><fmt:message key="fsvendor.search.status"/></label>
				<select id="status" name="status"  >
				
				<c:choose>
     				<c:when test="${status =='all'}">  
   							<option id="ext-gen106" selected="selected" value="all">All</option>
					</c:when>
					<c:otherwise>
						<option id="ext-gen106"  value="all">All</option>
					</c:otherwise>
				</c:choose>
				<c:choose>
     				<c:when test="${status =='active'}">  
   							<option id="ext-gen106" selected="selected" value="active">Active</option>
					</c:when>
					<c:otherwise>
						<option id="ext-gen106"  value="active">Active</option>
					</c:otherwise>
				</c:choose>
				<c:choose>
     				<c:when test="${status =='inactive'}">  
   							<option id="ext-gen106" selected="selected" value="inactive">Inactive</option>
					</c:when>
					<c:otherwise>
						<option id="ext-gen106"  value="inactive">Inactive</option>
					</c:otherwise>
				</c:choose>
				<c:choose>
     				<c:when test="${status =='testing'}">  
   							<option id="ext-gen106" selected="selected" value="testing">Testing</option>
					</c:when>
					<c:otherwise>
						<option id="ext-gen106"  value="testing">Testing</option>
					</c:otherwise>
				</c:choose>
					
					                             
							  
				</select>
			</li>
			<li class="">
				<input class="btn" type="submit" name="searchUser" value="<fmt:message key="fsvendor.search.button"/>" />
				<c:url value="/oma/fulfillmentServiceVendors.html?method=viewAllActive" var="formAction"/>
				<a  style="margin-top:0;" href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a>				
			</li>
		</ol>
	</form>
</div></div>

<div id="prodtype_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Vendors
	</div>
	<div class="x-panel-body">
<div class="userButtons" id="AddBtn1">

				<c:if test="${sessionScope.mode=='viewOnly'}">
					<div class="buttons" style="float:right;">
						<input type="button" onclick="none" disabled="disabled" value="Add Vendor" style="font-size: 12px;
							color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" 
							id="AddBtn" class="add_note btn">
					</div>
				</c:if>
				<c:if test="${sessionScope.mode!='viewOnly'}">
					<div class="buttons" style="float:right;">	
						<a class="btn" id="AddBtn" href="<c:url value="/oma/AddVendor.html?mode=add"/>" title="Add Vendor">Add Vendor</a>  
					</div>
				</c:if>
	
</div>
<display:table name="vendorList" cellspacing="0" cellpadding="0" requestURI="/oma/fulfillmentServiceVendors.html?method=search"  
    defaultsort="1" id="vndr" pagesize="25" class="table dstable" sort="list">
    <display:column sortable="true" titleKey="fsVendor.id"	style="width: 10%;text-align:center"  sortProperty="venNum">
    <c:if test="${sessionScope.mode=='viewOnly'}">
			<secureurl:secureAnchor name="FSVendorDetailViewOnly"  arguments="${vndr.vndrFulfillmentServId},viewOnly,param" title="${vndr.venNum}" hideUnaccessibleLinks="true"/>
	</c:if>
	<c:if test="${sessionScope.mode!='viewOnly'}">
			<secureurl:secureAnchor name="FSVendorDetailViewOnly"  arguments="${vndr.vndrFulfillmentServId},edit,param" title="${vndr.venNum}" hideUnaccessibleLinks="true"/>
	</c:if>
	</display:column>
        
    <display:column property="venName" defaultorder="ascending" sortable="true" titleKey="fsVendor.name" style="width: 25%"/>
    <display:column property="statusCd"  sortable="true" titleKey="fsVendor.status" style="width: 7%"/>
    
    <display:column property="numStyles"  sortable="true" titleKey="fsVendor.numStyles" style="width: 10%;text-align:center"/>
    <display:column property="numSkus"  sortable="true" titleKey="fsVendor.numSkus" style="width: 10%;text-align:center"/>    	 
    <display:column property="updatedDate" format="{0,date,MM/dd/yyyy}"  sortable="true" titleKey="fsVendor.dtLastUpdated" style="width: 10%"/>
    <display:column property="updatedBy"  sortable="true" titleKey="fsVendor.lstUpdatedBy" style="width: 10%"/>
  	<display:column title="" style="width: 30%">
  	
	  	<c:if test="${sessionScope.mode!='viewOnly'}">
		  	<c:choose>
		     	<c:when test="${vndr.isLocked =='Y'}">  
		      	<input class="edit_car btn" type="button" onclick="none" disabled="disabled" 
		      			value="Edit" style="font-size: 12px; color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" title="Edit">
		     </c:when>
		     <c:otherwise>
		     	<secureurl:secureAnchor name="FSVendorDetail" cssStyle="btn" arguments="${vndr.vndrFulfillmentServId},edit" title="Edit" hideUnaccessibleLinks="true"/> 
		     </c:otherwise>
	    </c:choose>
		</c:if>
	</display:column>
    
    <display:setProperty name="paging.banner.item_name" value="vendor"/>
    <display:setProperty name="paging.banner.items_name" value="vendors"/>
</display:table>


</div></div>

</body>

<content tag="inlineStyle">
#preview_content *{
	zoom:1;
}
</content>

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
              window.scrollTo(0,<c:out value="${requestScope.scrollPos}" />);
});
</script>
]]>
</content>


