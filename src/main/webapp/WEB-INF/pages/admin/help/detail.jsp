<%@ include file="/common/taglibs.jsp"%>
<head>
	<title><fmt:message key="help.title" />
	</title>
	<meta name="heading"
		content="<fmt:message key='help.heading'/>" />
	<meta name="menu" content="UserMenu" />
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Help Content Detail
	</div>
	<div class="x-panel-body">
	<div class="userButtons">
		<a class="btn"
			href="<c:url value="helpForm.html?from=list&id=${content.contentId}" />"
			title="Edit Content"><fmt:message key="button.edit" />
		</a>
		<a class="btn" href="<c:url value="helpList.html"/>"
			title="Back to Help List"><fmt:message key="button.back" />
		</a>
	</div>
	<ul>
		<li>
			<label for="txt_contentKey">
				ContentKey:
			</label>
			<c:out value="${content.contentKey}" />
		</li>
		<li>
			<label for="txt_contentSection">
				Content Section:
			</label>
			<c:out value="${content.contentSection}" />
		</li>
		<li>
			<label for="txt_contentName">
				Content Name:
			</label>
			<c:out value="${content.contentName}" />
		</li>
		<li>
			<label for="txt_contentText">
				Content Text:
			</label>
			<div style="float:left">
				<c:out value="${content.contentText}" />
			</div>
		</li>
	</ul>
</div></div>
</body>
<content tag="inlineStyle">
.cars_panel label{
	float:left;
	font-weight:bold;
	padding-right:15px;
	text-align:right;
	width:150px;
}
.cars_panel li{
	padding:5px 0;
	clear:both;
}
</content>
<content tag="jscript">
<![CDATA[
]]>
</content>