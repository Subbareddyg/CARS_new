<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="manualcar.edit.title"/></title>
</head>
<body class="admin">
	<br/><br/>
	<h1>Export Manual Car data to file</h1>
	<br/>
	<div>
		<c:choose>
			<c:when test="${flag}">
				Created file and inserting Manual Car data to file: <c:out value="${path}" />
			</c:when>
			<c:otherwise>
				File already exists, appending Manual Car data to file: <c:out value="${path}" />
			</c:otherwise>
		</c:choose>
	</div>
</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript" src="<c:url value="/js/belk.cars.editmanualcar.js"/>"></script>
]]>
</content>