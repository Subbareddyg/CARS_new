<%@ include file="/common/taglibs.jsp"%>

<script>
function verifyCancel() {
	var stay=confirm("Are you sure you wish to cancel?");
	if (stay)
		window.location="../../admin/productgroup/productGroupForm.html?method=getAllProductGroups";
	else
		return false;
}
</script>
<head>
    <title><fmt:message key="productGroup.edit.title"/></title>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		<fmt:message key="productGroup.edit.title"/>
	</div>
	<div class="x-panel-body">
<spring:bind path="productTypeGroupForm.*">
	<c:if test="${not empty errors}">
		<div class="error">
			<c:forEach var="error" items="${errors}">
				<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>

<div id="attr_container">
<ol>
		<li>
			<span class="req" style="color:#FF0000">* </span>Indicates Required Fields
		</li>
	</ol>
<c:url value="/admin/productgroup/productGroupForm.html" var="formAction"/>
	<form:form commandName="productTypeGroupForm" method="post" action="${formAction}" onsubmit="return onFormSubmit(this)" id="productTypeGroupForm">
		<fieldset>
			<ul>
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="productGroup.edit.name"/>
						<span class="req" style="color:#FF0000">*</span>
					</label>
					<form:input path="name" id="txt_attr_name" cssClass="text"
						cssErrorClass="text medium error" maxlength="50" />					
				</li>
				<li>
					<label for="txt_attr_name">						
						<fmt:message key="productGroup.description"/>:
						<span class="req" style="color:#FF0000">*</span>
					</label>
					<form:textarea cols="10" rows="2" path="description" id="txt_attr_name" cssClass="text"
						cssErrorClass="text medium error" />		
				</li>
				<li>
					<label for="txt_attr_name">
						<fmt:message key="productGroup.edit.status"/>:
					</label>
					<form:select path="statusCd" id="txt_attr_name" cssClass="select"
						cssErrorClass="text medium error">
						<form:option value="ACTIVE">
							<fmt:message key="flag.active"/>
						</form:option>
						<form:option value="INACTIVE">
							<fmt:message key="flag.inactive"/>
						</form:option>
					</form:select>						
				</li>				
			</ul>
				<br/>								
			<div class="buttons">
				
				<input type="button" class="btn save_btn" name="method" value="<fmt:message key="button.cancel"/>"  onclick="verifyCancel();"/>
				<input type="submit" class="btn save_btn" name="method" value="<fmt:message key="button.save"/>" />
			</div>
		</fieldset>	
	    <input type="hidden" name="id" value="${productTypeGroupForm.id}"/>
	    <input type="hidden" name="action" value="addProductGroup"/>	   	    
	</form:form>
</div>
</div></div>
</body>
<content tag="inlineStyle">
div.buttons{
	padding-left:160px;
}
</content>
<content tag="jscript">
<![CDATA[
]]>
</content>