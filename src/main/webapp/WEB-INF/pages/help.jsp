<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="help.heading"/></title>
    <meta name="heading" content="<fmt:message key='help.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
    <script type="text/javascript" src="<c:url value='/scripts/selectbox.js'/>"></script>
</head>

<div id="help">
    <dl>
	<c:forEach var="content" items="${helpContent}" varStatus="status">
		<dt><c:out value="${content.contentName}"/></dt>
		<dd><c:out value="${content.contentText}"/></dd>
	</c:forEach>
    </dl>
    
    <br/>
    <app:secureAnchor name="EditHelpContent" arguments="${contentKey},${contentSection}"></app:secureAnchor>
</div>