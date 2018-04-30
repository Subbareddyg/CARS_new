<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="productGroup.profile.title.details" /></title>
<meta name="heading" content="<fmt:message key='productGroup.heading'/>" />
<meta name="menu" content="UserMenu" />
</head>

<body class="admin">
<div class="cars_panel x-hidden">
<div class="x-panel-header"><fmt:message key='productGroup.heading'/></div>
<div class="x-panel-body">
<c:import url="productGroupInfo.jsp"/>
</div></div>
<div id="productType_class_pnl" class="cars_panel x-hidden">
<div class="x-panel-header">Add Product Type to Group</div>
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

	<c:url value='associateGroupProductType.html?method=next' var="formAction"/>	
		<form:form commandName="productTypeGroupForm" method="post" action="${formAction}" id="productTypeGroupForm" name="productTypeGroupForm">
	<div class="attributeButtons"><a class="btn"
		href="#"
		onClick="document.productTypeGroupForm.submit();return false;"
		title="Edit Product Group"> <fmt:message key="button.save" /></a> <a
		class="btn"
		href='<c:url value="/admin/productgroup/productGroupForm.html?method=detail&amp;id=${productTypeGroupForm.id}"/>'
		title="Back to Product Type List"> < <fmt:message key="button.back" />  </a>
	</div>

				<input type="hidden" name="id" value="${productTypeGroupForm.id}"/>
				<c:if test="${not empty productTypeGroupForm.productTypesList}">	
					<ul class="depts_for_add">
				    	<c:forEach items="${productTypeGroupForm.productTypesList}" var="productType" varStatus="status">
					   		<li>
					   			<form:checkbox path="newProductTypeArr" label="${productType.name}" value="${productType.productTypeId}"/>
				   			</li>
						</c:forEach>
					</ul> <br/>
				</c:if>
		</form:form>
	
	</div>


</div>
</div>
</body>
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){return confirm('<fmt:message key="productGroup.confirm.delete"/>');});
	$("li").remove(":contains('OUTFIT_PRODUCT_TYPE')");
});

</script>
]]>
</content>