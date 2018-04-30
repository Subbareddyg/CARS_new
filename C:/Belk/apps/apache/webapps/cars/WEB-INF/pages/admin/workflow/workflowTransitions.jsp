<%@ include file="/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="transitions.title" />
	</title>
	<meta name="heading" content="<fmt:message key='workflow.transitions.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>
<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Add Workflow Transition
	</div>
	<div class="x-panel-body">
<ul>
<li>
<label for="txt_attr_name">Name: </label> <c:out value="${workflowForm.workflow.name}"/>
</li>
<li>
<label for="txt_description">Description:</label> <c:out value="${workflowForm.workflow.description}"/>
</li>
</ul>
<spring:bind path="workflowForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				&nbsp;&nbsp;&nbsp; <br><img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>
	<c:url value="transitionForm.html" var="formAction"/>
	<form:form commandName="workflowForm" method="post" action="${formAction}" id="workflowForm">
		<input type="hidden" name="id" value="${workflowForm.workflowId}"/>			
		<c:if test="${not empty workflowForm.workflowTransitionId}">
			<input type="hidden" name="workflowTransitionId" value="${workflowForm.workflowTransitionId}"/>	
		</c:if>
		<input type="hidden" name="workflowId" value="${workflowForm.workflowId}"/>											
		<input type="hidden" name="action" value="addTransition"/>
		<ul>
			<li>
				<label><fmt:message key="workflow.transition.page.name"/></label>
				<form:input path="transitionName" id="txt_attr_name" cssClass="text" cssErrorClass="text medium error" maxlength="50"/>
			</li>
			<li>
				<label><fmt:message key="workflow.transition.page.description"/></label>
				<form:textarea path="transitionDescription" id="txt_attr_name" cssClass="text" cssErrorClass="text medium error" />
			</li>
			<li>
				<label><fmt:message key="workflow.transition.page.current.user"/></label>
				<form:select path="currentUserType" items="${users}" itemLabel="name" itemValue="userTypeCd"  />
			</li>
			<li>
				<label><fmt:message key="workflow.transition.page.transition.user"/></label>
				<form:select path="transitionUserType" items="${users}" itemLabel="name" itemValue="userTypeCd"  />
			</li>
			<li>
				<label><fmt:message key="workflow.transition.page.current.status"/></label>
				<form:select path="currentStatusCd" items="${statuses}" itemLabel="name" itemValue="statusCd"  />
			</li>
			<li>
				<label><fmt:message key="workflow.transition.page.transition.status"/></label>
				<form:select path="transitionStatusCd" items="${statuses}" itemLabel="name" itemValue="statusCd"  />
			</li>
			<li>
				<label><fmt:message key="workflow.transition.page.task"/></label>
				<form:select path="task" items="${tasks}" itemLabel="name" itemValue="taskCd"  />
			</li>
			<li>
				<label><fmt:message key="workflow.transition.page.number.of.days"/></label>
				<form:input path="numberOfDaysToCompleteTask" id="txt_attr_name" cssClass="text" cssErrorClass="text medium error"/>
			</li>
			<li class="buttons">
				<input type="submit" class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
				<input id="save" class="btn" type="submit" value="Save Transition"/>
			</li>
		</ul>		
	</form:form>
</div></div>
</body>

<content tag="inlineStyle">
label{
	float:left;
	width:160px;
	font-weight:bold;
	text-align:right;
	padding-right:10px;
}
input.text,textarea,select{
	width:250px;
}
li{
	padding:5px 0;
}
li.buttons{
	padding-left:200px;
}
</content>
