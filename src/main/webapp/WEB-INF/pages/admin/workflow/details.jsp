<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="transition.profile.title"/></title>
    <meta name="heading" content="<fmt:message key='workflow.heading'/>"/>
    <meta name="menu" content="UserMenu"/>
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR Workflow Details
	</div>
	<div class="x-panel-body">
<c:import url="workflowinfo.jsp"/>
<br/>
<c:choose>
<c:when test="${not empty workflow.supportedStatus}"> 
Allowed Statuses: 
<select>
	<c:forEach items="${workflow.supportedStatus}" var="status">
		<option value="${status.name}" > <c:out value="${status.name}"/> </option>	
	</c:forEach>
</select> <br/>
</c:when>
<c:otherwise>
No statuses were found for this workflow.<br/>
</c:otherwise>
</c:choose>
<br/>

<secureurl:secureAnchor cssStyle="btn" name="AddTransition" title="Add Transition" localized="true" hideUnaccessibleLinks="true" arguments="${workflow.workflowId},addTransition"  /><br/>
<br/>
<c:choose>
	<c:when test="${not empty transitions}">
		Transitions:
		<display:table name="transitions" cellspacing="0" cellpadding="0" requestURI="details.html"  
		    defaultsort="1" id="transition" pagesize="25"   class="table">		    
		    <display:column property="transitionName" sortable="true" title="Name" style="width: 15%"/>
		    <display:column property="currentUserType.name" sortable="true" title="Current User" style="width: 15%"/>
		    <display:column property="currentStatus.name" sortable="true" title="Current Status" style="width: 15%"/>
		    <display:column property="transitionToUserType.name" sortable="true" title="Transition User" style="width: 15%"/>
		    <display:column property="transitionStatus.name" sortable="true" title="Transition Status" style="width: 15%"/>
		    <display:column property="workflowTask.name" sortable="true" title="Task Name" style="width: 15%"/>
		    <display:column>			    	
		    		<secureurl:secureAnchor name="RemoveTransition" cssStyle="remove" elementName="RemoveTransition" title="Remove" localized="true" arguments="${workflow.workflowId},${transition.workflowTransitionId},remove" hideUnaccessibleLinks="true" />
		    </display:column>
		    <display:column  style="width: 5%">
		   	 	<secureurl:secureAnchor name="EditTransition"  arguments="${workflow.workflowId},${transition.workflowTransitionId},editTransition" title="Edit" hideUnaccessibleLinks="true"/>
		    </display:column>
		    <display:setProperty name="paging.banner.item_name" value="transition"/>
		    <display:setProperty name="paging.banner.items_name" value="transitions"/>
		</display:table>	
	</c:when>
	<c:otherwise>
		No transitions were found for this workflow.
	</c:otherwise>
</c:choose>
</div></div>
</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	$('a.remove').click(function(){
		return confirm('<fmt:message key="transition.confirm.delete"/>');
	});
});
</script>
]]>
</content>