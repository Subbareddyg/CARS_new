<%@ include file="/common/taglibs.jsp"%>
<%@ include file="headerCatalogVendorsTab.jsp"%>

<head>
<title><fmt:message key="catalogvendors.page.title" /></title>
<c:if test="${pageContext.request.remoteUser != null}">
	<c:set var="headingUser">
		<authz:authentication operation="fullName" />
	</c:set>
</c:if>
<meta name="heading" content="<fmt:message key='workflow.heading'/>" />
<meta name="menu" content="UserMenu" />
</head>
<script>

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

function lockCatalog(catalogId,pagination) {
        document.forms[0].action ="catalogVendors.html?method=lockUnlockOpenCatalog&mode=lock&vendorCatalogID=" + catalogId +"&d-6682707-p="+pagination+"&scrollPos="+document.documentElement.scrollTop;
        document.forms[0].submit();
    }
     function unLockCatalog(catalogId,pagination) {
        document.forms[0].action ="catalogVendors.html?method=lockUnlockOpenCatalog&mode=unlock&vendorCatalogID=" + catalogId +"&d-6682707-p="+pagination+"&scrollPos="+document.documentElement.scrollTop;
        document.forms[0].submit();
    }
    function init(){
    		document.getElementById("tab2").className="activeTab";
                var pos = <c:out value="${scrollPos}" />;
               var element=document.getElementById('vendorId');
		if(element!=null && element.type != "hidden" && element.style.display != "none"  && !element.disabled && pos==0 ){
		 setTimeout(function() { element.focus(); }, 10);
		}
	  }
	
    function setValue123() {
        var val=document.getElementById("tempStatus").value;
       if(val=='All') {
         document.getElementById("status").options[0].selected=true;
       } else if(val=='Importing') {
         document.getElementById("status").options[2].selected=true;
       } else {
        document.getElementById("status").options[1].selected=true;
       }

    }

    function setChkValue() {
    var temp =document.getElementById("tempDepartmentOnly").checked;
    //alert(temp);
    if(temp) {
        document.getElementById("departmentOnly").value=true;
    } else {
        document.getElementById("departmentOnly").value=false;
    }
}

    </script>
<body class="admin">
<!-- Added by Vikas for displaying bread crumb-->
<div style="margin-left: 5px;"><br />
<b>Catalog Open Request </b> <br />
</div>
<br>
<spring:bind path="searchCatalogForm.*">
	<c:if test="${not empty errors}">
		<div class="error"><c:forEach var="error" items="${errors}">
			<img src="<c:url value="/images/iconWarning.gif"/>"
				alt="<fmt:message key="icon.warning"/>" class="icon" />
			<c:out value="${error}" escapeXml="false" />
			<br />
		</c:forEach></div>
		<br>
	</c:if>
</spring:bind>

<div id="search_for_attribute1" class="cars_panel x-hidden">
<div class="x-panel-header"><span class="x-panel-header-text">
Catalog Search </span></div>

<div class="x-panel-body"><c:url
	value="catalogVendors.html?method=searchOpenCatalogs" var="formAction" />
