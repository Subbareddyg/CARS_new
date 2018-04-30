<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="changePassword.title" /></title>
</head>
<body>
	<div id="forgot_password">
		<spring:bind path="changePasswordForm">
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
		<c:url value="/changePassword.html" var="formAction"/>
		<form:form commandName="changePasswordForm" method="post" id="changePasswordForm" action="${formAction}">
			<fieldset>
				<legend></legend>
				<ol>
					<li class="text">
						<label for="password">
							<fmt:message key="label.newPassword"/>:
						</label>
						<form:password path="password" id="password"/><form:errors path="password" id="password"/>
					</li>
					<li class="text">
						<label for="confirmPassword">
							<fmt:message key="label.confirmPassword"/>:
						</label>
						<form:password path="confirmPassword" id="confirmPassword"/><form:errors path="confirmPassword" id="confirmPassword"/>
					</li>
					<li>
						<a href="#" class="btn" id="btn_change_password"><fmt:message key='button.change.password'/></a>
						<input style="display:none;" type="submit" class="button" name="changePassword"
							value="<fmt:message key='button.change.password'/>" />
					</li>
				</ol>
			</fieldset>
			<form:hidden path="emailAddress"/>
		</form:form>
		
	</div>
</body>

<content tag="inlineStyle">
</content>

<content tag="jscript">
</content>