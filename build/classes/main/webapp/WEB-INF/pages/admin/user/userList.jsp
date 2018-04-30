<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="userList.title"/></title>
    <meta name="heading" content="<fmt:message key='userList.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<div id="search_for_user" class="cars_panel x-hidden">
	<div class="x-panel-header">
		User Search
	</div>
	<div class="x-panel-body">
<c:url value="/admin/user/searchuser.html" var="formAction"/>
	<form method="post"   action="${formAction}" id="searchForm">
		<ol>
			<li>
				<label><fmt:message key="user.search.first.name"/></label>
				<input type="text" id="userFName" name="userFName" value="<c:out value="${userfname}" />"/>
			</li>
			<li>
				<label><fmt:message key="user.search.last.name"/></label>
				<input type="text" id="userLName" name="userLName" value="<c:out value="${userlname}" />"/>
			</li>
			<li>
				<label><fmt:message key="user.search.emailAddress"/></label>
				<input type="text" id="emailId" name="emailId" value="<c:out value="${emailid}" />"/>
			</li>
			<li class="">
				<input class="btn" type="submit" name="searchUser" value="<fmt:message key="user.search.button"/>" />
				<c:url value="/admin/user/users.html" var="formAction"/>
				&nbsp;<a href='<c:out value="${formAction}" escapeXml="false"/>' id="a_view_all">View All</a>				
			</li>
		</ol>
	</form>
</div></div>
<div id="user_list" class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR Users
	</div>
	<div class="x-panel-body">
<div class="userButtons">
	<a class="btn" href="<c:url value="/admin/user/userForm.html?method=Add&from=list"/>" title="Add New User"><fmt:message key="userList.button.add"/></a>
</div>
<display:table name="userList" cellspacing="0" cellpadding="0" requestURI="/admin/user/searchuser.html"  
    defaultsort="1" id="users" pagesize="25"   class="table" sort="list" size="totalResultSize"  partialList="true" export="false">
    <display:column property="fullName" sortable="true" titleKey="activeUsers.fullName"  style="width: 25%"/>
    <display:column property="username" sortable="true" titleKey="user.emailAddress" style="width: 30%" autolink="true" media="html"/>
    <display:column property="formattedPhoneNumber" sortable="false" titleKey="user.phone" style="width: 15%" autolink="true" media="html"/>
    <display:column property="userType.name" sortable="true" titleKey="user.userType" style="width: 15%" autolink="true" media="html"/>
    <display:column> 
    <c:choose>
    	<c:when test="${loggedInUser.id != users.id}">
    		 <secureurl:secureAnchor name="RemoveUser" elementName="RemoveUser" cssStyle="remove" localized="true" arguments="${users.id},remove" hideUnaccessibleLinks="true"/>
    	</c:when>
    </c:choose>   					    	
    </display:column>
   <display:column>
   	<secureurl:secureAnchor name="UserDetail"  elementName="UserDetail"  arguments="${users.id}" title="Detail" hideUnaccessibleLinks="true"/>
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
	$('a.remove').click(function(){return confirm('<fmt:message key="user.confirm.delete"/>');});	
});


</script>
]]>
</content>