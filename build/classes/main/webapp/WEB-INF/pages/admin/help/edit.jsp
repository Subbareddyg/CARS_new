<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="helpContent.edit.title"/></title>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		<fmt:message key="helpContent.edit.h1"/>
	</div>
	<div class="x-panel-body">
<spring:bind path="helpContentForm.*">
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
<c:url value="/admin/help/helpForm.html" var="formAction"/>
	<form:form commandName="helpContentForm" method="post" action="${formAction}" onsubmit="return onFormSubmit(this)" id="helpContentForm">
		<fieldset>
			<ul>
				<li>
					<label for="txt_content_key">						
						<fmt:message key="help.content.key"/>:
					</label>
					<form:input path="contentKey" id="txt_content_key" cssClass="text"
						cssErrorClass="text medium error" />					
				</li>
				<li>
					<label for="txt_content_section">						
						<fmt:message key="help.content.section"/>:
					</label>
					<form:input path="contentSection" id="txt_content_section" cssClass="text"
						cssErrorClass="text medium error" />					
				</li>
				<li>
					<label for="txt_content_name">						
						<fmt:message key="help.content.name"/>:
					</label>
					<form:input path="contentName" id="txt_content_name" cssClass="text"
						cssErrorClass="text medium error" />					
				</li>
				<li>
					<label for="txt_content_text">						
						<fmt:message key="help.content.text"/>:
					</label>
					<form:textarea cols="10" rows="2" path="contentText" id="txt_content_text" cssClass="text"
						cssErrorClass="text medium error" />					
				</li>
			</ul>
			<br/>								
			<div class="buttons">
				<input type="submit" class="btn cancel_btn" name="cancel" value="<fmt:message key="button.cancel"/>" />
				<input type="submit" class="btn save_btn" name="save" value="<fmt:message key="button.save"/>" />
			</div>
		</fieldset>	
	    <input type="hidden" name="contentId" value="${helpContentForm.contentId}"/>
	    <input type="hidden" name="statusCd" value="${helpContentForm.statusCd}"/>
	    <input type="hidden" name="action" value="addHelpContent"/>	   	    
	</form:form>
</div></div>
</body>

<content tag="inlineStyle">
form li label{
	float:left;
	font-weight:bold;
	width:200px;
	padding-right:10px;
	padding-top:3px;
	text-align:right;
}
form li{
	clear:both;
	padding:5px 0;
}
textarea{
	width:300px;
	height:75px;
}
input.text{
	width:300px;
}
div.buttons{
	padding-left:210px;
}
</content>

<content tag="jscript">
<![CDATA[
]]>
</content>