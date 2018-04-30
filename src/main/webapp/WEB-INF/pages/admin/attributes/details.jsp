<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="attributeProfile.title"/></title>
    <meta name="heading" content="<fmt:message key='attributeProfile.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>


<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Attribute Details
	</div>

	<div class="x-panel-body">
<div class="attributeButtons">
		<c:choose>
			<c:when test="${isCopyTextAllowed == 'true' && isBuyer == 'true'&& sessionScope.isDataGovernanceAdmin == 'false'}">
				<a class="disablebtnright" disabled onclick="return false" href="<c:url value="attributeForm.html?from=list&id=${attribute.attributeId}" />" title="Edit Attrbute"><fmt:message key="button.edit"/></a>
			</c:when>
			<c:otherwise>
				<a class="btn" href="<c:url value="attributeForm.html?from=list&id=${attribute.attributeId}" />" title="Edit Attrbute"><fmt:message key="button.edit"/></a>
			</c:otherwise>
    	</c:choose>
	<a class="btn" href="<c:url value='/admin/searchattribute.html?${attributeSearchCriteria.criteriaAsQueryString}'/>" title="Back to Attribute List"><fmt:message key="button.back"/></a>	
</div>
<c:import url="attributeInfo.jsp"/>
</div></div>
</body>