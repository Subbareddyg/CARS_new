<%@ include file="/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="userProfile.title" />
	</title>
	<meta name="heading" content="<fmt:message key='userNotes.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">
<div id="user_details" class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR User Details
	</div>
	<div class="x-panel-body">
<spring:bind path="userForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>
<c:set var="user" value="${userForm.user}" scope="request"/>
<c:import url="userInfo.jsp"/>
<c:url value="/admin/user/addUserNotes.html" var="formAction"/>		
<form:form commandName="userForm" method="post" action="${formAction}" id="userNotesForm">
	<input type="hidden" name="id" value="${userForm.user.id}"/>
	<input type="hidden" name="method" value="addUserNote"/>				
	<form:textarea rows="5" cols="25" path="notes" /><br/><br/>
	<div class="buttons">
		<input type="submit" class="btn" name="Save" value="Save" />
	</div>				
</form:form>
</div></div>
</body>

<content tag="inlineStyle">
</content>

<content tag="jscript">
<![CDATA[
]]>
</content>