<%@ include file="/common/taglibs.jsp"%>

<head>
	<title>Associate Attribute With Class
	</title>
	<meta name="heading" content="Associate Attribute With Class" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Attribute Details
	</div>
	<div class="x-panel-body">
<c:set var="attribute" value="${attributeForm.attribute}" scope="request"/>
<div class="attributeButtons">	
	<secureurl:secureAnchor cssStyle="btn" name="AttributeEdit" title="Back"  localized="true" hideUnaccessibleLinks="true" arguments="${attribute.attributeId}"/>
</div>

<c:import url="attributeInfo.jsp"/>
</div></div>
<div id="attr_class_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Associate with Classification
	</div>
	<div class="x-panel-body">

<spring:bind path="attributeForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				&nbsp;&nbsp;&nbsp; <br><img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
		<br/>
		<hr>
	</c:if>
</spring:bind>

<c:url value='/admin/classificationsearch.html' var="formAction"/>
<form action="${formAction}" id="classSearchForm">
<div class="searchButtons">
	Class Number: <input type="text" id="classificationId" name="classificationId" value="<c:out value="${classId}" />"/>
	Class Name: <input type="text" id="classificationName" name="classificationName" value="<c:out value="${className}" />"/>
	<input type="hidden" name="attrId" id="attrId" value="${attributeForm.attribute.attributeId}" />
	<input type="hidden" name="action" value="associateWithClass"/>		
	<input type="submit" class="btn" style="display:inline;float:none;" name="searchClassification" value="Search" />
	
</div>
</form>
<br/>
<hr>
	
<c:url value='associateClassAttribute.html' var="formAction"/>	
<form:form commandName="attributeForm" method="post" action="${formAction}" id="attributeForm">
		<input type="hidden" name="classificationAttrId" value="${attributeForm.attribute.attributeId}"/>
		<input type="hidden" name="action" value="associateWithClass"/>	
		<c:if test="${not empty attributeForm.classificationList}">	
			<ul class="depts_for_add">
		    	<c:forEach items="${attributeForm.classificationList}" var="classification" varStatus="status">
		    		<c:set var="classId" scope="request">
		    			<c:out value="${classification.classId}"/>
		    		</c:set>
			    	<c:set var="fullDescription" scope="request">
						<c:out value="${classification.belkClassNumber}"/> - <c:out value="${classification.name}"/> (Department: <c:out value="${classification.department.deptCd}"/>)
			   		</c:set>		   			<li>
			   		<form:checkbox path="classifications" label="${fullDescription}" value="${classId}"/>
		   			</li>
				</c:forEach>
			</ul> <br/>
			<input id="save" type="submit" value="Add Classification"/>		
		</c:if>
</form:form>
</div></div>
</body>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">

$(document).ready(function(){

	$("li").remove(":contains('OUTFIT_CLASS')");
	
	$('#attributeForm').submit(function() {
	    if ($('input:checkbox', this).is(':checked'))
	        {
	    } else {
	        alert('Please select at least one Classification!');
	        return false;
    }
});

});



</script>

]]>
</content>