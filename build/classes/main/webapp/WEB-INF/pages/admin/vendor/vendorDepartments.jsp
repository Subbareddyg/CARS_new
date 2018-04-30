<%@ include file="/common/taglibs.jsp"%>

<head>
	<title><fmt:message key="userProfile.title" /></title>
	<meta name="heading" content="<fmt:message key='vendorDepartments.heading'/>" />
	<meta name="menu" content="UserMenu" />	
</head>

<body class="admin">
<div class="cars_panel x-hidden">
	<div class="x-panel-header">
		CAR User Details
	</div>
	<div class="x-panel-body">
<spring:bind path="userDepartmentForm.*">
	<c:if test="${not empty status.errorMessages}">
		<div class="error">
			<c:forEach var="error" items="${status.errorMessages}">
				&nbsp;&nbsp;&nbsp;<img src="<c:url value="/images/iconWarning.gif"/>"
					alt="<fmt:message key="icon.warning"/>" class="icon" />
				<c:out value="${error}" escapeXml="false" />
				<br />
			</c:forEach>
		</div>
	</c:if>
</spring:bind>
<c:set var="user" value="${userDepartmentForm.user}" scope="request"/>
<c:import url="vendorInfo.jsp" />
</div></div>
<div id="user_dept_pnl" class="cars_panel x-hidden">
	<div class="x-panel-header">
		Add To Department
	</div>
	<div class="x-panel-body">
		<c:url value="addDepartment.html" var="formAction"/>
		<form:form commandName="userDepartmentForm" method="post" action="${formAction}" id="userDepartmentForm">
			<input type="hidden" name="id" value="${userDepartmentForm.user.id}"/>
			<input type="hidden" name="method" value="addDepartment"/>
			<c:choose>
				<c:when test="${fn:length(departments)== fn:length(userDepartments)}">
						All departments have been selected for this user.
				</c:when>	
				<c:otherwise>
					<c:if test="${not empty departments}">
						<div style="padding:0 0 10px 600px;" class="buttons">
							<input class="btn" type="submit" value="Add Department(s)"/>	
						</div>
						<br style="clear:both;" />
						<div class="filter" style="padding:5px 0 5px 20px;background:#f0f0f0;clear:both;margin-top:5px;">
							<strong>Filter:</strong> <input type="text" id="txt_dept_filter" />
							<span id="filter_results"></span>
						</div>
						<ul class="depts_for_add">
							<app:extendedcheckboxes path="departments" items="${departments}" useritems="${userDepartments}" itemValue="deptIdAsString" itemLabel="descriptiveName" element="li" sortBy="deptCd" />
						</ul>
						<div style="padding:10px 0 0 600px;" class="buttons">
							<input class="btn" type="submit" value="Add Department(s)"/>		
						</div>
					</c:if>
				</c:otherwise>
			</c:choose>
		</form:form>
	</div>
</div>
</body>

<content tag="inlineStyle">
</content>

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
	
	
	
});
</script>
]]>
</content>