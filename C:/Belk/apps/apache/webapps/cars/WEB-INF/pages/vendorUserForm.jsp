<%@ include file="/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="userProfile.title" />
	</title>
	<meta name="heading" content="<fmt:message key='userProfile.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">
<c:if test="${param.from != 'profile'}">

<div class="userButtons">
	<%-- 
	<a class="btn" href="javascript:alert('TODO implement me');" title="Edit CAR User"><fmt:message key="button.save"/></a>
	<a class="btn" href="javascript:alert('TODO implement me');" title="Back to User List"><fmt:message key="button.cancel"/></a>
	--%>
	<h1>Edit Vendor User - <c:out value="${user.firstName}" escapeXml="false" /> <c:out value="${user.lastName}" escapeXml="false" /></h1>
</div>

</c:if>
<spring:bind path="user.*">
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
<c:url value="/vendorUserForm.html" var="formAction"/>
<form:form commandName="user" method="post" action="${formAction}" id="userForm">
	<form:hidden path="id" />
	<form:hidden path="version" />
	<input type="hidden" name="from" value="<c:out value="${param.from}"/>" />
	<input type="hidden" name="isAjaxForm" value="false" />

	<c:if test="${empty user.version}">
		<input type="hidden" name="encryptPass" value="true" />
	</c:if>
	<input type="hidden" name="car" value="${param.car}" />
	<ol>
		<li>
			<p class="required">Required fields are noted with an *</p>
		</li>
		<li class="email">
			<appfuse:label styleClass="desc" key="user.emailAddress" />
			<form:errors path="emailAddress" cssClass="fieldError" />
			<form:input path="emailAddress" id="emailAddress" cssClass="text large"	cssErrorClass="text medium error" />
		</li>
		<li class="first">
			<appfuse:label styleClass="desc" key="user.firstName" />
			<form:errors path="firstName" cssClass="fieldError" />
			<form:input path="firstName" id="firstName" cssClass="text"
				cssErrorClass="text error" maxlength="50" />
		</li>
		<li class="last">
			<appfuse:label styleClass="desc" key="user.lastName" />
			<form:errors path="lastName" cssClass="fieldError" />
			<form:input path="lastName" id="lastName" cssClass="text"
				cssErrorClass="text error" maxlength="50" />
		</li>
		<li class="addr1">
			<appfuse:label styleClass="desc" key="user.address.address1" />
			<form:input path="addr1" id="addr1" cssClass="text large"
				cssErrorClass="text large error" />
			<form:errors path="addr1" cssClass="fieldError" />
		</li>
		<li class="addr2">
			<appfuse:label styleClass="desc" key="user.address.address2" />
			<form:input path="addr2" id="addr2" cssClass="text large"
				cssErrorClass="text large error" />
			<form:errors path="addr2" cssClass="fieldError" />
		</li>
		<li class="city">
			<appfuse:label styleClass="desc" key="user.address.city" />
			<form:input path="city" id="city" cssClass="text medium"
				cssErrorClass="text medium error" />
			<form:errors path="city" cssClass="fieldError" />
		</li>
		<li class="state">
			<appfuse:label key="user.address.province" styleClass="desc" />
			<form:input path="stateCd" id="stateCd" cssClass="text state"
				cssErrorClass="text state error" />
			<form:errors path="stateCd" cssClass="fieldError" />
		</li>
		<li class="zip">
			<appfuse:label key="user.address.postalCode" styleClass="desc" />
			<form:input path="zipcode" id="zipcode" cssClass="text medium"
				cssErrorClass="text medium error" />
			<form:errors path="zipcode" cssClass="fieldError" />
		</li>		
		<li class="phone">
			<appfuse:label styleClass="desc" key="user.phoneAreaCode" />
			(<form:input path="phoneAreaCode" id="phoneAreaCode" 
				cssErrorClass="text small error" size="3" maxlength="3" />)
			<form:errors path="phoneAreaCode" cssClass="fieldError" />
			<form:input path="phoneNumber1" id="phoneNumber1" 
				cssErrorClass="text small error" size="3" maxlength="3"/>
			<form:errors path="phoneNumber1" cssClass="fieldError" />
			<form:input path="phoneNumber2" id="phoneNumber2" cssErrorClass="text small error" size="4" maxlength="4"/>
			<form:errors path="phoneNumber2" cssClass="fieldError" />
		</li>
		<li class="alt_phone">
			<appfuse:label styleClass="desc" key="user.address.alt.phone" />
			(<form:input path="altPhoneAreaCode" id="altPhoneAreaCode" 
				cssErrorClass="text small error" size="3" maxlength="3" />)
			<form:errors path="altPhoneAreaCode" cssClass="fieldError" />
			<form:input path="altPhoneNumber1" id="altPhoneNumber1" cssErrorClass="text small error" size="3" maxlength="3" />
			<form:errors path="altPhoneNumber1" cssClass="fieldError"  />
			<form:input path="altPhoneNumber2" id="altPhoneNumber2" cssErrorClass="text small error" size="4" maxlength="4" />
			<form:errors path="altPhoneNumber2" cssClass="fieldError" />			
		</li>								
		<li class="buttons">
			<input type="submit" class="cancel_btn btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
			<input type="submit" class="save_btn btn" name="save" value="<fmt:message key="button.save"/>" />
		</li>
	</ol>
</form:form>
</body>

<content tag="inlineStyle">
</content>

<content tag="jscript">
<![CDATA[
]]>
</content>
