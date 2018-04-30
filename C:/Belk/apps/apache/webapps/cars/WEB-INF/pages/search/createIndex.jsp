<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="search.heading" /></title>
    <meta name="heading" content="<fmt:message key='search.heading'/>"/>
    <meta name="menu" content="Search"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<div id="search">
	<c:out value="${context.numIndexed}"/> indexes created successfully. 
</div>