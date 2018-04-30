<%@ include file="/common/taglibs.jsp"%>

<script>
function verifyCancel() {
	var stay=confirm("Are you sure you wish to cancel?");
	if (stay)
		window.location="../../admin/vendor/user/vendorDeptForm.html";
	else
		return false;
}
</script>
<head>
    <title><fmt:message key="vendorDetails.heading"/></title>
</head>

<body class="admin">
<div id="assign_vendor_departments" class="cars_panel x-hidden">
	<div class="x-panel-header">
		<fmt:message key="sendcars.add.title"/>
	</div>
	<div class="x-panel-body">

<c:url value="/vendor/vendorDeptForm.html" var="formAction">
	<c:param name="id" value="${user.id}"/>    		
</c:url>
<form:form commandName="carUserVendorDeparmentForm" method="post" action="${formAction}" onsubmit="return onFormSubmit(this)" 
 id="carUserVendorDeparmentForm">
	<table style="border:0px">
		<tr>
			<td style="border:0px">
							<label><fmt:message key="department.deptCd"/>:</label>
							<form:select id="departmentCid" path="departmentCid">
							<c:forEach var="dept" items="${user.departments}">
		                			<option value="${dept.deptCd}">
		                  				<c:out value="${dept.deptCd}" />
		                			</option>
		              			</c:forEach>
						    </form:select>
		            	
		     </td>
		      <td style="border:0px">	
						
							<label>Vendor Number:</label>
						 	<form:select id="vendorNumber" path="vendorNumber">
		              			<c:forEach var="vend" items="${user.vendors}">
		                			<option  value="${vend.vendorNumber}">
		                  				<c:out value="${vend.vendorNumber}" />
		                			</option>
		                		</c:forEach>
		              		 </form:select>
		            		
			</td>
			<td style="border:0px">
				<table style="border:0px">
			    	<tr>
			    		<td style="border:0px">
							<input type="submit" class="btn" name="save" value="<fmt:message key="button.save"/>" />&nbsp;&nbsp;
						</td>
						<td style="border:0px">
							<secureurl:secureAnchor cssStyle="btn" name="VendorDetails" title="Back" localized="true" hideUnaccessibleLinks="true" arguments="${user.id}"/>
				 		</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</form:form>
</div>
</div>
</body>
<content tag="inlineStyle">
div.buttons{
	padding-left:160px;
}
</content>

<content tag="jscript">
<![CDATA[
<script type="text/javascript">
$('#carUserVendorDeparmentForm').submit(function() {
		return confirm('<fmt:message key="user.vendor.department.confirm.add"/>');
});
</script>
]]>
</content>
