<%@ include file="/common/taglibs.jsp"%>
<head>
		<title><fmt:message key="bmattributemapping.title"/></title>
</head>
<body class="admin">
    <div class="x-panel-header">
		<fmt:message key="bmattributemapping.heading"/>
	</div>
	<div class="attrMappingList">
		<display:table name="mappingList" cellspacing="0" cellpadding="0" requestURI=""  
		    defaultsort="1" id="attr" class="table" sort="list" >
		    <display:column property="attributeName" sortable="true" titleKey="bmattributemapping.details.AttributeName" style="width: 30%; word-break:break-all"/>
		    <display:column property="bmAttributeName" sortable="true" titleKey="bmattributemapping.details.BMAttributeName" style="width: 30%;word-break:break-all"/>
		    <display:column property="type" sortable="true" titleKey="bmattributemapping.details.Type" style="width: 25%;word-break:break-all"/>
		    <display:column property="displayName" sortable="true" titleKey="bmattributemapping.details.DisplayName" style="width: 25%"/>
		</display:table>
    </div>
</body>