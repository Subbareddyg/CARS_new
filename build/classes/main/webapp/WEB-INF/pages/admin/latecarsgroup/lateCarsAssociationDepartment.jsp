<%@ include file="/common/taglibs.jsp"%>

<div class="x-panel-header">Departments</div>
<div class="x-panel-body">
	<c:url value="/admin/user/addDepartment.html" var="formAction" />
	<form:form commandName="lateCarsAssociationForm" method="post"
		action="${formAction}" id="lateCarsAssociationForm">
		<input type="hidden" name="id" value="${userDepartmentForm.user.id}" />
		<input type="hidden" name="method" value="addDepartment" />

		<c:if test="${not empty departments}">
			<br style="clear: both;" />
			<div class="filter"
				style="padding: 5px 0 5px 20px; background: #f0f0f0; clear: both; margin-top: 5px;">
				&nbsp;<strong>Filter:</strong> <input type="text"
					id="txt_dept_filter" /> <span id="filter_results"></span>
			</div>
			<ul class="depts_for_add">
				<app:extendedcheckboxes path="departments" items="${departments}"
					useritems="${userDepartments}" itemValue="deptIdAsString"
					itemLabel="descriptiveName" element="li" sortBy="deptCd" />
			</ul>
		</c:if>
	</form:form>
</div>

