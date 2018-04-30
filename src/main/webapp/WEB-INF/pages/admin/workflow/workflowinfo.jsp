<%@ include file="/common/taglibs.jsp"%>


<div class="userButtons">
	<a class="btn" href="<c:url value="workflows.html"/>" title="Back to Wofklow List"><fmt:message key="button.back"/></a>	
</div>
<ul>
<li>
<label for="txt_attr_name">Name: </label> <c:out value="${workflow.name}"/>
</li>
<li>
<label for="txt_description">Description:</label> <c:out value="${workflow.description}"/>
</li>
</ul>