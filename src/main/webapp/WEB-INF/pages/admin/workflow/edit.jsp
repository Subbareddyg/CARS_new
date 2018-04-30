<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="workflow.edit.title"/></title>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Edit Workflow
	</div>
	<div class="x-panel-body">
<spring:bind path="workflowForm">
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

<div id="attr_container">
	<c:url value="/admin/workflow/workflowForm.html" var="formAction"/>
	<form:form commandName="workflowForm" method="post" action="${formAction}" onsubmit="return onFormSubmit(this)" id="workflowForm">
		<fieldset>
			<ul>
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="workflow.edit.name"/>
					</label>
					<form:input path="name" id="txt_attr_name" cssClass="text"
						cssErrorClass="text medium error" />					
				</li>
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="workflow.edit.description"/>
					</label>
					<form:textarea cols="10" rows="2" path="description" id="txt_attr_name" cssClass="text"
						cssErrorClass="text medium error" />					
				</li>
				<li>				
				<label for="txt_attr_name">
						<fmt:message key="workflow.edit.status"/>
					</label>
					<br/>
					 <c:forEach items="${statuses}" var="wStatus">
						<form:checkbox label="${wStatus.name}" value="${wStatus.statusCd}" path="statuses"/><br/>
					</c:forEach>	
											
				</li>
				<li>
				<label for="txt_attr_name">						
						<fmt:message key="workflow.type"/>
					</label>
					<form:select path="workflowType" items="${types}" itemLabel="name" itemValue="typeCd"></form:select>
				</li>
			</ul>
				<br/>								
			<div class="buttons">
				<input type="submit" class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" />
			</div>
		</fieldset>	
	    <input type="hidden" name="id" value="${workflowForm.workflowId}"/>
	    <input type="hidden" name="action" value="addWorkflow"/>	   	    
	</form:form>
</div>
</div></div>
</body>