<form:form name="searchCatalogForm" commandName="searchCatalogForm"
	method="post" action="${formAction}" id="searchCatalogForm">

	<table class="car_info" cellspacing="0" cellpadding="0"
		style="border: 0px;">
		<tr>
			<td style="border: 0px; font-weight: bold;"><label
				for="txt_attr_name">Vendor #:</label></td>
			<spring:bind path="searchCatalogForm.vendorId">
				<td style="border: 0px"><input type="text" id="vendorId"
					name="vendorId"
					value="<c:out value="${searchCatalogForm.vendorId}" />"
					cssClass="text" cssErrorClass="text medium error"
					cssStyle="disabled:true;border:none" /></td>
			</spring:bind>

			<td style="border: 0px; font-weight: bold;"><label>Vendor
			Name:</label></td>
			<spring:bind path="searchCatalogForm.vendorName">
				<td style="border: 0px;"><input type="text" id="vendorName"
					name="vendorName" size="20"
					value="<c:out value="${searchCatalogForm.vendorName}" />" /></td>
			</spring:bind>
			<td style="border: 0px;"><input type="submit" class="btn"
				name="method" value="Search" /> <c:url
				value="/catalogVendors.html?method=searchOpenCatalogs"
				var="formAction" /> &nbsp; &nbsp; <a
				href='../vendorCatalog/catalogVendors.html?method=viewAllOpenCatalogs'
				id="a_view_all">View All</a>&nbsp;</td>
		</tr>
		<tr>
			<td style="border: 0px; font-weight: bold;"><label> User
			Departments only:</label></td>
			<td style="border: 0px;"><input id="departmentOnly"
				type="hidden" name="departmentOnly"
				value="<c:out value="${searchCatalogForm.departmentOnly}" />" /> <c:if
				test="${searchCatalogForm.departmentOnly eq 'true'}">
				<input id="tempDepartmentOnly" type="checkbox" checked
					name="tempDepartmentOnly" style="width: 16px;"
					onclick="setChkValue();" />
			</c:if> <c:if test="${searchCatalogForm.departmentOnly !='true'}">
				<input id="tempDepartmentOnly" type="checkbox"
					name="tempDepartmentOnly" style="width: 16px;"
					onclick="setChkValue();" />
			</c:if></td>
			<td style="border: 0px; font-weight: bold;"><label>Status:</label></td>
			<td style="border: 0px;"><spring:bind
				path="searchCatalogForm.status">
				<select id="status" class="required" name="status"
					value="<c:out value="${searchCatalogForm.status}" />">
					<option value="All">All</option>
					<option value="Data Mapping">Data Mapping</option>
					<option value="Importing">Importing</option>
				</select>
			</spring:bind></td>
			<td style="border: 0px">&nbsp;</td>

		</tr>
	</table>
	<input type="hidden" id="tempStatus" name="tempStatus"
		value="<c:out value="${searchCatalogForm.tempStatus}" />" />
</form:form></div>
</div>
<br>



<div id="user_list" class="cars_panel x-hidden">

<div class="x-panel-header">Vendor Catalogs</div>

<div class="x-panel-body">
<div class="userButtons"><secureurl:secureAnchor name="newCatalog"
	cssStyle="btn" title="New Spreadsheet" hideUnaccessibleLinks="true" /></div>

<display:table name="vendorCatalogList" cellspacing="0" cellpadding="0"
	requestURI="/vendorCatalog/catalogVendors.html" defaultsort="1"
	id="vendorCatalog" pagesize="25" class="dstable" sort="list">
	<display:column sortable="true" titleKey="vendorCatalog.id"
		style="width:5%">
		<secureurl:secureAnchor name="vendorCatalogID" cssStyle="removeVendor"
			title="${vendorCatalog.vendorCatalogID}"
			elementName="vendorCatalogID" localized="true"
			hideUnaccessibleLinks="true"
			arguments="${vendorCatalog.vendorCatalogID},viewOnly" />
	</display:column>

	<display:column property="vendorCatalogName" sortable="true"
		titleKey="vendorCatalog.name" style="width: 20%;white-space:normal" />
	<display:column property="vendor.name" sortable="true" title="Vendor"
		style="width: 20%" />
	<display:column property="source" sortable="true"
		titleKey="vendorCatalog.source" style="width: 12%" />
	<display:column property="statusCD" sortable="true"
		titleKey="vendorCatalog.status" style="width: 10%" />
	<display:column property="updatedDate" format="{0,date,MM/dd/yyyy}"
		sortable="true" titleKey="vendorCatalog.lastUpdatedDate"
		style="width: 10%" />
	<display:column property="updatedBy" sortable="true"
		titleKey="vendorCatalog.updatedby" style="width: 8%;white-space:normal" />
	<display:column title="Lock" style="width:15%">
		<c:choose>
		
		
			<c:when test="${vendorCatalog.lockedBy !=null}">
			
			 	<input class="edit_car btn" type="button" onclick="none" disabled="disabled" value="Edit" style="font-size: 12px; color: gray; background: none repeat scroll 0% 0% rgb(204, 204, 204);" title="Edit">

			</c:when>
					<c:otherwise>
					<secureurl:secureAnchor name="editVendorCatalog" cssStyle="btn"
									arguments="${vendorCatalog.vendorCatalogID},edit" title="Edit"
									hideUnaccessibleLinks="true" />

					</c:otherwise>
			
			
		</c:choose>
	</display:column>
	<display:setProperty name="paging.banner.item_name" value="catalog" />
	<display:setProperty name="paging.banner.items_name" value="catalogs" />
</display:table></div>
<script>
        setValue123();

</script>
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
              window.scrollTo(0,<c:out value="${scrollPos}" />);
        });
    </script>
    ]]>
</content>
