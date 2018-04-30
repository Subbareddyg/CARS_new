<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="workflow.title"/></title>
    <meta name="heading" content="<fmt:message key='workflow.heading'/>"/>
    <meta name="menu" content="AdminMenu"/>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR Workflows
	</div>
	<div class="x-panel-body">
<display:table name="workflowList" cellspacing="0" cellpadding="0" requestURI="workflows.html" defaultsort="1" id="workflow" pagesize="25" class="table">
    <display:column property="name" sortable="true" titleKey="workflow.name" style="width: 25%"/>    	
    <display:column property="description" sortable="true" titleKey="workflow.description" style="width: 25%"/>
    <display:column property="workflowType.name" sortable="true" titleKey="workflow.type" style="width: 25%"/>
    <display:column>
    	<secureurl:secureAnchor name="WorkflowDetail" elementName="" onclick="" arguments="${workflow.workflowId}" title="Detail" hideUnaccessibleLinks="true"/>
    </display:column> 
    
    <display:setProperty name="paging.banner.item_name" value="workflow"/>
    <display:setProperty name="paging.banner.items_name" value="workflows"/>
</display:table>
</div></div>
</body>