<%@ include file="/common/taglibs.jsp"%>

<head>
	<title>Associate Attribute With Department
	</title>
	<meta name="heading" content="<fmt:message key='attribute.department.heading'/>" />
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
<div id="attr_dept_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Associate With Department
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

<c:url value='associateDepartmentAttribute.html' var="formAction"/>	
<form:form commandName="attributeForm" method="post" action="${formAction}" id="attributeForm">
	<input type="hidden" name="deptAttrId" value="${attributeForm.attribute.attributeId}"/>
	<input type="hidden" name="action" value="associateWithDepartment"/>
	
	<c:choose>
		<c:when test="${fn:length(departments)== fn:length(attributeDepartments)}">
			All departments have been selected for this Attribute.
		</c:when>	
		<c:otherwise>
			<c:if test="${not empty departments}">
				<div style="padding:0 0 10px 600px;" class="buttons">
					<input class="btn" type="submit" value="Add Department(s)"/>	
				</div>
				<div class="filter" style="padding:5px 0 5px 20px;background:#f0f0f0;">
					<input type="checkbox" id="chk_select_all" />&nbsp;<strong>Filter:</strong> <input type="text" id="txt_dept_filter" />
					<span id="filter_results"></span>
				</div>
				<ul class="depts_for_add">	
					<app:extendedcheckboxes path="departments" items="${departments}" useritems="${attributeDepartments}" itemValue="deptIdAsString" itemLabel="descriptiveName"  element="li" sortBy="deptCd"/>
				</ul>
				<div style="padding:10px 0 0 600px;" class="buttons">
					<input class="btn" type="submit" value="Add Department(s)"/>		
				</div>
			</c:if>
		</c:otherwise>
	</c:choose>	
</form:form>
</div></div>
</body>
	
<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$(document).ready(function(){
	// web 2.0 filter :)
	var $ul=$('ul.depts_for_add');
	$("li").remove(":contains('OUTFIT_DEPARTMENT')");
	var $lis=$('li',$ul);
	$('#filter_results').html($lis.length+' Departments');
	$('#txt_dept_filter').keyup(function(){
		var $this=$(this);
		$lis.hide();
		if($this.val().length>0){
			$('#filter_results').html($('li:contains("'+$this.val().toUpperCase()+'")',$ul).show().length+' Departments');;
		}
		else{
			$lis.show();
			$('#filter_results').html($lis.length+' Departments');
		}
	});
    $('#chk_select_all').click(function(){
    	if($(this).is(':checked')){
    		$('input[type="checkbox"]:not(:checked)').attr('checked','checked');
    	}
    	else{
    		$('input[type="checkbox"]:checked').removeAttr('checked');
    	}
    });
});

$('#attributeForm').submit(function() {
    if ($('input:checkbox', this).is(':checked'))
        {
    } else {
        alert('Please select at least one Department!');
        return false;
    }
});
</script>
]]>
</content>