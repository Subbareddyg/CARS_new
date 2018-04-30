<%@ include file="/common/taglibs.jsp"%>

<head>
	<title>Associate Product Type With Class</title>
	<meta name="heading" content="<fmt:message key='productType.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		Product Type
	</div>
	<div class="x-panel-body">
		<div class="attributeButtons">	
			<secureurl:secureAnchor cssStyle="btn" name="ProductDetail" title="Back"  localized="true" hideUnaccessibleLinks="true" arguments="${productTypeForm.productType.productTypeId}"/>
		</div>
		<c:set var="product" value="${productTypeForm.productType}" scope="request"/>
		<c:import url="productInfo.jsp"/>
</div></div>
<div id="productType_class_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Associate with Classification
	</div>
	<div class="x-panel-body">
		<spring:bind path="productTypeForm.*">
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
		<c:url value='/admin/producttypeclassificationsearch.html' var="formAction"/>
		<form action="${formAction}" id="classSearchForm">
		<div class="searchButtons">
			Class Number: <input type="text" id="classificationId" name="classificationId" value="<c:out value="${classId}" />"/>
			Class Name: <input type="text" id="classificationName" name="classificationName" value="<c:out value="${className}" />"/>
			<input type="hidden" name="productTypeId" id="productTypeId" value="${productTypeForm.productType.productTypeId}" />
			<input type="hidden" name="action" value="associateWithClass"/>		
			<input type="submit" class="btn" style="display:inline;float:none;" name="searchClassification" value="Search" />
			
		</div>
		</form>
		<br/>
		<hr>
		<c:url value='/admin/producttype/associateClassProductType.html' var="formAction"/>	
		<form:form commandName="productTypeForm" method="post" action="${formAction}" id="productTypeForm">
				<input type="hidden" name="classProductTypeId" value="${productTypeForm.productType.productTypeId}"/>
				<input type="hidden" name="action" value="associateWithClass"/>	
				<c:if test="${not empty productTypeForm.classificationList}">		
					<ul class="depts_for_add">
				    	<c:forEach items="${productTypeForm.classificationList}" var="classification" varStatus="status">
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

});

$('#productTypeForm').submit(function() {
    if ($('input:checkbox', this).is(':checked'))
        {
    } else {
        alert('Please select at least one Classification!');
        return false;
    }
});

</script>

]]>
</content>