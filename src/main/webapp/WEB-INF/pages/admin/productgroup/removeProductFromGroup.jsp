<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
	<title>Remove Product From Group</title>
</head>
<body class="admin">
		All Product Types must be associated with a Group.<br/> 
		You are requesting to remove <c:out value="${productTypeGroupForm.chProductType.name}"/>
		From the Accessories group, please indicate the new group it will be associated with.<br/><br/>
	<c:url value="/admin/productgroup/productGroupForm.html" var="formAction"/>
	
	<form:form commandName="productTypeGroupForm" method="post" action="${formAction}" id="productTypeGroupForm">
	
		<input type="hidden" name="typeId" value="${productTypeGroupForm.chProductType.name}" />
		<strong>New Product Type Group:</strong>
		<form:select path="action" items="${productTypeGroupForm.productGroupList}" itemLabel="name" itemValue="productGroupId"
			cssClass="text" cssErrorClass="text medium error">
		</form:select>
		<br/><br/>
		<div class="buttons">
			<a class="btn" href='<c:url value="/admin/producttype/productGroups.html?method=detail&amp;id=${productTypeGroupForm.id}"/>'>
				<fmt:message key="button.cancel"/>
			</a>
			<input type="submit" class="btn save_btn" name="method" value="<fmt:message key="button.save"/>" />
		</div>
	</form:form>
</body>
</html>