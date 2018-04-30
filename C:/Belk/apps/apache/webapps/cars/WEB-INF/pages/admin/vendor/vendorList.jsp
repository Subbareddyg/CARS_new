<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="heading" content="<fmt:message key='vendorList.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<div id="search_for_user" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Vendor Search
	</div>
	<div class="x-panel-body">
<c:url value="/vendor/searchvendor.html" var="formAction"/>
	<form method="post"   action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label><fmt:message key="vendor.search.first.name"/></label>
				<input type="text" id="userFName" name="userFName" value="<c:out value="${userfname}" />"/>
			</li>
			<li>
				<label><fmt:message key="vendor.search.last.name"/></label>
				<input type="text" id="userLName" name="userLName" value="<c:out value="${userlname}" />"/>
			</li>
			<li>
				<label><fmt:message key="vendor.search.emailAddress"/></label>
				<input type="text" id="emailId" name="emailId" value="<c:out value="${emailid}" />"/>
			</li>
			<li class="">
				<input type="submit" class="btn" name="searchVendor" value="<fmt:message key="vendor.search.button"/>" />
				<c:url value="/vendors.html" var="formAction"/>
				&nbsp;<a href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a>				
				
			</li>
		</ol>
	</form>
</div></div>
<div id="user_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR Vendors
	</div>
	<div class="x-panel-body">
<!--
<div class="userButtons">
	<a class="btn" href="<c:url value="vendorForm.html?method=Add&from=list"/>" title="Add New Vendor"><fmt:message key="vendorList.button.add"/></a>
</div>
-->
<display:table name="userList" cellspacing="0" cellpadding="0" requestURI="/vendor/searchvendor.html"  
    defaultsort="1" id="users" pagesize="25" class="table" sort="list">
    <display:column property="fullName" sortable="true" titleKey="activeUsers.fullName"  style="width: 25%"/>
    <display:column property="username" sortable="true" titleKey="vendor.emailAddress" style="width: 30%" autolink="true" media="html"/>
    <display:column property="formattedPhoneNumber" sortable="false" titleKey="vendor.phone" style="width: 15%" autolink="true" media="html"/>
    <display:column property="userType.name" sortable="true" titleKey="vendor.userType" style="width: 15%" autolink="true" media="html"/>
    <display:column> 
    <c:choose>
    	<c:when test="${loggedInvendor.id != users.id}">
    		 <secureurl:secureAnchor name="RemoveUserVendor" elementName="RemoveVendor" cssStyle="remove" localized="true" arguments="${users.id},remove"  hideUnaccessibleLinks="true"/>
    	</c:when>
    </c:choose>   					    	
    </display:column>
   <display:column>
   	<secureurl:secureAnchor name="UserVendorDetail"  elementName="VendorDetail"  arguments="${users.id}" title="Detail" hideUnaccessibleLinks="true"/>
   </display:column>
    <display:setProperty name="paging.banner.item_name" value="user"/>
    <display:setProperty name="paging.banner.items_name" value="users"/>
</display:table>
</div></div>
</body>

<content tag="inlineStyle">
#user_list{
	margin-top:10px;
}
</content>

<content tag="jscript">
<![CDATA[

<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="user.vendor.confirm.delete"/>');});	
});


</script>
]]>
</content>